package datasource.channel.data;

import com.aliyun.alink.linksdk.connectsdk.BaseApiResponse;
import datasource.bean.ActivePayload;
import datasource.bean.AddModelClient;
import datasource.bean.ProvisionInfo4Master;
import datasource.bean.SigmeshIotDev;
import datasource.bean.SigmeshKey;
import datasource.bean.SubscribeGroupAddr_t;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanProvisionInfo4Master extends BaseApiResponse {
    public ActivePayload activePayload;
    public int nodeMaxSize;
    public int provisionerAddr;
    public List<SigmeshIotDev> sigmeshIotDevList = null;
    public List<SigmeshIotDev> shareDeviceList = null;
    public List<SigmeshKey> sigmeshKeys = null;
    public List<AddModelClient> addModelClient = null;
    public List<SubscribeGroupAddr_t> subscribeGroupAddr = null;

    public ProvisionInfo4Master convert2CommonObject() {
        ProvisionInfo4Master provisionInfo4Master = new ProvisionInfo4Master();
        provisionInfo4Master.setActivePayload(getActivePayload());
        provisionInfo4Master.setAddModelClient(getAddModelClient());
        provisionInfo4Master.setNodeMaxSize(getNodeMaxSize());
        provisionInfo4Master.setProvisionerAddr(getProvisionerAddr());
        provisionInfo4Master.setShareDeviceList(getShareDeviceList());
        provisionInfo4Master.setSigmeshIotDevList(getSigmeshIotDevList());
        provisionInfo4Master.setSigmeshKeys(getSigmeshKeys());
        provisionInfo4Master.setSubscribeGroupAddr(getSubscribeGroupAddr());
        return provisionInfo4Master;
    }

    public ActivePayload getActivePayload() {
        return this.activePayload;
    }

    public List<AddModelClient> getAddModelClient() {
        return this.addModelClient;
    }

    public int getNodeMaxSize() {
        return this.nodeMaxSize;
    }

    public int getProvisionerAddr() {
        return this.provisionerAddr;
    }

    public List<SigmeshIotDev> getShareDeviceList() {
        return this.shareDeviceList;
    }

    public List<SigmeshIotDev> getSigmeshIotDevList() {
        return this.sigmeshIotDevList;
    }

    public List<SigmeshKey> getSigmeshKeys() {
        return this.sigmeshKeys;
    }

    public List<SubscribeGroupAddr_t> getSubscribeGroupAddr() {
        return this.subscribeGroupAddr;
    }

    public void setActivePayload(ActivePayload activePayload) {
        this.activePayload = activePayload;
    }

    public void setAddModelClient(List<AddModelClient> list) {
        this.addModelClient = list;
    }

    public void setNodeMaxSize(int i) {
        this.nodeMaxSize = i;
    }

    public void setProvisionerAddr(int i) {
        this.provisionerAddr = i;
    }

    public void setShareDeviceList(List<SigmeshIotDev> list) {
        this.shareDeviceList = list;
    }

    public void setSigmeshIotDevList(List<SigmeshIotDev> list) {
        this.sigmeshIotDevList = list;
    }

    public void setSigmeshKeys(List<SigmeshKey> list) {
        this.sigmeshKeys = list;
    }

    public void setSubscribeGroupAddr(List<SubscribeGroupAddr_t> list) {
        this.subscribeGroupAddr = list;
    }
}
