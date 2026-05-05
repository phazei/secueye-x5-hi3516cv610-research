package com.aliyun.alink.business.devicecenter.config.model;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DCAlibabaConcurrentConfigParams extends DCConfigParams {
    public List<DCAlibabaConfigParams> configParamsList;
    public String familyId;

    @Override // com.aliyun.alink.business.devicecenter.config.model.DCConfigParams
    public String toString() {
        return "DCAlibabaConcurrentConfigParams{configParamsList=" + this.configParamsList + '}';
    }
}
