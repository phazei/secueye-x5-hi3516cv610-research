package com.aliyun.alink.h2.stream.api;

import com.aliyun.alink.h2.api.Profile;
import com.aliyun.alink.h2.stream.a.d;

/* JADX INFO: loaded from: classes2.dex */
public class StreamSenderFactory {
    public static IStreamSender streamSender(Profile profile) {
        return new d(profile);
    }
}
