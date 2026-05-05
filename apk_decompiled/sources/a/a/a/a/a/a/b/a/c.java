package a.a.a.a.a.a.b.a;

import com.alibaba.ailabs.iot.bleadvertise.msg.provision.InexpensiveProvisionType;

/* JADX INFO: compiled from: ProvisionRandomMsg.java */
/* JADX INFO: loaded from: classes.dex */
public class c extends b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public byte[] f1130c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public byte[] f1131d;
    public byte[] e;

    public c(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        super(InexpensiveProvisionType.PROVISION_RANDOM, new byte[18]);
        this.f1130c = new byte[2];
        if (bArr.length >= 2) {
            int length = bArr.length;
            byte[] bArr4 = this.f1130c;
            System.arraycopy(bArr, length - bArr4.length, bArr4, 0, bArr4.length);
        }
        this.f1131d = new byte[8];
        byte[] bArr5 = this.f1131d;
        System.arraycopy(bArr2, 0, bArr5, 0, bArr5.length);
        this.e = new byte[8];
        byte[] bArr6 = this.e;
        System.arraycopy(bArr3, 0, bArr6, 0, bArr6.length);
        byte[] bArr7 = this.f1130c;
        System.arraycopy(bArr7, 0, this.f1129b, 0, bArr7.length);
        byte[] bArr8 = this.f1131d;
        System.arraycopy(bArr8, 0, this.f1129b, this.f1130c.length, bArr8.length);
        byte[] bArr9 = this.e;
        System.arraycopy(bArr9, 0, this.f1129b, this.f1130c.length + this.f1131d.length, bArr9.length);
    }
}
