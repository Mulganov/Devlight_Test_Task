package com.mulganov.devlight_test_task.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.mulganov.devlight_test_task.R;
import com.mulganov.devlight_test_task.support.api.API;
import com.mulganov.devlight_test_task.support.api.ListHeros;
import com.mulganov.devlight_test_task.support.list.BoxAdapter;
import com.mulganov.devlight_test_task.support.list.Element;

import java.util.ArrayList;

public class Main extends Fragment {

    private ListView listView;

    public static Main newInstance() {
        return new Main();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = getActivity().findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, Hero.newInstance(((TextView)view.findViewById(R.id.ivName)).getText().toString()))
                        .commitNow();
            }
        });

        ArrayList<Element> list = new ArrayList<>();

        for (ListHeros.Date.Results res: API.listHeros.data.results){
            Bitmap bitmap = BitmapFactory.decodeFile(res.thumbnail.file);
            String name = res.name;
            int comics = res.comics.available;
            int series = res.series.available;

            Element el = new Element(bitmap, name, comics, series);

            list.add(el);
        }

        final BoxAdapter adaptor = new BoxAdapter(getContext(), list);

        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(adaptor);
            }
        });

        final SearchView searchView = getActivity().findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("0");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = searchView.getQuery().toString();

                ArrayList<Element> list = new ArrayList<>();

                for (ListHeros.Date.Results res: API.listHeros.data.results){

                    String name = res.name;

                    if (isEqualsText(name.toLowerCase(), text.toLowerCase())){
                        Bitmap bitmap = BitmapFactory.decodeFile(res.thumbnail.file);
                        int comics = res.comics.available;
                        int series = res.series.available;

                        Element el = new Element(bitmap, name, comics, series);

                        list.add(el);
                    }

                }


                if (list.size() == 0){
                    list.add(new Element(null, null, 0, 0));
                }

                final BoxAdapter adaptor = new BoxAdapter(getContext(), list);

                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adaptor);
                    }
                });
                return false;
            }
        });
    }

    private boolean isEqualsText(String str1, String str2){
        if (str1.contains(str2)){
            for (int i = 0; i < str2.length(); i++){
                char c1 = str1.charAt(i);
                char c2 = str2.charAt(i);

                if (c1 == c2){

                }else
                    return false;
            }
        }else
            return false;

        return true;
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    public void onClick(View view) {

    }
}
