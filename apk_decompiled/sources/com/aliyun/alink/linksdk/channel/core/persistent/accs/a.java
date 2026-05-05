package com.aliyun.alink.linksdk.channel.core.persistent.accs;

import android.content.Context;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.ASend;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener;
import com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeRrpcListener;
import com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentConnectState;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentInitParams;
import com.aliyun.alink.linksdk.channel.core.persistent.event.PersistentEventDispatcher;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.PersisitentNetParams;
import com.aliyun.alink.linksdk.tools.ALog;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsException;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: compiled from: AccsNet.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements IPersisitentNet {

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.channel.core.persistent.accs.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: AccsNet.java */
    public static class C0214a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final a f4107a = new a();
    }

    public static a a() {
        return C0214a.f4107a;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.base.INet
    public ASend asyncSend(ARequest aRequest, IOnCallListener iOnCallListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "asyncSend unsupported with accs channel.");
        return null;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void destroy() {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "destroy() called");
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void dynamicRegister(Context context, PersistentInitParams persistentInitParams, IOnCallListener iOnCallListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "dynamicRegister unsupported with accs channel.");
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public PersistentConnectState getConnectState() {
        boolean zIsAccsConnected;
        com.aliyun.alink.linksdk.channel.core.utils.a.a("ConnectSDK", "getConnectState()");
        try {
            zIsAccsConnected = ACCSClient.getAccsClient(AccsClientConfig.DEFAULT_CONFIGTAG).isAccsConnected();
        } catch (AccsException e) {
            e.printStackTrace();
            com.aliyun.alink.linksdk.channel.core.utils.a.d("AccsNet", "getConnectState getAccsClient AccsException=" + e);
            zIsAccsConnected = false;
        } catch (Exception e2) {
            com.aliyun.alink.linksdk.channel.core.utils.a.d("AccsNet", "getConnectState getAccsClient e=" + e2);
            zIsAccsConnected = false;
        }
        return zIsAccsConnected ? PersistentConnectState.CONNECTED : PersistentConnectState.DISCONNECTED;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void init(Context context, PersistentInitParams persistentInitParams) {
        if (getConnectState() == PersistentConnectState.CONNECTED) {
            PersistentEventDispatcher.getInstance().broadcastMessage(1, null, null, 200, "is initing or inited.");
        } else {
            PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, 4300, "init tg_push sdk first or wait tg_push init done.");
        }
        EventBus.getDefault().register(this);
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public boolean isDeiniting() {
        return false;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void openLog(boolean z) {
        if (z) {
            ALog.setLevel((byte) 1);
        } else {
            ALog.setLevel((byte) 4);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void reconnect() {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "reconnect unsupported with accs channel.");
    }

    @Override // com.aliyun.alink.linksdk.channel.core.base.INet
    public void retry(ASend aSend) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "retry unsupported with accs channel.");
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void subscribe(String str, IOnSubscribeListener iOnSubscribeListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "subscribe unsupported with accs channel.");
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void subscribeRrpc(String str, IOnSubscribeRrpcListener iOnSubscribeRrpcListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "unSbscribeRrpc unsupported with accs channel.");
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void unSubscribe(String str, IOnSubscribeListener iOnSubscribeListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "unSubscribe unsupported with accs channel.");
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void destroy(long j, Object obj, Object obj2) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "destroy() called with: quiesceTimeout = [" + j + "], userContext = [" + obj + "], callback = [" + obj2 + "]");
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void subscribe(String str, PersisitentNetParams persisitentNetParams, IOnSubscribeListener iOnSubscribeListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("AccsNet", "subscribe with params unsupported with accs channel.");
    }
}
