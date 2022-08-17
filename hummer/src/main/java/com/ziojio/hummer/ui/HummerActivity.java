package com.ziojio.hummer.ui;

import androidx.annotation.NonNull;

import com.didi.hummer.context.HummerContext;
import com.didi.hummer.core.engine.JSValue;
import com.ziojio.hummer.ZuiHummer;


public class HummerActivity extends com.didi.hummer.HummerActivity {

    @Override
    protected void renderHummer() {
        super.renderHummer();
    }

    @Override
    protected void initHummerRegister(HummerContext context) {
        ZuiHummer.registerComponents(context);
    }

    /**
     * 页面渲染成功的回调
     */
    @Override
    protected void onPageRenderSucceed(@NonNull HummerContext hmContext, @NonNull JSValue jsPage) {

    }

    /**
     * 页面渲染失败的回调
     */
    @Override
    protected void onPageRenderFailed(@NonNull Exception e) {

    }
}