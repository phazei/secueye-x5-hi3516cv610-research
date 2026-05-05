package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class la implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1490a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ String f1491b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ String f1492c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ int f1493d;
    public final /* synthetic */ int e;
    public final /* synthetic */ int f;
    public final /* synthetic */ int g;
    public final /* synthetic */ IActionListener h;
    public final /* synthetic */ MeshService.b i;

    public la(MeshService.b bVar, int i, String str, String str2, int i2, int i3, int i4, int i5, IActionListener iActionListener) {
        this.i = bVar;
        this.f1490a = i;
        this.f1491b = str;
        this.f1492c = str2;
        this.f1493d = i2;
        this.e = i3;
        this.f = i4;
        this.g = i5;
        this.h = iActionListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        int i = this.f1490a;
        if (i > 0) {
            this.i.a(this.f1491b, this.f1492c, this.f1493d, this.e, this.f, this.g, this.h, i - 1);
        } else {
            Utils.notifyFailed(this.h, -31, "Timeout after 2 seconds of operation");
        }
    }
}
