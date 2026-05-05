package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.ViewPager;
import com.seculink.app.R;
import kt.SensorView;
import net.lucode.hackware.magicindicator.MagicIndicator;
import view.MyGlTextureView;
import view.ShadowButton;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityIpcameraThreeEyesBindingImpl extends ActivityIpcameraThreeEyesBinding {

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
        sViewsWithIds.put(R.id.layout_top, 1);
        sViewsWithIds.put(R.id.iv_back, 2);
        sViewsWithIds.put(R.id.tv_title, 3);
        sViewsWithIds.put(R.id.iv_setting, 4);
        sViewsWithIds.put(R.id.layout_play, 5);
        sViewsWithIds.put(R.id.timer, 6);
        sViewsWithIds.put(R.id.layout_gun, 7);
        sViewsWithIds.put(R.id.SensorView, 8);
        sViewsWithIds.put(R.id.layout_gun_orientation, 9);
        sViewsWithIds.put(R.id.player_gun1, 10);
        sViewsWithIds.put(R.id.player_gun2, 11);
        sViewsWithIds.put(R.id.iv_night_top, 12);
        sViewsWithIds.put(R.id.player_ball, 13);
        sViewsWithIds.put(R.id.layout_quality, 14);
        sViewsWithIds.put(R.id.quality_btn, 15);
        sViewsWithIds.put(R.id.player_info_tv, 16);
        sViewsWithIds.put(R.id.iv_night_bottom, 17);
        sViewsWithIds.put(R.id.tv_zoom, 18);
        sViewsWithIds.put(R.id.iv_full, 19);
        sViewsWithIds.put(R.id.ll_full, 20);
        sViewsWithIds.put(R.id.video_play_ibtn, 21);
        sViewsWithIds.put(R.id.ipc_offline_text, 22);
        sViewsWithIds.put(R.id.outline_time, 23);
        sViewsWithIds.put(R.id.traffic_4g_expired, 24);
        sViewsWithIds.put(R.id.immediate_renewal, 25);
        sViewsWithIds.put(R.id.iv_snap, 26);
        sViewsWithIds.put(R.id.video_buffering_bar, 27);
        sViewsWithIds.put(R.id.wakeup_text, 28);
        sViewsWithIds.put(R.id.quality_dlg, 29);
        sViewsWithIds.put(R.id.tv_h_quality, 30);
        sViewsWithIds.put(R.id.tv_m_quality, 31);
        sViewsWithIds.put(R.id.tv_l_quality, 32);
        sViewsWithIds.put(R.id.light_dlg, 33);
        sViewsWithIds.put(R.id.tv_light1, 34);
        sViewsWithIds.put(R.id.tv_light2, 35);
        sViewsWithIds.put(R.id.tv_light3, 36);
        sViewsWithIds.put(R.id.layout_control, 37);
        sViewsWithIds.put(R.id.layout_bottom, 38);
        sViewsWithIds.put(R.id.ll_capture, 39);
        sViewsWithIds.put(R.id.capture_btn, 40);
        sViewsWithIds.put(R.id.tv_capture, 41);
        sViewsWithIds.put(R.id.ll_record, 42);
        sViewsWithIds.put(R.id.record_btn, 43);
        sViewsWithIds.put(R.id.tv_record, 44);
        sViewsWithIds.put(R.id.speaker_btn, 45);
        sViewsWithIds.put(R.id.ll_listener, 46);
        sViewsWithIds.put(R.id.listener_btn, 47);
        sViewsWithIds.put(R.id.tv_voice, 48);
        sViewsWithIds.put(R.id.ll_more_double_eye, 49);
        sViewsWithIds.put(R.id.more_image, 50);
        sViewsWithIds.put(R.id.more_text_double_eye, 51);
        sViewsWithIds.put(R.id.layout_center, 52);
        sViewsWithIds.put(R.id.rl_touch_view, 53);
        sViewsWithIds.put(R.id.iv_charge_4g_flow, 54);
        sViewsWithIds.put(R.id.layout_af, 55);
        sViewsWithIds.put(R.id.bt_zoom_add_btn, 56);
        sViewsWithIds.put(R.id.bt_zoom_reduce_btn, 57);
        sViewsWithIds.put(R.id.layout_zoom, 58);
        sViewsWithIds.put(R.id.tv_zoom_back, 59);
        sViewsWithIds.put(R.id.zoom_reduce_btn, 60);
        sViewsWithIds.put(R.id.zoom_add_btn, 61);
        sViewsWithIds.put(R.id.focus_reduce_btn, 62);
        sViewsWithIds.put(R.id.focus_add_btn, 63);
        sViewsWithIds.put(R.id.tv_preset, 64);
        sViewsWithIds.put(R.id.et_preset, 65);
        sViewsWithIds.put(R.id.bt_preset_invoke, 66);
        sViewsWithIds.put(R.id.bt_preset_add, 67);
        sViewsWithIds.put(R.id.layout_more, 68);
        sViewsWithIds.put(R.id.topicIndicator, 69);
        sViewsWithIds.put(R.id.line, 70);
        sViewsWithIds.put(R.id.topicViewPager, 71);
        sViewsWithIds.put(R.id.full_screen, 72);
        sViewsWithIds.put(R.id.full_night_vision, 73);
        sViewsWithIds.put(R.id.full_switch_window, 74);
        sViewsWithIds.put(R.id.full_sound, 75);
        sViewsWithIds.put(R.id.full_intercom, 76);
        sViewsWithIds.put(R.id.full_video, 77);
        sViewsWithIds.put(R.id.full_camera, 78);
        sViewsWithIds.put(R.id.full_add_zoom, 79);
        sViewsWithIds.put(R.id.full_reduce_zoom, 80);
    }

    public ActivityIpcameraThreeEyesBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 81, sIncludes, sViewsWithIds));
    }

    private ActivityIpcameraThreeEyesBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (SensorView) objArr[8], (Button) objArr[67], (Button) objArr[66], (Button) objArr[56], (Button) objArr[57], (ImageButton) objArr[40], (EditText) objArr[65], (Button) objArr[63], (Button) objArr[62], (ImageView) objArr[79], (ShadowButton) objArr[78], (ShadowButton) objArr[76], (ShadowButton) objArr[73], (ImageView) objArr[80], (RelativeLayout) objArr[72], (ShadowButton) objArr[75], (ShadowButton) objArr[74], (ShadowButton) objArr[77], (Button) objArr[25], (TextView) objArr[22], (ImageView) objArr[2], (ImageView) objArr[54], (ImageView) objArr[19], (ImageView) objArr[17], (ImageView) objArr[12], (ImageView) objArr[4], (ImageView) objArr[26], (LinearLayout) objArr[55], (LinearLayout) objArr[38], (RelativeLayout) objArr[52], (RelativeLayout) objArr[37], (RelativeLayout) objArr[7], (LinearLayout) objArr[9], (RelativeLayout) objArr[68], (RelativeLayout) objArr[5], (LinearLayout) objArr[14], (RelativeLayout) objArr[1], (LinearLayout) objArr[58], (FrameLayout) objArr[33], (View) objArr[70], (ImageButton) objArr[47], (LinearLayout) objArr[39], (ImageView) objArr[20], (LinearLayout) objArr[46], (LinearLayout) objArr[49], (LinearLayout) objArr[42], (RelativeLayout) objArr[0], (ToggleButton) objArr[50], (TextView) objArr[51], (TextView) objArr[23], (MyGlTextureView) objArr[13], (MyGlTextureView) objArr[10], (MyGlTextureView) objArr[11], (TextView) objArr[16], (TextView) objArr[15], (LinearLayout) objArr[29], (ImageButton) objArr[43], (RelativeLayout) objArr[53], (ImageButton) objArr[45], (TextView) objArr[6], (MagicIndicator) objArr[69], (ViewPager) objArr[71], (TextView) objArr[24], (TextView) objArr[41], (TextView) objArr[30], (TextView) objArr[32], (TextView) objArr[34], (TextView) objArr[35], (TextView) objArr[36], (TextView) objArr[31], (TextView) objArr[64], (TextView) objArr[44], (TextView) objArr[3], (TextView) objArr[48], (TextView) objArr[18], (TextView) objArr[59], (ProgressBar) objArr[27], (ImageButton) objArr[21], (TextView) objArr[28], (Button) objArr[61], (Button) objArr[60]);
        this.mDirtyFlags = -1L;
        this.maxLayout.setTag(null);
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
