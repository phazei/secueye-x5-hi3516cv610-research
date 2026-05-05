package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.haibin.calendarview.CalendarView;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityCloudStorageBindingImpl extends ActivityCloudStorageBinding {

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
        sViewsWithIds.put(R.id.tv_title, 1);
        sViewsWithIds.put(R.id.portrait_player, 2);
        sViewsWithIds.put(R.id.play, 3);
        sViewsWithIds.put(R.id.layout_control, 4);
        sViewsWithIds.put(R.id.iv_shadow_top, 5);
        sViewsWithIds.put(R.id.iv_back, 6);
        sViewsWithIds.put(R.id.iv_shadow_bottom, 7);
        sViewsWithIds.put(R.id.iv_play_state, 8);
        sViewsWithIds.put(R.id.iv_refund, 9);
        sViewsWithIds.put(R.id.iv_advance, 10);
        sViewsWithIds.put(R.id.layout_bottom_control, 11);
        sViewsWithIds.put(R.id.layout_shot, 12);
        sViewsWithIds.put(R.id.iv_sound, 13);
        sViewsWithIds.put(R.id.tv_speed, 14);
        sViewsWithIds.put(R.id.iv_full, 15);
        sViewsWithIds.put(R.id.layout_time, 16);
        sViewsWithIds.put(R.id.tv_start_time, 17);
        sViewsWithIds.put(R.id.seekBar, 18);
        sViewsWithIds.put(R.id.tv_end_time, 19);
        sViewsWithIds.put(R.id.layout_function, 20);
        sViewsWithIds.put(R.id.layout_date, 21);
        sViewsWithIds.put(R.id.iv_left, 22);
        sViewsWithIds.put(R.id.tv_date, 23);
        sViewsWithIds.put(R.id.iv_right, 24);
        sViewsWithIds.put(R.id.rv_list, 25);
        sViewsWithIds.put(R.id.layout_no_data, 26);
        sViewsWithIds.put(R.id.layout_bottom, 27);
        sViewsWithIds.put(R.id.layout_ptz, 28);
        sViewsWithIds.put(R.id.layout_bullet, 29);
        sViewsWithIds.put(R.id.tv_playback_gun, 30);
        sViewsWithIds.put(R.id.layout_bullet2, 31);
        sViewsWithIds.put(R.id.layout_bullet3, 32);
        sViewsWithIds.put(R.id.layout_calendarView, 33);
        sViewsWithIds.put(R.id.tv_cancel, 34);
        sViewsWithIds.put(R.id.tv_success, 35);
        sViewsWithIds.put(R.id.tv_curdate, 36);
        sViewsWithIds.put(R.id.calendarView, 37);
    }

    public ActivityCloudStorageBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 38, sIncludes, sViewsWithIds));
    }

    private ActivityCloudStorageBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (CalendarView) objArr[37], (ImageView) objArr[10], (ImageView) objArr[6], (ImageView) objArr[15], (ImageView) objArr[22], (ImageView) objArr[8], (ImageView) objArr[9], (ImageView) objArr[24], (ImageView) objArr[7], (ImageView) objArr[5], (ImageView) objArr[13], (LinearLayout) objArr[0], (LinearLayout) objArr[27], (LinearLayout) objArr[11], (LinearLayout) objArr[29], (LinearLayout) objArr[31], (LinearLayout) objArr[32], (LinearLayout) objArr[33], (RelativeLayout) objArr[4], (LinearLayout) objArr[21], (LinearLayout) objArr[20], (LinearLayout) objArr[26], (LinearLayout) objArr[28], (ImageView) objArr[12], (RelativeLayout) objArr[16], (SimpleExoPlayerView) objArr[3], (RelativeLayout) objArr[2], (RecyclerView) objArr[25], (SeekBar) objArr[18], (TextView) objArr[34], (TextView) objArr[36], (TextView) objArr[23], (TextView) objArr[19], (TextView) objArr[30], (TextView) objArr[14], (TextView) objArr[17], (TextView) objArr[35], (TitleView) objArr[1]);
        this.mDirtyFlags = -1L;
        this.landscapePlayer.setTag(null);
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
