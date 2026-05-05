package com.alibaba.ailabs.tg.utils;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.lang.Character;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class StringUtils {
    public static int lengthOf(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int i = 0;
        for (char c2 : str.toCharArray()) {
            i = isChinese(c2) ? i + 2 : i + 1;
        }
        return i;
    }

    public static boolean isChinese(char c2) {
        Character.UnicodeBlock unicodeBlockOf = Character.UnicodeBlock.of(c2);
        return unicodeBlockOf == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || unicodeBlockOf == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || unicodeBlockOf == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || unicodeBlockOf == Character.UnicodeBlock.GENERAL_PUNCTUATION || unicodeBlockOf == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || unicodeBlockOf == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean isChinese(String str) {
        return Pattern.compile("^[一-龥]+$").matcher(str).matches();
    }

    public static boolean isMobile(String str) {
        return !TextUtils.isEmpty(str) && Pattern.compile("[0-9]*").matcher(str).matches() && lengthOf(str) == 11 && str.startsWith("1");
    }

    public static String quotes(String str) {
        return TextUtils.isEmpty(str) ? str : String.format("“%1$2s”", str);
    }

    public static String checkNoNull(String str) {
        return (str == null || TmpConstant.GROUP_ROLE_UNKNOWN.equalsIgnoreCase(str)) ? "" : str;
    }

    public static int compareVersion(String str, String str2) throws Exception {
        if (str == null || str2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        String[] strArrSplit = str.split("\\.");
        String[] strArrSplit2 = str2.split("\\.");
        int iMin = Math.min(strArrSplit.length, strArrSplit2.length);
        int length = 0;
        for (int i = 0; i < iMin; i++) {
            length = strArrSplit[i].length() - strArrSplit2[i].length();
            if (length != 0 || (length = strArrSplit[i].compareTo(strArrSplit2[i])) != 0) {
                break;
            }
        }
        return length != 0 ? length : strArrSplit.length - strArrSplit2.length;
    }

    public static boolean equalsIgnoreCase(String str, String str2) {
        if (str == null) {
            return str2 == null;
        }
        return str.equalsIgnoreCase(str2);
    }

    public static boolean equals(CharSequence charSequence, CharSequence charSequence2) {
        int length;
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null || (length = charSequence.length()) != charSequence2.length()) {
            return false;
        }
        if ((charSequence instanceof String) && (charSequence2 instanceof String)) {
            return charSequence.equals(charSequence2);
        }
        for (int i = 0; i < length; i++) {
            if (charSequence.charAt(i) != charSequence2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSpace(String str) {
        if (str == null) {
            return true;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static String reverse(String str) {
        int length = length(str);
        if (length <= 1) {
            return str;
        }
        int i = length >> 1;
        char[] charArray = str.toCharArray();
        for (int i2 = 0; i2 < i; i2++) {
            char c2 = charArray[i2];
            int i3 = (length - i2) - 1;
            charArray[i2] = charArray[i3];
            charArray[i3] = c2;
        }
        return new String(charArray);
    }

    public static int length(CharSequence charSequence) {
        if (charSequence == null) {
            return 0;
        }
        return charSequence.length();
    }
}
