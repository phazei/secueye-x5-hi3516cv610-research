package view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class DialogView extends AlertDialog implements View.OnClickListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private String content;
    private int imageRes;
    private View line;
    private ProgressBar loading;
    private OnNegativeClickListener negativeClickListener;
    private String negativeText;
    private OnPositiveClickListener positiveClickListener;
    private String positiveText;
    private ImageView status;
    private int statusNum;
    private String title;
    private View title_line;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvDes;
    private TextView tvTitle;
    private boolean twoButton;

    public interface OnNegativeClickListener {
        void onNegativeClick(DialogView dialogView);
    }

    public interface OnPositiveClickListener {
        void onPositiveClick(DialogView dialogView);
    }

    protected DialogView(Context context) {
        super(context);
        this.imageRes = 0;
        this.twoButton = true;
        this.statusNum = 0;
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_view_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
        getWindow().setAttributes(getWindow().getAttributes());
    }

    private void initView() {
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.tvDes = (TextView) findViewById(R.id.tv_des);
        this.tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        this.tvCancel = (TextView) findViewById(R.id.tv_cancel);
        this.loading = (ProgressBar) findViewById(R.id.loading);
        this.status = (ImageView) findViewById(R.id.status);
        this.line = findViewById(R.id.vertical_line);
        this.title_line = findViewById(R.id.title_line);
        String str = this.negativeText;
        if (str != null) {
            this.tvCancel.setText(str);
        }
        String str2 = this.positiveText;
        if (str2 != null) {
            this.tvConfirm.setText(str2);
        }
        if (this.title != null) {
            this.tvTitle.setVisibility(0);
            this.tvTitle.setText(this.title);
            this.title_line.setVisibility(0);
        }
        int i = this.imageRes;
        if (i != 0) {
            this.status.setImageResource(i);
            this.status.setVisibility(0);
        }
        if (!this.twoButton) {
            this.line.setVisibility(8);
            this.tvConfirm.setVisibility(8);
        }
        this.tvDes.setText(this.content);
    }

    private void initEvent() {
        this.tvConfirm.setOnClickListener(this);
        this.tvCancel.setOnClickListener(this);
    }

    public void hideConfirmButton() {
        this.line.setVisibility(8);
        this.tvConfirm.setVisibility(8);
    }

    public void showConfirmButton() {
        this.line.setVisibility(0);
        this.tvConfirm.setVisibility(0);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        int id = view2.getId();
        if (id == R.id.tv_confirm) {
            this.positiveClickListener.onPositiveClick(this);
        } else if (id == R.id.tv_cancel) {
            this.negativeClickListener.onNegativeClick(this);
        }
    }

    public static class Builder {

        /* JADX INFO: renamed from: dialog, reason: collision with root package name */
        private DialogView f8107dialog;

        public DialogView build(DialogView dialogView) {
            return dialogView;
        }

        public Builder(Context context) {
            this.f8107dialog = new DialogView(context);
        }

        public Builder setTitle(String str) {
            this.f8107dialog.title = str;
            return this;
        }

        public Builder setContent(String str) {
            this.f8107dialog.content = str;
            return this;
        }

        public Builder setStatus(int i) {
            this.f8107dialog.imageRes = i;
            return this;
        }

        public Builder setTwoButton(boolean z) {
            this.f8107dialog.twoButton = z;
            return this;
        }

        public Builder setPositiveClickListener(String str, OnPositiveClickListener onPositiveClickListener) {
            this.f8107dialog.positiveClickListener = onPositiveClickListener;
            this.f8107dialog.positiveText = str;
            return this;
        }

        public Builder setNegativeClickListener(String str, OnNegativeClickListener onNegativeClickListener) {
            this.f8107dialog.negativeClickListener = onNegativeClickListener;
            this.f8107dialog.negativeText = str;
            return this;
        }

        public DialogView build() {
            return this.f8107dialog;
        }
    }

    public TextView getTvDes() {
        return this.tvDes;
    }

    public TextView getTvCancel() {
        return this.tvCancel;
    }

    public TextView getTvConfirm() {
        return this.tvConfirm;
    }

    public ImageView getStatus() {
        return this.status;
    }

    public int getStatusNum() {
        return this.statusNum;
    }

    public void setStatusNum(int i) {
        this.statusNum = i;
    }

    public ProgressBar getLoading() {
        return this.loading;
    }
}
