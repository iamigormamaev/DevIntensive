package com.softdesign.devintensive.utils;

/**
 * Created by Игорь on 23.06.2016.
 */
public interface ConstantManager {
    String TAG_PREFIX = "DEV ";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";
    String USER_PHONE_KEY = "USER_PHONE_KEY";
    String USER_MAIL_KEY = "USER_MAIL_KEY";
    String USER_VK_KEY = "USER_VK_KEY";
    String USER_GIT_KEY = "USER_GIT_KEY";
    String USER_BIO_KEY = "USER_BIO_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";


    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE = 2;
    int REQUEST_GALLERY_PICTURE = 3;
    int PERMISSION_REQUEST_SETTINGS_CODE = 4;
    int CAMERA_REQUEST_PERMISSION_CODE = 5;
    int DIAL_REQUEST_PERMISSION_CODE = 6;
}