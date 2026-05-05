package com.aliyun.iot.link.ui.component.wheelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.core.view.ViewCompat;
import com.aliyun.iot.link.ui.component.R;
import com.aliyun.iot.link.ui.component.wheelview.source.DataHolder;
import com.aliyun.iot.link.ui.component.wheelview.source.ListDataHolder;
import com.aliyun.iot.link.ui.component.wheelview.source.NumberDataHolder;
import com.aliyun.iot.link.ui.component.wheelview.source.WheelDataSource;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class ILopWheelView<T> extends View implements Runnable {
    private static final float DEFAULT_FRICTION = 0.06f;
    private static final String DEFAULT_INTEGER_FORMAT = "%d";
    private static final int DEFAULT_NORMAL_TEXT_COLOR = -7829368;
    private static final int DEFAULT_RESERVED_DECIMAL_DIGITS = 2;
    private static final int DEFAULT_SELECTED_TEXT_COLOR = -16777216;
    private static final int DEFAULT_VISIBLE_ITEM = 5;
    private static final String TAG = "WheelView";
    private float _normalTextSizeOrigin;
    private float _selectTextSizeOrigin;
    private boolean mAutoAdjustTextSize;
    private long mClickTimeout;
    private int mCurrentScrollPosition;

    @NonNull
    private DataHolder<T> mDataHolder;
    private int mDecimalDigitNumber;
    private DecimalFormat mDecimalFormat;
    private int mDividerColor;
    private float mDividerHeight;
    private Rect mDrawRect;
    private float mFriction;
    private boolean mFroze;
    private String mIntegerFormat;
    private boolean mIsCyclic;
    private boolean mIsDragging;
    private boolean mIsFlingScroll;
    private int mItemHeight;
    private int mMaxScrollY;
    private int mMaximumVelocity;
    private int mMinScrollY;
    private int mMinimumVelocity;
    private int mNormalTextColor;
    private float mNormalTextSize;
    private OnItemSelectedListener<T> mOnItemSelectedListener;
    private Paint mPaint;
    private int mScrollOffsetY;
    private Scroller mScroller;
    private int mSelectedItemPosition;
    private int mSelectedItemTextColor;
    private float mSelectedItemTextSize;
    private int mSelectedRectColor;
    private SoundPlayer mSoundPlayer;
    private boolean mSpringBackEffect;
    private long mTouchDownTime;
    private float mTouchY;
    private VelocityTracker mVelocityTracker;
    private int mVisibleItemNum;
    private List<WheelLayer> mWheelLayers;
    private static final float DEFAULT_SELECTED_TEXT_SIZE = DimensionUtil.sp2px(18.0f);
    private static final float DEFAULT_NORMAL_TEXT_SIZE = DimensionUtil.sp2px(14.0f);
    private static final float DEFAULT_DIVIDER_HEIGHT = DimensionUtil.dip2px(1.0f);
    public static boolean mEnableLog = false;

    public interface OnItemSelectedListener<T> {
        void onItemSelected(T t, int i);

        void onWheelSelecting(T t, int i);
    }

    public ILopWheelView(Context context) {
        this(context, null);
    }

    public ILopWheelView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ILopWheelView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPaint = new Paint(1);
        this.mItemHeight = 1;
        this.mDataHolder = new DataHolder.EmptyHolder();
        this.mFriction = DEFAULT_FRICTION;
        this.mIsDragging = false;
        this.mIsFlingScroll = false;
        this.mFroze = false;
        this.mWheelLayers = new ArrayList();
        obtainAttrs(context, attributeSet);
        init(context);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SoundPlayer soundPlayer = this.mSoundPlayer;
        if (soundPlayer != null) {
            soundPlayer.release();
        }
    }

    private void obtainAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ILopWheelView);
        float dimension = typedArrayObtainStyledAttributes.getDimension(R.styleable.ILopWheelView_selectedTextSize, DEFAULT_SELECTED_TEXT_SIZE);
        this.mSelectedItemTextSize = dimension;
        this._selectTextSizeOrigin = dimension;
        float dimension2 = typedArrayObtainStyledAttributes.getDimension(R.styleable.ILopWheelView_normalTextSize, DEFAULT_NORMAL_TEXT_SIZE);
        this.mNormalTextSize = dimension2;
        this._normalTextSizeOrigin = dimension2;
        this.mAutoAdjustTextSize = typedArrayObtainStyledAttributes.getBoolean(R.styleable.ILopWheelView_autoAdjustTextSize, false);
        this.mNormalTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.ILopWheelView_normalTextColor, DEFAULT_NORMAL_TEXT_COLOR);
        this.mSelectedItemTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.ILopWheelView_selectedTextColor, -16777216);
        this.mDecimalDigitNumber = typedArrayObtainStyledAttributes.getInt(R.styleable.ILopWheelView_decimalDigitsNumber, 2);
        this.mIntegerFormat = typedArrayObtainStyledAttributes.getString(R.styleable.ILopWheelView_integerFormat);
        if (TextUtils.isEmpty(this.mIntegerFormat)) {
            this.mIntegerFormat = DEFAULT_INTEGER_FORMAT;
        }
        this.mVisibleItemNum = adjustVisibleItemNum(typedArrayObtainStyledAttributes.getInt(R.styleable.ILopWheelView_visibleItemNum, 5));
        this.mIsCyclic = typedArrayObtainStyledAttributes.getBoolean(R.styleable.ILopWheelView_cyclic, false);
        this.mDividerHeight = typedArrayObtainStyledAttributes.getDimension(R.styleable.ILopWheelView_dividerHeight, DEFAULT_DIVIDER_HEIGHT);
        this.mDividerColor = typedArrayObtainStyledAttributes.getColor(R.styleable.ILopWheelView_dividerColor, 0);
        this.mSelectedRectColor = typedArrayObtainStyledAttributes.getColor(R.styleable.ILopWheelView_selectedItemBackgroundColor, 0);
        this.mFriction = typedArrayObtainStyledAttributes.getFloat(R.styleable.ILopWheelView_friction, DEFAULT_FRICTION);
        this.mSpringBackEffect = !typedArrayObtainStyledAttributes.getBoolean(R.styleable.ILopWheelView_fixSpringBack, true);
        typedArrayObtainStyledAttributes.recycle();
    }

    private void init(Context context) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mClickTimeout = ViewConfiguration.getTapTimeout();
        this.mScroller = new Scroller(context);
        this.mScroller.setFriction(this.mFriction);
        this.mDrawRect = new Rect();
        if (isInEditMode()) {
            return;
        }
        this.mSoundPlayer = new SoundPlayer();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mItemHeight = Math.max(View.MeasureSpec.getSize(i2) / this.mVisibleItemNum, 1);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mItemHeight = Math.max(1, i2 / this.mVisibleItemNum);
        this.mDrawRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        setBoundary();
        if (this.mAutoAdjustTextSize) {
            resizeTextSize(this.mItemHeight);
        }
        this.mScrollOffsetY = this.mSelectedItemPosition * this.mItemHeight;
    }

    private void resizeTextSize(int i) {
        float f = this._selectTextSizeOrigin;
        this.mSelectedItemTextSize = f;
        float f2 = this._normalTextSizeOrigin;
        this.mNormalTextSize = f2;
        float f3 = i * 0.9f;
        if (this.mSelectedItemTextSize > f3) {
            this.mSelectedItemTextSize = f3;
            this.mNormalTextSize = (this.mSelectedItemTextSize * f2) / f;
        }
    }

    private void setBoundary() {
        this.mMinScrollY = this.mIsCyclic ? Integer.MIN_VALUE : 0;
        this.mMaxScrollY = this.mIsCyclic ? Integer.MAX_VALUE : (this.mDataHolder.size() - 1) * this.mItemHeight;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mFroze) {
            Log("frozen.  skip touch events");
            return false;
        }
        initVelocityTracker(motionEvent);
        switch (motionEvent.getActionMasked()) {
            case 0:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (!this.mScroller.isFinished()) {
                    this.mScroller.forceFinished(true);
                }
                this.mIsDragging = true;
                this.mTouchY = motionEvent.getY();
                this.mTouchDownTime = System.currentTimeMillis();
                return true;
            case 1:
                this.mIsDragging = false;
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                float yVelocity = this.mVelocityTracker.getYVelocity();
                if (Math.abs(yVelocity) > this.mMinimumVelocity) {
                    this.mScroller.forceFinished(true);
                    this.mIsFlingScroll = true;
                    this.mScroller.fling(0, this.mScrollOffsetY, 0, (int) (-yVelocity), 0, 0, this.mMinScrollY, this.mMaxScrollY);
                    if (!this.mSpringBackEffect) {
                        fixBounchEffect();
                    }
                } else {
                    int y = System.currentTimeMillis() - this.mTouchDownTime <= this.mClickTimeout ? (int) (motionEvent.getY() - this.mDrawRect.centerY()) : 0;
                    int iCalculateDistanceNeedToScroll = y + calculateDistanceNeedToScroll((this.mScrollOffsetY + y) % this.mItemHeight);
                    if (!this.mIsCyclic) {
                        if (iCalculateDistanceNeedToScroll <= 0) {
                            iCalculateDistanceNeedToScroll = Math.max(iCalculateDistanceNeedToScroll, -this.mScrollOffsetY);
                        } else {
                            iCalculateDistanceNeedToScroll = Math.min(iCalculateDistanceNeedToScroll, this.mMaxScrollY - this.mScrollOffsetY);
                        }
                    }
                    this.mScroller.startScroll(0, this.mScrollOffsetY, 0, iCalculateDistanceNeedToScroll);
                }
                invalidateAndCheckItemChange();
                ViewCompat.postOnAnimation(this, this);
                recycleVelocityTracker();
                return true;
            case 2:
                float y2 = motionEvent.getY();
                float f = y2 - this.mTouchY;
                if (Math.abs(f) >= 1.0f) {
                    scroll((int) (-f));
                    this.mTouchY = y2;
                }
                return true;
            case 3:
                recycleVelocityTracker();
                return true;
            default:
                return true;
        }
    }

    private void fixBounchEffect() {
        int i;
        int finalY = this.mScroller.getFinalY();
        int iAbs = Math.abs(finalY % this.mItemHeight);
        int i2 = this.mItemHeight;
        if (iAbs <= (i2 >> 1)) {
            i = (finalY / i2) * i2;
        } else if (finalY < 0) {
            i = ((finalY / i2) * i2) - i2;
        } else {
            i = ((finalY / i2) * i2) + i2;
        }
        this.mScroller.setFinalY(i);
    }

    private void initVelocityTracker(MotionEvent motionEvent) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void scroll(int i) {
        this.mScrollOffsetY += i;
        if (!this.mIsCyclic) {
            this.mScrollOffsetY = Math.min(this.mMaxScrollY, Math.max(this.mMinScrollY, this.mScrollOffsetY));
        }
        invalidateAndCheckItemChange();
    }

    private void invalidateAndCheckItemChange() {
        invalidate();
        checkIfSelectItemChange();
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void checkIfSelectItemChange() {
        int currentPosition = getCurrentPosition();
        if (this.mCurrentScrollPosition != currentPosition) {
            OnItemSelectedListener<T> onItemSelectedListener = this.mOnItemSelectedListener;
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onWheelSelecting(this.mDataHolder.get(currentPosition), currentPosition);
            }
            playSoundEffect();
            this.mCurrentScrollPosition = currentPosition;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        int i2;
        super.onDraw(canvas);
        drawSelectedItemBackground(canvas);
        drawDivider(canvas);
        if (this.mFroze) {
            i2 = this.mSelectedItemPosition;
            i = 0;
        } else {
            int i3 = this.mScrollOffsetY;
            int i4 = this.mItemHeight;
            int i5 = i3 / i4;
            i = i3 % i4;
            i2 = i5;
        }
        int i6 = (this.mVisibleItemNum + 1) / 2;
        int i7 = i2 + i6;
        for (int i8 = (i2 - i6) + (this.mScrollOffsetY > 0 ? 1 : 0); i8 <= i7; i8++) {
            drawItem(canvas, i8, i);
        }
        Iterator<WheelLayer> it = this.mWheelLayers.iterator();
        while (it.hasNext()) {
            it.next().onDraw(this, canvas, this.mDrawRect);
        }
    }

    private void drawSelectedItemBackground(Canvas canvas) {
        int i = this.mSelectedRectColor;
        if (i != 0) {
            this.mPaint.setColor(i);
            canvas.drawRect(this.mDrawRect.left, this.mDrawRect.centerY() - (this.mItemHeight >> 1), this.mDrawRect.right, this.mDrawRect.centerY() + (this.mItemHeight >> 1), this.mPaint);
        }
    }

    private void drawDivider(Canvas canvas) {
        int i = this.mDividerColor;
        if (i == 0) {
            return;
        }
        this.mPaint.setColor(i);
        this.mPaint.setStrokeWidth(this.mDividerHeight);
        canvas.drawLine(this.mDrawRect.left, this.mDrawRect.centerY() - (this.mItemHeight >> 1), this.mDrawRect.right, this.mDrawRect.centerY() - (this.mItemHeight >> 1), this.mPaint);
        canvas.drawLine(this.mDrawRect.left, this.mDrawRect.centerY() + (this.mItemHeight >> 1), this.mDrawRect.right, this.mDrawRect.centerY() + (this.mItemHeight >> 1), this.mPaint);
    }

    private void drawItem(Canvas canvas, int i, int i2) {
        int i3;
        int i4;
        String textByIndex = getTextByIndex(i);
        if (textByIndex == null) {
            return;
        }
        if (this.mFroze) {
            i3 = (i - this.mSelectedItemPosition) * this.mItemHeight;
        } else {
            int i5 = this.mScrollOffsetY;
            int i6 = this.mItemHeight;
            i3 = ((i - (i5 / i6)) * i6) - i2;
        }
        int iAbs = Math.abs(i3);
        int i7 = this.mItemHeight;
        if (iAbs < i7) {
            float fAbs = this.mNormalTextSize + (((i7 - Math.abs(i3)) / (this.mItemHeight * 1.0f)) * (this.mSelectedItemTextSize - this.mNormalTextSize));
            int iEvaluate = evaluate((r3 - Math.abs(i3)) / (this.mItemHeight * 1.0f), this.mNormalTextColor, this.mSelectedItemTextColor);
            this.mPaint.setTextSize(fAbs);
            this.mPaint.setColor(iEvaluate);
            i4 = (int) ((this.mPaint.getFontMetrics().descent + this.mPaint.getFontMetrics().ascent) / 2.0f);
        } else {
            this.mPaint.setTextSize(this.mNormalTextSize);
            this.mPaint.setColor(this.mNormalTextColor);
            i4 = (int) ((this.mPaint.getFontMetrics().descent + this.mPaint.getFontMetrics().ascent) / 2.0f);
        }
        canvas.drawText(textByIndex, (int) ((((getWidth() - getPaddingLeft()) - getPaddingRight()) - this.mPaint.measureText(textByIndex)) / 2.0f), (this.mDrawRect.centerY() + i3) - i4, this.mPaint);
    }

    public int evaluate(float f, int i, int i2) {
        float f2 = ((i >> 24) & 255) / 255.0f;
        float fPow = (float) Math.pow(((i >> 16) & 255) / 255.0f, 2.2d);
        float fPow2 = (float) Math.pow(((i >> 8) & 255) / 255.0f, 2.2d);
        float fPow3 = (float) Math.pow((i & 255) / 255.0f, 2.2d);
        float fPow4 = (float) Math.pow(((i2 >> 16) & 255) / 255.0f, 2.2d);
        float f3 = f2 + (((((i2 >> 24) & 255) / 255.0f) - f2) * f);
        float fPow5 = fPow2 + ((((float) Math.pow(((i2 >> 8) & 255) / 255.0f, 2.2d)) - fPow2) * f);
        float fPow6 = fPow3 + (f * (((float) Math.pow((i2 & 255) / 255.0f, 2.2d)) - fPow3));
        return (Math.round(((float) Math.pow(fPow + ((fPow4 - fPow) * f), 0.45454545454545453d)) * 255.0f) << 16) | (Math.round(f3 * 255.0f) << 24) | (Math.round(((float) Math.pow(fPow5, 0.45454545454545453d)) * 255.0f) << 8) | Math.round(((float) Math.pow(fPow6, 0.45454545454545453d)) * 255.0f);
    }

    private String getTextByIndex(int i) {
        if (this.mDataHolder.isEmpty()) {
            Log("data is empty");
            return null;
        }
        if (!this.mIsCyclic && (i < 0 || i >= this.mDataHolder.size())) {
            return null;
        }
        int size = i % this.mDataHolder.size();
        if (size < 0) {
            size += this.mDataHolder.size();
        }
        return getItemDisplayText(this.mDataHolder.get(size));
    }

    public String getItemDisplayText(T t) {
        if (t == null) {
            return "";
        }
        if (t instanceof WheelDataSource) {
            return ((WheelDataSource) t).getDisplayText();
        }
        if (t instanceof Integer) {
            return !TextUtils.isEmpty(this.mIntegerFormat) ? String.format(Locale.getDefault(), this.mIntegerFormat, t) : String.valueOf(t);
        }
        if ((t instanceof Float) || (t instanceof Double)) {
            if (this.mDecimalFormat == null) {
                initDecimalFormat();
            }
            return this.mDecimalFormat.format(t);
        }
        return t.toString();
    }

    private void initDecimalFormat() {
        StringBuilder sb = new StringBuilder("0.");
        for (int i = 0; i < this.mDecimalDigitNumber; i++) {
            sb.append("0");
        }
        this.mDecimalFormat = new DecimalFormat(sb.toString());
        this.mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        this.mDecimalFormat.setGroupingSize(3);
        this.mDecimalFormat.setGroupingUsed(true);
    }

    public void playSoundEffect() {
        SoundPlayer soundPlayer = this.mSoundPlayer;
        if (soundPlayer != null) {
            soundPlayer.play();
        }
    }

    public void finishScroll() {
        if (this.mScroller.isFinished()) {
            return;
        }
        this.mScroller.abortAnimation();
        this.mSelectedItemPosition = (int) ((this.mScroller.getFinalY() / this.mItemHeight) + (this.mScrollOffsetY > 0 ? 0.5f : -0.5f));
        updateAfterStop();
    }

    public void stopScroll() {
        if (this.mScroller.isFinished()) {
            return;
        }
        this.mScroller.forceFinished(true);
        this.mSelectedItemPosition = (int) ((r0 / this.mItemHeight) + (this.mScrollOffsetY > 0 ? 0.5f : -0.5f));
        updateAfterStop();
    }

    private void updateAfterStop() {
        int i = this.mSelectedItemPosition;
        if (i < 0) {
            this.mSelectedItemPosition = (i % this.mDataHolder.size()) + this.mDataHolder.size();
        }
        if (this.mSelectedItemPosition >= this.mDataHolder.size()) {
            this.mSelectedItemPosition %= this.mDataHolder.size();
        }
        Log("stop position:" + this.mSelectedItemPosition);
        this.mScrollOffsetY = this.mItemHeight * this.mSelectedItemPosition;
        this.mIsFlingScroll = false;
        invalidateAndCheckItemChange();
    }

    private int calculateDistanceNeedToScroll(int i) {
        int iAbs = Math.abs(i);
        int i2 = this.mItemHeight;
        return iAbs > i2 / 2 ? this.mScrollOffsetY < 0 ? (-i2) - i : i2 - i : -i;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // java.lang.Runnable
    public void run() {
        if (this.mScroller.isFinished() && !this.mIsDragging && !this.mIsFlingScroll) {
            int currentPosition = getCurrentPosition();
            if (currentPosition == this.mSelectedItemPosition) {
                return;
            }
            this.mSelectedItemPosition = currentPosition;
            this.mCurrentScrollPosition = currentPosition;
            OnItemSelectedListener<T> onItemSelectedListener = this.mOnItemSelectedListener;
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelected(this.mDataHolder.get(this.mSelectedItemPosition), this.mSelectedItemPosition);
            }
        }
        if (this.mScroller.computeScrollOffset()) {
            this.mScrollOffsetY = this.mScroller.getCurrY();
            invalidateAndCheckItemChange();
            ViewCompat.postOnAnimation(this, this);
        } else if (this.mIsFlingScroll) {
            this.mIsFlingScroll = false;
            Scroller scroller = this.mScroller;
            int i = this.mScrollOffsetY;
            scroller.startScroll(0, i, 0, calculateDistanceNeedToScroll(i % this.mItemHeight));
            invalidateAndCheckItemChange();
            ViewCompat.postOnAnimation(this, this);
        }
    }

    private int getCurrentPosition() {
        int i;
        if (this.mItemHeight == 0 || this.mDataHolder.isEmpty()) {
            return 0;
        }
        int i2 = this.mScrollOffsetY;
        if (i2 < 0) {
            int i3 = this.mItemHeight;
            i = (i2 - (i3 / 2)) / i3;
        } else {
            int i4 = this.mItemHeight;
            i = (i2 + (i4 / 2)) / i4;
        }
        int size = i % this.mDataHolder.size();
        return size < 0 ? size + this.mDataHolder.size() : size;
    }

    public void setSoundEffectResource(@RawRes int i) {
        SoundPlayer soundPlayer = this.mSoundPlayer;
        if (soundPlayer != null) {
            soundPlayer.load(getContext(), i);
        }
    }

    public T getSelectedItemData() {
        return this.mDataHolder.get(this.mSelectedItemPosition);
    }

    public List<T> getData() {
        return this.mDataHolder.toList();
    }

    public Paint getPaint() {
        return this.mPaint;
    }

    public void setData(List<T> list) {
        if (list == null) {
            return;
        }
        setDataSource(new ListDataHolder(list));
    }

    public void setDataInRange(Number number, Number number2, Number number3, boolean z) {
        setDataSource(new NumberDataHolder(number, number2, number3, z));
    }

    public void setDataSource(DataHolder<T> dataHolder) {
        this.mScroller.forceFinished(true);
        this.mDataHolder = dataHolder;
        reset();
    }

    private void reset() {
        setBoundary();
        this.mScrollOffsetY = 0;
        this.mSelectedItemPosition = 0;
        this.mCurrentScrollPosition = 0;
        invalidateAndCheckItemChange();
    }

    public float getSelectedItemTextSize() {
        return this.mSelectedItemTextSize;
    }

    public void setSelectedTextSize(float f) {
        float fSp2px = DimensionUtil.sp2px(f);
        if (fSp2px == this.mSelectedItemTextSize) {
            return;
        }
        this.mSelectedItemTextSize = fSp2px;
        if (isAutoAdjustTextSize()) {
            requestLayout();
        }
        invalidate();
    }

    public boolean isAutoAdjustTextSize() {
        return this.mAutoAdjustTextSize;
    }

    public void setAutoAdjustTextSize(boolean z) {
        this.mAutoAdjustTextSize = z;
        requestLayout();
        invalidate();
    }

    public void setNormalItemTextColor(@ColorInt int i) {
        if (this.mNormalTextColor == i) {
            return;
        }
        this.mNormalTextColor = i;
        invalidate();
    }

    public float getNormalTextSize() {
        return this.mNormalTextSize;
    }

    public void setNormalTextSize(float f) {
        this.mNormalTextSize = f;
        invalidate();
    }

    public int getDecimalDigitNumber() {
        return this.mDecimalDigitNumber;
    }

    public void setDecimalDigitNumber(int i) {
        this.mDecimalDigitNumber = i;
        invalidate();
    }

    public boolean isSpringBackEffect() {
        return this.mSpringBackEffect;
    }

    public void setSpringBackEffect(boolean z) {
        this.mSpringBackEffect = z;
    }

    public void setSelectedItemTextColor(@ColorInt int i) {
        if (this.mSelectedItemTextColor == i) {
            return;
        }
        this.mSelectedItemTextColor = i;
        invalidate();
    }

    public String getIntegerFormat() {
        return this.mIntegerFormat;
    }

    public void setIntegerFormat(String str) {
        if (TextUtils.isEmpty(str) || str.equals(this.mIntegerFormat)) {
            return;
        }
        this.mIntegerFormat = str;
        invalidate();
    }

    public float getFriction() {
        return this.mFriction;
    }

    public void setFriction(float f) {
        this.mFriction = f;
        this.mScroller.setFriction(f);
    }

    public int getVisibleItemNum() {
        return this.mVisibleItemNum;
    }

    public void setVisibleItemNum(int i) {
        if (this.mVisibleItemNum == i) {
            return;
        }
        this.mVisibleItemNum = adjustVisibleItemNum(i);
        this.mScrollOffsetY = 0;
        invalidate();
    }

    private int adjustVisibleItemNum(int i) {
        return ((i / 2) * 2) + 1;
    }

    public boolean isCyclic() {
        return this.mIsCyclic;
    }

    public void setCyclic(boolean z) {
        if (this.mIsCyclic == z) {
            return;
        }
        this.mIsCyclic = z;
        setBoundary();
        invalidate();
    }

    public int getSelectedItemPosition() {
        return this.mSelectedItemPosition;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void setDefault(int i) {
        if (i < 0) {
            i = 0;
            Log("index not in range:0");
        } else if (i > this.mDataHolder.size() - 1) {
            i = this.mDataHolder.size() - 1;
            Log("index not in range:" + i);
        }
        int i2 = (this.mItemHeight * i) - this.mScrollOffsetY;
        if (i2 == 0) {
            return;
        }
        finishScroll();
        scroll(i2);
        this.mSelectedItemPosition = i;
        OnItemSelectedListener<T> onItemSelectedListener = this.mOnItemSelectedListener;
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(this.mDataHolder.get(this.mSelectedItemPosition), this.mSelectedItemPosition);
        }
    }

    private void Log(String str) {
        if (mEnableLog) {
            Log.d(TAG, str);
        }
    }

    public static boolean isEnableLog() {
        return mEnableLog;
    }

    public static void setEnableLog(boolean z) {
        mEnableLog = z;
    }

    public int getDividerColor() {
        return this.mDividerColor;
    }

    public void setDividerColor(@ColorInt int i) {
        if (this.mDividerColor == i) {
            return;
        }
        this.mDividerColor = i;
        invalidate();
    }

    public float getDividerHeight() {
        return this.mDividerHeight;
    }

    public void setDividerHeight(float f) {
        float fDip2px = DimensionUtil.dip2px(f);
        if (fDip2px == this.mDividerHeight) {
            return;
        }
        this.mDividerHeight = fDip2px;
        invalidate();
    }

    public int getSelectedRectColor() {
        return this.mSelectedRectColor;
    }

    public void setSelectedRectColor(@ColorInt int i) {
        if (this.mSelectedRectColor == i) {
            return;
        }
        this.mSelectedRectColor = i;
        invalidate();
    }

    public OnItemSelectedListener<T> getOnItemSelectedListener() {
        return this.mOnItemSelectedListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    public List<WheelLayer> getWheelLayers() {
        return this.mWheelLayers;
    }

    public void addWheelLayer(WheelLayer wheelLayer) {
        this.mWheelLayers.add(wheelLayer);
    }

    public boolean isFroze() {
        return this.mFroze;
    }

    public void setFroze(boolean z) {
        this.mFroze = z;
    }

    private static class SoundPlayer {
        private int mSoundId;
        private SoundPool mSoundPool;

        public SoundPlayer() {
            if (Build.VERSION.SDK_INT >= 21) {
                this.mSoundPool = new SoundPool.Builder().build();
            } else {
                this.mSoundPool = new SoundPool(1, 1, 0);
            }
        }

        public void load(Context context, @RawRes int i) {
            SoundPool soundPool = this.mSoundPool;
            if (soundPool != null) {
                this.mSoundId = soundPool.load(context, i, 1);
            }
        }

        public void play() {
            int i;
            SoundPool soundPool = this.mSoundPool;
            if (soundPool == null || (i = this.mSoundId) == 0) {
                return;
            }
            soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
        }

        public void release() {
            SoundPool soundPool = this.mSoundPool;
            if (soundPool != null) {
                soundPool.release();
            }
        }
    }
}
