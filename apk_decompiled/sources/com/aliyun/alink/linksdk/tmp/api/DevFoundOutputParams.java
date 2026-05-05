package com.aliyun.alink.linksdk.tmp.api;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

/* JADX INFO: loaded from: classes2.dex */
public class DevFoundOutputParams extends OutputParams {
    public static final String PARAMS_DEVICE_NAME = "device_name";
    public static final String PARAMS_GATEWAY_IOTID = "gatewayIotId";
    public static final String PARAMS_GATEWAY_NAME = "gatewayName";
    public static final String PARAMS_MODEL_NAME = "modelName";
    public static final String PARAMS_MODEL_TYPE = "model_type";
    public static final String PARAMS_PRODUCT_KEY = "product_key";

    public void setProductKey(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        put(PARAMS_PRODUCT_KEY, new ValueWrapper.StringValueWrapper(str));
    }

    public void setDeviceName(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        put(PARAMS_DEVICE_NAME, new ValueWrapper.StringValueWrapper(str));
    }

    public void setModelType(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        put(PARAMS_MODEL_TYPE, new ValueWrapper.StringValueWrapper(str));
    }

    public void setStringValue(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        put(str, new ValueWrapper.StringValueWrapper(str2));
    }

    public String getProductKey() {
        ValueWrapper.StringValueWrapper stringValueWrapper = (ValueWrapper.StringValueWrapper) get(PARAMS_PRODUCT_KEY);
        if (stringValueWrapper != null) {
            return stringValueWrapper.getValue();
        }
        return null;
    }

    public String getDeviceName() {
        ValueWrapper.StringValueWrapper stringValueWrapper = (ValueWrapper.StringValueWrapper) get(PARAMS_DEVICE_NAME);
        if (stringValueWrapper != null) {
            return stringValueWrapper.getValue();
        }
        return null;
    }

    public String getModelType() {
        ValueWrapper.StringValueWrapper stringValueWrapper = (ValueWrapper.StringValueWrapper) get(PARAMS_MODEL_TYPE);
        if (stringValueWrapper != null) {
            return stringValueWrapper.getValue();
        }
        return null;
    }
}
