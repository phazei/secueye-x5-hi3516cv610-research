package com.aliyun.alink.linksdk.securesigner.crypto;

/* JADX INFO: loaded from: classes2.dex */
public class SecureStorageException extends Exception {
    private String detailedInfo;

    public SecureStorageException() {
    }

    public SecureStorageException(String str) {
        super(str);
    }

    public SecureStorageException(String str, Throwable th) {
        super(str, th);
    }

    public SecureStorageException(Throwable th) {
        super(th);
    }

    public String getDetailedInfo() {
        return this.detailedInfo;
    }

    public void setDetailedInfo(String str) {
        this.detailedInfo = str;
    }
}
