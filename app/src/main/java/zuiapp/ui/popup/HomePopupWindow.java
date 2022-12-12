package zuiapp.ui.popup;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import razerdp.basepopup.BasePopupWindow;

/**
 * 自定义的PopupWindow
 */
public class HomePopupWindow extends BasePopupWindow {
    private ImageView mIvArrow;

    public HomePopupWindow(Context context) {
        super(context);
    }

    public void setExitListener(View.OnClickListener clickListener) {
    }

    @Override
    public void onPopupLayout(@NonNull Rect popupRect, @NonNull Rect anchorRect) {
        int gravity = computeGravity(popupRect, anchorRect);
        boolean verticalCenter = false;
        //计算垂直位置
        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                mIvArrow.setVisibility(View.VISIBLE);
                //设置箭头水平位置为相对于basepopup居中
                mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth()) >> 1);
                //设置箭头垂直位置为相对于basepopup底部
                mIvArrow.setTranslationY(popupRect.height() - mIvArrow.getHeight());
                //设置旋转角度0度（即箭头朝下，具体根据您的初始切图而定）
                mIvArrow.setRotation(0f);
                break;
            case Gravity.BOTTOM:
                mIvArrow.setVisibility(View.VISIBLE);
                mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth()) >> 1);
                mIvArrow.setTranslationY(0);
                mIvArrow.setRotation(180f);
                break;
            case Gravity.CENTER_VERTICAL:
                verticalCenter = true;
                break;
        }
        // switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
        //     case Gravity.LEFT:
        //         mIvArrow.setVisibility(View.VISIBLE);
        //         mIvArrow.setTranslationX(popupRect.width() - mIvArrow.getWidth());
        //         mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight()) >> 1);
        //         mIvArrow.setRotation(270f);
        //         break;
        //     case Gravity.RIGHT:
        //         mIvArrow.setVisibility(View.VISIBLE);
        //         mIvArrow.setTranslationX(0);
        //         mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight()) >> 1);
        //         mIvArrow.setRotation(90f);
        //         break;
        //     case Gravity.CENTER_HORIZONTAL:
        //         //如果basepopup与anchorview中心对齐，则隐藏箭头
        //         mIvArrow.setVisibility(verticalCenter ? View.INVISIBLE : View.VISIBLE);
        //         break;
        // }
    }

}
