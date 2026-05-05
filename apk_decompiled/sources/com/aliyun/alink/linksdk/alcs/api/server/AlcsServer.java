package com.aliyun.alink.linksdk.alcs.api.server;

import com.aliyun.alink.linksdk.alcs.api.utils.LogCat;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAP;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;
import com.aliyun.alink.linksdk.alcs.coap.resources.AlcsCoAPResource;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsServer {
    static final String TAG = "[alcs_coap_sdk]AlcsServer";
    private boolean isServerStart;
    protected AlcsCoAP mAlcsCoap;
    protected AlcsCoAPContext mAlcsContext;
    protected AlcsServerConfig mServerConfig;

    public AlcsServer(AlcsServerConfig alcsServerConfig) {
        this.mServerConfig = alcsServerConfig;
    }

    public void start() {
        LogCat.i(TAG, "start server isServerStart:" + this.isServerStart);
        if (this.isServerStart) {
            return;
        }
        this.mAlcsCoap = new AlcsCoAP();
        this.mAlcsContext = new AlcsCoAPContext();
        this.mAlcsContext.setPort(this.mServerConfig.mUnSafePort);
        this.mAlcsCoap.createNewCoAPContext(this.mAlcsContext);
        this.mAlcsCoap.registerAllResource(this.mAlcsContext.getContextId(), null, this.mServerConfig.getProductKey(), this.mServerConfig.getDeviceName());
        this.mAlcsCoap.initAuth(this.mAlcsContext.getContextId(), this.mServerConfig.getProductKey(), this.mServerConfig.getDeviceName(), 3);
        this.mAlcsCoap.alcsStart(this.mAlcsContext.getContextId());
        if (this.mServerConfig.getPreSecList() != null) {
            for (PrefixSecretNode prefixSecretNode : this.mServerConfig.getPreSecList()) {
                this.mAlcsCoap.addAlcsSvrAccessKey(this.mAlcsContext.getContextId(), prefixSecretNode.getPrefix(), prefixSecretNode.getSecret());
            }
        }
        this.mAlcsCoap.updateAlcsSvrBlackList(this.mAlcsContext.getContextId(), this.mServerConfig.mBlacklist);
        this.isServerStart = true;
    }

    public long sendRequest(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        LogCat.i(TAG, "sendRequest isServerStart:" + this.isServerStart);
        if (this.isServerStart) {
            return this.mAlcsCoap.sendRequest(this.mAlcsContext.getContextId(), alcsCoAPRequest, iAlcsCoAPReqHandler);
        }
        return 0L;
    }

    public long sendRequestSecure(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        LogCat.i(TAG, "sendRequestSecure isServerStart:" + this.isServerStart);
        if (this.isServerStart) {
            return this.mAlcsCoap.sendRequestS(this.mAlcsContext.getContextId(), alcsCoAPRequest, this.mServerConfig.getProductKey(), this.mServerConfig.getDeviceName(), iAlcsCoAPReqHandler);
        }
        return 0L;
    }

    public boolean sendResponse(AlcsCoAPResponse alcsCoAPResponse) {
        LogCat.i(TAG, "sendResponse isServerStart:" + this.isServerStart);
        if (this.isServerStart) {
            return this.mAlcsCoap.sendResponse(this.mAlcsContext.getContextId(), alcsCoAPResponse);
        }
        return false;
    }

    public boolean sendResponseSecure(AlcsCoAPResponse alcsCoAPResponse) {
        LogCat.i(TAG, "sendResponseSecure isServerStart:" + this.isServerStart);
        if (this.isServerStart) {
            return this.mAlcsCoap.sendResponseS(this.mAlcsContext.getContextId(), alcsCoAPResponse, this.mServerConfig.getProductKey(), this.mServerConfig.getDeviceName());
        }
        return false;
    }

    public boolean updateSvrPrefix(String str) {
        LogCat.i(TAG, "updateSvrPrefixSec isServerStart:" + this.isServerStart);
        if (this.isServerStart) {
            AlcsServerConfig alcsServerConfig = this.mServerConfig;
            if (alcsServerConfig != null && alcsServerConfig.getPreSecList() != null) {
                PrefixSecretNode prefixSecretNode = this.mServerConfig.getPreSecList().get(0);
                this.mAlcsCoap.removeAlcsSvrKey(this.mAlcsContext.getContextId(), prefixSecretNode.getPrefix());
                return this.mAlcsCoap.addAlcsSvrAccessKey(this.mAlcsContext.getContextId(), str, prefixSecretNode.getSecret());
            }
            LogCat.e(TAG, "updateSvrPrefixSec mServerConfig:" + this.mServerConfig + " or preseclist null");
        }
        return false;
    }

    public boolean removeSvrKey(String str) {
        ALog.d(TAG, "removeSvrKey authcode:" + str);
        if (this.isServerStart) {
            return this.mAlcsCoap.removeAlcsSvrKey(this.mAlcsContext.getContextId(), str);
        }
        return false;
    }

    public boolean addSvrAccessKey(String str, String str2) {
        ALog.d(TAG, "addSvrAccessKey authcode:" + str);
        if (this.isServerStart) {
            return this.mAlcsCoap.addAlcsSvrAccessKey(this.mAlcsContext.getContextId(), str, str2);
        }
        return false;
    }

    public boolean notifyRes(String str, byte[] bArr) {
        LogCat.i(TAG, "notifyRes isServerStart:" + this.isServerStart + " path:" + str);
        if (this.isServerStart) {
            return this.mAlcsCoap.notifyObserve(this.mAlcsContext.getContextId(), str, bArr);
        }
        return false;
    }

    public boolean unRegisterResource(String str) {
        LogCat.i(TAG, "unRegisterResource isServerStart:" + this.isServerStart + " path:" + str);
        return this.isServerStart && this.mAlcsCoap.unRegisterResourceByPath(this.mAlcsContext.getContextId(), str) > 0;
    }

    public boolean registerAllResource(AlcsCoAPResource alcsCoAPResource) {
        if (alcsCoAPResource == null) {
            ALog.e("AlcsCoAP", "registerAllResource resource null");
            return false;
        }
        this.mAlcsCoap.registerAllResource(this.mAlcsContext.getContextId(), alcsCoAPResource, this.mServerConfig.getProductKey(), this.mServerConfig.getDeviceName());
        return true;
    }

    public synchronized void stop() {
        LogCat.i(TAG, "stop isServerStart:" + this.isServerStart);
        if (this.isServerStart) {
            this.mAlcsCoap.alcsStop(this.mAlcsContext.getContextId());
            this.mAlcsCoap.freeCoAPContext(this.mAlcsContext.getContextId());
            this.mAlcsCoap = null;
            this.mAlcsContext = null;
            this.isServerStart = false;
        }
    }

    public AlcsServerConfig getServerConfig() {
        return this.mServerConfig;
    }

    public void setServerConfig(AlcsServerConfig alcsServerConfig) {
        this.mServerConfig = alcsServerConfig;
    }

    public void updateBlackList(String str) {
        LogCat.i(TAG, "updateBlackList blackList:" + str + " isServerStart:" + this.isServerStart);
        if (this.isServerStart) {
            this.mAlcsCoap.updateAlcsSvrBlackList(this.mAlcsContext.getContextId(), str);
        }
    }
}
