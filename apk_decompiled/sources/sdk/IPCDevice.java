package sdk;

import android.content.Context;
import bean.InvokeServiceRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.data.PanelMethodExtraData;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.google.gson.Gson;
import config.TMPConstants;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public class IPCDevice implements ILinkVisionAPI2Dev {
    private String iotId;
    private IPanelCallback panelCallback;
    private PanelDevice panelDevice;

    public void QueryPresetMap(IPanelCallback iPanelCallback) {
    }

    public PanelDevice getPanelDevice() {
        return this.panelDevice;
    }

    public String getIotId() {
        return this.iotId;
    }

    public boolean isValid() {
        PanelDevice panelDevice = this.panelDevice;
        return panelDevice != null && panelDevice.isInit();
    }

    public IPCDevice(Context context, String str) {
        this.iotId = "";
        this.iotId = str;
        IPCDeviceInit(context, str, true);
    }

    public IPCDevice(Context context, String str, IPanelCallback iPanelCallback) {
        this.iotId = "";
        this.iotId = str;
        this.panelCallback = iPanelCallback;
        IPCDeviceInit(context, str, true);
    }

    public IPanelCallback getPanelCallback() {
        return this.panelCallback;
    }

    public void reInit(Context context) {
        IPCDeviceInit(context, this.iotId, true);
    }

    public void reInit(Context context, IPanelCallback iPanelCallback) {
        this.panelCallback = iPanelCallback;
        IPCDeviceInit(context, this.iotId, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void IPCDeviceInit(final Context context, final String str, final boolean z) {
        try {
            PanelMethodExtraData panelMethodExtraData = new PanelMethodExtraData(TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY);
            panelMethodExtraData.mNeedRsp = false;
            this.panelDevice = new PanelDevice(str, panelMethodExtraData);
            this.panelDevice.init(context, new IPanelCallback() { // from class: sdk.IPCDevice.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, Object obj) {
                    if (z2) {
                        if (IPCDevice.this.panelCallback != null) {
                            IPCDevice.this.panelCallback.onComplete(z2, obj);
                        }
                    } else if (z) {
                        IPCDevice.this.IPCDeviceInit(context, str, false);
                    } else if (IPCDevice.this.panelCallback != null) {
                        IPCDevice.this.panelCallback.onComplete(z2, obj);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getStatus() {
        if (isValid()) {
            this.panelDevice.getStatus(this.panelCallback);
            return;
        }
        IPanelCallback iPanelCallback = this.panelCallback;
        if (iPanelCallback != null) {
            iPanelCallback.onComplete(false, "init is wrong");
        }
    }

    public void getStatus(IPanelCallback iPanelCallback) {
        if (isValid()) {
            this.panelDevice.getStatus(iPanelCallback);
        } else if (iPanelCallback != null) {
            iPanelCallback.onComplete(false, "init is wrong");
        }
    }

    public void setProperties(Map<String, Object> map) {
        IPanelCallback iPanelCallback;
        if (map == null && (iPanelCallback = this.panelCallback) != null) {
            iPanelCallback.onComplete(false, "request is invalid");
        }
        if (isValid()) {
            HashMap map2 = new HashMap();
            map2.put("iotId", this.iotId);
            map2.put("items", map);
            this.panelDevice.setProperties(JSON.toJSONString(map2), this.panelCallback);
            return;
        }
        IPanelCallback iPanelCallback2 = this.panelCallback;
        if (iPanelCallback2 != null) {
            iPanelCallback2.onComplete(false, "init is wrong");
        }
    }

    public void setProperties(Map<String, Object> map, IPanelCallback iPanelCallback) {
        if (map == null && iPanelCallback != null) {
            iPanelCallback.onComplete(false, "request is invalid");
        }
        if (!isValid()) {
            if (iPanelCallback != null) {
                iPanelCallback.onComplete(false, "init is wrong");
            }
        } else {
            HashMap map2 = new HashMap();
            map2.put("iotId", this.iotId);
            map2.put("items", map);
            this.panelDevice.setProperties(new Gson().toJson(map2), iPanelCallback);
        }
    }

    public void getProperties() {
        if (isValid()) {
            this.panelDevice.getProperties(this.panelCallback);
            return;
        }
        IPanelCallback iPanelCallback = this.panelCallback;
        if (iPanelCallback != null) {
            iPanelCallback.onComplete(false, "init is wrong");
        }
    }

    public void getProperties(IPanelCallback iPanelCallback) {
        if (isValid()) {
            this.panelDevice.getProperties(iPanelCallback);
        } else if (iPanelCallback != null) {
            iPanelCallback.onComplete(false, "init is wrong");
        }
    }

    private boolean invokeServiceRequestIsValid(InvokeServiceRequest invokeServiceRequest) {
        return (invokeServiceRequest == null || invokeServiceRequest.getIotId() == null || "".equals(invokeServiceRequest.getIotId()) || invokeServiceRequest.getIdentifier() == null || "".equals(invokeServiceRequest.getIdentifier())) ? false : true;
    }

    public void invokeService(InvokeServiceRequest invokeServiceRequest) {
        if (!invokeServiceRequestIsValid(invokeServiceRequest)) {
            IPanelCallback iPanelCallback = this.panelCallback;
            if (iPanelCallback != null) {
                iPanelCallback.onComplete(false, "request is invalid");
                return;
            }
            return;
        }
        if (isValid()) {
            this.panelDevice.invokeService(JSON.toJSONString(invokeServiceRequest), this.panelCallback);
        } else {
            IPanelCallback iPanelCallback2 = this.panelCallback;
            if (iPanelCallback2 != null) {
                iPanelCallback2.onComplete(false, "init is wrong");
            }
        }
    }

    public void invokeService(InvokeServiceRequest invokeServiceRequest, IPanelCallback iPanelCallback) {
        if (!invokeServiceRequestIsValid(invokeServiceRequest)) {
            if (iPanelCallback != null) {
                iPanelCallback.onComplete(false, "request is invalid");
            }
        } else if (isValid()) {
            this.panelDevice.invokeService(JSON.toJSONString(invokeServiceRequest), iPanelCallback);
        } else if (iPanelCallback != null) {
            iPanelCallback.onComplete(false, "init is wrong");
        }
    }

    public void startPTZ(int i, int i2) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_START_PTZ);
        HashMap map = new HashMap();
        map.put("ActionType", Integer.valueOf(i));
        map.put(RtspHeaders.Names.SPEED, Integer.valueOf(i2));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest);
    }

    public void startPTZ(int i, int i2, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_START_PTZ);
        HashMap map = new HashMap();
        map.put("ActionType", Integer.valueOf(i));
        map.put(RtspHeaders.Names.SPEED, Integer.valueOf(i2));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void stopPTZ() {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_STOP_PTZ);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest);
    }

    public void stopPTZ(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_STOP_PTZ);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void startPTZEx(int i, int i2, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_START_PTZ_EX);
        HashMap map = new HashMap();
        map.put("ActionType", Integer.valueOf(i));
        map.put("Step", Integer.valueOf(i2));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void reboot() {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_REBOOT);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest);
    }

    public void reboot(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_REBOOT);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void reset(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_DEVICE_DEFAULT);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void queryAPList(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_QUERY_AP_LIST);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void QueryRecordTimeList(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_QUERY_RECORD_DATE_LIST);
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void setQueryLocalEvent(String str, int i, int i2, int i3, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.QueryLocalEvent);
        HashMap map = new HashMap();
        map.put("StartTime", Integer.valueOf(i2));
        map.put("StopTime", Integer.valueOf(i3));
        map.put("EventType", Integer.valueOf(i));
        map.put("Message", str);
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void setFaceRecognitionServer(int i, String str, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.FaceRecognitionServer);
        HashMap map = new HashMap();
        map.put("Cmd", Integer.valueOf(i));
        map.put("Name", str);
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getNatModeConnectDeviceQuery(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.NatModeConnectDeviceQuery);
        HashMap map = new HashMap();
        map.put("Interface", Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getNatModeInterfaceInfoQuery(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.NatModeInterfaceInfoQuery);
        HashMap map = new HashMap();
        map.put("Interface", Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getLocationBasedService(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.LocationBasedService);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getGPSPositioningService(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.GPSPositioningService);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getFaceDataBasesStatus(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.FaceDataBasesStatus);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getFaceDataBasesQuery(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.FaceDataBasesQuery);
        HashMap map = new HashMap();
        map.put("FaceDataQueryIndex", 0);
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getNatAPConnectStatus(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.NatAPConnectStatus);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getEthernetConnectStatus(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.EthernetConnectStatus);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void setAPList(String str, String str2, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_SET_WIFI);
        HashMap map = new HashMap();
        map.put("Ssid", str);
        map.put("PassWord", str2);
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void queryCardRecordList(long j, long j2, int i, int i2) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_QUERY_RECORD_LIST);
        HashMap map = new HashMap();
        map.put("BeginTime", Long.valueOf(j));
        map.put("EndTime", Long.valueOf(j2));
        map.put("QuerySize", Integer.valueOf(i));
        map.put("Type", Integer.valueOf(i2));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest);
    }

    public void queryCardRecordList(long j, long j2, int i, int i2, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_QUERY_RECORD_LIST);
        HashMap map = new HashMap();
        map.put("BeginTime", Long.valueOf(j));
        map.put("EndTime", Long.valueOf(j2));
        map.put("QuerySize", Integer.valueOf(i));
        map.put("Type", Integer.valueOf(i2));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void formatStorageMedium() {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_FORMAT_STORAGE_MEDIUM);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest);
    }

    public void formatStorageMedium(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_FORMAT_STORAGE_MEDIUM);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void queryTFCard(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.QueryTFCard);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void capture(IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().capture(this.iotId, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void getEventRecordPlan2Dev(IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().getRecordPlan2Dev(this.iotId, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void addEventRecordPlan2Dev(String str, int i, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().addRecordPlan2Dev(this.iotId, str, i, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void updateEventRecordPlan2Dev(String str, int i, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().updateRecordPlan2Dev(this.iotId, str, i, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void deleteEventRecordPlan2Dev(int i, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().deleteRecordPlan2Dev(this.iotId, i, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void queryDevPictureFileList(long j, long j2, int i, int i2, int i3, int i4, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().queryDevPictureFileList(this.iotId, j, j2, i, i2, i3, i4, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void deleteDevPictureFile(String str, long j, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().deleteDevPictureFile(this.iotId, str, j, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void getDevPictureFileById(String str, int i, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().getDevPictureFileById(this.iotId, str, i, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void queryEventLst(long j, long j2, Integer num, int i, int i2, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().queryEventLst(this.iotId, j, j2, num, i, i2, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void queryVideoLst(int i, int i2, int i3, int i4, int i5, int i6, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().queryVideoLst(this.iotId, i, i2, i3, i4, i5, i6, ioTCallback);
    }

    @Override // sdk.ILinkVisionAPI2Dev
    public void queryMonthVideos(String str, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().queryMonthVideos(this.iotId, str, ioTCallback);
    }

    public void changeZoom(int i, float f, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_ZOOM_ACTION_CONTROL);
        HashMap map = new HashMap();
        map.put("ActionType", Integer.valueOf(i));
        map.put("E_Zoom", Float.valueOf(f));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void changeEZoom(int i, int i2, int i3, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.EZoomActionControl);
        HashMap map = new HashMap();
        map.put("ActionType", Integer.valueOf(i));
        map.put("Devid", Integer.valueOf(i2));
        map.put("Chnid", Integer.valueOf(i3));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getQueryNetModeStatus(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_QueryNetModeStatus);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getQueryFileList(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_QUERY_FILE_LIST);
        HashMap map = new HashMap();
        map.put("FileType", Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void changeFocus(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_FOCUS_ACTION_CONTROL);
        HashMap map = new HashMap();
        map.put("ActionType", Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void changePresetLocation(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_PRESET_LOCATION_CONTROL);
        HashMap map = new HashMap();
        map.put(RequestParameters.POSITION, Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void addPresetLocation(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_ADD_CONTROL);
        HashMap map = new HashMap();
        map.put(RequestParameters.POSITION, Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void deletePresetLocation(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier("PresetDeleteControl");
        HashMap map = new HashMap();
        map.put(RequestParameters.POSITION, Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void setWatchPos(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_WATCH_POS);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getVoiceList(String str, int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_DELETE_FILE);
        HashMap map = new HashMap();
        map.put("FileName", str);
        map.put("FileType", Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void getControllerList(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_GET_RF_KEY_LIST);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void deleteController(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_DELETE_RF_KEY);
        HashMap map = new HashMap();
        map.put("KeyNumber", Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void AddController(int i, int i2, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_ADD_RF_KEY);
        HashMap map = new HashMap();
        map.put("KeyNumber", Integer.valueOf(i));
        map.put("AlarmType", Integer.valueOf(i2));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void RFActionControl(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_RF_ACTION_CONTROL);
        HashMap map = new HashMap();
        map.put("KeyNumber", Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void QueryRFKeyStatus(int i, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_QUERY_RF_KEY_STATUS);
        HashMap map = new HashMap();
        map.put("KeyNumber", Integer.valueOf(i));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void GetBatteryPercentage(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_GetBatteryPercentage);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void GetCuTemperature(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.IDENTIFIER_GetCuTemperature);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void PresetDeleteControl(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier("PresetDeleteControl");
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void setPTZPointControl(int i, int i2, IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.PTZPointControl);
        HashMap map = new HashMap();
        map.put("X", Integer.valueOf(i));
        map.put("Y", Integer.valueOf(i2));
        invokeServiceRequest.setArgs(map);
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void setRFAMCControl(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.RFAMCControl);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }

    public void setRFSEEDControl(IPanelCallback iPanelCallback) {
        InvokeServiceRequest invokeServiceRequest = new InvokeServiceRequest();
        invokeServiceRequest.setIotId(this.iotId);
        invokeServiceRequest.setIdentifier(TMPConstants.RFSEEDControl);
        invokeServiceRequest.setArgs(new HashMap());
        invokeService(invokeServiceRequest, iPanelCallback);
    }
}
