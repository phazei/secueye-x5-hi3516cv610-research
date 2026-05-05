package com.aliyun.alink.linksdk.tmp.device.panel;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentConnectState;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentNet;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.device.panel.data.PanelMethodExtraData;
import com.aliyun.alink.linksdk.tmp.device.panel.data.group.GroupLocalStatePayload;
import com.aliyun.alink.linksdk.tmp.device.panel.linkselection.CloudGroup;
import com.aliyun.alink.linksdk.tmp.device.panel.linkselection.LocalGroup;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.ConnectListenerWrapper;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelGroupEventCallback;
import com.aliyun.alink.linksdk.tmp.device.payload.service.DeviceItem;
import com.aliyun.alink.linksdk.tmp.error.ParamsError;
import com.aliyun.alink.linksdk.tmp.network.NetworkManager;
import com.aliyun.alink.linksdk.tmp.utils.CheckMeshMessage;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.TgMeshHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public class PanelGroup {
    public static final String TAG = "[Tmp]PanelGroup";
    private CloudGroup mCloudGroup;
    private String mGroupId;
    private LocalGroup mLocalGroup;
    private long clickTime = 0;
    private ScheduledFuture scheduledFuture = null;

    public PanelGroup(String str) {
        ALog.d(TAG, "new PanelGroup groupId:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.w(TAG, "PanelGroup groupId empty");
            return;
        }
        this.mGroupId = str;
        this.mCloudGroup = new CloudGroup(str);
        this.mLocalGroup = new LocalGroup(str);
    }

    public void setGroupId(String str) {
        if (!TextUtils.isEmpty(this.mGroupId)) {
            ALog.w(TAG, "setGroupId groupId not empty");
            return;
        }
        this.mGroupId = str;
        this.mLocalGroup = new LocalGroup(str);
        this.mCloudGroup = new CloudGroup(str);
    }

    public void setGroupProperties(final String str, final IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        String string;
        int count;
        JSONObject object;
        ALog.d(TAG, "setGroupProperties params:" + str + " extraData:" + panelMethodExtraData + " callback:" + iPanelCallback);
        if (iPanelCallback == null) {
            ALog.e(TAG, "setGroupProperties callback empty");
            return;
        }
        final PanelMethodExtraData panelMethodExtraData2 = panelMethodExtraData == null ? new PanelMethodExtraData(TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST) : panelMethodExtraData;
        try {
            object = JSON.parseObject(str);
            string = object.getString("controlGroupId");
        } catch (Exception e) {
            e = e;
            string = null;
        }
        try {
            count = object.getInteger("deviceCount").intValue();
        } catch (Exception e2) {
            e = e2;
            ALog.w(TAG, "parseObject w:" + e.toString());
            count = 0;
        }
        if (count == 0) {
            count = TgMeshHelper.getCount(string);
        }
        final boolean z = !TextUtils.isEmpty(string) && TgMeshHelper.isMeshGroup(string);
        int i = 3000;
        if (count < 7) {
            i = 1000;
        } else if (count < 11) {
            i = 2000;
        }
        Log.d(TAG, "setGroupProperties:上次点击时间= " + this.clickTime);
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.clickTime;
        long j2 = (long) i;
        if (jCurrentTimeMillis - j > j2 || j == 0 || !z) {
            ALog.d(TAG, "直接下发 params:");
            setGroupProperties(str, iPanelCallback, panelMethodExtraData2, z);
        } else {
            ALog.d(TAG, "延迟下发 params:" + ((this.clickTime + j2) - System.currentTimeMillis()));
            ScheduledFuture scheduledFuture = this.scheduledFuture;
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
                this.scheduledFuture = null;
            }
            this.scheduledFuture = new ScheduledThreadPoolExecutor(1, new ThreadFactory() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelGroup.1
                @Override // java.util.concurrent.ThreadFactory
                public Thread newThread(@NonNull Runnable runnable) {
                    return new Thread(runnable, PanelGroup.TAG);
                }
            }).schedule(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelGroup.2
                @Override // java.lang.Runnable
                public void run() {
                    ALog.d(PanelGroup.TAG, "延迟下发run: ");
                    PanelGroup.this.setGroupProperties(str, iPanelCallback, panelMethodExtraData2, z);
                    PanelGroup.this.scheduledFuture = null;
                }
            }, (j2 + this.clickTime) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
        this.clickTime = System.currentTimeMillis();
    }

    public void setGroupProperties(final String str, final IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData, boolean z) {
        String string;
        final String str2;
        JSONObject object;
        ALog.d(TAG, "setGroupProperties params:" + str + " extraData:" + panelMethodExtraData + " callback:" + iPanelCallback + " isMesh:" + z);
        if (iPanelCallback == null) {
            ALog.e(TAG, "setGroupProperties callback empty");
            return;
        }
        final PanelMethodExtraData panelMethodExtraData2 = panelMethodExtraData == null ? new PanelMethodExtraData(TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST) : panelMethodExtraData;
        panelMethodExtraData2.build(JSON.parseObject(str));
        if (panelMethodExtraData2 != null && TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY == panelMethodExtraData2.channelStrategy) {
            CloudGroup cloudGroup = this.mCloudGroup;
            if (cloudGroup == null) {
                ALog.e(TAG, "setGroupProperties mCloudGroup empty");
                iPanelCallback.onComplete(false, new ParamsError());
                return;
            } else {
                cloudGroup.setGroupProperties(str, panelMethodExtraData2, iPanelCallback);
                return;
            }
        }
        String string2 = null;
        if (panelMethodExtraData2 != null && TmpEnum.ChannelStrategy.LOCAL_CHANNEL_ONLY == panelMethodExtraData2.channelStrategy) {
            LocalGroup localGroup = this.mLocalGroup;
            if (localGroup == null) {
                ALog.e(TAG, "setGroupProperties mLocalGroup empty");
                if (iPanelCallback != null) {
                    iPanelCallback.onComplete(false, new ParamsError());
                    return;
                }
                return;
            }
            localGroup.setGroupProperties(null, str, iPanelCallback, panelMethodExtraData2);
            return;
        }
        try {
            object = JSON.parseObject(str);
            string = object.getString("controlGroupId");
        } catch (Exception e) {
            e = e;
            string = null;
        }
        try {
            string2 = object.getString("iotId");
            ALog.d(TAG, "controlGroupId:" + string + " mainIotId:" + string2);
            str2 = string;
        } catch (Exception e2) {
            e = e2;
            ALog.w(TAG, "parseObject w:" + e.toString());
            str2 = string;
        }
        if (!TextUtils.isEmpty(str2) && TgMeshHelper.isMeshGroup(str2)) {
            final String str3 = string2;
            final IPanelCallback iPanelCallback2 = new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelGroup.3
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, @Nullable Object obj) {
                    iPanelCallback.onComplete(z2, obj);
                    if (z2) {
                        CheckMeshMessage.refreshAppDevice(str3, str, str2);
                    }
                }
            };
            if (!TextUtils.isEmpty(string2)) {
                if (TgMeshHelper.isMeshNodeReachable(string2)) {
                    ALog.d(TAG, "main device mesh ok");
                    this.mLocalGroup.setMeshGroupProperties(str, iPanelCallback2);
                    return;
                } else {
                    ALog.d(TAG, "main device mesh no");
                    CloudUtils.getStatus(string2, new ConnectListenerWrapper(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelGroup.4
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z2, @Nullable Object obj) {
                            ALog.d(PanelGroup.TAG, "setGroupProperties getStatus isSuccess:" + z2);
                            if (z2 && obj != null && !TextUtils.isEmpty(obj.toString())) {
                                try {
                                    JSONObject object2 = JSONObject.parseObject(obj.toString());
                                    if (object2 != null && object2.containsKey("data") && object2.getJSONObject("data") != null && object2.getJSONObject("data").containsKey("status") && object2.getJSONObject("data").getInteger("status").intValue() == 1) {
                                        if (PanelGroup.this.mCloudGroup != null) {
                                            PanelGroup.this.mCloudGroup.setGroupProperties(str, panelMethodExtraData2, iPanelCallback2);
                                            return;
                                        } else {
                                            ALog.e(PanelGroup.TAG, "setGroupProperties mLocalGroup&mCloudGroup empty ");
                                            iPanelCallback.onComplete(false, new ParamsError());
                                            return;
                                        }
                                    }
                                    ALog.d(PanelGroup.TAG, "setGroupProperties getStatus jsData:" + object2);
                                    iPanelCallback.onComplete(false, new JSONObject());
                                    return;
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                    ALog.d(PanelGroup.TAG, "setGroupProperties getStatus Exception");
                                    iPanelCallback.onComplete(false, new JSONObject());
                                    return;
                                }
                            }
                            ALog.d(PanelGroup.TAG, "setGroupProperties getStatus false");
                            iPanelCallback.onComplete(false, new JSONObject());
                        }
                    }));
                    return;
                }
            }
            ALog.d(TAG, "group main iotId empty");
            iPanelCallback.onComplete(false, new JSONObject());
            return;
        }
        if (this.mLocalGroup == null) {
            CloudGroup cloudGroup2 = this.mCloudGroup;
            if (cloudGroup2 == null) {
                ALog.e(TAG, "setGroupProperties mLocalGroup&mCloudGroup empty ");
                iPanelCallback.onComplete(false, new ParamsError());
                return;
            } else {
                cloudGroup2.setGroupProperties(str, panelMethodExtraData2, iPanelCallback);
                return;
            }
        }
        int networkType = NetworkManager.getNetworkType(TmpSdk.getContext());
        ALog.d(TAG, "getNetworkType networkType:" + networkType);
        if (networkType == 0) {
            this.mCloudGroup.setGroupProperties(str, panelMethodExtraData2, iPanelCallback);
        } else {
            this.mLocalGroup.isAllDeviceLocalOnlineAndConnected(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelGroup.5
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, @Nullable Object obj) {
                    ALog.d(PanelGroup.TAG, "isAllDeviceLocalOnlineAndConnected isSuccess:" + z2);
                    GroupLocalStatePayload groupLocalStatePayload = (!z2 || obj == null) ? null : (GroupLocalStatePayload) JSON.parseObject(String.valueOf(obj), GroupLocalStatePayload.class);
                    if (groupLocalStatePayload != null && groupLocalStatePayload.getData() != null && (groupLocalStatePayload.getData().groupLocalStatus == 1 || groupLocalStatePayload.getData().groupLocalStatus == 0)) {
                        ArrayList arrayList = new ArrayList();
                        final ArrayList arrayList2 = new ArrayList();
                        for (GroupLocalStatePayload.DeviceLocalStatus deviceLocalStatus : groupLocalStatePayload.getData().deviceLocalStatus) {
                            if (deviceLocalStatus != null) {
                                if (deviceLocalStatus.status == 1 && deviceLocalStatus.localOnLineSubStatus == TmpEnum.DeviceNetType.NET_BT.getValue() && PersistentNet.getInstance().getConnectState() == PersistentConnectState.CONNECTED) {
                                    arrayList2.add(new DeviceItem(deviceLocalStatus.iotId));
                                    arrayList.add(new DeviceItem(deviceLocalStatus.iotId));
                                } else {
                                    arrayList.add(new DeviceItem(deviceLocalStatus.iotId));
                                }
                            }
                        }
                        if (arrayList.size() > 0) {
                            PanelGroup.this.mCloudGroup.setBatchDeviceProperties(arrayList, str, panelMethodExtraData2, iPanelCallback);
                        }
                        if (arrayList2.size() > 0) {
                            PanelGroup.this.mLocalGroup.setGroupProperties(arrayList2, str, new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelGroup.5.1
                                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                                public void onComplete(boolean z3, @Nullable Object obj2) {
                                    Log.d(PanelGroup.TAG, "onComplete() called with: isSuccess = [" + z3 + "], data = [" + obj2 + "]");
                                    if (!z3) {
                                        PanelGroup.this.mCloudGroup.setBatchDeviceProperties(arrayList2, str, panelMethodExtraData2, iPanelCallback);
                                    } else {
                                        iPanelCallback.onComplete(true, obj2);
                                    }
                                }
                            }, panelMethodExtraData2);
                            return;
                        }
                        return;
                    }
                    ALog.d(PanelGroup.TAG, "PersistentConnectState connectState :" + PersistentNet.getInstance().getConnectState() + " networkType:" + NetworkManager.getNetworkType(TmpSdk.getContext()));
                    if (PanelGroup.this.mCloudGroup != null) {
                        PanelGroup.this.mCloudGroup.setGroupProperties(str, panelMethodExtraData2, iPanelCallback);
                        return;
                    }
                    ALog.e(PanelGroup.TAG, "setGroupProperties mCloudGroup empty");
                    IPanelCallback iPanelCallback3 = iPanelCallback;
                    if (iPanelCallback3 != null) {
                        iPanelCallback3.onComplete(false, new ParamsError());
                    }
                }
            });
        }
    }

    public void invokeGroupService(final String str, final IPanelCallback iPanelCallback, final PanelMethodExtraData panelMethodExtraData) {
        ALog.d(TAG, "invokeGroupService params:" + str + " extraData:" + panelMethodExtraData + " callback:" + iPanelCallback);
        if (panelMethodExtraData == null) {
            panelMethodExtraData = new PanelMethodExtraData(TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST);
        }
        panelMethodExtraData.build(JSON.parseObject(str));
        if (TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            CloudGroup cloudGroup = this.mCloudGroup;
            if (cloudGroup == null) {
                ALog.e(TAG, "invokeGroupService mCloudGroup empty");
                iPanelCallback.onComplete(false, new ParamsError());
                return;
            } else {
                cloudGroup.invokeGroupService(str, panelMethodExtraData, iPanelCallback);
                return;
            }
        }
        if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            LocalGroup localGroup = this.mLocalGroup;
            if (localGroup == null) {
                ALog.e(TAG, "invokeGroupService mLocalGroup empty");
                if (iPanelCallback != null) {
                    iPanelCallback.onComplete(false, new ParamsError());
                    return;
                }
                return;
            }
            localGroup.invokeGroupService(null, str, iPanelCallback, panelMethodExtraData);
            return;
        }
        if (this.mLocalGroup == null) {
            CloudGroup cloudGroup2 = this.mCloudGroup;
            if (cloudGroup2 == null) {
                ALog.e(TAG, "invokeGroupService mLocalGroup&mCloudGroup empty ");
                iPanelCallback.onComplete(false, new ParamsError());
                return;
            } else {
                cloudGroup2.invokeGroupService(str, panelMethodExtraData, iPanelCallback);
                return;
            }
        }
        int networkType = NetworkManager.getNetworkType(TmpSdk.getContext());
        ALog.d(TAG, "getNetworkType networkType:" + networkType);
        if (networkType == 0) {
            this.mCloudGroup.invokeGroupService(str, panelMethodExtraData, iPanelCallback);
        } else {
            this.mLocalGroup.isAllDeviceLocalOnlineAndConnected(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelGroup.6
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    ALog.d(PanelGroup.TAG, "invokeGroupService isAllDeviceLocalOnlineAndConnected isSuccess:" + z + " data:" + obj);
                    GroupLocalStatePayload groupLocalStatePayload = (!z || obj == null) ? null : (GroupLocalStatePayload) JSON.parseObject(String.valueOf(obj), GroupLocalStatePayload.class);
                    if (groupLocalStatePayload != null && groupLocalStatePayload.getData() != null && (groupLocalStatePayload.getData().groupLocalStatus == 1 || groupLocalStatePayload.getData().groupLocalStatus == 0)) {
                        ArrayList arrayList = new ArrayList();
                        ArrayList arrayList2 = new ArrayList();
                        for (GroupLocalStatePayload.DeviceLocalStatus deviceLocalStatus : groupLocalStatePayload.getData().deviceLocalStatus) {
                            if (deviceLocalStatus != null) {
                                if (deviceLocalStatus.status == 3) {
                                    arrayList.add(new DeviceItem(deviceLocalStatus.iotId));
                                }
                                if (deviceLocalStatus.status == 1 && deviceLocalStatus.localOnLineSubStatus == TmpEnum.DeviceNetType.NET_BT.getValue()) {
                                    if (PersistentNet.getInstance().getConnectState() == PersistentConnectState.CONNECTED) {
                                        arrayList.add(new DeviceItem(deviceLocalStatus.iotId));
                                    } else {
                                        arrayList2.add(new DeviceItem(deviceLocalStatus.iotId));
                                    }
                                }
                            }
                        }
                        if (arrayList.size() > 0) {
                            PanelGroup.this.mCloudGroup.invokeBatchService(arrayList, str, panelMethodExtraData, iPanelCallback);
                        }
                        PanelGroup.this.mLocalGroup.invokeGroupService(arrayList2, str, iPanelCallback, panelMethodExtraData);
                        return;
                    }
                    ALog.d(PanelGroup.TAG, "PersistentConnectState connectState :" + PersistentNet.getInstance().getConnectState() + " networkType:" + NetworkManager.getNetworkType(TmpSdk.getContext()));
                    if (PanelGroup.this.mCloudGroup != null) {
                        PanelGroup.this.mCloudGroup.invokeGroupService(str, panelMethodExtraData, iPanelCallback);
                        return;
                    }
                    ALog.e(PanelGroup.TAG, "invokeGroupService mCloudGroup empty");
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(false, new ParamsError());
                    }
                }
            });
        }
    }

    private boolean choseCloudChannel(GroupLocalStatePayload.DeviceLocalStatus deviceLocalStatus) {
        if (deviceLocalStatus.status == 3) {
            return true;
        }
        return deviceLocalStatus.status == 1 && deviceLocalStatus.localOnLineSubStatus == TmpEnum.DeviceNetType.NET_BT.getValue();
    }

    public void subAllEvents(IPanelGroupEventCallback iPanelGroupEventCallback, IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        LocalGroup localGroup = this.mLocalGroup;
        if (localGroup == null) {
            ALog.e(TAG, "subAllEvents mLocalGroup empty");
            if (iPanelCallback != null) {
                iPanelCallback.onComplete(false, new ParamsError());
                return;
            }
            return;
        }
        localGroup.subAllEvents(iPanelGroupEventCallback, iPanelCallback, panelMethodExtraData);
    }

    public void unsubAllEvents(IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        LocalGroup localGroup = this.mLocalGroup;
        if (localGroup == null) {
            ALog.e(TAG, "subAllEvents mLocalGroup empty");
            if (iPanelCallback != null) {
                iPanelCallback.onComplete(false, new ParamsError());
                return;
            }
            return;
        }
        localGroup.unsubAllEvents(iPanelCallback, panelMethodExtraData);
    }

    public void getLocalState(IPanelCallback iPanelCallback) {
        LocalGroup localGroup = this.mLocalGroup;
        if (localGroup == null) {
            ALog.e(TAG, "getLocalState mLocalGroup empty");
            if (iPanelCallback != null) {
                iPanelCallback.onComplete(false, new ParamsError());
                return;
            }
            return;
        }
        localGroup.getLocalState(iPanelCallback);
    }

    public CloudGroup getCloudGroup() {
        return this.mCloudGroup;
    }

    public LocalGroup getLocalGroup() {
        return this.mLocalGroup;
    }
}
