package com.alibaba.cloudapi.sdk.util;

import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.signature.ISignerFactory;
import com.alibaba.cloudapi.sdk.signature.ISinger;
import com.alibaba.cloudapi.sdk.signature.SignerFactoryManager;
import com.huawei.hms.framework.common.ContainerUtils;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes.dex */
public class SignUtil {
    public static String sign(ApiRequest apiRequest, String str) {
        try {
            String strBuildStringToSign = buildStringToSign(apiRequest);
            ISignerFactory iSignerFactoryFindSignerFactory = SignerFactoryManager.findSignerFactory(apiRequest.getSignatureMethod());
            if (iSignerFactoryFindSignerFactory == null) {
                throw new SdkException("unsupported signature method:" + apiRequest.getSignatureMethod());
            }
            ISinger signer = iSignerFactoryFindSignerFactory.getSigner();
            if (signer == null) {
                throw new SdkException("Oops!");
            }
            try {
                return signer.sign(strBuildStringToSign, str);
            } catch (Exception e) {
                throw new SdkException(e);
            }
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    private static String buildStringToSign(ApiRequest apiRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(apiRequest.getMethod().getValue());
        sb.append(SdkConstant.CLOUDAPI_LF);
        if (apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_ACCEPT) != null) {
            sb.append(apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_ACCEPT));
        }
        sb.append(SdkConstant.CLOUDAPI_LF);
        if (apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_MD5) != null) {
            sb.append(apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_MD5));
        }
        sb.append(SdkConstant.CLOUDAPI_LF);
        if (apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_TYPE) != null) {
            sb.append(apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_TYPE));
        }
        sb.append(SdkConstant.CLOUDAPI_LF);
        if (apiRequest.getFirstHeaderValue("date") != null) {
            sb.append(apiRequest.getFirstHeaderValue("date"));
        }
        sb.append(SdkConstant.CLOUDAPI_LF);
        sb.append(buildHeaders(apiRequest));
        sb.append(buildResource(apiRequest));
        return sb.toString();
    }

    private static String buildResource(ApiRequest apiRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(apiRequest.getPath());
        TreeMap treeMap = new TreeMap();
        if (apiRequest.getQuerys() != null && apiRequest.getQuerys().size() > 0) {
            treeMap.putAll(apiRequest.getQuerys());
        }
        if (apiRequest.getFormParams() != null && apiRequest.getFormParams().size() > 0) {
            treeMap.putAll(apiRequest.getFormParams());
        }
        if (treeMap.size() > 0) {
            sb.append("?");
            boolean z = true;
            for (String str : treeMap.keySet()) {
                if (z) {
                    z = false;
                } else {
                    sb.append("&");
                }
                sb.append(str);
                String str2 = (String) treeMap.get(str);
                if (str2 != null && !"".equals(str2)) {
                    sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
                    sb.append(str2);
                }
            }
        }
        return sb.toString();
    }

    private static String buildHeaders(ApiRequest apiRequest) {
        TreeMap treeMap = new TreeMap();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, List<String>> entry : apiRequest.getHeaders().entrySet()) {
            if (entry.getKey().startsWith(SdkConstant.CLOUDAPI_CA_HEADER_TO_SIGN_PREFIX_SYSTEM)) {
                if (i != 0) {
                    sb.append(",");
                }
                i++;
                sb.append(entry.getKey());
                treeMap.put(entry.getKey(), apiRequest.getFirstHeaderValue(entry.getKey()));
            }
        }
        apiRequest.addHeader(SdkConstant.CLOUDAPI_X_CA_SIGNATURE_HEADERS, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        for (Map.Entry entry2 : treeMap.entrySet()) {
            sb2.append((String) entry2.getKey());
            sb2.append(':');
            sb2.append((String) entry2.getValue());
            sb2.append(SdkConstant.CLOUDAPI_LF);
        }
        return sb2.toString();
    }
}
