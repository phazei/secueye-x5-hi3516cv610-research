package com.aliyun.iotx.linkvisual.media.video.beans;

/* JADX INFO: loaded from: classes2.dex */
public class FrameColor {
    public int brightness;
    public int contrast;
    public int hue;
    public int saturation;

    public FrameColor() {
    }

    public FrameColor(int i, int i2, int i3, int i4) {
        this.brightness = i;
        this.contrast = i2;
        this.saturation = i3;
        this.hue = i4;
    }

    public int getBrightness() {
        return this.brightness;
    }

    public int getContrast() {
        return this.contrast;
    }

    public int getHue() {
        return this.hue;
    }

    public int getSaturation() {
        return this.saturation;
    }

    public void setBrightness(int i) {
        this.brightness = i;
    }

    public void setContrast(int i) {
        this.contrast = i;
    }

    public void setHue(int i) {
        this.hue = i;
    }

    public void setSaturation(int i) {
        this.saturation = i;
    }
}
