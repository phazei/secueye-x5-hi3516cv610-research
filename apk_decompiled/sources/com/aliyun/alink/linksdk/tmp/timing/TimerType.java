package com.aliyun.alink.linksdk.tmp.timing;

/* JADX INFO: loaded from: classes2.dex */
public enum TimerType {
    TIMER_NONE(0),
    TIMER_COUNTDOWN(1),
    TIMER_NORMAL(2),
    TIMER_CIRCULATION(3);

    private int typeValue;

    TimerType(int i) {
        this.typeValue = i;
    }

    public int getTypeValue() {
        return this.typeValue;
    }

    public static TimerType fromValue(int i) {
        switch (i) {
            case 1:
                return TIMER_COUNTDOWN;
            case 2:
                return TIMER_NORMAL;
            case 3:
                return TIMER_CIRCULATION;
            default:
                return TIMER_NONE;
        }
    }
}
