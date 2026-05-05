package com.taobao.accs.base;

import com.taobao.accs.base.TaoBaseService;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public interface AccsDataListenerV2 extends AccsDataListener {
    void onBind(String str, int i, String str2, TaoBaseService.ExtraInfo extraInfo);

    void onResponse(String str, String str2, int i, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo);

    void onSendData(String str, String str2, int i, String str3, TaoBaseService.ExtraInfo extraInfo);

    void onUnbind(String str, int i, String str2, TaoBaseService.ExtraInfo extraInfo);
}
