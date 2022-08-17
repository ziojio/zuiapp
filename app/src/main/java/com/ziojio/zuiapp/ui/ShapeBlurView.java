package com.ziojio.zuiapp.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.github.mmin18.widget.RealtimeBlurView;
import com.ziojio.zuiapp.R;

public class ShapeBlurView extends RealtimeBlurView {
    private final Paint mPaint;
    private final RectF mRectF;
    private final float mRadius;

    public ShapeBlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mRectF = new RectF();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeBlurView);
        mRadius = a.getDimension(R.styleable.ShapeBlurView_radius, 0);
        a.recycle();
    }

    /**
     * Custom drawRoundRect
     */
    @Override
    protected void drawBlurredBitmap(Canvas canvas, Bitmap blurredBitmap, int overlayColor) {
        if (blurredBitmap != null) {
            mRectF.right = getWidth();
            mRectF.bottom = getHeight();

            mPaint.reset();
            mPaint.setAntiAlias(true);
            BitmapShader shader = new BitmapShader(blurredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();
            matrix.postScale(mRectF.width() / blurredBitmap.getWidth(), mRectF.height() / blurredBitmap.getHeight());
            shader.setLocalMatrix(matrix);
            mPaint.setShader(shader);
            // canvas.drawOval(mRectF, mPaint);
            canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
        }

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(overlayColor);
        // canvas.drawOval(mRectF, mPaint);
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
    }
}