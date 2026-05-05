package com.aliyun.alink.linksdk.tmp.device.panel.linkselection;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.MeshManager;
import com.aliyun.alink.linksdk.tmp.device.panel.data.LocalConnectParams;
import com.aliyun.alink.linksdk.tmp.device.panel.data.PanelMethodExtraData;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelDeviceLocalInitListener;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.SerializePropCallback;
import com.aliyun.alink.linksdk.tmp.device.payload.discovery.GetTslResponsePayload;
import com.aliyun.alink.linksdk.tmp.device.request.DeviceExtended.GetDeviceExtendRequest;
import com.aliyun.alink.linksdk.tmp.device.request.other.GetDeviceNetTypesSupportedRequest;
import com.aliyun.alink.linksdk.tmp.error.CommonError;
import com.aliyun.alink.linksdk.tmp.error.ParamsError;
import com.aliyun.alink.linksdk.tmp.listener.IProcessListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.CheckMeshMessage;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.DeviceClassificationUtil;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.ResponseUtils;
import com.aliyun.alink.linksdk.tmp.utils.TgMeshHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;

/* JADX INFO: loaded from: classes2.dex */
public class MultipleChannelDevice {
    private static final int CLICKTIME = 800;
    private static final String TAG = "[Tmp]MultipleChannelDevice";
    private static final Map<String, ScheduledFuture> scheduledFutureMap = new HashMap();
    private CloudChannelDevice mCloudChannelDevice;
    private String mIotId;
    private LocalChannelDevice mLocalChannelDevice;
    private WeakReference<IPanelEventCallback> mPanelEventCallback;
    private PanelMethodExtraData mPanelMethodExtraData;
    private long clickTime = 0;
    private ScheduledFuture scheduledFuture = null;

    public MultipleChannelDevice(String str, PanelMethodExtraData panelMethodExtraData) {
        StringBuilder sb = new StringBuilder();
        sb.append("MultipleChannelDevice iotId:");
        sb.append(str);
        sb.append(" extraData:");
        sb.append(panelMethodExtraData == null ? TmpConstant.GROUP_ROLE_UNKNOWN : panelMethodExtraData.toString());
        ALog.d(TAG, sb.toString());
        this.mIotId = str;
        if (panelMethodExtraData == null) {
            this.mPanelMethodExtraData = new PanelMethodExtraData(TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST);
        } else {
            this.mPanelMethodExtraData = panelMethodExtraData;
        }
        this.mPanelEventCallback = new WeakReference<>(null);
        this.mCloudChannelDevice = new CloudChannelDevice(str, this, null);
        this.mLocalChannelDevice = new LocalChannelDevice(str, null, this);
    }

    private boolean isIniting() {
        boolean z = this.mCloudChannelDevice.isIniting() && this.mLocalChannelDevice.isLocalIniting();
        ALog.d(TAG, "isIniting :" + z);
        return z;
    }

    private boolean isLocalIniting() {
        boolean zIsLocalIniting = this.mLocalChannelDevice.isLocalIniting();
        ALog.d(TAG, "isLocalIniting :" + zIsLocalIniting);
        return zIsLocalIniting;
    }

    public boolean isLocalConnected() {
        boolean zIsLocalConnected = this.mLocalChannelDevice.isLocalConnected();
        ALog.d(TAG, "isLocalInited :" + zIsLocalConnected);
        return zIsLocalConnected;
    }

    public void uninit() {
        ALog.d(TAG, "uninit iotid:" + this.mIotId);
        this.mCloudChannelDevice.uninit();
        this.mLocalChannelDevice.uninit();
    }

    public void init(IPanelCallback iPanelCallback) {
        init(null);
    }

    public String getIotId() {
        return this.mIotId;
    }

    public String getProductKey() {
        return this.mLocalChannelDevice.getProductKey();
    }

    public String getDeviceName() {
        return this.mLocalChannelDevice.getDeviceName();
    }

    public IPanelEventCallback getPanelEventCallback() {
        return this.mPanelEventCallback.get();
    }

    public void init(final IPanelCallback iPanelCallback, IPanelDeviceLocalInitListener iPanelDeviceLocalInitListener) {
        ALog.d(TAG, "init  mIotId:" + this.mIotId + " callback:" + iPanelCallback + " mIsIniting:" + isIniting() + " localInitListener:" + iPanelDeviceLocalInitListener);
        if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_ONLY == this.mPanelMethodExtraData.channelStrategy) {
            this.mLocalChannelDevice.init(new LocalConnectParams(), iPanelCallback, iPanelDeviceLocalInitListener);
        } else if (TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY == this.mPanelMethodExtraData.channelStrategy) {
            this.mCloudChannelDevice.init(iPanelCallback);
        } else if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST == this.mPanelMethodExtraData.channelStrategy) {
            this.mLocalChannelDevice.init(new LocalConnectParams(), new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    MultipleChannelDevice.this.mCloudChannelDevice.init(iPanelCallback);
                }
            }, iPanelDeviceLocalInitListener);
        }
    }

    public boolean isInit() {
        boolean zIsInit;
        if (TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY == this.mPanelMethodExtraData.channelStrategy) {
            zIsInit = this.mCloudChannelDevice.isInit();
        } else if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_ONLY == this.mPanelMethodExtraData.channelStrategy) {
            zIsInit = this.mLocalChannelDevice.isInit();
        } else {
            zIsInit = this.mCloudChannelDevice.isInit() && this.mLocalChannelDevice.isInit();
        }
        ALog.d(TAG, "isInit :" + zIsInit);
        return zIsInit;
    }

    public void getDeviceTSL(final IPanelCallback iPanelCallback) {
        if (iPanelCallback == null) {
            ALog.e(TAG, "getDeviceTSL callback null");
            return;
        }
        ALog.d(TAG, "getDeviceTSL callback:" + iPanelCallback);
        CloudUtils.getTsl(this.mIotId, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.2
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.e(MultipleChannelDevice.TAG, "getDeviceTSL success");
                ALog.d(MultipleChannelDevice.TAG, "updateTslByCloud ***** iotId:" + MultipleChannelDevice.this.mIotId + " onResponse:" + aResponse);
                if (aResponse == null || aResponse.data == null) {
                    iPanelCallback.onComplete(false, new ErrorInfo(300, "getTsl aResponse error"));
                    return;
                }
                try {
                    GetTslResponsePayload getTslResponsePayload = (GetTslResponsePayload) JSON.parseObject(String.valueOf(aResponse.data), GetTslResponsePayload.class);
                    ALog.d(MultipleChannelDevice.TAG, "onResponse() called with: aResponse = [" + aResponse.getData() + "]");
                    StringBuilder sb = new StringBuilder();
                    sb.append("onResponse() called with: getCode = ");
                    sb.append(getTslResponsePayload.getCode());
                    ALog.d(MultipleChannelDevice.TAG, sb.toString());
                    iPanelCallback.onComplete(true, getTslResponsePayload);
                } catch (Exception e) {
                    ALog.e(MultipleChannelDevice.TAG, "parseObject error:" + e.toString());
                    iPanelCallback.onComplete(false, new ErrorInfo(300, "parseObject error:" + e.toString()));
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.e(MultipleChannelDevice.TAG, "getDeviceTSL failure");
                iPanelCallback.onComplete(false, aError);
            }
        });
    }

    public void queryTimerDownlinkTask(final IPanelCallback iPanelCallback) {
        if (iPanelCallback == null) {
            ALog.e(TAG, "queryTimerDownlinkTask callback null");
            return;
        }
        ALog.d(TAG, "queryTimerDownlinkTask callback:" + iPanelCallback);
        CloudUtils.queryTimerDownlinkTask(this.mIotId, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.3
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.e(MultipleChannelDevice.TAG, "queryTimerDownlinkTask success");
                ALog.d(MultipleChannelDevice.TAG, "queryTimerDownlinkTask ***** iotId:" + MultipleChannelDevice.this.mIotId + " onResponse:" + aResponse);
                if (aResponse == null || aResponse.data == null) {
                    iPanelCallback.onComplete(false, new ErrorInfo(300, "getTsl aResponse error"));
                    return;
                }
                try {
                    GetTslResponsePayload getTslResponsePayload = (GetTslResponsePayload) JSON.parseObject(String.valueOf(aResponse.data), GetTslResponsePayload.class);
                    ALog.d(MultipleChannelDevice.TAG, "queryTimerDownlinkTask() called with: aResponse = [" + aResponse.getData() + "]");
                    StringBuilder sb = new StringBuilder();
                    sb.append("queryTimerDownlinkTask() called with: getCode = ");
                    sb.append(getTslResponsePayload.getCode());
                    ALog.d(MultipleChannelDevice.TAG, sb.toString());
                    iPanelCallback.onComplete(true, getTslResponsePayload);
                } catch (Exception e) {
                    ALog.e(MultipleChannelDevice.TAG, "parseObject error:" + e.toString());
                    iPanelCallback.onComplete(false, new ErrorInfo(300, "parseObject error:" + e.toString()));
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.e(MultipleChannelDevice.TAG, "queryTimerDownlinkTask failure");
                iPanelCallback.onComplete(false, aError);
            }
        });
    }

    public void getProperties(final IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        StringBuilder sb = new StringBuilder();
        sb.append("getProperties callback:");
        sb.append(iPanelCallback);
        sb.append(" extraData:");
        sb.append(panelMethodExtraData == null ? "" : panelMethodExtraData.toString());
        ALog.d(TAG, sb.toString());
        if (iPanelCallback == null) {
            ALog.e(TAG, "getProperties callback null");
            return;
        }
        if (panelMethodExtraData == null) {
            ALog.w(TAG, "getProperties extraData null");
            panelMethodExtraData = new PanelMethodExtraData(TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST);
        }
        if (TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            this.mCloudChannelDevice.getProperties(iPanelCallback);
        } else if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            this.mLocalChannelDevice.getProperties(iPanelCallback, true);
        } else if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST == panelMethodExtraData.channelStrategy) {
            this.mLocalChannelDevice.getProperties(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.4
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    if (!z) {
                        MultipleChannelDevice.this.mCloudChannelDevice.getProperties(iPanelCallback);
                    } else {
                        iPanelCallback.onComplete(z, obj);
                    }
                }
            }, true);
        }
    }

    public void setProperties(final String str, final IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        final PanelMethodExtraData panelMethodExtraData2;
        StringBuilder sb = new StringBuilder();
        sb.append("setProperties params:");
        sb.append(str);
        sb.append(" extraData:");
        sb.append(panelMethodExtraData == null ? "" : panelMethodExtraData.toString());
        ALog.d(TAG, sb.toString());
        if (iPanelCallback == null) {
            ALog.e(TAG, "setProperties callback null");
            return;
        }
        JSONObject object = JSONObject.parseObject(str);
        JSONObject jSONObject = object != null ? object.getJSONObject("items") : null;
        final boolean z = jSONObject != null && jSONObject.containsKey("DeviceTimer");
        ALog.d(TAG, "device timer set action: " + z);
        if (panelMethodExtraData == null) {
            ALog.w(TAG, "setProperties extraData null");
            panelMethodExtraData2 = new PanelMethodExtraData(TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST);
        } else {
            panelMethodExtraData2 = panelMethodExtraData;
        }
        if (DeviceShadowMgr.getInstance().getSupportDownAllProps(getIotId())) {
            panelMethodExtraData2.channelStrategy = TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY;
        }
        final IPanelCallback iPanelCallback2 = new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.5
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z2, @Nullable Object obj) {
                iPanelCallback.onComplete(z2, obj);
                if (!z2 || z) {
                    return;
                }
                CheckMeshMessage.updateDeviceProperties(MultipleChannelDevice.this.getIotId(), str, MultipleChannelDevice.this.getPanelEventCallback());
                CheckMeshMessage.refreshAppDevice(MultipleChannelDevice.this.getIotId(), str);
            }
        };
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.clickTime;
        if (jCurrentTimeMillis - j > 800 || j == 0 || !isBleMeshDevice() || !panelMethodExtraData2.mRateLimiting) {
            ALog.d(TAG, "直接下发 params:");
            if (isBleMeshDevice()) {
                iPanelCallback = iPanelCallback2;
            }
            setProperties(str, iPanelCallback, panelMethodExtraData2, true);
        } else {
            ALog.d(TAG, "延迟下发 params:" + ((this.clickTime + 800) - System.currentTimeMillis()));
            ScheduledFuture scheduledFuture = this.scheduledFuture;
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
            }
            this.scheduledFuture = new ScheduledThreadPoolExecutor(1, new ThreadFactory() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.6
                @Override // java.util.concurrent.ThreadFactory
                public Thread newThread(@NonNull Runnable runnable) {
                    return new Thread(runnable, MultipleChannelDevice.TAG);
                }
            }).schedule(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.7
                @Override // java.lang.Runnable
                public void run() {
                    MultipleChannelDevice multipleChannelDevice = MultipleChannelDevice.this;
                    multipleChannelDevice.setProperties(str, multipleChannelDevice.isBleMeshDevice() ? iPanelCallback2 : iPanelCallback, panelMethodExtraData2, true);
                }
            }, (this.clickTime + 800) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
        this.clickTime = System.currentTimeMillis();
    }

    public void setProperties(final String str, final IPanelCallback iPanelCallback, final PanelMethodExtraData panelMethodExtraData, boolean z) {
        panelMethodExtraData.build(JSON.parseObject(str));
        ALog.d(TAG, "extraData :" + panelMethodExtraData);
        if (TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            this.mCloudChannelDevice.setProperties(str, panelMethodExtraData, iPanelCallback);
            return;
        }
        if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            this.mLocalChannelDevice.setProperties(str, panelMethodExtraData, iPanelCallback, true);
        } else if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST == panelMethodExtraData.channelStrategy) {
            if (DeviceClassificationUtil.isBleMeshDeviceViaProductInfo(TmpStorage.getInstance().getProductInfo(this.mIotId))) {
                meshLocalFrisControl(str, iPanelCallback, panelMethodExtraData);
            } else {
                this.mLocalChannelDevice.setProperties(str, panelMethodExtraData, new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.8
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z2, Object obj) {
                        if (!z2) {
                            TmpStorage.getInstance().getProductInfo(MultipleChannelDevice.this.mIotId);
                            MultipleChannelDevice.this.mCloudChannelDevice.setProperties(str, panelMethodExtraData, iPanelCallback);
                        } else {
                            iPanelCallback.onComplete(true, obj);
                        }
                    }
                }, true);
            }
        }
    }

    private void meshLocalFrisControl(final String str, final IPanelCallback iPanelCallback, final PanelMethodExtraData panelMethodExtraData) {
        ALog.d(TAG, "meshLocalFrisControl");
        if (TgMeshHelper.isMeshNodeReachable(this.mIotId)) {
            ALog.d(TAG, "isMeshNodeReachable true");
            this.mLocalChannelDevice.setProperties(str, panelMethodExtraData, new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.9
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    if (z) {
                        iPanelCallback.onComplete(true, obj);
                    } else if (DeviceClassificationUtil.isBleMeshDeviceViaProductInfo(TmpStorage.getInstance().getProductInfo(MultipleChannelDevice.this.mIotId))) {
                        MultipleChannelDevice.this.bleMeshCloudControl(str, panelMethodExtraData, iPanelCallback);
                    } else {
                        MultipleChannelDevice.this.mCloudChannelDevice.setProperties(str, panelMethodExtraData, iPanelCallback);
                    }
                }
            }, true);
        } else {
            ALog.d(TAG, "isMeshNodeReachable false");
            TgMeshHelper.connect(this.mIotId);
            bleMeshCloudControl(str, panelMethodExtraData, iPanelCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void backupCloudControl(String str, PanelMethodExtraData panelMethodExtraData, final IPanelCallback iPanelCallback) {
        ALog.d(TAG, "backupCloudControl");
        this.mLocalChannelDevice.setProperties(str, panelMethodExtraData, new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.10
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    iPanelCallback.onComplete(true, obj);
                } else {
                    iPanelCallback.onComplete(false, new JSONObject());
                }
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bleMeshCloudControl(String str, final PanelMethodExtraData panelMethodExtraData, final IPanelCallback iPanelCallback) {
        ALog.d(TAG, "bleMeshCloudControl params:" + str);
        JSONObject object = JSON.parseObject(str);
        object.put("comboFlag", Boolean.valueOf(isMeshComboDevice()));
        final String jSONString = JSON.toJSONString(object);
        this.mCloudChannelDevice.getStatus(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.11
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                ALog.d(MultipleChannelDevice.TAG, "bleMeshCloudControl getStatus isSuccess:" + z);
                if (z && obj != null && !TextUtils.isEmpty(obj.toString())) {
                    try {
                        JSONObject object2 = JSONObject.parseObject(obj.toString());
                        if (object2 != null && object2.containsKey("data") && object2.getJSONObject("data") != null && object2.getJSONObject("data").containsKey("status") && object2.getJSONObject("data").getInteger("status").intValue() == 1) {
                            ALog.d(MultipleChannelDevice.TAG, "mCloudChannelDevice.setProperties params:" + jSONString);
                            MultipleChannelDevice.this.mCloudChannelDevice.setPropertiesMesh(jSONString, panelMethodExtraData, iPanelCallback);
                        } else {
                            ALog.d(MultipleChannelDevice.TAG, "bleMeshCloudControl getStatus jsData:" + object2);
                            MultipleChannelDevice.this.backupCloudControl(jSONString, panelMethodExtraData, iPanelCallback);
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        ALog.d(MultipleChannelDevice.TAG, "bleMeshCloudControl getStatus Exception");
                        MultipleChannelDevice.this.backupCloudControl(jSONString, panelMethodExtraData, iPanelCallback);
                        return;
                    }
                }
                ALog.d(MultipleChannelDevice.TAG, "bleMeshCloudControl getStatus false");
                MultipleChannelDevice.this.backupCloudControl(jSONString, panelMethodExtraData, iPanelCallback);
            }
        });
    }

    private boolean isMeshComboDevice() {
        return DeviceClassificationUtil.isComboMeshDeviceViaIotID(this.mIotId);
    }

    public void setPropertyAlias(String str, IPanelCallback iPanelCallback) {
        DeviceShadowMgr.getInstance().setPropertyAlias(str, iPanelCallback);
    }

    @Deprecated
    public void getEvents(IPanelCallback iPanelCallback) {
        getLastEvent(iPanelCallback);
    }

    public void getLastEvent(IPanelCallback iPanelCallback) {
        this.mCloudChannelDevice.getLastEvent(iPanelCallback);
    }

    public void invokeService(final String str, final IPanelCallback iPanelCallback, final PanelMethodExtraData panelMethodExtraData) {
        StringBuilder sb = new StringBuilder();
        sb.append("invokeService params:");
        sb.append(str);
        sb.append("callback:");
        sb.append(iPanelCallback);
        sb.append(" extraData:");
        sb.append(panelMethodExtraData == null ? "" : panelMethodExtraData.toString());
        ALog.d(TAG, sb.toString());
        if (iPanelCallback == null) {
            ALog.e(TAG, "invokeService callback null");
            return;
        }
        if (panelMethodExtraData == null) {
            ALog.w(TAG, "invokeService extraData null");
            panelMethodExtraData = new PanelMethodExtraData(TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST);
        }
        panelMethodExtraData.build(JSON.parseObject(str));
        if (TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            this.mCloudChannelDevice.invokeService(str, panelMethodExtraData, iPanelCallback);
        } else if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            this.mLocalChannelDevice.invokeService(str, panelMethodExtraData, iPanelCallback, true);
        } else if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST == panelMethodExtraData.channelStrategy) {
            this.mLocalChannelDevice.invokeService(str, panelMethodExtraData, new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.12
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    if (!z) {
                        MultipleChannelDevice.this.mCloudChannelDevice.invokeService(str, panelMethodExtraData, iPanelCallback);
                    } else {
                        iPanelCallback.onComplete(z, obj);
                    }
                }
            }, true);
        }
    }

    public void getStatus(IPanelCallback iPanelCallback) {
        int i;
        ALog.d(TAG, "getStatus callback:" + iPanelCallback);
        if (iPanelCallback == null) {
            ALog.e(TAG, "getStatus callback  null");
            return;
        }
        if (isBleMeshDevice()) {
            MeshManager.DeviceStatus meshCurrentStatus = MeshManager.getInstance().getMeshCurrentStatus(this.mIotId);
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            switch (meshCurrentStatus) {
                case online:
                    i = 1;
                    break;
                case offline:
                    i = 3;
                    break;
                default:
                    i = 1;
                    break;
            }
            try {
                jSONObject.put("status", i);
                jSONObject.put("time", System.currentTimeMillis());
                iPanelCallback.onComplete(true, ResponseUtils.getSuccessRspJson(jSONObject));
                return;
            } catch (JSONException e) {
                e.printStackTrace();
                iPanelCallback.onComplete(false, new ParamsError());
                return;
            }
        }
        this.mCloudChannelDevice.getStatus(iPanelCallback);
    }

    public void subAllEvents(IPanelEventCallback iPanelEventCallback, IPanelCallback iPanelCallback) {
        subAllEvents(iPanelEventCallback, iPanelCallback, this.mPanelMethodExtraData);
    }

    public void subAllEvents(IPanelEventCallback iPanelEventCallback, final IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        StringBuilder sb = new StringBuilder();
        sb.append("subAllEvents listener:");
        sb.append(iPanelEventCallback);
        sb.append(" callback:");
        sb.append(iPanelCallback);
        sb.append(" extraData:");
        sb.append(panelMethodExtraData == null ? "" : panelMethodExtraData.toString());
        ALog.d(TAG, sb.toString());
        if (iPanelEventCallback == null) {
            ALog.e(TAG, "subAllEvent callback null");
            return;
        }
        final SerializePropCallback serializePropCallback = new SerializePropCallback(iPanelEventCallback);
        this.mPanelEventCallback = new WeakReference<>(serializePropCallback);
        if (panelMethodExtraData == null) {
            ALog.w(TAG, "subAllEvents extraData null");
            panelMethodExtraData = new PanelMethodExtraData(TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST);
        }
        if (TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            this.mCloudChannelDevice.subAllEvents(serializePropCallback, iPanelCallback);
        } else if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_ONLY == panelMethodExtraData.channelStrategy) {
            this.mLocalChannelDevice.subAllEvents(serializePropCallback, iPanelCallback, true);
        } else if (TmpEnum.ChannelStrategy.LOCAL_CHANNEL_FIRST == panelMethodExtraData.channelStrategy) {
            this.mCloudChannelDevice.subAllEvents(serializePropCallback, new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.13
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, @Nullable Object obj) {
                    MultipleChannelDevice.this.mLocalChannelDevice.subAllEvents(serializePropCallback, new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.13.1
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z2, @Nullable Object obj2) {
                            boolean z3 = z2 || z;
                            ALog.d(MultipleChannelDevice.TAG, "subAllEvents onComplete ret:" + z3);
                            if (iPanelCallback != null) {
                                iPanelCallback.onComplete(z3, null);
                            }
                        }
                    }, true);
                }
            });
        }
    }

    public void startLocalConnect(IPanelCallback iPanelCallback) {
        ALog.d(TAG, "startLocalConnect callback:" + iPanelCallback + " mLocalChannelDevice:" + this.mLocalChannelDevice);
        if (iPanelCallback == null) {
            ALog.e(TAG, "startLocalConnect callback:" + iPanelCallback);
            return;
        }
        this.mLocalChannelDevice.startLocalConnect(iPanelCallback);
    }

    public void stopLocalConnect(IPanelCallback iPanelCallback) {
        ALog.d(TAG, "stopLocalConnect callback:" + iPanelCallback);
        if (iPanelCallback == null) {
            ALog.e(TAG, "stopLocalConnect callback:" + iPanelCallback);
            return;
        }
        this.mLocalChannelDevice.stopLocalConnect(iPanelCallback);
    }

    public void getLocalConnectionState(IPanelCallback iPanelCallback) {
        ALog.d(TAG, "getLocalConnectionState islocaliniting:" + isLocalIniting() + " isIniting:" + isIniting());
        if (iPanelCallback == null) {
            ALog.e(TAG, "stopLocalConnect callback:" + iPanelCallback);
            return;
        }
        this.mLocalChannelDevice.getLocalConnectionState(iPanelCallback);
    }

    public void getWifiStatus(final IPanelCallback iPanelCallback) {
        if (iPanelCallback == null) {
            ALog.e(TAG, "getWifiStatus callback empty");
        } else {
            DeviceShadowMgr.getInstance().getDeviceWifiStatus(getIotId(), new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.14
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onSuccess(Object obj) {
                    String str;
                    if (iPanelCallback != null) {
                        String strValueOf = String.valueOf(TmpEnum.DeviceWifiStatus.DeviceWifiStatus_NotSupport.getValue());
                        try {
                            str = (String) ((GetDeviceExtendRequest.DeviceExtendGetResponse) JSON.parseObject((String) obj, GetDeviceExtendRequest.DeviceExtendGetResponse.class)).data;
                        } catch (Exception e) {
                            ALog.e(MultipleChannelDevice.TAG, "getWifiStatus parseObject e:" + e.toString());
                            IPanelCallback iPanelCallback2 = iPanelCallback;
                            if (iPanelCallback2 != null) {
                                iPanelCallback2.onComplete(false, new CommonError(new ErrorInfo(-200, "getDeviceWifiStatus error")));
                            }
                            str = strValueOf;
                        }
                        iPanelCallback.onComplete(true, str);
                    }
                }

                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onFail(ErrorInfo errorInfo) {
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(false, new CommonError(errorInfo));
                    }
                }
            });
        }
    }

    public void getCachedWifiStatus(IPanelCallback iPanelCallback) {
        String str;
        if (iPanelCallback == null) {
            ALog.e(TAG, "getWifiStatus callback empty");
            return;
        }
        String deviceWifiStatus = DeviceShadowMgr.getInstance().getDeviceWifiStatus(getIotId());
        if (TextUtils.isEmpty(deviceWifiStatus)) {
            iPanelCallback.onComplete(false, new CommonError(new ErrorInfo(-200, "getDeviceWifiStatus error")));
            return;
        }
        String strValueOf = String.valueOf(TmpEnum.DeviceWifiStatus.DeviceWifiStatus_NotSupport.getValue());
        try {
            str = (String) ((GetDeviceExtendRequest.DeviceExtendGetResponse) JSON.parseObject(deviceWifiStatus, GetDeviceExtendRequest.DeviceExtendGetResponse.class)).data;
        } catch (Exception e) {
            ALog.e(TAG, "getWifiStatus parseObject e:" + e.toString());
            if (iPanelCallback != null) {
                iPanelCallback.onComplete(false, new CommonError(new ErrorInfo(-200, "getDeviceWifiStatus error")));
            }
            str = strValueOf;
        }
        iPanelCallback.onComplete(true, str);
    }

    public void getDeviceNetTypesSupported(final IPanelCallback iPanelCallback) {
        if (iPanelCallback == null) {
            ALog.e(TAG, "getWifiStatus callback empty");
        } else {
            DeviceShadowMgr.getInstance().getDeviceSupportedNetTypesByIotId(getIotId(), new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice.15
                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onSuccess(Object obj) {
                    GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse getDeviceNetTypesSupportedResponse;
                    if (iPanelCallback != null) {
                        try {
                            getDeviceNetTypesSupportedResponse = (GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse) JSON.parseObject((String) obj, GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse.class);
                        } catch (Exception e) {
                            ALog.e(MultipleChannelDevice.TAG, "getDeviceNetTypesSupported e:" + e.toString());
                            getDeviceNetTypesSupportedResponse = null;
                        }
                        String strValueOf = String.valueOf(TmpEnum.DeviceNetType.NET_UNKNOWN.getValue());
                        if (getDeviceNetTypesSupportedResponse != null) {
                            strValueOf = String.valueOf(TmpEnum.DeviceNetType.formatDeviceNetType((List) getDeviceNetTypesSupportedResponse.data));
                        }
                        iPanelCallback.onComplete(true, strValueOf);
                    }
                }

                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onFail(ErrorInfo errorInfo) {
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(false, new CommonError(errorInfo));
                    }
                }
            });
        }
    }

    public boolean isBleMeshDevice() {
        LocalChannelDevice localChannelDevice = this.mLocalChannelDevice;
        if (localChannelDevice != null) {
            return localChannelDevice.isBleMeshDevice();
        }
        return false;
    }
}
