package com.baviux.homeassistant.launcher.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileUtils {

    public static String getRawFileContents(Context context, int rawResId){
        String line;
        StringBuilder jsStringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(rawResId)))) {
            while ((line = reader.readLine()) != null) {
                jsStringBuilder.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return jsStringBuilder.toString();
    }

}
