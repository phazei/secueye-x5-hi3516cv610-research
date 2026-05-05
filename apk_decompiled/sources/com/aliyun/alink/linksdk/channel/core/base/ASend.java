package com.aliyun.alink.linksdk.channel.core.base;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ASend {
    public final IOnCallListener listener;
    public final ARequest request;
    public AResponse response;
    public ISendStatus status;

    public ASend(ARequest aRequest, IOnCallListener iOnCallListener) {
        this.response = null;
        this.status = null;
        this.request = aRequest;
        this.listener = iOnCallListener;
        this.status = ASendStatus.waitingToSend;
        this.response = new AResponse();
    }

    public IOnCallListener getListener() {
        return this.listener;
    }

    public ARequest getRequest() {
        return this.request;
    }

    public AResponse getResponse() {
        return this.response;
    }

    public ISendStatus getStatus() {
        return this.status;
    }
}
