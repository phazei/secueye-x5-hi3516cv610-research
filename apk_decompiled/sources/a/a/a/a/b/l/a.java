package a.a.a.a.b.l;

import com.alibaba.ailabs.iot.mesh.bean.MeshAccessPayload;
import com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import datasource.bean.SigmeshIotDev;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.spongycastle.util.encoders.Hex;

/* JADX INFO: compiled from: ControlMsgUtHelper.java */
/* JADX INFO: loaded from: classes.dex */
class a implements MeshMsgListener {
    @Override // com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener
    public void onReceiveMeshMessage(byte[] bArr, MeshAccessPayload meshAccessPayload) {
        short s = ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort();
        a.a.a.a.b.m.a.c("ControlMsgUtUtil", "onReceiveMeshMessage openCode " + meshAccessPayload.getOpCode() + ", address " + ((int) s));
        if (meshAccessPayload.getOpCode().equalsIgnoreCase("D3A801")) {
            SigmeshIotDev sigmeshIotDevQueryDeviceInfoByUnicastAddress = MeshDeviceInfoManager.getInstance().queryDeviceInfoByUnicastAddress(s, meshAccessPayload.getNetKey());
            String hexString = Hex.toHexString(meshAccessPayload.getParameters());
            if (sigmeshIotDevQueryDeviceInfoByUnicastAddress == null) {
                c.a(s, "", "", hexString);
            } else {
                c.a(s, sigmeshIotDevQueryDeviceInfoByUnicastAddress.getDevId(), sigmeshIotDevQueryDeviceInfoByUnicastAddress.getProductKey(), hexString);
            }
        }
    }
}
