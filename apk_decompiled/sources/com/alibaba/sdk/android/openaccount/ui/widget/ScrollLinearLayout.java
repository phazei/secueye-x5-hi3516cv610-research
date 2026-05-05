package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;

/* JADX INFO: loaded from: classes.dex */
public class ScrollLinearLayout extends LinearLayout {
    private Scroller mScroller;

    public ScrollLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mScroller = new Scroller(context);
    }

    public void smoothScrollTo(int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        this.mScroller.startScroll(i, i2, i5, i4 - i2, Math.abs(i5) * 2);
        invalidate();
    }

    public void stopScroll() {
        if (this.mScroller.isFinished()) {
            return;
        }
        this.mScroller.abortAnimation();
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            if (AliSDKLogger.isDebugEnabled()) {
                AliSDKLogger.d(OpenAccountUIConstants.LOG_TAG, "computeScroll scrollX = " + this.mScroller.getCurrX());
            }
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    public boolean isScrolling() {
        return !this.mScroller.isFinished();
    }
}
