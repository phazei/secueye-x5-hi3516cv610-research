package com.aliyun.alink.linksdk.channel.core.persistent;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.ASend;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.accs.a;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.PersisitentNetParams;

/* JADX INFO: loaded from: classes2.dex */
public class PersistentNet implements IPersisitentNet {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static String f4102c = "PersistentNet";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static String f4103d = "MQTT";
    public static String e = "ACCS";
    public static String f = "0.8.0";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f4104a = f4103d;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IPersisitentNet f4105b = null;

    public static class InstanceHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final PersistentNet f4106a = new PersistentNet();
    }

    public static PersistentNet getInstance() {
        return InstanceHolder.f4106a;
    }

    public final IPersisitentNet a() {
        if (this.f4105b == null) {
            if (e.equals(this.f4104a)) {
                this.f4105b = a.a();
            } else {
                this.f4105b = b.i();
            }
        }
        return this.f4105b;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.base.INet
    public ASend asyncSend(ARequest aRequest, IOnCallListener iOnCallListener) {
        return a().asyncSend((PersistentRequest) aRequest, iOnCallListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void destroy() {
        a().destroy();
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void dynamicRegister(Context context, PersistentInitParams persistentInitParams, IOnCallListener iOnCallListener) {
        a().dynamicRegister(context, persistentInitParams, iOnCallListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public PersistentConnectState getConnectState() {
        return a().getConnectState();
    }

    public String getDefaultProtocol() {
        return this.f4104a;
    }

    public PersistentInitParams getInitParams() {
        IPersisitentNet iPersisitentNetA = a();
        if (iPersisitentNetA instanceof b) {
            return ((b) iPersisitentNetA).d();
        }
        return null;
    }

    public String getSDKVersion() {
        return f;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void init(Context context, PersistentInitParams persistentInitParams) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a(f4102c, "init(), SDK Ver = " + f);
        a().init(context, persistentInitParams);
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public boolean isDeiniting() {
        return a().isDeiniting();
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void openLog(boolean z) {
        a().openLog(z);
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void reconnect() {
        a().reconnect();
    }

    @Override // com.aliyun.alink.linksdk.channel.core.base.INet
    public void retry(ASend aSend) {
        a().retry(aSend);
    }

    public void setDefaultProtocol(String str) {
        if (str == null || str.equals(f4103d)) {
            this.f4104a = f4103d;
            this.f4105b = b.i();
        } else if (str.equals(e)) {
            this.f4104a = e;
            this.f4105b = a.a();
        }
    }

    public void setReportVersion(String str) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a(f4102c, "setReportVersion() called with: version = [" + str + "], moduleVersion=0.8.0-7fc077a");
        if (TextUtils.isEmpty(str)) {
            return;
        }
        f = str;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void subscribe(String str, IOnSubscribeListener iOnSubscribeListener) {
        a().subscribe(str, iOnSubscribeListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void subscribeRrpc(String str, IOnSubscribeRrpcListener iOnSubscribeRrpcListener) {
        a().subscribeRrpc(str, iOnSubscribeRrpcListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void unSubscribe(String str, IOnSubscribeListener iOnSubscribeListener) {
        a().unSubscribe(str, iOnSubscribeListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void destroy(long j, Object obj, Object obj2) {
        if ((a() instanceof b) && com.aliyun.alink.linksdk.channel.core.utils.b.a("org.eclipse.paho.client.mqttv3.IMqttActionListener")) {
            a().destroy(j, obj, obj2);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void subscribe(String str, PersisitentNetParams persisitentNetParams, IOnSubscribeListener iOnSubscribeListener) {
        a().subscribe(str, persisitentNetParams, iOnSubscribeListener);
    }
}
