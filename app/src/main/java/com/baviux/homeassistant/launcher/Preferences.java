package com.baviux.homeassistant.launcher;

import android.content.Context;
import android.preference.PreferenceManager;

public class Preferences {
    private final static String KEY_LOCK_SCREEN = "lock_screen";
    private final static String KEY_BACK_KEY_BEHAVIOR = "back_key_behavior";
    private final static String KEY_HIDE_ADMIN_MENU_ITEMS = "hide_admin_menu_items";
    private final static String KEY_HIDE_TOOLBAR = "hide_toolbar";
    private final static String KEY_URL = "URL";

    private static boolean mLockScreen;
    private static boolean mBackKeyBehavior;
    private static boolean mHideAdminMenuItems;
    private static boolean mHideToolbar;
    private static String mUrl;

    public static boolean getUseLockScreen(){
        return mLockScreen;
    }

    public static boolean getAdjustBackKeyBehavior(){
        return mBackKeyBehavior;
    }

    public static boolean getHideAdminMenuItems(){
        return mHideAdminMenuItems;
    }

    public static boolean getHideToolbar(){
        return mHideToolbar;
    }

    public static String getUrl(){
        return mUrl;
    }

    public static void setUseLockScreen(boolean value){
        mLockScreen = value;
    }

    public static void setAdjustBackKeyBehavior(boolean value){
        mBackKeyBehavior = value;
    }

    public static void setHideAdminMenuItems(boolean value){
        mHideAdminMenuItems = value;
    }

    public static void setHideToolbar(boolean value){
        mHideToolbar = value;
    }

    public static void setUrl(String value){
        mUrl = value;
    }

    public static void load(Context context){
        mLockScreen = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_LOCK_SCREEN, true);
        mBackKeyBehavior = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_BACK_KEY_BEHAVIOR, true);
        mHideAdminMenuItems = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_HIDE_ADMIN_MENU_ITEMS, true);
        mHideToolbar = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_HIDE_TOOLBAR, false);
        mUrl = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_URL, null);
    }

    public static void save(Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(KEY_LOCK_SCREEN, mLockScreen)
                .putBoolean(KEY_BACK_KEY_BEHAVIOR, mBackKeyBehavior)
                .putBoolean(KEY_HIDE_ADMIN_MENU_ITEMS, mHideAdminMenuItems)
                .putBoolean(KEY_HIDE_TOOLBAR, mHideToolbar)
                .putString(KEY_URL, mUrl).apply();
    }
}
