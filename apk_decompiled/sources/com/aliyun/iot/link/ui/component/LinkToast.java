package com.aliyun.iot.link.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.StringRes;

/* JADX INFO: loaded from: classes2.dex */
public class LinkToast {
    private Context mContext;
    private Toast mToast;

    private LinkToast(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static LinkToast makeText(Context context, CharSequence charSequence) {
        return new LinkToast(context).update(context, charSequence);
    }

    public static LinkToast makeText(Context context, @StringRes int i) {
        return makeText(context, context.getResources().getString(i));
    }

    @Deprecated
    public static LinkToast makeText(Context context, CharSequence charSequence, int i) {
        return makeText(context, charSequence);
    }

    @Deprecated
    public static LinkToast makeText(Context context, @StringRes int i, int i2) {
        return makeText(context, context.getResources().getString(i), i2);
    }

    public void show() {
        if (this.mToast != null) {
            Log.d("LinkToast", "show()");
            this.mToast.show();
        }
    }

    public LinkToast setGravity(int i) {
        Toast toast = this.mToast;
        if (toast != null) {
            if (i == 80) {
                toast.setGravity(i, 0, this.mContext.getResources().getDimensionPixelOffset(R.dimen.link_toast_bottom_margin));
            } else {
                toast.setGravity(i, 0, 0);
            }
        }
        return this;
    }

    public LinkToast setGravity(int i, int i2, int i3) {
        Toast toast = this.mToast;
        if (toast != null) {
            toast.setGravity(i, i2, i3);
        }
        return this;
    }

    @SuppressLint({"ShowToast"})
    private LinkToast update(Context context, CharSequence charSequence) {
        if (this.mToast == null) {
            this.mToast = new Toast(context.getApplicationContext());
        }
        this.mToast.setDuration(0);
        this.mToast.setGravity(17, 0, 0);
        View viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.link_toast, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.text);
        if (textView != null) {
            textView.setText(charSequence);
        }
        Toast toast = this.mToast;
        if (toast != null) {
            toast.setView(viewInflate);
        }
        return this;
    }

    public void cancel() {
        Toast toast = this.mToast;
        if (toast != null) {
            toast.cancel();
        }
    }
}
