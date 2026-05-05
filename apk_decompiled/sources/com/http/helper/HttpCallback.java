package com.http.helper;

/* JADX INFO: loaded from: classes3.dex */
public interface HttpCallback<T, K> {
    void onFail(String str, T t);

    void onSuccess(String str, K k);
}
