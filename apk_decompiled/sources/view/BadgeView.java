package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;
import tools.DensityUtils;
import tools.MathUtils;
import view.Badge;

/* JADX INFO: loaded from: classes5.dex */
public class BadgeView extends View implements Badge {
    public static final int DEFAULT_BADGE_PADDING = 5;
    public static final int DEFAULT_COLOR_BACKGROUND = -1552832;
    public static final int DEFAULT_COLOR_BADGE_TEXT = -1;
    public static final int DEFAULT_GRAVITY_OFFSET = 1;
    public static final int DEFAULT_TEXT_SIZE = 11;
    private BadgeContainer badgeContainer;
    protected ViewGroup mActivityRoot;
    protected BadgeAnimator mAnimator;
    protected float mBackgroundBorderWidth;
    protected Paint mBadgeBackgroundBorderPaint;
    protected Paint mBadgeBackgroundPaint;
    protected RectF mBadgeBackgroundRect;
    protected PointF mBadgeCenter;
    protected int mBadgeGravity;
    protected int mBadgeNumber;
    protected float mBadgePadding;
    protected String mBadgeText;
    protected Paint.FontMetrics mBadgeTextFontMetrics;
    protected TextPaint mBadgeTextPaint;
    protected RectF mBadgeTextRect;
    protected float mBadgeTextSize;
    protected Bitmap mBitmapClip;
    protected int mColorBackground;
    protected int mColorBackgroundBorder;
    protected int mColorBadgeText;
    protected PointF mControlPoint;
    protected float mDefaultRadius;
    protected PointF mDragCenter;
    protected boolean mDragOutOfRange;
    protected Path mDragPath;
    protected int mDragQuadrant;
    protected Badge.OnDragStateChangedListener mDragStateChangedListener;
    protected boolean mDraggable;
    protected boolean mDragging;
    protected Drawable mDrawableBackground;
    protected boolean mDrawableBackgroundClip;
    protected boolean mExact;
    protected float mFinalDragDistance;
    protected float mGravityOffsetX;
    protected float mGravityOffsetY;
    protected int mHeight;
    protected List<PointF> mInnerTangentPoints;
    protected PointF mRowBadgeCenter;
    protected boolean mShowShadow;
    protected View mTargetView;
    protected int mWidth;

    @Override // view.Badge
    public void showView() {
    }

    public BadgeView(Context context) {
        this(context, null);
    }

    private BadgeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    private BadgeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setLayerType(1, null);
        this.mBadgeTextRect = new RectF();
        this.mBadgeBackgroundRect = new RectF();
        this.mDragPath = new Path();
        this.mBadgeCenter = new PointF();
        this.mDragCenter = new PointF();
        this.mRowBadgeCenter = new PointF();
        this.mControlPoint = new PointF();
        this.mInnerTangentPoints = new ArrayList();
        this.mBadgeTextPaint = new TextPaint();
        this.mBadgeTextPaint.setAntiAlias(true);
        this.mBadgeTextPaint.setSubpixelText(true);
        this.mBadgeTextPaint.setFakeBoldText(true);
        this.mBadgeTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        this.mBadgeBackgroundPaint = new Paint();
        this.mBadgeBackgroundPaint.setAntiAlias(true);
        this.mBadgeBackgroundPaint.setStyle(Paint.Style.FILL);
        this.mBadgeBackgroundBorderPaint = new Paint();
        this.mBadgeBackgroundBorderPaint.setAntiAlias(true);
        this.mBadgeBackgroundBorderPaint.setStyle(Paint.Style.STROKE);
        this.mColorBackground = DEFAULT_COLOR_BACKGROUND;
        this.mColorBadgeText = -1;
        this.mBadgeTextSize = DensityUtils.sp2px(getContext(), 11.0f);
        this.mBadgePadding = DensityUtils.dp2px(getContext(), 5.0f);
        this.mBadgeNumber = 0;
        this.mBadgeGravity = 8388661;
        this.mGravityOffsetX = DensityUtils.dp2px(getContext(), 1.0f);
        this.mGravityOffsetY = DensityUtils.dp2px(getContext(), 1.0f);
        this.mFinalDragDistance = DensityUtils.dp2px(getContext(), 90.0f);
        this.mShowShadow = true;
        this.mDrawableBackgroundClip = false;
        if (Build.VERSION.SDK_INT >= 21) {
            setTranslationZ(1000.0f);
        }
    }

    @Override // view.Badge
    public Badge bindTarget(View view2) {
        if (view2 == null) {
            throw new IllegalStateException("targetView can not be null");
        }
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        ViewParent parent = view2.getParent();
        if (parent instanceof ViewGroup) {
            this.mTargetView = view2;
            if (parent instanceof BadgeContainer) {
                ((BadgeContainer) parent).addView(this);
            } else {
                ViewGroup viewGroup = (ViewGroup) parent;
                int iIndexOfChild = viewGroup.indexOfChild(view2);
                ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                viewGroup.removeView(view2);
                this.badgeContainer = new BadgeContainer(getContext());
                if (viewGroup instanceof RelativeLayout) {
                    this.badgeContainer.setId(view2.getId());
                }
                viewGroup.addView(this.badgeContainer, iIndexOfChild, layoutParams);
                this.badgeContainer.addView(view2);
                this.badgeContainer.addView(this);
            }
            return this;
        }
        throw new IllegalStateException("targetView must have a parent");
    }

    @Override // view.Badge
    public View getTargetView() {
        return this.mTargetView;
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mActivityRoot == null) {
            findViewRoot(this.mTargetView);
        }
    }

    private void findViewRoot(View view2) {
        this.mActivityRoot = (ViewGroup) view2.getRootView();
        if (this.mActivityRoot == null) {
            findActivityRoot(view2);
        }
    }

    private void findActivityRoot(View view2) {
        if (view2.getParent() != null && (view2.getParent() instanceof View)) {
            findActivityRoot((View) view2.getParent());
        } else if (view2 instanceof ViewGroup) {
            this.mActivityRoot = (ViewGroup) view2;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case 0:
            case 5:
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (this.mDraggable && motionEvent.getPointerId(motionEvent.getActionIndex()) == 0 && x > this.mBadgeBackgroundRect.left && x < this.mBadgeBackgroundRect.right && y > this.mBadgeBackgroundRect.top && y < this.mBadgeBackgroundRect.bottom && this.mBadgeText != null) {
                    initRowBadgeCenter();
                    this.mDragging = true;
                    updateListener(1);
                    this.mDefaultRadius = DensityUtils.dp2px(getContext(), 7.0f);
                    getParent().requestDisallowInterceptTouchEvent(true);
                    screenFromWindow(true);
                    this.mDragCenter.x = motionEvent.getRawX();
                    this.mDragCenter.y = motionEvent.getRawY();
                }
                break;
            case 1:
            case 3:
            case 6:
                if (motionEvent.getPointerId(motionEvent.getActionIndex()) == 0 && this.mDragging) {
                    this.mDragging = false;
                    onPointerUp();
                }
                break;
            case 2:
                if (this.mDragging) {
                    this.mDragCenter.x = motionEvent.getRawX();
                    this.mDragCenter.y = motionEvent.getRawY();
                    invalidate();
                }
                break;
        }
        return this.mDragging || super.onTouchEvent(motionEvent);
    }

    private void onPointerUp() {
        if (this.mDragOutOfRange) {
            animateHide(this.mDragCenter);
            updateListener(5);
        } else {
            reset();
            updateListener(4);
        }
    }

    protected Bitmap createBadgeBitmap() {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(((int) this.mBadgeBackgroundRect.width()) + DensityUtils.dp2px(getContext(), 3.0f), ((int) this.mBadgeBackgroundRect.height()) + DensityUtils.dp2px(getContext(), 3.0f), Bitmap.Config.ARGB_8888);
        drawBadge(new Canvas(bitmapCreateBitmap), new PointF(r1.getWidth() / 2.0f, r1.getHeight() / 2.0f), getBadgeCircleRadius());
        return bitmapCreateBitmap;
    }

    protected void screenFromWindow(boolean z) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (z) {
            this.mActivityRoot.addView(this, new FrameLayout.LayoutParams(-1, -1));
        } else {
            bindTarget(this.mTargetView);
        }
    }

    private void showShadowImpl(boolean z) {
        int iDp2px = DensityUtils.dp2px(getContext(), 1.0f);
        int iDp2px2 = DensityUtils.dp2px(getContext(), 1.5f);
        switch (this.mDragQuadrant) {
            case 1:
                iDp2px = DensityUtils.dp2px(getContext(), 1.0f);
                iDp2px2 = DensityUtils.dp2px(getContext(), -1.5f);
                break;
            case 2:
                iDp2px = DensityUtils.dp2px(getContext(), -1.0f);
                iDp2px2 = DensityUtils.dp2px(getContext(), -1.5f);
                break;
            case 3:
                iDp2px = DensityUtils.dp2px(getContext(), -1.0f);
                iDp2px2 = DensityUtils.dp2px(getContext(), 1.5f);
                break;
            case 4:
                iDp2px = DensityUtils.dp2px(getContext(), 1.0f);
                iDp2px2 = DensityUtils.dp2px(getContext(), 1.5f);
                break;
        }
        this.mBadgeBackgroundPaint.setShadowLayer(z ? DensityUtils.dp2px(getContext(), 2.0f) : 0.0f, iDp2px, iDp2px2, 855638016);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        BadgeAnimator badgeAnimator = this.mAnimator;
        if (badgeAnimator != null && badgeAnimator.isRunning()) {
            this.mAnimator.draw(canvas);
            return;
        }
        if (this.mBadgeText != null) {
            initPaints();
            float badgeCircleRadius = getBadgeCircleRadius();
            float pointDistance = this.mDefaultRadius * (1.0f - (MathUtils.getPointDistance(this.mRowBadgeCenter, this.mDragCenter) / this.mFinalDragDistance));
            if (this.mDraggable && this.mDragging) {
                this.mDragQuadrant = MathUtils.getQuadrant(this.mDragCenter, this.mRowBadgeCenter);
                showShadowImpl(this.mShowShadow);
                boolean z = pointDistance < ((float) DensityUtils.dp2px(getContext(), 1.5f));
                this.mDragOutOfRange = z;
                if (z) {
                    updateListener(3);
                    drawBadge(canvas, this.mDragCenter, badgeCircleRadius);
                    return;
                } else {
                    updateListener(2);
                    drawDragging(canvas, pointDistance, badgeCircleRadius);
                    drawBadge(canvas, this.mDragCenter, badgeCircleRadius);
                    return;
                }
            }
            findBadgeCenter();
            drawBadge(canvas, this.mBadgeCenter, badgeCircleRadius);
        }
    }

    private void initPaints() {
        showShadowImpl(this.mShowShadow);
        this.mBadgeBackgroundPaint.setColor(this.mColorBackground);
        this.mBadgeBackgroundBorderPaint.setColor(this.mColorBackgroundBorder);
        this.mBadgeBackgroundBorderPaint.setStrokeWidth(this.mBackgroundBorderWidth);
        this.mBadgeTextPaint.setColor(this.mColorBadgeText);
        this.mBadgeTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void drawDragging(Canvas canvas, float f, float f2) {
        float f3;
        float f4;
        float f5 = this.mDragCenter.y - this.mRowBadgeCenter.y;
        float f6 = this.mDragCenter.x - this.mRowBadgeCenter.x;
        this.mInnerTangentPoints.clear();
        if (f6 != 0.0f) {
            double d2 = (-1.0d) / ((double) (f5 / f6));
            MathUtils.getInnerTangentPoints(this.mDragCenter, f2, Double.valueOf(d2), this.mInnerTangentPoints);
            MathUtils.getInnerTangentPoints(this.mRowBadgeCenter, f, Double.valueOf(d2), this.mInnerTangentPoints);
        } else {
            MathUtils.getInnerTangentPoints(this.mDragCenter, f2, Double.valueOf(0.0d), this.mInnerTangentPoints);
            MathUtils.getInnerTangentPoints(this.mRowBadgeCenter, f, Double.valueOf(0.0d), this.mInnerTangentPoints);
        }
        this.mDragPath.reset();
        Path path = this.mDragPath;
        float f7 = this.mRowBadgeCenter.x;
        float f8 = this.mRowBadgeCenter.y;
        int i = this.mDragQuadrant;
        path.addCircle(f7, f8, f, (i == 1 || i == 2) ? Path.Direction.CCW : Path.Direction.CW);
        this.mControlPoint.x = (this.mRowBadgeCenter.x + this.mDragCenter.x) / 2.0f;
        this.mControlPoint.y = (this.mRowBadgeCenter.y + this.mDragCenter.y) / 2.0f;
        this.mDragPath.moveTo(this.mInnerTangentPoints.get(2).x, this.mInnerTangentPoints.get(2).y);
        this.mDragPath.quadTo(this.mControlPoint.x, this.mControlPoint.y, this.mInnerTangentPoints.get(0).x, this.mInnerTangentPoints.get(0).y);
        this.mDragPath.lineTo(this.mInnerTangentPoints.get(1).x, this.mInnerTangentPoints.get(1).y);
        this.mDragPath.quadTo(this.mControlPoint.x, this.mControlPoint.y, this.mInnerTangentPoints.get(3).x, this.mInnerTangentPoints.get(3).y);
        this.mDragPath.lineTo(this.mInnerTangentPoints.get(2).x, this.mInnerTangentPoints.get(2).y);
        this.mDragPath.close();
        canvas.drawPath(this.mDragPath, this.mBadgeBackgroundPaint);
        if (this.mColorBackgroundBorder == 0 || this.mBackgroundBorderWidth <= 0.0f) {
            return;
        }
        this.mDragPath.reset();
        this.mDragPath.moveTo(this.mInnerTangentPoints.get(2).x, this.mInnerTangentPoints.get(2).y);
        this.mDragPath.quadTo(this.mControlPoint.x, this.mControlPoint.y, this.mInnerTangentPoints.get(0).x, this.mInnerTangentPoints.get(0).y);
        this.mDragPath.moveTo(this.mInnerTangentPoints.get(1).x, this.mInnerTangentPoints.get(1).y);
        this.mDragPath.quadTo(this.mControlPoint.x, this.mControlPoint.y, this.mInnerTangentPoints.get(3).x, this.mInnerTangentPoints.get(3).y);
        int i2 = this.mDragQuadrant;
        if (i2 == 1 || i2 == 2) {
            f3 = this.mInnerTangentPoints.get(2).x - this.mRowBadgeCenter.x;
            f4 = this.mRowBadgeCenter.y - this.mInnerTangentPoints.get(2).y;
        } else {
            f3 = this.mInnerTangentPoints.get(3).x - this.mRowBadgeCenter.x;
            f4 = this.mRowBadgeCenter.y - this.mInnerTangentPoints.get(3).y;
        }
        double dAtan = Math.atan(f4 / f3);
        int i3 = this.mDragQuadrant;
        float fRadianToAngle = 360.0f - ((float) MathUtils.radianToAngle(MathUtils.getTanRadian(dAtan, i3 + (-1) == 0 ? 4 : i3 - 1)));
        if (Build.VERSION.SDK_INT >= 21) {
            this.mDragPath.addArc(this.mRowBadgeCenter.x - f, this.mRowBadgeCenter.y - f, this.mRowBadgeCenter.x + f, this.mRowBadgeCenter.y + f, fRadianToAngle, 180.0f);
        } else {
            this.mDragPath.addArc(new RectF(this.mRowBadgeCenter.x - f, this.mRowBadgeCenter.y - f, this.mRowBadgeCenter.x + f, this.mRowBadgeCenter.y + f), fRadianToAngle, 180.0f);
        }
        canvas.drawPath(this.mDragPath, this.mBadgeBackgroundBorderPaint);
    }

    private void drawBadge(Canvas canvas, PointF pointF, float f) {
        if (pointF.x == -1000.0f && pointF.y == -1000.0f) {
            return;
        }
        if (this.mBadgeText.isEmpty() || this.mBadgeText.length() == 1) {
            float f2 = (int) f;
            this.mBadgeBackgroundRect.left = pointF.x - f2;
            this.mBadgeBackgroundRect.top = pointF.y - f2;
            this.mBadgeBackgroundRect.right = pointF.x + f2;
            this.mBadgeBackgroundRect.bottom = pointF.y + f2;
            if (this.mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawCircle(pointF.x, pointF.y, f, this.mBadgeBackgroundPaint);
                if (this.mColorBackgroundBorder != 0 && this.mBackgroundBorderWidth > 0.0f) {
                    canvas.drawCircle(pointF.x, pointF.y, f, this.mBadgeBackgroundBorderPaint);
                }
            }
        } else {
            this.mBadgeBackgroundRect.left = pointF.x - ((this.mBadgeTextRect.width() / 2.0f) + this.mBadgePadding);
            this.mBadgeBackgroundRect.top = pointF.y - ((this.mBadgeTextRect.height() / 2.0f) + (this.mBadgePadding * 0.5f));
            this.mBadgeBackgroundRect.right = pointF.x + (this.mBadgeTextRect.width() / 2.0f) + this.mBadgePadding;
            this.mBadgeBackgroundRect.bottom = pointF.y + (this.mBadgeTextRect.height() / 2.0f) + (this.mBadgePadding * 0.5f);
            float fHeight = this.mBadgeBackgroundRect.height() / 2.0f;
            if (this.mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawRoundRect(this.mBadgeBackgroundRect, fHeight, fHeight, this.mBadgeBackgroundPaint);
                if (this.mColorBackgroundBorder != 0 && this.mBackgroundBorderWidth > 0.0f) {
                    canvas.drawRoundRect(this.mBadgeBackgroundRect, fHeight, fHeight, this.mBadgeBackgroundBorderPaint);
                }
            }
        }
        if (this.mBadgeText.isEmpty()) {
            return;
        }
        canvas.drawText(this.mBadgeText, pointF.x, (((this.mBadgeBackgroundRect.bottom + this.mBadgeBackgroundRect.top) - this.mBadgeTextFontMetrics.bottom) - this.mBadgeTextFontMetrics.top) / 2.0f, this.mBadgeTextPaint);
    }

    private void drawBadgeBackground(Canvas canvas) {
        this.mBadgeBackgroundPaint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        int i = (int) this.mBadgeBackgroundRect.left;
        int i2 = (int) this.mBadgeBackgroundRect.top;
        int width = (int) this.mBadgeBackgroundRect.right;
        int height = (int) this.mBadgeBackgroundRect.bottom;
        if (this.mDrawableBackgroundClip) {
            width = this.mBitmapClip.getWidth() + i;
            height = this.mBitmapClip.getHeight() + i2;
            canvas.saveLayer(i, i2, width, height, null, 31);
        }
        this.mDrawableBackground.setBounds(i, i2, width, height);
        this.mDrawableBackground.draw(canvas);
        if (this.mDrawableBackgroundClip) {
            this.mBadgeBackgroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(this.mBitmapClip, i, i2, this.mBadgeBackgroundPaint);
            canvas.restore();
            this.mBadgeBackgroundPaint.setXfermode(null);
            if (this.mBadgeText.isEmpty() || this.mBadgeText.length() == 1) {
                canvas.drawCircle(this.mBadgeBackgroundRect.centerX(), this.mBadgeBackgroundRect.centerY(), this.mBadgeBackgroundRect.width() / 2.0f, this.mBadgeBackgroundBorderPaint);
                return;
            } else {
                RectF rectF = this.mBadgeBackgroundRect;
                canvas.drawRoundRect(rectF, rectF.height() / 2.0f, this.mBadgeBackgroundRect.height() / 2.0f, this.mBadgeBackgroundBorderPaint);
                return;
            }
        }
        canvas.drawRect(this.mBadgeBackgroundRect, this.mBadgeBackgroundBorderPaint);
    }

    private void createClipLayer() {
        if (this.mBadgeText != null && this.mDrawableBackgroundClip) {
            Bitmap bitmap = this.mBitmapClip;
            if (bitmap != null && !bitmap.isRecycled()) {
                this.mBitmapClip.recycle();
            }
            float badgeCircleRadius = getBadgeCircleRadius();
            if (this.mBadgeText.isEmpty() || this.mBadgeText.length() == 1) {
                int i = ((int) badgeCircleRadius) * 2;
                this.mBitmapClip = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_4444);
                new Canvas(this.mBitmapClip).drawCircle(r0.getWidth() / 2.0f, r0.getHeight() / 2.0f, r0.getWidth() / 2.0f, this.mBadgeBackgroundPaint);
                return;
            }
            this.mBitmapClip = Bitmap.createBitmap((int) (this.mBadgeTextRect.width() + (this.mBadgePadding * 2.0f)), (int) (this.mBadgeTextRect.height() + this.mBadgePadding), Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(this.mBitmapClip);
            if (Build.VERSION.SDK_INT >= 21) {
                canvas.drawRoundRect(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), canvas.getHeight() / 2.0f, canvas.getHeight() / 2.0f, this.mBadgeBackgroundPaint);
            } else {
                canvas.drawRoundRect(new RectF(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight()), canvas.getHeight() / 2.0f, canvas.getHeight() / 2.0f, this.mBadgeBackgroundPaint);
            }
        }
    }

    private float getBadgeCircleRadius() {
        if (this.mBadgeText.isEmpty()) {
            return this.mBadgePadding;
        }
        if (this.mBadgeText.length() != 1) {
            return this.mBadgeBackgroundRect.height() / 2.0f;
        }
        if (this.mBadgeTextRect.height() > this.mBadgeTextRect.width()) {
            return (this.mBadgeTextRect.height() / 2.0f) + (this.mBadgePadding * 0.5f);
        }
        return (this.mBadgeTextRect.width() / 2.0f) + (this.mBadgePadding * 0.5f);
    }

    private void findBadgeCenter() {
        float fMax = Math.max(this.mBadgeTextRect.height(), this.mBadgeTextRect.width());
        switch (this.mBadgeGravity) {
            case 17:
                PointF pointF = this.mBadgeCenter;
                pointF.x = this.mWidth / 2.0f;
                pointF.y = this.mHeight / 2.0f;
                break;
            case 49:
                PointF pointF2 = this.mBadgeCenter;
                pointF2.x = this.mWidth / 2.0f;
                pointF2.y = this.mGravityOffsetY + this.mBadgePadding + (this.mBadgeTextRect.height() / 2.0f);
                break;
            case 81:
                PointF pointF3 = this.mBadgeCenter;
                pointF3.x = this.mWidth / 2.0f;
                pointF3.y = this.mHeight - ((this.mGravityOffsetY + this.mBadgePadding) + (this.mBadgeTextRect.height() / 2.0f));
                break;
            case 8388627:
                PointF pointF4 = this.mBadgeCenter;
                pointF4.x = this.mGravityOffsetX + this.mBadgePadding + (fMax / 2.0f);
                pointF4.y = this.mHeight / 2.0f;
                break;
            case 8388629:
                PointF pointF5 = this.mBadgeCenter;
                pointF5.x = this.mWidth - ((this.mGravityOffsetX + this.mBadgePadding) + (fMax / 2.0f));
                pointF5.y = this.mHeight / 2.0f;
                break;
            case 8388659:
                PointF pointF6 = this.mBadgeCenter;
                float f = this.mGravityOffsetX;
                float f2 = this.mBadgePadding;
                pointF6.x = f + f2 + (fMax / 2.0f);
                pointF6.y = this.mGravityOffsetY + f2 + (this.mBadgeTextRect.height() / 2.0f);
                break;
            case 8388661:
                PointF pointF7 = this.mBadgeCenter;
                float f3 = this.mWidth;
                float f4 = this.mGravityOffsetX;
                float f5 = this.mBadgePadding;
                pointF7.x = f3 - ((f4 + f5) + (fMax / 2.0f));
                pointF7.y = this.mGravityOffsetY + f5 + (this.mBadgeTextRect.height() / 2.0f);
                break;
            case 8388691:
                PointF pointF8 = this.mBadgeCenter;
                float f6 = this.mGravityOffsetX;
                float f7 = this.mBadgePadding;
                pointF8.x = f6 + f7 + (fMax / 2.0f);
                pointF8.y = this.mHeight - ((this.mGravityOffsetY + f7) + (this.mBadgeTextRect.height() / 2.0f));
                break;
            case 8388693:
                PointF pointF9 = this.mBadgeCenter;
                float f8 = this.mWidth;
                float f9 = this.mGravityOffsetX;
                float f10 = this.mBadgePadding;
                pointF9.x = f8 - ((f9 + f10) + (fMax / 2.0f));
                pointF9.y = this.mHeight - ((this.mGravityOffsetY + f10) + (this.mBadgeTextRect.height() / 2.0f));
                break;
        }
        initRowBadgeCenter();
    }

    private void measureText() {
        RectF rectF = this.mBadgeTextRect;
        rectF.left = 0.0f;
        rectF.top = 0.0f;
        if (TextUtils.isEmpty(this.mBadgeText)) {
            RectF rectF2 = this.mBadgeTextRect;
            rectF2.right = 0.0f;
            rectF2.bottom = 0.0f;
        } else {
            this.mBadgeTextPaint.setTextSize(this.mBadgeTextSize);
            this.mBadgeTextRect.right = this.mBadgeTextPaint.measureText(this.mBadgeText);
            this.mBadgeTextFontMetrics = this.mBadgeTextPaint.getFontMetrics();
            this.mBadgeTextRect.bottom = this.mBadgeTextFontMetrics.descent - this.mBadgeTextFontMetrics.ascent;
        }
        createClipLayer();
    }

    private void initRowBadgeCenter() {
        getLocationOnScreen(new int[2]);
        this.mRowBadgeCenter.x = this.mBadgeCenter.x + r0[0];
        this.mRowBadgeCenter.y = this.mBadgeCenter.y + r0[1];
    }

    protected void animateHide(PointF pointF) {
        if (this.mBadgeText == null) {
            return;
        }
        BadgeAnimator badgeAnimator = this.mAnimator;
        if (badgeAnimator == null || !badgeAnimator.isRunning()) {
            screenFromWindow(true);
            this.mAnimator = new BadgeAnimator(createBadgeBitmap(), pointF, this);
            this.mAnimator.start();
            setBadgeNumber(0);
        }
    }

    public void reset() {
        PointF pointF = this.mDragCenter;
        pointF.x = -1000.0f;
        pointF.y = -1000.0f;
        this.mDragQuadrant = 4;
        screenFromWindow(false);
        getParent().requestDisallowInterceptTouchEvent(false);
        invalidate();
    }

    @Override // view.Badge
    public void hide(boolean z) {
        if (z && this.mActivityRoot != null) {
            initRowBadgeCenter();
            animateHide(this.mRowBadgeCenter);
        } else {
            setBadgeNumber(0);
        }
    }

    @Override // view.Badge
    public Badge setBadgeNumber(int i) {
        this.mBadgeNumber = i;
        int i2 = this.mBadgeNumber;
        if (i2 < 0) {
            this.mBadgeText = "";
        } else if (i2 > 99) {
            this.mBadgeText = this.mExact ? String.valueOf(i2) : "99+";
        } else if (i2 > 0) {
            this.mBadgeText = String.valueOf(i2);
        } else {
            this.mBadgeText = null;
        }
        measureText();
        invalidate();
        return this;
    }

    @Override // view.Badge
    public int getBadgeNumber() {
        return this.mBadgeNumber;
    }

    @Override // view.Badge
    public Badge setBadgeText(String str) {
        this.mBadgeText = str;
        this.mBadgeNumber = 1;
        measureText();
        invalidate();
        return this;
    }

    @Override // view.Badge
    public String getBadgeText() {
        return this.mBadgeText;
    }

    @Override // view.Badge
    public Badge setExactMode(boolean z) {
        this.mExact = z;
        int i = this.mBadgeNumber;
        if (i > 99) {
            setBadgeNumber(i);
        }
        return this;
    }

    @Override // view.Badge
    public boolean isExactMode() {
        return this.mExact;
    }

    @Override // view.Badge
    public Badge setShowShadow(boolean z) {
        this.mShowShadow = z;
        invalidate();
        return this;
    }

    @Override // view.Badge
    public boolean isShowShadow() {
        return this.mShowShadow;
    }

    @Override // view.Badge
    public Badge setBadgeBackgroundColor(int i) {
        this.mColorBackground = i;
        if (this.mColorBackground == 0) {
            this.mBadgeTextPaint.setXfermode(null);
        } else {
            this.mBadgeTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }
        invalidate();
        return this;
    }

    @Override // view.Badge
    public Badge stroke(int i, float f, boolean z) {
        this.mColorBackgroundBorder = i;
        if (z) {
            f = DensityUtils.dp2px(getContext(), f);
        }
        this.mBackgroundBorderWidth = f;
        invalidate();
        return this;
    }

    @Override // view.Badge
    public int getBadgeBackgroundColor() {
        return this.mColorBackground;
    }

    @Override // view.Badge
    public Badge setBadgeBackground(Drawable drawable) {
        return setBadgeBackground(drawable, false);
    }

    @Override // view.Badge
    public Badge setBadgeBackground(Drawable drawable, boolean z) {
        this.mDrawableBackgroundClip = z;
        this.mDrawableBackground = drawable;
        createClipLayer();
        invalidate();
        return this;
    }

    @Override // view.Badge
    public Drawable getBadgeBackground() {
        return this.mDrawableBackground;
    }

    @Override // view.Badge
    public Badge setBadgeTextColor(int i) {
        this.mColorBadgeText = i;
        invalidate();
        return this;
    }

    @Override // view.Badge
    public int getBadgeTextColor() {
        return this.mColorBadgeText;
    }

    @Override // view.Badge
    public Badge setBadgeTextSize(float f, boolean z) {
        if (z) {
            f = DensityUtils.dp2px(getContext(), f);
        }
        this.mBadgeTextSize = f;
        measureText();
        invalidate();
        return this;
    }

    @Override // view.Badge
    public float getBadgeTextSize(boolean z) {
        return z ? DensityUtils.px2dp(getContext(), this.mBadgeTextSize) : this.mBadgeTextSize;
    }

    @Override // view.Badge
    public Badge setBadgePadding(float f, boolean z) {
        if (z) {
            f = DensityUtils.dp2px(getContext(), f);
        }
        this.mBadgePadding = f;
        createClipLayer();
        invalidate();
        return this;
    }

    @Override // view.Badge
    public float getBadgePadding(boolean z) {
        return z ? DensityUtils.px2dp(getContext(), this.mBadgePadding) : this.mBadgePadding;
    }

    @Override // view.Badge
    public boolean isDraggable() {
        return this.mDraggable;
    }

    @Override // view.Badge
    public Badge setBadgeGravity(int i) {
        if (i == 8388659 || i == 8388661 || i == 8388691 || i == 8388693 || i == 17 || i == 49 || i == 81 || i == 8388627 || i == 8388629) {
            this.mBadgeGravity = i;
            invalidate();
            return this;
        }
        throw new IllegalStateException("only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP , Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ,Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END");
    }

    @Override // view.Badge
    public int getBadgeGravity() {
        return this.mBadgeGravity;
    }

    @Override // view.Badge
    public Badge setGravityOffset(float f, boolean z) {
        return setGravityOffset(f, f, z);
    }

    @Override // view.Badge
    public Badge setGravityOffset(float f, float f2, boolean z) {
        if (z) {
            f = DensityUtils.dp2px(getContext(), f);
        }
        this.mGravityOffsetX = f;
        if (z) {
            f2 = DensityUtils.dp2px(getContext(), f2);
        }
        this.mGravityOffsetY = f2;
        invalidate();
        return this;
    }

    @Override // view.Badge
    public float getGravityOffsetX(boolean z) {
        return z ? DensityUtils.px2dp(getContext(), this.mGravityOffsetX) : this.mGravityOffsetX;
    }

    @Override // view.Badge
    public float getGravityOffsetY(boolean z) {
        return z ? DensityUtils.px2dp(getContext(), this.mGravityOffsetY) : this.mGravityOffsetY;
    }

    private void updateListener(int i) {
        Badge.OnDragStateChangedListener onDragStateChangedListener = this.mDragStateChangedListener;
        if (onDragStateChangedListener != null) {
            onDragStateChangedListener.onDragStateChanged(i, this, this.mTargetView);
        }
    }

    @Override // view.Badge
    public Badge setOnDragStateChangedListener(Badge.OnDragStateChangedListener onDragStateChangedListener) {
        this.mDraggable = onDragStateChangedListener != null;
        this.mDragStateChangedListener = onDragStateChangedListener;
        return this;
    }

    @Override // view.Badge
    public PointF getDragCenter() {
        if (this.mDraggable && this.mDragging) {
            return this.mDragCenter;
        }
        return null;
    }

    private class BadgeContainer extends ViewGroup {
        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
            if (getParent() instanceof RelativeLayout) {
                return;
            }
            super.dispatchRestoreInstanceState(sparseArray);
        }

        public BadgeContainer(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            for (int i5 = 0; i5 < getChildCount(); i5++) {
                View childAt = getChildAt(i5);
                childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            }
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            View view2 = null;
            View view3 = null;
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (childAt instanceof BadgeView) {
                    view3 = childAt;
                } else {
                    view2 = childAt;
                }
            }
            if (view2 == null) {
                super.onMeasure(i, i2);
                return;
            }
            view2.measure(i, i2);
            if (view3 != null) {
                view3.measure(View.MeasureSpec.makeMeasureSpec(view2.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(view2.getMeasuredHeight(), 1073741824));
            }
            setMeasuredDimension(view2.getMeasuredWidth(), view2.getMeasuredHeight());
        }
    }

    @Override // view.Badge
    public void hideView() {
        BadgeContainer badgeContainer = this.badgeContainer;
        if (badgeContainer == null || ((ViewGroup) badgeContainer.getParent()) == null) {
            return;
        }
        ((ViewGroup) this.badgeContainer.getParent()).removeView(this.badgeContainer);
    }
}
