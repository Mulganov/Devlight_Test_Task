package com.mulganov.devlight_test_task.support.helpers;

import com.google.gson.Gson;
import com.mulganov.devlight_test_task.support.api.API;
import com.mulganov.devlight_test_task.support.api.ListHeros;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONHelper {

//    public static JSONElement getElement(String uurl, String file , Context context) {
//        InternetHelper.getFile(uurl, file);
//        return importFromJSON(context, file);
//    }

    public static ListHeros importFromJSON(String file) {
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            ListHeros elements = gson.fromJson(streamReader, ListHeros.class);
            return elements;
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  null;
    }

    public static void exportFromJSON(File dir) {

        Gson gson = new Gson();

        String text = "";
        text = gson.toJson(API.listHeros);

        System.out.println("Text: " + text + "\n");

        try {
            DataOutputStream file = new DataOutputStream(
                    new FileOutputStream(dir));
            file.write(text.getBytes());
            file.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        InputStream initialStream = null;
        try {
            initialStream = new FileInputStream(
                    dir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
