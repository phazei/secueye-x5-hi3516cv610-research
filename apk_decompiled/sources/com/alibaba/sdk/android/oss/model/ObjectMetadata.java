package com.alibaba.sdk.android.oss.model;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.OSSHeaders;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ObjectMetadata {
    public static final String AES_256_SERVER_SIDE_ENCRYPTION = "AES256";
    private Map<String, String> userMetadata = new HashMap();
    private Map<String, Object> metadata = new HashMap();

    public Map<String, String> getUserMetadata() {
        return this.userMetadata;
    }

    public void setUserMetadata(Map<String, String> map) {
        this.userMetadata.clear();
        if (map == null || map.isEmpty()) {
            return;
        }
        this.userMetadata.putAll(map);
    }

    public void setHeader(String str, Object obj) {
        this.metadata.put(str, obj);
    }

    public void addUserMetadata(String str, String str2) {
        this.userMetadata.put(str, str2);
    }

    public Date getLastModified() {
        return (Date) this.metadata.get("Last-Modified");
    }

    public void setLastModified(Date date) {
        this.metadata.put("Last-Modified", date);
    }

    public Date getExpirationTime() throws ParseException {
        return DateUtil.parseRfc822Date((String) this.metadata.get("Expires"));
    }

    public String getRawExpiresValue() {
        return (String) this.metadata.get("Expires");
    }

    public void setExpirationTime(Date date) {
        this.metadata.put("Expires", DateUtil.formatRfc822Date(date));
    }

    public long getContentLength() {
        Long l = (Long) this.metadata.get("Content-Length");
        if (l == null) {
            return 0L;
        }
        return l.longValue();
    }

    public void setContentLength(long j) {
        if (j > OSSConstants.DEFAULT_FILE_SIZE_LIMIT) {
            throw new IllegalArgumentException("The content length could not be more than 5GB.");
        }
        this.metadata.put("Content-Length", Long.valueOf(j));
    }

    public String getContentType() {
        return (String) this.metadata.get("Content-Type");
    }

    public void setContentType(String str) {
        this.metadata.put("Content-Type", str);
    }

    public String getContentMD5() {
        return (String) this.metadata.get("Content-MD5");
    }

    public void setContentMD5(String str) {
        this.metadata.put("Content-MD5", str);
    }

    public void setSHA1(String str) {
        this.metadata.put(OSSHeaders.OSS_HASH_SHA1, str);
    }

    public String getSHA1() {
        return (String) this.metadata.get(OSSHeaders.OSS_HASH_SHA1);
    }

    public String getContentEncoding() {
        return (String) this.metadata.get("Content-Encoding");
    }

    public void setContentEncoding(String str) {
        this.metadata.put("Content-Encoding", str);
    }

    public String getCacheControl() {
        return (String) this.metadata.get("Cache-Control");
    }

    public void setCacheControl(String str) {
        this.metadata.put("Cache-Control", str);
    }

    public String getContentDisposition() {
        return (String) this.metadata.get(HttpHeaders.CONTENT_DISPOSITION);
    }

    public void setContentDisposition(String str) {
        this.metadata.put(HttpHeaders.CONTENT_DISPOSITION, str);
    }

    public String getETag() {
        return (String) this.metadata.get("ETag");
    }

    public String getServerSideEncryption() {
        return (String) this.metadata.get(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION);
    }

    public void setServerSideEncryption(String str) {
        this.metadata.put(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION, str);
    }

    public String getObjectType() {
        return (String) this.metadata.get(OSSHeaders.OSS_OBJECT_TYPE);
    }

    public Map<String, Object> getRawMetadata() {
        return Collections.unmodifiableMap(this.metadata);
    }

    public String toString() {
        String string = "";
        try {
            string = getExpirationTime().toString();
        } catch (Exception unused) {
        }
        return "Last-Modified:" + getLastModified() + SdkConstant.CLOUDAPI_LF + "Expires:" + string + SdkConstant.CLOUDAPI_LF + "rawExpires:" + getRawExpiresValue() + SdkConstant.CLOUDAPI_LF + "Content-MD5:" + getContentMD5() + SdkConstant.CLOUDAPI_LF + OSSHeaders.OSS_OBJECT_TYPE + ":" + getObjectType() + SdkConstant.CLOUDAPI_LF + OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION + ":" + getServerSideEncryption() + SdkConstant.CLOUDAPI_LF + HttpHeaders.CONTENT_DISPOSITION + ":" + getContentDisposition() + SdkConstant.CLOUDAPI_LF + "Content-Encoding:" + getContentEncoding() + SdkConstant.CLOUDAPI_LF + "Cache-Control:" + getCacheControl() + SdkConstant.CLOUDAPI_LF + "ETag:" + getETag() + SdkConstant.CLOUDAPI_LF;
    }
}
