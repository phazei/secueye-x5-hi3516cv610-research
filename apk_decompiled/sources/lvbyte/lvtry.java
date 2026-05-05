package lvbyte;

/* JADX INFO: loaded from: classes4.dex */
public class lvtry {
    public static lvnew.lvdo lvdo(int i, int i2, int i3, int i4, int i5) {
        float f = i;
        float f2 = i2;
        float f3 = i3;
        float f4 = i4;
        if (i5 == 1) {
            return new lvnew.lvdo(0, 0, i, i2);
        }
        if (i4 * i > i3 * i2) {
            float f5 = f3 * (f2 / f4);
            return new lvnew.lvdo((int) ((f - f5) / 2.0f), 0, (int) f5, i2);
        }
        float f6 = f4 * (f / f3);
        return new lvnew.lvdo(0, (int) ((f2 - f6) / 2.0f), i, (int) f6);
    }

    public static byte[] lvdo(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[bArr.length];
        int i3 = i * i2;
        int i4 = (i3 / 4) + i3;
        System.arraycopy(bArr, 0, bArr2, 0, i3);
        int i5 = i3;
        int i6 = i4;
        while (i3 < i4) {
            bArr2[i5] = bArr[i6];
            bArr2[i5 + 1] = bArr[i3];
            i3++;
            i6++;
            i5 += 2;
        }
        return bArr2;
    }
}
