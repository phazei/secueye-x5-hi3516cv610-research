package com.aliyun.alink.h2.netty;

import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: compiled from: NettyMessageFormatter.java */
/* JADX INFO: loaded from: classes2.dex */
final class f {
    static a a(String str, Object obj) {
        return a(str, new Object[]{obj});
    }

    static a a(String str, Object obj, Object obj2) {
        return a(str, new Object[]{obj, obj2});
    }

    static a a(String str, Object[] objArr) {
        if (objArr != null && objArr.length != 0) {
            int length = objArr.length - 1;
            Object obj = objArr[length];
            Throwable th = obj instanceof Throwable ? (Throwable) obj : null;
            if (str == null) {
                return new a((String) null, th);
            }
            int iIndexOf = str.indexOf("{}");
            if (iIndexOf == -1) {
                return new a(str, th);
            }
            StringBuilder sb = new StringBuilder(str.length() + 50);
            int i = 0;
            int i2 = 0;
            do {
                boolean z = iIndexOf == 0 || str.charAt(iIndexOf + (-1)) != '\\';
                if (z) {
                    sb.append((CharSequence) str, i, iIndexOf);
                } else {
                    sb.append((CharSequence) str, i, iIndexOf - 1);
                    z = iIndexOf >= 2 && str.charAt(iIndexOf + (-2)) == '\\';
                }
                i = iIndexOf + 2;
                if (z) {
                    a(sb, objArr[i2], (Set<Object[]>) null);
                    i2++;
                    if (i2 <= length) {
                        break;
                        break;
                    }
                    break;
                }
                sb.append("{}");
                iIndexOf = str.indexOf("{}", i);
            } while (iIndexOf != -1);
            sb.append((CharSequence) str, i, str.length());
            return new a(sb.toString(), i2 <= length ? th : null);
        }
        return new a(str, (Throwable) null);
    }

    private static void a(StringBuilder sb, Object obj, Set<Object[]> set) {
        if (obj == null) {
            sb.append(TmpConstant.GROUP_ROLE_UNKNOWN);
            return;
        }
        Class<?> cls = obj.getClass();
        if (!cls.isArray()) {
            if (Number.class.isAssignableFrom(cls)) {
                if (cls == Long.class) {
                    sb.append(((Long) obj).longValue());
                    return;
                }
                if (cls != Integer.class && cls != Short.class && cls != Byte.class) {
                    if (cls == Double.class) {
                        sb.append(((Double) obj).doubleValue());
                        return;
                    } else if (cls == Float.class) {
                        sb.append(((Float) obj).floatValue());
                        return;
                    } else {
                        a(sb, obj);
                        return;
                    }
                }
                sb.append(((Number) obj).intValue());
                return;
            }
            a(sb, obj);
            return;
        }
        sb.append('[');
        if (cls == boolean[].class) {
            a(sb, (boolean[]) obj);
        } else if (cls == byte[].class) {
            a(sb, (byte[]) obj);
        } else if (cls == char[].class) {
            a(sb, (char[]) obj);
        } else if (cls == short[].class) {
            a(sb, (short[]) obj);
        } else if (cls == int[].class) {
            a(sb, (int[]) obj);
        } else if (cls == long[].class) {
            a(sb, (long[]) obj);
        } else if (cls == float[].class) {
            a(sb, (float[]) obj);
        } else if (cls == double[].class) {
            a(sb, (double[]) obj);
        } else {
            a(sb, (Object[]) obj, set);
        }
        sb.append(']');
    }

    private static void a(StringBuilder sb, Object obj) {
        try {
            sb.append(obj.toString());
        } catch (Throwable th) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type [" + obj.getClass().getName() + ']');
            th.printStackTrace();
            sb.append("[FAILED toString()]");
        }
    }

    private static void a(StringBuilder sb, Object[] objArr, Set<Object[]> set) {
        if (objArr.length != 0) {
            if (set == null) {
                set = new HashSet<>(objArr.length);
            }
            if (set.add(objArr)) {
                a(sb, objArr[0], set);
                for (int i = 1; i < objArr.length; i++) {
                    sb.append(", ");
                    a(sb, objArr[i], set);
                }
                set.remove(objArr);
                return;
            }
            sb.append("...");
        }
    }

    private static void a(StringBuilder sb, boolean[] zArr) {
        if (zArr.length != 0) {
            sb.append(zArr[0]);
            for (int i = 1; i < zArr.length; i++) {
                sb.append(", ");
                sb.append(zArr[i]);
            }
        }
    }

    private static void a(StringBuilder sb, byte[] bArr) {
        if (bArr.length != 0) {
            sb.append((int) bArr[0]);
            for (int i = 1; i < bArr.length; i++) {
                sb.append(", ");
                sb.append((int) bArr[i]);
            }
        }
    }

    private static void a(StringBuilder sb, char[] cArr) {
        if (cArr.length != 0) {
            sb.append(cArr[0]);
            for (int i = 1; i < cArr.length; i++) {
                sb.append(", ");
                sb.append(cArr[i]);
            }
        }
    }

    private static void a(StringBuilder sb, short[] sArr) {
        if (sArr.length != 0) {
            sb.append((int) sArr[0]);
            for (int i = 1; i < sArr.length; i++) {
                sb.append(", ");
                sb.append((int) sArr[i]);
            }
        }
    }

    private static void a(StringBuilder sb, int[] iArr) {
        if (iArr.length != 0) {
            sb.append(iArr[0]);
            for (int i = 1; i < iArr.length; i++) {
                sb.append(", ");
                sb.append(iArr[i]);
            }
        }
    }

    private static void a(StringBuilder sb, long[] jArr) {
        if (jArr.length != 0) {
            sb.append(jArr[0]);
            for (int i = 1; i < jArr.length; i++) {
                sb.append(", ");
                sb.append(jArr[i]);
            }
        }
    }

    private static void a(StringBuilder sb, float[] fArr) {
        if (fArr.length != 0) {
            sb.append(fArr[0]);
            for (int i = 1; i < fArr.length; i++) {
                sb.append(", ");
                sb.append(fArr[i]);
            }
        }
    }

    private static void a(StringBuilder sb, double[] dArr) {
        if (dArr.length != 0) {
            sb.append(dArr[0]);
            for (int i = 1; i < dArr.length; i++) {
                sb.append(", ");
                sb.append(dArr[i]);
            }
        }
    }
}
