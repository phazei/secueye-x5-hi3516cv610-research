package com.aliyun.iot.link.ui.component.wheelview.source;

import java.lang.Number;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class NumberDataHolder<T extends Number> implements DataHolder<T> {
    private boolean includeLast;
    private Number max;
    private Number min;
    private int size;
    private Number step;

    public NumberDataHolder(Number number, Number number2, Number number3, boolean z) {
        this.min = number;
        this.max = number2;
        this.step = number3;
        this.includeLast = z;
        if (number.getClass() != number2.getClass() || number3.getClass() != number.getClass() || number3.getClass() != number2.getClass()) {
            throw new IllegalArgumentException("The parameter type must be the same");
        }
        if (number instanceof Integer) {
            this.size = ((number2.intValue() - number.intValue()) / number3.intValue()) + 1;
            if (!z || (number2.intValue() - number.intValue()) % number3.intValue() <= 0) {
                return;
            }
            this.size++;
            return;
        }
        if (number instanceof Float) {
            this.size = (int) (((number2.floatValue() - number.floatValue()) / number3.floatValue()) + 1.0f);
            if (z) {
                float fFloatValue = number2.floatValue();
                float fFloatValue2 = number.floatValue();
                float fFloatValue3 = number3.floatValue();
                int i = this.size;
                if (fFloatValue > fFloatValue2 + (fFloatValue3 * i)) {
                    this.size = i + 1;
                    return;
                }
                return;
            }
            return;
        }
        if (number instanceof Double) {
            this.size = (int) (((number2.doubleValue() - number.doubleValue()) / number3.doubleValue()) + 1.0d);
            if (z) {
                double dDoubleValue = number2.doubleValue();
                double dDoubleValue2 = number.doubleValue();
                double dDoubleValue3 = number3.doubleValue();
                int i2 = this.size;
                if (dDoubleValue > dDoubleValue2 + (dDoubleValue3 * ((double) i2))) {
                    this.size = i2 + 1;
                    return;
                }
                return;
            }
            return;
        }
        throw new UnsupportedOperationException("parameter type not supported");
    }

    @Override // com.aliyun.iot.link.ui.component.wheelview.source.DataHolder
    public int size() {
        return this.size;
    }

    @Override // com.aliyun.iot.link.ui.component.wheelview.source.DataHolder
    public T get(int i) {
        Number number = this.min;
        if (number instanceof Integer) {
            Integer numValueOf = Integer.valueOf(number.intValue() + (this.step.intValue() * i));
            return numValueOf.intValue() > this.max.intValue() ? Integer.valueOf(this.max.intValue()) : numValueOf;
        }
        if (number instanceof Float) {
            Float fValueOf = Float.valueOf(number.floatValue() + (this.step.floatValue() * i));
            return fValueOf.floatValue() > this.max.floatValue() ? Float.valueOf(this.max.floatValue()) : fValueOf;
        }
        if (!(number instanceof Double)) {
            return null;
        }
        Double dValueOf = Double.valueOf(number.doubleValue() + (this.step.doubleValue() * ((double) i)));
        return dValueOf.doubleValue() > this.max.doubleValue() ? Double.valueOf(this.max.doubleValue()) : dValueOf;
    }

    @Override // com.aliyun.iot.link.ui.component.wheelview.source.DataHolder
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // com.aliyun.iot.link.ui.component.wheelview.source.DataHolder
    public List<T> toList() {
        ArrayList arrayList = new ArrayList();
        Number number = this.min;
        if (number instanceof Integer) {
            int iIntValue = number.intValue();
            while (iIntValue < this.max.intValue()) {
                arrayList.add(Integer.valueOf(iIntValue));
                iIntValue += this.step.intValue();
            }
            if (this.includeLast && this.size > arrayList.size()) {
                arrayList.add(Integer.valueOf(this.max.intValue()));
            }
        } else if (number instanceof Float) {
            float fFloatValue = number.floatValue();
            while (fFloatValue < this.max.floatValue()) {
                arrayList.add(Float.valueOf(fFloatValue));
                fFloatValue += this.step.floatValue();
            }
            if (this.includeLast && this.size > arrayList.size()) {
                arrayList.add(Float.valueOf(this.max.floatValue()));
            }
        } else if (number instanceof Double) {
            double dDoubleValue = number.doubleValue();
            while (dDoubleValue < this.max.doubleValue()) {
                arrayList.add(Double.valueOf(dDoubleValue));
                dDoubleValue += this.step.doubleValue();
            }
            if (this.includeLast && this.size > arrayList.size()) {
                arrayList.add(Double.valueOf(this.max.doubleValue()));
            }
        }
        return arrayList;
    }
}
