package a.a.a.a.a.a.b.a;

import a.a.a.a.a.h;
import com.alibaba.ailabs.iot.bleadvertise.msg.provision.InexpensiveProvisionType;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;

/* JADX INFO: compiled from: AddAppKeyMsg.java */
/* JADX INFO: loaded from: classes.dex */
public class a extends b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f1126c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public boolean f1127d;

    public a(byte[] bArr, byte b2, byte[] bArr2, String str) {
        super(InexpensiveProvisionType.PROVISIONING_ADD_APPKEY, new byte[19]);
        this.f1126c = "InexpensiveMesh" + a.class.getSimpleName();
        this.f1127d = true;
        int i = 0;
        try {
            byte[] bytes = (str + "SessionKey").getBytes("ASCII");
            String str2 = this.f1126c;
            StringBuilder sb = new StringBuilder();
            sb.append("confirmationBytes: ");
            sb.append(ConvertUtils.bytes2HexString(bytes));
            a.a.a.a.b.m.a.c(str2, sb.toString());
            byte[] bArrA = h.a(bytes);
            if (bArrA == null) {
                this.f1127d = false;
                return;
            }
            byte[] bArr3 = this.f1129b;
            System.arraycopy(bArrA, 0, bArr3, 0, bArr3.length);
            byte[] bArr4 = new byte[19];
            if (bArr.length >= 2) {
                System.arraycopy(bArr, 0, bArr4, 0, 2);
            }
            bArr4[2] = b2;
            System.arraycopy(bArr2, 0, bArr4, 3, bArr2.length);
            a.a.a.a.b.m.a.c(this.f1126c, "plainData = " + ConvertUtils.bytes2HexString(bArr4));
            if (bArr4.length != this.f1129b.length) {
                a.a.a.a.b.m.a.b(this.f1126c, "provisionData length is not equal provision Data");
                this.f1127d = false;
                return;
            }
            while (true) {
                byte[] bArr5 = this.f1129b;
                if (i >= bArr5.length) {
                    a.a.a.a.b.m.a.c(this.f1126c, "plainData = " + ConvertUtils.bytes2HexString(this.f1129b));
                    return;
                }
                bArr5[i] = (byte) (bArr5[i] ^ bArr4[i]);
                i++;
            }
        } catch (UnsupportedEncodingException | UnsupportedCharsetException e) {
            e.printStackTrace();
            this.f1127d = false;
        }
    }
}
