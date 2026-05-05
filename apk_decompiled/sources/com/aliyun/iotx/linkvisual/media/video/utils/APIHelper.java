package com.aliyun.iotx.linkvisual.media.video.utils;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iotx.linkvisual.media.Version;
import com.aliyun.iotx.linkvisual.media.video.utils.mtopapi.MtopClient;
import java.util.Map;
import lvbyte.lvif;

/* JADX INFO: loaded from: classes2.dex */
public class APIHelper {
    private static String HOST;
    private static MediaInitListener mediaInitListener;

    class lvdo implements IoTCallback {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ IAPIHelperListener f5214lvdo;

        lvdo(IAPIHelperListener iAPIHelperListener) {
            this.f5214lvdo = iAPIHelperListener;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            lvif lvifVar = new lvif();
            lvifVar.lvdo(500);
            lvifVar.lvif(exc.toString());
            lvifVar.lvdo(exc.toString());
            this.f5214lvdo.onFailed(lvifVar);
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            int code = ioTResponse.getCode();
            lvif lvifVar = new lvif();
            lvifVar.lvdo(code);
            if (code != 200) {
                lvifVar.lvif(ioTResponse.getMessage());
                lvifVar.lvdo(ioTResponse.getLocalizedMsg());
                this.f5214lvdo.onFailed(lvifVar);
                return;
            }
            try {
                JSONObject object = JSON.parseObject(String.valueOf(ioTResponse.getData()));
                lvifVar.lvif(ioTResponse.getMessage());
                lvifVar.lvdo(ioTResponse.getLocalizedMsg());
                lvifVar.lvdo(object);
                this.f5214lvdo.onResponse(lvifVar);
            } catch (JSONException e) {
                e.printStackTrace();
                lvifVar.lvdo(500);
                lvifVar.lvif(e.toString());
                lvifVar.lvdo(e.toString());
                this.f5214lvdo.onFailed(lvifVar);
            }
        }
    }

    public static void sendIoTRequest(lvbyte.lvdo lvdoVar, Map<String, Object> map, String str, IAPIHelperListener iAPIHelperListener) {
        if (Version.isTg) {
            MtopClient.getInstance().sendIoTRequest(map, lvdoVar.lvfor(), mediaInitListener.getAuthInfo(), str, lvdoVar.lvint(), iAPIHelperListener);
            return;
        }
        IoTRequestBuilder authType = new IoTRequestBuilder().setAuthType(AlinkConstants.KEY_IOT_AUTH);
        TextUtils.isEmpty(HOST);
        authType.setApiVersion(lvdoVar.lvif());
        new IoTAPIClientFactory().getClient().send(authType.setParams(map).setPath(lvdoVar.lvdo()).build(), new lvdo(iAPIHelperListener));
    }

    public static void setHost(String str) {
    }

    public static void setInitListener(MediaInitListener mediaInitListener2) {
        mediaInitListener = mediaInitListener2;
    }
}
