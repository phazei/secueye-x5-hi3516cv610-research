package com.aliyun.alink.h2.a.a;

import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

/* JADX INFO: compiled from: DeviceAuthHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements com.aliyun.alink.h2.a.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Map<String, String> f3799a = new HashMap();

    public b(Map<String, String> map) {
        this.f3799a.putAll(map);
        String strValueOf = String.valueOf(System.currentTimeMillis());
        String str = map.get("param-product-key");
        String str2 = map.get("param-device-name");
        String str3 = map.get("param-client-id");
        String str4 = map.get("deviceSecret");
        String str5 = TmpConstant.KEY_CLIENT_ID + str3 + "deviceName" + str2 + "productKey" + str + "timestamp" + strValueOf;
        this.f3799a.put("param-timestamp", strValueOf);
        this.f3799a.put("param-signmethod", "hmacmd5");
        this.f3799a.put("param-sign", a(str5, str4));
        this.f3799a.remove("deviceSecret");
    }

    @Override // com.aliyun.alink.h2.a.a
    public Map<String, String> a() {
        return this.f3799a;
    }

    private String a(String str, String str2) {
        return new String(Hex.encodeHex(new HmacUtils(HmacAlgorithms.HMAC_MD5, str2).hmac(str)));
    }
}
