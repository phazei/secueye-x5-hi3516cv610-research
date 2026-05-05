package a.a.a.a.b;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.bean.MeshNodeStatus;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.ListUtils;
import datasource.MeshConfigCallback;
import datasource.bean.ConfigResultMap;
import datasource.bean.ConfigurationData;
import datasource.bean.ProvisionAppKey;
import datasource.bean.SigmeshKey;
import java.util.Collections;
import java.util.List;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: renamed from: a.a.a.a.b.g, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0332g implements MeshConfigCallback<ConfigurationData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1326a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1327b;

    public C0332g(DeviceProvisioningWorker deviceProvisioningWorker, IActionListener iActionListener) {
        this.f1327b = deviceProvisioningWorker;
        this.f1326a = iActionListener;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(ConfigurationData configurationData) {
        a.a.a.a.b.m.a.a(this.f1327b.f2789b, "provisionComplete request success");
        this.f1327b.o.clear();
        this.f1327b.n.clear();
        if (configurationData == null || configurationData.getConfigResultMap() == null || configurationData.getConfigResultMap().getAddAppKey() == null) {
            return;
        }
        ConfigResultMap configResultMap = configurationData.getConfigResultMap();
        this.f1327b.x = configResultMap.getSubscribeGroupAddr();
        this.f1327b.y = configResultMap.getConfigModelPublication();
        List<Integer> appKeyIndexes = configResultMap.getAddAppKey().getAppKeyIndexes();
        SigmeshKey sigmeshKey = (SigmeshKey) this.f1327b.t.get(((Integer) this.f1327b.v.get(0)).intValue());
        List<SigmeshKey> sigmeshKeys = configResultMap.getSigmeshKeys();
        if (!ListUtils.isEmpty(sigmeshKeys)) {
            sigmeshKey = sigmeshKeys.get(0);
        }
        List<ProvisionAppKey> provisionAppKeys = sigmeshKey.getProvisionAppKeys();
        Collections.sort(provisionAppKeys);
        for (ProvisionAppKey provisionAppKey : provisionAppKeys) {
            if (provisionAppKey != null && (this.f1327b.i instanceof ProvisionedMeshNode)) {
                ((ProvisionedMeshNode) this.f1327b.i).setAddedAppKey(provisionAppKey.getAppKeyIndex(), provisionAppKey.getAppKey());
            }
            if (this.f1327b.z.getFeatureFlag1() != -109 || provisionAppKey == null || provisionAppKey.getAppKeyIndex() == 0) {
                if (provisionAppKey != null && appKeyIndexes.contains(Integer.valueOf(provisionAppKey.getAppKeyIndex()))) {
                    this.f1327b.o.add(provisionAppKey.getAppKey());
                    this.f1327b.n.add(Integer.valueOf(provisionAppKey.getAppKeyIndex()));
                }
            }
        }
        this.f1327b.u.clear();
        if (configResultMap.getBindModel() != null) {
            this.f1327b.u.addAll(configResultMap.getBindModel());
        }
        this.f1327b.g = true;
        if (this.f1327b.i != null && !this.f1327b.i.getSupportFastProvision()) {
            a.a.a.a.b.m.a.a(this.f1327b.f2789b, "IsProvisioningComplete: true");
            this.f1327b.c(MeshNodeStatus.PROVISIONING_COMPLETE.getState());
        }
        if (this.f1327b.j) {
            this.f1327b.p = true;
            DeviceProvisioningWorker deviceProvisioningWorker = this.f1327b;
            deviceProvisioningWorker.onCompositionDataStatusReceived((ProvisionedMeshNode) deviceProvisioningWorker.i);
        }
        this.f1326a.onSuccess(true);
        a.a.a.a.b.m.a.a(this.f1327b.f2789b, "IsReconnecting: true");
        LocalBroadcastManager.getInstance(this.f1327b.f2790c).sendBroadcast(new Intent(Utils.ACTION_IS_RECONNECTING).putExtra(Utils.EXTRA_DATA, true));
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.b(this.f1327b.f2789b, "provisionComplete request failed, errorMessage: " + str2);
        this.f1326a.onFailure(-1, str + " " + str2);
    }
}
