package a.a.a.a.b;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.ailabs.iot.mesh.MeshService;
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

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class K implements MeshConfigCallback<ConfigurationData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1211a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ MeshService f1212b;

    public K(MeshService meshService, IActionListener iActionListener) {
        this.f1212b = meshService;
        this.f1211a = iActionListener;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(ConfigurationData configurationData) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "provisionComplete request success");
        this.f1212b.mAppKeyQueue.clear();
        this.f1212b.mAppKeyIndexQueue.clear();
        if (configurationData == null || configurationData.getConfigResultMap() == null || configurationData.getConfigResultMap().getAddAppKey() == null) {
            return;
        }
        ConfigResultMap configResultMap = configurationData.getConfigResultMap();
        this.f1212b.mSubscribeGroupAddrs = configResultMap.getSubscribeGroupAddr();
        this.f1212b.mPublishGroupAddrs = configResultMap.getConfigModelPublication();
        List<Integer> appKeyIndexes = configResultMap.getAddAppKey().getAppKeyIndexes();
        SigmeshKey sigmeshKey = (SigmeshKey) this.f1212b.mSigmeshKeys.get(((Integer) this.f1212b.mNetKeyIndexes.get(0)).intValue());
        List<SigmeshKey> sigmeshKeys = configResultMap.getSigmeshKeys();
        if (!ListUtils.isEmpty(sigmeshKeys)) {
            sigmeshKey = sigmeshKeys.get(0);
        }
        List<ProvisionAppKey> provisionAppKeys = sigmeshKey.getProvisionAppKeys();
        if (a.a.a.a.b.d.a.f1315a || !this.f1212b.mUnprovisionedMeshNodeData.isFastProvisionMesh()) {
            Collections.reverse(provisionAppKeys);
        }
        for (ProvisionAppKey provisionAppKey : provisionAppKeys) {
            if (provisionAppKey != null && appKeyIndexes.contains(Integer.valueOf(provisionAppKey.getAppKeyIndex()))) {
                this.f1212b.mAppKeyQueue.add(provisionAppKey.getAppKey());
                this.f1212b.mAppKeyIndexQueue.add(Integer.valueOf(provisionAppKey.getAppKeyIndex()));
            }
        }
        this.f1212b.mBindModel.clear();
        if (configResultMap.getBindModel() != null) {
            this.f1212b.mBindModel.addAll(configResultMap.getBindModel());
        }
        this.f1212b.mIsProvisioningComplete = true;
        if (this.f1212b.mCurrentProvisionMeshNode != null && !this.f1212b.mCurrentProvisionMeshNode.getSupportFastProvision()) {
            a.a.a.a.b.m.a.a(MeshService.TAG, "IsProvisioningComplete: true");
            this.f1212b.sendBroadcastProvisioningState(MeshNodeStatus.PROVISIONING_COMPLETE.getState());
        }
        if (this.f1212b.mDeviceIsReadyInConfigurationStep) {
            this.f1212b.mShouldAddAppKeyBeAdded = true;
            MeshService meshService = this.f1212b;
            meshService.onCompositionDataStatusReceived((ProvisionedMeshNode) meshService.mCurrentProvisionMeshNode);
        }
        this.f1211a.onSuccess(true);
        a.a.a.a.b.m.a.a(MeshService.TAG, "IsReconnecting: true");
        LocalBroadcastManager.getInstance(this.f1212b).sendBroadcast(new Intent(Utils.ACTION_IS_RECONNECTING).putExtra(Utils.EXTRA_DATA, true));
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.b(MeshService.TAG, "provisionComplete request failed, errorMessage: " + str2);
        this.f1211a.onFailure(-1, str + " " + str2);
    }
}
