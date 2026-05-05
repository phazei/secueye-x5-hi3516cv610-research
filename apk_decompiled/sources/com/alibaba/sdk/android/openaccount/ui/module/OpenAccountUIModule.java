package com.alibaba.sdk.android.openaccount.ui.module;

import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.pluto.meta.ModuleInfo;
import com.alibaba.sdk.android.pluto.meta.ModuleInfoBuilder;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountUIModule {
    public static ModuleInfo getModuleInfo() {
        ModuleInfoBuilder moduleInfoBuilder = new ModuleInfoBuilder(OpenAccountUIConstants.TAG);
        moduleInfoBuilder.addBeanInfo(OpenAccountUIService.class, OpenAccountUIServiceImpl.class, "init", (Map<String, String>) null);
        HashMap map = new HashMap();
        moduleInfoBuilder.setModuleProperties(map);
        map.put("TEST_QR_CODE_LOGIN_URL", "http://login-openaccount.daily.taobao.net/login/sdkQrcodeShow.htm?appKey=%s");
        map.put("ONLINE_QR_CODE_LOGIN_URL", "https://login-openaccount.taobao.com/login/sdkQrcodeShow.htm?appKey=%s");
        map.put("PRE_QR_CODE_LOGIN_URL", "https://login-openaccount.taobao.com/login/sdkQrcodeShow.htm?appKey=%s");
        map.put("SANDBOX_QR_CODE_LOGIN_URL", "http://login-openaccount.daily.taobao.net/login/sdkQrcodeShow.htm?appKey=%s");
        map.put("TEST_QR_LOGIN_CONFIRM_URL", "http://login-openaccount.daily.taobao.net/login/qrcodeLoginV2.htm?shortURL=%s");
        map.put("SANDBOX_QR_LOGIN_CONFIRM_URL", "http://login-openaccount.daily.taobao.net/login/qrcodeLoginV2.htm?shortURL=%s");
        map.put("ONLINE_QR_LOGIN_CONFIRM_URL", "https://login-openaccount.taobao.com/login/qrcodeLoginV2.htm?shortURL=%s");
        map.put("PRE_QR_LOGIN_CONFIRM_URL", "https://pre-login-openaccount.taobao.com/login/qrcodeLoginV2.htm?shortURL=%s");
        return moduleInfoBuilder.build();
    }
}
