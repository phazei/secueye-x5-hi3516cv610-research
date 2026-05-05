package a.a.a.a.b.i;

import b.InterfaceC0367a;

/* JADX INFO: compiled from: FastProvisionWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class y implements InterfaceC0367a.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ J f1448a;

    public y(J j) {
        this.f1448a = j;
    }

    @Override // b.InterfaceC0367a.b
    public void generate(String str) {
        a.a.a.a.b.m.a.c(this.f1448a.f1354a, "receive confirmationCloud: " + str);
        this.f1448a.D = str;
        if (this.f1448a.w) {
            J j = this.f1448a;
            j.onReceiveConfirmationFromCloud(j.i, this.f1448a.D);
        }
    }
}
