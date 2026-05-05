package com.aliyun.iot.aep.sdk.framework.network;

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
import com.aliyun.iot.aep.sdk.log.ALog;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkConnectiveManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f4707a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private BroadcastReceiver f4708b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Set<INetworkChangeListener> f4709c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private ConnectivityManager.NetworkCallback f4710d;
    private AtomicBoolean e;

    public interface INetworkChangeListener {
        void onNetworkStateChange(NetworkInfo networkInfo, Network network);
    }

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final NetworkConnectiveManager f4713a = new NetworkConnectiveManager();
    }

    public static NetworkConnectiveManager getInstance() {
        return a.f4713a;
    }

    private NetworkConnectiveManager() {
        this.f4707a = null;
        this.f4708b = null;
        this.f4709c = null;
        this.e = new AtomicBoolean(false);
        this.f4709c = Collections.synchronizedSet(new HashSet());
        if (a()) {
            ALog.d("NetworkConnectiveManager", "use request network.");
            this.f4710d = new ConnectivityManager.NetworkCallback() { // from class: com.aliyun.iot.aep.sdk.framework.network.NetworkConnectiveManager.1
                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    ALog.i("NetworkConnectiveManager", "onAvailable");
                    ConnectivityManager connectivityManagerE = NetworkConnectiveManager.this.e();
                    if (connectivityManagerE != null) {
                        NetworkConnectiveManager.this.a(connectivityManagerE.getNetworkInfo(network), network);
                    }
                }

                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onLost(Network network) {
                    super.onLost(network);
                    ALog.i("NetworkConnectiveManager", "onLost");
                    ConnectivityManager connectivityManagerE = NetworkConnectiveManager.this.e();
                    if (connectivityManagerE != null) {
                        NetworkConnectiveManager.this.a(connectivityManagerE.getNetworkInfo(network), network);
                    }
                }
            };
        } else {
            ALog.d("NetworkConnectiveManager", "use broadcast receiver.");
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
        this.f4707a = context.getApplicationContext();
        if (!a()) {
            f();
            return;
        }
        try {
            c();
        } catch (Exception e) {
            ALog.w("NetworkConnectiveManager", "initNetworkConnectiveManager exception = " + e);
            d();
            f();
        }
    }

    private boolean a() {
        return Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT != 23;
    }

    public void deinitNetworkConnectiveManager() {
        ALog.d("NetworkConnectiveManager", "deinitNetworkConnectiveManager() called");
        g();
        d();
        h();
        this.e.set(false);
    }

    private void b() {
        ALog.d("NetworkConnectiveManager", "initBR() called");
        this.f4708b = new BroadcastReceiver() { // from class: com.aliyun.iot.aep.sdk.framework.network.NetworkConnectiveManager.2
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
                    NetworkConnectiveManager.this.a(networkInfo, null);
                }
            }
        };
    }

    private void c() {
        ConnectivityManager connectivityManagerE;
        ALog.d("NetworkConnectiveManager", "initRN() called version=" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 21 || (connectivityManagerE = e()) == null) {
            return;
        }
        connectivityManagerE.requestNetwork(new NetworkRequest.Builder().build(), this.f4710d);
    }

    private void d() {
        ConnectivityManager connectivityManagerE;
        ALog.d("NetworkConnectiveManager", "deinitRN() called");
        try {
            if (Build.VERSION.SDK_INT < 21 || (connectivityManagerE = e()) == null) {
                return;
            }
            connectivityManagerE.unregisterNetworkCallback(this.f4710d);
        } catch (Exception e) {
            ALog.d("NetworkConnectiveManager", "deinitRN exception=" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ConnectivityManager e() {
        Context context = this.f4707a;
        if (context != null) {
            return (ConnectivityManager) context.getSystemService("connectivity");
        }
        return null;
    }

    private void f() {
        if (this.f4707a == null) {
            ALog.w("NetworkConnectiveManager", "registerReceiver failed, context=null.");
            return;
        }
        ALog.d("NetworkConnectiveManager", "registerReceiver() called.");
        b();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        this.f4707a.registerReceiver(this.f4708b, intentFilter);
    }

    private void g() {
        try {
            if (this.f4708b == null || this.f4707a == null) {
                return;
            }
            this.f4707a.unregisterReceiver(this.f4708b);
            this.f4708b = null;
        } catch (Exception e) {
            ALog.w("NetworkConnectiveManager", "unregisterAPBroadcast exception=" + e);
        }
    }

    protected void a(NetworkInfo networkInfo, Network network) {
        ALog.d("NetworkConnectiveManager", "dispatch() called with: networkInfo = [" + networkInfo + "], network = [" + network + "]");
        Set<INetworkChangeListener> set = this.f4709c;
        if (set == null) {
            ALog.w("NetworkConnectiveManager", "dispatch network state change listener is empty.");
            return;
        }
        synchronized (set) {
            for (INetworkChangeListener iNetworkChangeListener : this.f4709c) {
                if (iNetworkChangeListener != null) {
                    ALog.d("NetworkConnectiveManager", "dispatch network state change -> " + iNetworkChangeListener);
                    iNetworkChangeListener.onNetworkStateChange(networkInfo, network);
                }
            }
        }
    }

    public void registerConnectiveListener(INetworkChangeListener iNetworkChangeListener) {
        ALog.d("NetworkConnectiveManager", "registerConnectiveListener() called with: listener = [" + iNetworkChangeListener + "]");
        Set<INetworkChangeListener> set = this.f4709c;
        if (set != null) {
            set.add(iNetworkChangeListener);
        }
    }

    public void unregisterConnectiveListener(INetworkChangeListener iNetworkChangeListener) {
        ALog.d("NetworkConnectiveManager", "unregisterConnectiveListener() called with: listener = [" + iNetworkChangeListener + "]");
        Set<INetworkChangeListener> set = this.f4709c;
        if (set != null) {
            set.remove(iNetworkChangeListener);
        }
    }

    private void h() {
        ALog.d("NetworkConnectiveManager", "clearListener() called");
        Set<INetworkChangeListener> set = this.f4709c;
        if (set != null) {
            set.clear();
        }
    }
}
