package a.a.a.a.a.a.b.a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.ailabs.iot.bleadvertise.msg.provision.InexpensiveProvisionType;

/* JADX INFO: compiled from: InexpensiveProvisionMsg.java */
/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public InexpensiveProvisionType f1128a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public byte[] f1129b;

    public b(@NonNull InexpensiveProvisionType inexpensiveProvisionType, @Nullable byte[] bArr) {
        this.f1128a = inexpensiveProvisionType;
        this.f1129b = bArr;
    }

    public byte[] a() {
        byte[] bArr = this.f1129b;
        int length = bArr == null ? 2 : bArr.length + 2;
        byte[] bArr2 = new byte[length];
        if (a.a.a.a.b.d.a.f1315a) {
            bArr2[0] = 14;
        } else {
            bArr2[0] = 15;
        }
        bArr2[1] = this.f1128a.getType();
        if (length > 2) {
            byte[] bArr3 = this.f1129b;
            System.arraycopy(bArr3, 0, bArr2, 2, bArr3.length);
        }
        if (bArr2.length <= 26) {
            return bArr2;
        }
        a.a.a.a.b.m.a.b("FastProvisionMsg", "manufacture data length too large. " + bArr2.length);
        return null;
    }
}
