package com.mulganov.devlight_test_task.support.api;

import com.google.gson.Gson;
import com.mulganov.devlight_test_task.support.helpers.JSONHelper;
import com.mulganov.devlight_test_task.ui.Loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class API {

    public static File file;
    public static ListHeros listHeros;

    public interface ApiInterface {

        // option 1: a resource relative to your base URL
        @GET("resource/example.zip")
        Call<ResponseBody> downloadFileWithFixedUrl();

        // option 2: using a dynamic URL
        @GET
        Call<ResponseBody> downloadFileWithDynamicUrl(@Url String fileUrl);
    }

    public static class ServiceGenerator {

        public final static String API_BASE_URL = "https://gateway.marvel.com/";

        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        public static <S> S createService(Class<S> serviceClass){
            Retrofit retrofit = builder.client(httpClient.build()).build();
            return retrofit.create(serviceClass);
        }

    }

    public static void downloadFileAndConvertToJSON(String url, final Loader loader){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.downloadFileWithDynamicUrl(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    boolean writtenToDisk = false;

                    writtenToDisk = writeResponseBodyToDisk(response.body());

                    System.out.println("Download: " + writtenToDisk);

                    listHeros = JSONHelper.importFromJSON(file.getAbsolutePath());

                    System.out.println("heros: " + listHeros.toString());

                    loader.downloadEvent();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private static boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static void getListHerosOfJSON(File file){
        listHeros = JSONHelper.importFromJSON(file.getAbsolutePath());
    }
}
