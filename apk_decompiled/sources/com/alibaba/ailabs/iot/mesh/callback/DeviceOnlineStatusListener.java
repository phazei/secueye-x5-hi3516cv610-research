package com.alibaba.ailabs.iot.mesh.callback;

import com.alibaba.ailabs.iot.mesh.TgMeshManager;

/* JADX INFO: loaded from: classes.dex */
public interface DeviceOnlineStatusListener {
    void onOnlineStatusChange(String str, TgMeshManager.DevOnlineStatus devOnlineStatus);
}
