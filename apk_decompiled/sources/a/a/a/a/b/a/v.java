package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class v implements SIGMeshBizRequest.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f1282a;

    public v(String str) {
        this.f1282a = str;
    }

    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
    public byte[] getEncodedParameters() {
        try {
            return MeshParserUtils.toByteArray(this.f1282a);
        } catch (Exception e) {
            e.printStackTrace();
            a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", "getCommonFireAndForgotRequest: eshParserUtils.toByteArray e=" + e);
            return null;
        }
    }
}
