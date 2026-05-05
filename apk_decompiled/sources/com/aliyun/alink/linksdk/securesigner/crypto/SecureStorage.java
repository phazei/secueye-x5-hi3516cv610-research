package com.aliyun.alink.linksdk.securesigner.crypto;

/* JADX INFO: loaded from: classes2.dex */
public interface SecureStorage {
    String get(String str) throws SecureStorageException;

    void put(String str, String str2) throws SecureStorageException;

    void remove(String str) throws SecureStorageException;
}
