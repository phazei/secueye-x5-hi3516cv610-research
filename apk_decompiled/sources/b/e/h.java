package b.e;

import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: MeshTransportStackEntryPoint.java */
/* JADX INFO: loaded from: classes.dex */
public class h implements f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ i f2167a;

    public h(i iVar) {
        this.f2167a = iVar;
    }

    @Override // b.e.f
    public void sendSegmentAcknowledgementMessage(b.d.b bVar) {
        b.d.b bVarI = this.f2167a.i(bVar);
        a.a.a.a.b.m.a.a(i.r, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarI.m().get(0), false));
        ProvisionedMeshNode meshNode = this.f2167a.u.getMeshNode(MeshParserUtils.toByteArray(bVar.l()), bVar.f());
        if (meshNode == null) {
            return;
        }
        this.f2167a.t.sendPdu(meshNode, bVarI.m().get(0));
        this.f2167a.u.onBlockAcknowledgementSent(meshNode);
    }
}
