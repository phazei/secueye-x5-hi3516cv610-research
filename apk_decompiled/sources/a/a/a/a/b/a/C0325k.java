package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: a.a.a.a.b.a.k, reason: case insensitive filesystem */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class C0325k implements SIGMeshBizRequest.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Map f1266a;

    public C0325k(Map map) {
        this.f1266a = map;
    }

    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
    public byte[] getEncodedParameters() {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.put(SIGMeshBizRequestGenerator.d());
        byteBufferOrder.put(new byte[]{Constants.CMD_TYPE.CMD_AUDIO_UPSTREAM, -16});
        int i = 3;
        for (SIGMeshBizRequestGenerator.Attribute attribute : new SIGMeshBizRequestGenerator.Attribute[]{SIGMeshBizRequestGenerator.Attribute.powerstate, SIGMeshBizRequestGenerator.Attribute.brightness, SIGMeshBizRequestGenerator.Attribute.colorTemperature}) {
            if (this.f1266a.containsKey(attribute.attributeName)) {
                i += attribute.attrParameterLength;
                byteBufferOrder.put(attribute.getValueEncoder().a(this.f1266a.get(attribute.attributeName)));
            }
        }
        return Arrays.copyOfRange(byteBufferOrder.array(), 0, i);
    }
}
