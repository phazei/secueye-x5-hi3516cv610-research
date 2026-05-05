package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import datasource.bean.AddPublish;
import datasource.bean.SubscribeGroupAddr;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.utils.ConfigModelPublicationSetParams;

/* JADX INFO: renamed from: a.a.a.a.b.m, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0354m implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1494a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f1495b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1496c;

    public RunnableC0354m(DeviceProvisioningWorker deviceProvisioningWorker, ProvisionedMeshNode provisionedMeshNode, byte[] bArr) {
        this.f1496c = deviceProvisioningWorker;
        this.f1494a = provisionedMeshNode;
        this.f1495b = bArr;
    }

    @Override // java.lang.Runnable
    public void run() {
        SubscribeGroupAddr subscribeGroupAddr;
        if (this.f1496c.y == null || this.f1496c.y.size() <= 0) {
            if (this.f1496c.x == null || this.f1496c.x.size() <= 0 || (subscribeGroupAddr = (SubscribeGroupAddr) this.f1496c.x.remove(0)) == null || subscribeGroupAddr.getGroupAddr() == null || subscribeGroupAddr.getModelId() == null) {
                return;
            }
            Integer groupAddr = subscribeGroupAddr.getGroupAddr();
            this.f1496c.e.a(this.f1494a, this.f1495b, new byte[]{(byte) ((groupAddr.intValue() >> 8) & 255), (byte) (groupAddr.intValue() & 255)}, subscribeGroupAddr.getModelId().intValue());
            return;
        }
        AddPublish addPublish = (AddPublish) this.f1496c.y.remove(0);
        if (addPublish != null) {
            Integer publishAddr = addPublish.getPublishAddr();
            Integer modelElementAddr = addPublish.getModelElementAddr();
            ConfigModelPublicationSetParams configModelPublicationSetParams = new ConfigModelPublicationSetParams(this.f1494a, new byte[]{(byte) ((modelElementAddr.intValue() >> 8) & 255), (byte) (modelElementAddr.intValue() & 255)}, addPublish.getModelId().intValue(), new byte[]{(byte) ((publishAddr.intValue() >> 8) & 255), (byte) (publishAddr.intValue() & 255)}, addPublish.getAppKeyIndex().intValue());
            configModelPublicationSetParams.setPublishTtl(addPublish.getTtl().intValue());
            configModelPublicationSetParams.setPublicationResolution(addPublish.getPublishPeriod().intValue());
            configModelPublicationSetParams.setPublicationSteps(addPublish.getPublishRetransmitIntervalSteps().intValue());
            configModelPublicationSetParams.setPublishRetransmitCount(addPublish.getPublishRetransmitCount().intValue());
            this.f1496c.e.a(configModelPublicationSetParams);
        }
    }
}
