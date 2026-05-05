package com.alibaba.sdk.android.logger.a;

import android.util.Log;
import com.alibaba.sdk.android.logger.IObjectLogFormat;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private HashMap<Class, IObjectLogFormat> f2903a = new HashMap<>();

    public String a(Object obj) {
        if (obj == null) {
            return TmpConstant.GROUP_ROLE_UNKNOWN;
        }
        int i = 0;
        if (obj instanceof Object[]) {
            StringBuilder sb = new StringBuilder();
            Object[] objArr = (Object[]) obj;
            sb.append("[");
            while (i < objArr.length) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(a(objArr[i]));
                i++;
            }
            sb.append("]");
            return sb.toString();
        }
        if (obj instanceof short[]) {
            StringBuilder sb2 = new StringBuilder();
            short[] sArr = (short[]) obj;
            sb2.append("[");
            while (i < sArr.length) {
                if (i != 0) {
                    sb2.append(", ");
                }
                sb2.append((int) sArr[i]);
                i++;
            }
            sb2.append("]");
            return sb2.toString();
        }
        if (obj instanceof int[]) {
            StringBuilder sb3 = new StringBuilder();
            int[] iArr = (int[]) obj;
            sb3.append("[");
            while (i < iArr.length) {
                if (i != 0) {
                    sb3.append(", ");
                }
                sb3.append(iArr[i]);
                i++;
            }
            sb3.append("]");
            return sb3.toString();
        }
        if (obj instanceof long[]) {
            StringBuilder sb4 = new StringBuilder();
            long[] jArr = (long[]) obj;
            sb4.append("[");
            while (i < jArr.length) {
                if (i != 0) {
                    sb4.append(", ");
                }
                sb4.append(jArr[i]);
                i++;
            }
            sb4.append("]");
            return sb4.toString();
        }
        if (obj instanceof boolean[]) {
            StringBuilder sb5 = new StringBuilder();
            boolean[] zArr = (boolean[]) obj;
            sb5.append("[");
            while (i < zArr.length) {
                if (i != 0) {
                    sb5.append(", ");
                }
                sb5.append(zArr[i]);
                i++;
            }
            sb5.append("]");
            return sb5.toString();
        }
        if (obj instanceof char[]) {
            StringBuilder sb6 = new StringBuilder();
            char[] cArr = (char[]) obj;
            sb6.append("[");
            while (i < cArr.length) {
                if (i != 0) {
                    sb6.append(", ");
                }
                sb6.append(cArr[i]);
                i++;
            }
            sb6.append("]");
            return sb6.toString();
        }
        if (obj instanceof float[]) {
            StringBuilder sb7 = new StringBuilder();
            float[] fArr = (float[]) obj;
            sb7.append("[");
            while (i < fArr.length) {
                if (i != 0) {
                    sb7.append(", ");
                }
                sb7.append(fArr[i]);
                i++;
            }
            sb7.append("]");
            return sb7.toString();
        }
        if (obj instanceof double[]) {
            StringBuilder sb8 = new StringBuilder();
            double[] dArr = (double[]) obj;
            sb8.append("[");
            while (i < dArr.length) {
                if (i != 0) {
                    sb8.append(", ");
                }
                sb8.append(dArr[i]);
                i++;
            }
            sb8.append("]");
            return sb8.toString();
        }
        if (!(obj instanceof byte[])) {
            if (obj instanceof Throwable) {
                return Log.getStackTraceString((Throwable) obj);
            }
            IObjectLogFormat iObjectLogFormat = this.f2903a.get(obj.getClass());
            return iObjectLogFormat != null ? iObjectLogFormat.format(obj) : obj.toString();
        }
        StringBuilder sb9 = new StringBuilder();
        byte[] bArr = (byte[]) obj;
        sb9.append("[");
        while (i < bArr.length) {
            if (i != 0) {
                sb9.append(", ");
            }
            sb9.append((int) bArr[i]);
            i++;
        }
        sb9.append("]");
        return sb9.toString();
    }

    public <T> void a(Class<T> cls, IObjectLogFormat<T> iObjectLogFormat) {
        this.f2903a.put(cls, iObjectLogFormat);
    }
}
