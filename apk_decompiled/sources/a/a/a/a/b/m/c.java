package a.a.a.a.b.m;

/* JADX INFO: compiled from: FastProvisionUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class c {
    public static void a(String str, byte[] bArr) {
        if (bArr != null) {
            StringBuilder sb = new StringBuilder();
            for (byte b2 : bArr) {
                sb.append(b2 & 255);
                sb.append(" ");
            }
            a.c(str, sb.toString());
        }
    }

    public static byte[] b(String str) {
        String[] strArrSplit = str.split(":");
        if (strArrSplit.length != 6) {
            return null;
        }
        byte[] bArr = new byte[strArrSplit.length];
        for (int i = 0; i < strArrSplit.length; i++) {
            try {
                bArr[i] = (byte) (Integer.parseInt(strArrSplit[i], 16) & 255);
            } catch (NumberFormatException unused) {
                return null;
            }
        }
        return bArr;
    }

    public static byte[] a(String str) {
        byte[] bArr = new byte[2];
        byte[] bArrB = b(str);
        if (bArrB == null) {
            a.b("FastProvisionUtil", "mac address is not assigned");
        } else {
            bArr[0] = bArrB[4];
            bArr[1] = bArrB[5];
        }
        return bArr;
    }
}
