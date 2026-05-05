package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.text.style.URLSpan;
import android.view.View;
import com.alibaba.sdk.android.openaccount.ui.TokenWebViewActivity;

/* JADX INFO: loaded from: classes.dex */
public class OAUrlSpan extends URLSpan {
    public OAUrlSpan(String str) {
        super(str);
    }

    @Override // android.text.style.URLSpan, android.text.style.ClickableSpan
    public void onClick(View view2) {
        if (view2 == null || view2.getContext() == null) {
            return;
        }
        Context context = view2.getContext();
        Intent intent = new Intent();
        intent.setClass(context, TokenWebViewActivity.class);
        intent.putExtra("url", getURL());
        context.startActivity(intent);
    }
}
