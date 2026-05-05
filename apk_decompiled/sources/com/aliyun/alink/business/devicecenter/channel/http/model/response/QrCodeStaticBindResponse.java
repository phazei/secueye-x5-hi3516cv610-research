package com.aliyun.alink.business.devicecenter.channel.http.model.response;

import com.aliyun.alink.business.devicecenter.channel.http.model.DataObject;

/* JADX INFO: loaded from: classes.dex */
public class QrCodeStaticBindResponse extends DataObject {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f3511a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f3512b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f3513c;

    public static class QrCodeStaticBindResponseBuilder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public String f3514a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public String f3515b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public int f3516c;

        public QrCodeStaticBindResponse build() {
            return new QrCodeStaticBindResponse(this.f3514a, this.f3515b, this.f3516c);
        }

        public QrCodeStaticBindResponseBuilder code(int i) {
            this.f3516c = i;
            return this;
        }

        public QrCodeStaticBindResponseBuilder device_id(String str) {
            this.f3514a = str;
            return this;
        }

        public QrCodeStaticBindResponseBuilder message(String str) {
            this.f3515b = str;
            return this;
        }

        public String toString() {
            return "QrCodeStaticBindResponse.QrCodeStaticBindResponseBuilder(device_id=" + this.f3514a + ", message=" + this.f3515b + ", code=" + this.f3516c + ")";
        }
    }

    public QrCodeStaticBindResponse(String str, String str2, int i) {
        this.f3511a = str;
        this.f3512b = str2;
        this.f3513c = i;
    }

    public static QrCodeStaticBindResponseBuilder builder() {
        return new QrCodeStaticBindResponseBuilder();
    }

    public int getCode() {
        return this.f3513c;
    }

    public String getDevice_id() {
        return this.f3511a;
    }

    public String getMessage() {
        return this.f3512b;
    }

    public void setCode(int i) {
        this.f3513c = i;
    }

    public void setDevice_id(String str) {
        this.f3511a = str;
    }

    public void setMessage(String str) {
        this.f3512b = str;
    }
}
