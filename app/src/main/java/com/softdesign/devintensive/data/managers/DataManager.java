package com.softdesign.devintensive.data.managers;

import android.content.Context;


import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import retrofit2.Call;

public class DataManager {

    private static DataManager INSTANCE = null;
    private RestService mRestService;

    private PreferenceManager mPreferenceManager;

    private DataManager() {
        this.mPreferenceManager = new PreferenceManager();
this.mRestService = ServiceGenerator.createService(RestService.class);
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

    public Call<UserListRes> getUserList() {
        return mRestService.getUserList();
    }


    // region ===================Network====================
    public Call<UserModelRes> loginUser (UserLoginReq userLoginReq) {
        return  mRestService.loginUser(userLoginReq);
    }
    //endregion

   // ===================Database====================

}
