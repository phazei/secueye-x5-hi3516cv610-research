package a.a.a.a.a.a.b.a;

import a.a.a.a.a.h;
import com.alibaba.ailabs.iot.bleadvertise.msg.provision.InexpensiveProvisionType;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;

/* JADX INFO: compiled from: SendProvisionDataMsg.java */
/* JADX INFO: loaded from: classes.dex */
public class d extends b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f1132c;

    public d(byte[] bArr, byte b2, byte[] bArr2, byte b3, byte[] bArr3, String str) {
        super(InexpensiveProvisionType.PROVISION_DATA, new byte[22]);
        this.f1132c = true;
        a.a.a.a.b.m.a.c("SendProvisionDataMsg", "macBytes = " + ConvertUtils.bytes2HexString(bArr) + ", networkKey = " + ConvertUtils.bytes2HexString(bArr2) + ", unicastAddress = " + ConvertUtils.bytes2HexString(bArr3) + ", confirmationCloud = " + str);
        int i = 0;
        if (bArr.length >= 2) {
            System.arraycopy(bArr, 0, this.f1129b, 0, 2);
        }
        try {
            byte[] bytes = (str + "SessionKey").getBytes("ASCII");
            StringBuilder sb = new StringBuilder();
            sb.append("confirmationBytes: ");
            sb.append(ConvertUtils.bytes2HexString(bytes));
            a.a.a.a.b.m.a.c("SendProvisionDataMsg", sb.toString());
            byte[] bArrA = h.a(bytes);
            if (bArrA == null) {
                this.f1132c = false;
                return;
            }
            a.a.a.a.b.m.a.c("SendProvisionDataMsg", "sessionKey: " + ConvertUtils.bytes2HexString(bArrA));
            byte[] bArr4 = this.f1129b;
            System.arraycopy(bArrA, 0, bArr4, 0, bArr4.length);
            byte[] bArrA2 = a(bArr, b2, bArr2, b3, bArr3);
            a.a.a.a.b.m.a.c("SendProvisionDataMsg", "plainData = " + ConvertUtils.bytes2HexString(bArrA2));
            if (bArrA2 == null || bArrA2.length != this.f1129b.length) {
                a.a.a.a.b.m.a.b("SendProvisionDataMsg", "provisionData length is not equal provision Data");
                this.f1132c = false;
                return;
            }
            while (true) {
                byte[] bArr5 = this.f1129b;
                if (i >= bArr5.length) {
                    a.a.a.a.b.m.a.c("SendProvisionDataMsg", "plainData = " + ConvertUtils.bytes2HexString(this.f1129b));
                    return;
                }
                bArr5[i] = (byte) (bArr5[i] ^ bArrA2[i]);
                i++;
            }
        } catch (UnsupportedEncodingException | UnsupportedCharsetException e) {
            e.printStackTrace();
            this.f1132c = false;
        }
    }

    public final byte[] a(byte[] bArr, byte b2, byte[] bArr2, byte b3, byte[] bArr3) {
        if (bArr2 == null || bArr2.length != 16 || bArr3 == null || bArr3.length != 2) {
            return null;
        }
        byte[] bArr4 = new byte[22];
        System.arraycopy(bArr, bArr.length - 2, bArr4, 0, 2);
        bArr4[2] = b2;
        System.arraycopy(bArr2, 0, bArr4, 3, bArr2.length);
        bArr4[19] = b3;
        System.arraycopy(bArr3, 0, bArr4, 20, bArr3.length);
        return bArr4;
    }

    public boolean b() {
        return this.f1132c;
    }
}
