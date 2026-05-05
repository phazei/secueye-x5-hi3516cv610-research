package com.aliyun.alink.business.devicecenter.utils;

import com.alibaba.fastjson.parser.JSONLexer;
import io.netty.util.internal.StringUtil;
import java.io.UnsupportedEncodingException;
import kotlin.text.Typography;
import org.apache.commons.codec.language.Soundex;

/* JADX INFO: loaded from: classes2.dex */
public class DictionaryEncryptionUtils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static char[] f3759a = {0, 'U', '!', 30, 'x', '\t', 'a', 'V', '\f', 'q', 'g', '/', '=', 'K', 1, '+', 'l', 28, Soundex.SILENT_MARKER, '8', 'P', 'X', 'Z', '@', '_', 'F', 27, 'p', 'b', '}', 'h', 'k', '^', '?', 'O', Typography.amp, '\'', StringUtil.CARRIAGE_RETURN, '(', ']', 11, '[', 6, 'e', 'N', 15, 'A', Typography.greater, '\\', 23, 'T', '|', Typography.dollar, '1', 24, 19, 's', 'z', 3, 'I', '2', 'L', '%', 'c', 'w', 'v', 'Q', JSONLexer.EOI, 5, ':', 'i', Typography.less, '4', 'C', '7', 'D', 'f', '\b', 127, 'Y', ' ', 'G', 'r', 22, '.', '5', 'n', '3', 20, 31, 'R', ')', 'E', 'j', StringUtil.COMMA, 14, 21, '0', 'B', '6', 16, '`', 't', 'M', 'd', 'H', 'y', '\n', 2, 4, 'm', 7, ';', '\"', 29, '*', '~', '9', 17, '{', 18, 25, 'o', 'W', '#', 'u', 'J', 'S', 156, 175, 145, 164, Typography.paragraph, 168, Typography.section, 151, 152, 132, Typography.half, 185, 130, Typography.plusMinus, Typography.pound, 190, Typography.cent, 137, 149, 154, 148, 142, Typography.registered, 186, Typography.copyright, 159, 166, Typography.middleDot, 173, 180, 135, 165, Typography.leftGuillemete, 136, 133, 147, 170, 144, 129, Typography.rightGuillemete, 143, 134, 146, Typography.nbsp, 172, 139, 181, 128, Typography.degree, 153, 179, 188, 184, 157, 191, 141, 150, 138, 158, 178, 155, 131, 161, 140, 193, 216, 213, 214, 197, 192, 211, 205, 194, Typography.times, 217, 201, 204, 202, 220, 210, 222, 198, 199, 221, 223, 203, 218, 195, 219, 200, 212, 206, 208, 209, 196, 207, 239, 234, 227, 230, 235, 225, 228, 233, 226, 224, 236, 229, 231, 232, 238, 237, 241, 247, 244, 245, 242, 243, 240, 246, 251, 249, 250, 248, 252, 253, 254, 255};

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static char[] f3760b = {0, 14, 'l', ':', 'm', 'D', '*', 'g', 'E', 5, 'k', '(', '\b', '%', '_', Soundex.SILENT_MARKER, 'd', '~', 'x', '7', 'X', '`', 'S', '1', '6', 'y', 'C', 18, 17, 'r', 3, 'Y', 'P', 2, 'q', '|', Typography.less, Typography.greater, '+', Typography.dollar, Typography.amp, '[', 's', 15, 'V', JSONLexer.EOI, 'T', 11, 'a', '5', '4', 'W', 'H', 'U', 'c', 'J', 19, 'u', 'M', 'p', 'G', '\f', '/', '!', 23, '.', 'b', 'I', 'K', '\\', 25, 'Q', 'i', ';', 'v', StringUtil.CARRIAGE_RETURN, '=', 'o', StringUtil.COMMA, '\"', 20, 'B', 'Z', 127, '2', '\t', 7, '{', 21, 'O', 22, ')', '0', '\'', ' ', 24, 'e', 6, 28, '?', 'h', '#', 'L', '\n', 30, 'F', ']', 31, 16, 'n', '^', 'z', 27, 1, 'R', '8', 'f', '}', 'A', '@', 4, 'j', '9', 'w', '3', 29, 't', 'N', 175, 166, 140, Typography.half, 137, Typography.cent, Typography.copyright, 158, 161, 145, 185, 173, 191, Typography.middleDot, 149, 168, 165, 130, 170, Typography.pound, 148, 146, 184, 135, 136, Typography.plusMinus, 147, 188, 128, 181, 186, 153, Typography.leftGuillemete, 190, 144, 142, 131, 159, 154, 134, 141, 152, 164, Typography.nbsp, 172, 156, 150, 129, Typography.degree, 133, Typography.rightGuillemete, 178, 157, Typography.registered, 132, 155, 180, 139, 151, Typography.section, 179, 138, 143, Typography.paragraph, 197, 192, 200, Typography.times, 222, 196, 209, 210, 217, 203, 205, 213, 204, 199, 219, 223, 220, 221, 207, 198, 218, 194, 195, 201, 193, 202, 214, 216, 206, 211, 208, 212, 233, 229, 232, 226, 230, 235, 227, 236, 237, 231, 225, 228, 234, 239, 238, 224, 246, 240, 244, 245, 242, 243, 247, 241, 251, 249, 250, 248, 252, 253, 254, 255};

    public static byte[] getDecode(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) f3760b[bArr[i] & 255];
        }
        return bArr;
    }

    public static String getDecodeString(String str) {
        byte[] decode;
        if (str == null || (decode = getDecode(AlinkWifiSolutionUtils.hexStringTobytes(str))) == null) {
            return null;
        }
        try {
            return new String(decode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(decode);
        }
    }

    public static byte[] getEncode(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) f3759a[bArr[i] & 255];
        }
        return bArr;
    }
}
