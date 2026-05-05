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
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.seculink.app.R;
import kt.DrawLineView;
import kt.SensorView;
import view.CircleTooView;
import view.FourPicturesView;
import view.IPCTitleView;
import view.LineHorizontalView;
import view.MyGlTextureView;
import view.ShadowButton;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityIpcameraLayoutBindingImpl extends ActivityIpcameraLayoutBinding {

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
        sViewsWithIds.put(R.id.line_view_item, 4);
        sViewsWithIds.put(R.id.four_pic, 5);
        sViewsWithIds.put(R.id.drawLineView, 6);
        sViewsWithIds.put(R.id.layout_osd, 7);
        sViewsWithIds.put(R.id.tv_osd, 8);
        sViewsWithIds.put(R.id.timer, 9);
        sViewsWithIds.put(R.id.SensorView, 10);
        sViewsWithIds.put(R.id.lineview, 11);
        sViewsWithIds.put(R.id.video_play_ibtn, 12);
        sViewsWithIds.put(R.id.ipc_offline_text, 13);
        sViewsWithIds.put(R.id.outline_time, 14);
        sViewsWithIds.put(R.id.traffic_4g_expired, 15);
        sViewsWithIds.put(R.id.immediate_renewal, 16);
        sViewsWithIds.put(R.id.iv_snap, 17);
        sViewsWithIds.put(R.id.video_buffering_bar, 18);
        sViewsWithIds.put(R.id.wakeup_text, 19);
        sViewsWithIds.put(R.id.quality_control, 20);
        sViewsWithIds.put(R.id.ll_video_qt, 21);
        sViewsWithIds.put(R.id.ll_quality, 22);
        sViewsWithIds.put(R.id.quality_btn1, 23);
        sViewsWithIds.put(R.id.quality_btn2, 24);
        sViewsWithIds.put(R.id.quality_btn3, 25);
        sViewsWithIds.put(R.id.player_info_tv2, 26);
        sViewsWithIds.put(R.id.left_device, 27);
        sViewsWithIds.put(R.id.right_device, 28);
        sViewsWithIds.put(R.id.iv_light_while, 29);
        sViewsWithIds.put(R.id.four_pic_btn, 30);
        sViewsWithIds.put(R.id.four_one_pic, 31);
        sViewsWithIds.put(R.id.ipc_line1, 32);
        sViewsWithIds.put(R.id.controller_panel, 33);
        sViewsWithIds.put(R.id.bottomView, 34);
        sViewsWithIds.put(R.id.ll_capture, 35);
        sViewsWithIds.put(R.id.capture_btn, 36);
        sViewsWithIds.put(R.id.tv_capture, 37);
        sViewsWithIds.put(R.id.ll_record, 38);
        sViewsWithIds.put(R.id.record_btn, 39);
        sViewsWithIds.put(R.id.tv_record, 40);
        sViewsWithIds.put(R.id.speaker_btn, 41);
        sViewsWithIds.put(R.id.ll_listener, 42);
        sViewsWithIds.put(R.id.listener_btn, 43);
        sViewsWithIds.put(R.id.tv_voice, 44);
        sViewsWithIds.put(R.id.ll_zoom, 45);
        sViewsWithIds.put(R.id.exo_zoom_tbtn, 46);
        sViewsWithIds.put(R.id.full_text, 47);
        sViewsWithIds.put(R.id.rlcenter, 48);
        sViewsWithIds.put(R.id.rl_touch_view_position, 49);
        sViewsWithIds.put(R.id.tab_layout, 50);
        sViewsWithIds.put(R.id.tab_ptz, 51);
        sViewsWithIds.put(R.id.rl_touch_view, 52);
        sViewsWithIds.put(R.id.ZOOM_view, 53);
        sViewsWithIds.put(R.id.zoom_reduce_btn, 54);
        sViewsWithIds.put(R.id.zoom_add_btn, 55);
        sViewsWithIds.put(R.id.focus_reduce_btn, 56);
        sViewsWithIds.put(R.id.focus_add_btn, 57);
        sViewsWithIds.put(R.id.tv_preset, 58);
        sViewsWithIds.put(R.id.et_preset, 59);
        sViewsWithIds.put(R.id.bt_preset_invoke, 60);
        sViewsWithIds.put(R.id.bt_preset_add, 61);
        sViewsWithIds.put(R.id.autor_view, 62);
        sViewsWithIds.put(R.id.relativeLayout, 63);
        sViewsWithIds.put(R.id.back_ll, 64);
        sViewsWithIds.put(R.id.back, 65);
        sViewsWithIds.put(R.id.door_text, 66);
        sViewsWithIds.put(R.id.controller_edit, 67);
        sViewsWithIds.put(R.id.tips, 68);
        sViewsWithIds.put(R.id.button4, 69);
        sViewsWithIds.put(R.id.button3, 70);
        sViewsWithIds.put(R.id.button2, 71);
        sViewsWithIds.put(R.id.button1, 72);
        sViewsWithIds.put(R.id.doorbell, 73);
        sViewsWithIds.put(R.id.scroll, 74);
        sViewsWithIds.put(R.id.ll1, 75);
        sViewsWithIds.put(R.id.image1, 76);
        sViewsWithIds.put(R.id.text1, 77);
        sViewsWithIds.put(R.id.ll2, 78);
        sViewsWithIds.put(R.id.image2, 79);
        sViewsWithIds.put(R.id.text2, 80);
        sViewsWithIds.put(R.id.ll3, 81);
        sViewsWithIds.put(R.id.image3, 82);
        sViewsWithIds.put(R.id.text3, 83);
        sViewsWithIds.put(R.id.ll4, 84);
        sViewsWithIds.put(R.id.image4, 85);
        sViewsWithIds.put(R.id.text4, 86);
        sViewsWithIds.put(R.id.guideline4, 87);
        sViewsWithIds.put(R.id.guideline5, 88);
        sViewsWithIds.put(R.id.button5, 89);
        sViewsWithIds.put(R.id.button6, 90);
        sViewsWithIds.put(R.id.change_zoom, 91);
        sViewsWithIds.put(R.id.add_zoom, 92);
        sViewsWithIds.put(R.id.reduce_zoom, 93);
        sViewsWithIds.put(R.id.bt_control, 94);
        sViewsWithIds.put(R.id.iv_charge_4g_flow, 95);
        sViewsWithIds.put(R.id.layout_face_recognition, 96);
        sViewsWithIds.put(R.id.layout_face_model, 97);
        sViewsWithIds.put(R.id.et_enter, 98);
        sViewsWithIds.put(R.id.tv_enter, 99);
        sViewsWithIds.put(R.id.tv_enter1, 100);
        sViewsWithIds.put(R.id.tv_stop_enter, 101);
        sViewsWithIds.put(R.id.tv_delete, 102);
        sViewsWithIds.put(R.id.tv_delete_all, 103);
        sViewsWithIds.put(R.id.tv_query, 104);
        sViewsWithIds.put(R.id.layout_face_query, 105);
        sViewsWithIds.put(R.id.tv_enter_model, 106);
        sViewsWithIds.put(R.id.tv_face_data, 107);
        sViewsWithIds.put(R.id.rv_face, 108);
        sViewsWithIds.put(R.id.bottom_scroll, 109);
        sViewsWithIds.put(R.id.delete_ll, 110);
        sViewsWithIds.put(R.id.ll_bottom, 111);
        sViewsWithIds.put(R.id.layout_cloud_playback, 112);
        sViewsWithIds.put(R.id.flip_btn, 113);
        sViewsWithIds.put(R.id.cloud_text, 114);
        sViewsWithIds.put(R.id.ll_flip, 115);
        sViewsWithIds.put(R.id.video_btn, 116);
        sViewsWithIds.put(R.id.video_back_text, 117);
        sViewsWithIds.put(R.id.ll_controller, 118);
        sViewsWithIds.put(R.id.controller_btn, 119);
        sViewsWithIds.put(R.id.ll_share, 120);
        sViewsWithIds.put(R.id.share_btn, 121);
        sViewsWithIds.put(R.id.share_text, 122);
        sViewsWithIds.put(R.id.bottom_zoom, 123);
        sViewsWithIds.put(R.id.bottom_zoom_btn, 124);
        sViewsWithIds.put(R.id.ll_more, 125);
        sViewsWithIds.put(R.id.more_btn, 126);
        sViewsWithIds.put(R.id.more_text, 127);
        sViewsWithIds.put(R.id.live_ll, 128);
        sViewsWithIds.put(R.id.live_btn, 129);
        sViewsWithIds.put(R.id.bottom_shop, 130);
        sViewsWithIds.put(R.id.bottom_shop_btn, 131);
        sViewsWithIds.put(R.id.f6225fragment, 132);
        sViewsWithIds.put(R.id.landscape_player, 133);
        sViewsWithIds.put(R.id.full_screen, 134);
        sViewsWithIds.put(R.id.full_night_vision, 135);
        sViewsWithIds.put(R.id.full_sound, 136);
        sViewsWithIds.put(R.id.full_intercom, 137);
        sViewsWithIds.put(R.id.full_video, 138);
        sViewsWithIds.put(R.id.full_camera, 139);
        sViewsWithIds.put(R.id.full_add_zoom, 140);
        sViewsWithIds.put(R.id.full_reduce_zoom, 141);
        sViewsWithIds.put(R.id.clarity, 142);
        sViewsWithIds.put(R.id.quality_dlg, 143);
        sViewsWithIds.put(R.id.tv_h_quality, 144);
        sViewsWithIds.put(R.id.tv_m_quality, 145);
        sViewsWithIds.put(R.id.tv_l_quality, 146);
        sViewsWithIds.put(R.id.light_dlg, 147);
        sViewsWithIds.put(R.id.tv_light1, 148);
        sViewsWithIds.put(R.id.tv_light2, 149);
        sViewsWithIds.put(R.id.tv_light3, 150);
    }

    public ActivityIpcameraLayoutBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 151, sIncludes, sViewsWithIds));
    }

    private ActivityIpcameraLayoutBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (SensorView) objArr[10], (LinearLayout) objArr[53], (ImageView) objArr[92], (LinearLayout) objArr[62], (TextView) objArr[65], (LinearLayout) objArr[64], (LinearLayout) objArr[109], (LinearLayout) objArr[130], (Button) objArr[131], (LinearLayout) objArr[34], (LinearLayout) objArr[123], (Button) objArr[124], (Button) objArr[94], (Button) objArr[61], (Button) objArr[60], (ShadowButton) objArr[72], (ShadowButton) objArr[71], (ShadowButton) objArr[70], (ShadowButton) objArr[69], (LinearLayout) objArr[89], (LinearLayout) objArr[90], (ImageButton) objArr[36], (LinearLayout) objArr[91], (ConstraintLayout) objArr[142], (TextView) objArr[114], (Button) objArr[119], (ImageView) objArr[67], (ConstraintLayout) objArr[33], (LinearLayout) objArr[110], (TextView) objArr[66], (ShadowButton) objArr[73], (DrawLineView) objArr[6], (EditText) objArr[98], (EditText) objArr[59], (ToggleButton) objArr[46], (Button) objArr[113], (Button) objArr[57], (Button) objArr[56], (ImageView) objArr[31], (FourPicturesView) objArr[5], (LinearLayout) objArr[30], (FrameLayout) objArr[132], (ImageView) objArr[140], (ShadowButton) objArr[139], (ShadowButton) objArr[137], (ShadowButton) objArr[135], (ImageView) objArr[141], (ConstraintLayout) objArr[134], (ShadowButton) objArr[136], (TextView) objArr[47], (ShadowButton) objArr[138], (Guideline) objArr[87], (Guideline) objArr[88], (ImageView) objArr[76], (ImageView) objArr[79], (ImageView) objArr[82], (ImageView) objArr[85], (Button) objArr[16], (Guideline) objArr[32], (TextView) objArr[13], (ImageView) objArr[95], (ImageView) objArr[29], (ImageView) objArr[17], (ConstraintLayout) objArr[133], (LinearLayout) objArr[112], (LinearLayout) objArr[97], (LinearLayout) objArr[105], (LinearLayout) objArr[96], (ConstraintLayout) objArr[0], (LinearLayout) objArr[7], (ImageView) objArr[27], (FrameLayout) objArr[147], (CircleTooView) objArr[4], (LineHorizontalView) objArr[11], (ImageButton) objArr[43], (Button) objArr[129], (LinearLayout) objArr[128], (LinearLayout) objArr[75], (LinearLayout) objArr[78], (LinearLayout) objArr[81], (LinearLayout) objArr[84], (LinearLayout) objArr[111], (LinearLayout) objArr[35], (LinearLayout) objArr[118], (LinearLayout) objArr[115], (LinearLayout) objArr[42], (LinearLayout) objArr[125], (LinearLayout) objArr[22], (LinearLayout) objArr[38], (LinearLayout) objArr[120], (LinearLayout) objArr[21], (LinearLayout) objArr[45], (Button) objArr[126], (TextView) objArr[127], (TextView) objArr[14], (MyGlTextureView) objArr[3], (TextView) objArr[26], (ConstraintLayout) objArr[2], (TextView) objArr[23], (TextView) objArr[24], (TextView) objArr[25], (RelativeLayout) objArr[20], (FrameLayout) objArr[143], (ImageButton) objArr[39], (ImageView) objArr[93], (RelativeLayout) objArr[63], (ImageView) objArr[28], (LinearLayout) objArr[52], (LinearLayout) objArr[49], (RelativeLayout) objArr[48], (RecyclerView) objArr[108], (LinearLayout) objArr[74], (Button) objArr[121], (TextView) objArr[122], (ImageButton) objArr[41], (TabLayout) objArr[50], (TabItem) objArr[51], (TextView) objArr[77], (TextView) objArr[80], (TextView) objArr[83], (TextView) objArr[86], (TextView) objArr[9], (TextView) objArr[68], (TextView) objArr[15], (TextView) objArr[37], (TextView) objArr[102], (TextView) objArr[103], (TextView) objArr[99], (TextView) objArr[100], (TextView) objArr[106], (TextView) objArr[107], (TextView) objArr[144], (TextView) objArr[146], (TextView) objArr[148], (TextView) objArr[149], (TextView) objArr[150], (TextView) objArr[145], (TextView) objArr[8], (TextView) objArr[58], (TextView) objArr[104], (TextView) objArr[40], (TextView) objArr[101], (IPCTitleView) objArr[1], (TextView) objArr[44], (TextView) objArr[117], (Button) objArr[116], (ProgressBar) objArr[18], (ImageButton) objArr[12], (TextView) objArr[19], (Button) objArr[55], (Button) objArr[54]);
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
