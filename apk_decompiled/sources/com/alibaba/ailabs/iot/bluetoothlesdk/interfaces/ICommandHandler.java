package com.alibaba.ailabs.iot.bluetoothlesdk.interfaces;

/* JADX INFO: loaded from: classes.dex */
public interface ICommandHandler {
    boolean canHandle(int i, int[] iArr);

    void handleData(int i, int[] iArr);
}
