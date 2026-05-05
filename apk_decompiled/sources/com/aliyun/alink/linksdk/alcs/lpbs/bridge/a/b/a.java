package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthParams;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: DefaultICAStorage.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f4038a = "[AlcsLPBS]DefaultICAStorage";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected static final String f4039b = "asKey_pre_";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected static final String f4040c = "asToken_pre_";
    private static final String f = "DefaultICAStoragePerf";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected SharedPreferences f4041d;
    protected SharedPreferences.Editor e;
    private Context g;

    public a(Context context) {
        this.g = context;
        this.f4041d = this.g.getSharedPreferences(f, 0);
        this.e = this.f4041d.edit();
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.f
    public void a(String str, String str2, String str3) {
        this.e.putString(f4039b + str, str2);
        this.e.putString(f4040c + str, str3);
        this.e.apply();
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.f
    public ICAAuthParams a(String str) {
        String string = this.f4041d.getString(f4039b + str, null);
        String string2 = this.f4041d.getString(f4040c + str, null);
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
            ALog.e(f4038a, "getAccessInfo empty id:" + str);
            return null;
        }
        ICAAuthParams iCAAuthParams = new ICAAuthParams();
        iCAAuthParams.accessKey = string;
        iCAAuthParams.accessToken = string2;
        return iCAAuthParams;
    }
}
