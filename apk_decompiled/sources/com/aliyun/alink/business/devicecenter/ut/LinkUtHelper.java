package com.aliyun.alink.business.devicecenter.ut;

import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.RequestManage;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.DeviceCommonConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.linksdk.tmp.api.DevFoundOutputParams;
import com.facebook.internal.NativeProtocol;
import com.ut.mini.UTAnalytics;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class LinkUtHelper {
    public static final String CONNECT_FAIL = "sdk_bluetooth_connect_fail";
    public static final String CONNECT_START = "sdk_bluetooth_connect_start";
    public static final String CONNECT_SUCCESS = "sdk_bluetooth_connect_success";
    public static final String CONNECT_TIMEOUT = "sdk_bluetooth_connect_timeout";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f3751a = "LinkUtHelper";

    public static HashMap<String, String> a(UtLinkInfo utLinkInfo) {
        HashMap<String, String> map = new HashMap<>(5);
        if (TextUtils.isEmpty(utLinkInfo.getUserId())) {
            map.put("user_id", RequestManage.getInstance().getUserId());
        } else {
            map.put("user_id", utLinkInfo.getUserId());
        }
        if (!TextUtils.isEmpty(utLinkInfo.getProductKey())) {
            map.put(DevFoundOutputParams.PARAMS_PRODUCT_KEY, utLinkInfo.getProductKey());
        }
        if (!TextUtils.isEmpty(utLinkInfo.getLinkType())) {
            map.put("link_type", utLinkInfo.getLinkType());
        }
        if (!TextUtils.isEmpty(utLinkInfo.getConnectionTime())) {
            map.put("connection_time", utLinkInfo.getConnectionTime());
        }
        if (!TextUtils.isEmpty(utLinkInfo.getErrorCode())) {
            map.put(NativeProtocol.BRIDGE_ARG_ERROR_CODE, utLinkInfo.getErrorCode());
        }
        return map;
    }

    public static void connectEvent(String str, UtLinkInfo utLinkInfo) {
        if (DCEnvHelper.getHasUTAbility()) {
            try {
                a(str, a(utLinkInfo));
            } catch (Exception e) {
                ALog.w(f3751a, "connectEvent->doUtTrack e=" + e);
            }
        }
    }

    public static void a(String str, Map<String, String> map) {
        if (DCEnvHelper.isTgEnv()) {
            HashMap map2 = new HashMap(2);
            map2.put(WifiProvisionUtConst.KEY_BUSIZ_INFO, JSON.toJSONString(map));
            a("peiwang_sdk_bluetooth_connect", str, map2, "");
        }
    }

    public static void a(String str, String str2, Map<String, String> map, String str3) {
        Map<String, String> map2 = map == null ? new HashMap(8) : map;
        LogUtils.i(f3751a, "customHit: eventName" + str2 + ", " + JSON.toJSONString(map2));
        UTOriginalCustomHitBuilder uTOriginalCustomHitBuilder = new UTOriginalCustomHitBuilder(str, 19999, str2, (String) null, (String) null, map2);
        if (!TextUtils.isEmpty(str3)) {
            uTOriginalCustomHitBuilder.setProperty(DeviceCommonConstants.SPM_KEY, str3);
        }
        UTAnalytics.getInstance().getDefaultTracker().send(uTOriginalCustomHitBuilder.build());
    }
}
