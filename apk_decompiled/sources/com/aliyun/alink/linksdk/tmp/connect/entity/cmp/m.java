package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.cmp.connect.channel.MqttRrpcRequest;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcHandle;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;
import com.aliyun.alink.linksdk.tmp.device.payload.cloud.ResponsePayload;
import com.aliyun.alink.linksdk.tmp.device.payload.cloud.UpdatePrefixRequestPayload;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.lang.ref.WeakReference;

/* JADX INFO: compiled from: UpdatePrefixHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class m implements IConnectRrpcListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4270a = "[Tmp]UpdatePrefixHandler";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected WeakReference<com.aliyun.alink.linksdk.tmp.device.a> f4271b;

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

    public m(com.aliyun.alink.linksdk.tmp.device.a aVar) {
        this.f4271b = new WeakReference<>(aVar);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
    public void onReceived(ARequest aRequest, IConnectRrpcHandle iConnectRrpcHandle) {
        ALog.d(f4270a, "onReceive aRequest:" + aRequest + " iConnectRrpcHandle:" + iConnectRrpcHandle);
        if (aRequest != null && (aRequest instanceof MqttRrpcRequest)) {
            UpdatePrefixRequestPayload updatePrefixRequestPayload = (UpdatePrefixRequestPayload) GsonUtils.fromJson(String.valueOf(((MqttRrpcRequest) aRequest).payloadObj), new TypeToken<UpdatePrefixRequestPayload>() { // from class: com.aliyun.alink.linksdk.tmp.connect.entity.cmp.m.1
            }.getType());
            if (updatePrefixRequestPayload != null && updatePrefixRequestPayload.params != null) {
                com.aliyun.alink.linksdk.tmp.device.a aVar = this.f4271b.get();
                if (aVar != null) {
                    aVar.d(updatePrefixRequestPayload.params.prefix);
                }
                iConnectRrpcHandle.onRrpcResponse(null, new ResponsePayload(updatePrefixRequestPayload.id, 200));
                return;
            }
            iConnectRrpcHandle.onRrpcResponse(null, new ResponsePayload(updatePrefixRequestPayload.id, 300));
            return;
        }
        ALog.e(f4270a, "onNotify aMessage  error");
    }

    public void a() {
        ALog.d(f4270a, "unSubTopic");
    }
}
