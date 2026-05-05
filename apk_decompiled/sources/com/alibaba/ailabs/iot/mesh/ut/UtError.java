package com.alibaba.ailabs.iot.mesh.ut;

/* JADX INFO: loaded from: classes.dex */
public enum UtError {
    MESH_SCAN_TIMEOUT("SDKMeshScanTimeout", 20014),
    MESH_DISCONNECT("SDKMeshDisconnect", 20015),
    MESH_LINK_LOSS_OCCURRED("SDKMeshLinkLossOccurred", 20016),
    MESH_INIT_FAILED_STOP("SDKMeshInitFailed", 20017),
    MESH_NETWORK_NOT_CONNECT("SDKMeshNetworkNotConnected", 20004);

    public int code;
    public String msg;

    UtError(String str, int i) {
        this.msg = str;
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
