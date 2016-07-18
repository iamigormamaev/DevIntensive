package com.softdesign.devintensive.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileUserActivity extends BaseActivity {

    private Toolbar mToolBar;
    private ImageView mProfileImage;
    private EditText mUserBio;
    private TextView mUserRating, mUserCodeLines, mUserProjects;
    private CollapsingToolbarLayout mCollapsingToolBarLayout;
    private CoordinatorLayout mCoordinatorLayout;

    private ListView mRepoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mUserBio = (EditText) findViewById(R.id.account_about_me_ev);
        mUserRating = (TextView) findViewById(R.id.score_box_rating_tv);
        mUserCodeLines = (TextView) findViewById(R.id.score_box_lines_code_tv);
        mUserProjects = (TextView) findViewById(R.id.score_box_projects_tv);
        mCollapsingToolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_layout);

        mRepoListView = (ListView) findViewById(R.id.repository_list);
        setupToolBar();
        initProfileData();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolBar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initProfileData() {
        UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        final List<String> repositories = userDTO.getRepositories();
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);

        mRepoListView.setAdapter(repositoriesAdapter);

        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 15.07.2016 Реализовать просмотр репозитория по Intent.ACTION_VIEW
            }
        });

        mUserBio.setText(userDTO.getBio());
        mUserRating.setText(userDTO.getRating());
        mUserCodeLines.setText(userDTO.getCodeLines());
        mUserProjects.setText(userDTO.getProjects());

        mCollapsingToolBarLayout.setTitle(userDTO.getFullName());

        Picasso.with(this)
                .load(userDTO.getPhoto())
                .placeholder(R.drawable.user_bg)
                .error(R.drawable.user_bg)
                .into(mProfileImage);
    }
}
