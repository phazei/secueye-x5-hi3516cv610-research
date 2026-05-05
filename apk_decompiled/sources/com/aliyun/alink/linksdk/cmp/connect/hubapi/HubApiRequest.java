package com.aliyun.alink.linksdk.cmp.connect.hubapi;

import android.text.TextUtils;
import com.aliyun.alink.apiclient.CommonRequest;
import com.aliyun.alink.apiclient.constants.MethodType;
import com.aliyun.alink.apiclient.constants.Schema;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class HubApiRequest extends ARequest {
    public Map<String, Object> params = null;
    public String domain = null;
    public MethodType method = null;
    public String path = null;
    public Schema schema = null;

    public static HubApiRequest build(String str, Map<String, Object> map) {
        return build(null, MethodType.POST, str, Schema.HTTPS, map);
    }

    public static HubApiRequest build(String str, MethodType methodType, String str2, Schema schema, Map<String, Object> map) {
        HubApiRequest hubApiRequest = new HubApiRequest();
        hubApiRequest.domain = str;
        hubApiRequest.method = methodType;
        hubApiRequest.path = str2;
        hubApiRequest.schema = schema;
        if (map != null) {
            hubApiRequest.params.putAll(map);
        }
        return hubApiRequest;
    }

    public void addParams(String str, Object obj) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (this.params == null) {
            this.params = new HashMap();
        }
        this.params.put(str, obj);
    }

    public CommonRequest toChannelRequest() {
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setPath(this.path);
        if (!TextUtils.isEmpty(this.domain)) {
            commonRequest.setDomain(this.domain);
        }
        MethodType methodType = this.method;
        if (methodType != null) {
            commonRequest.setMethod(methodType);
        }
        Schema schema = this.schema;
        if (schema != null) {
            commonRequest.setSchema(schema);
        }
        Map<String, Object> map = this.params;
        if (map != null) {
            commonRequest.setQueryParams(map);
        }
        return commonRequest;
    }
}
