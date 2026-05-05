package com.alibaba.ailabs.iot.mesh.grouping;

import com.alibaba.ailabs.iot.mesh.callback.ConfigActionListener;

/* JADX INFO: loaded from: classes.dex */
public interface IMeshGroupingService {
    void configModelSubscription(String str, String str2, String str3, ConfigActionListener<Boolean> configActionListener);
}
