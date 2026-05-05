package com.alibaba.ailabs.iot.mesh.managers;

import a.a.a.a.b.G;
import a.a.a.a.b.m.a;
import android.text.TextUtils;
import b.u;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.iot.mesh.bean.MeshDeviceInfo;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import datasource.bean.ProvisionAppKey;
import datasource.bean.ProvisionNetKey;
import datasource.bean.SigmeshIotDev;
import datasource.bean.SigmeshKey;
import datasource.bean.local.DeviceBindItem;
import datasource.bean.local.DeviceBindModel;
import datasource.bean.local.DeviceConverModel;
import datasource.bean.local.DeviceModel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes.dex */
public class MeshDeviceInfoManager {
    public static final String TAG = "" + MeshDeviceInfoManager.class.getName();
    public static volatile MeshDeviceInfoManager instance;
    public ConcurrentHashMap<String, String> mappingDevIdMap;
    public ConcurrentHashMap<String, SigmeshIotDev> sigmeshIotDevMap;
    public Map<String, DeviceBindItem> mDeviceId2DeviceBindItems = new LinkedHashMap();
    public List<String> mMacAddressWhiteList = new ArrayList();
    public List<MeshDeviceInfo> meshDeviceInfoList = new CopyOnWriteArrayList();
    public List<String> mLowPowerMacList = new LinkedList();
    public int mDeviceTypeDesc = 0;
    public final String DELETE = RequestParameters.SUBRESOURCE_DELETE;
    public final String ADD = TmpConstant.GROUP_OP_ADD;

    public enum MeshDeviceType {
        TYPE_SIGMESH(1),
        TYPE_SIGMESH_LOWPOWER(16),
        TYPE_TINY_MESH(256);

        public int typeId;

        MeshDeviceType(int i) {
            this.typeId = i;
        }

        public int getTypeId() {
            return this.typeId;
        }
    }

    public MeshDeviceInfoManager() {
        this.sigmeshIotDevMap = null;
        this.mappingDevIdMap = null;
        this.sigmeshIotDevMap = new ConcurrentHashMap<>();
        this.mappingDevIdMap = new ConcurrentHashMap<>();
    }

    private void addProvisionMeshNode(SigmeshIotDev sigmeshIotDev, SigmeshKey sigmeshKey, byte[] bArr) {
        UnprovisionedMeshNode unprovisionedMeshNode = new UnprovisionedMeshNode();
        int unicastaddress = sigmeshIotDev.getUnicastaddress();
        unprovisionedMeshNode.setBluetoothAddress(sigmeshIotDev.getMac());
        unprovisionedMeshNode.setUnicastAddress(new byte[]{(byte) ((unicastaddress >> 8) & 255), (byte) (unicastaddress & 255)});
        unprovisionedMeshNode.setSupportFastProvision(false);
        unprovisionedMeshNode.setIvIndex(ByteBuffer.allocate(4).putInt(0).array());
        unprovisionedMeshNode.setNetworkKey(bArr);
        ProvisionedMeshNode provisionedMeshNode = new ProvisionedMeshNode(unprovisionedMeshNode);
        for (ProvisionAppKey provisionAppKey : sigmeshKey.getProvisionAppKeys()) {
            provisionedMeshNode.setAddedAppKey(provisionAppKey.getAppKeyIndex(), provisionAppKey.getAppKey());
        }
        provisionedMeshNode.setIsProvisioned(true);
        provisionedMeshNode.setConfigured(true);
        String devId = sigmeshIotDev.getDevId();
        provisionedMeshNode.setSupportFastProvision(AliMeshUUIDParserUtil.isSupportFastProvision(devId));
        provisionedMeshNode.setSupportFastGattProvision(AliMeshUUIDParserUtil.isSupportFastProvisionViaGatt(devId));
        provisionedMeshNode.setBluetoothDeviceAddress(sigmeshIotDev.getMac());
        provisionedMeshNode.setDevId(devId);
        if (!TextUtils.isEmpty(sigmeshIotDev.getDeviceKey())) {
            provisionedMeshNode.setDeviceKey(MeshParserUtils.toByteArray(sigmeshIotDev.getDeviceKey()));
        }
        G.a().d().a(provisionedMeshNode, true, false);
    }

    public static MeshDeviceInfoManager getInstance() {
        if (instance == null) {
            synchronized (MeshDeviceInfoManager.class) {
                if (instance == null) {
                    instance = new MeshDeviceInfoManager();
                }
            }
        }
        return instance;
    }

    private void removeMappingDevId(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        for (Map.Entry<String, String> entry : this.mappingDevIdMap.entrySet()) {
            if (str.equals(entry.getValue())) {
                this.mappingDevIdMap.remove(entry.getKey());
            }
        }
    }

    private void updateMappingDevId(String str, String str2) {
        a.a(TAG, String.format("Update mapping iotid(%s) -> devId(%s)", str, str2));
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mappingDevIdMap.put(str, str2);
    }

    public void addLowPowerDevice(List<MeshDeviceInfo> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        for (MeshDeviceInfo meshDeviceInfo : list) {
            try {
                String strReplaceAll = Utils.deviceId2Mac(meshDeviceInfo.getDevId()).replaceAll(":", "");
                if (this.mMacAddressWhiteList != null && !this.mMacAddressWhiteList.contains(strReplaceAll)) {
                    if (!TextUtils.isEmpty(strReplaceAll) && !meshDeviceInfo.isLowPower()) {
                        this.mMacAddressWhiteList.add(strReplaceAll);
                    } else if (meshDeviceInfo.isLowPower() && !this.mLowPowerMacList.contains(strReplaceAll.toLowerCase())) {
                        this.mLowPowerMacList.add(strReplaceAll.toLowerCase());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public boolean checkMacAddressInWhiteList(String str) {
        String strReplaceAll = str.replaceAll(":", "");
        List<String> list = this.mMacAddressWhiteList;
        return list == null || list.size() == 0 || this.mMacAddressWhiteList.contains(strReplaceAll.toUpperCase()) || this.mMacAddressWhiteList.contains(strReplaceAll.toLowerCase()) || this.mMacAddressWhiteList.contains(strReplaceAll);
    }

    public void clearWhiteList() {
        this.mMacAddressWhiteList.clear();
    }

    public String convertDevIdToIotId(String str) {
        SigmeshIotDev sigmeshIotDev;
        return a.a.a.a.b.d.a.f1315a ? str : (TextUtils.isEmpty(str) || (sigmeshIotDev = this.sigmeshIotDevMap.get(str)) == null) ? "" : sigmeshIotDev.getIotId();
    }

    public DeviceConverModel coverDeviceBind(DeviceModel deviceModel) {
        DeviceConverModel deviceConverModel = new DeviceConverModel();
        deviceConverModel.setCommandType(deviceModel.getCommandType());
        deviceConverModel.setCommandDomain(deviceModel.getCommandDomain());
        deviceConverModel.setCommandName(deviceModel.getCommandName());
        DeviceConverModel.PayloadBean payloadBean = new DeviceConverModel.PayloadBean();
        payloadBean.setSize(deviceModel.getPayload().getSize());
        payloadBean.setBatchNo(deviceModel.getPayload().getBatchNo());
        payloadBean.setChangeType(deviceModel.getPayload().getChangeType());
        payloadBean.setMessageId(deviceModel.getPayload().getMessageId());
        payloadBean.setMessageType(deviceModel.getPayload().getMessageType());
        payloadBean.setPageIndex(deviceModel.getPayload().getPageIndex());
        payloadBean.setPageSize(deviceModel.getPayload().getPageSize());
        List<DeviceModel.PayloadBean.DataBean> data = deviceModel.getPayload().getData();
        ArrayList arrayList = new ArrayList();
        for (DeviceModel.PayloadBean.DataBean dataBean : data) {
            DeviceConverModel.PayloadBean.DataBean dataBean2 = new DeviceConverModel.PayloadBean.DataBean();
            dataBean2.setAppKeyIndex(dataBean.getAppKeyIndex());
            dataBean2.setDevId(dataBean.getDevId());
            dataBean2.setDevType(dataBean.getDevType());
            dataBean2.setDevTypeEn(dataBean.getDevTypeEn());
            dataBean2.setInfrared(dataBean.isInfrared());
            dataBean2.setIotId(dataBean.getIotId());
            dataBean2.setLowerPower(dataBean.isLowerPower());
            dataBean2.setNetKeyIndex(dataBean.getNetKeyIndex());
            dataBean2.setOriginType(dataBean.getOriginType());
            dataBean2.setPlatform(dataBean.getPlatform());
            dataBean2.setProductKey(dataBean.getProductKey());
            dataBean2.setStatus(dataBean.getStatus());
            dataBean2.setTransparent(dataBean.isTransparent());
            dataBean2.setUnicastAddress(dataBean.getUnicastAddress());
            dataBean2.setSkillId(3404);
            arrayList.add(dataBean2);
        }
        payloadBean.setData(arrayList);
        deviceConverModel.setPayload(payloadBean);
        return deviceConverModel;
    }

    public String coverIotIdToDevId(String str) {
        return (a.a.a.a.b.d.a.f1315a || TextUtils.isEmpty(str) || !this.mappingDevIdMap.containsKey(str)) ? str : this.mappingDevIdMap.get(str);
    }

    public ConcurrentHashMap<String, SigmeshIotDev> getSigmeshIotDevMap() {
        return this.sigmeshIotDevMap;
    }

    public boolean isComboMeshDevice(String str) {
        return AliMeshUUIDParserUtil.isComboMesh(coverIotIdToDevId(str));
    }

    public boolean isLowCostDeviceExist() {
        List<MeshDeviceInfo> list = this.meshDeviceInfoList;
        if (list == null) {
            a.c(TAG, "isLowCostDeviceExist is null");
            return false;
        }
        if (list.isEmpty()) {
            a.c(TAG, "meshDeviceInfoList is empty");
        }
        for (MeshDeviceInfo meshDeviceInfo : this.meshDeviceInfoList) {
            a.c(TAG, "devId " + meshDeviceInfo.getDevId() + ", lowCostMesh " + meshDeviceInfo.isLowCostMeshDevice());
            if (meshDeviceInfo.isLowCostMeshDevice()) {
                return true;
            }
        }
        return false;
    }

    public boolean isLowPowerDevice(int i) {
        short s = ByteBuffer.wrap(AddressUtils.getUnicastAddressBytes(i)).order(ByteOrder.BIG_ENDIAN).getShort();
        u.a aVarD = G.a().d().d();
        if (aVarD == null) {
            a.b(TAG, "Internal error(the primary network cannot be found, there is a problem with Mesh initialization logic)");
            return false;
        }
        SigmeshIotDev sigmeshIotDevQueryDeviceInfoByUnicastAddress = getInstance().queryDeviceInfoByUnicastAddress(s, aVarD.d());
        if (sigmeshIotDevQueryDeviceInfoByUnicastAddress != null) {
            return isLowPowerDevice(sigmeshIotDevQueryDeviceInfoByUnicastAddress.getDevId());
        }
        a.a(TAG, "sigmeshIotDev is null");
        return false;
    }

    public boolean isLowPowerDeviceViaMac(String str) {
        return this.mLowPowerMacList.contains(str.replaceAll(":", "").toLowerCase());
    }

    public boolean isOnyTinyMeshExistInCurrentUser() {
        int i = this.mDeviceTypeDesc;
        return i != 0 && (i | MeshDeviceType.TYPE_TINY_MESH.typeId) == MeshDeviceType.TYPE_TINY_MESH.typeId;
    }

    public boolean isTinyMeshExistInCurrentUser() {
        int i = this.mDeviceTypeDesc;
        return i != 0 && (i & MeshDeviceType.TYPE_TINY_MESH.typeId) == MeshDeviceType.TYPE_TINY_MESH.typeId;
    }

    public void parseDeviceBindList(DeviceBindModel deviceBindModel) {
        if (deviceBindModel == null || deviceBindModel.getData() == null || deviceBindModel.getData().size() == 0) {
            return;
        }
        List<DeviceBindItem> data = deviceBindModel.getData();
        if (RequestParameters.SUBRESOURCE_DELETE.equals(deviceBindModel.getChangeType())) {
            Iterator<DeviceBindItem> it = data.iterator();
            while (it.hasNext()) {
                this.mDeviceId2DeviceBindItems.remove(it.next().getDevId().toLowerCase());
            }
            ArrayList arrayList = new ArrayList();
            for (DeviceBindItem deviceBindItem : data) {
                if (!TextUtils.isEmpty(deviceBindItem.getDevId())) {
                    arrayList.add(deviceBindItem.getDevId());
                    String strReplaceAll = ("sigmesh".equals(deviceBindItem.getPlatform()) || "tinymesh".equals(deviceBindItem.getPlatform()) || "sigmeshLowPower".equals(deviceBindItem.getPlatform())) ? Utils.deviceId2Mac(deviceBindItem.getDevId()).replaceAll(":", "") : null;
                    if (!TextUtils.isEmpty(strReplaceAll) && !deviceBindItem.isLowerPower()) {
                        if (!this.mMacAddressWhiteList.contains(strReplaceAll)) {
                            this.mMacAddressWhiteList.add(strReplaceAll);
                        }
                        this.mLowPowerMacList.remove(strReplaceAll.toLowerCase());
                    }
                }
            }
            removeDeviceInfoViaIds(arrayList);
            TgMeshManager.getInstance().updateMeshNetworkParameters(true);
            return;
        }
        for (DeviceBindItem deviceBindItem2 : data) {
            if (!this.mDeviceId2DeviceBindItems.containsKey(deviceBindItem2.getDevId().toLowerCase())) {
                this.mDeviceId2DeviceBindItems.put(deviceBindItem2.getDevId().toLowerCase(), deviceBindItem2);
            }
        }
        if (a.a.a.a.b.d.a.f1315a) {
            u.a aVarD = G.a().d().d();
            String address = (aVarD == null || aVarD.e() == null || aVarD.e().h() == null) ? null : aVarD.e().h().getAddress();
            a.a(TAG, "connect device mac = " + address);
            if (!TextUtils.isEmpty(address)) {
                for (DeviceBindItem deviceBindItem3 : data) {
                    String strDeviceId2Mac = ("sigmesh".equals(deviceBindItem3.getPlatform()) || "tinymesh".equals(deviceBindItem3.getPlatform()) || "sigmeshLowPower".equals(deviceBindItem3.getPlatform())) ? Utils.deviceId2Mac(deviceBindItem3.getDevId()) : null;
                    a.a(TAG, "change type add isLowPower = " + deviceBindItem3.isLowerPower() + ", mac = " + strDeviceId2Mac);
                    if (!TextUtils.isEmpty(strDeviceId2Mac) && deviceBindItem3.isLowerPower() && address.equalsIgnoreCase(strDeviceId2Mac) && aVarD.j()) {
                        a.a(TAG, "is lowPower disconnect mac = " + strDeviceId2Mac);
                        G.a().d().a(true, (MeshService.OnDisconnectListener) new a.a.a.a.b.g.a(this));
                    }
                }
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (DeviceBindItem deviceBindItem4 : data) {
            SigmeshIotDev sigmeshIotDev = new SigmeshIotDev();
            sigmeshIotDev.setDevId(deviceBindItem4.getDevId());
            sigmeshIotDev.setUnicastaddress(deviceBindItem4.getUnicastAddress());
            sigmeshIotDev.setProductKey(deviceBindItem4.getProductKey());
            sigmeshIotDev.setIotId(deviceBindItem4.getIotId());
            if ("sigmesh".equals(deviceBindItem4.getPlatform()) || "tinymesh".equals(deviceBindItem4.getPlatform()) || "sigmeshLowPower".equals(deviceBindItem4.getPlatform())) {
                String strReplaceAll2 = Utils.deviceId2Mac(deviceBindItem4.getDevId()).replaceAll(":", "");
                if (TextUtils.isEmpty(strReplaceAll2) || deviceBindItem4.isLowerPower()) {
                    if (deviceBindItem4.isLowerPower() && !this.mLowPowerMacList.contains(strReplaceAll2.toLowerCase())) {
                        this.mLowPowerMacList.add(strReplaceAll2.toLowerCase());
                    }
                } else if (!this.mMacAddressWhiteList.contains(strReplaceAll2)) {
                    this.mMacAddressWhiteList.add(strReplaceAll2);
                }
                ProvisionedMeshNode provisionedMeshNode = (ProvisionedMeshNode) G.a().b();
                if (provisionedMeshNode != null) {
                    ArrayList arrayList3 = new ArrayList();
                    SigmeshKey sigmeshKey = new SigmeshKey();
                    ProvisionNetKey provisionNetKey = new ProvisionNetKey();
                    provisionNetKey.setNetKeyIndex(0);
                    provisionNetKey.setNetKey(MeshParserUtils.bytesToHex(provisionedMeshNode.getNetworkKey(), false));
                    sigmeshKey.setProvisionNetKey(provisionNetKey);
                    if (provisionedMeshNode.getAddedAppKeys() != null) {
                        ArrayList arrayList4 = new ArrayList();
                        ProvisionAppKey provisionAppKey = new ProvisionAppKey();
                        provisionAppKey.setAppKeyIndex(0);
                        provisionAppKey.setAppKey(provisionedMeshNode.getAddedAppKeys().get(0));
                        arrayList4.add(provisionAppKey);
                        arrayList4.add(provisionAppKey);
                        sigmeshKey.setProvisionAppKeys(arrayList4);
                    }
                    arrayList3.add(sigmeshKey);
                    sigmeshIotDev.setSigmeshKeys(arrayList3);
                    addProvisionMeshNode(sigmeshIotDev, sigmeshKey, provisionedMeshNode.getNetworkKey());
                }
                arrayList2.add(sigmeshIotDev);
                if (!a.a.a.a.b.d.a.f1315a) {
                    updateMappingDevId(deviceBindItem4.getIotId(), deviceBindItem4.getDevId());
                }
            }
        }
        updateDeviceInfos(arrayList2);
    }

    public SigmeshIotDev queryDeviceInfoByUnicastAddress(int i, byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        String strBytesToHex = MeshParserUtils.bytesToHex(bArr, false);
        for (SigmeshIotDev sigmeshIotDev : this.sigmeshIotDevMap.values()) {
            if (sigmeshIotDev.getUnicastaddress() == i) {
                if (a.a.a.a.b.d.a.f1315a) {
                    return sigmeshIotDev;
                }
                if (sigmeshIotDev.getSigmeshKeys() == null) {
                    continue;
                } else {
                    Iterator<SigmeshKey> it = sigmeshIotDev.getSigmeshKeys().iterator();
                    while (it.hasNext()) {
                        ProvisionNetKey provisionNetKey = it.next().getProvisionNetKey();
                        if (provisionNetKey != null && provisionNetKey.getNetKey().equalsIgnoreCase(strBytesToHex)) {
                            return sigmeshIotDev;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void removeDeviceInfoViaIds(List<String> list) {
        if (list == null) {
            return;
        }
        for (String str : list) {
            if (!TextUtils.isEmpty(str)) {
                this.sigmeshIotDevMap.remove(str);
                removeMappingDevId(str);
            }
        }
    }

    public void setMeshDeviceInfo(List<MeshDeviceInfo> list) {
        addLowPowerDevice(list);
        this.meshDeviceInfoList.clear();
        if (list != null) {
            this.meshDeviceInfoList.addAll(list);
        }
        this.mDeviceTypeDesc = 0;
        for (MeshDeviceInfo meshDeviceInfo : list) {
            if (meshDeviceInfo != null) {
                if (meshDeviceInfo.isLowCostMeshDevice()) {
                    this.mDeviceTypeDesc |= MeshDeviceType.TYPE_TINY_MESH.getTypeId();
                } else if (meshDeviceInfo.isLowPower()) {
                    this.mDeviceTypeDesc |= MeshDeviceType.TYPE_SIGMESH_LOWPOWER.getTypeId();
                } else {
                    this.mDeviceTypeDesc |= MeshDeviceType.TYPE_SIGMESH.getTypeId();
                }
            }
        }
    }

    public void updateDeviceInfos(List<SigmeshIotDev> list) {
        if (list == null) {
            return;
        }
        for (SigmeshIotDev sigmeshIotDev : list) {
            if (sigmeshIotDev != null && !TextUtils.isEmpty(sigmeshIotDev.getDevId()) && (sigmeshIotDev.getSigmeshKeys() != null || !this.sigmeshIotDevMap.containsKey(sigmeshIotDev.getDevId()))) {
                this.sigmeshIotDevMap.put(sigmeshIotDev.getDevId(), sigmeshIotDev);
                if (!a.a.a.a.b.d.a.f1315a) {
                    updateMappingDevId(sigmeshIotDev.getIotId(), sigmeshIotDev.getDevId());
                }
            }
        }
    }

    public boolean isLowPowerDevice(String str) {
        DeviceBindItem deviceBindItem = this.mDeviceId2DeviceBindItems.get(str.toLowerCase(Locale.ROOT));
        if (deviceBindItem != null) {
            return deviceBindItem.isLowerPower();
        }
        SigmeshIotDev sigmeshIotDev = this.sigmeshIotDevMap.get(str);
        if (sigmeshIotDev != null) {
            return sigmeshIotDev.isLowPower();
        }
        return false;
    }
}
