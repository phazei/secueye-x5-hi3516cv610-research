package view;

import android.view.View;

/* JADX INFO: loaded from: classes5.dex */
public interface OnViewPagerListener {
    void onInitComplete(View view2);

    void onPageDragging();

    void onPageRelease(boolean z, int i, View view2);

    void onPageSelected(int i, boolean z, View view2);
}
