package com.naufalrzld.belajarretrofit.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.naufalrzld.belajarretrofit.R;
import com.naufalrzld.belajarretrofit.fragment.TokoFragment;
import com.naufalrzld.belajarretrofit.model.member.Member;
import com.naufalrzld.belajarretrofit.utils.SessionManager;
import com.naufalrzld.belajarretrofit.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private View navHeader;

    private SessionManager seesion;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    public static int navItemIndex = 0;

    private static final String TAG_TOKO = "toko";
    private static final String TAG_BARANG = "barang";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_TOKO;

    private String[] fragmentTitle;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        seesion = new SessionManager(this);
        sharedPreferencesUtils = new SharedPreferencesUtils(this, "DataMember");

        fragmentTitle = getResources().getStringArray(R.array.nav_item_fragment_titles);

        navHeaderView();

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_TOKO;
            loadFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navHeaderView();
    }

    private void navHeaderView() {
        navHeader = navigationView.getHeaderView(0);
        HeaderViewHolder headerViewHolder = new HeaderViewHolder(navHeader);

        if (sharedPreferencesUtils.checkIfDataExists("profile")) {
            Member member = sharedPreferencesUtils.getObjectData("profile", Member.class);
            String nama = member.getFirstName() + " " + member.getLastName();
            String email = member.getEmail();
            headerViewHolder.tvNama.setText(nama);
            headerViewHolder.tvEmail.setText(email);

            String letter = "A";

            if(nama != null && !nama.isEmpty()) {
                letter = nama.substring(0, 1);
            }

            int color = mColorGenerator.getRandomColor();
            mDrawableBuilder = TextDrawable.builder().buildRound(letter, color);
            headerViewHolder.profileImage.setImageDrawable(mDrawableBuilder);
        }
    }

    private void loadFragment() {
        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawerLayout.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getFragment() {
        switch (navItemIndex) {
            case 0:
                TokoFragment homeFragment = new TokoFragment();
                return homeFragment;
            default:
                return new TokoFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(fragmentTitle[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_store:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_TOKO;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_BARANG;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_about_us:
                        return true;
                    case R.id.nav_logout:
                        logout();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    private void logout() {
        seesion.setLogin(false);
        sharedPreferencesUtils.clearAllData();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    protected static class HeaderViewHolder {
        @BindView(R.id.profile_image)
        ImageView profileImage;
        @BindView(R.id.tvNama)
        TextView tvNama;
        @BindView(R.id.tvEmail)
        TextView tvEmail;

        HeaderViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
