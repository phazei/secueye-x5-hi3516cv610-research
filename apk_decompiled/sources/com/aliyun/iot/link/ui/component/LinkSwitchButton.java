package com.aliyun.iot.link.ui.component;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import androidx.annotation.FloatRange;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.aliyun.iot.link.ui.component.wheelview.DimensionUtil;

/* JADX INFO: loaded from: classes2.dex */
public class LinkSwitchButton extends CompoundButton {
    private static final int DEFAULT_ANIMATION_DURATION = 250;
    private static final String TAG = "SwitchButton";
    private long mAnimationDuration;
    private ObjectAnimator mAnimator;
    private Drawable mBackgroundDrawable;
    private Drawable mCurrentBackDrawable;
    private Drawable mNextBackDrawable;
    private CharSequence mOffText;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private CharSequence mOnText;
    private Paint mPaint;
    private float mProgress;
    private RectF mThumbDrawRectF;
    private Drawable mThumbDrawable;
    private int mThumbPadding;
    private RectF mThumbRectF;
    private long mTouchDownTime;
    private int mTouchDownX;
    private static int[] STATE_ON = {android.R.attr.state_checked, android.R.attr.state_enabled, android.R.attr.state_pressed};
    private static int[] STATE_OFF = {-16842912, android.R.attr.state_enabled, android.R.attr.state_pressed};

    public LinkSwitchButton(Context context) {
        this(context, null);
    }

    public LinkSwitchButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinkSwitchButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAnimationDuration = 250L;
        this.mPaint = new Paint(1);
        obtainAttrs(context, attributeSet);
        init();
    }

    private void obtainAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinkSwitchButton);
        this.mThumbPadding = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.LinkSwitchButton_switchThumbPadding, (int) dip2px(2.5f));
        this.mBackgroundDrawable = typedArrayObtainStyledAttributes.getDrawable(R.styleable.LinkSwitchButton_switchBackground);
        if (this.mBackgroundDrawable == null) {
            this.mBackgroundDrawable = new ColorDrawable(Color.parseColor("#4cd964"));
        }
        this.mThumbDrawable = typedArrayObtainStyledAttributes.getDrawable(R.styleable.LinkSwitchButton_switchThumb);
        if (this.mThumbDrawable == null) {
            this.mThumbDrawable = new ColorDrawable(-1);
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    private void init() {
        this.mThumbRectF = new RectF();
        this.mThumbDrawRectF = new RectF();
        this.mAnimator = ObjectAnimator.ofFloat(this, NotificationCompat.CATEGORY_PROGRESS, 0.0f, 1.0f).setDuration(this.mAnimationDuration);
        this.mAnimator.setDuration(this.mAnimationDuration);
        this.mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.mTouchDownTime = System.currentTimeMillis();
                this.mTouchDownX = (int) motionEvent.getX();
                return true;
            case 1:
            case 3:
                if (System.currentTimeMillis() - this.mTouchDownTime < ViewConfiguration.getTapTimeout()) {
                    setChecked(!isChecked());
                    animateToState(isChecked());
                } else {
                    animateToState(getProgress() > 0.5f);
                    setChecked(getProgress() > 0.5f);
                }
                return true;
            case 2:
                setProgress(getProgress() + ((motionEvent.getX() - this.mTouchDownX) / (getWidth() - getHeight())));
                return true;
            default:
                return true;
        }
    }

    public void animateToState(boolean z) {
        if (this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (z) {
            this.mAnimator.setFloatValues(this.mProgress, 1.0f);
        } else {
            this.mAnimator.setFloatValues(this.mProgress, 0.0f);
        }
        this.mAnimator.start();
    }

    public void setProgress(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (f > 1.0f) {
            this.mProgress = 1.0f;
        } else if (f < 0.0f) {
            this.mProgress = 0.0f;
        } else {
            this.mProgress = f;
        }
        invalidate();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    @RequiresApi(api = 21)
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = this.mBackgroundDrawable;
        if (drawable instanceof ColorDrawable) {
            this.mPaint.setColor(((ColorDrawable) drawable).getColor());
            canvas.drawRoundRect(0.0f, 0.0f, getWidth(), getHeight(), getHeight() >> 1, getHeight() >> 1, this.mPaint);
        } else if (drawable instanceof StateListDrawable) {
            int progress = (int) ((isChecked() ? getProgress() : 1.0f - getProgress()) * 255.0f);
            this.mCurrentBackDrawable.setAlpha(progress);
            this.mCurrentBackDrawable.draw(canvas);
            this.mNextBackDrawable.setAlpha(255 - progress);
            this.mNextBackDrawable.draw(canvas);
        }
        this.mThumbDrawRectF.set(this.mThumbRectF);
        this.mThumbDrawRectF.offset(this.mProgress * (getWidth() - getHeight()), 0.0f);
        Drawable drawable2 = this.mThumbDrawable;
        if (drawable2 instanceof ColorDrawable) {
            this.mPaint.setColor(((ColorDrawable) drawable2).getColor());
            RectF rectF = this.mThumbDrawRectF;
            canvas.drawRoundRect(rectF, rectF.height() / 2.0f, this.mThumbDrawRectF.height() / 2.0f, this.mPaint);
        } else {
            drawable2.setBounds((int) this.mThumbDrawRectF.left, (int) this.mThumbDrawRectF.top, (int) this.mThumbDrawRectF.right, (int) this.mThumbDrawRectF.bottom);
            this.mThumbDrawable.draw(canvas);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] iArr = isChecked() ? STATE_OFF : STATE_ON;
        Drawable drawable = this.mBackgroundDrawable;
        if (drawable instanceof StateListDrawable) {
            drawable.setState(iArr);
            this.mNextBackDrawable = this.mBackgroundDrawable.getCurrent().mutate();
            setDrawableState(this.mBackgroundDrawable);
            this.mCurrentBackDrawable = this.mBackgroundDrawable.getCurrent().mutate();
        }
    }

    private void setDrawableState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(getDrawableState());
            invalidate();
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(getMeasuredSize((int) DimensionUtil.dip2px(40.0f), i), getMeasuredSize((int) DimensionUtil.dip2px(24.0f), i2));
        resizeDrawableBounds();
    }

    private int getMeasuredSize(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i2);
        return (mode == Integer.MIN_VALUE || mode == 0 || mode != 1073741824) ? i : View.MeasureSpec.getSize(i2);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        resizeDrawableBounds();
    }

    private void resizeDrawableBounds() {
        this.mBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        RectF rectF = this.mThumbRectF;
        int i = this.mThumbPadding;
        rectF.set(i, i, getMeasuredHeight() - this.mThumbPadding, getMeasuredHeight() - this.mThumbPadding);
        this.mThumbDrawable.setBounds((int) this.mThumbRectF.left, (int) this.mThumbRectF.top, (int) this.mThumbRectF.right, (int) this.mThumbRectF.bottom);
    }

    public CharSequence getOnText() {
        return this.mOnText;
    }

    public void setOnText(CharSequence charSequence) {
        this.mOnText = charSequence;
    }

    public CharSequence getOffText() {
        return this.mOffText;
    }

    public void setOffText(CharSequence charSequence) {
        this.mOffText = charSequence;
    }

    public int getThumbPadding() {
        return this.mThumbPadding;
    }

    public void setThumbPadding(int i) {
        this.mThumbPadding = i;
    }

    public long getAnimationDuration() {
        return this.mAnimationDuration;
    }

    public void setAnimationDuration(long j) {
        this.mAnimationDuration = j;
    }

    @Override // android.widget.CompoundButton
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        super.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        setChecked(z, false, true);
    }

    public void setChecked(boolean z, boolean z2, boolean z3) {
        if (isChecked() == z) {
            return;
        }
        if (!z3) {
            super.setOnCheckedChangeListener(null);
        }
        super.setChecked(z);
        super.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        if (z2) {
            ObjectAnimator objectAnimator = this.mAnimator;
            if (objectAnimator != null && objectAnimator.isRunning()) {
                this.mAnimator.cancel();
            }
            setProgress(z ? 1.0f : 0.0f);
            invalidate();
            return;
        }
        animateToState(z);
    }

    public float getProgress() {
        return this.mProgress;
    }

    public static float dip2px(float f) {
        return TypedValue.applyDimension(1, f, Resources.getSystem().getDisplayMetrics());
    }
}
