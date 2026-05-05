package com.alibaba.sdk.android.openaccount.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.SendSMSCallback;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.util.OpenAccountRiskControlContext;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.aliyun.alink.business.devicecenter.api.share.DeviceShareManager;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.framework.region.CountryManager;
import com.aliyun.iot.aep.sdk.framework.utils.MailHelper;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class SendSMSForLoginTask {
    public static final String TAG = "SendSMSTask";

    public static void SendSMSForLogin(final String str, final String str2, final SendSMSCallback sendSMSCallback) {
        ((ExecutorService) OpenAccountSDK.getService(ExecutorService.class)).postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.task.SendSMSForLoginTask.1
            @Override // java.lang.Runnable
            public void run() {
                new IoTAPIClientFactory().getClient().send(SendSMSForLoginTask.buildRegionApiRequest(str, str2), new IoTCallback() { // from class: com.alibaba.sdk.android.openaccount.task.SendSMSForLoginTask.1.1
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest, Exception exc) {
                        sendSMSCallback.onFailure(-1, ResourceUtils.getString("alisdk_openaccount_message_18_message"));
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                        if (ioTResponse != null) {
                            if (ioTResponse.getCode() == 200 && ioTResponse.getData() != null) {
                                SendSMSForLoginTask.sendPhoneSmsCode(str, str2, JSONObject.parseObject(ioTResponse.getData().toString()).getString("apiGatewayEndpoint"), sendSMSCallback);
                            } else {
                                sendSMSCallback.onFailure(ioTResponse.getCode(), ioTResponse.getMessage());
                            }
                        }
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IoTRequest buildRegionApiRequest(String str, String str2) {
        IoTRequestBuilder scheme = new IoTRequestBuilder().setApiVersion("1.0.2").setPath("living/account/region/get").setHost("test".equalsIgnoreCase(GlobalConfig.getInstance().getApiEnv()) ? "api-performance.aliplus.com" : "api.link.aliyun.com").setScheme(Scheme.HTTPS);
        HashMap map = new HashMap();
        map.put("countryCode", CountryManager.COUNTRY_CHINA_ABBR);
        if (MailHelper.isEmail(str)) {
            map.put("email", str);
            map.put("phoneLocationCode", "");
            map.put("type", DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL);
        } else {
            map.put("phone", str);
            map.put("phoneLocationCode", str2);
            map.put("type", "PHONE");
        }
        scheme.setParams(map);
        return scheme.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendPhoneSmsCode(String str, String str2, String str3, final SendSMSCallback sendSMSCallback) {
        new IoTAPIClientFactory().getClient().send(buildApiRequest(str, str2, str3), new IoTCallback() { // from class: com.alibaba.sdk.android.openaccount.task.SendSMSForLoginTask.2
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                SendSMSCallback sendSMSCallback2 = sendSMSCallback;
                if (sendSMSCallback2 != null) {
                    sendSMSCallback2.onFailure(-1, ResourceUtils.getString("alisdk_openaccount_message_18_message"));
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 1) {
                    SendSMSCallback sendSMSCallback2 = sendSMSCallback;
                    if (sendSMSCallback2 != null) {
                        sendSMSCallback2.onSuccess();
                        return;
                    }
                    return;
                }
                if (ioTResponse.getCode() == 20152 || ioTResponse.getCode() == 28544 || ioTResponse.getCode() == 7000) {
                    SendSMSCallback sendSMSCallback3 = sendSMSCallback;
                    if (sendSMSCallback3 != null) {
                        sendSMSCallback3.onFailure(ioTResponse.getCode(), ResourceUtils.getString("send_sms_code_failed"));
                        return;
                    }
                    return;
                }
                SendSMSCallback sendSMSCallback4 = sendSMSCallback;
                if (sendSMSCallback4 != null) {
                    sendSMSCallback4.onFailure(ioTResponse.getCode(), ioTResponse.getMessage());
                }
            }
        });
    }

    private static IoTRequest buildApiRequest(String str, String str2, String str3) {
        IoTRequestBuilder scheme = new IoTRequestBuilder().setApiVersion("1.0.0").setPath("/living/account/smscode/forlogin/send").setHost(str3).setScheme(Scheme.HTTPS);
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        map2.put(UtilityImpl.NET_TYPE_MOBILE, str);
        map2.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, str2);
        map2.put("riskControlInfo", OpenAccountRiskControlContext.buildRiskContext());
        map.put("requestStr", JSON.toJSONString(map2));
        scheme.setParams(map);
        return scheme.build();
    }
}
