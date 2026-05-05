package tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import enums.NetworkStateEnum;

/* JADX INFO: loaded from: classes4.dex */
public class NetWorkStateReceiver extends BroadcastReceiver {
    private NetworkStateEnum currentNetworkState;
    private NetWorkChangeListener netWorkChangeListener;

    public NetWorkStateReceiver(NetWorkChangeListener netWorkChangeListener) {
        this.netWorkChangeListener = netWorkChangeListener;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        NetworkStateEnum currentNetworkState = NetworkUtil.getCurrentNetworkState(context);
        if (this.netWorkChangeListener == null) {
            return;
        }
        try {
            if (currentNetworkState == NetworkStateEnum.NONE) {
                return;
            }
            this.netWorkChangeListener.stateChanged(currentNetworkState);
        } finally {
            this.currentNetworkState = currentNetworkState;
        }
    }
}
