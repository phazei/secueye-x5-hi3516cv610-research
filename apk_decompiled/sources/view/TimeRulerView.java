package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.seculink.app.R;
import java.lang.reflect.Field;
import java.util.List;

/* JADX INFO: loaded from: classes5.dex */
public class TimeRulerView extends View {
    private static final boolean LOG_ENABLE = true;
    public static final int MAX_TIME_VALUE = 86400;
    private final int MAX_VELOCITY;
    private final int MIN_VELOCITY;
    private final int SCROLL_SLOP;
    private int bgColor;

    @IntRange(from = 0, to = 86400)
    private int currentTime;
    private float dialLen;
    private int gradationColor;
    private int gradationTextColor;
    private float gradationTextGapBottom;
    private float gradationTextGapTop;
    private float gradationTextSize;
    private float gradationWidth;
    private boolean hasMoveIgnoreScale;
    private float hourLen;
    private int indicatorColor;
    private float indicatorTriangleSideLen;
    private float indicatorWidth;
    private boolean isMoving;
    private boolean isOnTouch;
    private boolean isScaling;
    private float mCurrentDistance;
    private int mHalfWidth;
    private int mHeight;
    private float mHourLenBaseLine;
    private int mInitialX;
    private int mLastX;
    private int mLastY;
    private OnTimeChangedListener mListener;
    private final float mOneSecondGap;
    private Paint mPaint;
    private float[] mPerCountScaleThresholds;
    private int mPerTextCountIndex;
    private float mScale;
    private ScaleGestureDetector mScaleGestureDetector;
    private Scroller mScroller;
    private float mTextCenterVerticalBaseLine;
    private final float mTextHalfWidth;
    private final float mTextHalfWidthSec;
    private TextPaint mTextPaint;
    private List<TimePart> mTimePartList;
    private Path mTrianglePath;
    private float mUnitGap;
    private int mUnitSecond;
    private VelocityTracker mVelocityTracker;
    private int mWidth;
    private int mWidthRangeValue;
    private float minuteLen;
    private OnTouchStateChangeListener onTouchStateChangeListener;
    private int partColor;
    private float partHeight;
    private float secondLen;
    private static final int[] mUnitSeconds = {1, 1, 5, 5, 10, 10, 10, 10, 60, 60, 300, 300, 900, 900, 900, 900, 900, 900};
    private static int[] mPerTextCounts = {20, 20, 30, 30, 60, 60, 120, PsExtractor.VIDEO_STREAM_MASK, 300, 600, 1200, 1800, 3600, 7200, 10800, 14400, 18000, 21600};

    public interface OnTimeChangedListener {
        void onTimeChanged(int i);

        void onTimeSelected(int i);

        void seekError();
    }

    public interface OnTouchStateChangeListener {
        void onTouch(boolean z);
    }

    public static class TimePart {
        public int endTime;
        public int startTime;
        public int timeColor;
    }

    public TimeRulerView(Context context) {
        this(context, null);
    }

    public TimeRulerView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TimeRulerView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPerCountScaleThresholds = new float[]{26.0f, 14.0f, 12.0f, 7.0f, 6.0f, 3.6f, 1.8f, 1.5f, 0.8f, 0.4f, 0.25f, 0.125f, 0.07f, 0.04f, 0.03f, 0.025f, 0.02f, 0.015f};
        this.mPerTextCountIndex = 9;
        this.mUnitSecond = mUnitSeconds[this.mPerTextCountIndex];
        this.mScale = 0.481998f;
        this.mOneSecondGap = dp2px(12.0f) / 60.0f;
        this.mUnitGap = this.mScale * this.mOneSecondGap * this.mUnitSecond;
        initAttrs(context, attributeSet);
        init(context);
        initScaleGestureDetector(context);
        this.mTextHalfWidth = this.mTextPaint.measureText("00:00") * 0.5f;
        this.mTextHalfWidthSec = this.mTextPaint.measureText("00:00:00") * 0.5f;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.SCROLL_SLOP = viewConfiguration.getScaledTouchSlop();
        this.MIN_VELOCITY = viewConfiguration.getScaledMinimumFlingVelocity();
        this.MAX_VELOCITY = viewConfiguration.getScaledMaximumFlingVelocity();
        initScaleInfos();
        calculateValues();
    }

    private void initScaleInfos() {
        this.mPerTextCountIndex = 9;
        this.mScale = 0.481998f;
        this.mUnitSecond = mUnitSeconds[this.mPerTextCountIndex];
        this.mUnitGap = this.mScale * this.mOneSecondGap * this.mUnitSecond;
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TimeRuleView);
        this.bgColor = typedArrayObtainStyledAttributes.getColor(0, Color.parseColor("#EEEEEE"));
        this.gradationColor = typedArrayObtainStyledAttributes.getColor(3, -7829368);
        this.partHeight = typedArrayObtainStyledAttributes.getDimension(15, dp2px(20.0f));
        this.partColor = typedArrayObtainStyledAttributes.getColor(14, Color.parseColor("#F58D24"));
        this.gradationWidth = typedArrayObtainStyledAttributes.getDimension(8, 1.0f);
        this.dialLen = typedArrayObtainStyledAttributes.getDimension(2, dp2px(2.0f));
        this.secondLen = typedArrayObtainStyledAttributes.getDimension(16, dp2px(3.0f));
        this.minuteLen = typedArrayObtainStyledAttributes.getDimension(13, dp2px(5.0f));
        this.hourLen = typedArrayObtainStyledAttributes.getDimension(9, dp2px(10.0f));
        this.gradationTextColor = typedArrayObtainStyledAttributes.getColor(4, -7829368);
        this.gradationTextSize = typedArrayObtainStyledAttributes.getDimension(7, sp2px(12.0f));
        this.gradationTextGapTop = typedArrayObtainStyledAttributes.getDimension(6, dp2px(6.0f));
        this.gradationTextGapBottom = typedArrayObtainStyledAttributes.getDimension(5, dp2px(10.0f));
        this.currentTime = typedArrayObtainStyledAttributes.getInt(1, 0);
        this.indicatorTriangleSideLen = typedArrayObtainStyledAttributes.getDimension(12, dp2px(15.0f));
        this.indicatorWidth = typedArrayObtainStyledAttributes.getDimension(11, dp2px(1.0f));
        this.indicatorColor = typedArrayObtainStyledAttributes.getColor(10, SupportMenu.CATEGORY_MASK);
        typedArrayObtainStyledAttributes.recycle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateValues() {
        this.mCurrentDistance = (this.currentTime / this.mUnitSecond) * this.mUnitGap;
    }

    private void init(Context context) {
        this.mPaint = new Paint(1);
        this.mTextPaint = new TextPaint(1);
        this.mTextPaint.setTextSize(this.gradationTextSize);
        this.mTextPaint.setColor(this.gradationTextColor);
        this.mTrianglePath = new Path();
        this.mScroller = new Scroller(context);
    }

    public void setBigScale() {
        int i = this.mPerTextCountIndex;
        if (i > 0) {
            this.mPerTextCountIndex = i - 2;
            if (this.mPerTextCountIndex <= 0) {
                this.mPerTextCountIndex = 0;
            }
            float[] fArr = this.mPerCountScaleThresholds;
            int i2 = this.mPerTextCountIndex;
            this.mScale = fArr[i2];
            this.mUnitSecond = mUnitSeconds[i2];
            float f = this.mScale * this.mOneSecondGap;
            int i3 = this.mUnitSecond;
            this.mUnitGap = f * i3;
            this.mWidthRangeValue = (int) ((this.mWidth / this.mUnitGap) * i3);
            calculateValues();
            invalidate();
        }
    }

    public void setSmallScale() {
        int i = this.mPerTextCountIndex;
        float[] fArr = this.mPerCountScaleThresholds;
        if (i < fArr.length - 1) {
            this.mPerTextCountIndex = i + 2;
            if (this.mPerTextCountIndex >= fArr.length - 1) {
                this.mPerTextCountIndex = fArr.length - 1;
            }
            float[] fArr2 = this.mPerCountScaleThresholds;
            int i2 = this.mPerTextCountIndex;
            this.mScale = fArr2[i2];
            this.mUnitSecond = mUnitSeconds[i2];
            float f = this.mScale * this.mOneSecondGap;
            int i3 = this.mUnitSecond;
            this.mUnitGap = f * i3;
            this.mWidthRangeValue = (int) ((this.mWidth / this.mUnitGap) * i3);
            calculateValues();
            invalidate();
        }
    }

    public void setMaxScale() {
        this.mPerTextCountIndex = 0;
        float[] fArr = this.mPerCountScaleThresholds;
        int i = this.mPerTextCountIndex;
        this.mScale = fArr[i];
        this.mUnitSecond = mUnitSeconds[i];
        float f = this.mScale * this.mOneSecondGap;
        int i2 = this.mUnitSecond;
        this.mUnitGap = f * i2;
        this.mWidthRangeValue = (int) ((this.mWidth / this.mUnitGap) * i2);
        calculateValues();
        invalidate();
    }

    public void setMinScale() {
        float[] fArr = this.mPerCountScaleThresholds;
        this.mPerTextCountIndex = fArr.length - 1;
        int i = this.mPerTextCountIndex;
        this.mScale = fArr[i];
        this.mUnitSecond = mUnitSeconds[i];
        float f = this.mScale * this.mOneSecondGap;
        int i2 = this.mUnitSecond;
        this.mUnitGap = f * i2;
        this.mWidthRangeValue = (int) ((this.mWidth / this.mUnitGap) * i2);
        calculateValues();
        invalidate();
    }

    public void resetScale() {
        initScaleInfos();
        this.mWidthRangeValue = (int) ((this.mWidth / this.mUnitGap) * this.mUnitSecond);
        calculateValues();
        invalidate();
    }

    private void initScaleGestureDetector(Context context) {
        this.mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() { // from class: view.TimeRulerView.1
            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                float scaleFactor = scaleGestureDetector.getScaleFactor();
                TimeRulerView.this.logD("onScale...focusX=%f, focusY=%f, scaleFactor=%f", Float.valueOf(scaleGestureDetector.getFocusX()), Float.valueOf(scaleGestureDetector.getFocusY()), Float.valueOf(scaleFactor));
                float f = TimeRulerView.this.mPerCountScaleThresholds[0];
                float f2 = TimeRulerView.this.mPerCountScaleThresholds[TimeRulerView.this.mPerCountScaleThresholds.length - 1];
                if (scaleFactor > 1.0f && TimeRulerView.this.mScale >= f) {
                    return true;
                }
                if (scaleFactor < 1.0f && TimeRulerView.this.mScale <= f2) {
                    return true;
                }
                TimeRulerView.this.mScale *= scaleFactor;
                TimeRulerView timeRulerView = TimeRulerView.this;
                timeRulerView.mScale = Math.max(f2, Math.min(f, timeRulerView.mScale));
                TimeRulerView timeRulerView2 = TimeRulerView.this;
                timeRulerView2.mPerTextCountIndex = timeRulerView2.findScaleIndex(timeRulerView2.mScale);
                TimeRulerView.this.mUnitSecond = TimeRulerView.mUnitSeconds[TimeRulerView.this.mPerTextCountIndex];
                TimeRulerView timeRulerView3 = TimeRulerView.this;
                timeRulerView3.mUnitGap = timeRulerView3.mScale * TimeRulerView.this.mOneSecondGap * TimeRulerView.this.mUnitSecond;
                TimeRulerView.this.mWidthRangeValue = (int) ((r9.mWidth / TimeRulerView.this.mUnitGap) * TimeRulerView.this.mUnitSecond);
                TimeRulerView timeRulerView4 = TimeRulerView.this;
                timeRulerView4.logD("onScale: mScale=%1$f, mPerTextCountIndex=%2$d, mUnitSecond=%3$d, mUnitGap=%4$f", Float.valueOf(timeRulerView4.mScale), Integer.valueOf(TimeRulerView.this.mPerTextCountIndex), Integer.valueOf(TimeRulerView.this.mUnitSecond), Float.valueOf(TimeRulerView.this.mUnitGap));
                TimeRulerView.this.calculateValues();
                TimeRulerView.this.invalidate();
                return true;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                TimeRulerView.this.logD("onScaleBegin...", new Object[0]);
                TimeRulerView.this.isScaling = true;
                TimeRulerView.this.isOnTouch = true;
                return true;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
                TimeRulerView.this.isScaling = false;
                TimeRulerView.this.logD("onScaleEnd...", new Object[0]);
            }
        });
        int scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            Field declaredField = ScaleGestureDetector.class.getDeclaredField("mMinSpan");
            declaredField.setAccessible(true);
            declaredField.set(this.mScaleGestureDetector, Integer.valueOf(scaledTouchSlop));
            declaredField.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findScaleIndex(float f) {
        int length = this.mPerCountScaleThresholds.length - 1;
        int i = (length + 0) >> 1;
        int i2 = 0;
        do {
            float[] fArr = this.mPerCountScaleThresholds;
            if (f >= fArr[i] && f < fArr[i - 1]) {
                break;
            }
            if (f >= this.mPerCountScaleThresholds[i - 1]) {
                length = i;
            } else {
                i2 = i + 1;
            }
            i = (i2 + length) >> 1;
            if (i2 >= length) {
                break;
            }
        } while (i != 0);
        return i;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        this.mWidth = View.MeasureSpec.getSize(i);
        this.mHeight = View.MeasureSpec.getSize(i2);
        if (View.MeasureSpec.getMode(i2) == Integer.MIN_VALUE) {
            this.mHeight = dp2px(60.0f);
        }
        int i3 = this.mWidth;
        this.mHalfWidth = i3 >> 1;
        int i4 = this.mHeight;
        this.partHeight = i4;
        this.mWidthRangeValue = (int) ((i3 / this.mUnitGap) * this.mUnitSecond);
        setMeasuredDimension(i3, i4);
        setTextYLocation();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        int pointerId = motionEvent.getPointerId(actionIndex);
        int actionMasked = motionEvent.getActionMasked();
        logD("onTouchEvent:isOnTouch=%b isScaling=%b, isMoving=%b, actionIndex=%d, pointerId=%d, actionMasked=%d, action=%d, pointerCount=%d, currentDistance=%f", Boolean.valueOf(this.isOnTouch), Boolean.valueOf(this.isScaling), Boolean.valueOf(this.isMoving), Integer.valueOf(actionIndex), Integer.valueOf(pointerId), Integer.valueOf(actionMasked), Integer.valueOf(motionEvent.getAction()), Integer.valueOf(motionEvent.getPointerCount()), Float.valueOf(this.mCurrentDistance));
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        this.mScaleGestureDetector.onTouchEvent(motionEvent);
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (actionMasked) {
            case 0:
                callbackTouchStateTrue();
                this.isOnTouch = true;
                this.isMoving = false;
                this.hasMoveIgnoreScale = false;
                this.mInitialX = x;
                if (!this.mScroller.isFinished()) {
                    this.mScroller.forceFinished(true);
                    this.hasMoveIgnoreScale = true;
                }
                break;
            case 1:
            case 3:
                if (!this.isScaling) {
                    this.mVelocityTracker.computeCurrentVelocity(1000, this.MAX_VELOCITY);
                    int xVelocity = (int) this.mVelocityTracker.getXVelocity();
                    if (Math.abs(xVelocity) >= this.MIN_VELOCITY) {
                        this.mScroller.fling((int) this.mCurrentDistance, 0, -xVelocity, 0, 0, (int) ((86400 / this.mUnitSecond) * this.mUnitGap), 0, 0);
                        this.isMoving = false;
                        invalidate();
                        callbackTouchStateFalse();
                        this.isOnTouch = false;
                        this.mListener.onTimeSelected(this.currentTime);
                    } else if (!this.hasMoveIgnoreScale) {
                        this.isOnTouch = false;
                    } else if (1 == this.mUnitSecond) {
                        scrollToGradation();
                    } else if (this.mListener != null) {
                        if (this.mTimePartList != null) {
                            for (int i = 0; i < this.mTimePartList.size() && (i != this.mTimePartList.size() - 1 || this.currentTime <= this.mTimePartList.get(i).endTime); i++) {
                            }
                        }
                        callbackTouchStateFalse();
                        this.isOnTouch = false;
                        this.mListener.onTimeSelected(this.currentTime);
                    }
                }
                break;
            case 2:
                if (!this.isScaling) {
                    int i2 = x - this.mLastX;
                    if (!this.isMoving) {
                        int i3 = y - this.mLastY;
                        if (Math.abs(x - this.mInitialX) > this.SCROLL_SLOP && Math.abs(i2) > Math.abs(i3)) {
                            this.isMoving = true;
                            this.hasMoveIgnoreScale = true;
                            this.mCurrentDistance -= i2;
                            computeTime(false);
                        }
                    } else {
                        this.mCurrentDistance -= i2;
                        computeTime(false);
                    }
                }
                break;
            case 5:
                this.isScaling = true;
                this.isMoving = false;
                break;
            case 6:
                this.isScaling = false;
                this.mInitialX = (int) motionEvent.getX(actionIndex == 0 ? 1 : 0);
                break;
        }
        this.mLastX = x;
        this.mLastY = y;
        return true;
    }

    private void computeTime(boolean z) {
        this.mCurrentDistance = Math.min((86400 / this.mUnitSecond) * this.mUnitGap, Math.max(0.0f, this.mCurrentDistance));
        this.currentTime = (int) ((this.mCurrentDistance / this.mUnitGap) * this.mUnitSecond);
        OnTimeChangedListener onTimeChangedListener = this.mListener;
        if (onTimeChangedListener != null) {
            onTimeChangedListener.onTimeChanged(this.currentTime);
            if (z) {
                if (this.mTimePartList != null) {
                    for (int i = 0; i < this.mTimePartList.size() && (i != this.mTimePartList.size() - 1 || this.currentTime <= this.mTimePartList.get(i).endTime); i++) {
                    }
                }
                callbackTouchStateFalse();
                this.isOnTouch = false;
                this.mListener.onTimeSelected(this.currentTime);
            }
        }
        invalidate();
    }

    private void scrollToGradation() {
        this.currentTime = Math.round(this.mCurrentDistance / this.mUnitGap);
        this.currentTime = Math.max(0, Math.min(this.currentTime, 86400));
        this.mCurrentDistance = this.currentTime * this.mUnitGap;
        logD("scrollToGradation: mCurrentDistance=%f, currentTime=%d", Float.valueOf(this.mCurrentDistance), Integer.valueOf(this.currentTime));
        if (this.mListener != null) {
            callbackTouchStateFalse();
            this.isOnTouch = false;
            this.mListener.onTimeChanged(this.currentTime);
            this.mListener.onTimeSelected(this.currentTime);
        }
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(this.bgColor);
        drawTimeParts(canvas);
        drawRule(canvas);
        drawTimeIndicator(canvas);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.mCurrentDistance = this.mScroller.getCurrX();
            if (this.mScroller.getCurrX() == this.mScroller.getFinalX()) {
                if (1 == this.mUnitSecond) {
                    scrollToGradation();
                    return;
                } else {
                    computeTime(true);
                    return;
                }
            }
            computeTime(false);
        }
    }

    private void drawRule(Canvas canvas) {
        this.mPaint.setColor(this.gradationColor);
        this.mPaint.setStrokeWidth(this.gradationWidth);
        int i = this.mUnitSecond;
        int i2 = i * 3;
        int iMax = Math.max(0, (((int) ((this.mCurrentDistance - this.mHalfWidth) / this.mUnitGap)) * i) - i2);
        int iMin = Math.min(86400, iMax + i2 + this.mWidthRangeValue + i2);
        float f = this.mHalfWidth - (this.mCurrentDistance - ((iMax / this.mUnitSecond) * this.mUnitGap));
        int i3 = mPerTextCounts[this.mPerTextCountIndex];
        logD("start >>%d, end >>%d, startOffset >>%f, mUnitGap >>%f,mUnitSecond >>%d", Integer.valueOf(iMax), Integer.valueOf(iMin), Float.valueOf(f), Float.valueOf(this.mUnitGap), Integer.valueOf(this.mUnitSecond));
        while (iMax <= iMin) {
            if (iMax % 3600 == 0) {
                float f2 = this.mHourLenBaseLine;
                canvas.drawLine(f, f2, f, f2 - this.hourLen, this.mPaint);
            } else if (iMax % 60 == 0) {
                float f3 = this.mHourLenBaseLine;
                canvas.drawLine(f, f3, f, f3 - this.minuteLen, this.mPaint);
            } else if (iMax % i3 == 0) {
                float f4 = this.mHourLenBaseLine;
                canvas.drawLine(f, f4, f, f4 - this.dialLen, this.mPaint);
            } else {
                float f5 = this.mHourLenBaseLine;
                canvas.drawLine(f, f5, f, f5 - this.secondLen, this.mPaint);
            }
            if (iMax % i3 == 0) {
                if (this.mPerTextCountIndex < 4) {
                    canvas.drawText(formatTimeHHmmss(iMax), f - this.mTextHalfWidthSec, this.mTextCenterVerticalBaseLine, this.mTextPaint);
                } else {
                    canvas.drawText(formatTimeHHmm(iMax), f - this.mTextHalfWidth, this.mTextCenterVerticalBaseLine, this.mTextPaint);
                }
            }
            f += this.mUnitGap;
            iMax += this.mUnitSecond;
        }
    }

    private void setTextYLocation() {
        Paint.FontMetrics fontMetrics = this.mTextPaint.getFontMetrics();
        this.mTextCenterVerticalBaseLine = this.mHeight - this.gradationTextGapBottom;
        this.mHourLenBaseLine = (this.mTextCenterVerticalBaseLine + fontMetrics.ascent) - this.gradationTextGapTop;
    }

    private void drawTimeIndicator(Canvas canvas) {
        this.mPaint.setColor(this.indicatorColor);
        this.mPaint.setStrokeWidth(this.indicatorWidth);
        int i = this.mHalfWidth;
        canvas.drawLine(i, 0.0f, i, this.mHeight, this.mPaint);
        if (this.mTrianglePath.isEmpty()) {
            float f = this.indicatorTriangleSideLen * 0.5f;
            float f2 = -f;
            this.mTrianglePath.moveTo(f2, (-this.mHeight) / 2);
            this.mTrianglePath.rLineTo(this.indicatorTriangleSideLen, 0.0f);
            this.mTrianglePath.rLineTo(f2, (float) (Math.sin(Math.toRadians(60.0d)) * ((double) f)));
            this.mTrianglePath.close();
        }
        this.mPaint.setStrokeWidth(1.0f);
        this.mPaint.setStyle(Paint.Style.FILL);
        int iSave = canvas.save();
        canvas.translate(this.mHalfWidth, this.mHeight / 2);
        canvas.rotate(180.0f);
        canvas.drawPath(this.mTrianglePath, this.mPaint);
        canvas.restoreToCount(iSave);
        this.mPaint.setStyle(Paint.Style.STROKE);
    }

    private void drawTimeParts(Canvas canvas) {
        if (this.mTimePartList == null) {
            return;
        }
        this.mPaint.setStrokeWidth(this.partHeight);
        float f = this.partHeight * 0.5f;
        float f2 = this.mUnitGap / this.mUnitSecond;
        int size = this.mTimePartList.size();
        for (int i = 0; i < size; i++) {
            TimePart timePart = this.mTimePartList.get(i);
            float f3 = (this.mHalfWidth - this.mCurrentDistance) + (timePart.startTime * f2);
            float f4 = (timePart.endTime * f2) + (this.mHalfWidth - this.mCurrentDistance);
            if (timePart.timeColor == 1) {
                this.mPaint.setColor(Color.parseColor("#FA5454"));
            } else {
                this.mPaint.setColor(Color.parseColor("#2999FF"));
            }
            if (f3 < this.mWidth && f4 > 0.0f) {
                canvas.drawLine(f3, f, f4, f, this.mPaint);
            }
        }
    }

    public static String formatTimeHHmm(@IntRange(from = 0, to = 86400) int i) {
        if (i < 0) {
            i = 0;
        }
        int i2 = i / 3600;
        int i3 = (i % 3600) / 60;
        StringBuilder sb = new StringBuilder();
        if (i2 < 10) {
            sb.append('0');
        }
        sb.append(i2);
        sb.append(':');
        if (i3 < 10) {
            sb.append('0');
        }
        sb.append(i3);
        return sb.toString();
    }

    public static String formatTimeHHmmss(@IntRange(from = 0, to = 86400) int i) {
        int iMax = Math.max(0, Math.min(i, 86400));
        int i2 = iMax / 3600;
        int i3 = iMax % 3600;
        int i4 = i3 / 60;
        int i5 = i3 % 60;
        StringBuilder sb = new StringBuilder();
        if (i2 < 10) {
            sb.append('0');
        }
        sb.append(i2);
        sb.append(':');
        if (i4 < 10) {
            sb.append('0');
        }
        sb.append(i4);
        sb.append(':');
        if (i5 < 10) {
            sb.append('0');
        }
        sb.append(i5);
        return sb.toString();
    }

    private int dp2px(float f) {
        return (int) TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }

    private int sp2px(float f) {
        return (int) TypedValue.applyDimension(2, f, getResources().getDisplayMetrics());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str, Object... objArr) {
        Log.d("TimerRulerView", String.format(str, objArr));
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mListener = onTimeChangedListener;
    }

    public void setOnTouchStateChangeListener(OnTouchStateChangeListener onTouchStateChangeListener) {
        this.onTouchStateChangeListener = onTouchStateChangeListener;
    }

    public void setTimePartList(List<TimePart> list) {
        this.mTimePartList = list;
        postInvalidate();
    }

    public void setTimePartListAndBackZero(List<TimePart> list) {
        this.mTimePartList = list;
        initScaleInfos();
        setCurrentTime(0);
    }

    public void setTimePartListAndBackCenter(List<TimePart> list) {
        this.mTimePartList = list;
        initScaleInfos();
        this.mWidthRangeValue = (int) ((this.mWidth / this.mUnitGap) * this.mUnitSecond);
        resetToMiddle();
    }

    @SuppressLint({"DefaultLocale"})
    public boolean setCurrentTime(@IntRange(from = 0, to = 86400) int i) {
        int iMin;
        Log.d("TimeRuler", String.format("setCurrentTime:%1$b, currentTime:%2$d, newTime:%3$d", Boolean.valueOf(this.isOnTouch), Integer.valueOf(this.currentTime), Integer.valueOf(i)));
        if (this.isOnTouch || this.currentTime == (iMin = Math.min(86400, Math.max(i, 0)))) {
            return false;
        }
        this.currentTime = iMin;
        calculateValues();
        postInvalidate();
        return true;
    }

    private void resetToMiddle() {
        this.currentTime = 43200;
        calculateValues();
        postInvalidate();
    }

    public int getCurrentTime() {
        return Math.min(86400, Math.max(this.currentTime, 0));
    }

    private void callbackTouchStateFalse() {
        OnTouchStateChangeListener onTouchStateChangeListener = this.onTouchStateChangeListener;
        if (onTouchStateChangeListener == null || !this.isOnTouch) {
            return;
        }
        onTouchStateChangeListener.onTouch(false);
    }

    private void callbackTouchStateTrue() {
        OnTouchStateChangeListener onTouchStateChangeListener = this.onTouchStateChangeListener;
        if (onTouchStateChangeListener == null || this.isOnTouch) {
            return;
        }
        onTouchStateChangeListener.onTouch(true);
    }
}
