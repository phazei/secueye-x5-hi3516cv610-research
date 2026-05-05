package com.alibaba.sdk.android.oss;

import android.content.Context;
import anet.channel.util.HttpConstant;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLogToFileUtils;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.ExtensionRequestOperation;
import com.alibaba.sdk.android.oss.internal.InternalRequestOperation;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.internal.ObjectURLPresigner;
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
import com.alibaba.sdk.android.oss.model.GeneratePresignedUrlRequest;
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
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;
import com.alibaba.sdk.android.oss.model.TriggerCallbackRequest;
import com.alibaba.sdk.android.oss.model.TriggerCallbackResult;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/* JADX INFO: loaded from: classes.dex */
class OSSImpl implements OSS {
    private ClientConfiguration conf;
    private OSSCredentialProvider credentialProvider;
    private URI endpointURI;
    private ExtensionRequestOperation extensionRequestOperation;
    private InternalRequestOperation internalRequestOperation;

    public OSSImpl(Context context, String str, OSSCredentialProvider oSSCredentialProvider, ClientConfiguration clientConfiguration) {
        OSSLogToFileUtils.init(context.getApplicationContext(), clientConfiguration);
        try {
            String strTrim = str.trim();
            if (!strTrim.startsWith(HttpConstant.HTTP)) {
                strTrim = "http://" + strTrim;
            }
            this.endpointURI = new URI(strTrim);
            if (oSSCredentialProvider == null) {
                throw new IllegalArgumentException("CredentialProvider can't be null.");
            }
            this.credentialProvider = oSSCredentialProvider;
            this.conf = clientConfiguration == null ? ClientConfiguration.getDefaultConf() : clientConfiguration;
            this.internalRequestOperation = new InternalRequestOperation(context.getApplicationContext(), this.endpointURI, oSSCredentialProvider, this.conf);
            this.extensionRequestOperation = new ExtensionRequestOperation(this.internalRequestOperation);
        } catch (URISyntaxException unused) {
            throw new IllegalArgumentException("Endpoint must be a string like 'http://oss-cn-****.aliyuncs.com',or your cname like 'http://image.cnamedomain.com'!");
        }
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<CreateBucketResult> asyncCreateBucket(CreateBucketRequest createBucketRequest, OSSCompletedCallback<CreateBucketRequest, CreateBucketResult> oSSCompletedCallback) {
        return this.internalRequestOperation.createBucket(createBucketRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public CreateBucketResult createBucket(CreateBucketRequest createBucketRequest) throws ServiceException, ClientException {
        return (CreateBucketResult) this.internalRequestOperation.createBucket(createBucketRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<DeleteBucketResult> asyncDeleteBucket(DeleteBucketRequest deleteBucketRequest, OSSCompletedCallback<DeleteBucketRequest, DeleteBucketResult> oSSCompletedCallback) {
        return this.internalRequestOperation.deleteBucket(deleteBucketRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public DeleteBucketResult deleteBucket(DeleteBucketRequest deleteBucketRequest) throws ServiceException, ClientException {
        return (DeleteBucketResult) this.internalRequestOperation.deleteBucket(deleteBucketRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<GetBucketACLResult> asyncGetBucketACL(GetBucketACLRequest getBucketACLRequest, OSSCompletedCallback<GetBucketACLRequest, GetBucketACLResult> oSSCompletedCallback) {
        return this.internalRequestOperation.getBucketACL(getBucketACLRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public GetBucketACLResult getBucketACL(GetBucketACLRequest getBucketACLRequest) throws ServiceException, ClientException {
        return (GetBucketACLResult) this.internalRequestOperation.getBucketACL(getBucketACLRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<PutObjectResult> asyncPutObject(PutObjectRequest putObjectRequest, OSSCompletedCallback<PutObjectRequest, PutObjectResult> oSSCompletedCallback) {
        return this.internalRequestOperation.putObject(putObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public PutObjectResult putObject(PutObjectRequest putObjectRequest) throws ServiceException, ClientException {
        return this.internalRequestOperation.syncPutObject(putObjectRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<GetObjectResult> asyncGetObject(GetObjectRequest getObjectRequest, OSSCompletedCallback<GetObjectRequest, GetObjectResult> oSSCompletedCallback) {
        return this.internalRequestOperation.getObject(getObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public GetObjectResult getObject(GetObjectRequest getObjectRequest) throws ServiceException, ClientException {
        return (GetObjectResult) this.internalRequestOperation.getObject(getObjectRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<DeleteObjectResult> asyncDeleteObject(DeleteObjectRequest deleteObjectRequest, OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult> oSSCompletedCallback) {
        return this.internalRequestOperation.deleteObject(deleteObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public DeleteObjectResult deleteObject(DeleteObjectRequest deleteObjectRequest) throws ServiceException, ClientException {
        return (DeleteObjectResult) this.internalRequestOperation.deleteObject(deleteObjectRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<AppendObjectResult> asyncAppendObject(AppendObjectRequest appendObjectRequest, OSSCompletedCallback<AppendObjectRequest, AppendObjectResult> oSSCompletedCallback) {
        return this.internalRequestOperation.appendObject(appendObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public AppendObjectResult appendObject(AppendObjectRequest appendObjectRequest) throws ServiceException, ClientException {
        return this.internalRequestOperation.syncAppendObject(appendObjectRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<HeadObjectResult> asyncHeadObject(HeadObjectRequest headObjectRequest, OSSCompletedCallback<HeadObjectRequest, HeadObjectResult> oSSCompletedCallback) {
        return this.internalRequestOperation.headObject(headObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public HeadObjectResult headObject(HeadObjectRequest headObjectRequest) throws ServiceException, ClientException {
        return (HeadObjectResult) this.internalRequestOperation.headObject(headObjectRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<CopyObjectResult> asyncCopyObject(CopyObjectRequest copyObjectRequest, OSSCompletedCallback<CopyObjectRequest, CopyObjectResult> oSSCompletedCallback) {
        return this.internalRequestOperation.copyObject(copyObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public CopyObjectResult copyObject(CopyObjectRequest copyObjectRequest) throws ServiceException, ClientException {
        return (CopyObjectResult) this.internalRequestOperation.copyObject(copyObjectRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<ListObjectsResult> asyncListObjects(ListObjectsRequest listObjectsRequest, OSSCompletedCallback<ListObjectsRequest, ListObjectsResult> oSSCompletedCallback) {
        return this.internalRequestOperation.listObjects(listObjectsRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public ListObjectsResult listObjects(ListObjectsRequest listObjectsRequest) throws ServiceException, ClientException {
        return (ListObjectsResult) this.internalRequestOperation.listObjects(listObjectsRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<InitiateMultipartUploadResult> asyncInitMultipartUpload(InitiateMultipartUploadRequest initiateMultipartUploadRequest, OSSCompletedCallback<InitiateMultipartUploadRequest, InitiateMultipartUploadResult> oSSCompletedCallback) {
        return this.internalRequestOperation.initMultipartUpload(initiateMultipartUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public InitiateMultipartUploadResult initMultipartUpload(InitiateMultipartUploadRequest initiateMultipartUploadRequest) throws ServiceException, ClientException {
        return (InitiateMultipartUploadResult) this.internalRequestOperation.initMultipartUpload(initiateMultipartUploadRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<UploadPartResult> asyncUploadPart(UploadPartRequest uploadPartRequest, OSSCompletedCallback<UploadPartRequest, UploadPartResult> oSSCompletedCallback) {
        return this.internalRequestOperation.uploadPart(uploadPartRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public UploadPartResult uploadPart(UploadPartRequest uploadPartRequest) throws ServiceException, ClientException {
        return this.internalRequestOperation.syncUploadPart(uploadPartRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<CompleteMultipartUploadResult> asyncCompleteMultipartUpload(CompleteMultipartUploadRequest completeMultipartUploadRequest, OSSCompletedCallback<CompleteMultipartUploadRequest, CompleteMultipartUploadResult> oSSCompletedCallback) {
        return this.internalRequestOperation.completeMultipartUpload(completeMultipartUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest completeMultipartUploadRequest) throws ServiceException, ClientException {
        return this.internalRequestOperation.syncCompleteMultipartUpload(completeMultipartUploadRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<AbortMultipartUploadResult> asyncAbortMultipartUpload(AbortMultipartUploadRequest abortMultipartUploadRequest, OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult> oSSCompletedCallback) {
        return this.internalRequestOperation.abortMultipartUpload(abortMultipartUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public AbortMultipartUploadResult abortMultipartUpload(AbortMultipartUploadRequest abortMultipartUploadRequest) throws ServiceException, ClientException {
        return (AbortMultipartUploadResult) this.internalRequestOperation.abortMultipartUpload(abortMultipartUploadRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<ListPartsResult> asyncListParts(ListPartsRequest listPartsRequest, OSSCompletedCallback<ListPartsRequest, ListPartsResult> oSSCompletedCallback) {
        return this.internalRequestOperation.listParts(listPartsRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public ListPartsResult listParts(ListPartsRequest listPartsRequest) throws ServiceException, ClientException {
        return (ListPartsResult) this.internalRequestOperation.listParts(listPartsRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public void updateCredentialProvider(OSSCredentialProvider oSSCredentialProvider) {
        this.credentialProvider = oSSCredentialProvider;
        this.internalRequestOperation.setCredentialProvider(oSSCredentialProvider);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<CompleteMultipartUploadResult> asyncMultipartUpload(MultipartUploadRequest multipartUploadRequest, OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult> oSSCompletedCallback) {
        return this.extensionRequestOperation.multipartUpload(multipartUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public CompleteMultipartUploadResult multipartUpload(MultipartUploadRequest multipartUploadRequest) throws ServiceException, ClientException {
        return (CompleteMultipartUploadResult) this.extensionRequestOperation.multipartUpload(multipartUploadRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<ResumableUploadResult> asyncResumableUpload(ResumableUploadRequest resumableUploadRequest, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> oSSCompletedCallback) {
        return this.extensionRequestOperation.resumableUpload(resumableUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public ResumableUploadResult resumableUpload(ResumableUploadRequest resumableUploadRequest) throws ServiceException, ClientException {
        return (ResumableUploadResult) this.extensionRequestOperation.resumableUpload(resumableUploadRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<ResumableUploadResult> asyncSequenceUpload(ResumableUploadRequest resumableUploadRequest, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> oSSCompletedCallback) {
        return this.extensionRequestOperation.sequenceUpload(resumableUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public ResumableUploadResult sequenceUpload(ResumableUploadRequest resumableUploadRequest) throws ServiceException, ClientException {
        return (ResumableUploadResult) this.extensionRequestOperation.sequenceUpload(resumableUploadRequest, null).getResult();
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public String presignConstrainedObjectURL(GeneratePresignedUrlRequest generatePresignedUrlRequest) throws ClientException {
        return new ObjectURLPresigner(this.endpointURI, this.credentialProvider, this.conf).presignConstrainedURL(generatePresignedUrlRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public String presignConstrainedObjectURL(String str, String str2, long j) throws ClientException {
        return new ObjectURLPresigner(this.endpointURI, this.credentialProvider, this.conf).presignConstrainedURL(str, str2, j);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public String presignPublicObjectURL(String str, String str2) {
        return new ObjectURLPresigner(this.endpointURI, this.credentialProvider, this.conf).presignPublicURL(str, str2);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public boolean doesObjectExist(String str, String str2) throws ServiceException, ClientException {
        return this.extensionRequestOperation.doesObjectExist(str, str2);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public void abortResumableUpload(ResumableUploadRequest resumableUploadRequest) throws IOException {
        this.extensionRequestOperation.abortResumableUpload(resumableUploadRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<TriggerCallbackResult> asyncTriggerCallback(TriggerCallbackRequest triggerCallbackRequest, OSSCompletedCallback<TriggerCallbackRequest, TriggerCallbackResult> oSSCompletedCallback) {
        return this.internalRequestOperation.triggerCallback(triggerCallbackRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public TriggerCallbackResult triggerCallback(TriggerCallbackRequest triggerCallbackRequest) throws ServiceException, ClientException {
        return this.internalRequestOperation.asyncTriggerCallback(triggerCallbackRequest);
    }
}
