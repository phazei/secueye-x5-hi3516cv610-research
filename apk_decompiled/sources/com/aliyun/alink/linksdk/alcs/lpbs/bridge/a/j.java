package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import com.aliyun.alink.linksdk.alcs.api.ICAConnectListener;
import com.aliyun.alink.linksdk.alcs.api.ICAMsgListener;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthPairs;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthParams;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.alcs.data.ica.ICARspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProviderListener;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ICALocalAuthProvider.java */
/* JADX INFO: loaded from: classes2.dex */
public class j implements IAuthProvider {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f4075c = "[AlcsLPBS]ICALocalAuthProvider";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.d f4076a = new com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.e();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.f f4077b;

    public j(com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.f fVar) {
        this.f4077b = fVar;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider
    public void queryAuthInfo(final PalDeviceInfo palDeviceInfo, String str, Object obj, final IAuthProviderListener iAuthProviderListener) {
        com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.f fVar;
        if (iAuthProviderListener == null) {
            ALog.e(f4075c, "queryAuthInfo listener null");
            return;
        }
        if (palDeviceInfo == null || obj == null || (fVar = this.f4077b) == null) {
            ALog.e(f4075c, "queryAuthInfo palDeviceInfo or params or mStoragenull");
            iAuthProviderListener.onComplete(palDeviceInfo, null);
            return;
        }
        ICAAuthParams iCAAuthParamsA = fVar.a(palDeviceInfo.getDevId());
        ALog.d(f4075c, "mStorage getAccessInfo icaAuthParams:" + iCAAuthParamsA);
        if (iCAAuthParamsA != null) {
            iAuthProviderListener.onComplete(palDeviceInfo, iCAAuthParamsA);
        } else {
            this.f4076a.a((ICADiscoveryDeviceInfo) obj, new ICAConnectListener() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.j.1
                @Override // com.aliyun.alink.linksdk.alcs.api.ICAConnectListener
                public void onLoad(int i, String str2, ICADeviceInfo iCADeviceInfo) {
                    if (i != 200) {
                        iAuthProviderListener.onComplete(palDeviceInfo, null);
                    } else {
                        final ICAAuthPairs iCAAuthPairsA = com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.b.a("0");
                        j.this.f4076a.a(iCAAuthPairsA.authServerParams, new ICAMsgListener() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.j.1.1
                            @Override // com.aliyun.alink.linksdk.alcs.api.ICAMsgListener
                            public void onLoad(ICARspMessage iCARspMessage) {
                                if (iCARspMessage == null || iCARspMessage.code != 0) {
                                    j.this.f4076a.a();
                                    iAuthProviderListener.onComplete(palDeviceInfo, null);
                                } else {
                                    j.this.f4077b.a(palDeviceInfo.getDevId(), iCAAuthPairsA.authParams.accessKey, iCAAuthPairsA.authParams.accessToken);
                                    j.this.f4076a.a();
                                    iAuthProviderListener.onComplete(palDeviceInfo, iCAAuthPairsA.authParams);
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
