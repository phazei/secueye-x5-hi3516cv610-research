package com.alibaba.sdk.android.openaccount.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.sdk.android.openaccount.ui.CustomWidget;
import com.alibaba.sdk.android.openaccount.ui.LayoutMapping;
import com.alibaba.sdk.android.openaccount.ui.util.AttributeUtils;
import com.alibaba.sdk.android.openaccount.ui.util.OpenAccountUIUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.util.WidgetUtils;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class TitleBar extends RelativeLayout implements CustomWidget {
    protected Button back;
    protected TextView title;

    public TitleBar(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (isInEditMode()) {
            return;
        }
        LayoutInflater.from(context).inflate(getLayoutId(context), (ViewGroup) this, true);
        this.back = (Button) findViewById(ResourceUtils.getRId(context, "back"));
        this.back.setTypeface(OpenAccountUIUtils.getDefaultFont());
        this.back.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.TitleBar.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ((Activity) context).finish();
            }
        });
        this.title = (TextView) findViewById(ResourceUtils.getRId(context, "title"));
        TypedArray typedArrayObtainStyledAttributes = AttributeUtils.obtainStyledAttributes(context, attributeSet);
        WidgetUtils.setBackgroundDrawable(this, AttributeUtils.getDrawable(context, typedArrayObtainStyledAttributes, "ali_sdk_openaccount_attrs_title_bar_bg"));
        String string = AttributeUtils.getString(context, typedArrayObtainStyledAttributes, "ali_sdk_openaccount_attrs_title_bar_title");
        if (string != null) {
            this.title.setText(string);
        }
        this.title.setTextColor(AttributeUtils.getColor(context, typedArrayObtainStyledAttributes, "ali_sdk_openaccount_attrs_title_bar_text_color"));
        this.back.setTextColor(AttributeUtils.getColor(context, typedArrayObtainStyledAttributes, "ali_sdk_openaccount_attrs_title_bar_back_text_color"));
        setVisibility(AttributeUtils.getInt(context, typedArrayObtainStyledAttributes, "ali_sdk_openaccount_attrs_title_bar_visibility"));
        typedArrayObtainStyledAttributes.recycle();
    }

    public void setTitle(String str) {
        TextView textView = this.title;
        if (textView != null) {
            textView.setText(str);
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.CustomWidget
    public int getLayoutId(Context context) {
        if (LayoutMapping.hasCustomLayout(getClass())) {
            return LayoutMapping.get(getClass()).intValue();
        }
        return ResourceUtils.getRLayout(context, "ali_sdk_openaccount_title_bar");
    }
}
