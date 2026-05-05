package com.aliyun.alink.linksdk.tmp.utils;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public class ErrorInfo {
    private int mErrorCode;
    private String mErrorMsg;

    public ErrorInfo(int i, String str) {
        this.mErrorCode = i;
        this.mErrorMsg = str;
    }

    public ErrorInfo(AError aError) {
        if (aError != null) {
            this.mErrorCode = aError.getCode();
            this.mErrorMsg = aError.getMsg();
        }
    }

    public boolean isSuccess() {
        int i = this.mErrorCode;
        if (i != 0) {
            return i >= 200 && i < 300;
        }
        return true;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public void setErrorCode(int i) {
        this.mErrorCode = i;
    }

    public String getErrorMsg() {
        return this.mErrorMsg;
    }

    public void setErrorMsg(String str) {
        this.mErrorMsg = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mErrorCode);
        sb.append(TextUtils.isEmpty(this.mErrorMsg) ? TmpConstant.GROUP_ROLE_UNKNOWN : this.mErrorMsg);
        return sb.toString();
    }
}
