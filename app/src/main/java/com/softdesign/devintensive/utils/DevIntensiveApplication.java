package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.facebook.stetho.Stetho;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;

import org.greenrobot.greendao.database.Database;

public class DevIntensiveApplication extends Application {
    private static Context sContext;
    public static SharedPreferences sSharedPreferences;
    private static DaoSession sDaoSession;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = getApplicationContext();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devintensive-db");
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}

