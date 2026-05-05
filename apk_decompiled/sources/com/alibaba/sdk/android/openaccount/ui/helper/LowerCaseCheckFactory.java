package com.alibaba.sdk.android.openaccount.ui.helper;

import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class LowerCaseCheckFactory implements ICheckListener {
    @Override // com.alibaba.sdk.android.openaccount.ui.helper.ICheckListener
    public int onCheck(String str) {
        int i = 0;
        while (Pattern.compile("[a-z]").matcher(str).find()) {
            i++;
        }
        if (i <= 0) {
            return 0;
        }
        return i;
    }
}
