package com.aliyun.iot.push.impl;

import android.app.Application;
import android.util.Log;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.PushInitStatus;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.aliyun.alink.linksdk.securesigner.SecuritySourceContext;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import com.aliyun.iot.push.PushInitConfig;
import com.aliyun.iot.push.utils.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import org.android.agoo.common.CallBack;

/* JADX INFO: loaded from: classes2.dex */
public class IoTMainlandPushManager {
    public static boolean turnoffPushBeforeTurnOn = false;

    public void init(final Application application, final PushInitConfig pushInitConfig) {
        PushServiceFactory.init(application);
        final CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
        if (Utils.hasSecurityGuardDep()) {
            cloudPushService.setSecurityGuardAuthCode(pushInitConfig.getAuthCode());
        } else {
            cloudPushService.setAppSecret(SecuritySourceContext.getInstance().getAppSecretKey());
        }
        cloudPushService.register(application, new CommonCallback() { // from class: com.aliyun.iot.push.impl.IoTMainlandPushManager.1
            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onSuccess(String str) {
                ALog.i("IoTMainlandPushManager", "mainland: init cloudchannel success deviceToken:" + IoTMainlandPushManager.this.getDeviceId() + ", response=" + str);
                Log.i("IoTMainlandPushManager", "local mainland: init cloudchannel success deviceToken:" + IoTMainlandPushManager.this.getDeviceId() + ", response=" + str);
                if (!AdapterUtilityImpl.isTargetProcess(application)) {
                    ALog.w("IoTMainlandPushManager", "register success, turnOnPush only allow in target process");
                } else if (!IoTMainlandPushManager.turnoffPushBeforeTurnOn) {
                    IoTMainlandPushManager.this.a(cloudPushService, pushInitConfig);
                } else {
                    ALog.d("IoTMainlandPushManager", "debug.push.turnoff=true, turnoff first.");
                    cloudPushService.turnOffPushChannel(new CommonCallback() { // from class: com.aliyun.iot.push.impl.IoTMainlandPushManager.1.1
                        @Override // com.alibaba.sdk.android.push.CommonCallback
                        public void onSuccess(String str2) {
                            ALog.d("IoTMainlandPushManager", "turnOffPushChannel --> onSuccess() called with: s = [" + str2 + "]");
                            IoTMainlandPushManager.this.a(cloudPushService, pushInitConfig);
                        }

                        @Override // com.alibaba.sdk.android.push.CommonCallback
                        public void onFailed(String str2, String str3) {
                            ALog.d("IoTMainlandPushManager", "turnOffPushChannel --> onFailed() called with: errorCode = [" + str3 + "], s1 = [" + str3 + "]");
                            if (pushInitConfig == null || pushInitConfig.getPushInitCallback() == null) {
                                return;
                            }
                            ALog.d("IoTMainlandPushManager", "turnOffPushChannel onFailed -> " + pushInitConfig.getPushInitCallback());
                            if (PushInitStatus.getInstance().listener != null) {
                                PushInitStatus.getInstance().listener.onInitPush(false);
                            } else {
                                PushInitStatus.getInstance().isInitPush = false;
                            }
                            pushInitConfig.getPushInitCallback().onFailed(str2, str3);
                        }
                    });
                }
            }

            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onFailed(String str, String str2) {
                StringBuilder sb = new StringBuilder();
                sb.append("init cloudchannel failed -- errorcode:");
                sb.append(str);
                sb.append(" -- errorMessage:");
                sb.append(str2);
                sb.append(", config:");
                sb.append(pushInitConfig);
                sb.append(", config.getPushInitCallback():");
                PushInitConfig pushInitConfig2 = pushInitConfig;
                sb.append(pushInitConfig2 == null ? "" : pushInitConfig2.getPushInitCallback());
                ALog.w("IoTMainlandPushManager", sb.toString());
                PushInitConfig pushInitConfig3 = pushInitConfig;
                if (pushInitConfig3 == null || pushInitConfig3.getPushInitCallback() == null) {
                    return;
                }
                ALog.d("IoTMainlandPushManager", "onFailed -> " + pushInitConfig.getPushInitCallback());
                if (PushInitStatus.getInstance().listener != null) {
                    PushInitStatus.getInstance().listener.onInitPush(false);
                } else {
                    PushInitStatus.getInstance().isInitPush = false;
                }
                pushInitConfig.getPushInitCallback().onFailed(str, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(CloudPushService cloudPushService, final PushInitConfig pushInitConfig) {
        ALog.d("IoTMainlandPushManager", "turnOnPush() called with: pushService = [" + cloudPushService + "], config = [" + pushInitConfig + "]");
        if (cloudPushService != null) {
            cloudPushService.turnOnPushChannel(new CommonCallback() { // from class: com.aliyun.iot.push.impl.IoTMainlandPushManager.2
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("turnOnPushChannel --> onSuccess() called with: response = [");
                    sb.append(str);
                    sb.append("]");
                    sb.append(", config:");
                    sb.append(pushInitConfig);
                    sb.append(", config.getPushInitCallback():");
                    PushInitConfig pushInitConfig2 = pushInitConfig;
                    sb.append(pushInitConfig2 == null ? "" : pushInitConfig2.getPushInitCallback());
                    ALog.d("IoTMainlandPushManager", sb.toString());
                    PushInitConfig pushInitConfig3 = pushInitConfig;
                    if (pushInitConfig3 == null || pushInitConfig3.getPushInitCallback() == null) {
                        return;
                    }
                    ALog.d("IoTMainlandPushManager", "turnOnPushChannel --> onSuccess -> " + pushInitConfig.getPushInitCallback());
                    if (PushInitStatus.getInstance().listener != null) {
                        PushInitStatus.getInstance().listener.onInitPush(true);
                    } else {
                        PushInitStatus.getInstance().isInitPush = true;
                    }
                    pushInitConfig.getPushInitCallback().onSuccess(str);
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str, String str2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("turnOnPushChannel --> init cloudchannel success, but turnOnPushChannel fail -- errorcode:");
                    sb.append(str);
                    sb.append(" -- errorMessage:");
                    sb.append(str2);
                    sb.append(", config:");
                    sb.append(pushInitConfig);
                    sb.append(", config.getPushInitCallback():");
                    PushInitConfig pushInitConfig2 = pushInitConfig;
                    sb.append(pushInitConfig2 == null ? "" : pushInitConfig2.getPushInitCallback());
                    ALog.w("IoTMainlandPushManager", sb.toString());
                    PushInitConfig pushInitConfig3 = pushInitConfig;
                    if (pushInitConfig3 == null || pushInitConfig3.getPushInitCallback() == null) {
                        return;
                    }
                    if (PushInitStatus.getInstance().listener != null) {
                        PushInitStatus.getInstance().listener.onInitPush(false);
                    } else {
                        PushInitStatus.getInstance().isInitPush = false;
                    }
                    pushInitConfig.getPushInitCallback().onFailed(str, str2);
                }
            });
        }
    }

    public void deinit(Application application, final CallBack callBack) {
        ALog.d("IoTMainlandPushManager", "mainland: deinit() called with: application = [" + application + "], callBack = [" + callBack + "]");
        PushServiceFactory.getCloudPushService().unRegister(application, new CommonCallback() { // from class: com.aliyun.iot.push.impl.IoTMainlandPushManager.3
            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onSuccess(String str) {
                ALog.d("IoTMainlandPushManager", "onSuccess() called with: s = [" + str + "]");
                CallBack callBack2 = callBack;
                if (callBack2 != null) {
                    callBack2.onSuccess();
                }
            }

            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onFailed(String str, String str2) {
                ALog.w("IoTMainlandPushManager", "onFailed() called with: error = [" + str + "], errorMsg = [" + str2 + "]");
                CallBack callBack2 = callBack;
                if (callBack2 != null) {
                    callBack2.onFailure(str, str2);
                }
            }
        });
    }

    public String getDeviceId() {
        return PushServiceFactory.getCloudPushService().getDeviceId();
    }
}
