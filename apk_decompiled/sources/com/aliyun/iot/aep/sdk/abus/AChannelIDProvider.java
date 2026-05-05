package com.aliyun.iot.aep.sdk.abus;

import com.aliyun.iot.aep.sdk.abus.utils.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class AChannelIDProvider implements IChannelIDProvider {
    private static final int BUS_CHANNELID_MAX_COUNT = 4095;
    private static final int BUS_CHANNELID_PRIVATE_SEED = 8192;
    private static final String TAG = "ChannelIDProvider";
    private int channelNo = 0;

    @Override // com.aliyun.iot.aep.sdk.abus.IChannelIDProvider
    public synchronized int generateChannelID() {
        int i = this.channelNo + 1;
        this.channelNo = i;
        if (4095 <= i) {
            ALog.w(TAG, "generateChannelID(): to much channel: " + this.channelNo);
        }
        return this.channelNo + 8192;
    }
}
