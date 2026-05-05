package com.alibaba.sdk.android.tbrest.rest;

import android.content.Context;
import com.alibaba.sdk.android.tbrest.SendService;
import com.alibaba.sdk.android.tbrest.request.BizRequest;
import com.alibaba.sdk.android.tbrest.request.UrlWrapper;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import com.alibaba.sdk.android.tbrest.utils.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: RestReqSend.java */
/* JADX INFO: loaded from: classes.dex */
public class f {
    public static boolean a(SendService sendService, String str, Context context, String str2, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        try {
            LogUtil.i("RestAPI start send log!");
            String strA = e.a(sendService, str, j, str3, i, obj, obj2, obj3, map);
            if (StringUtils.isNotBlank(strA)) {
                LogUtil.i("RestAPI build data succ!");
                HashMap map2 = new HashMap(1);
                map2.put(String.valueOf(i), strA);
                byte[] packRequest = null;
                try {
                    packRequest = BizRequest.getPackRequest(sendService, str, context, map2);
                } catch (Exception e) {
                    LogUtil.e(e.toString());
                }
                if (packRequest == null) {
                    return false;
                }
                LogUtil.i("packRequest success!");
                return UrlWrapper.sendRequest(sendService, str2, packRequest).isSuccess();
            }
            LogUtil.i("UTRestAPI build data failure!");
            return false;
        } catch (Throwable th) {
            LogUtil.e("system error!", th);
            return false;
        }
    }

    public static boolean b(SendService sendService, String str, Context context, String str2, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        try {
            LogUtil.i("RestAPI start send log by url!");
            String strA = e.a(sendService, str, j, str3, i, obj, obj2, obj3, map);
            if (StringUtils.isNotBlank(strA)) {
                LogUtil.i("RestAPI build data succ by url!");
                HashMap map2 = new HashMap(1);
                map2.put(String.valueOf(i), strA);
                byte[] packRequest = null;
                try {
                    packRequest = BizRequest.getPackRequest(sendService, str, context, map2);
                } catch (Exception e) {
                    LogUtil.e(e.toString());
                }
                if (packRequest == null) {
                    return false;
                }
                LogUtil.i("packRequest success by url!");
                return UrlWrapper.sendRequest(sendService, str, str2, packRequest).isSuccess();
            }
            LogUtil.i("UTRestAPI build data failure by url!");
            return false;
        } catch (Throwable th) {
            LogUtil.e("system error by url!", th);
            return false;
        }
    }

    @Deprecated
    public static String a(SendService sendService, String str, String str2, Context context, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        try {
            LogUtil.i("sendLogByUrl RestAPI start send log!");
            d dVarA = e.a(sendService, str2, str, context, j, str3, i, obj, obj2, obj3, map);
            if (dVarA != null) {
                LogUtil.i("sendLogByUrl RestAPI build data succ!");
                Map<String, Object> mapM24a = dVarA.m24a();
                if (mapM24a == null) {
                    LogUtil.i("sendLogByUrl postReqData is null!");
                    return null;
                }
                String strA = dVarA.a();
                if (StringUtils.isEmpty(strA)) {
                    LogUtil.i("sendLogByUrl reqUrl is null!");
                    return null;
                }
                byte[] bArrA = b.a(2, strA, mapM24a, true);
                if (bArrA != null) {
                    try {
                        String str4 = new String(bArrA, "UTF-8");
                        if (!StringUtils.isEmpty(str4)) {
                            return str4;
                        }
                    } catch (UnsupportedEncodingException e) {
                        LogUtil.e("sendLogByUrl result encoding UTF-8 error!", e);
                    }
                }
            } else {
                LogUtil.i("sendLogByUrl UTRestAPI build data failure!");
            }
        } catch (Throwable th) {
            LogUtil.e("sendLogByUrl system error!", th);
        }
        return null;
    }
}
