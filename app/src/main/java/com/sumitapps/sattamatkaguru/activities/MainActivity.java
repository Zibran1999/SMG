package com.sumitapps.sattamatkaguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.databinding.ActivityMainBinding;
import com.sumitapps.sattamatkaguru.utils.MyReceiver;

public class MainActivity extends AppCompatActivity {
    public static final String BroadCastStringForAction = "checkingInternet";
    FirebaseAnalytics firebaseAnalytics;
    ActivityMainBinding binding;
    int count = 1;

    private IntentFilter intentFilter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, MyReceiver.class);
        startService(serviceIntent);
        binding.tvNotConnected.setVisibility(View.GONE);
        if (isOnline(getApplicationContext())) {
            Set_Visibility_ON();
        } else {
            Set_Visibility_OFF();
        }
    }

    private void Set_Visibility_ON() {
        binding.tvNotConnected.setVisibility(View.GONE);
        binding.lottie.setVisibility(View.VISIBLE);
        if (count == 2) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }, 3000);
        }
    }

    private void Set_Visibility_OFF() {
        binding.lottie.setVisibility(View.GONE);
        binding.tvNotConnected.setVisibility(View.VISIBLE);
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
}