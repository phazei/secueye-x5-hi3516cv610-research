package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import io.netty.handler.codec.http2.Http2CodecUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class y implements SIGMeshBizRequestGenerator.a<Short> {
    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator.a
    public byte[] a(Short sh) {
        return new byte[]{(byte) (sh.shortValue() & Http2CodecUtil.MAX_UNSIGNED_BYTE)};
    }
}
