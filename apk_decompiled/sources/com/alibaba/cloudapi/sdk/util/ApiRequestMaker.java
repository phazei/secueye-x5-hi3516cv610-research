package com.alibaba.cloudapi.sdk.util;

import android.text.TextUtils;
import android.util.Base64;
import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class ApiRequestMaker {
    public static void make(ApiRequest apiRequest, String str, String str2) {
        apiRequest.setPath(combinePathParam(apiRequest.getPath(), apiRequest.getPathParams()));
        StringBuilder sb = new StringBuilder();
        sb.append(apiRequest.getScheme().getValue());
        sb.append(apiRequest.getHost());
        sb.append(apiRequest.getPath());
        if (apiRequest.getQuerys() != null && apiRequest.getQuerys().size() > 0) {
            sb.append("?");
            sb.append(HttpCommonUtil.buildParamString(apiRequest.getQuerys()));
        }
        apiRequest.setUrl(sb.toString());
        Date date = apiRequest.getCurrentDate() == null ? new Date() : apiRequest.getCurrentDate();
        apiRequest.addHeader("date", getHttpDateHeaderValue(date));
        apiRequest.addHeader(SdkConstant.CLOUDAPI_X_CA_TIMESTAMP, String.valueOf(date.getTime()));
        apiRequest.addHeader(SdkConstant.CLOUDAPI_X_CA_NONCE, UUID.randomUUID().toString());
        apiRequest.addHeader(HttpConstant.CLOUDAPI_HTTP_HEADER_USER_AGENT, SdkConstant.CLOUDAPI_USER_AGENT);
        apiRequest.addHeader("host", apiRequest.getHost());
        apiRequest.addHeader(SdkConstant.CLOUDAPI_X_CA_KEY, str);
        apiRequest.addHeader(SdkConstant.CLOUDAPI_X_CA_VERSION, "1");
        apiRequest.addHeader(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_TYPE, apiRequest.getMethod().getRequestContentType());
        apiRequest.addHeader(HttpConstant.CLOUDAPI_HTTP_HEADER_ACCEPT, apiRequest.getMethod().getAcceptContentType());
        if (!TextUtils.isEmpty(apiRequest.getSignatureMethod())) {
            apiRequest.addHeader(SdkConstant.CLOUDAPI_X_CA_SIGNATURE_METHOD, apiRequest.getSignatureMethod());
        }
        if (apiRequest.getBody() != null && apiRequest.getBody().length > 0) {
            apiRequest.addHeader(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_MD5, base64AndMD5(apiRequest.getBody()));
        }
        apiRequest.addHeader(SdkConstant.CLOUDAPI_X_CA_SIGNATURE, SignUtil.sign(apiRequest, str2));
        for (String str3 : apiRequest.getHeaders().keySet()) {
            List<String> list = apiRequest.getHeaders().get(str3);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, new String(list.get(i).getBytes(SdkConstant.CLOUDAPI_ENCODING), SdkConstant.CLOUDAPI_HEADER_ENCODING));
                }
            }
            apiRequest.getHeaders().put(str3, list);
        }
    }

    private static String combinePathParam(String str, Map<String, String> map) {
        if (map == null) {
            return str;
        }
        for (String str2 : map.keySet()) {
            str = str.replace("[" + str2 + "]", map.get(str2));
        }
        return str;
    }

    private static String getHttpDateHeaderValue(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(date);
    }

    private static String base64AndMD5(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("bytes can not be null");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(bArr);
            byte[] bArrDigest = messageDigest.digest();
            byte[] bArrEncode = Base64.encode(bArrDigest, 0);
            byte[] bArr2 = new byte[24];
            for (int i = 0; i < 24; i++) {
                bArr2[i] = bArrEncode[i];
            }
            return new String(bArr2, SdkConstant.CLOUDAPI_ENCODING);
        } catch (NoSuchAlgorithmException unused) {
            throw new IllegalArgumentException("unknown algorithm MD5");
        }
    }
}
