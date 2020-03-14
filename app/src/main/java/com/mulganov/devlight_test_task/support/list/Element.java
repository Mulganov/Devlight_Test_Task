package com.mulganov.devlight_test_task.support.list;

import android.graphics.Bitmap;

import com.mulganov.devlight_test_task.support.api.ListHeros;

public class Element {

    Bitmap bitmap;
    String name;
    int comics;
    int series;

    public Element(Bitmap bitmap, String name, int comics, int series) {
        this.bitmap = bitmap;
        this.name = name;
        this.comics = comics;
        this.series = series;
    }

    public String toString(){
        return name;
    }
}