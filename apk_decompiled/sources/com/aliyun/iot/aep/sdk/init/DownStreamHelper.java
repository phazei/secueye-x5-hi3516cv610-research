package com.aliyun.iot.aep.sdk.init;

import android.app.Application;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayConnectConfig;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayConnectState;
import com.aliyun.alink.linksdk.channel.gateway.api.IGatewayConnectListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileChannel;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectConfig;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectState;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.config.AConfigure;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.framework.network.NetworkConnectiveManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import sdk.EnvConfigure;

/* JADX INFO: loaded from: classes2.dex */
public class DownStreamHelper {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static int f4740b = 2000;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private MobileConnectConfig f4741a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Handler f4742c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f4743d;
    private Runnable e;
    private final AtomicBoolean f;
    private final NetworkConnectiveManager.INetworkChangeListener g;

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final DownStreamHelper f4752a = new DownStreamHelper();
    }

    private DownStreamHelper() {
        this.f4743d = false;
        this.e = new Runnable() { // from class: com.aliyun.iot.aep.sdk.init.DownStreamHelper.1
            @Override // java.lang.Runnable
            public void run() {
                if (MobileChannel.getInstance().getMobileConnectState() != MobileConnectState.CONNECTED) {
                    ALog.d("DownStreamHelper", "do re-init mqtt:" + JSON.toJSONString(DownStreamHelper.this.f4741a));
                    DownStreamHelper downStreamHelper = DownStreamHelper.this;
                    downStreamHelper.startMqtt(downStreamHelper.f4741a);
                }
            }
        };
        this.f = new AtomicBoolean(false);
        this.g = new NetworkConnectiveManager.INetworkChangeListener() { // from class: com.aliyun.iot.aep.sdk.init.DownStreamHelper.2
            @Override // com.aliyun.iot.aep.sdk.framework.network.NetworkConnectiveManager.INetworkChangeListener
            public void onNetworkStateChange(NetworkInfo networkInfo, Network network) {
                ALog.d("DownStreamHelper", "onNetworkStateChange() called with: networkInfo = [" + networkInfo + "]");
                if (networkInfo == null || !networkInfo.isConnected()) {
                    return;
                }
                try {
                    if (MobileChannel.getInstance().getMobileConnectState() != MobileConnectState.CONNECTED) {
                        ALog.w("DownStreamHelper", "网络状态变化，重新初始化长连接通道");
                        DownStreamHelper.this.startMqtt(DownStreamHelper.this.f4741a);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ALog.e("DownStreamHelper", e.toString());
                }
            }
        };
        this.f4741a = new MobileConnectConfig();
        this.f4742c = new Handler(Looper.getMainLooper());
    }

    public static final DownStreamHelper getInstance() {
        return a.f4752a;
    }

    private void a(Application application) {
        try {
            if (this.f4743d) {
                return;
            }
            ALog.d("DownStreamHelper", "registerNetworkListener");
            NetworkConnectiveManager.getInstance().initNetworkConnectiveManager(application.getApplicationContext());
            NetworkConnectiveManager.getInstance().registerConnectiveListener(this.g);
            this.f4743d = true;
        } catch (Exception e) {
            e.printStackTrace();
            ALog.e("DownStreamHelper", e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Application application) {
        try {
            this.f4743d = false;
            ALog.d("DownStreamHelper", "unregisterNetworkListener");
            NetworkConnectiveManager.getInstance().unregisterConnectiveListener(this.g);
            NetworkConnectiveManager.getInstance().deinitNetworkConnectiveManager();
        } catch (Exception e) {
            e.printStackTrace();
            ALog.e("DownStreamHelper", e.toString());
        }
    }

    public void startMqtt(String str) {
        if (this.f4741a == null) {
            return;
        }
        a();
        f4740b = 2000;
        MobileConnectConfig mobileConnectConfig = this.f4741a;
        mobileConnectConfig.channelHost = str;
        startMqttOne(mobileConnectConfig);
    }

    public void startMqttOne(MobileConnectConfig mobileConnectConfig) {
        a(AApplication.getInstance());
        startMqtt(mobileConnectConfig);
    }

    public void startMqtt(final MobileConnectConfig mobileConnectConfig) {
        this.f4741a = mobileConnectConfig;
        GlobalConfig.getInstance().getInitConfig().getRegionType();
        if (this.f.get()) {
            ALog.e("DownStreamHelper", "ending connect ongoing, update config and do nothing then");
            return;
        }
        this.f.set(true);
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: com.aliyun.iot.aep.sdk.init.DownStreamHelper.3
            @Override // java.lang.Runnable
            public void run() {
                ALog.d("DownStreamHelper", "startMqtt stateNow：" + MobileChannel.getInstance().getMobileConnectState());
                MobileChannel.getInstance().endConnect(10000L, new IMqttActionListener() { // from class: com.aliyun.iot.aep.sdk.init.DownStreamHelper.3.1
                    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
                    public void onSuccess(IMqttToken iMqttToken) {
                        ALog.d("DownStreamHelper", "mqtt endConnect success");
                        DownStreamHelper.this.f.set(false);
                        DownStreamHelper.this.a(mobileConnectConfig);
                    }

                    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
                    public void onFailure(IMqttToken iMqttToken, Throwable th) {
                        ALog.d("DownStreamHelper", "mqtt endConnect fail");
                        DownStreamHelper.this.f.set(false);
                        DownStreamHelper.this.a(mobileConnectConfig);
                    }
                });
            }
        });
        a(AApplication.getInstance());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(MobileConnectConfig mobileConnectConfig) {
        ALog.d("DownStreamHelper", "startMqttInternal:" + JSON.toJSONString(mobileConnectConfig));
        if (mobileConnectConfig == null) {
            return;
        }
        final AApplication aApplication = AApplication.getInstance();
        MobileChannel.getInstance().startConnect(aApplication, mobileConnectConfig, new IMobileConnectListener() { // from class: com.aliyun.iot.aep.sdk.init.DownStreamHelper.4
            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener
            public void onConnectStateChange(MobileConnectState mobileConnectState) {
                ALog.d("DownStreamHelper", "IMobileConnectListener onConnectStateChange(), state = " + mobileConnectState.toString());
                AConfigure.getInstance().putConfig(EnvConfigure.KEY_TRACE_ID, MobileChannel.getInstance().getClientId());
                if (MobileConnectState.CONNECTED.equals(mobileConnectState)) {
                    DownStreamHelper.this.b(aApplication);
                    DownStreamHelper.this.initBreeze(aApplication);
                    ConnectSDK.getInstance().init(aApplication);
                }
            }
        });
        b();
    }

    private void a() {
        ALog.d("DownStreamHelper", "stopReInit");
        try {
            this.f4742c.removeCallbacks(this.e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void b() {
        try {
            a();
            f4740b = Math.min(f4740b * 2, 64000);
            ALog.d("DownStreamHelper", "startReInit delay=" + f4740b);
            this.f4742c.postDelayed(this.e, (long) f4740b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initBreeze(Application application) {
        ALog.i("DownStreamHelper", "initGateWayChannel app：" + application);
        try {
            String clientId = MobileChannel.getInstance().getClientId();
            if (TextUtils.isEmpty(clientId)) {
                ALog.w("DownStreamHelper", "请检查长连接通道是否初始化成功");
                return;
            }
            String str = clientId.split("&")[1];
            String str2 = clientId.split("&")[0];
            if (SDKManager.isGatewayConnectAvailable()) {
                GatewayChannel.getInstance().startConnect(application, new GatewayConnectConfig(str, str2, ""), new IGatewayConnectListener() { // from class: com.aliyun.iot.aep.sdk.init.DownStreamHelper.5
                    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayConnectListener
                    public void onConnectStateChange(GatewayConnectState gatewayConnectState) {
                        ALog.d("DownStreamHelper", "IGatewayConnectListener onConnectStateChange(), state = " + gatewayConnectState.toString());
                        if (gatewayConnectState == GatewayConnectState.CONNECTED) {
                            ALog.d("DownStreamHelper", "网关建联成功");
                        } else if (gatewayConnectState == GatewayConnectState.CONNECTFAIL) {
                            ALog.i("DownStreamHelper", "Gateway connect failed");
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            ALog.e("DownStreamHelper", "breeze sdk missing");
        }
    }
}
