package tools;

import android.view.View;

/* JADX INFO: loaded from: classes4.dex */
public abstract class OnMultiClickListener implements View.OnClickListener {
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    public abstract void onMultiClick(View view2);

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - lastClickTime >= 500) {
            lastClickTime = jCurrentTimeMillis;
            onMultiClick(view2);
        }
    }
}
