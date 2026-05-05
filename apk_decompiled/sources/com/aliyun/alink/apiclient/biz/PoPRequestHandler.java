package com.aliyun.alink.apiclient.biz;

import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.aliyun.alink.apiclient.CommonPoPRequest;
import com.aliyun.alink.apiclient.CommonRequest;
import com.aliyun.alink.apiclient.CommonResponse;
import com.aliyun.alink.apiclient.IoTCallback;
import com.aliyun.alink.apiclient.LocalData;
import com.aliyun.alink.apiclient.model.DeviceAuthInfo;
import com.aliyun.alink.apiclient.utils.AcsURLEncoder;
import com.aliyun.alink.apiclient.utils.DatatypeConverter;
import com.http.helper.HttpCallback;
import com.http.okhttp.OkHttpManager;
import com.http.utils.LogUtils;
import com.huawei.hms.framework.common.ContainerUtils;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
public class PoPRequestHandler implements IHandler {
    private static final String ALGORITHM_NAME = "HmacSHA1";
    public static final String ENCODING = "UTF-8";
    private static final String SEPARATOR = "&";
    private static final String TAG = "PoPRequestHandler";

    @Override // com.aliyun.alink.apiclient.biz.IHandler
    public void handle(CommonRequest commonRequest, IoTCallback ioTCallback) {
        if (ioTCallback == null || commonRequest == null) {
            return;
        }
        if (!(commonRequest instanceof CommonPoPRequest)) {
            ioTCallback.onFailure(commonRequest, new IllegalArgumentException("requestNotPopRequest"));
        } else {
            requestPop((CommonPoPRequest) commonRequest, ioTCallback);
        }
    }

    private void requestPop(final CommonPoPRequest commonPoPRequest, final IoTCallback ioTCallback) {
        LogUtils.print(TAG, "requestPop() called with: queryParams = [" + commonPoPRequest.getQueryParams() + "]");
        StringBuilder sb = new StringBuilder();
        sb.append(RequestHelper.getBaseUrl(commonPoPRequest, null));
        sb.append("/?");
        try {
            OkHttpManager.getInstance().postAsync(sb.toString(), null, RequestHelper.getFormMapString(getQueryMap(commonPoPRequest, RequestHelper.convert(commonPoPRequest.getQueryParams()))), new HttpCallback<IOException, String>() { // from class: com.aliyun.alink.apiclient.biz.PoPRequestHandler.1
                @Override // com.http.helper.HttpCallback
                public void onFail(String str, IOException iOException) {
                    LogUtils.error(PoPRequestHandler.TAG, "onFail() called with: s = [" + str + "], e = [" + iOException + "]");
                    ioTCallback.onFailure(commonPoPRequest, iOException);
                }

                @Override // com.http.helper.HttpCallback
                public void onSuccess(String str, String str2) {
                    LogUtils.print(PoPRequestHandler.TAG, "onSuccess() called with: s = [" + str + "], s2 = [" + str2 + "]");
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setData(str2);
                    ioTCallback.onResponse(commonPoPRequest, commonResponse);
                }
            });
        } catch (IOException e) {
            LogUtils.error(TAG, "onFail() called with: e = [" + e + "]");
            e.printStackTrace();
            ioTCallback.onFailure(commonPoPRequest, e);
        } catch (Exception e2) {
            LogUtils.error(TAG, "onFail() called with: e = [" + e2 + "]");
            e2.printStackTrace();
            ioTCallback.onFailure(commonPoPRequest, e2);
        }
    }

    private Map<String, String> getQueryMap(CommonPoPRequest commonPoPRequest, Map<String, String> map) {
        TreeMap treeMap = new TreeMap();
        DeviceAuthInfo deviceData = LocalData.getInstance().getDeviceData();
        treeMap.put("AccessKeyId", deviceData.accessKeyId);
        treeMap.put("Format", commonPoPRequest.getFormat());
        treeMap.put("RegionId", commonPoPRequest.getRegionId());
        treeMap.put("SignatureMethod", commonPoPRequest.getSignatureMethod());
        treeMap.put("SignatureNonce", commonPoPRequest.getSignatureNonce());
        treeMap.put("SignatureVersion", commonPoPRequest.getSignatureVersion());
        treeMap.put(RtspHeaders.Names.TIMESTAMP, commonPoPRequest.getTimestamp());
        treeMap.put("Version", commonPoPRequest.getVersion());
        treeMap.putAll(map);
        String strComposeStringToSign = composeStringToSign("POST", treeMap);
        LogUtils.print(TAG, "strToSign=" + strComposeStringToSign);
        treeMap.put(RequestParameters.SIGNATURE, signString(strComposeStringToSign, deviceData.accessSecret + "&"));
        return treeMap;
    }

    public String composeStringToSign(String str, Map<String, String> map) {
        String[] strArr = (String[]) map.keySet().toArray(new String[0]);
        Arrays.sort(strArr);
        StringBuilder sb = new StringBuilder();
        try {
            for (String str2 : strArr) {
                sb.append("&");
                sb.append(AcsURLEncoder.percentEncode(str2));
                sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
                sb.append(AcsURLEncoder.percentEncode(map.get(str2)));
            }
            return str + "&" + AcsURLEncoder.percentEncode("/") + "&" + AcsURLEncoder.percentEncode(sb.toString().substring(1));
        } catch (UnsupportedEncodingException unused) {
            throw new RuntimeException("UTF-8 encoding is not supported.");
        }
    }

    public String signString(String str, String str2) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(str2.getBytes("UTF-8"), "HmacSHA1"));
            return DatatypeConverter.printBase64Binary(mac.doFinal(str.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.toString());
        } catch (InvalidKeyException e2) {
            throw new IllegalArgumentException(e2.toString());
        } catch (NoSuchAlgorithmException e3) {
            throw new IllegalArgumentException(e3.toString());
        }
    }
}
