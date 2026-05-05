package com.aliyun.alink.linksdk.tmp.connect;

import android.text.TextUtils;
import com.alibaba.sdk.android.push.xiaomi.BuildConfig;
import com.aliyun.alink.linksdk.cmp.api.CommonRequest;
import com.aliyun.alink.linksdk.tmp.devicemodel.Profile;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: loaded from: classes2.dex */
public abstract class CommonRequestBuilder<Builder, Payload> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4231a = "CommonRequestBuilder";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected Object f4233c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected String f4234d;
    protected String e;
    protected int f;
    protected String g;
    protected Payload l;
    protected boolean h = false;
    protected long i = 5000;
    protected RequestType j = RequestType.NORMAL;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected Method f4232b = Method.GET;
    protected Builder k = this;

    public abstract d c();

    public Builder a(boolean z) {
        this.h = z;
        return this.k;
    }

    public Builder a(Object obj) {
        this.f4233c = obj;
        return this.k;
    }

    public Builder a(String str) {
        this.e = str;
        return this.k;
    }

    public Builder b(String str) {
        this.g = str;
        return this.k;
    }

    public Builder b(Payload payload) {
        this.l = payload;
        return this.k;
    }

    public Builder c(String str) {
        this.f4234d = str;
        return this.k;
    }

    public Builder a(long j) {
        this.i = j;
        return this.k;
    }

    public Builder a(RequestType requestType) {
        this.j = requestType;
        return this.k;
    }

    public int a() {
        return this.f;
    }

    public Builder a(int i) {
        this.f = i;
        return this.k;
    }

    public Method b() {
        return this.f4232b;
    }

    public void a(Method method) {
        this.f4232b = method;
    }

    public static String a(Profile profile, String str) {
        if (profile == null) {
            LogCat.e(f4231a, "formatPath error param null profile:" + profile + " method" + str);
            return str;
        }
        return a(profile.getProdKey(), profile.getName(), str, "sys");
    }

    public static String a(String str, String str2, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogCat.e(f4231a, "formatPath error param null productKey:" + str + " deviceName:" + str2 + " method" + str3);
            return str3;
        }
        if (TextUtils.isEmpty(str3)) {
            LogCat.e(f4231a, "formatPath error param  method" + str3);
            return "/" + str4 + "/" + str + "/" + str2;
        }
        return "/" + str4 + "/" + str + "/" + str2 + "/" + str3.replace(".", "/");
    }

    public static String d(String str) {
        return TmpConstant.PATH_GROUP_PRE + str;
    }

    public enum Method {
        UNKNOW(0),
        POST(1),
        GET(2),
        DELETE(3),
        PUT(4);

        protected int mValue;

        Method(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }

        public CommonRequest.METHOD toCRMethod() {
            CommonRequest.METHOD method = CommonRequest.METHOD.GET;
            switch (this.mValue) {
                case 1:
                    return CommonRequest.METHOD.POST;
                case 2:
                    return CommonRequest.METHOD.GET;
                case 3:
                    return CommonRequest.METHOD.DELETE;
                case 4:
                    return CommonRequest.METHOD.PUT;
                default:
                    return method;
            }
        }
    }

    public enum RequestType {
        NORMAL(1, BuildConfig.FLAVOR),
        MULTIPLE_RESPONSE(2, "multiple_response"),
        RELEATE(3, "releate");

        protected String mDesc;
        protected int mType;

        RequestType(int i, String str) {
            this.mType = i;
            this.mDesc = str;
        }

        public int getType() {
            return this.mType;
        }

        public String getDesc() {
            return this.mDesc;
        }
    }
}
