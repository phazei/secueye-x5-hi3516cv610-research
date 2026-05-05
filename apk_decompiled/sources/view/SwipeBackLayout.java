package view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.imid.swipebacklayout.lib.ViewDragHelper;

/* JADX INFO: loaded from: classes5.dex */
public class SwipeBackLayout extends FrameLayout {
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;
    public static final int EDGE_ALL = 11;
    public static final int EDGE_BOTTOM = 8;
    private static final int[] EDGE_FLAGS = {1, 2, 8, 11};
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int FULL_ALPHA = 255;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int OVERSCROLL_DISTANCE = 10;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private Activity mActivity;
    private int mContentLeft;
    private int mContentTop;
    private View mContentView;
    private ViewDragHelper mDragHelper;
    private int mEdgeFlag;
    private boolean mEnable;
    private boolean mInLayout;
    private List<SwipeListener> mListeners;
    private int mScrimColor;
    private float mScrimOpacity;
    private float mScrollPercent;
    private float mScrollThreshold;
    private Drawable mShadowBottom;
    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private Rect mTmpRect;
    private int mTrackingEdge;

    public interface SwipeListener {
        void onEdgeTouch(int i);

        void onScrollOverThreshold();

        void onScrollStateChange(int i, float f);
    }

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.SwipeBackLayoutStyle);
    }

    public SwipeBackLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;
        this.mEnable = true;
        this.mScrimColor = -1728053248;
        this.mTmpRect = new Rect();
        this.mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SwipeBackLayout, i, R.style.SwipeBackLayout);
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(1, -1);
        if (dimensionPixelSize > 0) {
            setEdgeSize(dimensionPixelSize);
        }
        setEdgeTrackingEnabled(EDGE_FLAGS[typedArrayObtainStyledAttributes.getInt(0, 0)]);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(3, R.drawable.shadow_left);
        int resourceId2 = typedArrayObtainStyledAttributes.getResourceId(4, R.drawable.shadow_right);
        int resourceId3 = typedArrayObtainStyledAttributes.getResourceId(2, R.drawable.shadow_bottom);
        setShadow(resourceId, 1);
        setShadow(resourceId2, 2);
        setShadow(resourceId3, 8);
        typedArrayObtainStyledAttributes.recycle();
        float f = getResources().getDisplayMetrics().density * 400.0f;
        this.mDragHelper.setMinVelocity(f);
        this.mDragHelper.setMaxVelocity(f * 2.0f);
    }

    public void setSensitivity(Context context, float f) {
        this.mDragHelper.setSensitivity(context, f);
    }

    private void setContentView(View view2) {
        this.mContentView = view2;
    }

    public void setEnableGesture(boolean z) {
        this.mEnable = z;
    }

    public void setEdgeTrackingEnabled(int i) {
        this.mEdgeFlag = i;
        this.mDragHelper.setEdgeTrackingEnabled(this.mEdgeFlag);
    }

    public void setScrimColor(int i) {
        this.mScrimColor = i;
        invalidate();
    }

    public void setEdgeSize(int i) {
        this.mDragHelper.setEdgeSize(i);
    }

    @Deprecated
    public void setSwipeListener(SwipeListener swipeListener) {
        addSwipeListener(swipeListener);
    }

    public void addSwipeListener(SwipeListener swipeListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(swipeListener);
    }

    public void removeSwipeListener(SwipeListener swipeListener) {
        List<SwipeListener> list = this.mListeners;
        if (list == null) {
            return;
        }
        list.remove(swipeListener);
    }

    public void setScrollThresHold(float f) {
        if (f >= 1.0f || f <= 0.0f) {
            throw new IllegalArgumentException("Threshold value should be between 0 and 1.0");
        }
        this.mScrollThreshold = f;
    }

    public void setShadow(Drawable drawable, int i) {
        if ((i & 1) != 0) {
            this.mShadowLeft = drawable;
        } else if ((i & 2) != 0) {
            this.mShadowRight = drawable;
        } else if ((i & 8) != 0) {
            this.mShadowBottom = drawable;
        }
        invalidate();
    }

    public void setShadow(int i, int i2) {
        setShadow(getResources().getDrawable(i), i2);
    }

    public void scrollToFinishActivity() {
        int intrinsicWidth;
        int width = this.mContentView.getWidth();
        int height = this.mContentView.getHeight();
        int i = this.mEdgeFlag;
        int i2 = 0;
        if ((i & 1) != 0) {
            intrinsicWidth = width + this.mShadowLeft.getIntrinsicWidth() + 10;
            this.mTrackingEdge = 1;
        } else if ((i & 2) != 0) {
            intrinsicWidth = ((-width) - this.mShadowRight.getIntrinsicWidth()) - 10;
            this.mTrackingEdge = 2;
        } else if ((i & 8) != 0) {
            int intrinsicHeight = ((-height) - this.mShadowBottom.getIntrinsicHeight()) - 10;
            this.mTrackingEdge = 8;
            intrinsicWidth = 0;
            i2 = intrinsicHeight;
        } else {
            intrinsicWidth = 0;
        }
        this.mDragHelper.smoothSlideViewTo(this.mContentView, intrinsicWidth, i2);
        invalidate();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mEnable) {
            return false;
        }
        try {
            return this.mDragHelper.shouldInterceptTouchEvent(motionEvent);
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mEnable) {
            return false;
        }
        try {
            this.mDragHelper.processTouchEvent(motionEvent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mInLayout = true;
        View view2 = this.mContentView;
        if (view2 != null) {
            int i5 = this.mContentLeft;
            view2.layout(i5, this.mContentTop, view2.getMeasuredWidth() + i5, this.mContentTop + this.mContentView.getMeasuredHeight());
            this.mInLayout = false;
        }
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.mInLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view2, long j) {
        boolean z = view2 == this.mContentView;
        boolean zDrawChild = super.drawChild(canvas, view2, j);
        if (this.mScrimOpacity > 0.0f && z && this.mDragHelper.getViewDragState() != 0) {
            drawShadow(canvas, view2);
            drawScrim(canvas, view2);
        }
        return zDrawChild;
    }

    private void drawScrim(Canvas canvas, View view2) {
        int i = (this.mScrimColor & 16777215) | (((int) ((((-16777216) & r0) >>> 24) * this.mScrimOpacity)) << 24);
        int i2 = this.mTrackingEdge;
        if ((i2 & 1) != 0) {
            canvas.clipRect(0, 0, view2.getLeft(), getHeight());
        } else if ((i2 & 2) != 0) {
            canvas.clipRect(view2.getRight(), 0, getRight(), getHeight());
        } else if ((i2 & 8) != 0) {
            canvas.clipRect(view2.getLeft(), view2.getBottom(), getRight(), getHeight());
        }
        canvas.drawColor(i);
    }

    private void drawShadow(Canvas canvas, View view2) {
        Rect rect = this.mTmpRect;
        view2.getHitRect(rect);
        if ((this.mEdgeFlag & 1) != 0) {
            this.mShadowLeft.setBounds(rect.left - this.mShadowLeft.getIntrinsicWidth(), rect.top, rect.left, rect.bottom);
            this.mShadowLeft.setAlpha((int) (this.mScrimOpacity * 255.0f));
            this.mShadowLeft.draw(canvas);
        }
        if ((this.mEdgeFlag & 2) != 0) {
            this.mShadowRight.setBounds(rect.right, rect.top, rect.right + this.mShadowRight.getIntrinsicWidth(), rect.bottom);
            this.mShadowRight.setAlpha((int) (this.mScrimOpacity * 255.0f));
            this.mShadowRight.draw(canvas);
        }
        if ((this.mEdgeFlag & 8) != 0) {
            this.mShadowBottom.setBounds(rect.left, rect.bottom, rect.right, rect.bottom + this.mShadowBottom.getIntrinsicHeight());
            this.mShadowBottom.setAlpha((int) (this.mScrimOpacity * 255.0f));
            this.mShadowBottom.draw(canvas);
        }
    }

    public void attachToActivity(Activity activity2) {
        this.mActivity = activity2;
        TypedArray typedArrayObtainStyledAttributes = activity2.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(0, 0);
        typedArrayObtainStyledAttributes.recycle();
        ViewGroup viewGroup = (ViewGroup) activity2.getWindow().getDecorView();
        ViewGroup viewGroup2 = (ViewGroup) viewGroup.getChildAt(0);
        viewGroup2.setBackgroundResource(resourceId);
        viewGroup.removeView(viewGroup2);
        addView(viewGroup2);
        setContentView(viewGroup2);
        viewGroup.addView(this);
    }

    @Override // android.view.View
    public void computeScroll() {
        this.mScrimOpacity = 1.0f - this.mScrollPercent;
        if (this.mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        private boolean mIsScrollOverValid;

        private ViewDragCallback() {
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public boolean tryCaptureView(View view2, int i) {
            boolean zIsEdgeTouched = SwipeBackLayout.this.mDragHelper.isEdgeTouched(SwipeBackLayout.this.mEdgeFlag, i);
            boolean zCheckTouchSlop = true;
            if (zIsEdgeTouched) {
                if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(1, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 1;
                } else if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(2, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 2;
                } else if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(8, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 8;
                }
                if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners.isEmpty()) {
                    Iterator it = SwipeBackLayout.this.mListeners.iterator();
                    while (it.hasNext()) {
                        ((SwipeListener) it.next()).onEdgeTouch(SwipeBackLayout.this.mTrackingEdge);
                    }
                }
                this.mIsScrollOverValid = true;
            }
            if (SwipeBackLayout.this.mEdgeFlag == 1 || SwipeBackLayout.this.mEdgeFlag == 2) {
                zCheckTouchSlop = true ^ SwipeBackLayout.this.mDragHelper.checkTouchSlop(2, i);
            } else if (SwipeBackLayout.this.mEdgeFlag != 8) {
                if (SwipeBackLayout.this.mEdgeFlag != 11) {
                    zCheckTouchSlop = false;
                }
            } else {
                zCheckTouchSlop = true ^ SwipeBackLayout.this.mDragHelper.checkTouchSlop(1, i);
            }
            return zIsEdgeTouched & zCheckTouchSlop;
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int getViewHorizontalDragRange(View view2) {
            return SwipeBackLayout.this.mEdgeFlag & 3;
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int getViewVerticalDragRange(View view2) {
            return SwipeBackLayout.this.mEdgeFlag & 8;
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public void onViewPositionChanged(View view2, int i, int i2, int i3, int i4) {
            super.onViewPositionChanged(view2, i, i2, i3, i4);
            if ((SwipeBackLayout.this.mTrackingEdge & 1) == 0) {
                if ((SwipeBackLayout.this.mTrackingEdge & 2) == 0) {
                    if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                        SwipeBackLayout.this.mScrollPercent = Math.abs(i2 / (r3.mContentView.getHeight() + SwipeBackLayout.this.mShadowBottom.getIntrinsicHeight()));
                    }
                } else {
                    SwipeBackLayout.this.mScrollPercent = Math.abs(i / (r3.mContentView.getWidth() + SwipeBackLayout.this.mShadowRight.getIntrinsicWidth()));
                }
            } else {
                SwipeBackLayout.this.mScrollPercent = Math.abs(i / (r3.mContentView.getWidth() + SwipeBackLayout.this.mShadowLeft.getIntrinsicWidth()));
            }
            SwipeBackLayout.this.mContentLeft = i;
            SwipeBackLayout.this.mContentTop = i2;
            SwipeBackLayout.this.invalidate();
            if (SwipeBackLayout.this.mScrollPercent < SwipeBackLayout.this.mScrollThreshold && !this.mIsScrollOverValid) {
                this.mIsScrollOverValid = true;
            }
            if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners.isEmpty() && SwipeBackLayout.this.mDragHelper.getViewDragState() == 1 && SwipeBackLayout.this.mScrollPercent >= SwipeBackLayout.this.mScrollThreshold && this.mIsScrollOverValid) {
                this.mIsScrollOverValid = false;
                Iterator it = SwipeBackLayout.this.mListeners.iterator();
                while (it.hasNext()) {
                    ((SwipeListener) it.next()).onScrollOverThreshold();
                }
            }
            if (SwipeBackLayout.this.mScrollPercent < 1.0f || SwipeBackLayout.this.mActivity.isFinishing()) {
                return;
            }
            SwipeBackLayout.this.mActivity.finish();
            SwipeBackLayout.this.mActivity.overridePendingTransition(0, 0);
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public void onViewReleased(View view2, float f, float f2) {
            int i;
            int width = view2.getWidth();
            int height = view2.getHeight();
            int intrinsicWidth = 0;
            if ((SwipeBackLayout.this.mTrackingEdge & 1) != 0) {
                i = 0;
                intrinsicWidth = (f > 0.0f || (f == 0.0f && SwipeBackLayout.this.mScrollPercent > SwipeBackLayout.this.mScrollThreshold)) ? width + SwipeBackLayout.this.mShadowLeft.getIntrinsicWidth() + 10 : 0;
            } else if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                intrinsicWidth = (f < 0.0f || (f == 0.0f && SwipeBackLayout.this.mScrollPercent > SwipeBackLayout.this.mScrollThreshold)) ? -(width + SwipeBackLayout.this.mShadowLeft.getIntrinsicWidth() + 10) : 0;
                i = 0;
            } else if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                i = (f2 < 0.0f || (f2 == 0.0f && SwipeBackLayout.this.mScrollPercent > SwipeBackLayout.this.mScrollThreshold)) ? -(height + SwipeBackLayout.this.mShadowBottom.getIntrinsicHeight() + 10) : 0;
            } else {
                i = 0;
            }
            SwipeBackLayout.this.mDragHelper.settleCapturedViewAt(intrinsicWidth, i);
            SwipeBackLayout.this.invalidate();
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View view2, int i, int i2) {
            if ((SwipeBackLayout.this.mTrackingEdge & 1) == 0) {
                if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                    return Math.min(0, Math.max(i, -view2.getWidth()));
                }
                return 0;
            }
            return Math.min(view2.getWidth(), Math.max(i, 0));
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int clampViewPositionVertical(View view2, int i, int i2) {
            if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                return Math.min(0, Math.max(i, -view2.getHeight()));
            }
            return 0;
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public void onViewDragStateChanged(int i) {
            super.onViewDragStateChanged(i);
            if (SwipeBackLayout.this.mListeners == null || SwipeBackLayout.this.mListeners.isEmpty()) {
                return;
            }
            Iterator it = SwipeBackLayout.this.mListeners.iterator();
            while (it.hasNext()) {
                ((SwipeListener) it.next()).onScrollStateChange(i, SwipeBackLayout.this.mScrollPercent);
            }
        }
    }
}
