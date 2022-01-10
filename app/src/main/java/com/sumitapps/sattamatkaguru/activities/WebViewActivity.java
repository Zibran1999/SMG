package com.sumitapps.sattamatkaguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.databinding.ActivityWebViewBinding;
import com.sumitapps.sattamatkaguru.utils.CommonMethod;

public class WebViewActivity extends AppCompatActivity {
    ActivityWebViewBinding binding;
    WebView webView;
    String catTitle,catUrl;
    Dialog loadingDialog;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        webView = binding.webView;
        binding.backIcon.setOnClickListener(v -> onBackPressed());

        catTitle = getIntent().getStringExtra("title");
        catUrl = getIntent().getStringExtra("url");
        binding.webViewTitle.setText(catTitle);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().getLoadsImagesAutomatically();

        loadingDialog = CommonMethod.getLoadingDialog(WebViewActivity.this);
        loadingDialog.show();

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("TAG", "Error: " + description);
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();

                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loadingDialog.dismiss();

                super.onPageFinished(view, url);
            }
        });

        webView.loadUrl(catUrl);
      //  webView.setVisibility(View.VISIBLE);


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}