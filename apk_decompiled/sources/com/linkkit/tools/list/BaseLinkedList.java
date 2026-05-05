package com.linkkit.tools.list;

import com.linkkit.tools.a;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes3.dex */
public class BaseLinkedList<T> {
    private static final String TAG = "BaseLinkedList";
    protected LinkedList<T> chainList;
    private final Object mListLock = new Object();

    public BaseLinkedList() {
        this.chainList = null;
        this.chainList = new LinkedList<>();
    }

    public void add(T t) {
        a.a(TAG, "addChain chain=" + t);
        synchronized (this.mListLock) {
            if (!this.chainList.contains(t)) {
                this.chainList.addFirst(t);
            }
        }
    }

    public void addLast(T t) {
        a.a(TAG, "addLast chain=" + t);
        synchronized (this.mListLock) {
            if (!this.chainList.contains(t)) {
                this.chainList.addLast(t);
            }
        }
    }

    public void addList(LinkedList<T> linkedList) {
        a.a(TAG, "addList chain=" + linkedList);
        synchronized (this.mListLock) {
            this.chainList.clear();
            if (linkedList == null) {
                return;
            }
            this.chainList.addAll(linkedList);
        }
    }

    public boolean contains(T t) {
        synchronized (this.mListLock) {
            if (this.chainList != null && this.chainList.size() >= 1) {
                return this.chainList.contains(t);
            }
            return false;
        }
    }

    public T getItem(int i) {
        synchronized (this.mListLock) {
            if (i >= 0) {
                if (i + 1 <= getSize()) {
                    return this.chainList.get(i);
                }
            }
            return null;
        }
    }

    public void remove(T t) {
        a.a(TAG, "remove chain=" + t);
        synchronized (this.mListLock) {
            this.chainList.remove(t);
        }
    }

    public int getSize() {
        int size;
        synchronized (this.mListLock) {
            size = this.chainList.size();
        }
        return size;
    }

    public void clear() {
        synchronized (this.mListLock) {
            this.chainList.clear();
        }
    }
}
