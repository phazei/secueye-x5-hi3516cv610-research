package com.alibaba.sdk.android.openaccount.rpc.model;

import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class RpcResponse {
    public JSONArray arrayData;
    public int code;
    public JSONObject data;
    public String message;
    public int subCode;
    public String traceId;
    public String type;

    public String toString() {
        return "RpcResponse{code=" + this.code + ", subCode=" + this.subCode + ", traceId='" + this.traceId + "', message='" + this.message + "', data=" + this.data + ", arrayData=" + this.arrayData + '}';
    }
}
