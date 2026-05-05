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
import android.widget.ImageView;
import android.widget.TextView;

/* JADX INFO: loaded from: classes2.dex */
public class LinkBottomWebDialog {
    private AlertDialog alertDialog;
    private Context context;
    private ImageView ivClose;
    private TextView tvTitle;
    private WebView webView;

    public interface OnClickListener {
        void onClick(LinkBottomWebDialog linkBottomWebDialog);
    }

    public LinkBottomWebDialog(final Builder builder) {
        this.alertDialog = new AlertDialog.Builder(builder.mContext).create();
        this.context = builder.mContext;
        this.alertDialog.setCancelable(builder.mCancelable);
        this.alertDialog.setCanceledOnTouchOutside(builder.mCanceledOnTouchOutside);
        View viewInflate = LayoutInflater.from(builder.mContext).inflate(R.layout.dialog_bottom_web, (ViewGroup) null);
        this.tvTitle = (TextView) viewInflate.findViewById(R.id.tv_title);
        this.ivClose = (ImageView) viewInflate.findViewById(R.id.iv_close);
        this.webView = (WebView) viewInflate.findViewById(R.id.webview);
        this.alertDialog.setView(viewInflate);
        this.tvTitle.setText(builder.mTitle);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(builder.mUrl);
        this.ivClose.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.LinkBottomWebDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (builder.onClickListener != null) {
                    builder.onClickListener.onClick(LinkBottomWebDialog.this);
                }
            }
        });
        this.alertDialog.getWindow().setWindowAnimations(R.style.ActionSheetDialogAnimation);
    }

    public void show(int i, int i2) {
        this.alertDialog.show();
        Window window = this.alertDialog.getWindow();
        window.setBackgroundDrawable(this.context.getResources().getDrawable(R.drawable.shape_ffffff_12_top));
        WindowManager.LayoutParams attributes = window.getAttributes();
        int i3 = this.context.getResources().getDisplayMetrics().widthPixels;
        int i4 = this.context.getResources().getDisplayMetrics().heightPixels;
        attributes.gravity = 80;
        if (i < 0) {
            attributes.width = (int) (((double) i3) * 0.72d);
        } else {
            attributes.width = i3 - i;
        }
        if (i2 < 0) {
            attributes.height = (int) (((double) i4) * 0.72d);
        } else {
            attributes.height = i4 - i2;
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
        String mTitle = "";
        String mUrl = "";

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

        public Builder setOnclick(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder setUrl(String str) {
            this.mUrl = str;
            return this;
        }

        public LinkBottomWebDialog create() {
            return new LinkBottomWebDialog(this);
        }
    }
}
