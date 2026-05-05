package com.alibaba.ailabs.iot.mesh.callback;

import com.alibaba.ailabs.iot.mesh.bean.MeshAccessPayload;

/* JADX INFO: loaded from: classes.dex */
public interface MeshMsgListener {
    void onReceiveMeshMessage(byte[] bArr, MeshAccessPayload meshAccessPayload);
}
