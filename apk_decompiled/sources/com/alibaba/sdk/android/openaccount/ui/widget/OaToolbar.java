package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import com.alibaba.sdk.android.openaccount.ui.R;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public class OaToolbar extends Toolbar {
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;

    public OaToolbar(Context context) {
        super(context);
        resolveAttribute(context, null, R.attr.toolbarStyle);
    }

    public OaToolbar(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        resolveAttribute(context, attributeSet, R.attr.toolbarStyle);
    }

    public OaToolbar(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        resolveAttribute(context, attributeSet, i);
    }

    private void resolveAttribute(Context context, @Nullable AttributeSet attributeSet, int i) {
        Context context2 = getContext();
        TypedArray typedArrayObtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R.styleable.Toolbar, i, 0);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        if (resourceId != 0) {
            setTitleTextAppearance(context2, resourceId);
        }
        int i2 = this.mTitleTextColor;
        if (i2 != 0) {
            setTitleTextColor(i2);
        }
        typedArrayObtainStyledAttributes.recycle();
        post(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.OaToolbar.1
            @Override // java.lang.Runnable
            public void run() {
                if (OaToolbar.this.getLayoutParams() instanceof Toolbar.LayoutParams) {
                    ((Toolbar.LayoutParams) OaToolbar.this.getLayoutParams()).gravity = 17;
                }
            }
        });
    }

    @Override // androidx.appcompat.widget.Toolbar
    public CharSequence getTitle() {
        return this.mTitleText;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitle(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.mTitleTextView == null) {
                Context context = getContext();
                this.mTitleTextView = new TextView(context);
                this.mTitleTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i = this.mTitleTextAppearance;
                if (i != 0) {
                    this.mTitleTextView.setTextAppearance(context, i);
                }
                int i2 = this.mTitleTextColor;
                if (i2 != 0) {
                    this.mTitleTextView.setTextColor(i2);
                }
            }
            if (this.mTitleTextView.getParent() != this) {
                addCenterView(this.mTitleTextView);
            }
        } else {
            TextView textView = this.mTitleTextView;
            if (textView != null && textView.getParent() == this) {
                removeView(this.mTitleTextView);
            }
        }
        TextView textView2 = this.mTitleTextView;
        if (textView2 != null) {
            textView2.setText(charSequence);
        }
        this.mTitleText = charSequence;
    }

    private void addCenterView(View view2) {
        Toolbar.LayoutParams layoutParamsGenerateLayoutParams;
        ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
        if (layoutParams == null) {
            layoutParamsGenerateLayoutParams = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(layoutParams)) {
            layoutParamsGenerateLayoutParams = generateLayoutParams(layoutParams);
        } else {
            layoutParamsGenerateLayoutParams = (Toolbar.LayoutParams) layoutParams;
        }
        addView(view2, layoutParamsGenerateLayoutParams);
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public Toolbar.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(getContext(), attributeSet);
        layoutParams.gravity = 17;
        return layoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public Toolbar.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        Toolbar.LayoutParams layoutParams2;
        if (layoutParams instanceof Toolbar.LayoutParams) {
            layoutParams2 = new Toolbar.LayoutParams((Toolbar.LayoutParams) layoutParams);
        } else if (layoutParams instanceof ActionBar.LayoutParams) {
            layoutParams2 = new Toolbar.LayoutParams((ActionBar.LayoutParams) layoutParams);
        } else if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            layoutParams2 = new Toolbar.LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        } else {
            layoutParams2 = new Toolbar.LayoutParams(layoutParams);
        }
        layoutParams2.gravity = 17;
        return layoutParams2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public Toolbar.LayoutParams generateDefaultLayoutParams() {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        return layoutParams;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitleTextAppearance(Context context, @StyleRes int i) {
        this.mTitleTextAppearance = i;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, i);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitleTextColor(@ColorInt int i) {
        this.mTitleTextColor = i;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationIcon(@Nullable Drawable drawable) {
        super.setNavigationIcon(drawable);
        setGravityCenter();
    }

    public void setGravityCenter() {
        post(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.OaToolbar.2
            @Override // java.lang.Runnable
            public void run() {
                OaToolbar.this.setCenter("mNavButtonView");
                OaToolbar.this.setCenter("mMenuView");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCenter(String str) {
        try {
            Field declaredField = getClass().getSuperclass().getDeclaredField(str);
            declaredField.setAccessible(true);
            Object obj = declaredField.get(this);
            if (obj != null && (obj instanceof View)) {
                View view2 = (View) obj;
                ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                if (layoutParams instanceof ActionBar.LayoutParams) {
                    ((ActionBar.LayoutParams) layoutParams).gravity = 17;
                    view2.setLayoutParams(layoutParams);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }
}
