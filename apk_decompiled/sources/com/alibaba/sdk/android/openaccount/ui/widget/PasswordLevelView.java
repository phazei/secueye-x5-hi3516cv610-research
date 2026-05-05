package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.alibaba.sdk.android.openaccount.ui.R;

/* JADX INFO: loaded from: classes.dex */
public class PasswordLevelView extends LinearLayout {
    private TextView mLevelTextView;
    private LinearLayout mLinearLayout;
    private int mPasswordLevel;

    public PasswordLevelView(Context context) {
        this(context, null);
    }

    public PasswordLevelView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public PasswordLevelView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.PasswordLevelView);
        this.mPasswordLevel = typedArrayObtainStyledAttributes.getInt(R.styleable.PasswordLevelView_password_level, 0);
        typedArrayObtainStyledAttributes.recycle();
        initView(LayoutInflater.from(context).inflate(R.layout.ali_sdk_openaccount_password_level_view, (ViewGroup) this, true));
    }

    private void initView(View view2) {
        this.mLinearLayout = (LinearLayout) view2.findViewById(R.id.level_view_container);
        this.mLevelTextView = (TextView) view2.findViewById(R.id.tv_level);
        setPasswordLevel(this.mPasswordLevel);
    }

    public void setPasswordLevel(int i) {
        int i2;
        this.mLinearLayout.removeAllViews();
        switch (i) {
            case 0:
                i2 = R.string.ali_sdk_openaccount_text_register_password_extremely_weak;
                break;
            case 1:
                i2 = R.string.ali_sdk_openaccount_text_register_password_weak;
                break;
            case 2:
                i2 = R.string.ali_sdk_openaccount_text_register_password_secondary;
                break;
            case 3:
                i2 = R.string.ali_sdk_openaccount_text_register_password_strong;
                break;
            default:
                i2 = R.string.ali_sdk_openaccount_text_register_password_extremely_weak;
                break;
        }
        this.mLevelTextView.setText(i2);
        this.mPasswordLevel = i;
        int i3 = 0;
        while (true) {
            int i4 = this.mPasswordLevel;
            if (i3 >= i4) {
                int i5 = 3 - i4;
                for (int i6 = 0; i6 < i5; i6++) {
                    View view2 = new View(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 10);
                    layoutParams.leftMargin = 10;
                    view2.setLayoutParams(layoutParams);
                    view2.setBackgroundResource(R.color.ali_sdk_openaccount_button_bg_disable);
                    this.mLinearLayout.addView(view2);
                }
                return;
            }
            View view3 = new View(getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(100, 10);
            layoutParams2.leftMargin = 10;
            view3.setLayoutParams(layoutParams2);
            view3.setBackgroundResource(R.color.ali_sdk_openaccount_text_display);
            this.mLinearLayout.addView(view3);
            i3++;
        }
    }
}
