package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class ShadowButton extends BaseShadowButton {
    private int COVER_ALPHA;
    private int mPressedColor;
    private Paint mPressedPaint;

    public ShadowButton(Context context) {
        super(context);
        this.COVER_ALPHA = 48;
    }

    public ShadowButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        this.COVER_ALPHA = 48;
    }

    public ShadowButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.COVER_ALPHA = 48;
    }

    @Override // view.BaseShadowButton
    protected void init(Context context, AttributeSet attributeSet) {
        super.init(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ShadowButton);
        this.COVER_ALPHA = typedArrayObtainStyledAttributes.getInteger(0, this.COVER_ALPHA);
        this.mPressedColor = typedArrayObtainStyledAttributes.getColor(1, getResources().getColor(R.color.default_shadow_button_color_pressed));
        typedArrayObtainStyledAttributes.recycle();
        this.mPressedPaint = new Paint();
        this.mPressedPaint.setStyle(Paint.Style.FILL);
        this.mPressedPaint.setColor(this.mPressedColor);
        this.mPressedPaint.setAlpha(0);
        this.mPressedPaint.setAntiAlias(true);
    }

    @Override // view.BaseShadowButton, android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mShapeType == 0) {
            canvas.drawCircle(this.mWidth / 2.0f, this.mHeight / 2.0f, this.mWidth / 2.1038f, this.mPressedPaint);
        } else {
            canvas.drawRoundRect(this.mRectF, this.mRadius, this.mRadius, this.mPressedPaint);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0016  */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r3) {
        /*
            r2 = this;
            int r0 = r3.getAction()
            r1 = 3
            if (r0 == r1) goto L16
            switch(r0) {
                case 0: goto Lb;
                case 1: goto L16;
                default: goto La;
            }
        La:
            goto L1f
        Lb:
            android.graphics.Paint r0 = r2.mPressedPaint
            int r1 = r2.COVER_ALPHA
            r0.setAlpha(r1)
            r2.invalidate()
            goto L1f
        L16:
            android.graphics.Paint r0 = r2.mPressedPaint
            r1 = 0
            r0.setAlpha(r1)
            r2.invalidate()
        L1f:
            boolean r3 = super.onTouchEvent(r3)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: view.ShadowButton.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public int getPressedColor() {
        return this.mPressedColor;
    }

    public void setPressedColor(int i) {
        this.mPressedPaint.setColor(this.mPressedColor);
        invalidate();
    }
}
