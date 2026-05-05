package anetwork.channel.stat;

import anetwork.channel.statist.StatisticData;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface INetworkStat {
    String get(String str);

    void put(String str, StatisticData statisticData);

    void reset(String str);
}
