package com.alibaba.ailabs.iot.mesh;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import meshprovisioner.utils.AddressUtils;

/* JADX INFO: loaded from: classes.dex */
public class MeshSceneJob {
    public static final String TAG = "MeshSceneJob";
    public int jobId;
    public int sceneNumber;
    public List<String> devices = new CopyOnWriteArrayList();
    public List<MeshGroupInfo> groupInfo = new CopyOnWriteArrayList();
    public Map<String, Object> attributeMap = new ConcurrentHashMap();

    public static class MeshGroupInfo {
        public byte[] dstAddress;

        public final byte[] getDstAddress() {
            return this.dstAddress;
        }

        public void setDstAddress(byte[] bArr) {
            this.dstAddress = bArr;
        }

        public void setDstAddress(int i) {
            this.dstAddress = AddressUtils.getUnicastAddressBytes(i);
        }
    }

    public MeshSceneJob(int i) {
        this.jobId = i;
    }

    public void addDevList(List<String> list) {
        this.devices.addAll(list);
    }

    public void addDevice(String str) {
        this.devices.add(str);
    }

    public Map<String, Object> getAttributeMap() {
        return this.attributeMap;
    }

    public List<String> getDevices() {
        return this.devices;
    }

    public List<MeshGroupInfo> getGroupInfo() {
        return this.groupInfo;
    }

    public int getJobId() {
        return this.jobId;
    }

    public void removeDevList(List<String> list) {
        this.devices.removeAll(list);
    }

    public void setAttributeMap(Map<String, Object> map) {
        this.attributeMap = map;
    }

    public void setDevices(List<String> list) {
        this.devices = list;
    }

    public void setGroupInfo(List<MeshGroupInfo> list) {
        this.groupInfo = list;
    }

    public void setJobId(int i) {
        this.jobId = i;
    }
}
