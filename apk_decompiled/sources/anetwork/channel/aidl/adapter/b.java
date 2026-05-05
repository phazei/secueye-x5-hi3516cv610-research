package anetwork.channel.aidl.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.util.ALog;
import anetwork.channel.Network;
import anetwork.channel.NetworkListener;
import anetwork.channel.Request;
import anetwork.channel.Response;
import anetwork.channel.aidl.Connection;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.aidl.IRemoteNetworkGetter;
import anetwork.channel.aidl.NetworkResponse;
import anetwork.channel.aidl.ParcelableRequest;
import anetwork.channel.aidl.RemoteNetwork;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.degrade.DegradableNetworkDelegate;
import anetwork.channel.http.HttpNetworkDelegate;
import anetwork.channel.util.RequestConstant;
import com.aliyun.alink.linksdk.alcs.coap.resources.LinkFormat;
import java.util.concurrent.Future;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class b implements Network {
    protected static final int DEGRADE = 1;
    protected static final int HTTP = 0;
    protected static String TAG = "anet.NetworkProxy";
    private Context mContext;
    private volatile RemoteNetwork mDelegate = null;
    private int mType;

    public b(Context context, int i) {
        this.mType = 0;
        this.mContext = context;
        this.mType = i;
    }

    @Override // anetwork.channel.Network
    public Connection getConnection(Request request, Object obj) {
        ALog.i(TAG, "networkProxy getConnection", request.getSeqNo(), new Object[0]);
        recordRequestStat(request);
        initDelegateInstance(true);
        ParcelableRequest parcelableRequest = new ParcelableRequest(request);
        if (parcelableRequest.url == null) {
            return new ConnectionDelegate(-102);
        }
        try {
            return this.mDelegate.getConnection(parcelableRequest);
        } catch (Throwable th) {
            reportRemoteError(th, "[getConnection]call getConnection method failed.");
            return new ConnectionDelegate(-103);
        }
    }

    @Override // anetwork.channel.Network
    public Response syncSend(Request request, Object obj) {
        ALog.i(TAG, "networkProxy syncSend", request.getSeqNo(), new Object[0]);
        recordRequestStat(request);
        initDelegateInstance(true);
        ParcelableRequest parcelableRequest = new ParcelableRequest(request);
        if (parcelableRequest.url == null) {
            return new NetworkResponse(-102);
        }
        try {
            return this.mDelegate.syncSend(parcelableRequest);
        } catch (Throwable th) {
            reportRemoteError(th, "[syncSend]call syncSend method failed.");
            return new NetworkResponse(-103);
        }
    }

    private void initDelegateInstance(boolean z) {
        if (this.mDelegate != null) {
            return;
        }
        if (NetworkConfigCenter.isRemoteNetworkServiceEnable()) {
            boolean zIsTargetProcess = GlobalAppRuntimeInfo.isTargetProcess();
            if (NetworkConfigCenter.isBindServiceOptimize() && zIsTargetProcess) {
                d.a(this.mContext, false);
                if (d.f1988c && this.mDelegate == null) {
                    this.mDelegate = this.mType == 1 ? new DegradableNetworkDelegate(this.mContext) : new HttpNetworkDelegate(this.mContext);
                    ALog.i(TAG, "[initDelegateInstance] getNetworkInstance when binding service", null, new Object[0]);
                    return;
                } else {
                    tryGetRemoteNetworkInstance(this.mType);
                    if (this.mDelegate != null) {
                        return;
                    }
                }
            } else {
                d.a(this.mContext, z);
                tryGetRemoteNetworkInstance(this.mType);
                if (this.mDelegate != null) {
                    return;
                }
            }
            if (NetworkConfigCenter.isAllowSpdyWhenBindServiceFailed() && zIsTargetProcess && d.f1987b) {
                synchronized (this) {
                    if (this.mDelegate == null) {
                        this.mDelegate = this.mType == 1 ? new DegradableNetworkDelegate(this.mContext) : new HttpNetworkDelegate(this.mContext);
                        ALog.e(TAG, "[initDelegateInstance] getNetworkInstance when bindService failed.", null, new Object[0]);
                        return;
                    }
                }
            }
        }
        synchronized (this) {
            if (this.mDelegate == null) {
                if (ALog.isPrintLog(2)) {
                    ALog.i(TAG, "[getLocalNetworkInstance]", null, new Object[0]);
                }
                this.mDelegate = new HttpNetworkDelegate(this.mContext);
            }
        }
    }

    @Override // anetwork.channel.Network
    public Future<Response> asyncSend(Request request, Object obj, Handler handler, NetworkListener networkListener) {
        ALog.i(TAG, "networkProxy asyncSend", request.getSeqNo(), new Object[0]);
        recordRequestStat(request);
        initDelegateInstance(Looper.myLooper() != Looper.getMainLooper());
        ParcelableRequest parcelableRequest = new ParcelableRequest(request);
        ParcelableNetworkListenerWrapper parcelableNetworkListenerWrapper = (networkListener == null && handler == null) ? null : new ParcelableNetworkListenerWrapper(networkListener, handler, obj);
        if (parcelableRequest.url == null) {
            if (parcelableNetworkListenerWrapper != null) {
                try {
                    parcelableNetworkListenerWrapper.onFinished(new DefaultFinishEvent(-102));
                } catch (RemoteException unused) {
                }
            }
            return new a(new NetworkResponse(-102));
        }
        try {
            return new a(this.mDelegate.asyncSend(parcelableRequest, parcelableNetworkListenerWrapper));
        } catch (Throwable th) {
            if (parcelableNetworkListenerWrapper != null) {
                try {
                    parcelableNetworkListenerWrapper.onFinished(new DefaultFinishEvent(-102));
                } catch (RemoteException unused2) {
                }
            }
            reportRemoteError(th, "[asyncSend]call asyncSend exception");
            return new a(new NetworkResponse(-103));
        }
    }

    private synchronized void tryGetRemoteNetworkInstance(int i) {
        if (this.mDelegate != null) {
            return;
        }
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "[tryGetRemoteNetworkInstance] type=" + i, null, new Object[0]);
        }
        IRemoteNetworkGetter iRemoteNetworkGetterA = d.a();
        if (iRemoteNetworkGetterA != null) {
            try {
                this.mDelegate = iRemoteNetworkGetterA.get(i);
            } catch (Throwable th) {
                reportRemoteError(th, "[tryGetRemoteNetworkInstance]get RemoteNetwork Delegate failed.");
            }
        }
    }

    private void reportRemoteError(Throwable th, String str) {
        ALog.e(TAG, null, str, th, new Object[0]);
        ExceptionStatistic exceptionStatistic = new ExceptionStatistic(-103, null, LinkFormat.RESOURCE_TYPE);
        exceptionStatistic.exceptionStack = th.toString();
        AppMonitor.getInstance().commitStat(exceptionStatistic);
    }

    private void recordRequestStat(Request request) {
        if (request == null) {
            return;
        }
        request.setExtProperty(RequestConstant.KEY_REQ_START, String.valueOf(System.currentTimeMillis()));
        String extProperty = request.getExtProperty(RequestConstant.KEY_TRACE_ID);
        if (TextUtils.isEmpty(extProperty)) {
            extProperty = anet.channel.fulltrace.a.a().createRequest();
        }
        request.setExtProperty(RequestConstant.KEY_TRACE_ID, extProperty);
        request.setExtProperty(RequestConstant.KEY_REQ_PROCESS, GlobalAppRuntimeInfo.getCurrentProcess());
    }
}
