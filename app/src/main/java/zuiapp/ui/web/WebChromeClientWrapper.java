package zuiapp.ui.web;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

public class WebChromeClientWrapper extends WebChromeClient {
    private WebChromeClient delegate;

    public WebChromeClientWrapper(WebChromeClient webChromeClient) {
        this.delegate = webChromeClient;
    }

    public WebChromeClient getBaseWebChromeClient() {
        return delegate;
    }

    public void setBaseWebChromeClient(WebChromeClient delegate) {
        this.delegate = delegate;
    }

    public void onProgressChanged(WebView view, int newProgress) {
        if (delegate != null)
            delegate.onProgressChanged(view, newProgress);
        else super.onProgressChanged(view, newProgress);
    }

    public void onReceivedTitle(WebView view, String title) {
        if (delegate != null)
            delegate.onReceivedTitle(view, title);
        else super.onReceivedTitle(view, title);
    }

    public void onReceivedIcon(WebView view, Bitmap icon) {
        if (delegate != null)
            delegate.onReceivedIcon(view, icon);
        else super.onReceivedIcon(view, icon);
    }

    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        if (delegate != null)
            delegate.onReceivedTouchIconUrl(view, url, precomposed);
        else super.onReceivedTouchIconUrl(view, url, precomposed);
    }

    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (delegate != null)
            delegate.onShowCustomView(view, callback);
        else super.onShowCustomView(view, callback);
    }

    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        if (delegate != null)
            delegate.onShowCustomView(view, requestedOrientation, callback);
        else super.onShowCustomView(view, requestedOrientation, callback);
    }

    public void onHideCustomView() {
        if (delegate != null)
            delegate.onHideCustomView();
        else super.onHideCustomView();
    }

    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        if (delegate != null)
            return delegate.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        else return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    public void onRequestFocus(WebView view) {
        if (delegate != null)
            delegate.onRequestFocus(view);
        else super.onRequestFocus(view);
    }

    public void onCloseWindow(WebView window) {
        if (delegate != null)
            delegate.onCloseWindow(window);
        else super.onCloseWindow(window);
    }

    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (delegate != null)
            return delegate.onJsAlert(view, url, message, result);
        else return super.onJsAlert(view, url, message, result);
    }

    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        if (delegate != null)
            return delegate.onJsConfirm(view, url, message, result);
        else return super.onJsConfirm(view, url, message, result);
    }

    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (delegate != null)
            return delegate.onJsPrompt(view, url, message, defaultValue, result);
        else return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        if (delegate != null)
            return delegate.onJsBeforeUnload(view, url, message, result);
        else return super.onJsBeforeUnload(view, url, message, result);
    }

    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize,
                                        long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        if (delegate != null)
            delegate.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        else
            super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
    }

    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        if (delegate != null)
            delegate.onGeolocationPermissionsShowPrompt(origin, callback);
        else super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    public void onGeolocationPermissionsHidePrompt() {
        if (delegate != null)
            delegate.onGeolocationPermissionsHidePrompt();
        else super.onGeolocationPermissionsHidePrompt();
    }

    public void onPermissionRequest(PermissionRequest request) {
        if (delegate != null)
            delegate.onPermissionRequest(request);
        else super.onPermissionRequest(request);
    }

    public void onPermissionRequestCanceled(PermissionRequest request) {
        if (delegate != null)
            delegate.onPermissionRequestCanceled(request);
        else super.onPermissionRequestCanceled(request);
    }

    public boolean onJsTimeout() {
        if (delegate != null)
            return delegate.onJsTimeout();
        else return super.onJsTimeout();
    }

    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        if (delegate != null)
            delegate.onConsoleMessage(message, lineNumber, sourceID);
        else super.onConsoleMessage(message, lineNumber, sourceID);
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (delegate != null)
            return delegate.onConsoleMessage(consoleMessage);
        else return super.onConsoleMessage(consoleMessage);
    }

    public Bitmap getDefaultVideoPoster() {
        if (delegate != null)
            return delegate.getDefaultVideoPoster();
        else return super.getDefaultVideoPoster();
    }

    public View getVideoLoadingProgressView() {
        if (delegate != null)
            return delegate.getVideoLoadingProgressView();
        else return super.getVideoLoadingProgressView();
    }

    public void getVisitedHistory(ValueCallback<String[]> callback) {
        if (delegate != null)
            delegate.getVisitedHistory(callback);
        else super.getVisitedHistory(callback);
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (delegate != null)
            return delegate.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        else return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }
}