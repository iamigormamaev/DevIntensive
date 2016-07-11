package com.softdesign.devintensive.data.managers;

public class DataManager {
    private static DataManager INSTANCE = null;


    private PreferenceManager mPreferenceManager;

    private DataManager() {
        this.mPreferenceManager = new PreferenceManager();
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }


}
