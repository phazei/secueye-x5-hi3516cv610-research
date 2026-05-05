package a.a.a.a.a.a.a.b;

import a.a.a.a.a.g;
import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;
import com.alibaba.ailabs.iot.bleadvertise.msg.control.InexpensiveControlCmdType;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: compiled from: InexpensiveControlMsg.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public byte[] f1118a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public byte f1119b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public byte f1120c = g.c().b();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public BleAdvertiseCallback<Boolean> f1121d;

    public a(byte[] bArr, byte b2, BleAdvertiseCallback<Boolean> bleAdvertiseCallback) {
        this.f1118a = bArr;
        this.f1119b = b2;
        this.f1121d = bleAdvertiseCallback;
    }

    public final byte a(int i, int i2) {
        return (byte) (((i << 4) + i2) & 255);
    }

    public List<byte[]> a() {
        byte[] bArr = this.f1118a;
        if (bArr == null) {
            return null;
        }
        return a(bArr, this.f1120c);
    }

    public BleAdvertiseCallback<Boolean> b() {
        return this.f1121d;
    }

    public byte c() {
        return this.f1120c;
    }

    public byte d() {
        return this.f1119b;
    }

    public final List<byte[]> a(byte[] bArr, byte b2) {
        int length = (bArr.length / 18) + 1;
        ArrayList arrayList = new ArrayList(length);
        int i = 0;
        while (i < length) {
            int i2 = i * 18;
            int iMin = Math.min(bArr.length - i2, 18);
            byte[] bArr2 = new byte[iMin + 6];
            bArr2[0] = 14;
            bArr2[1] = InexpensiveControlCmdType.SEND_CTRL_CMD.getType();
            bArr2[2] = this.f1119b;
            bArr2[3] = b2;
            i++;
            bArr2[4] = a(length, i);
            bArr2[5] = (byte) (iMin & 255);
            System.arraycopy(bArr, i2, bArr2, 6, iMin);
            arrayList.add(bArr2);
        }
        return arrayList;
    }
}
