package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
public final class JSONScanner extends JSONLexerBase {
    private final int len;
    private final String text;

    static boolean checkDate(char c2, char c3, char c4, char c5, char c6, char c7, int i, int i2) {
        if (c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9' || c5 < '0' || c5 > '9') {
            return false;
        }
        if (c6 == '0') {
            if (c7 < '1' || c7 > '9') {
                return false;
            }
        } else {
            if (c6 != '1') {
                return false;
            }
            if (c7 != '0' && c7 != '1' && c7 != '2') {
                return false;
            }
        }
        if (i == 48) {
            return i2 >= 49 && i2 <= 57;
        }
        if (i == 49 || i == 50) {
            return i2 >= 48 && i2 <= 57;
        }
        if (i == 51) {
            return i2 == 48 || i2 == 49;
        }
        return false;
    }

    private boolean checkTime(char c2, char c3, char c4, char c5, char c6, char c7) {
        if (c2 == '0') {
            if (c3 < '0' || c3 > '9') {
                return false;
            }
        } else if (c2 == '1') {
            if (c3 < '0' || c3 > '9') {
                return false;
            }
        } else if (c2 != '2' || c3 < '0' || c3 > '4') {
            return false;
        }
        if (c4 < '0' || c4 > '5') {
            if (c4 != '6' || c5 != '0') {
                return false;
            }
        } else if (c5 < '0' || c5 > '9') {
            return false;
        }
        return (c6 < '0' || c6 > '5') ? c6 == '6' && c7 == '0' : c7 >= '0' && c7 <= '9';
    }

    public JSONScanner(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String str, int i) {
        super(i);
        this.text = str;
        this.len = this.text.length();
        this.bp = -1;
        next();
        if (this.ch == 65279) {
            next();
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final char charAt(int i) {
        return i >= this.len ? JSONLexer.EOI : this.text.charAt(i);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final char next() {
        int i = this.bp + 1;
        this.bp = i;
        char cCharAt = i >= this.len ? JSONLexer.EOI : this.text.charAt(i);
        this.ch = cCharAt;
        return cCharAt;
    }

    public JSONScanner(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] cArr, int i, int i2) {
        this(new String(cArr, 0, i), i2);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void copyTo(int i, int i2, char[] cArr) {
        this.text.getChars(i, i2 + i, cArr, 0);
    }

    static boolean charArrayCompare(String str, int i, char[] cArr) {
        int length = cArr.length;
        if (length + i > str.length()) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (cArr[i2] != str.charAt(i + i2)) {
                return false;
            }
        }
        return true;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final boolean charArrayCompare(char[] cArr) {
        return charArrayCompare(this.text, this.bp, cArr);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final int indexOf(char c2, int i) {
        return this.text.indexOf(c2, i);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String addSymbol(int i, int i2, int i3, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.text, i, i2, i3);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public byte[] bytesValue() {
        if (this.token == 26) {
            int i = this.np + 1;
            int i2 = this.sp;
            if (i2 % 2 != 0) {
                throw new JSONException("illegal state. " + i2);
            }
            byte[] bArr = new byte[i2 / 2];
            for (int i3 = 0; i3 < bArr.length; i3++) {
                int i4 = (i3 * 2) + i;
                char cCharAt = this.text.charAt(i4);
                char cCharAt2 = this.text.charAt(i4 + 1);
                char c2 = '0';
                int i5 = cCharAt - (cCharAt <= '9' ? '0' : '7');
                if (cCharAt2 > '9') {
                    c2 = '7';
                }
                bArr[i3] = (byte) ((i5 << 4) | (cCharAt2 - c2));
            }
            return bArr;
        }
        if (!this.hasSpecial) {
            return IOUtils.decodeBase64(this.text, this.np + 1, this.sp);
        }
        return IOUtils.decodeBase64(new String(this.sbuf, 0, this.sp));
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final String stringVal() {
        if (!this.hasSpecial) {
            return subString(this.np + 1, this.sp);
        }
        return new String(this.sbuf, 0, this.sp);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String subString(int i, int i2) {
        if (ASMUtils.IS_ANDROID) {
            if (i2 < this.sbuf.length) {
                this.text.getChars(i, i + i2, this.sbuf, 0);
                return new String(this.sbuf, 0, i2);
            }
            char[] cArr = new char[i2];
            this.text.getChars(i, i2 + i, cArr, 0);
            return new String(cArr);
        }
        return this.text.substring(i, i2 + i);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final char[] sub_chars(int i, int i2) {
        if (ASMUtils.IS_ANDROID && i2 < this.sbuf.length) {
            this.text.getChars(i, i2 + i, this.sbuf, 0);
            return this.sbuf;
        }
        char[] cArr = new char[i2];
        this.text.getChars(i, i2 + i, cArr, 0);
        return cArr;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final String numberString() {
        char cCharAt = charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (cCharAt == 'L' || cCharAt == 'S' || cCharAt == 'B' || cCharAt == 'F' || cCharAt == 'D') {
            i--;
        }
        return subString(this.np, i);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final BigDecimal decimalValue() {
        char cCharAt = charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (cCharAt == 'L' || cCharAt == 'S' || cCharAt == 'B' || cCharAt == 'F' || cCharAt == 'D') {
            i--;
        }
        if (i > 65535) {
            throw new JSONException("decimal overflow");
        }
        int i2 = this.np;
        if (i < this.sbuf.length) {
            this.text.getChars(i2, i2 + i, this.sbuf, 0);
            return new BigDecimal(this.sbuf, 0, i, MathContext.UNLIMITED);
        }
        char[] cArr = new char[i];
        this.text.getChars(i2, i + i2, cArr, 0);
        return new BigDecimal(cArr, 0, cArr.length, MathContext.UNLIMITED);
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean z) {
        return scanISO8601DateIfMatch(z, this.len - this.bp);
    }

    private boolean scanISO8601DateIfMatch(boolean z, int i) {
        char c2;
        boolean z2;
        char c3;
        char cCharAt;
        char c4;
        char c5;
        char c6;
        int i2;
        int i3;
        int i4;
        int i5;
        char c7;
        char cCharAt2;
        char cCharAt3;
        char c8;
        char c9;
        char c10;
        char c11;
        char c12;
        char c13;
        int i6;
        char c14;
        int i7;
        int i8;
        char cCharAt4;
        char cCharAt5;
        int i9;
        char c15;
        char c16;
        char cCharAt6;
        int i10;
        int i11;
        char cCharAt7;
        char cCharAt8;
        char cCharAt9;
        if (i < 8) {
            return false;
        }
        char cCharAt10 = charAt(this.bp);
        char cCharAt11 = charAt(this.bp + 1);
        char cCharAt12 = charAt(this.bp + 2);
        char cCharAt13 = charAt(this.bp + 3);
        char cCharAt14 = charAt(this.bp + 4);
        char c17 = 5;
        char cCharAt15 = charAt(this.bp + 5);
        char cCharAt16 = charAt(this.bp + 6);
        char cCharAt17 = charAt(this.bp + 7);
        if (!z) {
            if (i > 13) {
                char cCharAt18 = charAt((this.bp + i) - 1);
                char cCharAt19 = charAt((this.bp + i) - 2);
                if (cCharAt10 == '/' && cCharAt11 == 'D' && cCharAt12 == 'a' && cCharAt13 == 't' && cCharAt14 == 'e' && cCharAt15 == '(' && cCharAt18 == '/' && cCharAt19 == ')') {
                    int i12 = -1;
                    for (int i13 = 6; i13 < i; i13++) {
                        char cCharAt20 = charAt(this.bp + i13);
                        if (cCharAt20 != '+') {
                            if (cCharAt20 < '0' || cCharAt20 > '9') {
                                break;
                            }
                        } else {
                            i12 = i13;
                        }
                    }
                    if (i12 == -1) {
                        return false;
                    }
                    int i14 = this.bp + 6;
                    long j = Long.parseLong(subString(i14, (this.bp + i12) - i14));
                    this.calendar = Calendar.getInstance(this.timeZone, this.locale);
                    this.calendar.setTimeInMillis(j);
                    this.token = 5;
                    return true;
                }
                c17 = 5;
            } else {
                c17 = 5;
            }
        }
        if (i == 8 || i == 14) {
            c2 = cCharAt11;
            z2 = false;
            c3 = '9';
        } else if (i == 16 && ((cCharAt9 = charAt(this.bp + 10)) == 'T' || cCharAt9 == ' ')) {
            c2 = cCharAt11;
            z2 = false;
            c3 = '9';
        } else {
            if (i != 17 || charAt(this.bp + 6) == '-') {
                if (i < 9) {
                    return false;
                }
                char cCharAt21 = charAt(this.bp + 8);
                char cCharAt22 = charAt(this.bp + 9);
                if ((cCharAt14 == '-' && cCharAt17 == '-') || (cCharAt14 == '/' && cCharAt17 == '/')) {
                    if (cCharAt22 == ' ') {
                        c11 = cCharAt15;
                        c12 = cCharAt16;
                        c13 = cCharAt21;
                        i6 = 9;
                        c14 = '0';
                        cCharAt21 = cCharAt12;
                    } else {
                        c12 = cCharAt16;
                        c14 = cCharAt21;
                        c13 = cCharAt22;
                        i6 = 10;
                        cCharAt21 = cCharAt12;
                        c11 = cCharAt15;
                    }
                } else if (cCharAt14 == '-' && cCharAt16 == '-') {
                    if (cCharAt21 == ' ') {
                        cCharAt21 = cCharAt12;
                        c12 = cCharAt15;
                        c13 = cCharAt17;
                        i6 = 8;
                        c11 = '0';
                        c14 = '0';
                    } else {
                        c12 = cCharAt15;
                        c14 = cCharAt17;
                        c13 = cCharAt21;
                        i6 = 9;
                        c11 = '0';
                        cCharAt21 = cCharAt12;
                    }
                } else if ((cCharAt12 == '.' && cCharAt15 == '.') || (cCharAt12 == '-' && cCharAt15 == '-')) {
                    c12 = cCharAt14;
                    c14 = cCharAt10;
                    c13 = cCharAt11;
                    cCharAt10 = cCharAt16;
                    cCharAt11 = cCharAt17;
                    i6 = 10;
                    cCharAt13 = cCharAt22;
                    c11 = cCharAt13;
                } else if (cCharAt21 == 'T') {
                    cCharAt21 = cCharAt12;
                    c11 = cCharAt14;
                    c12 = cCharAt15;
                    c14 = cCharAt16;
                    c13 = cCharAt17;
                    i6 = 8;
                } else {
                    if (cCharAt14 != 24180 && cCharAt14 != 45380) {
                        return false;
                    }
                    if (cCharAt17 != 26376 && cCharAt17 != 50900) {
                        if (cCharAt16 != 26376 && cCharAt16 != 50900) {
                            return false;
                        }
                        if (cCharAt21 == 26085 || cCharAt21 == 51068) {
                            cCharAt21 = cCharAt12;
                            c12 = cCharAt15;
                            c13 = cCharAt17;
                            i6 = 10;
                            c11 = '0';
                            c14 = '0';
                        } else {
                            if (cCharAt22 != 26085 && cCharAt22 != 51068) {
                                return false;
                            }
                            c12 = cCharAt15;
                            c14 = cCharAt17;
                            c13 = cCharAt21;
                            i6 = 10;
                            c11 = '0';
                            cCharAt21 = cCharAt12;
                        }
                    } else if (cCharAt22 == 26085 || cCharAt22 == 51068) {
                        c11 = cCharAt15;
                        c12 = cCharAt16;
                        c13 = cCharAt21;
                        i6 = 10;
                        c14 = '0';
                        cCharAt21 = cCharAt12;
                    } else {
                        if (charAt(this.bp + 10) != 26085 && charAt(this.bp + 10) != 51068) {
                            return false;
                        }
                        i6 = 11;
                        c12 = cCharAt16;
                        c14 = cCharAt21;
                        c13 = cCharAt22;
                        cCharAt21 = cCharAt12;
                        c11 = cCharAt15;
                    }
                }
                if (!checkDate(cCharAt10, cCharAt11, cCharAt21, cCharAt13, c11, c12, c14, c13)) {
                    return false;
                }
                int i15 = i6;
                setCalendar(cCharAt10, cCharAt11, cCharAt21, cCharAt13, c11, c12, c14, c13);
                char cCharAt23 = charAt(this.bp + i15);
                if (cCharAt23 == 'T' && i == 16 && i15 == 8 && charAt(this.bp + 15) == 'Z') {
                    char cCharAt24 = charAt(this.bp + i15 + 1);
                    char cCharAt25 = charAt(this.bp + i15 + 2);
                    char cCharAt26 = charAt(this.bp + i15 + 3);
                    char cCharAt27 = charAt(this.bp + i15 + 4);
                    char cCharAt28 = charAt(this.bp + i15 + 5);
                    char cCharAt29 = charAt(this.bp + i15 + 6);
                    if (!checkTime(cCharAt24, cCharAt25, cCharAt26, cCharAt27, cCharAt28, cCharAt29)) {
                        return false;
                    }
                    setTime(cCharAt24, cCharAt25, cCharAt26, cCharAt27, cCharAt28, cCharAt29);
                    this.calendar.set(14, 0);
                    if (this.calendar.getTimeZone().getRawOffset() != 0) {
                        String[] availableIDs = TimeZone.getAvailableIDs(0);
                        if (availableIDs.length > 0) {
                            this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
                        }
                    }
                    this.token = 5;
                    return true;
                }
                if (cCharAt23 != 'T' && (cCharAt23 != ' ' || z)) {
                    if (cCharAt23 == '\"' || cCharAt23 == 26 || cCharAt23 == 26085 || cCharAt23 == 51068) {
                        this.calendar.set(11, 0);
                        this.calendar.set(12, 0);
                        this.calendar.set(13, 0);
                        this.calendar.set(14, 0);
                        int i16 = this.bp + i15;
                        this.bp = i16;
                        this.ch = charAt(i16);
                        this.token = 5;
                        return true;
                    }
                    if ((cCharAt23 != '+' && cCharAt23 != '-') || this.len != i15 + 6 || charAt(this.bp + i15 + 3) != ':' || charAt(this.bp + i15 + 4) != '0' || charAt(this.bp + i15 + 5) != '0') {
                        return false;
                    }
                    setTime('0', '0', '0', '0', '0', '0');
                    this.calendar.set(14, 0);
                    setTimeZone(cCharAt23, charAt(this.bp + i15 + 1), charAt(this.bp + i15 + 2));
                    return true;
                }
                if (i < i15 + 9 || charAt(this.bp + i15 + 3) != ':' || charAt(this.bp + i15 + 6) != ':') {
                    return false;
                }
                char cCharAt30 = charAt(this.bp + i15 + 1);
                char cCharAt31 = charAt(this.bp + i15 + 2);
                char cCharAt32 = charAt(this.bp + i15 + 4);
                char cCharAt33 = charAt(this.bp + i15 + 5);
                char cCharAt34 = charAt(this.bp + i15 + 7);
                char cCharAt35 = charAt(this.bp + i15 + 8);
                if (!checkTime(cCharAt30, cCharAt31, cCharAt32, cCharAt33, cCharAt34, cCharAt35)) {
                    return false;
                }
                setTime(cCharAt30, cCharAt31, cCharAt32, cCharAt33, cCharAt34, cCharAt35);
                int i17 = -1;
                if (charAt(this.bp + i15 + 9) == '.') {
                    int i18 = i15 + 11;
                    if (i < i18 || (cCharAt6 = charAt(this.bp + i15 + 10)) < '0' || cCharAt6 > '9') {
                        return false;
                    }
                    int i19 = cCharAt6 - '0';
                    if (i <= i18 || (cCharAt8 = charAt(this.bp + i15 + 11)) < '0' || cCharAt8 > '9') {
                        i10 = i19;
                        i11 = 1;
                    } else {
                        i10 = (i19 * 10) + (cCharAt8 - '0');
                        i11 = 2;
                    }
                    if (i11 != 2 || (cCharAt7 = charAt(this.bp + i15 + 12)) < '0' || cCharAt7 > '9') {
                        int i20 = i10;
                        i17 = i11;
                        i7 = i20;
                    } else {
                        i7 = (i10 * 10) + (cCharAt7 - '0');
                        i17 = 3;
                    }
                } else {
                    i7 = 0;
                }
                this.calendar.set(14, i7);
                char cCharAt36 = charAt(this.bp + i15 + 10 + i17);
                if (cCharAt36 == ' ') {
                    int i21 = i17 + 1;
                    i8 = i21;
                    cCharAt4 = charAt(this.bp + i15 + 10 + i21);
                } else {
                    i8 = i17;
                    cCharAt4 = cCharAt36;
                }
                if (cCharAt4 == '+' || cCharAt4 == '-') {
                    char cCharAt37 = charAt(this.bp + i15 + 10 + i8 + 1);
                    if (cCharAt37 < '0' || cCharAt37 > '1' || (cCharAt5 = charAt(this.bp + i15 + 10 + i8 + 2)) < '0' || cCharAt5 > '9') {
                        return false;
                    }
                    char cCharAt38 = charAt(this.bp + i15 + 10 + i8 + 3);
                    if (cCharAt38 == ':') {
                        char cCharAt39 = charAt(this.bp + i15 + 10 + i8 + 4);
                        char cCharAt40 = charAt(this.bp + i15 + 10 + i8 + 5);
                        if (cCharAt39 == '4' && cCharAt40 == '5') {
                            if (cCharAt37 != '1' || (cCharAt5 != '2' && cCharAt5 != '3')) {
                                if (cCharAt37 != '0') {
                                    return false;
                                }
                                if (cCharAt5 != '5' && cCharAt5 != '8') {
                                    return false;
                                }
                            }
                        } else if ((cCharAt39 != '0' && cCharAt39 != '3') || cCharAt40 != '0') {
                            return false;
                        }
                        c16 = cCharAt40;
                        i9 = 6;
                        c15 = cCharAt39;
                    } else if (cCharAt38 == '0') {
                        char cCharAt41 = charAt(this.bp + i15 + 10 + i8 + 4);
                        if (cCharAt41 != '0' && cCharAt41 != '3') {
                            return false;
                        }
                        c15 = cCharAt41;
                        i9 = 5;
                        c16 = '0';
                    } else if (cCharAt38 == '3' && charAt(this.bp + i15 + 10 + i8 + 4) == '0') {
                        c15 = '3';
                        i9 = 5;
                        c16 = '0';
                    } else if (cCharAt38 == '4' && charAt(this.bp + i15 + 10 + i8 + 4) == '5') {
                        c16 = '5';
                        i9 = 5;
                        c15 = '4';
                    } else {
                        i9 = 3;
                        c15 = '0';
                        c16 = '0';
                    }
                    setTimeZone(cCharAt4, cCharAt37, cCharAt5, c15, c16);
                } else if (cCharAt4 == 'Z') {
                    if (this.calendar.getTimeZone().getRawOffset() != 0) {
                        String[] availableIDs2 = TimeZone.getAvailableIDs(0);
                        if (availableIDs2.length > 0) {
                            this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs2[0]));
                        }
                    }
                    i9 = 1;
                } else {
                    i9 = 0;
                }
                int i22 = i15 + 10 + i8 + i9;
                char cCharAt42 = charAt(this.bp + i22);
                if (cCharAt42 != 26 && cCharAt42 != '\"') {
                    return false;
                }
                int i23 = this.bp + i22;
                this.bp = i23;
                this.ch = charAt(i23);
                this.token = 5;
                return true;
            }
            c2 = cCharAt11;
            z2 = false;
            c3 = '9';
        }
        if (z) {
            return z2;
        }
        char cCharAt43 = charAt(this.bp + 8);
        boolean z3 = cCharAt14 == '-' && cCharAt17 == '-';
        boolean z4 = z3 && i == 16;
        boolean z5 = z3 && i == 17;
        if (z5 || z4) {
            cCharAt = charAt(this.bp + 9);
            c4 = cCharAt15;
            c5 = cCharAt16;
            c6 = cCharAt43;
        } else if (cCharAt14 == '-' && cCharAt16 == '-') {
            c5 = cCharAt15;
            cCharAt = cCharAt17;
            c4 = '0';
            c6 = '0';
        } else {
            c4 = cCharAt14;
            c5 = cCharAt15;
            c6 = cCharAt16;
            cCharAt = cCharAt17;
        }
        char c18 = c3;
        if (!checkDate(cCharAt10, c2, cCharAt12, cCharAt13, c4, c5, c6, cCharAt)) {
            return false;
        }
        setCalendar(cCharAt10, c2, cCharAt12, cCharAt13, c4, c5, c6, cCharAt);
        if (i != 8) {
            char cCharAt44 = charAt(this.bp + 9);
            char cCharAt45 = charAt(this.bp + 10);
            char cCharAt46 = charAt(this.bp + 11);
            char cCharAt47 = charAt(this.bp + 12);
            char cCharAt48 = charAt(this.bp + 13);
            if ((z5 && cCharAt45 == 'T' && cCharAt48 == ':' && charAt(this.bp + 16) == 'Z') || (z4 && ((cCharAt45 == ' ' || cCharAt45 == 'T') && cCharAt48 == ':'))) {
                cCharAt2 = charAt(this.bp + 14);
                cCharAt3 = charAt(this.bp + 15);
                cCharAt43 = cCharAt46;
                c7 = cCharAt47;
                c8 = '0';
                c9 = '0';
            } else {
                c7 = cCharAt44;
                cCharAt2 = cCharAt45;
                cCharAt3 = cCharAt46;
                c8 = cCharAt47;
                c9 = cCharAt48;
            }
            if (!checkTime(cCharAt43, c7, cCharAt2, cCharAt3, c8, c9)) {
                return false;
            }
            if (i != 17 || z5) {
                i3 = 0;
                c10 = '0';
            } else {
                char cCharAt49 = charAt(this.bp + 14);
                char cCharAt50 = charAt(this.bp + 15);
                char cCharAt51 = charAt(this.bp + 16);
                if (cCharAt49 < '0' || cCharAt49 > c18 || cCharAt50 < '0' || cCharAt50 > c18 || cCharAt51 < '0' || cCharAt51 > c18) {
                    return false;
                }
                i3 = ((cCharAt49 - '0') * 100) + ((cCharAt50 - '0') * 10) + (cCharAt51 - '0');
                c10 = '0';
            }
            i5 = ((cCharAt43 - '0') * 10) + (c7 - c10);
            int i24 = (cCharAt3 - c10) + ((cCharAt2 - c10) * 10);
            i4 = ((c8 - c10) * 10) + (c9 - c10);
            i2 = i24;
        } else {
            i2 = 0;
            i3 = 0;
            i4 = 0;
            i5 = 0;
        }
        this.calendar.set(11, i5);
        this.calendar.set(12, i2);
        this.calendar.set(13, i4);
        this.calendar.set(14, i3);
        this.token = 5;
        return true;
    }

    protected void setTime(char c2, char c3, char c4, char c5, char c6, char c7) {
        this.calendar.set(11, ((c2 - '0') * 10) + (c3 - '0'));
        this.calendar.set(12, ((c4 - '0') * 10) + (c5 - '0'));
        this.calendar.set(13, ((c6 - '0') * 10) + (c7 - '0'));
    }

    protected void setTimeZone(char c2, char c3, char c4) {
        setTimeZone(c2, c3, c4, '0', '0');
    }

    protected void setTimeZone(char c2, char c3, char c4, char c5, char c6) {
        int i = ((((c3 - '0') * 10) + (c4 - '0')) * 3600 * 1000) + ((((c5 - '0') * 10) + (c6 - '0')) * 60 * 1000);
        if (c2 == '-') {
            i = -i;
        }
        if (this.calendar.getTimeZone().getRawOffset() != i) {
            this.calendar.setTimeZone(new SimpleTimeZone(i, Integer.toString(i)));
        }
    }

    private void setCalendar(char c2, char c3, char c4, char c5, char c6, char c7, char c8, char c9) {
        this.calendar = Calendar.getInstance(this.timeZone, this.locale);
        this.calendar.set(1, ((c2 - '0') * 1000) + ((c3 - '0') * 100) + ((c4 - '0') * 10) + (c5 - '0'));
        this.calendar.set(2, (((c6 - '0') * 10) + (c7 - '0')) - 1);
        this.calendar.set(5, ((c8 - '0') * 10) + (c9 - '0'));
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public boolean isEOF() {
        if (this.bp != this.len) {
            return this.ch == 26 && this.bp + 1 >= this.len;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0068, code lost:
    
        if (r3 != '.') goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x006a, code lost:
    
        r14.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x006c, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x006d, code lost:
    
        if (r15 >= 0) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x006f, code lost:
    
        r14.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0071, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0072, code lost:
    
        if (r6 == false) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0074, code lost:
    
        if (r3 == '\"') goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0076, code lost:
    
        r14.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0078, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0079, code lost:
    
        r4 = charAt(r11);
        r11 = r11 + 1;
        r3 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0085, code lost:
    
        if (r3 == ',') goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0087, code lost:
    
        if (r3 != '}') goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x008e, code lost:
    
        if (isWhitespace(r3) == false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0090, code lost:
    
        r4 = charAt(r11);
        r11 = r11 + 1;
        r3 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0099, code lost:
    
        r14.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x009b, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x009c, code lost:
    
        r11 = r11 - 1;
        r14.bp = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00a1, code lost:
    
        if (r3 != ',') goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00a3, code lost:
    
        r0 = r14.bp + 1;
        r14.bp = r0;
        r14.ch = charAt(r0);
        r14.matchStat = 3;
        r14.token = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00b3, code lost:
    
        if (r7 == false) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00b6, code lost:
    
        return -r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00b7, code lost:
    
        if (r3 != '}') goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00b9, code lost:
    
        r14.bp = r11;
        r3 = r14.bp + 1;
        r14.bp = r3;
        r3 = charAt(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00c4, code lost:
    
        if (r3 != ',') goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00c6, code lost:
    
        r14.token = 16;
        r0 = r14.bp + 1;
        r14.bp = r0;
        r14.ch = charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00d6, code lost:
    
        if (r3 != ']') goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00d8, code lost:
    
        r14.token = 15;
        r0 = r14.bp + 1;
        r14.bp = r0;
        r14.ch = charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00e8, code lost:
    
        if (r3 != '}') goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00ea, code lost:
    
        r14.token = 13;
        r0 = r14.bp + 1;
        r14.bp = r0;
        r14.ch = charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00fc, code lost:
    
        if (r3 != 26) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00fe, code lost:
    
        r14.token = 20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0102, code lost:
    
        r14.matchStat = 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x010a, code lost:
    
        if (isWhitespace(r3) == false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x010c, code lost:
    
        r3 = r14.bp + 1;
        r14.bp = r3;
        r3 = charAt(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0116, code lost:
    
        r14.bp = r1;
        r14.ch = r2;
        r14.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x011c, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x011d, code lost:
    
        if (r7 == false) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0120, code lost:
    
        return -r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:?, code lost:
    
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:?, code lost:
    
        return r15;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int scanFieldInt(char[] r15) {
        /*
            Method dump skipped, instruction units count: 292
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldInt(char[]):int");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public String scanFieldString(char[] cArr) {
        int i;
        this.matchStat = 0;
        int i2 = this.bp;
        char c2 = this.ch;
        while (!charArrayCompare(this.text, this.bp, cArr)) {
            if (isWhitespace(this.ch)) {
                next();
                while (isWhitespace(this.ch)) {
                    next();
                }
            } else {
                this.matchStat = -2;
                return stringDefaultValue();
            }
        }
        int length = this.bp + cArr.length;
        int i3 = length + 1;
        char cCharAt = charAt(length);
        if (cCharAt != '\"') {
            int i4 = i3;
            int i5 = 0;
            while (isWhitespace(cCharAt)) {
                i5++;
                int i6 = i4 + 1;
                char cCharAt2 = charAt(i4);
                i4 = i6;
                cCharAt = cCharAt2;
            }
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            i = i5;
            i3 = i4;
        } else {
            i = 0;
        }
        int iIndexOf = indexOf('\"', i3);
        if (iIndexOf == -1) {
            throw new JSONException("unclosed str");
        }
        String strSubString = subString(i3, iIndexOf - i3);
        if (strSubString.indexOf(92) != -1) {
            while (true) {
                int i7 = 0;
                for (int i8 = iIndexOf - 1; i8 >= 0 && charAt(i8) == '\\'; i8--) {
                    i7++;
                }
                if (i7 % 2 == 0) {
                    break;
                }
                iIndexOf = indexOf('\"', iIndexOf + 1);
            }
            int length2 = iIndexOf - (((this.bp + cArr.length) + 1) + i);
            strSubString = readString(sub_chars(this.bp + cArr.length + 1 + i, length2), length2);
        }
        if ((this.features & Feature.TrimStringFieldValue.mask) != 0) {
            strSubString = strSubString.trim();
        }
        char cCharAt3 = charAt(iIndexOf + 1);
        while (cCharAt3 != ',' && cCharAt3 != '}') {
            if (isWhitespace(cCharAt3)) {
                iIndexOf++;
                cCharAt3 = charAt(iIndexOf + 1);
            } else {
                this.matchStat = -1;
                return stringDefaultValue();
            }
        }
        this.bp = iIndexOf + 1;
        this.ch = cCharAt3;
        if (cCharAt3 == ',') {
            int i9 = this.bp + 1;
            this.bp = i9;
            this.ch = charAt(i9);
            this.matchStat = 3;
            return strSubString;
        }
        int i10 = this.bp + 1;
        this.bp = i10;
        char cCharAt4 = charAt(i10);
        if (cCharAt4 == ',') {
            this.token = 16;
            int i11 = this.bp + 1;
            this.bp = i11;
            this.ch = charAt(i11);
        } else if (cCharAt4 == ']') {
            this.token = 15;
            int i12 = this.bp + 1;
            this.bp = i12;
            this.ch = charAt(i12);
        } else if (cCharAt4 == '}') {
            this.token = 13;
            int i13 = this.bp + 1;
            this.bp = i13;
            this.ch = charAt(i13);
        } else if (cCharAt4 == 26) {
            this.token = 20;
        } else {
            this.bp = i2;
            this.ch = c2;
            this.matchStat = -1;
            return stringDefaultValue();
        }
        this.matchStat = 4;
        return strSubString;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public Date scanFieldDate(char[] cArr) {
        char cCharAt;
        long j;
        Date date;
        int i;
        boolean z = false;
        this.matchStat = 0;
        int i2 = this.bp;
        char c2 = this.ch;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = this.bp + cArr.length;
        int i3 = length + 1;
        char cCharAt2 = charAt(length);
        if (cCharAt2 == '\"') {
            int iIndexOf = indexOf('\"', i3);
            if (iIndexOf == -1) {
                throw new JSONException("unclosed str");
            }
            this.bp = i3;
            if (scanISO8601DateIfMatch(false, iIndexOf - i3)) {
                Date time = this.calendar.getTime();
                char cCharAt3 = charAt(iIndexOf + 1);
                this.bp = i2;
                while (cCharAt3 != ',' && cCharAt3 != '}') {
                    if (isWhitespace(cCharAt3)) {
                        iIndexOf++;
                        cCharAt3 = charAt(iIndexOf + 1);
                    } else {
                        this.matchStat = -1;
                        return null;
                    }
                }
                this.bp = iIndexOf + 1;
                this.ch = cCharAt3;
                char c3 = cCharAt3;
                date = time;
                cCharAt = c3;
            } else {
                this.bp = i2;
                this.matchStat = -1;
                return null;
            }
        } else {
            char c4 = '9';
            char c5 = '0';
            if (cCharAt2 != '-' && (cCharAt2 < '0' || cCharAt2 > '9')) {
                this.matchStat = -1;
                return null;
            }
            if (cCharAt2 == '-') {
                cCharAt2 = charAt(i3);
                i3++;
                z = true;
            }
            if (cCharAt2 < '0' || cCharAt2 > '9') {
                cCharAt = cCharAt2;
                j = 0;
            } else {
                j = cCharAt2 - '0';
                while (true) {
                    i = i3 + 1;
                    cCharAt = charAt(i3);
                    if (cCharAt < c5 || cCharAt > c4) {
                        break;
                    }
                    j = (j * 10) + ((long) (cCharAt - '0'));
                    i3 = i;
                    c4 = '9';
                    c5 = '0';
                }
                if (cCharAt == ',' || cCharAt == '}') {
                    this.bp = i - 1;
                }
            }
            if (j < 0) {
                this.matchStat = -1;
                return null;
            }
            if (z) {
                j = -j;
            }
            date = new Date(j);
        }
        if (cCharAt == ',') {
            int i4 = this.bp + 1;
            this.bp = i4;
            this.ch = charAt(i4);
            this.matchStat = 3;
            this.token = 16;
            return date;
        }
        int i5 = this.bp + 1;
        this.bp = i5;
        char cCharAt4 = charAt(i5);
        if (cCharAt4 == ',') {
            this.token = 16;
            int i6 = this.bp + 1;
            this.bp = i6;
            this.ch = charAt(i6);
        } else if (cCharAt4 == ']') {
            this.token = 15;
            int i7 = this.bp + 1;
            this.bp = i7;
            this.ch = charAt(i7);
        } else if (cCharAt4 == '}') {
            this.token = 13;
            int i8 = this.bp + 1;
            this.bp = i8;
            this.ch = charAt(i8);
        } else if (cCharAt4 == 26) {
            this.token = 20;
        } else {
            this.bp = i2;
            this.ch = c2;
            this.matchStat = -1;
            return null;
        }
        this.matchStat = 4;
        return date;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public long scanFieldSymbol(char[] cArr) {
        this.matchStat = 0;
        while (!charArrayCompare(this.text, this.bp, cArr)) {
            if (isWhitespace(this.ch)) {
                next();
                while (isWhitespace(this.ch)) {
                    next();
                }
            } else {
                this.matchStat = -2;
                return 0L;
            }
        }
        int length = this.bp + cArr.length;
        int i = length + 1;
        char cCharAt = charAt(length);
        if (cCharAt != '\"') {
            while (isWhitespace(cCharAt)) {
                cCharAt = charAt(i);
                i++;
            }
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return 0L;
            }
        }
        long j = TypeUtils.fnv1a_64_magic_hashcode;
        while (true) {
            int i2 = i + 1;
            char cCharAt2 = charAt(i);
            if (cCharAt2 == '\"') {
                this.bp = i2;
                char cCharAt3 = charAt(this.bp);
                this.ch = cCharAt3;
                while (cCharAt3 != ',') {
                    if (cCharAt3 == '}') {
                        next();
                        skipWhitespace();
                        char current = getCurrent();
                        if (current == ',') {
                            this.token = 16;
                            int i3 = this.bp + 1;
                            this.bp = i3;
                            this.ch = charAt(i3);
                        } else if (current == ']') {
                            this.token = 15;
                            int i4 = this.bp + 1;
                            this.bp = i4;
                            this.ch = charAt(i4);
                        } else if (current == '}') {
                            this.token = 13;
                            int i5 = this.bp + 1;
                            this.bp = i5;
                            this.ch = charAt(i5);
                        } else if (current == 26) {
                            this.token = 20;
                        } else {
                            this.matchStat = -1;
                            return 0L;
                        }
                        this.matchStat = 4;
                        return j;
                    }
                    if (isWhitespace(cCharAt3)) {
                        int i6 = this.bp + 1;
                        this.bp = i6;
                        cCharAt3 = charAt(i6);
                    } else {
                        this.matchStat = -1;
                        return 0L;
                    }
                }
                int i7 = this.bp + 1;
                this.bp = i7;
                this.ch = charAt(i7);
                this.matchStat = 3;
                return j;
            }
            if (i2 > this.len) {
                this.matchStat = -1;
                return 0L;
            }
            j = (j ^ ((long) cCharAt2)) * TypeUtils.fnv1a_64_magic_prime;
            i = i2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x00df, code lost:
    
        if (r1 != ']') goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00e5, code lost:
    
        if (r3.size() != 0) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00e7, code lost:
    
        r1 = r9 + 1;
        r5 = r3;
        r3 = charAt(r9);
        r2 = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00f1, code lost:
    
        r17.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00f3, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0166, code lost:
    
        r17.matchStat = 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0169, code lost:
    
        return r5;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.Collection<java.lang.String> scanFieldStringArray(char[] r18, java.lang.Class<?> r19) {
        /*
            Method dump skipped, instruction units count: 424
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldStringArray(char[], java.lang.Class):java.util.Collection");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public long scanFieldLong(char[] cArr) {
        int i;
        char cCharAt;
        boolean z;
        int i2;
        char cCharAt2;
        int i3;
        char cCharAt3;
        this.matchStat = 0;
        int i4 = this.bp;
        char c2 = this.ch;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = this.bp + cArr.length;
        int i5 = length + 1;
        char cCharAt4 = charAt(length);
        boolean z2 = cCharAt4 == '\"';
        if (z2) {
            i = i5 + 1;
            cCharAt = charAt(i5);
        } else {
            i = i5;
            cCharAt = cCharAt4;
        }
        if (cCharAt == '-') {
            int i6 = i + 1;
            char cCharAt5 = charAt(i);
            z = true;
            i = i6;
            cCharAt = cCharAt5;
        } else {
            z = false;
        }
        if (cCharAt >= '0') {
            char c3 = '9';
            if (cCharAt <= '9') {
                long j = cCharAt - '0';
                while (true) {
                    i2 = i + 1;
                    cCharAt2 = charAt(i);
                    if (cCharAt2 < '0' || cCharAt2 > c3) {
                        break;
                    }
                    j = (j * 10) + ((long) (cCharAt2 - '0'));
                    i = i2;
                    c3 = '9';
                }
                if (cCharAt2 == '.') {
                    this.matchStat = -1;
                    return 0L;
                }
                if (!z2) {
                    i3 = i2;
                    cCharAt3 = cCharAt2;
                } else {
                    if (cCharAt2 != '\"') {
                        this.matchStat = -1;
                        return 0L;
                    }
                    i3 = i2 + 1;
                    cCharAt3 = charAt(i2);
                }
                if (cCharAt3 == ',' || cCharAt3 == '}') {
                    this.bp = i3 - 1;
                }
                if (!(j >= 0 || (j == Long.MIN_VALUE && z))) {
                    this.bp = i4;
                    this.ch = c2;
                    this.matchStat = -1;
                    return 0L;
                }
                while (cCharAt3 != ',') {
                    if (cCharAt3 == '}') {
                        int i7 = this.bp + 1;
                        this.bp = i7;
                        char cCharAt6 = charAt(i7);
                        while (true) {
                            if (cCharAt6 == ',') {
                                this.token = 16;
                                int i8 = this.bp + 1;
                                this.bp = i8;
                                this.ch = charAt(i8);
                                break;
                            }
                            if (cCharAt6 == ']') {
                                this.token = 15;
                                int i9 = this.bp + 1;
                                this.bp = i9;
                                this.ch = charAt(i9);
                                break;
                            }
                            if (cCharAt6 == '}') {
                                this.token = 13;
                                int i10 = this.bp + 1;
                                this.bp = i10;
                                this.ch = charAt(i10);
                                break;
                            }
                            if (cCharAt6 == 26) {
                                this.token = 20;
                                break;
                            }
                            if (isWhitespace(cCharAt6)) {
                                int i11 = this.bp + 1;
                                this.bp = i11;
                                cCharAt6 = charAt(i11);
                            } else {
                                this.bp = i4;
                                this.ch = c2;
                                this.matchStat = -1;
                                return 0L;
                            }
                        }
                        this.matchStat = 4;
                        return z ? -j : j;
                    }
                    if (isWhitespace(cCharAt3)) {
                        this.bp = i3;
                        int i12 = i3 + 1;
                        char cCharAt7 = charAt(i3);
                        i3 = i12;
                        cCharAt3 = cCharAt7;
                    } else {
                        this.matchStat = -1;
                        return 0L;
                    }
                }
                int i13 = this.bp + 1;
                this.bp = i13;
                this.ch = charAt(i13);
                this.matchStat = 3;
                this.token = 16;
                return z ? -j : j;
            }
        }
        this.bp = i4;
        this.ch = c2;
        this.matchStat = -1;
        return 0L;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public boolean scanFieldBoolean(char[] cArr) {
        int i;
        char cCharAt;
        int i2;
        char cCharAt2;
        boolean z;
        int i3;
        int i4;
        this.matchStat = 0;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return false;
        }
        int i5 = this.bp;
        int length = this.bp + cArr.length;
        int i6 = length + 1;
        char cCharAt3 = charAt(length);
        boolean z2 = cCharAt3 == '\"';
        if (z2) {
            i = i6 + 1;
            cCharAt = charAt(i6);
        } else {
            i = i6;
            cCharAt = cCharAt3;
        }
        if (cCharAt == 't') {
            int i7 = i + 1;
            if (charAt(i) != 'r') {
                this.matchStat = -1;
                return false;
            }
            int i8 = i7 + 1;
            if (charAt(i7) != 'u') {
                this.matchStat = -1;
                return false;
            }
            int i9 = i8 + 1;
            if (charAt(i8) != 'e') {
                this.matchStat = -1;
                return false;
            }
            if (z2) {
                int i10 = i9 + 1;
                if (charAt(i9) != '\"') {
                    this.matchStat = -1;
                    return false;
                }
                i9 = i10;
            }
            this.bp = i9;
            cCharAt2 = charAt(this.bp);
            z = true;
        } else if (cCharAt == 'f') {
            int i11 = i + 1;
            if (charAt(i) != 'a') {
                this.matchStat = -1;
                return false;
            }
            int i12 = i11 + 1;
            if (charAt(i11) != 'l') {
                this.matchStat = -1;
                return false;
            }
            int i13 = i12 + 1;
            if (charAt(i12) != 's') {
                this.matchStat = -1;
                return false;
            }
            int i14 = i13 + 1;
            if (charAt(i13) != 'e') {
                this.matchStat = -1;
                return false;
            }
            if (z2) {
                i4 = i14 + 1;
                if (charAt(i14) != '\"') {
                    this.matchStat = -1;
                    return false;
                }
            } else {
                i4 = i14;
            }
            this.bp = i4;
            cCharAt2 = charAt(this.bp);
            z = false;
        } else if (cCharAt == '1') {
            if (z2) {
                i3 = i + 1;
                if (charAt(i) != '\"') {
                    this.matchStat = -1;
                    return false;
                }
            } else {
                i3 = i;
            }
            this.bp = i3;
            cCharAt2 = charAt(this.bp);
            z = true;
        } else if (cCharAt == '0') {
            if (z2) {
                i2 = i + 1;
                if (charAt(i) != '\"') {
                    this.matchStat = -1;
                    return false;
                }
            } else {
                i2 = i;
            }
            this.bp = i2;
            cCharAt2 = charAt(this.bp);
            z = false;
        } else {
            this.matchStat = -1;
            return false;
        }
        while (true) {
            if (cCharAt2 == ',') {
                int i15 = this.bp + 1;
                this.bp = i15;
                this.ch = charAt(i15);
                this.matchStat = 3;
                this.token = 16;
                break;
            }
            if (cCharAt2 == '}') {
                int i16 = this.bp + 1;
                this.bp = i16;
                char cCharAt4 = charAt(i16);
                while (true) {
                    if (cCharAt4 == ',') {
                        this.token = 16;
                        int i17 = this.bp + 1;
                        this.bp = i17;
                        this.ch = charAt(i17);
                        break;
                    }
                    if (cCharAt4 == ']') {
                        this.token = 15;
                        int i18 = this.bp + 1;
                        this.bp = i18;
                        this.ch = charAt(i18);
                        break;
                    }
                    if (cCharAt4 == '}') {
                        this.token = 13;
                        int i19 = this.bp + 1;
                        this.bp = i19;
                        this.ch = charAt(i19);
                        break;
                    }
                    if (cCharAt4 == 26) {
                        this.token = 20;
                        break;
                    }
                    if (isWhitespace(cCharAt4)) {
                        int i20 = this.bp + 1;
                        this.bp = i20;
                        cCharAt4 = charAt(i20);
                    } else {
                        this.matchStat = -1;
                        return false;
                    }
                }
                this.matchStat = 4;
            } else if (isWhitespace(cCharAt2)) {
                int i21 = this.bp + 1;
                this.bp = i21;
                cCharAt2 = charAt(i21);
            } else {
                this.bp = i5;
                charAt(this.bp);
                this.matchStat = -1;
                return false;
            }
        }
        return z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0082, code lost:
    
        if (r4 != '.') goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0084, code lost:
    
        r16.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0086, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0087, code lost:
    
        if (r7 == false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0089, code lost:
    
        if (r4 == '\"') goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x008b, code lost:
    
        r16.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008d, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x008e, code lost:
    
        r2 = r13 + 1;
        r4 = charAt(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0095, code lost:
    
        r2 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0096, code lost:
    
        if (r3 >= 0) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0098, code lost:
    
        r16.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x009a, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x009d, code lost:
    
        if (r4 != r17) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x009f, code lost:
    
        r16.bp = r2;
        r16.ch = charAt(r16.bp);
        r16.matchStat = 3;
        r16.token = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00ae, code lost:
    
        if (r8 == false) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00b1, code lost:
    
        return -r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00b6, code lost:
    
        if (isWhitespace(r4) == false) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00b8, code lost:
    
        r4 = charAt(r2);
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00c2, code lost:
    
        r16.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00c4, code lost:
    
        if (r8 == false) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00c7, code lost:
    
        return -r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:?, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:?, code lost:
    
        return r3;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int scanInt(char r17) {
        /*
            Method dump skipped, instruction units count: 316
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanInt(char):int");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public double scanDouble(char c2) {
        int i;
        char cCharAt;
        long j;
        int i2;
        int i3;
        double d2;
        int i4;
        this.matchStat = 0;
        int i5 = this.bp;
        int i6 = i5 + 1;
        char cCharAt2 = charAt(i5);
        boolean z = cCharAt2 == '\"';
        if (z) {
            int i7 = i6 + 1;
            char cCharAt3 = charAt(i6);
            i6 = i7;
            cCharAt2 = cCharAt3;
        }
        boolean z2 = cCharAt2 == '-';
        if (z2) {
            int i8 = i6 + 1;
            char cCharAt4 = charAt(i6);
            i6 = i8;
            cCharAt2 = cCharAt4;
        }
        if (cCharAt2 >= '0') {
            char c3 = '9';
            if (cCharAt2 <= '9') {
                long j2 = cCharAt2 - '0';
                while (true) {
                    i = i6 + 1;
                    cCharAt = charAt(i6);
                    if (cCharAt < '0' || cCharAt > '9') {
                        break;
                    }
                    j2 = (j2 * 10) + ((long) (cCharAt - '0'));
                    i6 = i;
                }
                if (cCharAt == '.') {
                    int i9 = i + 1;
                    char cCharAt5 = charAt(i);
                    if (cCharAt5 < '0' || cCharAt5 > '9') {
                        this.matchStat = -1;
                        return 0.0d;
                    }
                    j2 = (j2 * 10) + ((long) (cCharAt5 - '0'));
                    j = 10;
                    while (true) {
                        i4 = i9 + 1;
                        cCharAt = charAt(i9);
                        if (cCharAt < '0' || cCharAt > c3) {
                            break;
                        }
                        j2 = (j2 * 10) + ((long) (cCharAt - '0'));
                        j *= 10;
                        i9 = i4;
                        c3 = '9';
                    }
                    i = i4;
                } else {
                    j = 1;
                }
                boolean z3 = cCharAt == 'e' || cCharAt == 'E';
                if (z3) {
                    int i10 = i + 1;
                    char cCharAt6 = charAt(i);
                    if (cCharAt6 == '+' || cCharAt6 == '-') {
                        int i11 = i10 + 1;
                        cCharAt = charAt(i10);
                        i = i11;
                    } else {
                        i = i10;
                        cCharAt = cCharAt6;
                    }
                    while (cCharAt >= '0' && cCharAt <= '9') {
                        char cCharAt7 = charAt(i);
                        i++;
                        cCharAt = cCharAt7;
                    }
                }
                if (!z) {
                    i2 = this.bp;
                    i3 = (i - i2) - 1;
                } else {
                    if (cCharAt != '\"') {
                        this.matchStat = -1;
                        return 0.0d;
                    }
                    int i12 = i + 1;
                    char cCharAt8 = charAt(i);
                    i2 = this.bp + 1;
                    i3 = (i12 - i2) - 2;
                    i = i12;
                    cCharAt = cCharAt8;
                }
                if (z3 || i3 >= 18) {
                    d2 = Double.parseDouble(subString(i2, i3));
                } else {
                    d2 = j2 / j;
                    if (z2) {
                        d2 = -d2;
                    }
                }
                if (cCharAt == c2) {
                    this.bp = i;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    this.token = 16;
                    return d2;
                }
                this.matchStat = -1;
                return d2;
            }
        }
        if (cCharAt2 == 'n') {
            int i13 = i6 + 1;
            if (charAt(i6) == 'u') {
                int i14 = i13 + 1;
                if (charAt(i13) == 'l') {
                    int i15 = i14 + 1;
                    if (charAt(i14) == 'l') {
                        this.matchStat = 5;
                        int i16 = i15 + 1;
                        char cCharAt9 = charAt(i15);
                        if (z && cCharAt9 == '\"') {
                            int i17 = i16 + 1;
                            char cCharAt10 = charAt(i16);
                            i16 = i17;
                            cCharAt9 = cCharAt10;
                        }
                        while (cCharAt9 != ',') {
                            if (cCharAt9 == ']') {
                                this.bp = i16;
                                this.ch = charAt(this.bp);
                                this.matchStat = 5;
                                this.token = 15;
                                return 0.0d;
                            }
                            if (isWhitespace(cCharAt9)) {
                                int i18 = i16 + 1;
                                char cCharAt11 = charAt(i16);
                                i16 = i18;
                                cCharAt9 = cCharAt11;
                            } else {
                                this.matchStat = -1;
                                return 0.0d;
                            }
                        }
                        this.bp = i16;
                        this.ch = charAt(this.bp);
                        this.matchStat = 5;
                        this.token = 16;
                        return 0.0d;
                    }
                }
            }
        }
        this.matchStat = -1;
        return 0.0d;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public long scanLong(char c2) {
        int i;
        char cCharAt;
        boolean z = false;
        this.matchStat = 0;
        int i2 = this.bp;
        int i3 = i2 + 1;
        char cCharAt2 = charAt(i2);
        boolean z2 = cCharAt2 == '\"';
        if (z2) {
            int i4 = i3 + 1;
            char cCharAt3 = charAt(i3);
            i3 = i4;
            cCharAt2 = cCharAt3;
        }
        boolean z3 = cCharAt2 == '-';
        if (z3) {
            int i5 = i3 + 1;
            char cCharAt4 = charAt(i3);
            i3 = i5;
            cCharAt2 = cCharAt4;
        }
        char c3 = '0';
        if (cCharAt2 >= '0' && cCharAt2 <= '9') {
            long j = cCharAt2 - '0';
            while (true) {
                i = i3 + 1;
                cCharAt = charAt(i3);
                if (cCharAt < c3 || cCharAt > '9') {
                    break;
                }
                j = (j * 10) + ((long) (cCharAt - '0'));
                i3 = i;
                c3 = '0';
            }
            if (cCharAt == '.') {
                this.matchStat = -1;
                return 0L;
            }
            if (z2) {
                if (cCharAt != '\"') {
                    this.matchStat = -1;
                    return 0L;
                }
                cCharAt = charAt(i);
                i++;
            }
            if (j >= 0 || (j == Long.MIN_VALUE && z3)) {
                z = true;
            }
            if (!z) {
                this.matchStat = -1;
                return 0L;
            }
            while (cCharAt != c2) {
                if (isWhitespace(cCharAt)) {
                    cCharAt = charAt(i);
                    i++;
                } else {
                    this.matchStat = -1;
                    return j;
                }
            }
            this.bp = i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z3 ? -j : j;
        }
        if (cCharAt2 == 'n') {
            int i6 = i3 + 1;
            if (charAt(i3) == 'u') {
                int i7 = i6 + 1;
                if (charAt(i6) == 'l') {
                    int i8 = i7 + 1;
                    if (charAt(i7) == 'l') {
                        this.matchStat = 5;
                        int i9 = i8 + 1;
                        char cCharAt5 = charAt(i8);
                        if (z2 && cCharAt5 == '\"') {
                            int i10 = i9 + 1;
                            char cCharAt6 = charAt(i9);
                            i9 = i10;
                            cCharAt5 = cCharAt6;
                        }
                        while (cCharAt5 != ',') {
                            if (cCharAt5 == ']') {
                                this.bp = i9;
                                this.ch = charAt(this.bp);
                                this.matchStat = 5;
                                this.token = 15;
                                return 0L;
                            }
                            if (isWhitespace(cCharAt5)) {
                                int i11 = i9 + 1;
                                char cCharAt7 = charAt(i9);
                                i9 = i11;
                                cCharAt5 = cCharAt7;
                            } else {
                                this.matchStat = -1;
                                return 0L;
                            }
                        }
                        this.bp = i9;
                        this.ch = charAt(this.bp);
                        this.matchStat = 5;
                        this.token = 16;
                        return 0L;
                    }
                }
            }
        }
        this.matchStat = -1;
        return 0L;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public Date scanDate(char c2) {
        boolean z;
        int i;
        char cCharAt;
        long j;
        char cCharAt2;
        Date date;
        int i2;
        this.matchStat = 0;
        int i3 = this.bp;
        char c3 = this.ch;
        int i4 = this.bp;
        int i5 = i4 + 1;
        char cCharAt3 = charAt(i4);
        if (cCharAt3 == '\"') {
            int iIndexOf = indexOf('\"', i5);
            if (iIndexOf == -1) {
                throw new JSONException("unclosed str");
            }
            this.bp = i5;
            if (scanISO8601DateIfMatch(false, iIndexOf - i5)) {
                date = this.calendar.getTime();
                cCharAt2 = charAt(iIndexOf + 1);
                this.bp = i3;
                while (cCharAt2 != ',' && cCharAt2 != ']') {
                    if (isWhitespace(cCharAt2)) {
                        iIndexOf++;
                        cCharAt2 = charAt(iIndexOf + 1);
                    } else {
                        this.bp = i3;
                        this.ch = c3;
                        this.matchStat = -1;
                        return null;
                    }
                }
                this.bp = iIndexOf + 1;
                this.ch = cCharAt2;
            } else {
                this.bp = i3;
                this.ch = c3;
                this.matchStat = -1;
                return null;
            }
        } else {
            char c4 = '9';
            char c5 = '0';
            if (cCharAt3 != '-' && (cCharAt3 < '0' || cCharAt3 > '9')) {
                if (cCharAt3 == 'n') {
                    int i6 = i5 + 1;
                    if (charAt(i5) == 'u') {
                        int i7 = i6 + 1;
                        if (charAt(i6) == 'l') {
                            int i8 = i7 + 1;
                            if (charAt(i7) == 'l') {
                                cCharAt2 = charAt(i8);
                                this.bp = i8;
                                date = null;
                            }
                        }
                    }
                }
                this.bp = i3;
                this.ch = c3;
                this.matchStat = -1;
                return null;
            }
            if (cCharAt3 == '-') {
                i = i5 + 1;
                cCharAt3 = charAt(i5);
                z = true;
            } else {
                z = false;
                i = i5;
            }
            if (cCharAt3 < '0' || cCharAt3 > '9') {
                cCharAt = cCharAt3;
                j = 0;
            } else {
                j = cCharAt3 - '0';
                while (true) {
                    i2 = i + 1;
                    cCharAt = charAt(i);
                    if (cCharAt < c5 || cCharAt > c4) {
                        break;
                    }
                    j = (j * 10) + ((long) (cCharAt - '0'));
                    i = i2;
                    c4 = '9';
                    c5 = '0';
                }
                if (cCharAt == ',' || cCharAt == ']') {
                    this.bp = i2 - 1;
                }
            }
            if (j < 0) {
                this.bp = i3;
                this.ch = c3;
                this.matchStat = -1;
                return null;
            }
            if (z) {
                j = -j;
            }
            cCharAt2 = cCharAt;
            date = new Date(j);
        }
        if (cCharAt2 == ',') {
            int i9 = this.bp + 1;
            this.bp = i9;
            this.ch = charAt(i9);
            this.matchStat = 3;
            return date;
        }
        int i10 = this.bp + 1;
        this.bp = i10;
        char cCharAt4 = charAt(i10);
        if (cCharAt4 == ',') {
            this.token = 16;
            int i11 = this.bp + 1;
            this.bp = i11;
            this.ch = charAt(i11);
        } else if (cCharAt4 == ']') {
            this.token = 15;
            int i12 = this.bp + 1;
            this.bp = i12;
            this.ch = charAt(i12);
        } else if (cCharAt4 == '}') {
            this.token = 13;
            int i13 = this.bp + 1;
            this.bp = i13;
            this.ch = charAt(i13);
        } else if (cCharAt4 == 26) {
            this.ch = JSONLexer.EOI;
            this.token = 20;
        } else {
            this.bp = i3;
            this.ch = c3;
            this.matchStat = -1;
            return null;
        }
        this.matchStat = 4;
        return date;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void arrayCopy(int i, char[] cArr, int i2, int i3) {
        this.text.getChars(i, i3 + i, cArr, i2);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public String info() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int i2 = 1;
        int i3 = 1;
        while (i < this.bp) {
            if (this.text.charAt(i) == '\n') {
                i2++;
                i3 = 1;
            }
            i++;
            i3++;
        }
        sb.append("pos ");
        sb.append(this.bp);
        sb.append(", line ");
        sb.append(i2);
        sb.append(", column ");
        sb.append(i3);
        if (this.text.length() < 65535) {
            sb.append(this.text);
        } else {
            sb.append(this.text.substring(0, 65535));
        }
        return sb.toString();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public String[] scanFieldStringArray(char[] cArr, int i, SymbolTable symbolTable) {
        int i2;
        char cCharAt;
        int i3 = this.bp;
        char c2 = this.ch;
        while (isWhitespace(this.ch)) {
            next();
        }
        if (cArr != null) {
            this.matchStat = 0;
            if (!charArrayCompare(cArr)) {
                this.matchStat = -2;
                return null;
            }
            int length = this.bp + cArr.length;
            int i4 = length + 1;
            char cCharAt2 = this.text.charAt(length);
            while (isWhitespace(cCharAt2)) {
                cCharAt2 = this.text.charAt(i4);
                i4++;
            }
            if (cCharAt2 == ':') {
                i2 = i4 + 1;
                cCharAt = this.text.charAt(i4);
                while (isWhitespace(cCharAt)) {
                    cCharAt = this.text.charAt(i2);
                    i2++;
                }
            } else {
                this.matchStat = -1;
                return null;
            }
        } else {
            i2 = this.bp + 1;
            cCharAt = this.ch;
        }
        if (cCharAt == '[') {
            this.bp = i2;
            this.ch = this.text.charAt(this.bp);
            String[] strArr = i >= 0 ? new String[i] : new String[4];
            int i5 = 0;
            while (true) {
                if (isWhitespace(this.ch)) {
                    next();
                } else {
                    if (this.ch != '\"') {
                        this.bp = i3;
                        this.ch = c2;
                        this.matchStat = -1;
                        return null;
                    }
                    String strScanSymbol = scanSymbol(symbolTable, '\"');
                    if (i5 == strArr.length) {
                        String[] strArr2 = new String[strArr.length + (strArr.length >> 1) + 1];
                        System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
                        strArr = strArr2;
                    }
                    int i6 = i5 + 1;
                    strArr[i5] = strScanSymbol;
                    while (isWhitespace(this.ch)) {
                        next();
                    }
                    if (this.ch == ',') {
                        next();
                        i5 = i6;
                    } else {
                        if (strArr.length != i6) {
                            String[] strArr3 = new String[i6];
                            System.arraycopy(strArr, 0, strArr3, 0, i6);
                            strArr = strArr3;
                        }
                        while (isWhitespace(this.ch)) {
                            next();
                        }
                        if (this.ch == ']') {
                            next();
                            return strArr;
                        }
                        this.bp = i3;
                        this.ch = c2;
                        this.matchStat = -1;
                        return null;
                    }
                }
            }
        } else {
            if (cCharAt == 'n' && this.text.startsWith("ull", this.bp + 1)) {
                this.bp += 4;
                this.ch = this.text.charAt(this.bp);
                return null;
            }
            this.matchStat = -1;
            return null;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public boolean matchField2(char[] cArr) {
        while (isWhitespace(this.ch)) {
            next();
        }
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return false;
        }
        int length = this.bp + cArr.length;
        int i = length + 1;
        char cCharAt = this.text.charAt(length);
        while (isWhitespace(cCharAt)) {
            cCharAt = this.text.charAt(i);
            i++;
        }
        if (cCharAt == ':') {
            this.bp = i;
            this.ch = charAt(this.bp);
            return true;
        }
        this.matchStat = -2;
        return false;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final void skipObject() {
        skipObject(false);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final void skipObject(boolean z) {
        int i = this.bp;
        boolean z2 = false;
        int i2 = 0;
        while (i < this.text.length()) {
            char cCharAt = this.text.charAt(i);
            if (cCharAt == '\\') {
                if (i >= this.len - 1) {
                    this.ch = cCharAt;
                    this.bp = i;
                    throw new JSONException("illegal str, " + info());
                }
                i++;
            } else if (cCharAt == '\"') {
                z2 = !z2;
            } else if (cCharAt != '{') {
                if (cCharAt == '}' && !z2 && i2 - 1 == -1) {
                    this.bp = i + 1;
                    int i3 = this.bp;
                    int length = this.text.length();
                    char cCharAt2 = JSONLexer.EOI;
                    if (i3 == length) {
                        this.ch = JSONLexer.EOI;
                        this.token = 20;
                        return;
                    }
                    this.ch = this.text.charAt(this.bp);
                    if (this.ch == ',') {
                        this.token = 16;
                        int i4 = this.bp + 1;
                        this.bp = i4;
                        if (i4 < this.text.length()) {
                            cCharAt2 = this.text.charAt(i4);
                        }
                        this.ch = cCharAt2;
                        return;
                    }
                    if (this.ch == '}') {
                        this.token = 13;
                        next();
                        return;
                    } else if (this.ch == ']') {
                        this.token = 15;
                        next();
                        return;
                    } else {
                        nextToken(16);
                        return;
                    }
                }
            } else if (!z2) {
                i2++;
            }
            i++;
        }
        for (int i5 = 0; i5 < this.bp; i5++) {
            if (i5 < this.text.length() && this.text.charAt(i5) == ' ') {
                i++;
            }
        }
        if (i != this.text.length()) {
            return;
        }
        throw new JSONException("illegal str, " + info());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final void skipArray() {
        skipArray(false);
    }

    public final void skipArray(boolean z) {
        int i = this.bp;
        boolean z2 = false;
        int i2 = 0;
        while (i < this.text.length()) {
            char cCharAt = this.text.charAt(i);
            if (cCharAt == '\\') {
                if (i >= this.len - 1) {
                    this.ch = cCharAt;
                    this.bp = i;
                    throw new JSONException("illegal str, " + info());
                }
                i++;
            } else if (cCharAt == '\"') {
                z2 = !z2;
            } else if (cCharAt != '[') {
                char cCharAt2 = JSONLexer.EOI;
                if (cCharAt == '{' && z) {
                    int i3 = this.bp + 1;
                    this.bp = i3;
                    if (i3 < this.text.length()) {
                        cCharAt2 = this.text.charAt(i3);
                    }
                    this.ch = cCharAt2;
                    skipObject(z);
                } else if (cCharAt == ']' && !z2 && i2 - 1 == -1) {
                    this.bp = i + 1;
                    if (this.bp == this.text.length()) {
                        this.ch = JSONLexer.EOI;
                        this.token = 20;
                        return;
                    } else {
                        this.ch = this.text.charAt(this.bp);
                        nextToken(16);
                        return;
                    }
                }
            } else if (!z2) {
                i2++;
            }
            i++;
        }
        if (i != this.text.length()) {
            return;
        }
        throw new JSONException("illegal str, " + info());
    }

    public final void skipString() {
        if (this.ch == '\"') {
            int i = this.bp;
            while (true) {
                i++;
                if (i < this.text.length()) {
                    char cCharAt = this.text.charAt(i);
                    if (cCharAt == '\\') {
                        if (i < this.len - 1) {
                            i++;
                        }
                    } else if (cCharAt == '\"') {
                        String str = this.text;
                        int i2 = i + 1;
                        this.bp = i2;
                        this.ch = str.charAt(i2);
                        return;
                    }
                } else {
                    throw new JSONException("unclosed str");
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x0091, code lost:
    
        if (r3 == false) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x009b, code lost:
    
        throw new com.alibaba.fastjson.JSONException("illegal json.");
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean seekArrayToItem(int r10) {
        /*
            Method dump skipped, instruction units count: 222
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.seekArrayToItem(int):boolean");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public int seekObjectToField(long j, boolean z) {
        if (this.token == 20) {
            return -1;
        }
        if (this.token == 13 || this.token == 15) {
            nextToken();
            return -1;
        }
        if (this.token != 12 && this.token != 16) {
            throw new UnsupportedOperationException(JSONToken.name(this.token));
        }
        while (this.ch != '}') {
            char c2 = this.ch;
            char cCharAt = JSONLexer.EOI;
            if (c2 == 26) {
                return -1;
            }
            if (this.ch != '\"') {
                skipWhitespace();
            }
            if (this.ch == '\"') {
                long j2 = TypeUtils.fnv1a_64_magic_hashcode;
                int i = this.bp + 1;
                while (true) {
                    if (i >= this.text.length()) {
                        break;
                    }
                    char cCharAt2 = this.text.charAt(i);
                    if (cCharAt2 == '\\') {
                        i++;
                        if (i == this.text.length()) {
                            throw new JSONException("unclosed str, " + info());
                        }
                        cCharAt2 = this.text.charAt(i);
                    }
                    if (cCharAt2 == '\"') {
                        this.bp = i + 1;
                        this.ch = this.bp >= this.text.length() ? (char) 26 : this.text.charAt(this.bp);
                    } else {
                        j2 = (j2 ^ ((long) cCharAt2)) * TypeUtils.fnv1a_64_magic_prime;
                        i++;
                    }
                }
                if (j2 == j) {
                    if (this.ch != ':') {
                        skipWhitespace();
                    }
                    if (this.ch != ':') {
                        return 3;
                    }
                    int i2 = this.bp + 1;
                    this.bp = i2;
                    this.ch = i2 >= this.text.length() ? (char) 26 : this.text.charAt(i2);
                    if (this.ch == ',') {
                        int i3 = this.bp + 1;
                        this.bp = i3;
                        if (i3 < this.text.length()) {
                            cCharAt = this.text.charAt(i3);
                        }
                        this.ch = cCharAt;
                        this.token = 16;
                        return 3;
                    }
                    if (this.ch == ']') {
                        int i4 = this.bp + 1;
                        this.bp = i4;
                        if (i4 < this.text.length()) {
                            cCharAt = this.text.charAt(i4);
                        }
                        this.ch = cCharAt;
                        this.token = 15;
                        return 3;
                    }
                    if (this.ch == '}') {
                        int i5 = this.bp + 1;
                        this.bp = i5;
                        if (i5 < this.text.length()) {
                            cCharAt = this.text.charAt(i5);
                        }
                        this.ch = cCharAt;
                        this.token = 13;
                        return 3;
                    }
                    if (this.ch >= '0' && this.ch <= '9') {
                        this.sp = 0;
                        this.pos = this.bp;
                        scanNumber();
                        return 3;
                    }
                    nextToken(2);
                    return 3;
                }
                if (this.ch != ':') {
                    skipWhitespace();
                }
                if (this.ch == ':') {
                    int i6 = this.bp + 1;
                    this.bp = i6;
                    this.ch = i6 >= this.text.length() ? (char) 26 : this.text.charAt(i6);
                    if (this.ch != '\"' && this.ch != '\'' && this.ch != '{' && this.ch != '[' && this.ch != '0' && this.ch != '1' && this.ch != '2' && this.ch != '3' && this.ch != '4' && this.ch != '5' && this.ch != '6' && this.ch != '7' && this.ch != '8' && this.ch != '9' && this.ch != '+' && this.ch != '-') {
                        skipWhitespace();
                    }
                    if (this.ch == '-' || this.ch == '+' || (this.ch >= '0' && this.ch <= '9')) {
                        next();
                        while (this.ch >= '0' && this.ch <= '9') {
                            next();
                        }
                        if (this.ch == '.') {
                            next();
                            while (this.ch >= '0' && this.ch <= '9') {
                                next();
                            }
                        }
                        if (this.ch == 'E' || this.ch == 'e') {
                            next();
                            if (this.ch == '-' || this.ch == '+') {
                                next();
                            }
                            while (this.ch >= '0' && this.ch <= '9') {
                                next();
                            }
                        }
                        if (this.ch != ',') {
                            skipWhitespace();
                        }
                        if (this.ch == ',') {
                            next();
                        }
                    } else if (this.ch == '\"') {
                        skipString();
                        if (this.ch != ',' && this.ch != '}') {
                            skipWhitespace();
                        }
                        if (this.ch == ',') {
                            next();
                        }
                    } else if (this.ch == 't') {
                        next();
                        if (this.ch == 'r') {
                            next();
                            if (this.ch == 'u') {
                                next();
                                if (this.ch == 'e') {
                                    next();
                                }
                            }
                        }
                        if (this.ch != ',' && this.ch != '}') {
                            skipWhitespace();
                        }
                        if (this.ch == ',') {
                            next();
                        }
                    } else if (this.ch == 'n') {
                        next();
                        if (this.ch == 'u') {
                            next();
                            if (this.ch == 'l') {
                                next();
                                if (this.ch == 'l') {
                                    next();
                                }
                            }
                        }
                        if (this.ch != ',' && this.ch != '}') {
                            skipWhitespace();
                        }
                        if (this.ch == ',') {
                            next();
                        }
                    } else if (this.ch == 'f') {
                        next();
                        if (this.ch == 'a') {
                            next();
                            if (this.ch == 'l') {
                                next();
                                if (this.ch == 's') {
                                    next();
                                    if (this.ch == 'e') {
                                        next();
                                    }
                                }
                            }
                        }
                        if (this.ch != ',' && this.ch != '}') {
                            skipWhitespace();
                        }
                        if (this.ch == ',') {
                            next();
                        }
                    } else if (this.ch == '{') {
                        int i7 = this.bp + 1;
                        this.bp = i7;
                        if (i7 < this.text.length()) {
                            cCharAt = this.text.charAt(i7);
                        }
                        this.ch = cCharAt;
                        if (z) {
                            this.token = 12;
                            return 1;
                        }
                        skipObject(false);
                        if (this.token == 13) {
                            return -1;
                        }
                    } else if (this.ch == '[') {
                        next();
                        if (z) {
                            this.token = 14;
                            return 2;
                        }
                        skipArray(false);
                        if (this.token == 13) {
                            return -1;
                        }
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else {
                    throw new JSONException("illegal json, " + info());
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }
        next();
        nextToken();
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x0195, code lost:
    
        if (r14.ch == '{') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0199, code lost:
    
        if (r14.ch == '[') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x019d, code lost:
    
        if (r14.ch == '0') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x01a3, code lost:
    
        if (r14.ch == '1') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x01a9, code lost:
    
        if (r14.ch == '2') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x01af, code lost:
    
        if (r14.ch == '3') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x01b5, code lost:
    
        if (r14.ch == '4') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x01bb, code lost:
    
        if (r14.ch == '5') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x01c1, code lost:
    
        if (r14.ch == '6') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x01c7, code lost:
    
        if (r14.ch == '7') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x01cd, code lost:
    
        if (r14.ch == '8') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x01d1, code lost:
    
        if (r14.ch == '9') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x01d5, code lost:
    
        if (r14.ch == '+') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x01d9, code lost:
    
        if (r14.ch == '-') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x01db, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x01e0, code lost:
    
        if (r14.ch == '-') goto L198;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x01e4, code lost:
    
        if (r14.ch == '+') goto L199;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x01e8, code lost:
    
        if (r14.ch < '0') goto L209;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x01ec, code lost:
    
        if (r14.ch > '9') goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x01f1, code lost:
    
        if (r14.ch != '\"') goto L202;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x01f3, code lost:
    
        skipString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x01f8, code lost:
    
        if (r14.ch == ',') goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x01fc, code lost:
    
        if (r14.ch == '}') goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x01fe, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0203, code lost:
    
        if (r14.ch != ',') goto L221;
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0205, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x020c, code lost:
    
        if (r14.ch != '{') goto L213;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x020e, code lost:
    
        r2 = r14.bp + 1;
        r14.bp = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x021a, code lost:
    
        if (r2 < r14.text.length()) goto L153;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x021d, code lost:
    
        r4 = r14.text.charAt(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0223, code lost:
    
        r14.ch = r4;
        skipObject(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x022c, code lost:
    
        if (r14.ch != '[') goto L215;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x022e, code lost:
    
        next();
        skipArray(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x023b, code lost:
    
        throw new java.lang.UnsupportedOperationException();
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x023c, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0241, code lost:
    
        if (r14.ch < '0') goto L228;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0245, code lost:
    
        if (r14.ch > '9') goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0247, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x024f, code lost:
    
        if (r14.ch != '.') goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0251, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0256, code lost:
    
        if (r14.ch < '0') goto L230;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x025a, code lost:
    
        if (r14.ch > '9') goto L231;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x025c, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x0264, code lost:
    
        if (r14.ch == 'E') goto L178;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x026a, code lost:
    
        if (r14.ch != 'e') goto L188;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x026c, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0271, code lost:
    
        if (r14.ch == '-') goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x0275, code lost:
    
        if (r14.ch != '+') goto L234;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x0277, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x027c, code lost:
    
        if (r14.ch < '0') goto L232;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x0280, code lost:
    
        if (r14.ch > '9') goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x0282, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x0288, code lost:
    
        if (r14.ch == ',') goto L191;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x028a, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x028f, code lost:
    
        if (r14.ch != ',') goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0291, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x02b0, code lost:
    
        throw new com.alibaba.fastjson.JSONException("illegal json, " + info());
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00a7, code lost:
    
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00aa, code lost:
    
        if (r8 >= r15.length) goto L226;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00b0, code lost:
    
        if (r6 != r15[r8]) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b3, code lost:
    
        r8 = r8 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b6, code lost:
    
        r8 = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00bf, code lost:
    
        if (r8 == (-1)) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00c3, code lost:
    
        if (r14.ch == ':') goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00c5, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00ca, code lost:
    
        if (r14.ch != ':') goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00cc, code lost:
    
        r15 = r14.bp + 1;
        r14.bp = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00d8, code lost:
    
        if (r15 < r14.text.length()) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00da, code lost:
    
        r15 = 26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00dc, code lost:
    
        r15 = r14.text.charAt(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00e2, code lost:
    
        r14.ch = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00e6, code lost:
    
        if (r14.ch != ',') goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00e8, code lost:
    
        r15 = r14.bp + 1;
        r14.bp = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00f4, code lost:
    
        if (r15 < r14.text.length()) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00f7, code lost:
    
        r4 = r14.text.charAt(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00fd, code lost:
    
        r14.ch = r4;
        r14.token = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0106, code lost:
    
        if (r14.ch != ']') goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0108, code lost:
    
        r15 = r14.bp + 1;
        r14.bp = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0114, code lost:
    
        if (r15 < r14.text.length()) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0117, code lost:
    
        r4 = r14.text.charAt(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x011d, code lost:
    
        r14.ch = r4;
        r14.token = 15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0126, code lost:
    
        if (r14.ch != '}') goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0128, code lost:
    
        r15 = r14.bp + 1;
        r14.bp = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0134, code lost:
    
        if (r15 < r14.text.length()) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0137, code lost:
    
        r4 = r14.text.charAt(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x013d, code lost:
    
        r14.ch = r4;
        r14.token = 13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0146, code lost:
    
        if (r14.ch < '0') goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x014a, code lost:
    
        if (r14.ch > '9') goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x014c, code lost:
    
        r14.sp = 0;
        r14.pos = r14.bp;
        scanNumber();
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0156, code lost:
    
        nextToken(2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x015a, code lost:
    
        r14.matchStat = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x015d, code lost:
    
        return r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0160, code lost:
    
        if (r14.ch == ':') goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0162, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0167, code lost:
    
        if (r14.ch != ':') goto L208;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0169, code lost:
    
        r3 = r14.bp + 1;
        r14.bp = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0175, code lost:
    
        if (r3 < r14.text.length()) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0177, code lost:
    
        r3 = 26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0179, code lost:
    
        r3 = r14.text.charAt(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x017f, code lost:
    
        r14.ch = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x018b, code lost:
    
        if (r14.ch == '\"') goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0191, code lost:
    
        if (r14.ch == '\'') goto L129;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int seekObjectToField(long[] r15) {
        /*
            Method dump skipped, instruction units count: 695
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.seekObjectToField(long[]):int");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public String scanTypeName(SymbolTable symbolTable) {
        int iIndexOf;
        if (!this.text.startsWith("\"@type\":\"", this.bp) || (iIndexOf = this.text.indexOf(34, this.bp + 9)) == -1) {
            return null;
        }
        this.bp += 9;
        int iCharAt = 0;
        for (int i = this.bp; i < iIndexOf; i++) {
            iCharAt = (iCharAt * 31) + this.text.charAt(i);
        }
        String strAddSymbol = addSymbol(this.bp, iIndexOf - this.bp, iCharAt, symbolTable);
        char cCharAt = this.text.charAt(iIndexOf + 1);
        if (cCharAt != ',' && cCharAt != ']') {
            return null;
        }
        this.bp = iIndexOf + 2;
        this.ch = this.text.charAt(this.bp);
        return strAddSymbol;
    }
}
