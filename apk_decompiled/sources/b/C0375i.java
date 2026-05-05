package b;

import com.alibaba.ailabs.iot.aisbase.spec.TLV;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import java.io.UnsupportedEncodingException;
import java.util.List;

/* JADX INFO: renamed from: b.i, reason: case insensitive filesystem */
/* JADX INFO: compiled from: MeshManagerApi.java */
/* JADX INFO: loaded from: classes.dex */
public class C0375i implements IActionListener<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2185a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ C0378l f2186b;

    public C0375i(C0378l c0378l, IActionListener iActionListener) {
        this.f2186b = c0378l;
        this.f2185a = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f2186b.n.removeCallbacks(this.f2186b.o);
        a.a.a.a.b.m.a.c(C0378l.f2190a, "sendVendorCommonMessage onFailure errorCode = " + i + "; desc = " + str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onSuccess(Object obj) {
        List<TLV> multiFromBytes;
        byte b2;
        this.f2186b.n.removeCallbacks(this.f2186b.o);
        a.a.a.a.b.m.a.c(C0378l.f2190a, "sendVendorCommonMessage onSuccess result = " + obj);
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            String str = null;
            byte b3 = 0;
            if (bArr == null || bArr.length <= 3) {
                multiFromBytes = null;
            } else {
                byte[] bArr2 = new byte[bArr.length - 3];
                System.arraycopy(bArr, 3, bArr2, 0, bArr2.length);
                multiFromBytes = TLV.parseMultiFromBytes(bArr2);
            }
            if (multiFromBytes == null || multiFromBytes.size() <= 0) {
                b2 = 0;
            } else {
                byte b4 = 0;
                b2 = 0;
                for (TLV tlv : multiFromBytes) {
                    if (tlv.getType() == 1) {
                        b4 = tlv.getValue()[0];
                    } else if (tlv.getType() == 2) {
                        try {
                            str = new String(tlv.getValue(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else if (tlv.getType() == 3) {
                        b2 = tlv.getValue()[0];
                    }
                }
                b3 = b4;
            }
            if (b3 == 1) {
                this.f2185a.onSuccess(Byte.valueOf(b3));
            } else {
                this.f2185a.onFailure(b3, "wifi config failure");
            }
            a.a.a.a.b.m.a.c(C0378l.f2190a, "sendVendorCommonMessage onSuccess code = " + ((int) b3) + "; type = " + ((int) b2) + "; message = " + str);
        }
    }
}
