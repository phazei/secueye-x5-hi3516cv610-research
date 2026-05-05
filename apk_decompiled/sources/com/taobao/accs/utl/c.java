package com.taobao.accs.utl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class c extends RomInfoCollecter {
    @Override // com.taobao.accs.utl.RomInfoCollecter
    public String collect() {
        String strF = UtilityImpl.f();
        return (strF != null || this.mNextCollecter == null) ? strF : this.mNextCollecter.collect();
    }
}
