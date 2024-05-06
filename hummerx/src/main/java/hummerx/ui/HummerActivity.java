package hummerx.ui;


import com.didi.hummer.context.HummerContext;
import com.didi.hummer.core.engine.JSValue;

import androidx.annotation.NonNull;

public class HummerActivity extends com.didi.hummer.HummerActivity {

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