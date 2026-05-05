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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.seculink.app.R;
import kt.SensorView;
import net.lucode.hackware.magicindicator.MagicIndicator;
import view.DragFloatButton;
import view.MyGlTextureView;
import view.ShadowButton;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityIpcameraFourEyesBindingImpl extends ActivityIpcameraFourEyesBinding {

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
        sViewsWithIds.put(R.id.layout_play, 1);
        sViewsWithIds.put(R.id.timer, 2);
        sViewsWithIds.put(R.id.layout_gun, 3);
        sViewsWithIds.put(R.id.SensorView, 4);
        sViewsWithIds.put(R.id.layout_scroll, 5);
        sViewsWithIds.put(R.id.layout_gun_orientation, 6);
        sViewsWithIds.put(R.id.player_gun1, 7);
        sViewsWithIds.put(R.id.player_gun3, 8);
        sViewsWithIds.put(R.id.player_ball, 9);
        sViewsWithIds.put(R.id.player_gun2, 10);
        sViewsWithIds.put(R.id.scrollview, 11);
        sViewsWithIds.put(R.id.rv_live, 12);
        sViewsWithIds.put(R.id.layout_quality, 13);
        sViewsWithIds.put(R.id.quality_btn, 14);
        sViewsWithIds.put(R.id.tv_ptz, 15);
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
        sViewsWithIds.put(R.id.layout_up_down, 37);
        sViewsWithIds.put(R.id.bt_top, 38);
        sViewsWithIds.put(R.id.iv_1, 39);
        sViewsWithIds.put(R.id.iv_2, 40);
        sViewsWithIds.put(R.id.iv_3, 41);
        sViewsWithIds.put(R.id.iv_4, 42);
        sViewsWithIds.put(R.id.iv_box, 43);
        sViewsWithIds.put(R.id.bt_bottom, 44);
        sViewsWithIds.put(R.id.layout_top, 45);
        sViewsWithIds.put(R.id.iv_back, 46);
        sViewsWithIds.put(R.id.tv_title, 47);
        sViewsWithIds.put(R.id.iv_setting, 48);
        sViewsWithIds.put(R.id.layout_control, 49);
        sViewsWithIds.put(R.id.rl_touch_view, 50);
        sViewsWithIds.put(R.id.iv_charge_4g_flow, 51);
        sViewsWithIds.put(R.id.layout_af, 52);
        sViewsWithIds.put(R.id.bt_zoom_add_btn, 53);
        sViewsWithIds.put(R.id.bt_zoom_reduce_btn, 54);
        sViewsWithIds.put(R.id.layout_center, 55);
        sViewsWithIds.put(R.id.layout_zoom, 56);
        sViewsWithIds.put(R.id.tv_zoom_back, 57);
        sViewsWithIds.put(R.id.zoom_reduce_btn, 58);
        sViewsWithIds.put(R.id.zoom_add_btn, 59);
        sViewsWithIds.put(R.id.focus_reduce_btn, 60);
        sViewsWithIds.put(R.id.focus_add_btn, 61);
        sViewsWithIds.put(R.id.tv_preset, 62);
        sViewsWithIds.put(R.id.et_preset, 63);
        sViewsWithIds.put(R.id.bt_preset_invoke, 64);
        sViewsWithIds.put(R.id.bt_preset_add, 65);
        sViewsWithIds.put(R.id.layout_more, 66);
        sViewsWithIds.put(R.id.topicIndicator, 67);
        sViewsWithIds.put(R.id.line, 68);
        sViewsWithIds.put(R.id.topicViewPager, 69);
        sViewsWithIds.put(R.id.layout_bottom, 70);
        sViewsWithIds.put(R.id.ll_capture, 71);
        sViewsWithIds.put(R.id.capture_btn, 72);
        sViewsWithIds.put(R.id.tv_capture, 73);
        sViewsWithIds.put(R.id.ll_record, 74);
        sViewsWithIds.put(R.id.record_btn, 75);
        sViewsWithIds.put(R.id.tv_record, 76);
        sViewsWithIds.put(R.id.speaker_btn, 77);
        sViewsWithIds.put(R.id.ll_listener, 78);
        sViewsWithIds.put(R.id.listener_btn, 79);
        sViewsWithIds.put(R.id.tv_voice, 80);
        sViewsWithIds.put(R.id.ll_more_double_eye, 81);
        sViewsWithIds.put(R.id.more_image, 82);
        sViewsWithIds.put(R.id.more_text_double_eye, 83);
        sViewsWithIds.put(R.id.full_screen, 84);
        sViewsWithIds.put(R.id.full_night_vision, 85);
        sViewsWithIds.put(R.id.full_switch_window, 86);
        sViewsWithIds.put(R.id.full_sound, 87);
        sViewsWithIds.put(R.id.full_intercom, 88);
        sViewsWithIds.put(R.id.full_video, 89);
        sViewsWithIds.put(R.id.full_camera, 90);
        sViewsWithIds.put(R.id.full_add_zoom, 91);
        sViewsWithIds.put(R.id.full_reduce_zoom, 92);
    }

    public ActivityIpcameraFourEyesBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 93, sIncludes, sViewsWithIds));
    }

    private ActivityIpcameraFourEyesBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (SensorView) objArr[4], (ImageView) objArr[44], (Button) objArr[65], (Button) objArr[64], (ImageView) objArr[38], (Button) objArr[53], (Button) objArr[54], (ImageButton) objArr[72], (EditText) objArr[63], (Button) objArr[61], (Button) objArr[60], (ImageView) objArr[91], (ShadowButton) objArr[90], (ShadowButton) objArr[88], (ShadowButton) objArr[85], (ImageView) objArr[92], (RelativeLayout) objArr[84], (ShadowButton) objArr[87], (ShadowButton) objArr[86], (ShadowButton) objArr[89], (Button) objArr[25], (TextView) objArr[22], (ImageView) objArr[39], (ImageView) objArr[40], (ImageView) objArr[41], (ImageView) objArr[42], (ImageView) objArr[46], (DragFloatButton) objArr[43], (ImageView) objArr[51], (ImageView) objArr[19], (ImageView) objArr[17], (ImageView) objArr[48], (ImageView) objArr[26], (LinearLayout) objArr[52], (LinearLayout) objArr[70], (RelativeLayout) objArr[55], (RelativeLayout) objArr[49], (RelativeLayout) objArr[3], (LinearLayout) objArr[6], (RelativeLayout) objArr[66], (RelativeLayout) objArr[1], (LinearLayout) objArr[13], (RelativeLayout) objArr[5], (RelativeLayout) objArr[45], (LinearLayout) objArr[37], (LinearLayout) objArr[56], (FrameLayout) objArr[33], (View) objArr[68], (ImageButton) objArr[79], (LinearLayout) objArr[71], (ImageView) objArr[20], (LinearLayout) objArr[78], (LinearLayout) objArr[81], (LinearLayout) objArr[74], (RelativeLayout) objArr[0], (ToggleButton) objArr[82], (TextView) objArr[83], (TextView) objArr[23], (MyGlTextureView) objArr[9], (MyGlTextureView) objArr[7], (MyGlTextureView) objArr[10], (MyGlTextureView) objArr[8], (TextView) objArr[16], (TextView) objArr[14], (LinearLayout) objArr[29], (ImageButton) objArr[75], (RelativeLayout) objArr[50], (RecyclerView) objArr[12], (ScrollView) objArr[11], (ImageButton) objArr[77], (TextView) objArr[2], (MagicIndicator) objArr[67], (ViewPager) objArr[69], (TextView) objArr[24], (TextView) objArr[73], (TextView) objArr[30], (TextView) objArr[32], (TextView) objArr[34], (TextView) objArr[35], (TextView) objArr[36], (TextView) objArr[31], (TextView) objArr[62], (TextView) objArr[15], (TextView) objArr[76], (TextView) objArr[47], (TextView) objArr[80], (TextView) objArr[18], (TextView) objArr[57], (ProgressBar) objArr[27], (ImageButton) objArr[21], (TextView) objArr[28], (Button) objArr[59], (Button) objArr[58]);
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
