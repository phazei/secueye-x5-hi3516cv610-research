package com.aliyun.alink.linksdk.tmp.devicemodel.specs;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes2.dex */
public class MetaSpec {
    private String length;
    private String max;
    private String min;

    @SerializedName("1")
    private String one;
    private String precise;
    private String step;
    private String unit;

    @SerializedName("0")
    private String zero;

    public String getMin() {
        return this.min;
    }

    public void setMin(String str) {
        this.min = str;
    }

    public String getMax() {
        return this.max;
    }

    public void setMax(String str) {
        this.max = str;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String str) {
        this.unit = str;
    }

    public String getLength() {
        return this.length;
    }

    public void setLength(String str) {
        this.length = str;
    }

    public String getZero() {
        return this.zero;
    }

    public void setZero(String str) {
        this.zero = str;
    }

    public String getOne() {
        return this.one;
    }

    public void setOne(String str) {
        this.one = str;
    }

    public String getStep() {
        return this.step;
    }

    public void setStep(String str) {
        this.step = str;
    }
}
