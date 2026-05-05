package com.alibaba.sdk.android.openaccount;

import com.xiaomi.mipush.sdk.Constants;

/* JADX INFO: loaded from: classes.dex */
public class Version {
    private int major;
    private int micro;
    private int minor;

    public Version(int i, int i2, int i3) {
        this.major = i;
        this.minor = i2;
        this.micro = i3;
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public int getMicro() {
        return this.micro;
    }

    void setVersion(String str) {
        if (str == null) {
            throw new IllegalStateException("null version");
        }
        String[] strArrSplit = str.split("[.]");
        if (strArrSplit.length != 3) {
            throw new IllegalArgumentException(str + " is not a valid version");
        }
        this.major = Integer.parseInt(strArrSplit[0]);
        this.minor = Integer.parseInt(strArrSplit[1]);
        int iIndexOf = strArrSplit[2].indexOf(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
        if (iIndexOf != -1) {
            this.micro = Integer.parseInt(strArrSplit[2].substring(0, iIndexOf));
        } else {
            this.micro = Integer.parseInt(strArrSplit[2]);
        }
    }

    public int hashCode() {
        return ((((this.major + 31) * 31) + this.micro) * 31) + this.minor;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Version version = (Version) obj;
        return this.major == version.major && this.micro == version.micro && this.minor == version.minor;
    }

    public String toString() {
        return this.major + "." + this.minor + "." + this.micro;
    }
}
