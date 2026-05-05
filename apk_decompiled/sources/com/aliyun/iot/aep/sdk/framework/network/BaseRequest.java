package com.aliyun.iot.aep.sdk.framework.network;

import com.aliyun.iot.aep.sdk.framework.utils.ObjectUtil;
import java.io.Serializable;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class BaseRequest implements Serializable {
    public Map<String, Object> getParams() {
        return ObjectUtil.objectToMap(this);
    }
}
