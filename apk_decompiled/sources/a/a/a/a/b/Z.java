package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.fastjson.JSON;
import datasource.MeshConfigCallback;
import datasource.bean.ProvisionInfo4Master;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class Z implements MeshConfigCallback<ProvisionInfo4Master> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MeshService f1242a;

    public Z(MeshService meshService) {
        this.f1242a = meshService;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(ProvisionInfo4Master provisionInfo4Master) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "fullRefreshProvisionInfo request success:" + JSON.toJSONString(provisionInfo4Master));
        try {
            G.a().b(provisionInfo4Master);
        } catch (Exception unused) {
        }
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.b(MeshService.TAG, "fullRefreshProvisionInfo request failed, errorMessage: " + str2);
    }
}
