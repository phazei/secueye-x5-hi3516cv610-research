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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import kt.DrawLineView;
import kt.SensorView;
import view.CircleTooView;
import view.FourPicturesView;
import view.IPCTitleView;
import view.MyGlTextureView;
import view.ShadowButton;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityIpcameraDoubleEyeLayoutBindingImpl extends ActivityIpcameraDoubleEyeLayoutBinding {

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
        sViewsWithIds.put(R.id.player2, 3);
        sViewsWithIds.put(R.id.player, 4);
        sViewsWithIds.put(R.id.iv_light_while2, 5);
        sViewsWithIds.put(R.id.layout_video, 6);
        sViewsWithIds.put(R.id.line_view_item, 7);
        sViewsWithIds.put(R.id.quality_control, 8);
        sViewsWithIds.put(R.id.ll_video_qt, 9);
        sViewsWithIds.put(R.id.quality_btn, 10);
        sViewsWithIds.put(R.id.player_info_tv, 11);
        sViewsWithIds.put(R.id.iv_light_while, 12);
        sViewsWithIds.put(R.id.four_pic_btn, 13);
        sViewsWithIds.put(R.id.four_one_pic, 14);
        sViewsWithIds.put(R.id.ll_full, 15);
        sViewsWithIds.put(R.id.exo_double_eye_btn, 16);
        sViewsWithIds.put(R.id.four_pic, 17);
        sViewsWithIds.put(R.id.drawLineView, 18);
        sViewsWithIds.put(R.id.layout_osd, 19);
        sViewsWithIds.put(R.id.OSD, 20);
        sViewsWithIds.put(R.id.timer, 21);
        sViewsWithIds.put(R.id.SensorView, 22);
        sViewsWithIds.put(R.id.video_play_ibtn, 23);
        sViewsWithIds.put(R.id.ipc_offline_text, 24);
        sViewsWithIds.put(R.id.outline_time, 25);
        sViewsWithIds.put(R.id.traffic_4g_expired, 26);
        sViewsWithIds.put(R.id.immediate_renewal, 27);
        sViewsWithIds.put(R.id.iv_snap, 28);
        sViewsWithIds.put(R.id.video_buffering_bar, 29);
        sViewsWithIds.put(R.id.wakeup_text, 30);
        sViewsWithIds.put(R.id.controller_panel, 31);
        sViewsWithIds.put(R.id.bottomView, 32);
        sViewsWithIds.put(R.id.ll_capture, 33);
        sViewsWithIds.put(R.id.capture_btn, 34);
        sViewsWithIds.put(R.id.tv_capture, 35);
        sViewsWithIds.put(R.id.ll_record, 36);
        sViewsWithIds.put(R.id.record_btn, 37);
        sViewsWithIds.put(R.id.tv_record, 38);
        sViewsWithIds.put(R.id.speaker_btn, 39);
        sViewsWithIds.put(R.id.ll_listener, 40);
        sViewsWithIds.put(R.id.listener_btn, 41);
        sViewsWithIds.put(R.id.tv_voice, 42);
        sViewsWithIds.put(R.id.ll_more_double_eye, 43);
        sViewsWithIds.put(R.id.more_image, 44);
        sViewsWithIds.put(R.id.more_text_double_eye, 45);
        sViewsWithIds.put(R.id.rlcenter, 46);
        sViewsWithIds.put(R.id.rl_touch_view_position, 47);
        sViewsWithIds.put(R.id.ptz_text, 48);
        sViewsWithIds.put(R.id.zoom_text, 49);
        sViewsWithIds.put(R.id.rl_touch_view, 50);
        sViewsWithIds.put(R.id.layout_af, 51);
        sViewsWithIds.put(R.id.bt_zoom_add_btn, 52);
        sViewsWithIds.put(R.id.bt_zoom_reduce_btn, 53);
        sViewsWithIds.put(R.id.ZOOM_view, 54);
        sViewsWithIds.put(R.id.tv_zoom_back, 55);
        sViewsWithIds.put(R.id.zoom_reduce_btn, 56);
        sViewsWithIds.put(R.id.zoom_add_btn, 57);
        sViewsWithIds.put(R.id.focus_reduce_btn, 58);
        sViewsWithIds.put(R.id.focus_add_btn, 59);
        sViewsWithIds.put(R.id.tv_preset, 60);
        sViewsWithIds.put(R.id.et_preset, 61);
        sViewsWithIds.put(R.id.bt_preset_invoke, 62);
        sViewsWithIds.put(R.id.bt_preset_add, 63);
        sViewsWithIds.put(R.id.autor_view, 64);
        sViewsWithIds.put(R.id.relativeLayout, 65);
        sViewsWithIds.put(R.id.back_ll, 66);
        sViewsWithIds.put(R.id.back, 67);
        sViewsWithIds.put(R.id.door_text, 68);
        sViewsWithIds.put(R.id.controller_edit, 69);
        sViewsWithIds.put(R.id.tips, 70);
        sViewsWithIds.put(R.id.button4, 71);
        sViewsWithIds.put(R.id.button3, 72);
        sViewsWithIds.put(R.id.button2, 73);
        sViewsWithIds.put(R.id.button1, 74);
        sViewsWithIds.put(R.id.doorbell, 75);
        sViewsWithIds.put(R.id.scroll, 76);
        sViewsWithIds.put(R.id.ll1, 77);
        sViewsWithIds.put(R.id.image1, 78);
        sViewsWithIds.put(R.id.text1, 79);
        sViewsWithIds.put(R.id.ll2, 80);
        sViewsWithIds.put(R.id.image2, 81);
        sViewsWithIds.put(R.id.text2, 82);
        sViewsWithIds.put(R.id.ll3, 83);
        sViewsWithIds.put(R.id.image3, 84);
        sViewsWithIds.put(R.id.text3, 85);
        sViewsWithIds.put(R.id.ll4, 86);
        sViewsWithIds.put(R.id.image4, 87);
        sViewsWithIds.put(R.id.text4, 88);
        sViewsWithIds.put(R.id.guideline4, 89);
        sViewsWithIds.put(R.id.guideline5, 90);
        sViewsWithIds.put(R.id.change_zoom, 91);
        sViewsWithIds.put(R.id.add_zoom, 92);
        sViewsWithIds.put(R.id.reduce_zoom, 93);
        sViewsWithIds.put(R.id.bt_control, 94);
        sViewsWithIds.put(R.id.bottom_scroll, 95);
        sViewsWithIds.put(R.id.delete_ll, 96);
        sViewsWithIds.put(R.id.ll_bottom, 97);
        sViewsWithIds.put(R.id.ll_service_4g, 98);
        sViewsWithIds.put(R.id.service_4g_btn, 99);
        sViewsWithIds.put(R.id.service_4g_text, 100);
        sViewsWithIds.put(R.id.ll_flips, 101);
        sViewsWithIds.put(R.id.flip_btn, 102);
        sViewsWithIds.put(R.id.cloud_text, 103);
        sViewsWithIds.put(R.id.ll_flip, 104);
        sViewsWithIds.put(R.id.video_btn, 105);
        sViewsWithIds.put(R.id.video_back_text, 106);
        sViewsWithIds.put(R.id.ll_controller, 107);
        sViewsWithIds.put(R.id.controller_btn, 108);
        sViewsWithIds.put(R.id.ll_share, 109);
        sViewsWithIds.put(R.id.share_btn, 110);
        sViewsWithIds.put(R.id.share_text, 111);
        sViewsWithIds.put(R.id.bottom_zoom, 112);
        sViewsWithIds.put(R.id.bottom_zoom_btn, 113);
        sViewsWithIds.put(R.id.ll_more, 114);
        sViewsWithIds.put(R.id.more_btn, 115);
        sViewsWithIds.put(R.id.more_text, 116);
        sViewsWithIds.put(R.id.live_ll, 117);
        sViewsWithIds.put(R.id.live_btn, 118);
        sViewsWithIds.put(R.id.bottom_shop, 119);
        sViewsWithIds.put(R.id.bottom_shop_btn, 120);
        sViewsWithIds.put(R.id.f6225fragment, 121);
        sViewsWithIds.put(R.id.landscape_player, 122);
        sViewsWithIds.put(R.id.full_screen, 123);
        sViewsWithIds.put(R.id.full_night_vision, 124);
        sViewsWithIds.put(R.id.full_switch_window, 125);
        sViewsWithIds.put(R.id.full_sound, 126);
        sViewsWithIds.put(R.id.full_intercom, 127);
        sViewsWithIds.put(R.id.full_video, 128);
        sViewsWithIds.put(R.id.full_camera, 129);
        sViewsWithIds.put(R.id.full_add_zoom, 130);
        sViewsWithIds.put(R.id.full_reduce_zoom, 131);
        sViewsWithIds.put(R.id.clarity, 132);
        sViewsWithIds.put(R.id.quality_dlg, 133);
        sViewsWithIds.put(R.id.tv_h_quality, 134);
        sViewsWithIds.put(R.id.tv_m_quality, 135);
        sViewsWithIds.put(R.id.tv_l_quality, 136);
        sViewsWithIds.put(R.id.light_dlg, 137);
        sViewsWithIds.put(R.id.tv_light1, 138);
        sViewsWithIds.put(R.id.tv_light2, 139);
        sViewsWithIds.put(R.id.tv_light3, 140);
        sViewsWithIds.put(R.id.iv_charge_4g_flow, 141);
    }

    public ActivityIpcameraDoubleEyeLayoutBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 142, sIncludes, sViewsWithIds));
    }

    private ActivityIpcameraDoubleEyeLayoutBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (TextView) objArr[20], (SensorView) objArr[22], (LinearLayout) objArr[54], (ImageView) objArr[92], (LinearLayout) objArr[64], (TextView) objArr[67], (LinearLayout) objArr[66], (LinearLayout) objArr[95], (LinearLayout) objArr[119], (Button) objArr[120], (LinearLayout) objArr[32], (LinearLayout) objArr[112], (Button) objArr[113], (Button) objArr[94], (Button) objArr[63], (Button) objArr[62], (Button) objArr[52], (Button) objArr[53], (ShadowButton) objArr[74], (ShadowButton) objArr[73], (ShadowButton) objArr[72], (ShadowButton) objArr[71], (ImageButton) objArr[34], (LinearLayout) objArr[91], (ConstraintLayout) objArr[132], (TextView) objArr[103], (Button) objArr[108], (ImageView) objArr[69], (ConstraintLayout) objArr[31], (LinearLayout) objArr[96], (TextView) objArr[68], (ShadowButton) objArr[75], (DrawLineView) objArr[18], (EditText) objArr[61], (ToggleButton) objArr[16], (Button) objArr[102], (Button) objArr[59], (Button) objArr[58], (ImageView) objArr[14], (FourPicturesView) objArr[17], (LinearLayout) objArr[13], (FrameLayout) objArr[121], (ImageView) objArr[130], (ShadowButton) objArr[129], (ShadowButton) objArr[127], (ShadowButton) objArr[124], (ImageView) objArr[131], (ConstraintLayout) objArr[123], (ShadowButton) objArr[126], (ShadowButton) objArr[125], (ShadowButton) objArr[128], (Guideline) objArr[89], (Guideline) objArr[90], (ImageView) objArr[78], (ImageView) objArr[81], (ImageView) objArr[84], (ImageView) objArr[87], (Button) objArr[27], (TextView) objArr[24], (ImageView) objArr[141], (ImageView) objArr[12], (ImageView) objArr[5], (ImageView) objArr[28], (ConstraintLayout) objArr[122], (LinearLayout) objArr[51], (LinearLayout) objArr[19], (LinearLayout) objArr[6], (FrameLayout) objArr[137], (CircleTooView) objArr[7], (ImageButton) objArr[41], (Button) objArr[118], (LinearLayout) objArr[117], (LinearLayout) objArr[77], (LinearLayout) objArr[80], (LinearLayout) objArr[83], (LinearLayout) objArr[86], (LinearLayout) objArr[97], (LinearLayout) objArr[33], (LinearLayout) objArr[107], (LinearLayout) objArr[104], (LinearLayout) objArr[101], (ImageView) objArr[15], (LinearLayout) objArr[40], (LinearLayout) objArr[114], (LinearLayout) objArr[43], (LinearLayout) objArr[36], (LinearLayout) objArr[98], (LinearLayout) objArr[109], (LinearLayout) objArr[9], (ConstraintLayout) objArr[0], (Button) objArr[115], (ToggleButton) objArr[44], (TextView) objArr[116], (TextView) objArr[45], (TextView) objArr[25], (MyGlTextureView) objArr[4], (MyGlTextureView) objArr[3], (TextView) objArr[11], (ConstraintLayout) objArr[2], (TextView) objArr[48], (TextView) objArr[10], (RelativeLayout) objArr[8], (FrameLayout) objArr[133], (ImageButton) objArr[37], (ImageView) objArr[93], (RelativeLayout) objArr[65], (RelativeLayout) objArr[50], (LinearLayout) objArr[47], (RelativeLayout) objArr[46], (LinearLayout) objArr[76], (Button) objArr[99], (TextView) objArr[100], (Button) objArr[110], (TextView) objArr[111], (ImageButton) objArr[39], (TextView) objArr[79], (TextView) objArr[82], (TextView) objArr[85], (TextView) objArr[88], (TextView) objArr[21], (TextView) objArr[70], (TextView) objArr[26], (TextView) objArr[35], (TextView) objArr[134], (TextView) objArr[136], (TextView) objArr[138], (TextView) objArr[139], (TextView) objArr[140], (TextView) objArr[135], (TextView) objArr[60], (TextView) objArr[38], (IPCTitleView) objArr[1], (TextView) objArr[42], (TextView) objArr[55], (TextView) objArr[106], (Button) objArr[105], (ProgressBar) objArr[29], (ImageButton) objArr[23], (TextView) objArr[30], (Button) objArr[57], (Button) objArr[56], (TextView) objArr[49]);
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
