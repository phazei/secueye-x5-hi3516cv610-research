package com.aliyun.iot.link.ui.component;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class LinkBottomDialog {
    LinearLayout mContainer;
    AlertDialog mDialog;
    Button mNegativeBtn;
    Button mPositiveBtn;
    TextView mTitleTv;

    public interface OnClickListener {
        void onClick(LinkBottomDialog linkBottomDialog);
    }

    public interface OnItemClickListener {
        void onItemClick(LinkBottomDialog linkBottomDialog, String str, int i);
    }

    static class ItemEntry {
        int color;
        OnItemClickListener onItemClickListener;
        String text;

        ItemEntry() {
        }
    }

    LinkBottomDialog(final Builder builder) {
        this.mDialog = new AlertDialog.Builder(builder.mContext).create();
        View viewInflate = LayoutInflater.from(builder.mContext).inflate(R.layout.bottom_dialog, (ViewGroup) null);
        this.mTitleTv = (TextView) viewInflate.findViewById(R.id.title);
        this.mPositiveBtn = (Button) viewInflate.findViewById(R.id.positive_btn);
        this.mNegativeBtn = (Button) viewInflate.findViewById(R.id.negative_btn);
        this.mContainer = (LinearLayout) viewInflate.findViewById(R.id.container);
        this.mDialog.setView(viewInflate);
        this.mTitleTv.setText(builder.mTitle);
        if (-1 != builder.mNegativeBtnTextColor) {
            this.mNegativeBtn.setTextColor(builder.mNegativeBtnTextColor);
        }
        if (-1 != builder.mPositiveBtnTextColor) {
            this.mPositiveBtn.setTextColor(builder.mPositiveBtnTextColor);
        }
        List<ItemEntry> list = builder.items;
        for (final int i = 0; i < list.size(); i++) {
            final ItemEntry itemEntry = list.get(i);
            TextView textView = (TextView) LayoutInflater.from(builder.mContext).inflate(R.layout.dialog_item, (ViewGroup) null);
            textView.setText(itemEntry.text);
            textView.setTextColor(itemEntry.color);
            this.mContainer.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.LinkBottomDialog.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (itemEntry.onItemClickListener != null) {
                        itemEntry.onItemClickListener.onItemClick(LinkBottomDialog.this, itemEntry.text, i);
                    }
                }
            });
        }
        if (TextUtils.isEmpty(builder.mPositiveBtnText)) {
            this.mPositiveBtn.setVisibility(8);
            viewInflate.findViewById(R.id.divider).setVisibility(8);
        } else {
            this.mPositiveBtn.setVisibility(0);
            this.mPositiveBtn.setText(builder.mPositiveBtnText);
        }
        this.mNegativeBtn.setText(builder.mNegativeBtnText);
        this.mPositiveBtn.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.LinkBottomDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (builder.mPositiveListener != null) {
                    builder.mPositiveListener.onClick(LinkBottomDialog.this);
                }
            }
        });
        this.mNegativeBtn.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.LinkBottomDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (builder.mNegativeListener != null) {
                    builder.mNegativeListener.onClick(LinkBottomDialog.this);
                }
            }
        });
        this.mDialog.setCancelable(builder.mCancelable);
        this.mDialog.setCanceledOnTouchOutside(builder.mCanceledOnTouchOutside);
        this.mDialog.getWindow().setWindowAnimations(R.style.ActionSheetDialogAnimation);
        this.mDialog.getWindow().setGravity(80);
        this.mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    public void show() {
        this.mDialog.show();
    }

    public void dismiss() {
        this.mDialog.dismiss();
    }

    public static class Builder {
        public static final int INPUT = 2;
        public static final int NORMAL = 1;
        Context mContext;
        OnClickListener mNegativeListener;
        String mPositiveBtnText;
        OnClickListener mPositiveListener;
        String mTitle = "title";
        String mNegativeBtnText = "CANCEL";
        boolean mCanceledOnTouchOutside = true;
        boolean mCancelable = true;
        int mPositiveBtnTextColor = -1;
        int mNegativeBtnTextColor = -1;
        List<ItemEntry> items = new ArrayList();

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(String str) {
            this.mTitle = str;
            return this;
        }

        public Builder addItem(String str, OnItemClickListener onItemClickListener) {
            return addItem(str, Color.parseColor("#0079FF"), onItemClickListener);
        }

        public Builder addItem(String str, @ColorInt int i, OnItemClickListener onItemClickListener) {
            ItemEntry itemEntry = new ItemEntry();
            itemEntry.text = str;
            itemEntry.color = i;
            itemEntry.onItemClickListener = onItemClickListener;
            this.items.add(itemEntry);
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean z) {
            this.mCanceledOnTouchOutside = z;
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.mCancelable = z;
            return this;
        }

        public Builder setPositiveButton(String str, OnClickListener onClickListener) {
            this.mPositiveBtnText = str;
            this.mPositiveListener = onClickListener;
            return this;
        }

        public Builder setPositiveButtonColor(@ColorInt int i) {
            this.mPositiveBtnTextColor = i;
            return this;
        }

        public Builder setNegativeButtonColor(@ColorInt int i) {
            this.mNegativeBtnTextColor = i;
            return this;
        }

        public Builder setNegativeButton(String str, OnClickListener onClickListener) {
            this.mNegativeBtnText = str;
            this.mNegativeListener = onClickListener;
            return this;
        }

        public LinkBottomDialog create() {
            return new LinkBottomDialog(this);
        }
    }
}
