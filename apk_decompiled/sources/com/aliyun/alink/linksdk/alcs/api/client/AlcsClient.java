package com.aliyun.alink.linksdk.alcs.api.client;

import android.os.Handler;
import android.os.Message;
import com.aliyun.alink.linksdk.alcs.api.AlcsCoAPSdk;
import com.aliyun.alink.linksdk.alcs.api.utils.ErrorInfo;
import com.aliyun.alink.linksdk.alcs.api.utils.LogCat;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAP;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;
import com.aliyun.alink.linksdk.alcs.coap.resources.AlcsCoAPResource;
import com.aliyun.alink.linksdk.tools.ALog;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsClient {
    static final String TAG = "[alcs_coap_sdk]AlcsClient";
    protected AlcsCoAP mAlcsCoap;
    protected AlcsCoAPContext mAlcsContext;
    protected AlcsClientConfig mConfig;
    protected IHeartBeatHandler mHBListener;
    protected HBRun mHbRun = new HBRun(this);
    protected boolean mIsIniting;
    protected IDeviceStateListener mStateListener;

    public AlcsClient(AlcsClientConfig alcsClientConfig) {
        this.mConfig = alcsClientConfig;
    }

    public void init(IDeviceHandler iDeviceHandler) {
        LogCat.i(TAG, "init AlcsClient mAlcsContext:" + this.mAlcsContext + " mConfig:" + this.mConfig);
        if (this.mConfig == null) {
            ALog.e(TAG, "init AlcsClient error config null");
            iDeviceHandler.onFail(null, null);
            return;
        }
        if (this.mAlcsContext == null) {
            this.mAlcsContext = new AlcsCoAPContext();
            this.mAlcsCoap = new AlcsCoAP();
            this.mAlcsCoap.createNewCoAPContext(this.mAlcsContext);
            this.mAlcsCoap.alcsStart(this.mAlcsContext.getContextId());
            this.mAlcsCoap.initAuth(this.mAlcsContext.getContextId(), this.mConfig.getProductKey(), this.mConfig.getDeviceName(), 3);
        }
        if (!this.mConfig.isNeddAuth()) {
            iDeviceHandler.onSuccess(null);
            startHb();
        } else {
            this.mIsIniting = true;
            ALog.d(TAG, "start auth");
            this.mAlcsCoap.authHasKey(this.mAlcsContext.getContextId(), this.mConfig.getDstAddr(), this.mConfig.getDstPort(), this.mConfig.getProductKey(), this.mConfig.getDeviceName(), this.mConfig.getAccessKey(), this.mConfig.getAccessToken(), new ClientAuthHandler(this, iDeviceHandler));
        }
    }

    public long sendRequest(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        ALog.d(TAG, "sendRequest ");
        if (isInit()) {
            return this.mAlcsCoap.sendRequest(this.mAlcsContext.getContextId(), alcsCoAPRequest, iAlcsCoAPReqHandler);
        }
        return 0L;
    }

    public long sendRequestSecure(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        ALog.d(TAG, "sendRequestSecure ");
        if (isInit()) {
            return this.mAlcsCoap.sendRequestS(this.mAlcsContext.getContextId(), alcsCoAPRequest, this.mConfig.getProductKey(), this.mConfig.getDeviceName(), iAlcsCoAPReqHandler);
        }
        return 0L;
    }

    public long startDiscover(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        ALog.d(TAG, "startDiscover ");
        if (alcsCoAPRequest != null) {
            alcsCoAPRequest.setMulticast(1);
        }
        if (isInit()) {
            return this.mAlcsCoap.sendRequest(this.mAlcsContext.getContextId(), alcsCoAPRequest, iAlcsCoAPReqHandler);
        }
        return 0L;
    }

    public boolean stopDiscover(long j) {
        ALog.d(TAG, "stopDiscover msgId:" + j);
        return cancelRequest(j);
    }

    public long subscribe(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        ALog.d(TAG, "subscribe()");
        return sendRequestSecure(alcsCoAPRequest.setObserve(), iAlcsCoAPReqHandler);
    }

    public long unSubscribe(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        ALog.d(TAG, "unSubscribe()");
        return sendRequestSecure(alcsCoAPRequest.setObserveCancel(), iAlcsCoAPReqHandler);
    }

    public boolean cancelRequest(long j) {
        ALog.d(TAG, "cancelRequest msgId:" + j);
        if (isInit()) {
            return this.mAlcsCoap.cancelMessage(this.mAlcsContext.getContextId(), j);
        }
        return false;
    }

    public boolean unRegisterResource(String str) {
        LogCat.i(TAG, "unRegisterResource path:" + str);
        return isInit() && this.mAlcsCoap.unRegisterResourceByPath(this.mAlcsContext.getContextId(), str) > 0;
    }

    public boolean sendResponse(AlcsCoAPResponse alcsCoAPResponse) {
        ALog.d(TAG, "sendResponse ");
        if (isInit()) {
            return this.mAlcsCoap.sendResponse(this.mAlcsContext.getContextId(), alcsCoAPResponse);
        }
        return false;
    }

    public boolean sendResponseSecure(AlcsCoAPResponse alcsCoAPResponse) {
        ALog.d(TAG, "sendResponseSecure ");
        if (isInit()) {
            return this.mAlcsCoap.sendResponseS(this.mAlcsContext.getContextId(), alcsCoAPResponse, this.mConfig.getProductKey(), this.mConfig.getDeviceName());
        }
        return false;
    }

    public boolean registerAllResource(AlcsCoAPResource alcsCoAPResource) {
        ALog.d(TAG, "registerAllResource ");
        if (alcsCoAPResource == null) {
            ALog.e(TAG, "registerAllResource resource null");
            return false;
        }
        this.mAlcsCoap.registerAllResource(this.mAlcsContext.getContextId(), alcsCoAPResource, this.mConfig.getProductKey(), this.mConfig.getDeviceName());
        return true;
    }

    public void stop() {
        AlcsCoAPContext alcsCoAPContext;
        ALog.d(TAG, "stop ");
        AlcsCoAP alcsCoAP = this.mAlcsCoap;
        if (alcsCoAP == null || (alcsCoAPContext = this.mAlcsContext) == null) {
            return;
        }
        alcsCoAP.alcsStop(alcsCoAPContext.getContextId());
        destroy();
        this.mIsIniting = false;
    }

    protected void destroy() {
        AlcsCoAP alcsCoAP;
        ALog.d(TAG, "destroy ");
        AlcsCoAPContext alcsCoAPContext = this.mAlcsContext;
        if (alcsCoAPContext != null && (alcsCoAP = this.mAlcsCoap) != null) {
            alcsCoAP.freeCoAPContext(alcsCoAPContext.getContextId());
        }
        this.mAlcsCoap = null;
        this.mAlcsContext = null;
    }

    public void onAuth(boolean z, int i, IDeviceHandler iDeviceHandler) {
        this.mIsIniting = false;
        ALog.d(TAG, "onAuth isSuccess:" + z);
        if (z) {
            if (iDeviceHandler != null) {
                iDeviceHandler.onSuccess(null);
            } else {
                ALog.d(TAG, "onAuth success callback empty");
            }
            startHb();
            return;
        }
        if (iDeviceHandler != null) {
            iDeviceHandler.onFail(null, new ErrorInfo(i, "errror"));
        } else {
            ALog.d(TAG, "onAuth faile callback empty");
        }
    }

    public boolean isInit() {
        ALog.d(TAG, "isInit mIsIniting:" + this.mIsIniting + " mAlcsCoap:" + this.mAlcsCoap);
        return (this.mAlcsCoap == null || this.mIsIniting) ? false : true;
    }

    public boolean isServerOnline() {
        if (isInit()) {
            return this.mAlcsCoap.isServerDevOnline(this.mAlcsContext.getContextId(), this.mConfig.getDstAddr(), this.mConfig.getDstPort(), this.mConfig.getProductKey(), this.mConfig.getDeviceName());
        }
        return false;
    }

    public void setHBListener(IHeartBeatHandler iHeartBeatHandler) {
        this.mHBListener = iHeartBeatHandler;
    }

    public void setDeviceStateListener(IDeviceStateListener iDeviceStateListener) {
        this.mStateListener = iDeviceStateListener;
    }

    public void startHb() {
        if (this.mConfig != null) {
            ALog.d(TAG, "startHb  ip:" + this.mConfig.getDstAddr() + " port:" + this.mConfig.getDstPort() + " hbtt:" + this.mConfig.mHeartBeatTimeout);
        } else {
            ALog.e(TAG, "startHb mConfig null");
        }
        if (isInit()) {
            AlcsClientConfig alcsClientConfig = this.mConfig;
            if (alcsClientConfig != null && alcsClientConfig.mHeartBeatTimeout > 0) {
                ALog.d(TAG, "startHb mHeartBeatTimeout:" + this.mConfig.mHeartBeatTimeout);
                AlcsCoAPSdk.mHandler.postDelayed(this.mHbRun, (long) this.mConfig.mHeartBeatTimeout);
                return;
            }
            ALog.e(TAG, "startHb error");
        }
    }

    protected static class MessageHandler extends Handler {
        protected MessageHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }

    protected static class HBRun implements Runnable {
        private WeakReference<AlcsClient> mAlcsClientRef;

        public HBRun(AlcsClient alcsClient) {
            this.mAlcsClientRef = new WeakReference<>(alcsClient);
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // java.lang.Runnable
        public void run() {
            AlcsClient alcsClient = this.mAlcsClientRef.get();
            if (alcsClient == null) {
                ALog.e(AlcsClient.TAG, "mAlcsClientRef get return null");
                return;
            }
            boolean zIsServerOnline = alcsClient.isServerOnline();
            ALog.d(AlcsClient.TAG, "HBRun run mHBListener:" + alcsClient.mHBListener + " isOnlien:" + zIsServerOnline + " mStateListener:" + alcsClient.mStateListener);
            if (alcsClient.mStateListener != null) {
                alcsClient.mStateListener.onDeviceStateChange(zIsServerOnline ? 1 : 0);
            }
            if (zIsServerOnline) {
                if (alcsClient.mHBListener != null) {
                    alcsClient.mHBListener.onBeat(alcsClient.mConfig.getDstAddr(), alcsClient.mConfig.getDstPort());
                }
                alcsClient.startHb();
            } else if (alcsClient.mHBListener != null) {
                alcsClient.mHBListener.onTimeout(alcsClient.mConfig.getDstAddr(), alcsClient.mConfig.getDstPort());
            }
        }
    }
}
