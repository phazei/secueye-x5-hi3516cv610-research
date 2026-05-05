package com.taobao.agoo;

import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public abstract class IListAliasCallbackInner extends IListAliasCallback {
    @Override // com.taobao.agoo.IListAliasCallback
    public void onSuccess(List<String> list) {
    }

    public abstract void onSuccess(Map<String, String> map);
}
