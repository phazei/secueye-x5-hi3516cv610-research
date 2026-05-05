package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class BaseShadowButton extends AppCompatButton {
    public static final int SHAPE_TYPE_RECTANGLE = 1;
    public static final int SHAPE_TYPE_ROUND = 0;
    protected Paint mBackgroundPaint;
    protected int mHeight;
    protected int mRadius;
    protected RectF mRectF;
    protected int mShapeType;
    protected int mWidth;

    public BaseShadowButton(Context context) {
        super(context);
        init(context, null);
    }

    public BaseShadowButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public BaseShadowButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ShadowButton);
        this.mShapeType = typedArrayObtainStyledAttributes.getInt(7, 1);
        this.mRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(3, getResources().getDimensionPixelSize(R.dimen.dimen_2));
        int color = typedArrayObtainStyledAttributes.getColor(2, 0);
        typedArrayObtainStyledAttributes.recycle();
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setStyle(Paint.Style.FILL);
        this.mBackgroundPaint.setAlpha(Color.alpha(color));
        this.mBackgroundPaint.setColor(color);
        this.mBackgroundPaint.setAntiAlias(true);
        setWillNotDraw(false);
        setDrawingCacheEnabled(true);
        setClickable(true);
        eraseOriginalBackgroundColor(color);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
        this.mRectF = new RectF(0.0f, 0.0f, this.mWidth, this.mHeight);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        Paint paint = this.mBackgroundPaint;
        if (paint == null) {
            super.onDraw(canvas);
            return;
        }
        if (this.mShapeType == 0) {
            int i = this.mWidth;
            canvas.drawCircle(i / 2.0f, this.mHeight / 2.0f, i / 2.0f, paint);
        } else {
            RectF rectF = this.mRectF;
            int i2 = this.mRadius;
            canvas.drawRoundRect(rectF, i2, i2, paint);
        }
        super.onDraw(canvas);
    }

    protected void eraseOriginalBackgroundColor(int i) {
        if (i != 0) {
            setBackgroundColor(0);
        }
    }

    public void setUnpressedColor(int i) {
        this.mBackgroundPaint.setAlpha(Color.alpha(i));
        this.mBackgroundPaint.setColor(i);
        eraseOriginalBackgroundColor(i);
        invalidate();
    }

    public int getShapeType() {
        return this.mShapeType;
    }

    public void setShapeType(int i) {
        this.mShapeType = i;
        invalidate();
    }

    public int getRadius() {
        return this.mRadius;
    }

    public void setRadius(int i) {
        this.mRadius = i;
        invalidate();
    }
}
