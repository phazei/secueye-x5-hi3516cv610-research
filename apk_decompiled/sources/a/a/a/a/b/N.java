package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import datasource.bean.BindModel;
import java.util.List;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.utils.AddressUtils;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class N implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1218a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f1219b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ MeshService f1220c;

    public N(MeshService meshService, ProvisionedMeshNode provisionedMeshNode, int i) {
        this.f1220c = meshService;
        this.f1218a = provisionedMeshNode;
        this.f1219b = i;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1220c.mBindModel.size() == 0) {
            if (this.f1220c.mAppKeyQueue.isEmpty()) {
                this.f1220c.getInfoByAuthInfo(this.f1218a.getUnicastAddress());
                return;
            } else {
                this.f1220c.addShareAppKey(this.f1218a);
                return;
            }
        }
        BindModel bindModel = (BindModel) this.f1220c.mBindModel.get(0);
        if (bindModel == null || bindModel.getModelElementAddr() == null) {
            return;
        }
        Integer modelElementAddr = bindModel.getModelElementAddr();
        this.f1220c.mModelIds = bindModel.getModelIds();
        if (modelElementAddr == null || this.f1220c.mModelIds == null) {
            return;
        }
        this.f1220c.bindAppKey(this.f1218a, AddressUtils.getUnicastAddressBytes(modelElementAddr.intValue()), this.f1219b, (List<Integer>) this.f1220c.mModelIds);
    }
}
