package com.alibaba.ailabs.iot.mesh;

import a.a.a.a.b.G;
import a.a.a.a.b.m.a;
import a.a.a.a.b.pa;
import a.a.a.a.b.qa;
import a.a.a.a.b.ra;
import a.a.a.a.b.sa;
import a.a.a.a.b.ta;
import a.a.a.a.b.ua;
import android.content.Context;
import androidx.annotation.Nullable;
import com.alibaba.ailabs.iot.mesh.MeshSceneJob;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: loaded from: classes.dex */
public class SceneTransaction {
    public static final String TAG = "SceneTransaction";
    public List<MeshSceneJob> additionJobList;
    public List<MeshSceneJob> deletionJobList;
    public List<MeshSceneJob> failedAdditionJobs;
    public List<MeshSceneJob> failedDeletionJobs;
    public AtomicInteger jobIdCount;
    public Map<Integer, MeshSceneJob> jobMap;
    public short sceneNumber;

    public interface SceneTransactionCallback<T> {
        void onFailure(String str, int i, String str2);

        void onSuccess(String str, T t);
    }

    public static void bind(Context context, String str, List<Map<String, Short>> list, SceneTransactionCallback<Boolean> sceneTransactionCallback) {
        for (Map<String, Short> map : list) {
            for (String str2 : map.keySet()) {
                SIGMeshBizRequest sIGMeshBizRequestA = SIGMeshBizRequestGenerator.a(context.getApplicationContext(), str, str2, map.get(str2).shortValue(), new ra(str2, sceneTransactionCallback));
                if (sIGMeshBizRequestA != null) {
                    TgMeshManager.getInstance().offerBizRequest(sIGMeshBizRequestA);
                }
            }
        }
    }

    public static void bindEx(Context context, String str, List<Map<String, Short>> list, SceneTransactionCallback<Boolean> sceneTransactionCallback) {
        for (Map<String, Short> map : list) {
            SIGMeshBizRequest sIGMeshBizRequestA = SIGMeshBizRequestGenerator.a(context.getApplicationContext(), str, map, new sa(map, sceneTransactionCallback, str));
            if (sIGMeshBizRequestA != null) {
                TgMeshManager.getInstance().offerBizRequest(sIGMeshBizRequestA);
            }
        }
    }

    private boolean isEmptyFailedJob(List<MeshSceneJob> list) {
        if (list != null && list.size() != 0) {
            for (MeshSceneJob meshSceneJob : list) {
                if (meshSceneJob.getDevices() != null && meshSceneJob.getDevices().size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void unbind(Context context, String str, List<Map<String, Short>> list, SceneTransactionCallback<Boolean> sceneTransactionCallback) {
        Iterator<Map<String, Short>> it = list.iterator();
        while (it.hasNext()) {
            for (String str2 : it.next().keySet()) {
                TgMeshManager.getInstance().offerBizRequest(SIGMeshBizRequestGenerator.a(context.getApplicationContext(), str, str2, new ta(str2, sceneTransactionCallback)));
            }
        }
    }

    public static void unbindEx(Context context, String str, List<Map<String, Short>> list, SceneTransactionCallback<Boolean> sceneTransactionCallback) {
        for (Map<String, Short> map : list) {
            TgMeshManager.getInstance().offerBizRequest(SIGMeshBizRequestGenerator.c(context.getApplicationContext(), str, map, new ua(map, sceneTransactionCallback, str)));
        }
    }

    public SceneTransaction addDevices(int i, @Nullable List<String> list, @Nullable Map<String, Object> map) {
        a.c(TAG, String.format("add %d devices for job:%d", Integer.valueOf(list.size()), Integer.valueOf(i)));
        if (!this.jobMap.containsKey(Integer.valueOf(i))) {
            a.b(TAG, "No such Job ID in jobMap");
        }
        MeshSceneJob meshSceneJob = this.jobMap.get(Integer.valueOf(i));
        if (list != null && list.size() > 0) {
            meshSceneJob.addDevList(list);
        }
        if (map != null && map.size() > 0) {
            meshSceneJob.setAttributeMap(map);
        }
        this.additionJobList.add(meshSceneJob);
        return this;
    }

    public void cancelAllCommit() {
        TgMeshManager.getInstance().cancelAllBizRequest();
    }

    public void commit(Context context, boolean z, SceneTransactionCallback<Boolean> sceneTransactionCallback) {
        List<MeshSceneJob> list = !z ? this.additionJobList : this.failedAdditionJobs;
        List<MeshSceneJob> list2 = !z ? this.deletionJobList : this.failedDeletionJobs;
        if (z && isEmptyFailedJob(this.failedAdditionJobs) && isEmptyFailedJob(this.failedDeletionJobs)) {
            a.c(TAG, "Empty failed jobs, do nothing");
            if (sceneTransactionCallback != null) {
                sceneTransactionCallback.onSuccess("", true);
                return;
            }
            return;
        }
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        CopyOnWriteArrayList copyOnWriteArrayList2 = new CopyOnWriteArrayList();
        LinkedList linkedList = new LinkedList();
        for (MeshSceneJob meshSceneJob : list) {
            meshSceneJob.getGroupInfo();
            List<String> devices = meshSceneJob.getDevices();
            Map<String, Object> attributeMap = meshSceneJob.getAttributeMap();
            MeshSceneJob meshSceneJob2 = new MeshSceneJob(-1);
            meshSceneJob2.setAttributeMap(attributeMap);
            copyOnWriteArrayList.add(meshSceneJob2);
            for (String str : devices) {
                SIGMeshBizRequest sIGMeshBizRequestA = SIGMeshBizRequestGenerator.a(context.getApplicationContext(), str, this.sceneNumber, attributeMap, new pa(this, str, sceneTransactionCallback, meshSceneJob2));
                if (sIGMeshBizRequestA != null) {
                    linkedList.add(sIGMeshBizRequestA);
                }
            }
        }
        Iterator<MeshSceneJob> it = list2.iterator();
        while (it.hasNext()) {
            List<String> devices2 = it.next().getDevices();
            MeshSceneJob meshSceneJob3 = new MeshSceneJob(-2);
            copyOnWriteArrayList2.add(meshSceneJob3);
            for (String str2 : devices2) {
                SIGMeshBizRequest sIGMeshBizRequestA2 = SIGMeshBizRequestGenerator.a(context.getApplicationContext(), str2, this.sceneNumber, new qa(this, str2, sceneTransactionCallback, meshSceneJob3));
                if (sIGMeshBizRequestA2 != null) {
                    linkedList.add(sIGMeshBizRequestA2);
                }
            }
        }
        TgMeshManager.getInstance().offerBizRequest(linkedList);
        this.failedAdditionJobs = copyOnWriteArrayList;
        this.failedDeletionJobs = copyOnWriteArrayList2;
        if (this.failedAdditionJobs.size() > 0 || this.failedDeletionJobs.size() > 0) {
            a.b(TAG, "There are failed devIds. Need retry on those.");
        }
    }

    public SceneTransaction deleteDevices(int i, @Nullable List<String> list, @Nullable Map<String, Object> map) {
        a.c(TAG, String.format("delete %d devices for job:%d", Integer.valueOf(list.size()), Integer.valueOf(i)));
        if (!this.jobMap.containsKey(Integer.valueOf(i))) {
            a.b(TAG, "No such Job ID in jobMap");
        }
        MeshSceneJob meshSceneJob = this.jobMap.get(Integer.valueOf(i));
        if (list != null && list.size() > 0) {
            meshSceneJob.addDevList(list);
        }
        this.deletionJobList.add(meshSceneJob);
        return this;
    }

    public SceneTransaction deleteSceneJob(int i) {
        if (!this.jobMap.containsKey(Integer.valueOf(i))) {
            a.b(TAG, "No such Job ID in jobMap");
        }
        this.jobMap.remove(Integer.valueOf(i));
        return this;
    }

    public int getFailedRecordSize() {
        List<MeshSceneJob> list = this.failedAdditionJobs;
        int size = 0;
        if (list != null) {
            for (MeshSceneJob meshSceneJob : list) {
                if (meshSceneJob.getDevices() != null && meshSceneJob.getDevices().size() > 0) {
                    size += meshSceneJob.getDevices().size();
                }
            }
        }
        List<MeshSceneJob> list2 = this.failedDeletionJobs;
        if (list2 != null) {
            for (MeshSceneJob meshSceneJob2 : list2) {
                if (meshSceneJob2.getDevices() != null && meshSceneJob2.getDevices().size() > 0) {
                    size += meshSceneJob2.getDevices().size();
                }
            }
        }
        return size;
    }

    public void init(short s) {
        a.c(TAG, "init scene transaction with sceneNumber: " + ((int) s));
        this.sceneNumber = s;
        this.jobMap = new ConcurrentHashMap();
        this.additionJobList = new CopyOnWriteArrayList();
        this.deletionJobList = new CopyOnWriteArrayList();
        this.failedAdditionJobs = new CopyOnWriteArrayList();
        this.failedDeletionJobs = new CopyOnWriteArrayList();
        this.jobIdCount = new AtomicInteger(0);
    }

    public void preview(List<MeshSceneJob.MeshGroupInfo> list, List<String> list2, Map<String, Object> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        LinkedList linkedList = new LinkedList();
        if (list == null || list.size() <= 0) {
            a.c(TAG, "empty groupInfo List");
        } else {
            Iterator<MeshSceneJob.MeshGroupInfo> it = list.iterator();
            while (it.hasNext()) {
                linkedList.add(SIGMeshBizRequestGenerator.a(it.next().getDstAddress(), map));
            }
        }
        if (list2 == null || list2.size() <= 0) {
            a.c(TAG, "empty devices List");
        } else {
            Iterator<String> it2 = list2.iterator();
            while (it2.hasNext()) {
                String strCoverIotIdToDevId = MeshDeviceInfoManager.getInstance().coverIotIdToDevId(it2.next());
                ProvisionedMeshNode provisionedMeshNodeB = G.a().d().b(strCoverIotIdToDevId);
                if (provisionedMeshNodeB == null) {
                    a.d(TAG, String.format("%s not found!", strCoverIotIdToDevId));
                } else {
                    linkedList.add(SIGMeshBizRequestGenerator.a(provisionedMeshNodeB.getUnicastAddress(), map));
                }
            }
        }
        if (linkedList.size() > 0) {
            TgMeshManager.getInstance().offerBizRequest(linkedList);
        }
    }

    public int putSceneJob(@Nullable List<MeshSceneJob.MeshGroupInfo> list, @Nullable List<String> list2, @Nullable Map<String, Object> map) {
        int andIncrement = this.jobIdCount.getAndIncrement();
        a.c(TAG, "put job: " + andIncrement);
        MeshSceneJob meshSceneJob = new MeshSceneJob(andIncrement);
        if (list != null && list.size() > 0) {
            meshSceneJob.setGroupInfo(list);
        }
        if (list2 != null && list2.size() > 0) {
            meshSceneJob.setDevices(list2);
        }
        if (map != null && map.size() > 0) {
            meshSceneJob.setAttributeMap(map);
        }
        if (this.jobMap.containsKey(Integer.valueOf(andIncrement))) {
            a.b(TAG, "Conflict of Job ID in jobMap");
        } else {
            this.jobMap.put(Integer.valueOf(andIncrement), meshSceneJob);
        }
        return andIncrement;
    }

    public SceneTransaction updateAttribute(int i, Map<String, Object> map) {
        if (!this.jobMap.containsKey(Integer.valueOf(i))) {
            a.b(TAG, "No such Job ID in jobMap");
        }
        this.jobMap.get(Integer.valueOf(i)).setAttributeMap(map);
        return this;
    }

    public int putSceneJob() {
        int andIncrement = this.jobIdCount.getAndIncrement();
        MeshSceneJob meshSceneJob = new MeshSceneJob(andIncrement);
        if (!this.jobMap.containsKey(Integer.valueOf(andIncrement))) {
            this.jobMap.put(Integer.valueOf(andIncrement), meshSceneJob);
        } else {
            a.b(TAG, "Conflict of Job ID in jobMap");
        }
        return andIncrement;
    }
}
