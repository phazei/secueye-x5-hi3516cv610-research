package com.aliyun.alink.h2.a.a;

import com.aliyun.alink.h2.api.AuthSignMethod;
import com.aliyun.alink.h2.api.IAuthSign;
import com.heytap.mcssdk.constant.IntentConstant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

/* JADX INFO: compiled from: AppKeyAuthHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements com.aliyun.alink.h2.a.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3797a = AuthSignMethod.SHA1.getMethod();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<String, String> f3798b = new HashMap();

    public a(Map<String, String> map, IAuthSign iAuthSign) {
        String authSign;
        this.f3798b.putAll(map);
        String str = this.f3798b.get(IntentConstant.APP_SECRET);
        String strValueOf = String.valueOf(new Random().nextLong());
        String str2 = "random=" + strValueOf;
        if (iAuthSign == null) {
            authSign = a(str2, str);
            this.f3798b.put("param-sign-method", f3797a);
        } else {
            authSign = iAuthSign.getAuthSign(str2);
        }
        this.f3798b.put("name", "appkey");
        this.f3798b.put("param-sign", authSign);
        this.f3798b.put("param-random", strValueOf);
        this.f3798b.remove(IntentConstant.APP_SECRET);
    }

    @Override // com.aliyun.alink.h2.a.a
    public Map<String, String> a() {
        return this.f3798b;
    }

    private String a(String str, String str2) {
        return new String(Hex.encodeHex(new HmacUtils(HmacAlgorithms.HMAC_SHA_1, str2).hmac(str)));
    }
}
