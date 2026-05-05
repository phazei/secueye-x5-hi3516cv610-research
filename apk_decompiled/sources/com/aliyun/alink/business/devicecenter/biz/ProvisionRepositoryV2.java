package com.aliyun.alink.business.devicecenter.biz;

import android.util.Log;
import com.alibaba.ailabs.tg.basebiz.user.UserManager;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.biz.model.request.BatchCloseGatewayUnProvisionTaskRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.BatchGetGatewayProvisionTaskRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.MeshDeviceDiscoveryTriggerRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.ProvisionSLBRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.QueryDiscoveredDevicesRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.QueryProductKeyRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.TriggerGatewayBatchProvisionRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.mtop.BatchCloseGatewayUnProvisionTaskMTopRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.mtop.BatchGetGatewayProvisionTaskMTopRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.mtop.MeshDeviceDiscoveryTriggerMTopRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.mtop.ProvisionSLBMTopRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.mtop.QueryDiscoveredDevicesMTopRequest;
import com.aliyun.alink.business.devicecenter.biz.model.request.mtop.TriggerGatewayBatchProvisionMTopRequest;
import com.aliyun.alink.business.devicecenter.biz.model.response.MeshDeviceDiscoveryTriggerResponse;
import com.aliyun.alink.business.devicecenter.biz.model.response.QueryDiscoveredDevicesResponse;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.model.ProvisionSLBItem;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;
import com.aliyun.alink.linksdk.connectsdk.ApiHelper;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ProvisionRepositoryV2 {
    public static void a(String str, int i, int i2, Boolean bool, List list, ApiCallBack<QueryDiscoveredDevicesResponse> apiCallBack) {
        QueryDiscoveredDevicesRequest queryDiscoveredDevicesRequest;
        if (DCEnvHelper.isTgEnv()) {
            QueryDiscoveredDevicesMTopRequest queryDiscoveredDevicesMTopRequest = new QueryDiscoveredDevicesMTopRequest();
            queryDiscoveredDevicesMTopRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
            queryDiscoveredDevicesRequest = queryDiscoveredDevicesMTopRequest;
        } else {
            queryDiscoveredDevicesRequest = new QueryDiscoveredDevicesRequest();
        }
        queryDiscoveredDevicesRequest.setGatewayIotId(str);
        queryDiscoveredDevicesRequest.setPageNo(i);
        queryDiscoveredDevicesRequest.setPageSize(i2);
        queryDiscoveredDevicesRequest.setAppReportDevices(list);
        queryDiscoveredDevicesRequest.setScanStartedFirstQuery(bool.booleanValue());
        ApiHelper.getInstance().send(queryDiscoveredDevicesRequest, apiCallBack);
    }

    public static void batchMeshDeviceProvisionTrigger(JSONArray jSONArray, String str, ApiCallBack<List<String>> apiCallBack) {
        TriggerGatewayBatchProvisionRequest triggerGatewayBatchProvisionRequest;
        if (apiCallBack == null) {
            throw new IllegalArgumentException("callback is null");
        }
        Log.d("ProvisionRepositoryV2", "batchMeshDeviceProvisionTrigger: " + JSONObject.toJSONString(jSONArray));
        if (DCEnvHelper.isTgEnv()) {
            TriggerGatewayBatchProvisionMTopRequest triggerGatewayBatchProvisionMTopRequest = new TriggerGatewayBatchProvisionMTopRequest();
            triggerGatewayBatchProvisionMTopRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
            triggerGatewayBatchProvisionRequest = triggerGatewayBatchProvisionMTopRequest;
        } else {
            triggerGatewayBatchProvisionRequest = new TriggerGatewayBatchProvisionRequest();
        }
        triggerGatewayBatchProvisionRequest.setDiscoveredDeviceList(jSONArray);
        triggerGatewayBatchProvisionRequest.setGatewayIotId(str);
        ApiHelper.getInstance().send(triggerGatewayBatchProvisionRequest, apiCallBack);
    }

    public static void closeBatchMeshProvision(JSONArray jSONArray) {
        BatchCloseGatewayUnProvisionTaskRequest batchCloseGatewayUnProvisionTaskRequest;
        Log.d("ProvisionRepositoryV2", "getBatchMeshProvisionResult() called with: taskIds = [" + jSONArray.toJSONString() + "]");
        if (DCEnvHelper.isTgEnv()) {
            BatchCloseGatewayUnProvisionTaskMTopRequest batchCloseGatewayUnProvisionTaskMTopRequest = new BatchCloseGatewayUnProvisionTaskMTopRequest();
            batchCloseGatewayUnProvisionTaskMTopRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
            batchCloseGatewayUnProvisionTaskRequest = batchCloseGatewayUnProvisionTaskMTopRequest;
        } else {
            batchCloseGatewayUnProvisionTaskRequest = new BatchCloseGatewayUnProvisionTaskRequest();
        }
        batchCloseGatewayUnProvisionTaskRequest.setTaskIdList(jSONArray);
        ApiHelper.getInstance().send(batchCloseGatewayUnProvisionTaskRequest, new ApiCallBack() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2.2
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str) {
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(Object obj) {
            }
        });
    }

    public static void getBatchMeshProvisionResult(JSONArray jSONArray, ApiCallBack<JSONArray> apiCallBack) {
        BatchGetGatewayProvisionTaskRequest batchGetGatewayProvisionTaskRequest;
        if (apiCallBack == null) {
            throw new IllegalArgumentException("getBatchMeshProvisionResult callback is null");
        }
        if (DCEnvHelper.isTgEnv()) {
            BatchGetGatewayProvisionTaskMTopRequest batchGetGatewayProvisionTaskMTopRequest = new BatchGetGatewayProvisionTaskMTopRequest();
            batchGetGatewayProvisionTaskMTopRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
            batchGetGatewayProvisionTaskRequest = batchGetGatewayProvisionTaskMTopRequest;
        } else {
            batchGetGatewayProvisionTaskRequest = new BatchGetGatewayProvisionTaskRequest();
        }
        batchGetGatewayProvisionTaskRequest.setTaskIdList(jSONArray);
        Log.d("ProvisionRepositoryV2", "getBatchMeshProvisionResult() called with: taskIds = [" + jSONArray.toJSONString() + "], callback = [" + apiCallBack + "]");
        ApiHelper.getInstance().send(batchGetGatewayProvisionTaskRequest, apiCallBack);
    }

    public static void getDiscoveredMeshDevice(final String str, final int i, final int i2, final Boolean bool, final List list, final JSONArray jSONArray, final MeshDiscoverCallback meshDiscoverCallback) {
        Log.d("ProvisionRepositoryV2", "getDiscoveredMeshDevice() called with: iotId = [" + str + "], pageNo = [" + i + "], pageSize = [" + i2 + "], isScanStartedFirstQuery = [" + bool + "], appReportDevice = [" + list + "], result = [" + jSONArray + "], callback = [" + meshDiscoverCallback + "]");
        if (meshDiscoverCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        a(str, i, i2, bool, list, new ApiCallBack<QueryDiscoveredDevicesResponse>() { // from class: com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2.1
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i3, String str2) {
                if (jSONArray.size() > 0) {
                    meshDiscoverCallback.onSuccess(jSONArray);
                } else {
                    meshDiscoverCallback.onFailure(str2);
                }
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(QueryDiscoveredDevicesResponse queryDiscoveredDevicesResponse) {
                int total = queryDiscoveredDevicesResponse.getTotal();
                Log.d("ProvisionRepositoryV2", "onResponse: totalCount=" + total);
                JSONArray list2 = queryDiscoveredDevicesResponse.getList();
                jSONArray.addAll(list2);
                if (list2 == null || list2.size() < i2) {
                    meshDiscoverCallback.onSuccess(jSONArray);
                } else if (list2.size() != i2 || list2.size() >= total) {
                    meshDiscoverCallback.onSuccess(jSONArray);
                } else {
                    Log.d("ProvisionRepositoryV2", "onResponse: 加载第二页");
                    ProvisionRepositoryV2.getDiscoveredMeshDevice(str, i + 1, i2, bool, list, jSONArray, meshDiscoverCallback);
                }
            }
        });
    }

    public static void meshDeviceDiscoveryTrigger(String str, boolean z, ApiCallBack<MeshDeviceDiscoveryTriggerResponse> apiCallBack) {
        MeshDeviceDiscoveryTriggerRequest meshDeviceDiscoveryTriggerRequest;
        if (DCEnvHelper.isTgEnv()) {
            MeshDeviceDiscoveryTriggerMTopRequest meshDeviceDiscoveryTriggerMTopRequest = new MeshDeviceDiscoveryTriggerMTopRequest();
            meshDeviceDiscoveryTriggerMTopRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
            meshDeviceDiscoveryTriggerRequest = meshDeviceDiscoveryTriggerMTopRequest;
        } else {
            meshDeviceDiscoveryTriggerRequest = new MeshDeviceDiscoveryTriggerRequest();
        }
        meshDeviceDiscoveryTriggerRequest.setGatewayIotId(str);
        meshDeviceDiscoveryTriggerRequest.setStopSearch(z);
        ApiHelper.getInstance().send(meshDeviceDiscoveryTriggerRequest, apiCallBack);
    }

    public static void pidReturnToPk(String str, ApiCallBack<Object> apiCallBack) {
        ALog.d("ProvisionRepositoryV2", "pidReturnToPk called with: PID = " + str);
        if (DCEnvHelper.isTgEnv()) {
            if (apiCallBack != null) {
                apiCallBack.onFail(-1, "Not implement in genie platform");
            }
        } else {
            QueryProductKeyRequest queryProductKeyRequest = new QueryProductKeyRequest();
            queryProductKeyRequest.setProductId(str);
            ApiHelper.getInstance().send(queryProductKeyRequest, apiCallBack);
        }
    }

    public static void provisionSLB(ArrayList arrayList, ApiCallBack<List<ProvisionSLBItem>> apiCallBack) {
        ProvisionSLBRequest provisionSLBRequest;
        if (DCEnvHelper.isTgEnv()) {
            ProvisionSLBMTopRequest provisionSLBMTopRequest = new ProvisionSLBMTopRequest();
            provisionSLBMTopRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
            provisionSLBRequest = provisionSLBMTopRequest;
        } else {
            provisionSLBRequest = new ProvisionSLBRequest();
        }
        provisionSLBRequest.setSubDeviceIdList(arrayList);
        ApiHelper.getInstance().send(provisionSLBRequest, apiCallBack);
    }
}
