package com.aliyun.alink.apiclient;

import com.aliyun.alink.apiclient.biz.IHandler;
import com.aliyun.alink.apiclient.biz.RequestHandlerFactory;
import com.aliyun.alink.apiclient.model.DeviceAuthInfo;
import com.aliyun.alink.apiclient.utils.StringUtils;
import com.http.utils.LogUtils;

/* JADX INFO: loaded from: classes.dex */
public class IoTAPIClientImpl implements IoTApiClient {
    private static final String TAG = "[ITC]IoTAPIClientImpl";
    private boolean iotApiClientInited;

    private static class SingletonHolder {
        private static final IoTAPIClientImpl INSTANCE = new IoTAPIClientImpl();

        private SingletonHolder() {
        }
    }

    private IoTAPIClientImpl() {
        this.iotApiClientInited = false;
    }

    public static IoTAPIClientImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override // com.aliyun.alink.apiclient.IoTApiClient
    public void init(InitializeConfig initializeConfig) {
        if (initializeConfig == null) {
            LogUtils.error(TAG, "config null.");
            throw new RuntimeException("IoTApiClient init configNull exception.");
        }
        if (StringUtils.isEmptyString(initializeConfig.productKey) || StringUtils.isEmptyString(initializeConfig.deviceName) || StringUtils.isEmptyString(initializeConfig.domain)) {
            LogUtils.error(TAG, "config prams error, pk||dn||domain is empty.");
            throw new RuntimeException("IoTApiClient init params exception.");
        }
        if (StringUtils.isEmptyString(initializeConfig.deviceSecret) && StringUtils.isEmptyString(initializeConfig.productSecret)) {
            LogUtils.error(TAG, "config prams error, ps&&ds is empty.");
            throw new RuntimeException("IoTApiClient init sign secret exception.");
        }
        synchronized (this) {
            if (this.iotApiClientInited) {
                LogUtils.error(TAG, "can not duplicate initialize.");
            } else {
                LocalData.getInstance().setDeviceData(new DeviceAuthInfo(initializeConfig));
                this.iotApiClientInited = true;
            }
        }
    }

    @Override // com.aliyun.alink.apiclient.IoTApiClient
    public void send(CommonRequest commonRequest, IoTCallback ioTCallback) {
        if (!this.iotApiClientInited) {
            throw new RuntimeException("IoTApiClient not inited.");
        }
        if (commonRequest == null && ioTCallback == null) {
            LogUtils.error(TAG, "send error, request&callback null.");
            return;
        }
        if (commonRequest == null) {
            LogUtils.error(TAG, "send error, request=null.");
            ioTCallback.onFailure(commonRequest, new Exception("requestNull"));
            return;
        }
        IHandler iHandlerCreateHandler = new RequestHandlerFactory().createHandler(commonRequest);
        if (iHandlerCreateHandler == null) {
            ioTCallback.onFailure(commonRequest, new IllegalArgumentException("request illegal"));
        } else {
            iHandlerCreateHandler.handle(commonRequest, ioTCallback);
        }
    }
}
