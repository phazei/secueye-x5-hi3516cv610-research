package com.alibaba.sdk.android.openaccount.ui.helper;

import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class SpecialCharCheckFactory implements ICheckListener {
    @Override // com.alibaba.sdk.android.openaccount.ui.helper.ICheckListener
    public int onCheck(String str) {
        int i = 0;
        while (Pattern.compile("\\W").matcher(str).find()) {
            i++;
        }
        if (i <= 0) {
            return 0;
        }
        return i;
    }
}
