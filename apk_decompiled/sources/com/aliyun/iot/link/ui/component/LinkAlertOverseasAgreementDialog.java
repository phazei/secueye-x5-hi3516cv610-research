package com.aliyun.iot.link.ui.component;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

/* JADX INFO: loaded from: classes2.dex */
public class LinkAlertOverseasAgreementDialog {
    private AlertDialog alertDialog;
    private Context context;
    private TextView tvCommit;
    private TextView tvTitle;
    private WebView webView;

    public interface OnClickListener {
        void onClick(LinkAlertOverseasAgreementDialog linkAlertOverseasAgreementDialog);
    }

    public LinkAlertOverseasAgreementDialog(final Builder builder) {
        this.alertDialog = new AlertDialog.Builder(builder.mContext).create();
        this.context = builder.mContext;
        this.alertDialog.setCancelable(builder.mCancelable);
        this.alertDialog.setCanceledOnTouchOutside(builder.mCanceledOnTouchOutside);
        View viewInflate = LayoutInflater.from(builder.mContext).inflate(R.layout.dialog_alert_overseas_agreement, (ViewGroup) null);
        this.tvCommit = (TextView) viewInflate.findViewById(R.id.tv_commit);
        this.tvTitle = (TextView) viewInflate.findViewById(R.id.tv_title);
        this.webView = (WebView) viewInflate.findViewById(R.id.webview);
        this.alertDialog.setView(viewInflate);
        this.tvTitle.setText(builder.mTitle);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(builder.mUrl);
        this.tvCommit.setText(builder.mCommit);
        if (builder.commitColorText != -1) {
            this.tvCommit.setTextColor(this.context.getResources().getColor(builder.commitColorText));
        }
        this.tvCommit.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.LinkAlertOverseasAgreementDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (builder.onClickListener != null) {
                    builder.onClickListener.onClick(LinkAlertOverseasAgreementDialog.this);
                }
            }
        });
    }

    public void show(int i) {
        this.alertDialog.show();
        Window window = this.alertDialog.getWindow();
        window.setFlags(1024, 1024);
        window.setBackgroundDrawable(this.context.getResources().getDrawable(R.drawable.shape_ffffff_12));
        WindowManager.LayoutParams attributes = window.getAttributes();
        int i2 = this.context.getResources().getDisplayMetrics().widthPixels;
        int i3 = this.context.getResources().getDisplayMetrics().heightPixels;
        attributes.gravity = 17;
        if (i < 0) {
            attributes.width = (int) (((double) i2) * 0.72d);
        } else {
            attributes.width = i2 - i;
        }
        window.setAttributes(attributes);
    }

    public void dismiss() {
        this.alertDialog.dismiss();
        WebView webView = this.webView;
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(this.webView);
            }
            this.webView.stopLoading();
            this.webView.getSettings().setJavaScriptEnabled(false);
            this.webView.clearHistory();
            this.webView.removeAllViews();
            this.webView.destroy();
        }
    }

    public static class Builder {
        Context mContext;
        OnClickListener onClickListener;
        boolean mCanceledOnTouchOutside = true;
        boolean mCancelable = true;
        String mTitle = "title";
        String mCommit = "commit";
        String mUrl = "";
        int commitColorText = -1;

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

        public Builder setTitle(String str) {
            this.mTitle = str;
            return this;
        }

        public Builder setUrl(String str) {
            this.mUrl = str;
            return this;
        }

        public Builder setOnClick(String str, OnClickListener onClickListener) {
            this.mCommit = str;
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder setCommitColorText(int i) {
            this.commitColorText = i;
            return this;
        }

        public LinkAlertOverseasAgreementDialog create() {
            return new LinkAlertOverseasAgreementDialog(this);
        }
    }
}
