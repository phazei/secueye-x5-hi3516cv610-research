package com.aliyun.alink.linksdk.tmp.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.alcs.lpbs.data.breeze.ComboAuthInfo;
import com.aliyun.alink.linksdk.tmp.data.auth.AccessInfo;
import com.aliyun.alink.linksdk.tmp.data.auth.ServerEncryptInfo;
import com.aliyun.alink.linksdk.tmp.device.panel.data.ProductInfoPayload;
import com.aliyun.alink.linksdk.tmp.utils.SecurityGuardProxy;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class TmpStorage {
    public static final String FLAG_CLOUD = "cloud";
    public static final String FLAG_LOCAL = "local";
    public static final String FLAG_PROVISIONER = "provisioner";
    protected static final String TAG = "[Tmp]TmpStorage";
    protected static final String TMP_ACCESS_TOKEN_IDLIST = "tmp_id_token_list";
    protected static final String TMP_ACCESS_TOKEN_ID_SPLITE = "===";
    protected static final String TMP_STORAGE_ASKEY_PRE = "asKey_pre_";
    protected static final String TMP_STORAGE_ASTOKEN_PRE = "asToken_pre_";
    protected static final String TMP_STORAGE_BKLIST_PRE = "bklist_pre_";
    protected static final String TMP_STORAGE_COMBO_ASKEY_PRE = "asComboKey_pre_";
    protected static final String TMP_STORAGE_COMBO_ASTOKEN_PRE = "asComboToken_pre_";
    protected static final String TMP_STORAGE_DATAFORMAT_PRE = "dataformat_pre_";
    protected static final String TMP_STORAGE_DEVICE_PRE = "devName_pre_";
    protected static final String TMP_STORAGE_DEV_DETAIL_PRE = "dev_detail_pre";
    protected static final String TMP_STORAGE_DNTOMAC_PRE = "translate_dn_to_mac_pre_";
    protected static final String TMP_STORAGE_FILENAME = "tmp_pref";
    public static final String TMP_STORAGE_GROUP_AUTH_KEY_PRE = "group_auth_key_pre";
    public static final String TMP_STORAGE_GROUP_AUTH_TOKEN_PRE = "group_auth_token_pre";
    public static final String TMP_STORAGE_GROUP_LOCAL_GROUP_ID_PRE = "group_local_groupid_pre";
    public static final String TMP_STORAGE_GROUP_LOCAL_GROUP_INFO_PRE = "group_local_groupinfo_pre";
    protected static final String TMP_STORAGE_IOTID_PRE = "iotid_pre_";
    protected static final String TMP_STORAGE_MACTODN_PRE = "translate_mac_to_dn_pre_";
    protected static final String TMP_STORAGE_PIDTOPK_PRE = "translate_pid_to_pk_pre_";
    protected static final String TMP_STORAGE_PREFIX_PRE = "prefix_pre_";
    protected static final String TMP_STORAGE_PRODKEY_PRE = "prodKey_pre_";
    protected static final String TMP_STORAGE_PRODUCTINFO_PRE = "productinfo_pre_";
    protected static final String TMP_STORAGE_PROVISION_ASTOKEN_PRE = "provision_asToken_pre_";
    protected static final String TMP_STORAGE_PROVISION_PREFIX_PRE = "prefix_pre_";
    protected static final String TMP_STORAGE_PROVISION_SECRET_PRE = "secret_pre_";
    public static final String TMP_STORAGE_SCRIPT_DIGESTMETHOD_PRE = "script_digestmethod_pre";
    public static final String TMP_STORAGE_SCRIPT_DIGEST_PRE = "script_digest_pre";
    protected static final String TMP_STORAGE_SCRIPT_PRE = "script_pre";
    protected static final String TMP_STORAGE_SECRET_PRE = "secret_pre_";
    protected static final String TMP_STORAGE_SYNC_ASCODE_PRE = "asSyncCode_pre_";
    protected static final String TMP_STORAGE_SYNC_ASKEY_PRE = "asSyncKey_pre_";
    protected static final String TMP_STORAGE_SYNC_ASTOKEN_PRE = "asSyncToken_pre_";
    protected static final String TMP_STORAGE_TOALIIOTDEVINFO_DEVICENAME_PRE = "translate_to_ali_dev_info_devicenname_pre_";
    protected static final String TMP_STORAGE_TOALIIOTDEVINFO_PRODUCTKEY_PRE = "translate_to_ali_dev_info_productkey_pre_";
    protected static final String TMP_STORAGE_TSL_PRE = "tsl_pre_";
    protected static final String TMP_STORAGE__PROVISION_ASKEY_PRE = "provision_asKey_pre_";
    protected Map<String, String> mAccessTokenList = new ConcurrentHashMap();
    protected Context mContext;
    protected SharedPreferences.Editor mEditor;
    protected SecurityGuardProxy mSecurityProxy;
    protected SharedPreferences mSharedPerfences;

    public static class InstanceHolder {
        protected static TmpStorage mInstance = new TmpStorage();
    }

    public static TmpStorage getInstance() {
        return InstanceHolder.mInstance;
    }

    public void init(Context context) {
        ALog.d(TAG, "init");
        this.mContext = context;
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.storage.TmpStorage.1
            @Override // java.lang.Runnable
            public void run() {
                ALog.d(TmpStorage.TAG, "AsyncTask init");
                TmpStorage tmpStorage = TmpStorage.this;
                tmpStorage.mSharedPerfences = tmpStorage.mContext.getSharedPreferences(TmpStorage.TMP_STORAGE_FILENAME, 0);
                TmpStorage tmpStorage2 = TmpStorage.this;
                tmpStorage2.mEditor = tmpStorage2.mSharedPerfences.edit();
                TmpStorage tmpStorage3 = TmpStorage.this;
                tmpStorage3.mSecurityProxy = new SecurityGuardProxy(tmpStorage3.mContext);
                TmpStorage.this.readAccessTokenIds();
            }
        });
    }

    public void clearAccessTokenCache() {
        ALog.d(TAG, "clearAccessTokenCache");
        Map<String, String> map = this.mAccessTokenList;
        if (map == null || map.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, String>> it = this.mAccessTokenList.entrySet().iterator();
        while (it.hasNext()) {
            saveAccessInfo(it.next().getKey(), null, null, false, "cloud");
        }
        this.mAccessTokenList.clear();
        writeAccessTokenIds();
    }

    public void readAccessTokenIds() {
        String[] strArrSplit;
        SharedPreferences sharedPreferences = this.mSharedPerfences;
        if (sharedPreferences == null) {
            ALog.e(TAG, "readAccessTokenIds SharedPerfences");
            return;
        }
        String string = sharedPreferences.getString(TMP_ACCESS_TOKEN_IDLIST, "");
        ALog.d(TAG, "readAccessTokenIds accessTokenIds:" + string);
        if (TextUtils.isEmpty(string) || (strArrSplit = string.split(TMP_ACCESS_TOKEN_ID_SPLITE)) == null) {
            return;
        }
        for (int i = 0; i < strArrSplit.length; i++) {
            this.mAccessTokenList.put(strArrSplit[i], strArrSplit[i]);
        }
    }

    protected void addAccessTokenId(String str) {
        ALog.d(TAG, "addAccessTokenId id:" + str);
        this.mAccessTokenList.put(str, str);
        writeAccessTokenIds();
    }

    protected void removeAccessTokenId(String str) {
        ALog.d(TAG, "removeAccessTokenId id:" + str);
        this.mAccessTokenList.remove(str);
        writeAccessTokenIds();
    }

    protected void writeAccessTokenIds() {
        if (this.mEditor == null) {
            ALog.e(TAG, "writeAccessTokenIds Editor empty");
            return;
        }
        Map<String, String> map = this.mAccessTokenList;
        if (map != null && !map.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : this.mAccessTokenList.entrySet()) {
                ALog.d(TAG, "writeAccessTokenIds id:" + entry.getValue());
                sb.append(TMP_ACCESS_TOKEN_ID_SPLITE);
                sb.append(entry.getKey());
            }
            this.mEditor.putString(TMP_ACCESS_TOKEN_IDLIST, sb.toString());
            this.mEditor.apply();
            return;
        }
        this.mEditor.remove(TMP_ACCESS_TOKEN_IDLIST);
    }

    public String getIotId(String str, String str2) {
        ALog.d(TAG, "getIotId pk:" + str + " dn:'" + str2);
        if (this.mSharedPerfences == null) {
            ALog.e(TAG, "getIotId SharedPerfences empty");
            return null;
        }
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            ALog.e(TAG, "getIotId pk or dn empty");
            return null;
        }
        return this.mSharedPerfences.getString(TMP_STORAGE_IOTID_PRE + TextHelper.combineStr(str, str2), "");
    }

    public void saveIotId(String str, String str2, String str3) {
        ALog.d(TAG, "saveIotId pk:" + str + " dn:" + str2 + " iotId:" + str3);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            ALog.e(TAG, "saveIotId pk or dn empty");
            return;
        }
        if (this.mEditor == null) {
            ALog.e(TAG, "saveIotId Editor empty");
            return;
        }
        if (TextUtils.isEmpty(str3)) {
            this.mEditor.remove(TMP_STORAGE_IOTID_PRE + TextHelper.combineStr(str, str2));
        } else {
            this.mEditor.putString(TMP_STORAGE_IOTID_PRE + TextHelper.combineStr(str, str2), str3);
        }
        this.mEditor.apply();
    }

    public DeviceInfo getDeviceInfo(String str) {
        ALog.d(TAG, "getDeviceInfo id:" + str);
        if (this.mSharedPerfences == null) {
            ALog.e(TAG, "getDeviceInfo SharedPerfences");
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String string = this.mSharedPerfences.getString(TMP_STORAGE_PRODKEY_PRE + str, "");
        String string2 = this.mSharedPerfences.getString(TMP_STORAGE_DEVICE_PRE + str, "");
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
            ALog.e(TAG, "getDeviceInfo error empty");
            return null;
        }
        return new DeviceInfo(string, string2);
    }

    public void saveDeviceInfo(String str, String str2, String str3) {
        ALog.d(TAG, "saveDeviceInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "saveDeviceInfo id error empty");
            return;
        }
        if (this.mEditor == null) {
            ALog.e(TAG, "saveDeviceInfo Editor empty");
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            this.mEditor.remove(TMP_STORAGE_PRODKEY_PRE + str);
        } else {
            this.mEditor.putString(TMP_STORAGE_PRODKEY_PRE + str, str2);
        }
        if (TextUtils.isEmpty(str3)) {
            this.mEditor.remove(TMP_STORAGE_DEVICE_PRE + str);
        } else {
            this.mEditor.putString(TMP_STORAGE_DEVICE_PRE + str, str3);
        }
        this.mEditor.apply();
    }

    public void saveProductInfo(String str, ProductInfoPayload.ProductInfo productInfo) {
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "saveProductInfo id error empty");
            return;
        }
        if (this.mEditor == null) {
            ALog.e(TAG, "saveProductInfo Editor empty");
            return;
        }
        String jSONString = null;
        try {
            jSONString = JSON.toJSONString(productInfo);
        } catch (Exception e) {
            ALog.e(TAG, "saveProductInfo toJSONString error:" + e.toString());
        }
        ALog.d(TAG, "saveProductInfo id:" + str + " productInfo:" + jSONString);
        if (TextUtils.isEmpty(jSONString)) {
            this.mEditor.remove(TMP_STORAGE_PRODUCTINFO_PRE + str);
        } else {
            this.mEditor.putString(TMP_STORAGE_PRODUCTINFO_PRE + str, jSONString);
        }
        this.mEditor.apply();
    }

    public ProductInfoPayload.ProductInfo getProductInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        SharedPreferences sharedPreferences = this.mSharedPerfences;
        if (sharedPreferences == null) {
            ALog.e(TAG, "getProductInfo SharedPerfences");
            return null;
        }
        String string = sharedPreferences.getString(TMP_STORAGE_PRODUCTINFO_PRE + str, "");
        ALog.d(TAG, "getProductInfo id:" + str + " ProductInfo:" + string);
        try {
            return (ProductInfoPayload.ProductInfo) JSON.parseObject(string, ProductInfoPayload.ProductInfo.class);
        } catch (Exception e) {
            ALog.w(TAG, "getProductInfo error :" + e.toString());
            return null;
        }
    }

    public String getTsl(String str) {
        ALog.d(TAG, "getTsl id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "getTsl id error empty");
            return "";
        }
        SharedPreferences sharedPreferences = this.mSharedPerfences;
        if (sharedPreferences == null) {
            ALog.e(TAG, "getTsl SharedPerfences");
            return null;
        }
        return sharedPreferences.getString(TMP_STORAGE_TSL_PRE + str, "");
    }

    public void saveTsl(String str, String str2) {
        ALog.d(TAG, "saveTsl id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "saveTsl id error empty");
            return;
        }
        if (this.mEditor == null) {
            ALog.e(TAG, "saveTsl Editor empty");
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            this.mEditor.remove(TMP_STORAGE_TSL_PRE + str);
        } else {
            this.mEditor.putString(TMP_STORAGE_TSL_PRE + str, str2);
        }
        this.mEditor.apply();
    }

    public void saveBlackList(String str, String str2) {
        ALog.d(TAG, "saveBlackList id:" + str + " blackList:" + str2);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "saveBlackList error id null");
            return;
        }
        if (this.mEditor == null) {
            ALog.e(TAG, "saveBlackList Editor empty");
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            this.mEditor.remove(TMP_STORAGE_BKLIST_PRE + str);
        } else {
            this.mEditor.putString(TMP_STORAGE_BKLIST_PRE + str, str2);
        }
        this.mEditor.apply();
    }

    public String getBlackList(String str) {
        ALog.d(TAG, "getBlackList id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "getBlackList id error empty");
            return "";
        }
        SharedPreferences sharedPreferences = this.mSharedPerfences;
        if (sharedPreferences == null) {
            ALog.e(TAG, "getBlackList SharedPerfences");
            return null;
        }
        return sharedPreferences.getString(TMP_STORAGE_BKLIST_PRE + str, "");
    }

    public ComboAuthInfo.SyncAccessInfo getSyncAccessInfo(String str) {
        ALog.d(TAG, "getSyncAccessInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "getSyncAccessInfo id error empty");
            return null;
        }
        SecurityGuardProxy securityGuardProxy = this.mSecurityProxy;
        if (securityGuardProxy == null) {
            ALog.e(TAG, "getSyncAccessInfo SecurityProxy empty");
            return null;
        }
        String stringDDpEx = securityGuardProxy.getStringDDpEx(TMP_STORAGE_SYNC_ASKEY_PRE + str);
        String stringDDpEx2 = this.mSecurityProxy.getStringDDpEx(TMP_STORAGE_SYNC_ASTOKEN_PRE + str);
        String stringDDpEx3 = this.mSecurityProxy.getStringDDpEx(TMP_STORAGE_SYNC_ASCODE_PRE + str);
        if (TextUtils.isEmpty(stringDDpEx) || TextUtils.isEmpty(stringDDpEx2) || TextUtils.isEmpty(stringDDpEx3)) {
            ALog.e(TAG, "getSyncAccessInfo accessKey or asToken empty");
            return null;
        }
        return new ComboAuthInfo.SyncAccessInfo(stringDDpEx, stringDDpEx2, stringDDpEx3);
    }

    public void saveSyncAccessInfo(String str, String str2, String str3, String str4) {
        ALog.d(TAG, "saveSyncAccessInfo id:" + str + " asKey:" + str2 + " authCode:" + str4);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "saveSyncAccessInfo id error empty");
            return;
        }
        if (this.mSecurityProxy == null) {
            ALog.e(TAG, "saveSyncAccessInfo SecurityProxy empty");
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_SYNC_ASKEY_PRE + str);
        } else {
            this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_SYNC_ASKEY_PRE + str, str2);
        }
        if (TextUtils.isEmpty(str3)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_SYNC_ASTOKEN_PRE + str);
        } else {
            this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_SYNC_ASTOKEN_PRE + str, str3);
        }
        if (TextUtils.isEmpty(str4)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_SYNC_ASCODE_PRE + str);
            return;
        }
        this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_SYNC_ASCODE_PRE + str, str4);
    }

    public AccessInfo getComboAccessInfo(String str) {
        ALog.d(TAG, "getComboAccessInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "getComboAccessInfo id error empty");
            return null;
        }
        SecurityGuardProxy securityGuardProxy = this.mSecurityProxy;
        if (securityGuardProxy == null) {
            ALog.e(TAG, "getComboAccessInfo SecurityProxy empty");
            return null;
        }
        String stringDDpEx = securityGuardProxy.getStringDDpEx(TMP_STORAGE_COMBO_ASKEY_PRE + str);
        String stringDDpEx2 = this.mSecurityProxy.getStringDDpEx(TMP_STORAGE_COMBO_ASTOKEN_PRE + str);
        if (TextUtils.isEmpty(stringDDpEx) || TextUtils.isEmpty(stringDDpEx2)) {
            ALog.e(TAG, "getComboAccessInfo accessKey or asToken empty");
            return null;
        }
        return new AccessInfo(stringDDpEx, stringDDpEx2);
    }

    public void saveComboAccessInfo(String str, String str2, String str3) {
        ALog.d(TAG, "saveComboAccessInfo id:" + str + " asKey:" + str2);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "saveComboAccessInfo id error empty");
            return;
        }
        if (this.mSecurityProxy == null) {
            ALog.e(TAG, "saveComboAccessInfo SecurityProxy empty");
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_COMBO_ASKEY_PRE + str);
        } else {
            this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_COMBO_ASKEY_PRE + str, str2);
        }
        if (TextUtils.isEmpty(str3)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_COMBO_ASTOKEN_PRE + str);
            return;
        }
        this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_COMBO_ASTOKEN_PRE + str, str3);
    }

    public AccessInfo getAccessInfo(String str, String str2) {
        return getAccessInfoInner(getRealId(str, str2));
    }

    public AccessInfo getAccessInfo(String str) {
        return getAccessInfo(str, "cloud");
    }

    protected AccessInfo getAccessInfoInner(String str) {
        ALog.d(TAG, "getAccessInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "getAccessInfo id error empty");
            return null;
        }
        SecurityGuardProxy securityGuardProxy = this.mSecurityProxy;
        if (securityGuardProxy == null) {
            ALog.e(TAG, "getAccessInfoInner SecurityProxy empty");
            return null;
        }
        String stringDDpEx = securityGuardProxy.getStringDDpEx(TMP_STORAGE_ASKEY_PRE + str);
        String stringDDpEx2 = this.mSecurityProxy.getStringDDpEx(TMP_STORAGE_ASTOKEN_PRE + str);
        if (TextUtils.isEmpty(stringDDpEx) || TextUtils.isEmpty(stringDDpEx2)) {
            ALog.e(TAG, "getAccessInfo accessKey or asToken empty");
            return null;
        }
        return new AccessInfo(stringDDpEx, stringDDpEx2);
    }

    public void saveAccessInfo(String str, String str2, String str3) {
        saveAccessInfo(str, str2, str3, true, "cloud");
    }

    public void saveAccessInfo(String str, String str2, String str3, boolean z, String str4) {
        saveAccessInfoInner(getRealId(str, str4), str2, str3, z);
    }

    protected void saveAccessInfoInner(String str, String str2, String str3, boolean z) {
        ALog.d(TAG, "saveAccessInfoInner id:" + str + " asKey:" + str2 + " updateIds:" + z);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "saveAccessInfoInner id error empty");
            return;
        }
        if (this.mSecurityProxy == null) {
            ALog.e(TAG, "saveAccessInfoInner SecurityProxy empty");
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            if (z) {
                removeAccessTokenId(str);
            }
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_ASKEY_PRE + str);
        } else {
            if (z) {
                addAccessTokenId(str);
            }
            this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_ASKEY_PRE + str, str2);
        }
        if (TextUtils.isEmpty(str3)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_ASTOKEN_PRE + str);
            return;
        }
        this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_ASTOKEN_PRE + str, str3);
    }

    protected String getRealId(String str, String str2) {
        if ("cloud".equals(str2)) {
            return str;
        }
        return str + str2;
    }

    public AccessInfo getProvisionAccessInfo(String str) {
        ALog.d(TAG, "getProvisionAccessInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "getProvisionAccessInfo id error empty");
            return null;
        }
        SecurityGuardProxy securityGuardProxy = this.mSecurityProxy;
        if (securityGuardProxy == null) {
            ALog.e(TAG, "getProvisionAccessInfo SecurityProxy empty");
            return null;
        }
        String stringDDpEx = securityGuardProxy.getStringDDpEx(TMP_STORAGE__PROVISION_ASKEY_PRE + str);
        String stringDDpEx2 = this.mSecurityProxy.getStringDDpEx(TMP_STORAGE_PROVISION_ASTOKEN_PRE + str);
        if (TextUtils.isEmpty(stringDDpEx) || TextUtils.isEmpty(stringDDpEx2)) {
            ALog.e(TAG, "getAccessInfo accessKey or asToken empty");
            return null;
        }
        return new AccessInfo(stringDDpEx, stringDDpEx2);
    }

    public void saveProvisionAccessInfo(String str, String str2, String str3) {
        ALog.d(TAG, "saveProvisionAccessInfo id:" + str + " asKey:" + str2);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "saveProvisionAccessInfo id error empty");
            return;
        }
        if (this.mSecurityProxy == null) {
            ALog.e(TAG, "saveProvisionAccessInfo SecurityProxy empty");
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE__PROVISION_ASKEY_PRE + str);
        } else {
            this.mSecurityProxy.addStringDDpEx(TMP_STORAGE__PROVISION_ASKEY_PRE + str, str2);
        }
        if (TextUtils.isEmpty(str3)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_PROVISION_ASTOKEN_PRE + str);
            return;
        }
        this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_PROVISION_ASTOKEN_PRE + str, str3);
    }

    public ServerEncryptInfo getServerEnptInfo(String str, String str2) {
        return getServerEnptInfo(getRealId(str, str2));
    }

    public ServerEncryptInfo getServerEnptInfo(String str) {
        ALog.d(TAG, "getServerEnptInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.w(TAG, "getServerEnptInfo error id empty");
            return null;
        }
        SecurityGuardProxy securityGuardProxy = this.mSecurityProxy;
        if (securityGuardProxy == null) {
            ALog.e(TAG, "getServerEnptInfo SecurityProxy empty");
            return null;
        }
        String stringDDpEx = securityGuardProxy.getStringDDpEx("prefix_pre_" + str);
        String stringDDpEx2 = this.mSecurityProxy.getStringDDpEx("secret_pre_" + str);
        if (TextUtils.isEmpty(stringDDpEx) || TextUtils.isEmpty(stringDDpEx2)) {
            ALog.w(TAG, "getServerEnptInfo prefix or secret null");
            return null;
        }
        return new ServerEncryptInfo(stringDDpEx, stringDDpEx2);
    }

    public void saveServerEnptInfo(String str, String str2, String str3, String str4) {
        saveServerEnptInfo(getRealId(str, str4), str2, str3);
    }

    public void saveServerEnptInfo(String str, String str2, String str3) {
        ALog.d(TAG, "saveServerEnptInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "saveServerEnptInfo error id null");
            return;
        }
        if (this.mSecurityProxy == null) {
            ALog.e(TAG, "saveServerEnptInfo SecurityProxy empty");
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            this.mSecurityProxy.removeStringDDpEx("prefix_pre_" + str);
        } else {
            this.mSecurityProxy.addStringDDpEx("prefix_pre_" + str, str2);
        }
        if (TextUtils.isEmpty(str3)) {
            this.mSecurityProxy.removeStringDDpEx("secret_pre_" + str);
            return;
        }
        this.mSecurityProxy.addStringDDpEx("secret_pre_" + str, str3);
    }

    public String getDevDetailInfo(String str) {
        ALog.d(TAG, "getDevDetailInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        SharedPreferences sharedPreferences = this.mSharedPerfences;
        if (sharedPreferences == null) {
            ALog.e(TAG, "getDevDetailInfo mSharedPerfences empty");
            return null;
        }
        return sharedPreferences.getString(TMP_STORAGE_DEV_DETAIL_PRE + str, null);
    }

    public boolean saveDevDetailInfo(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        SharedPreferences.Editor editor = this.mEditor;
        if (editor == null) {
            ALog.e(TAG, "saveDevDetailInfo mEditor empty");
            return false;
        }
        editor.putString(TMP_STORAGE_DEV_DETAIL_PRE + str, str2);
        this.mEditor.apply();
        return true;
    }

    public boolean removeDevDetailInfo(String str) {
        ALog.d(TAG, "removeDevDetailInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        SharedPreferences.Editor editor = this.mEditor;
        if (editor == null) {
            ALog.e(TAG, "removeDevDetailInfo mEditor empty");
            return false;
        }
        editor.remove(TMP_STORAGE_DEV_DETAIL_PRE + str);
        this.mEditor.apply();
        return true;
    }

    public boolean saveDigest(String str, String str2) {
        return saveString(str, TMP_STORAGE_SCRIPT_DIGEST_PRE, str2);
    }

    public String getDigest(String str) {
        return getString(str, TMP_STORAGE_SCRIPT_DIGEST_PRE);
    }

    public boolean saveDigestMethod(String str, String str2) {
        return saveString(str, TMP_STORAGE_SCRIPT_DIGESTMETHOD_PRE, str2);
    }

    public String getDigestMethod(String str) {
        return getString(str, TMP_STORAGE_SCRIPT_DIGESTMETHOD_PRE);
    }

    public boolean saveScript(String str, String str2) {
        return saveString(str, TMP_STORAGE_SCRIPT_PRE, str2);
    }

    public String getScript(String str) {
        return getString(str, TMP_STORAGE_SCRIPT_PRE);
    }

    public boolean saveDnToMac(String str, String str2) {
        ALog.d(TAG, "saveDnToMac dn:" + str + " mac:" + str2);
        return saveString(str, TMP_STORAGE_DNTOMAC_PRE, str2);
    }

    public String getMacByDn(String str) {
        String string = getString(str, TMP_STORAGE_DNTOMAC_PRE);
        ALog.d(TAG, "getMacByDn dn:" + str + " mac:" + string);
        return string;
    }

    public boolean saveMacToDn(String str, String str2) {
        ALog.d(TAG, "saveMacToDn dn:" + str2 + " mac:" + str);
        return saveString(str, TMP_STORAGE_MACTODN_PRE, str2);
    }

    public String getDnByMac(String str) {
        String string = getString(str, TMP_STORAGE_MACTODN_PRE);
        ALog.d(TAG, "geDnByMac dn:" + string + " mac:" + str);
        return string;
    }

    public String getPkByPid(String str) {
        String string = getString(str, TMP_STORAGE_PIDTOPK_PRE);
        ALog.d(TAG, "getPkByPid pid:" + str + " pk:" + string);
        return string;
    }

    public boolean savePkByPid(String str, String str2) {
        ALog.d(TAG, "savePkByPid pid:" + str + " pk:" + str2);
        return saveString(str, TMP_STORAGE_PIDTOPK_PRE, str2);
    }

    public boolean clearGroupInfo(String str) {
        removeLocalGroupInfo(str);
        removeGroupAccessInfo(str);
        removeLocalGroupId(str);
        return true;
    }

    public boolean saveLocalGroupId(String str, String str2) {
        return saveString(str, TMP_STORAGE_GROUP_LOCAL_GROUP_ID_PRE, str2);
    }

    public String getLocalGroupId(String str) {
        return getString(str, TMP_STORAGE_GROUP_LOCAL_GROUP_ID_PRE);
    }

    public boolean removeLocalGroupId(String str) {
        return clearString(str, TMP_STORAGE_GROUP_LOCAL_GROUP_ID_PRE);
    }

    public String getLocalGroupInfo(String str) {
        return getString(str, TMP_STORAGE_GROUP_LOCAL_GROUP_INFO_PRE);
    }

    public boolean saveLocalGroupInfo(String str, String str2) {
        return saveString(str, TMP_STORAGE_GROUP_LOCAL_GROUP_INFO_PRE, str2);
    }

    public boolean removeLocalGroupInfo(String str) {
        return clearString(str, TMP_STORAGE_GROUP_LOCAL_GROUP_INFO_PRE);
    }

    public boolean saveGroupAccessInfo(String str, AccessInfo accessInfo) {
        ALog.d(TAG, "saveGroupAccessInfo id:" + str + " accessInfo:" + accessInfo);
        if (TextUtils.isEmpty(str) || accessInfo == null) {
            ALog.e(TAG, "saveProvisionAccessInfo id error empty or accessInfo null");
            return false;
        }
        if (this.mSecurityProxy == null) {
            ALog.e(TAG, "saveGroupAccessInfo SecurityProxy empty");
            return false;
        }
        if (TextUtils.isEmpty(accessInfo.mAccessKey)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_GROUP_AUTH_KEY_PRE + str);
        } else {
            this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_GROUP_AUTH_KEY_PRE + str, accessInfo.mAccessKey);
        }
        if (TextUtils.isEmpty(accessInfo.mAccessToken)) {
            this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_GROUP_AUTH_TOKEN_PRE + str);
            return true;
        }
        this.mSecurityProxy.addStringDDpEx(TMP_STORAGE_GROUP_AUTH_TOKEN_PRE + str, accessInfo.mAccessToken);
        return true;
    }

    public AccessInfo getGroupAccessInfo(String str) {
        ALog.d(TAG, "getGroupAccessInfo groupId:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "getGroupAccessInfo id error empty");
            return null;
        }
        SecurityGuardProxy securityGuardProxy = this.mSecurityProxy;
        if (securityGuardProxy == null) {
            ALog.e(TAG, "saveGroupAccessInfo SecurityProxy empty");
            return null;
        }
        String stringDDpEx = securityGuardProxy.getStringDDpEx(TMP_STORAGE_GROUP_AUTH_KEY_PRE + str);
        String stringDDpEx2 = this.mSecurityProxy.getStringDDpEx(TMP_STORAGE_GROUP_AUTH_TOKEN_PRE + str);
        if (TextUtils.isEmpty(stringDDpEx) || TextUtils.isEmpty(stringDDpEx2)) {
            ALog.e(TAG, "getGroupAccessInfo accessKey or asToken empty");
            return null;
        }
        return new AccessInfo(stringDDpEx, stringDDpEx2);
    }

    public boolean removeGroupAccessInfo(String str) {
        ALog.d(TAG, "removeGroupAccessInfo id:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "removeGroupAccessInfo id error empty or accessInfo null");
            return false;
        }
        SecurityGuardProxy securityGuardProxy = this.mSecurityProxy;
        if (securityGuardProxy == null) {
            ALog.e(TAG, "saveGroupAccessInfo SecurityProxy empty");
            return false;
        }
        securityGuardProxy.removeStringDDpEx(TMP_STORAGE_GROUP_AUTH_KEY_PRE + str);
        this.mSecurityProxy.removeStringDDpEx(TMP_STORAGE_GROUP_AUTH_TOKEN_PRE + str);
        return true;
    }

    public String getString(String str, String str2) {
        ALog.d(TAG, "getString id:" + str + " prefix:" + str2);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        SharedPreferences sharedPreferences = this.mSharedPerfences;
        if (sharedPreferences == null) {
            ALog.e(TAG, "getString mSharedPerfences empty");
            return null;
        }
        return sharedPreferences.getString(str2 + str, null);
    }

    public boolean saveString(String str, String str2, String str3) {
        ALog.d(TAG, "saveString id:" + str + " prefix:" + str2 + " value isempty:" + TextUtils.isEmpty(str3));
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        SharedPreferences.Editor editor = this.mEditor;
        if (editor == null) {
            ALog.e(TAG, "saveString mEditor empty");
            return false;
        }
        editor.putString(str2 + str, str3);
        this.mEditor.apply();
        return true;
    }

    public boolean clearString(String str, String str2) {
        ALog.d(TAG, "clearString id:" + str + " prefix:" + str2);
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        SharedPreferences.Editor editor = this.mEditor;
        if (editor == null) {
            ALog.e(TAG, "saveString mEditor empty");
            return false;
        }
        editor.remove(str2 + str);
        this.mEditor.apply();
        return true;
    }

    public static class DeviceInfo {
        public String mDeviceName;
        public String mProductKey;

        public DeviceInfo(String str, String str2) {
            this.mProductKey = str;
            this.mDeviceName = str2;
        }

        public String getId() {
            return TextHelper.combineStr(this.mProductKey, this.mDeviceName);
        }
    }
}
