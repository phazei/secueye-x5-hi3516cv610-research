package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.alibaba.sdk.android.openaccount.ui.util.OpenAccountUIUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;

/* JADX INFO: loaded from: classes.dex */
public class PasswordInputBox extends AbsInputBoxWrapper {
    private boolean isPassword;

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_password_input_box";
    }

    public PasswordInputBox(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isPassword = true;
        if (isInEditMode()) {
            return;
        }
        Typeface defaultFont = OpenAccountUIUtils.getDefaultFont();
        final Button button = (Button) findViewById("open_eye");
        button.setTypeface(defaultFont);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ResourceUtils.getRStyleableIntArray(context, "inputBox"));
        this.inputBoxWithClear.getEditText().setInputType(129);
        this.inputBoxWithClear.getEditText().setHint(typedArrayObtainStyledAttributes.getString(ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_hint")));
        int i = typedArrayObtainStyledAttributes.getInt(ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_input_maxLength"), 0);
        if (i > 0) {
            this.inputBoxWithClear.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
        }
        typedArrayObtainStyledAttributes.recycle();
        button.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.PasswordInputBox.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PasswordInputBox.this.isPassword = !r0.isPassword;
                PasswordInputBox.this.inputBoxWithClear.setPassword(PasswordInputBox.this.isPassword);
                if (PasswordInputBox.this.isPassword) {
                    button.setText(ResourceUtils.getString(view2.getContext(), "ali_sdk_openaccount_dynamic_icon_eye"));
                } else {
                    button.setText(ResourceUtils.getString(view2.getContext(), "ali_sdk_openaccount_dynamic_icon_eye_open"));
                }
            }
        });
    }

    public void setUsePasswordMasking(boolean z) {
        this.isPassword = z;
        ((Button) findViewById("open_eye")).setText(ResourceUtils.getString(getContext(), z ? "ali_sdk_openaccount_dynamic_icon_eye" : "ali_sdk_openaccount_dynamic_icon_eye_open"));
        this.inputBoxWithClear.setPassword(z);
    }
}
