package com.aliyun.alink.linksdk.alcs.coap;

import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsCoAPResponse extends AlcsCoAPMessage {
    private AlcsCoAPConstant.ResponseCode code;
    private boolean last = true;
    private long rtt;

    public static AlcsCoAPResponse createResponse(AlcsCoAPRequest alcsCoAPRequest, AlcsCoAPConstant.ResponseCode responseCode) {
        AlcsCoAPResponse alcsCoAPResponse = new AlcsCoAPResponse(responseCode);
        alcsCoAPResponse.setDestination(alcsCoAPRequest.getSource());
        alcsCoAPResponse.setDestinationPort(alcsCoAPRequest.getSourcePort());
        alcsCoAPResponse.setToken(alcsCoAPRequest.getToken());
        return alcsCoAPResponse;
    }

    public final AlcsCoAPResponse setObserve() {
        getOptions().setObserve(0);
        return this;
    }

    public AlcsCoAPResponse(AlcsCoAPConstant.ResponseCode responseCode) {
        this.code = responseCode;
    }

    public AlcsCoAPResponse(int i) {
        this.code = AlcsCoAPConstant.ResponseCode.valueOf(i);
    }

    public void setResponseCode(int i) {
        this.code = AlcsCoAPConstant.ResponseCode.valueOf(i);
    }

    public AlcsCoAPConstant.ResponseCode getCode() {
        return this.code;
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPMessage
    public int getRawCode() {
        return this.code.value;
    }

    public boolean isLast() {
        return this.last;
    }

    public void setLast(boolean z) {
        this.last = z;
    }

    public long getRTT() {
        return this.rtt;
    }

    public void setRTT(long j) {
        this.rtt = j;
    }

    public boolean isNotification() {
        return getOptions().hasObserve();
    }

    public boolean hasBlockOption() {
        return getOptions().hasBlock1() || getOptions().hasBlock2();
    }

    public final boolean isError() {
        return isClientError() || isServerError();
    }

    public final boolean isClientError() {
        return AlcsCoAPConstant.ResponseCode.isClientError(this.code);
    }

    public final boolean isServerError() {
        return AlcsCoAPConstant.ResponseCode.isServerError(this.code);
    }
}
