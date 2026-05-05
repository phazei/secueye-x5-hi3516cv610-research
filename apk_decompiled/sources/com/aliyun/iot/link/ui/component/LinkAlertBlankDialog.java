package com.aliyun.iot.link.ui.component;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/* JADX INFO: loaded from: classes2.dex */
public class LinkAlertBlankDialog {
    Context mCtx;
    AlertDialog mDialog;
    int mDialogBackGround;
    int mDialogType;

    private LinkAlertBlankDialog(Builder builder) {
        this.mDialog = new AlertDialog.Builder(builder.mContext).create();
        this.mCtx = builder.mContext;
        if (builder.f4904view != null) {
            this.mDialog.setView(builder.f4904view);
        }
        if (builder.dialogBackGround == 0) {
            this.mDialogBackGround = R.drawable.alert_dialog_bg;
        }
        this.mDialogType = builder.dialogType;
        this.mDialog.setCancelable(builder.mCancelable);
        this.mDialog.setCanceledOnTouchOutside(builder.mCanceledOnTouchOutside);
        this.mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        if (this.mDialogType == 80) {
            this.mDialog.getWindow().setWindowAnimations(R.style.ActionSheetDialogAnimation);
            this.mDialog.getWindow().setGravity(80);
        }
    }

    public void show(int i) {
        this.mDialog.show();
        Window window = this.mDialog.getWindow();
        window.setBackgroundDrawable(this.mCtx.getResources().getDrawable(this.mDialogBackGround));
        WindowManager.LayoutParams attributes = window.getAttributes();
        int i2 = this.mCtx.getResources().getDisplayMetrics().widthPixels;
        if (this.mDialogType == 80) {
            attributes.gravity = 80;
        } else {
            attributes.gravity = 16;
        }
        if (i < 0) {
            attributes.width = (int) (((double) i2) * 0.72d);
        } else {
            attributes.width = i2 - i;
        }
        window.setAttributes(attributes);
    }

    public void dismiss() {
        this.mDialog.dismiss();
    }

    public static class Builder {
        int dialogBackGround;
        int dialogType;
        Context mContext;

        /* JADX INFO: renamed from: view, reason: collision with root package name */
        View f4904view;
        boolean mCanceledOnTouchOutside = true;
        boolean mCancelable = true;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setCanceledOnTouchOutside(boolean z) {
            this.mCanceledOnTouchOutside = z;
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.mCancelable = z;
            return this;
        }

        public Builder setView(View view2) {
            this.f4904view = view2;
            return this;
        }

        public Builder setDialogBackGround(int i) {
            this.dialogBackGround = i;
            return this;
        }

        public Builder setDialogType(int i) {
            this.dialogType = i;
            return this;
        }

        public LinkAlertBlankDialog create() {
            return new LinkAlertBlankDialog(this);
        }
    }
}
