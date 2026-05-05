package com.alibaba.ailabs.iot.aisbase;

import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.Arrays;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.u, reason: case insensitive filesystem */
/* JADX INFO: compiled from: Objects.java */
/* JADX INFO: loaded from: classes.dex */
public class C0457u {
    public static boolean a(Object obj, Object obj2) {
        return (obj == null || obj2 == null) ? obj == obj2 : ((obj instanceof Object[]) && (obj2 instanceof Object[])) ? Arrays.deepEquals((Object[]) obj, (Object[]) obj2) : ((obj instanceof boolean[]) && (obj2 instanceof boolean[])) ? Arrays.equals((boolean[]) obj, (boolean[]) obj2) : ((obj instanceof byte[]) && (obj2 instanceof byte[])) ? Arrays.equals((byte[]) obj, (byte[]) obj2) : ((obj instanceof char[]) && (obj2 instanceof char[])) ? Arrays.equals((char[]) obj, (char[]) obj2) : ((obj instanceof double[]) && (obj2 instanceof double[])) ? Arrays.equals((double[]) obj, (double[]) obj2) : ((obj instanceof float[]) && (obj2 instanceof float[])) ? Arrays.equals((float[]) obj, (float[]) obj2) : ((obj instanceof int[]) && (obj2 instanceof int[])) ? Arrays.equals((int[]) obj, (int[]) obj2) : ((obj instanceof long[]) && (obj2 instanceof long[])) ? Arrays.equals((long[]) obj, (long[]) obj2) : ((obj instanceof short[]) && (obj2 instanceof short[])) ? Arrays.equals((short[]) obj, (short[]) obj2) : obj.equals(obj2);
    }

    public static boolean b(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    public static int a(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static String a(Object obj) {
        return obj == null ? TmpConstant.GROUP_ROLE_UNKNOWN : obj.toString();
    }
}
