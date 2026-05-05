package com.aliyun.iot.link.ui.component.progress_dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import com.aliyun.iot.link.ui.component.R;
import com.aliyun.iot.link.ui.component.progress_dialog.BGAProgressBar;

/* JADX INFO: loaded from: classes2.dex */
public class BGAProgressLoadingDialog extends Dialog {
    BGAProgressBar bgaProgressBar;

    public BGAProgressLoadingDialog(@NonNull Context context) {
        this(context, 0);
    }

    public BGAProgressLoadingDialog(@NonNull Context context, int i) {
        super(context, i);
        this.bgaProgressBar = null;
        init();
    }

    private void init() {
        Window window = getWindow();
        window.requestFeature(1);
        setContentView(R.layout.dialog_loading);
        int i = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.35f);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = i;
        attributes.height = i;
        attributes.gravity = 17;
        attributes.dimAmount = 0.0f;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        this.bgaProgressBar = (BGAProgressBar) findViewById(R.id.bga_progress_bar);
    }

    public void setBgaProgressBarProgress(int i) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setProgress(i);
        }
    }

    public void setTextColor(String str) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setTextColor(str);
        }
    }

    public void setTextSize(int i) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setTextSize(i);
        }
    }

    public void setTextMargin(int i) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setTextMargin(i);
        }
    }

    public void setReachedColor(String str) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setReachedColor(str);
        }
    }

    public void setUnReachedColor(String str) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setUnReachedColor(str);
        }
    }

    public void setReachedWidth(int i) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setReachedWidth(i);
        }
    }

    public void setUnReachedWidth(int i) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setUnReachedWidth(i);
        }
    }

    public void isCapRounded(boolean z) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.isCapRounded(z);
        }
    }

    public void setStartAngle(int i) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setStartAngle(i);
        }
    }

    public void setMode(BGAProgressBar.Mode mode) {
        BGAProgressBar bGAProgressBar = this.bgaProgressBar;
        if (bGAProgressBar != null) {
            bGAProgressBar.setMode(mode);
        }
    }
}
