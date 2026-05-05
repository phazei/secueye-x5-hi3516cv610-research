package com.aliyun.alink.linksdk.alcs.coap;

import android.text.TextUtils;
import android.util.SparseArray;
import com.aliyun.alink.linksdk.alcs.coap.option.Option;
import com.aliyun.alink.linksdk.alcs.coap.option.OptionSet;
import com.aliyun.alink.linksdk.alcs.coap.resources.AlcsCoAPResource;
import com.aliyun.alink.linksdk.alcs.coap.resources.Resource;
import com.aliyun.alink.linksdk.tools.ALog;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsCoAP {
    protected static final String TAG = "[alcs_coap_sdk]AlcsCoAP";
    protected static SparseArray<IAuthHandler> mAuthHandlerList;
    protected static Map<Long, AlcsCoAPContext> mContextList;
    protected static Map<Long, Map<Long, IAlcsCoAPReqHandler>> mReqHandlerList;
    protected static Map<String, AlcsCoAPResource> mResourceList;
    protected static AtomicInteger mUserData;

    protected static native void setLogLevelNative(int i);

    protected native void addIntOption(long j, long j2, int i, int i2);

    protected native boolean addKey(String str, int i, int i2, String str2);

    protected native void addStringOption(long j, long j2, int i, String str);

    protected native boolean addSvrAccessKey(long j, String str, String str2);

    public native void alcsStart(long j);

    public native void alcsStop(long j);

    protected native boolean authHasKey(long j, String str, int i, String str2, String str3, String str4, String str5, int i2);

    public native boolean cancelMessage(long j, long j2);

    protected native long createCoAPContext(AlcsCoAPContext alcsCoAPContext);

    protected native void freeContext(long j);

    public native boolean initAuth(long j, String str, String str2, int i);

    protected native long initRequest(long j, AlcsCoAPRequest alcsCoAPRequest);

    protected native long initResponse(long j, AlcsCoAPResponse alcsCoAPResponse);

    protected native boolean isServerOnLine(long j, String str, int i, String str2, String str3);

    public native boolean notifyObserve(long j, String str, byte[] bArr);

    protected native long registerResource(long j, AlcsCoAPResource alcsCoAPResource, String str, String str2);

    protected native boolean removeKey(String str, int i, int i2);

    protected native boolean removeSvrKey(long j, String str);

    protected native boolean sendAlcsRequest(long j, long j2, String str, int i);

    protected native boolean sendAlcsRequestSecure(long j, long j2, String str, int i, String str2, String str3);

    protected native boolean sendAlcsResponse(long j, long j2, String str, int i);

    protected native boolean sendAlcsResponseSecure(long j, long j2, String str, int i, String str2, String str3);

    protected native void unInitMessage(long j, long j2);

    protected native long unRegisterResource(long j, String str);

    protected native boolean updateSvrBlackList(long j, String str);

    static {
        System.loadLibrary("coap");
        mUserData = new AtomicInteger(0);
    }

    public AlcsCoAP() {
        if (mContextList == null) {
            mContextList = new HashMap();
        }
        if (mResourceList == null) {
            mResourceList = new HashMap();
        }
        if (mReqHandlerList == null) {
            mReqHandlerList = new HashMap();
        }
        if (mAuthHandlerList == null) {
            mAuthHandlerList = new SparseArray<>();
        }
    }

    public long createNewCoAPContext(AlcsCoAPContext alcsCoAPContext) {
        if (alcsCoAPContext == null) {
            ALog.e(TAG, "createNewCoAPContext error context null");
            return -1L;
        }
        long contextByPort = getContextByPort(alcsCoAPContext.getPort());
        if (contextByPort != 0) {
            alcsCoAPContext.mContextId = contextByPort;
            return contextByPort;
        }
        long jCreateCoAPContext = createCoAPContext(alcsCoAPContext);
        alcsCoAPContext.setContextId(jCreateCoAPContext);
        mContextList.put(Long.valueOf(jCreateCoAPContext), alcsCoAPContext);
        return jCreateCoAPContext;
    }

    protected long getContextByPort(int i) {
        Map<Long, AlcsCoAPContext> map = mContextList;
        long jLongValue = 0;
        if (map == null) {
            ALog.e(TAG, "getContextByPort mContextList empty");
            return 0L;
        }
        Iterator<Map.Entry<Long, AlcsCoAPContext>> it = map.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<Long, AlcsCoAPContext> next = it.next();
            if (next.getValue().getPort() == i) {
                jLongValue = next.getKey().longValue();
                break;
            }
        }
        ALog.d(TAG, "getContextByPort port:" + i + " contextId:" + jLongValue);
        return jLongValue;
    }

    public long freeCoAPContext(long j) {
        mContextList.remove(Long.valueOf(j));
        freeContext(j);
        return j;
    }

    public long createCoAPContext(AlcsCoAPContext alcsCoAPContext, AlcsCoAPResource alcsCoAPResource) {
        long jCreateNewCoAPContext = createNewCoAPContext(alcsCoAPContext);
        registerAllResource(jCreateNewCoAPContext, alcsCoAPResource, null, null);
        return jCreateNewCoAPContext;
    }

    public void registerAllResource(long j, AlcsCoAPResource alcsCoAPResource, String str, String str2) {
        if (alcsCoAPResource == null || TextUtils.isEmpty(alcsCoAPResource.getPath())) {
            ALog.e(TAG, "registerAllResource resource null");
            return;
        }
        registerResource(j, alcsCoAPResource, str, str2);
        mResourceList.put(alcsCoAPResource.getPath(), alcsCoAPResource);
        Collection<Resource> children = alcsCoAPResource.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }
        Iterator<Resource> it = children.iterator();
        while (it.hasNext()) {
            registerResource(j, (AlcsCoAPResource) it.next(), str, str2);
        }
    }

    public void registerAllResource(long j, AlcsCoAPResource alcsCoAPResource) {
        registerAllResource(j, alcsCoAPResource, null, null);
    }

    public void unRegisterResource(long j, AlcsCoAPResource alcsCoAPResource) {
        unRegisterResourceByPath(j, alcsCoAPResource.getPath());
    }

    public long unRegisterResourceByPath(long j, String str) {
        ALog.d(TAG, "unRegisterResourceByPath contextId:" + j + " path:" + str);
        return unRegisterResource(j, str);
    }

    public long sendRequest(long j, AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        ALog.d(TAG, "sendRequest coapContextId:" + j + " requestCallback:" + iAlcsCoAPReqHandler);
        long jInitRequest = initRequest(j, alcsCoAPRequest);
        putRequestCallback(j, jInitRequest, iAlcsCoAPReqHandler);
        initOptionSet(j, jInitRequest, alcsCoAPRequest.getOptions());
        sendAlcsRequest(j, jInitRequest, alcsCoAPRequest.getDestination().getHostAddress(), alcsCoAPRequest.getDestinationPort());
        unInitMessage(j, jInitRequest);
        return jInitRequest;
    }

    public long sendRequestS(long j, AlcsCoAPRequest alcsCoAPRequest, String str, String str2, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        ALog.d(TAG, "sendRequests coapContextId:" + j + " requestCallback:" + iAlcsCoAPReqHandler);
        long jInitRequest = initRequest(j, alcsCoAPRequest);
        putRequestCallback(j, jInitRequest, iAlcsCoAPReqHandler);
        initOptionSet(j, jInitRequest, alcsCoAPRequest.getOptions());
        sendAlcsRequestSecure(j, jInitRequest, alcsCoAPRequest.getDestination().getHostAddress(), alcsCoAPRequest.getDestinationPort(), str, str2);
        unInitMessage(j, jInitRequest);
        return jInitRequest;
    }

    public boolean sendResponse(long j, AlcsCoAPResponse alcsCoAPResponse) {
        long jInitResponse = initResponse(j, alcsCoAPResponse);
        ALog.d(TAG, "sendResponse coapContextId:" + j + " msgId:" + jInitResponse);
        initOptionSet(j, jInitResponse, alcsCoAPResponse.getOptions());
        sendAlcsResponse(j, jInitResponse, alcsCoAPResponse.getDestination().getHostAddress(), alcsCoAPResponse.getDestinationPort());
        unInitMessage(j, jInitResponse);
        return true;
    }

    public boolean sendResponseS(long j, AlcsCoAPResponse alcsCoAPResponse, String str, String str2) {
        ALog.d(TAG, "sendResponse coapContextId:" + j);
        long jInitResponse = initResponse(j, alcsCoAPResponse);
        initOptionSet(j, jInitResponse, alcsCoAPResponse.getOptions());
        sendAlcsResponseSecure(j, jInitResponse, alcsCoAPResponse.getDestination().getHostAddress(), alcsCoAPResponse.getDestinationPort(), str, str2);
        unInitMessage(j, jInitResponse);
        return true;
    }

    public boolean sendObserveResponse(long j, AlcsCoAPRequest alcsCoAPRequest, AlcsCoAPResponse alcsCoAPResponse) {
        ALog.d(TAG, "sendObserveResponse coapContextId:" + j);
        return notifyObserve(j, alcsCoAPRequest.getURI(), alcsCoAPResponse.getPayload());
    }

    public boolean authHasKey(long j, String str, int i, String str2, String str3, String str4, String str5, IAuthHandler iAuthHandler) {
        int iIncrementAndGet = mUserData.incrementAndGet();
        ALog.d(TAG, "authHasKey coapContextId:" + j + " ip:" + str + " port:" + i + " userData:" + iIncrementAndGet + " productKey:" + str2 + " deviceName:" + str3 + " accessKey:" + str4 + " accessToken:" + str5);
        mAuthHandlerList.put(iIncrementAndGet, iAuthHandler);
        return authHasKey(j, str, i, str2, str3, str4, str5, iIncrementAndGet);
    }

    public boolean isServerDevOnline(long j, String str, int i, String str2, String str3) {
        boolean zIsServerOnLine = isServerOnLine(j, str, i, str2, str3);
        ALog.d(TAG, "isServerDevOnline coapContextId:" + j + " ip:" + str + " port:" + i + " pk:" + str2 + " dn:" + str3 + " ret:" + zIsServerOnLine);
        return zIsServerOnLine;
    }

    public boolean addAlcsSvrAccessKey(long j, String str, String str2) {
        boolean zAddSvrAccessKey = addSvrAccessKey(j, str, str2);
        ALog.d(TAG, "addAlcsSvrAccessKey coapContextId:" + j + " prefix:" + str + " ret:" + zAddSvrAccessKey);
        return zAddSvrAccessKey;
    }

    public boolean removeAlcsSvrKey(long j, String str) {
        boolean zRemoveSvrKey = removeSvrKey(j, str);
        ALog.d(TAG, "removeAlcsSvrKey coapContextId:" + j + " prefix:" + str + " ret:" + zRemoveSvrKey);
        return zRemoveSvrKey;
    }

    public boolean updateAlcsSvrBlackList(long j, String str) {
        boolean zUpdateSvrBlackList = updateSvrBlackList(j, str);
        ALog.d(TAG, "updateAlcsSvrBlackList coapContextId:" + j + " blackList:" + str);
        return zUpdateSvrBlackList;
    }

    public static void onRecvRequestHandler(long j, String str, String str2, int i, AlcsCoAPRequest alcsCoAPRequest) {
        InetAddress byName;
        ALog.d(TAG, "onRecvRequestHandler test contextId:" + j + " resourceId:" + str + " Ip:" + str2 + " port:" + i + " request:" + alcsCoAPRequest);
        alcsCoAPRequest.setSourcePort(i);
        try {
            byName = InetAddress.getByName(str2);
        } catch (Exception e) {
            e.printStackTrace();
            byName = null;
        }
        alcsCoAPRequest.setSource(byName);
        AlcsCoAPResource alcsCoAPResource = mResourceList.get(str);
        AlcsCoAPContext alcsCoAPContext = mContextList.get(Long.valueOf(j));
        if (alcsCoAPResource != null && alcsCoAPResource.getHandler() != null) {
            alcsCoAPResource.getHandler().onRecRequest(alcsCoAPContext, alcsCoAPRequest);
        } else {
            ALog.e(TAG, "onRecvRequestHandler callback error null");
        }
    }

    public static void onSendRequestComplete(long j, long j2, String str, int i, int i2, AlcsCoAPResponse alcsCoAPResponse) {
        InetAddress byName;
        ALog.d(TAG, "onSendRequestComplete contextId:" + j + " msgId:" + j2 + " Ip:" + str + " port:" + i + " result:" + i2 + " response:" + alcsCoAPResponse);
        try {
            byName = InetAddress.getByName(str);
        } catch (Exception e) {
            e.printStackTrace();
            byName = null;
        }
        if (alcsCoAPResponse != null) {
            alcsCoAPResponse.setSource(byName);
            alcsCoAPResponse.setSourcePort(i);
        }
        AlcsCoAPContext alcsCoAPContext = mContextList.get(Long.valueOf(j));
        IAlcsCoAPReqHandler requestCallback = getRequestCallback(j, j2);
        if (requestCallback != null) {
            requestCallback.onReqComplete(alcsCoAPContext, i2, alcsCoAPResponse);
        } else {
            ALog.e(TAG, "onSendRequestComplete callback error null");
        }
    }

    public static void onClientAuthComplete(long j, String str, int i, int i2, int i3) {
        ALog.d(TAG, "onClientAuthComplete contextId:" + j + " Ip:" + str + " port:" + i + " result:" + i3 + " userdata:" + i2);
        IAuthHandler iAuthHandler = mAuthHandlerList.get(i2);
        if (iAuthHandler != null) {
            iAuthHandler.onAuthResult(str, i, i3);
        } else {
            ALog.e(TAG, "onClientAuthComplete error handler not found");
        }
        mAuthHandlerList.remove(i2);
    }

    public void setLogLevel(int i) {
        ALog.d(TAG, "setNativeLogLevel logLevel:" + i);
        setLogLevelNative(i);
    }

    public static void setLogLevelEx(int i) {
        ALog.d(TAG, "setNativeLogLevel logLevel:" + i);
        setLogLevelNative(i);
    }

    protected void initOptionSet(long j, long j2, OptionSet optionSet) {
        List<Option> listAsSortedList;
        if (optionSet == null || (listAsSortedList = optionSet.asSortedList()) == null || listAsSortedList.isEmpty()) {
            return;
        }
        for (Option option : listAsSortedList) {
            if (TextUtils.isEmpty(option.getStringValue())) {
                ALog.e(TAG, "initOptionSet stringvalue empty");
            } else {
                addStringOption(j, j2, option.getNumber(), option.getStringValue());
            }
        }
    }

    protected void putRequestCallback(long j, long j2, IAlcsCoAPReqHandler iAlcsCoAPReqHandler) {
        ALog.d(TAG, "putRequestCallback coapContextId: " + j + " msgId:" + j2);
        Map<Long, IAlcsCoAPReqHandler> map = mReqHandlerList.get(Long.valueOf(j));
        if (map == null) {
            map = new HashMap<>();
            mReqHandlerList.put(Long.valueOf(j), map);
        }
        map.put(Long.valueOf(j2), iAlcsCoAPReqHandler);
    }

    protected static IAlcsCoAPReqHandler getRequestCallback(long j, long j2) {
        ALog.d(TAG, "getRequestCallback coapContextId: " + j + " msgId:" + j2);
        Map<Long, IAlcsCoAPReqHandler> map = mReqHandlerList.get(Long.valueOf(j));
        if (map == null) {
            ALog.e(TAG, "getRequestCallback not find context");
            return null;
        }
        return map.get(Long.valueOf(j2));
    }
}
