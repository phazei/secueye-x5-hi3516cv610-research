package com.taobao.accs.utl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public abstract class RomInfoCollecter {
    protected RomInfoCollecter mNextCollecter;

    public abstract String collect();

    public static RomInfoCollecter getCollecter() {
        return new c();
    }
}
