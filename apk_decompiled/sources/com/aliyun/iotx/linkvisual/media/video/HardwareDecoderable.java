package com.aliyun.iotx.linkvisual.media.video;

/* JADX INFO: loaded from: classes2.dex */
public interface HardwareDecoderable {

    public enum DecoderStrategy {
        HARDWARE_FIRST(0),
        FORCE_SOFTWARE(1);


        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private int f5071lvdo;

        DecoderStrategy(int i) {
            this.f5071lvdo = i;
        }

        public static DecoderStrategy parseInt(int i) {
            for (DecoderStrategy decoderStrategy : values()) {
                if (decoderStrategy.f5071lvdo == i) {
                    return decoderStrategy;
                }
            }
            return null;
        }

        public int getValue() {
            return this.f5071lvdo;
        }
    }

    public enum DecoderType {
        HARDWARE(0),
        SOFTWARE(1);


        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private int f5072lvdo;

        DecoderType(int i) {
            this.f5072lvdo = i;
        }

        public static DecoderType parseInt(int i) {
            for (DecoderType decoderType : values()) {
                if (decoderType.f5072lvdo == i) {
                    return decoderType;
                }
            }
            return null;
        }

        public int getValue() {
            return this.f5072lvdo;
        }
    }

    DecoderType getDecoderType();

    void setDecoderStrategy(DecoderStrategy decoderStrategy);
}
