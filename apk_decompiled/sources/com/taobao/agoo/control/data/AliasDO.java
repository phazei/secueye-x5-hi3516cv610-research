package com.taobao.agoo.control.data;

import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.JsonUtility;

/* JADX INFO: loaded from: classes3.dex */
public class AliasDO extends BaseDO {
    public static final String INVALID_TOKEN = "deprecated_alias_token_should_be_ignored";
    public static final String JSON_ALIAS_TOKEN_MAP = "aliasTokenMap";
    public static final String JSON_CMD_ADDALIAS = "setAlias";
    public static final String JSON_CMD_LISTALIAS = "getAliasTokenMap";
    public static final String JSON_CMD_REMOVEALIAS = "removeAlias";
    public static final String JSON_CMD_REMOVEALLALIAS = "unbindAllAliasByDeviceId";
    public static final String JSON_CMD_REMOVEALLALIASANDADDALIAS = "resetDeviceAndBindCurrentAlias";
    public static final String JSON_CMD_RESETALIASDEVICEONE2ONE = "resetDeviceAndAliasToSingleBind";
    public static final String JSON_CMD_RESETAlIASANDBINDCURRENT = "resetAliasAndBindCurrentDevice";
    private static final String TAG = "AliasDO";
    public String alias;
    public String appKey;
    public String deviceId;
    public String pushAliasToken;

    @Override // com.taobao.agoo.control.data.BaseDO
    public byte[] buildData() {
        try {
            String string = new JsonUtility.JsonObjectBuilder().put(BaseDO.JSON_CMD, this.cmd).put("appKey", this.appKey).put("deviceId", this.deviceId).put("alias", this.alias).put("pushAliasToken", this.pushAliasToken).build().toString();
            ALog.i(TAG, "buildData", "data", string);
            return string.getBytes("utf-8");
        } catch (Throwable th) {
            ALog.e(TAG, "buildData", th, new Object[0]);
            return null;
        }
    }

    public static byte[] buildRemoveAllDeviceWithThisAliasAndBindCurrentDevice(String str, String str2, String str3) {
        AliasDO aliasDO = new AliasDO();
        aliasDO.appKey = str;
        aliasDO.deviceId = str2;
        aliasDO.alias = str3;
        aliasDO.cmd = JSON_CMD_RESETAlIASANDBINDCURRENT;
        return aliasDO.buildData();
    }

    public static byte[] buildAddAliasToCurrentDevice(String str, String str2, String str3) {
        AliasDO aliasDO = new AliasDO();
        aliasDO.appKey = str;
        aliasDO.deviceId = str2;
        aliasDO.alias = str3;
        aliasDO.cmd = JSON_CMD_ADDALIAS;
        return aliasDO.buildData();
    }

    public static byte[] buildRemoveAliasFromThisDevice(String str, String str2, String str3, String str4) {
        AliasDO aliasDO = new AliasDO();
        aliasDO.appKey = str;
        aliasDO.deviceId = str2;
        aliasDO.alias = str3;
        aliasDO.pushAliasToken = str4;
        aliasDO.cmd = JSON_CMD_REMOVEALIAS;
        return aliasDO.buildData();
    }

    public static byte[] buildRemoveAllAliasFromThisDevice(String str, String str2) {
        AliasDO aliasDO = new AliasDO();
        aliasDO.appKey = str;
        aliasDO.deviceId = str2;
        aliasDO.cmd = JSON_CMD_REMOVEALLALIAS;
        return aliasDO.buildData();
    }

    public static byte[] buildListAliasOnThisDevice(String str, String str2) {
        AliasDO aliasDO = new AliasDO();
        aliasDO.appKey = str;
        aliasDO.deviceId = str2;
        aliasDO.cmd = JSON_CMD_LISTALIAS;
        return aliasDO.buildData();
    }

    public static byte[] buildRemoveAllAliasFromThisDeviceAndBindThisAlias(String str, String str2, String str3) {
        AliasDO aliasDO = new AliasDO();
        aliasDO.appKey = str;
        aliasDO.deviceId = str2;
        aliasDO.alias = str3;
        aliasDO.cmd = JSON_CMD_REMOVEALLALIASANDADDALIAS;
        return aliasDO.buildData();
    }

    public static byte[] buildResetAliasDeviceOne2One(String str, String str2, String str3) {
        AliasDO aliasDO = new AliasDO();
        aliasDO.appKey = str;
        aliasDO.deviceId = str2;
        aliasDO.alias = str3;
        aliasDO.cmd = JSON_CMD_RESETALIASDEVICEONE2ONE;
        return aliasDO.buildData();
    }
}
