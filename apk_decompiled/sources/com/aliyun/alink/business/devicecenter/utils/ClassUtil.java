package com.aliyun.alink.business.devicecenter.utils;

import com.aliyun.alink.business.devicecenter.log.ALog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ClassUtil {
    public static List<Class<?>> getClasses(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            try {
                arrayList.add(Class.forName(str));
            } catch (ClassNotFoundException unused) {
                ALog.w("ClassUtil", "class not found for name: " + str);
            }
        }
        return arrayList;
    }
}
