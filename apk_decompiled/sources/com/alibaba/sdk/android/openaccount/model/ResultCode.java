package com.alibaba.sdk.android.openaccount.model;

import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;

/* JADX INFO: loaded from: classes.dex */
public class ResultCode {
    public int code;
    public String message;

    public ResultCode() {
    }

    public ResultCode(Message message) {
        this(message.code, message.message);
    }

    public ResultCode(int i, String str) {
        this.code = i;
        this.message = str;
    }

    public boolean isSuccess() {
        return this.code == 100;
    }

    public static ResultCode create(Message message) {
        return new ResultCode(message.code, message.message);
    }

    public static ResultCode create(int i, Object... objArr) {
        return new ResultCode(i, MessageUtils.getMessageContent(i, objArr));
    }

    public int hashCode() {
        return 31 + this.code;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.code == ((ResultCode) obj).code;
    }
}
