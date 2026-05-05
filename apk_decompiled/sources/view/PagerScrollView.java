package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/* JADX INFO: loaded from: classes5.dex */
public class PagerScrollView extends HorizontalScrollView {
    private int currentPage;
    private GestureDetector gestureDetector;
    private int screenWidth;

    public PagerScrollView(Context context) {
        super(context);
        this.currentPage = 0;
        init(context);
    }

    public PagerScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.currentPage = 0;
        init(context);
    }

    private void init(Context context) {
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        setOverScrollMode(2);
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.gestureDetector.onTouchEvent(motionEvent);
        if (motionEvent.getAction() == 1) {
            snapToPage();
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    private void snapToPage() {
        int scrollX = getScrollX();
        int iCalculateTargetPage = calculateTargetPage(scrollX, Math.round(scrollX / this.screenWidth));
        smoothScrollTo(this.screenWidth * iCalculateTargetPage, 0);
        this.currentPage = iCalculateTargetPage;
    }

    private int calculateTargetPage(int i, int i2) {
        int i3 = this.screenWidth;
        int i4 = i - (i2 * i3);
        return i4 > i3 / 2 ? i2 + 1 : i4 < (-i3) / 2 ? i2 - 1 : i2;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private GestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (Math.abs(f) <= 1000.0f) {
                return false;
            }
            if (f > 0.0f) {
                PagerScrollView pagerScrollView = PagerScrollView.this;
                pagerScrollView.currentPage = Math.max(0, pagerScrollView.currentPage - 1);
            } else {
                PagerScrollView pagerScrollView2 = PagerScrollView.this;
                pagerScrollView2.currentPage = Math.min(pagerScrollView2.getChildCount() - 1, PagerScrollView.this.currentPage + 1);
            }
            PagerScrollView pagerScrollView3 = PagerScrollView.this;
            pagerScrollView3.smoothScrollTo(pagerScrollView3.currentPage * PagerScrollView.this.screenWidth, 0);
            return true;
        }
    }
}
