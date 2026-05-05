package com.aliyun.iot.link.ui.component.nav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import com.aliyun.iot.link.ui.component.R;
import com.aliyun.iot.link.ui.component.nav.UIBarItem;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
@SuppressLint({"ClickableViewAccessibility"})
public class UINavigationBar extends FrameLayout {
    private static final int DISPLAY_TITLE_CENTER_IN_BAR = 4;
    private static final int DISPLAY_TITLE_CENTER_IN_CONTENT = 2;
    private static final int DISPLAY_TITLE_UNDECLARED = 1;
    public static final String TAG = "UINavigationBar";
    private ColorStateList actionImageTintList;
    private ColorStateList actionTextTintList;
    private FrameLayout contentView;
    private int displayMode;
    private View divider;
    private TextView internalTitleView;
    private TextView navigationBack;
    private final View.OnTouchListener observableAlphaListener;
    private List<UIBarButtonItem> rightBarButtonItems;
    private LinearLayout rightBarNavigationView;
    private Rect touchRect;

    public UINavigationBar(Context context) {
        this(context, null);
    }

    public UINavigationBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UINavigationBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.displayMode = 1;
        this.rightBarButtonItems = new ArrayList();
        this.touchRect = new Rect();
        this.observableAlphaListener = new View.OnTouchListener() { // from class: com.aliyun.iot.link.ui.component.nav.UINavigationBar.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:29:0x006a  */
            @Override // android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public boolean onTouch(android.view.View r7, android.view.MotionEvent r8) {
                /*
                    r6 = this;
                    int r0 = r8.getAction()
                    r1 = 0
                    r2 = 1065353216(0x3f800000, float:1.0)
                    r3 = 2
                    if (r0 != r3) goto L35
                    com.aliyun.iot.link.ui.component.nav.UINavigationBar r3 = com.aliyun.iot.link.ui.component.nav.UINavigationBar.this
                    android.graphics.Rect r3 = com.aliyun.iot.link.ui.component.nav.UINavigationBar.access$000(r3)
                    r7.getDrawingRect(r3)
                    com.aliyun.iot.link.ui.component.nav.UINavigationBar r3 = com.aliyun.iot.link.ui.component.nav.UINavigationBar.this
                    android.graphics.Rect r3 = com.aliyun.iot.link.ui.component.nav.UINavigationBar.access$000(r3)
                    float r4 = r8.getX()
                    int r4 = (int) r4
                    float r8 = r8.getY()
                    int r8 = (int) r8
                    boolean r8 = r3.contains(r4, r8)
                    if (r8 != 0) goto L35
                    float r8 = r7.getAlpha()
                    int r8 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
                    if (r8 == 0) goto L35
                    r7.setAlpha(r2)
                    return r1
                L35:
                    r8 = 3
                    if (r0 == 0) goto L3e
                    r3 = 1
                    if (r0 == r3) goto L3e
                    if (r0 == r8) goto L3e
                    return r1
                L3e:
                    int r3 = com.aliyun.iot.link.ui.component.R.string.ui_navigation_menu_item_raw     // Catch: java.lang.Exception -> L4d
                    java.lang.Object r3 = r7.getTag(r3)     // Catch: java.lang.Exception -> L4d
                    com.aliyun.iot.link.ui.component.nav.UIBarItem r3 = (com.aliyun.iot.link.ui.component.nav.UIBarItem) r3     // Catch: java.lang.Exception -> L4d
                    if (r3 == 0) goto L55
                    boolean r3 = r3.isEnable     // Catch: java.lang.Exception -> L4d
                    if (r3 != 0) goto L55
                    return r1
                L4d:
                    r3 = move-exception
                    java.lang.String r4 = "UINavigationBar"
                    java.lang.String r5 = "onTouch: "
                    android.util.Log.e(r4, r5, r3)
                L55:
                    if (r0 == r8) goto L6a
                    switch(r0) {
                        case 0: goto L5b;
                        case 1: goto L6a;
                        default: goto L5a;
                    }
                L5a:
                    goto L75
                L5b:
                    float r8 = r7.getAlpha()
                    r0 = 1053609165(0x3ecccccd, float:0.4)
                    int r8 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
                    if (r8 == 0) goto L75
                    r7.setAlpha(r0)
                    goto L75
                L6a:
                    float r8 = r7.getAlpha()
                    int r8 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
                    if (r8 == 0) goto L75
                    r7.setAlpha(r2)
                L75:
                    return r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iot.link.ui.component.nav.UINavigationBar.AnonymousClass1.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        };
        if (getBackground() == null) {
            setBackgroundColor(-1);
        }
        setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.ui_nav_bar_height));
        inflateView(context);
        findSubViews();
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.UINavigationBar, 0, 0);
        try {
            Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(R.styleable.UINavigationBar_divider);
            if (drawable != null) {
                if (Build.VERSION.SDK_INT >= 16) {
                    this.divider.setBackground(drawable);
                } else {
                    this.divider.setBackgroundDrawable(drawable);
                }
            }
            Drawable drawable2 = typedArrayObtainStyledAttributes.getDrawable(R.styleable.UINavigationBar_navigationIcon);
            if (drawable2 == null) {
                setNavigationIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_nav_back));
            } else {
                setNavigationIcon(drawable2);
            }
            setNavigationText(typedArrayObtainStyledAttributes.getString(R.styleable.UINavigationBar_navigationText));
            int resourceId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.UINavigationBar_titleTextStyle, R.style.ui__navigationBar_TitleTextAppearance);
            if (-1 != resourceId) {
                if (Build.VERSION.SDK_INT >= 23) {
                    this.internalTitleView.setTextAppearance(resourceId);
                } else {
                    this.internalTitleView.setTextAppearance(getContext(), resourceId);
                }
            }
            String string = typedArrayObtainStyledAttributes.getString(R.styleable.UINavigationBar_title);
            if (!TextUtils.isEmpty(string)) {
                setTitle(string);
            }
            this.actionTextTintList = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.UINavigationBar_actionTextTintList);
            this.actionImageTintList = typedArrayObtainStyledAttributes.getColorStateList(R.styleable.UINavigationBar_actionImageTintList);
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    private void inflateView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.link_actionbar_layout, (ViewGroup) this, true);
    }

    private void findSubViews() {
        this.internalTitleView = new TextView(new ContextThemeWrapper(getContext(), R.style.ui__navigationBar_Title));
        this.contentView = (FrameLayout) findViewById(R.id.ui_nav_bar_content_view);
        this.navigationBack = (TextView) findViewById(R.id.ui_nav_bar_nav_back);
        this.navigationBack.setOnTouchListener(this.observableAlphaListener);
        this.navigationBack.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.nav.UINavigationBar.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
            }
        });
        this.divider = findViewById(R.id.ui_nav_bar_divider);
        this.rightBarNavigationView = (LinearLayout) findViewById(R.id.ui_nav_bar_menu_view);
    }

    public void setNavigationBackAction(@Nullable final UIBarItem.Action action) {
        TextView textView = this.navigationBack;
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.nav.UINavigationBar.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    UIBarItem.Action action2 = action;
                    if (action2 != null) {
                        action2.invoke(view2);
                    }
                }
            });
        }
    }

    public void setTitle(@StringRes int i) {
        setTitle(getResources().getString(i));
    }

    public void setTitle(CharSequence charSequence) {
        TextView textView = this.internalTitleView;
        if (textView != null) {
            textView.setText(charSequence);
            requestLayoutTitle();
        }
    }

    public void setDivider(@DrawableRes int i) {
        if (i != 0) {
            this.divider.setBackgroundResource(i);
        } else if (Build.VERSION.SDK_INT >= 16) {
            this.divider.setBackground(null);
        } else {
            this.divider.setBackgroundDrawable(null);
        }
    }

    public void setNavigationIcon(Drawable drawable) {
        if (this.navigationBack != null) {
            if (Build.VERSION.SDK_INT >= 17) {
                this.navigationBack.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
            } else {
                this.navigationBack.setCompoundDrawables(drawable, null, null, null);
            }
            requestLayoutTitle();
        }
    }

    public void setNavigationText(@Nullable String str) {
        TextView textView = this.navigationBack;
        if (textView != null) {
            textView.setText(str);
            requestLayoutTitle();
        }
    }

    public TextView getNavigationBack() {
        return this.navigationBack;
    }

    public void setDisplayNavBackEnabled(boolean z) {
        int i = z ? 0 : 4;
        TextView textView = this.navigationBack;
        if (textView != null) {
            textView.setVisibility(i);
            requestLayoutTitle();
        }
    }

    public void setDisplayTitleEnabled(boolean z) {
        int i = z ? 0 : 4;
        TextView textView = this.internalTitleView;
        if (textView != null) {
            textView.setVisibility(i);
        }
    }

    public void setDisplayDividerEnable(boolean z) {
        if (z) {
            this.divider.setVisibility(0);
        } else {
            this.divider.setVisibility(4);
        }
    }

    public void addItem(UIBarButtonItem uIBarButtonItem) {
        LinearLayout linearLayout = this.rightBarNavigationView;
        if (linearLayout != null) {
            setItem(uIBarButtonItem, linearLayout.getChildCount());
        }
    }

    public void updateItem(int i) {
        if (this.rightBarNavigationView != null) {
            UIBarButtonItem uIBarButtonItem = null;
            int i2 = 0;
            while (true) {
                if (i2 >= this.rightBarNavigationView.getChildCount()) {
                    i2 = -1;
                    break;
                }
                uIBarButtonItem = (UIBarButtonItem) this.rightBarNavigationView.getChildAt(i2).getTag(R.string.ui_navigation_menu_item_raw);
                if (uIBarButtonItem != null && uIBarButtonItem.tag == i) {
                    break;
                } else {
                    i2++;
                }
            }
            if (-1 == i2) {
                return;
            }
            View childAt = this.rightBarNavigationView.getChildAt(i2);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setText(uIBarButtonItem.getTitle());
            } else if (!(childAt instanceof ImageView)) {
                return;
            } else {
                ((ImageView) childAt).setImageDrawable(uIBarButtonItem.getIcon());
            }
            childAt.setAlpha(uIBarButtonItem.isEnable ? 1.0f : 0.4f);
            childAt.setEnabled(uIBarButtonItem.isEnable);
            childAt.setTag(R.string.ui_navigation_menu_item_raw, uIBarButtonItem);
            final UIBarItem.Action action = uIBarButtonItem.action;
            childAt.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.nav.UINavigationBar.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    UIBarItem.Action action2 = action;
                    if (action2 != null) {
                        action2.invoke(view2);
                    }
                }
            });
            this.rightBarButtonItems.set(i2, uIBarButtonItem);
            requestLayoutTitle();
        }
    }

    public void setItem(UIBarButtonItem uIBarButtonItem, int i) {
        if (this.rightBarNavigationView != null) {
            if (i == this.rightBarButtonItems.size()) {
                this.rightBarButtonItems.add(uIBarButtonItem);
                requestLayoutTitle();
            } else {
                this.rightBarButtonItems.set(i, uIBarButtonItem);
                this.rightBarNavigationView.removeViewAt(i);
            }
            inflateMenuControl(uIBarButtonItem, i);
        }
    }

    public void removeItem(UIBarButtonItem uIBarButtonItem) {
        if (this.rightBarNavigationView != null) {
            removeItemAt(this.rightBarButtonItems.indexOf(uIBarButtonItem));
        }
    }

    public void removeItemAt(int i) {
        if (this.rightBarNavigationView == null || i < 0) {
            return;
        }
        this.rightBarButtonItems.remove(i);
        this.rightBarNavigationView.removeViewAt(i);
        requestLayoutTitle();
    }

    private void inflateMenuControl(final UIBarButtonItem uIBarButtonItem, int i) {
        LinearLayout.LayoutParams layoutParams;
        View textView;
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        if (this.rightBarNavigationView == null) {
            return;
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.ui_nav_bar_control_size);
        if (uIBarButtonItem.getIcon() != null) {
            layoutParams = new LinearLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize);
            layoutParams.gravity = 16;
            textView = new ImageView(new ContextThemeWrapper(getContext(), R.style.ui__navigationBar_ImageControl));
            ImageView imageView = (ImageView) textView;
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageDrawable(uIBarButtonItem.getIcon());
            if (Build.VERSION.SDK_INT >= 21 && (colorStateList2 = this.actionImageTintList) != null) {
                imageView.setImageTintList(colorStateList2);
            }
        } else if (uIBarButtonItem.getTitle() != null) {
            layoutParams = new LinearLayout.LayoutParams(-2, dimensionPixelSize);
            layoutParams.gravity = 16;
            textView = new TextView(new ContextThemeWrapper(getContext(), R.style.ui__navigationBar_TextControl));
            TextView textView2 = (TextView) textView;
            textView2.setMaxWidth(getMaxTextControlWidth());
            textView2.setText(uIBarButtonItem.getTitle());
            if (Build.VERSION.SDK_INT >= 21 && (colorStateList = this.actionTextTintList) != null) {
                textView2.setTextColor(colorStateList);
            }
        } else {
            throw new IllegalArgumentException("UIBarItem should has either title or image");
        }
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.nav.UINavigationBar.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (uIBarButtonItem.action != null) {
                    uIBarButtonItem.action.invoke(view2);
                }
            }
        });
        textView.setOnTouchListener(this.observableAlphaListener);
        textView.setTag(R.string.ui_navigation_menu_item_raw, uIBarButtonItem);
        textView.setAlpha(uIBarButtonItem.isEnable ? 1.0f : 0.4f);
        textView.setEnabled(uIBarButtonItem.isEnable);
        this.rightBarNavigationView.addView(textView, i, layoutParams);
    }

    private int getMaxTextControlWidth() {
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.ui_nav_menu_view_inset_start_text);
        return (((int) (((double) getTotalWidth()) * 0.32d)) - dimensionPixelOffset) - getResources().getDimensionPixelOffset(R.dimen.ui_nav_menu_view_inset_end_text);
    }

    private int getTotalWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private void setRightViewPadding() {
        int dimensionPixelOffset;
        int dimensionPixelOffset2;
        if (this.rightBarNavigationView == null) {
            return;
        }
        if (this.rightBarButtonItems.isEmpty()) {
            this.rightBarNavigationView.setPadding(0, 0, getResources().getDimensionPixelOffset(R.dimen.ui_nav_menu_view_inset_end_text), 0);
            return;
        }
        if (this.rightBarButtonItems.get(0).getTitle() != null) {
            dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.ui_nav_menu_view_inset_start_text);
            dimensionPixelOffset2 = getResources().getDimensionPixelOffset(R.dimen.ui_nav_menu_view_inset_end_text);
        } else {
            dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.ui_nav_menu_view_inset_start_icon);
            dimensionPixelOffset2 = getResources().getDimensionPixelOffset(R.dimen.ui_nav_menu_view_inset_end_icon);
        }
        this.rightBarNavigationView.setPadding(dimensionPixelOffset, 0, dimensionPixelOffset2, 0);
    }

    private void requestLayoutTitle() {
        setRightViewPadding();
        TextView textView = this.internalTitleView;
        if (textView == null) {
            return;
        }
        CharSequence text = textView.getText();
        Paint paint = this.internalTitleView.getPaint();
        if (paint == null) {
            paint = new Paint();
            paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.ui_nav_bar_title_size));
            paint.setTypeface(Typeface.DEFAULT);
        }
        Rect rect = new Rect();
        boolean z = text != null && text.length() > 0;
        paint.getTextBounds(z ? text.toString() : "", 0, z ? text.length() : 0, rect);
        final int iWidth = z ? rect.width() : 0;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.aliyun.iot.link.ui.component.nav.UINavigationBar.6
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    UINavigationBar.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                UINavigationBar uINavigationBar = UINavigationBar.this;
                uINavigationBar.updateTitleDisplayMode(iWidth, uINavigationBar.rightBarNavigationView.getWidth());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTitleDisplayMode(int i, int i2) {
        int i3 = Math.min((getTotalWidth() / 2) - (this.navigationBack.getVisibility() == 0 ? this.navigationBack.getWidth() : 0), (getTotalWidth() / 2) - i2) <= i / 2 ? 2 : 4;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        int i4 = this.displayMode;
        if (i4 != i3) {
            if (i3 == 2) {
                if (i4 != 1) {
                    removeView(this.internalTitleView);
                }
                this.contentView.addView(this.internalTitleView, layoutParams);
            } else if (i3 == 4) {
                if (i4 != 1) {
                    this.contentView.removeView(this.internalTitleView);
                }
                addView(this.internalTitleView, layoutParams);
            }
            this.displayMode = i3;
        }
    }

    public static final class UIBarButtonItem extends UIBarItem {
        static final int NO_TAG = Integer.MIN_VALUE;

        public UIBarButtonItem(String str, @Nullable UIBarItem.Action action) {
            this.tag = Integer.MIN_VALUE;
            setTitle(str);
            setAction(action);
            setEnable(true);
        }

        public UIBarButtonItem(Drawable drawable, @Nullable UIBarItem.Action action) {
            this.tag = Integer.MIN_VALUE;
            setIcon(drawable);
            setAction(action);
            setEnable(true);
        }

        public UIBarButtonItem(int i, String str, boolean z, @Nullable UIBarItem.Action action) {
            this.tag = i;
            setTitle(str);
            setEnable(z);
            setAction(action);
        }

        public String toString() {
            return "UIBarButtonItem{tag=" + this.tag + ", title='" + this.title + "', icon=" + this.icon + ", isEnable=" + this.isEnable + ", action=" + this.action + '}';
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof UIBarButtonItem)) {
                return false;
            }
            UIBarButtonItem uIBarButtonItem = (UIBarButtonItem) obj;
            if (uIBarButtonItem.title == null) {
                String str = this.title;
            } else {
                uIBarButtonItem.title.equals(this.title);
            }
            return uIBarButtonItem.tag == this.tag;
        }

        public int hashCode() {
            return this.tag * 31;
        }
    }
}
