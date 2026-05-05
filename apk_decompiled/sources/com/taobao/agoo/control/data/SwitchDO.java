package com.taobao.agoo.control.data;

import android.text.TextUtils;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.JsonUtility;

/* JADX INFO: loaded from: classes3.dex */
public class SwitchDO extends BaseDO {
    public static final String JSON_CMD_DISABLEPUSH = "disablePush";
    public static final String JSON_CMD_ENABLEPUSH = "enablePush";
    private static final String TAG = "SwitchDO";
    public String appKey;
    public String deviceId;
    public String utdid;

    @Override // com.taobao.agoo.control.data.BaseDO
    public byte[] buildData() {
        try {
            JsonUtility.JsonObjectBuilder jsonObjectBuilder = new JsonUtility.JsonObjectBuilder();
            jsonObjectBuilder.put(BaseDO.JSON_CMD, this.cmd).put("appKey", this.appKey);
            if (TextUtils.isEmpty(this.deviceId)) {
                jsonObjectBuilder.put("utdid", this.utdid);
            } else {
                jsonObjectBuilder.put("deviceId", this.deviceId);
            }
            String string = jsonObjectBuilder.build().toString();
            ALog.i(TAG, "buildData", "data", string);
            return string.getBytes("utf-8");
        } catch (Throwable th) {
            ALog.e(TAG, "buildData", th, new Object[0]);
            return null;
        }
    }

    public static byte[] buildSwitchDO(String str, String str2, String str3, boolean z) {
        SwitchDO switchDO = new SwitchDO();
        switchDO.appKey = str;
        switchDO.deviceId = str2;
        switchDO.utdid = str3;
        if (z) {
            switchDO.cmd = JSON_CMD_ENABLEPUSH;
        } else {
            switchDO.cmd = JSON_CMD_DISABLEPUSH;
        }
        return switchDO.buildData();
    }
}
