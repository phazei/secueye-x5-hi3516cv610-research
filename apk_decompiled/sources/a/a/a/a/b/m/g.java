package a.a.a.a.b.m;

/* JADX INFO: compiled from: NumberUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class g {
    public static byte[] a(int i) {
        byte[] bArr = {(byte) ((i >> 8) % 256), (byte) (i % 256), (byte) (i % 256), (byte) (i % 256)};
        int i2 = i >> 8;
        int i3 = i2 >> 8;
        return bArr;
    }

    public static int a(String str) {
        if (str.length() <= 0) {
            return 0;
        }
        int i = 0;
        for (char c2 : str.toCharArray()) {
            i = (i * 31) + c2;
        }
        return i;
    }
}
