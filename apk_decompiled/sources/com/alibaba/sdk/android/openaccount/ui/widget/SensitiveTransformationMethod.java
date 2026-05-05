package com.alibaba.sdk.android.openaccount.ui.widget;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

/* JADX INFO: loaded from: classes.dex */
public class SensitiveTransformationMethod implements TransformationMethod {
    public static final SensitiveTransformationMethod INSTANCE = new SensitiveTransformationMethod();

    @Override // android.text.method.TransformationMethod
    public void onFocusChanged(View view2, CharSequence charSequence, boolean z, int i, Rect rect) {
    }

    @Override // android.text.method.TransformationMethod
    public CharSequence getTransformation(CharSequence charSequence, View view2) {
        if (charSequence.length() < 4) {
            return charSequence;
        }
        StringBuilder sb = new StringBuilder();
        int length = charSequence.length() / 3;
        sb.append(charSequence.subSequence(0, length));
        int length2 = (charSequence.length() - length) / 2;
        for (int i = 0; i < length2; i++) {
            sb.append(WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD);
        }
        sb.append(charSequence.subSequence(length + length2, charSequence.length()));
        return sb.toString();
    }
}
