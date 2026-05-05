package com.aliyun.iot.link.ui.component.wheelview.source;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ListDataHolder<T> implements DataHolder<T> {
    private List<T> mDataList;

    public ListDataHolder(List<T> list) {
        this.mDataList = list;
    }

    @Override // com.aliyun.iot.link.ui.component.wheelview.source.DataHolder
    public int size() {
        return this.mDataList.size();
    }

    @Override // com.aliyun.iot.link.ui.component.wheelview.source.DataHolder
    public T get(int i) {
        return this.mDataList.get(i);
    }

    @Override // com.aliyun.iot.link.ui.component.wheelview.source.DataHolder
    public boolean isEmpty() {
        return this.mDataList.isEmpty();
    }

    @Override // com.aliyun.iot.link.ui.component.wheelview.source.DataHolder
    public List<T> toList() {
        return this.mDataList;
    }
}
