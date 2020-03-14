package com.mulganov.devlight_test_task.support.helpers;

import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class InternetHelper {

    public static File getFile(String uurl, String file){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            URL url = new URL(uurl);
            bis = new BufferedInputStream(url.openStream());
            bos = new BufferedOutputStream(new FileOutputStream(new File(file)));

            int c;
            while ((c = bis.read()) != -1) {
                bos.write(c);
            }

            System.out.println(uurl + " OK");

            bos.close();
            bis.close();

            return new File(file);

        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println(uurl + " NOT");
        }

        return null;
    }

}
