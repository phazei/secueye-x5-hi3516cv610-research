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
public class ActivityIpcameraThreeFalseEyesBindingImpl extends ActivityIpcameraThreeFalseEyesBinding {

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
        sViewsWithIds.put(R.id.layout_player_gun1, 10);
        sViewsWithIds.put(R.id.tv_time1, 11);
        sViewsWithIds.put(R.id.player_gun1, 12);
        sViewsWithIds.put(R.id.view_gun, 13);
        sViewsWithIds.put(R.id.layout_player_gun2, 14);
        sViewsWithIds.put(R.id.tv_time2, 15);
        sViewsWithIds.put(R.id.player_gun2, 16);
        sViewsWithIds.put(R.id.view_back, 17);
        sViewsWithIds.put(R.id.iv_night_top, 18);
        sViewsWithIds.put(R.id.player_ball, 19);
        sViewsWithIds.put(R.id.layout_quality, 20);
        sViewsWithIds.put(R.id.quality_btn, 21);
        sViewsWithIds.put(R.id.player_info_tv, 22);
        sViewsWithIds.put(R.id.iv_night_bottom, 23);
        sViewsWithIds.put(R.id.tv_zoom, 24);
        sViewsWithIds.put(R.id.iv_full, 25);
        sViewsWithIds.put(R.id.ll_full, 26);
        sViewsWithIds.put(R.id.video_play_ibtn, 27);
        sViewsWithIds.put(R.id.ipc_offline_text, 28);
        sViewsWithIds.put(R.id.outline_time, 29);
        sViewsWithIds.put(R.id.traffic_4g_expired, 30);
        sViewsWithIds.put(R.id.immediate_renewal, 31);
        sViewsWithIds.put(R.id.iv_snap, 32);
        sViewsWithIds.put(R.id.video_buffering_bar, 33);
        sViewsWithIds.put(R.id.wakeup_text, 34);
        sViewsWithIds.put(R.id.quality_dlg, 35);
        sViewsWithIds.put(R.id.tv_h_quality, 36);
        sViewsWithIds.put(R.id.tv_m_quality, 37);
        sViewsWithIds.put(R.id.tv_l_quality, 38);
        sViewsWithIds.put(R.id.light_dlg, 39);
        sViewsWithIds.put(R.id.tv_light1, 40);
        sViewsWithIds.put(R.id.tv_light2, 41);
        sViewsWithIds.put(R.id.tv_light3, 42);
        sViewsWithIds.put(R.id.layout_control, 43);
        sViewsWithIds.put(R.id.layout_bottom, 44);
        sViewsWithIds.put(R.id.ll_capture, 45);
        sViewsWithIds.put(R.id.capture_btn, 46);
        sViewsWithIds.put(R.id.tv_capture, 47);
        sViewsWithIds.put(R.id.ll_record, 48);
        sViewsWithIds.put(R.id.record_btn, 49);
        sViewsWithIds.put(R.id.tv_record, 50);
        sViewsWithIds.put(R.id.speaker_btn, 51);
        sViewsWithIds.put(R.id.ll_listener, 52);
        sViewsWithIds.put(R.id.listener_btn, 53);
        sViewsWithIds.put(R.id.tv_voice, 54);
        sViewsWithIds.put(R.id.ll_more_double_eye, 55);
        sViewsWithIds.put(R.id.more_image, 56);
        sViewsWithIds.put(R.id.more_text_double_eye, 57);
        sViewsWithIds.put(R.id.layout_center2, 58);
        sViewsWithIds.put(R.id.edit_width, 59);
        sViewsWithIds.put(R.id.edit_height, 60);
        sViewsWithIds.put(R.id.bt_wh, 61);
        sViewsWithIds.put(R.id.edit_zoom, 62);
        sViewsWithIds.put(R.id.bt_zoom, 63);
        sViewsWithIds.put(R.id.bt_reset, 64);
        sViewsWithIds.put(R.id.edit_x, 65);
        sViewsWithIds.put(R.id.edit_y, 66);
        sViewsWithIds.put(R.id.bt_left_move, 67);
        sViewsWithIds.put(R.id.bt_right_move, 68);
        sViewsWithIds.put(R.id.layout_center, 69);
        sViewsWithIds.put(R.id.rl_touch_view, 70);
        sViewsWithIds.put(R.id.layout_af, 71);
        sViewsWithIds.put(R.id.bt_zoom_add_btn, 72);
        sViewsWithIds.put(R.id.bt_zoom_reduce_btn, 73);
        sViewsWithIds.put(R.id.iv_charge_4g_flow, 74);
        sViewsWithIds.put(R.id.layout_zoom, 75);
        sViewsWithIds.put(R.id.tv_zoom_back, 76);
        sViewsWithIds.put(R.id.zoom_reduce_btn, 77);
        sViewsWithIds.put(R.id.zoom_add_btn, 78);
        sViewsWithIds.put(R.id.focus_reduce_btn, 79);
        sViewsWithIds.put(R.id.focus_add_btn, 80);
        sViewsWithIds.put(R.id.tv_preset, 81);
        sViewsWithIds.put(R.id.et_preset, 82);
        sViewsWithIds.put(R.id.bt_preset_invoke, 83);
        sViewsWithIds.put(R.id.bt_preset_add, 84);
        sViewsWithIds.put(R.id.layout_more, 85);
        sViewsWithIds.put(R.id.topicIndicator, 86);
        sViewsWithIds.put(R.id.line, 87);
        sViewsWithIds.put(R.id.topicViewPager, 88);
        sViewsWithIds.put(R.id.full_screen, 89);
        sViewsWithIds.put(R.id.full_night_vision, 90);
        sViewsWithIds.put(R.id.full_switch_window, 91);
        sViewsWithIds.put(R.id.full_sound, 92);
        sViewsWithIds.put(R.id.full_intercom, 93);
        sViewsWithIds.put(R.id.full_video, 94);
        sViewsWithIds.put(R.id.full_camera, 95);
        sViewsWithIds.put(R.id.full_add_zoom, 96);
        sViewsWithIds.put(R.id.full_reduce_zoom, 97);
    }

    public ActivityIpcameraThreeFalseEyesBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 98, sIncludes, sViewsWithIds));
    }

    private ActivityIpcameraThreeFalseEyesBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (SensorView) objArr[8], (Button) objArr[67], (Button) objArr[84], (Button) objArr[83], (Button) objArr[64], (Button) objArr[68], (Button) objArr[61], (Button) objArr[63], (Button) objArr[72], (Button) objArr[73], (ImageButton) objArr[46], (EditText) objArr[60], (EditText) objArr[59], (EditText) objArr[65], (EditText) objArr[66], (EditText) objArr[62], (EditText) objArr[82], (Button) objArr[80], (Button) objArr[79], (ImageView) objArr[96], (ShadowButton) objArr[95], (ShadowButton) objArr[93], (ShadowButton) objArr[90], (ImageView) objArr[97], (RelativeLayout) objArr[89], (ShadowButton) objArr[92], (ShadowButton) objArr[91], (ShadowButton) objArr[94], (Button) objArr[31], (TextView) objArr[28], (ImageView) objArr[2], (ImageView) objArr[74], (ImageView) objArr[25], (ImageView) objArr[23], (ImageView) objArr[18], (ImageView) objArr[4], (ImageView) objArr[32], (LinearLayout) objArr[71], (LinearLayout) objArr[44], (RelativeLayout) objArr[69], (RelativeLayout) objArr[58], (RelativeLayout) objArr[43], (RelativeLayout) objArr[7], (LinearLayout) objArr[9], (RelativeLayout) objArr[85], (RelativeLayout) objArr[5], (RelativeLayout) objArr[10], (RelativeLayout) objArr[14], (LinearLayout) objArr[20], (RelativeLayout) objArr[1], (LinearLayout) objArr[75], (FrameLayout) objArr[39], (View) objArr[87], (ImageButton) objArr[53], (LinearLayout) objArr[45], (ImageView) objArr[26], (LinearLayout) objArr[52], (LinearLayout) objArr[55], (LinearLayout) objArr[48], (RelativeLayout) objArr[0], (ToggleButton) objArr[56], (TextView) objArr[57], (TextView) objArr[29], (MyGlTextureView) objArr[19], (MyGlTextureView) objArr[12], (MyGlTextureView) objArr[16], (TextView) objArr[22], (TextView) objArr[21], (LinearLayout) objArr[35], (ImageButton) objArr[49], (RelativeLayout) objArr[70], (ImageButton) objArr[51], (TextView) objArr[6], (MagicIndicator) objArr[86], (ViewPager) objArr[88], (TextView) objArr[30], (TextView) objArr[47], (TextView) objArr[36], (TextView) objArr[38], (TextView) objArr[40], (TextView) objArr[41], (TextView) objArr[42], (TextView) objArr[37], (TextView) objArr[81], (TextView) objArr[50], (TextView) objArr[11], (TextView) objArr[15], (TextView) objArr[3], (TextView) objArr[54], (TextView) objArr[24], (TextView) objArr[76], (ProgressBar) objArr[33], (ImageButton) objArr[27], (View) objArr[17], (View) objArr[13], (TextView) objArr[34], (Button) objArr[78], (Button) objArr[77]);
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
