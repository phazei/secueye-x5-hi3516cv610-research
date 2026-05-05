package com.aliyun.alink.linksdk.tmp.connect.a;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.api.CommonRequest;
import com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: TmpRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class j<Builder, Payload> extends CommonRequestBuilder<Builder, Payload> {
    private static final String m = "[Tmp]TmpRequestBuilder";
    protected Boolean o;
    protected String p;
    protected String q;
    protected String r;
    protected int u;
    protected String v;
    protected String w;
    protected boolean t = false;
    protected String s = "sys";

    public boolean e() {
        return this.t;
    }

    public void b(boolean z) {
        this.t = z;
    }

    public Builder b(int i) {
        this.u = i;
        return this.k;
    }

    public Builder j(String str) {
        this.s = str;
        return this.k;
    }

    public Builder c(boolean z) {
        this.o = Boolean.valueOf(z);
        return this.k;
    }

    public String f() {
        return this.p;
    }

    public Builder k(String str) {
        this.p = str;
        return this.k;
    }

    public String g() {
        return this.q;
    }

    public Builder l(String str) {
        this.q = str;
        return this.k;
    }

    public String h() {
        return this.r;
    }

    public Builder m(String str) {
        this.r = str;
        return this.k;
    }

    public String i() {
        return this.v;
    }

    public Builder n(String str) {
        this.v = str;
        return this.k;
    }

    public String j() {
        return this.w;
    }

    public Builder o(String str) {
        this.w = str;
        return this.k;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder
    public com.aliyun.alink.linksdk.tmp.connect.d c() {
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.ip = this.e;
        commonRequest.port = this.f;
        commonRequest.topic = a(f(), g(), h(), this.s);
        commonRequest.mothod = b().toCRMethod();
        commonRequest.payload = TextUtils.isEmpty(this.g) ? GsonUtils.toJson(this.l) : this.g;
        commonRequest.context = this.f4233c;
        commonRequest.type = Integer.valueOf(this.u);
        commonRequest.isSecurity = this.h;
        commonRequest.iotId = this.v;
        commonRequest.tgMeshType = this.w;
        com.aliyun.alink.linksdk.tmp.connect.entity.cmp.h hVar = new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.h(commonRequest);
        hVar.a(f());
        hVar.b(g());
        hVar.a(this.f4233c);
        hVar.a(this.h);
        ALog.d(m, "createRequest payload: " + commonRequest.payload);
        return hVar;
    }
}
