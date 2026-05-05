package com.alibaba.fastjson.parser;

import anetwork.channel.util.RequestConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.google.android.exoplayer2.C;
import io.netty.util.internal.StringUtil;
import java.io.Closeable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public abstract class JSONLexerBase implements JSONLexer, Closeable {
    protected static final int INT_MULTMIN_RADIX_TEN = -214748364;
    protected static final long MULTMIN_RADIX_TEN = -922337203685477580L;
    protected int bp;
    protected char ch;
    protected int eofPos;
    protected int features;
    protected boolean hasSpecial;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    protected int sp;
    protected String stringDefaultValue;
    protected int token;
    private static final ThreadLocal<char[]> SBUF_LOCAL = new ThreadLocal<>();
    protected static final char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    protected static final int[] digits = new int[103];
    protected Calendar calendar = null;
    protected TimeZone timeZone = JSON.defaultTimeZone;
    protected Locale locale = JSON.defaultLocale;
    public int matchStat = 0;
    protected int nanos = 0;

    public static boolean isWhitespace(char c2) {
        return c2 <= ' ' && (c2 == ' ' || c2 == '\n' || c2 == '\r' || c2 == '\t' || c2 == '\f' || c2 == '\b');
    }

    public abstract String addSymbol(int i, int i2, int i3, SymbolTable symbolTable);

    protected abstract void arrayCopy(int i, char[] cArr, int i2, int i3);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract byte[] bytesValue();

    protected abstract boolean charArrayCompare(char[] cArr);

    public abstract char charAt(int i);

    protected abstract void copyTo(int i, int i2, char[] cArr);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract BigDecimal decimalValue();

    public abstract int indexOf(char c2, int i);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String info() {
        return "";
    }

    public abstract boolean isEOF();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract char next();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract String numberString();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanTypeName(SymbolTable symbolTable) {
        return null;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract String stringVal();

    public abstract String subString(int i, int i2);

    protected abstract char[] sub_chars(int i, int i2);

    protected void lexError(String str, Object... objArr) {
        this.token = 1;
    }

    static {
        for (int i = 48; i <= 57; i++) {
            digits[i] = i - 48;
        }
        for (int i2 = 97; i2 <= 102; i2++) {
            digits[i2] = (i2 - 97) + 10;
        }
        for (int i3 = 65; i3 <= 70; i3++) {
            digits[i3] = (i3 - 65) + 10;
        }
    }

    public JSONLexerBase(int i) {
        this.stringDefaultValue = null;
        this.features = i;
        if ((i & Feature.InitStringFieldAsEmpty.mask) != 0) {
            this.stringDefaultValue = "";
        }
        this.sbuf = SBUF_LOCAL.get();
        if (this.sbuf == null) {
            this.sbuf = new char[512];
        }
    }

    public final int matchStat() {
        return this.matchStat;
    }

    public void setToken(int i) {
        this.token = i;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextToken() {
        this.sp = 0;
        while (true) {
            this.pos = this.bp;
            char c2 = this.ch;
            if (c2 == '/') {
                skipComment();
            } else {
                if (c2 == '\"') {
                    scanString();
                    return;
                }
                if (c2 == ',') {
                    next();
                    this.token = 16;
                    return;
                }
                if (c2 >= '0' && c2 <= '9') {
                    scanNumber();
                    return;
                }
                char c3 = this.ch;
                if (c3 == '-') {
                    scanNumber();
                    return;
                }
                switch (c3) {
                    case '\b':
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                        next();
                        break;
                    case '\'':
                        if (!isEnabled(Feature.AllowSingleQuotes)) {
                            throw new JSONException("Feature.AllowSingleQuotes is false");
                        }
                        scanStringSingleQuote();
                        return;
                    case '(':
                        next();
                        this.token = 10;
                        return;
                    case ')':
                        next();
                        this.token = 11;
                        return;
                    case '+':
                        next();
                        scanNumber();
                        return;
                    case '.':
                        next();
                        this.token = 25;
                        return;
                    case ':':
                        next();
                        this.token = 17;
                        return;
                    case ';':
                        next();
                        this.token = 24;
                        return;
                    case 'N':
                    case 'S':
                    case 'T':
                    case 'u':
                        scanIdent();
                        return;
                    case '[':
                        next();
                        this.token = 14;
                        return;
                    case ']':
                        next();
                        this.token = 15;
                        return;
                    case 'f':
                        scanFalse();
                        return;
                    case 'n':
                        scanNullOrNew();
                        return;
                    case 't':
                        scanTrue();
                        return;
                    case 'x':
                        scanHex();
                        return;
                    case '{':
                        next();
                        this.token = 12;
                        return;
                    case '}':
                        next();
                        this.token = 13;
                        return;
                    default:
                        if (isEOF()) {
                            if (this.token == 20) {
                                throw new JSONException("EOF error");
                            }
                            this.token = 20;
                            int i = this.bp;
                            this.pos = i;
                            this.eofPos = i;
                            return;
                        }
                        char c4 = this.ch;
                        if (c4 <= 31 || c4 == 127) {
                            next();
                        } else {
                            lexError("illegal.char", String.valueOf((int) c4));
                            next();
                            return;
                        }
                        break;
                        break;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:110:0x007b A[SYNTHETIC] */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void nextToken(int r11) {
        /*
            Method dump skipped, instruction units count: 274
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.nextToken(int):void");
    }

    public final void nextIdent() {
        while (isWhitespace(this.ch)) {
            next();
        }
        char c2 = this.ch;
        if (c2 == '_' || c2 == '$' || Character.isLetter(c2)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextTokenWithColon() {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithChar(char c2) {
        this.sp = 0;
        while (true) {
            char c3 = this.ch;
            if (c3 == c2) {
                next();
                nextToken();
                return;
            }
            if (c3 == ' ' || c3 == '\n' || c3 == '\r' || c3 == '\t' || c3 == '\f' || c3 == '\b') {
                next();
            } else {
                throw new JSONException("not match " + c2 + " - " + this.ch + ", info : " + info());
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int token() {
        return this.token;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String tokenName() {
        return JSONToken.name(this.token);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int pos() {
        return this.pos;
    }

    public final String stringDefaultValue() {
        return this.stringDefaultValue;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final Number integerValue() throws NumberFormatException {
        long j;
        long j2;
        boolean z = false;
        if (this.np == -1) {
            this.np = 0;
        }
        int i = this.np;
        int i2 = this.sp + i;
        char c2 = ' ';
        char cCharAt = charAt(i2 - 1);
        if (cCharAt == 'B') {
            i2--;
            c2 = 'B';
        } else if (cCharAt == 'L') {
            i2--;
            c2 = 'L';
        } else if (cCharAt == 'S') {
            i2--;
            c2 = 'S';
        }
        if (charAt(this.np) == '-') {
            j = Long.MIN_VALUE;
            i++;
            z = true;
        } else {
            j = C.TIME_UNSET;
        }
        long j3 = MULTMIN_RADIX_TEN;
        if (i < i2) {
            j2 = -(charAt(i) - '0');
            i++;
        } else {
            j2 = 0;
        }
        while (i < i2) {
            int i3 = i + 1;
            int iCharAt = charAt(i) - '0';
            if (j2 < j3) {
                return new BigInteger(numberString(), 10);
            }
            long j4 = j2 * 10;
            long j5 = iCharAt;
            if (j4 < j + j5) {
                return new BigInteger(numberString(), 10);
            }
            j2 = j4 - j5;
            i = i3;
            j3 = MULTMIN_RADIX_TEN;
        }
        if (!z) {
            long j6 = -j2;
            if (j6 > 2147483647L || c2 == 'L') {
                return Long.valueOf(j6);
            }
            if (c2 == 'S') {
                return Short.valueOf((short) j6);
            }
            if (c2 == 'B') {
                return Byte.valueOf((byte) j6);
            }
            return Integer.valueOf((int) j6);
        }
        if (i <= this.np + 1) {
            throw new JSONException("illegal number format : " + numberString());
        }
        if (j2 < -2147483648L || c2 == 'L') {
            return Long.valueOf(j2);
        }
        if (c2 == 'S') {
            return Short.valueOf((short) j2);
        }
        if (c2 == 'B') {
            return Byte.valueOf((byte) j2);
        }
        return Integer.valueOf((int) j2);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextTokenWithColon(int i) {
        nextTokenWithChar(':');
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public float floatValue() {
        char cCharAt;
        String strNumberString = numberString();
        float f = Float.parseFloat(strNumberString);
        if ((f != 0.0f && f != Float.POSITIVE_INFINITY) || (cCharAt = strNumberString.charAt(0)) <= '0' || cCharAt > '9') {
            return f;
        }
        throw new JSONException("float overflow : " + strNumberString);
    }

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void config(Feature feature, boolean z) {
        this.features = Feature.config(this.features, feature, z);
        if ((this.features & Feature.InitStringFieldAsEmpty.mask) != 0) {
            this.stringDefaultValue = "";
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isEnabled(Feature feature) {
        return isEnabled(feature.mask);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isEnabled(int i) {
        return (i & this.features) != 0;
    }

    public final boolean isEnabled(int i, int i2) {
        return ((this.features & i2) == 0 && (i & i2) == 0) ? false : true;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final char getCurrent() {
        return this.ch;
    }

    protected void skipComment() {
        char c2;
        next();
        char c3 = this.ch;
        if (c3 == '/') {
            do {
                next();
                c2 = this.ch;
                if (c2 == '\n') {
                    next();
                    return;
                }
            } while (c2 != 26);
            return;
        }
        if (c3 == '*') {
            next();
            while (true) {
                char c4 = this.ch;
                if (c4 == 26) {
                    return;
                }
                if (c4 == '*') {
                    next();
                    if (this.ch == '/') {
                        next();
                        return;
                    }
                } else {
                    next();
                }
            }
        } else {
            throw new JSONException("invalid comment");
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbol(SymbolTable symbolTable) {
        skipWhitespace();
        char c2 = this.ch;
        if (c2 == '\"') {
            return scanSymbol(symbolTable, '\"');
        }
        if (c2 == '\'') {
            if (!isEnabled(Feature.AllowSingleQuotes)) {
                throw new JSONException("syntax error");
            }
            return scanSymbol(symbolTable, '\'');
        }
        if (c2 == '}') {
            next();
            this.token = 13;
            return null;
        }
        if (c2 == ',') {
            next();
            this.token = 16;
            return null;
        }
        if (c2 == 26) {
            this.token = 20;
            return null;
        }
        if (!isEnabled(Feature.AllowUnQuotedFieldNames)) {
            throw new JSONException("syntax error");
        }
        return scanSymbolUnQuoted(symbolTable);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbol(SymbolTable symbolTable, char c2) {
        String strAddSymbol;
        this.np = this.bp;
        this.sp = 0;
        boolean z = false;
        int i = 0;
        while (true) {
            char next = next();
            if (next == c2) {
                this.token = 4;
                if (!z) {
                    int i2 = this.np;
                    strAddSymbol = addSymbol(i2 == -1 ? 0 : i2 + 1, this.sp, i, symbolTable);
                } else {
                    strAddSymbol = symbolTable.addSymbol(this.sbuf, 0, this.sp, i);
                }
                this.sp = 0;
                next();
                return strAddSymbol;
            }
            if (next == 26) {
                throw new JSONException("unclosed.str");
            }
            if (next == '\\') {
                if (!z) {
                    int i3 = this.sp;
                    char[] cArr = this.sbuf;
                    if (i3 >= cArr.length) {
                        int length = cArr.length * 2;
                        if (i3 <= length) {
                            i3 = length;
                        }
                        char[] cArr2 = new char[i3];
                        char[] cArr3 = this.sbuf;
                        System.arraycopy(cArr3, 0, cArr2, 0, cArr3.length);
                        this.sbuf = cArr2;
                    }
                    arrayCopy(this.np + 1, this.sbuf, 0, this.sp);
                    z = true;
                }
                char next2 = next();
                switch (next2) {
                    case '/':
                        i = (i * 31) + 47;
                        putChar('/');
                        break;
                    case '0':
                        i = (i * 31) + next2;
                        putChar((char) 0);
                        break;
                    case '1':
                        i = (i * 31) + next2;
                        putChar((char) 1);
                        break;
                    case '2':
                        i = (i * 31) + next2;
                        putChar((char) 2);
                        break;
                    case '3':
                        i = (i * 31) + next2;
                        putChar((char) 3);
                        break;
                    case '4':
                        i = (i * 31) + next2;
                        putChar((char) 4);
                        break;
                    case '5':
                        i = (i * 31) + next2;
                        putChar((char) 5);
                        break;
                    case '6':
                        i = (i * 31) + next2;
                        putChar((char) 6);
                        break;
                    case '7':
                        i = (i * 31) + next2;
                        putChar((char) 7);
                        break;
                    default:
                        switch (next2) {
                            case 't':
                                i = (i * 31) + 9;
                                putChar('\t');
                                break;
                            case 'u':
                                int i4 = Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16);
                                i = (i * 31) + i4;
                                putChar((char) i4);
                                break;
                            case 'v':
                                i = (i * 31) + 11;
                                putChar((char) 11);
                                break;
                            default:
                                switch (next2) {
                                    case '\"':
                                        i = (i * 31) + 34;
                                        putChar('\"');
                                        break;
                                    case '\'':
                                        i = (i * 31) + 39;
                                        putChar('\'');
                                        break;
                                    case 'F':
                                    case 'f':
                                        i = (i * 31) + 12;
                                        putChar('\f');
                                        break;
                                    case '\\':
                                        i = (i * 31) + 92;
                                        putChar('\\');
                                        break;
                                    case 'b':
                                        i = (i * 31) + 8;
                                        putChar('\b');
                                        break;
                                    case 'n':
                                        i = (i * 31) + 10;
                                        putChar('\n');
                                        break;
                                    case 'r':
                                        i = (i * 31) + 13;
                                        putChar(StringUtil.CARRIAGE_RETURN);
                                        break;
                                    case 'x':
                                        char next3 = next();
                                        this.ch = next3;
                                        char next4 = next();
                                        this.ch = next4;
                                        int[] iArr = digits;
                                        char c3 = (char) ((iArr[next3] * 16) + iArr[next4]);
                                        i = (i * 31) + c3;
                                        putChar(c3);
                                        break;
                                    default:
                                        this.ch = next2;
                                        throw new JSONException("unclosed.str.lit");
                                }
                                break;
                        }
                        break;
                }
            } else {
                i = (i * 31) + next;
                if (!z) {
                    this.sp++;
                } else {
                    int i5 = this.sp;
                    char[] cArr4 = this.sbuf;
                    if (i5 == cArr4.length) {
                        putChar(next);
                    } else {
                        this.sp = i5 + 1;
                        cArr4[i5] = next;
                    }
                }
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void resetStringPosition() {
        this.sp = 0;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        if (this.token == 1 && this.pos == 0 && this.bp == 1) {
            this.bp = 0;
        }
        boolean[] zArr = IOUtils.firstIdentifierFlags;
        int i = this.ch;
        if (!(i >= zArr.length || zArr[i])) {
            throw new JSONException("illegal identifier : " + this.ch + info());
        }
        boolean[] zArr2 = IOUtils.identifierFlags;
        this.np = this.bp;
        this.sp = 1;
        while (true) {
            char next = next();
            if (next < zArr2.length && !zArr2[next]) {
                break;
            }
            i = (i * 31) + next;
            this.sp++;
        }
        this.ch = charAt(this.bp);
        this.token = 18;
        if (this.sp == 4 && i == 3392903 && charAt(this.np) == 'n' && charAt(this.np + 1) == 'u' && charAt(this.np + 2) == 'l' && charAt(this.np + 3) == 'l') {
            return null;
        }
        if (symbolTable == null) {
            return subString(this.np, this.sp);
        }
        return addSymbol(this.np, this.sp, i, symbolTable);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void scanString() {
        char next;
        char next2;
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char next3 = next();
            if (next3 == '\"') {
                this.token = 4;
                this.ch = next();
                return;
            }
            if (next3 != 26) {
                boolean z = true;
                if (next3 == '\\') {
                    if (!this.hasSpecial) {
                        this.hasSpecial = true;
                        int i = this.sp;
                        char[] cArr = this.sbuf;
                        if (i >= cArr.length) {
                            int length = cArr.length * 2;
                            if (i <= length) {
                                i = length;
                            }
                            char[] cArr2 = new char[i];
                            char[] cArr3 = this.sbuf;
                            System.arraycopy(cArr3, 0, cArr2, 0, cArr3.length);
                            this.sbuf = cArr2;
                        }
                        copyTo(this.np + 1, this.sp, this.sbuf);
                    }
                    char next4 = next();
                    switch (next4) {
                        case '/':
                            putChar('/');
                            break;
                        case '0':
                            putChar((char) 0);
                            break;
                        case '1':
                            putChar((char) 1);
                            break;
                        case '2':
                            putChar((char) 2);
                            break;
                        case '3':
                            putChar((char) 3);
                            break;
                        case '4':
                            putChar((char) 4);
                            break;
                        case '5':
                            putChar((char) 5);
                            break;
                        case '6':
                            putChar((char) 6);
                            break;
                        case '7':
                            putChar((char) 7);
                            break;
                        default:
                            switch (next4) {
                                case 't':
                                    putChar('\t');
                                    break;
                                case 'u':
                                    putChar((char) Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16));
                                    break;
                                case 'v':
                                    putChar((char) 11);
                                    break;
                                default:
                                    switch (next4) {
                                        case '\"':
                                            putChar('\"');
                                            break;
                                        case '\'':
                                            putChar('\'');
                                            break;
                                        case 'F':
                                        case 'f':
                                            putChar('\f');
                                            break;
                                        case '\\':
                                            putChar('\\');
                                            break;
                                        case 'b':
                                            putChar('\b');
                                            break;
                                        case 'n':
                                            putChar('\n');
                                            break;
                                        case 'r':
                                            putChar(StringUtil.CARRIAGE_RETURN);
                                            break;
                                        case 'x':
                                            next = next();
                                            next2 = next();
                                            boolean z2 = (next >= '0' && next <= '9') || (next >= 'a' && next <= 'f') || (next >= 'A' && next <= 'F');
                                            if ((next2 < '0' || next2 > '9') && ((next2 < 'a' || next2 > 'f') && (next2 < 'A' || next2 > 'F'))) {
                                                z = false;
                                            }
                                            if (z2 && z) {
                                                int[] iArr = digits;
                                                putChar((char) ((iArr[next] * 16) + iArr[next2]));
                                            }
                                            break;
                                        default:
                                            this.ch = next4;
                                            throw new JSONException("unclosed string : " + next4);
                                    }
                                    break;
                            }
                            break;
                    }
                } else if (this.hasSpecial) {
                    int i2 = this.sp;
                    char[] cArr4 = this.sbuf;
                    if (i2 == cArr4.length) {
                        putChar(next3);
                    } else {
                        this.sp = i2 + 1;
                        cArr4[i2] = next3;
                    }
                } else {
                    this.sp++;
                }
            } else if (!isEOF()) {
                putChar(JSONLexer.EOI);
            } else {
                throw new JSONException("unclosed string : " + next3);
            }
        }
        throw new JSONException("invalid escape character \\x" + next + next2);
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public Locale getLocale() {
        return this.locale;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int intValue() {
        int i;
        boolean z;
        int i2 = 0;
        if (this.np == -1) {
            this.np = 0;
        }
        int i3 = this.np;
        int i4 = this.sp + i3;
        if (charAt(i3) == '-') {
            i3++;
            i = Integer.MIN_VALUE;
            z = true;
        } else {
            i = -2147483647;
            z = false;
        }
        if (i3 < i4) {
            i2 = -(charAt(i3) - '0');
            i3++;
        }
        while (i3 < i4) {
            int i5 = i3 + 1;
            char cCharAt = charAt(i3);
            if (cCharAt == 'L' || cCharAt == 'S' || cCharAt == 'B') {
                i3 = i5;
                break;
            }
            int i6 = cCharAt - '0';
            if (i2 < -214748364) {
                throw new NumberFormatException(numberString());
            }
            int i7 = i2 * 10;
            if (i7 < i + i6) {
                throw new NumberFormatException(numberString());
            }
            i2 = i7 - i6;
            i3 = i5;
        }
        if (!z) {
            return -i2;
        }
        if (i3 > this.np + 1) {
            return i2;
        }
        throw new NumberFormatException(numberString());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        char[] cArr = this.sbuf;
        if (cArr.length <= 8192) {
            SBUF_LOCAL.set(cArr);
        }
        this.sbuf = null;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isRef() {
        return this.sp == 4 && charAt(this.np + 1) == '$' && charAt(this.np + 2) == 'r' && charAt(this.np + 3) == 'e' && charAt(this.np + 4) == 'f';
    }

    public final int scanType(String str) {
        this.matchStat = 0;
        if (!charArrayCompare(typeFieldName)) {
            return -2;
        }
        int length = this.bp + typeFieldName.length;
        int length2 = str.length();
        for (int i = 0; i < length2; i++) {
            if (str.charAt(i) != charAt(length + i)) {
                return -1;
            }
        }
        int i2 = length + length2;
        if (charAt(i2) != '\"') {
            return -1;
        }
        int i3 = i2 + 1;
        this.ch = charAt(i3);
        char c2 = this.ch;
        if (c2 == ',') {
            int i4 = i3 + 1;
            this.ch = charAt(i4);
            this.bp = i4;
            this.token = 16;
            return 3;
        }
        if (c2 == '}') {
            i3++;
            this.ch = charAt(i3);
            char c3 = this.ch;
            if (c3 == ',') {
                this.token = 16;
                i3++;
                this.ch = charAt(i3);
            } else if (c3 == ']') {
                this.token = 15;
                i3++;
                this.ch = charAt(i3);
            } else if (c3 == '}') {
                this.token = 13;
                i3++;
                this.ch = charAt(i3);
            } else {
                if (c3 != 26) {
                    return -1;
                }
                this.token = 20;
            }
            this.matchStat = 4;
        }
        this.bp = i3;
        return this.matchStat;
    }

    public final boolean matchField(char[] cArr) {
        while (!charArrayCompare(cArr)) {
            if (!isWhitespace(this.ch)) {
                return false;
            }
            next();
        }
        this.bp += cArr.length;
        this.ch = charAt(this.bp);
        char c2 = this.ch;
        if (c2 == '{') {
            next();
            this.token = 12;
        } else if (c2 == '[') {
            next();
            this.token = 14;
        } else if (c2 == 'S' && charAt(this.bp + 1) == 'e' && charAt(this.bp + 2) == 't' && charAt(this.bp + 3) == '[') {
            this.bp += 3;
            this.ch = charAt(this.bp);
            this.token = 21;
        } else {
            nextToken();
        }
        return true;
    }

    public int matchField(long j) {
        throw new UnsupportedOperationException();
    }

    public boolean seekArrayToItem(int i) {
        throw new UnsupportedOperationException();
    }

    public int seekObjectToField(long j, boolean z) {
        throw new UnsupportedOperationException();
    }

    public int seekObjectToField(long[] jArr) {
        throw new UnsupportedOperationException();
    }

    public int seekObjectToFieldDeepScan(long j) {
        throw new UnsupportedOperationException();
    }

    public void skipObject() {
        throw new UnsupportedOperationException();
    }

    public void skipObject(boolean z) {
        throw new UnsupportedOperationException();
    }

    public void skipArray() {
        throw new UnsupportedOperationException();
    }

    public String scanFieldString(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return stringDefaultValue();
        }
        int length = cArr.length;
        int i = length + 1;
        if (charAt(this.bp + length) != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int iIndexOf = indexOf('\"', this.bp + cArr.length + 1);
        if (iIndexOf == -1) {
            throw new JSONException("unclosed str");
        }
        int length2 = this.bp + cArr.length + 1;
        String strSubString = subString(length2, iIndexOf - length2);
        if (strSubString.indexOf(92) != -1) {
            while (true) {
                int i2 = 0;
                for (int i3 = iIndexOf - 1; i3 >= 0 && charAt(i3) == '\\'; i3--) {
                    i2++;
                }
                if (i2 % 2 == 0) {
                    break;
                }
                iIndexOf = indexOf('\"', iIndexOf + 1);
            }
            int i4 = this.bp;
            int length3 = iIndexOf - ((cArr.length + i4) + 1);
            strSubString = readString(sub_chars(i4 + cArr.length + 1, length3), length3);
        }
        int i5 = this.bp;
        int length4 = i + (iIndexOf - ((cArr.length + i5) + 1)) + 1;
        int i6 = length4 + 1;
        char cCharAt = charAt(i5 + length4);
        if (cCharAt == ',') {
            this.bp += i6;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return strSubString;
        }
        if (cCharAt == '}') {
            int i7 = i6 + 1;
            char cCharAt2 = charAt(this.bp + i6);
            if (cCharAt2 == ',') {
                this.token = 16;
                this.bp += i7;
                this.ch = charAt(this.bp);
            } else if (cCharAt2 == ']') {
                this.token = 15;
                this.bp += i7;
                this.ch = charAt(this.bp);
            } else if (cCharAt2 == '}') {
                this.token = 13;
                this.bp += i7;
                this.ch = charAt(this.bp);
            } else if (cCharAt2 == 26) {
                this.token = 20;
                this.bp += i7 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            this.matchStat = 4;
            return strSubString;
        }
        this.matchStat = -1;
        return stringDefaultValue();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanString(char c2) {
        this.matchStat = 0;
        char cCharAt = charAt(this.bp + 0);
        if (cCharAt == 'n') {
            if (charAt(this.bp + 1) == 'u' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 'l') {
                if (charAt(this.bp + 4) == c2) {
                    this.bp += 5;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    return null;
                }
                this.matchStat = -1;
                return null;
            }
            this.matchStat = -1;
            return null;
        }
        int i = 1;
        while (cCharAt != '\"') {
            if (isWhitespace(cCharAt)) {
                cCharAt = charAt(this.bp + i);
                i++;
            } else {
                this.matchStat = -1;
                return stringDefaultValue();
            }
        }
        int i2 = this.bp + i;
        int iIndexOf = indexOf('\"', i2);
        if (iIndexOf == -1) {
            throw new JSONException("unclosed str");
        }
        String strSubString = subString(this.bp + i, iIndexOf - i2);
        if (strSubString.indexOf(92) != -1) {
            while (true) {
                int i3 = 0;
                for (int i4 = iIndexOf - 1; i4 >= 0 && charAt(i4) == '\\'; i4--) {
                    i3++;
                }
                if (i3 % 2 == 0) {
                    break;
                }
                iIndexOf = indexOf('\"', iIndexOf + 1);
            }
            int i5 = iIndexOf - i2;
            strSubString = readString(sub_chars(this.bp + 1, i5), i5);
        }
        int i6 = i + (iIndexOf - i2) + 1;
        int i7 = i6 + 1;
        char cCharAt2 = charAt(this.bp + i6);
        while (cCharAt2 != c2) {
            if (!isWhitespace(cCharAt2)) {
                if (cCharAt2 == ']') {
                    this.bp += i7;
                    this.ch = charAt(this.bp);
                    this.matchStat = -1;
                }
                return strSubString;
            }
            cCharAt2 = charAt(this.bp + i7);
            i7++;
        }
        this.bp += i7;
        this.ch = charAt(this.bp);
        this.matchStat = 3;
        this.token = 16;
        return strSubString;
    }

    public long scanFieldSymbol(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = cArr.length;
        int i = length + 1;
        if (charAt(this.bp + length) != '\"') {
            this.matchStat = -1;
            return 0L;
        }
        long j = TypeUtils.fnv1a_64_magic_hashcode;
        while (true) {
            int i2 = i + 1;
            char cCharAt = charAt(this.bp + i);
            if (cCharAt == '\"') {
                int i3 = i2 + 1;
                char cCharAt2 = charAt(this.bp + i2);
                if (cCharAt2 == ',') {
                    this.bp += i3;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    return j;
                }
                if (cCharAt2 == '}') {
                    int i4 = i3 + 1;
                    char cCharAt3 = charAt(this.bp + i3);
                    if (cCharAt3 == ',') {
                        this.token = 16;
                        this.bp += i4;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt3 == ']') {
                        this.token = 15;
                        this.bp += i4;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt3 == '}') {
                        this.token = 13;
                        this.bp += i4;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt3 == 26) {
                        this.token = 20;
                        this.bp += i4 - 1;
                        this.ch = JSONLexer.EOI;
                    } else {
                        this.matchStat = -1;
                        return 0L;
                    }
                    this.matchStat = 4;
                    return j;
                }
                this.matchStat = -1;
                return 0L;
            }
            j = (j ^ ((long) cCharAt)) * TypeUtils.fnv1a_64_magic_prime;
            if (cCharAt == '\\') {
                this.matchStat = -1;
                return 0L;
            }
            i = i2;
        }
    }

    public long scanEnumSymbol(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = cArr.length;
        int i = length + 1;
        if (charAt(this.bp + length) != '\"') {
            this.matchStat = -1;
            return 0L;
        }
        long j = TypeUtils.fnv1a_64_magic_hashcode;
        while (true) {
            int i2 = i + 1;
            char cCharAt = charAt(this.bp + i);
            if (cCharAt == '\"') {
                int i3 = i2 + 1;
                char cCharAt2 = charAt(this.bp + i2);
                if (cCharAt2 == ',') {
                    this.bp += i3;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    return j;
                }
                if (cCharAt2 == '}') {
                    int i4 = i3 + 1;
                    char cCharAt3 = charAt(this.bp + i3);
                    if (cCharAt3 == ',') {
                        this.token = 16;
                        this.bp += i4;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt3 == ']') {
                        this.token = 15;
                        this.bp += i4;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt3 == '}') {
                        this.token = 13;
                        this.bp += i4;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt3 == 26) {
                        this.token = 20;
                        this.bp += i4 - 1;
                        this.ch = JSONLexer.EOI;
                    } else {
                        this.matchStat = -1;
                        return 0L;
                    }
                    this.matchStat = 4;
                    return j;
                }
                this.matchStat = -1;
                return 0L;
            }
            j = (j ^ ((long) ((cCharAt < 'A' || cCharAt > 'Z') ? cCharAt : cCharAt + ' '))) * TypeUtils.fnv1a_64_magic_prime;
            if (cCharAt == '\\') {
                this.matchStat = -1;
                return 0L;
            }
            i = i2;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public Enum<?> scanEnum(Class<?> cls, SymbolTable symbolTable, char c2) {
        String strScanSymbolWithSeperator = scanSymbolWithSeperator(symbolTable, c2);
        if (strScanSymbolWithSeperator == null) {
            return null;
        }
        return Enum.valueOf(cls, strScanSymbolWithSeperator);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanSymbolWithSeperator(SymbolTable symbolTable, char c2) {
        this.matchStat = 0;
        char cCharAt = charAt(this.bp + 0);
        if (cCharAt == 'n') {
            if (charAt(this.bp + 1) == 'u' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 'l') {
                if (charAt(this.bp + 4) == c2) {
                    this.bp += 5;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    return null;
                }
                this.matchStat = -1;
                return null;
            }
            this.matchStat = -1;
            return null;
        }
        if (cCharAt != '\"') {
            this.matchStat = -1;
            return null;
        }
        int i = 0;
        int i2 = 1;
        while (true) {
            int i3 = i2 + 1;
            char cCharAt2 = charAt(this.bp + i2);
            if (cCharAt2 == '\"') {
                int i4 = this.bp;
                int i5 = i4 + 0 + 1;
                String strAddSymbol = addSymbol(i5, ((i4 + i3) - i5) - 1, i, symbolTable);
                int i6 = i3 + 1;
                char cCharAt3 = charAt(this.bp + i3);
                while (cCharAt3 != c2) {
                    if (isWhitespace(cCharAt3)) {
                        cCharAt3 = charAt(this.bp + i6);
                        i6++;
                    } else {
                        this.matchStat = -1;
                        return strAddSymbol;
                    }
                }
                this.bp += i6;
                this.ch = charAt(this.bp);
                this.matchStat = 3;
                return strAddSymbol;
            }
            i = (i * 31) + cCharAt2;
            if (cCharAt2 == '\\') {
                this.matchStat = -1;
                return null;
            }
            i2 = i3;
        }
    }

    public Collection<String> newCollectionByType(Class<?> cls) {
        if (cls.isAssignableFrom(HashSet.class)) {
            return new HashSet();
        }
        if (cls.isAssignableFrom(ArrayList.class)) {
            return new ArrayList();
        }
        if (cls.isAssignableFrom(LinkedList.class)) {
            return new LinkedList();
        }
        try {
            return (Collection) cls.newInstance();
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ea, code lost:
    
        if (r12 != ']') goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00f0, code lost:
    
        if (r13.size() != 0) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f2, code lost:
    
        r0 = r1 + 1;
        r12 = charAt(r11.bp + r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0179, code lost:
    
        throw new com.alibaba.fastjson.JSONException("illega str");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.Collection<java.lang.String> scanFieldStringArray(char[] r12, java.lang.Class<?> r13) {
        /*
            Method dump skipped, instruction units count: 378
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldStringArray(char[], java.lang.Class):java.util.Collection");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void scanStringArray(Collection<String> collection, char c2) {
        int i;
        char cCharAt;
        int i2;
        char cCharAt2;
        this.matchStat = 0;
        char cCharAt3 = charAt(this.bp + 0);
        char c3 = 'u';
        char c4 = 'n';
        if (cCharAt3 == 'n' && charAt(this.bp + 1) == 'u' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 'l' && charAt(this.bp + 1 + 3) == c2) {
            this.bp += 5;
            this.ch = charAt(this.bp);
            this.matchStat = 5;
            return;
        }
        if (cCharAt3 != '[') {
            this.matchStat = -1;
            return;
        }
        char cCharAt4 = charAt(this.bp + 1);
        int i3 = 2;
        while (true) {
            if (cCharAt4 == c4 && charAt(this.bp + i3) == c3 && charAt(this.bp + i3 + 1) == 'l' && charAt(this.bp + i3 + 2) == 'l') {
                int i4 = i3 + 3;
                i = i4 + 1;
                cCharAt = charAt(this.bp + i4);
                collection.add(null);
            } else {
                if (cCharAt4 == ']' && collection.size() == 0) {
                    i2 = i3 + 1;
                    cCharAt2 = charAt(this.bp + i3);
                    break;
                }
                if (cCharAt4 != '\"') {
                    this.matchStat = -1;
                    return;
                }
                int i5 = this.bp + i3;
                int iIndexOf = indexOf('\"', i5);
                if (iIndexOf == -1) {
                    throw new JSONException("unclosed str");
                }
                String strSubString = subString(this.bp + i3, iIndexOf - i5);
                if (strSubString.indexOf(92) != -1) {
                    while (true) {
                        int i6 = 0;
                        for (int i7 = iIndexOf - 1; i7 >= 0 && charAt(i7) == '\\'; i7--) {
                            i6++;
                        }
                        if (i6 % 2 == 0) {
                            break;
                        } else {
                            iIndexOf = indexOf('\"', iIndexOf + 1);
                        }
                    }
                    int i8 = iIndexOf - i5;
                    strSubString = readString(sub_chars(this.bp + i3, i8), i8);
                }
                int i9 = this.bp;
                int i10 = i3 + (iIndexOf - (i9 + i3)) + 1;
                i = i10 + 1;
                cCharAt = charAt(i9 + i10);
                collection.add(strSubString);
            }
            if (cCharAt == ',') {
                i3 = i + 1;
                cCharAt4 = charAt(this.bp + i);
                c3 = 'u';
                c4 = 'n';
            } else if (cCharAt == ']') {
                i2 = i + 1;
                cCharAt2 = charAt(this.bp + i);
            } else {
                this.matchStat = -1;
                return;
            }
        }
        if (cCharAt2 == c2) {
            this.bp += i2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return;
        }
        this.matchStat = -1;
    }

    public int scanFieldInt(char[] cArr) {
        int i;
        char cCharAt;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char cCharAt2 = charAt(this.bp + length);
        boolean z = cCharAt2 == '-';
        if (z) {
            cCharAt2 = charAt(this.bp + i2);
            i2++;
        }
        if (cCharAt2 < '0' || cCharAt2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i3 = cCharAt2 - '0';
        while (true) {
            i = i2 + 1;
            cCharAt = charAt(this.bp + i2);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            i3 = (i3 * 10) + (cCharAt - '0');
            i2 = i;
        }
        if (cCharAt == '.') {
            this.matchStat = -1;
            return 0;
        }
        if ((i3 < 0 || i > cArr.length + 14) && !(i3 == Integer.MIN_VALUE && i == 17 && z)) {
            this.matchStat = -1;
            return 0;
        }
        if (cCharAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z ? -i3 : i3;
        }
        if (cCharAt == '}') {
            int i4 = i + 1;
            char cCharAt3 = charAt(this.bp + i);
            if (cCharAt3 == ',') {
                this.token = 16;
                this.bp += i4;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == ']') {
                this.token = 15;
                this.bp += i4;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == '}') {
                this.token = 13;
                this.bp += i4;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == 26) {
                this.token = 20;
                this.bp += i4 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return 0;
            }
            this.matchStat = 4;
            return z ? -i3 : i3;
        }
        this.matchStat = -1;
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:65:0x0122, code lost:
    
        r2 = r4;
        r18.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0125, code lost:
    
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int[] scanFieldIntArray(char[] r19) {
        /*
            Method dump skipped, instruction units count: 294
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldIntArray(char[]):int[]");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public boolean scanBoolean(char c2) {
        boolean z = false;
        this.matchStat = 0;
        char cCharAt = charAt(this.bp + 0);
        int i = 2;
        if (cCharAt == 't') {
            if (charAt(this.bp + 1) == 'r' && charAt(this.bp + 1 + 1) == 'u' && charAt(this.bp + 1 + 2) == 'e') {
                cCharAt = charAt(this.bp + 4);
                i = 5;
                z = true;
            } else {
                this.matchStat = -1;
                return false;
            }
        } else if (cCharAt == 'f') {
            if (charAt(this.bp + 1) == 'a' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 's' && charAt(this.bp + 1 + 3) == 'e') {
                cCharAt = charAt(this.bp + 5);
                i = 6;
            } else {
                this.matchStat = -1;
                return false;
            }
        } else if (cCharAt == '1') {
            cCharAt = charAt(this.bp + 1);
            z = true;
        } else if (cCharAt == '0') {
            cCharAt = charAt(this.bp + 1);
        } else {
            i = 1;
        }
        while (cCharAt != c2) {
            if (isWhitespace(cCharAt)) {
                cCharAt = charAt(this.bp + i);
                i++;
            } else {
                this.matchStat = -1;
                return z;
            }
        }
        this.bp += i;
        this.ch = charAt(this.bp);
        this.matchStat = 3;
        return z;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public int scanInt(char c2) {
        int i;
        int i2;
        int i3;
        char cCharAt;
        this.matchStat = 0;
        char cCharAt2 = charAt(this.bp + 0);
        boolean z = cCharAt2 == '\"';
        if (z) {
            cCharAt2 = charAt(this.bp + 1);
            i = 2;
        } else {
            i = 1;
        }
        boolean z2 = cCharAt2 == '-';
        if (z2) {
            cCharAt2 = charAt(this.bp + i);
            i++;
        }
        if (cCharAt2 >= '0' && cCharAt2 <= '9') {
            int i4 = cCharAt2 - '0';
            while (true) {
                i3 = i + 1;
                cCharAt = charAt(this.bp + i);
                if (cCharAt < '0' || cCharAt > '9') {
                    break;
                }
                i4 = (i4 * 10) + (cCharAt - '0');
                i = i3;
            }
            if (cCharAt == '.') {
                this.matchStat = -1;
                return 0;
            }
            if (i4 < 0) {
                this.matchStat = -1;
                return 0;
            }
            while (cCharAt != c2) {
                if (isWhitespace(cCharAt)) {
                    char cCharAt3 = charAt(this.bp + i3);
                    i3++;
                    cCharAt = cCharAt3;
                } else {
                    this.matchStat = -1;
                    return z2 ? -i4 : i4;
                }
            }
            this.bp += i3;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z2 ? -i4 : i4;
        }
        if (cCharAt2 == 'n' && charAt(this.bp + i) == 'u' && charAt(this.bp + i + 1) == 'l' && charAt(this.bp + i + 2) == 'l') {
            this.matchStat = 5;
            int i5 = i + 3;
            int i6 = i5 + 1;
            char cCharAt4 = charAt(this.bp + i5);
            if (z && cCharAt4 == '\"') {
                i2 = i6 + 1;
                cCharAt4 = charAt(this.bp + i6);
            } else {
                i2 = i6;
            }
            while (cCharAt4 != ',') {
                if (cCharAt4 == ']') {
                    this.bp += i2;
                    this.ch = charAt(this.bp);
                    this.matchStat = 5;
                    this.token = 15;
                    return 0;
                }
                if (isWhitespace(cCharAt4)) {
                    cCharAt4 = charAt(this.bp + i2);
                    i2++;
                } else {
                    this.matchStat = -1;
                    return 0;
                }
            }
            this.bp += i2;
            this.ch = charAt(this.bp);
            this.matchStat = 5;
            this.token = 16;
            return 0;
        }
        this.matchStat = -1;
        return 0;
    }

    public boolean scanFieldBoolean(char[] cArr) {
        boolean z;
        int i;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return false;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char cCharAt = charAt(this.bp + length);
        if (cCharAt == 't') {
            int i3 = i2 + 1;
            if (charAt(this.bp + i2) != 'r') {
                this.matchStat = -1;
                return false;
            }
            int i4 = i3 + 1;
            if (charAt(this.bp + i3) != 'u') {
                this.matchStat = -1;
                return false;
            }
            i = i4 + 1;
            if (charAt(this.bp + i4) != 'e') {
                this.matchStat = -1;
                return false;
            }
            z = true;
        } else if (cCharAt == 'f') {
            int i5 = i2 + 1;
            if (charAt(this.bp + i2) != 'a') {
                this.matchStat = -1;
                return false;
            }
            int i6 = i5 + 1;
            if (charAt(this.bp + i5) != 'l') {
                this.matchStat = -1;
                return false;
            }
            int i7 = i6 + 1;
            if (charAt(this.bp + i6) != 's') {
                this.matchStat = -1;
                return false;
            }
            int i8 = i7 + 1;
            if (charAt(this.bp + i7) != 'e') {
                this.matchStat = -1;
                return false;
            }
            z = false;
            i = i8;
        } else {
            this.matchStat = -1;
            return false;
        }
        int i9 = i + 1;
        char cCharAt2 = charAt(this.bp + i);
        if (cCharAt2 == ',') {
            this.bp += i9;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z;
        }
        if (cCharAt2 == '}') {
            int i10 = i9 + 1;
            char cCharAt3 = charAt(this.bp + i9);
            if (cCharAt3 == ',') {
                this.token = 16;
                this.bp += i10;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == ']') {
                this.token = 15;
                this.bp += i10;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == '}') {
                this.token = 13;
                this.bp += i10;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == 26) {
                this.token = 20;
                this.bp += i10 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return false;
            }
            this.matchStat = 4;
            return z;
        }
        this.matchStat = -1;
        return false;
    }

    public long scanFieldLong(char[] cArr) {
        int i;
        boolean z;
        int i2;
        char cCharAt;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = cArr.length;
        int i3 = length + 1;
        char cCharAt2 = charAt(this.bp + length);
        if (cCharAt2 == '-') {
            i = i3 + 1;
            cCharAt2 = charAt(this.bp + i3);
            z = true;
        } else {
            i = i3;
            z = false;
        }
        if (cCharAt2 < '0' || cCharAt2 > '9') {
            this.matchStat = -1;
            return 0L;
        }
        long j = cCharAt2 - '0';
        while (true) {
            i2 = i + 1;
            cCharAt = charAt(this.bp + i);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            j = (j * 10) + ((long) (cCharAt - '0'));
            i = i2;
        }
        if (cCharAt == '.') {
            this.matchStat = -1;
            return 0L;
        }
        if (!(i2 - cArr.length < 21 && (j >= 0 || (j == Long.MIN_VALUE && z)))) {
            this.matchStat = -1;
            return 0L;
        }
        if (cCharAt == ',') {
            this.bp += i2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z ? -j : j;
        }
        if (cCharAt == '}') {
            int i4 = i2 + 1;
            char cCharAt3 = charAt(this.bp + i2);
            if (cCharAt3 == ',') {
                this.token = 16;
                this.bp += i4;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == ']') {
                this.token = 15;
                this.bp += i4;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == '}') {
                this.token = 13;
                this.bp += i4;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == 26) {
                this.token = 20;
                this.bp += i4 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return 0L;
            }
            this.matchStat = 4;
            return z ? -j : j;
        }
        this.matchStat = -1;
        return 0L;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public long scanLong(char c2) {
        int i;
        int i2;
        int i3;
        char cCharAt;
        char c3;
        this.matchStat = 0;
        char cCharAt2 = charAt(this.bp + 0);
        boolean z = cCharAt2 == '\"';
        if (z) {
            cCharAt2 = charAt(this.bp + 1);
            i = 2;
        } else {
            i = 1;
        }
        boolean z2 = cCharAt2 == '-';
        if (z2) {
            cCharAt2 = charAt(this.bp + i);
            i++;
        }
        if (cCharAt2 < '0' || cCharAt2 > '9') {
            if (cCharAt2 == 'n' && charAt(this.bp + i) == 'u' && charAt(this.bp + i + 1) == 'l' && charAt(this.bp + i + 2) == 'l') {
                this.matchStat = 5;
                int i4 = i + 3;
                int i5 = i4 + 1;
                char cCharAt3 = charAt(this.bp + i4);
                if (z && cCharAt3 == '\"') {
                    i2 = i5 + 1;
                    cCharAt3 = charAt(this.bp + i5);
                } else {
                    i2 = i5;
                }
                while (cCharAt3 != ',') {
                    if (cCharAt3 == ']') {
                        this.bp += i2;
                        this.ch = charAt(this.bp);
                        this.matchStat = 5;
                        this.token = 15;
                        return 0L;
                    }
                    if (isWhitespace(cCharAt3)) {
                        cCharAt3 = charAt(this.bp + i2);
                        i2++;
                    } else {
                        this.matchStat = -1;
                        return 0L;
                    }
                }
                this.bp += i2;
                this.ch = charAt(this.bp);
                this.matchStat = 5;
                this.token = 16;
                return 0L;
            }
            this.matchStat = -1;
            return 0L;
        }
        long j = cCharAt2 - '0';
        while (true) {
            i3 = i + 1;
            cCharAt = charAt(this.bp + i);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            j = (j * 10) + ((long) (cCharAt - '0'));
            i = i3;
        }
        if (cCharAt == '.') {
            this.matchStat = -1;
            return 0L;
        }
        if (!(j >= 0 || (j == Long.MIN_VALUE && z2))) {
            throw new NumberFormatException(subString(this.bp, i3 - 1));
        }
        if (!z) {
            c3 = c2;
        } else {
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return 0L;
            }
            cCharAt = charAt(this.bp + i3);
            c3 = c2;
            i3++;
        }
        while (cCharAt != c3) {
            if (isWhitespace(cCharAt)) {
                cCharAt = charAt(this.bp + i3);
                i3++;
            } else {
                this.matchStat = -1;
                return j;
            }
        }
        this.bp += i3;
        this.ch = charAt(this.bp);
        this.matchStat = 3;
        this.token = 16;
        return z2 ? -j : j;
    }

    public final float scanFieldFloat(char[] cArr) {
        int i;
        char cCharAt;
        boolean z;
        long j;
        int length;
        int i2;
        char cCharAt2;
        float f;
        int i3;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0.0f;
        }
        int length2 = cArr.length;
        int i4 = length2 + 1;
        char cCharAt3 = charAt(this.bp + length2);
        boolean z2 = cCharAt3 == '\"';
        if (z2) {
            cCharAt3 = charAt(this.bp + i4);
            i4++;
        }
        boolean z3 = cCharAt3 == '-';
        if (z3) {
            cCharAt3 = charAt(this.bp + i4);
            i4++;
        }
        if (cCharAt3 >= '0') {
            char c2 = '9';
            if (cCharAt3 <= '9') {
                long j2 = cCharAt3 - '0';
                while (true) {
                    i = i4 + 1;
                    cCharAt = charAt(this.bp + i4);
                    if (cCharAt < '0' || cCharAt > '9') {
                        break;
                    }
                    j2 = (j2 * 10) + ((long) (cCharAt - '0'));
                    i4 = i;
                }
                if (cCharAt == '.') {
                    int i5 = i + 1;
                    char cCharAt4 = charAt(this.bp + i);
                    if (cCharAt4 < '0' || cCharAt4 > '9') {
                        this.matchStat = -1;
                        return 0.0f;
                    }
                    z = z2;
                    j2 = (j2 * 10) + ((long) (cCharAt4 - '0'));
                    j = 10;
                    while (true) {
                        i3 = i5 + 1;
                        cCharAt = charAt(this.bp + i5);
                        if (cCharAt < '0' || cCharAt > c2) {
                            break;
                        }
                        j2 = (j2 * 10) + ((long) (cCharAt - '0'));
                        j *= 10;
                        i5 = i3;
                        c2 = '9';
                    }
                    i = i3;
                } else {
                    z = z2;
                    j = 1;
                }
                boolean z4 = cCharAt == 'e' || cCharAt == 'E';
                if (z4) {
                    int i6 = i + 1;
                    char cCharAt5 = charAt(this.bp + i);
                    if (cCharAt5 == '+' || cCharAt5 == '-') {
                        int i7 = i6 + 1;
                        cCharAt = charAt(this.bp + i6);
                        i = i7;
                    } else {
                        i = i6;
                        cCharAt = cCharAt5;
                    }
                    while (cCharAt >= '0' && cCharAt <= '9') {
                        cCharAt = charAt(this.bp + i);
                        i++;
                    }
                }
                if (!z) {
                    int i8 = this.bp;
                    length = cArr.length + i8;
                    i2 = ((i8 + i) - length) - 1;
                    cCharAt2 = cCharAt;
                } else {
                    if (cCharAt != '\"') {
                        this.matchStat = -1;
                        return 0.0f;
                    }
                    int i9 = i + 1;
                    cCharAt2 = charAt(this.bp + i);
                    int i10 = this.bp;
                    length = cArr.length + i10 + 1;
                    i2 = ((i10 + i9) - length) - 2;
                    i = i9;
                }
                if (z4 || i2 >= 17) {
                    f = Float.parseFloat(subString(length, i2));
                } else {
                    f = (float) (j2 / j);
                    if (z3) {
                        f = -f;
                    }
                }
                if (cCharAt2 == ',') {
                    this.bp += i;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    this.token = 16;
                    return f;
                }
                if (cCharAt2 == '}') {
                    int i11 = i + 1;
                    char cCharAt6 = charAt(this.bp + i);
                    if (cCharAt6 == ',') {
                        this.token = 16;
                        this.bp += i11;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt6 == ']') {
                        this.token = 15;
                        this.bp += i11;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt6 == '}') {
                        this.token = 13;
                        this.bp += i11;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt6 == 26) {
                        this.bp += i11 - 1;
                        this.token = 20;
                        this.ch = JSONLexer.EOI;
                    } else {
                        this.matchStat = -1;
                        return 0.0f;
                    }
                    this.matchStat = 4;
                    return f;
                }
                this.matchStat = -1;
                return 0.0f;
            }
        }
        boolean z5 = z2;
        if (cCharAt3 == 'n' && charAt(this.bp + i4) == 'u' && charAt(this.bp + i4 + 1) == 'l' && charAt(this.bp + i4 + 2) == 'l') {
            this.matchStat = 5;
            int i12 = i4 + 3;
            int i13 = i12 + 1;
            char cCharAt7 = charAt(this.bp + i12);
            if (z5 && cCharAt7 == '\"') {
                cCharAt7 = charAt(this.bp + i13);
                i13++;
            }
            while (cCharAt7 != ',') {
                if (cCharAt7 == '}') {
                    this.bp += i13;
                    this.ch = charAt(this.bp);
                    this.matchStat = 5;
                    this.token = 13;
                    return 0.0f;
                }
                if (isWhitespace(cCharAt7)) {
                    cCharAt7 = charAt(this.bp + i13);
                    i13++;
                } else {
                    this.matchStat = -1;
                    return 0.0f;
                }
            }
            this.bp += i13;
            this.ch = charAt(this.bp);
            this.matchStat = 5;
            this.token = 16;
            return 0.0f;
        }
        this.matchStat = -1;
        return 0.0f;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final float scanFloat(char c2) {
        int i;
        int i2;
        int i3;
        char cCharAt;
        long j;
        int i4;
        int i5;
        float f;
        int i6;
        this.matchStat = 0;
        char cCharAt2 = charAt(this.bp + 0);
        boolean z = cCharAt2 == '\"';
        if (z) {
            cCharAt2 = charAt(this.bp + 1);
            i = 2;
        } else {
            i = 1;
        }
        boolean z2 = cCharAt2 == '-';
        if (z2) {
            cCharAt2 = charAt(this.bp + i);
            i++;
        }
        if (cCharAt2 < '0' || cCharAt2 > '9') {
            if (cCharAt2 == 'n' && charAt(this.bp + i) == 'u' && charAt(this.bp + i + 1) == 'l' && charAt(this.bp + i + 2) == 'l') {
                this.matchStat = 5;
                int i7 = i + 3;
                int i8 = i7 + 1;
                char cCharAt3 = charAt(this.bp + i7);
                if (z && cCharAt3 == '\"') {
                    i2 = i8 + 1;
                    cCharAt3 = charAt(this.bp + i8);
                } else {
                    i2 = i8;
                }
                while (cCharAt3 != ',') {
                    if (cCharAt3 == ']') {
                        this.bp += i2;
                        this.ch = charAt(this.bp);
                        this.matchStat = 5;
                        this.token = 15;
                        return 0.0f;
                    }
                    if (isWhitespace(cCharAt3)) {
                        cCharAt3 = charAt(this.bp + i2);
                        i2++;
                    } else {
                        this.matchStat = -1;
                        return 0.0f;
                    }
                }
                this.bp += i2;
                this.ch = charAt(this.bp);
                this.matchStat = 5;
                this.token = 16;
                return 0.0f;
            }
            this.matchStat = -1;
            return 0.0f;
        }
        long j2 = cCharAt2 - '0';
        while (true) {
            i3 = i + 1;
            cCharAt = charAt(this.bp + i);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            j2 = (j2 * 10) + ((long) (cCharAt - '0'));
            i = i3;
        }
        if (cCharAt == '.') {
            int i9 = i3 + 1;
            char cCharAt4 = charAt(this.bp + i3);
            if (cCharAt4 < '0' || cCharAt4 > '9') {
                this.matchStat = -1;
                return 0.0f;
            }
            j2 = (j2 * 10) + ((long) (cCharAt4 - '0'));
            long j3 = 10;
            while (true) {
                i6 = i9 + 1;
                cCharAt = charAt(this.bp + i9);
                if (cCharAt < '0' || cCharAt > '9') {
                    break;
                }
                j2 = (j2 * 10) + ((long) (cCharAt - '0'));
                j3 *= 10;
                i9 = i6;
            }
            i3 = i6;
            j = j3;
        } else {
            j = 1;
        }
        boolean z3 = cCharAt == 'e' || cCharAt == 'E';
        if (z3) {
            int i10 = i3 + 1;
            char cCharAt5 = charAt(this.bp + i3);
            if (cCharAt5 == '+' || cCharAt5 == '-') {
                int i11 = i10 + 1;
                cCharAt = charAt(this.bp + i10);
                i3 = i11;
            } else {
                i3 = i10;
                cCharAt = cCharAt5;
            }
            while (cCharAt >= '0' && cCharAt <= '9') {
                cCharAt = charAt(this.bp + i3);
                i3++;
            }
        }
        if (!z) {
            i4 = this.bp;
            i5 = ((i4 + i3) - i4) - 1;
        } else {
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return 0.0f;
            }
            int i12 = i3 + 1;
            cCharAt = charAt(this.bp + i3);
            int i13 = this.bp;
            i4 = i13 + 1;
            i5 = ((i13 + i12) - i4) - 2;
            i3 = i12;
        }
        if (z3 || i5 >= 17) {
            f = Float.parseFloat(subString(i4, i5));
        } else {
            f = (float) (j2 / j);
            if (z2) {
                f = -f;
            }
        }
        if (cCharAt == c2) {
            this.bp += i3;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return f;
        }
        this.matchStat = -1;
        return f;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public double scanDouble(char c2) {
        int i;
        int i2;
        char cCharAt;
        boolean z;
        long j;
        int i3;
        int i4;
        char cCharAt2;
        int i5;
        double d2;
        int i6;
        char cCharAt3;
        this.matchStat = 0;
        char cCharAt4 = charAt(this.bp + 0);
        boolean z2 = cCharAt4 == '\"';
        if (z2) {
            cCharAt4 = charAt(this.bp + 1);
            i = 2;
        } else {
            i = 1;
        }
        boolean z3 = cCharAt4 == '-';
        if (z3) {
            cCharAt4 = charAt(this.bp + i);
            i++;
        }
        if (cCharAt4 < '0' || cCharAt4 > '9') {
            if (cCharAt4 == 'n' && charAt(this.bp + i) == 'u' && charAt(this.bp + i + 1) == 'l' && charAt(this.bp + i + 2) == 'l') {
                this.matchStat = 5;
                int i7 = i + 3;
                int i8 = i7 + 1;
                char cCharAt5 = charAt(this.bp + i7);
                if (z2 && cCharAt5 == '\"') {
                    cCharAt5 = charAt(this.bp + i8);
                    i8++;
                }
                while (cCharAt5 != ',') {
                    if (cCharAt5 == ']') {
                        this.bp += i8;
                        this.ch = charAt(this.bp);
                        this.matchStat = 5;
                        this.token = 15;
                        return 0.0d;
                    }
                    if (isWhitespace(cCharAt5)) {
                        cCharAt5 = charAt(this.bp + i8);
                        i8++;
                    } else {
                        this.matchStat = -1;
                        return 0.0d;
                    }
                }
                this.bp += i8;
                this.ch = charAt(this.bp);
                this.matchStat = 5;
                this.token = 16;
                return 0.0d;
            }
            this.matchStat = -1;
            return 0.0d;
        }
        long j2 = cCharAt4 - '0';
        while (true) {
            i2 = i + 1;
            cCharAt = charAt(this.bp + i);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            j2 = (j2 * 10) + ((long) (cCharAt - '0'));
            i = i2;
        }
        if (cCharAt == '.') {
            int i9 = i2 + 1;
            char cCharAt6 = charAt(this.bp + i2);
            if (cCharAt6 < '0' || cCharAt6 > '9') {
                this.matchStat = -1;
                return 0.0d;
            }
            j2 = (j2 * 10) + ((long) (cCharAt6 - '0'));
            long j3 = 10;
            while (true) {
                i6 = i9 + 1;
                cCharAt3 = charAt(this.bp + i9);
                if (cCharAt3 < '0' || cCharAt3 > '9') {
                    break;
                }
                j2 = (j2 * 10) + ((long) (cCharAt3 - '0'));
                j3 *= 10;
                i9 = i6;
                z3 = z3;
            }
            z = z3;
            i2 = i6;
            long j4 = j3;
            cCharAt = cCharAt3;
            j = j4;
        } else {
            z = z3;
            j = 1;
        }
        boolean z4 = cCharAt == 'e' || cCharAt == 'E';
        if (z4) {
            int i10 = i2 + 1;
            cCharAt = charAt(this.bp + i2);
            if (cCharAt == '+' || cCharAt == '-') {
                cCharAt = charAt(this.bp + i10);
                i2 = i10 + 1;
            } else {
                i2 = i10;
            }
            while (cCharAt >= '0' && cCharAt <= '9') {
                cCharAt = charAt(this.bp + i2);
                i2++;
            }
        }
        if (!z2) {
            i3 = this.bp;
            i4 = ((i3 + i2) - i3) - 1;
            cCharAt2 = cCharAt;
            i5 = i2;
        } else {
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return 0.0d;
            }
            i5 = i2 + 1;
            cCharAt2 = charAt(this.bp + i2);
            int i11 = this.bp;
            i3 = i11 + 1;
            i4 = ((i11 + i5) - i3) - 2;
        }
        if (z4 || i4 >= 17) {
            d2 = Double.parseDouble(subString(i3, i4));
        } else {
            d2 = j2 / j;
            if (z) {
                d2 = -d2;
            }
        }
        if (cCharAt2 == c2) {
            this.bp += i5;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return d2;
        }
        this.matchStat = -1;
        return d2;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public BigDecimal scanDecimal(char c2) {
        int i;
        int i2;
        int i3;
        char cCharAt;
        int i4;
        int i5;
        this.matchStat = 0;
        char cCharAt2 = charAt(this.bp + 0);
        boolean z = cCharAt2 == '\"';
        if (z) {
            cCharAt2 = charAt(this.bp + 1);
            i = 2;
        } else {
            i = 1;
        }
        if (cCharAt2 == '-') {
            cCharAt2 = charAt(this.bp + i);
            i++;
        }
        if (cCharAt2 < '0' || cCharAt2 > '9') {
            if (cCharAt2 == 'n' && charAt(this.bp + i) == 'u' && charAt(this.bp + i + 1) == 'l' && charAt(this.bp + i + 2) == 'l') {
                this.matchStat = 5;
                int i6 = i + 3;
                int i7 = i6 + 1;
                char cCharAt3 = charAt(this.bp + i6);
                if (z && cCharAt3 == '\"') {
                    i2 = i7 + 1;
                    cCharAt3 = charAt(this.bp + i7);
                } else {
                    i2 = i7;
                }
                while (cCharAt3 != ',') {
                    if (cCharAt3 == '}') {
                        this.bp += i2;
                        this.ch = charAt(this.bp);
                        this.matchStat = 5;
                        this.token = 13;
                        return null;
                    }
                    if (isWhitespace(cCharAt3)) {
                        cCharAt3 = charAt(this.bp + i2);
                        i2++;
                    } else {
                        this.matchStat = -1;
                        return null;
                    }
                }
                this.bp += i2;
                this.ch = charAt(this.bp);
                this.matchStat = 5;
                this.token = 16;
                return null;
            }
            this.matchStat = -1;
            return null;
        }
        while (true) {
            i3 = i + 1;
            cCharAt = charAt(this.bp + i);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            i = i3;
        }
        if (cCharAt == '.') {
            int i8 = i3 + 1;
            char cCharAt4 = charAt(this.bp + i3);
            if (cCharAt4 < '0' || cCharAt4 > '9') {
                this.matchStat = -1;
                return null;
            }
            while (true) {
                i3 = i8 + 1;
                cCharAt = charAt(this.bp + i8);
                if (cCharAt < '0' || cCharAt > '9') {
                    break;
                }
                i8 = i3;
            }
        }
        if (cCharAt == 'e' || cCharAt == 'E') {
            int i9 = i3 + 1;
            cCharAt = charAt(this.bp + i3);
            if (cCharAt == '+' || cCharAt == '-') {
                cCharAt = charAt(this.bp + i9);
                i3 = i9 + 1;
            } else {
                i3 = i9;
            }
            while (cCharAt >= '0' && cCharAt <= '9') {
                cCharAt = charAt(this.bp + i3);
                i3++;
            }
        }
        if (!z) {
            i4 = this.bp;
            i5 = ((i4 + i3) - i4) - 1;
        } else {
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return null;
            }
            int i10 = i3 + 1;
            cCharAt = charAt(this.bp + i3);
            int i11 = this.bp;
            i4 = i11 + 1;
            i5 = ((i11 + i10) - i4) - 2;
            i3 = i10;
        }
        if (i5 > 65535) {
            throw new JSONException("decimal overflow");
        }
        char[] cArrSub_chars = sub_chars(i4, i5);
        BigDecimal bigDecimal = new BigDecimal(cArrSub_chars, 0, cArrSub_chars.length, MathContext.UNLIMITED);
        if (cCharAt == ',') {
            this.bp += i3;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return bigDecimal;
        }
        if (cCharAt == ']') {
            int i12 = i3 + 1;
            char cCharAt5 = charAt(this.bp + i3);
            if (cCharAt5 == ',') {
                this.token = 16;
                this.bp += i12;
                this.ch = charAt(this.bp);
            } else if (cCharAt5 == ']') {
                this.token = 15;
                this.bp += i12;
                this.ch = charAt(this.bp);
            } else if (cCharAt5 == '}') {
                this.token = 13;
                this.bp += i12;
                this.ch = charAt(this.bp);
            } else if (cCharAt5 == 26) {
                this.token = 20;
                this.bp += i12 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return bigDecimal;
        }
        this.matchStat = -1;
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x01b6, code lost:
    
        r2 = r4;
        r18.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x01b9, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00a0, code lost:
    
        r18.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00a2, code lost:
    
        return r4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final float[] scanFieldFloatArray(char[] r19) {
        /*
            Method dump skipped, instruction units count: 442
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldFloatArray(char[]):float[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b4, code lost:
    
        r21.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00b8, code lost:
    
        return r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x013c, code lost:
    
        r2 = r18 + 1;
        r1 = charAt(r21.bp + r18);
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0147, code lost:
    
        if (r4 == r3.length) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0149, code lost:
    
        r5 = new float[r4];
        r6 = 0;
        java.lang.System.arraycopy(r3, 0, r5, 0, r4);
        r3 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0151, code lost:
    
        r6 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0153, code lost:
    
        if (r8 < r7.length) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0155, code lost:
    
        r5 = new float[(r7.length * 3) / 2][];
        java.lang.System.arraycopy(r3, r6, r5, r6, r4);
        r7 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0160, code lost:
    
        r4 = r8 + 1;
        r7[r8] = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0166, code lost:
    
        if (r1 != ',') goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0168, code lost:
    
        r3 = r2 + 1;
        r1 = charAt(r21.bp + r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0175, code lost:
    
        if (r1 != ']') goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0177, code lost:
    
        r3 = r2 + 1;
        r2 = charAt(r21.bp + r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0181, code lost:
    
        r3 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0196, code lost:
    
        r21.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x019c, code lost:
    
        return (float[][]) null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final float[][] scanFieldFloatArray2(char[] r22) {
        /*
            Method dump skipped, instruction units count: 549
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldFloatArray2(char[]):float[][]");
    }

    public final double scanFieldDouble(char[] cArr) {
        int i;
        char cCharAt;
        boolean z;
        int i2;
        long j;
        int length;
        int i3;
        char cCharAt2;
        double d2;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0.0d;
        }
        int length2 = cArr.length;
        int i4 = length2 + 1;
        char cCharAt3 = charAt(this.bp + length2);
        boolean z2 = cCharAt3 == '\"';
        if (z2) {
            cCharAt3 = charAt(this.bp + i4);
            i4++;
        }
        boolean z3 = cCharAt3 == '-';
        if (z3) {
            cCharAt3 = charAt(this.bp + i4);
            i4++;
        }
        if (cCharAt3 >= '0') {
            char c2 = '9';
            if (cCharAt3 <= '9') {
                int i5 = i4;
                long j2 = cCharAt3 - '0';
                while (true) {
                    i = i5 + 1;
                    cCharAt = charAt(this.bp + i5);
                    if (cCharAt < '0' || cCharAt > '9') {
                        break;
                    }
                    j2 = (j2 * 10) + ((long) (cCharAt - '0'));
                    i5 = i;
                    z2 = z2;
                }
                boolean z4 = z2;
                if (cCharAt == '.') {
                    int i6 = i + 1;
                    char cCharAt4 = charAt(this.bp + i);
                    if (cCharAt4 < '0' || cCharAt4 > '9') {
                        this.matchStat = -1;
                        return 0.0d;
                    }
                    z = z3;
                    j2 = (j2 * 10) + ((long) (cCharAt4 - '0'));
                    j = 10;
                    while (true) {
                        i2 = i6 + 1;
                        cCharAt = charAt(this.bp + i6);
                        if (cCharAt < '0' || cCharAt > c2) {
                            break;
                        }
                        j2 = (j2 * 10) + ((long) (cCharAt - '0'));
                        j *= 10;
                        i6 = i2;
                        c2 = '9';
                    }
                } else {
                    z = z3;
                    i2 = i;
                    j = 1;
                }
                boolean z5 = cCharAt == 'e' || cCharAt == 'E';
                if (z5) {
                    int i7 = i2 + 1;
                    cCharAt = charAt(this.bp + i2);
                    if (cCharAt == '+' || cCharAt == '-') {
                        cCharAt = charAt(this.bp + i7);
                        i2 = i7 + 1;
                    } else {
                        i2 = i7;
                    }
                    while (cCharAt >= '0' && cCharAt <= '9') {
                        cCharAt = charAt(this.bp + i2);
                        i2++;
                    }
                }
                if (!z4) {
                    int i8 = this.bp;
                    length = cArr.length + i8;
                    i3 = ((i8 + i2) - length) - 1;
                    cCharAt2 = cCharAt;
                } else {
                    if (cCharAt != '\"') {
                        this.matchStat = -1;
                        return 0.0d;
                    }
                    int i9 = i2 + 1;
                    cCharAt2 = charAt(this.bp + i2);
                    int i10 = this.bp;
                    length = cArr.length + i10 + 1;
                    i3 = ((i10 + i9) - length) - 2;
                    i2 = i9;
                }
                if (z5 || i3 >= 17) {
                    d2 = Double.parseDouble(subString(length, i3));
                } else {
                    d2 = j2 / j;
                    if (z) {
                        d2 = -d2;
                    }
                }
                if (cCharAt2 == ',') {
                    this.bp += i2;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    this.token = 16;
                    return d2;
                }
                if (cCharAt2 == '}') {
                    int i11 = i2 + 1;
                    char cCharAt5 = charAt(this.bp + i2);
                    if (cCharAt5 == ',') {
                        this.token = 16;
                        this.bp += i11;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt5 == ']') {
                        this.token = 15;
                        this.bp += i11;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt5 == '}') {
                        this.token = 13;
                        this.bp += i11;
                        this.ch = charAt(this.bp);
                    } else if (cCharAt5 == 26) {
                        this.token = 20;
                        this.bp += i11 - 1;
                        this.ch = JSONLexer.EOI;
                    } else {
                        this.matchStat = -1;
                        return 0.0d;
                    }
                    this.matchStat = 4;
                    return d2;
                }
                this.matchStat = -1;
                return 0.0d;
            }
        }
        int i12 = i4;
        boolean z6 = z2;
        if (cCharAt3 == 'n' && charAt(this.bp + i12) == 'u' && charAt(this.bp + i12 + 1) == 'l' && charAt(this.bp + i12 + 2) == 'l') {
            this.matchStat = 5;
            int i13 = i12 + 3;
            int i14 = i13 + 1;
            char cCharAt6 = charAt(this.bp + i13);
            if (z6 && cCharAt6 == '\"') {
                cCharAt6 = charAt(this.bp + i14);
                i14++;
            }
            while (cCharAt6 != ',') {
                if (cCharAt6 == '}') {
                    this.bp += i14;
                    this.ch = charAt(this.bp);
                    this.matchStat = 5;
                    this.token = 13;
                    return 0.0d;
                }
                if (isWhitespace(cCharAt6)) {
                    cCharAt6 = charAt(this.bp + i14);
                    i14++;
                } else {
                    this.matchStat = -1;
                    return 0.0d;
                }
            }
            this.bp += i14;
            this.ch = charAt(this.bp);
            this.matchStat = 5;
            this.token = 16;
            return 0.0d;
        }
        this.matchStat = -1;
        return 0.0d;
    }

    public BigDecimal scanFieldDecimal(char[] cArr) {
        int i;
        char cCharAt;
        int length;
        int i2;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length2 = cArr.length;
        int i3 = length2 + 1;
        char cCharAt2 = charAt(this.bp + length2);
        boolean z = cCharAt2 == '\"';
        if (z) {
            cCharAt2 = charAt(this.bp + i3);
            i3++;
        }
        if (cCharAt2 == '-') {
            cCharAt2 = charAt(this.bp + i3);
            i3++;
        }
        if (cCharAt2 < '0' || cCharAt2 > '9') {
            if (cCharAt2 == 'n' && charAt(this.bp + i3) == 'u' && charAt(this.bp + i3 + 1) == 'l' && charAt(this.bp + i3 + 2) == 'l') {
                this.matchStat = 5;
                int i4 = i3 + 3;
                int i5 = i4 + 1;
                char cCharAt3 = charAt(this.bp + i4);
                if (z && cCharAt3 == '\"') {
                    cCharAt3 = charAt(this.bp + i5);
                    i5++;
                }
                while (cCharAt3 != ',') {
                    if (cCharAt3 == '}') {
                        this.bp += i5;
                        this.ch = charAt(this.bp);
                        this.matchStat = 5;
                        this.token = 13;
                        return null;
                    }
                    if (isWhitespace(cCharAt3)) {
                        cCharAt3 = charAt(this.bp + i5);
                        i5++;
                    } else {
                        this.matchStat = -1;
                        return null;
                    }
                }
                this.bp += i5;
                this.ch = charAt(this.bp);
                this.matchStat = 5;
                this.token = 16;
                return null;
            }
            this.matchStat = -1;
            return null;
        }
        while (true) {
            i = i3 + 1;
            cCharAt = charAt(this.bp + i3);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            i3 = i;
        }
        if (cCharAt == '.') {
            int i6 = i + 1;
            char cCharAt4 = charAt(this.bp + i);
            if (cCharAt4 < '0' || cCharAt4 > '9') {
                this.matchStat = -1;
                return null;
            }
            while (true) {
                i = i6 + 1;
                cCharAt = charAt(this.bp + i6);
                if (cCharAt < '0' || cCharAt > '9') {
                    break;
                }
                i6 = i;
            }
        }
        if (cCharAt == 'e' || cCharAt == 'E') {
            int i7 = i + 1;
            cCharAt = charAt(this.bp + i);
            if (cCharAt == '+' || cCharAt == '-') {
                cCharAt = charAt(this.bp + i7);
                i = i7 + 1;
            } else {
                i = i7;
            }
            while (cCharAt >= '0' && cCharAt <= '9') {
                cCharAt = charAt(this.bp + i);
                i++;
            }
        }
        if (!z) {
            int i8 = this.bp;
            length = cArr.length + i8;
            i2 = ((i8 + i) - length) - 1;
        } else {
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return null;
            }
            int i9 = i + 1;
            cCharAt = charAt(this.bp + i);
            int i10 = this.bp;
            length = cArr.length + i10 + 1;
            i2 = ((i10 + i9) - length) - 2;
            i = i9;
        }
        if (i2 > 65535) {
            throw new JSONException("scan decimal overflow");
        }
        char[] cArrSub_chars = sub_chars(length, i2);
        BigDecimal bigDecimal = new BigDecimal(cArrSub_chars, 0, cArrSub_chars.length, MathContext.UNLIMITED);
        if (cCharAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return bigDecimal;
        }
        if (cCharAt == '}') {
            int i11 = i + 1;
            char cCharAt5 = charAt(this.bp + i);
            if (cCharAt5 == ',') {
                this.token = 16;
                this.bp += i11;
                this.ch = charAt(this.bp);
            } else if (cCharAt5 == ']') {
                this.token = 15;
                this.bp += i11;
                this.ch = charAt(this.bp);
            } else if (cCharAt5 == '}') {
                this.token = 13;
                this.bp += i11;
                this.ch = charAt(this.bp);
            } else if (cCharAt5 == 26) {
                this.token = 20;
                this.bp += i11 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return bigDecimal;
        }
        this.matchStat = -1;
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0072, code lost:
    
        r16 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.math.BigInteger scanFieldBigInteger(char[] r21) {
        /*
            Method dump skipped, instruction units count: 472
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldBigInteger(char[]):java.math.BigInteger");
    }

    public Date scanFieldDate(char[] cArr) {
        int i;
        long j;
        Date date;
        int i2;
        char cCharAt;
        boolean z = false;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = cArr.length;
        int i3 = length + 1;
        char cCharAt2 = charAt(this.bp + length);
        if (cCharAt2 == '\"') {
            int iIndexOf = indexOf('\"', this.bp + cArr.length + 1);
            if (iIndexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int length2 = this.bp + cArr.length + 1;
            String strSubString = subString(length2, iIndexOf - length2);
            if (strSubString.indexOf(92) != -1) {
                while (true) {
                    int i4 = 0;
                    for (int i5 = iIndexOf - 1; i5 >= 0 && charAt(i5) == '\\'; i5--) {
                        i4++;
                    }
                    if (i4 % 2 == 0) {
                        break;
                    }
                    iIndexOf = indexOf('\"', iIndexOf + 1);
                }
                int i6 = this.bp;
                int length3 = iIndexOf - ((cArr.length + i6) + 1);
                strSubString = readString(sub_chars(i6 + cArr.length + 1, length3), length3);
            }
            int i7 = this.bp;
            int length4 = i3 + (iIndexOf - ((cArr.length + i7) + 1)) + 1;
            i = length4 + 1;
            cCharAt2 = charAt(i7 + length4);
            JSONScanner jSONScanner = new JSONScanner(strSubString);
            try {
                if (jSONScanner.scanISO8601DateIfMatch(false)) {
                    date = jSONScanner.getCalendar().getTime();
                } else {
                    this.matchStat = -1;
                    return null;
                }
            } finally {
                jSONScanner.close();
            }
        } else {
            if (cCharAt2 != '-' && (cCharAt2 < '0' || cCharAt2 > '9')) {
                this.matchStat = -1;
                return null;
            }
            if (cCharAt2 == '-') {
                cCharAt2 = charAt(this.bp + i3);
                i3++;
                z = true;
            }
            if (cCharAt2 < '0' || cCharAt2 > '9') {
                i = i3;
                j = 0;
            } else {
                j = cCharAt2 - '0';
                while (true) {
                    i2 = i3 + 1;
                    cCharAt = charAt(this.bp + i3);
                    if (cCharAt < '0' || cCharAt > '9') {
                        break;
                    }
                    j = (j * 10) + ((long) (cCharAt - '0'));
                    i3 = i2;
                }
                cCharAt2 = cCharAt;
                i = i2;
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
        if (cCharAt2 == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return date;
        }
        if (cCharAt2 == '}') {
            int i8 = i + 1;
            char cCharAt3 = charAt(this.bp + i);
            if (cCharAt3 == ',') {
                this.token = 16;
                this.bp += i8;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == ']') {
                this.token = 15;
                this.bp += i8;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == '}') {
                this.token = 13;
                this.bp += i8;
                this.ch = charAt(this.bp);
            } else if (cCharAt3 == 26) {
                this.token = 20;
                this.bp += i8 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return date;
        }
        this.matchStat = -1;
        return null;
    }

    public Date scanDate(char c2) {
        long j;
        int i;
        Date date;
        boolean z = false;
        this.matchStat = 0;
        char cCharAt = charAt(this.bp + 0);
        if (cCharAt == '\"') {
            int iIndexOf = indexOf('\"', this.bp + 1);
            if (iIndexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int i2 = this.bp + 1;
            String strSubString = subString(i2, iIndexOf - i2);
            if (strSubString.indexOf(92) != -1) {
                while (true) {
                    int i3 = 0;
                    for (int i4 = iIndexOf - 1; i4 >= 0 && charAt(i4) == '\\'; i4--) {
                        i3++;
                    }
                    if (i3 % 2 == 0) {
                        break;
                    }
                    iIndexOf = indexOf('\"', iIndexOf + 1);
                }
                int i5 = this.bp;
                int i6 = iIndexOf - (i5 + 1);
                strSubString = readString(sub_chars(i5 + 1, i6), i6);
            }
            int i7 = this.bp;
            int i8 = (iIndexOf - (i7 + 1)) + 1 + 1;
            int i9 = i8 + 1;
            cCharAt = charAt(i7 + i8);
            JSONScanner jSONScanner = new JSONScanner(strSubString);
            try {
                if (jSONScanner.scanISO8601DateIfMatch(false)) {
                    date = jSONScanner.getCalendar().getTime();
                    jSONScanner.close();
                    i = i9;
                } else {
                    this.matchStat = -1;
                    return null;
                }
            } finally {
                jSONScanner.close();
            }
        } else {
            char c3 = '9';
            int i10 = 2;
            if (cCharAt == '-' || (cCharAt >= '0' && cCharAt <= '9')) {
                if (cCharAt == '-') {
                    cCharAt = charAt(this.bp + 1);
                    z = true;
                } else {
                    i10 = 1;
                }
                if (cCharAt < '0' || cCharAt > '9') {
                    j = 0;
                    i = i10;
                } else {
                    j = cCharAt - '0';
                    while (true) {
                        i = i10 + 1;
                        cCharAt = charAt(this.bp + i10);
                        if (cCharAt < '0' || cCharAt > c3) {
                            break;
                        }
                        j = (j * 10) + ((long) (cCharAt - '0'));
                        i10 = i;
                        c3 = '9';
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
            } else if (cCharAt == 'n' && charAt(this.bp + 1) == 'u' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 'l') {
                this.matchStat = 5;
                cCharAt = charAt(this.bp + 4);
                i = 5;
                date = null;
            } else {
                this.matchStat = -1;
                return null;
            }
        }
        if (cCharAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return date;
        }
        if (cCharAt == ']') {
            int i11 = i + 1;
            char cCharAt2 = charAt(this.bp + i);
            if (cCharAt2 == ',') {
                this.token = 16;
                this.bp += i11;
                this.ch = charAt(this.bp);
            } else if (cCharAt2 == ']') {
                this.token = 15;
                this.bp += i11;
                this.ch = charAt(this.bp);
            } else if (cCharAt2 == '}') {
                this.token = 13;
                this.bp += i11;
                this.ch = charAt(this.bp);
            } else if (cCharAt2 == 26) {
                this.token = 20;
                this.bp += i11 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return date;
        }
        this.matchStat = -1;
        return null;
    }

    public UUID scanFieldUUID(char[] cArr) {
        int i;
        int i2;
        char cCharAt;
        UUID uuid;
        int i3;
        int i4;
        char cCharAt2;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = cArr.length;
        int i12 = length + 1;
        char cCharAt3 = charAt(this.bp + length);
        char c2 = 4;
        if (cCharAt3 == '\"') {
            int iIndexOf = indexOf('\"', this.bp + cArr.length + 1);
            if (iIndexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int length2 = this.bp + cArr.length + 1;
            int i13 = iIndexOf - length2;
            char c3 = 'F';
            char c4 = 'f';
            char c5 = 'A';
            char c6 = 'a';
            char c7 = '0';
            if (i13 == 36) {
                int i14 = 0;
                long j = 0;
                while (i14 < 8) {
                    char cCharAt4 = charAt(length2 + i14);
                    if (cCharAt4 >= '0' && cCharAt4 <= '9') {
                        i11 = cCharAt4 - '0';
                    } else if (cCharAt4 >= 'a' && cCharAt4 <= 'f') {
                        i11 = (cCharAt4 - 'a') + 10;
                    } else {
                        if (cCharAt4 < c5 || cCharAt4 > c3) {
                            this.matchStat = -2;
                            return null;
                        }
                        i11 = (cCharAt4 - 'A') + 10;
                    }
                    j = (j << 4) | ((long) i11);
                    i14++;
                    c5 = 'A';
                    c3 = 'F';
                }
                int i15 = 9;
                while (i15 < 13) {
                    char cCharAt5 = charAt(length2 + i15);
                    if (cCharAt5 >= '0' && cCharAt5 <= '9') {
                        i10 = cCharAt5 - '0';
                    } else if (cCharAt5 >= 'a' && cCharAt5 <= c4) {
                        i10 = (cCharAt5 - 'a') + 10;
                    } else {
                        if (cCharAt5 < 'A' || cCharAt5 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i10 = (cCharAt5 - 'A') + 10;
                    }
                    j = (j << 4) | ((long) i10);
                    i15++;
                    iIndexOf = iIndexOf;
                    c4 = 'f';
                }
                int i16 = iIndexOf;
                long j2 = j;
                for (int i17 = 14; i17 < 18; i17++) {
                    char cCharAt6 = charAt(length2 + i17);
                    if (cCharAt6 >= '0' && cCharAt6 <= '9') {
                        i9 = cCharAt6 - '0';
                    } else if (cCharAt6 >= 'a' && cCharAt6 <= 'f') {
                        i9 = (cCharAt6 - 'a') + 10;
                    } else {
                        if (cCharAt6 < 'A' || cCharAt6 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i9 = (cCharAt6 - 'A') + 10;
                    }
                    j2 = (j2 << 4) | ((long) i9);
                }
                int i18 = 19;
                long j3 = 0;
                while (i18 < 23) {
                    char cCharAt7 = charAt(length2 + i18);
                    if (cCharAt7 >= '0' && cCharAt7 <= '9') {
                        i8 = cCharAt7 - '0';
                    } else if (cCharAt7 >= 'a' && cCharAt7 <= 'f') {
                        i8 = (cCharAt7 - 'a') + 10;
                    } else {
                        if (cCharAt7 < 'A' || cCharAt7 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i8 = (cCharAt7 - 'A') + 10;
                    }
                    j3 = (j3 << c2) | ((long) i8);
                    i18++;
                    j2 = j2;
                    c2 = 4;
                }
                long j4 = j2;
                long j5 = j3;
                for (int i19 = 24; i19 < 36; i19++) {
                    char cCharAt8 = charAt(length2 + i19);
                    if (cCharAt8 >= '0' && cCharAt8 <= '9') {
                        i7 = cCharAt8 - '0';
                    } else if (cCharAt8 >= 'a' && cCharAt8 <= 'f') {
                        i7 = (cCharAt8 - 'a') + 10;
                    } else {
                        if (cCharAt8 < 'A' || cCharAt8 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i7 = (cCharAt8 - 'A') + 10;
                    }
                    j5 = (j5 << 4) | ((long) i7);
                }
                uuid = new UUID(j4, j5);
                int i20 = this.bp;
                int length3 = i12 + (i16 - ((cArr.length + i20) + 1)) + 1;
                i4 = length3 + 1;
                cCharAt2 = charAt(i20 + length3);
            } else if (i13 == 32) {
                int i21 = 0;
                long j6 = 0;
                for (int i22 = 16; i21 < i22; i22 = 16) {
                    char cCharAt9 = charAt(length2 + i21);
                    if (cCharAt9 >= '0' && cCharAt9 <= '9') {
                        i6 = cCharAt9 - '0';
                    } else if (cCharAt9 >= 'a' && cCharAt9 <= 'f') {
                        i6 = (cCharAt9 - 'a') + 10;
                    } else {
                        if (cCharAt9 < 'A' || cCharAt9 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i6 = (cCharAt9 - 'A') + 10;
                    }
                    j6 = (j6 << 4) | ((long) i6);
                    i21++;
                }
                int i23 = 16;
                long j7 = 0;
                while (i23 < 32) {
                    char cCharAt10 = charAt(length2 + i23);
                    if (cCharAt10 >= c7 && cCharAt10 <= '9') {
                        i5 = cCharAt10 - '0';
                    } else if (cCharAt10 >= c6 && cCharAt10 <= 'f') {
                        i5 = (cCharAt10 - 'a') + 10;
                    } else {
                        if (cCharAt10 < 'A' || cCharAt10 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i5 = (cCharAt10 - 'A') + 10;
                    }
                    j7 = (j7 << 4) | ((long) i5);
                    i23++;
                    c7 = '0';
                    c6 = 'a';
                }
                uuid = new UUID(j6, j7);
                int i24 = this.bp;
                int length4 = i12 + (iIndexOf - ((cArr.length + i24) + 1)) + 1;
                i4 = length4 + 1;
                cCharAt2 = charAt(i24 + length4);
            } else {
                this.matchStat = -1;
                return null;
            }
            char c8 = cCharAt2;
            i2 = i4;
            cCharAt = c8;
        } else {
            if (cCharAt3 == 'n') {
                int i25 = i12 + 1;
                if (charAt(this.bp + i12) == 'u') {
                    int i26 = i25 + 1;
                    if (charAt(this.bp + i25) == 'l') {
                        int i27 = i26 + 1;
                        if (charAt(this.bp + i26) == 'l') {
                            i2 = i27 + 1;
                            cCharAt = charAt(this.bp + i27);
                            uuid = null;
                        } else {
                            i = -1;
                        }
                    } else {
                        i = -1;
                    }
                } else {
                    i = -1;
                }
            } else {
                i = -1;
            }
            this.matchStat = i;
            return null;
        }
        if (cCharAt == ',') {
            this.bp += i2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return uuid;
        }
        if (cCharAt == '}') {
            int i28 = i2 + 1;
            char cCharAt11 = charAt(this.bp + i2);
            if (cCharAt11 == ',') {
                this.token = 16;
                this.bp += i28;
                this.ch = charAt(this.bp);
                i3 = 4;
            } else if (cCharAt11 == ']') {
                this.token = 15;
                this.bp += i28;
                this.ch = charAt(this.bp);
                i3 = 4;
            } else if (cCharAt11 == '}') {
                this.token = 13;
                this.bp += i28;
                this.ch = charAt(this.bp);
                i3 = 4;
            } else if (cCharAt11 == 26) {
                this.token = 20;
                this.bp += i28 - 1;
                this.ch = JSONLexer.EOI;
                i3 = 4;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = i3;
            return uuid;
        }
        this.matchStat = -1;
        return null;
    }

    public UUID scanUUID(char c2) {
        int i;
        char cCharAt;
        UUID uuid;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        this.matchStat = 0;
        char cCharAt2 = charAt(this.bp + 0);
        if (cCharAt2 == '\"') {
            int iIndexOf = indexOf('\"', this.bp + 1);
            if (iIndexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int i9 = this.bp + 1;
            int i10 = iIndexOf - i9;
            char c3 = '9';
            char c4 = 'A';
            char c5 = 'a';
            char c6 = '0';
            if (i10 == 36) {
                int i11 = 0;
                long j = 0;
                while (i11 < 8) {
                    char cCharAt3 = charAt(i9 + i11);
                    if (cCharAt3 >= '0' && cCharAt3 <= '9') {
                        i8 = cCharAt3 - '0';
                    } else if (cCharAt3 >= c5 && cCharAt3 <= 'f') {
                        i8 = (cCharAt3 - 'a') + 10;
                    } else {
                        if (cCharAt3 < c4 || cCharAt3 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i8 = (cCharAt3 - 'A') + 10;
                    }
                    j = (j << 4) | ((long) i8);
                    i11++;
                    c4 = 'A';
                    c5 = 'a';
                }
                int i12 = 9;
                while (i12 < 13) {
                    char cCharAt4 = charAt(i9 + i12);
                    if (cCharAt4 >= '0' && cCharAt4 <= c3) {
                        i7 = cCharAt4 - '0';
                    } else if (cCharAt4 >= 'a' && cCharAt4 <= 'f') {
                        i7 = (cCharAt4 - 'a') + 10;
                    } else {
                        if (cCharAt4 < 'A' || cCharAt4 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i7 = (cCharAt4 - 'A') + 10;
                    }
                    j = (j << 4) | ((long) i7);
                    i12++;
                    iIndexOf = iIndexOf;
                    c3 = '9';
                }
                int i13 = iIndexOf;
                long j2 = j;
                for (int i14 = 14; i14 < 18; i14++) {
                    char cCharAt5 = charAt(i9 + i14);
                    if (cCharAt5 >= '0' && cCharAt5 <= '9') {
                        i6 = cCharAt5 - '0';
                    } else if (cCharAt5 >= 'a' && cCharAt5 <= 'f') {
                        i6 = (cCharAt5 - 'a') + 10;
                    } else {
                        if (cCharAt5 < 'A' || cCharAt5 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i6 = (cCharAt5 - 'A') + 10;
                    }
                    j2 = (j2 << 4) | ((long) i6);
                }
                int i15 = 19;
                long j3 = 0;
                while (i15 < 23) {
                    char cCharAt6 = charAt(i9 + i15);
                    if (cCharAt6 >= c6 && cCharAt6 <= '9') {
                        i5 = cCharAt6 - '0';
                    } else if (cCharAt6 >= 'a' && cCharAt6 <= 'f') {
                        i5 = (cCharAt6 - 'a') + 10;
                    } else {
                        if (cCharAt6 < 'A' || cCharAt6 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i5 = (cCharAt6 - 'A') + 10;
                    }
                    j3 = (j3 << 4) | ((long) i5);
                    i15++;
                    c6 = '0';
                }
                long j4 = j3;
                for (int i16 = 24; i16 < 36; i16++) {
                    char cCharAt7 = charAt(i9 + i16);
                    if (cCharAt7 >= '0' && cCharAt7 <= '9') {
                        i4 = cCharAt7 - '0';
                    } else if (cCharAt7 >= 'a' && cCharAt7 <= 'f') {
                        i4 = (cCharAt7 - 'a') + 10;
                    } else {
                        if (cCharAt7 < 'A' || cCharAt7 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i4 = (cCharAt7 - 'A') + 10;
                    }
                    j4 = (j4 << 4) | ((long) i4);
                }
                uuid = new UUID(j2, j4);
                int i17 = this.bp;
                int i18 = (i13 - (i17 + 1)) + 1 + 1;
                i = i18 + 1;
                cCharAt = charAt(i17 + i18);
            } else if (i10 == 32) {
                long j5 = 0;
                for (int i19 = 0; i19 < 16; i19++) {
                    char cCharAt8 = charAt(i9 + i19);
                    if (cCharAt8 >= '0' && cCharAt8 <= '9') {
                        i3 = cCharAt8 - '0';
                    } else if (cCharAt8 >= 'a' && cCharAt8 <= 'f') {
                        i3 = (cCharAt8 - 'a') + 10;
                    } else {
                        if (cCharAt8 < 'A' || cCharAt8 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i3 = (cCharAt8 - 'A') + 10;
                    }
                    j5 = (j5 << 4) | ((long) i3);
                }
                long j6 = 0;
                for (int i20 = 16; i20 < 32; i20++) {
                    char cCharAt9 = charAt(i9 + i20);
                    if (cCharAt9 >= '0' && cCharAt9 <= '9') {
                        i2 = cCharAt9 - '0';
                    } else if (cCharAt9 >= 'a' && cCharAt9 <= 'f') {
                        i2 = (cCharAt9 - 'a') + 10;
                    } else {
                        if (cCharAt9 < 'A' || cCharAt9 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i2 = (cCharAt9 - 'A') + 10;
                    }
                    j6 = (j6 << 4) | ((long) i2);
                }
                uuid = new UUID(j5, j6);
                int i21 = this.bp;
                int i22 = (iIndexOf - (i21 + 1)) + 1 + 1;
                i = i22 + 1;
                cCharAt = charAt(i21 + i22);
            } else {
                this.matchStat = -1;
                return null;
            }
        } else if (cCharAt2 == 'n' && charAt(this.bp + 1) == 'u' && charAt(this.bp + 2) == 'l' && charAt(this.bp + 3) == 'l') {
            i = 5;
            cCharAt = charAt(this.bp + 4);
            uuid = null;
        } else {
            int i23 = -1;
            this.matchStat = i23;
            return null;
        }
        if (cCharAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return uuid;
        }
        if (cCharAt == ']') {
            int i24 = i + 1;
            char cCharAt10 = charAt(this.bp + i);
            if (cCharAt10 == ',') {
                this.token = 16;
                this.bp += i24;
                this.ch = charAt(this.bp);
            } else if (cCharAt10 == ']') {
                this.token = 15;
                this.bp += i24;
                this.ch = charAt(this.bp);
            } else if (cCharAt10 == '}') {
                this.token = 13;
                this.bp += i24;
                this.ch = charAt(this.bp);
            } else if (cCharAt10 == 26) {
                this.token = 20;
                this.bp += i24 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return uuid;
        }
        this.matchStat = -1;
        return null;
    }

    public final void scanTrue() {
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'u') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        char c2 = this.ch;
        if (c2 == ' ' || c2 == ',' || c2 == '}' || c2 == ']' || c2 == '\n' || c2 == '\r' || c2 == '\t' || c2 == 26 || c2 == '\f' || c2 == '\b' || c2 == ':' || c2 == '/') {
            this.token = 6;
            return;
        }
        throw new JSONException("scan true error");
    }

    public final void scanNullOrNew() {
        scanNullOrNew(true);
    }

    public final void scanNullOrNew(boolean z) {
        char c2;
        if (this.ch != 'n') {
            throw new JSONException("error parse null or new");
        }
        next();
        char c3 = this.ch;
        if (c3 != 'u') {
            if (c3 != 'e') {
                throw new JSONException("error parse new");
            }
            next();
            if (this.ch != 'w') {
                throw new JSONException("error parse new");
            }
            next();
            char c4 = this.ch;
            if (c4 == ' ' || c4 == ',' || c4 == '}' || c4 == ']' || c4 == '\n' || c4 == '\r' || c4 == '\t' || c4 == 26 || c4 == '\f' || c4 == '\b') {
                this.token = 9;
                return;
            }
            throw new JSONException("scan new error");
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse null");
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse null");
        }
        next();
        char c5 = this.ch;
        if (c5 == ' ' || c5 == ',' || c5 == '}' || c5 == ']' || c5 == '\n' || c5 == '\r' || c5 == '\t' || c5 == 26 || ((c5 == ':' && z) || (c2 = this.ch) == '\f' || c2 == '\b')) {
            this.token = 8;
            return;
        }
        throw new JSONException("scan null error");
    }

    public final void scanFalse() {
        if (this.ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'a') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 's') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        char c2 = this.ch;
        if (c2 == ' ' || c2 == ',' || c2 == '}' || c2 == ']' || c2 == '\n' || c2 == '\r' || c2 == '\t' || c2 == 26 || c2 == '\f' || c2 == '\b' || c2 == ':' || c2 == '/') {
            this.token = 7;
            return;
        }
        throw new JSONException("scan false error");
    }

    public final void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        String strStringVal = stringVal();
        if (TmpConstant.GROUP_ROLE_UNKNOWN.equalsIgnoreCase(strStringVal)) {
            this.token = 8;
            return;
        }
        if ("new".equals(strStringVal)) {
            this.token = 9;
            return;
        }
        if ("true".equals(strStringVal)) {
            this.token = 6;
            return;
        }
        if (RequestConstant.FALSE.equals(strStringVal)) {
            this.token = 7;
            return;
        }
        if ("undefined".equals(strStringVal)) {
            this.token = 23;
            return;
        }
        if ("Set".equals(strStringVal)) {
            this.token = 21;
        } else if ("TreeSet".equals(strStringVal)) {
            this.token = 22;
        } else {
            this.token = 18;
        }
    }

    public static String readString(char[] cArr, int i) {
        char[] cArr2 = new char[i];
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            char c2 = cArr[i2];
            if (c2 != '\\') {
                cArr2[i3] = c2;
                i3++;
            } else {
                i2++;
                char c3 = cArr[i2];
                switch (c3) {
                    case '/':
                        cArr2[i3] = '/';
                        i3++;
                        break;
                    case '0':
                        cArr2[i3] = 0;
                        i3++;
                        break;
                    case '1':
                        cArr2[i3] = 1;
                        i3++;
                        break;
                    case '2':
                        cArr2[i3] = 2;
                        i3++;
                        break;
                    case '3':
                        cArr2[i3] = 3;
                        i3++;
                        break;
                    case '4':
                        cArr2[i3] = 4;
                        i3++;
                        break;
                    case '5':
                        cArr2[i3] = 5;
                        i3++;
                        break;
                    case '6':
                        cArr2[i3] = 6;
                        i3++;
                        break;
                    case '7':
                        cArr2[i3] = 7;
                        i3++;
                        break;
                    default:
                        switch (c3) {
                            case 't':
                                cArr2[i3] = '\t';
                                i3++;
                                break;
                            case 'u':
                                int i4 = i2 + 1;
                                int i5 = i4 + 1;
                                int i6 = i5 + 1;
                                i2 = i6 + 1;
                                cArr2[i3] = (char) Integer.parseInt(new String(new char[]{cArr[i4], cArr[i5], cArr[i6], cArr[i2]}), 16);
                                i3++;
                                break;
                            case 'v':
                                cArr2[i3] = 11;
                                i3++;
                                break;
                            default:
                                switch (c3) {
                                    case '\"':
                                        cArr2[i3] = '\"';
                                        i3++;
                                        break;
                                    case '\'':
                                        cArr2[i3] = '\'';
                                        i3++;
                                        break;
                                    case 'F':
                                    case 'f':
                                        cArr2[i3] = '\f';
                                        i3++;
                                        break;
                                    case '\\':
                                        cArr2[i3] = '\\';
                                        i3++;
                                        break;
                                    case 'b':
                                        cArr2[i3] = '\b';
                                        i3++;
                                        break;
                                    case 'n':
                                        cArr2[i3] = '\n';
                                        i3++;
                                        break;
                                    case 'r':
                                        cArr2[i3] = StringUtil.CARRIAGE_RETURN;
                                        i3++;
                                        break;
                                    case 'x':
                                        int[] iArr = digits;
                                        int i7 = i2 + 1;
                                        int i8 = iArr[cArr[i7]] * 16;
                                        i2 = i7 + 1;
                                        cArr2[i3] = (char) (i8 + iArr[cArr[i2]]);
                                        i3++;
                                        break;
                                    default:
                                        throw new JSONException("unclosed.str.lit");
                                }
                                break;
                        }
                        break;
                }
            }
            i2++;
        }
        return new String(cArr2, 0, i3);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public boolean isBlankInput() {
        int i = 0;
        while (true) {
            char cCharAt = charAt(i);
            if (cCharAt == 26) {
                this.token = 20;
                return true;
            }
            if (!isWhitespace(cCharAt)) {
                return false;
            }
            i++;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void skipWhitespace() {
        while (true) {
            char c2 = this.ch;
            if (c2 > '/') {
                return;
            }
            if (c2 == ' ' || c2 == '\r' || c2 == '\n' || c2 == '\t' || c2 == '\f' || c2 == '\b') {
                next();
            } else if (c2 != '/') {
                return;
            } else {
                skipComment();
            }
        }
    }

    private void scanStringSingleQuote() {
        char next;
        char next2;
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char next3 = next();
            if (next3 == '\'') {
                this.token = 4;
                next();
                return;
            }
            if (next3 != 26) {
                boolean z = true;
                if (next3 == '\\') {
                    if (!this.hasSpecial) {
                        this.hasSpecial = true;
                        int i = this.sp;
                        char[] cArr = this.sbuf;
                        if (i > cArr.length) {
                            char[] cArr2 = new char[i * 2];
                            System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
                            this.sbuf = cArr2;
                        }
                        copyTo(this.np + 1, this.sp, this.sbuf);
                    }
                    char next4 = next();
                    switch (next4) {
                        case '/':
                            putChar('/');
                            break;
                        case '0':
                            putChar((char) 0);
                            break;
                        case '1':
                            putChar((char) 1);
                            break;
                        case '2':
                            putChar((char) 2);
                            break;
                        case '3':
                            putChar((char) 3);
                            break;
                        case '4':
                            putChar((char) 4);
                            break;
                        case '5':
                            putChar((char) 5);
                            break;
                        case '6':
                            putChar((char) 6);
                            break;
                        case '7':
                            putChar((char) 7);
                            break;
                        default:
                            switch (next4) {
                                case 't':
                                    putChar('\t');
                                    break;
                                case 'u':
                                    putChar((char) Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16));
                                    break;
                                case 'v':
                                    putChar((char) 11);
                                    break;
                                default:
                                    switch (next4) {
                                        case '\"':
                                            putChar('\"');
                                            break;
                                        case '\'':
                                            putChar('\'');
                                            break;
                                        case 'F':
                                        case 'f':
                                            putChar('\f');
                                            break;
                                        case '\\':
                                            putChar('\\');
                                            break;
                                        case 'b':
                                            putChar('\b');
                                            break;
                                        case 'n':
                                            putChar('\n');
                                            break;
                                        case 'r':
                                            putChar(StringUtil.CARRIAGE_RETURN);
                                            break;
                                        case 'x':
                                            next = next();
                                            next2 = next();
                                            boolean z2 = (next >= '0' && next <= '9') || (next >= 'a' && next <= 'f') || (next >= 'A' && next <= 'F');
                                            if ((next2 < '0' || next2 > '9') && ((next2 < 'a' || next2 > 'f') && (next2 < 'A' || next2 > 'F'))) {
                                                z = false;
                                            }
                                            if (z2 && z) {
                                                int[] iArr = digits;
                                                putChar((char) ((iArr[next] * 16) + iArr[next2]));
                                            }
                                            break;
                                        default:
                                            this.ch = next4;
                                            throw new JSONException("unclosed single-quote string");
                                    }
                                    break;
                            }
                            break;
                    }
                } else if (this.hasSpecial) {
                    int i2 = this.sp;
                    char[] cArr3 = this.sbuf;
                    if (i2 == cArr3.length) {
                        putChar(next3);
                    } else {
                        this.sp = i2 + 1;
                        cArr3[i2] = next3;
                    }
                } else {
                    this.sp++;
                }
            } else if (!isEOF()) {
                putChar(JSONLexer.EOI);
            } else {
                throw new JSONException("unclosed single-quote string");
            }
        }
        throw new JSONException("invalid escape character \\x" + next + next2);
    }

    protected final void putChar(char c2) {
        int i = this.sp;
        char[] cArr = this.sbuf;
        if (i >= cArr.length) {
            int length = cArr.length * 2;
            if (length < i) {
                length = i + 1;
            }
            char[] cArr2 = new char[length];
            char[] cArr3 = this.sbuf;
            System.arraycopy(cArr3, 0, cArr2, 0, cArr3.length);
            this.sbuf = cArr2;
        }
        char[] cArr4 = this.sbuf;
        int i2 = this.sp;
        this.sp = i2 + 1;
        cArr4[i2] = c2;
    }

    public final void scanHex() {
        char next;
        if (this.ch != 'x') {
            throw new JSONException("illegal state. " + this.ch);
        }
        next();
        if (this.ch != '\'') {
            throw new JSONException("illegal state. " + this.ch);
        }
        this.np = this.bp;
        next();
        if (this.ch == '\'') {
            next();
            this.token = 26;
            return;
        }
        while (true) {
            next = next();
            if ((next < '0' || next > '9') && (next < 'A' || next > 'F')) {
                break;
            } else {
                this.sp++;
            }
        }
        if (next == '\'') {
            this.sp++;
            next();
            this.token = 26;
        } else {
            throw new JSONException("illegal state. " + next);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00cb  */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void scanNumber() {
        /*
            Method dump skipped, instruction units count: 215
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanNumber():void");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final long longValue() throws NumberFormatException {
        long j;
        long j2;
        boolean z = false;
        if (this.np == -1) {
            this.np = 0;
        }
        int i = this.np;
        int i2 = this.sp + i;
        if (charAt(i) == '-') {
            j = Long.MIN_VALUE;
            i++;
            z = true;
        } else {
            j = C.TIME_UNSET;
        }
        if (i < i2) {
            j2 = -(charAt(i) - '0');
            i++;
        } else {
            j2 = 0;
        }
        while (i < i2) {
            int i3 = i + 1;
            char cCharAt = charAt(i);
            if (cCharAt == 'L' || cCharAt == 'S' || cCharAt == 'B') {
                i = i3;
                break;
            }
            int i4 = cCharAt - '0';
            if (j2 < MULTMIN_RADIX_TEN) {
                throw new NumberFormatException(numberString());
            }
            long j3 = j2 * 10;
            long j4 = i4;
            if (j3 < j + j4) {
                throw new NumberFormatException(numberString());
            }
            j2 = j3 - j4;
            i = i3;
        }
        if (!z) {
            return -j2;
        }
        if (i > this.np + 1) {
            return j2;
        }
        throw new NumberFormatException(numberString());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final Number decimalValue(boolean z) {
        char cCharAt = charAt((this.np + this.sp) - 1);
        try {
            if (cCharAt == 'F') {
                return Float.valueOf(Float.parseFloat(numberString()));
            }
            if (cCharAt == 'D') {
                return Double.valueOf(Double.parseDouble(numberString()));
            }
            if (z) {
                return decimalValue();
            }
            return Double.valueOf(doubleValue());
        } catch (NumberFormatException e) {
            throw new JSONException(e.getMessage() + ", " + info());
        }
    }

    public String[] scanFieldStringArray(char[] cArr, int i, SymbolTable symbolTable) {
        throw new UnsupportedOperationException();
    }

    public boolean matchField2(char[] cArr) {
        throw new UnsupportedOperationException();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public int getFeatures() {
        return this.features;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void setFeatures(int i) {
        this.features = i;
    }
}
