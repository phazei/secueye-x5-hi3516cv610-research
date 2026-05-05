package com.aliyun.alink.business.devicecenter.channel.coap.deliver;

import com.aliyun.alink.business.devicecenter.cache.ProvisionDeviceInfoCache;
import com.aliyun.alink.business.devicecenter.channel.BaseChainProcessor;
import com.aliyun.alink.business.devicecenter.channel.coap.CoAPClient;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.taobao.accs.utl.BaseMonitor;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class CoAPNotifyDeliver extends BaseChainProcessor<IAlcsCoAPResHandler> implements IAlcsCoAPResHandler {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public AlcsCoAPResponse f3465b = null;

    public void ack(AlcsCoAPRequest alcsCoAPRequest) {
        if (alcsCoAPRequest == null) {
            ALog.w("CoAPNotifyDeliver", "onRecRequest ack request empty.");
            return;
        }
        this.f3465b = AlcsCoAPResponse.createResponse(alcsCoAPRequest, AlcsCoAPConstant.ResponseCode._UNKNOWN_SUCCESS_CODE);
        this.f3465b.setToken(alcsCoAPRequest.getToken());
        this.f3465b.setMID(alcsCoAPRequest.getMID());
        this.f3465b.setPayload("{\"id\":" + alcsCoAPRequest.getMID() + ", \"code\":200, \"data\":{}}");
        ALog.d(BaseMonitor.COUNT_ACK, "ack token=" + alcsCoAPRequest.getTokenString() + ",msgId=" + alcsCoAPRequest.getMID());
        ThreadPool.submit(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.channel.coap.deliver.CoAPNotifyDeliver.1
            @Override // java.lang.Runnable
            public void run() {
                CoAPClient.getInstance().sendResponse(CoAPNotifyDeliver.this.f3465b);
            }
        });
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler
    public void onRecRequest(AlcsCoAPContext alcsCoAPContext, AlcsCoAPRequest alcsCoAPRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append("onRecRequest request=");
        sb.append(alcsCoAPRequest != null ? alcsCoAPRequest.getPayloadString() : TmpConstant.GROUP_ROLE_UNKNOWN);
        ALog.d("CoAPNotifyDeliver", sb.toString());
        if (ProvisionDeviceInfoCache.getInstance().getCache() != null && ProvisionDeviceInfoCache.getInstance().handleNotifyCache(alcsCoAPRequest)) {
            ALog.d("CoAPNotifyDeliver", "ProvisionDeviceInfoCache");
            ArrayList<T> arrayList = this.chainList;
            if (arrayList == 0 || arrayList.size() == 0) {
                ack(alcsCoAPRequest);
                return;
            }
        }
        ArrayList<T> arrayList2 = this.chainList;
        if (arrayList2 == 0 || arrayList2.size() == 0) {
            ALog.d("CoAPNotifyDeliver", "onRecRequest chainList empty. to handle cache.");
            return;
        }
        for (int i = 0; i < this.chainList.size(); i++) {
            ArrayList<T> arrayList3 = this.chainList;
            if (arrayList3 != 0 && arrayList3.get(i) != null) {
                ((IAlcsCoAPResHandler) this.chainList.get(i)).onRecRequest(alcsCoAPContext, alcsCoAPRequest);
            }
        }
    }
}
