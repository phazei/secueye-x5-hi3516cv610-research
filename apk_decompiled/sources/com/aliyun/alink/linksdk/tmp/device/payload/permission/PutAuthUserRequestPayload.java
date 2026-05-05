package com.aliyun.alink.linksdk.tmp.device.payload.permission;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;

/* JADX INFO: loaded from: classes2.dex */
public class PutAuthUserRequestPayload extends CommonRequestPayload<PutAuthUserRequestParam> {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [T, com.aliyun.alink.linksdk.tmp.device.payload.permission.PutAuthUserRequestPayload$PutAuthUserRequestParam] */
    public PutAuthUserRequestPayload(String str, String str2) {
        super(str, str2);
        this.params = new PutAuthUserRequestParam();
        ((PutAuthUserRequestParam) this.params).setProdKey(str);
        ((PutAuthUserRequestParam) this.params).setDeviceName(str2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload
    public void setSessionKey(String str) {
        super.setSessionKey(str);
        ((PutAuthUserRequestParam) this.params).setSessionKey(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setAuthId(String str) {
        ((PutAuthUserRequestParam) this.params).setUid(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setToken(String str) {
        ((PutAuthUserRequestParam) this.params).setToken(str);
    }

    public static class PutAuthUserRequestParam {
        protected String deviceName;
        protected String prodKey;
        protected String sessionKey;
        protected String token;
        protected String uid;

        public String getToken() {
            return this.token;
        }

        public void setToken(String str) {
            this.token = str;
        }

        public String getProdKey() {
            return this.prodKey;
        }

        public void setProdKey(String str) {
            this.prodKey = str;
        }

        public String getDeviceName() {
            return this.deviceName;
        }

        public void setDeviceName(String str) {
            this.deviceName = str;
        }

        public String getSessionKey() {
            return this.sessionKey;
        }

        public void setSessionKey(String str) {
            this.sessionKey = str;
        }

        public String getUid() {
            return this.uid;
        }

        public void setUid(String str) {
            this.uid = str;
        }
    }
}
