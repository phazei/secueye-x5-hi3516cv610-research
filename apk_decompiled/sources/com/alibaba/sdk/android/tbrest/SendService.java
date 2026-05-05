package com.alibaba.sdk.android.tbrest;

import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.tbrest.rest.RestConstants;
import com.alibaba.sdk.android.tbrest.rest.f;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SendService {
    static final SendService instance = new SendService();
    public Context context = null;
    public String appId = null;
    public String appKey = null;
    public String appSecret = null;
    public String appVersion = null;
    public String channel = null;
    public String userNick = null;
    public String host = null;
    public Boolean openHttp = false;
    public String country = null;
    private a sendAsyncExecutor = new a();

    public static SendService getInstance() {
        return instance;
    }

    public void init(Context context, String str, String str2, String str3, String str4, String str5) {
        this.context = context;
        this.appId = str;
        this.appKey = str2;
        this.appVersion = str3;
        this.channel = str4;
        this.userNick = str5;
    }

    public void updateAppVersion(String str) {
        if (str != null) {
            this.appVersion = str;
        }
    }

    public void updateUserNick(String str) {
        if (str != null) {
            this.userNick = str;
        }
    }

    public void updateChannel(String str) {
        if (str != null) {
            this.channel = str;
        }
    }

    public void changeHost(String str) {
        if (str != null) {
            this.host = str;
        }
    }

    public String getChangeHost() {
        return this.host;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public Boolean sendRequest(String str, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        String str3;
        if (canSend().booleanValue()) {
            if (str == null) {
                String str4 = this.host;
                str3 = str4 != null ? str4 : RestConstants.G_DEFAULT_ADASHX_HOST;
            } else {
                str3 = str;
            }
            return Boolean.valueOf(f.a(this, this.appKey, this.context, str3, j, str2, i, obj, obj2, obj3, map));
        }
        return false;
    }

    public void sendRequestAsyn(String str, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        String str3;
        if (canSend().booleanValue()) {
            if (str == null) {
                String str4 = this.host;
                str3 = str4 != null ? str4 : RestConstants.G_DEFAULT_ADASHX_HOST;
            } else {
                str3 = str;
            }
            this.sendAsyncExecutor.a(new RestThread(this, "rest thread", this.appKey, this.context, str3, j, str2, i, obj, obj2, obj3, map, false));
        }
    }

    public void sendRequestAsynByAppkeyAndUrl(String str, String str2, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        if (canSend().booleanValue()) {
            if (str == null) {
                Log.e(LogUtil.TAG, "need set url");
            } else {
                this.sendAsyncExecutor.a(new RestThread(this, "rest thread", str2 == null ? this.appKey : str2, this.context, str, j, str3, i, obj, obj2, obj3, map, true));
            }
        }
    }

    @Deprecated
    public String sendRequestByUrl(String str, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        if (canSend().booleanValue()) {
            return f.a(this, str, this.appKey, this.context, j, str2, i, obj, obj2, obj3, map);
        }
        return null;
    }

    private Boolean canSend() {
        if (this.appId == null || this.appVersion == null || this.appKey == null || this.context == null) {
            LogUtil.e("have send args is null，you must init first. appId " + this.appId + " appVersion " + this.appVersion + " appKey " + this.appKey);
            return false;
        }
        return true;
    }

    public static class RestThread implements Runnable {
        private Object aArg1;
        private Object aArg2;
        private Object aArg3;
        private int aEventId;
        private Map<String, String> aExtData;
        private String aPage;
        private long aTimestamp;
        private String adashxServerHost;
        private String appKey;
        private Context context;
        private Boolean isUrl;
        private SendService mSendService;

        public RestThread() {
            this.isUrl = false;
        }

        public RestThread(SendService sendService, String str, String str2, Context context, String str3, long j, String str4, int i, Object obj, Object obj2, Object obj3, Map<String, String> map, Boolean bool) {
            this.isUrl = false;
            this.context = context;
            this.adashxServerHost = str3;
            this.aTimestamp = j;
            this.aPage = str4;
            this.aEventId = i;
            this.aArg1 = obj;
            this.aArg2 = obj2;
            this.aArg3 = obj3;
            this.aExtData = map;
            this.appKey = str2;
            this.isUrl = bool;
            this.mSendService = sendService;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (this.isUrl.booleanValue()) {
                    f.b(this.mSendService, this.appKey, this.context, this.adashxServerHost, this.aTimestamp, this.aPage, this.aEventId, this.aArg1, this.aArg2, this.aArg3, this.aExtData);
                } else {
                    f.a(this.mSendService, this.appKey, this.context, this.adashxServerHost, this.aTimestamp, this.aPage, this.aEventId, this.aArg1, this.aArg2, this.aArg3, this.aExtData);
                }
            } catch (Exception e) {
                LogUtil.e("send log asyn error ", e);
            }
        }
    }
}
