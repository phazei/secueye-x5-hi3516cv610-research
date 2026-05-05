package com.aliyun.alink.business.devicecenter.channel.coap;

import com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAP;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAP;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;
import com.aliyun.alink.linksdk.alcs.coap.resources.AlcsCoAPResource;

/* JADX INFO: loaded from: classes.dex */
public class AlcsCoAPService implements IAlcsCoAP {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public AlcsCoAP f3460b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public long f3459a = -1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public AlcsCoAPServiceStatus f3461c = AlcsCoAPServiceStatus.IDLE;

    public AlcsCoAPService() {
        this.f3460b = null;
        this.f3460b = new AlcsCoAP();
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAPSend
    public boolean cancelMessage(long j) {
        try {
            return this.f3460b.cancelMessage(this.f3459a, j);
        } catch (Exception e) {
            ALog.w("AlcsCoAPService", "cancelMessage exception." + e);
            return false;
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAP
    public void deInitCoAPService() {
        try {
            this.f3460b.freeCoAPContext(this.f3459a);
            this.f3461c = AlcsCoAPServiceStatus.IDLE;
        } catch (Exception e) {
            ALog.w("AlcsCoAPService", "deInitCoAPService freeCoAPContext exception." + e);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAP
    public long getContextId() {
        return this.f3459a;
    }

    public AlcsCoAPServiceStatus getServiceStatus() {
        return this.f3461c;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAP
    public void initCoAPService(AlcsCoAPContext alcsCoAPContext, AlcsCoAPResource alcsCoAPResource) {
        try {
            this.f3459a = this.f3460b.createCoAPContext(alcsCoAPContext, alcsCoAPResource);
            this.f3461c = AlcsCoAPServiceStatus.INITED;
            StringBuilder sb = new StringBuilder();
            sb.append("initCoAPService contexId=");
            sb.append(this.f3459a);
            ALog.d("AlcsCoAPService", sb.toString());
        } catch (Exception e) {
            ALog.w("AlcsCoAPService", "initCoAPService createCoAPContext exception." + e);
        }
    }

    public void registerResource(AlcsCoAPResource alcsCoAPResource) {
        try {
            if (this.f3461c == AlcsCoAPServiceStatus.IDLE) {
                ALog.d("AlcsCoAPService", "registerResource contexId=");
                return;
            }
            if (alcsCoAPResource == null) {
                ALog.d("AlcsCoAPService", "registerResource contexId=");
                return;
            }
            this.f3460b.registerAllResource(this.f3459a, alcsCoAPResource);
            StringBuilder sb = new StringBuilder();
            sb.append("initCoAPService contexId=");
            sb.append(this.f3459a);
            ALog.d("AlcsCoAPService", sb.toString());
        } catch (Exception e) {
            ALog.w("AlcsCoAPService", "initCoAPService createCoAPContext exception." + e);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAPSend
    public long sendRequest(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        try {
            return this.f3460b.sendRequest(this.f3459a, alcsCoAPRequest, iAlcsCoAPReqHandler);
        } catch (Exception e) {
            ALog.w("AlcsCoAPService", "sendRequest sendAlcsRequest exception." + e);
            return -1L;
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAPSend
    public boolean sendResponse(AlcsCoAPResponse alcsCoAPResponse) {
        try {
            return this.f3460b.sendResponse(this.f3459a, alcsCoAPResponse);
        } catch (Exception e) {
            ALog.w("AlcsCoAPService", "sendResponse sendAlcsResponse exception." + e);
            return false;
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAP
    public void startCoAPService() {
        try {
            this.f3460b.alcsStart(this.f3459a);
            this.f3461c = AlcsCoAPServiceStatus.STARTED;
        } catch (Exception e) {
            ALog.w("AlcsCoAPService", "startCoAPService alcsStart exception." + e);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAP
    public void stopCoAPService() {
        try {
            this.f3460b.alcsStop(this.f3459a);
            this.f3461c = AlcsCoAPServiceStatus.STOPPED;
        } catch (Exception e) {
            ALog.w("AlcsCoAPService", "stopCoAPService alcsStop exception." + e);
        }
    }

    public void unRegisterResource(AlcsCoAPResource alcsCoAPResource) {
        try {
            if (this.f3461c == AlcsCoAPServiceStatus.IDLE) {
                ALog.d("AlcsCoAPService", "registerResource context not inited.");
                return;
            }
            if (alcsCoAPResource == null) {
                ALog.d("AlcsCoAPService", "registerResource context not inited.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("initCoAPService contexId=");
            sb.append(this.f3459a);
            ALog.d("AlcsCoAPService", sb.toString());
        } catch (Exception e) {
            ALog.w("AlcsCoAPService", "initCoAPService createCoAPContext exception." + e);
        }
    }
}
