package com.alibaba.sdk.android.openaccount.rpc;

import com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;

/* JADX INFO: loaded from: classes.dex */
public interface RpcService {
    String apiPrefix();

    void degradeDaily();

    String getSource();

    RpcResponse invoke(RpcRequest rpcRequest);

    void registerSessionInfo(String str);
}
