package com.aliyun.alink.linksdk.tmp.timing;

/* JADX INFO: loaded from: classes2.dex */
public enum TimerEnableType {
    TIMER_ENABLE_KNOWN(-1),
    TIMER_ENABLE_NONE(0),
    TIMER_ENABLE(1),
    TIMER_IN_FLIGHT(2);

    private int typeValue;

    TimerEnableType(int i) {
        this.typeValue = i;
    }

    public int getTypeValue() {
        return this.typeValue;
    }

    public static TimerEnableType fromTypeValue(int i) {
        switch (i) {
            case 0:
                return TIMER_ENABLE_NONE;
            case 1:
                return TIMER_ENABLE;
            case 2:
                return TIMER_IN_FLIGHT;
            default:
                return TIMER_ENABLE_KNOWN;
        }
    }
}
