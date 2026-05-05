package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.taobao.accs.data.Message;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Properties;
import org.apache.commons.codec.language.Soundex;

/* JADX INFO: loaded from: classes.dex */
public class IOUtils {
    public static final char[] ASCII_CHARS;
    public static final char[] CA;
    static final char[] DigitOnes;
    static final char[] DigitTens;
    public static final String FASTJSON_COMPATIBLEWITHFIELDNAME = "fastjson.compatibleWithFieldName";
    public static final String FASTJSON_COMPATIBLEWITHJAVABEAN = "fastjson.compatibleWithJavaBean";
    public static final String FASTJSON_PROPERTIES = "fastjson.properties";
    public static final int[] IA;
    static final char[] digits;
    public static final char[] replaceChars;
    static final int[] sizeTable;
    public static final byte[] specicalFlags_doubleQuotes;
    public static final boolean[] specicalFlags_doubleQuotesFlags;
    public static final byte[] specicalFlags_singleQuotes;
    public static final boolean[] specicalFlags_singleQuotesFlags;
    public static final Properties DEFAULT_PROPERTIES = new Properties();
    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static final boolean[] firstIdentifierFlags = new boolean[256];
    public static final boolean[] identifierFlags = new boolean[256];

    public static int stringSize(long j) {
        long j2 = 10;
        for (int i = 1; i < 19; i++) {
            if (j < j2) {
                return i;
            }
            j2 *= 10;
        }
        return 19;
    }

    static {
        char c2 = 0;
        while (true) {
            boolean[] zArr = firstIdentifierFlags;
            if (c2 >= zArr.length) {
                break;
            }
            if (c2 >= 'A' && c2 <= 'Z') {
                zArr[c2] = true;
            } else if (c2 >= 'a' && c2 <= 'z') {
                firstIdentifierFlags[c2] = true;
            } else if (c2 == '_' || c2 == '$') {
                firstIdentifierFlags[c2] = true;
            }
            c2 = (char) (c2 + 1);
        }
        char c3 = 0;
        while (true) {
            boolean[] zArr2 = identifierFlags;
            if (c3 < zArr2.length) {
                if (c3 >= 'A' && c3 <= 'Z') {
                    zArr2[c3] = true;
                } else if (c3 >= 'a' && c3 <= 'z') {
                    identifierFlags[c3] = true;
                } else if (c3 == '_') {
                    identifierFlags[c3] = true;
                } else if (c3 >= '0' && c3 <= '9') {
                    identifierFlags[c3] = true;
                }
                c3 = (char) (c3 + 1);
            } else {
                try {
                    break;
                } catch (Throwable unused) {
                }
            }
        }
        loadPropertiesFromFile();
        specicalFlags_doubleQuotes = new byte[161];
        specicalFlags_singleQuotes = new byte[161];
        specicalFlags_doubleQuotesFlags = new boolean[161];
        specicalFlags_singleQuotesFlags = new boolean[161];
        replaceChars = new char[93];
        byte[] bArr = specicalFlags_doubleQuotes;
        bArr[0] = 4;
        bArr[1] = 4;
        bArr[2] = 4;
        bArr[3] = 4;
        bArr[4] = 4;
        bArr[5] = 4;
        bArr[6] = 4;
        bArr[7] = 4;
        bArr[8] = 1;
        bArr[9] = 1;
        bArr[10] = 1;
        bArr[11] = 4;
        bArr[12] = 1;
        bArr[13] = 1;
        bArr[34] = 1;
        bArr[92] = 1;
        byte[] bArr2 = specicalFlags_singleQuotes;
        bArr2[0] = 4;
        bArr2[1] = 4;
        bArr2[2] = 4;
        bArr2[3] = 4;
        bArr2[4] = 4;
        bArr2[5] = 4;
        bArr2[6] = 4;
        bArr2[7] = 4;
        bArr2[8] = 1;
        bArr2[9] = 1;
        bArr2[10] = 1;
        bArr2[11] = 4;
        bArr2[12] = 1;
        bArr2[13] = 1;
        bArr2[92] = 1;
        bArr2[39] = 1;
        for (int i = 14; i <= 31; i++) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }
        for (int i2 = 127; i2 < 160; i2++) {
            specicalFlags_doubleQuotes[i2] = 4;
            specicalFlags_singleQuotes[i2] = 4;
        }
        for (int i3 = 0; i3 < 161; i3++) {
            specicalFlags_doubleQuotesFlags[i3] = specicalFlags_doubleQuotes[i3] != 0;
            specicalFlags_singleQuotesFlags[i3] = specicalFlags_singleQuotes[i3] != 0;
        }
        char[] cArr = replaceChars;
        cArr[0] = '0';
        cArr[1] = '1';
        cArr[2] = '2';
        cArr[3] = '3';
        cArr[4] = '4';
        cArr[5] = '5';
        cArr[6] = '6';
        cArr[7] = '7';
        cArr[8] = 'b';
        cArr[9] = 't';
        cArr[10] = 'n';
        cArr[11] = 'v';
        cArr[12] = 'f';
        cArr[13] = 'r';
        cArr[34] = '\"';
        cArr[39] = '\'';
        cArr[47] = '/';
        cArr[92] = '\\';
        ASCII_CHARS = new char[]{'0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6', '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', '0', 'F', '1', '0', '1', '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1', 'A', '1', 'B', '1', 'C', '1', 'D', '1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4', '2', '5', '2', '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', 'D', '2', 'E', '2', 'F'};
        digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        DigitTens = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        DigitOnes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        sizeTable = new int[]{9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};
        CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        IA = new int[256];
        Arrays.fill(IA, -1);
        int length = CA.length;
        for (int i4 = 0; i4 < length; i4++) {
            IA[CA[i4]] = i4;
        }
        IA[61] = 0;
    }

    public static String getStringProperty(String str) {
        String property;
        try {
            property = System.getProperty(str);
        } catch (SecurityException unused) {
            property = null;
        }
        return property == null ? DEFAULT_PROPERTIES.getProperty(str) : property;
    }

    public static void loadPropertiesFromFile() {
        InputStream inputStream = (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: com.alibaba.fastjson.util.IOUtils.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public InputStream run() {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader != null) {
                    return contextClassLoader.getResourceAsStream(IOUtils.FASTJSON_PROPERTIES);
                }
                return ClassLoader.getSystemResourceAsStream(IOUtils.FASTJSON_PROPERTIES);
            }
        });
        if (inputStream != null) {
            try {
                DEFAULT_PROPERTIES.load(inputStream);
                inputStream.close();
            } catch (IOException unused) {
            }
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    public static void getChars(long j, int i, char[] cArr) {
        char c2;
        if (j < 0) {
            c2 = Soundex.SILENT_MARKER;
            j = -j;
        } else {
            c2 = 0;
        }
        while (j > 2147483647L) {
            long j2 = j / 100;
            int i2 = (int) (j - (((j2 << 6) + (j2 << 5)) + (j2 << 2)));
            int i3 = i - 1;
            cArr[i3] = DigitOnes[i2];
            i = i3 - 1;
            cArr[i] = DigitTens[i2];
            j = j2;
        }
        int i4 = (int) j;
        while (i4 >= 65536) {
            int i5 = i4 / 100;
            int i6 = i4 - (((i5 << 6) + (i5 << 5)) + (i5 << 2));
            int i7 = i - 1;
            cArr[i7] = DigitOnes[i6];
            i = i7 - 1;
            cArr[i] = DigitTens[i6];
            i4 = i5;
        }
        while (true) {
            int i8 = (52429 * i4) >>> 19;
            i--;
            cArr[i] = digits[i4 - ((i8 << 3) + (i8 << 1))];
            if (i8 == 0) {
                break;
            } else {
                i4 = i8;
            }
        }
        if (c2 != 0) {
            cArr[i - 1] = c2;
        }
    }

    public static void getChars(int i, int i2, char[] cArr) {
        char c2;
        if (i < 0) {
            c2 = Soundex.SILENT_MARKER;
            i = -i;
        } else {
            c2 = 0;
        }
        while (i >= 65536) {
            int i3 = i / 100;
            int i4 = i - (((i3 << 6) + (i3 << 5)) + (i3 << 2));
            int i5 = i2 - 1;
            cArr[i5] = DigitOnes[i4];
            i2 = i5 - 1;
            cArr[i2] = DigitTens[i4];
            i = i3;
        }
        while (true) {
            int i6 = (52429 * i) >>> 19;
            i2--;
            cArr[i2] = digits[i - ((i6 << 3) + (i6 << 1))];
            if (i6 == 0) {
                break;
            } else {
                i = i6;
            }
        }
        if (c2 != 0) {
            cArr[i2 - 1] = c2;
        }
    }

    public static void getChars(byte b2, int i, char[] cArr) {
        char c2;
        int i2;
        if (b2 < 0) {
            c2 = Soundex.SILENT_MARKER;
            i2 = -b2;
        } else {
            c2 = 0;
            i2 = b2;
        }
        while (true) {
            int i3 = (52429 * i2) >>> 19;
            i--;
            cArr[i] = digits[i2 - ((i3 << 3) + (i3 << 1))];
            if (i3 == 0) {
                break;
            } else {
                i2 = i3;
            }
        }
        if (c2 != 0) {
            cArr[i - 1] = c2;
        }
    }

    public static int stringSize(int i) {
        int i2 = 0;
        while (i > sizeTable[i2]) {
            i2++;
        }
        return i2 + 1;
    }

    public static void decode(CharsetDecoder charsetDecoder, ByteBuffer byteBuffer, CharBuffer charBuffer) {
        try {
            CoderResult coderResultDecode = charsetDecoder.decode(byteBuffer, charBuffer, true);
            if (!coderResultDecode.isUnderflow()) {
                coderResultDecode.throwException();
            }
            CoderResult coderResultFlush = charsetDecoder.flush(charBuffer);
            if (coderResultFlush.isUnderflow()) {
                return;
            }
            coderResultFlush.throwException();
        } catch (CharacterCodingException e) {
            throw new JSONException("utf8 decode error, " + e.getMessage(), e);
        }
    }

    public static boolean firstIdentifier(char c2) {
        boolean[] zArr = firstIdentifierFlags;
        return c2 < zArr.length && zArr[c2];
    }

    public static boolean isIdent(char c2) {
        boolean[] zArr = identifierFlags;
        return c2 < zArr.length && zArr[c2];
    }

    public static byte[] decodeBase64(char[] cArr, int i, int i2) {
        int i3;
        int i4 = 0;
        if (i2 == 0) {
            return new byte[0];
        }
        int i5 = (i + i2) - 1;
        while (i < i5 && IA[cArr[i]] < 0) {
            i++;
        }
        while (i5 > 0 && IA[cArr[i5]] < 0) {
            i5--;
        }
        int i6 = cArr[i5] == '=' ? cArr[i5 + (-1)] == '=' ? 2 : 1 : 0;
        int i7 = (i5 - i) + 1;
        if (i2 > 76) {
            i3 = (cArr[76] == '\r' ? i7 / 78 : 0) << 1;
        } else {
            i3 = 0;
        }
        int i8 = (((i7 - i3) * 6) >> 3) - i6;
        byte[] bArr = new byte[i8];
        int i9 = (i8 / 3) * 3;
        int i10 = i;
        int i11 = 0;
        int i12 = 0;
        while (i11 < i9) {
            int[] iArr = IA;
            int i13 = i10 + 1;
            int i14 = i13 + 1;
            int i15 = (iArr[cArr[i10]] << 18) | (iArr[cArr[i13]] << 12);
            int i16 = i14 + 1;
            int i17 = i15 | (iArr[cArr[i14]] << 6);
            int i18 = i16 + 1;
            int i19 = i17 | iArr[cArr[i16]];
            int i20 = i11 + 1;
            bArr[i11] = (byte) (i19 >> 16);
            int i21 = i20 + 1;
            bArr[i20] = (byte) (i19 >> 8);
            int i22 = i21 + 1;
            bArr[i21] = (byte) i19;
            if (i3 > 0 && (i12 = i12 + 1) == 19) {
                i18 += 2;
                i12 = 0;
            }
            i10 = i18;
            i11 = i22;
        }
        if (i11 < i8) {
            int i23 = 0;
            while (i10 <= i5 - i6) {
                i4 |= IA[cArr[i10]] << (18 - (i23 * 6));
                i23++;
                i10++;
            }
            int i24 = 16;
            while (i11 < i8) {
                bArr[i11] = (byte) (i4 >> i24);
                i24 -= 8;
                i11++;
            }
        }
        return bArr;
    }

    public static byte[] decodeBase64(String str, int i, int i2) {
        int i3;
        int i4 = 0;
        if (i2 == 0) {
            return new byte[0];
        }
        int i5 = (i + i2) - 1;
        while (i < i5 && IA[str.charAt(i)] < 0) {
            i++;
        }
        while (i5 > 0 && IA[str.charAt(i5)] < 0) {
            i5--;
        }
        int i6 = str.charAt(i5) == '=' ? str.charAt(i5 + (-1)) == '=' ? 2 : 1 : 0;
        int i7 = (i5 - i) + 1;
        if (i2 > 76) {
            i3 = (str.charAt(76) == '\r' ? i7 / 78 : 0) << 1;
        } else {
            i3 = 0;
        }
        int i8 = (((i7 - i3) * 6) >> 3) - i6;
        byte[] bArr = new byte[i8];
        int i9 = (i8 / 3) * 3;
        int i10 = i;
        int i11 = 0;
        int i12 = 0;
        while (i11 < i9) {
            int i13 = i10 + 1;
            int i14 = i13 + 1;
            int i15 = (IA[str.charAt(i10)] << 18) | (IA[str.charAt(i13)] << 12);
            int i16 = i14 + 1;
            int i17 = i15 | (IA[str.charAt(i14)] << 6);
            int i18 = i16 + 1;
            int i19 = i17 | IA[str.charAt(i16)];
            int i20 = i11 + 1;
            bArr[i11] = (byte) (i19 >> 16);
            int i21 = i20 + 1;
            bArr[i20] = (byte) (i19 >> 8);
            int i22 = i21 + 1;
            bArr[i21] = (byte) i19;
            if (i3 > 0 && (i12 = i12 + 1) == 19) {
                i18 += 2;
                i12 = 0;
            }
            i10 = i18;
            i11 = i22;
        }
        if (i11 < i8) {
            int i23 = 0;
            while (i10 <= i5 - i6) {
                i4 |= IA[str.charAt(i10)] << (18 - (i23 * 6));
                i23++;
                i10++;
            }
            int i24 = 16;
            while (i11 < i8) {
                bArr[i11] = (byte) (i4 >> i24);
                i24 -= 8;
                i11++;
            }
        }
        return bArr;
    }

    public static byte[] decodeBase64(String str) {
        int i;
        int length = str.length();
        int i2 = 0;
        if (length == 0) {
            return new byte[0];
        }
        int i3 = length - 1;
        int i4 = 0;
        while (i4 < i3 && IA[str.charAt(i4) & 255] < 0) {
            i4++;
        }
        while (i3 > 0 && IA[str.charAt(i3) & 255] < 0) {
            i3--;
        }
        int i5 = str.charAt(i3) == '=' ? str.charAt(i3 + (-1)) == '=' ? 2 : 1 : 0;
        int i6 = (i3 - i4) + 1;
        if (length > 76) {
            i = (str.charAt(76) == '\r' ? i6 / 78 : 0) << 1;
        } else {
            i = 0;
        }
        int i7 = (((i6 - i) * 6) >> 3) - i5;
        byte[] bArr = new byte[i7];
        int i8 = (i7 / 3) * 3;
        int i9 = 0;
        int i10 = i4;
        int i11 = 0;
        while (i11 < i8) {
            int i12 = i10 + 1;
            int i13 = i12 + 1;
            int i14 = (IA[str.charAt(i10)] << 18) | (IA[str.charAt(i12)] << 12);
            int i15 = i13 + 1;
            int i16 = i14 | (IA[str.charAt(i13)] << 6);
            int i17 = i15 + 1;
            int i18 = i16 | IA[str.charAt(i15)];
            int i19 = i11 + 1;
            bArr[i11] = (byte) (i18 >> 16);
            int i20 = i19 + 1;
            bArr[i19] = (byte) (i18 >> 8);
            int i21 = i20 + 1;
            bArr[i20] = (byte) i18;
            if (i > 0 && (i9 = i9 + 1) == 19) {
                i17 += 2;
                i9 = 0;
            }
            i10 = i17;
            i11 = i21;
        }
        if (i11 < i7) {
            int i22 = 0;
            while (i10 <= i3 - i5) {
                i2 |= IA[str.charAt(i10)] << (18 - (i22 * 6));
                i22++;
                i10++;
            }
            int i23 = 16;
            while (i11 < i7) {
                bArr[i11] = (byte) (i2 >> i23);
                i23 -= 8;
                i11++;
            }
        }
        return bArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0089  */
    /* JADX WARN: Type inference failed for: r10v15, types: [int] */
    /* JADX WARN: Type inference failed for: r10v25 */
    /* JADX WARN: Type inference failed for: r10v3, types: [char, int] */
    /* JADX WARN: Type inference failed for: r10v30, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int encodeUTF8(char[] r9, int r10, int r11, byte[] r12) {
        /*
            Method dump skipped, instruction units count: 210
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.IOUtils.encodeUTF8(char[], int, int, byte[]):int");
    }

    public static int decodeUTF8(byte[] bArr, int i, int i2, char[] cArr) {
        int i3 = i + i2;
        int iMin = Math.min(i2, cArr.length);
        int i4 = i;
        int i5 = 0;
        while (i5 < iMin && bArr[i4] >= 0) {
            cArr[i5] = (char) bArr[i4];
            i5++;
            i4++;
        }
        while (i4 < i3) {
            int i6 = i4 + 1;
            byte b2 = bArr[i4];
            if (b2 >= 0) {
                cArr[i5] = (char) b2;
                i4 = i6;
                i5++;
            } else if ((b2 >> 5) != -2 || (b2 & BinaryMemcacheOpcodes.GATQ) == 0) {
                if ((b2 >> 4) == -2) {
                    int i7 = i6 + 1;
                    if (i7 >= i3) {
                        return -1;
                    }
                    byte b3 = bArr[i6];
                    int i8 = i7 + 1;
                    byte b4 = bArr[i7];
                    if ((b2 == -32 && (b3 & 224) == 128) || (b3 & 192) != 128 || (b4 & 192) != 128) {
                        return -1;
                    }
                    char c2 = (char) (((b3 << 6) ^ (b2 << 12)) ^ ((-123008) ^ b4));
                    if (c2 >= 55296 && c2 < 57344) {
                        return -1;
                    }
                    cArr[i5] = c2;
                    i5++;
                    i4 = i8;
                } else {
                    if ((b2 >> 3) != -2 || i6 + 2 >= i3) {
                        return -1;
                    }
                    int i9 = i6 + 1;
                    byte b5 = bArr[i6];
                    int i10 = i9 + 1;
                    byte b6 = bArr[i9];
                    int i11 = i10 + 1;
                    byte b7 = bArr[i10];
                    int i12 = (((b2 << 18) ^ (b5 << 12)) ^ (b6 << 6)) ^ (3678080 ^ b7);
                    if ((b5 & 192) != 128 || (b6 & 192) != 128 || (b7 & 192) != 128 || i12 < 65536 || i12 >= 1114112) {
                        return -1;
                    }
                    int i13 = i5 + 1;
                    cArr[i5] = (char) ((i12 >>> 10) + 55232);
                    i5 = i13 + 1;
                    cArr[i13] = (char) ((i12 & Message.EXT_HEADER_VALUE_MAX_LEN) + 56320);
                    i4 = i11;
                }
            } else {
                if (i6 >= i3) {
                    return -1;
                }
                int i14 = i6 + 1;
                byte b8 = bArr[i6];
                if ((b8 & 192) != 128) {
                    return -1;
                }
                cArr[i5] = (char) ((b8 ^ (b2 << 6)) ^ 3968);
                i4 = i14;
                i5++;
            }
        }
        return i5;
    }

    public static String readAll(Reader reader) {
        StringBuilder sb = new StringBuilder();
        try {
            char[] cArr = new char[2048];
            while (true) {
                int i = reader.read(cArr, 0, cArr.length);
                if (i >= 0) {
                    sb.append(cArr, 0, i);
                } else {
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            throw new JSONException("read string from reader error", e);
        }
    }

    public static boolean isValidJsonpQueryParam(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt != '.' && !isIdent(cCharAt)) {
                return false;
            }
        }
        return true;
    }
}
