package tools;

import android.util.Log;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import java.util.HashMap;

/* JADX INFO: loaded from: classes4.dex */
public class MessageClient {
    private static MessageClient mInstance;
    private MessageApi messageApi = new MessageApi() { // from class: tools.MessageClient.1
        @Override // tools.MessageApi
        public void getMsg(int i, int i2, long j, long j2, String str, final String str2, final int i3, final CallBack callBack) {
            HashMap map = new HashMap();
            map.put("iotId", str);
            map.put("beginTime", Long.valueOf(j));
            map.put(AUserTrack.UTKEY_END_TIME, Long.valueOf(j2));
            map.put("eventType", Integer.valueOf(i2));
            map.put("pageStart", Integer.valueOf(i));
            map.put(AlinkConstants.KEY_PAGE_SIZE, 100);
            map.put("pictureType", 0);
            new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/event/query").setApiVersion("2.1.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: tools.MessageClient.1.1
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    Log.d("TAG", "onFailure: " + exc);
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    callBack.compete(ioTResponse, str2, i3);
                }
            });
        }

        @Override // tools.MessageApi
        public void getMessage(int i, int i2, String str, String str2, long j, long j2, final CallBack callBack) {
            HashMap map = new HashMap();
            map.put("nextToken", Integer.valueOf(i));
            map.put("maxResults", Integer.valueOf(i2));
            map.put("type", str);
            map.put("messageType", str2);
            map.put("startCreateTime", Long.valueOf(j));
            map.put("endCreateTime", Long.valueOf(j2));
            new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/message/center/query/push/message").setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: tools.MessageClient.1.2
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    callBack.compete(ioTResponse, null, 0);
                }
            });
        }
    };

    public static synchronized MessageClient getInstance() {
        if (mInstance == null) {
            mInstance = new MessageClient();
        }
        return mInstance;
    }

    public MessageApi getApi() {
        return mInstance.messageApi;
    }
}
