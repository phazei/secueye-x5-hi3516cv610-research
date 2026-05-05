package com.alibaba.sdk.android.openaccount.util.safe;

import com.google.android.exoplayer2.extractor.ts.PsExtractor;

/* JADX INFO: loaded from: classes.dex */
public final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int EIGHTBIT = 8;
    private static final int FOURBYTE = 4;
    private static final int LOOKUPLENGTH = 64;
    private static final char PAD = '=';
    private static final int SIGN = -128;
    private static final int SIXTEENBIT = 16;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final boolean fDebug = false;
    private static final byte[] base64Alphabet = new byte[128];
    private static final char[] lookUpBase64Alphabet = new char[64];

    private static boolean isPad(char c2) {
        return c2 == '=';
    }

    private static boolean isWhiteSpace(char c2) {
        return c2 == ' ' || c2 == '\r' || c2 == '\n' || c2 == '\t';
    }

    static {
        int i;
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < 128; i4++) {
            base64Alphabet[i4] = -1;
        }
        for (int i5 = 90; i5 >= 65; i5--) {
            base64Alphabet[i5] = (byte) (i5 - 65);
        }
        int i6 = 122;
        while (true) {
            i = 26;
            if (i6 < 97) {
                break;
            }
            base64Alphabet[i6] = (byte) ((i6 - 97) + 26);
            i6--;
        }
        int i7 = 57;
        while (true) {
            i2 = 52;
            if (i7 < 48) {
                break;
            }
            base64Alphabet[i7] = (byte) ((i7 - 48) + 52);
            i7--;
        }
        byte[] bArr = base64Alphabet;
        bArr[43] = 62;
        bArr[47] = 63;
        for (int i8 = 0; i8 <= 25; i8++) {
            lookUpBase64Alphabet[i8] = (char) (i8 + 65);
        }
        int i9 = 0;
        while (i <= 51) {
            lookUpBase64Alphabet[i] = (char) (i9 + 97);
            i++;
            i9++;
        }
        while (i2 <= 61) {
            lookUpBase64Alphabet[i2] = (char) (i3 + 48);
            i2++;
            i3++;
        }
        char[] cArr = lookUpBase64Alphabet;
        cArr[62] = '+';
        cArr[63] = '/';
    }

    private static boolean isData(char c2) {
        return c2 < 128 && base64Alphabet[c2] != -1;
    }

    public static String encode(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        int length = bArr.length * 8;
        if (length == 0) {
            return "";
        }
        int i = length % 24;
        int i2 = length / 24;
        char[] cArr = new char[(i != 0 ? i2 + 1 : i2) * 4];
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < i2) {
            int i6 = i4 + 1;
            byte b2 = bArr[i4];
            int i7 = i6 + 1;
            byte b3 = bArr[i6];
            int i8 = i7 + 1;
            byte b4 = bArr[i7];
            byte b5 = (byte) (b3 & 15);
            byte b6 = (byte) (b2 & 3);
            byte b7 = (byte) ((b2 & (-128)) == 0 ? b2 >> 2 : (b2 >> 2) ^ 192);
            byte b8 = (byte) ((b3 & (-128)) == 0 ? b3 >> 4 : (b3 >> 4) ^ PsExtractor.VIDEO_STREAM_MASK);
            int i9 = (b4 & (-128)) == 0 ? b4 >> 6 : (b4 >> 6) ^ 252;
            int i10 = i5 + 1;
            char[] cArr2 = lookUpBase64Alphabet;
            cArr[i5] = cArr2[b7];
            int i11 = i10 + 1;
            cArr[i10] = cArr2[(b6 << 4) | b8];
            int i12 = i11 + 1;
            cArr[i11] = cArr2[(b5 << 2) | ((byte) i9)];
            cArr[i12] = cArr2[b4 & 63];
            i3++;
            i5 = i12 + 1;
            i4 = i8;
        }
        if (i == 8) {
            byte b9 = bArr[i4];
            byte b10 = (byte) (b9 & 3);
            int i13 = (b9 & (-128)) == 0 ? b9 >> 2 : (b9 >> 2) ^ 192;
            int i14 = i5 + 1;
            char[] cArr3 = lookUpBase64Alphabet;
            cArr[i5] = cArr3[(byte) i13];
            int i15 = i14 + 1;
            cArr[i14] = cArr3[b10 << 4];
            cArr[i15] = PAD;
            cArr[i15 + 1] = PAD;
        } else if (i == 16) {
            byte b11 = bArr[i4];
            byte b12 = bArr[i4 + 1];
            byte b13 = (byte) (b12 & 15);
            byte b14 = (byte) (b11 & 3);
            byte b15 = (byte) ((b11 & (-128)) == 0 ? b11 >> 2 : (b11 >> 2) ^ 192);
            int i16 = (b12 & (-128)) == 0 ? b12 >> 4 : (b12 >> 4) ^ PsExtractor.VIDEO_STREAM_MASK;
            int i17 = i5 + 1;
            char[] cArr4 = lookUpBase64Alphabet;
            cArr[i5] = cArr4[b15];
            int i18 = i17 + 1;
            cArr[i17] = cArr4[((byte) i16) | (b14 << 4)];
            cArr[i18] = cArr4[b13 << 2];
            cArr[i18 + 1] = PAD;
        }
        return new String(cArr);
    }

    public static byte[] decode(String str) {
        if (str == null) {
            return null;
        }
        char[] charArray = str.toCharArray();
        int iRemoveWhiteSpace = removeWhiteSpace(charArray);
        if (iRemoveWhiteSpace % 4 != 0) {
            return null;
        }
        int i = iRemoveWhiteSpace / 4;
        if (i == 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[i * 3];
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < i - 1) {
            int i5 = i3 + 1;
            char c2 = charArray[i3];
            if (isData(c2)) {
                int i6 = i5 + 1;
                char c3 = charArray[i5];
                if (isData(c3)) {
                    int i7 = i6 + 1;
                    char c4 = charArray[i6];
                    if (isData(c4)) {
                        int i8 = i7 + 1;
                        char c5 = charArray[i7];
                        if (isData(c5)) {
                            byte[] bArr2 = base64Alphabet;
                            byte b2 = bArr2[c2];
                            byte b3 = bArr2[c3];
                            byte b4 = bArr2[c4];
                            byte b5 = bArr2[c5];
                            int i9 = i4 + 1;
                            bArr[i4] = (byte) ((b2 << 2) | (b3 >> 4));
                            int i10 = i9 + 1;
                            bArr[i9] = (byte) (((b3 & 15) << 4) | ((b4 >> 2) & 15));
                            i4 = i10 + 1;
                            bArr[i10] = (byte) ((b4 << 6) | b5);
                            i2++;
                            i3 = i8;
                        }
                    }
                }
            }
            return null;
        }
        int i11 = i3 + 1;
        char c6 = charArray[i3];
        if (isData(c6)) {
            int i12 = i11 + 1;
            char c7 = charArray[i11];
            if (isData(c7)) {
                byte[] bArr3 = base64Alphabet;
                byte b6 = bArr3[c6];
                byte b7 = bArr3[c7];
                int i13 = i12 + 1;
                char c8 = charArray[i12];
                char c9 = charArray[i13];
                if (!isData(c8) || !isData(c9)) {
                    if (isPad(c8) && isPad(c9)) {
                        if ((b7 & 15) != 0) {
                            return null;
                        }
                        int i14 = i2 * 3;
                        byte[] bArr4 = new byte[i14 + 1];
                        System.arraycopy(bArr, 0, bArr4, 0, i14);
                        bArr4[i4] = (byte) ((b6 << 2) | (b7 >> 4));
                        return bArr4;
                    }
                    if (isPad(c8) || !isPad(c9)) {
                        return null;
                    }
                    byte b8 = base64Alphabet[c8];
                    if ((b8 & 3) != 0) {
                        return null;
                    }
                    int i15 = i2 * 3;
                    byte[] bArr5 = new byte[i15 + 2];
                    System.arraycopy(bArr, 0, bArr5, 0, i15);
                    bArr5[i4] = (byte) ((b6 << 2) | (b7 >> 4));
                    bArr5[i4 + 1] = (byte) (((b8 >> 2) & 15) | ((b7 & 15) << 4));
                    return bArr5;
                }
                byte[] bArr6 = base64Alphabet;
                byte b9 = bArr6[c8];
                byte b10 = bArr6[c9];
                int i16 = i4 + 1;
                bArr[i4] = (byte) ((b6 << 2) | (b7 >> 4));
                bArr[i16] = (byte) (((b7 & 15) << 4) | ((b9 >> 2) & 15));
                bArr[i16 + 1] = (byte) (b10 | (b9 << 6));
                return bArr;
            }
        }
        return null;
    }

    private static int removeWhiteSpace(char[] cArr) {
        if (cArr == null) {
            return 0;
        }
        int length = cArr.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (!isWhiteSpace(cArr[i2])) {
                cArr[i] = cArr[i2];
                i++;
            }
        }
        return i;
    }
}
