package com.aliyun.iotx.linkvisual.media.video.processing;

import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.video.beans.Yuv420pFrame;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultVideoFrameProcessor implements IVideoFrameProcessor {
    public static final String TAG = "DefaultVideoFrameProcessor";

    @Override // com.aliyun.iotx.linkvisual.media.video.processing.IVideoFrameProcessor
    public boolean processing(Yuv420pFrame yuv420pFrame) {
        ALog.d(TAG, "using DefaultVideoFrameProcessor to processing video frame, nothing happen.");
        return true;
    }
}
