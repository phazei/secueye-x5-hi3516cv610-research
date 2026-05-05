package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.ViewPager;
import com.seculink.app.R;
import kt.SensorView;
import net.lucode.hackware.magicindicator.MagicIndicator;
import view.MyGlTextureView;
import view.ShadowButton;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityIpcameraThreeEyesBinding extends ViewDataBinding {

    @NonNull
    public final SensorView SensorView;

    @NonNull
    public final Button btPresetAdd;

    @NonNull
    public final Button btPresetInvoke;

    @NonNull
    public final Button btZoomAddBtn;

    @NonNull
    public final Button btZoomReduceBtn;

    @NonNull
    public final ImageButton captureBtn;

    @NonNull
    public final EditText etPreset;

    @NonNull
    public final Button focusAddBtn;

    @NonNull
    public final Button focusReduceBtn;

    @NonNull
    public final ImageView fullAddZoom;

    @NonNull
    public final ShadowButton fullCamera;

    @NonNull
    public final ShadowButton fullIntercom;

    @NonNull
    public final ShadowButton fullNightVision;

    @NonNull
    public final ImageView fullReduceZoom;

    @NonNull
    public final RelativeLayout fullScreen;

    @NonNull
    public final ShadowButton fullSound;

    @NonNull
    public final ShadowButton fullSwitchWindow;

    @NonNull
    public final ShadowButton fullVideo;

    @NonNull
    public final Button immediateRenewal;

    @NonNull
    public final TextView ipcOfflineText;

    @NonNull
    public final ImageView ivBack;

    @NonNull
    public final ImageView ivCharge4gFlow;

    @NonNull
    public final ImageView ivFull;

    @NonNull
    public final ImageView ivNightBottom;

    @NonNull
    public final ImageView ivNightTop;

    @NonNull
    public final ImageView ivSetting;

    @NonNull
    public final ImageView ivSnap;

    @NonNull
    public final LinearLayout layoutAf;

    @NonNull
    public final LinearLayout layoutBottom;

    @NonNull
    public final RelativeLayout layoutCenter;

    @NonNull
    public final RelativeLayout layoutControl;

    @NonNull
    public final RelativeLayout layoutGun;

    @NonNull
    public final LinearLayout layoutGunOrientation;

    @NonNull
    public final RelativeLayout layoutMore;

    @NonNull
    public final RelativeLayout layoutPlay;

    @NonNull
    public final LinearLayout layoutQuality;

    @NonNull
    public final RelativeLayout layoutTop;

    @NonNull
    public final LinearLayout layoutZoom;

    @NonNull
    public final FrameLayout lightDlg;

    @NonNull
    public final View line;

    @NonNull
    public final ImageButton listenerBtn;

    @NonNull
    public final LinearLayout llCapture;

    @NonNull
    public final ImageView llFull;

    @NonNull
    public final LinearLayout llListener;

    @NonNull
    public final LinearLayout llMoreDoubleEye;

    @NonNull
    public final LinearLayout llRecord;

    @NonNull
    public final RelativeLayout maxLayout;

    @NonNull
    public final ToggleButton moreImage;

    @NonNull
    public final TextView moreTextDoubleEye;

    @NonNull
    public final TextView outlineTime;

    @NonNull
    public final MyGlTextureView playerBall;

    @NonNull
    public final MyGlTextureView playerGun1;

    @NonNull
    public final MyGlTextureView playerGun2;

    @NonNull
    public final TextView playerInfoTv;

    @NonNull
    public final TextView qualityBtn;

    @NonNull
    public final LinearLayout qualityDlg;

    @NonNull
    public final ImageButton recordBtn;

    @NonNull
    public final RelativeLayout rlTouchView;

    @NonNull
    public final ImageButton speakerBtn;

    @NonNull
    public final TextView timer;

    @NonNull
    public final MagicIndicator topicIndicator;

    @NonNull
    public final ViewPager topicViewPager;

    @NonNull
    public final TextView traffic4gExpired;

    @NonNull
    public final TextView tvCapture;

    @NonNull
    public final TextView tvHQuality;

    @NonNull
    public final TextView tvLQuality;

    @NonNull
    public final TextView tvLight1;

    @NonNull
    public final TextView tvLight2;

    @NonNull
    public final TextView tvLight3;

    @NonNull
    public final TextView tvMQuality;

    @NonNull
    public final TextView tvPreset;

    @NonNull
    public final TextView tvRecord;

    @NonNull
    public final TextView tvTitle;

    @NonNull
    public final TextView tvVoice;

    @NonNull
    public final TextView tvZoom;

    @NonNull
    public final TextView tvZoomBack;

    @NonNull
    public final ProgressBar videoBufferingBar;

    @NonNull
    public final ImageButton videoPlayIbtn;

    @NonNull
    public final TextView wakeupText;

    @NonNull
    public final Button zoomAddBtn;

    @NonNull
    public final Button zoomReduceBtn;

    protected ActivityIpcameraThreeEyesBinding(DataBindingComponent dataBindingComponent, View view2, int i, SensorView sensorView, Button button, Button button2, Button button3, Button button4, ImageButton imageButton, EditText editText, Button button5, Button button6, ImageView imageView, ShadowButton shadowButton, ShadowButton shadowButton2, ShadowButton shadowButton3, ImageView imageView2, RelativeLayout relativeLayout, ShadowButton shadowButton4, ShadowButton shadowButton5, ShadowButton shadowButton6, Button button7, TextView textView, ImageView imageView3, ImageView imageView4, ImageView imageView5, ImageView imageView6, ImageView imageView7, ImageView imageView8, ImageView imageView9, LinearLayout linearLayout, LinearLayout linearLayout2, RelativeLayout relativeLayout2, RelativeLayout relativeLayout3, RelativeLayout relativeLayout4, LinearLayout linearLayout3, RelativeLayout relativeLayout5, RelativeLayout relativeLayout6, LinearLayout linearLayout4, RelativeLayout relativeLayout7, LinearLayout linearLayout5, FrameLayout frameLayout, View view3, ImageButton imageButton2, LinearLayout linearLayout6, ImageView imageView10, LinearLayout linearLayout7, LinearLayout linearLayout8, LinearLayout linearLayout9, RelativeLayout relativeLayout8, ToggleButton toggleButton, TextView textView2, TextView textView3, MyGlTextureView myGlTextureView, MyGlTextureView myGlTextureView2, MyGlTextureView myGlTextureView3, TextView textView4, TextView textView5, LinearLayout linearLayout10, ImageButton imageButton3, RelativeLayout relativeLayout9, ImageButton imageButton4, TextView textView6, MagicIndicator magicIndicator, ViewPager viewPager, TextView textView7, TextView textView8, TextView textView9, TextView textView10, TextView textView11, TextView textView12, TextView textView13, TextView textView14, TextView textView15, TextView textView16, TextView textView17, TextView textView18, TextView textView19, TextView textView20, ProgressBar progressBar, ImageButton imageButton5, TextView textView21, Button button8, Button button9) {
        super(dataBindingComponent, view2, i);
        this.SensorView = sensorView;
        this.btPresetAdd = button;
        this.btPresetInvoke = button2;
        this.btZoomAddBtn = button3;
        this.btZoomReduceBtn = button4;
        this.captureBtn = imageButton;
        this.etPreset = editText;
        this.focusAddBtn = button5;
        this.focusReduceBtn = button6;
        this.fullAddZoom = imageView;
        this.fullCamera = shadowButton;
        this.fullIntercom = shadowButton2;
        this.fullNightVision = shadowButton3;
        this.fullReduceZoom = imageView2;
        this.fullScreen = relativeLayout;
        this.fullSound = shadowButton4;
        this.fullSwitchWindow = shadowButton5;
        this.fullVideo = shadowButton6;
        this.immediateRenewal = button7;
        this.ipcOfflineText = textView;
        this.ivBack = imageView3;
        this.ivCharge4gFlow = imageView4;
        this.ivFull = imageView5;
        this.ivNightBottom = imageView6;
        this.ivNightTop = imageView7;
        this.ivSetting = imageView8;
        this.ivSnap = imageView9;
        this.layoutAf = linearLayout;
        this.layoutBottom = linearLayout2;
        this.layoutCenter = relativeLayout2;
        this.layoutControl = relativeLayout3;
        this.layoutGun = relativeLayout4;
        this.layoutGunOrientation = linearLayout3;
        this.layoutMore = relativeLayout5;
        this.layoutPlay = relativeLayout6;
        this.layoutQuality = linearLayout4;
        this.layoutTop = relativeLayout7;
        this.layoutZoom = linearLayout5;
        this.lightDlg = frameLayout;
        this.line = view3;
        this.listenerBtn = imageButton2;
        this.llCapture = linearLayout6;
        this.llFull = imageView10;
        this.llListener = linearLayout7;
        this.llMoreDoubleEye = linearLayout8;
        this.llRecord = linearLayout9;
        this.maxLayout = relativeLayout8;
        this.moreImage = toggleButton;
        this.moreTextDoubleEye = textView2;
        this.outlineTime = textView3;
        this.playerBall = myGlTextureView;
        this.playerGun1 = myGlTextureView2;
        this.playerGun2 = myGlTextureView3;
        this.playerInfoTv = textView4;
        this.qualityBtn = textView5;
        this.qualityDlg = linearLayout10;
        this.recordBtn = imageButton3;
        this.rlTouchView = relativeLayout9;
        this.speakerBtn = imageButton4;
        this.timer = textView6;
        this.topicIndicator = magicIndicator;
        this.topicViewPager = viewPager;
        this.traffic4gExpired = textView7;
        this.tvCapture = textView8;
        this.tvHQuality = textView9;
        this.tvLQuality = textView10;
        this.tvLight1 = textView11;
        this.tvLight2 = textView12;
        this.tvLight3 = textView13;
        this.tvMQuality = textView14;
        this.tvPreset = textView15;
        this.tvRecord = textView16;
        this.tvTitle = textView17;
        this.tvVoice = textView18;
        this.tvZoom = textView19;
        this.tvZoomBack = textView20;
        this.videoBufferingBar = progressBar;
        this.videoPlayIbtn = imageButton5;
        this.wakeupText = textView21;
        this.zoomAddBtn = button8;
        this.zoomReduceBtn = button9;
    }

    @NonNull
    public static ActivityIpcameraThreeEyesBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityIpcameraThreeEyesBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityIpcameraThreeEyesBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ipcamera_three_eyes, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityIpcameraThreeEyesBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityIpcameraThreeEyesBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityIpcameraThreeEyesBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ipcamera_three_eyes, null, false, dataBindingComponent);
    }

    public static ActivityIpcameraThreeEyesBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityIpcameraThreeEyesBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityIpcameraThreeEyesBinding) bind(dataBindingComponent, view2, R.layout.activity_ipcamera_three_eyes);
    }
}
