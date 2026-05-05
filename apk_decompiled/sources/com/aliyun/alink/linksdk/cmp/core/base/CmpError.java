package com.aliyun.alink.linksdk.cmp.core.base;

import androidx.core.app.FrameMetricsAggregator;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.DeviceCommonConstants;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public class CmpError extends AError {
    public CmpError() {
        setSubDomain("cmpError");
    }

    public static CmpError PARAMS_ERROR() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(510);
        cmpError.setMsg("params error");
        return cmpError;
    }

    public static CmpError UNKNOW() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(FrameMetricsAggregator.EVERY_DURATION);
        cmpError.setMsg("unsupport");
        return cmpError;
    }

    public static CmpError UNSUPPORT() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(512);
        cmpError.setMsg("unsupport");
        return cmpError;
    }

    public static CmpError APIGW_SEND_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(513);
        cmpError.setMsg("api gateway send fail");
        return cmpError;
    }

    public static CmpError REGISTER_CONNECT_ERROR_EXIST() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(514);
        cmpError.setMsg("connect is exist");
        return cmpError;
    }

    public static CmpError REGISTER_MQTT_CONNECT_ERROR_APIGW_NOT_REG() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(DeviceCommonConstants.WHAT_ERROR_SEND_SUCCESS);
        cmpError.setMsg("Please register api gateway firstly");
        return cmpError;
    }

    public static CmpError REGISTER_MQTT_CONNECT_ERROR_AUTH_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(DeviceCommonConstants.WHAT_ERROR_ZCONFIG_TIMEOUT);
        cmpError.setMsg("get mobile triple values error");
        return cmpError;
    }

    public static CmpError SEND_ERROR_CONNECT_NOT_FOUND() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(517);
        cmpError.setMsg("connect not found , please register firstly");
        return cmpError;
    }

    public static CmpError ALCS_INIT_MULTIPORT_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(DeviceCommonConstants.WHAT_NETWORK_NOT_FOUND);
        cmpError.setMsg("init alcs multiport fail");
        return cmpError;
    }

    public static CmpError CONNECT_AUTH_INFO_ERROR() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(DeviceCommonConstants.WHAT_DHCP_START_FAILED);
        cmpError.setMsg("auth result data error");
        return cmpError;
    }

    public static CmpError ALCS_INIT_ERROR() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(DeviceCommonConstants.WHAT_DHCP_TIMEOUT);
        cmpError.setMsg("alcs client init error");
        return cmpError;
    }

    public static CmpError CONNECT_FAIL_DISCONNECT() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(DeviceCommonConstants.WHAT_GW_CONN_TIMEOUT);
        cmpError.setMsg("current connect is not connected");
        return cmpError;
    }

    public static CmpError ALCS_SEND_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(DeviceCommonConstants.WHAT_GW_AUTH_FAILED);
        cmpError.setMsg("Alcs send fail");
        return cmpError;
    }

    public static CmpError ALCS_SEND_FAIL(int i) {
        CmpError cmpError = new CmpError();
        cmpError.setCode(i);
        cmpError.setMsg("Alcs send fail");
        return cmpError;
    }

    public static CmpError ALCS_SUBSCRIBE_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(DeviceCommonConstants.WHAT_DEVICE_CONNECT_SUCCESS);
        cmpError.setMsg("Alcs subscribe fail");
        return cmpError;
    }

    public static CmpError ALCS_UNSUBSCRIBE_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(524);
        cmpError.setMsg("Alcs un subscribe fail");
        return cmpError;
    }

    public static CmpError ALCS_REQUEST_CLIENT_AUTH_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(525);
        cmpError.setMsg("Alcs request client auth info fail");
        return cmpError;
    }

    public static CmpError ALCS_REQUEST_SERVER_AUTH_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(526);
        cmpError.setMsg("Alcs request server auth info fail");
        return cmpError;
    }

    public static CmpError MQTT_CONNECT_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(527);
        cmpError.setMsg("mqtt connect fail");
        return cmpError;
    }

    public static CmpError HUB_API_SEND_FAIL() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(528);
        cmpError.setMsg("hub api client send fail");
        return cmpError;
    }

    public static CmpError REGISTER_CONNECT_IS_REGISTERING() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(529);
        cmpError.setMsg("singleton connect is Registering");
        return cmpError;
    }

    public static CmpError PUBLISH_RESOURCE_ERROR() {
        CmpError cmpError = new CmpError();
        cmpError.setCode(530);
        cmpError.setMsg("publish resource error");
        return cmpError;
    }

    public String toString() {
        return "CmpError:[code = " + getCode() + ",msg = " + getMsg() + "]";
    }
}
