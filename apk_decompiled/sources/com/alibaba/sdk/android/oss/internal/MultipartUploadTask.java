package com.alibaba.sdk.android.oss.internal;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest;
import com.alibaba.sdk.android.oss.network.ExecutionContext;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
public class MultipartUploadTask extends BaseMultipartUploadTask<MultipartUploadRequest, CompleteMultipartUploadResult> implements Callable<CompleteMultipartUploadResult> {
    public MultipartUploadTask(InternalRequestOperation internalRequestOperation, MultipartUploadRequest multipartUploadRequest, OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult> oSSCompletedCallback, ExecutionContext executionContext) {
        super(internalRequestOperation, multipartUploadRequest, oSSCompletedCallback, executionContext);
    }

    @Override // com.alibaba.sdk.android.oss.internal.BaseMultipartUploadTask
    protected void initMultipartUploadId() throws ServiceException, ClientException {
        this.mUploadId = ((InitiateMultipartUploadResult) this.mApiOperation.initMultipartUpload(new InitiateMultipartUploadRequest(this.mRequest.getBucketName(), this.mRequest.getObjectKey(), this.mRequest.getMetadata()), null).getResult()).getUploadId();
        this.mRequest.setUploadId(this.mUploadId);
    }

    @Override // com.alibaba.sdk.android.oss.internal.BaseMultipartUploadTask
    protected CompleteMultipartUploadResult doMultipartUpload() throws ServiceException, ClientException, InterruptedException, IOException {
        checkCancel();
        this.mUploadFile = new File(this.mRequest.getUploadFilePath());
        this.mFileLength = this.mUploadFile.length();
        if (this.mFileLength == 0) {
            throw new ClientException("file length must not be 0");
        }
        int[] iArr = new int[2];
        checkPartSize(iArr);
        final int iMin = iArr[0];
        final int i = iArr[1];
        int i2 = 0;
        for (final int i3 = 0; i3 < i; i3++) {
            checkException();
            if (this.mPoolExecutor != null) {
                if (i3 == i - 1) {
                    iMin = (int) Math.min(iMin, this.mFileLength - ((long) i2));
                }
                i2 += iMin;
                this.mPoolExecutor.execute(new Runnable() { // from class: com.alibaba.sdk.android.oss.internal.MultipartUploadTask.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MultipartUploadTask.this.uploadPart(i3, iMin, i);
                    }
                });
            }
        }
        if (checkWaitCondition(i)) {
            synchronized (this.mLock) {
                this.mLock.wait();
            }
        }
        if (this.mUploadException != null) {
            abortThisUpload();
        }
        checkException();
        CompleteMultipartUploadResult completeMultipartUploadResult = completeMultipartUploadResult();
        releasePool();
        return completeMultipartUploadResult;
    }

    @Override // com.alibaba.sdk.android.oss.internal.BaseMultipartUploadTask
    protected void abortThisUpload() {
        if (this.mUploadId != null) {
            this.mApiOperation.abortMultipartUpload(new AbortMultipartUploadRequest(this.mRequest.getBucketName(), this.mRequest.getObjectKey(), this.mUploadId), null).waitUntilFinished();
        }
    }

    @Override // com.alibaba.sdk.android.oss.internal.BaseMultipartUploadTask
    protected void processException(Exception exc) {
        synchronized (this.mLock) {
            this.mPartExceptionCount++;
            if (this.mUploadException == null) {
                this.mUploadException = exc;
                this.mLock.notify();
            }
        }
    }

    @Override // com.alibaba.sdk.android.oss.internal.BaseMultipartUploadTask
    protected void preUploadPart(int i, int i2, int i3) throws Exception {
        checkException();
    }
}
