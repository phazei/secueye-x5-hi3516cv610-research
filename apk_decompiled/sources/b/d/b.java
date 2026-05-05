package b.d;

import java.util.HashMap;
import java.util.Map;
import meshprovisioner.control.TransportControlMessage;

/* JADX INFO: compiled from: ControlMessage.java */
/* JADX INFO: loaded from: classes.dex */
public class b extends c {
    public byte[] u;
    public TransportControlMessage v;

    public b() {
        this.f2144a = 1;
    }

    public void a(TransportControlMessage transportControlMessage) {
        this.v = transportControlMessage;
    }

    @Override // b.d.c
    public void b(HashMap<Integer, byte[]> map) {
        super.b(map);
    }

    @Override // b.d.c
    public void c(HashMap<Integer, byte[]> map) {
        this.f2147d = map;
    }

    public void g(byte[] bArr) {
        this.u = bArr;
    }

    @Override // b.d.c
    public HashMap<Integer, byte[]> k() {
        return super.k();
    }

    @Override // b.d.c
    public Map<Integer, byte[]> m() {
        return this.f2147d;
    }

    public TransportControlMessage u() {
        return this.v;
    }

    public byte[] v() {
        return this.u;
    }
}
