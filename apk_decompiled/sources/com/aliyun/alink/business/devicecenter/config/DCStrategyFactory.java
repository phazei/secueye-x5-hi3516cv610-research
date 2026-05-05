package com.aliyun.alink.business.devicecenter.config;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.plugin.ProvisionPlugin;

/* JADX INFO: loaded from: classes.dex */
public class DCStrategyFactory implements IStrategyFactory {
    @Override // com.aliyun.alink.business.devicecenter.config.IStrategyFactory
    public IConfigStrategy createStrategy(Context context, LinkType linkType) {
        ALog.d("DCStrategyFactory", "createStrategy() called with: linkType = [" + linkType + "], context = [" + context + "]");
        if (linkType == null || linkType == LinkType.ALI_DEFAULT) {
            linkType = LinkType.ALI_BROADCAST;
        }
        return ProvisionPlugin.getInstance().createStrategy(DeviceCenterBiz.getInstance().getAppContext(), linkType);
    }
}
