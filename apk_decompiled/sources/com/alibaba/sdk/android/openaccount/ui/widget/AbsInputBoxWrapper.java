package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import com.alibaba.sdk.android.pluto.Pluto;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbsInputBoxWrapper extends LinearLayoutTemplate {
    protected InputBoxWithClear inputBoxWithClear;

    public AbsInputBoxWrapper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (isInEditMode()) {
            return;
        }
        this.inputBoxWithClear = (InputBoxWithClear) findViewById("input_box");
        Pluto.DEFAULT_INSTANCE.inject(this);
    }

    public InputBoxWithClear getInputBoxWithClear() {
        return this.inputBoxWithClear;
    }

    public EditText getEditText() {
        return this.inputBoxWithClear.getEditText();
    }
}
