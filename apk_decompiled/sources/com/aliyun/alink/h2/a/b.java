package com.aliyun.alink.h2.a;

import com.aliyun.alink.h2.api.H2ClientException;
import com.aliyun.alink.h2.api.Profile;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: AuthenticationFactory.java */
/* JADX INFO: loaded from: classes2.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Map<String, a> f3800a = new HashMap();

    public static a a(Profile profile) {
        Map<String, String> authParams = profile.getAuthParams();
        if (authParams == null || authParams.isEmpty()) {
            throw new H2ClientException("authorization parameter is empty");
        }
        String str = profile.getAuthParams().get("name");
        if ("devicename".equals(str)) {
            return new com.aliyun.alink.h2.a.a.b(authParams);
        }
        if ("appkey".equals(str)) {
            return new com.aliyun.alink.h2.a.a.a(authParams, profile.getAuthSign());
        }
        if (f3800a.containsKey(str)) {
            return f3800a.get(str);
        }
        throw new H2ClientException("unsupported auth type: " + str);
    }
}
