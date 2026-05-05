package com.aliyun.alink.linksdk.tmp.config;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.c.d;
import com.aliyun.alink.linksdk.tmp.device.e.c;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IProvisionListener;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.resource.b;
import com.aliyun.alink.linksdk.tmp.resource.e;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class ProvisionReceiver extends c implements b {
    private static final String TAG = "[Tmp]ProvisionReceiver";
    private String mDeviceName;
    private boolean mIsStarted;
    private Map<Integer, IProvisionListener> mListenerList;
    private String mProductKey;

    private ProvisionReceiver() {
        this.mIsStarted = false;
        this.mListenerList = new ConcurrentHashMap();
    }

    public static ProvisionReceiver getInstance() {
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder {
        private static ProvisionReceiver mInstance = new ProvisionReceiver();

        private InstanceHolder() {
        }
    }

    public void init(DeviceConfig deviceConfig, Object obj, IDevListener iDevListener) {
        this.mConfig = deviceConfig;
        super.init(obj, iDevListener);
    }

    public void start(String str, String str2) {
        ALog.d(TAG, "start pk:" + str + " dn:" + str + " mIsStarted:" + this.mIsStarted);
        if (this.mIsStarted) {
            return;
        }
        this.mProductKey = str;
        this.mDeviceName = str2;
        e.a().a(this.mDeviceImpl.a(), TmpConstant.IDENTIFIER_SETUP, TextHelper.formatTopic(str, str2, TmpConstant.PATH_SETUP), true, (b) this);
        this.mIsStarted = true;
    }

    public void stop() {
        ALog.d(TAG, "stop pk:" + this.mProductKey + " dn:" + this.mDeviceName + " mIsStarted:" + this.mIsStarted);
        if (this.mIsStarted) {
            this.mIsStarted = false;
            e.a().a(this.mDeviceImpl.a(), TmpConstant.IDENTIFIER_SETUP, TextHelper.formatTopic(this.mProductKey, this.mDeviceName, TmpConstant.PATH_SETUP));
        }
    }

    public void addListener(IProvisionListener iProvisionListener) {
        if (iProvisionListener != null) {
            this.mListenerList.put(Integer.valueOf(iProvisionListener.hashCode()), iProvisionListener);
        }
    }

    public void removeListener(IProvisionListener iProvisionListener) {
        if (iProvisionListener != null) {
            this.mListenerList.remove(Integer.valueOf(iProvisionListener.hashCode()));
        }
    }

    private void onNotify(String str, String str2, Object obj, ITResResponseCallback iTResResponseCallback) {
        HashMap map = new HashMap(this.mListenerList);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            ((IProvisionListener) ((Map.Entry) it.next()).getValue()).onMsg(str, str2, obj, new d(iTResResponseCallback, map.size()));
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.resource.b
    public void onProcess(String str, String str2, Object obj, ITResResponseCallback iTResResponseCallback) {
        onNotify(str, str2, obj, iTResResponseCallback);
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onSuccess(Object obj, OutputParams outputParams) {
        ALog.d(TAG, "onSuccess returnValue:" + outputParams);
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onFail(Object obj, ErrorInfo errorInfo) {
        ALog.d(TAG, "onFail errorInfo:" + errorInfo);
    }
}
