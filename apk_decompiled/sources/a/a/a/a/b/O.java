package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import datasource.bean.AddPublish;
import datasource.bean.SubscribeGroupAddr;
import java.util.List;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.ConfigModelPublicationSetParams;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class O implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1221a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f1222b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f1223c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ MeshService f1224d;

    public O(MeshService meshService, ProvisionedMeshNode provisionedMeshNode, int i, int i2) {
        this.f1224d = meshService;
        this.f1221a = provisionedMeshNode;
        this.f1222b = i;
        this.f1223c = i2;
    }

    @Override // java.lang.Runnable
    public void run() {
        SubscribeGroupAddr subscribeGroupAddr;
        if (this.f1224d.mModelIds.size() > 0) {
            this.f1224d.bindAppKey(this.f1221a, AddressUtils.getUnicastAddressBytes(this.f1222b), this.f1223c, (List<Integer>) this.f1224d.mModelIds);
            return;
        }
        if (this.f1224d.mPublishGroupAddrs == null || this.f1224d.mPublishGroupAddrs.size() <= 0) {
            if (this.f1224d.mSubscribeGroupAddrs == null || this.f1224d.mSubscribeGroupAddrs.size() <= 0 || (subscribeGroupAddr = (SubscribeGroupAddr) this.f1224d.mSubscribeGroupAddrs.remove(0)) == null || subscribeGroupAddr.getGroupAddr() == null || subscribeGroupAddr.getModelId() == null) {
                return;
            }
            Integer groupAddr = subscribeGroupAddr.getGroupAddr();
            int iIntValue = subscribeGroupAddr.getModelElementAddr().intValue();
            this.f1224d.mMeshManagerApi.a(this.f1221a, new byte[]{(byte) (iIntValue & 255), (byte) (65280 & iIntValue)}, new byte[]{(byte) ((groupAddr.intValue() >> 8) & 255), (byte) (groupAddr.intValue() & 255)}, subscribeGroupAddr.getModelId().intValue());
            return;
        }
        AddPublish addPublish = (AddPublish) this.f1224d.mPublishGroupAddrs.remove(0);
        if (addPublish != null) {
            Integer publishAddr = addPublish.getPublishAddr();
            Integer modelElementAddr = addPublish.getModelElementAddr();
            ConfigModelPublicationSetParams configModelPublicationSetParams = new ConfigModelPublicationSetParams(this.f1221a, new byte[]{(byte) ((modelElementAddr.intValue() >> 8) & 255), (byte) (modelElementAddr.intValue() & 255)}, addPublish.getModelId().intValue(), new byte[]{(byte) ((publishAddr.intValue() >> 8) & 255), (byte) (publishAddr.intValue() & 255)}, addPublish.getAppKeyIndex().intValue());
            configModelPublicationSetParams.setPublishTtl(addPublish.getTtl().intValue());
            configModelPublicationSetParams.setPublicationResolution(addPublish.getPublishPeriod().intValue());
            configModelPublicationSetParams.setPublicationSteps(addPublish.getPublishRetransmitIntervalSteps().intValue());
            configModelPublicationSetParams.setPublishRetransmitCount(addPublish.getPublishRetransmitCount().intValue());
            this.f1224d.mMeshManagerApi.a(configModelPublicationSetParams);
        }
    }
}
