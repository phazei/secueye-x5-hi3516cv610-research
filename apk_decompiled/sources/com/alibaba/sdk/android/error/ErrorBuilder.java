package com.alibaba.sdk.android.error;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class ErrorBuilder {
    private Code code;
    private String msg = null;
    private String detail = null;
    private ArrayList<String> solutions = new ArrayList<>();

    private ErrorBuilder(Code code) {
        this.code = null;
        this.code = code;
    }

    public static ErrorBuilder builder(Code code) {
        return new ErrorBuilder(code);
    }

    public ErrorCode build() {
        ErrorCode errorCode = new ErrorCode(this.code);
        String str = this.msg;
        if (str != null) {
            errorCode.setMsg(str);
        }
        String str2 = this.detail;
        if (str2 != null) {
            errorCode.setDetail(str2);
        }
        if (this.solutions.size() > 0) {
            errorCode.setSolutions((String[]) this.solutions.toArray(new String[0]));
        }
        return errorCode;
    }

    public ErrorBuilder detail(String str) {
        this.detail = str;
        return this;
    }

    public ErrorBuilder msg(String str) {
        this.msg = str;
        return this;
    }

    public ErrorBuilder solution(String str) {
        this.solutions.add(str);
        return this;
    }

    public ErrorBuilder solutions(String[] strArr) {
        this.solutions.clear();
        for (String str : strArr) {
            this.solutions.add(str);
        }
        return this;
    }
}
