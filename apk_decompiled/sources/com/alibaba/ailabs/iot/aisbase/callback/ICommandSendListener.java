package com.alibaba.ailabs.iot.aisbase.callback;

/* JADX INFO: loaded from: classes.dex */
public interface ICommandSendListener {
    void onFailure(int i, String str);

    void onSent();
}
