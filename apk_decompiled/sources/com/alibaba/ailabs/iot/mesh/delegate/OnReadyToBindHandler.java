package com.alibaba.ailabs.iot.mesh.delegate;

import datasource.MeshConfigCallback;

/* JADX INFO: loaded from: classes.dex */
public interface OnReadyToBindHandler {
    void onReadyToBind(String str, MeshConfigCallback<Boolean> meshConfigCallback);
}
