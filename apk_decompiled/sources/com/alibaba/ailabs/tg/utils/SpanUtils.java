package com.alibaba.ailabs.tg.utils;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public class SpanUtils {
    private static final int COLOR_DEFAULT = -16777217;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private int backgroundColor;
    private ClickableSpan clickSpan;
    private int flag;
    private int fontSize;
    private boolean fontSizeIsDp;
    private int foregroundColor;
    private boolean isBold;
    private boolean isBoldItalic;
    private boolean isItalic;
    private int mType;
    private Object[] spans;
    private String url;
    private final int mTypeCharSequence = 0;
    private SpannableStringBuilder mBuilder = new SpannableStringBuilder();
    private CharSequence mText = "";

    @Deprecated
    public static void setBackgroundSpan(TextView textView, String str, String str2, int i) {
        if (textView == null || TextUtils.isEmpty(str)) {
            return;
        }
        if (TextUtils.isEmpty(str2) || !str.contains(str2) || str.indexOf(str2) < 0) {
            textView.setText(str);
            return;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        spannableStringBuilder.setSpan(new BackgroundColorSpan(i), str.indexOf(str2), str.indexOf(str2) + str2.length(), 33);
        textView.setText(spannableStringBuilder);
    }

    public SpanUtils() {
        setDefault();
    }

    private void setDefault() {
        this.flag = 33;
        this.foregroundColor = COLOR_DEFAULT;
        this.backgroundColor = COLOR_DEFAULT;
        this.fontSize = -1;
        this.isBold = false;
        this.isItalic = false;
        this.isBoldItalic = false;
        this.clickSpan = null;
        this.url = null;
        this.spans = null;
    }

    public SpanUtils setFlag(int i) {
        this.flag = i;
        return this;
    }

    public SpanUtils setForegroundColor(@ColorInt int i) {
        this.foregroundColor = i;
        return this;
    }

    public SpanUtils setBackgroundColor(@ColorInt int i) {
        this.backgroundColor = i;
        return this;
    }

    public SpanUtils setFontSize(@IntRange(from = 0) int i) {
        return setFontSize(i, false);
    }

    public SpanUtils setFontSize(@IntRange(from = 0) int i, boolean z) {
        this.fontSize = i;
        this.fontSizeIsDp = z;
        return this;
    }

    public SpanUtils setBold() {
        this.isBold = true;
        return this;
    }

    public SpanUtils setItalic() {
        this.isItalic = true;
        return this;
    }

    public SpanUtils setBoldItalic() {
        this.isBoldItalic = true;
        return this;
    }

    public SpanUtils setClickSpan(@NonNull ClickableSpan clickableSpan) {
        this.clickSpan = clickableSpan;
        return this;
    }

    public SpanUtils setUrl(@NonNull String str) {
        this.url = str;
        return this;
    }

    public SpanUtils setSpans(@NonNull Object... objArr) {
        if (objArr.length > 0) {
            this.spans = objArr;
        }
        return this;
    }

    public SpanUtils append(@NonNull CharSequence charSequence) {
        apply(0);
        this.mText = charSequence;
        return this;
    }

    public SpanUtils appendLine() {
        apply(0);
        this.mText = LINE_SEPARATOR;
        return this;
    }

    public SpanUtils appendLine(@NonNull CharSequence charSequence) {
        apply(0);
        this.mText = ((Object) charSequence) + LINE_SEPARATOR;
        return this;
    }

    private void apply(int i) {
        applyLast();
        this.mType = i;
    }

    public SpannableStringBuilder create() {
        applyLast();
        return this.mBuilder;
    }

    private void applyLast() {
        if (this.mType == 0) {
            updateCharCharSequence();
        }
        setDefault();
    }

    private void updateCharCharSequence() {
        if (this.mText.length() == 0) {
            return;
        }
        int length = this.mBuilder.length();
        this.mBuilder.append(this.mText);
        int length2 = this.mBuilder.length();
        int i = this.foregroundColor;
        if (i != COLOR_DEFAULT) {
            this.mBuilder.setSpan(new ForegroundColorSpan(i), length, length2, this.flag);
        }
        int i2 = this.backgroundColor;
        if (i2 != COLOR_DEFAULT) {
            this.mBuilder.setSpan(new BackgroundColorSpan(i2), length, length2, this.flag);
        }
        int i3 = this.fontSize;
        if (i3 != -1) {
            this.mBuilder.setSpan(new AbsoluteSizeSpan(i3, this.fontSizeIsDp), length, length2, this.flag);
        }
        if (this.isBold) {
            this.mBuilder.setSpan(new StyleSpan(1), length, length2, this.flag);
        }
        if (this.isItalic) {
            this.mBuilder.setSpan(new StyleSpan(2), length, length2, this.flag);
        }
        if (this.isBoldItalic) {
            this.mBuilder.setSpan(new StyleSpan(3), length, length2, this.flag);
        }
        ClickableSpan clickableSpan = this.clickSpan;
        if (clickableSpan != null) {
            this.mBuilder.setSpan(clickableSpan, length, length2, this.flag);
        }
        String str = this.url;
        if (str != null) {
            this.mBuilder.setSpan(new URLSpan(str), length, length2, this.flag);
        }
        Object[] objArr = this.spans;
        if (objArr != null) {
            for (Object obj : objArr) {
                this.mBuilder.setSpan(obj, length, length2, this.flag);
            }
        }
    }
}
