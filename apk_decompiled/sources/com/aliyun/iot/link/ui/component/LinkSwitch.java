package com.aliyun.iot.link.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.SwitchCompat;

/* JADX INFO: loaded from: classes2.dex */
public class LinkSwitch extends SwitchCompat {
    public LinkSwitch(Context context) {
        this(context, null);
    }

    public LinkSwitch(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinkSwitch(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setClickable(true);
        setShowText(false);
        setTrackResource(R.drawable.switch_track_bg);
        setThumbResource(R.drawable.switch_thumb);
    }
}
