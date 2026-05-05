package com.alibaba.sdk.android.openaccount;

import com.alibaba.sdk.android.openaccount.message.Message;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountSDKException extends RuntimeException {
    private Message message;

    public OpenAccountSDKException(Message message, Throwable th) {
        super(message == null ? "" : message.message, th);
        this.message = message;
    }

    public OpenAccountSDKException(Message message) {
        super(message == null ? "" : message.message);
        this.message = message;
    }

    public Message getSDKMessage() {
        return this.message;
    }
}
