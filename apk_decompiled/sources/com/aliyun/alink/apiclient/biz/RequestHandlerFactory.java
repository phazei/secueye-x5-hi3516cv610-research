package com.aliyun.alink.apiclient.biz;

import com.aliyun.alink.apiclient.CommonPoPRequest;
import com.aliyun.alink.apiclient.CommonRequest;

/* JADX INFO: loaded from: classes.dex */
public class RequestHandlerFactory {
    public IHandler createHandler(CommonRequest commonRequest) {
        if (commonRequest == null) {
            return null;
        }
        if ("/auth/register/device".equals(commonRequest.getPath())) {
            return new GetDeviceTriadRequestHAndler();
        }
        if (commonRequest instanceof CommonPoPRequest) {
            return new PoPRequestHandler();
        }
        return new IoTRequestHandler();
    }
}
