package anet.channel.statist;

import anet.channel.status.NetworkStatusHelper;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
@Monitor(module = "networkPrefer", monitorPoint = "mtuDetect")
public class MtuDetectStat extends StatObject {

    @Dimension
    public int errCode;

    @Dimension
    public int mtu;

    @Dimension
    public int pingSuccessCount;

    @Dimension
    public int pingTimeoutCount;

    @Dimension
    public String rtt;

    @Dimension
    public String nettype = NetworkStatusHelper.getNetworkSubType();

    @Dimension
    public String mnc = NetworkStatusHelper.getSimOp();

    @Dimension
    public String bssid = NetworkStatusHelper.getWifiBSSID();
}
