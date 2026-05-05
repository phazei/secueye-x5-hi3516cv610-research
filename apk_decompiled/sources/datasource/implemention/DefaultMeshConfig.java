package datasource.implemention;

import a.a.a.a.b.m.a;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.tg.mtop.genie.GenieMtopNetBusinessManager;
import com.alibaba.ailabs.tg.network.Callback;
import com.alibaba.ailabs.tg.network.NetworkLifecycle;
import com.alibaba.ailabs.tg.network.NetworkLifecycleOwner;
import com.alibaba.android.multiendinonebridge.Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import datasource.MeshConfig;
import datasource.MeshConfigCallback;
import datasource.bean.ConfigurationData;
import datasource.bean.CustomGroupControllModel;
import datasource.bean.DeleteDeviceRespDataExtends;
import datasource.bean.DeviceStatus;
import datasource.bean.IoTGatewayEvent;
import datasource.bean.IotDeviceInfo;
import datasource.bean.ProvisionInfo;
import datasource.bean.ProvisionInfo4Master;
import datasource.bean.ServerConfirmation;
import datasource.bean.Sigmesh;
import datasource.bean.WakeUpServiceContext;
import datasource.implemention.data.ConfigurationRespData;
import datasource.implemention.data.GetInfoByAuthInfoRespData;
import datasource.implemention.data.GetProvisionInfoRespData;
import datasource.implemention.data.IoTGatewayInvokeData;
import datasource.implemention.data.IoTWakeUpData;
import datasource.implemention.data.IotCustomGroupControllRespData;
import datasource.implemention.data.IotDeleteDeviceRespData;
import datasource.implemention.data.IotDeviceControlRespData;
import datasource.implemention.data.IotGetUserDeviceListRespData;
import datasource.implemention.data.IotProvisionCompleteRespData;
import datasource.implemention.data.IotReportDevicesStatusRespData;
import datasource.implemention.data.ProvisionAuthRespData;
import datasource.implemention.data.ProvisionConfirmRespData;
import datasource.implemention.data.ProvisionInfo4MasterRespData;
import datasource.implemention.service.IoTGetwayService;
import datasource.implemention.service.IoTOTAService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public class DefaultMeshConfig implements MeshConfig, NetworkLifecycleOwner {
    public static final String TAG = "tg_mesh_sdk_" + DefaultMeshConfig.class.getSimpleName();
    public MeshRequestService meshRequestService = (MeshRequestService) NetworkBusinessManager.getService(MeshRequestService.class);

    @Override // datasource.MeshConfig
    public void batchBindMeshDevices(String str, JSONArray jSONArray, final MeshConfigCallback<Object> meshConfigCallback) {
        this.meshRequestService.getBatchBindMeshDevices(str, jSONArray).bindTo(this).enqueue(new Callback<Object>() { // from class: datasource.implemention.DefaultMeshConfig.15
            public void onFailure(int i, String str2, String str3) {
                a.a(DefaultMeshConfig.TAG, "getInfoByAuthInfo fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str2, str3);
                }
            }

            public void onSuccess(int i, Object obj) {
                a.a(DefaultMeshConfig.TAG, "getInfoByAuthInfo success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(Integer.valueOf(i));
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void configuration(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, final MeshConfigCallback<ConfigurationData> meshConfigCallback) {
        a.a(TAG, "configuration called...");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str4);
        jSONObject.put("macAddress", (Object) str2);
        jSONObject.put("deviceKey", (Object) str5);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str6);
        this.meshRequestService.configuration(str, str3, jSONObject.toJSONString()).bindTo(this).enqueue(new Callback<ConfigurationRespData>() { // from class: datasource.implemention.DefaultMeshConfig.6
            public void onFailure(int i, String str7, String str8) {
                a.a(DefaultMeshConfig.TAG, "configuration fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str7, str8);
                }
            }

            public void onSuccess(int i, ConfigurationRespData configurationRespData) {
                a.a(DefaultMeshConfig.TAG, "configuration success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(configurationRespData != null ? configurationRespData.getModel() : null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void deleteDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, final MeshConfigCallback<DeleteDeviceRespDataExtends> meshConfigCallback) {
        a.a(TAG, "deleteDevice called...");
        this.meshRequestService.deleteDevice(str, str2, str3).bindTo(this).enqueue(new Callback<IotDeleteDeviceRespData>() { // from class: datasource.implemention.DefaultMeshConfig.8
            public void onFailure(int i, String str4, String str5) {
                a.a(DefaultMeshConfig.TAG, "deleteDevice fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str4, str5);
                }
            }

            public void onSuccess(int i, IotDeleteDeviceRespData iotDeleteDeviceRespData) {
                a.a(DefaultMeshConfig.TAG, "deleteDevice success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(iotDeleteDeviceRespData != null ? iotDeleteDeviceRespData.getExtensions() : null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void deviceControl(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, final MeshConfigCallback<List<Sigmesh>> meshConfigCallback) {
        a.a(TAG, "deviceControl called...");
        this.meshRequestService.deviceControl(str, str2, str3, str4).bindTo(this).enqueue(new Callback<IotDeviceControlRespData>() { // from class: datasource.implemention.DefaultMeshConfig.9
            public void onFailure(int i, String str5, String str6) {
                a.a(DefaultMeshConfig.TAG, "deviceControl fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str5, str6);
                }
            }

            public void onSuccess(int i, IotDeviceControlRespData iotDeviceControlRespData) {
                a.a(DefaultMeshConfig.TAG, "deviceControl success...");
                if (iotDeviceControlRespData != null) {
                    MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                    if (meshConfigCallback2 != null) {
                        meshConfigCallback2.onSuccess(iotDeviceControlRespData.getExtensions() != null ? iotDeviceControlRespData.getExtensions().getSigmesh() : null);
                        return;
                    }
                    return;
                }
                MeshConfigCallback meshConfigCallback3 = meshConfigCallback;
                if (meshConfigCallback3 != null) {
                    meshConfigCallback3.onSuccess(null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void getInfoByAuthInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, final MeshConfigCallback<String> meshConfigCallback) {
        a.a(TAG, "getInfoByAuthInfo called...");
        this.meshRequestService.getInfoByAuthInfo(str, str2, str3, str4).bindTo(this).enqueue(new Callback<GetInfoByAuthInfoRespData>() { // from class: datasource.implemention.DefaultMeshConfig.7
            public void onFailure(int i, String str5, String str6) {
                a.a(DefaultMeshConfig.TAG, "getInfoByAuthInfo fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str5, str6);
                }
            }

            public void onSuccess(int i, GetInfoByAuthInfoRespData getInfoByAuthInfoRespData) {
                a.a(DefaultMeshConfig.TAG, "getInfoByAuthInfo success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(getInfoByAuthInfoRespData != null ? getInfoByAuthInfoRespData.getModel() : null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void getIotDeviceList(@NonNull String str, final MeshConfigCallback<List<IotDeviceInfo>> meshConfigCallback) {
        a.a(TAG, "getIotDeviceList called...");
        this.meshRequestService.getIotDeviceList(str).bindTo(this).enqueue(new Callback<IotGetUserDeviceListRespData>() { // from class: datasource.implemention.DefaultMeshConfig.11
            public void onFailure(int i, String str2, String str3) {
                a.a(DefaultMeshConfig.TAG, "getIotDeviceList fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str2, str3);
                }
            }

            public void onSuccess(int i, IotGetUserDeviceListRespData iotGetUserDeviceListRespData) {
                a.a(DefaultMeshConfig.TAG, "getIotDeviceList success...");
                if (iotGetUserDeviceListRespData == null || iotGetUserDeviceListRespData.getModel() == null) {
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (IotDeviceInfo iotDeviceInfo : iotGetUserDeviceListRespData.getModel()) {
                    if ("SIGMESH".equalsIgnoreCase(iotDeviceInfo.getPlatform().getName())) {
                        arrayList.add(iotDeviceInfo);
                    }
                }
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(arrayList);
                }
            }
        });
    }

    public NetworkLifecycle getNetworkLifecycle() {
        return new NetworkLifecycle();
    }

    @Override // datasource.MeshConfig
    public void getProvisionInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, final MeshConfigCallback<ProvisionInfo> meshConfigCallback) {
        a.a(TAG, "getProvisionInfo called...");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str3);
        jSONObject.put("macAddress", (Object) str2);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str4);
        this.meshRequestService.getProvisionInfo(str, jSONObject.toJSONString()).bindTo(this).enqueue(new Callback<GetProvisionInfoRespData>() { // from class: datasource.implemention.DefaultMeshConfig.2
            public void onFailure(int i, String str5, String str6) {
                a.a(DefaultMeshConfig.TAG, "getProvisionInfo fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str5, str6);
                }
            }

            public void onSuccess(int i, GetProvisionInfoRespData getProvisionInfoRespData) {
                a.a(DefaultMeshConfig.TAG, "getProvisionInfo success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(getProvisionInfoRespData != null ? getProvisionInfoRespData.getModel() : null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void getProvisionInfo4Master(@NonNull String str, String str2, final MeshConfigCallback<ProvisionInfo4Master> meshConfigCallback) {
        a.a(TAG, "getProvisionInfo4Master called...");
        this.meshRequestService.getProvisionInfo4Master(str, str2).bindTo(this).enqueue(new Callback<ProvisionInfo4MasterRespData>() { // from class: datasource.implemention.DefaultMeshConfig.1
            public void onFailure(int i, String str3, String str4) {
                a.a(DefaultMeshConfig.TAG, "getProvisionInfo4Master fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str3, str4);
                }
            }

            public void onSuccess(int i, ProvisionInfo4MasterRespData provisionInfo4MasterRespData) {
                a.a(DefaultMeshConfig.TAG, "getProvisionInfo4Master success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(provisionInfo4MasterRespData != null ? provisionInfo4MasterRespData.getModel() : null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void groupControl(String str, String str2, String str3, final MeshConfigCallback<List<Sigmesh>> meshConfigCallback) {
        JSONObject object = JSONObject.parseObject(str3);
        a.a(TAG, "groupControl :" + object.toJSONString());
        String string = object.getString("familyId");
        String string2 = object.getString("devId");
        Object obj = object.get("params");
        if (obj == null) {
            return;
        }
        Object obj2 = ((Map) obj).get("powerstate");
        try {
        } catch (NumberFormatException e) {
            a.a(TAG, "groupControl:ex = " + e.getMessage());
        }
        if (obj2 == null) {
            a.a(TAG, "onoffObj == null");
            return;
        }
        int i = obj2 instanceof String ? Integer.parseInt((String) obj2) : obj2 instanceof Integer ? ((Integer) obj2).intValue() : -1;
        if (i == -1) {
            a.a(TAG, "groupControl:onOff == -1");
            return;
        }
        a.a(TAG, "groupControl: familyId=" + string + ";onOff=" + i + ";devId=" + string2);
        String str4 = "{\"method\":\"thing.attribute.set\",\"devId\":\"" + string2 + "\",\"msgId\":\"" + (new Date().getTime() + Utils.getRandomString(19)) + "\",\"params\":{\"powerstate\":" + i + "},\"version\":\"1.0\",\"extension\":{\"pushGenie\":\"false\"}}";
        a.a(TAG, "groupControl: otherParams=" + str4);
        this.meshRequestService.customGroupControl(Long.parseLong(string), Long.parseLong(str), str4).bindTo(this).enqueue(new Callback<IotCustomGroupControllRespData>() { // from class: datasource.implemention.DefaultMeshConfig.13
            public void onFailure(int i2, String str5, String str6) {
                a.a(DefaultMeshConfig.TAG, "getIotDeviceList fail... errorMessage:" + str6 + "；errorCode:" + str5);
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str5, str6);
                }
            }

            public void onSuccess(int i2, IotCustomGroupControllRespData iotCustomGroupControllRespData) {
                a.a(DefaultMeshConfig.TAG, "customGroupControl success:" + JSON.toJSONString(iotCustomGroupControllRespData.getModel()));
                if (iotCustomGroupControllRespData == null || iotCustomGroupControllRespData.getModel() == null || meshConfigCallback == null) {
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (CustomGroupControllModel customGroupControllModel : iotCustomGroupControllRespData.getModel()) {
                    if (customGroupControllModel != null && customGroupControllModel.getData() != null) {
                        arrayList.addAll(customGroupControllModel.getData().getSigmesh());
                    }
                }
                meshConfigCallback.onSuccess(arrayList);
            }
        });
    }

    @Override // datasource.MeshConfig
    public void provisionAuth(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, @NonNull String str7, final MeshConfigCallback<Boolean> meshConfigCallback) {
        a.a(TAG, "provisionAuth called...");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str6);
        jSONObject.put("macAddress", (Object) str2);
        jSONObject.put("confirmationKey", (Object) str3);
        jSONObject.put("deviceConfirmation", (Object) str5);
        jSONObject.put("deviceRandom", (Object) str4);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str7);
        this.meshRequestService.provisionAuth(str, jSONObject.toJSONString()).bindTo(this).enqueue(new Callback<ProvisionAuthRespData>() { // from class: datasource.implemention.DefaultMeshConfig.4
            public void onFailure(int i, String str8, String str9) {
                a.a(DefaultMeshConfig.TAG, "provisionAuth fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str8, str9);
                }
            }

            public void onSuccess(int i, ProvisionAuthRespData provisionAuthRespData) {
                a.a(DefaultMeshConfig.TAG, "provisionAuth success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(provisionAuthRespData != null ? provisionAuthRespData.getModel() : null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void provisionComplete(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, final MeshConfigCallback<ConfigurationData> meshConfigCallback) {
        a.a(TAG, "provisionComplete called...");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str4);
        jSONObject.put("macAddress", (Object) str2);
        jSONObject.put("deviceKey", (Object) str3);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str5);
        this.meshRequestService.provisionComplete(str, jSONObject.toJSONString()).bindTo(this).enqueue(new Callback<IotProvisionCompleteRespData>() { // from class: datasource.implemention.DefaultMeshConfig.5
            public void onFailure(int i, String str6, String str7) {
                a.a(DefaultMeshConfig.TAG, "provisionComplete fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str6, str7);
                }
            }

            public void onSuccess(int i, IotProvisionCompleteRespData iotProvisionCompleteRespData) {
                a.a(DefaultMeshConfig.TAG, "provisionComplete success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(iotProvisionCompleteRespData != null ? iotProvisionCompleteRespData.getModel() : null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void provisionConfirm(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, final MeshConfigCallback<ServerConfirmation> meshConfigCallback) {
        a.a(TAG, "provisionConfirm called...");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str5);
        jSONObject.put("macAddress", (Object) str3);
        jSONObject.put("confirmationKey", (Object) str4);
        jSONObject.put("provisionRandom", (Object) str2);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str6);
        this.meshRequestService.provisionConfirm(str, jSONObject.toJSONString()).bindTo(this).enqueue(new Callback<ProvisionConfirmRespData>() { // from class: datasource.implemention.DefaultMeshConfig.3
            public void onFailure(int i, String str7, String str8) {
                a.a(DefaultMeshConfig.TAG, "provisionConfirm fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str7, str8);
                }
            }

            public void onSuccess(int i, ProvisionConfirmRespData provisionConfirmRespData) {
                a.a(DefaultMeshConfig.TAG, "provisionConfirm success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(provisionConfirmRespData != null ? provisionConfirmRespData.getModel() : null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void reportDevicesStatus(@NonNull String str, @NonNull String str2, @NonNull List<DeviceStatus> list, final MeshConfigCallback<String> meshConfigCallback) {
        a.a(TAG, "reportDevicesStatus called...");
        this.meshRequestService.reportDevicesStatus(str, str2, JSON.toJSONString(list)).bindTo(this).enqueue(new Callback<IotReportDevicesStatusRespData>() { // from class: datasource.implemention.DefaultMeshConfig.10
            public void onFailure(int i, String str3, String str4) {
                a.a(DefaultMeshConfig.TAG, "reportDevicesStatus fail...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onFailure(str3, str4);
                }
            }

            public void onSuccess(int i, IotReportDevicesStatusRespData iotReportDevicesStatusRespData) {
                a.a(DefaultMeshConfig.TAG, "reportDevicesStatus success...");
                MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                if (meshConfigCallback2 != null) {
                    meshConfigCallback2.onSuccess(iotReportDevicesStatusRespData != null ? iotReportDevicesStatusRespData.getModel() : null);
                }
            }
        });
    }

    @Override // datasource.MeshConfig
    public void triggerGatewayEventAccs(String str, IoTGatewayEvent ioTGatewayEvent) {
        String jSONString = JSON.toJSONString(ioTGatewayEvent);
        a.a(TAG, "triggerGatewayEventAccs:" + jSONString);
        ((IoTGetwayService) GenieMtopNetBusinessManager.getService(IoTGetwayService.class)).invokeEventMethod(str, jSONString).bindTo(this).enqueue(new Callback<IoTGatewayInvokeData>() { // from class: datasource.implemention.DefaultMeshConfig.12
            public void onFailure(int i, String str2, String str3) {
                a.a(DefaultMeshConfig.TAG, "IoTGatewayService failure:" + i + ";s:" + str2 + ";s1:" + str3);
            }

            public void onSuccess(int i, IoTGatewayInvokeData ioTGatewayInvokeData) {
                a.a(DefaultMeshConfig.TAG, "IoTGatewayService success code:" + i + ";msg:" + ioTGatewayInvokeData.getModel());
            }
        });
    }

    @Override // datasource.MeshConfig
    public void wakeUpDevice(String str, WakeUpServiceContext wakeUpServiceContext, final MeshConfigCallback<List<Sigmesh>> meshConfigCallback) {
        String jSONString = JSON.toJSONString(wakeUpServiceContext);
        a.a(TAG, "wakeUpDevice:" + jSONString + ";devId:" + str);
        ((IoTOTAService) GenieMtopNetBusinessManager.getService(IoTOTAService.class)).wakeupDevice(str, wakeUpServiceContext).bindTo(this).enqueue(new Callback<IoTWakeUpData>() { // from class: datasource.implemention.DefaultMeshConfig.14
            public void onFailure(int i, String str2, String str3) {
                meshConfigCallback.onFailure(str2, str3);
            }

            public void onSuccess(int i, IoTWakeUpData ioTWakeUpData) {
                if (ioTWakeUpData != null) {
                    MeshConfigCallback meshConfigCallback2 = meshConfigCallback;
                    if (meshConfigCallback2 != null) {
                        meshConfigCallback2.onSuccess(ioTWakeUpData.getModel() != null ? ioTWakeUpData.getModel().getSigmesh() : null);
                        return;
                    }
                    return;
                }
                MeshConfigCallback meshConfigCallback3 = meshConfigCallback;
                if (meshConfigCallback3 != null) {
                    meshConfigCallback3.onSuccess(null);
                }
            }
        });
    }
}
