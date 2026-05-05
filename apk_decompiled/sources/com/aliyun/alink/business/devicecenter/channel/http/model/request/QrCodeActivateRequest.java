package com.aliyun.alink.business.devicecenter.channel.http.model.request;

/* JADX INFO: loaded from: classes.dex */
public class QrCodeActivateRequest extends BaseRequest {

    public static class QrCodeActivateRequestBuilder {
        public QrCodeActivateRequest build() {
            return new QrCodeActivateRequest();
        }

        public String toString() {
            return "QrCodeActivateRequest.QrCodeActivateRequestBuilder()";
        }
    }

    public static QrCodeActivateRequestBuilder builder() {
        return new QrCodeActivateRequestBuilder();
    }
}
