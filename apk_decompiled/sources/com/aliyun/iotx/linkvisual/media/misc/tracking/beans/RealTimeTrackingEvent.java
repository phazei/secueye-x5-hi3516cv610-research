package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

import com.alibaba.fastjson.JSON;

/* JADX INFO: loaded from: classes2.dex */
public class RealTimeTrackingEvent extends BaseEvent {
    private RealTimeTrackingParams params;

    public static final class Builder {
        private RealTimeTrackingParams params;

        private Builder() {
        }

        public RealTimeTrackingEvent build() {
            return new RealTimeTrackingEvent(this);
        }

        public Builder params(RealTimeTrackingParams realTimeTrackingParams) {
            this.params = realTimeTrackingParams;
            return this;
        }
    }

    public RealTimeTrackingEvent() {
        this.params = RealTimeTrackingParams.newBuilder().build();
    }

    private RealTimeTrackingEvent(Builder builder) {
        super("1.2", "realtimeTracking");
        setParams(builder.params);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static RealTimeTrackingEvent parseFromJSONString(String str) {
        return (RealTimeTrackingEvent) JSON.parseObject(str, RealTimeTrackingEvent.class);
    }

    public RealTimeTrackingParams getParams() {
        return this.params;
    }

    public void setParams(RealTimeTrackingParams realTimeTrackingParams) {
        this.params = realTimeTrackingParams;
    }
}
