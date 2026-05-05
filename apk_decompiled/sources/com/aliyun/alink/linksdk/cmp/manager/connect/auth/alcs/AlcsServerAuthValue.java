package com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsServerAuthValue {
    public String prefix;
    public String secret;

    public boolean checkValid() {
        return (TextUtils.isEmpty(this.prefix) || TextUtils.isEmpty(this.secret)) ? false : true;
    }

    public Map<String, String> getMap() {
        HashMap map = new HashMap();
        map.put("PREFIX", this.prefix);
        map.put("SECRET", this.secret);
        return map;
    }
}
