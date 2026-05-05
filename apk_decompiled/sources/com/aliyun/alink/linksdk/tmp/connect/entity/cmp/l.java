package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.cmp.connect.channel.MqttRrpcRequest;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcHandle;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;
import com.aliyun.alink.linksdk.tmp.device.payload.cloud.ResponsePayload;
import com.aliyun.alink.linksdk.tmp.device.payload.cloud.UpdateBlackListRequestPayload;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.lang.ref.WeakReference;

/* JADX INFO: compiled from: UpdateBlackListHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class l implements IConnectRrpcListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4267a = "[Tmp]UpdateBlackListHandler";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected WeakReference<com.aliyun.alink.linksdk.tmp.device.a> f4268b;

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
    public void onResponseFailed(ARequest aRequest, AError aError) {
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
    public void onResponseSuccess(ARequest aRequest) {
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
    public void onSubscribeFailed(ARequest aRequest, AError aError) {
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
    public void onSubscribeSuccess(ARequest aRequest) {
    }

    public l(com.aliyun.alink.linksdk.tmp.device.a aVar) {
        this.f4268b = new WeakReference<>(aVar);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
    public void onReceived(ARequest aRequest, IConnectRrpcHandle iConnectRrpcHandle) {
        ALog.d(f4267a, "onReceive aRequest:" + aRequest + " iConnectRrpcHandle:" + iConnectRrpcHandle);
        if (aRequest != null && (aRequest instanceof MqttRrpcRequest)) {
            UpdateBlackListRequestPayload updateBlackListRequestPayload = (UpdateBlackListRequestPayload) GsonUtils.fromJson(String.valueOf(((MqttRrpcRequest) aRequest).payloadObj), new TypeToken<UpdateBlackListRequestPayload>() { // from class: com.aliyun.alink.linksdk.tmp.connect.entity.cmp.l.1
            }.getType());
            if (updateBlackListRequestPayload != null && updateBlackListRequestPayload.params != null) {
                com.aliyun.alink.linksdk.tmp.device.a aVar = this.f4268b.get();
                if (aVar != null) {
                    aVar.e(updateBlackListRequestPayload.params.blacklist);
                }
                iConnectRrpcHandle.onRrpcResponse(null, new ResponsePayload(updateBlackListRequestPayload.id, 200));
                return;
            }
            iConnectRrpcHandle.onRrpcResponse(null, new ResponsePayload(updateBlackListRequestPayload.id, 300));
            return;
        }
        ALog.e(f4267a, "onNotify aMessage  error");
    }

    public void a() {
        ALog.d(f4267a, "unSubTopic");
    }
}
