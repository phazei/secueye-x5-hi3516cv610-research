package com.aliyun.alink.business.devicecenter.channel.http.model.response;

import com.aliyun.alink.business.devicecenter.channel.http.model.DataObject;

/* JADX INFO: loaded from: classes.dex */
public class QrCodeActivateResponse extends DataObject {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f3507a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f3508b;

    public static class QrCodeActivateResponseBuilder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public String f3509a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public String f3510b;

        public QrCodeActivateResponse build() {
            return new QrCodeActivateResponse(this.f3509a, this.f3510b);
        }

        public String toString() {
            return "QrCodeActivateResponse.QrCodeActivateResponseBuilder(uuid=" + this.f3509a + ", userId=" + this.f3510b + ")";
        }

        public QrCodeActivateResponseBuilder userId(String str) {
            this.f3510b = str;
            return this;
        }

        public QrCodeActivateResponseBuilder uuid(String str) {
            this.f3509a = str;
            return this;
        }
    }

    public QrCodeActivateResponse(String str, String str2) {
        this.f3507a = str;
        this.f3508b = str2;
    }

    public static QrCodeActivateResponseBuilder builder() {
        return new QrCodeActivateResponseBuilder();
    }

    public String getUserId() {
        return this.f3508b;
    }

    public String getUuid() {
        return this.f3507a;
    }
}
