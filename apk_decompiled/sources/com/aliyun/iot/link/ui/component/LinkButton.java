package com.aliyun.iot.link.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;

/* JADX INFO: loaded from: classes2.dex */
public class LinkButton extends AppCompatButton {
    public LinkButton(Context context) {
        this(context, null);
    }

    public LinkButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.buttonStyle);
    }

    public LinkButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
