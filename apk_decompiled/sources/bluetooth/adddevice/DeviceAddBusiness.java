package bluetooth.adddevice;

import android.os.Handler;
import android.os.Message;
import bean.FoundDevice;
import bean.FoundDeviceListItem;
import bean.SupportDeviceListItem;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DeviceAddBusiness {
    public static final int MESSAGE_RESPONSE_FILTERDEVICE = 200705;
    public static final int MESSAGE_RESPONSE_SUPPORTDEVICE_FAILED = 135169;
    public static final int MESSAGE_RESPONSE_SUPPORTDEVICE_SUCCESS = 69633;
    private Handler handler;
    private List<FoundDevice> localFoundDevice = new ArrayList();

    public DeviceAddBusiness(Handler handler) {
        this.handler = handler;
    }

    public void reset() {
        this.localFoundDevice.clear();
    }

    public void getSupportDeviceListFromSever() {
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/productInfo/getByAppKey").setApiVersion("1.1.1").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(new HashMap()).build(), new IoTCallback() { // from class: bluetooth.adddevice.DeviceAddBusiness.1
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                if (DeviceAddBusiness.this.handler == null) {
                    return;
                }
                Message.obtain(DeviceAddBusiness.this.handler, DeviceAddBusiness.MESSAGE_RESPONSE_SUPPORTDEVICE_FAILED).sendToTarget();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (DeviceAddBusiness.this.handler == null) {
                    return;
                }
                int code = ioTResponse.getCode();
                String message = ioTResponse.getMessage();
                if (code != 200) {
                    Message.obtain(DeviceAddBusiness.this.handler, DeviceAddBusiness.MESSAGE_RESPONSE_SUPPORTDEVICE_FAILED, message).sendToTarget();
                    return;
                }
                ArrayList arrayList = new ArrayList();
                Object data = ioTResponse.getData();
                if (data != null && (data instanceof JSONArray)) {
                    arrayList = DeviceAddBusiness.this.parseSupportDeviceListFromSever((JSONArray) data);
                }
                Message.obtain(DeviceAddBusiness.this.handler, DeviceAddBusiness.MESSAGE_RESPONSE_SUPPORTDEVICE_SUCCESS, arrayList).sendToTarget();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<SupportDeviceListItem> parseSupportDeviceListFromSever(JSONArray jSONArray) {
        ArrayList<SupportDeviceListItem> arrayList = new ArrayList<>();
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                SupportDeviceListItem supportDeviceListItem = new SupportDeviceListItem();
                supportDeviceListItem.deviceName = jSONObject.getString("name");
                supportDeviceListItem.productKey = jSONObject.getString("productKey");
                arrayList.add(supportDeviceListItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.util.ConcurrentModificationException
    	at java.base/java.util.ArrayList$Itr.checkForComodification(Unknown Source)
    	at java.base/java.util.ArrayList$Itr.next(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:358)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*  JADX ERROR: JadxRuntimeException in pass: FinishTypeInference
        jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r0v6 java.util.Collection<? extends bean.FoundDevice>
        	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:236)
        	at jadx.core.dex.visitors.typeinference.FinishTypeInference.lambda$visit$0(FinishTypeInference.java:27)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.typeinference.FinishTypeInference.visit(FinishTypeInference.java:22)
        */
    public void filterDevice(java.util.List<bean.FoundDeviceListItem> r7) {
        /*
            r6 = this;
            java.util.Iterator r0 = r7.iterator()
        L4:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L1c
            java.lang.Object r1 = r0.next()
            bean.FoundDeviceListItem r1 = (bean.FoundDeviceListItem) r1
            java.util.List<bean.FoundDevice> r2 = r6.localFoundDevice
            boolean r1 = r2.contains(r1)
            if (r1 == 0) goto L4
            r0.remove()
            goto L4
        L1c:
            java.util.List<bean.FoundDevice> r0 = r6.localFoundDevice
            r0.addAll(r0)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.Iterator r1 = r7.iterator()
        L2a:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L4e
            java.lang.Object r2 = r1.next()
            bean.FoundDeviceListItem r2 = (bean.FoundDeviceListItem) r2
            java.util.HashMap r3 = new java.util.HashMap
            r4 = 2
            r3.<init>(r4)
            java.lang.String r4 = "productKey"
            java.lang.String r5 = r2.productKey
            r3.put(r4, r5)
            java.lang.String r4 = "deviceName"
            java.lang.String r2 = r2.deviceName
            r3.put(r4, r2)
            r0.add(r3)
            goto L2a
        L4e:
            com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder r1 = new com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder
            r1.<init>()
            java.lang.String r2 = "/awss/enrollee/product/filter"
            com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder r1 = r1.setPath(r2)
            java.lang.String r2 = "1.0.2"
            com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder r1 = r1.setApiVersion(r2)
            java.lang.String r2 = "iotDevices"
            com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder r0 = r1.addParam(r2, r0)
            java.lang.String r1 = "iotAuth"
            com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder r0 = r0.setAuthType(r1)
            com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest r0 = r0.build()
            com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory r1 = new com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory
            r1.<init>()
            com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient r1 = r1.getClient()
            bluetooth.adddevice.DeviceAddBusiness$2 r2 = new bluetooth.adddevice.DeviceAddBusiness$2
            r2.<init>()
            r1.send(r0, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: bluetooth.adddevice.DeviceAddBusiness.filterDevice(java.util.List):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<FoundDeviceListItem> paraseFilterDevice(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                FoundDeviceListItem foundDeviceListItem = new FoundDeviceListItem();
                foundDeviceListItem.deviceName = jSONObject.getString("deviceName");
                foundDeviceListItem.productKey = jSONObject.getString("productKey");
                arrayList.add(foundDeviceListItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
