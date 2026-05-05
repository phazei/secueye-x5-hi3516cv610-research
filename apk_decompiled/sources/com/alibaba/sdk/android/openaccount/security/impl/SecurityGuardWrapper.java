package com.alibaba.sdk.android.openaccount.security.impl;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.OAWSecurityData;
import com.alibaba.sdk.android.openaccount.model.OAWUAData;
import com.alibaba.sdk.android.openaccount.model.ResultCode;
import com.alibaba.sdk.android.openaccount.security.SecRuntimeException;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.dynamicdataencrypt.IDynamicDataEncryptComponent;
import com.alibaba.wireless.security.open.securitybody.ISecurityBodyComponent;
import com.alibaba.wireless.security.open.umid.IUMIDInitListenerEx;
import com.aliyun.alink.linksdk.securesigner.SecurityImpl;
import com.aliyun.alink.linksdk.securesigner.crypto.KeystoreSecureStorage;
import com.aliyun.alink.linksdk.securesigner.crypto.SecureStorageException;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class SecurityGuardWrapper implements SecurityGuardService, EnvironmentChangeListener {
    public static final SecurityGuardWrapper INSTANCE = new SecurityGuardWrapper();
    private static final String TAG = "oa_security";

    @Autowired
    private ConfigService configService;
    private Context context;

    @Autowired
    private UserTrackerService userTrackerService;

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public String getSecurityToken() {
        return "";
    }

    private SecurityGuardWrapper() {
    }

    public ResultCode init(Context context) {
        this.context = context;
        if (Utils.hasSecurityGuardDep()) {
            try {
                if (!this.configService.getBooleanProperty("disableSecurityGuardInit", false)) {
                    SecurityGuardManager.getInitializer().initialize(context);
                    SecurityGuardManager securityGuardManager = SecurityGuardManager.getInstance(context);
                    if (securityGuardManager == null) {
                        return new ResultCode(MessageUtils.createMessage(MessageConstants.SECURITY_GUARD_INIT_EXCEPTION, new Object[0]));
                    }
                    if (ConfigManager.getInstance().getEnvironment().equals(Environment.TEST)) {
                        securityGuardManager.getUMIDComp().initUMID(INSTANCE.getAppKey(), 2, ConfigManager.getInstance().getSecurityImagePostfix(), new IUMIDInitListenerEx() { // from class: com.alibaba.sdk.android.openaccount.security.impl.SecurityGuardWrapper.1
                            public void onUMIDInitFinishedEx(String str, int i) {
                            }
                        });
                    }
                }
                OpenAccountSDK.setProperty(OpenAccountConstants.APP_KEY, INSTANCE.getAppKey());
            } catch (SecRuntimeException e) {
                e.printStackTrace();
                return createResultCode(e.getErrorCode(), e);
            } catch (SecException e2) {
                e2.printStackTrace();
                return createResultCode(e2.getErrorCode(), e2);
            }
        }
        return ResultCode.create(MessageUtils.createMessage(100, new Object[0]));
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
        OpenAccountSDK.setProperty(OpenAccountConstants.APP_KEY, INSTANCE.getAppKey());
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x007e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.alibaba.sdk.android.openaccount.model.ResultCode createResultCode(int r6, java.lang.Exception r7) {
        /*
            r5 = this;
            com.alibaba.sdk.android.openaccount.ConfigManager r0 = com.alibaba.sdk.android.openaccount.ConfigManager.getInstance()
            java.lang.String r0 = r0.getSecurityImagePostfix()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L11
            java.lang.String r0 = ""
            goto L2a
        L11:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "_"
            r0.append(r1)
            com.alibaba.sdk.android.openaccount.ConfigManager r1 = com.alibaba.sdk.android.openaccount.ConfigManager.getInstance()
            java.lang.String r1 = r1.getSecurityImagePostfix()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
        L2a:
            r1 = 106(0x6a, float:1.49E-43)
            r2 = 0
            if (r6 == r1) goto L7e
            r1 = 212(0xd4, float:2.97E-43)
            r3 = 1
            if (r6 == r1) goto L73
            switch(r6) {
                case 102: goto L7e;
                case 103: goto L7e;
                default: goto L37;
            }
        L37:
            switch(r6) {
                case 202: goto L68;
                case 203: goto L5d;
                case 204: goto L73;
                case 205: goto L73;
                default: goto L3a;
            }
        L3a:
            r0 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r1 = new java.lang.Object[r3]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = r7.getMessage()
            r3.append(r4)
            java.lang.String r4 = " sec code "
            r3.append(r4)
            r3.append(r6)
            java.lang.String r6 = r3.toString()
            r1[r2] = r6
            com.alibaba.sdk.android.openaccount.message.Message r6 = com.alibaba.sdk.android.openaccount.message.MessageUtils.createMessage(r0, r1)
            goto L86
        L5d:
            r6 = 702(0x2be, float:9.84E-43)
            java.lang.Object[] r1 = new java.lang.Object[r3]
            r1[r2] = r0
            com.alibaba.sdk.android.openaccount.message.Message r6 = com.alibaba.sdk.android.openaccount.message.MessageUtils.createMessage(r6, r1)
            goto L86
        L68:
            r6 = 701(0x2bd, float:9.82E-43)
            java.lang.Object[] r1 = new java.lang.Object[r3]
            r1[r2] = r0
            com.alibaba.sdk.android.openaccount.message.Message r6 = com.alibaba.sdk.android.openaccount.message.MessageUtils.createMessage(r6, r1)
            goto L86
        L73:
            r6 = 705(0x2c1, float:9.88E-43)
            java.lang.Object[] r1 = new java.lang.Object[r3]
            r1[r2] = r0
            com.alibaba.sdk.android.openaccount.message.Message r6 = com.alibaba.sdk.android.openaccount.message.MessageUtils.createMessage(r6, r1)
            goto L86
        L7e:
            r6 = 704(0x2c0, float:9.87E-43)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            com.alibaba.sdk.android.openaccount.message.Message r6 = com.alibaba.sdk.android.openaccount.message.MessageUtils.createMessage(r6, r0)
        L86:
            java.lang.String r0 = "security"
            com.alibaba.sdk.android.openaccount.trace.AliSDKLogger.log(r0, r6, r7)
            com.alibaba.sdk.android.openaccount.model.ResultCode r6 = com.alibaba.sdk.android.openaccount.model.ResultCode.create(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.openaccount.security.impl.SecurityGuardWrapper.createResultCode(int, java.lang.Exception):com.alibaba.sdk.android.openaccount.model.ResultCode");
    }

    private SecurityGuardManager getSecurityGuardManager() {
        try {
            return SecurityGuardManager.getInstance(this.context);
        } catch (SecException e) {
            e.printStackTrace();
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }

    private boolean isWeakSecurity() {
        return getSecurityGuardManager().getSDKVerison().contains("weak");
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public String getProviderName() {
        return isWeakSecurity() ? "mini" : "full";
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public synchronized String getValueFromDynamicDataStore(String str) {
        if (Utils.hasSecurityGuardDep()) {
            try {
                String string = getSecurityGuardManager().getDynamicDataStoreComp().getString(str);
                String[] strArr = new String[4];
                strArr[0] = "key";
                strArr[1] = str;
                strArr[2] = "ret";
                strArr[3] = string == null ? "NULL" : "N-NULL";
                logSecurityGuardUTMessage(UTConstants.GET_DYNAMIC_DATA_STORE, true, strArr);
                return string;
            } catch (SecException e) {
                logSecurityGuardUTMessage(UTConstants.GET_DYNAMIC_DATA_STORE, false, "key", str, "code", String.valueOf(e.getErrorCode()));
                AliSDKLogger.e(TAG, "Sec Exception, the code = " + e.getErrorCode(), e);
                return null;
            }
        }
        try {
            return KeystoreSecureStorage.getInstance(this.context).get(str);
        } catch (SecureStorageException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    @RequiresApi(api = 19)
    public synchronized byte[] getByteArrayFromDynamicDataStore(String str) {
        if (Utils.hasSecurityGuardDep()) {
            try {
                byte[] byteArray = getSecurityGuardManager().getDynamicDataStoreComp().getByteArray(str);
                String[] strArr = new String[4];
                strArr[0] = "key";
                strArr[1] = str;
                strArr[2] = "ret";
                strArr[3] = byteArray == null ? "NULL" : "N-NULL";
                logSecurityGuardUTMessage(UTConstants.GET_DYNAMIC_DATA_STORE, true, strArr);
                return byteArray;
            } catch (SecException e) {
                e.printStackTrace();
                logSecurityGuardUTMessage(UTConstants.GET_DYNAMIC_DATA_STORE, false, "key", str, "code", String.valueOf(e.getErrorCode()));
                AliSDKLogger.e(TAG, "Sec Exception, the code = " + e.getErrorCode(), e);
                return null;
            }
        }
        try {
            return KeystoreSecureStorage.getInstance(this.context).get(str).getBytes(StandardCharsets.UTF_8);
        } catch (SecureStorageException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public synchronized void removeValueFromDynamicDataStore(String str) {
        if (Utils.hasSecurityGuardDep()) {
            try {
                getSecurityGuardManager().getDynamicDataStoreComp().removeString(str);
                logSecurityGuardUTMessage(UTConstants.REMOVE_DYNAMIC_DATA_STORE, true, "key", str);
            } catch (SecException e) {
                e.printStackTrace();
                logSecurityGuardUTMessage(UTConstants.REMOVE_DYNAMIC_DATA_STORE, false, "key", str, "code", String.valueOf(e.getErrorCode()));
                throw new SecRuntimeException(e.getErrorCode(), e);
            }
        } else {
            try {
                KeystoreSecureStorage.getInstance(this.context).remove(str);
            } catch (SecureStorageException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public String getAppKey() {
        if (!TextUtils.isEmpty(ConfigManager.getInstance().getAppKey())) {
            return ConfigManager.getInstance().getAppKey();
        }
        if (Utils.hasSecurityGuardDep()) {
            try {
                return getSecurityGuardManager().getStaticDataStoreComp().getAppKeyByIndex(ConfigManager.getInstance().getAppKeyIndex(), ConfigManager.getInstance().getSecurityImagePostfix());
            } catch (SecException e) {
                e.printStackTrace();
                logSecurityGuardUTMessage(UTConstants.GET_APPKEY, false, "code", String.valueOf(e.getErrorCode()));
                throw new SecRuntimeException(e.getErrorCode(), e);
            }
        }
        return new SecurityImpl().getAppKey();
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public synchronized String getValueFromStaticDataStore(String str) {
        if (Utils.hasSecurityGuardDep()) {
            try {
                String extraData = getSecurityGuardManager().getStaticDataStoreComp().getExtraData(str, ConfigManager.getInstance().getSecurityImagePostfix());
                String[] strArr = new String[4];
                strArr[0] = "key";
                strArr[1] = str;
                strArr[2] = "ret";
                strArr[3] = extraData == null ? "NULL" : "N-NULL";
                logSecurityGuardUTMessage(UTConstants.GET_STATIC_DATA_STORE, true, strArr);
                return extraData;
            } catch (SecException e) {
                e.printStackTrace();
                logSecurityGuardUTMessage(UTConstants.GET_STATIC_DATA_STORE, false, "key", str, "code", String.valueOf(e.getErrorCode()));
                AliSDKLogger.e(TAG, "Sec Exception, the code = " + e.getErrorCode(), e);
                return null;
            }
        }
        try {
            return KeystoreSecureStorage.getInstance(this.context).get(str);
        } catch (SecureStorageException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public String signRequest(String str, int i) {
        if (Utils.hasSecurityGuardDep()) {
            SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
            securityGuardParamContext.paramMap.put("INPUT", str);
            securityGuardParamContext.appKey = OpenAccountSDK.getProperty(OpenAccountConstants.APP_KEY);
            securityGuardParamContext.requestType = i;
            try {
                return getSecurityGuardManager().getSecureSignatureComp().signRequest(securityGuardParamContext, ConfigManager.getInstance().getSecurityImagePostfix());
            } catch (SecException e) {
                e.printStackTrace();
                String[] strArr = new String[6];
                strArr[0] = "type";
                strArr[1] = String.valueOf(i);
                strArr[2] = "inputStr";
                strArr[3] = str == null ? "NULL" : "N-NULL";
                strArr[4] = "code";
                strArr[5] = String.valueOf(e.getErrorCode());
                logSecurityGuardUTMessage(UTConstants.SIGN_REQUEST, false, strArr);
                throw new SecRuntimeException(e.getErrorCode(), e);
            }
        }
        return new SecurityImpl().sign(str, "");
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public synchronized void putValueInDynamicDataStore(String str, String str2) {
        if (Utils.hasSecurityGuardDep()) {
            try {
                int iPutString = getSecurityGuardManager().getDynamicDataStoreComp().putString(str, str2);
                String[] strArr = new String[6];
                strArr[0] = "key";
                strArr[1] = str;
                strArr[2] = "value";
                strArr[3] = str2 == null ? "NULL" : "N-NULL";
                strArr[4] = "res";
                strArr[5] = String.valueOf(iPutString);
                logSecurityGuardUTMessage(UTConstants.PUT_DYNAMIC_DATA_STORE, true, strArr);
            } catch (SecException e) {
                e.printStackTrace();
                String[] strArr2 = new String[6];
                strArr2[0] = "key";
                strArr2[1] = str;
                strArr2[2] = "value";
                strArr2[3] = str2 == null ? "NULL" : "N-NULL";
                strArr2[4] = "code";
                strArr2[5] = String.valueOf(e.getErrorCode());
                logSecurityGuardUTMessage(UTConstants.PUT_DYNAMIC_DATA_STORE, false, strArr2);
                throw new SecRuntimeException(e.getErrorCode(), e);
            }
        } else {
            try {
                KeystoreSecureStorage.getInstance(this.context).put(str, str2);
            } catch (SecureStorageException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    @RequiresApi(api = 19)
    public synchronized void putValueInDynamicDataStore(String str, byte[] bArr) {
        if (Utils.hasSecurityGuardDep()) {
            try {
                int iPutByteArray = getSecurityGuardManager().getDynamicDataStoreComp().putByteArray(str, bArr);
                String[] strArr = new String[6];
                strArr[0] = "key";
                strArr[1] = str;
                strArr[2] = "value";
                strArr[3] = bArr == null ? "NULL" : "N-NULL";
                strArr[4] = "res";
                strArr[5] = String.valueOf(iPutByteArray);
                logSecurityGuardUTMessage(UTConstants.PUT_DYNAMIC_DATA_STORE, true, strArr);
            } catch (SecException e) {
                e.printStackTrace();
                String[] strArr2 = new String[6];
                strArr2[0] = "key";
                strArr2[1] = str;
                strArr2[2] = "value";
                strArr2[3] = bArr == null ? "NULL" : "N-NULL";
                strArr2[4] = "code";
                strArr2[5] = String.valueOf(e.getErrorCode());
                logSecurityGuardUTMessage(UTConstants.PUT_DYNAMIC_DATA_STORE, false, strArr2);
                throw new SecRuntimeException(e.getErrorCode(), e);
            }
        } else {
            try {
                KeystoreSecureStorage.getInstance(this.context).put(str, new String(bArr, StandardCharsets.UTF_8));
            } catch (SecureStorageException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void logSecurityGuardUTMessage(String str, boolean z, String... strArr) {
        if ("true".equals(OpenAccountSDK.getProperty("disableSecurityGuardUT"))) {
            return;
        }
        try {
            if (this.userTrackerService != null) {
                HashMap map = new HashMap();
                map.put("process", CommonUtils.getCurrentProcessName());
                if (strArr.length > 1) {
                    int length = strArr.length;
                    for (int i = 0; i < length; i += 2) {
                        map.put(strArr[i], strArr[i + 1]);
                    }
                }
                map.put("provider", getProviderName());
                this.userTrackerService.sendCustomHit(str, 0L, z ? "success" : "error", map);
            }
        } catch (Throwable unused) {
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public OAWUAData getWUA() {
        ISecurityBodyComponent iSecurityBodyComponent;
        if (Utils.hasSecurityGuardDep()) {
            try {
                SecurityGuardManager securityGuardManager = SecurityGuardManager.getInstance(new ContextWrapper(OpenAccountSDK.getAndroidContext()));
                if (securityGuardManager == null || (iSecurityBodyComponent = (ISecurityBodyComponent) securityGuardManager.getInterface(ISecurityBodyComponent.class)) == null) {
                    return null;
                }
                String strValueOf = String.valueOf(System.currentTimeMillis());
                String appKey = INSTANCE.getAppKey();
                String securityBodyDataEx = iSecurityBodyComponent.getSecurityBodyDataEx(strValueOf, appKey, (String) null, (HashMap) null, 4, convertEnvToMtop());
                if (TextUtils.isEmpty(securityBodyDataEx)) {
                    securityBodyDataEx = getSecurityBodyData(strValueOf, appKey);
                }
                return new OAWUAData(getAppKey(), strValueOf, securityBodyDataEx);
            } catch (Error e) {
                e.printStackTrace();
                return null;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return null;
    }

    public String getSecurityBodyData(String str, String str2) {
        com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent securityBodyComp;
        try {
            com.taobao.wireless.security.sdk.SecurityGuardManager securityGuardManager = com.taobao.wireless.security.sdk.SecurityGuardManager.getInstance(this.context);
            if (securityGuardManager == null || (securityBodyComp = securityGuardManager.getSecurityBodyComp()) == null) {
                return null;
            }
            return securityBodyComp.getSecurityBodyData(str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int convertEnvToMtop() {
        switch (ConfigManager.getInstance().getEnvironment()) {
            case ONLINE:
            default:
                return 0;
            case PRE:
                return 1;
            case TEST:
                return 2;
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public OAWSecurityData buildWSecurityData() {
        OAWSecurityData oAWSecurityData = new OAWSecurityData();
        OAWUAData wua = getWUA();
        if (wua != null) {
            oAWSecurityData.wua = wua.wua;
            oAWSecurityData.t = wua.t;
        }
        oAWSecurityData.umidToken = getSecurityToken();
        return oAWSecurityData;
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public String encode(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                IDynamicDataEncryptComponent dynamicDataEncryptComp = getSecurityGuardManager().getDynamicDataEncryptComp();
                if (dynamicDataEncryptComp != null) {
                    String strDynamicEncryptDDp = dynamicDataEncryptComp.dynamicEncryptDDp(str);
                    return TextUtils.isEmpty(strDynamicEncryptDDp) ? str : strDynamicEncryptDDp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    @Override // com.alibaba.sdk.android.openaccount.security.SecurityGuardService
    public String decrypt(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                IDynamicDataEncryptComponent dynamicDataEncryptComp = getSecurityGuardManager().getDynamicDataEncryptComp();
                if (dynamicDataEncryptComp != null) {
                    String strDynamicDecrypt = dynamicDataEncryptComp.dynamicDecrypt(str);
                    return TextUtils.isEmpty(strDynamicDecrypt) ? str : strDynamicDecrypt;
                }
            } catch (Exception unused) {
            }
        }
        return str;
    }
}
