package com.alibaba.sdk.android.openaccount.initialization;

/* JADX INFO: loaded from: classes.dex */
public interface InitializationServiceClient {
    <T> T request(InitializationHandler<T> initializationHandler, Class<T> cls);

    <T> T request(InitializationHandler<T> initializationHandler, Class<T> cls, InitializationHandler... initializationHandlerArr);

    void request();

    void request(InitializationHandler... initializationHandlerArr);
}
