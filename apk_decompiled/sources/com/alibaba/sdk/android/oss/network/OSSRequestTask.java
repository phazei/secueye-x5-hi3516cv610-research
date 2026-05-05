package com.alibaba.sdk.android.oss.network;

import anet.channel.request.Request;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.CRC64;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.internal.OSSRetryHandler;
import com.alibaba.sdk.android.oss.internal.OSSRetryType;
import com.alibaba.sdk.android.oss.internal.RequestMessage;
import com.alibaba.sdk.android.oss.internal.ResponseMessage;
import com.alibaba.sdk.android.oss.internal.ResponseParser;
import com.alibaba.sdk.android.oss.internal.ResponseParsers;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.CheckedInputStream;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* JADX INFO: loaded from: classes.dex */
public class OSSRequestTask<T extends OSSResult> implements Callable<T> {
    private OkHttpClient client;
    private ExecutionContext context;
    private int currentRetryCount = 0;
    private RequestMessage message;
    private ResponseParser<T> responseParser;
    private OSSRetryHandler retryHandler;

    public OSSRequestTask(RequestMessage requestMessage, ResponseParser responseParser, ExecutionContext executionContext, int i) {
        this.responseParser = responseParser;
        this.message = requestMessage;
        this.context = executionContext;
        this.client = executionContext.getClient();
        this.retryHandler = new OSSRetryHandler(i);
    }

    @Override // java.util.concurrent.Callable
    public T call() throws Exception {
        Request requestBuild;
        Call call;
        Exception clientException;
        Call callNewCall;
        ResponseMessage responseMessageBuildResponseMessage;
        OSSRequest request;
        long contentLength;
        String stringBody;
        InputStream content;
        try {
            if (this.context.getApplicationContext() != null) {
                OSSLog.logInfo(OSSUtils.buildBaseLogInfo(this.context.getApplicationContext()));
            }
            OSSLog.logDebug("[call] - ");
            request = this.context.getRequest();
            OSSUtils.ensureRequestValid(request, this.message);
            OSSUtils.signRequest(this.message);
        } catch (Exception e) {
            e = e;
            requestBuild = null;
            call = null;
        }
        if (this.context.getCancellationHandler().isCancelled()) {
            throw new InterruptedIOException("This task is cancelled!");
        }
        Request.Builder builderUrl = new Request.Builder().url(this.message.buildCanonicalURL());
        for (String str : this.message.getHeaders().keySet()) {
            builderUrl = builderUrl.addHeader(str, (String) this.message.getHeaders().get(str));
        }
        String str2 = (String) this.message.getHeaders().get("Content-Type");
        switch (this.message.getMethod()) {
            case POST:
            case PUT:
                OSSUtils.assertTrue(str2 != null, "Content type can't be null when upload!");
                if (this.message.getUploadData() != null) {
                    content = new ByteArrayInputStream(this.message.getUploadData());
                    contentLength = this.message.getUploadData().length;
                    stringBody = null;
                } else if (this.message.getUploadFilePath() != null) {
                    File file = new File(this.message.getUploadFilePath());
                    FileInputStream fileInputStream = new FileInputStream(file);
                    contentLength = file.length();
                    content = fileInputStream;
                    stringBody = null;
                } else if (this.message.getContent() != null) {
                    content = this.message.getContent();
                    contentLength = this.message.getContentLength();
                    stringBody = null;
                } else {
                    contentLength = 0;
                    stringBody = this.message.getStringBody();
                    content = null;
                }
                if (content != null) {
                    if (this.message.isCheckCRC64()) {
                        content = new CheckedInputStream(content, new CRC64());
                    }
                    this.message.setContent(content);
                    this.message.setContentLength(contentLength);
                    builderUrl = builderUrl.method(this.message.getMethod().toString(), NetworkProgressHelper.addProgressRequestBody(content, contentLength, str2, this.context));
                } else if (stringBody != null) {
                    builderUrl = builderUrl.method(this.message.getMethod().toString(), RequestBody.create(MediaType.parse(str2), stringBody.getBytes("UTF-8")));
                } else {
                    builderUrl = builderUrl.method(this.message.getMethod().toString(), RequestBody.create((MediaType) null, new byte[0]));
                }
                break;
            case GET:
                builderUrl = builderUrl.get();
                break;
            case HEAD:
                builderUrl = builderUrl.head();
                break;
            case DELETE:
                builderUrl = builderUrl.delete();
                break;
        }
        requestBuild = builderUrl.build();
        try {
            if (request instanceof GetObjectRequest) {
                this.client = NetworkProgressHelper.addProgressResponseListener(this.client, this.context);
                OSSLog.logDebug("getObject");
            }
            callNewCall = this.client.newCall(requestBuild);
            try {
                this.context.getCancellationHandler().setCall(callNewCall);
                Response responseExecute = callNewCall.execute();
                if (OSSLog.isEnableLog()) {
                    Map<String, List<String>> multimap = responseExecute.headers().toMultimap();
                    StringBuilder sb = new StringBuilder();
                    sb.append("response:---------------------\n");
                    sb.append("response code: " + responseExecute.code() + " for url: " + requestBuild.url() + SdkConstant.CLOUDAPI_LF);
                    for (String str3 : multimap.keySet()) {
                        sb.append("responseHeader [" + str3 + "]: ");
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(multimap.get(str3).get(0));
                        sb2.append(SdkConstant.CLOUDAPI_LF);
                        sb.append(sb2.toString());
                    }
                    OSSLog.logDebug(sb.toString());
                }
                responseMessageBuildResponseMessage = buildResponseMessage(this.message, responseExecute);
                clientException = null;
            } catch (Exception e2) {
                call = callNewCall;
                e = e2;
                OSSLog.logError("Encounter local execpiton: " + e.toString());
                if (OSSLog.isEnableLog()) {
                    e.printStackTrace();
                }
                clientException = new ClientException(e.getMessage(), e);
                callNewCall = call;
                responseMessageBuildResponseMessage = null;
            }
        } catch (Exception e3) {
            e = e3;
            call = null;
        }
        if (responseMessageBuildResponseMessage != null) {
            try {
                DateUtil.setCurrentServerTime(DateUtil.parseRfc822Date((String) responseMessageBuildResponseMessage.getHeaders().get("Date")).getTime());
            } catch (Exception unused) {
            }
        }
        if (clientException == null && (responseMessageBuildResponseMessage.getStatusCode() == 203 || responseMessageBuildResponseMessage.getStatusCode() >= 300)) {
            clientException = ResponseParsers.parseResponseErrorXML(responseMessageBuildResponseMessage, requestBuild.method().equals(Request.Method.HEAD));
        } else if (clientException == null) {
            try {
                T t = (T) this.responseParser.parse(responseMessageBuildResponseMessage);
                if (this.context.getCompletedCallback() != null) {
                    try {
                        this.context.getCompletedCallback().onSuccess(this.context.getRequest(), t);
                    } catch (Exception unused2) {
                    }
                }
                return t;
            } catch (IOException e4) {
                clientException = new ClientException(e4.getMessage(), e4);
            }
        }
        Exception clientException2 = ((callNewCall == null || !callNewCall.isCanceled()) && !this.context.getCancellationHandler().isCancelled()) ? clientException : new ClientException("Task is cancelled!", clientException.getCause(), true);
        OSSRetryType oSSRetryTypeShouldRetry = this.retryHandler.shouldRetry(clientException2, this.currentRetryCount);
        OSSLog.logError("[run] - retry, retry type: " + oSSRetryTypeShouldRetry);
        if (oSSRetryTypeShouldRetry == OSSRetryType.OSSRetryTypeShouldRetry) {
            this.currentRetryCount++;
            if (this.context.getRetryCallback() != null) {
                this.context.getRetryCallback().onRetryCallback();
            }
            return (T) call();
        }
        if (oSSRetryTypeShouldRetry == OSSRetryType.OSSRetryTypeShouldFixedTimeSkewedAndRetry) {
            if (responseMessageBuildResponseMessage != null) {
                this.message.getHeaders().put("Date", responseMessageBuildResponseMessage.getHeaders().get("Date"));
            }
            this.currentRetryCount++;
            if (this.context.getRetryCallback() != null) {
                this.context.getRetryCallback().onRetryCallback();
            }
            return (T) call();
        }
        if (clientException2 instanceof ClientException) {
            if (this.context.getCompletedCallback() != null) {
                this.context.getCompletedCallback().onFailure(this.context.getRequest(), (ClientException) clientException2, null);
                throw clientException2;
            }
            throw clientException2;
        }
        if (this.context.getCompletedCallback() != null) {
            this.context.getCompletedCallback().onFailure(this.context.getRequest(), null, (ServiceException) clientException2);
            throw clientException2;
        }
        throw clientException2;
    }

    private ResponseMessage buildResponseMessage(RequestMessage requestMessage, Response response) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequest(requestMessage);
        responseMessage.setResponse(response);
        HashMap map = new HashMap();
        Headers headers = response.headers();
        for (int i = 0; i < headers.size(); i++) {
            map.put(headers.name(i), headers.value(i));
        }
        responseMessage.setHeaders(map);
        responseMessage.setStatusCode(response.code());
        responseMessage.setContentLength(response.body().contentLength());
        responseMessage.setContent(response.body().byteStream());
        return responseMessage;
    }
}
