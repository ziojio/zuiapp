package hummerx.ui;

import androidx.annotation.NonNull;

import com.didi.hummer.context.HummerContext;
import com.didi.hummer.core.engine.JSValue;

import hummerx.Hummerx;


public class HummerActivity extends com.didi.hummer.HummerActivity {

    @Override
    protected void renderHummer() {
        super.renderHummer();
    }

    @Override
    protected void initHummerRegister(HummerContext context) {
        super.initHummerRegister(context);
        Hummerx.registerComponents(context);
    }

    /**
     * 页面渲染成功的回调
     */
    @Override
    protected void onPageRenderSucceed(@NonNull HummerContext hmContext, @NonNull JSValue jsPage) {
        super.onPageRenderSucceed(hmContext, jsPage);

    }

    /**
     * 页面渲染失败的回调
     */
    @Override
    protected void onPageRenderFailed(@NonNull Exception e) {
        super.onPageRenderFailed(e);

    }
}