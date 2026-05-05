package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.cmp.core.base.AResponse;

/* JADX INFO: compiled from: CpResponse.java */
/* JADX INFO: loaded from: classes2.dex */
public class i extends com.aliyun.alink.linksdk.tmp.connect.e<AResponse> {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f4260b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected int f4261c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected String f4262d;

    @Override // com.aliyun.alink.linksdk.tmp.connect.e
    public boolean b() {
        return true;
    }

    public i(AResponse aResponse) {
        super(aResponse);
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.e
    public String c() {
        return this.f4260b;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.tmp.connect.e
    public String e() {
        if (this.f4248a == 0 || ((AResponse) this.f4248a).data == null) {
            return null;
        }
        if (((AResponse) this.f4248a).data instanceof byte[]) {
            try {
                return new String((byte[]) ((AResponse) this.f4248a).data, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return ((AResponse) this.f4248a).data.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.tmp.connect.e
    public byte[] f() {
        if (this.f4248a == 0 || ((AResponse) this.f4248a).data == null || !(((AResponse) this.f4248a).data instanceof byte[])) {
            return null;
        }
        return (byte[]) ((AResponse) this.f4248a).data;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.tmp.connect.e
    public void a(String str) {
        if (this.f4248a == 0) {
            return;
        }
        ((AResponse) this.f4248a).data = str;
    }

    public void c(String str) {
        this.f4260b = str;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.e
    public int d() {
        return this.f4261c;
    }

    public void a(int i) {
        this.f4261c = i;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.e
    public void b(String str) {
        this.f4262d = str;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.e
    public String g() {
        return this.f4262d;
    }
}
