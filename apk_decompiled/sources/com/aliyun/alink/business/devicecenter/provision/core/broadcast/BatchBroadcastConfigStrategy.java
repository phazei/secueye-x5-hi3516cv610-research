package com.aliyun.alink.business.devicecenter.provision.core.broadcast;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_BROADCAST_IN_BATCHES)
public class BatchBroadcastConfigStrategy extends AlinkBroadcastConfigStrategy {
    public BatchBroadcastConfigStrategy() {
    }

    @Override // com.aliyun.alink.business.devicecenter.provision.core.broadcast.AlinkBroadcastConfigStrategy, com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_BROADCAST_IN_BATCHES.getName();
    }

    public BatchBroadcastConfigStrategy(Context context) {
        super(context);
    }
}
