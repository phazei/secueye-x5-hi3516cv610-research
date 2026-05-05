package com.alibaba.sdk.android.openaccount.security;

import com.alibaba.sdk.android.openaccount.model.OAWSecurityData;
import com.alibaba.sdk.android.openaccount.model.OAWUAData;

/* JADX INFO: loaded from: classes.dex */
public interface SecurityGuardService {
    OAWSecurityData buildWSecurityData();

    String decrypt(String str);

    String encode(String str);

    String getAppKey();

    byte[] getByteArrayFromDynamicDataStore(String str);

    String getProviderName();

    String getSecurityToken();

    String getValueFromDynamicDataStore(String str);

    String getValueFromStaticDataStore(String str);

    OAWUAData getWUA();

    void putValueInDynamicDataStore(String str, String str2);

    void putValueInDynamicDataStore(String str, byte[] bArr);

    void removeValueFromDynamicDataStore(String str);

    String signRequest(String str, int i);
}
