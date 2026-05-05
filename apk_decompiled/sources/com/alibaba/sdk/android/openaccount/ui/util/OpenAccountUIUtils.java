package com.alibaba.sdk.android.openaccount.ui.util;

import android.graphics.Typeface;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.widget.InputBoxWithHistory;
import com.alibaba.sdk.android.openaccount.util.JSONUtils;
import com.alibaba.sdk.android.pluto.Pluto;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountUIUtils {
    private static Typeface typeface;

    public static Typeface getDefaultFont() {
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(OpenAccountSDK.getAndroidContext().getAssets(), OpenAccountUIConstants.TTF_FILE);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return typeface;
    }

    public static String getQrFromParameter(Map<String, String> map) {
        if (map == null) {
            return "bcqrlogin";
        }
        String str = map.get("from");
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String str2 = map.get("domain");
        if (TextUtils.isEmpty(str2)) {
            return "bcqrlogin";
        }
        return "bcqrlogin_" + str2;
    }

    public static boolean updateHistoryAccounts(String str) {
        SecurityGuardService securityGuardService;
        if (str == null || (securityGuardService = (SecurityGuardService) Pluto.DEFAULT_INSTANCE.getBean(SecurityGuardService.class)) == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList(InputBoxWithHistory.MAX_LOGINID_HISTORY);
        try {
            String valueFromDynamicDataStore = securityGuardService.getValueFromDynamicDataStore(InputBoxWithHistory.historySavedKey);
            if (valueFromDynamicDataStore != null && valueFromDynamicDataStore.length() > 0) {
                JSONArray jSONArray = new JSONArray(valueFromDynamicDataStore);
                for (int i = 0; i < jSONArray.length(); i++) {
                    if (!TextUtils.isEmpty(jSONArray.optString(i))) {
                        arrayList.add(jSONArray.getString(i));
                    }
                }
            }
        } catch (Exception e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "loadSavedInputHistory error:" + e.getMessage(), e);
        }
        Iterator it = arrayList.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (str.equals(it.next())) {
                it.remove();
                z = true;
            }
        }
        if (arrayList.size() >= InputBoxWithHistory.MAX_LOGINID_HISTORY) {
            for (int size = arrayList.size() - 1; size >= InputBoxWithHistory.MAX_LOGINID_HISTORY - 1; size--) {
                arrayList.remove(size);
            }
        }
        arrayList.add(0, str);
        securityGuardService.putValueInDynamicDataStore(InputBoxWithHistory.historySavedKey, JSONUtils.toJsonArray(arrayList).toString());
        return z;
    }
}
