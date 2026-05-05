package com.aliyun.alink.business.devicecenter.api.add;

import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.aliyun.alink.linksdk.tmp.utils.ErrorCode;
import com.huawei.agconnect.exception.AGCServerException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.internal.http.StatusLine;
import org.mozilla.classfile.ByteCode;

/* JADX INFO: loaded from: classes.dex */
public enum ProvisionStatus {
    QR_PROVISION_READY(116, "qr code content"),
    PAP_PROVISION_FOUND_CONN_DEV(117, "phone ap found to provision device"),
    PAP_PROVISION_SWITCH_AP_SUCCESS(118, "phone ap switch ap success"),
    PAP_PROVISION_RECOVER_WIFI_FAILED(119, "phone ap close ap failed, recover wifi by yourself."),
    DEVICE_SUPPORT_SERVICE(201, "device support service."),
    SAP_NEED_USER_TO_CONNECT_DEVICE_AP(ByteCode.BREAKPOINT, "need user to connect device ap"),
    SAP_NEED_USER_TO_RECOVER_WIFI(203, "need user to recover wifi"),
    PORTAL_NEED_USER_TO_MANUALLY(204, "need user to manually open wifi portal"),
    BLE_NEED_CONNECT_SUCCESS(205, "ble need connect success"),
    BLE_NEED_CONNECT_STATE(HttpConstant.SC_PARTIAL_CONTENT, "ble need connect state"),
    BLE_NEED_DISCONNECT(211, "ble  disconnect state"),
    SAP_CONNECTED_TO_DEVICE_AP(205, "has connected to device ap"),
    SAP_RECOVERED_WIFI(HttpConstant.SC_PARTIAL_CONTENT, "has recovered wifi"),
    BLE_NEED_BLE_ENABLED(301, "need ble to be enabled"),
    BLE_NEED_LOCATION_SERVICE_ENABLED(302, "need location service to be enabled"),
    BLE_NEED_LOCATION_PERMISSION(303, "need location ACCESS_COARSE_LOCATION permission"),
    BLE_DEVICE_SCAN_NO_RESULT(304, "scan target ble device failed."),
    BLE_DEVICE_SCAN_SUCCESS(ErrorCode.ERROR_CODE_NOTSUPPORT, "scan target ble device success."),
    BLE_DEVICE_RECEIVE_SWITCHAP_ACK(ErrorCode.ERROR_CODE_ONLINEFAIL, "receive device switchap ack."),
    BLE_DEVICE_CONNECTING(309, "app try to connecting device"),
    BLE_DEVICE_CONNECTED(310, "app has connected with device"),
    BLE_DEVICE_CONNECTED_AP(311, "device has connected with ap."),
    BLE_DEVICE_CONNECTED_CLOUD(312, "device has connected with cloud server."),
    SOUND_BOX_SCAN_DEVICE_SUCCESS(StatusLine.HTTP_TEMP_REDIRECT, "scan device success"),
    SOUND_BOX_CONNECT_CLOUD_SUCCESS(StatusLine.HTTP_PERM_REDIRECT, "sound box connect network success"),
    PROVISION_APP_TOKEN(100, "notify app bind token"),
    PROVISION_START_IN_CONCURRENT_MODE(AGCServerException.AUTHENTICATION_INVALID, "provision start in batch mode"),
    MESH_COMBO_WIFI_CONNECT_CLOUD_STATUS(AGCServerException.TOKEN_INVALID, "combo mesh wifi channel connect status");

    public int code;
    public String message;
    public String traceID = "0";
    public int stateCode = 0;
    public String buffer = null;
    public Map extraParams = null;

    ProvisionStatus(int i, String str) {
        this.code = 0;
        this.message = null;
        this.code = i;
        this.message = str;
    }

    public ProvisionStatus addExtraParams(String str, Object obj) {
        if (TextUtils.isEmpty(str)) {
            return this;
        }
        if (this.extraParams == null) {
            this.extraParams = new HashMap();
        }
        this.extraParams.put(str, obj);
        return this;
    }

    public int code() {
        return this.code;
    }

    public String getBuffer() {
        return this.buffer;
    }

    public Map getExtraParams() {
        return this.extraParams;
    }

    public int getStateCode() {
        return this.stateCode;
    }

    public String getTraceID() {
        return this.traceID;
    }

    public String message() {
        return this.message;
    }

    public void setBuffer(String str) {
        this.buffer = str;
    }

    public ProvisionStatus setExtraParams(Map map) {
        this.extraParams = map;
        return this;
    }

    public ProvisionStatus setMessage(String str) {
        this.message = str;
        return this;
    }

    public void setStateCode(int i) {
        this.stateCode = i;
    }

    public void setTraceID(String str) {
        this.traceID = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return "ProvisionStatus[code=" + this.code + ",message=" + this.message + ",extraParams=" + this.extraParams + "]";
    }

    public Object getExtraParams(String str) {
        Map map = this.extraParams;
        if (map != null) {
            return map.get(str);
        }
        return null;
    }
}
