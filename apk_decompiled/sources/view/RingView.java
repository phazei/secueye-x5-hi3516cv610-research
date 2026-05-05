package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import tools.DensityUtil;
import tools.LogEx;

/* JADX INFO: loaded from: classes5.dex */
public class RingView extends View {
    private static final String TAG = "RingView";
    private float angle;
    private int bgResId;
    private int fillResId;
    private int mHeight;
    private Paint mPaint;
    private RectF mRectF;
    private int mWidth;
    private int ringWidth;

    public RingView(Context context) {
        super(context);
        init(context);
    }

    public RingView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public RingView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mPaint = new Paint();
        this.ringWidth = DensityUtil.dip2px(context, 8.0f);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.ringWidth);
        this.mRectF = new RectF();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0.0f, 0.0f);
        setPaintColor(this.bgResId);
        RectF rectF = this.mRectF;
        int i = this.ringWidth;
        rectF.set(i, i, this.mWidth - i, this.mHeight - i);
        canvas.drawArc(this.mRectF, 0.0f, 360.0f, false, this.mPaint);
        setPaintColor(this.fillResId);
        RectF rectF2 = this.mRectF;
        int i2 = this.ringWidth;
        rectF2.set(i2, i2, this.mWidth - i2, this.mHeight - i2);
        canvas.drawArc(this.mRectF, -90.0f, this.angle, false, this.mPaint);
    }

    private void setPaintColor(int i) {
        this.mPaint.setColor(getResources().getColor(i));
    }

    public void setValue(float f, float f2) {
        this.angle = (f2 / f) * 360.0f;
        LogEx.e(true, TAG, "angle: " + this.angle);
        invalidate();
    }

    public void setFillColor(@ColorRes int i) {
        this.fillResId = i;
    }

    public void setBgColor(@ColorRes int i) {
        this.bgResId = i;
    }
}
