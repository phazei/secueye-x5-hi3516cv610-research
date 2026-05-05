package com.aliyun.alink.linksdk.tmp.timing;

import android.text.TextUtils;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceTimerAttributeModel {
    private long time;
    private List<ValueItem> value;

    public static final class ValueItem {
        public String A;
        public int E;
        public String N;
        public int R;
        public int S;
        public String T;
        public int Y;
        public int Z;

        public boolean checkIsSet() {
            return (TextUtils.isEmpty(this.T) || this.T.length() == 0) ? false : true;
        }

        public ValueItem() {
            this.Y = 0;
            this.E = 0;
        }

        public ValueItem(ValueItem valueItem) {
            this.A = valueItem.A;
            this.R = valueItem.R;
            this.S = valueItem.S;
            this.T = valueItem.T;
            this.E = valueItem.E;
            this.Y = valueItem.Y;
            this.Z = valueItem.Z;
            this.N = valueItem.N;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ValueItem valueItem = (ValueItem) obj;
            return this.R == valueItem.R && this.S == valueItem.S && this.E == valueItem.E && this.Y == valueItem.Y && this.Z == valueItem.Z && Objects.equals(this.A, valueItem.A) && Objects.equals(this.T, valueItem.T) && Objects.equals(this.N, valueItem.N);
        }

        public int hashCode() {
            return Objects.hash(this.A, Integer.valueOf(this.R), Integer.valueOf(this.S), this.T, Integer.valueOf(this.E), Integer.valueOf(this.Y), Integer.valueOf(this.Z), this.N);
        }
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public List<ValueItem> getValue() {
        return this.value;
    }

    public void setValue(List<ValueItem> list) {
        this.value = list;
    }
}
