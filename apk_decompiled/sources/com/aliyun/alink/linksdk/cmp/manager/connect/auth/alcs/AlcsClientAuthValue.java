package com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsClientAuthValue {
    public String accessKey;
    public String accessToken;
    public String deviceName;
    public String iotId;
    public String productKey;

    public boolean checkValid() {
        return (TextUtils.isEmpty(this.iotId) || TextUtils.isEmpty(this.productKey) || TextUtils.isEmpty(this.deviceName) || TextUtils.isEmpty(this.accessKey) || TextUtils.isEmpty(this.accessToken)) ? false : true;
    }

    public Map<String, String> getMap() {
        HashMap map = new HashMap();
        map.put("PK", this.productKey);
        map.put("DN", this.deviceName);
        map.put("KEY", this.accessKey);
        map.put("TOKEN", this.accessToken);
        map.put("ID", this.iotId);
        return map;
    }
}
