package com.taobao.agoo;

import android.content.Context;
import android.content.Intent;
import org.android.agoo.control.BaseIntentService;

/* JADX INFO: loaded from: classes3.dex */
public abstract class TaobaoBaseIntentService extends BaseIntentService {
    @Override // org.android.agoo.control.BaseIntentService
    protected abstract void onError(Context context, String str);

    @Override // org.android.agoo.control.BaseIntentService
    protected abstract void onMessage(Context context, Intent intent);

    @Override // org.android.agoo.control.BaseIntentService
    protected abstract void onRegistered(Context context, String str);

    @Deprecated
    protected abstract void onUnregistered(Context context, String str);
}
