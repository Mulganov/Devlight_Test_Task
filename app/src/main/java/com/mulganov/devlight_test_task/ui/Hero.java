package com.mulganov.devlight_test_task.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.mulganov.devlight_test_task.R;
import com.mulganov.devlight_test_task.support.api.API;
import com.mulganov.devlight_test_task.support.api.ListHeros;
import com.mulganov.devlight_test_task.support.list.BoxAdapter;
import com.mulganov.devlight_test_task.support.list.Element;

import java.util.ArrayList;

public class Hero extends Fragment {

    private ListView listView;
    private ListHeros.Date.Results res;
    private boolean comics;
    private boolean series;
    private boolean link;
    private WebView web;
    private int colorToolbar;

    public static Hero newInstance(String name) {
        Hero hero = new Hero();
        hero.getRes(name);
        return hero;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hero, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        toolbar.setTitle(res.name);
        colorToolbar = Color.WHITE;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, Main.newInstance())
                        .commitNow();
            }
        });


        web = getActivity().findViewById(R.id.web);

        ImageView image = getActivity().findViewById(R.id.image);

        final Bitmap bitmap = BitmapFactory.decodeFile(res.thumbnail.file);
        image.setImageBitmap(bitmap);

        image.setMaxHeight(bitmap.getHeight());

        TextView des = getActivity().findViewById(R.id.description);
        des.setText(res.description);

        getActivity().findViewById(R.id.comics_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> a = new ArrayList<>();

                if (!comics){
                    for (ListHeros.Date.Results.Items items :res.comics.items){
                        a.add(items.name);
                    }
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, a);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ListView)getActivity().findViewById(R.id.comics_l)).setAdapter(adapter);
                        setListViewHeight(((ListView)getActivity().findViewById(R.id.comics_l)));
                    }
                });

                comics = !comics;
            }
        });

        getActivity().findViewById(R.id.series_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> a = new ArrayList<>();

                if (!series){
                    for (ListHeros.Date.Results.Items items :res.series.items){
                        a.add(items.name);
                    }
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, a);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ListView)getActivity().findViewById(R.id.series_l)).setAdapter(adapter);
                        setListViewHeight(((ListView)getActivity().findViewById(R.id.series_l)));
                    }
                });

                series = !series;
            }
        });

        getActivity().findViewById(R.id.link_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> a = new ArrayList<>();

                if (!link){
                    for (ListHeros.Date.Results.Urls urls :res.urls){
                        a.add(urls.type);
                    }
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, a);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ListView)getActivity().findViewById(R.id.link_l)).setAdapter(adapter);
                        setListViewHeight(((ListView)getActivity().findViewById(R.id.link_l)));

                        ((ListView)getActivity().findViewById(R.id.link_l)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                for (ListHeros.Date.Results.Urls urls :res.urls){
                                    String text = ((TextView)view).getText().toString();
                                    if (urls.type.equalsIgnoreCase(text)){
                                        System.out.println("url: " + urls.url);
                                       // web.setVisibility(View.VISIBLE);
                                        web.loadUrl(urls.url);
                                    }
                                }
                            }
                        });
                    }
                });

                link = !link;
            }
        });

        final ScrollView scrollView = getActivity().findViewById(R.id.scroll);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    ImageView image = scrollView.findViewById(R.id.image);

                    if ( image.getY() + image.getHeight() - scrollY < 0 ){

                        Bitmap b = Bitmap.createBitmap(1, 1, bitmap.getConfig());

                        b = Bitmap.createScaledBitmap(bitmap, 1, 1, true);

                        toolbar.setBackgroundColor(b.getPixel(0, 0));
                    }else{
                        toolbar.setBackgroundColor(colorToolbar);
                    }
                }
            });
        }
    }

    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void getRes(String name){
        for (ListHeros.Date.Results res: API.listHeros.data.results){
            if (res.name.equalsIgnoreCase(name)){
                this.res = res;
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();

    }

}
