package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.task.AbsAsyncTask;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.util.OpenAccountUIUtils;
import com.alibaba.sdk.android.openaccount.util.HttpHelper;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.util.WidgetUtils;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class CheckCodeInputBox extends AbsInputBoxWrapper {
    protected ImageView checkCode;
    protected String checkCodeUrl;
    protected Button refresh;

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_checkcode_input_box";
    }

    public CheckCodeInputBox(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (isInEditMode()) {
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ResourceUtils.getRStyleableIntArray(context, "inputBox"));
        int rStyleable = ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_inputType");
        if (rStyleable != 0) {
            this.inputBoxWithClear.getEditText().setInputType(rStyleable);
        }
        int i = typedArrayObtainStyledAttributes.getInt(ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_input_maxLength"), 0);
        if (i > 0) {
            this.inputBoxWithClear.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
        }
        typedArrayObtainStyledAttributes.recycle();
        this.checkCode = (ImageView) findViewById("image");
        this.refresh = (Button) findViewById("refresh");
        this.refresh.setTypeface(OpenAccountUIUtils.getDefaultFont());
        this.refresh.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.CheckCodeInputBox.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (CheckCodeInputBox.this.checkCodeUrl != null) {
                    CheckCodeInputBox checkCodeInputBox = CheckCodeInputBox.this;
                    checkCodeInputBox.refreshCheckCode(checkCodeInputBox.checkCodeUrl);
                }
            }
        });
    }

    public void refreshCheckCode(String str) {
        this.checkCodeUrl = str;
        new RefreshCheckCodeTask().execute(new String[]{str});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap getHttpBitmap(String str) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = HttpHelper.get(str);
            try {
                Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStream);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "getHttpBitmap error:" + e.getMessage(), e);
                    }
                }
                return bitmapDecodeStream;
            } catch (Throwable th) {
                th = th;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                        AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "getHttpBitmap error:" + e2.getMessage(), e2);
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
        }
    }

    private class RefreshCheckCodeTask extends AbsAsyncTask<String, Void, Bitmap> {
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        protected void doFinally() {
        }

        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        protected void doWhenException(Throwable th) {
        }

        private RefreshCheckCodeTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(CheckCodeInputBox.this.getResources(), bitmap);
                if (CheckCodeInputBox.this.checkCode.getBackground() instanceof BitmapDrawable) {
                    Bitmap bitmap2 = ((BitmapDrawable) CheckCodeInputBox.this.checkCode.getBackground()).getBitmap();
                    if (!bitmap2.isRecycled()) {
                        bitmap2.recycle();
                    }
                }
                WidgetUtils.setBackgroundDrawable(CheckCodeInputBox.this.checkCode, bitmapDrawable);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Bitmap asyncExecute(String... strArr) {
            return CheckCodeInputBox.this.getHttpBitmap(strArr[0]);
        }
    }
}
