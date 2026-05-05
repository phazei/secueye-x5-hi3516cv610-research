package a.a.a.a.b;

import com.alibaba.ailabs.iot.aisbase.spec.TLV;
import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import java.io.UnsupportedEncodingException;
import java.util.List;

/* JADX INFO: renamed from: a.a.a.a.b.v, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0362v implements IActionListener<byte[]> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1551a;

    public C0362v(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1551a = deviceProvisioningWorker;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(byte[] bArr) {
        List<TLV> multiFromBytes;
        if (bArr == null || bArr.length <= 3) {
            multiFromBytes = null;
        } else {
            byte[] bArr2 = new byte[bArr.length - 3];
            System.arraycopy(bArr, 3, bArr2, 0, bArr2.length);
            multiFromBytes = TLV.parseMultiFromBytes(bArr2);
        }
        if (multiFromBytes == null || multiFromBytes.size() <= 0) {
            this.f1551a.a((byte) -1);
            return;
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
        this.f1551a.a(b2);
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1551a.a(false, i, 0, str);
    }
}
