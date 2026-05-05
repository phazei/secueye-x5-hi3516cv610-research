package anet.channel.status;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import anet.channel.util.ALog;
import java.util.ArrayList;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class c extends ConnectivityManager.NetworkCallback {
    c() {
    }

    @Override // android.net.ConnectivityManager.NetworkCallback
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
        b.l = new ArrayList(linkProperties.getDnsServers());
    }

    @Override // android.net.ConnectivityManager.NetworkCallback
    public void onLost(Network network) {
        super.onLost(network);
        ALog.i("awcn.NetworkStatusMonitor", "network onLost", null, new Object[0]);
        b.f1834b = false;
    }

    @Override // android.net.ConnectivityManager.NetworkCallback
    public void onAvailable(Network network) {
        super.onAvailable(network);
        ALog.i("awcn.NetworkStatusMonitor", "network onAvailable", null, new Object[0]);
        b.f1834b = true;
    }
}
