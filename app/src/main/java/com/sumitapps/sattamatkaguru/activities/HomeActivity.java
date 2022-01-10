package com.sumitapps.sattamatkaguru.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.adapters.CatItemAdapter;
import com.sumitapps.sattamatkaguru.databinding.ActivityHomeBinding;
import com.sumitapps.sattamatkaguru.models.CatItemModel;
import com.sumitapps.sattamatkaguru.models.PageViewModel;
import com.sumitapps.sattamatkaguru.utils.CommonMethod;
import com.sumitapps.sattamatkaguru.utils.MyReceiver;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CatItemAdapter.CatItemInterface {

    public static final String BroadCastStringForAction = "checkingInternet";
    private static final String ONESIGNAL_APP_ID = "da5be7cf-cc61-4d56-a950-83b5f31e2577";
    private static final float END_SCALE = 0.7f;
    int REQUEST_CODE = 11;
    int count = 1;
    ActivityHomeBinding binding;
    TextView versionCode;
    ImageView navMenu;
    Dialog loadingDialog;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CatItemAdapter catItemAdapter;
    ConstraintLayout categoryContainer;
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadCastStringForAction)) {
                if (intent.getStringExtra("online_status").equals("true")) {

                    Set_Visibility_ON();
                    count++;
                } else {
                    Set_Visibility_OFF();
                }
            }
        }
    };
    PageViewModel pageViewModel;
    List<CatItemModel> catItemModels = new ArrayList<>();
    private IntentFilter intentFilter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        versionCode = binding.versionCode;
        navigationView = binding.navigation;
        navMenu = binding.navMenu;
        drawerLayout = binding.drawerLayout;
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.catItemRecyclerView.setLayoutManager(layoutManager);
        loadingDialog = CommonMethod.getLoadingDialog(HomeActivity.this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, MyReceiver.class);
        startService(serviceIntent);
        binding.noInternet.setVisibility(View.GONE);
        if (isOnline(getApplicationContext())) {
            Set_Visibility_ON();
        } else {
            Set_Visibility_OFF();
        }

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            versionCode.setText("Version : " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        binding.result.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ResultActivity.class)));
        binding.news.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NewsActivity.class)));

        binding.guessing.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,WebViewActivity.class);
            intent.putExtra("title","Guessing");
            intent.putExtra("url","www.google.com");
            startActivity(intent);
        });

        binding.chart.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PanelChartActivity.class)));
        inAppUpdate();
        oneSignal();
        fetchCatItems();
    }

    private void fetchCatItems() {
        loadingDialog.show();
        pageViewModel.getCatItemList().observe(this, catItemModelList -> {
            if (catItemModelList != null) {
                catItemModels.clear();
                catItemModels.addAll(catItemModelList.getData());
                Log.d("CatData",catItemModelList.getData().toString());
                catItemAdapter = new CatItemAdapter(this,this);
                binding.catItemRecyclerView.setAdapter(catItemAdapter);
                catItemAdapter.updateCatItemList(catItemModels);
                loadingDialog.dismiss();
            }
        });
    }

    private void Set_Visibility_ON() {
        binding.noInternet.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.VISIBLE);
        enableNavItems();
        if (count == 2) {
            navigationDrawer();
        }
    }

    private void Set_Visibility_OFF() {
        binding.noInternet.setVisibility(View.VISIBLE);
        binding.scrollView.setVisibility(View.GONE);
        disableNavItems();
    }

    private void inAppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(HomeActivity.this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, HomeActivity.this, REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Toast.makeText(HomeActivity.this, "Downloading Started", Toast.LENGTH_SHORT).show();
            if (resultCode != RESULT_OK) {
                Log.d("DDDDD", "Downloading Failed" + resultCode);
            }
        } else {
            Toast.makeText(HomeActivity.this, "Downloading Failed", Toast.LENGTH_SHORT).show();
        }
    }


    private void navigationDrawer() {
        navigationView = findViewById(R.id.navigation);
        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        navigationView.setCheckedItem(R.id.nav_home);
        categoryContainer = findViewById(R.id.container_lay);

        navigationView.setCheckedItem(R.id.nav_home);
        navMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(Color.parseColor("#DEE4EA"));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                categoryContainer.setScaleX(offsetScale);
                categoryContainer.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = categoryContainer.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                categoryContainer.setTranslationX(xTranslation);
            }
        });
    }

    public void oneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(new ExampleNotificationOpenedHandler());
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_contact:
                try {
                    CommonMethod.whatsApp(getApplicationContext());
                } catch (UnsupportedEncodingException | PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_rate:
                CommonMethod.rateApp(getApplicationContext());
                break;
            case R.id.nav_privacy:
                startActivity(new Intent(HomeActivity.this, PrivacyPolicy.class));
                break;
            case R.id.nav_share:
                CommonMethod.shareApp(this);
                break;
            default:
        }
        return true;
    }

    public void enableNavItems() {
        Menu navMenu = navigationView.getMenu();
        MenuItem nav_insta = navMenu.findItem(R.id.nav_share);
        nav_insta.setEnabled(true);

        MenuItem nav_policy = navMenu.findItem(R.id.nav_privacy);
        nav_policy.setEnabled(true);

        MenuItem nav_home = navMenu.findItem(R.id.nav_home);
        nav_home.setEnabled(true);

        MenuItem nav_rate = navMenu.findItem(R.id.nav_rate);
        nav_rate.setEnabled(true);

        MenuItem nav_contact = navMenu.findItem(R.id.nav_contact);
        nav_contact.setEnabled(true);
    }

    public void disableNavItems() {
        Menu navMenu = navigationView.getMenu();
        MenuItem nav_insta = navMenu.findItem(R.id.nav_share);
        nav_insta.setEnabled(false);

        MenuItem nav_policy = navMenu.findItem(R.id.nav_privacy);
        nav_policy.setEnabled(false);

        MenuItem nav_home = navMenu.findItem(R.id.nav_home);
        nav_home.setEnabled(false);

        MenuItem nav_rate = navMenu.findItem(R.id.nav_rate);
        nav_rate.setEnabled(false);

        MenuItem nav_contact = navMenu.findItem(R.id.nav_contact);
        nav_contact.setEnabled(false);
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onItemClicked(CatItemModel catItemModel) {
        Intent intent = new Intent(HomeActivity.this,WebViewActivity.class);
        intent.putExtra("title",catItemModel.getCatName());
        intent.putExtra("url",catItemModel.getCatUrl());
        startActivity(intent);
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {

        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}