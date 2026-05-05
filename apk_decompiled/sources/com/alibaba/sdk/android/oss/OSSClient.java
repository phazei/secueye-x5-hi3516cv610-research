package com.alibaba.sdk.android.oss;

import android.content.Context;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
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

/* JADX INFO: loaded from: classes.dex */
public class OSSClient implements OSS {
    private OSS mOss;

    public OSSClient(Context context, String str, OSSCredentialProvider oSSCredentialProvider) {
        this(context, str, oSSCredentialProvider, null);
    }

    public OSSClient(Context context, String str, OSSCredentialProvider oSSCredentialProvider, ClientConfiguration clientConfiguration) {
        this.mOss = new OSSImpl(context, str, oSSCredentialProvider, clientConfiguration);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<CreateBucketResult> asyncCreateBucket(CreateBucketRequest createBucketRequest, OSSCompletedCallback<CreateBucketRequest, CreateBucketResult> oSSCompletedCallback) {
        return this.mOss.asyncCreateBucket(createBucketRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public CreateBucketResult createBucket(CreateBucketRequest createBucketRequest) throws ServiceException, ClientException {
        return this.mOss.createBucket(createBucketRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<DeleteBucketResult> asyncDeleteBucket(DeleteBucketRequest deleteBucketRequest, OSSCompletedCallback<DeleteBucketRequest, DeleteBucketResult> oSSCompletedCallback) {
        return this.mOss.asyncDeleteBucket(deleteBucketRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public DeleteBucketResult deleteBucket(DeleteBucketRequest deleteBucketRequest) throws ServiceException, ClientException {
        return this.mOss.deleteBucket(deleteBucketRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<GetBucketACLResult> asyncGetBucketACL(GetBucketACLRequest getBucketACLRequest, OSSCompletedCallback<GetBucketACLRequest, GetBucketACLResult> oSSCompletedCallback) {
        return this.mOss.asyncGetBucketACL(getBucketACLRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public GetBucketACLResult getBucketACL(GetBucketACLRequest getBucketACLRequest) throws ServiceException, ClientException {
        return this.mOss.getBucketACL(getBucketACLRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<PutObjectResult> asyncPutObject(PutObjectRequest putObjectRequest, OSSCompletedCallback<PutObjectRequest, PutObjectResult> oSSCompletedCallback) {
        return this.mOss.asyncPutObject(putObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public PutObjectResult putObject(PutObjectRequest putObjectRequest) throws ServiceException, ClientException {
        return this.mOss.putObject(putObjectRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<GetObjectResult> asyncGetObject(GetObjectRequest getObjectRequest, OSSCompletedCallback<GetObjectRequest, GetObjectResult> oSSCompletedCallback) {
        return this.mOss.asyncGetObject(getObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public GetObjectResult getObject(GetObjectRequest getObjectRequest) throws ServiceException, ClientException {
        return this.mOss.getObject(getObjectRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<DeleteObjectResult> asyncDeleteObject(DeleteObjectRequest deleteObjectRequest, OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult> oSSCompletedCallback) {
        return this.mOss.asyncDeleteObject(deleteObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public DeleteObjectResult deleteObject(DeleteObjectRequest deleteObjectRequest) throws ServiceException, ClientException {
        return this.mOss.deleteObject(deleteObjectRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<AppendObjectResult> asyncAppendObject(AppendObjectRequest appendObjectRequest, OSSCompletedCallback<AppendObjectRequest, AppendObjectResult> oSSCompletedCallback) {
        return this.mOss.asyncAppendObject(appendObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public AppendObjectResult appendObject(AppendObjectRequest appendObjectRequest) throws ServiceException, ClientException {
        return this.mOss.appendObject(appendObjectRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<HeadObjectResult> asyncHeadObject(HeadObjectRequest headObjectRequest, OSSCompletedCallback<HeadObjectRequest, HeadObjectResult> oSSCompletedCallback) {
        return this.mOss.asyncHeadObject(headObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public HeadObjectResult headObject(HeadObjectRequest headObjectRequest) throws ServiceException, ClientException {
        return this.mOss.headObject(headObjectRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<CopyObjectResult> asyncCopyObject(CopyObjectRequest copyObjectRequest, OSSCompletedCallback<CopyObjectRequest, CopyObjectResult> oSSCompletedCallback) {
        return this.mOss.asyncCopyObject(copyObjectRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public CopyObjectResult copyObject(CopyObjectRequest copyObjectRequest) throws ServiceException, ClientException {
        return this.mOss.copyObject(copyObjectRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<ListObjectsResult> asyncListObjects(ListObjectsRequest listObjectsRequest, OSSCompletedCallback<ListObjectsRequest, ListObjectsResult> oSSCompletedCallback) {
        return this.mOss.asyncListObjects(listObjectsRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public ListObjectsResult listObjects(ListObjectsRequest listObjectsRequest) throws ServiceException, ClientException {
        return this.mOss.listObjects(listObjectsRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<InitiateMultipartUploadResult> asyncInitMultipartUpload(InitiateMultipartUploadRequest initiateMultipartUploadRequest, OSSCompletedCallback<InitiateMultipartUploadRequest, InitiateMultipartUploadResult> oSSCompletedCallback) {
        return this.mOss.asyncInitMultipartUpload(initiateMultipartUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public InitiateMultipartUploadResult initMultipartUpload(InitiateMultipartUploadRequest initiateMultipartUploadRequest) throws ServiceException, ClientException {
        return this.mOss.initMultipartUpload(initiateMultipartUploadRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<UploadPartResult> asyncUploadPart(UploadPartRequest uploadPartRequest, OSSCompletedCallback<UploadPartRequest, UploadPartResult> oSSCompletedCallback) {
        return this.mOss.asyncUploadPart(uploadPartRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public UploadPartResult uploadPart(UploadPartRequest uploadPartRequest) throws ServiceException, ClientException {
        return this.mOss.uploadPart(uploadPartRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<CompleteMultipartUploadResult> asyncCompleteMultipartUpload(CompleteMultipartUploadRequest completeMultipartUploadRequest, OSSCompletedCallback<CompleteMultipartUploadRequest, CompleteMultipartUploadResult> oSSCompletedCallback) {
        return this.mOss.asyncCompleteMultipartUpload(completeMultipartUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest completeMultipartUploadRequest) throws ServiceException, ClientException {
        return this.mOss.completeMultipartUpload(completeMultipartUploadRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<AbortMultipartUploadResult> asyncAbortMultipartUpload(AbortMultipartUploadRequest abortMultipartUploadRequest, OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult> oSSCompletedCallback) {
        return this.mOss.asyncAbortMultipartUpload(abortMultipartUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public AbortMultipartUploadResult abortMultipartUpload(AbortMultipartUploadRequest abortMultipartUploadRequest) throws ServiceException, ClientException {
        return this.mOss.abortMultipartUpload(abortMultipartUploadRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<ListPartsResult> asyncListParts(ListPartsRequest listPartsRequest, OSSCompletedCallback<ListPartsRequest, ListPartsResult> oSSCompletedCallback) {
        return this.mOss.asyncListParts(listPartsRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public ListPartsResult listParts(ListPartsRequest listPartsRequest) throws ServiceException, ClientException {
        return this.mOss.listParts(listPartsRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public void updateCredentialProvider(OSSCredentialProvider oSSCredentialProvider) {
        this.mOss.updateCredentialProvider(oSSCredentialProvider);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<CompleteMultipartUploadResult> asyncMultipartUpload(MultipartUploadRequest multipartUploadRequest, OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult> oSSCompletedCallback) {
        return this.mOss.asyncMultipartUpload(multipartUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public CompleteMultipartUploadResult multipartUpload(MultipartUploadRequest multipartUploadRequest) throws ServiceException, ClientException {
        return this.mOss.multipartUpload(multipartUploadRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<ResumableUploadResult> asyncResumableUpload(ResumableUploadRequest resumableUploadRequest, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> oSSCompletedCallback) {
        return this.mOss.asyncResumableUpload(resumableUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public ResumableUploadResult resumableUpload(ResumableUploadRequest resumableUploadRequest) throws ServiceException, ClientException {
        return this.mOss.resumableUpload(resumableUploadRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<ResumableUploadResult> asyncSequenceUpload(ResumableUploadRequest resumableUploadRequest, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> oSSCompletedCallback) {
        return this.mOss.asyncSequenceUpload(resumableUploadRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public ResumableUploadResult sequenceUpload(ResumableUploadRequest resumableUploadRequest) throws ServiceException, ClientException {
        return this.mOss.sequenceUpload(resumableUploadRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public String presignConstrainedObjectURL(GeneratePresignedUrlRequest generatePresignedUrlRequest) throws ClientException {
        return this.mOss.presignConstrainedObjectURL(generatePresignedUrlRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public String presignConstrainedObjectURL(String str, String str2, long j) throws ClientException {
        return this.mOss.presignConstrainedObjectURL(str, str2, j);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public String presignPublicObjectURL(String str, String str2) {
        return this.mOss.presignPublicObjectURL(str, str2);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public boolean doesObjectExist(String str, String str2) throws ServiceException, ClientException {
        return this.mOss.doesObjectExist(str, str2);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public void abortResumableUpload(ResumableUploadRequest resumableUploadRequest) throws IOException {
        this.mOss.abortResumableUpload(resumableUploadRequest);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public OSSAsyncTask<TriggerCallbackResult> asyncTriggerCallback(TriggerCallbackRequest triggerCallbackRequest, OSSCompletedCallback<TriggerCallbackRequest, TriggerCallbackResult> oSSCompletedCallback) {
        return this.mOss.asyncTriggerCallback(triggerCallbackRequest, oSSCompletedCallback);
    }

    @Override // com.alibaba.sdk.android.oss.OSS
    public TriggerCallbackResult triggerCallback(TriggerCallbackRequest triggerCallbackRequest) throws ServiceException, ClientException {
        return this.mOss.triggerCallback(triggerCallbackRequest);
    }
}
