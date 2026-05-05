package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.ItemView;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityMotionDetectBindingImpl extends ActivityMotionDetectBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    private long mDirtyFlags;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean setVariable(int i, @Nullable Object obj) {
        return true;
    }

    static {
        sViewsWithIds.put(R.id.fl_titlebar, 1);
        sViewsWithIds.put(R.id.layout_switch, 2);
        sViewsWithIds.put(R.id.layout_ptz, 3);
        sViewsWithIds.put(R.id.layout_bullet, 4);
        sViewsWithIds.put(R.id.tv_playback_gun, 5);
        sViewsWithIds.put(R.id.layout_ptz_show, 6);
        sViewsWithIds.put(R.id.yi_dong_zhen_ce_ling_ming_du, 7);
        sViewsWithIds.put(R.id.yi_dong_zhui_zong, 8);
        sViewsWithIds.put(R.id.ren_xing_zhen_ce, 9);
        sViewsWithIds.put(R.id.ren_xing_zhui_zong, 10);
        sViewsWithIds.put(R.id.device_auto_locate, 11);
        sViewsWithIds.put(R.id.layout_qu_yu, 12);
        sViewsWithIds.put(R.id.qu_yu, 13);
        sViewsWithIds.put(R.id.qu_yu_set, 14);
        sViewsWithIds.put(R.id.layout_kua_xian, 15);
        sViewsWithIds.put(R.id.kua_xian, 16);
        sViewsWithIds.put(R.id.kua_xian_set, 17);
        sViewsWithIds.put(R.id.layout_bullet_show, 18);
        sViewsWithIds.put(R.id.lian_dong, 19);
        sViewsWithIds.put(R.id.ren_xing_zhen_ce_bullet, 20);
        sViewsWithIds.put(R.id.yi_dong_zhen_ce_ling_ming_du_bullet, 21);
        sViewsWithIds.put(R.id.item_mobile_push, 22);
        sViewsWithIds.put(R.id.item_mobile_push2, 23);
        sViewsWithIds.put(R.id.item_mobile_strong_push, 24);
        sViewsWithIds.put(R.id.item_alarm_mode, 25);
        sViewsWithIds.put(R.id.alarm_text, 26);
        sViewsWithIds.put(R.id.item_alarm_time, 27);
    }

    public ActivityMotionDetectBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 28, sIncludes, sViewsWithIds));
    }

    private ActivityMotionDetectBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (TextView) objArr[26], (LongItemView) objArr[11], (TitleView) objArr[1], (ItemView) objArr[25], (ItemView) objArr[27], (LongItemView) objArr[22], (LongItemView) objArr[23], (ItemView) objArr[24], (LongItemView) objArr[16], (LongItemView) objArr[17], (LinearLayout) objArr[4], (LinearLayout) objArr[18], (LinearLayout) objArr[15], (LinearLayout) objArr[0], (LinearLayout) objArr[3], (LinearLayout) objArr[6], (LinearLayout) objArr[12], (LinearLayout) objArr[2], (ItemView) objArr[19], (LongItemView) objArr[13], (LongItemView) objArr[14], (LongItemView) objArr[9], (ItemView) objArr[20], (LongItemView) objArr[10], (TextView) objArr[5], (LongItemView) objArr[7], (ItemView) objArr[21], (LongItemView) objArr[8]);
        this.mDirtyFlags = -1L;
        this.layoutMain.setTag(null);
        setRootTag(view2);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 1L;
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
    protected void executeBindings() {
        synchronized (this) {
            long j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
    }
}
