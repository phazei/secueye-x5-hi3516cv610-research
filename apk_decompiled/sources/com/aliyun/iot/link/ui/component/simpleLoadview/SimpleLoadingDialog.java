package com.aliyun.iot.link.ui.component.simpleLoadview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.aliyun.iot.link.ui.component.R;

/* JADX INFO: loaded from: classes2.dex */
public class SimpleLoadingDialog extends Dialog {
    private String TAG;
    private Context context;
    private LinkSimpleLoadView mSIloadView;

    public SimpleLoadingDialog(Context context) {
        this(context, 0, 0);
    }

    public SimpleLoadingDialog(Context context, int i, int i2) {
        super(context, R.style.link_loading_fulldialog);
        this.TAG = "SimpleLoadingDialog";
        this.mSIloadView = new LinkSimpleLoadView(context);
        this.context = context;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
        if (i != 0 && i2 != 0) {
            getWindow().setLayout(i, i2);
            layoutParams.width = i;
            layoutParams.height = i2;
        } else {
            getWindow().setLayout(-1, -1);
            layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
        }
        getWindow().setFlags(8, 8);
        getWindow().setGravity(17);
        setContentView(this.mSIloadView, layoutParams);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setLoadingViewStyle(R.style.SimpleLoadViewStyle);
        setCanceledOnTouchOutside(false);
    }

    @Override // android.app.Dialog
    public final void show() {
        if (isShowing()) {
            return;
        }
        super.show();
    }

    public void setLoadingViewStyle(int i) {
        TypedArray typedArrayObtainStyledAttributes = this.context.obtainStyledAttributes(i, R.styleable.LinkSimpleLoadView);
        this.mSIloadView.applyStyle(typedArrayObtainStyledAttributes);
        typedArrayObtainStyledAttributes.recycle();
    }

    public void setLoadViewRootBgColor(int i) {
        this.mSIloadView.setLoadViewRootBgColor(i);
    }

    public void setLoadViewRootBgDrawbleRes(int i) {
        this.mSIloadView.setLoadViewRootBgDrawbleRes(i);
    }

    public void setLoadViewLoacation(float f) {
        LinkSimpleLoadView linkSimpleLoadView = this.mSIloadView;
        if (linkSimpleLoadView != null) {
            linkSimpleLoadView.setLoadViewLoacation(f);
        }
    }

    public void setTipViewLoacation(float f) {
        LinkSimpleLoadView linkSimpleLoadView = this.mSIloadView;
        if (linkSimpleLoadView != null) {
            linkSimpleLoadView.setTipViewLoacation(f);
        }
    }

    public void showLoading(String str, Drawable drawable) {
        show();
        setFixedHeight();
        this.mSIloadView.showLoading(str, drawable);
    }

    public void showLoading(String str) {
        show();
        setFixedHeight();
        this.mSIloadView.showLoading(str);
    }

    private void setFixedHeight() {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = 17;
        attributes.height = getContext().getResources().getDisplayMetrics().heightPixels;
        getWindow().setAttributes(attributes);
    }

    public void showError(String str, Drawable drawable) {
        show();
        this.mSIloadView.showError(str, drawable);
    }

    public void showError(String str) {
        show();
        this.mSIloadView.showError(str);
    }

    @Override // android.app.Dialog
    @TargetApi(17)
    public void onBackPressed() {
        Context context = this.context;
        if (context == null) {
            Log.e(this.TAG, "context is null");
            return;
        }
        if (Activity.class.isInstance(context)) {
            Activity activity2 = (Activity) this.context;
            if (activity2.isFinishing() || activity2.isDestroyed()) {
                return;
            }
            if (isShowing()) {
                dismiss();
            }
            activity2.onBackPressed();
            return;
        }
        dismiss();
        Log.e(this.TAG, "context is not an activity!");
    }

    public void setTopbarClickable(View view2) {
        if (view2 == null) {
            return;
        }
        this.mSIloadView.setTopbarClickableArea(view2, new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.simpleLoadview.SimpleLoadingDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                SimpleLoadingDialog.this.onBackPressed();
            }
        });
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismiss();
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        LinkSimpleLoadView linkSimpleLoadView = this.mSIloadView;
        if (linkSimpleLoadView != null) {
            linkSimpleLoadView.hide();
        }
        super.dismiss();
    }
}
