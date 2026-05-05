package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import datasource.bean.AddPublish;
import datasource.bean.SubscribeGroupAddr;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.utils.ConfigModelPublicationSetParams;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class P implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1225a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f1226b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ MeshService f1227c;

    public P(MeshService meshService, ProvisionedMeshNode provisionedMeshNode, byte[] bArr) {
        this.f1227c = meshService;
        this.f1225a = provisionedMeshNode;
        this.f1226b = bArr;
    }

    @Override // java.lang.Runnable
    public void run() {
        SubscribeGroupAddr subscribeGroupAddr;
        if (this.f1227c.mPublishGroupAddrs == null || this.f1227c.mPublishGroupAddrs.size() <= 0) {
            if (this.f1227c.mSubscribeGroupAddrs == null || this.f1227c.mSubscribeGroupAddrs.size() <= 0 || (subscribeGroupAddr = (SubscribeGroupAddr) this.f1227c.mSubscribeGroupAddrs.remove(0)) == null || subscribeGroupAddr.getGroupAddr() == null || subscribeGroupAddr.getModelId() == null) {
                return;
            }
            Integer groupAddr = subscribeGroupAddr.getGroupAddr();
            this.f1227c.mMeshManagerApi.a(this.f1225a, this.f1226b, new byte[]{(byte) ((groupAddr.intValue() >> 8) & 255), (byte) (groupAddr.intValue() & 255)}, subscribeGroupAddr.getModelId().intValue());
            return;
        }
        AddPublish addPublish = (AddPublish) this.f1227c.mPublishGroupAddrs.remove(0);
        if (addPublish != null) {
            Integer publishAddr = addPublish.getPublishAddr();
            Integer modelElementAddr = addPublish.getModelElementAddr();
            ConfigModelPublicationSetParams configModelPublicationSetParams = new ConfigModelPublicationSetParams(this.f1225a, new byte[]{(byte) ((modelElementAddr.intValue() >> 8) & 255), (byte) (modelElementAddr.intValue() & 255)}, addPublish.getModelId().intValue(), new byte[]{(byte) ((publishAddr.intValue() >> 8) & 255), (byte) (publishAddr.intValue() & 255)}, addPublish.getAppKeyIndex().intValue());
            configModelPublicationSetParams.setPublishTtl(addPublish.getTtl().intValue());
            configModelPublicationSetParams.setPublicationResolution(addPublish.getPublishPeriod().intValue());
            configModelPublicationSetParams.setPublicationSteps(addPublish.getPublishRetransmitIntervalSteps().intValue());
            configModelPublicationSetParams.setPublishRetransmitCount(addPublish.getPublishRetransmitCount().intValue());
            this.f1227c.mMeshManagerApi.a(configModelPublicationSetParams);
        }
    }
}
