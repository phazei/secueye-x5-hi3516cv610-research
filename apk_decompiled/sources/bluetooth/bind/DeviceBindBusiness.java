package bluetooth.bind;

import android.text.TextUtils;
import bean.Device;
import com.aliyun.alink.business.devicecenter.api.discovery.IOnDeviceTokenGetListener;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DeviceBindBusiness {
    private static final int BIND_STATUS_DOING = 11;
    private static final int BIND_STATUS_FAILED = 13;
    private static final int BIND_STATUS_NONE = 10;
    private static final int BIND_STATUS_SUCCESS = 12;
    private static final int QUREY_STATUS_DOING = 1;
    private static final int QUREY_STATUS_FAILED = 3;
    private static final int QUREY_STATUS_NONE = 0;
    private static final int QUREY_STATUS_SUCCESS = 2;
    private static final String TAG = "DeviceBindBusiness";
    private String groupId;
    private Device mDevice;
    private OnBindDeviceCompletedListener onBindDeviceCompletedListener;
    private int qureyStatus = 0;
    private int bindStatus = 10;

    public DeviceBindBusiness setGroupId(String str) {
        this.groupId = str;
        return this;
    }

    public void queryProductInfo(final Device device) {
        if (device == null) {
            throw new IllegalArgumentException("device can not be null");
        }
        this.qureyStatus = 1;
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/detailInfo/queryProductInfoByProductKey").setApiVersion("1.1.1").addParam("productKey", device.pk).setAuthType(AlinkConstants.KEY_IOT_AUTH).build(), new IoTCallback() { // from class: bluetooth.bind.DeviceBindBusiness.1
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, final Exception exc) {
                DeviceBindBusiness.this.qureyStatus = 3;
                DeviceBindBusiness.this.bindStatus = 13;
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: bluetooth.bind.DeviceBindBusiness.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (DeviceBindBusiness.this.onBindDeviceCompletedListener != null) {
                                DeviceBindBusiness.this.onBindDeviceCompletedListener.onFailed(exc);
                            }
                        } catch (Exception e) {
                            ALog.e(DeviceBindBusiness.TAG, "exception happen when call listener.onFailed", e);
                            e.printStackTrace();
                        }
                        DeviceBindBusiness.this.onBindDeviceCompletedListener = null;
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                if (200 != ioTResponse.getCode() || !(ioTResponse.getData() instanceof JSONObject)) {
                    DeviceBindBusiness.this.qureyStatus = 3;
                    DeviceBindBusiness.this.bindStatus = 13;
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: bluetooth.bind.DeviceBindBusiness.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                if (DeviceBindBusiness.this.onBindDeviceCompletedListener != null) {
                                    DeviceBindBusiness.this.onBindDeviceCompletedListener.onFailed(ioTResponse.getCode(), ioTResponse.getMessage(), ioTResponse.getLocalizedMsg());
                                }
                            } catch (Exception e) {
                                ALog.e(DeviceBindBusiness.TAG, "exception happen when call listener.onFailed", e);
                                e.printStackTrace();
                            }
                            DeviceBindBusiness.this.onBindDeviceCompletedListener = null;
                        }
                    });
                    return;
                }
                String strOptString = ((JSONObject) ioTResponse.getData()).optString("netType");
                device.netType = strOptString;
                if ("NET_WIFI".equalsIgnoreCase(strOptString) || "NET_ETHERNET".equalsIgnoreCase(strOptString)) {
                    DeviceBindBusiness.this.bindWithWiFi(device);
                    return;
                }
                if ("NET_CELLULAR".equalsIgnoreCase(strOptString) || "NET_ZIGBEE".equalsIgnoreCase(strOptString) || "NET_OTHER".equalsIgnoreCase(strOptString) || "NET_BT".equalsIgnoreCase(strOptString)) {
                    DeviceBindBusiness.this.qureyStatus = 2;
                    Device device2 = new Device();
                    device2.pk = device.pk;
                    device2.dn = device.dn;
                    device2.netType = device.netType;
                    DeviceBindBusiness.this.mDevice = device2;
                    if (DeviceBindBusiness.this.bindStatus == 11) {
                        DeviceBindBusiness deviceBindBusiness = DeviceBindBusiness.this;
                        deviceBindBusiness.bindDeviceInternal(deviceBindBusiness.onBindDeviceCompletedListener);
                        return;
                    }
                    return;
                }
                DeviceBindBusiness.this.bindStatus = 13;
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: bluetooth.bind.DeviceBindBusiness.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (DeviceBindBusiness.this.onBindDeviceCompletedListener != null) {
                                DeviceBindBusiness.this.onBindDeviceCompletedListener.onFailed(new IllegalArgumentException("unsupported net type"));
                            }
                        } catch (Exception e) {
                            ALog.e(DeviceBindBusiness.TAG, "exception happen when call listener.onFailed", e);
                            e.printStackTrace();
                        }
                        DeviceBindBusiness.this.onBindDeviceCompletedListener = null;
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindDeviceInternal(final OnBindDeviceCompletedListener onBindDeviceCompletedListener) {
        String pathByDevice = getPathByDevice(this.mDevice);
        if (TextUtils.isEmpty(pathByDevice)) {
            onBindDeviceCompletedListener.onFailed(new UnsupportedOperationException("ble bind is not support at present@" + this.mDevice.toString()));
        }
        HashMap map = new HashMap();
        map.put("productKey", this.mDevice.pk);
        map.put("deviceName", this.mDevice.dn);
        if (!TextUtils.isEmpty(this.mDevice.token)) {
            map.put("token", this.mDevice.token);
        }
        if (!TextUtils.isEmpty(this.groupId)) {
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(this.groupId);
            map.put("groupIds", arrayList);
        }
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(pathByDevice).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: bluetooth.bind.DeviceBindBusiness.2
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, final Exception exc) {
                ALog.d(DeviceBindBusiness.TAG, "onFailure");
                DeviceBindBusiness.this.bindStatus = 13;
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: bluetooth.bind.DeviceBindBusiness.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            onBindDeviceCompletedListener.onFailed(exc);
                        } catch (Exception e) {
                            ALog.e(DeviceBindBusiness.TAG, "exception happen when call listener.onFailed", e);
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                ALog.d(DeviceBindBusiness.TAG, "onResponse bindWithWiFi ok");
                if (200 != ioTResponse.getCode() || !(ioTResponse.getData() instanceof String)) {
                    DeviceBindBusiness.this.bindStatus = 13;
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: bluetooth.bind.DeviceBindBusiness.2.2
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                onBindDeviceCompletedListener.onFailed(ioTResponse.getCode(), ioTResponse.getMessage(), ioTResponse.getLocalizedMsg());
                            } catch (Exception e) {
                                ALog.e(DeviceBindBusiness.TAG, "exception happen when call listener.onFailed", e);
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    final String str = (String) ioTResponse.getData();
                    DeviceBindBusiness.this.bindStatus = 12;
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: bluetooth.bind.DeviceBindBusiness.2.3
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                onBindDeviceCompletedListener.onSuccess(str);
                            } catch (Exception e) {
                                ALog.e(DeviceBindBusiness.TAG, "exception happen when call listener.onSuccess", e);
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public void bindDevice(Device device, OnBindDeviceCompletedListener onBindDeviceCompletedListener) {
        if (this.bindStatus == 11) {
            onBindDeviceCompletedListener.onFailed(new IllegalStateException("bindStatus = BIND_STATUS_DOING"));
            return;
        }
        this.bindStatus = 11;
        int i = this.qureyStatus;
        if (i == 2) {
            bindDeviceInternal(onBindDeviceCompletedListener);
        } else if (i == 1) {
            this.onBindDeviceCompletedListener = onBindDeviceCompletedListener;
        } else {
            this.onBindDeviceCompletedListener = onBindDeviceCompletedListener;
            queryProductInfo(device);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindWithWiFi(final Device device) {
        ALog.d(TAG, "bindWithWiFi");
        this.qureyStatus = 1;
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        LocalDeviceMgr.getInstance().getDeviceToken(device.pk, device.dn, 2000, new IOnDeviceTokenGetListener() { // from class: bluetooth.bind.DeviceBindBusiness.3
            @Override // com.aliyun.alink.business.devicecenter.api.discovery.IOnDeviceTokenGetListener
            public void onSuccess(String str) {
                ALog.d(DeviceBindBusiness.TAG, "getDeviceToken onSuccess token = " + str);
                if (atomicBoolean.get()) {
                    return;
                }
                atomicBoolean.set(true);
                DeviceBindBusiness.this.qureyStatus = 2;
                Device device2 = new Device();
                device2.pk = device.pk;
                device2.dn = device.dn;
                device2.netType = device.netType;
                device2.token = str;
                DeviceBindBusiness.this.mDevice = device2;
                if (DeviceBindBusiness.this.bindStatus == 11) {
                    DeviceBindBusiness deviceBindBusiness = DeviceBindBusiness.this;
                    deviceBindBusiness.bindDeviceInternal(deviceBindBusiness.onBindDeviceCompletedListener);
                }
            }

            @Override // com.aliyun.alink.business.devicecenter.api.discovery.IOnDeviceTokenGetListener
            public void onFail(final String str) {
                ALog.e(DeviceBindBusiness.TAG, "getDeviceToken onFail s = " + str);
                DeviceBindBusiness.this.qureyStatus = 3;
                DeviceBindBusiness.this.bindStatus = 13;
                if (atomicBoolean.get()) {
                    return;
                }
                atomicBoolean.set(true);
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: bluetooth.bind.DeviceBindBusiness.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (DeviceBindBusiness.this.onBindDeviceCompletedListener != null) {
                                DeviceBindBusiness.this.onBindDeviceCompletedListener.onFailed(new RuntimeException(str));
                            }
                        } catch (Exception e) {
                            ALog.e(DeviceBindBusiness.TAG, "exception happen when call listener.onFailed", e);
                            e.printStackTrace();
                        }
                        DeviceBindBusiness.this.onBindDeviceCompletedListener = null;
                    }
                });
            }
        });
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String getPathByDevice(bean.Device r2) {
        /*
            r1 = this;
            java.lang.String r2 = r2.netType
            java.lang.String r2 = r2.toUpperCase()
            int r0 = r2.hashCode()
            switch(r0) {
                case -2125520807: goto L40;
                case -1995574700: goto L36;
                case -1622758932: goto L2c;
                case -1176552596: goto L22;
                case 783500718: goto L18;
                case 2103711895: goto Le;
                default: goto Ld;
            }
        Ld:
            goto L4a
        Le:
            java.lang.String r0 = "NET_WIFI"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L4a
            r2 = 0
            goto L4b
        L18:
            java.lang.String r0 = "NET_OTHER"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L4a
            r2 = 4
            goto L4b
        L22:
            java.lang.String r0 = "NET_ZIGBEE"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L4a
            r2 = 3
            goto L4b
        L2c:
            java.lang.String r0 = "NET_CELLULAR"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L4a
            r2 = 2
            goto L4b
        L36:
            java.lang.String r0 = "NET_BT"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L4a
            r2 = 5
            goto L4b
        L40:
            java.lang.String r0 = "NET_ETHERNET"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L4a
            r2 = 1
            goto L4b
        L4a:
            r2 = -1
        L4b:
            switch(r2) {
                case 0: goto L56;
                case 1: goto L56;
                case 2: goto L53;
                case 3: goto L50;
                case 4: goto L50;
                default: goto L4e;
            }
        L4e:
            r2 = 0
            return r2
        L50:
            java.lang.String r2 = "/awss/subdevice/bind"
            return r2
        L53:
            java.lang.String r2 = "/awss/gprs/user/bind"
            return r2
        L56:
            java.lang.String r2 = "/awss/enrollee/user/bind"
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: bluetooth.bind.DeviceBindBusiness.getPathByDevice(bean.Device):java.lang.String");
    }
}
