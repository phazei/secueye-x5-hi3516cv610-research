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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
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
public abstract class ActivityIpcameraDoubleEyeLayoutBinding extends ViewDataBinding {

    @NonNull
    public final TextView OSD;

    @NonNull
    public final SensorView SensorView;

    @NonNull
    public final LinearLayout ZOOMView;

    @NonNull
    public final ImageView addZoom;

    @NonNull
    public final LinearLayout autorView;

    @NonNull
    public final TextView back;

    @NonNull
    public final LinearLayout backLl;

    @NonNull
    public final LinearLayout bottomScroll;

    @NonNull
    public final LinearLayout bottomShop;

    @NonNull
    public final Button bottomShopBtn;

    @NonNull
    public final LinearLayout bottomView;

    @NonNull
    public final LinearLayout bottomZoom;

    @NonNull
    public final Button bottomZoomBtn;

    @NonNull
    public final Button btControl;

    @NonNull
    public final Button btPresetAdd;

    @NonNull
    public final Button btPresetInvoke;

    @NonNull
    public final Button btZoomAddBtn;

    @NonNull
    public final Button btZoomReduceBtn;

    @NonNull
    public final ShadowButton button1;

    @NonNull
    public final ShadowButton button2;

    @NonNull
    public final ShadowButton button3;

    @NonNull
    public final ShadowButton button4;

    @NonNull
    public final ImageButton captureBtn;

    @NonNull
    public final LinearLayout changeZoom;

    @NonNull
    public final ConstraintLayout clarity;

    @NonNull
    public final TextView cloudText;

    @NonNull
    public final Button controllerBtn;

    @NonNull
    public final ImageView controllerEdit;

    @NonNull
    public final ConstraintLayout controllerPanel;

    @NonNull
    public final LinearLayout deleteLl;

    @NonNull
    public final TextView doorText;

    @NonNull
    public final ShadowButton doorbell;

    @NonNull
    public final DrawLineView drawLineView;

    @NonNull
    public final EditText etPreset;

    @NonNull
    public final ToggleButton exoDoubleEyeBtn;

    @NonNull
    public final Button flipBtn;

    @NonNull
    public final Button focusAddBtn;

    @NonNull
    public final Button focusReduceBtn;

    @NonNull
    public final ImageView fourOnePic;

    @NonNull
    public final FourPicturesView fourPic;

    @NonNull
    public final LinearLayout fourPicBtn;

    /* JADX INFO: renamed from: fragment, reason: collision with root package name */
    @NonNull
    public final FrameLayout f6228fragment;

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
    public final ConstraintLayout fullScreen;

    @NonNull
    public final ShadowButton fullSound;

    @NonNull
    public final ShadowButton fullSwitchWindow;

    @NonNull
    public final ShadowButton fullVideo;

    @NonNull
    public final Guideline guideline4;

    @NonNull
    public final Guideline guideline5;

    @NonNull
    public final ImageView image1;

    @NonNull
    public final ImageView image2;

    @NonNull
    public final ImageView image3;

    @NonNull
    public final ImageView image4;

    @NonNull
    public final Button immediateRenewal;

    @NonNull
    public final TextView ipcOfflineText;

    @NonNull
    public final ImageView ivCharge4gFlow;

    @NonNull
    public final ImageView ivLightWhile;

    @NonNull
    public final ImageView ivLightWhile2;

    @NonNull
    public final ImageView ivSnap;

    @NonNull
    public final ConstraintLayout landscapePlayer;

    @NonNull
    public final LinearLayout layoutAf;

    @NonNull
    public final LinearLayout layoutOsd;

    @NonNull
    public final LinearLayout layoutVideo;

    @NonNull
    public final FrameLayout lightDlg;

    @NonNull
    public final CircleTooView lineViewItem;

    @NonNull
    public final ImageButton listenerBtn;

    @NonNull
    public final Button liveBtn;

    @NonNull
    public final LinearLayout liveLl;

    @NonNull
    public final LinearLayout ll1;

    @NonNull
    public final LinearLayout ll2;

    @NonNull
    public final LinearLayout ll3;

    @NonNull
    public final LinearLayout ll4;

    @NonNull
    public final LinearLayout llBottom;

    @NonNull
    public final LinearLayout llCapture;

    @NonNull
    public final LinearLayout llController;

    @NonNull
    public final LinearLayout llFlip;

    @NonNull
    public final LinearLayout llFlips;

    @NonNull
    public final ImageView llFull;

    @NonNull
    public final LinearLayout llListener;

    @NonNull
    public final LinearLayout llMore;

    @NonNull
    public final LinearLayout llMoreDoubleEye;

    @NonNull
    public final LinearLayout llRecord;

    @NonNull
    public final LinearLayout llService4g;

    @NonNull
    public final LinearLayout llShare;

    @NonNull
    public final LinearLayout llVideoQt;

    @NonNull
    public final ConstraintLayout maxLayout;

    @NonNull
    public final Button moreBtn;

    @NonNull
    public final ToggleButton moreImage;

    @NonNull
    public final TextView moreText;

    @NonNull
    public final TextView moreTextDoubleEye;

    @NonNull
    public final TextView outlineTime;

    @NonNull
    public final MyGlTextureView player;

    @NonNull
    public final MyGlTextureView player2;

    @NonNull
    public final TextView playerInfoTv;

    @NonNull
    public final ConstraintLayout portraitPlayer;

    @NonNull
    public final TextView ptzText;

    @NonNull
    public final TextView qualityBtn;

    @NonNull
    public final RelativeLayout qualityControl;

    @NonNull
    public final FrameLayout qualityDlg;

    @NonNull
    public final ImageButton recordBtn;

    @NonNull
    public final ImageView reduceZoom;

    @NonNull
    public final RelativeLayout relativeLayout;

    @NonNull
    public final RelativeLayout rlTouchView;

    @NonNull
    public final LinearLayout rlTouchViewPosition;

    @NonNull
    public final RelativeLayout rlcenter;

    @NonNull
    public final LinearLayout scroll;

    @NonNull
    public final Button service4gBtn;

    @NonNull
    public final TextView service4gText;

    @NonNull
    public final Button shareBtn;

    @NonNull
    public final TextView shareText;

    @NonNull
    public final ImageButton speakerBtn;

    @NonNull
    public final TextView text1;

    @NonNull
    public final TextView text2;

    @NonNull
    public final TextView text3;

    @NonNull
    public final TextView text4;

    @NonNull
    public final TextView timer;

    @NonNull
    public final TextView tips;

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
    public final IPCTitleView tvTitle;

    @NonNull
    public final TextView tvVoice;

    @NonNull
    public final TextView tvZoomBack;

    @NonNull
    public final TextView videoBackText;

    @NonNull
    public final Button videoBtn;

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

    @NonNull
    public final TextView zoomText;

    protected ActivityIpcameraDoubleEyeLayoutBinding(DataBindingComponent dataBindingComponent, View view2, int i, TextView textView, SensorView sensorView, LinearLayout linearLayout, ImageView imageView, LinearLayout linearLayout2, TextView textView2, LinearLayout linearLayout3, LinearLayout linearLayout4, LinearLayout linearLayout5, Button button, LinearLayout linearLayout6, LinearLayout linearLayout7, Button button2, Button button3, Button button4, Button button5, Button button6, Button button7, ShadowButton shadowButton, ShadowButton shadowButton2, ShadowButton shadowButton3, ShadowButton shadowButton4, ImageButton imageButton, LinearLayout linearLayout8, ConstraintLayout constraintLayout, TextView textView3, Button button8, ImageView imageView2, ConstraintLayout constraintLayout2, LinearLayout linearLayout9, TextView textView4, ShadowButton shadowButton5, DrawLineView drawLineView, EditText editText, ToggleButton toggleButton, Button button9, Button button10, Button button11, ImageView imageView3, FourPicturesView fourPicturesView, LinearLayout linearLayout10, FrameLayout frameLayout, ImageView imageView4, ShadowButton shadowButton6, ShadowButton shadowButton7, ShadowButton shadowButton8, ImageView imageView5, ConstraintLayout constraintLayout3, ShadowButton shadowButton9, ShadowButton shadowButton10, ShadowButton shadowButton11, Guideline guideline, Guideline guideline2, ImageView imageView6, ImageView imageView7, ImageView imageView8, ImageView imageView9, Button button12, TextView textView5, ImageView imageView10, ImageView imageView11, ImageView imageView12, ImageView imageView13, ConstraintLayout constraintLayout4, LinearLayout linearLayout11, LinearLayout linearLayout12, LinearLayout linearLayout13, FrameLayout frameLayout2, CircleTooView circleTooView, ImageButton imageButton2, Button button13, LinearLayout linearLayout14, LinearLayout linearLayout15, LinearLayout linearLayout16, LinearLayout linearLayout17, LinearLayout linearLayout18, LinearLayout linearLayout19, LinearLayout linearLayout20, LinearLayout linearLayout21, LinearLayout linearLayout22, LinearLayout linearLayout23, ImageView imageView14, LinearLayout linearLayout24, LinearLayout linearLayout25, LinearLayout linearLayout26, LinearLayout linearLayout27, LinearLayout linearLayout28, LinearLayout linearLayout29, LinearLayout linearLayout30, ConstraintLayout constraintLayout5, Button button14, ToggleButton toggleButton2, TextView textView6, TextView textView7, TextView textView8, MyGlTextureView myGlTextureView, MyGlTextureView myGlTextureView2, TextView textView9, ConstraintLayout constraintLayout6, TextView textView10, TextView textView11, RelativeLayout relativeLayout, FrameLayout frameLayout3, ImageButton imageButton3, ImageView imageView15, RelativeLayout relativeLayout2, RelativeLayout relativeLayout3, LinearLayout linearLayout31, RelativeLayout relativeLayout4, LinearLayout linearLayout32, Button button15, TextView textView12, Button button16, TextView textView13, ImageButton imageButton4, TextView textView14, TextView textView15, TextView textView16, TextView textView17, TextView textView18, TextView textView19, TextView textView20, TextView textView21, TextView textView22, TextView textView23, TextView textView24, TextView textView25, TextView textView26, TextView textView27, TextView textView28, TextView textView29, IPCTitleView iPCTitleView, TextView textView30, TextView textView31, TextView textView32, Button button17, ProgressBar progressBar, ImageButton imageButton5, TextView textView33, Button button18, Button button19, TextView textView34) {
        super(dataBindingComponent, view2, i);
        this.OSD = textView;
        this.SensorView = sensorView;
        this.ZOOMView = linearLayout;
        this.addZoom = imageView;
        this.autorView = linearLayout2;
        this.back = textView2;
        this.backLl = linearLayout3;
        this.bottomScroll = linearLayout4;
        this.bottomShop = linearLayout5;
        this.bottomShopBtn = button;
        this.bottomView = linearLayout6;
        this.bottomZoom = linearLayout7;
        this.bottomZoomBtn = button2;
        this.btControl = button3;
        this.btPresetAdd = button4;
        this.btPresetInvoke = button5;
        this.btZoomAddBtn = button6;
        this.btZoomReduceBtn = button7;
        this.button1 = shadowButton;
        this.button2 = shadowButton2;
        this.button3 = shadowButton3;
        this.button4 = shadowButton4;
        this.captureBtn = imageButton;
        this.changeZoom = linearLayout8;
        this.clarity = constraintLayout;
        this.cloudText = textView3;
        this.controllerBtn = button8;
        this.controllerEdit = imageView2;
        this.controllerPanel = constraintLayout2;
        this.deleteLl = linearLayout9;
        this.doorText = textView4;
        this.doorbell = shadowButton5;
        this.drawLineView = drawLineView;
        this.etPreset = editText;
        this.exoDoubleEyeBtn = toggleButton;
        this.flipBtn = button9;
        this.focusAddBtn = button10;
        this.focusReduceBtn = button11;
        this.fourOnePic = imageView3;
        this.fourPic = fourPicturesView;
        this.fourPicBtn = linearLayout10;
        this.f6228fragment = frameLayout;
        this.fullAddZoom = imageView4;
        this.fullCamera = shadowButton6;
        this.fullIntercom = shadowButton7;
        this.fullNightVision = shadowButton8;
        this.fullReduceZoom = imageView5;
        this.fullScreen = constraintLayout3;
        this.fullSound = shadowButton9;
        this.fullSwitchWindow = shadowButton10;
        this.fullVideo = shadowButton11;
        this.guideline4 = guideline;
        this.guideline5 = guideline2;
        this.image1 = imageView6;
        this.image2 = imageView7;
        this.image3 = imageView8;
        this.image4 = imageView9;
        this.immediateRenewal = button12;
        this.ipcOfflineText = textView5;
        this.ivCharge4gFlow = imageView10;
        this.ivLightWhile = imageView11;
        this.ivLightWhile2 = imageView12;
        this.ivSnap = imageView13;
        this.landscapePlayer = constraintLayout4;
        this.layoutAf = linearLayout11;
        this.layoutOsd = linearLayout12;
        this.layoutVideo = linearLayout13;
        this.lightDlg = frameLayout2;
        this.lineViewItem = circleTooView;
        this.listenerBtn = imageButton2;
        this.liveBtn = button13;
        this.liveLl = linearLayout14;
        this.ll1 = linearLayout15;
        this.ll2 = linearLayout16;
        this.ll3 = linearLayout17;
        this.ll4 = linearLayout18;
        this.llBottom = linearLayout19;
        this.llCapture = linearLayout20;
        this.llController = linearLayout21;
        this.llFlip = linearLayout22;
        this.llFlips = linearLayout23;
        this.llFull = imageView14;
        this.llListener = linearLayout24;
        this.llMore = linearLayout25;
        this.llMoreDoubleEye = linearLayout26;
        this.llRecord = linearLayout27;
        this.llService4g = linearLayout28;
        this.llShare = linearLayout29;
        this.llVideoQt = linearLayout30;
        this.maxLayout = constraintLayout5;
        this.moreBtn = button14;
        this.moreImage = toggleButton2;
        this.moreText = textView6;
        this.moreTextDoubleEye = textView7;
        this.outlineTime = textView8;
        this.player = myGlTextureView;
        this.player2 = myGlTextureView2;
        this.playerInfoTv = textView9;
        this.portraitPlayer = constraintLayout6;
        this.ptzText = textView10;
        this.qualityBtn = textView11;
        this.qualityControl = relativeLayout;
        this.qualityDlg = frameLayout3;
        this.recordBtn = imageButton3;
        this.reduceZoom = imageView15;
        this.relativeLayout = relativeLayout2;
        this.rlTouchView = relativeLayout3;
        this.rlTouchViewPosition = linearLayout31;
        this.rlcenter = relativeLayout4;
        this.scroll = linearLayout32;
        this.service4gBtn = button15;
        this.service4gText = textView12;
        this.shareBtn = button16;
        this.shareText = textView13;
        this.speakerBtn = imageButton4;
        this.text1 = textView14;
        this.text2 = textView15;
        this.text3 = textView16;
        this.text4 = textView17;
        this.timer = textView18;
        this.tips = textView19;
        this.traffic4gExpired = textView20;
        this.tvCapture = textView21;
        this.tvHQuality = textView22;
        this.tvLQuality = textView23;
        this.tvLight1 = textView24;
        this.tvLight2 = textView25;
        this.tvLight3 = textView26;
        this.tvMQuality = textView27;
        this.tvPreset = textView28;
        this.tvRecord = textView29;
        this.tvTitle = iPCTitleView;
        this.tvVoice = textView30;
        this.tvZoomBack = textView31;
        this.videoBackText = textView32;
        this.videoBtn = button17;
        this.videoBufferingBar = progressBar;
        this.videoPlayIbtn = imageButton5;
        this.wakeupText = textView33;
        this.zoomAddBtn = button18;
        this.zoomReduceBtn = button19;
        this.zoomText = textView34;
    }

    @NonNull
    public static ActivityIpcameraDoubleEyeLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityIpcameraDoubleEyeLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityIpcameraDoubleEyeLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ipcamera_double_eye_layout, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityIpcameraDoubleEyeLayoutBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityIpcameraDoubleEyeLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityIpcameraDoubleEyeLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ipcamera_double_eye_layout, null, false, dataBindingComponent);
    }

    public static ActivityIpcameraDoubleEyeLayoutBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityIpcameraDoubleEyeLayoutBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityIpcameraDoubleEyeLayoutBinding) bind(dataBindingComponent, view2, R.layout.activity_ipcamera_double_eye_layout);
    }
}
