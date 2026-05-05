package bluetooth.adddevice;

import android.os.Handler;
import android.os.Message;
import bean.BleFoundDevice;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class AddBleDeviceBusiness {
    public static final int MESSAGE_RESPONSE_FILTERDEVICE = 200705;
    private Handler handler;
    private List<BleFoundDevice> localFoundDevice = new ArrayList();

    public AddBleDeviceBusiness(Handler handler) {
        this.handler = handler;
    }

    public void reset() {
        this.localFoundDevice.clear();
    }

    public void filterDevice(final List<BleFoundDevice> list) {
        Iterator<BleFoundDevice> it = list.iterator();
        while (it.hasNext()) {
            if (this.localFoundDevice.contains(it.next())) {
                it.remove();
            }
        }
        this.localFoundDevice.addAll(list);
        if (list.size() == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (BleFoundDevice bleFoundDevice : list) {
            HashMap map = new HashMap(2);
            map.put("productKey", bleFoundDevice.productKey);
            map.put("deviceName", bleFoundDevice.deviceName);
            arrayList.add(map);
        }
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.PROVISION_DEVICE_FILTER).setApiVersion("1.0.2").addParam("iotDevices", (List) arrayList).setAuthType(AlinkConstants.KEY_IOT_AUTH).build(), new IoTCallback() { // from class: bluetooth.adddevice.AddBleDeviceBusiness.1
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                AddBleDeviceBusiness.this.localFoundDevice.removeAll(list);
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (200 != ioTResponse.getCode()) {
                    AddBleDeviceBusiness.this.localFoundDevice.removeAll(list);
                    return;
                }
                if (!(ioTResponse.getData() instanceof JSONArray)) {
                    AddBleDeviceBusiness.this.localFoundDevice.removeAll(list);
                    return;
                }
                JSONArray jSONArray = (JSONArray) ioTResponse.getData();
                if (jSONArray == null) {
                    AddBleDeviceBusiness.this.localFoundDevice.removeAll(list);
                    return;
                }
                ALog.d("JC", "有返回数据，表示服务端支持此pk，dn");
                List listParaseFilterDevice = AddBleDeviceBusiness.this.paraseFilterDevice(jSONArray);
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    if (!listParaseFilterDevice.contains((BleFoundDevice) it2.next())) {
                        it2.remove();
                    }
                }
                Message.obtain(AddBleDeviceBusiness.this.handler, 200705, list).sendToTarget();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<BleFoundDevice> paraseFilterDevice(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                BleFoundDevice bleFoundDevice = new BleFoundDevice();
                bleFoundDevice.deviceName = jSONObject.getString("deviceName");
                bleFoundDevice.productKey = jSONObject.getString("productKey");
                arrayList.add(bleFoundDevice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
