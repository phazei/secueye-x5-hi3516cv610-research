package com.aliyun.iot.link.ui.component.progress_dialog;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

/* JADX INFO: loaded from: classes2.dex */
public class BGAProgressBar extends ProgressBar {
    private static final int DEFAULT_RADIUS = 16;
    private static final String DEFAULT_REACHED_COLOR = "#70A800";
    private static final int DEFAULT_REACHED_HEIGHT = 2;
    private static final int DEFAULT_START_ANGLE = 270;
    private static final String DEFAULT_TEXT_COLOR = "#70A800";
    private static final int DEFAULT_TEXT_MARGIN = 4;
    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final String DEFAULT_UNREACHED_COLOR = "#CCCCCC";
    private static final int DEFAULT_UNREACHED_HEIGHT = 2;
    private static final String TAG = "BGAProgressBar";
    private RectF mArcRectF;
    private boolean mIsCapRounded;
    private boolean mIsHiddenText;
    private int mMaxStrokeWidth;
    private int mMaxUnReachedEndX;
    private Mode mMode;
    private Paint mPaint;
    private int mRadius;
    private int mReachedColor;
    private int mReachedHeight;
    private int mStartAngle;
    private String mText;
    private int mTextColor;
    private int mTextHeight;
    private int mTextMargin;
    private Rect mTextRect;
    private int mTextSize;
    private int mTextWidth;
    private int mUnReachedColor;
    private int mUnReachedHeight;

    public enum Mode {
        System,
        Horizontal,
        Circle
    }

    public BGAProgressBar(Context context) {
        this(context, null);
    }

    public BGAProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.progressBarStyle);
    }

    public BGAProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTextRect = new Rect();
        initDefaultAttrs(context);
        initCustomAttrs(context, attributeSet);
        this.mMaxStrokeWidth = Math.max(this.mReachedHeight, this.mUnReachedHeight);
    }

    private void initDefaultAttrs(Context context) {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mMode = Mode.System;
        this.mTextColor = Color.parseColor("#70A800");
        this.mTextSize = sp2px(context, 10.0f);
        this.mTextMargin = dp2px(context, 4.0f);
        this.mReachedColor = Color.parseColor("#70A800");
        this.mReachedHeight = dp2px(context, 2.0f);
        this.mUnReachedColor = Color.parseColor(DEFAULT_UNREACHED_COLOR);
        this.mUnReachedHeight = dp2px(context, 2.0f);
        this.mIsCapRounded = false;
        this.mIsHiddenText = false;
        this.mStartAngle = DEFAULT_START_ANGLE;
        this.mRadius = dp2px(context, 16.0f);
    }

    private void initCustomAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar);
        int indexCount = typedArrayObtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            initAttr(typedArrayObtainStyledAttributes.getIndex(i), typedArrayObtainStyledAttributes);
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    protected void initAttr(int i, TypedArray typedArray) {
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_mode) {
            this.mMode = Mode.values()[typedArray.getInt(i, Mode.System.ordinal())];
            return;
        }
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_textColor) {
            this.mTextColor = typedArray.getColor(i, this.mTextColor);
            return;
        }
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_textSize) {
            this.mTextSize = typedArray.getDimensionPixelOffset(i, this.mTextSize);
            return;
        }
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_textMargin) {
            this.mTextMargin = typedArray.getDimensionPixelOffset(i, this.mTextMargin);
            return;
        }
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_reachedColor) {
            this.mReachedColor = typedArray.getColor(i, this.mReachedColor);
            return;
        }
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_reachedHeight) {
            this.mReachedHeight = typedArray.getDimensionPixelOffset(i, this.mReachedHeight);
            return;
        }
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_unReachedColor) {
            this.mUnReachedColor = typedArray.getColor(i, this.mUnReachedColor);
            return;
        }
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_unReachedHeight) {
            this.mUnReachedHeight = typedArray.getDimensionPixelOffset(i, this.mUnReachedHeight);
            return;
        }
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_isCapRounded) {
            this.mIsCapRounded = typedArray.getBoolean(i, this.mIsCapRounded);
            if (this.mIsCapRounded) {
                this.mPaint.setStrokeCap(Paint.Cap.ROUND);
                return;
            }
            return;
        }
        if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_isHiddenText) {
            this.mIsHiddenText = typedArray.getBoolean(i, this.mIsHiddenText);
        } else if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_radius) {
            this.mRadius = typedArray.getDimensionPixelOffset(i, this.mRadius);
        } else if (i == com.aliyun.iot.link.ui.component.R.styleable.BGAProgressBar_bga_pb_startAngle) {
            this.mStartAngle = typedArray.getInt(i, this.mStartAngle);
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onMeasure(int i, int i2) {
        int iMax;
        if (this.mMode == Mode.System) {
            super.onMeasure(i, i2);
        } else if (this.mMode == Mode.Horizontal) {
            calculateTextWidthAndHeight();
            int size = View.MeasureSpec.getSize(i);
            int paddingTop = getPaddingTop() + getPaddingBottom();
            if (this.mIsHiddenText) {
                iMax = paddingTop + Math.max(this.mReachedHeight, this.mUnReachedHeight);
            } else {
                iMax = paddingTop + Math.max(this.mTextHeight, Math.max(this.mReachedHeight, this.mUnReachedHeight));
            }
            setMeasuredDimension(size, resolveSize(iMax, i2));
            this.mMaxUnReachedEndX = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
        } else if (this.mMode == Mode.Circle) {
            int paddingLeft = (this.mRadius * 2) + this.mMaxStrokeWidth + getPaddingLeft() + getPaddingRight();
            int iMin = Math.min(resolveSize(paddingLeft, i), resolveSize(paddingLeft, i2));
            this.mRadius = (((iMin - getPaddingLeft()) - getPaddingRight()) - this.mMaxStrokeWidth) / 2;
            if (this.mArcRectF == null) {
                this.mArcRectF = new RectF();
            }
            this.mArcRectF.set(0.0f, 0.0f, this.mRadius * 2, this.mRadius * 2);
            setMeasuredDimension(iMin, iMin);
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        if (this.mMode == Mode.System) {
            super.onDraw(canvas);
        } else if (this.mMode == Mode.Horizontal) {
            onDrawHorizontal(canvas);
        } else if (this.mMode == Mode.Circle) {
            onDrawCircle(canvas);
        }
    }

    private void onDrawHorizontal(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getMeasuredHeight() / 2);
        int i = this.mMaxUnReachedEndX;
        float progress = ((getProgress() * 1.0f) / getMax()) * i;
        if (this.mIsHiddenText) {
            if (progress > i) {
                progress = i;
            }
            if (progress > 0.0f) {
                this.mPaint.setColor(this.mReachedColor);
                this.mPaint.setStrokeWidth(this.mReachedHeight);
                this.mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(0.0f, 0.0f, progress, 0.0f, this.mPaint);
            }
            float f = this.mIsCapRounded ? progress + (((this.mReachedHeight + this.mUnReachedHeight) * 1.0f) / 2.0f) : progress;
            if (f < this.mMaxUnReachedEndX) {
                this.mPaint.setColor(this.mUnReachedColor);
                this.mPaint.setStrokeWidth(this.mUnReachedHeight);
                this.mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(f, 0.0f, this.mMaxUnReachedEndX, 0.0f, this.mPaint);
            }
        } else {
            calculateTextWidthAndHeight();
            float f2 = (this.mMaxUnReachedEndX - this.mTextWidth) - this.mTextMargin;
            if (progress > f2) {
                progress = f2;
            }
            if (progress > 0.0f) {
                this.mPaint.setColor(this.mReachedColor);
                this.mPaint.setStrokeWidth(this.mReachedHeight);
                this.mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(0.0f, 0.0f, progress, 0.0f, this.mPaint);
            }
            this.mPaint.setTextAlign(Paint.Align.LEFT);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.mTextColor);
            if (progress > 0.0f) {
                progress += this.mTextMargin;
            }
            canvas.drawText(this.mText, progress, this.mTextHeight / 2, this.mPaint);
            float f3 = progress + this.mTextWidth + this.mTextMargin;
            if (f3 < this.mMaxUnReachedEndX) {
                this.mPaint.setColor(this.mUnReachedColor);
                this.mPaint.setStrokeWidth(this.mUnReachedHeight);
                this.mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(f3, 0.0f, this.mMaxUnReachedEndX, 0.0f, this.mPaint);
            }
        }
        canvas.restore();
    }

    private void onDrawCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft() + (this.mMaxStrokeWidth / 2), getPaddingTop() + (this.mMaxStrokeWidth / 2));
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.mUnReachedColor);
        this.mPaint.setStrokeWidth(this.mUnReachedHeight);
        int i = this.mRadius;
        canvas.drawCircle(i, i, i, this.mPaint);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.mReachedColor);
        this.mPaint.setStrokeWidth(this.mReachedHeight);
        canvas.drawArc(this.mArcRectF, this.mStartAngle, ((getProgress() * 1.0f) / getMax()) * 360.0f, false, this.mPaint);
        if (!this.mIsHiddenText) {
            calculateTextWidthAndHeight();
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.mTextColor);
            this.mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(this.mText, this.mRadius, r1 + (this.mTextHeight / 2), this.mPaint);
        }
        canvas.restore();
    }

    private void calculateTextWidthAndHeight() {
        this.mText = String.format("%d", Integer.valueOf((int) (((getProgress() * 1.0f) / getMax()) * 100.0f))) + "%";
        this.mPaint.setTextSize((float) this.mTextSize);
        this.mPaint.setStyle(Paint.Style.FILL);
        Paint paint = this.mPaint;
        String str = this.mText;
        paint.getTextBounds(str, 0, str.length(), this.mTextRect);
        this.mTextWidth = this.mTextRect.width();
        this.mTextHeight = this.mTextRect.height();
    }

    public static int dp2px(Context context, float f) {
        return (int) TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float f) {
        return (int) TypedValue.applyDimension(2, f, context.getResources().getDisplayMetrics());
    }

    public void setTextColor(String str) {
        this.mTextColor = Color.parseColor(str);
    }

    public void setTextSize(int i) {
        this.mTextSize = i;
    }

    public void setTextMargin(int i) {
        this.mTextMargin = i;
    }

    public void setReachedColor(String str) {
        this.mUnReachedColor = Color.parseColor(str);
    }

    public void setUnReachedColor(String str) {
        this.mUnReachedColor = Color.parseColor(str);
    }

    public void setReachedWidth(int i) {
        this.mReachedHeight = i;
    }

    public void setUnReachedWidth(int i) {
        this.mUnReachedHeight = i;
    }

    public void isCapRounded(boolean z) {
        this.mIsCapRounded = z;
    }

    public void setStartAngle(int i) {
        this.mStartAngle = i;
    }

    public void setMode(Mode mode) {
        this.mMode = mode;
    }
}
