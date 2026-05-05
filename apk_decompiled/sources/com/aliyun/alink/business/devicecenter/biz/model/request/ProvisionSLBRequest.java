package com.aliyun.alink.business.devicecenter.biz.model.request;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ProvisionSLBRequest extends BaseApiRequest {
    public List<String> subDeviceIdList;
    public String REQUEST_METHOD = "POST";
    public String API_HOST = "";
    public String API_PATH = AlinkConstants.PROVISION_BATCH_SLB;
    public String API_VERSION = "1.0.1";

    public List<String> getSubDeviceIdList() {
        return this.subDeviceIdList;
    }

    public void setSubDeviceIdList(List<String> list) {
        this.subDeviceIdList = list;
    }
}
