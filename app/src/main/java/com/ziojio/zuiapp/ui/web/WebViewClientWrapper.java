package com.ziojio.zuiapp.ui.web;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

public class WebViewClientWrapper extends WebViewClient {
    private WebViewClient delegate;

    public WebViewClientWrapper(WebViewClient webViewClient) {
        this.delegate = webViewClient;
    }

    public WebViewClient getBaseWebViewClient() {
        return delegate;
    }

    public void setBaseWebViewClient(WebViewClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (delegate != null)
            return delegate.shouldOverrideUrlLoading(view, url);
        else return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (delegate != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return delegate.shouldOverrideUrlLoading(view, request);
        else return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (delegate != null)
            delegate.onPageStarted(view, url, favicon);
        else super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (delegate != null)
            delegate.onPageFinished(view, url);
        else super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        if (delegate != null)
            delegate.onLoadResource(view, url);
        else super.onLoadResource(view, url);
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        if (delegate != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            delegate.onPageCommitVisible(view, url);
        else super.onPageCommitVisible(view, url);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (delegate != null)
            return delegate.shouldInterceptRequest(view, url);
        else return super.shouldInterceptRequest(view, url);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (delegate != null)
            return delegate.shouldInterceptRequest(view, request);
        else return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        if (delegate != null)
            delegate.onTooManyRedirects(view, cancelMsg, continueMsg);
        else super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (delegate != null)
            delegate.onReceivedError(view, errorCode, description, failingUrl);
        else super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (delegate != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            delegate.onReceivedError(view, request, error);
        else super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        if (delegate != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            delegate.onReceivedHttpError(view, request, errorResponse);
        else super.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        if (delegate != null)
            delegate.onFormResubmission(view, dontResend, resend);
        else super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        if (delegate != null)
            delegate.doUpdateVisitedHistory(view, url, isReload);
        else super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (delegate != null)
            delegate.onReceivedSslError(view, handler, error);
        else super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        if (delegate != null)
            delegate.onReceivedClientCertRequest(view, request);
        else super.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        if (delegate != null)
            delegate.onReceivedHttpAuthRequest(view, handler, host, realm);
        else super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if (delegate != null)
            return delegate.shouldOverrideKeyEvent(view, event);
        else return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        if (delegate != null)
            delegate.onUnhandledKeyEvent(view, event);
        else super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        if (delegate != null)
            delegate.onScaleChanged(view, oldScale, newScale);
        else super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
        if (delegate != null)
            delegate.onReceivedLoginRequest(view, realm, account, args);
        else super.onReceivedLoginRequest(view, realm, account, args);
    }

    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        if (delegate != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return delegate.onRenderProcessGone(view, detail);
        }
        return super.onRenderProcessGone(view, detail);
    }

    @Override
    public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
        if (delegate != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            delegate.onSafeBrowsingHit(view, request, threatType, callback);
        } else super.onSafeBrowsingHit(view, request, threatType, callback);
    }
}