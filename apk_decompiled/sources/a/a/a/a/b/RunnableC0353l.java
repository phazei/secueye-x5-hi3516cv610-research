package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import datasource.bean.AddPublish;
import datasource.bean.SubscribeGroupAddr;
import java.util.List;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.ConfigModelPublicationSetParams;

/* JADX INFO: renamed from: a.a.a.a.b.l, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0353l implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1477a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f1478b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f1479c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1480d;

    public RunnableC0353l(DeviceProvisioningWorker deviceProvisioningWorker, ProvisionedMeshNode provisionedMeshNode, int i, int i2) {
        this.f1480d = deviceProvisioningWorker;
        this.f1477a = provisionedMeshNode;
        this.f1478b = i;
        this.f1479c = i2;
    }

    @Override // java.lang.Runnable
    public void run() {
        SubscribeGroupAddr subscribeGroupAddr;
        if (this.f1480d.w.size() > 0) {
            this.f1480d.a(this.f1477a, AddressUtils.getUnicastAddressBytes(this.f1478b), this.f1479c, (List<Integer>) this.f1480d.w);
            return;
        }
        if (this.f1480d.y == null || this.f1480d.y.size() <= 0) {
            if (this.f1480d.x == null || this.f1480d.x.size() <= 0 || (subscribeGroupAddr = (SubscribeGroupAddr) this.f1480d.x.remove(0)) == null || subscribeGroupAddr.getGroupAddr() == null || subscribeGroupAddr.getModelId() == null) {
                return;
            }
            Integer groupAddr = subscribeGroupAddr.getGroupAddr();
            int iIntValue = subscribeGroupAddr.getModelElementAddr().intValue();
            this.f1480d.e.a(this.f1477a, new byte[]{(byte) (iIntValue & 255), (byte) (65280 & iIntValue)}, new byte[]{(byte) ((groupAddr.intValue() >> 8) & 255), (byte) (groupAddr.intValue() & 255)}, subscribeGroupAddr.getModelId().intValue());
            return;
        }
        AddPublish addPublish = (AddPublish) this.f1480d.y.remove(0);
        if (addPublish != null) {
            Integer publishAddr = addPublish.getPublishAddr();
            Integer modelElementAddr = addPublish.getModelElementAddr();
            ConfigModelPublicationSetParams configModelPublicationSetParams = new ConfigModelPublicationSetParams(this.f1477a, new byte[]{(byte) ((modelElementAddr.intValue() >> 8) & 255), (byte) (modelElementAddr.intValue() & 255)}, addPublish.getModelId().intValue(), new byte[]{(byte) ((publishAddr.intValue() >> 8) & 255), (byte) (publishAddr.intValue() & 255)}, addPublish.getAppKeyIndex().intValue());
            configModelPublicationSetParams.setPublishTtl(addPublish.getTtl().intValue());
            configModelPublicationSetParams.setPublicationResolution(addPublish.getPublishPeriod().intValue());
            configModelPublicationSetParams.setPublicationSteps(addPublish.getPublishRetransmitIntervalSteps().intValue());
            configModelPublicationSetParams.setPublishRetransmitCount(addPublish.getPublishRetransmitCount().intValue());
            this.f1480d.e.a(configModelPublicationSetParams);
        }
    }
}
