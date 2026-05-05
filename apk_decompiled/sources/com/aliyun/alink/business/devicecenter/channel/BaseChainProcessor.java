package com.aliyun.alink.business.devicecenter.channel;

import com.aliyun.alink.business.devicecenter.log.ALog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BaseChainProcessor<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Object f3422a = new Object();
    public ArrayList<T> chainList;

    public BaseChainProcessor() {
        this.chainList = null;
        this.chainList = new ArrayList<>();
    }

    public void addChain(T t) {
        ALog.d("BaseChainProcessor", "addChain chain=" + t);
        synchronized (this.f3422a) {
            if (!this.chainList.contains(t)) {
                this.chainList.add(t);
            }
        }
    }

    public void addChainList(List<T> list) {
        ALog.d("BaseChainProcessor", "addChainList chain=" + list);
        synchronized (this.f3422a) {
            this.chainList.clear();
            if (list == null) {
                return;
            }
            this.chainList.addAll(list);
        }
    }

    public void clearChain() {
        synchronized (this.f3422a) {
            this.chainList.clear();
        }
    }

    public boolean contains(T t) {
        synchronized (this.f3422a) {
            if (this.chainList != null && this.chainList.size() >= 1) {
                return this.chainList.contains(t);
            }
            return false;
        }
    }

    public T getChainItem(int i) {
        T t;
        if (i <= -1 || i >= getChainSize()) {
            return null;
        }
        synchronized (this.f3422a) {
            t = this.chainList.get(i);
        }
        return t;
    }

    public int getChainSize() {
        int size;
        synchronized (this.f3422a) {
            size = this.chainList.size();
        }
        return size;
    }

    public void removeChain(T t) {
        ALog.d("BaseChainProcessor", "removeChain chain=" + t);
        synchronized (this.f3422a) {
            this.chainList.remove(t);
        }
    }

    public void removeChainItem(int i) {
        ALog.d("BaseChainProcessor", "removeChainItem index=" + i);
        synchronized (this.f3422a) {
            this.chainList.remove(i);
        }
    }
}
