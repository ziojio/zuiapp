package zuiapp.ui.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

import androidx.webkit.WebViewAssetLoader;

public class WebViewUtil {

    /**
     * 应用的 {@link WebView} 设置
     *
     * @param webview
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebView(WebView webview) {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        // Setting this off for security. Off by default for SDK versions >= 16.
        settings.setAllowFileAccessFromFileURLs(false);
        // Off by default, deprecated for SDK versions >= 30.
        settings.setAllowUniversalAccessFromFileURLs(false);
        // Keeping these off is less critical but still a good idea, especially if your app is not
        // using file:// or content:// URLs.
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
    }

    /**
     * 创建应用本地资源加载器
     */
    public static WebViewAssetLoader createWebViewAssetLoader(Context context) {
        return new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(context))
                .addPathHandler("/res/", new WebViewAssetLoader.ResourcesPathHandler(context))
                .addPathHandler("/webcache/", new WebViewAssetLoader.InternalStoragePathHandler(context,
                        new File(context.getCacheDir(), "webcache")))
                .setDomain("hummer.didi.cn")
                .setHttpAllowed(true)
                .build();
    }

    /**
     * @param url
     * @return true 1.拦截请求 2.应用处理这个请求
     */
    public static boolean interceptRequest(String url) {
        if (url.contains("baidu.com")) {
            // 不允许访问百度
            return true;
        }
        if (url.startsWith("file://")) {
            // 不允许访问文件
            return true;
        }
        return false;
    }

}
