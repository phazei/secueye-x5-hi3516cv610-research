package com.alibaba.sdk.android.openaccount.ui.helper;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PasswordCheckHelper {
    private int mCount;
    private List<ICheckListener> mList = new ArrayList();
    private String mPassword;

    public PasswordCheckHelper(String str) {
        this.mPassword = str;
        addCheckRules(new LowerCaseCheckFactory());
        addCheckRules(new UpperCaseCheckFactory());
        addCheckRules(new NumberCheckFactory());
        addCheckRules(new SpecialCharCheckFactory());
    }

    public void addCheckRules(ICheckListener iCheckListener) {
        this.mList.add(iCheckListener);
    }

    public void check(IPassWordCheckListener iPassWordCheckListener) {
        if (iPassWordCheckListener == null) {
            return;
        }
        if (TextUtils.isEmpty(this.mPassword) || this.mPassword.length() < 8) {
            iPassWordCheckListener.onCheckPassword(0);
            return;
        }
        Iterator<ICheckListener> it = this.mList.iterator();
        while (it.hasNext()) {
            if (it.next().onCheck(this.mPassword) > 0) {
                this.mCount++;
            }
        }
        iPassWordCheckListener.onCheckPassword(this.mCount - 1);
    }

    public void setPassword(String str) {
        this.mPassword = str;
        this.mCount = 0;
    }
}
