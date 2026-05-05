package com.taobao.accs.flowcontrol;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.utl.ALog;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class FlowControl {
    public static final int DELAY_MAX = -1;
    public static final int DELAY_MAX_BRUSH = -1000;
    public static final int HIGH_FLOW_CTRL = 2;
    public static final int HIGH_FLOW_CTRL_BRUSH = 3;
    public static final int LOW_FLOW_CTRL = 1;
    public static final int NO_FLOW_CTRL = 0;
    public static final String SERVICE_ALL = "ALL";
    public static final String SERVICE_ALL_BRUSH = "ALL_BRUSH";
    public static final int STATUS_FLOW_CTRL_ALL = 420;
    public static final int STATUS_FLOW_CTRL_BRUSH = 422;
    public static final int STATUS_FLOW_CTRL_CUR = 421;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f6325a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private FlowCtrlInfoHolder f6326b;

    public FlowControl(Context context) {
        this.f6325a = context;
    }

    public int a(Map<Integer, String> map, String str) {
        long j;
        int iIntValue;
        FlowControlInfo flowControlInfo;
        if (map != null) {
            try {
                String str2 = map.get(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_STATUS.ordinal()));
                String str3 = map.get(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_DELAY.ordinal()));
                String str4 = map.get(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_EXPIRE.ordinal()));
                String str5 = map.get(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_BUSINESS.ordinal()));
                iIntValue = TextUtils.isEmpty(str2) ? 0 : Integer.valueOf(str2).intValue();
                try {
                    long jLongValue = TextUtils.isEmpty(str3) ? 0L : Long.valueOf(str3).longValue();
                    try {
                        long jLongValue2 = TextUtils.isEmpty(str4) ? 0L : Long.valueOf(str4).longValue();
                        if ((iIntValue != 420 && iIntValue != 421 && iIntValue != 422) || !a(jLongValue, jLongValue2)) {
                            return 0;
                        }
                        try {
                            synchronized (this) {
                                try {
                                    if (this.f6326b == null) {
                                        this.f6326b = new FlowCtrlInfoHolder();
                                    }
                                    if (iIntValue == 420) {
                                        j = jLongValue;
                                        flowControlInfo = new FlowControlInfo("ALL", "", iIntValue, jLongValue, jLongValue2, System.currentTimeMillis());
                                        this.f6326b.put("ALL", "", flowControlInfo);
                                    } else {
                                        j = jLongValue;
                                        if (iIntValue == 422) {
                                            flowControlInfo = new FlowControlInfo(SERVICE_ALL_BRUSH, "", iIntValue, j, jLongValue2, System.currentTimeMillis());
                                            this.f6326b.put(SERVICE_ALL_BRUSH, "", flowControlInfo);
                                        } else if (iIntValue != 421 || TextUtils.isEmpty(str)) {
                                            flowControlInfo = null;
                                        } else {
                                            FlowControlInfo flowControlInfo2 = new FlowControlInfo(str, str5, iIntValue, j, jLongValue2, System.currentTimeMillis());
                                            this.f6326b.put(str, str5, flowControlInfo2);
                                            flowControlInfo = flowControlInfo2;
                                        }
                                    }
                                    if (flowControlInfo != null) {
                                        ALog.e("FlowControl", "updateFlowCtrlInfo " + flowControlInfo.toString(), new Object[0]);
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    j = jLongValue;
                                    try {
                                        throw th;
                                    } catch (Throwable th2) {
                                        th = th2;
                                    }
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        j = jLongValue;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    j = 0;
                }
            } catch (Throwable th6) {
                th = th6;
                j = 0;
                iIntValue = 0;
            }
            ALog.e("FlowControl", "updateFlowCtrlInfo", th, new Object[0]);
        } else {
            j = 0;
            iIntValue = 0;
        }
        if (j > 0) {
            return 1;
        }
        if (j == 0) {
            return 0;
        }
        return 422 == iIntValue ? 3 : 2;
    }

    private boolean a(long j, long j2) {
        if (j != 0 && j2 > 0) {
            return true;
        }
        ALog.e("FlowControl", "error flow ctrl info", new Object[0]);
        return false;
    }

    public long a(String str, String str2) {
        long j;
        long j2;
        long j3;
        FlowCtrlInfoHolder flowCtrlInfoHolder = this.f6326b;
        long j4 = 0;
        if (flowCtrlInfoHolder == null || flowCtrlInfoHolder.flowCtrlMap == null || TextUtils.isEmpty(str)) {
            return 0L;
        }
        synchronized (this) {
            FlowControlInfo flowControlInfo = this.f6326b.get("ALL", null);
            FlowControlInfo flowControlInfo2 = this.f6326b.get(SERVICE_ALL_BRUSH, null);
            FlowControlInfo flowControlInfo3 = this.f6326b.get(str, null);
            FlowControlInfo flowControlInfo4 = this.f6326b.get(str, str2);
            j = (flowControlInfo == null || flowControlInfo.isExpired()) ? 0L : flowControlInfo.delayTime;
            long j5 = (flowControlInfo2 == null || flowControlInfo2.isExpired()) ? 0L : flowControlInfo2.delayTime;
            j2 = (flowControlInfo3 == null || flowControlInfo3.isExpired()) ? 0L : flowControlInfo3.delayTime;
            if (flowControlInfo4 != null && !flowControlInfo4.isExpired()) {
                j4 = flowControlInfo4.delayTime;
            }
            j3 = -1;
            if (j != -1 && j4 != -1 && j2 != -1) {
                if (j5 == -1) {
                    j3 = -1000;
                } else {
                    long j6 = j > j4 ? j : j4;
                    j3 = j6 > j2 ? j6 : j2;
                }
            }
            if ((flowControlInfo4 != null && flowControlInfo4.isExpired()) || (flowControlInfo != null && flowControlInfo.isExpired())) {
                a();
            }
        }
        ALog.e("FlowControl", "getFlowCtrlDelay service " + str + " biz " + str2 + " result:" + j3 + " global:" + j + " serviceDelay:" + j2 + " bidDelay:" + j4, new Object[0]);
        return j3;
    }

    private void a() {
        FlowCtrlInfoHolder flowCtrlInfoHolder = this.f6326b;
        if (flowCtrlInfoHolder == null || flowCtrlInfoHolder.flowCtrlMap == null) {
            return;
        }
        synchronized (this) {
            Iterator<Map.Entry<String, FlowControlInfo>> it = this.f6326b.flowCtrlMap.entrySet().iterator();
            while (it.hasNext()) {
                if (it.next().getValue().isExpired()) {
                    it.remove();
                }
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class FlowControlInfo implements Serializable {
        private static final long serialVersionUID = -2259991484877844919L;
        public String bizId;
        public long delayTime;
        public long expireTime;
        public String serviceId;
        public long startTime;
        public int status;

        public FlowControlInfo(String str, String str2, int i, long j, long j2, long j3) {
            this.serviceId = str;
            this.bizId = str2;
            this.status = i;
            this.delayTime = j;
            this.expireTime = j2 <= 0 ? 0L : j2;
            this.startTime = j3 > 0 ? j3 : 0L;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - (this.startTime + this.expireTime) > 0;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("flow ctrl serviceId:");
            stringBuffer.append(this.serviceId);
            stringBuffer.append(" bizId:");
            stringBuffer.append(this.bizId);
            stringBuffer.append(" status:");
            stringBuffer.append(this.status);
            stringBuffer.append(" delayTime:");
            stringBuffer.append(this.delayTime);
            stringBuffer.append(" startTime:");
            stringBuffer.append(this.startTime);
            stringBuffer.append(" expireTime:");
            stringBuffer.append(this.expireTime);
            return stringBuffer.toString();
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class FlowCtrlInfoHolder implements Serializable {
        private static final long serialVersionUID = 6307563052429742524L;
        Map<String, FlowControlInfo> flowCtrlMap = null;

        public void put(String str, String str2, FlowControlInfo flowControlInfo) {
            if (!TextUtils.isEmpty(str2)) {
                str = str + OpenAccountUIConstants.UNDER_LINE + str2;
            }
            if (this.flowCtrlMap == null) {
                this.flowCtrlMap = new HashMap();
            }
            this.flowCtrlMap.put(str, flowControlInfo);
        }

        public FlowControlInfo get(String str, String str2) {
            if (this.flowCtrlMap == null) {
                return null;
            }
            if (!TextUtils.isEmpty(str2)) {
                str = str + OpenAccountUIConstants.UNDER_LINE + str2;
            }
            return this.flowCtrlMap.get(str);
        }
    }
}
