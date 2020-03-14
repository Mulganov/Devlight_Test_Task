package com.mulganov.devlight_test_task.support.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mulganov.devlight_test_task.R;
import com.mulganov.devlight_test_task.support.api.ListHeros;

import java.util.ArrayList;

public class BoxAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Element> objects;

    public BoxAdapter(Context context, ArrayList<Element> elements) {
        ctx = context;
        objects = elements;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Element p = getProduct(position);

        if (p.bitmap != null){
            ((ImageView) view.findViewById(R.id.ivImage)).setImageBitmap(createRoundImage(p.bitmap));

            ((TextView) view.findViewById(R.id.ivName)).setText(p.name);
            ((TextView) view.findViewById(R.id.comics)).setText("comics: " + p.comics);
            ((TextView) view.findViewById(R.id.series)).setText("series: " + p.series);
        }
        else{
            ((ImageView) view.findViewById(R.id.ivImage)).setImageBitmap(null);

            ((TextView) view.findViewById(R.id.ivName)).setText("Empty state");
            ((TextView) view.findViewById(R.id.comics)).setText("");
            ((TextView) view.findViewById(R.id.series)).setText("");
        }


        return view;
    }

    // товар по позиции
    Element getProduct(int position) {
        return ((Element) getItem(position));
    }

    private static Bitmap createRoundImage(Bitmap loadedImage) {
        loadedImage = Bitmap.createScaledBitmap(loadedImage, 100, 100, true);
        Bitmap circleBitmap = Bitmap.createBitmap(loadedImage.getWidth(),
                loadedImage.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(loadedImage,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(loadedImage.getWidth() / 2, loadedImage.getHeight() / 2,
                loadedImage.getWidth() / 2, paint);
        return circleBitmap;
    }
}