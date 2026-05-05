package com.aliyun.alink.business.devicecenter.biz;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.ailabs.tg.basebiz.user.UserManager;
import com.alibaba.ailabs.tg.mtop.OnResponseListener;
import com.alibaba.ailabs.tg.network.mtop.inner.MtopHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.add.BatchDiscoveryParams;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.model.CheckBindTokenMtopResponse;
import com.aliyun.alink.business.devicecenter.biz.model.CheckBindTokenRequest;
import com.aliyun.alink.business.devicecenter.biz.model.GetBindTokenMtopResponse;
import com.aliyun.alink.business.devicecenter.biz.model.GetBindTokenRequest;
import com.aliyun.alink.business.devicecenter.channel.http.DCError;
import com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback;
import com.aliyun.alink.business.devicecenter.channel.http.RetryTransitoryClient;
import com.aliyun.alink.business.devicecenter.channel.http.TransitoryClient;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.model.CheckTokenModel;
import com.aliyun.alink.business.devicecenter.model.FilterData;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes.dex */
public class ProvisionRepository {
    public static void a(String str, int i, int i2, Boolean bool, List list, final IoTCallback ioTCallback) {
        TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.2").setPath(AlinkConstants.HTTP_PATH_GET_DISCOVERED_MESH_DEVICE).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam("gatewayIotId", str).addParam(AlinkConstants.KEY_PAGE_NO, i).addParam(AlinkConstants.KEY_PAGE_SIZE, i2).addParam(AlinkConstants.KEY_APP_REPORT_DEVICE, list).addParam(AlinkConstants.KEY_SCAN_FIRST_QUERY, bool).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.10
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onFailure(ioTRequest, exc);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onResponse(ioTRequest, ioTResponse);
                }
            }
        });
    }

    public static RetryTransitoryClient batchMeshDeviceProvisionTrigger(JSONArray jSONArray, String str, final IoTCallback ioTCallback) {
        if (ioTCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("ProvisionRepository", "meshDeviceProvisionTrigger apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return null;
        }
        Log.d("ProvisionRepository", "batchMeshDeviceProvisionTrigger: " + JSONObject.toJSONString(jSONArray));
        return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.0").setPath("/living/awss/bt/mesh/gateway/provision/devices/trigger").setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam(AlinkConstants.KEY_GATEWAY_BATCH, (List) jSONArray).addParam("gatewayIotId", str).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.12
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onFailure(ioTRequest, exc);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onResponse(ioTRequest, ioTResponse);
                }
            }
        });
    }

    public static void checkCloudProvisionTokenForMtop(String str, String str2, String str3, final IRequestCallback<CheckBindTokenMtopResponse> iRequestCallback) {
        if (iRequestCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (TextUtils.isEmpty(str3)) {
            iRequestCallback.onFail(new DCError(String.valueOf(DCErrorCode.PF_PARAMS_ERROR), "token cannot be null"), null);
            return;
        }
        if (!DCEnvHelper.isTgEnv()) {
            ALog.w("ProvisionRepository", "iLopTokenCheck mtop not exist, return.");
            iRequestCallback.onFail(new DCError(String.valueOf(DCErrorCode.PF_PARAMS_ERROR), "mtop or tg scan not exist"), null);
            return;
        }
        CheckBindTokenRequest checkBindTokenRequest = new CheckBindTokenRequest();
        checkBindTokenRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
        checkBindTokenRequest.setProductKey(str);
        checkBindTokenRequest.setDeviceName(str2);
        checkBindTokenRequest.setToken(str3);
        MtopHelper.getInstance().asyncRequestApi(checkBindTokenRequest, CheckBindTokenMtopResponse.class, new OnResponseListener() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.7
            public void onResponseFailed(int i, String str4, String str5) {
                ALog.d("ProvisionRepository", "onResponseFailed() called with: i = [" + i + "], msgCode = [" + str4 + "], msgInfo = [" + str5 + "]");
                if (iRequestCallback != null) {
                    iRequestCallback.onFail(new DCError(str4, str5), null);
                }
            }

            public void onResponseSuccess(BaseOutDo baseOutDo, int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("onResponseSuccess() called with: baseOutDo = [");
                sb.append(baseOutDo);
                sb.append("], i = [");
                sb.append(i);
                sb.append("], type=");
                boolean z = baseOutDo instanceof CheckBindTokenMtopResponse;
                sb.append(z);
                sb.append(" callback=");
                sb.append(iRequestCallback);
                sb.append(", hash=");
                sb.append(hashCode());
                ALog.d("ProvisionRepository", sb.toString());
                IRequestCallback iRequestCallback2 = iRequestCallback;
                if (iRequestCallback2 == null || !z) {
                    ALog.d("ProvisionRepository", "onResponseSuccess error");
                } else {
                    iRequestCallback2.onSuccess((CheckBindTokenMtopResponse) baseOutDo);
                }
            }
        }, 0);
    }

    public static void checkToken(String str, String str2, String str3, IoTCallback ioTCallback) {
        ALog.d("ProvisionRepository", "checkToken() called with: pk = [" + str + "], dn = [" + str2 + "], token = [" + str3 + "], callback = [" + ioTCallback + "]");
        if (ioTCallback == null) {
            ALog.w("ProvisionRepository", "checkToken callback=null, return.");
            return;
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("ProvisionRepository", "checkToken apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return;
        }
        if (TextUtils.isEmpty(str3)) {
            ALog.w("ProvisionRepository", "checkToken token invalid, return");
            ioTCallback.onFailure(null, new Exception("params invalid, token invalid."));
            return;
        }
        try {
            HashMap map = new HashMap();
            map.put("productKey", str);
            map.put("deviceName", str2);
            map.put("token", str3);
            TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion(AlinkConstants.HTTP_PATH_CHECK_TOKEN_VERSION).setPath(AlinkConstants.HTTP_PATH_CHECK_TOKEN).setAuthType(AlinkConstants.KEY_IOT_AUTH).setScheme(Scheme.HTTPS).setParams(map).build(), ioTCallback);
        } catch (Exception unused) {
        }
    }

    public static void checkTokens(List<CheckTokenModel> list, IoTCallback ioTCallback) {
        ALog.d("ProvisionRepository", "checkToken() called with: checkTokenModelList = [" + list + "], callback = [" + ioTCallback + "]");
        if (ioTCallback == null) {
            ALog.w("ProvisionRepository", "checkToken callback=null, return.");
            return;
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("ProvisionRepository", "checkToken apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return;
        }
        if (list == null || list.isEmpty()) {
            ALog.w("ProvisionRepository", "checkTokenModelList invalid, return");
            ioTCallback.onFailure(null, new Exception("checkTokenModelList invalid."));
            return;
        }
        try {
            HashMap map = new HashMap();
            ArrayList arrayList = new ArrayList();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                CheckTokenModel checkTokenModel = list.get(i);
                if (checkTokenModel != null) {
                    HashMap map2 = new HashMap();
                    map2.put("productKey", checkTokenModel.productKey);
                    map2.put("deviceName", checkTokenModel.deviceName);
                    map2.put("token", checkTokenModel.bindToken);
                    arrayList.add(map2);
                }
            }
            map.put("tokenCheckList", arrayList);
            TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.0").setPath(AlinkConstants.HTTP_PATH_CHECK_TOKENS).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), ioTCallback);
        } catch (Exception unused) {
        }
    }

    public static RetryTransitoryClient closeBatchMeshProvision(JSONArray jSONArray) {
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("ProvisionRepository", "getBatchMeshProvisionResult apiclient not exist, return.");
            return null;
        }
        Log.d("ProvisionRepository", "getBatchMeshProvisionResult() called with: taskIds = [" + jSONArray.toJSONString() + "]");
        return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.0").setPath(AlinkConstants.HTTP_PATH_GET_BATCH_MESH_PROVISION_CLOSE).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam(AlinkConstants.KEY_TASK_ID_LIST, (List) jSONArray).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.15
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            }
        });
    }

    public static void cloudToFilterDevice(List<FilterData> list, final IoTCallback ioTCallback) {
        JSONArray jSONArray = new JSONArray();
        try {
            jSONArray = JSONObject.parseArray(JSON.toJSONString(list));
        } catch (Throwable th) {
            ALog.e("ProvisionRepository", "filter error:" + th.getMessage());
        }
        HashMap map = new HashMap();
        map.put("iotDevices", jSONArray);
        IoTRequestBuilder authType = new IoTRequestBuilder().setApiVersion(AlinkConstants.PROVISION_DEVICE_FILTER_VERSION).setPath(AlinkConstants.PROVISION_DEVICE_FILTER).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH);
        authType.setParams(map);
        new IoTAPIClientFactory().getClient().send(authType.build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.17
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onFailure(ioTRequest, exc);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onResponse(ioTRequest, ioTResponse);
                }
            }
        });
    }

    public static RetryTransitoryClient getBatchMeshProvisionResult(JSONArray jSONArray, final IoTCallback ioTCallback) {
        if (ioTCallback == null) {
            throw new IllegalArgumentException("getBatchMeshProvisionResult callback is null");
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("ProvisionRepository", "getBatchMeshProvisionResult apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return null;
        }
        Log.d("ProvisionRepository", "getBatchMeshProvisionResult() called with: taskIds = [" + jSONArray.toJSONString() + "], callback = [" + ioTCallback + "]");
        return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.0").setPath(AlinkConstants.HTTP_PATH_GET_BATCH_MESH_PROVISION_RESULT).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam(AlinkConstants.KEY_TASK_ID_LIST, (List) jSONArray).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.14
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onFailure(ioTRequest, exc);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onResponse(ioTRequest, ioTResponse);
                }
            }
        });
    }

    public static RetryTransitoryClient getCipher(String str, String str2, String str3, String str4, final IoTCallback ioTCallback) {
        ALog.d("ProvisionRepository", "getCipher() called with: pk = [" + str + "], dn = [" + str2 + "], random = [" + str3 + "], callback = [" + ioTCallback + "]");
        if (ioTCallback == null) {
            ALog.w("ProvisionRepository", "getCipher callback=null, return.");
            return null;
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("ProvisionRepository", "getCipher apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return null;
        }
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str3)) {
            ALog.w("ProvisionRepository", "getCipher pk or random invalid, return");
            ioTCallback.onFailure(null, new Exception("params invalid, pk or random invalid."));
            return null;
        }
        DCUserTrack.addTrackData(AlinkConstants.KEY_START_TIME_GET_CIPHER, String.valueOf(System.currentTimeMillis()));
        PerformanceLog.trace("ProvisionRepository", "getCipher");
        HashMap map = new HashMap();
        map.put("productKey", str);
        map.put("deviceName", str2);
        map.put(AlinkConstants.KEY_PRODUCT_ID, null);
        map.put(AlinkConstants.KEY_ENCRYPTED, str4);
        map.put(AlinkConstants.KEY_RANDOM, str3);
        map.put("params", new HashMap());
        return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion(AlinkConstants.HTTP_PATH_GET_CIPHER_VERSION).setPath(AlinkConstants.HTTP_PATH_GET_CIPHER).setScheme(Scheme.HTTPS).addParam("deviceInfoForCipher", (Map) map).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.1
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_GET_CIPHER, String.valueOf(System.currentTimeMillis()));
                DCUserTrack.addTrackData(AlinkConstants.KEY_GET_CIPHER_RESULT, "0");
                PerformanceLog.trace("ProvisionRepository", AlinkConstants.KEY_GET_CIPHER_RESULT, PerformanceLog.getJsonObject("result", "fail"));
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onFailure(ioTRequest, exc);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_GET_CIPHER, String.valueOf(System.currentTimeMillis()));
                if (ioTResponse == null || ioTResponse.getCode() != 200) {
                    DCUserTrack.addTrackData(AlinkConstants.KEY_GET_CIPHER_RESULT, "0");
                    PerformanceLog.trace("ProvisionRepository", AlinkConstants.KEY_GET_CIPHER_RESULT, PerformanceLog.getJsonObject("result", "fail", "alinkid", TransitoryClient.getInstance().getTraceId(ioTResponse)));
                } else {
                    DCUserTrack.addTrackData(AlinkConstants.KEY_GET_CIPHER_RESULT, "1");
                    PerformanceLog.trace("ProvisionRepository", AlinkConstants.KEY_GET_CIPHER_RESULT, PerformanceLog.getJsonObject("result", "success", "alinkid", TransitoryClient.getInstance().getTraceId(ioTResponse)));
                }
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onResponse(ioTRequest, ioTResponse);
                }
            }
        });
    }

    public static RetryTransitoryClient getCloudProvisionToken(String str, final IoTCallback ioTCallback) {
        if (ioTCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (DCEnvHelper.hasApiClient()) {
            return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.0").setPath(AlinkConstants.HTTP_PATH_ILOP_TOKEN_REQUEST).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam("bssid", str).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.4
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onFailure(ioTRequest, exc);
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onResponse(ioTRequest, ioTResponse);
                    }
                }
            });
        }
        ALog.w("ProvisionRepository", "getCloudProvisionToken apiclient not exist, return.");
        ioTCallback.onFailure(null, new Exception("apiclient not exist"));
        return null;
    }

    public static void getCloudProvisionTokenForMtop(String str, final IRequestCallback<GetBindTokenMtopResponse> iRequestCallback) {
        if (iRequestCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (!DCEnvHelper.isTgEnv()) {
            ALog.w("ProvisionRepository", "getCloudProvisionToken apiclient & mtop not exist, return.");
            iRequestCallback.onFail(new DCError(String.valueOf(DCErrorCode.PF_USER_INVOKE_ERROR), "apiclient & mtop not exist"), null);
            return;
        }
        GetBindTokenRequest getBindTokenRequest = new GetBindTokenRequest();
        getBindTokenRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
        getBindTokenRequest.setBssid(str);
        ALog.d("ProvisionRepository", "getCloudProvisionTokenForMtop callback=" + iRequestCallback + ", hashCode=" + iRequestCallback.hashCode());
        MtopHelper.getInstance().asyncRequestApi(getBindTokenRequest, GetBindTokenMtopResponse.class, new OnResponseListener() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.5
            public void onResponseFailed(int i, String str2, String str3) {
                ALog.d("ProvisionRepository", "onResponseFailed() called with: i = [" + i + "], msgCode = [" + str2 + "], msgInfo = [" + str3 + "], hashCode=" + iRequestCallback.hashCode());
                IRequestCallback iRequestCallback2 = iRequestCallback;
                if (iRequestCallback2 != null) {
                    iRequestCallback2.onFail(new DCError(str2, str3), null);
                }
            }

            public void onResponseSuccess(BaseOutDo baseOutDo, int i) {
                ALog.d("ProvisionRepository", "onResponseSuccess() called with: baseOutDo = [" + baseOutDo + "], i = [" + i + "], hashCode=" + iRequestCallback.hashCode());
                IRequestCallback iRequestCallback2 = iRequestCallback;
                if (iRequestCallback2 == null || !(baseOutDo instanceof GetBindTokenMtopResponse)) {
                    return;
                }
                iRequestCallback2.onSuccess((GetBindTokenMtopResponse) baseOutDo);
            }
        }, 0);
    }

    public static void getDiscoveredMeshDevice(final String str, final int i, final int i2, final Boolean bool, final List list, final JSONArray jSONArray, final MeshDiscoverCallback meshDiscoverCallback) {
        Log.d("ProvisionRepository", "getDiscoveredMeshDevice() called with: iotId = [" + str + "], pageNo = [" + i + "], pageSize = [" + i2 + "], isScanStartedFirstQuery = [" + bool + "], appReportDevice = [" + list + "], result = [" + jSONArray + "], callback = [" + meshDiscoverCallback + "]");
        if (meshDiscoverCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (DCEnvHelper.hasApiClient()) {
            a(str, i, i2, bool, list, new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.9
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    if (jSONArray.size() > 0) {
                        meshDiscoverCallback.onSuccess(jSONArray);
                    } else {
                        meshDiscoverCallback.onFailure(exc.toString());
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    if (ioTResponse == null || ioTResponse.getCode() != 200 || ioTResponse.getData() == null) {
                        if (jSONArray.size() > 0) {
                            meshDiscoverCallback.onSuccess(jSONArray);
                            return;
                        } else {
                            meshDiscoverCallback.onFailure("");
                            return;
                        }
                    }
                    JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                    if (object == null) {
                        if (jSONArray.size() > 0) {
                            meshDiscoverCallback.onSuccess(jSONArray);
                            return;
                        } else {
                            meshDiscoverCallback.onFailure("");
                            return;
                        }
                    }
                    int intValue = object.getIntValue(AlinkConstants.KEY_TOTAL);
                    Log.d("ProvisionRepository", "onResponse: totalCount=" + intValue);
                    JSONArray jSONArray2 = object.getJSONArray(AlinkConstants.KEY_LIST);
                    jSONArray.addAll(jSONArray2);
                    if (jSONArray2 == null || jSONArray2.size() < i2) {
                        meshDiscoverCallback.onSuccess(jSONArray);
                    } else if (jSONArray2.size() != i2 || jSONArray2.size() >= intValue) {
                        meshDiscoverCallback.onSuccess(jSONArray);
                    } else {
                        Log.d("ProvisionRepository", "onResponse: 加载第二页");
                        ProvisionRepository.getDiscoveredMeshDevice(str, i + 1, i2, bool, list, jSONArray, meshDiscoverCallback);
                    }
                }
            });
        } else {
            ALog.w("ProvisionRepository", "getDiscoveredMeshDevice apiclient not exist, return.");
            meshDiscoverCallback.onFailure("apiclient not exist");
        }
    }

    public static RetryTransitoryClient getMeshProvisionResult(String str, final IoTCallback ioTCallback) {
        if (ioTCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (DCEnvHelper.hasApiClient()) {
            return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.0").setPath(AlinkConstants.HTTP_PATH_GET_MESH_PROVISION_RESULT).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam(AlinkConstants.KEY_TASK_ID, str).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.13
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onFailure(ioTRequest, exc);
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onResponse(ioTRequest, ioTResponse);
                    }
                }
            });
        }
        ALog.w("ProvisionRepository", "getMeshProvisionResult apiclient not exist, return.");
        ioTCallback.onFailure(null, new Exception("apiclient not exist"));
        return null;
    }

    public static RetryTransitoryClient iLopTokenCheck(String str, String str2, String str3, final IoTCallback ioTCallback) {
        if (ioTCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (TextUtils.isEmpty(str3)) {
            ioTCallback.onFailure(null, new IllegalArgumentException("token cannot be null"));
            return null;
        }
        if (DCEnvHelper.hasApiClient()) {
            return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.1").setPath(AlinkConstants.HTTP_PATH_ILOP_CHECK_BIND_TOKEN).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam("productKey", str).addParam("deviceName", str2).addParam("token", str3).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.6
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onFailure(ioTRequest, exc);
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onResponse(ioTRequest, ioTResponse);
                    }
                }
            });
        }
        ALog.w("ProvisionRepository", "iLopTokenCheck apiclient not exist, return.");
        ioTCallback.onFailure(null, new Exception("apiclient not exist"));
        return null;
    }

    public static RetryTransitoryClient meshDeviceDiscoveryTrigger(String str, boolean z, final IoTCallback ioTCallback) {
        if (ioTCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (DCEnvHelper.hasApiClient()) {
            return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.1").setPath(AlinkConstants.HTTP_PATH_TRIGGER_MESH_DISCOVER).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam("gatewayIotId", str).addParam(AlinkConstants.KEY_SEARCH_STOP_FLAG, Boolean.valueOf(z)).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.8
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onFailure(ioTRequest, exc);
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onResponse(ioTRequest, ioTResponse);
                    }
                }
            });
        }
        ALog.w("ProvisionRepository", "meshDeviceDiscoveryTrigger apiclient not exist, return.");
        ioTCallback.onFailure(null, new Exception("apiclient not exist"));
        return null;
    }

    public static RetryTransitoryClient meshDeviceProvisionTrigger(String str, String str2, final IoTCallback ioTCallback) {
        if (ioTCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (DCEnvHelper.hasApiClient()) {
            return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.0").setPath(AlinkConstants.HTTP_PATH_MESH_TRIGGER_PROVISION).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam("gatewayIotId", str).addParam("deviceId", str2).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.11
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onFailure(ioTRequest, exc);
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onResponse(ioTRequest, ioTResponse);
                    }
                }
            });
        }
        ALog.w("ProvisionRepository", "meshDeviceProvisionTrigger apiclient not exist, return.");
        ioTCallback.onFailure(null, new Exception("apiclient not exist"));
        return null;
    }

    public static void pidReturnToPk(String str, final IoTCallback ioTCallback) {
        try {
            HashMap map = new HashMap();
            map.put(AlinkConstants.KEY_PRODUCT_ID, str);
            IoTRequestBuilder authType = new IoTRequestBuilder().setApiVersion(AlinkConstants.PROVISION_DEVICE_PIDTOPK_VERSION).setPath(AlinkConstants.PROVISION_DEVICE_PIDTOPK).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH);
            authType.setParams(map);
            new IoTAPIClientFactory().getClient().send(authType.build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.16
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onFailure(ioTRequest, exc);
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onResponse(ioTRequest, ioTResponse);
                    }
                }
            });
        } catch (Exception e) {
            ALog.e("ProvisionRepository", e.getLocalizedMessage());
        }
    }

    public static void provisionSLB(ArrayList arrayList, final IoTCallback ioTCallback) {
        try {
            new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setApiVersion("1.0.1").setPath(AlinkConstants.PROVISION_BATCH_SLB).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam(AlinkConstants.KEY_SUB_DEVICE_LIST, (List) arrayList).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.18
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onFailure(ioTRequest, exc);
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onResponse(ioTRequest, ioTResponse);
                    }
                }
            });
        } catch (Exception e) {
            ALog.e("ProvisionRepository", e.getLocalizedMessage());
        }
    }

    public static RetryTransitoryClient triggerDevice2Search(BatchDiscoveryParams batchDiscoveryParams, final IoTCallback ioTCallback) {
        ALog.d("ProvisionRepository", "triggerDevice2Search() called with: params = [" + batchDiscoveryParams + "], callback = [" + ioTCallback + "]");
        if (batchDiscoveryParams == null || TextUtils.isEmpty(batchDiscoveryParams.productKey) || TextUtils.isEmpty(batchDiscoveryParams.deviceName)) {
            throw new IllegalArgumentException("invalid batch discovery params");
        }
        if (ioTCallback == null) {
            ioTCallback.onFailure(null, new IllegalArgumentException("callback is null"));
            return null;
        }
        if (DCEnvHelper.hasApiClient()) {
            return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.0").setPath(AlinkConstants.HTTP_PATH_SWITCH_MODE).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam("regProductKey", batchDiscoveryParams.productKey).addParam("regDeviceName", batchDiscoveryParams.deviceName).addParam("enrolleeProductKey", batchDiscoveryParams.enrolleeProductKey).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.2
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onFailure(ioTRequest, exc);
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onResponse(ioTRequest, ioTResponse);
                    }
                }
            });
        }
        ALog.w("ProvisionRepository", "triggerDevice2Search apiclient not exist, return.");
        ioTCallback.onFailure(null, new Exception("apiclient not exist"));
        return null;
    }

    public static RetryTransitoryClient uploadDeviceError(String str, String str2, JSONObject jSONObject, final IoTCallback ioTCallback) {
        if (ioTCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (jSONObject == null) {
            ioTCallback.onFailure(null, new IllegalArgumentException("upload data is null"));
            return null;
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("ProvisionRepository", "uploadDeviceError apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(jSONObject);
        return TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setApiVersion("1.0.0").setPath(AlinkConstants.HTTP_PATH_DEVICE_ERROR_UPLOAD).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam("productKey", str).addParam("deviceName", str2).addParam("type", "BIZ_CONNECTION").addParam("errors", (List) arrayList).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepository.3
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onFailure(ioTRequest, exc);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onResponse(ioTRequest, ioTResponse);
                }
            }
        });
    }
}
