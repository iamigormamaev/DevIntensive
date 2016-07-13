package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mLogin, mPassword;
    Button mSignIn;
    TextView mRememberPassword;
    CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mLogin = (EditText) findViewById(R.id.auth_login_et);
        mPassword = (EditText) findViewById(R.id.auth_password_et);
        mSignIn = (Button) findViewById(R.id.auth_sign_in_btn);
        mRememberPassword = (TextView) findViewById(R.id.auth_remember_password_tw);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.auth_coordinator_layout);
        mDataManager = DataManager.getInstance();

        mRememberPassword.setOnClickListener(this);
        mSignIn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auth_sign_in_btn:
                signIn();
                break;
            case R.id.auth_remember_password_tw:
                rememberPassword();
                break;
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberPassIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberPassIntent);
    }

    private void loginSuccess(UserModelRes userModel) {
        mDataManager.getPreferenceManager().saveUserToken(userModel.getData().getToken());
        mDataManager.getPreferenceManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValue(userModel);
        saveUserFields(userModel);
        saveFirstSecondNameUser(userModel);
        saveUserPhotoAndAvatar(userModel);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);

    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvalible(this)) {


            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Неверный логин или пароль");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: 13.07.2016 обработать ошибки
                }
            });
        } else {
            showSnackbar("Нет подключения к интернету");
        }
    }

    private void saveUserValue(UserModelRes userModel) {
        int[] userValues = {userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()};
        DataManager.getInstance().getPreferenceManager().saveUserValues(userValues);

    }

    private void saveUserFields(UserModelRes userModel) {
        List<String> userFields = new ArrayList<>();
        userFields.add(userModel.getData().getUser().getContacts().getPhone());
        userFields.add(userModel.getData().getUser().getContacts().getEmail());
        userFields.add(userModel.getData().getUser().getContacts().getVk());
        userFields.add(userModel.getData().getUser().getRepositories().getRepo().get(0).getGit());
        userFields.add(userModel.getData().getUser().getPublicInfo().getBio());

        DataManager.getInstance().getPreferenceManager().saveUserFields(userFields);
    }

    private void saveFirstSecondNameUser(UserModelRes userModel) {
        String firstName = userModel.getData().getUser().getFirstName();
        String secondName = userModel.getData().getUser().getSecondName();
        DataManager.getInstance().getPreferenceManager()
                .saveFirstNameUser(firstName);
        DataManager.getInstance().getPreferenceManager()
                .saveSecondNameUser(secondName);

    }

    private void saveUserPhotoAndAvatar(UserModelRes userModel) {
        DataManager.getInstance().getPreferenceManager().saveUserPhoto(userModel.getData().getUser().getPublicInfo().getPhoto());
        DataManager.getInstance().getPreferenceManager().saveAvatarImage(userModel.getData().getUser().getPublicInfo().getAvatar());
    }
}
