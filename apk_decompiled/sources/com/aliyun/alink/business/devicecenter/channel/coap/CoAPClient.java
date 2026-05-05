package com.aliyun.alink.business.devicecenter.channel.coap;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.channel.coap.deliver.CoAPNotifyDeliver;
import com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAPSend;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler;
import com.aliyun.alink.linksdk.alcs.coap.resources.AlcsCoAPResource;

/* JADX INFO: loaded from: classes.dex */
public class CoAPClient implements IAlcsCoAPSend {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static CoAPClient f3462a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public AlcsCoAPService f3463b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public CoAPNotifyDeliver f3464c = null;

    public CoAPClient() {
        this.f3463b = null;
        if (DCEnvHelper.hasCoapAbilitiAB()) {
            this.f3463b = new AlcsCoAPService();
            initCoApService();
        }
    }

    public static CoAPClient getInstance() {
        if (f3462a == null) {
            synchronized (CoAPClient.class) {
                if (f3462a == null) {
                    f3462a = new CoAPClient();
                }
            }
        }
        return f3462a;
    }

    public final void a(AlcsCoAPResource alcsCoAPResource) {
        initCoApService();
        AlcsCoAPService alcsCoAPService = this.f3463b;
        if (alcsCoAPService != null) {
            alcsCoAPService.registerResource(alcsCoAPResource);
        }
    }

    public void addNotifyListener(IAlcsCoAPResHandler iAlcsCoAPResHandler) {
        CoAPNotifyDeliver coAPNotifyDeliver = this.f3464c;
        if (coAPNotifyDeliver == null) {
            ALog.w("CoAPClient", "this.handler is null");
        } else {
            coAPNotifyDeliver.addChain(iAlcsCoAPResHandler);
            initCoApService();
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAPSend
    public boolean cancelMessage(long j) {
        if (this.f3463b == null) {
            ALog.w("CoAPClient", "service is null");
            return false;
        }
        ALog.d("CoAPClient", "cancelMessage msgId=" + j);
        return this.f3463b.cancelMessage(j);
    }

    public AlcsCoAPServiceStatus getCoAPServiceStatus() {
        AlcsCoAPService alcsCoAPService = this.f3463b;
        return alcsCoAPService != null ? alcsCoAPService.getServiceStatus() : AlcsCoAPServiceStatus.IDLE;
    }

    public void initCoApService() {
        if (this.f3463b == null) {
            ALog.w("CoAPClient", "service is null");
            return;
        }
        ALog.d("CoAPClient", "initCoapService");
        if (this.f3463b.getServiceStatus() != AlcsCoAPServiceStatus.IDLE) {
            if (this.f3463b.getServiceStatus() == AlcsCoAPServiceStatus.INITED) {
                this.f3463b.startCoAPService();
                return;
            } else {
                if (this.f3463b.getServiceStatus() == AlcsCoAPServiceStatus.STOPPED) {
                    this.f3463b.startCoAPService();
                    return;
                }
                return;
            }
        }
        AlcsCoAPContext alcsCoAPContext = new AlcsCoAPContext();
        CoAPNotifyDeliver coAPNotifyDeliver = this.f3464c;
        if (coAPNotifyDeliver == null) {
            this.f3464c = new CoAPNotifyDeliver();
        } else {
            coAPNotifyDeliver.clearChain();
        }
        AlcsCoAPResource alcsCoAPResource = new AlcsCoAPResource(AlinkConstants.KEY_DEVICE_INFO, true);
        alcsCoAPResource.setPath(AlinkConstants.COAP_PATH_NOTIFY_DEVICE_INFO);
        alcsCoAPResource.setPermission(3);
        alcsCoAPResource.setHandler(this.f3464c);
        alcsCoAPContext.setPort(5683);
        this.f3463b.initCoAPService(alcsCoAPContext, alcsCoAPResource);
        this.f3463b.startCoAPService();
    }

    public void printResponse(AlcsCoAPContext alcsCoAPContext, AlcsCoAPResponse alcsCoAPResponse) {
        String str;
        if (alcsCoAPResponse != null) {
            str = "payload=" + alcsCoAPResponse.getPayloadString() + ",token=" + alcsCoAPResponse.getTokenString() + ",coapContext=" + alcsCoAPContext + ",response=" + alcsCoAPResponse;
        } else {
            str = null;
        }
        ALog.d("CoAPClient", "response[" + str + "]");
    }

    public void registerResource(String str, String str2) {
        if (this.f3464c == null) {
            ALog.w("CoAPClient", "this.handler is null");
            return;
        }
        AlcsCoAPResource alcsCoAPResource = new AlcsCoAPResource(str, true);
        alcsCoAPResource.setPath(str2);
        alcsCoAPResource.setPermission(3);
        alcsCoAPResource.setHandler(this.f3464c);
        a(alcsCoAPResource);
    }

    public void releaseCoAPService() {
        ALog.d("CoAPClient", "releaseCoAPService called.");
        CoAPNotifyDeliver coAPNotifyDeliver = this.f3464c;
        if (coAPNotifyDeliver != null) {
            coAPNotifyDeliver.clearChain();
        }
        AlcsCoAPService alcsCoAPService = this.f3463b;
        if (alcsCoAPService != null) {
            alcsCoAPService.deInitCoAPService();
        }
    }

    public void removeNotifyListener(IAlcsCoAPResHandler iAlcsCoAPResHandler) {
        CoAPNotifyDeliver coAPNotifyDeliver = this.f3464c;
        if (coAPNotifyDeliver == null) {
            ALog.w("CoAPClient", "this.handler is null");
        } else {
            coAPNotifyDeliver.removeChain(iAlcsCoAPResHandler);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAPSend
    public long sendRequest(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        String str;
        if (this.f3463b == null) {
            ALog.w("CoAPClient", "service is null");
            return -1L;
        }
        ALog.d("CoAPClient", "sendRequest call.");
        initCoApService();
        long jSendRequest = this.f3463b.sendRequest(alcsCoAPRequest, iAlcsCoAPReqHandler);
        if (alcsCoAPRequest == null) {
            str = "";
        } else {
            str = ",request=[CoAPUri=" + alcsCoAPRequest.getURI() + ",token=" + alcsCoAPRequest.getTokenString() + ",payload=" + alcsCoAPRequest.getPayloadString() + "]";
        }
        ALog.d("CoAPClient", "sendRequest msgId=" + jSendRequest + str);
        return jSendRequest;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.coap.listener.IAlcsCoAPSend
    public boolean sendResponse(AlcsCoAPResponse alcsCoAPResponse) {
        String str;
        if (this.f3463b == null) {
            ALog.w("CoAPClient", "service is null");
            return false;
        }
        if (alcsCoAPResponse == null) {
            str = "";
        } else {
            str = ",response=[token=" + alcsCoAPResponse.getTokenString() + ",payload=" + alcsCoAPResponse.getPayloadString() + "]";
        }
        ALog.d("CoAPClient", "sendResponse " + str);
        initCoApService();
        return this.f3463b.sendResponse(alcsCoAPResponse);
    }

    public long subcribe(String str, String str2, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        if (this.f3463b == null) {
            ALog.w("CoAPClient", "service is null");
            return -1L;
        }
        ALog.llog((byte) 3, "CoAPClient", "subcribe() called with: data = [" + str + "], coapUri = [" + str2 + "]");
        try {
            if (TextUtils.isEmpty(str)) {
                ALog.w("CoAPClient", "subcribe request data is empty.");
                return -1L;
            }
            if (TextUtils.isEmpty(str2)) {
                ALog.w("CoAPClient", "subcribe request coap uri is empty.");
                return -1L;
            }
            AlcsCoAPRequest alcsCoAPRequest = new AlcsCoAPRequest(AlcsCoAPConstant.Code.GET, AlcsCoAPConstant.Type.NON);
            alcsCoAPRequest.setPayload(str);
            alcsCoAPRequest.setURI(str2);
            alcsCoAPRequest.setObserve();
            return sendRequest(alcsCoAPRequest, iAlcsCoAPReqHandler);
        } catch (Exception e) {
            ALog.w("CoAPClient", "unSubcribe data=" + str + ",coapUri=" + str2 + ",e=" + e);
            return -1L;
        }
    }

    public void unSubcribe(String str, String str2) {
        if (this.f3463b == null) {
            ALog.w("CoAPClient", "service is null");
            return;
        }
        ALog.llog((byte) 3, "CoAPClient", "unSubcribe() called with: data = [" + str + "], coapUri = [" + str2 + "]");
        try {
            if (TextUtils.isEmpty(str)) {
                ALog.w("CoAPClient", "unSubcribe request data is empty.");
                return;
            }
            if (TextUtils.isEmpty(str2)) {
                ALog.w("CoAPClient", "unSubcribe request coap uri is empty.");
                return;
            }
            AlcsCoAPRequest alcsCoAPRequest = new AlcsCoAPRequest(AlcsCoAPConstant.Code.GET, AlcsCoAPConstant.Type.NON);
            alcsCoAPRequest.setPayload(str);
            alcsCoAPRequest.setURI(str2);
            alcsCoAPRequest.setObserveCancel();
            sendRequest(alcsCoAPRequest, null);
        } catch (Exception e) {
            ALog.w("CoAPClient", "unSubcribe data=" + str + ",coapUri=" + str2 + ",e=" + e);
        }
    }
}
