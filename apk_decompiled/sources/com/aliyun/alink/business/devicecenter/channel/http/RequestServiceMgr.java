package com.aliyun.alink.business.devicecenter.channel.http;

import com.aliyun.alink.business.devicecenter.channel.http.services.IRequestService;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class RequestServiceMgr {
    public static final String ACTIVATION_REQUEST_SERVICE = "activationRequestService";
    public static final String ACTIVATION_RTOS_REQUEST_SERVICE = "rtosBindRequestService";
    public static final String STATIC_BIND_REQUEST_SERVICE = "staticBindRequestService";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Map<String, IRequestService> f3493a;

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final RequestServiceMgr f3494a = new RequestServiceMgr();
    }

    public static RequestServiceMgr getInstance() {
        return SingletonHolder.f3494a;
    }

    public IRequestService getRequestService(String str) {
        if (StringUtils.isEmpty(str)) {
            ALog.w("RequestServiceMgr", "requestServiceName: " + str);
            throw new IllegalArgumentException("requestServiceName or requestService is invalid");
        }
        if (this.f3493a.containsKey(str)) {
            return this.f3493a.get(str);
        }
        ALog.w("RequestServiceMgr", "requestServiceName: " + str + " not exist.");
        return null;
    }

    public void registerRequestService(String str, IRequestService iRequestService) {
        if (!StringUtils.isEmpty(str) && iRequestService != null) {
            if (this.f3493a.containsKey(str) && this.f3493a.get(str) != null) {
                ALog.w("RequestServiceMgr", String.format("requestServiceName: %s has exist. %s is replaced by %s", str, this.f3493a.get(str).getClass().getName(), iRequestService.getClass().getName()));
            }
            this.f3493a.put(str, iRequestService);
            return;
        }
        ALog.w("RequestServiceMgr", "requestServiceName: " + str + " requestService: " + iRequestService);
        throw new IllegalArgumentException("requestServiceName or requestService is invalid");
    }

    public RequestServiceMgr() {
        this.f3493a = new ConcurrentHashMap();
    }
}
