package com.alibaba.sdk.android.oss.internal;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.HttpMethod;
import com.alibaba.sdk.android.oss.common.OSSHeaders;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.common.utils.CRC64;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.common.utils.VersionInfoUtils;
import com.alibaba.sdk.android.oss.exception.InconsistentException;
import com.alibaba.sdk.android.oss.internal.ResponseParsers;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.AppendObjectRequest;
import com.alibaba.sdk.android.oss.model.AppendObjectResult;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.CopyObjectRequest;
import com.alibaba.sdk.android.oss.model.CopyObjectResult;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteBucketRequest;
import com.alibaba.sdk.android.oss.model.DeleteBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetBucketACLRequest;
import com.alibaba.sdk.android.oss.model.GetBucketACLResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.HeadObjectRequest;
import com.alibaba.sdk.android.oss.model.HeadObjectResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.ListObjectsRequest;
import com.alibaba.sdk.android.oss.model.ListObjectsResult;
import com.alibaba.sdk.android.oss.model.ListPartsRequest;
import com.alibaba.sdk.android.oss.model.ListPartsResult;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.TriggerCallbackRequest;
import com.alibaba.sdk.android.oss.model.TriggerCallbackResult;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;
import com.alibaba.sdk.android.oss.network.ExecutionContext;
import com.alibaba.sdk.android.oss.network.OSSRequestTask;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/* JADX INFO: loaded from: classes.dex */
public class InternalRequestOperation {
    private static final int LIST_PART_MAX_RETURNS = 1000;
    private static final int MAX_PART_NUMBER = 10000;
    private static ExecutorService executorService = Executors.newFixedThreadPool(5, new ThreadFactory() { // from class: com.alibaba.sdk.android.oss.internal.InternalRequestOperation.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "oss-android-api-thread");
        }
    });
    private Context applicationContext;
    private ClientConfiguration conf;
    private OSSCredentialProvider credentialProvider;
    private volatile URI endpoint;
    private OkHttpClient innerClient;
    private int maxRetryCount;

    public InternalRequestOperation(Context context, final URI uri, OSSCredentialProvider oSSCredentialProvider, ClientConfiguration clientConfiguration) {
        this.maxRetryCount = 2;
        this.applicationContext = context;
        this.endpoint = uri;
        this.credentialProvider = oSSCredentialProvider;
        this.conf = clientConfiguration;
        OkHttpClient.Builder builderHostnameVerifier = new OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).retryOnConnectionFailure(false).cache(null).hostnameVerifier(new HostnameVerifier() { // from class: com.alibaba.sdk.android.oss.internal.InternalRequestOperation.2
            @Override // javax.net.ssl.HostnameVerifier
            public boolean verify(String str, SSLSession sSLSession) {
                return HttpsURLConnection.getDefaultHostnameVerifier().verify(uri.getHost(), sSLSession);
            }
        });
        if (clientConfiguration != null) {
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(clientConfiguration.getMaxConcurrentRequest());
            builderHostnameVerifier.connectTimeout(clientConfiguration.getConnectionTimeout(), TimeUnit.MILLISECONDS).readTimeout(clientConfiguration.getSocketTimeout(), TimeUnit.MILLISECONDS).writeTimeout(clientConfiguration.getSocketTimeout(), TimeUnit.MILLISECONDS).dispatcher(dispatcher);
            if (clientConfiguration.getProxyHost() != null && clientConfiguration.getProxyPort() != 0) {
                builderHostnameVerifier.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(clientConfiguration.getProxyHost(), clientConfiguration.getProxyPort())));
            }
            this.maxRetryCount = clientConfiguration.getMaxErrorRetry();
        }
        this.innerClient = builderHostnameVerifier.build();
    }

    public PutObjectResult syncPutObject(PutObjectRequest putObjectRequest) throws ServiceException, ClientException {
        PutObjectResult putObjectResult = (PutObjectResult) putObject(putObjectRequest, null).getResult();
        checkCRC64(putObjectRequest, putObjectResult);
        return putObjectResult;
    }

    public OSSAsyncTask<PutObjectResult> putObject(PutObjectRequest putObjectRequest, final OSSCompletedCallback<PutObjectRequest, PutObjectResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(putObjectRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.PUT);
        requestMessage.setBucketName(putObjectRequest.getBucketName());
        requestMessage.setObjectKey(putObjectRequest.getObjectKey());
        if (putObjectRequest.getUploadData() != null) {
            requestMessage.setUploadData(putObjectRequest.getUploadData());
        }
        if (putObjectRequest.getUploadFilePath() != null) {
            requestMessage.setUploadFilePath(putObjectRequest.getUploadFilePath());
        }
        if (putObjectRequest.getCallbackParam() != null) {
            requestMessage.getHeaders().put("x-oss-callback", OSSUtils.populateMapToBase64JsonString(putObjectRequest.getCallbackParam()));
        }
        if (putObjectRequest.getCallbackVars() != null) {
            requestMessage.getHeaders().put("x-oss-callback-var", OSSUtils.populateMapToBase64JsonString(putObjectRequest.getCallbackVars()));
        }
        OSSUtils.populateRequestMetadata(requestMessage.getHeaders(), putObjectRequest.getMetadata());
        canonicalizeRequestMessage(requestMessage, putObjectRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), putObjectRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() { // from class: com.alibaba.sdk.android.oss.internal.InternalRequestOperation.3
                @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
                public void onSuccess(PutObjectRequest putObjectRequest2, PutObjectResult putObjectResult) {
                    InternalRequestOperation.this.checkCRC64(putObjectRequest2, putObjectResult, oSSCompletedCallback);
                }

                @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
                public void onFailure(PutObjectRequest putObjectRequest2, ClientException clientException, ServiceException serviceException) {
                    oSSCompletedCallback.onFailure(putObjectRequest2, clientException, serviceException);
                }
            });
        }
        if (putObjectRequest.getRetryCallback() != null) {
            executionContext.setRetryCallback(putObjectRequest.getRetryCallback());
        }
        executionContext.setProgressCallback(putObjectRequest.getProgressCallback());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.PutObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<CreateBucketResult> createBucket(CreateBucketRequest createBucketRequest, OSSCompletedCallback<CreateBucketRequest, CreateBucketResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(createBucketRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.PUT);
        requestMessage.setBucketName(createBucketRequest.getBucketName());
        if (createBucketRequest.getBucketACL() != null) {
            requestMessage.getHeaders().put(OSSHeaders.OSS_CANNED_ACL, createBucketRequest.getBucketACL().toString());
        }
        try {
            requestMessage.createBucketRequestBodyMarshall(createBucketRequest.getLocationConstraint());
            canonicalizeRequestMessage(requestMessage, createBucketRequest);
            ExecutionContext executionContext = new ExecutionContext(getInnerClient(), createBucketRequest, this.applicationContext);
            if (oSSCompletedCallback != null) {
                executionContext.setCompletedCallback(oSSCompletedCallback);
            }
            return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.CreateBucketResponseParser(), executionContext, this.maxRetryCount)), executionContext);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OSSAsyncTask<DeleteBucketResult> deleteBucket(DeleteBucketRequest deleteBucketRequest, OSSCompletedCallback<DeleteBucketRequest, DeleteBucketResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(deleteBucketRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.DELETE);
        requestMessage.setBucketName(deleteBucketRequest.getBucketName());
        canonicalizeRequestMessage(requestMessage, deleteBucketRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), deleteBucketRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.DeleteBucketResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<GetBucketACLResult> getBucketACL(GetBucketACLRequest getBucketACLRequest, OSSCompletedCallback<GetBucketACLRequest, GetBucketACLResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(RequestParameters.SUBRESOURCE_ACL, "");
        requestMessage.setIsAuthorizationRequired(getBucketACLRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(getBucketACLRequest.getBucketName());
        requestMessage.setParameters(linkedHashMap);
        canonicalizeRequestMessage(requestMessage, getBucketACLRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), getBucketACLRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.GetBucketACLResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public AppendObjectResult syncAppendObject(AppendObjectRequest appendObjectRequest) throws ServiceException, ClientException {
        AppendObjectResult appendObjectResult = (AppendObjectResult) appendObject(appendObjectRequest, null).getResult();
        boolean z = appendObjectRequest.getCRC64() == OSSRequest.CRC64Config.YES;
        if (appendObjectRequest.getInitCRC64() != null && z) {
            appendObjectResult.setClientCRC(Long.valueOf(CRC64.combine(appendObjectRequest.getInitCRC64().longValue(), appendObjectResult.getClientCRC().longValue(), appendObjectResult.getNextPosition() - appendObjectRequest.getPosition())));
        }
        checkCRC64(appendObjectRequest, appendObjectResult);
        return appendObjectResult;
    }

    public OSSAsyncTask<AppendObjectResult> appendObject(AppendObjectRequest appendObjectRequest, final OSSCompletedCallback<AppendObjectRequest, AppendObjectResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(appendObjectRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(appendObjectRequest.getBucketName());
        requestMessage.setObjectKey(appendObjectRequest.getObjectKey());
        if (appendObjectRequest.getUploadData() != null) {
            requestMessage.setUploadData(appendObjectRequest.getUploadData());
        }
        if (appendObjectRequest.getUploadFilePath() != null) {
            requestMessage.setUploadFilePath(appendObjectRequest.getUploadFilePath());
        }
        requestMessage.getParameters().put("append", "");
        requestMessage.getParameters().put(RequestParameters.POSITION, String.valueOf(appendObjectRequest.getPosition()));
        OSSUtils.populateRequestMetadata(requestMessage.getHeaders(), appendObjectRequest.getMetadata());
        canonicalizeRequestMessage(requestMessage, appendObjectRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), appendObjectRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(new OSSCompletedCallback<AppendObjectRequest, AppendObjectResult>() { // from class: com.alibaba.sdk.android.oss.internal.InternalRequestOperation.4
                @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
                public void onSuccess(AppendObjectRequest appendObjectRequest2, AppendObjectResult appendObjectResult) {
                    boolean z = appendObjectRequest2.getCRC64() == OSSRequest.CRC64Config.YES;
                    if (appendObjectRequest2.getInitCRC64() != null && z) {
                        appendObjectResult.setClientCRC(Long.valueOf(CRC64.combine(appendObjectRequest2.getInitCRC64().longValue(), appendObjectResult.getClientCRC().longValue(), appendObjectResult.getNextPosition() - appendObjectRequest2.getPosition())));
                    }
                    InternalRequestOperation.this.checkCRC64(appendObjectRequest2, appendObjectResult, oSSCompletedCallback);
                }

                @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
                public void onFailure(AppendObjectRequest appendObjectRequest2, ClientException clientException, ServiceException serviceException) {
                    oSSCompletedCallback.onFailure(appendObjectRequest2, clientException, serviceException);
                }
            });
        }
        executionContext.setProgressCallback(appendObjectRequest.getProgressCallback());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.AppendObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<HeadObjectResult> headObject(HeadObjectRequest headObjectRequest, OSSCompletedCallback<HeadObjectRequest, HeadObjectResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(headObjectRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.HEAD);
        requestMessage.setBucketName(headObjectRequest.getBucketName());
        requestMessage.setObjectKey(headObjectRequest.getObjectKey());
        canonicalizeRequestMessage(requestMessage, headObjectRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), headObjectRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.HeadObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<GetObjectResult> getObject(GetObjectRequest getObjectRequest, OSSCompletedCallback<GetObjectRequest, GetObjectResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(getObjectRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(getObjectRequest.getBucketName());
        requestMessage.setObjectKey(getObjectRequest.getObjectKey());
        if (getObjectRequest.getRange() != null) {
            requestMessage.getHeaders().put("Range", getObjectRequest.getRange().toString());
        }
        if (getObjectRequest.getxOssProcess() != null) {
            requestMessage.getParameters().put(RequestParameters.X_OSS_PROCESS, getObjectRequest.getxOssProcess());
        }
        canonicalizeRequestMessage(requestMessage, getObjectRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), getObjectRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        executionContext.setProgressCallback(getObjectRequest.getProgressListener());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.GetObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<CopyObjectResult> copyObject(CopyObjectRequest copyObjectRequest, OSSCompletedCallback<CopyObjectRequest, CopyObjectResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(copyObjectRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.PUT);
        requestMessage.setBucketName(copyObjectRequest.getDestinationBucketName());
        requestMessage.setObjectKey(copyObjectRequest.getDestinationKey());
        OSSUtils.populateCopyObjectHeaders(copyObjectRequest, requestMessage.getHeaders());
        canonicalizeRequestMessage(requestMessage, copyObjectRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), copyObjectRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.CopyObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<DeleteObjectResult> deleteObject(DeleteObjectRequest deleteObjectRequest, OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(deleteObjectRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.DELETE);
        requestMessage.setBucketName(deleteObjectRequest.getBucketName());
        requestMessage.setObjectKey(deleteObjectRequest.getObjectKey());
        canonicalizeRequestMessage(requestMessage, deleteObjectRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), deleteObjectRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.DeleteObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<ListObjectsResult> listObjects(ListObjectsRequest listObjectsRequest, OSSCompletedCallback<ListObjectsRequest, ListObjectsResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(listObjectsRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(listObjectsRequest.getBucketName());
        canonicalizeRequestMessage(requestMessage, listObjectsRequest);
        OSSUtils.populateListObjectsRequestParameters(listObjectsRequest, requestMessage.getParameters());
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), listObjectsRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.ListObjectsResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<InitiateMultipartUploadResult> initMultipartUpload(InitiateMultipartUploadRequest initiateMultipartUploadRequest, OSSCompletedCallback<InitiateMultipartUploadRequest, InitiateMultipartUploadResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(initiateMultipartUploadRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(initiateMultipartUploadRequest.getBucketName());
        requestMessage.setObjectKey(initiateMultipartUploadRequest.getObjectKey());
        requestMessage.getParameters().put(RequestParameters.SUBRESOURCE_UPLOADS, "");
        if (initiateMultipartUploadRequest.isSequential) {
            requestMessage.getParameters().put(RequestParameters.SUBRESOURCE_SEQUENTIAL, "");
        }
        OSSUtils.populateRequestMetadata(requestMessage.getHeaders(), initiateMultipartUploadRequest.getMetadata());
        canonicalizeRequestMessage(requestMessage, initiateMultipartUploadRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), initiateMultipartUploadRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.InitMultipartResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public UploadPartResult syncUploadPart(UploadPartRequest uploadPartRequest) throws ServiceException, ClientException {
        UploadPartResult uploadPartResult = (UploadPartResult) uploadPart(uploadPartRequest, null).getResult();
        checkCRC64(uploadPartRequest, uploadPartResult);
        return uploadPartResult;
    }

    public OSSAsyncTask<UploadPartResult> uploadPart(UploadPartRequest uploadPartRequest, final OSSCompletedCallback<UploadPartRequest, UploadPartResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(uploadPartRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.PUT);
        requestMessage.setBucketName(uploadPartRequest.getBucketName());
        requestMessage.setObjectKey(uploadPartRequest.getObjectKey());
        requestMessage.getParameters().put(RequestParameters.UPLOAD_ID, uploadPartRequest.getUploadId());
        requestMessage.getParameters().put(RequestParameters.PART_NUMBER, String.valueOf(uploadPartRequest.getPartNumber()));
        requestMessage.setUploadData(uploadPartRequest.getPartContent());
        if (uploadPartRequest.getMd5Digest() != null) {
            requestMessage.getHeaders().put("Content-MD5", uploadPartRequest.getMd5Digest());
        }
        canonicalizeRequestMessage(requestMessage, uploadPartRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), uploadPartRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(new OSSCompletedCallback<UploadPartRequest, UploadPartResult>() { // from class: com.alibaba.sdk.android.oss.internal.InternalRequestOperation.5
                @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
                public void onSuccess(UploadPartRequest uploadPartRequest2, UploadPartResult uploadPartResult) {
                    InternalRequestOperation.this.checkCRC64(uploadPartRequest2, uploadPartResult, oSSCompletedCallback);
                }

                @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
                public void onFailure(UploadPartRequest uploadPartRequest2, ClientException clientException, ServiceException serviceException) {
                    oSSCompletedCallback.onFailure(uploadPartRequest2, clientException, serviceException);
                }
            });
        }
        executionContext.setProgressCallback(uploadPartRequest.getProgressCallback());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.UploadPartResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public CompleteMultipartUploadResult syncCompleteMultipartUpload(CompleteMultipartUploadRequest completeMultipartUploadRequest) throws ServiceException, ClientException {
        CompleteMultipartUploadResult completeMultipartUploadResult = (CompleteMultipartUploadResult) completeMultipartUpload(completeMultipartUploadRequest, null).getResult();
        if (completeMultipartUploadResult.getServerCRC() != null) {
            completeMultipartUploadResult.setClientCRC(Long.valueOf(calcObjectCRCFromParts(completeMultipartUploadRequest.getPartETags())));
        }
        checkCRC64(completeMultipartUploadRequest, completeMultipartUploadResult);
        return completeMultipartUploadResult;
    }

    public OSSAsyncTask<CompleteMultipartUploadResult> completeMultipartUpload(CompleteMultipartUploadRequest completeMultipartUploadRequest, final OSSCompletedCallback<CompleteMultipartUploadRequest, CompleteMultipartUploadResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(completeMultipartUploadRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(completeMultipartUploadRequest.getBucketName());
        requestMessage.setObjectKey(completeMultipartUploadRequest.getObjectKey());
        requestMessage.setStringBody(OSSUtils.buildXMLFromPartEtagList(completeMultipartUploadRequest.getPartETags()));
        requestMessage.getParameters().put(RequestParameters.UPLOAD_ID, completeMultipartUploadRequest.getUploadId());
        if (completeMultipartUploadRequest.getCallbackParam() != null) {
            requestMessage.getHeaders().put("x-oss-callback", OSSUtils.populateMapToBase64JsonString(completeMultipartUploadRequest.getCallbackParam()));
        }
        if (completeMultipartUploadRequest.getCallbackVars() != null) {
            requestMessage.getHeaders().put("x-oss-callback-var", OSSUtils.populateMapToBase64JsonString(completeMultipartUploadRequest.getCallbackVars()));
        }
        OSSUtils.populateRequestMetadata(requestMessage.getHeaders(), completeMultipartUploadRequest.getMetadata());
        canonicalizeRequestMessage(requestMessage, completeMultipartUploadRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), completeMultipartUploadRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(new OSSCompletedCallback<CompleteMultipartUploadRequest, CompleteMultipartUploadResult>() { // from class: com.alibaba.sdk.android.oss.internal.InternalRequestOperation.6
                @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
                public void onSuccess(CompleteMultipartUploadRequest completeMultipartUploadRequest2, CompleteMultipartUploadResult completeMultipartUploadResult) {
                    if (completeMultipartUploadResult.getServerCRC() != null) {
                        completeMultipartUploadResult.setClientCRC(Long.valueOf(InternalRequestOperation.this.calcObjectCRCFromParts(completeMultipartUploadRequest2.getPartETags())));
                    }
                    InternalRequestOperation.this.checkCRC64(completeMultipartUploadRequest2, completeMultipartUploadResult, oSSCompletedCallback);
                }

                @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
                public void onFailure(CompleteMultipartUploadRequest completeMultipartUploadRequest2, ClientException clientException, ServiceException serviceException) {
                    oSSCompletedCallback.onFailure(completeMultipartUploadRequest2, clientException, serviceException);
                }
            });
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.CompleteMultipartUploadResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<AbortMultipartUploadResult> abortMultipartUpload(AbortMultipartUploadRequest abortMultipartUploadRequest, OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(abortMultipartUploadRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.DELETE);
        requestMessage.setBucketName(abortMultipartUploadRequest.getBucketName());
        requestMessage.setObjectKey(abortMultipartUploadRequest.getObjectKey());
        requestMessage.getParameters().put(RequestParameters.UPLOAD_ID, abortMultipartUploadRequest.getUploadId());
        canonicalizeRequestMessage(requestMessage, abortMultipartUploadRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), abortMultipartUploadRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.AbortMultipartUploadResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<ListPartsResult> listParts(ListPartsRequest listPartsRequest, OSSCompletedCallback<ListPartsRequest, ListPartsResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(listPartsRequest.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(listPartsRequest.getBucketName());
        requestMessage.setObjectKey(listPartsRequest.getObjectKey());
        requestMessage.getParameters().put(RequestParameters.UPLOAD_ID, listPartsRequest.getUploadId());
        Integer maxParts = listPartsRequest.getMaxParts();
        if (maxParts != null) {
            if (!OSSUtils.checkParamRange(maxParts.intValue(), 0L, true, 1000L, true)) {
                throw new IllegalArgumentException("MaxPartsOutOfRange: 1000");
            }
            requestMessage.getParameters().put(RequestParameters.MAX_PARTS, maxParts.toString());
        }
        Integer partNumberMarker = listPartsRequest.getPartNumberMarker();
        if (partNumberMarker != null) {
            if (!OSSUtils.checkParamRange(partNumberMarker.intValue(), 0L, false, 10000L, true)) {
                throw new IllegalArgumentException("PartNumberMarkerOutOfRange: 10000");
            }
            requestMessage.getParameters().put(RequestParameters.PART_NUMBER_MARKER, partNumberMarker.toString());
        }
        canonicalizeRequestMessage(requestMessage, listPartsRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), listPartsRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.ListPartsResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    private boolean checkIfHttpDnsAvailable(boolean z) {
        String host;
        if (!z || this.applicationContext == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            host = System.getProperty("http.proxyHost");
        } else {
            host = android.net.Proxy.getHost(this.applicationContext);
        }
        String proxyHost = this.conf.getProxyHost();
        if (!TextUtils.isEmpty(proxyHost)) {
            host = proxyHost;
        }
        return TextUtils.isEmpty(host);
    }

    public OkHttpClient getInnerClient() {
        return this.innerClient;
    }

    private void canonicalizeRequestMessage(RequestMessage requestMessage, OSSRequest oSSRequest) {
        Map headers = requestMessage.getHeaders();
        if (headers.get("Date") == null) {
            headers.put("Date", DateUtil.currentFixedSkewedTimeInRFC822Format());
        }
        if ((requestMessage.getMethod() == HttpMethod.POST || requestMessage.getMethod() == HttpMethod.PUT) && OSSUtils.isEmptyString((String) headers.get("Content-Type"))) {
            headers.put("Content-Type", OSSUtils.determineContentType(null, requestMessage.getUploadFilePath(), requestMessage.getObjectKey()));
        }
        requestMessage.setHttpDnsEnable(checkIfHttpDnsAvailable(this.conf.isHttpDnsEnable()));
        requestMessage.setCredentialProvider(this.credentialProvider);
        requestMessage.getHeaders().put("User-Agent", VersionInfoUtils.getUserAgent(this.conf.getCustomUserMark()));
        boolean zIsCheckCRC64 = false;
        if (requestMessage.getHeaders().containsKey("Range") || requestMessage.getParameters().containsKey(RequestParameters.X_OSS_PROCESS)) {
            requestMessage.setCheckCRC64(false);
        }
        requestMessage.setIsInCustomCnameExcludeList(OSSUtils.isInCustomCnameExcludeList(this.endpoint.getHost(), this.conf.getCustomCnameExcludeList()));
        if (oSSRequest.getCRC64() == OSSRequest.CRC64Config.NULL) {
            zIsCheckCRC64 = this.conf.isCheckCRC64();
        } else if (oSSRequest.getCRC64() == OSSRequest.CRC64Config.YES) {
            zIsCheckCRC64 = true;
        }
        requestMessage.setCheckCRC64(zIsCheckCRC64);
        oSSRequest.setCRC64(zIsCheckCRC64 ? OSSRequest.CRC64Config.YES : OSSRequest.CRC64Config.NO);
    }

    public void setCredentialProvider(OSSCredentialProvider oSSCredentialProvider) {
        this.credentialProvider = oSSCredentialProvider;
    }

    private <Request extends OSSRequest, Result extends OSSResult> void checkCRC64(Request request, Result result) throws ClientException {
        if (request.getCRC64() == OSSRequest.CRC64Config.YES) {
            try {
                OSSUtils.checkChecksum(result.getClientCRC(), result.getServerCRC(), result.getRequestId());
            } catch (InconsistentException e) {
                throw new ClientException(e.getMessage(), e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <Request extends OSSRequest, Result extends OSSResult> void checkCRC64(Request request, Result result, OSSCompletedCallback<Request, Result> oSSCompletedCallback) {
        try {
            checkCRC64(request, result);
            if (oSSCompletedCallback != null) {
                oSSCompletedCallback.onSuccess(request, result);
            }
        } catch (ClientException e) {
            if (oSSCompletedCallback != null) {
                oSSCompletedCallback.onFailure(request, e, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long calcObjectCRCFromParts(List<PartETag> list) {
        long jCombine = 0;
        for (PartETag partETag : list) {
            if (partETag.getCRC64() == 0 || partETag.getPartSize() <= 0) {
                return 0L;
            }
            jCombine = CRC64.combine(jCombine, partETag.getCRC64(), partETag.getPartSize());
        }
        return jCombine;
    }

    public Context getApplicationContext() {
        return this.applicationContext;
    }

    public ClientConfiguration getConf() {
        return this.conf;
    }

    public OSSAsyncTask<TriggerCallbackResult> triggerCallback(TriggerCallbackRequest triggerCallbackRequest, OSSCompletedCallback<TriggerCallbackRequest, TriggerCallbackResult> oSSCompletedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(RequestParameters.X_OSS_PROCESS, "");
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(triggerCallbackRequest.getBucketName());
        requestMessage.setObjectKey(triggerCallbackRequest.getObjectKey());
        requestMessage.setParameters(linkedHashMap);
        String strBuildTriggerCallbackBody = OSSUtils.buildTriggerCallbackBody(triggerCallbackRequest.getCallbackParam(), triggerCallbackRequest.getCallbackVars());
        requestMessage.setStringBody(strBuildTriggerCallbackBody);
        requestMessage.getHeaders().put("Content-MD5", BinaryUtil.calculateBase64Md5(strBuildTriggerCallbackBody.getBytes()));
        canonicalizeRequestMessage(requestMessage, triggerCallbackRequest);
        ExecutionContext executionContext = new ExecutionContext(getInnerClient(), triggerCallbackRequest, this.applicationContext);
        if (oSSCompletedCallback != null) {
            executionContext.setCompletedCallback(oSSCompletedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask(requestMessage, new ResponseParsers.TriggerCallbackResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public TriggerCallbackResult asyncTriggerCallback(TriggerCallbackRequest triggerCallbackRequest) throws ServiceException, ClientException {
        return (TriggerCallbackResult) triggerCallback(triggerCallbackRequest, null).getResult();
    }
}
