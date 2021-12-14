package com.sumitapps.sattamatkaguru.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.sumitapps.sattamatkaguru.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID = "da5be7cf-cc61-4d56-a950-83b5f31e2577";

    int REQUEST_CODE = 11;
    private ActivityHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding.result.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ResultActivity.class)));
        binding.news.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NewsActivity.class)));
        binding.guessing.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), WebViewActivity.class)));
        binding.chart.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PanelChartActivity.class)));
        inAppUpdate();
        oneSignal();
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

    public void oneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(new ExampleNotificationOpenedHandler());
        OneSignal.setAppId(ONESIGNAL_APP_ID);
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