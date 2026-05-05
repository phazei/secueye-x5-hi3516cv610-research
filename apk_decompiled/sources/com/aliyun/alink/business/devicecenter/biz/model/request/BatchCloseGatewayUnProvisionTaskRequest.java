package com.aliyun.alink.business.devicecenter.biz.model.request;

import com.alibaba.fastjson.JSONArray;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes.dex */
public class BatchCloseGatewayUnProvisionTaskRequest extends BaseApiRequest {
    public JSONArray taskIdList;
    public String REQUEST_METHOD = "POST";
    public String API_HOST = "";
    public String API_PATH = AlinkConstants.HTTP_PATH_GET_BATCH_MESH_PROVISION_CLOSE;
    public String API_VERSION = "1.0.0";

    public JSONArray getTaskIdList() {
        return this.taskIdList;
    }

    public void setTaskIdList(JSONArray jSONArray) {
        this.taskIdList = jSONArray;
    }
}
