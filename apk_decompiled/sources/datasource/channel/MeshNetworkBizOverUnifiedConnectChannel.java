package datasource.channel;

import a.a.a.a.b.m.a;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.DeviceCommonConstants;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;
import com.aliyun.alink.linksdk.connectsdk.ApiHelper;
import com.aliyun.alink.linksdk.tmp.utils.ErrorCode;
import datasource.MeshConfig;
import datasource.MeshConfigCallback;
import datasource.bean.ConfigurationData;
import datasource.bean.DeleteDeviceRespDataExtends;
import datasource.bean.DeviceStatus;
import datasource.bean.IoTGatewayEvent;
import datasource.bean.IotDeviceInfo;
import datasource.bean.ProvisionInfo;
import datasource.bean.ProvisionInfo4Master;
import datasource.bean.ServerConfirmation;
import datasource.bean.Sigmesh;
import datasource.bean.WakeUpServiceContext;
import datasource.channel.data.FeiyanConfigurationData;
import datasource.channel.data.FeiyanControlData;
import datasource.channel.data.FeiyanGroupControlData;
import datasource.channel.data.FeiyanProvisionInfo;
import datasource.channel.data.FeiyanProvisionInfo4Master;
import datasource.channel.data.FeiyanRegisterEndpointData;
import datasource.channel.data.FeiyanServerConfirmation;
import datasource.channel.reqeust.DeviceControlRequest;
import datasource.channel.reqeust.GetProvisionInfo4MasterRequest;
import datasource.channel.reqeust.GetProvisionInfoRequest;
import datasource.channel.reqeust.GroupControlRequest;
import datasource.channel.reqeust.ProvisionAuthRequest;
import datasource.channel.reqeust.ProvisionCompleteRequest;
import datasource.channel.reqeust.ProvisionConfirmRequest;
import datasource.channel.reqeust.RegisterEndpointRequest;
import datasource.channel.reqeust.ReportDevicesStatusRequest;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class MeshNetworkBizOverUnifiedConnectChannel implements MeshConfig {
    public String TAG = "MeshNetworkBizOverUnifiedConnectChannel";

    public static void notifyFailed(MeshConfigCallback meshConfigCallback, int i, String str) {
        if (meshConfigCallback != null) {
            meshConfigCallback.onFailure(String.valueOf(i), str);
        }
    }

    public static <T> void notifySuccess(MeshConfigCallback<T> meshConfigCallback, T t) {
        if (meshConfigCallback != null) {
            meshConfigCallback.onSuccess(t);
        }
    }

    @Override // datasource.MeshConfig
    public void batchBindMeshDevices(String str, JSONArray jSONArray, MeshConfigCallback<Object> meshConfigCallback) {
    }

    @Override // datasource.MeshConfig
    public void configuration(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, MeshConfigCallback<ConfigurationData> meshConfigCallback) {
        meshConfigCallback.onFailure("-1", ErrorCode.ERROR_MSG_NOTSUPPORT);
    }

    @Override // datasource.MeshConfig
    public void deleteDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, MeshConfigCallback<DeleteDeviceRespDataExtends> meshConfigCallback) {
        meshConfigCallback.onFailure("-1", ErrorCode.ERROR_MSG_NOTSUPPORT);
    }

    @Override // datasource.MeshConfig
    public void deviceControl(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, final MeshConfigCallback<List<Sigmesh>> meshConfigCallback) {
        a.a("Mesh", "deviceControl params:" + str4);
        DeviceControlRequest deviceControlRequest = new DeviceControlRequest();
        deviceControlRequest.setDeviceId(str2);
        deviceControlRequest.setParams(str4);
        ApiHelper.getInstance().send(deviceControlRequest, new ApiCallBack<FeiyanControlData>() { // from class: datasource.channel.MeshNetworkBizOverUnifiedConnectChannel.6
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str5) {
                MeshNetworkBizOverUnifiedConnectChannel.notifyFailed(meshConfigCallback, i, str5);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(FeiyanControlData feiyanControlData) {
                if (feiyanControlData != null) {
                    MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, feiyanControlData.getSigmesh());
                } else {
                    MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void getInfoByAuthInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, MeshConfigCallback<String> meshConfigCallback) {
        meshConfigCallback.onFailure("-1", ErrorCode.ERROR_MSG_NOTSUPPORT);
    }

    @Override // datasource.MeshConfig
    public void getIotDeviceList(@NonNull String str, MeshConfigCallback<List<IotDeviceInfo>> meshConfigCallback) {
    }

    @Override // datasource.MeshConfig
    public void getProvisionInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, final MeshConfigCallback<ProvisionInfo> meshConfigCallback) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str3);
        jSONObject.put("macAddress", (Object) str2);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str4);
        String jSONString = jSONObject.toJSONString();
        GetProvisionInfoRequest getProvisionInfoRequest = new GetProvisionInfoRequest();
        getProvisionInfoRequest.setProvisionBaseReq(jSONString);
        ApiHelper.getInstance().send(getProvisionInfoRequest, new ApiCallBack<FeiyanProvisionInfo>() { // from class: datasource.channel.MeshNetworkBizOverUnifiedConnectChannel.2
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str5) {
                MeshNetworkBizOverUnifiedConnectChannel.notifyFailed(meshConfigCallback, i, str5);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(FeiyanProvisionInfo feiyanProvisionInfo) {
                if (feiyanProvisionInfo == null) {
                    MeshNetworkBizOverUnifiedConnectChannel.notifyFailed(meshConfigCallback, MeshUtConst$MeshErrorEnum.NULL_PROVISION_INFO_ERROR.getErrorCode(), MeshUtConst$MeshErrorEnum.NULL_PROVISION_INFO_ERROR.getErrorMsg());
                } else {
                    MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, feiyanProvisionInfo.convert2ProvisionInfo());
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void getProvisionInfo4Master(@NonNull String str, String str2, final MeshConfigCallback<ProvisionInfo4Master> meshConfigCallback) {
        GetProvisionInfo4MasterRequest getProvisionInfo4MasterRequest = new GetProvisionInfo4MasterRequest();
        getProvisionInfo4MasterRequest.setUuid(str2);
        a.a("Mesh", getProvisionInfo4MasterRequest.toString());
        ApiHelper.getInstance().send(getProvisionInfo4MasterRequest, new ApiCallBack<FeiyanProvisionInfo4Master>() { // from class: datasource.channel.MeshNetworkBizOverUnifiedConnectChannel.1
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str3) {
                MeshNetworkBizOverUnifiedConnectChannel.notifyFailed(meshConfigCallback, i, str3);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(FeiyanProvisionInfo4Master feiyanProvisionInfo4Master) {
                MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, feiyanProvisionInfo4Master.convert2CommonObject());
            }
        });
    }

    @Override // datasource.MeshConfig
    public void groupControl(String str, String str2, String str3, final MeshConfigCallback<List<Sigmesh>> meshConfigCallback) {
        a.a("Mesh", "Group control params:" + str3);
        GroupControlRequest groupControlRequest = new GroupControlRequest();
        groupControlRequest.setControlGroupId(str);
        groupControlRequest.setDeviceId(JSON.parseObject(str3).getString("iotId"));
        groupControlRequest.setParams(str3);
        ApiHelper.getInstance().send(groupControlRequest, new ApiCallBack<FeiyanGroupControlData>() { // from class: datasource.channel.MeshNetworkBizOverUnifiedConnectChannel.9
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str4) {
                MeshNetworkBizOverUnifiedConnectChannel.notifyFailed(meshConfigCallback, i, str4);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(FeiyanGroupControlData feiyanGroupControlData) {
                if (feiyanGroupControlData != null) {
                    MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, feiyanGroupControlData.getSigmesh());
                } else {
                    MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void provisionAuth(String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, @NonNull String str7, final MeshConfigCallback<Boolean> meshConfigCallback) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str6);
        jSONObject.put("macAddress", (Object) str2);
        jSONObject.put("confirmationKey", (Object) str3);
        jSONObject.put("deviceConfirmation", (Object) str5);
        jSONObject.put("deviceRandom", (Object) str4);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str7);
        String jSONString = jSONObject.toJSONString();
        ProvisionAuthRequest provisionAuthRequest = new ProvisionAuthRequest();
        provisionAuthRequest.setProvisionAuthorizationReq(jSONString);
        ApiHelper.getInstance().send(provisionAuthRequest, new ApiCallBack() { // from class: datasource.channel.MeshNetworkBizOverUnifiedConnectChannel.4
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str8) {
                MeshNetworkBizOverUnifiedConnectChannel.notifyFailed(meshConfigCallback, i, str8);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(Object obj) {
                if (obj instanceof Boolean) {
                    MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, (Boolean) obj);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void provisionComplete(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, final MeshConfigCallback<ConfigurationData> meshConfigCallback) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str4);
        jSONObject.put("macAddress", (Object) str2);
        jSONObject.put("deviceKey", (Object) str3);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str5);
        String jSONString = jSONObject.toJSONString();
        ProvisionCompleteRequest provisionCompleteRequest = new ProvisionCompleteRequest();
        provisionCompleteRequest.setProvisionCompleteReq(jSONString);
        ApiHelper.getInstance().send(provisionCompleteRequest, new ApiCallBack<FeiyanConfigurationData>() { // from class: datasource.channel.MeshNetworkBizOverUnifiedConnectChannel.5
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str6) {
                MeshNetworkBizOverUnifiedConnectChannel.notifyFailed(meshConfigCallback, i, str6);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(FeiyanConfigurationData feiyanConfigurationData) {
                MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, feiyanConfigurationData.convert2ConfigurationData());
            }
        });
    }

    @Override // datasource.MeshConfig
    public void provisionConfirm(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, final MeshConfigCallback<ServerConfirmation> meshConfigCallback) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str5);
        jSONObject.put("macAddress", (Object) str3);
        jSONObject.put("confirmationKey", (Object) str4);
        jSONObject.put("provisionRandom", (Object) str2);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str6);
        String jSONString = jSONObject.toJSONString();
        ProvisionConfirmRequest provisionConfirmRequest = new ProvisionConfirmRequest();
        provisionConfirmRequest.setProvisionConfirmationReq(jSONString);
        ApiHelper.getInstance().send(provisionConfirmRequest, new ApiCallBack<FeiyanServerConfirmation>() { // from class: datasource.channel.MeshNetworkBizOverUnifiedConnectChannel.3
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str7) {
                MeshNetworkBizOverUnifiedConnectChannel.notifyFailed(meshConfigCallback, i, str7);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(FeiyanServerConfirmation feiyanServerConfirmation) {
                MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, feiyanServerConfirmation.convert2ServerConfirmation());
            }
        });
    }

    @Override // datasource.MeshConfig
    public void reportDevicesStatus(@NonNull String str, @NonNull String str2, @NonNull List<DeviceStatus> list, final MeshConfigCallback<String> meshConfigCallback) {
        ReportDevicesStatusRequest reportDevicesStatusRequest = new ReportDevicesStatusRequest();
        reportDevicesStatusRequest.setDevicesStatusList(JSON.toJSONString(list));
        ApiHelper.getInstance().send(reportDevicesStatusRequest, new ApiCallBack() { // from class: datasource.channel.MeshNetworkBizOverUnifiedConnectChannel.7
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str3) {
                MeshNetworkBizOverUnifiedConnectChannel.notifyFailed(meshConfigCallback, i, str3);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(Object obj) {
                if (obj != null) {
                    MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, obj.toString());
                } else {
                    MeshNetworkBizOverUnifiedConnectChannel.notifySuccess(meshConfigCallback, null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void triggerGatewayEventAccs(String str, IoTGatewayEvent ioTGatewayEvent) {
        RegisterEndpointRequest registerEndpointRequest = new RegisterEndpointRequest();
        new RegisterEndpointRequest.RegisterRequest();
        RegisterEndpointRequest.RegisterRequest registerRequest = (RegisterEndpointRequest.RegisterRequest) ioTGatewayEvent.getEvent().getPayload().toJavaObject(RegisterEndpointRequest.RegisterRequest.class);
        String string = ioTGatewayEvent.getEvent().getPayload().getString(DeviceCommonConstants.KEY_DEVICE_ID);
        if (string.contains("&")) {
            int iIndexOf = string.indexOf("&");
            String strSubstring = string.substring(0, iIndexOf);
            registerRequest.setProductKey(string.substring(iIndexOf + 1));
            registerRequest.setUuid(strSubstring);
        } else {
            registerRequest.setUuid(string);
        }
        registerEndpointRequest.setRequest(registerRequest);
        a.a(this.TAG, "triggerGatewayEventAccs request:" + JSON.toJSONString(registerEndpointRequest));
        ApiHelper.getInstance().send(registerEndpointRequest, new ApiCallBack<FeiyanRegisterEndpointData>() { // from class: datasource.channel.MeshNetworkBizOverUnifiedConnectChannel.8
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str2) {
                a.a(MeshNetworkBizOverUnifiedConnectChannel.this.TAG, "triggerGatewayEventAccs onFail:" + i + ";msg:" + str2);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(FeiyanRegisterEndpointData feiyanRegisterEndpointData) {
                a.a(MeshNetworkBizOverUnifiedConnectChannel.this.TAG, "triggerGatewayEventAccs success:" + feiyanRegisterEndpointData);
            }
        });
    }

    @Override // datasource.MeshConfig
    public void wakeUpDevice(String str, WakeUpServiceContext wakeUpServiceContext, MeshConfigCallback<List<Sigmesh>> meshConfigCallback) {
        meshConfigCallback.onFailure("-1", ErrorCode.ERROR_MSG_NOTSUPPORT);
    }
}
