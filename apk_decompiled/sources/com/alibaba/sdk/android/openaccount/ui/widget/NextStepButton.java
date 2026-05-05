package com.alibaba.sdk.android.openaccount.ui.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;
import com.alibaba.sdk.android.openaccount.ui.util.AttributeUtils;
import org.mozilla.javascript.ES6Iterator;

/* JADX INFO: loaded from: classes.dex */
public class NextStepButton extends LinearLayoutTemplate {
    protected Button button;

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_next_step";
    }

    public NextStepButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.button = (Button) findViewById(ES6Iterator.NEXT_METHOD);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.text});
        String string = typedArrayObtainStyledAttributes.getString(0);
        if (!TextUtils.isEmpty(string)) {
            this.button.setText(string);
        }
        typedArrayObtainStyledAttributes.recycle();
        useCustomAttrs(context, attributeSet);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected void doUseCustomAttrs(Context context, TypedArray typedArray) {
        if (isInEditMode()) {
            return;
        }
        this.button.setBackgroundResource(AttributeUtils.getResourceId(context, typedArray, "ali_sdk_openaccount_attrs_next_step_bg"));
    }

    public void setText(String str) {
        this.button.setText(str);
    }
}
