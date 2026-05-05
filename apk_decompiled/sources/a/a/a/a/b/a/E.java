package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import io.netty.handler.codec.http2.Http2CodecUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class E implements SIGMeshBizRequestGenerator.a<Object> {
    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator.a
    public byte[] a(Object obj) {
        if (obj instanceof String) {
            try {
                short sRound = (short) Math.round((((double) (Integer.parseInt((String) obj) * 19200)) / 100.0d) + 800.0d);
                return new byte[]{(byte) (sRound & Http2CodecUtil.MAX_UNSIGNED_BYTE), (byte) (((byte) (sRound >> 8)) & 255)};
            } catch (NumberFormatException e) {
                a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", e.toString());
            }
        }
        if (!(obj instanceof Integer)) {
            return new byte[0];
        }
        short sRound2 = (short) Math.round((((double) (((Integer) obj).intValue() * 19200)) / 100.0d) + 800.0d);
        return new byte[]{(byte) (sRound2 & Http2CodecUtil.MAX_UNSIGNED_BYTE), (byte) (((byte) (sRound2 >> 8)) & 255)};
    }
}
