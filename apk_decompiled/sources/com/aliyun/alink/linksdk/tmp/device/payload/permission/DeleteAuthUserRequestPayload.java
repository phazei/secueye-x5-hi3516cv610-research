package com.aliyun.alink.linksdk.tmp.device.payload.permission;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DeleteAuthUserRequestPayload extends CommonRequestPayload<DeleteAuthUserParam> {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [T, com.aliyun.alink.linksdk.tmp.device.payload.permission.DeleteAuthUserRequestPayload$DeleteAuthUserParam] */
    public DeleteAuthUserRequestPayload(String str, String str2) {
        super(str, str2);
        this.params = new DeleteAuthUserParam();
        ((DeleteAuthUserParam) this.params).setProdKey(str);
        ((DeleteAuthUserParam) this.params).setDeviceName(str2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setIds(List<String> list) {
        ((DeleteAuthUserParam) this.params).setIds(list);
    }

    public static class DeleteAuthUserParam {
        protected String deviceName;
        protected String prodKey;
        protected List<String> uids;

        public List<String> getIds() {
            return this.uids;
        }

        public void setIds(List<String> list) {
            this.uids = list;
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
    }
}
