package anetwork.channel;

import anetwork.channel.statist.StatisticData;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class NetworkEvent {

    /* JADX INFO: compiled from: Taobao */
    public interface FinishEvent {
        String getDesc();

        int getHttpCode();

        StatisticData getStatisticData();
    }

    /* JADX INFO: compiled from: Taobao */
    public interface ProgressEvent {
        byte[] getBytedata();

        String getDesc();

        int getIndex();

        int getSize();

        int getTotal();
    }
}
