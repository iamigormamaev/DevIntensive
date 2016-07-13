package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferenceManager {

    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_MAIL_KEY, ConstantManager.USER_VK_KEY, ConstantManager.USER_GIT_KEY, ConstantManager.USER_BIO_KEY};
    private static final String[] USER_VALUES = {ConstantManager.USER_RATING_VALUE, ConstantManager.USER_CODES_LINES_VALUE, ConstantManager.USER_PROJECT_VALUE};


    public PreferenceManager() {
        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    public void saveUserFields(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));

        }
        editor.apply();
    }

    public List<String> loadUserFields() {
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY, null));

        return userFields;
    }

    public void SaveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto() {
        return Uri.parse(Uri.decode(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resourse://com.softdesign.devintensive/drawable/user_photo.jpg")));
    }

    public void saveUserToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, null);

    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, null);
    }

    public void saveUserValues(int[] userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));

        }
        editor.apply();
    }

    public List<String> loadUserValues() {
        List<String> userValues = new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_RATING_VALUE, "0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODES_LINES_VALUE, "0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_VALUE, "0"));
        return userValues;
    }


    public void saveFirstNameUser(String firstName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_FIRST_NAME_KEY, firstName);
        editor.apply();
    }

    public void saveSecondNameUser(String secondName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_SECOND_NAME_KEY, secondName);
        editor.apply();
    }

    public String getFirstNameUser() {
        return mSharedPreferences.getString(ConstantManager.USER_FIRST_NAME_KEY, null);
    }

    public String getSecondNameUser() {
        return mSharedPreferences.getString(ConstantManager.USER_SECOND_NAME_KEY, null);
    }


    public void saveUserPhoto(String photo) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_USER_PHOTO_KEY, photo.toString());
        editor.apply();
    }

    public void saveAvatarImage(String avatar) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(ConstantManager.USER_USER_AVATAR_KEY, avatar.toString());
        editor.apply();
    }
    public String getUserPhoto() {
        return mSharedPreferences.getString(ConstantManager.USER_USER_PHOTO_KEY, null);
    }
    public String getAvatarImage() {
        return mSharedPreferences.getString(ConstantManager.USER_USER_AVATAR_KEY, null);
    }


}
