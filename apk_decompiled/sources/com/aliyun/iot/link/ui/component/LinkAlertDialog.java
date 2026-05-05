package com.aliyun.iot.link.ui.component;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import com.aliyun.iot.link.ui.component.wheelview.DimensionUtil;

/* JADX INFO: loaded from: classes2.dex */
public class LinkAlertDialog {
    Context mCtx;
    AlertDialog mDialog;
    EditText mInputEt;
    TextView mMessageTv;
    View mNegativeBtn;
    TextView mNegativeTv;
    View mPositiveBtn;
    TextView mPositiveTv;
    TextView mTitleTv;
    int mType;

    public interface OnClickListener {
        void onClick(LinkAlertDialog linkAlertDialog);
    }

    private LinkAlertDialog(final Builder builder) {
        View viewInflate;
        this.mDialog = new AlertDialog.Builder(builder.mContext).create();
        this.mCtx = builder.mContext;
        this.mType = builder.mType;
        if (builder.mType == 2) {
            viewInflate = LayoutInflater.from(builder.mContext).inflate(R.layout.alert_dialog_input, (ViewGroup) null);
            this.mInputEt = (EditText) viewInflate.findViewById(R.id.input);
            setupInput(builder);
        } else {
            viewInflate = LayoutInflater.from(builder.mContext).inflate(R.layout.alert_dialog, (ViewGroup) null);
            this.mMessageTv = (TextView) viewInflate.findViewById(R.id.message);
            if (builder.mMessageSize > 0) {
                this.mMessageTv.setTextSize(1, builder.mMessageSize);
            }
            if (builder.messageNeeedBol) {
                this.mMessageTv.getPaint().setFakeBoldText(true);
            } else {
                this.mMessageTv.getPaint().setFakeBoldText(false);
            }
            setupMessage(builder);
        }
        this.mTitleTv = (TextView) viewInflate.findViewById(R.id.title);
        this.mPositiveTv = (TextView) viewInflate.findViewById(R.id.positive_tv);
        this.mPositiveBtn = viewInflate.findViewById(R.id.positive_btn);
        this.mNegativeTv = (TextView) viewInflate.findViewById(R.id.negative_tv);
        this.mNegativeBtn = viewInflate.findViewById(R.id.negative_btn);
        this.mDialog.setView(viewInflate);
        if (TextUtils.isEmpty(builder.mTitle)) {
            this.mTitleTv.setVisibility(8);
        } else {
            this.mTitleTv.setText(builder.mTitle);
            if (builder.mTitleSize > 0) {
                this.mTitleTv.setTextSize(1, builder.mTitleSize);
            }
        }
        this.mPositiveTv.setText(builder.mPositiveBtnText);
        this.mNegativeTv.setText(builder.mNegativeBtnText);
        this.mPositiveTv.setTextColor(builder.mPositiveTextColor);
        this.mNegativeTv.setTextColor(builder.mNegativeTextColor);
        if (builder.adjustButtonTextSizeForFullDisplay) {
            resizeButtonTextSize(builder);
        }
        if (TextUtils.isEmpty(builder.mPositiveBtnText)) {
            this.mPositiveBtn.setVisibility(8);
        }
        if (TextUtils.isEmpty(builder.mNegativeBtnText)) {
            this.mNegativeBtn.setVisibility(8);
        }
        this.mPositiveBtn.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.LinkAlertDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (builder.mPositiveListener != null) {
                    builder.mPositiveListener.onClick(LinkAlertDialog.this);
                }
            }
        });
        this.mNegativeBtn.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.LinkAlertDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (builder.mNegativeListener != null) {
                    builder.mNegativeListener.onClick(LinkAlertDialog.this);
                }
            }
        });
        if (builder.mInputTextWatcher != null) {
            this.mInputEt.addTextChangedListener(builder.mInputTextWatcher);
        }
        this.mDialog.setCancelable(builder.mCancelable);
        this.mDialog.setCanceledOnTouchOutside(builder.mCanceledOnTouchOutside);
        this.mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    @Nullable
    public EditText getInputEt() {
        return this.mInputEt;
    }

    private void resizeButtonTextSize(Builder builder) {
        TextPaint textPaint = new TextPaint();
        float fDip2px = ((this.mCtx.getResources().getDisplayMetrics().widthPixels * 0.7f) / 2.0f) - DimensionUtil.dip2px(4.0f);
        for (float f = 17.0f; f > builder.mButtonMinTextSize; f -= 0.2f) {
            textPaint.setTextSize(DimensionUtil.dip2px(f));
            float fMax = builder.mPositiveBtnText != null ? Math.max(0.0f, textPaint.measureText(builder.mPositiveBtnText)) : 0.0f;
            if (builder.mNegativeBtnText != null) {
                fMax = Math.max(fMax, textPaint.measureText(builder.mNegativeBtnText));
            }
            if (fMax < fDip2px) {
                this.mPositiveTv.setTextSize(f);
                this.mNegativeTv.setTextSize(f);
                return;
            }
        }
        this.mPositiveTv.setTextSize(builder.mButtonMinTextSize);
        this.mNegativeTv.setTextSize(builder.mButtonMinTextSize);
    }

    private void setupMessage(Builder builder) {
        this.mMessageTv.setText(builder.mMessage);
        this.mMessageTv.setGravity(builder.mMsgGravity);
    }

    private void setupInput(Builder builder) {
        int length;
        if (builder.mInputMaxLength > 0) {
            this.mInputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(builder.mInputMaxLength)});
        }
        if (!TextUtils.isEmpty(builder.mInput)) {
            this.mInputEt.setText(builder.mInput);
            EditText editText = this.mInputEt;
            if (builder.mInputMaxLength > 0) {
                length = Math.min(builder.mInputMaxLength, builder.mInput.length());
            } else {
                length = builder.mInput.length();
            }
            editText.setSelection(length);
        }
        this.mInputEt.setHint(builder.mInputHint);
        this.mDialog.getWindow().clearFlags(131080);
        this.mDialog.getWindow().setSoftInputMode(21);
    }

    public void show() {
        requestFocus();
        this.mDialog.show();
        Window window = this.mDialog.getWindow();
        window.setBackgroundDrawable(this.mCtx.getResources().getDrawable(R.drawable.alert_dialog_bg));
        WindowManager.LayoutParams attributes = window.getAttributes();
        int i = this.mCtx.getResources().getDisplayMetrics().widthPixels;
        attributes.gravity = 16;
        attributes.width = (int) (((double) i) * 0.72d);
        window.setAttributes(attributes);
    }

    public void dismiss() {
        clearFocus();
        this.mDialog.dismiss();
    }

    public void requestFocus() {
        if (this.mType != 2 || this.mInputEt == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) this.mCtx.getSystemService("input_method");
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(2, 0);
        }
        this.mInputEt.setFocusableInTouchMode(true);
        this.mInputEt.requestFocus();
    }

    public void clearFocus() {
        if (this.mType != 2 || this.mInputEt == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) this.mCtx.getSystemService("input_method");
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(this.mInputEt.getWindowToken(), 0);
        }
        this.mInputEt.clearFocus();
    }

    public String getInputText() {
        EditText editText = this.mInputEt;
        return editText != null ? editText.getText().toString() : "";
    }

    public TextView getTitleView() {
        return this.mTitleTv;
    }

    public TextView getMessageView() {
        return this.mMessageTv;
    }

    public static class Builder {
        public static final int INPUT = 2;
        public static final int NORMAL = 1;
        boolean adjustButtonTextSizeForFullDisplay;
        Context mContext;
        int mInputMaxLength;
        TextWatcher mInputTextWatcher;
        int mMessageSize;
        OnClickListener mNegativeListener;
        int mNegativeTextColor;
        OnClickListener mPositiveListener;
        int mPositiveTextColor;
        int mTitleSize;
        int mType;
        String mTitle = null;
        boolean messageNeeedBol = false;
        String mMessage = null;
        String mInputHint = null;
        String mInput = "";
        String mPositiveBtnText = null;
        String mNegativeBtnText = null;
        boolean mHideTitle = false;
        boolean mCanceledOnTouchOutside = true;
        boolean mCancelable = true;
        int mMsgGravity = 1;
        float mButtonMinTextSize = 10.0f;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setMessageTextSize(int i) {
            this.mMessageSize = i;
            return this;
        }

        public Builder setMessageNeedBold(boolean z) {
            this.messageNeeedBol = z;
            return this;
        }

        public Builder adjustButtonTextSizeForFullDisplay(boolean z) {
            this.adjustButtonTextSizeForFullDisplay = z;
            return this;
        }

        public Builder setButtonMinTextSize(float f) {
            this.mButtonMinTextSize = f;
            return this;
        }

        public Builder setType(int i) {
            this.mType = i;
            return this;
        }

        public Builder setTitle(String str) {
            this.mTitle = str;
            return this;
        }

        public Builder setTitle(String str, int i) {
            this.mTitleSize = i;
            return this;
        }

        public Builder hideTitle() {
            this.mHideTitle = true;
            return this;
        }

        public Builder setMessage(String str) {
            this.mMessage = str;
            return this;
        }

        public Builder setInputMaxLength(int i) {
            this.mInputMaxLength = i;
            return this;
        }

        public Builder setMessageGravity(int i) {
            this.mMsgGravity = i;
            return this;
        }

        public Builder setInputHint(String str) {
            this.mInputHint = str;
            return this;
        }

        public Builder setInput(String str) {
            this.mInput = str;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean z) {
            this.mCanceledOnTouchOutside = z;
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.mCancelable = z;
            return this;
        }

        public Builder setPositiveButton(String str, OnClickListener onClickListener) {
            return setPositiveButton(str, Color.parseColor("#0079FF"), onClickListener);
        }

        public Builder setPositiveButton(String str, @ColorInt int i, OnClickListener onClickListener) {
            this.mPositiveBtnText = str;
            this.mPositiveTextColor = i;
            this.mPositiveListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(String str, OnClickListener onClickListener) {
            return setNegativeButton(str, Color.parseColor("#0079FF"), onClickListener);
        }

        public Builder setNegativeButton(String str, @ColorInt int i, OnClickListener onClickListener) {
            this.mNegativeBtnText = str;
            this.mNegativeTextColor = i;
            this.mNegativeListener = onClickListener;
            return this;
        }

        public Builder setInputTextWatcher(TextWatcher textWatcher) {
            this.mInputTextWatcher = textWatcher;
            return this;
        }

        public LinkAlertDialog create() {
            return new LinkAlertDialog(this);
        }
    }
}
