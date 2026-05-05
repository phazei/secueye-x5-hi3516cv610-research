package a.a.a.a.b.a;

import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;

/* JADX INFO: renamed from: a.a.a.a.b.a.a, reason: case insensitive filesystem */
/* JADX INFO: compiled from: ConfigModelSubRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class C0315a extends SIGMeshBizRequest {
    public String s;
    public String t;
    public final int u;
    public final int v;
    public final int w;
    public final int x;

    public C0315a(@NonNull SIGMeshBizRequest.Type type, String str, String str2, int i, int i2, int i3, int i4) {
        super(type, SIGMeshBizRequest.Mode.UNICAST);
        this.t = str;
        this.s = str2;
        this.u = i;
        this.v = i2;
        this.w = i3;
        this.x = i4;
    }

    public String r() {
        return this.t;
    }

    public String s() {
        return this.s;
    }

    public int t() {
        return this.v;
    }

    public int u() {
        return this.x;
    }

    public int v() {
        return this.u;
    }

    public int w() {
        return this.w;
    }
}
