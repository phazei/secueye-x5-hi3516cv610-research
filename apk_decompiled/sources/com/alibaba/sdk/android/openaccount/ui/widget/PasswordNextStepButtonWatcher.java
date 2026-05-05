package com.alibaba.sdk.android.openaccount.ui.widget;

import android.text.Editable;
import com.alibaba.sdk.android.openaccount.ui.helper.ITextChangeListner;

/* JADX INFO: loaded from: classes.dex */
public class PasswordNextStepButtonWatcher extends NextStepButtonWatcher {
    private ITextChangeListner mListner;

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.NextStepButtonWatcher, android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        super.afterTextChanged(editable);
        this.mListner.onTextChange(editable.toString());
    }

    public void setListner(ITextChangeListner iTextChangeListner) {
        this.mListner = iTextChangeListner;
    }
}
