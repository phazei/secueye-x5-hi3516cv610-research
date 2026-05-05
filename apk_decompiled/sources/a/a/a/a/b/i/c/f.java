package a.a.a.a.b.i.c;

import aisscanner.ScanFilter;
import aisscanner.ScanResult;
import com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.mesh.provision.bean.FastProvisionDevice;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: compiled from: TinyMeshAdvTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class f implements ILeScanStrategy {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ g f1390a;

    public f(g gVar) {
        this.f1390a = gVar;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public BluetoothDeviceWrapper createFromScanResult(ScanResult scanResult) {
        if (scanResult == null || scanResult.getScanRecord() == null) {
            a.a.a.a.b.m.a.c(this.f1390a.f1391a, "scanResult or scanRecord is null");
            return null;
        }
        byte[] bytes = scanResult.getScanRecord().getBytes();
        if (bytes == null || bytes.length < 8) {
            String str = this.f1390a.f1391a;
            StringBuilder sb = new StringBuilder();
            sb.append("originData is ");
            sb.append(bytes == null ? TmpConstant.GROUP_ROLE_UNKNOWN : Integer.valueOf(bytes.length));
            a.a.a.a.b.m.a.c(str, sb.toString());
            return null;
        }
        a.a.a.a.b.m.a.c(this.f1390a.f1391a, "--createFromScanResult: " + ConvertUtils.bytes2HexString(bytes));
        byte[] bArr = new byte[bytes.length - 3];
        System.arraycopy(bytes, 3, bArr, 0, bArr.length);
        if (bArr[1] != -1) {
            a.a.a.a.b.m.a.c(this.f1390a.f1391a, "adType illegal, except 255, receive " + ((int) bArr[1]));
            return null;
        }
        if (bArr[2] == 1 && bArr[3] == -88) {
            int length = bArr.length - 5;
            byte[] bArr2 = new byte[length];
            System.arraycopy(bArr, 5, bArr2, 0, length);
            FastProvisionDevice fastProvisionDevice = new FastProvisionDevice();
            fastProvisionDevice.setScanResult(scanResult);
            fastProvisionDevice.a(bArr2);
            return fastProvisionDevice;
        }
        a.a.a.a.b.m.a.c(this.f1390a.f1391a, "cid illegal. except 0x01A8, receive " + ((int) bArr[2]) + " " + ((int) bArr[3]));
        return null;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public BluetoothDeviceSubtype getBluetoothDeviceSubtype() {
        return BluetoothDeviceSubtype.UNKNOWN;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public List<ScanFilter> getCustomScanFilters() {
        return new ArrayList();
    }
}
