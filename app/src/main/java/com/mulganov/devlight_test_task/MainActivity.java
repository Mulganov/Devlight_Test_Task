package com.mulganov.devlight_test_task;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mulganov.devlight_test_task.support.api.API;
import com.mulganov.devlight_test_task.ui.Loader;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    public static final String QUERY_URL = "https://gateway.marvel.com/v1/public/characters";
    public static String PUBLIC_KEY = "ef78313bd93fe176b4c84d2c3f15038b";
    public static String PRIVATE_KEY = "03967f9c7e251c045e13151629710f27958c8dca";
    public static int LIMIT = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getSupportActionBar().hide();

        API.file = new File(getFilesDir() + File.separator + "heros.json");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Loader.newInstance())
                    .commitNow();
        }
    }
}
