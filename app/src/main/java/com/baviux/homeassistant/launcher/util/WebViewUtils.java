package com.baviux.homeassistant.launcher.util;

import android.content.Context;
import android.util.Base64;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

public class WebViewUtils {

    public static void injectJavascriptFile(Context context, WebView view, int rawResId) {
        InputStream input;
        try {
            input = context.getResources().openRawResource(rawResId);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void execJavascriptFile(Context context, WebView view, int rawResId){
        execJavascript(context, view,  FileUtils.getRawFileContents(context, rawResId));
    }

    public static void execJavascript(Context context, WebView view, String javascript){
        view.loadUrl("javascript:(function() { " + javascript + " })();");
    }

}
