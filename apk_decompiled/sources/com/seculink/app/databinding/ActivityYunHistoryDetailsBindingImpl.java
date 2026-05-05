package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.adapters.TextViewBindingAdapter;
import bean.CloudPayModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityYunHistoryDetailsBindingImpl extends ActivityYunHistoryDetailsBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    private long mDirtyFlags;

    @NonNull
    private final TextView mboundView1;

    @NonNull
    private final TextView mboundView10;

    @NonNull
    private final TextView mboundView11;

    @NonNull
    private final TextView mboundView2;

    @NonNull
    private final TextView mboundView3;

    @NonNull
    private final TextView mboundView4;

    @NonNull
    private final TextView mboundView5;

    @NonNull
    private final TextView mboundView6;

    @NonNull
    private final TextView mboundView7;

    @NonNull
    private final TextView mboundView8;

    @NonNull
    private final TextView mboundView9;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    static {
        sViewsWithIds.put(R.id.left_rl, 12);
        sViewsWithIds.put(R.id.left_img, 13);
    }

    public ActivityYunHistoryDetailsBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 14, sIncludes, sViewsWithIds));
    }

    private ActivityYunHistoryDetailsBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (LinearLayout) objArr[0], (ImageView) objArr[13], (RelativeLayout) objArr[12]);
        this.mDirtyFlags = -1L;
        this.layoutMain.setTag(null);
        this.mboundView1 = (TextView) objArr[1];
        this.mboundView1.setTag(null);
        this.mboundView10 = (TextView) objArr[10];
        this.mboundView10.setTag(null);
        this.mboundView11 = (TextView) objArr[11];
        this.mboundView11.setTag(null);
        this.mboundView2 = (TextView) objArr[2];
        this.mboundView2.setTag(null);
        this.mboundView3 = (TextView) objArr[3];
        this.mboundView3.setTag(null);
        this.mboundView4 = (TextView) objArr[4];
        this.mboundView4.setTag(null);
        this.mboundView5 = (TextView) objArr[5];
        this.mboundView5.setTag(null);
        this.mboundView6 = (TextView) objArr[6];
        this.mboundView6.setTag(null);
        this.mboundView7 = (TextView) objArr[7];
        this.mboundView7.setTag(null);
        this.mboundView8 = (TextView) objArr[8];
        this.mboundView8.setTag(null);
        this.mboundView9 = (TextView) objArr[9];
        this.mboundView9.setTag(null);
        setRootTag(view2);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 2L;
        }
        requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean hasPendingBindings() {
        synchronized (this) {
            return this.mDirtyFlags != 0;
        }
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean setVariable(int i, @Nullable Object obj) {
        if (2 != i) {
            return false;
        }
        setModel((CloudPayModel) obj);
        return true;
    }

    @Override // com.seculink.app.databinding.ActivityYunHistoryDetailsBinding
    public void setModel(@Nullable CloudPayModel cloudPayModel) {
        this.mModel = cloudPayModel;
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        notifyPropertyChanged(2);
        super.requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() throws Throwable {
        long j;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12;
        String str13;
        String str14;
        long j2;
        int i;
        double d2;
        String str15;
        String str16;
        synchronized (this) {
            try {
                j = this.mDirtyFlags;
                this.mDirtyFlags = 0L;
            } catch (Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        CloudPayModel cloudPayModel = this.mModel;
        long j3 = j & 3;
        String str17 = null;
        if (j3 != 0) {
            double d3 = 0.0d;
            if (cloudPayModel != null) {
                String str18 = cloudPayModel.rechargeTime;
                String str19 = cloudPayModel.effectime;
                str15 = cloudPayModel.orderNum;
                str16 = cloudPayModel.failureTime;
                d3 = cloudPayModel.amount;
                str11 = cloudPayModel.username;
                str12 = cloudPayModel.timeUnit;
                str13 = cloudPayModel.transactionId;
                i = cloudPayModel.timeNum;
                d2 = cloudPayModel.buyType;
                str14 = str18;
                j2 = cloudPayModel.rechargeId;
                str17 = str19;
            } else {
                str11 = null;
                str12 = null;
                str13 = null;
                str14 = null;
                j2 = 0;
                i = 0;
                d2 = 0.0d;
                str15 = null;
                str16 = null;
            }
            str3 = str14 + "";
            str4 = str15 + "";
            str5 = d3 + "";
            str6 = str11 + "";
            str7 = str12 + "";
            str8 = str13 + "";
            str9 = i + "";
            str10 = d2 + "";
            str = j2 + "";
            str2 = str17 + "";
            str17 = str16 + "";
        } else {
            str = null;
            str2 = null;
            str3 = null;
            str4 = null;
            str5 = null;
            str6 = null;
            str7 = null;
            str8 = null;
            str9 = null;
            str10 = null;
        }
        if (j3 != 0) {
            TextViewBindingAdapter.setText(this.mboundView1, str);
            TextViewBindingAdapter.setText(this.mboundView10, str17);
            TextViewBindingAdapter.setText(this.mboundView11, str6);
            TextViewBindingAdapter.setText(this.mboundView2, str7);
            TextViewBindingAdapter.setText(this.mboundView3, str9);
            TextViewBindingAdapter.setText(this.mboundView4, str10);
            TextViewBindingAdapter.setText(this.mboundView5, str5);
            TextViewBindingAdapter.setText(this.mboundView6, str4);
            TextViewBindingAdapter.setText(this.mboundView7, str8);
            TextViewBindingAdapter.setText(this.mboundView8, str3);
            TextViewBindingAdapter.setText(this.mboundView9, str2);
        }
    }
}
