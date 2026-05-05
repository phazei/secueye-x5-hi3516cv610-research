package com.alibaba.fastjson.util;

import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.taobao.accs.data.Message;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/* JADX INFO: loaded from: classes.dex */
public class UTF8Decoder extends CharsetDecoder {
    private static final Charset charset = Charset.forName("UTF-8");

    private static boolean isMalformed2(int i, int i2) {
        return (i & 30) == 0 || (i2 & 192) != 128;
    }

    private static boolean isMalformed3(int i, int i2, int i3) {
        return ((i != -32 || (i2 & 224) != 128) && (i2 & 192) == 128 && (i3 & 192) == 128) ? false : true;
    }

    private static boolean isMalformed4(int i, int i2, int i3) {
        return ((i & 192) == 128 && (i2 & 192) == 128 && (i3 & 192) == 128) ? false : true;
    }

    private static boolean isNotContinuation(int i) {
        return (i & 192) != 128;
    }

    public UTF8Decoder() {
        super(charset, 1.0f, 1.0f);
    }

    private static CoderResult lookupN(ByteBuffer byteBuffer, int i) {
        for (int i2 = 1; i2 < i; i2++) {
            if (isNotContinuation(byteBuffer.get())) {
                return CoderResult.malformedForLength(i2);
            }
        }
        return CoderResult.malformedForLength(i);
    }

    public static CoderResult malformedN(ByteBuffer byteBuffer, int i) {
        switch (i) {
            case 1:
                byte b2 = byteBuffer.get();
                if ((b2 >> 2) == -2) {
                    return byteBuffer.remaining() < 4 ? CoderResult.UNDERFLOW : lookupN(byteBuffer, 5);
                }
                if ((b2 >> 1) == -2) {
                    if (byteBuffer.remaining() < 5) {
                        return CoderResult.UNDERFLOW;
                    }
                    return lookupN(byteBuffer, 6);
                }
                return CoderResult.malformedForLength(1);
            case 2:
                return CoderResult.malformedForLength(1);
            case 3:
                byte b3 = byteBuffer.get();
                byte b4 = byteBuffer.get();
                return CoderResult.malformedForLength(((b3 == -32 && (b4 & 224) == 128) || isNotContinuation(b4)) ? 1 : 2);
            case 4:
                int i2 = byteBuffer.get() & 255;
                int i3 = byteBuffer.get() & 255;
                if (i2 > 244 || ((i2 == 240 && (i3 < 144 || i3 > 191)) || ((i2 == 244 && (i3 & PsExtractor.VIDEO_STREAM_MASK) != 128) || isNotContinuation(i3)))) {
                    return CoderResult.malformedForLength(1);
                }
                return isNotContinuation(byteBuffer.get()) ? CoderResult.malformedForLength(2) : CoderResult.malformedForLength(3);
            default:
                throw new IllegalStateException();
        }
    }

    private static CoderResult malformed(ByteBuffer byteBuffer, int i, CharBuffer charBuffer, int i2, int i3) {
        byteBuffer.position(i - byteBuffer.arrayOffset());
        CoderResult coderResultMalformedN = malformedN(byteBuffer, i3);
        byteBuffer.position(i);
        charBuffer.position(i2);
        return coderResultMalformedN;
    }

    private static CoderResult xflow(Buffer buffer, int i, int i2, Buffer buffer2, int i3, int i4) {
        buffer.position(i);
        buffer2.position(i3);
        return (i4 == 0 || i2 - i < i4) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
    }

    private CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
        byte[] bArrArray = byteBuffer.array();
        int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
        int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
        char[] cArrArray = charBuffer.array();
        int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
        int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
        int iMin = Math.min(iArrayOffset2 - iArrayOffset, iArrayOffset4 - iArrayOffset3) + iArrayOffset3;
        while (iArrayOffset3 < iMin && bArrArray[iArrayOffset] >= 0) {
            cArrArray[iArrayOffset3] = (char) bArrArray[iArrayOffset];
            iArrayOffset3++;
            iArrayOffset++;
        }
        int i = iArrayOffset;
        int i2 = iArrayOffset3;
        while (i < iArrayOffset2) {
            byte b2 = bArrArray[i];
            if (b2 >= 0) {
                if (i2 >= iArrayOffset4) {
                    return xflow(byteBuffer, i, iArrayOffset2, charBuffer, i2, 1);
                }
                cArrArray[i2] = (char) b2;
                i++;
                i2++;
            } else if ((b2 >> 5) == -2) {
                if (iArrayOffset2 - i < 2 || i2 >= iArrayOffset4) {
                    return xflow(byteBuffer, i, iArrayOffset2, charBuffer, i2, 2);
                }
                byte b3 = bArrArray[i + 1];
                if (isMalformed2(b2, b3)) {
                    return malformed(byteBuffer, i, charBuffer, i2, 2);
                }
                cArrArray[i2] = (char) (((b2 << 6) ^ b3) ^ 3968);
                i += 2;
                i2++;
            } else if ((b2 >> 4) == -2) {
                if (iArrayOffset2 - i < 3 || i2 >= iArrayOffset4) {
                    return xflow(byteBuffer, i, iArrayOffset2, charBuffer, i2, 3);
                }
                byte b4 = bArrArray[i + 1];
                byte b5 = bArrArray[i + 2];
                if (isMalformed3(b2, b4, b5)) {
                    return malformed(byteBuffer, i, charBuffer, i2, 3);
                }
                cArrArray[i2] = (char) ((((b2 << 12) ^ (b4 << 6)) ^ b5) ^ 8064);
                i += 3;
                i2++;
            } else {
                if ((b2 >> 3) != -2) {
                    return malformed(byteBuffer, i, charBuffer, i2, 1);
                }
                if (iArrayOffset2 - i < 4 || iArrayOffset4 - i2 < 2) {
                    return xflow(byteBuffer, i, iArrayOffset2, charBuffer, i2, 4);
                }
                byte b6 = bArrArray[i + 1];
                byte b7 = bArrArray[i + 2];
                byte b8 = bArrArray[i + 3];
                int i3 = ((b2 & 7) << 18) | ((b6 & 63) << 12) | ((b7 & 63) << 6) | (b8 & 63);
                if (isMalformed4(b6, b7, b8) || i3 < 65536 || i3 > 1114111) {
                    return malformed(byteBuffer, i, charBuffer, i2, 4);
                }
                int i4 = i2 + 1;
                int i5 = i3 - 65536;
                cArrArray[i2] = (char) (((i5 >> 10) & Message.EXT_HEADER_VALUE_MAX_LEN) | 55296);
                cArrArray[i4] = (char) ((i5 & Message.EXT_HEADER_VALUE_MAX_LEN) | 56320);
                i += 4;
                i2 = i4 + 1;
            }
        }
        return xflow(byteBuffer, i, iArrayOffset2, charBuffer, i2, 0);
    }

    @Override // java.nio.charset.CharsetDecoder
    protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
        return decodeArrayLoop(byteBuffer, charBuffer);
    }
}
