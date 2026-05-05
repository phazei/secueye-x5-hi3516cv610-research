package com.alibaba.sdk.android.ams.common.util;

import anet.channel.strategy.dispatch.DispatchConstants;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class d {
    public static Map<String, String> a(Map<String, String> map) {
        HashMap map2 = new HashMap();
        map2.putAll(map);
        com.alibaba.sdk.android.ams.common.b.b bVarA = com.alibaba.sdk.android.ams.common.b.c.a();
        if (Utils.hasSecurityGuardDep()) {
            String strA = bVarA.a("signSeedKey");
            map2.put(DispatchConstants.SIGNTYPE, "2");
            map2.put("sign", bVarA.a(map2, "signSeedKey", strA));
            map2.put("signSeedKey", strA);
        } else {
            map2.put("sign", bVarA.a(map2, "TMP_SEED_KEY"));
        }
        return map2;
    }
}
