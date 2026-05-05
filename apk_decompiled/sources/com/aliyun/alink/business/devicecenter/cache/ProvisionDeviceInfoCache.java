package com.aliyun.alink.business.devicecenter.cache;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.coap.request.CoapRequestPayload;
import com.aliyun.alink.business.devicecenter.channel.coap.response.DevicePayload;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;

/* JADX INFO: loaded from: classes.dex */
public class ProvisionDeviceInfoCache {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Object f3415a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public DevicePayload f3416b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public long f3417c;

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final ProvisionDeviceInfoCache f3419a = new ProvisionDeviceInfoCache();
    }

    public static ProvisionDeviceInfoCache getInstance() {
        return SingletonHolder.f3419a;
    }

    public void clearCache() {
        ALog.d("ProvisionDeviceInfoCache", "clearCache");
        synchronized (this.f3415a) {
            this.f3416b = null;
            this.f3417c = -1L;
        }
    }

    public DevicePayload getCache() {
        ALog.d("ProvisionDeviceInfoCache", "getCache");
        synchronized (this.f3415a) {
            if (this.f3416b == null) {
                ALog.w("ProvisionDeviceInfoCache", "cache model empty.");
                return null;
            }
            if (!TextUtils.isEmpty(this.f3416b.productKey) && !TextUtils.isEmpty(this.f3416b.deviceName)) {
                if (TextUtils.isEmpty(this.f3416b.token)) {
                    ALog.w("ProvisionDeviceInfoCache", "cache model token empty.");
                    return this.f3416b;
                }
                if (System.currentTimeMillis() - this.f3417c > this.f3416b.getRemainTime()) {
                    ALog.w("ProvisionDeviceInfoCache", "cache model expired.");
                    this.f3416b.remainTime = null;
                    this.f3417c = -1L;
                    this.f3416b = null;
                }
                return this.f3416b;
            }
            ALog.w("ProvisionDeviceInfoCache", "cache model pk or dn empty.");
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean handleNotifyCache(AlcsCoAPRequest alcsCoAPRequest) {
        ALog.d("ProvisionDeviceInfoCache", "handleNotifyCache");
        try {
        } catch (Exception e) {
            ALog.w("ProvisionDeviceInfoCache", "handleNotifyCache exception e=" + e);
        }
        if (this.f3416b != null && !TextUtils.isEmpty(this.f3416b.productKey) && !TextUtils.isEmpty(this.f3416b.deviceName)) {
            CoapRequestPayload coapRequestPayload = (CoapRequestPayload) JSONObject.parseObject(alcsCoAPRequest.getPayloadString(), new TypeReference<CoapRequestPayload<DevicePayload>>() { // from class: com.aliyun.alink.business.devicecenter.cache.ProvisionDeviceInfoCache.1
            }.getType(), new Feature[0]);
            DevicePayload devicePayload = (DevicePayload) coapRequestPayload.params;
            if (AlinkConstants.COAP_METHOD_DEVICE_INFO_NOTIFY.equals(coapRequestPayload.method)) {
                if (this.f3416b.productKey.equals(devicePayload.productKey) && this.f3416b.deviceName.equals(devicePayload.deviceName)) {
                    ALog.d("ProvisionDeviceInfoCache", "ack device info notify.");
                    updateCache(devicePayload);
                    return true;
                }
            } else if (AlinkConstants.COAP_METHOD_NOTIFY_PROVISION_SUCCESS.equals(coapRequestPayload.method) && this.f3416b.productKey.equals(devicePayload.productKey) && this.f3416b.deviceName.equals(devicePayload.deviceName)) {
                ALog.d("ProvisionDeviceInfoCache", "ack connect ap.");
                return true;
            }
            return false;
        }
        this.f3416b = null;
        ALog.d("ProvisionDeviceInfoCache", "handleNotifyCache cache model is empty, return.");
        return false;
    }

    public void updateCache(DevicePayload devicePayload) {
        ALog.d("ProvisionDeviceInfoCache", "updateCache expiringModel=" + devicePayload);
        if (devicePayload == null) {
            ALog.d("ProvisionDeviceInfoCache", "updateCache expiringModel invalid PK|DN|OJ empty.");
            return;
        }
        if (!DeviceInfoUtils.isDevicePayloadValid(devicePayload)) {
            ALog.d("ProvisionDeviceInfoCache", "updateCache expiringModel invalid expiringModel = " + devicePayload);
            return;
        }
        synchronized (this.f3415a) {
            if (this.f3416b == null) {
                this.f3416b = new DevicePayload(devicePayload);
                if (!TextUtils.isEmpty(devicePayload.token)) {
                    this.f3417c = System.currentTimeMillis();
                }
            } else if (devicePayload.productKey.equals(this.f3416b.productKey) && devicePayload.deviceName.equals(this.f3416b.deviceName)) {
                if (TextUtils.isEmpty(devicePayload.token)) {
                    this.f3416b.remainTime = null;
                    this.f3417c = -1L;
                } else {
                    this.f3416b.token = devicePayload.token;
                    this.f3416b.remainTime = devicePayload.remainTime;
                    this.f3417c = System.currentTimeMillis();
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("updateCache success. cache model=");
            sb.append(this.f3416b);
            ALog.d("ProvisionDeviceInfoCache", sb.toString());
        }
    }

    public ProvisionDeviceInfoCache() {
        this.f3415a = new Object();
        this.f3416b = null;
        this.f3417c = -1L;
    }
}
