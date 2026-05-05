package com.aliyun.iot.push.impl;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import anet.channel.AwcnConfig;
import anet.channel.SessionCenter;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.IStrategyFilter;
import anet.channel.strategy.IStrategyInstance;
import anet.channel.strategy.IStrategyListener;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.HttpDispatcher;
import anetwork.channel.http.NetworkSdkSetting;
import com.alibaba.sdk.android.push.AliyunPushIntentService;
import com.alibaba.sdk.android.push.PushInitStatus;
import com.aliyun.alink.linksdk.securesigner.SecuritySourceContext;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import com.aliyun.iot.push.PushInitConfig;
import com.aliyun.iot.push.utils.ALog;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsException;
import com.taobao.agoo.ICallback;
import com.taobao.agoo.IRegister;
import com.taobao.agoo.TaobaoRegister;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.android.agoo.common.CallBack;

/* JADX INFO: loaded from: classes2.dex */
public class IoTOverseasPushManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f4940a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private AtomicBoolean f4941b = new AtomicBoolean(false);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private AtomicBoolean f4942c = new AtomicBoolean(false);

    public void init(Application application, String str, PushInitConfig pushInitConfig) {
        ALog.d("IoTOverseasPushManager", "init() called with: context = [" + application + "], appKey = [" + str + "], config = [" + pushInitConfig + "]");
        if (TextUtils.isEmpty(pushInitConfig.getDomain())) {
            throw new IllegalArgumentException("domain is empty.");
        }
        if (!TextUtils.isEmpty(pushInitConfig.getDomainPort()) && !TextUtils.isDigitsOnly(pushInitConfig.getDomainPort())) {
            throw new IllegalArgumentException("push domain port invalid.");
        }
        if (!TextUtils.isEmpty(pushInitConfig.getDomainBindIpPort())) {
            ALog.i("IoTOverseasPushManager", "push init with domain=" + pushInitConfig.getDomain() + "-->" + pushInitConfig.getDomainBindIpPort());
            HashMap map = new HashMap();
            map.put(pushInitConfig.getDomain(), pushInitConfig.getDomainBindIpPort());
            a(application, map, pushInitConfig.getDomain(), null);
        } else if (!TextUtils.isEmpty(pushInitConfig.getDomainPort())) {
            ALog.i("IoTOverseasPushManager", "push init with domain=" + pushInitConfig.getDomain() + ":" + pushInitConfig.getDomainPort());
            a(application, null, pushInitConfig.getDomain(), pushInitConfig.getDomainPort());
        }
        try {
            NetworkSdkSetting.init(application);
            HttpDispatcher.getInstance().setEnable(false);
            ACCSClient.setEnvironment(application, 0);
            AwcnConfig.setAccsSessionCreateForbiddenInBg(false);
            AccsClientConfig.Builder accsHeartbeatEnable = new AccsClientConfig.Builder().setAppKey(str).setInappHost(pushInitConfig.getDomain()).setInappPubKey(12).setChannelHost(TextUtils.isEmpty(pushInitConfig.getDomain()) ? "accs-beta.emas-validate.com" : pushInitConfig.getDomain()).setChannelPubKey(12).setTag(TextUtils.isEmpty(pushInitConfig.getPushClientTag()) ? AccsClientConfig.DEFAULT_CONFIGTAG : pushInitConfig.getPushClientTag()).setConfigEnv(0).setDisableChannel(true).setAccsHeartbeatEnable(true);
            if (Utils.hasSecurityGuardDep()) {
                accsHeartbeatEnable.setAutoCode(pushInitConfig.getAuthCode());
            } else {
                accsHeartbeatEnable.setAppSecret(SecuritySourceContext.getInstance().getAppSecretKey());
            }
            ACCSClient.init(application, accsHeartbeatEnable.build());
            a(application, str, pushInitConfig);
        } catch (AccsException e) {
            e.printStackTrace();
        }
    }

    private void a(Application application, String str, PushInitConfig pushInitConfig) {
        ALog.d("IoTOverseasPushManager", "initAgooPush() called with: context = [" + application + "], appKey = [" + str + "], config = [" + pushInitConfig + "]");
        new AnonymousClass1(pushInitConfig, application, str).start();
    }

    /* JADX INFO: renamed from: com.aliyun.iot.push.impl.IoTOverseasPushManager$1, reason: invalid class name */
    class AnonymousClass1 extends Thread {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ PushInitConfig f4943a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Application f4944b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ String f4945c;

        AnonymousClass1(PushInitConfig pushInitConfig, Application application, String str) {
            this.f4943a = pushInitConfig;
            this.f4944b = application;
            this.f4945c = str;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                String serviceName = this.f4943a.getServiceName();
                if (TextUtils.isEmpty(serviceName)) {
                    serviceName = AliyunPushIntentService.class.getName();
                }
                TaobaoRegister.setAgooMsgReceiveService(serviceName);
                TaobaoRegister.register(this.f4944b, TextUtils.isEmpty(this.f4943a.getPushClientTag()) ? AccsClientConfig.DEFAULT_CONFIGTAG : this.f4943a.getPushClientTag(), this.f4945c, "", "", new IRegister() { // from class: com.aliyun.iot.push.impl.IoTOverseasPushManager.1.1
                    @Override // com.taobao.agoo.IRegister
                    public void onSuccess(String str) {
                        ALog.i("IoTOverseasPushManager", "overseas: init cloudchannel success deviceToken:" + str);
                        Log.i("IoTOverseasPushManager", "local overseas: init cloudchannel success deviceToken:" + str);
                        IoTOverseasPushManager.this.f4941b.set(true);
                        IoTOverseasPushManager.this.f4940a = str;
                        try {
                            TaobaoRegister.bindAgoo(AnonymousClass1.this.f4944b, new ICallback() { // from class: com.aliyun.iot.push.impl.IoTOverseasPushManager.1.1.1
                                @Override // com.taobao.agoo.ICallback
                                public void onSuccess() {
                                    ALog.d("IoTOverseasPushManager", "bindAgoo onSuccess() called");
                                    if (AnonymousClass1.this.f4943a == null || AnonymousClass1.this.f4943a.getPushInitCallback() == null) {
                                        return;
                                    }
                                    ALog.d("IoTOverseasPushManager", "onSuccess -> " + AnonymousClass1.this.f4943a.getPushInitCallback());
                                    if (PushInitStatus.getInstance().listener != null) {
                                        PushInitStatus.getInstance().listener.onInitPush(true);
                                    } else {
                                        PushInitStatus.getInstance().isInitPush = true;
                                    }
                                    AnonymousClass1.this.f4943a.getPushInitCallback().onSuccess(IoTOverseasPushManager.this.getDeviceId());
                                }

                                @Override // com.taobao.agoo.ICallback
                                public void onFailure(String str2, String str3) {
                                    ALog.d("IoTOverseasPushManager", "bindAgoo onFailure() called with: errorCode = [" + str2 + "], errorMessage = [" + str3 + "]");
                                    if (AnonymousClass1.this.f4943a == null || AnonymousClass1.this.f4943a.getPushInitCallback() == null) {
                                        return;
                                    }
                                    ALog.d("IoTOverseasPushManager", "onFailed -> " + AnonymousClass1.this.f4943a.getPushInitCallback());
                                    if (PushInitStatus.getInstance().listener != null) {
                                        PushInitStatus.getInstance().listener.onInitPush(false);
                                    } else {
                                        PushInitStatus.getInstance().isInitPush = false;
                                    }
                                    AnonymousClass1.this.f4943a.getPushInitCallback().onFailed(str2, str3);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (AnonymousClass1.this.f4943a == null || AnonymousClass1.this.f4943a.getPushInitCallback() == null) {
                                return;
                            }
                            ALog.d("IoTOverseasPushManager", "onFailed -> " + AnonymousClass1.this.f4943a.getPushInitCallback());
                            if (PushInitStatus.getInstance().listener != null) {
                                PushInitStatus.getInstance().listener.onInitPush(false);
                            } else {
                                PushInitStatus.getInstance().isInitPush = false;
                            }
                            AnonymousClass1.this.f4943a.getPushInitCallback().onFailed("201011", "bindAgoo ecception=" + e.getMessage());
                        }
                    }

                    @Override // com.taobao.agoo.IRegister, com.taobao.agoo.ICallback
                    public void onFailure(String str, String str2) {
                        ALog.w("IoTOverseasPushManager", "init cloudchannel failed -- errorcode:" + str + " -- errorMessage:" + str2);
                        IoTOverseasPushManager.this.f4941b.set(false);
                        if (AnonymousClass1.this.f4943a == null || AnonymousClass1.this.f4943a.getPushInitCallback() == null) {
                            return;
                        }
                        ALog.d("IoTOverseasPushManager", "onFailed -> " + AnonymousClass1.this.f4943a.getPushInitCallback());
                        if (PushInitStatus.getInstance().listener != null) {
                            PushInitStatus.getInstance().listener.onInitPush(false);
                        } else {
                            PushInitStatus.getInstance().isInitPush = false;
                        }
                        AnonymousClass1.this.f4943a.getPushInitCallback().onFailed(str, str2);
                    }
                });
            } catch (AccsException e) {
                e.printStackTrace();
            }
        }
    }

    public String getDeviceId() {
        if (!this.f4941b.get()) {
            ALog.i("IoTOverseasPushManager", "getDeviceId failed, init push not finished.");
            return null;
        }
        return this.f4940a;
    }

    public void deinit(Application application, CallBack callBack) {
        ALog.d("IoTOverseasPushManager", "deinit() called with: application = [" + application + "], callBack = [" + callBack + "]");
        TaobaoRegister.unregister(application, callBack);
        if (callBack != null) {
            callBack.onSuccess();
        }
        this.f4940a = null;
    }

    private void a(Context context, final Map<String, String> map, final String str, final String str2) {
        if ((map == null || map.isEmpty()) && TextUtils.isEmpty(str2)) {
            return;
        }
        if (this.f4942c.get()) {
            ALog.d("IoTOverseasPushManager", "initNetwork has already inited, return.");
            return;
        }
        ALog.d("IoTOverseasPushManager", "initNetwork started.");
        this.f4942c.set(true);
        SessionCenter.init(context);
        final IStrategyInstance strategyCenter = StrategyCenter.getInstance();
        StrategyCenter.setInstance(new IStrategyInstance() { // from class: com.aliyun.iot.push.impl.IoTOverseasPushManager.2
            @Override // anet.channel.strategy.IStrategyInstance
            public String getUnitByHost(String str3) {
                return null;
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public void initialize(Context context2) {
                strategyCenter.initialize(context2);
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public void switchEnv() {
                strategyCenter.switchEnv();
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public void saveData() {
                strategyCenter.saveData();
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public String getFormalizeUrl(String str3) {
                return strategyCenter.getFormalizeUrl(str3);
            }

            /* JADX WARN: Removed duplicated region for block: B:9:0x002c  */
            @Override // anet.channel.strategy.IStrategyInstance
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public java.util.List<anet.channel.strategy.IConnStrategy> getConnStrategyListByHost(java.lang.String r4) {
                /*
                    r3 = this;
                    java.util.Map r0 = r3
                    if (r0 == 0) goto L2c
                    boolean r0 = r0.isEmpty()
                    if (r0 != 0) goto L2c
                    java.util.Map r0 = r3
                    java.lang.Object r0 = r0.get(r4)
                    java.lang.String r0 = (java.lang.String) r0
                    boolean r1 = android.text.TextUtils.isEmpty(r0)
                    if (r1 != 0) goto L2c
                    java.lang.String r1 = ":"
                    java.lang.String[] r0 = r0.split(r1)
                    java.util.ArrayList r1 = new java.util.ArrayList
                    r1.<init>()
                    com.aliyun.iot.push.impl.IoTOverseasPushManager$2$1 r2 = new com.aliyun.iot.push.impl.IoTOverseasPushManager$2$1
                    r2.<init>()
                    r1.add(r2)
                    goto L2d
                L2c:
                    r1 = 0
                L2d:
                    if (r1 != 0) goto L4e
                    java.lang.String r0 = r4
                    if (r0 == 0) goto L4e
                    boolean r0 = r0.equals(r4)
                    if (r0 == 0) goto L4e
                    java.lang.String r0 = r5
                    boolean r0 = android.text.TextUtils.isEmpty(r0)
                    if (r0 != 0) goto L4e
                    java.util.ArrayList r1 = new java.util.ArrayList
                    r1.<init>()
                    com.aliyun.iot.push.impl.IoTOverseasPushManager$2$2 r0 = new com.aliyun.iot.push.impl.IoTOverseasPushManager$2$2
                    r0.<init>()
                    r1.add(r0)
                L4e:
                    if (r1 != 0) goto L56
                    anet.channel.strategy.IStrategyInstance r0 = r2
                    java.util.List r1 = r0.getConnStrategyListByHost(r4)
                L56:
                    return r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iot.push.impl.IoTOverseasPushManager.AnonymousClass2.getConnStrategyListByHost(java.lang.String):java.util.List");
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public List<IConnStrategy> getConnStrategyListByHost(String str3, IStrategyFilter iStrategyFilter) {
                return getConnStrategyListByHost(str3);
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public String getSchemeByHost(String str3) {
                return strategyCenter.getSchemeByHost(str3);
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public String getSchemeByHost(String str3, String str4) {
                return strategyCenter.getSchemeByHost(str3, str4);
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public String getCNameByHost(String str3) {
                return strategyCenter.getCNameByHost(str3);
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public String getClientIp() {
                return strategyCenter.getClientIp();
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public void notifyConnEvent(String str3, IConnStrategy iConnStrategy, ConnEvent connEvent) {
                strategyCenter.notifyConnEvent(str3, iConnStrategy, connEvent);
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public void forceRefreshStrategy(String str3) {
                strategyCenter.forceRefreshStrategy(str3);
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public void registerListener(IStrategyListener iStrategyListener) {
                strategyCenter.registerListener(iStrategyListener);
            }

            @Override // anet.channel.strategy.IStrategyInstance
            public void unregisterListener(IStrategyListener iStrategyListener) {
                strategyCenter.unregisterListener(iStrategyListener);
            }
        });
    }
}
