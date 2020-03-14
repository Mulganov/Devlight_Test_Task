package com.mulganov.devlight_test_task.ui;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mulganov.devlight_test_task.MainActivity;
import com.mulganov.devlight_test_task.R;
import com.mulganov.devlight_test_task.support.api.API;
import com.mulganov.devlight_test_task.support.api.ListHeros;
import com.mulganov.devlight_test_task.support.helpers.InternetHelper;
import com.mulganov.devlight_test_task.support.helpers.JSONHelper;
import com.mulganov.devlight_test_task.support.list.Element;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class Loader extends Fragment {

    public static Loader newInstance() {
        return new Loader();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loader, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String timeStamp = Calendar.getInstance().getTime().toString();

        final String url = Uri.parse(MainActivity.QUERY_URL)
                .buildUpon()
                .appendQueryParameter("apikey", MainActivity.PUBLIC_KEY)
                .appendQueryParameter("hash", getMD5Hash(timeStamp))
                .appendQueryParameter("ts", timeStamp)
                .appendQueryParameter("limit", MainActivity.LIMIT + "")

                .build().toString();

        System.out.println( url );

        if (isReallyOnline()){
            API.downloadFileAndConvertToJSON(url , this);
        }else{
            final File file = new File(getActivity().getFilesDir() + "/listHeros.json");

            final TextView status = getActivity().findViewById(R.id.status);

            API.getListHerosOfJSON(file);

            status.post(new Runnable() {
                @Override
                public void run() {
                    status.setText("Похоже нету интернета.\nПодгрузка ранее загруженных ресурсов\nЧисло героев: " + API.listHeros.data.results.size() + "\nЗадержка перед показом главной программы: " + 5 + " секунд");
                }
            });


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goToMainFragment();
                        }
                    });
                }
            }).start();
        }


    }

    private boolean isReallyOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public void goToMainFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, Main.newInstance())
                .commitNow();
    }

    public void downloadEvent(){

        final TextView status = getActivity().findViewById(R.id.status);

        status.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (final ListHeros.Date.Results res: API.listHeros.data.results){
                    ListHeros.Date.Results.Thumbnail th = res.thumbnail;

                    String url = th.path + "/standard_small." + th.extension;

                    th.file = getActivity().getFilesDir() + url;

                    new File(getActivity().getFilesDir() + th.path).mkdirs();

                    final int n = i;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            status.setText("Download("+n+"/"+API.listHeros.data.results.size()+"): \n" + res.name);
                        }
                    });

                    InternetHelper.getFile(url, th.file);
                    i++;

                }

                System.out.println(API.listHeros.toString());

                status.post(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("The End");

                        goToMainFragment();
                    }
                });


                File file = new File(getActivity().getFilesDir() + "/listHeros.json");
                JSONHelper.exportFromJSON(file);
            }
        }).start();
    }

    public static String getMD5Hash(String timeStamp) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update((timeStamp + MainActivity.PRIVATE_KEY + MainActivity.PUBLIC_KEY).getBytes());
            byte[] byteData = messageDigest.digest();
            for (byte single : byteData) {
                sb.append(Integer.toString((single & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("Characters", "MD5 hash didn't work");
        }
        return sb.toString();
    }

    @Override
    public void onStart(){
        super.onStart();

    }
}
