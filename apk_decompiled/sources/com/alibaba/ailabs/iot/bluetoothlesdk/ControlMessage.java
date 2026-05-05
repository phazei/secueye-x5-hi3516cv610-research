package com.alibaba.ailabs.iot.bluetoothlesdk;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.fastjson.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ControlMessage {
    boolean enqueued;
    boolean finished;
    IActionListener mCallback;
    private JSONObject mJsonParameters;
    private byte[] mParameters;
    private byte request;
    final Type type;

    public enum Type {
        BIND,
        UNBIND,
        CONTROL
    }

    public ControlMessage(Type type) {
        this.request = (byte) 2;
        this.type = type;
    }

    public ControlMessage(Type type, byte b2, byte[] bArr) {
        this.request = (byte) 2;
        this.request = b2;
        this.type = type;
        this.mParameters = bArr;
    }

    public ControlMessage(Type type, JSONObject jSONObject) {
        this.request = (byte) 2;
        this.type = type;
        this.mJsonParameters = jSONObject;
    }

    public byte[] getParameters() {
        return this.mParameters;
    }

    public JSONObject getJsonParameters() {
        return this.mJsonParameters;
    }

    public ControlMessage callback(IActionListener iActionListener) {
        this.mCallback = iActionListener;
        return this;
    }

    public byte getRequest() {
        return this.request;
    }
}
