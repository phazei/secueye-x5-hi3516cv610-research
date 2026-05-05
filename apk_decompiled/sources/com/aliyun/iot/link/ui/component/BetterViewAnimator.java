package com.aliyun.iot.link.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewAnimator;

/* JADX INFO: loaded from: classes2.dex */
public class BetterViewAnimator extends ViewAnimator {
    public BetterViewAnimator(Context context) {
        super(context);
    }

    public BetterViewAnimator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setDisplayedChildId(int i) {
        if (getDisplayedChildId() == i) {
            return;
        }
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            if (getChildAt(i2).getId() == i) {
                setDisplayedChild(i2);
                return;
            }
        }
        new IllegalArgumentException("No view with ID " + getResources().getResourceEntryName(i)).printStackTrace();
    }

    public int getDisplayedChildId() {
        return getChildAt(getDisplayedChild()).getId();
    }
}
