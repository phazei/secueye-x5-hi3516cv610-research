package a.a.a.a.b.i;

import datasource.bean.DeviceStatus;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: renamed from: a.a.a.a.b.i.b, reason: case insensitive filesystem */
/* JADX INFO: compiled from: ConcurrentProvisionContext.java */
/* JADX INFO: loaded from: classes.dex */
public class C0336b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1369a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public List<DeviceStatus> f1370b = new LinkedList();

    public String a() {
        return this.f1369a;
    }

    public void a(String str) {
        this.f1369a = str;
    }

    public void a(List<DeviceStatus> list) {
        if (list != null) {
            this.f1370b.addAll(list);
        }
    }
}
