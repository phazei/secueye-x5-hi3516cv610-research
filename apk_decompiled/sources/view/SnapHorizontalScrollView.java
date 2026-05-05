package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;

/* JADX INFO: loaded from: classes5.dex */
public class SnapHorizontalScrollView extends HorizontalScrollView {
    private int currentPage;
    private GestureDetector gestureDetector;
    private int pageWidth;
    private Scroller scroller;
    private VelocityTracker velocityTracker;

    public SnapHorizontalScrollView(Context context) {
        super(context);
        this.currentPage = 0;
        this.pageWidth = 0;
        init(context);
    }

    public SnapHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.currentPage = 0;
        this.pageWidth = 0;
        init(context);
    }

    private void init(Context context) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.scroller = new Scroller(context);
        setOverScrollMode(2);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (getChildCount() > 0) {
            this.pageWidth = getWidth();
            getChildAt(0).getLayoutParams().width = this.pageWidth * getChildCount();
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.gestureDetector.onTouchEvent(motionEvent);
        if (motionEvent.getAction() == 1) {
            handleActionUp();
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public void computeScroll() {
        if (this.scroller.computeScrollOffset()) {
            scrollTo(this.scroller.getCurrX(), 0);
            invalidate();
        }
    }

    private void handleActionUp() {
        int iRound;
        this.velocityTracker.computeCurrentVelocity(1000);
        float xVelocity = this.velocityTracker.getXVelocity();
        int scrollX = getScrollX();
        if (Math.abs(xVelocity) > 500.0f) {
            iRound = xVelocity > 0.0f ? this.currentPage - 1 : this.currentPage + 1;
        } else {
            iRound = (int) Math.round(((double) scrollX) / ((double) this.pageWidth));
        }
        int iMax = Math.max(0, Math.min(iRound, getChildCount() - 1));
        smoothScrollToPage(iMax);
        this.currentPage = iMax;
        this.velocityTracker.recycle();
        this.velocityTracker = null;
    }

    public void smoothScrollToPage(int i) {
        int scrollX = (i * this.pageWidth) - getScrollX();
        this.scroller.startScroll(getScrollX(), 0, scrollX, 0, Math.min(500, Math.abs(scrollX) * 2));
        invalidate();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        private GestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            if (!SnapHorizontalScrollView.this.scroller.isFinished()) {
                SnapHorizontalScrollView.this.scroller.abortAnimation();
            }
            if (SnapHorizontalScrollView.this.velocityTracker != null) {
                SnapHorizontalScrollView.this.velocityTracker.clear();
            } else {
                SnapHorizontalScrollView.this.velocityTracker = VelocityTracker.obtain();
            }
            SnapHorizontalScrollView.this.velocityTracker.addMovement(motionEvent);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            SnapHorizontalScrollView.this.velocityTracker.addMovement(motionEvent2);
            return false;
        }
    }
}
