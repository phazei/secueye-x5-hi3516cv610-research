package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class G implements SIGMeshBizRequestGenerator.a {
    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator.a
    public byte[] a(Object obj) {
        return obj instanceof String ? new byte[]{(byte) (Integer.parseInt((String) obj) & 255)} : obj instanceof Integer ? new byte[]{(byte) (((Integer) obj).intValue() & 255)} : new byte[0];
    }
}
