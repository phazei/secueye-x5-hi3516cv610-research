package com.aliyun.iotx.linkvisual.media.audio;

/* JADX INFO: loaded from: classes2.dex */
public enum LiveIntercomVoiceType {
    Original(0),
    Uncle(1);

    private int value;

    LiveIntercomVoiceType(int i) {
        this.value = i;
    }

    public static LiveIntercomVoiceType parseInt(int i) {
        for (LiveIntercomVoiceType liveIntercomVoiceType : values()) {
            if (liveIntercomVoiceType.value == i) {
                return liveIntercomVoiceType;
            }
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}
