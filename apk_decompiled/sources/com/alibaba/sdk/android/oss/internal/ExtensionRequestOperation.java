package com.alibaba.sdk.android.oss.internal;

import android.os.Environment;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.HeadObjectRequest;
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;
import com.alibaba.sdk.android.oss.network.ExecutionContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/* JADX INFO: loaded from: classes.dex */
public class ExtensionRequestOperation {
    private static ExecutorService executorService = Executors.newFixedThreadPool(5, new ThreadFactory() { // from class: com.alibaba.sdk.android.oss.internal.ExtensionRequestOperation.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "oss-android-extensionapi-thread");
        }
    });
    private InternalRequestOperation apiOperation;

    public ExtensionRequestOperation(InternalRequestOperation internalRequestOperation) {
        this.apiOperation = internalRequestOperation;
    }

    public boolean doesObjectExist(String str, String str2) throws ServiceException, ClientException {
        try {
            this.apiOperation.headObject(new HeadObjectRequest(str, str2), null).getResult();
            return true;
        } catch (ServiceException e) {
            if (e.getStatusCode() == 404) {
                return false;
            }
            throw e;
        }
    }

    public void abortResumableUpload(ResumableUploadRequest resumableUploadRequest) throws IOException {
        setCRC64(resumableUploadRequest);
        String uploadFilePath = resumableUploadRequest.getUploadFilePath();
        if (OSSUtils.isEmptyString(resumableUploadRequest.getRecordDirectory())) {
            return;
        }
        String strCalculateMd5Str = BinaryUtil.calculateMd5Str((BinaryUtil.calculateMd5Str(uploadFilePath) + resumableUploadRequest.getBucketName() + resumableUploadRequest.getObjectKey() + String.valueOf(resumableUploadRequest.getPartSize())).getBytes());
        StringBuilder sb = new StringBuilder();
        sb.append(resumableUploadRequest.getRecordDirectory());
        sb.append("/");
        sb.append(strCalculateMd5Str);
        File file = new File(sb.toString());
        if (file.exists()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();
            bufferedReader.close();
            OSSLog.logDebug("[initUploadId] - Found record file, uploadid: " + line);
            if (resumableUploadRequest.getCRC64() == OSSRequest.CRC64Config.YES) {
                File file2 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + OSSConstants.RESOURCE_NAME_OSS + File.separator + line);
                if (file2.exists()) {
                    file2.delete();
                }
            }
            this.apiOperation.abortMultipartUpload(new AbortMultipartUploadRequest(resumableUploadRequest.getBucketName(), resumableUploadRequest.getObjectKey(), line), null);
        }
        file.delete();
    }

    public OSSAsyncTask<ResumableUploadResult> resumableUpload(ResumableUploadRequest resumableUploadRequest, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> oSSCompletedCallback) {
        setCRC64(resumableUploadRequest);
        ExecutionContext executionContext = new ExecutionContext(this.apiOperation.getInnerClient(), resumableUploadRequest, this.apiOperation.getApplicationContext());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new ResumableUploadTask(resumableUploadRequest, oSSCompletedCallback, executionContext, this.apiOperation)), executionContext);
    }

    public OSSAsyncTask<ResumableUploadResult> sequenceUpload(ResumableUploadRequest resumableUploadRequest, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> oSSCompletedCallback) {
        setCRC64(resumableUploadRequest);
        ExecutionContext executionContext = new ExecutionContext(this.apiOperation.getInnerClient(), resumableUploadRequest, this.apiOperation.getApplicationContext());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new SequenceUploadTask(resumableUploadRequest, oSSCompletedCallback, executionContext, this.apiOperation)), executionContext);
    }

    public OSSAsyncTask<CompleteMultipartUploadResult> multipartUpload(MultipartUploadRequest multipartUploadRequest, OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult> oSSCompletedCallback) {
        setCRC64(multipartUploadRequest);
        ExecutionContext executionContext = new ExecutionContext(this.apiOperation.getInnerClient(), multipartUploadRequest, this.apiOperation.getApplicationContext());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new MultipartUploadTask(this.apiOperation, multipartUploadRequest, oSSCompletedCallback, executionContext)), executionContext);
    }

    private void setCRC64(OSSRequest oSSRequest) {
        Enum crc64;
        if (oSSRequest.getCRC64() != OSSRequest.CRC64Config.NULL) {
            crc64 = oSSRequest.getCRC64();
        } else {
            crc64 = this.apiOperation.getConf().isCheckCRC64() ? OSSRequest.CRC64Config.YES : OSSRequest.CRC64Config.NO;
        }
        oSSRequest.setCRC64(crc64);
    }
}
