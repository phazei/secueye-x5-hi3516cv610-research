package com.alibaba.sdk.android.openaccount.hook;

import com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;

/* JADX INFO: loaded from: classes.dex */
public interface OAApiHook {
    RpcResponse onInterceptRequest(RpcRequest rpcRequest);

    boolean onInterceptResponse(RpcRequest rpcRequest, RpcResponse rpcResponse);
}
