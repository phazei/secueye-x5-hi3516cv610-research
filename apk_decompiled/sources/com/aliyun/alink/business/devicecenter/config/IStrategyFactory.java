package com.aliyun.alink.business.devicecenter.config;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;

/* JADX INFO: loaded from: classes.dex */
public interface IStrategyFactory {
    IConfigStrategy createStrategy(Context context, LinkType linkType);
}
