package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.util.AttributeUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public class SmsCodeInputBox extends AbsInputBoxWrapper {
    protected Handler handler;
    protected String mSendText;
    protected Button send;
    protected View.OnClickListener sendClickListener;

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_sms_code_input_box";
    }

    public Button getSend() {
        return this.send;
    }

    public SmsCodeInputBox(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.handler = new Handler(Looper.getMainLooper());
        this.mSendText = "ali_sdk_openaccount_text_send_sms_code";
        this.send = (Button) findViewById("send");
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ResourceUtils.getRStyleableIntArray(context, "inputBox"));
        int rStyleable = ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_inputType");
        if (rStyleable != 0) {
            this.inputBoxWithClear.getEditText().setInputType(rStyleable);
        }
        this.inputBoxWithClear.getEditText().setTextColor(getResources().getColor(R.color.ali_user_color_primary_dark));
        this.inputBoxWithClear.getEditText().setHint(typedArrayObtainStyledAttributes.getString(ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_hint")));
        int i = typedArrayObtainStyledAttributes.getInt(ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_input_maxLength"), 0);
        if (i > 0) {
            this.inputBoxWithClear.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
        }
        typedArrayObtainStyledAttributes.recycle();
        useCustomAttrs(context, attributeSet);
        this.send.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.SmsCodeInputBox.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (SmsCodeInputBox.this.sendClickListener != null) {
                    SmsCodeInputBox.this.sendClickListener.onClick(SmsCodeInputBox.this);
                }
            }
        });
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected void doUseCustomAttrs(Context context, TypedArray typedArray) {
        super.doUseCustomAttrs(context, typedArray);
        if (isInEditMode()) {
            return;
        }
        try {
            ColorStateList colorStateList = AttributeUtils.getColorStateList(context, typedArray, "ali_sdk_openaccount_attrs_send_sms_code_color");
            if (colorStateList != null) {
                this.send.setTextColor(colorStateList);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void startTimer(final Context context) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.SmsCodeInputBox.2
            private int counter = 60;

            static /* synthetic */ int access$010(AnonymousClass2 anonymousClass2) {
                int i = anonymousClass2.counter;
                anonymousClass2.counter = i - 1;
                return i;
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                SmsCodeInputBox.this.handler.post(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.SmsCodeInputBox.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (AnonymousClass2.this.counter > 0) {
                            SmsCodeInputBox.this.send.setText(String.format(ResourceUtils.getString(context, "ali_sdk_openaccount_text_count_down"), Integer.valueOf(AnonymousClass2.this.counter)));
                        } else {
                            SmsCodeInputBox.this.send.setText(ResourceUtils.getString(context, SmsCodeInputBox.this.getSendText()));
                            SmsCodeInputBox.this.send.setEnabled(true);
                            timer.cancel();
                        }
                        AnonymousClass2.access$010(AnonymousClass2.this);
                    }
                });
            }
        }, 0L, 1000L);
        this.send.setEnabled(false);
    }

    public void addSendClickListener(View.OnClickListener onClickListener) {
        this.sendClickListener = onClickListener;
    }

    public void setSendText(String str) {
        this.mSendText = str;
    }

    public String getSendText() {
        return this.mSendText;
    }
}
