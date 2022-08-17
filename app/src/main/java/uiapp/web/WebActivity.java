package uiapp.web;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;
import androidx.webkit.WebViewCompat;

import uiapp.databinding.ActivityWebviewBinding;
import uiapp.ui.base.BaseActivity;

import timber.log.Timber;

public class WebActivity extends BaseActivity {
    private ActivityWebviewBinding binding;
    private WebView webView;
    private WebViewAssetLoader assetLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewBinding.inflate(getLayoutInflater());
        webView = binding.webview;
        setContentView(binding.getRoot());

        PackageInfo webViewPackageInfo = WebViewCompat.getCurrentWebViewPackage(getApplicationContext());
        Timber.d("WebView: " + webViewPackageInfo.packageName);
        Timber.d("WebView: " + webViewPackageInfo.versionName);

        initWebView(webView);

        String url = getIntent().getStringExtra("url");
        if (url == null) {
            url = getIntent().getDataString();
        }
        if (url != null) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("file:///android_asset/test.html");
        }
        // webView.loadUrl("http://www.baidu.com");
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void onBackPressed() {
        Timber.d("onBackPressed");
        if (webView.canGoBack()) {
            webView.goBack();
        } else
            super.onBackPressed();
    }

    private void initWebView(WebView webview) {
        WebViewUtil.initWebView(webview);
        assetLoader = WebViewUtil.createWebViewAssetLoader(this);
        webview.setWebViewClient(new AppWebViewClient());
        webview.setWebChromeClient(new AppWebChromeClient());
    }

    class AppWebViewClient extends WebViewClientCompat {

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return assetLoader.shouldInterceptRequest(request.getUrl());
        }

        @Override
        public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
            if (WebViewUtil.interceptUrl(request.getUrl().toString())) {
                return true;
            } else return super.shouldOverrideUrlLoading(view, request);
        }
    }

    class AppWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            runOnUiThread(() -> binding.titlebar.setTitle(title));
        }
    }
}
