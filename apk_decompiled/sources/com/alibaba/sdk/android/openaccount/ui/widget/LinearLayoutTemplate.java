package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.alibaba.sdk.android.openaccount.ui.CustomWidget;
import com.alibaba.sdk.android.openaccount.ui.LayoutMapping;
import com.alibaba.sdk.android.openaccount.ui.util.AttributeUtils;
import com.alibaba.sdk.android.openaccount.ui.util.OpenAccountUIUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class LinearLayoutTemplate extends LinearLayout implements CustomWidget {
    protected Typeface iconfont;

    protected void doUseCustomAttrs(Context context, TypedArray typedArray) {
    }

    protected String getLayoutName() {
        return null;
    }

    public LinearLayoutTemplate(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (isInEditMode()) {
            return;
        }
        LayoutInflater.from(context).inflate(getLayoutId(context), (ViewGroup) this, true);
        this.iconfont = OpenAccountUIUtils.getDefaultFont();
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.CustomWidget
    public int getLayoutId(Context context) {
        if (LayoutMapping.hasCustomLayout(getClass())) {
            return LayoutMapping.get(getClass()).intValue();
        }
        return ResourceUtils.getRLayout(context, getLayoutName());
    }

    protected void useCustomAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = AttributeUtils.obtainStyledAttributes(context, attributeSet);
        doUseCustomAttrs(context, typedArrayObtainStyledAttributes);
        typedArrayObtainStyledAttributes.recycle();
    }

    public View findViewById(String str) {
        return super.findViewById(ResourceUtils.getRId(getContext(), str));
    }

    protected void setViewVisibility(int i, View... viewArr) {
        for (View view2 : viewArr) {
            if (view2 != null && view2.getVisibility() != i) {
                view2.setVisibility(i);
            }
        }
    }

    protected void setViewOnClickListener(View.OnClickListener onClickListener, View... viewArr) {
        for (View view2 : viewArr) {
            if (view2 != null) {
                view2.setOnClickListener(onClickListener);
            }
        }
    }
}
