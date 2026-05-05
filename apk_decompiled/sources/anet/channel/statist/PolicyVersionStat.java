package anet.channel.statist;

import anet.channel.status.NetworkStatusHelper;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
@Monitor(module = "networkPrefer", monitorPoint = "policyVersion")
public class PolicyVersionStat extends StatObject {

    @Dimension
    public String host;

    @Dimension
    public int reportType;

    @Dimension
    public int version;

    @Dimension
    public String netType = NetworkStatusHelper.getNetworkSubType();

    @Dimension
    public String mnc = NetworkStatusHelper.getSimOp();

    public PolicyVersionStat(String str, int i) {
        this.host = str;
        this.version = i;
    }
}
