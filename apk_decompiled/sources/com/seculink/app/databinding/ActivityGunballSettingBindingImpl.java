package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityGunballSettingBindingImpl extends ActivityGunballSettingBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    private long mDirtyFlags;

    @NonNull
    private final LinearLayout mboundView1;

    @NonNull
    private final LinearLayout mboundView2;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    static {
        sViewsWithIds.put(R.id.fl_titlebar, 3);
        sViewsWithIds.put(R.id.line, 4);
        sViewsWithIds.put(R.id.it_linkage, 5);
        sViewsWithIds.put(R.id.it_linkage_track, 6);
        sViewsWithIds.put(R.id.view2, 7);
        sViewsWithIds.put(R.id.osd_face, 8);
        sViewsWithIds.put(R.id.item_motion_detection, 9);
        sViewsWithIds.put(R.id.osd_night_mode, 10);
        sViewsWithIds.put(R.id.view3, 11);
        sViewsWithIds.put(R.id.osd_record_video_quality, 12);
        sViewsWithIds.put(R.id.osd_record_mode, 13);
    }

    public ActivityGunballSettingBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 14, sIncludes, sViewsWithIds));
    }

    private ActivityGunballSettingBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (TitleView) objArr[3], (ItemView) objArr[5], (ItemView) objArr[6], (ItemView) objArr[9], (ConstraintLayout) objArr[0], (View) objArr[4], (ItemView) objArr[8], (ItemView) objArr[10], (ItemView) objArr[13], (ItemView) objArr[12], (View) objArr[7], (View) objArr[11]);
        this.mDirtyFlags = -1L;
        this.layoutMain.setTag(null);
        this.mboundView1 = (LinearLayout) objArr[1];
        this.mboundView1.setTag(null);
        this.mboundView2 = (LinearLayout) objArr[2];
        this.mboundView2.setTag(null);
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
        if (1 != i) {
            return false;
        }
        setNvrOwner(((Integer) obj).intValue());
        return true;
    }

    @Override // com.seculink.app.databinding.ActivityGunballSettingBinding
    public void setNvrOwner(int i) {
        this.mNvrOwner = i;
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        notifyPropertyChanged(1);
        super.requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        long j;
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
        int i = this.mNvrOwner;
        long j2 = j & 3;
        int i2 = 0;
        if (j2 != 0) {
            boolean z = i == 1;
            if (j2 != 0) {
                j = z ? j | 8 : j | 4;
            }
            if (!z) {
                i2 = 8;
            }
        }
        if ((j & 3) != 0) {
            this.mboundView1.setVisibility(i2);
            this.mboundView2.setVisibility(i2);
        }
    }
}
