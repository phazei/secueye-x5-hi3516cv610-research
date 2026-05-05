package com.aliyun.iot.aep.sdk.framework.bundle;

import android.app.Application;
import android.content.res.AssetManager;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.aliyun.iot.aep.sdk.framework.bundle.PageConfigure;
import com.aliyun.iot.aep.sdk.log.ALog;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class BundleManager {
    public static int init(Application application, IBundleRegister iBundleRegister) {
        if (application == null || iBundleRegister == null) {
            return -1;
        }
        ArrayList<String> arrayListA = a(application);
        if (arrayListA == null || arrayListA.isEmpty()) {
            return 0;
        }
        Iterator<String> it = arrayListA.iterator();
        while (it.hasNext()) {
            BundleConfigure bundleConfigureA = a(application, it.next());
            if (bundleConfigureA != null && bundleConfigureA.pages != null && !bundleConfigureA.pages.isEmpty()) {
                Iterator<PageConfigure> it2 = bundleConfigureA.pages.iterator();
                while (it2.hasNext()) {
                    iBundleRegister.registerPage(application, it2.next());
                }
            }
        }
        return 0;
    }

    private static ArrayList<String> a(Application application) {
        AssetManager assets;
        String[] list;
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            assets = application.getAssets();
        } catch (Exception e) {
            ALog.e("BundleManager", "find-bundle-configure failed", e);
        }
        if (assets != null && (list = assets.list("bundle_configs")) != null && list.length > 0) {
            for (String str : list) {
                if (str != null && str.length() > 0 && str.matches("bundle_config_[A-z,0-9]+\\.json")) {
                    ALog.d("BundleManager", "page-file: bundle_configs/" + str);
                    arrayList.add("bundle_configs/" + str);
                }
            }
            return arrayList;
        }
        return arrayList;
    }

    private static BundleConfigure a(Application application, String str) {
        BundleConfigure bundleConfigure;
        String strA;
        JSONObject jSONObject;
        JSONArray jSONArray;
        if (application == null || str == null || str.isEmpty()) {
            return null;
        }
        try {
            strA = a(application.getAssets().open(str));
        } catch (Exception e) {
            e = e;
            bundleConfigure = null;
        }
        if (strA == null || strA.isEmpty() || (jSONArray = (jSONObject = new JSONObject(strA)).getJSONArray("pages")) == null) {
            return null;
        }
        bundleConfigure = new BundleConfigure();
        try {
            bundleConfigure.name = jSONObject.optString("name");
            bundleConfigure.pages = new ArrayList<>();
            for (int length = jSONArray.length() - 1; length >= 0; length--) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(length);
                JSONArray jSONArray2 = jSONObject2 != null ? jSONObject2.getJSONArray("navigationConfigures") : null;
                if (jSONArray2 != null && jSONArray2.length() > 0) {
                    PageConfigure pageConfigure = new PageConfigure();
                    pageConfigure.name = jSONObject2.optString("name");
                    pageConfigure.version = jSONObject2.optString("version");
                    pageConfigure.needLogin = jSONObject2.optBoolean("needLogin");
                    pageConfigure.navigationConfigures = new ArrayList<>();
                    for (int length2 = jSONArray2.length() - 1; length2 >= 0; length2--) {
                        JSONObject jSONObject3 = jSONArray2.getJSONObject(length2);
                        if (jSONObject3 != null) {
                            PageConfigure.NavigationConfigure navigationConfigure = new PageConfigure.NavigationConfigure();
                            navigationConfigure.navigationCode = jSONObject3.optString("navigationCode");
                            navigationConfigure.navigationIntentAction = jSONObject3.optString("navigationIntentAction");
                            navigationConfigure.navigationIntentCategory = jSONObject3.optString("navigationIntentCategory");
                            navigationConfigure.navigationIntentUrl = jSONObject3.optString("navigationIntentUrl");
                            ALog.d("BundleManager", "prepare navigation-configure: " + navigationConfigure.navigationCode + ", " + navigationConfigure.navigationIntentUrl);
                            pageConfigure.navigationConfigures.add(navigationConfigure);
                        }
                    }
                    bundleConfigure.pages.add(pageConfigure);
                }
            }
        } catch (Exception e2) {
            e = e2;
            ALog.e("BundleManager", "prepare-bundle-configure", e);
        }
        return bundleConfigure;
        ALog.e("BundleManager", "prepare-bundle-configure", e);
        return bundleConfigure;
    }

    private static String a(InputStream inputStream) {
        BufferedReader bufferedReader;
        StringBuilder sb;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                sb = new StringBuilder();
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                    sb.append(SdkConstant.CLOUDAPI_LF);
                }
            } catch (Exception e) {
                e = e;
                ALog.e("BundleManager", "read-file", e);
                sb = null;
            }
        } catch (Exception e2) {
            e = e2;
            bufferedReader = null;
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (Exception e3) {
                ALog.e("BundleManager", "read-file", e3);
            }
        }
        if (sb != null) {
            return sb.toString();
        }
        return null;
    }
}
