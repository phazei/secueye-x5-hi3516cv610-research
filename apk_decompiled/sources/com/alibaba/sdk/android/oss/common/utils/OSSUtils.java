package com.alibaba.sdk.android.oss.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.MimeTypeMap;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.OSSHeaders;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.alibaba.sdk.android.oss.common.auth.HmacSHA1Signature;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.exception.InconsistentException;
import com.alibaba.sdk.android.oss.internal.RequestMessage;
import com.alibaba.sdk.android.oss.model.CopyObjectRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.DeleteBucketRequest;
import com.alibaba.sdk.android.oss.model.GetBucketACLRequest;
import com.alibaba.sdk.android.oss.model.ListObjectsRequest;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.huawei.hms.framework.common.ContainerUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import kotlin.text.Typography;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class OSSUtils {
    private static final String NEW_LINE = "\n";
    private static final List<String> SIGNED_PARAMTERS = Arrays.asList(RequestParameters.SUBRESOURCE_ACL, RequestParameters.SUBRESOURCE_UPLOADS, "location", RequestParameters.SUBRESOURCE_CORS, RequestParameters.SUBRESOURCE_LOGGING, RequestParameters.SUBRESOURCE_WEBSITE, RequestParameters.SUBRESOURCE_REFERER, RequestParameters.SUBRESOURCE_LIFECYCLE, RequestParameters.SUBRESOURCE_DELETE, "append", RequestParameters.UPLOAD_ID, RequestParameters.PART_NUMBER, RequestParameters.SECURITY_TOKEN, RequestParameters.POSITION, RequestParameters.RESPONSE_HEADER_CACHE_CONTROL, RequestParameters.RESPONSE_HEADER_CONTENT_DISPOSITION, RequestParameters.RESPONSE_HEADER_CONTENT_ENCODING, RequestParameters.RESPONSE_HEADER_CONTENT_LANGUAGE, RequestParameters.RESPONSE_HEADER_CONTENT_TYPE, RequestParameters.RESPONSE_HEADER_EXPIRES, RequestParameters.X_OSS_PROCESS, RequestParameters.SUBRESOURCE_SEQUENTIAL);

    public static boolean checkParamRange(long j, long j2, boolean z, long j3, boolean z2) {
        return (z && z2) ? j2 <= j && j <= j3 : (!z || z2) ? (z || z2) ? j2 < j && j <= j3 : j2 < j && j < j3 : j2 <= j && j < j3;
    }

    public static void populateRequestMetadata(Map<String, String> map, ObjectMetadata objectMetadata) {
        if (objectMetadata == null) {
            return;
        }
        Map<String, Object> rawMetadata = objectMetadata.getRawMetadata();
        if (rawMetadata != null) {
            for (Map.Entry<String, Object> entry : rawMetadata.entrySet()) {
                map.put(entry.getKey(), entry.getValue().toString());
            }
        }
        Map<String, String> userMetadata = objectMetadata.getUserMetadata();
        if (userMetadata != null) {
            for (Map.Entry<String, String> entry2 : userMetadata.entrySet()) {
                String key = entry2.getKey();
                String value = entry2.getValue();
                if (key != null) {
                    key = key.trim();
                }
                if (value != null) {
                    value = value.trim();
                }
                map.put(key, value);
            }
        }
    }

    public static void populateListObjectsRequestParameters(ListObjectsRequest listObjectsRequest, Map<String, String> map) {
        if (listObjectsRequest.getPrefix() != null) {
            map.put(RequestParameters.PREFIX, listObjectsRequest.getPrefix());
        }
        if (listObjectsRequest.getMarker() != null) {
            map.put(RequestParameters.MARKER, listObjectsRequest.getMarker());
        }
        if (listObjectsRequest.getDelimiter() != null) {
            map.put(RequestParameters.DELIMITER, listObjectsRequest.getDelimiter());
        }
        if (listObjectsRequest.getMaxKeys() != null) {
            map.put(RequestParameters.MAX_KEYS, Integer.toString(listObjectsRequest.getMaxKeys().intValue()));
        }
        if (listObjectsRequest.getEncodingType() != null) {
            map.put(RequestParameters.ENCODING_TYPE, listObjectsRequest.getEncodingType());
        }
    }

    private enum MetadataDirective {
        COPY("COPY"),
        REPLACE("REPLACE");

        private final String directiveAsString;

        MetadataDirective(String str) {
            this.directiveAsString = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.directiveAsString;
        }
    }

    public static void populateCopyObjectHeaders(CopyObjectRequest copyObjectRequest, Map<String, String> map) {
        map.put(OSSHeaders.COPY_OBJECT_SOURCE, "/" + copyObjectRequest.getSourceBucketName() + "/" + HttpUtil.urlEncode(copyObjectRequest.getSourceKey(), "utf-8"));
        addDateHeader(map, OSSHeaders.COPY_OBJECT_SOURCE_IF_MODIFIED_SINCE, copyObjectRequest.getModifiedSinceConstraint());
        addDateHeader(map, OSSHeaders.COPY_OBJECT_SOURCE_IF_UNMODIFIED_SINCE, copyObjectRequest.getUnmodifiedSinceConstraint());
        addStringListHeader(map, OSSHeaders.COPY_OBJECT_SOURCE_IF_MATCH, copyObjectRequest.getMatchingETagConstraints());
        addStringListHeader(map, OSSHeaders.COPY_OBJECT_SOURCE_IF_NONE_MATCH, copyObjectRequest.getNonmatchingEtagConstraints());
        addHeader(map, OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION, copyObjectRequest.getServerSideEncryption());
        ObjectMetadata newObjectMetadata = copyObjectRequest.getNewObjectMetadata();
        if (newObjectMetadata != null) {
            map.put(OSSHeaders.COPY_OBJECT_METADATA_DIRECTIVE, MetadataDirective.REPLACE.toString());
            populateRequestMetadata(map, newObjectMetadata);
        }
        removeHeader(map, "Content-Length");
    }

    public static String buildXMLFromPartEtagList(List<PartETag> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("<CompleteMultipartUpload>\n");
        for (PartETag partETag : list) {
            sb.append("<Part>\n");
            sb.append("<PartNumber>" + partETag.getPartNumber() + "</PartNumber>\n");
            sb.append("<ETag>" + partETag.getETag() + "</ETag>\n");
            sb.append("</Part>\n");
        }
        sb.append("</CompleteMultipartUpload>\n");
        return sb.toString();
    }

    public static void addHeader(Map<String, String> map, String str, String str2) {
        if (str2 != null) {
            map.put(str, str2);
        }
    }

    public static void addDateHeader(Map<String, String> map, String str, Date date) {
        if (date != null) {
            map.put(str, DateUtil.formatRfc822Date(date));
        }
    }

    public static void addStringListHeader(Map<String, String> map, String str, List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        map.put(str, join(list));
    }

    public static void removeHeader(Map<String, String> map, String str) {
        if (str == null || !map.containsKey(str)) {
            return;
        }
        map.remove(str);
    }

    public static String join(List<String> list) {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String str : list) {
            if (!z) {
                sb.append(", ");
            }
            sb.append(str);
            z = false;
        }
        return sb.toString();
    }

    public static boolean isEmptyString(String str) {
        return TextUtils.isEmpty(str);
    }

    public static String buildCanonicalString(RequestMessage requestMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append(requestMessage.getMethod().toString() + "\n");
        Map headers = requestMessage.getHeaders();
        TreeMap treeMap = new TreeMap();
        if (headers != null) {
            for (Map.Entry entry : headers.entrySet()) {
                if (entry.getKey() != null) {
                    String lowerCase = ((String) entry.getKey()).toLowerCase();
                    if (lowerCase.equals("Content-Type".toLowerCase()) || lowerCase.equals("Content-MD5".toLowerCase()) || lowerCase.equals("Date".toLowerCase()) || lowerCase.startsWith(OSSHeaders.OSS_PREFIX)) {
                        treeMap.put(lowerCase, ((String) entry.getValue()).trim());
                    }
                }
            }
        }
        if (!treeMap.containsKey("Content-Type".toLowerCase())) {
            treeMap.put("Content-Type".toLowerCase(), "");
        }
        if (!treeMap.containsKey("Content-MD5".toLowerCase())) {
            treeMap.put("Content-MD5".toLowerCase(), "");
        }
        for (Map.Entry entry2 : treeMap.entrySet()) {
            String str = (String) entry2.getKey();
            Object value = entry2.getValue();
            if (str.startsWith(OSSHeaders.OSS_PREFIX)) {
                sb.append(str);
                sb.append(':');
                sb.append(value);
            } else {
                sb.append(value);
            }
            sb.append("\n");
        }
        sb.append(buildCanonicalizedResource(requestMessage.getBucketName(), requestMessage.getObjectKey(), requestMessage.getParameters()));
        return sb.toString();
    }

    public static String buildCanonicalizedResource(String str, String str2, Map<String, String> map) {
        String str3;
        if (str == null && str2 == null) {
            str3 = "/";
        } else if (str2 == null) {
            str3 = "/" + str + "/";
        } else {
            str3 = "/" + str + "/" + str2;
        }
        return buildCanonicalizedResource(str3, map);
    }

    public static String buildCanonicalizedResource(String str, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if (map != null) {
            String[] strArr = (String[]) map.keySet().toArray(new String[map.size()]);
            Arrays.sort(strArr);
            char c2 = '?';
            for (String str2 : strArr) {
                if (SIGNED_PARAMTERS.contains(str2)) {
                    sb.append(c2);
                    sb.append(str2);
                    String str3 = map.get(str2);
                    if (!isEmptyString(str3)) {
                        sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
                        sb.append(str3);
                    }
                    c2 = Typography.amp;
                }
            }
        }
        return sb.toString();
    }

    public static String paramToQueryString(Map<String, String> map, String str) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!z) {
                sb.append("&");
            }
            sb.append(HttpUtil.urlEncode(key, str));
            if (!isEmptyString(value)) {
                sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
                sb.append(HttpUtil.urlEncode(value, str));
            }
            z = false;
        }
        return sb.toString();
    }

    public static String populateMapToBase64JsonString(Map<String, String> map) {
        return Base64.encodeToString(new JSONObject(map).toString().getBytes(), 2);
    }

    public static String sign(String str, String str2, String str3) {
        try {
            return "OSS " + str + ":" + new HmacSHA1Signature().computeSignature(str2, str3).trim();
        } catch (Exception e) {
            throw new IllegalStateException("Compute signature failed!", e);
        }
    }

    public static boolean isCname(String str) {
        for (String str2 : OSSConstants.DEFAULT_CNAME_EXCLUDE_LIST) {
            if (str.toLowerCase().endsWith(str2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInCustomCnameExcludeList(String str, List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (str.endsWith(it.next().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static void assertTrue(boolean z, String str) {
        if (!z) {
            throw new IllegalArgumentException(str);
        }
    }

    public static boolean validateBucketName(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("^[a-z0-9][a-z0-9_\\-]{2,62}$");
    }

    public static void ensureBucketNameValid(String str) {
        if (!validateBucketName(str)) {
            throw new IllegalArgumentException("The bucket name is invalid. \nA bucket name must: \n1) be comprised of lower-case characters, numbers or dash(-); \n2) start with lower case or numbers; \n3) be between 3-63 characters long. ");
        }
    }

    public static boolean validateObjectKey(String str) {
        if (str == null || str.length() <= 0 || str.length() > 1023) {
            return false;
        }
        try {
            str.getBytes("utf-8");
            char[] charArray = str.toCharArray();
            char c2 = charArray[0];
            if (c2 == '/' || c2 == '\\') {
                return false;
            }
            for (char c3 : charArray) {
                if (c3 != '\t' && c3 < ' ') {
                    return false;
                }
            }
            return true;
        } catch (UnsupportedEncodingException unused) {
            return false;
        }
    }

    public static void ensureObjectKeyValid(String str) {
        if (!validateObjectKey(str)) {
            throw new IllegalArgumentException("The object key is invalid. \nAn object name should be: \n1) between 1 - 1023 bytes long when encoded as UTF-8 \n2) cannot contain LF or CR or unsupported chars in XML1.0, \n3) cannot begin with \"/\" or \"\\\".");
        }
    }

    public static boolean doesRequestNeedObjectKey(OSSRequest oSSRequest) {
        return ((oSSRequest instanceof ListObjectsRequest) || (oSSRequest instanceof CreateBucketRequest) || (oSSRequest instanceof DeleteBucketRequest) || (oSSRequest instanceof GetBucketACLRequest)) ? false : true;
    }

    public static void ensureRequestValid(OSSRequest oSSRequest, RequestMessage requestMessage) {
        ensureBucketNameValid(requestMessage.getBucketName());
        if (doesRequestNeedObjectKey(oSSRequest)) {
            ensureObjectKeyValid(requestMessage.getObjectKey());
        }
        if (oSSRequest instanceof CopyObjectRequest) {
            ensureObjectKeyValid(((CopyObjectRequest) oSSRequest).getDestinationKey());
        }
    }

    public static String determineContentType(String str, String str2, String str3) {
        String mimeTypeFromExtension;
        String mimeTypeFromExtension2;
        if (str != null) {
            return str;
        }
        MimeTypeMap singleton = MimeTypeMap.getSingleton();
        return (str2 == null || (mimeTypeFromExtension2 = singleton.getMimeTypeFromExtension(str2.substring(str2.lastIndexOf(46) + 1))) == null) ? (str3 == null || (mimeTypeFromExtension = singleton.getMimeTypeFromExtension(str3.substring(str3.lastIndexOf(46) + 1))) == null) ? "application/octet-stream" : mimeTypeFromExtension : mimeTypeFromExtension2;
    }

    public static void signRequest(RequestMessage requestMessage) throws Exception {
        if (requestMessage.isAuthorizationRequired()) {
            if (requestMessage.getCredentialProvider() == null) {
                throw new IllegalStateException("当前CredentialProvider为空！！！\n1. 请检查您是否在初始化OSSService时设置CredentialProvider;\n2. 如果您bucket为公共权限，请确认获取到Bucket后已经调用Bucket中接口声明ACL;");
            }
            OSSCredentialProvider credentialProvider = requestMessage.getCredentialProvider();
            OSSFederationToken federationToken = null;
            boolean z = credentialProvider instanceof OSSFederationCredentialProvider;
            if (z) {
                federationToken = ((OSSFederationCredentialProvider) credentialProvider).getValidFederationToken();
                if (federationToken == null) {
                    OSSLog.logError("Can't get a federation token");
                    throw new IOException("Can't get a federation token");
                }
                requestMessage.getHeaders().put(OSSHeaders.OSS_SECURITY_TOKEN, federationToken.getSecurityToken());
            } else if (credentialProvider instanceof OSSStsTokenCredentialProvider) {
                federationToken = credentialProvider.getFederationToken();
                requestMessage.getHeaders().put(OSSHeaders.OSS_SECURITY_TOKEN, federationToken.getSecurityToken());
            }
            String strBuildCanonicalString = buildCanonicalString(requestMessage);
            String strSign = "---initValue---";
            if (z || (credentialProvider instanceof OSSStsTokenCredentialProvider)) {
                strSign = sign(federationToken.getTempAK(), federationToken.getTempSK(), strBuildCanonicalString);
            } else if (credentialProvider instanceof OSSPlainTextAKSKCredentialProvider) {
                OSSPlainTextAKSKCredentialProvider oSSPlainTextAKSKCredentialProvider = (OSSPlainTextAKSKCredentialProvider) credentialProvider;
                strSign = sign(oSSPlainTextAKSKCredentialProvider.getAccessKeyId(), oSSPlainTextAKSKCredentialProvider.getAccessKeySecret(), strBuildCanonicalString);
            } else if (credentialProvider instanceof OSSCustomSignerCredentialProvider) {
                strSign = ((OSSCustomSignerCredentialProvider) credentialProvider).signContent(strBuildCanonicalString);
            }
            OSSLog.logDebug("signed content: " + strBuildCanonicalString + "   \n ---------   signature: " + strSign, false);
            requestMessage.getHeaders().put("Authorization", strSign);
        }
    }

    public static String buildBaseLogInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("=====[device info]=====\n");
        sb.append("[INFO]: android_version：" + Build.VERSION.RELEASE + "\n");
        sb.append("[INFO]: mobile_model：" + Build.MODEL + "\n");
        String operatorName = getOperatorName(context);
        if (!TextUtils.isEmpty(operatorName)) {
            sb.append("[INFO]: operator_name：" + operatorName + "\n");
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        String str = "unconnected";
        String str2 = "unknown";
        if (activeNetworkInfo != null && activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
            str2 = activeNetworkInfo.getTypeName() + " ";
            str = "connected";
        }
        sb.append("[INFO]: network_state：" + str + "\n");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[INFO]: network_type：");
        sb2.append(str2);
        sb.append(sb2.toString());
        return sb.toString();
    }

    private static String getOperatorName(Context context) {
        String simOperator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        return simOperator != null ? (simOperator.equals("46000") || simOperator.equals("46002")) ? "CMCC" : simOperator.equals("46001") ? "CUCC" : simOperator.equals("46003") ? "CTCC" : simOperator : "";
    }

    public static void checkChecksum(Long l, Long l2, String str) throws InconsistentException {
        if (l != null && l2 != null && !l.equals(l2)) {
            throw new InconsistentException(l, l2, str);
        }
    }

    public static boolean isIP(String str) {
        if (str.length() < 7 || str.length() > 15 || "".equals(str)) {
            return false;
        }
        return Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}").matcher(str).find();
    }

    public static String buildTriggerCallbackBody(Map<String, String> map, Map<String, String> map2) {
        StringBuilder sb = new StringBuilder();
        sb.append("x-oss-process=trigger/callback,callback_");
        if (map != null && map.size() > 0) {
            sb.append(Base64.encodeToString(new JSONObject(map).toString().getBytes(), 2));
        }
        sb.append(",callback-var_");
        if (map2 != null && map2.size() > 0) {
            sb.append(Base64.encodeToString(new JSONObject(map2).toString().getBytes(), 2));
        }
        return sb.toString();
    }
}
