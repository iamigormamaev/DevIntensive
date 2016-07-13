package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";


    private int mCurrentEditMode = 0;

    private ImageView mCallImg;
    private ImageView mVkOpenImg;
    private ImageView mGitOpenImg;
    private ImageView mEmailSentImg;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDraver;
    private FloatingActionButton mFloatingActionButton;
    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserBio;
    private List<EditText> mUserInfoViews;
    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout mAppBarLayout;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private DataManager mDataManager;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;
    private ImageView mProfileImage;
    private TextView mUserValueRating, mUserValueCodeLines, mUserValueProjects;
    private List<TextView> mUserValues;
    private ImageView mHeaderAvatar;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);

                }
                break;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallImg = (ImageView) findViewById(R.id.account_mobile_phone_call_img);
        mCallImg.setOnClickListener(this);
        mVkOpenImg = (ImageView) findViewById(R.id.account_vk_open_img);
        mVkOpenImg.setOnClickListener(this);
        mGitOpenImg = (ImageView) findViewById(R.id.account_repository_open_img);
        mGitOpenImg.setOnClickListener(this);
        mEmailSentImg = (ImageView) findViewById(R.id.account_email_sent_img);
        mEmailSentImg.setOnClickListener(this);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDraver = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        mFloatingActionButton.setOnClickListener(this);
        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mProfilePlaceholder.setOnClickListener(this);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mUserPhone = (EditText) findViewById(R.id.account_mobile_phone_ev);
        mUserMail = (EditText) findViewById(R.id.account_email_ev);
        mUserVk = (EditText) findViewById(R.id.account_vk_ev);
        mUserGit = (EditText) findViewById(R.id.account_repository_ev);
        mUserBio = (EditText) findViewById(R.id.account_about_me_ev);
        mDataManager = DataManager.getInstance();
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mUserValueRating = (TextView) findViewById(R.id.score_box_rating_tv);
        mUserValueCodeLines = (TextView) findViewById(R.id.score_box_lines_code_tv);
        mUserValueProjects = (TextView) findViewById(R.id.score_box_projects_tv);


        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        mUserValues = new ArrayList<>();
        mUserValues.add(mUserValueRating);
        mUserValues.add(mUserValueCodeLines);
        mUserValues.add(mUserValueProjects);

        Log.d(TAG, "onCreate");
        setupToolbar();
        setupDrawer();


        initUserFields();
        initUserInfoValues();
        Picasso.with(this)
                .load(DataManager.getInstance().getPreferenceManager().loadUserPhoto())
                .placeholder(R.drawable.user_bg)
                .into(mProfileImage);

        Picasso.with(this)
                .load(DataManager.getInstance().getPreferenceManager().getUserPhoto())
                .into(mProfileImage);


        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDraver.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (mNavigationDraver.isDrawerOpen(GravityCompat.START)) {
            mNavigationDraver.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_mobile_phone_call_img:
                makeDial();

                break;
            case R.id.account_email_sent_img:
                Intent sendEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mUserMail.getText().toString(), null));
                startActivity(sendEmail);
                break;
            case R.id.account_repository_open_img:
                Intent browseGitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + mUserGit.getText()));
                startActivity(browseGitIntent);
                break;
            case R.id.account_vk_open_img:
                Intent browseVkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + mUserVk.getText()));
                startActivity(browseVkIntent);
                break;

            case R.id.floating_action_button:
                if (mCurrentEditMode == 0) {
                    mCurrentEditMode = 1;
                    changeEditMode(mCurrentEditMode);
                } else {
                    mCurrentEditMode = 0;
                    changeEditMode(mCurrentEditMode);
                }
                break;
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDraver.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        mHeaderAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.header_avatar_iv);
        Picasso.with(this)
                .load(DataManager.getInstance().getPreferenceManager().getAvatarImage())
                .into(mHeaderAvatar);
//        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.header_user_photo));
//        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.
//        roundedBitmapDrawable.setCircular(true);
//        mHeaderAvatar.setImageDrawable(roundedBitmapDrawable);

        TextView headerFirstSecondName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_user_name_txt);
        headerFirstSecondName.setText(DataManager.getInstance().getPreferenceManager().getFirstNameUser() + " "
                + DataManager.getInstance().getPreferenceManager().getSecondNameUser());

        TextView headerEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_user_email_txt);
        headerEmail.setText(DataManager.getInstance().getPreferenceManager().loadUserFields().get(1));


    }

    private void changeEditMode(int mode) {
        if (mode == 1) {
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
                mFloatingActionButton.setImageResource(R.drawable.ic_done_black_24dp);
                showProfilePlaceholder();
                lockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            }
        } else {
            for (EditText userValue : mUserInfoViews) {
                saveUserFields();
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                mFloatingActionButton.setImageResource(R.drawable.ic_create_black_24dp);
                hideProfilePlaseholder();
                unlockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(Color.WHITE);

            }
        }

    }

    private void initUserFields() {
        List<String> userData = mDataManager.getPreferenceManager().loadUserFields();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }


    private void saveUserFields() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView :
                mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferenceManager().saveUserFields(userData);
    }

    private void loadPhotoFromGallery() {
        Intent takeGaleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGaleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGaleryIntent, getString(R.string.user_profile_choose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);

    }

    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();

            }

            if (mPhotoFile != null) {

                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);
            Snackbar.make(mCoordinatorLayout, R.string.need_to_permission, Snackbar.LENGTH_LONG)
                    .setAction(getText(R.string.give_permission), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }

                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                loadPhotoFromCamera();

            }

        } else if (requestCode == ConstantManager.DIAL_REQUEST_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeDial();
            }
        }
    }

    private void hideProfilePlaseholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selecItems = {getString(R.string.user_profile_dialog_galery), getString(R.string.user_profile_dialog_make_photo), getString(R.string.cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.user_profile_dialog_title);
                builder.setItems(selecItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:

                                loadPhotoFromGallery();
                                break;
                            case 1:

                                loadPhotoFromCamera();
                                break;
                            case 2:

                                dialogInterface.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storarageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storarageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        mDataManager.getInstance().getPreferenceManager().SaveUserPhoto(selectedImage);
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);

    }

    public void makeDial() {
        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mUserPhone.getText()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(dialIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CALL_PHONE
            }, ConstantManager.DIAL_REQUEST_PERMISSION_CODE);
            Snackbar.make(mCoordinatorLayout, getText(R.string.need_to_permission), Snackbar.LENGTH_LONG)
                    .setAction(R.string.give_permission, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }

                    }).show();
        }
    }

    public void initUserInfoValues() {
        List<String> userData = DataManager.getInstance().getPreferenceManager().loadUserValues();
        for (int i = 0; i < userData.size(); i++) {
            mUserValues.get(i).setText(userData.get(i));
        }
    }


}
