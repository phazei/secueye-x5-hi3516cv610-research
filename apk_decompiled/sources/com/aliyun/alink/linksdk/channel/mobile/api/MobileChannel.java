package com.aliyun.alink.linksdk.channel.mobile.api;

import a.a.a.a.a.a.a.a;
import a.a.a.a.a.a.b.b;
import a.a.a.a.a.a.b.c;
import android.content.Context;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;

/* JADX INFO: loaded from: classes2.dex */
public class MobileChannel implements IMobileChannel {
    public static final String TAG = "MobileChannel";
    public c mobileChannelImpl;

    public static class InstanceHolder {
        public static final IMobileChannel sInstance = new MobileChannel();
    }

    public static IMobileChannel getInstance() {
        return InstanceHolder.sInstance;
    }

    public static void setOpenLog(boolean z) {
        a.a(z ? 3 : 6);
        b.f1133a = z;
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void asyncSendRequest(String str, Map<String, Object> map, Object obj, IMobileRequestListener iMobileRequestListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.asyncSendRequest(str, map, obj, iMobileRequestListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void ayncSendPublishRequest(String str, Object obj, IMobileRequestListener iMobileRequestListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.ayncSendPublishRequest(str, obj, iMobileRequestListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void bindAccount(String str, IMobileRequestListener iMobileRequestListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.bindAccount(str, iMobileRequestListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void endConnect() {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.endConnect();
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public String getClientId() {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        return this.mobileChannelImpl.getClientId();
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public MobileConnectState getMobileConnectState() {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        return this.mobileChannelImpl.getMobileConnectState();
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void registerConnectListener(boolean z, IMobileConnectListener iMobileConnectListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.registerConnectListener(z, iMobileConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void registerDownstreamListener(boolean z, IMobileDownstreamListener iMobileDownstreamListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.registerDownstreamListener(z, iMobileDownstreamListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void startConnect(Context context, MobileConnectConfig mobileConnectConfig, IMobileConnectListener iMobileConnectListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.startConnect(context, mobileConnectConfig, iMobileConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void subscrbie(String str, IMobileSubscrbieListener iMobileSubscrbieListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.subscrbie(str, iMobileSubscrbieListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void unBindAccount(IMobileRequestListener iMobileRequestListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.unBindAccount(iMobileRequestListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void unRegisterConnectListener(IMobileConnectListener iMobileConnectListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.unRegisterConnectListener(iMobileConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void unRegisterDownstreamListener(IMobileDownstreamListener iMobileDownstreamListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.unRegisterDownstreamListener(iMobileDownstreamListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void unSubscrbie(String str, IMobileSubscrbieListener iMobileSubscrbieListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.unSubscrbie(str, iMobileSubscrbieListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void endConnect(long j, IMqttActionListener iMqttActionListener) {
        if (this.mobileChannelImpl == null) {
            this.mobileChannelImpl = new c();
        }
        this.mobileChannelImpl.endConnect(j, iMqttActionListener);
    }
}
