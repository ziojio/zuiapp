package com.ziojio.hummer;

import android.content.Context;

import com.didi.hummer.Hummer;
import com.didi.hummer.HummerConfig;
import com.didi.hummer.adapter.http.impl.DefaultHttpAdapter;
import com.didi.hummer.adapter.imageloader.impl.DefaultImageLoaderAdapter;
import com.didi.hummer.adapter.navigator.impl.DefaultNavigatorAdapter;
import com.didi.hummer.context.HummerContext;
import com.didi.hummer.register.HummerRegister$$hummer;
import com.ziojio.hummer.adapter.ZuiAppIntentCreator;
import com.ziojio.hummer.adapter.ZuiAppStorageAdapter;

import timber.log.Timber;


public class ZuiHummer {

    public static void initHummer(Context context) {
        HummerConfig config = new HummerConfig.Builder()
                // 自定义namespace（用于业务线隔离，需和Hummer容器中的namespace配合使用，可选）
                .setNamespace("hm")
                // JS异常回调（可选）
                .setExceptionCallback(e -> {
                    if (e != null) {
                        Timber.e("JSException: " + e.getMessage());
                    }
                })
                // RTL支持（可选）
                .setSupportRTL(false)
                // 字体文件Assets目录设置（可选）
                .setFontsAssetsPath("fonts")
                // 自定义路由（可在这里指定自定义Hummer容器，可选）
                .setNavigatorAdapter(new DefaultNavigatorAdapter(new ZuiAppIntentCreator()))
                // 自定义图片库（可选）
                .setImageLoaderAdapter(new DefaultImageLoaderAdapter())
                // 自定义网络库（可选）
                .setHttpAdapter(new DefaultHttpAdapter())
                // 自定义定位（可选）
                // .setLocationAdapter(new DefaultLocationAdapter())
                // 自定义持久化存储（可选）
                .setStorageAdapter(new ZuiAppStorageAdapter())
                // 构造HummerConfig
                .builder();
        Hummer.init(context, config);
    }


    public static void registerComponents(HummerContext hmContext) {
        HummerRegister$$hummer.init(hmContext);
    }
}
