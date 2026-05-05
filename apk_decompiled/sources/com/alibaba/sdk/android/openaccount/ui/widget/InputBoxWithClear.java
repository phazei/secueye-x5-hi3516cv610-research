package com.alibaba.sdk.android.openaccount.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.util.AttributeUtils;
import com.alibaba.sdk.android.openaccount.ui.util.OpenAccountUIUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;

/* JADX INFO: loaded from: classes.dex */
public class InputBoxWithClear extends LinearLayoutTemplate {
    protected TextView chosedCountryNum;
    protected TextView chosedCountryNumSub;
    protected TextView clear;
    protected ImageView countryChooseButton;
    protected EditText input;
    protected TextView leftIcon;
    protected TextWatcher textWatcher;

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_input_box";
    }

    public InputBoxWithClear(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (isInEditMode()) {
            return;
        }
        this.input = (EditText) findViewById("input");
        this.leftIcon = (TextView) findViewById("left_icon");
        this.clear = (TextView) findViewById("clear");
        Typeface defaultFont = OpenAccountUIUtils.getDefaultFont();
        this.clear.setTypeface(defaultFont);
        this.clear.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.InputBoxWithClear.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                InputBoxWithClear.this.input.setText((CharSequence) null);
                InputBoxWithClear.this.clear.setVisibility(8);
            }
        });
        this.input.addTextChangedListener(new TextWatcher() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.InputBoxWithClear.2
            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (InputBoxWithClear.this.textWatcher != null) {
                    InputBoxWithClear.this.textWatcher.onTextChanged(charSequence, i, i2, i3);
                }
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (InputBoxWithClear.this.textWatcher != null) {
                    InputBoxWithClear.this.textWatcher.beforeTextChanged(charSequence, i, i2, i3);
                }
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    InputBoxWithClear.this.clear.setVisibility(0);
                } else {
                    InputBoxWithClear.this.clear.setVisibility(8);
                }
                if (InputBoxWithClear.this.textWatcher != null) {
                    InputBoxWithClear.this.textWatcher.afterTextChanged(editable);
                }
            }
        });
        this.clear.setVisibility(8);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ResourceUtils.getRStyleableIntArray(context, "inputBox"));
        if (this.leftIcon != null) {
            this.leftIcon.setText(typedArrayObtainStyledAttributes.getString(ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_leftIconText")));
            this.leftIcon.setTypeface(defaultFont);
        }
        int i = typedArrayObtainStyledAttributes.getInt(ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_inputType"), 0);
        if (i != 0) {
            this.input.setInputType(i);
        }
        this.input.setHint(typedArrayObtainStyledAttributes.getString(ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_hint")));
        int i2 = typedArrayObtainStyledAttributes.getInt(ResourceUtils.getRStyleable(context, "inputBox_ali_sdk_openaccount_attrs_input_maxLength"), 0);
        if (i2 > 0) {
            this.input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(i2)});
        }
        typedArrayObtainStyledAttributes.recycle();
        this.chosedCountryNum = (TextView) findViewById("edt_chosed_country_num");
        this.chosedCountryNumSub = (TextView) findViewById("edt_chosed_country_num_sub");
        this.countryChooseButton = (ImageView) findViewById("country_choose_btn");
        useCustomAttrs(context, attributeSet);
    }

    public void setSupportForeignMobile(final Activity activity2, final Class<? extends Activity> cls, boolean z) {
        if (z) {
            setViewVisibility(0, this.chosedCountryNum, this.chosedCountryNumSub, this.countryChooseButton);
            setViewVisibility(8, this.leftIcon);
            setViewOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.InputBoxWithClear.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    Intent intent = new Intent();
                    intent.setClass(activity2.getBaseContext(), cls);
                    activity2.startActivityForResult(intent, RequestCode.MOBILE_COUNTRY_SELECTOR_REQUEST);
                }
            }, this.chosedCountryNum, this.chosedCountryNumSub, this.countryChooseButton);
        } else {
            setViewVisibility(8, this.chosedCountryNum, this.chosedCountryNumSub, this.countryChooseButton);
            setViewVisibility(0, this.leftIcon);
        }
    }

    public String getMobileLocationCode() {
        TextView textView = this.chosedCountryNum;
        if (textView == null || textView.getVisibility() != 0 || this.chosedCountryNum.getText() == null) {
            return null;
        }
        return this.chosedCountryNum.getText().toString();
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i != RequestCode.MOBILE_COUNTRY_SELECTOR_REQUEST) {
            return false;
        }
        if (this.chosedCountryNum == null || intent == null || intent.getStringExtra("countryCode") == null) {
            return true;
        }
        this.chosedCountryNum.setText(intent.getStringExtra("countryCode"));
        return true;
    }

    public void setInputType(int i) {
        this.input.setInputType(i);
    }

    public void setInputHint(String str) {
        this.input.setHint(str);
    }

    public String getInputHint() {
        if (this.input.getHint() == null) {
            return null;
        }
        return this.input.getHint().toString();
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected void doUseCustomAttrs(Context context, TypedArray typedArray) {
        this.clear.setTextColor(AttributeUtils.getColor(context, typedArray, "ali_sdk_openaccount_attrs_input_box_clear_btn_color"));
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }

    public String getEditTextContent() {
        if (this.input.getText() != null) {
            return this.input.getText().toString();
        }
        return null;
    }

    public EditText getEditText() {
        return this.input;
    }

    public TextView getLeftIcon() {
        return this.leftIcon;
    }

    public TextView getClearTextView() {
        return this.clear;
    }

    public void setPassword(boolean z) {
        if (z) {
            this.input.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ajustInputSelection();
        } else {
            this.input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ajustInputSelection();
        }
    }

    private void ajustInputSelection() {
        EditText editText = this.input;
        editText.setSelection(editText.getText().length());
    }
}
