package com.alibaba.android.multiendinonebridge;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import java.util.Map;
import org.android.agoo.common.AgooConstants;
import org.json.JSONException;

/* JADX INFO: loaded from: classes.dex */
public class IoTMultiendInOneBridge {
    public static String KEY_CTL_TRACE_ID = null;
    public static String KEY_CTL_TRIGGER_SOURCE = null;
    public static final int STATUS_DEVICE_OFFLINE = -2;
    public static final int STATUS_ERROR = -1;
    public static final int STATUS_SUCCESS = 0;
    private static final String TAG = "IoTMultiendInOneBridge";
    private static IoTMultiendInOneBridge mInstance = null;
    private static boolean mLoadLibraryError = false;
    private volatile boolean isInitialized = false;
    private Context mContext;
    private IUpstreamProxy mUpstreamProxy;

    private native int controlDevice(String str, IResponseCallback iResponseCallback);

    private native void enableHeartbeatNative(boolean z);

    private native int init();

    private native void localControlTranslation(String str, IResponseCallback iResponseCallback);

    private native void localStateTranslation(String str, IResponseCallback iResponseCallback);

    private native void manualSetDevOnlineStatusNative(String str, int i);

    private static native int nativeSetupJNI();

    private native void onDeviceStatusChangeNative(String str);

    private native void onProxyConnectedNative();

    private native int onRecvTextMsgNative(String str);

    private native int queryDevOnlineStatusNative(String str);

    private native void releaseNative();

    private native void updateSigMeshNetworkParameterNative(String str, String str2);

    public native int ctrlDevbyTSL(String str);

    static {
        try {
            System.loadLibrary("multieninone-bridge-lib");
        } catch (UnsatisfiedLinkError e) {
            mLoadLibraryError = true;
            Log.e(TAG, e.toString());
        }
        KEY_CTL_TRACE_ID = "trace_id";
        KEY_CTL_TRIGGER_SOURCE = "trigger_source";
    }

    public static IoTMultiendInOneBridge getInstance() {
        if (mInstance == null) {
            synchronized (IoTMultiendInOneBridge.class) {
                if (mInstance == null) {
                    mInstance = new IoTMultiendInOneBridge();
                }
            }
        }
        return mInstance;
    }

    private IoTMultiendInOneBridge() {
    }

    public int init(Context context) {
        Log.i(TAG, "Start init Bridge...");
        if (mLoadLibraryError) {
            Log.e(TAG, "Error loading library, stop init");
            return -1;
        }
        if (this.isInitialized) {
            Log.v(TAG, "Initialized, return");
            return 0;
        }
        if (context == null) {
            Log.e(TAG, "context is null");
            throw new IllegalArgumentException("context is null");
        }
        this.mContext = context.getApplicationContext();
        nativeSetupJNI();
        this.isInitialized = true;
        return init();
    }

    public void reset() {
        Log.i(TAG, "Reset IoTMultiendInOne SDK");
        this.isInitialized = false;
    }

    public void setUpstreamProxy(IUpstreamProxy iUpstreamProxy) {
        this.mUpstreamProxy = iUpstreamProxy;
    }

    public void localControlTranslation(String str, String str2, IResponseCallback iResponseCallback) {
        if (makeSureEnvironmentIsInitialized(iResponseCallback)) {
            JSONObject object = JSON.parseObject(str2);
            if (object == null) {
                if (iResponseCallback != null) {
                    iResponseCallback.onResponse(ErrorCode.INVALID_PARAMETERS.getCode(), "params can not be null");
                    return;
                }
                return;
            }
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            try {
                jSONObject.put("devId", str);
                if (object.containsKey("params")) {
                    jSONObject.put("params", object.getJSONObject("params"));
                } else {
                    jSONObject.put("params", str2);
                }
                if (object.containsKey("noAck")) {
                    jSONObject.put("noAck", object.getBooleanValue("noAck"));
                }
                localControlTranslation(jSONObject.toString(), new ResponseCallbackDelegate(iResponseCallback));
            } catch (JSONException e) {
                Log.e(TAG, "has json exception");
                e.printStackTrace();
            }
        }
    }

    public void localStateTranslation(String str, String str2, String str3, IResponseCallback iResponseCallback) {
        if (makeSureEnvironmentIsInitialized(iResponseCallback)) {
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            try {
                jSONObject.put("devId", str);
                jSONObject.put("opcode", str2);
                jSONObject.put("params", str3);
                localStateTranslation(jSONObject.toString(), new ResponseCallbackDelegate(iResponseCallback));
            } catch (JSONException e) {
                Log.e(TAG, "has json exception");
                e.printStackTrace();
            }
        }
    }

    public void controlDevice(String str, String str2, Map<String, Object> map, Map<String, String> map2, IResponseCallback iResponseCallback) {
        if (makeSureEnvironmentIsInitialized(iResponseCallback)) {
            String str3 = "";
            String str4 = "";
            if (map2 != null) {
                str3 = map2.get(KEY_CTL_TRACE_ID);
                str4 = map2.get(KEY_CTL_TRIGGER_SOURCE);
                if (TextUtils.isEmpty(str3)) {
                    str3 = new Date().getTime() + Utils.getRandomString(19);
                }
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("devId", (Object) str);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("method", (Object) str2);
            jSONObject2.put("params", (Object) map);
            jSONObject2.put("devId", (Object) str);
            jSONObject2.put("msgId", (Object) "");
            jSONObject2.put("version", (Object) "1.0");
            jSONObject.put("params", (Object) jSONObject2);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("trigger_source", (Object) str4);
            jSONObject3.put("local_trace_id", (Object) str3);
            jSONObject.put(AgooConstants.MESSAGE_TRACE, (Object) jSONObject3);
            controlDevice(jSONObject.toJSONString(), new ResponseCallbackDelegate(iResponseCallback));
        }
    }

    private static class ResponseCallbackDelegate implements IResponseCallback {
        private final IResponseCallback callback;
        private final Handler handler;

        public ResponseCallbackDelegate(IResponseCallback iResponseCallback) {
            this.callback = iResponseCallback;
            Looper looperMyLooper = Looper.myLooper();
            if (looperMyLooper != null) {
                this.handler = new Handler(looperMyLooper);
            } else {
                this.handler = new Handler(Looper.getMainLooper());
            }
        }

        @Override // com.alibaba.android.multiendinonebridge.IResponseCallback
        public void onResponse(final int i, final String str) {
            this.handler.post(new Runnable() { // from class: com.alibaba.android.multiendinonebridge.IoTMultiendInOneBridge.ResponseCallbackDelegate.1
                @Override // java.lang.Runnable
                public void run() {
                    if (ResponseCallbackDelegate.this.callback != null) {
                        ResponseCallbackDelegate.this.callback.onResponse(i, str);
                    }
                }
            });
        }
    }

    public int onRecvTextMsg(String str) {
        if (makeSureEnvironmentIsInitialized(null)) {
            return onRecvTextMsgNative(str);
        }
        return -1;
    }

    public void onDeviceStatusChange(String str) {
        if (makeSureEnvironmentIsInitialized(null)) {
            onDeviceStatusChangeNative(str);
        }
    }

    public int queryDevOnlineStatus(String str) {
        if (makeSureEnvironmentIsInitialized(null)) {
            return queryDevOnlineStatusNative(str);
        }
        return 3;
    }

    public void enableHeartbeat(boolean z) {
        if (makeSureEnvironmentIsInitialized(null)) {
            enableHeartbeatNative(z);
        }
    }

    public void onProxyConnected() {
        if (makeSureEnvironmentIsInitialized(null)) {
            onProxyConnectedNative();
        }
    }

    public void manualSetDevOnlineStatus(String str, int i) {
        if (makeSureEnvironmentIsInitialized(null)) {
            manualSetDevOnlineStatusNative(str, i);
        }
    }

    public void updateSigMeshNetworkParameter(String str, SigMeshNetworkParameters sigMeshNetworkParameters) {
        if (makeSureEnvironmentIsInitialized(null)) {
            updateSigMeshNetworkParameterNative(str, JSON.toJSONString(sigMeshNetworkParameters));
        }
    }

    public void release() {
        if (makeSureEnvironmentIsInitialized(null)) {
            releaseNative();
        }
    }

    private boolean makeSureEnvironmentIsInitialized(IResponseCallback iResponseCallback) {
        if (mLoadLibraryError) {
            if (iResponseCallback != null) {
                try {
                    iResponseCallback.onResponse(ErrorCode.ERROR_LOADING_LIBRARY.getCode(), ErrorCode.ERROR_LOADING_LIBRARY.getDescription());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
            return false;
        }
        if (this.isInitialized) {
            return true;
        }
        if (iResponseCallback != null) {
            try {
                iResponseCallback.onResponse(ErrorCode.NOT_INITIALIZED_YET.getCode(), ErrorCode.NOT_INITIALIZED_YET.getDescription());
            } catch (Exception e2) {
                Log.e(TAG, e2.toString());
            }
        }
        return false;
    }

    public static String getCachePath() {
        Context context;
        IoTMultiendInOneBridge ioTMultiendInOneBridge = mInstance;
        if (ioTMultiendInOneBridge == null || (context = ioTMultiendInOneBridge.mContext) == null) {
            Log.w(TAG, "mInstance: " + mInstance);
            return "";
        }
        return context.getCacheDir().getAbsolutePath();
    }

    public void sendTextMsg(String str, String str2, int i, String str3) {
        Log.d(TAG, String.format("sendTextMsg, eventName:%s, payloadJson:%s", str2, str3));
        IUpstreamProxy iUpstreamProxy = this.mUpstreamProxy;
        if (iUpstreamProxy != null) {
            try {
                iUpstreamProxy.invokeEventMethod(str2, str3);
            } catch (Exception unused) {
            }
        }
    }

    public void sendIoTCommand(int i, String str) {
        Log.d(TAG, String.format("Send IoT command, type: %d, payload: %s", Integer.valueOf(i), str));
        IUpstreamProxy iUpstreamProxy = this.mUpstreamProxy;
        if (iUpstreamProxy != null) {
            try {
                iUpstreamProxy.sendIoTCommand(i, str);
            } catch (Exception unused) {
            }
        }
    }
}
