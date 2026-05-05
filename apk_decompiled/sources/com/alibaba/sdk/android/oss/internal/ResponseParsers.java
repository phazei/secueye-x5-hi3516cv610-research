package com.alibaba.sdk.android.oss.internal;

import android.util.Xml;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSHeaders;
import com.alibaba.sdk.android.oss.common.utils.CRC64;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.AppendObjectResult;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.CopyObjectResult;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetBucketACLResult;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.HeadObjectResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.ListObjectsResult;
import com.alibaba.sdk.android.oss.model.ListPartsResult;
import com.alibaba.sdk.android.oss.model.OSSObjectSummary;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.Owner;
import com.alibaba.sdk.android.oss.model.PartSummary;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.TriggerCallbackResult;
import com.alibaba.sdk.android.oss.model.UploadPartResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public final class ResponseParsers {

    public static final class AbortMultipartUploadResponseParser extends AbstractResponseParser<AbortMultipartUploadResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public AbortMultipartUploadResult parseData(ResponseMessage responseMessage, AbortMultipartUploadResult abortMultipartUploadResult) throws IOException {
            return abortMultipartUploadResult;
        }
    }

    public static final class DeleteBucketResponseParser extends AbstractResponseParser<DeleteBucketResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public DeleteBucketResult parseData(ResponseMessage responseMessage, DeleteBucketResult deleteBucketResult) throws IOException {
            return deleteBucketResult;
        }
    }

    public static final class DeleteObjectResponseParser extends AbstractResponseParser<DeleteObjectResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public DeleteObjectResult parseData(ResponseMessage responseMessage, DeleteObjectResult deleteObjectResult) throws IOException {
            return deleteObjectResult;
        }
    }

    public static final class PutObjectResponseParser extends AbstractResponseParser<PutObjectResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public PutObjectResult parseData(ResponseMessage responseMessage, PutObjectResult putObjectResult) throws IOException {
            putObjectResult.setETag(ResponseParsers.trimQuotes((String) responseMessage.getHeaders().get("ETag")));
            if (responseMessage.getContentLength() > 0) {
                putObjectResult.setServerCallbackReturnBody(responseMessage.getResponse().body().string());
            }
            return putObjectResult;
        }
    }

    public static final class AppendObjectResponseParser extends AbstractResponseParser<AppendObjectResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public AppendObjectResult parseData(ResponseMessage responseMessage, AppendObjectResult appendObjectResult) throws IOException {
            String str = (String) responseMessage.getHeaders().get(OSSHeaders.OSS_NEXT_APPEND_POSITION);
            if (str != null) {
                appendObjectResult.setNextPosition(Long.valueOf(str));
            }
            appendObjectResult.setObjectCRC64((String) responseMessage.getHeaders().get(OSSHeaders.OSS_HASH_CRC64_ECMA));
            return appendObjectResult;
        }
    }

    public static final class HeadObjectResponseParser extends AbstractResponseParser<HeadObjectResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public HeadObjectResult parseData(ResponseMessage responseMessage, HeadObjectResult headObjectResult) throws IOException {
            headObjectResult.setMetadata(ResponseParsers.parseObjectMetadata(headObjectResult.getResponseHeader()));
            return headObjectResult;
        }
    }

    public static final class GetObjectResponseParser extends AbstractResponseParser<GetObjectResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public boolean needCloseResponse() {
            return false;
        }

        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public GetObjectResult parseData(ResponseMessage responseMessage, GetObjectResult getObjectResult) throws IOException {
            getObjectResult.setMetadata(ResponseParsers.parseObjectMetadata(getObjectResult.getResponseHeader()));
            getObjectResult.setContentLength(responseMessage.getContentLength());
            if (responseMessage.getRequest().isCheckCRC64()) {
                getObjectResult.setObjectContent(new CheckCRC64DownloadInputStream(responseMessage.getContent(), new CRC64(), responseMessage.getContentLength(), getObjectResult.getServerCRC().longValue(), getObjectResult.getRequestId()));
            } else {
                getObjectResult.setObjectContent(responseMessage.getContent());
            }
            return getObjectResult;
        }
    }

    public static final class CopyObjectResponseParser extends AbstractResponseParser<CopyObjectResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public CopyObjectResult parseData(ResponseMessage responseMessage, CopyObjectResult copyObjectResult) throws Exception {
            return ResponseParsers.parseCopyObjectResponseXML(responseMessage.getContent(), copyObjectResult);
        }
    }

    public static final class CreateBucketResponseParser extends AbstractResponseParser<CreateBucketResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public CreateBucketResult parseData(ResponseMessage responseMessage, CreateBucketResult createBucketResult) throws IOException {
            if (createBucketResult.getResponseHeader().containsKey("Location")) {
                createBucketResult.bucketLocation = createBucketResult.getResponseHeader().get("Location");
            }
            return createBucketResult;
        }
    }

    public static final class GetBucketACLResponseParser extends AbstractResponseParser<GetBucketACLResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public GetBucketACLResult parseData(ResponseMessage responseMessage, GetBucketACLResult getBucketACLResult) throws Exception {
            return ResponseParsers.parseGetBucketACLResponse(responseMessage.getContent(), getBucketACLResult);
        }
    }

    public static final class ListObjectsResponseParser extends AbstractResponseParser<ListObjectsResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public ListObjectsResult parseData(ResponseMessage responseMessage, ListObjectsResult listObjectsResult) throws Exception {
            return ResponseParsers.parseObjectListResponse(responseMessage.getContent(), listObjectsResult);
        }
    }

    public static final class InitMultipartResponseParser extends AbstractResponseParser<InitiateMultipartUploadResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public InitiateMultipartUploadResult parseData(ResponseMessage responseMessage, InitiateMultipartUploadResult initiateMultipartUploadResult) throws Exception {
            return ResponseParsers.parseInitMultipartResponseXML(responseMessage.getContent(), initiateMultipartUploadResult);
        }
    }

    public static final class UploadPartResponseParser extends AbstractResponseParser<UploadPartResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public UploadPartResult parseData(ResponseMessage responseMessage, UploadPartResult uploadPartResult) throws IOException {
            uploadPartResult.setETag(ResponseParsers.trimQuotes((String) responseMessage.getHeaders().get("ETag")));
            return uploadPartResult;
        }
    }

    public static final class CompleteMultipartUploadResponseParser extends AbstractResponseParser<CompleteMultipartUploadResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public CompleteMultipartUploadResult parseData(ResponseMessage responseMessage, CompleteMultipartUploadResult completeMultipartUploadResult) throws Exception {
            if (((String) responseMessage.getHeaders().get("Content-Type")).equals("application/xml")) {
                return ResponseParsers.parseCompleteMultipartUploadResponseXML(responseMessage.getContent(), completeMultipartUploadResult);
            }
            if (responseMessage.getResponse().body() == null) {
                return completeMultipartUploadResult;
            }
            completeMultipartUploadResult.setServerCallbackReturnBody(responseMessage.getResponse().body().string());
            return completeMultipartUploadResult;
        }
    }

    public static final class ListPartsResponseParser extends AbstractResponseParser<ListPartsResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public ListPartsResult parseData(ResponseMessage responseMessage, ListPartsResult listPartsResult) throws Exception {
            return ResponseParsers.parseListPartsResponseXML(responseMessage.getContent(), listPartsResult);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static CopyObjectResult parseCopyObjectResponseXML(InputStream inputStream, CopyObjectResult copyObjectResult) throws XmlPullParserException, IOException, ParseException {
        XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
        xmlPullParserNewPullParser.setInput(inputStream, "utf-8");
        int eventType = xmlPullParserNewPullParser.getEventType();
        while (eventType != 1) {
            if (eventType == 2) {
                String name = xmlPullParserNewPullParser.getName();
                if ("LastModified".equals(name)) {
                    copyObjectResult.setLastModified(DateUtil.parseIso8601Date(xmlPullParserNewPullParser.nextText()));
                } else if ("ETag".equals(name)) {
                    copyObjectResult.setEtag(xmlPullParserNewPullParser.nextText());
                }
            }
            eventType = xmlPullParserNewPullParser.next();
            if (eventType == 4) {
                eventType = xmlPullParserNewPullParser.next();
            }
        }
        return copyObjectResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ListPartsResult parseListPartsResponseXML(InputStream inputStream, ListPartsResult listPartsResult) throws XmlPullParserException, IOException, ParseException {
        ArrayList arrayList = new ArrayList();
        XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
        xmlPullParserNewPullParser.setInput(inputStream, "utf-8");
        int eventType = xmlPullParserNewPullParser.getEventType();
        PartSummary partSummary = null;
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = xmlPullParserNewPullParser.getName();
                    if ("Bucket".equals(name)) {
                        listPartsResult.setBucketName(xmlPullParserNewPullParser.nextText());
                    } else if ("Key".equals(name)) {
                        listPartsResult.setKey(xmlPullParserNewPullParser.nextText());
                    } else if ("UploadId".equals(name)) {
                        listPartsResult.setUploadId(xmlPullParserNewPullParser.nextText());
                    } else if ("PartNumberMarker".equals(name)) {
                        String strNextText = xmlPullParserNewPullParser.nextText();
                        if (!OSSUtils.isEmptyString(strNextText)) {
                            listPartsResult.setPartNumberMarker(Integer.valueOf(strNextText).intValue());
                        }
                    } else if ("NextPartNumberMarker".equals(name)) {
                        String strNextText2 = xmlPullParserNewPullParser.nextText();
                        if (!OSSUtils.isEmptyString(strNextText2)) {
                            listPartsResult.setNextPartNumberMarker(Integer.valueOf(strNextText2).intValue());
                        }
                    } else if ("MaxParts".equals(name)) {
                        String strNextText3 = xmlPullParserNewPullParser.nextText();
                        if (!OSSUtils.isEmptyString(strNextText3)) {
                            listPartsResult.setMaxParts(Integer.valueOf(strNextText3).intValue());
                        }
                    } else if ("IsTruncated".equals(name)) {
                        String strNextText4 = xmlPullParserNewPullParser.nextText();
                        if (!OSSUtils.isEmptyString(strNextText4)) {
                            listPartsResult.setTruncated(Boolean.valueOf(strNextText4).booleanValue());
                        }
                    } else if ("StorageClass".equals(name)) {
                        listPartsResult.setStorageClass(xmlPullParserNewPullParser.nextText());
                    } else if ("Part".equals(name)) {
                        partSummary = new PartSummary();
                    } else if ("PartNumber".equals(name)) {
                        String strNextText5 = xmlPullParserNewPullParser.nextText();
                        if (!OSSUtils.isEmptyString(strNextText5)) {
                            partSummary.setPartNumber(Integer.valueOf(strNextText5).intValue());
                        }
                    } else if ("LastModified".equals(name)) {
                        partSummary.setLastModified(DateUtil.parseIso8601Date(xmlPullParserNewPullParser.nextText()));
                    } else if ("ETag".equals(name)) {
                        partSummary.setETag(xmlPullParserNewPullParser.nextText());
                    } else if ("Size".equals(name)) {
                        String strNextText6 = xmlPullParserNewPullParser.nextText();
                        if (!OSSUtils.isEmptyString(strNextText6)) {
                            partSummary.setSize(Long.valueOf(strNextText6).longValue());
                        }
                    }
                    break;
                case 3:
                    if ("Part".equals(xmlPullParserNewPullParser.getName())) {
                        arrayList.add(partSummary);
                    }
                    break;
            }
            eventType = xmlPullParserNewPullParser.next();
            if (eventType == 4) {
                eventType = xmlPullParserNewPullParser.next();
            }
        }
        if (arrayList.size() > 0) {
            listPartsResult.setParts(arrayList);
        }
        return listPartsResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static CompleteMultipartUploadResult parseCompleteMultipartUploadResponseXML(InputStream inputStream, CompleteMultipartUploadResult completeMultipartUploadResult) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
        xmlPullParserNewPullParser.setInput(inputStream, "utf-8");
        int eventType = xmlPullParserNewPullParser.getEventType();
        while (eventType != 1) {
            if (eventType == 2) {
                String name = xmlPullParserNewPullParser.getName();
                if ("Location".equals(name)) {
                    completeMultipartUploadResult.setLocation(xmlPullParserNewPullParser.nextText());
                } else if ("Bucket".equals(name)) {
                    completeMultipartUploadResult.setBucketName(xmlPullParserNewPullParser.nextText());
                } else if ("Key".equals(name)) {
                    completeMultipartUploadResult.setObjectKey(xmlPullParserNewPullParser.nextText());
                } else if ("ETag".equals(name)) {
                    completeMultipartUploadResult.setETag(xmlPullParserNewPullParser.nextText());
                }
            }
            eventType = xmlPullParserNewPullParser.next();
            if (eventType == 4) {
                eventType = xmlPullParserNewPullParser.next();
            }
        }
        return completeMultipartUploadResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static InitiateMultipartUploadResult parseInitMultipartResponseXML(InputStream inputStream, InitiateMultipartUploadResult initiateMultipartUploadResult) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
        xmlPullParserNewPullParser.setInput(inputStream, "utf-8");
        int eventType = xmlPullParserNewPullParser.getEventType();
        while (eventType != 1) {
            if (eventType == 2) {
                String name = xmlPullParserNewPullParser.getName();
                if ("Bucket".equals(name)) {
                    initiateMultipartUploadResult.setBucketName(xmlPullParserNewPullParser.nextText());
                } else if ("Key".equals(name)) {
                    initiateMultipartUploadResult.setObjectKey(xmlPullParserNewPullParser.nextText());
                } else if ("UploadId".equals(name)) {
                    initiateMultipartUploadResult.setUploadId(xmlPullParserNewPullParser.nextText());
                }
            }
            eventType = xmlPullParserNewPullParser.next();
            if (eventType == 4) {
                eventType = xmlPullParserNewPullParser.next();
            }
        }
        return initiateMultipartUploadResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static GetBucketACLResult parseGetBucketACLResponse(InputStream inputStream, GetBucketACLResult getBucketACLResult) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
        xmlPullParserNewPullParser.setInput(inputStream, "utf-8");
        int eventType = xmlPullParserNewPullParser.getEventType();
        while (eventType != 1) {
            if (eventType == 2) {
                String name = xmlPullParserNewPullParser.getName();
                if ("Grant".equals(name)) {
                    getBucketACLResult.setBucketACL(xmlPullParserNewPullParser.nextText());
                } else if ("ID".equals(name)) {
                    getBucketACLResult.setBucketOwnerID(xmlPullParserNewPullParser.nextText());
                } else if ("DisplayName".equals(name)) {
                    getBucketACLResult.setBucketOwner(xmlPullParserNewPullParser.nextText());
                }
            }
            eventType = xmlPullParserNewPullParser.next();
            if (eventType == 4) {
                eventType = xmlPullParserNewPullParser.next();
            }
        }
        return getBucketACLResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ListObjectsResult parseObjectListResponse(InputStream inputStream, ListObjectsResult listObjectsResult) throws XmlPullParserException, IOException, ParseException {
        listObjectsResult.clearCommonPrefixes();
        listObjectsResult.clearObjectSummaries();
        XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
        xmlPullParserNewPullParser.setInput(inputStream, "utf-8");
        int eventType = xmlPullParserNewPullParser.getEventType();
        Owner owner = null;
        boolean z = false;
        OSSObjectSummary oSSObjectSummary = null;
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = xmlPullParserNewPullParser.getName();
                    if ("Name".equals(name)) {
                        listObjectsResult.setBucketName(xmlPullParserNewPullParser.nextText());
                    } else if (!"Prefix".equals(name)) {
                        if ("Marker".equals(name)) {
                            listObjectsResult.setMarker(xmlPullParserNewPullParser.nextText());
                        } else if ("Delimiter".equals(name)) {
                            listObjectsResult.setDelimiter(xmlPullParserNewPullParser.nextText());
                        } else if ("EncodingType".equals(name)) {
                            listObjectsResult.setEncodingType(xmlPullParserNewPullParser.nextText());
                        } else if ("MaxKeys".equals(name)) {
                            String strNextText = xmlPullParserNewPullParser.nextText();
                            if (!OSSUtils.isEmptyString(strNextText)) {
                                listObjectsResult.setMaxKeys(Integer.valueOf(strNextText).intValue());
                            }
                        } else if ("NextMarker".equals(name)) {
                            listObjectsResult.setNextMarker(xmlPullParserNewPullParser.nextText());
                        } else if ("IsTruncated".equals(name)) {
                            String strNextText2 = xmlPullParserNewPullParser.nextText();
                            if (!OSSUtils.isEmptyString(strNextText2)) {
                                listObjectsResult.setTruncated(Boolean.valueOf(strNextText2).booleanValue());
                            }
                        } else if ("Contents".equals(name)) {
                            oSSObjectSummary = new OSSObjectSummary();
                        } else if ("Key".equals(name)) {
                            oSSObjectSummary.setKey(xmlPullParserNewPullParser.nextText());
                        } else if ("LastModified".equals(name)) {
                            oSSObjectSummary.setLastModified(DateUtil.parseIso8601Date(xmlPullParserNewPullParser.nextText()));
                        } else if ("Size".equals(name)) {
                            String strNextText3 = xmlPullParserNewPullParser.nextText();
                            if (!OSSUtils.isEmptyString(strNextText3)) {
                                oSSObjectSummary.setSize(Long.valueOf(strNextText3).longValue());
                            }
                        } else if ("ETag".equals(name)) {
                            oSSObjectSummary.setETag(xmlPullParserNewPullParser.nextText());
                        } else if ("Type".equals(name)) {
                            oSSObjectSummary.setType(xmlPullParserNewPullParser.nextText());
                        } else if ("StorageClass".equals(name)) {
                            oSSObjectSummary.setStorageClass(xmlPullParserNewPullParser.nextText());
                        } else if ("Owner".equals(name)) {
                            owner = new Owner();
                        } else if ("ID".equals(name)) {
                            owner.setId(xmlPullParserNewPullParser.nextText());
                        } else if ("DisplayName".equals(name)) {
                            owner.setDisplayName(xmlPullParserNewPullParser.nextText());
                        } else if ("CommonPrefixes".equals(name)) {
                            z = true;
                        }
                    } else if (z) {
                        String strNextText4 = xmlPullParserNewPullParser.nextText();
                        if (!OSSUtils.isEmptyString(strNextText4)) {
                            listObjectsResult.addCommonPrefix(strNextText4);
                        }
                    } else {
                        listObjectsResult.setPrefix(xmlPullParserNewPullParser.nextText());
                    }
                    break;
                case 3:
                    String name2 = xmlPullParserNewPullParser.getName();
                    if (!"Owner".equals(xmlPullParserNewPullParser.getName())) {
                        if (!"Contents".equals(name2)) {
                            if ("CommonPrefixes".equals(name2)) {
                                z = false;
                            }
                        } else if (oSSObjectSummary != null) {
                            oSSObjectSummary.setBucketName(listObjectsResult.getBucketName());
                            listObjectsResult.addObjectSummary(oSSObjectSummary);
                        }
                    } else if (owner != null) {
                        oSSObjectSummary.setOwner(owner);
                    }
                    break;
            }
            eventType = xmlPullParserNewPullParser.next();
            if (eventType == 4) {
                eventType = xmlPullParserNewPullParser.next();
            }
        }
        return listObjectsResult;
    }

    public static String trimQuotes(String str) {
        if (str == null) {
            return null;
        }
        String strTrim = str.trim();
        if (strTrim.startsWith("\"")) {
            strTrim = strTrim.substring(1);
        }
        return strTrim.endsWith("\"") ? strTrim.substring(0, strTrim.length() - 1) : strTrim;
    }

    public static ObjectMetadata parseObjectMetadata(Map<String, String> map) throws IOException {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            for (String str : map.keySet()) {
                if (str.indexOf(OSSHeaders.OSS_USER_METADATA_PREFIX) >= 0) {
                    objectMetadata.addUserMetadata(str, map.get(str));
                } else if (str.equals("Last-Modified") || str.equals("Date")) {
                    try {
                        objectMetadata.setHeader(str, DateUtil.parseRfc822Date(map.get(str)));
                    } catch (ParseException e) {
                        throw new IOException(e.getMessage(), e);
                    }
                } else if (str.equals("Content-Length")) {
                    objectMetadata.setHeader(str, Long.valueOf(map.get(str)));
                } else if (str.equals("ETag")) {
                    objectMetadata.setHeader(str, trimQuotes(map.get(str)));
                } else {
                    objectMetadata.setHeader(str, map.get(str));
                }
            }
            return objectMetadata;
        } catch (Exception e2) {
            throw new IOException(e2.getMessage(), e2);
        }
    }

    public static ServiceException parseResponseErrorXML(ResponseMessage responseMessage, boolean z) throws ClientException {
        String strNextText;
        String str;
        String str2;
        String str3;
        int statusCode = responseMessage.getStatusCode();
        String strHeader = responseMessage.getResponse().header(OSSHeaders.OSS_HEADER_REQUEST_ID);
        String strNextText2 = null;
        if (z) {
            str3 = strHeader;
            str2 = null;
            strNextText = null;
            str = null;
        } else {
            try {
                String strString = responseMessage.getResponse().body().string();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(strString.getBytes());
                XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                xmlPullParserNewPullParser.setInput(byteArrayInputStream, "utf-8");
                int eventType = xmlPullParserNewPullParser.getEventType();
                String strNextText3 = null;
                strNextText = null;
                while (eventType != 1) {
                    if (eventType == 2) {
                        if ("Code".equals(xmlPullParserNewPullParser.getName())) {
                            strNextText2 = xmlPullParserNewPullParser.nextText();
                        } else if ("Message".equals(xmlPullParserNewPullParser.getName())) {
                            strNextText3 = xmlPullParserNewPullParser.nextText();
                        } else if ("RequestId".equals(xmlPullParserNewPullParser.getName())) {
                            strHeader = xmlPullParserNewPullParser.nextText();
                        } else if ("HostId".equals(xmlPullParserNewPullParser.getName())) {
                            strNextText = xmlPullParserNewPullParser.nextText();
                        }
                    }
                    eventType = xmlPullParserNewPullParser.next();
                    if (eventType == 4) {
                        eventType = xmlPullParserNewPullParser.next();
                    }
                }
                str = strString;
                str2 = strNextText2;
                strNextText2 = strNextText3;
                str3 = strHeader;
            } catch (IOException e) {
                throw new ClientException(e);
            } catch (XmlPullParserException e2) {
                throw new ClientException(e2);
            }
        }
        return new ServiceException(statusCode, strNextText2, str2, str3, strNextText, str);
    }

    public static final class TriggerCallbackResponseParser extends AbstractResponseParser<TriggerCallbackResult> {
        @Override // com.alibaba.sdk.android.oss.internal.AbstractResponseParser
        public TriggerCallbackResult parseData(ResponseMessage responseMessage, TriggerCallbackResult triggerCallbackResult) throws IOException {
            if (responseMessage.getContentLength() > 0) {
                triggerCallbackResult.setServerCallbackReturnBody(responseMessage.getResponse().body().string());
            }
            return triggerCallbackResult;
        }
    }
}
