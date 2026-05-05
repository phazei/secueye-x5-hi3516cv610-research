package com.aliyun.alink.business.devicecenter.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Parcelable;
import com.aliyun.alink.business.devicecenter.log.ALog;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkConnectiveManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f3767a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public BroadcastReceiver f3768b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public CopyOnWriteArrayList<INetworkChangeListener> f3769c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public ConnectivityManager.NetworkCallback f3770d;
    public AtomicBoolean e;

    public interface INetworkChangeListener {
        void onNetworkStateChange(NetworkInfo networkInfo, Network network);
    }

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final NetworkConnectiveManager f3773a = new NetworkConnectiveManager();
    }

    public static NetworkConnectiveManager getInstance() {
        return SingletonHolder.f3773a;
    }

    public final void b() {
        ALog.d("NetworkConnectiveManager", "clearListener() called");
        CopyOnWriteArrayList<INetworkChangeListener> copyOnWriteArrayList = this.f3769c;
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.clear();
        }
    }

    public final void c() {
        ConnectivityManager connectivityManagerD;
        ALog.d("NetworkConnectiveManager", "deinitRN() called");
        try {
            if (Build.VERSION.SDK_INT < 21 || (connectivityManagerD = d()) == null) {
                return;
            }
            connectivityManagerD.unregisterNetworkCallback(this.f3770d);
        } catch (Exception e) {
            ALog.d("NetworkConnectiveManager", "deinitRN exception=" + e);
        }
    }

    public final ConnectivityManager d() {
        Context context = this.f3767a;
        if (context != null) {
            return (ConnectivityManager) context.getSystemService("connectivity");
        }
        return null;
    }

    public void deinitNetworkConnectiveManager() {
        ALog.d("NetworkConnectiveManager", "deinitNetworkConnectiveManager() called");
        h();
        c();
        b();
        this.e.set(false);
    }

    public void dispatch(NetworkInfo networkInfo, Network network) {
        ALog.d("NetworkConnectiveManager", "dispatch() called with: networkInfo = [" + networkInfo + "], network = [" + network + "]");
        CopyOnWriteArrayList<INetworkChangeListener> copyOnWriteArrayList = this.f3769c;
        if (copyOnWriteArrayList == null) {
            ALog.w("NetworkConnectiveManager", "dispatch network state change listener is empty.");
            return;
        }
        int size = copyOnWriteArrayList.size();
        for (int i = 0; i < size; i++) {
            INetworkChangeListener iNetworkChangeListener = this.f3769c.get(i);
            if (iNetworkChangeListener != null) {
                iNetworkChangeListener.onNetworkStateChange(networkInfo, network);
            }
        }
    }

    public final void e() {
        ALog.d("NetworkConnectiveManager", "initBR() called");
        this.f3768b = new BroadcastReceiver() { // from class: com.aliyun.alink.business.devicecenter.utils.NetworkConnectiveManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (context == null || intent == null || intent.getAction() == null) {
                    return;
                }
                String action = intent.getAction();
                ALog.d("NetworkConnectiveManager", "connectBroadCastRecv, onReceive()" + action);
                if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                    Parcelable parcelableExtra = intent.getParcelableExtra("networkInfo");
                    if (!(parcelableExtra instanceof NetworkInfo)) {
                        ALog.e("NetworkConnectiveManager", "parcelableExtra network info is null.");
                        return;
                    }
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    ALog.i("NetworkConnectiveManager", "type:" + networkInfo.getType() + ", name:" + networkInfo.getTypeName() + ", subType=" + networkInfo.getSubtype() + ", subTypeName=" + networkInfo.getSubtypeName() + ", is connected=" + networkInfo.isConnected() + ", isAvailable=" + networkInfo.isAvailable());
                    NetworkConnectiveManager.this.dispatch(networkInfo, null);
                }
            }
        };
    }

    public final void f() {
        ConnectivityManager connectivityManagerD;
        ALog.d("NetworkConnectiveManager", "initRN() called version=" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 21 || (connectivityManagerD = d()) == null) {
            return;
        }
        connectivityManagerD.requestNetwork(new NetworkRequest.Builder().build(), this.f3770d);
    }

    public final void g() {
        if (this.f3767a == null) {
            ALog.w("NetworkConnectiveManager", "registerReceiver failed, context=null.");
            return;
        }
        ALog.d("NetworkConnectiveManager", "registerReceiver() called.");
        e();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        this.f3767a.registerReceiver(this.f3768b, intentFilter);
    }

    public final void h() {
        try {
            if (this.f3768b == null || this.f3767a == null) {
                return;
            }
            this.f3767a.unregisterReceiver(this.f3768b);
            this.f3768b = null;
        } catch (Exception e) {
            ALog.w("NetworkConnectiveManager", "unregisterAPBroadcast exception=" + e);
        }
    }

    public void initNetworkConnectiveManager(Context context) {
        ALog.d("NetworkConnectiveManager", "initNetworkConnectiveManager() called with: context = [" + context + "]");
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null.");
        }
        if (this.e.get()) {
            ALog.d("NetworkConnectiveManager", "initNetworkConnectiveManager has inited.");
        }
        this.e.set(true);
        this.f3767a = context.getApplicationContext();
        if (!a()) {
            g();
            return;
        }
        try {
            f();
        } catch (Exception e) {
            ALog.w("NetworkConnectiveManager", "initNetworkConnectiveManager exception = " + e);
            c();
            g();
        }
    }

    public void registerConnectiveListener(INetworkChangeListener iNetworkChangeListener) {
        CopyOnWriteArrayList<INetworkChangeListener> copyOnWriteArrayList;
        ALog.d("NetworkConnectiveManager", "registerConnectiveListener() called with: listener = [" + iNetworkChangeListener + "]");
        if (iNetworkChangeListener == null || (copyOnWriteArrayList = this.f3769c) == null || copyOnWriteArrayList.contains(iNetworkChangeListener)) {
            return;
        }
        this.f3769c.add(iNetworkChangeListener);
    }

    public void unregisterConnectiveListener(INetworkChangeListener iNetworkChangeListener) {
        CopyOnWriteArrayList<INetworkChangeListener> copyOnWriteArrayList;
        ALog.d("NetworkConnectiveManager", "unregisterConnectiveListener() called with: listener = [" + iNetworkChangeListener + "]");
        if (iNetworkChangeListener == null || (copyOnWriteArrayList = this.f3769c) == null) {
            return;
        }
        copyOnWriteArrayList.remove(iNetworkChangeListener);
    }

    public NetworkConnectiveManager() {
        this.f3767a = null;
        this.f3768b = null;
        this.f3769c = null;
        this.e = new AtomicBoolean(false);
        this.f3769c = new CopyOnWriteArrayList<>();
        if (!a()) {
            ALog.d("NetworkConnectiveManager", "use broadcast receiver.");
        } else {
            ALog.d("NetworkConnectiveManager", "use request network.");
            this.f3770d = new ConnectivityManager.NetworkCallback() { // from class: com.aliyun.alink.business.devicecenter.utils.NetworkConnectiveManager.1
                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    ALog.i("NetworkConnectiveManager", "onAvailable");
                    ConnectivityManager connectivityManagerD = NetworkConnectiveManager.this.d();
                    if (connectivityManagerD != null) {
                        NetworkConnectiveManager.this.dispatch(connectivityManagerD.getNetworkInfo(network), network);
                    }
                }

                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onLost(Network network) {
                    super.onLost(network);
                    ALog.i("NetworkConnectiveManager", "onLost");
                    ConnectivityManager connectivityManagerD = NetworkConnectiveManager.this.d();
                    if (connectivityManagerD != null) {
                        NetworkConnectiveManager.this.dispatch(connectivityManagerD.getNetworkInfo(network), network);
                    }
                }
            };
        }
    }

    public final boolean a() {
        int i = Build.VERSION.SDK_INT;
        return i >= 21 && i != 23;
    }
}
