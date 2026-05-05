package com.alibaba.sdk.android.error;

/* JADX INFO: loaded from: classes.dex */
public class IntCodeGenerator extends CodeGenerator {
    @Override // com.alibaba.sdk.android.error.CodeGenerator
    public Integer generateCodeInt(String str, String str2, String str3) {
        return Integer.valueOf(Integer.parseInt(str3));
    }

    @Override // com.alibaba.sdk.android.error.CodeGenerator
    public String generateCodeStr(String str, String str2, String str3) {
        return null;
    }
}
