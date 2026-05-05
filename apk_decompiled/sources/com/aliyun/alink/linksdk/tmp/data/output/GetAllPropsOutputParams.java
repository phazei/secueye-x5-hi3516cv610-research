package com.aliyun.alink.linksdk.tmp.data.output;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

/* JADX INFO: loaded from: classes2.dex */
public class GetAllPropsOutputParams extends OutputParams {
    public static final String PARAMS_GETALLPROPS_RESPONSE = "params_getallprops_response";

    public GetAllPropsOutputParams(e eVar) {
        super(PARAMS_GETALLPROPS_RESPONSE, new ValueWrapper(eVar));
    }
}
