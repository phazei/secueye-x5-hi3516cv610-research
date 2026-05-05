package com.aliyun.alink.sdk.bone.plugins.ut;

import android.R;
import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes2.dex */
public class KeyBoardListenerHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private WeakReference<Activity> f4482a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private OnKeyBoardChangeListener f4483b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ViewTreeObserver.OnGlobalLayoutListener f4484c = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.aliyun.alink.sdk.bone.plugins.ut.KeyBoardListenerHelper.1
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            ((Activity) KeyBoardListenerHelper.this.f4482a.get()).runOnUiThread(new Runnable() { // from class: com.aliyun.alink.sdk.bone.plugins.ut.KeyBoardListenerHelper.1.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!KeyBoardListenerHelper.this.isActivityValid() || KeyBoardListenerHelper.this.f4483b == null) {
                        return;
                    }
                    try {
                        Rect rect = new Rect();
                        ((Activity) KeyBoardListenerHelper.this.f4482a.get()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                        int height = ((Activity) KeyBoardListenerHelper.this.f4482a.get()).getWindow().getDecorView().getHeight() - rect.bottom;
                        KeyBoardListenerHelper.this.f4483b.OnKeyBoardChange(height > 200, (int) ((height * 1.0f) / ((Activity) KeyBoardListenerHelper.this.f4482a.get()).getResources().getDisplayMetrics().density));
                    } catch (Exception e) {
                        Log.e("BoneSystemPlugin", "onGlobalLayout error:" + e.getMessage());
                    }
                }
            });
        }
    };

    public interface OnKeyBoardChangeListener {
        void OnKeyBoardChange(boolean z, int i);
    }

    public KeyBoardListenerHelper(final Activity activity2) {
        this.f4482a = null;
        Log.d("BoneSystemPlugin", "KeyBoardListenerHelper() called with: activity = [" + activity2 + "]");
        if (activity2 == null) {
            return;
        }
        try {
            this.f4482a = new WeakReference<>(activity2);
            activity2.runOnUiThread(new Runnable() { // from class: com.aliyun.alink.sdk.bone.plugins.ut.KeyBoardListenerHelper.2
                @Override // java.lang.Runnable
                public void run() {
                    activity2.getWindow().setSoftInputMode(16);
                    ((Activity) KeyBoardListenerHelper.this.f4482a.get()).findViewById(R.id.content).getViewTreeObserver().addOnGlobalLayoutListener(KeyBoardListenerHelper.this.f4484c);
                }
            });
        } catch (Exception e) {
            Log.e("BoneSystemPlugin", "KeyBoardListenerHelper error:" + e.getMessage());
        }
    }

    public void destroy() {
        Log.i("BoneSystemPlugin", "destroy");
        if (isActivityValid()) {
            try {
                this.f4482a.get().runOnUiThread(new Runnable() { // from class: com.aliyun.alink.sdk.bone.plugins.ut.KeyBoardListenerHelper.3
                    @Override // java.lang.Runnable
                    public void run() {
                        ((Activity) KeyBoardListenerHelper.this.f4482a.get()).findViewById(R.id.content).getViewTreeObserver().removeOnGlobalLayoutListener(KeyBoardListenerHelper.this.f4484c);
                    }
                });
            } catch (Exception e) {
                Log.e("BoneSystemPlugin", "destroy error:" + e.getMessage());
            }
        }
    }

    public void setOnKeyBoardChangeListener(OnKeyBoardChangeListener onKeyBoardChangeListener) {
        Log.i("BoneSystemPlugin", "setOnKeyBoardChangeListener");
        this.f4483b = onKeyBoardChangeListener;
    }

    public boolean isActivityValid() {
        WeakReference<Activity> weakReference = this.f4482a;
        return (weakReference == null || weakReference.get() == null) ? false : true;
    }
}
