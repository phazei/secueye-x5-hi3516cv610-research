package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b;

import com.aliyun.alink.linksdk.alcs.api.ICAConnectListener;
import com.aliyun.alink.linksdk.alcs.api.ICAMsgListener;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthServerParams;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADiscoveryDeviceInfo;

/* JADX INFO: compiled from: ICAProvisioner.java */
/* JADX INFO: loaded from: classes2.dex */
public interface d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f4046a = "ServerAuthInfo";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final String f4047b = "core.service.setup";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final String f4048c = "1.0";

    void a();

    void a(ICADiscoveryDeviceInfo iCADiscoveryDeviceInfo, ICAConnectListener iCAConnectListener);

    boolean a(ICAAuthServerParams iCAAuthServerParams, ICAMsgListener iCAMsgListener);
}
