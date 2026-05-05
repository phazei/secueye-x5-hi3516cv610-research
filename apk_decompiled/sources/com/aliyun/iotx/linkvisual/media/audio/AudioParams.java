package com.aliyun.iotx.linkvisual.media.audio;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import tools.G711Code;

/* JADX INFO: loaded from: classes2.dex */
public class AudioParams {
    public static final int TYPE_AAC_LC = 2;
    public static final int TYPE_AMRNB = 4;
    public static final int TYPE_G711A = 1;
    public static final int TYPE_G711U = 3;
    public static final int TYPE_PCM = 0;
    public int mAudioEncoding;
    public int mAudioType;
    public int mChannelCount;
    public int mSampleRate;
    public static final AudioParams AUDIOPARAM_MONO_16K_PCM = new AudioParams(G711Code.SAMPLE_RATE_INHZ_16000, 1, 2, 0);
    public static final AudioParams AUDIOPARAM_MONO_8K_PCM = new AudioParams(8000, 1, 2, 0);
    public static final AudioParams AUDIOPARAM_MONO_8K_AMRNB = new AudioParams(8000, 1, 2, 0);
    public static final AudioParams AUDIOPARAM_STEREO_8K_PCM = new AudioParams(8000, 2, 2, 0);
    public static final AudioParams AUDIOPARAM_MONO_16K_G711A = new AudioParams(G711Code.SAMPLE_RATE_INHZ_16000, 1, 2, 1);
    public static final AudioParams AUDIOPARAM_MONO_8K_G711A = new AudioParams(8000, 1, 2, 1);
    public static final AudioParams AUDIOPARAM_MONO_8K_G711U = new AudioParams(8000, 1, 2, 3);
    public static final AudioParams AUDIOPARAM_MONO_16K_G711U = new AudioParams(G711Code.SAMPLE_RATE_INHZ_16000, 1, 2, 3);
    public static final AudioParams AUDIOPARAM_STEREO_8K_G711U = new AudioParams(8000, 2, 2, 3);
    public static final AudioParams AUDIOPARAM_MONO_16K_AAC_LC = new AudioParams(G711Code.SAMPLE_RATE_INHZ_16000, 1, 2, 2);
    public static final AudioParams AUDIOPARAM_MONO_8K_AAC_LC = new AudioParams(8000, 1, 2, 2);
    public static final AudioParams AUDIOPARAM_STEREO_44K_AAC_LC = new AudioParams(G711Code.SAMPLE_RATE_INHZ, 2, 2, 2);
    public static final AudioParams AUDIOPARAM_STEREO_44K_PCM = new AudioParams(G711Code.SAMPLE_RATE_INHZ, 2, 2, 0);
    public static final AudioParams AUDIOPARAM_MONO_48K_G711A = new AudioParams(48000, 1, 2, 1);
    public static final AudioParams AUDIOPARAM_MONO_48K_G711U = new AudioParams(48000, 1, 2, 3);
    public static final AudioParams AUDIOPARAM_MONO_48K_AAC_LC = new AudioParams(48000, 1, 2, 2);

    public AudioParams(int i, int i2, int i3) {
        this.mSampleRate = i;
        this.mChannelCount = i2;
        this.mAudioType = i3;
    }

    public AudioParams(int i, int i2, int i3, int i4) {
        this.mSampleRate = i;
        this.mChannelCount = i2;
        this.mAudioEncoding = i3;
        this.mAudioType = i4;
    }

    public boolean checkSupport() {
        int i;
        int i2;
        return this.mChannelCount != 0 && this.mAudioEncoding != 0 && (i = this.mSampleRate) >= 8000 && i <= 48000 && (i2 = this.mAudioType) >= 0 && i2 <= 3;
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            AudioParams audioParams = (AudioParams) obj;
            if (this.mAudioEncoding == audioParams.getAudioEncoding() && this.mAudioType == audioParams.getAudioType() && this.mSampleRate == audioParams.getSampleRate() && this.mChannelCount == audioParams.getChannelCount()) {
                return true;
            }
        }
        return false;
    }

    public int getAudioEncoding() {
        return this.mAudioEncoding;
    }

    public int getAudioType() {
        return this.mAudioType;
    }

    public int getBitsPerSample() {
        return this.mAudioEncoding != 3 ? 16 : 8;
    }

    public int getChannelCount() {
        return this.mChannelCount;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public void setAudioEncoding(int i) {
        this.mAudioEncoding = i;
    }

    public void setAudioType(int i) {
        this.mAudioType = i;
    }

    public void setBitsPerSample(int i) {
        this.mAudioEncoding = i != 8 ? 2 : 3;
    }

    public void setChannelCount(int i) {
        this.mChannelCount = i;
    }

    public void setSampleRate(int i) {
        this.mSampleRate = i;
    }

    public String toString() {
        return "AudioEncoding:" + this.mAudioEncoding + SdkConstant.CLOUDAPI_LF + "AudioType:" + this.mAudioType + SdkConstant.CLOUDAPI_LF + "SampleRate:" + this.mSampleRate + SdkConstant.CLOUDAPI_LF + "ChannelCount:" + this.mChannelCount + SdkConstant.CLOUDAPI_LF;
    }
}
