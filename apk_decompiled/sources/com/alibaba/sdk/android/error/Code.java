package com.alibaba.sdk.android.error;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class Code implements Serializable {
    private final Integer codeInt;
    private final String codeStr;

    public Code(String str, Integer num) {
        this.codeStr = str;
        this.codeInt = num;
        if (num == null && str == null) {
            throw new IllegalStateException("错误码定义错误");
        }
    }

    public int getCodeInt() {
        Integer num = this.codeInt;
        if (num != null) {
            return num.intValue();
        }
        throw new IllegalStateException("不支持整数类型的错误码");
    }

    public String getCodeStr() {
        String str = this.codeStr;
        if (str != null) {
            return str;
        }
        throw new IllegalStateException("不支持字符串类型的错误码");
    }

    public String toString() {
        String str = this.codeStr;
        if (str != null) {
            return str;
        }
        Integer num = this.codeInt;
        if (num != null) {
            return String.valueOf(num);
        }
        return "invalid code " + hashCode();
    }
}
