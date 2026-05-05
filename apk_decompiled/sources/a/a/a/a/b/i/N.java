package a.a.a.a.b.i;

import android.util.Pair;
import com.alibaba.ailabs.iot.aisbase.spec.TLV;
import java.io.UnsupportedEncodingException;
import java.util.List;

/* JADX INFO: compiled from: WiFiConfigOverMeshLogicController.java */
/* JADX INFO: loaded from: classes.dex */
public class N implements a.a.a.a.b.a.I<byte[]> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ P f1361a;

    public N(P p) {
        this.f1361a = p;
    }

    @Override // a.a.a.a.b.a.I
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public Pair<Integer, Object> parseResponse(byte[] bArr) {
        List<TLV> multiFromBytes;
        if (bArr == null || bArr.length <= 3) {
            multiFromBytes = null;
        } else {
            byte[] bArr2 = new byte[bArr.length - 3];
            System.arraycopy(bArr, 3, bArr2, 0, bArr2.length);
            multiFromBytes = TLV.parseMultiFromBytes(bArr2);
        }
        if (multiFromBytes == null || multiFromBytes.size() <= 0) {
            return new Pair<>(-32, "");
        }
        byte b2 = 0;
        for (TLV tlv : multiFromBytes) {
            if (tlv.getType() == 1) {
                b2 = tlv.getValue()[0];
            } else if (tlv.getType() == 2) {
                try {
                    new String(tlv.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else if (tlv.getType() == 3) {
                byte b3 = tlv.getValue()[0];
            }
        }
        a.a.a.a.b.m.a.c(this.f1361a.f1364b, String.format("D3 ack recevied, code: %02X", Byte.valueOf(b2)));
        return b2 == 1 ? new Pair<>(0, Byte.valueOf(b2)) : new Pair<>(-70, String.valueOf((int) b2));
    }
}
