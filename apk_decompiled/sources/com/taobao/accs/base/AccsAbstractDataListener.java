package com.taobao.accs.base;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.sdk.android.error.ErrorCode;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.IACCSManager;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.common.Constants;
import com.taobao.accs.ut.monitor.NetPerformanceMonitor;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UTMini;
import com.vivo.push.PushClientConstants;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public abstract class AccsAbstractDataListener implements AccsDataListenerV2 {
    private static final String TAG = "AccsAbstractDataListener";

    @Override // com.taobao.accs.base.AccsDataListener
    public void onAntiBrush(boolean z, TaoBaseService.ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onBind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onConnected(TaoBaseService.ConnectInfo connectInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onDisconnected(TaoBaseService.ConnectInfo connectInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onResponse(String str, String str2, int i, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onSendData(String str, String str2, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onUnbind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListenerV2
    public void onBind(String str, int i, String str2, TaoBaseService.ExtraInfo extraInfo) {
        onBind(str, i, extraInfo);
    }

    @Override // com.taobao.accs.base.AccsDataListenerV2
    public void onUnbind(String str, int i, String str2, TaoBaseService.ExtraInfo extraInfo) {
        onUnbind(str, i, extraInfo);
    }

    @Override // com.taobao.accs.base.AccsDataListenerV2
    public void onResponse(String str, String str2, int i, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        onResponse(str, str2, i, bArr, extraInfo);
    }

    @Override // com.taobao.accs.base.AccsDataListenerV2
    public void onSendData(String str, String str2, int i, String str3, TaoBaseService.ExtraInfo extraInfo) {
        onSendData(str, str2, i, extraInfo);
    }

    public static int onReceiveData(Context context, Intent intent, AccsDataListenerV2 accsDataListenerV2) {
        String stringExtra;
        if (accsDataListenerV2 == null || context == null) {
            ALog.e(TAG, "onReceiveData listener or context null", new Object[0]);
            return 2;
        }
        if (intent != null) {
            try {
                int intExtra = intent.getIntExtra("command", -1);
                ErrorCode errorCode = Constants.getErrorCode(intent);
                String stringExtra2 = intent.getStringExtra(Constants.KEY_USER_ID);
                String stringExtra3 = intent.getStringExtra(Constants.KEY_DATA_ID);
                stringExtra = intent.getStringExtra(Constants.KEY_SERVICE_ID);
                try {
                    if (ALog.isPrintLog(ALog.Level.I)) {
                        ALog.i(TAG, "onReceiveData", Constants.KEY_DATA_ID, stringExtra3, Constants.KEY_SERVICE_ID, stringExtra, "command", Integer.valueOf(intExtra), PushClientConstants.TAG_CLASS_NAME, accsDataListenerV2.getClass().getName());
                    }
                    if (intExtra > 0) {
                        UTMini.getInstance().commitEvent(66001, "MsgToBuss5", "commandId=" + intExtra, "serviceId=" + stringExtra + " dataId=" + stringExtra3, Integer.valueOf(Constants.SDK_VERSION_CODE));
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_TO_BUSS, "3commandId=" + intExtra + "serviceId=" + stringExtra, 0.0d);
                        switch (intExtra) {
                            case 5:
                                accsDataListenerV2.onBind(stringExtra, errorCode.getCodeInt(), getExtraInfo(intent));
                                break;
                            case 6:
                                accsDataListenerV2.onUnbind(stringExtra, errorCode.getCodeInt(), getExtraInfo(intent));
                                break;
                            case 100:
                                String stringExtra4 = intent.getStringExtra(Constants.KEY_DATA_ID);
                                if (TextUtils.equals("res", intent.getStringExtra(Constants.KEY_SEND_TYPE))) {
                                    accsDataListenerV2.onResponse(stringExtra, stringExtra4, errorCode.getCodeInt(), errorCode.getMsg(), intent.getByteArrayExtra("data"), getExtraInfo(intent));
                                } else {
                                    accsDataListenerV2.onSendData(stringExtra, stringExtra4, errorCode.getCodeInt(), errorCode.getMsg(), getExtraInfo(intent));
                                }
                                break;
                            case 101:
                                byte[] byteArrayExtra = intent.getByteArrayExtra("data");
                                boolean booleanExtra = intent.getBooleanExtra(Constants.KEY_NEED_BUSINESS_ACK, false);
                                if (byteArrayExtra != null) {
                                    String stringExtra5 = intent.getStringExtra(Constants.KEY_DATA_ID);
                                    if (ALog.isPrintLog(ALog.Level.D)) {
                                        ALog.d(TAG, "onReceiveData COMMAND_RECEIVE_DATA onData dataId:" + stringExtra5 + " serviceId:" + stringExtra, new Object[0]);
                                    }
                                    TaoBaseService.ExtraInfo extraInfo = getExtraInfo(intent);
                                    if (booleanExtra) {
                                        ALog.i(TAG, "onReceiveData try to send biz ack dataId " + stringExtra5, new Object[0]);
                                        sendBusinessAck(context, intent, stringExtra5, extraInfo.oriExtHeader);
                                    }
                                    NetPerformanceMonitor netPerformanceMonitor = (NetPerformanceMonitor) intent.getSerializableExtra(Constants.KEY_MONIROT);
                                    if (netPerformanceMonitor != null) {
                                        netPerformanceMonitor.onToAccsTime();
                                    }
                                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_TO_BUSS_SUCCESS, "1commandId=101serviceId=" + stringExtra, 0.0d);
                                    accsDataListenerV2.onData(stringExtra, stringExtra2, stringExtra5, byteArrayExtra, extraInfo);
                                } else {
                                    ALog.e(TAG, "onReceiveData COMMAND_RECEIVE_DATA msg null", new Object[0]);
                                    AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, stringExtra, "1", "COMMAND_RECEIVE_DATA msg null");
                                }
                                break;
                            case 103:
                                boolean booleanExtra2 = intent.getBooleanExtra(Constants.KEY_CONNECT_AVAILABLE, false);
                                String stringExtra6 = intent.getStringExtra("host");
                                boolean booleanExtra3 = intent.getBooleanExtra(Constants.KEY_TYPE_INAPP, false);
                                boolean booleanExtra4 = intent.getBooleanExtra(Constants.KEY_CENTER_HOST, false);
                                if (!TextUtils.isEmpty(stringExtra6)) {
                                    if (booleanExtra2) {
                                        accsDataListenerV2.onConnected(new TaoBaseService.ConnectInfo(stringExtra6, booleanExtra3, booleanExtra4));
                                    } else {
                                        accsDataListenerV2.onDisconnected(new TaoBaseService.ConnectInfo(stringExtra6, booleanExtra3, booleanExtra4, errorCode.getCodeInt(), errorCode.getMsg()));
                                    }
                                }
                                break;
                            case 104:
                                boolean booleanExtra5 = intent.getBooleanExtra(Constants.KEY_ANTI_BRUSH_RET, false);
                                ALog.e(TAG, "onReceiveData anti brush result:" + booleanExtra5, new Object[0]);
                                accsDataListenerV2.onAntiBrush(booleanExtra5, null);
                                break;
                            default:
                                ALog.w(TAG, "onReceiveData command not handled " + intExtra, new Object[0]);
                                break;
                        }
                    } else {
                        ALog.w(TAG, "onReceiveData command not handled " + intExtra, new Object[0]);
                    }
                } catch (Exception e) {
                    e = e;
                    AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, stringExtra, "1", "callback error" + e.toString());
                    ALog.e(TAG, "onReceiveData", e, new Object[0]);
                }
            } catch (Exception e2) {
                e = e2;
                stringExtra = "";
            }
        }
        return 2;
    }

    private static Map<TaoBaseService.ExtHeaderType, String> getExtHeader(Map<Integer, String> map) {
        HashMap map2;
        if (map == null) {
            return null;
        }
        try {
            map2 = new HashMap();
        } catch (Exception e) {
            e = e;
            map2 = null;
        }
        try {
            for (TaoBaseService.ExtHeaderType extHeaderType : TaoBaseService.ExtHeaderType.values()) {
                String str = map.get(Integer.valueOf(extHeaderType.ordinal()));
                if (!TextUtils.isEmpty(str)) {
                    map2.put(extHeaderType, str);
                }
            }
        } catch (Exception e2) {
            e = e2;
            ALog.e(TAG, "getExtHeader", e, new Object[0]);
        }
        return map2;
    }

    private static TaoBaseService.ExtraInfo getExtraInfo(Intent intent) {
        TaoBaseService.ExtraInfo extraInfo = new TaoBaseService.ExtraInfo();
        try {
            HashMap map = (HashMap) intent.getSerializableExtra(TaoBaseService.ExtraInfo.EXT_HEADER);
            Map<TaoBaseService.ExtHeaderType, String> extHeader = getExtHeader(map);
            String stringExtra = intent.getStringExtra(Constants.KEY_PACKAGE_NAME);
            String stringExtra2 = intent.getStringExtra("host");
            extraInfo.connType = intent.getIntExtra(Constants.KEY_CONN_TYPE, 0);
            extraInfo.extHeader = extHeader;
            extraInfo.oriExtHeader = map;
            extraInfo.fromPackage = stringExtra;
            extraInfo.fromHost = stringExtra2;
        } catch (Throwable th) {
            ALog.e(TAG, "getExtraInfo", th, new Object[0]);
        }
        return extraInfo;
    }

    private static void sendBusinessAck(Context context, Intent intent, String str, Map<Integer, String> map) {
        try {
            ALog.i(TAG, "sendBusinessAck", Constants.KEY_DATA_ID, str);
            if (intent != null) {
                String stringExtra = intent.getStringExtra("host");
                String stringExtra2 = intent.getStringExtra("source");
                String stringExtra3 = intent.getStringExtra(Constants.KEY_TARGET);
                String stringExtra4 = intent.getStringExtra("appKey");
                String stringExtra5 = intent.getStringExtra(Constants.KEY_CONFIG_TAG);
                short shortExtra = intent.getShortExtra(Constants.KEY_FLAGS, (short) 0);
                IACCSManager accsInstance = ACCSManager.getAccsInstance(context, stringExtra4, stringExtra5);
                if (accsInstance != null) {
                    accsInstance.sendBusinessAck(stringExtra3, stringExtra2, str, shortExtra, stringExtra, map);
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_BUSINESS_ACK_SUCC, "", 0.0d);
                } else {
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_BUSINESS_ACK_FAIL, "no acsmgr", 0.0d);
                }
            }
        } catch (Throwable th) {
            ALog.e(TAG, "sendBusinessAck", th, new Object[0]);
            AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_BUSINESS_ACK_FAIL, th.toString(), 0.0d);
        }
    }
}
