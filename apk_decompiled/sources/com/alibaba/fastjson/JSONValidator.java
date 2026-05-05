package com.alibaba.fastjson;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/* JADX INFO: loaded from: classes.dex */
public abstract class JSONValidator implements Cloneable, Closeable {
    protected char ch;
    protected boolean eof;
    protected Type type;
    private Boolean validateResult;
    protected int pos = -1;
    protected int count = 0;
    protected boolean supportMultiValue = false;

    public enum Type {
        Object,
        Array,
        Value
    }

    static final boolean isWhiteSpace(char c2) {
        return c2 == ' ' || c2 == '\t' || c2 == '\r' || c2 == '\n' || c2 == '\f' || c2 == '\b';
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    abstract void next();

    public static JSONValidator fromUtf8(byte[] bArr) {
        return new UTF8Validator(bArr);
    }

    public static JSONValidator fromUtf8(InputStream inputStream) {
        return new UTF8InputStreamValidator(inputStream);
    }

    public static JSONValidator from(String str) {
        return new UTF16Validator(str);
    }

    public static JSONValidator from(Reader reader) {
        return new ReaderValidator(reader);
    }

    public boolean isSupportMultiValue() {
        return this.supportMultiValue;
    }

    public JSONValidator setSupportMultiValue(boolean z) {
        this.supportMultiValue = z;
        return this;
    }

    public Type getType() {
        if (this.type == null) {
            validate();
        }
        return this.type;
    }

    public boolean validate() {
        Boolean bool = this.validateResult;
        if (bool != null) {
            return bool.booleanValue();
        }
        while (any()) {
            skipWhiteSpace();
            this.count++;
            if (this.eof) {
                this.validateResult = true;
                return true;
            }
            if (this.supportMultiValue) {
                skipWhiteSpace();
                if (this.eof) {
                    this.validateResult = true;
                    return true;
                }
            } else {
                this.validateResult = false;
                return false;
            }
        }
        this.validateResult = false;
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:132:0x0189  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x01a6  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x01af  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean any() {
        /*
            Method dump skipped, instruction units count: 540
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONValidator.any():boolean");
    }

    protected void fieldName() {
        next();
        while (true) {
            char c2 = this.ch;
            if (c2 == '\\') {
                next();
                if (this.ch == 'u') {
                    next();
                    next();
                    next();
                    next();
                    next();
                } else {
                    next();
                }
            } else {
                if (c2 == '\"') {
                    next();
                    return;
                }
                next();
            }
        }
    }

    protected boolean string() {
        next();
        while (!this.eof) {
            char c2 = this.ch;
            if (c2 == '\\') {
                next();
                if (this.ch == 'u') {
                    next();
                    next();
                    next();
                    next();
                    next();
                } else {
                    next();
                }
            } else {
                if (c2 == '\"') {
                    next();
                    return true;
                }
                next();
            }
        }
        return false;
    }

    void skipWhiteSpace() {
        while (isWhiteSpace(this.ch)) {
            next();
        }
    }

    static class UTF8Validator extends JSONValidator {
        private final byte[] bytes;

        public UTF8Validator(byte[] bArr) {
            this.bytes = bArr;
            next();
            skipWhiteSpace();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void next() {
            this.pos++;
            int i = this.pos;
            byte[] bArr = this.bytes;
            if (i >= bArr.length) {
                this.ch = (char) 0;
                this.eof = true;
            } else {
                this.ch = (char) bArr[this.pos];
            }
        }
    }

    static class UTF8InputStreamValidator extends JSONValidator {
        private static final ThreadLocal<byte[]> bufLocal = new ThreadLocal<>();
        private byte[] buf;
        private final InputStream is;
        private int end = -1;
        private int readCount = 0;

        public UTF8InputStreamValidator(InputStream inputStream) {
            this.is = inputStream;
            this.buf = bufLocal.get();
            if (this.buf != null) {
                bufLocal.set(null);
            } else {
                this.buf = new byte[8192];
            }
            next();
            skipWhiteSpace();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void next() {
            if (this.pos < this.end) {
                byte[] bArr = this.buf;
                int i = this.pos + 1;
                this.pos = i;
                this.ch = (char) bArr[i];
                return;
            }
            if (this.eof) {
                return;
            }
            try {
                int i2 = this.is.read(this.buf, 0, this.buf.length);
                this.readCount++;
                if (i2 > 0) {
                    this.ch = (char) this.buf[0];
                    this.pos = 0;
                    this.end = i2 - 1;
                } else {
                    if (i2 == -1) {
                        this.pos = 0;
                        this.end = 0;
                        this.buf = null;
                        this.ch = (char) 0;
                        this.eof = true;
                        return;
                    }
                    this.pos = 0;
                    this.end = 0;
                    this.buf = null;
                    this.ch = (char) 0;
                    this.eof = true;
                    throw new JSONException("read error");
                }
            } catch (IOException unused) {
                throw new JSONException("read error");
            }
        }

        @Override // com.alibaba.fastjson.JSONValidator, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            bufLocal.set(this.buf);
            this.is.close();
        }
    }

    static class UTF16Validator extends JSONValidator {
        private final String str;

        public UTF16Validator(String str) {
            this.str = str;
            next();
            skipWhiteSpace();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void next() {
            this.pos++;
            if (this.pos >= this.str.length()) {
                this.ch = (char) 0;
                this.eof = true;
            } else {
                this.ch = this.str.charAt(this.pos);
            }
        }

        @Override // com.alibaba.fastjson.JSONValidator
        protected final void fieldName() {
            char cCharAt;
            int i = this.pos;
            do {
                i++;
                if (i >= this.str.length() || (cCharAt = this.str.charAt(i)) == '\\') {
                    next();
                    while (true) {
                        if (this.ch == '\\') {
                            next();
                            if (this.ch == 'u') {
                                next();
                                next();
                                next();
                                next();
                                next();
                            } else {
                                next();
                            }
                        } else if (this.ch == '\"') {
                            next();
                            return;
                        } else if (this.eof) {
                            return;
                        } else {
                            next();
                        }
                    }
                }
            } while (cCharAt != '\"');
            int i2 = i + 1;
            this.ch = this.str.charAt(i2);
            this.pos = i2;
        }
    }

    static class ReaderValidator extends JSONValidator {
        private static final ThreadLocal<char[]> bufLocal = new ThreadLocal<>();
        private char[] buf;
        final Reader r;
        private int end = -1;
        private int readCount = 0;

        ReaderValidator(Reader reader) {
            this.r = reader;
            this.buf = bufLocal.get();
            if (this.buf != null) {
                bufLocal.set(null);
            } else {
                this.buf = new char[8192];
            }
            next();
            skipWhiteSpace();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void next() {
            if (this.pos < this.end) {
                char[] cArr = this.buf;
                int i = this.pos + 1;
                this.pos = i;
                this.ch = cArr[i];
                return;
            }
            if (this.eof) {
                return;
            }
            try {
                int i2 = this.r.read(this.buf, 0, this.buf.length);
                this.readCount++;
                if (i2 > 0) {
                    this.ch = this.buf[0];
                    this.pos = 0;
                    this.end = i2 - 1;
                } else {
                    if (i2 == -1) {
                        this.pos = 0;
                        this.end = 0;
                        this.buf = null;
                        this.ch = (char) 0;
                        this.eof = true;
                        return;
                    }
                    this.pos = 0;
                    this.end = 0;
                    this.buf = null;
                    this.ch = (char) 0;
                    this.eof = true;
                    throw new JSONException("read error");
                }
            } catch (IOException unused) {
                throw new JSONException("read error");
            }
        }

        @Override // com.alibaba.fastjson.JSONValidator, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            bufLocal.set(this.buf);
            this.r.close();
        }
    }
}
