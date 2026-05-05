package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b;

import android.util.Base64;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthPairs;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthParams;
import com.aliyun.alink.linksdk.alcs.lpbs.utils.RandomUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.utils.SecureUtils;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ICAAuthInfoCreater.java */
/* JADX INFO: loaded from: classes2.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f4042a = "2";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final String f4043b = "0";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final String f4044c = "1";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final String f4045d = "Xtau@iot";
    public static final String e = "Yx3DdsyetbSezlvc";
    public static final String f = "1";
    public static final String g = "001";
    private static final String h = "[AlcsLPBS]ICAAuthInfoCreater";
    private static final int i = 8;
    private static final int j = 16;

    public static ICAAuthPairs a(String str) {
        String randomString = RandomUtils.getRandomString(8);
        String randomString2 = RandomUtils.getRandomString(16);
        ICAAuthParams iCAAuthParamsA = a(randomString, randomString2, str);
        ICAAuthPairs iCAAuthPairs = new ICAAuthPairs(iCAAuthParamsA.accessKey, iCAAuthParamsA.accessToken, randomString, randomString2);
        ALog.d(h, "authCode:" + randomString + " secret:" + randomString2 + " ak:" + iCAAuthParamsA.accessKey + " at:" + iCAAuthParamsA.accessToken);
        return iCAAuthPairs;
    }

    public static ICAAuthParams a(String str, String str2, String str3) {
        ICAAuthParams iCAAuthParams = new ICAAuthParams();
        iCAAuthParams.accessKey = str + "1" + str3 + "001";
        iCAAuthParams.accessToken = Base64.encodeToString(SecureUtils.getHMacSha1Str(iCAAuthParams.accessKey, str2), 2);
        return iCAAuthParams;
    }
}
