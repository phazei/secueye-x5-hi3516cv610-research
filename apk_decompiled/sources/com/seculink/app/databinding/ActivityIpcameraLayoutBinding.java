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
public abstract class ActivityIpcameraLayoutBinding extends ViewDataBinding {

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
    public final ShadowButton button1;

    @NonNull
    public final ShadowButton button2;

    @NonNull
    public final ShadowButton button3;

    @NonNull
    public final ShadowButton button4;

    @NonNull
    public final LinearLayout button5;

    @NonNull
    public final LinearLayout button6;

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
    public final EditText etEnter;

    @NonNull
    public final EditText etPreset;

    @NonNull
    public final ToggleButton exoZoomTbtn;

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
    public final FrameLayout f6229fragment;

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
    public final TextView fullText;

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
    public final Guideline ipcLine1;

    @NonNull
    public final TextView ipcOfflineText;

    @NonNull
    public final ImageView ivCharge4gFlow;

    @NonNull
    public final ImageView ivLightWhile;

    @NonNull
    public final ImageView ivSnap;

    @NonNull
    public final ConstraintLayout landscapePlayer;

    @NonNull
    public final LinearLayout layoutCloudPlayback;

    @NonNull
    public final LinearLayout layoutFaceModel;

    @NonNull
    public final LinearLayout layoutFaceQuery;

    @NonNull
    public final LinearLayout layoutFaceRecognition;

    @NonNull
    public final ConstraintLayout layoutMain;

    @NonNull
    public final LinearLayout layoutOsd;

    @NonNull
    public final ImageView leftDevice;

    @NonNull
    public final FrameLayout lightDlg;

    @NonNull
    public final CircleTooView lineViewItem;

    @NonNull
    public final LineHorizontalView lineview;

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
    public final LinearLayout llListener;

    @NonNull
    public final LinearLayout llMore;

    @NonNull
    public final LinearLayout llQuality;

    @NonNull
    public final LinearLayout llRecord;

    @NonNull
    public final LinearLayout llShare;

    @NonNull
    public final LinearLayout llVideoQt;

    @NonNull
    public final LinearLayout llZoom;

    @NonNull
    public final Button moreBtn;

    @NonNull
    public final TextView moreText;

    @NonNull
    public final TextView outlineTime;

    @NonNull
    public final MyGlTextureView play;

    @NonNull
    public final TextView playerInfoTv2;

    @NonNull
    public final ConstraintLayout portraitPlayer;

    @NonNull
    public final TextView qualityBtn1;

    @NonNull
    public final TextView qualityBtn2;

    @NonNull
    public final TextView qualityBtn3;

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
    public final ImageView rightDevice;

    @NonNull
    public final LinearLayout rlTouchView;

    @NonNull
    public final LinearLayout rlTouchViewPosition;

    @NonNull
    public final RelativeLayout rlcenter;

    @NonNull
    public final RecyclerView rvFace;

    @NonNull
    public final LinearLayout scroll;

    @NonNull
    public final Button shareBtn;

    @NonNull
    public final TextView shareText;

    @NonNull
    public final ImageButton speakerBtn;

    @NonNull
    public final TabLayout tabLayout;

    @NonNull
    public final TabItem tabPtz;

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
    public final TextView tvDelete;

    @NonNull
    public final TextView tvDeleteAll;

    @NonNull
    public final TextView tvEnter;

    @NonNull
    public final TextView tvEnter1;

    @NonNull
    public final TextView tvEnterModel;

    @NonNull
    public final TextView tvFaceData;

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
    public final TextView tvOsd;

    @NonNull
    public final TextView tvPreset;

    @NonNull
    public final TextView tvQuery;

    @NonNull
    public final TextView tvRecord;

    @NonNull
    public final TextView tvStopEnter;

    @NonNull
    public final IPCTitleView tvTitle;

    @NonNull
    public final TextView tvVoice;

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

    protected ActivityIpcameraLayoutBinding(DataBindingComponent dataBindingComponent, View view2, int i, SensorView sensorView, LinearLayout linearLayout, ImageView imageView, LinearLayout linearLayout2, TextView textView, LinearLayout linearLayout3, LinearLayout linearLayout4, LinearLayout linearLayout5, Button button, LinearLayout linearLayout6, LinearLayout linearLayout7, Button button2, Button button3, Button button4, Button button5, ShadowButton shadowButton, ShadowButton shadowButton2, ShadowButton shadowButton3, ShadowButton shadowButton4, LinearLayout linearLayout8, LinearLayout linearLayout9, ImageButton imageButton, LinearLayout linearLayout10, ConstraintLayout constraintLayout, TextView textView2, Button button6, ImageView imageView2, ConstraintLayout constraintLayout2, LinearLayout linearLayout11, TextView textView3, ShadowButton shadowButton5, DrawLineView drawLineView, EditText editText, EditText editText2, ToggleButton toggleButton, Button button7, Button button8, Button button9, ImageView imageView3, FourPicturesView fourPicturesView, LinearLayout linearLayout12, FrameLayout frameLayout, ImageView imageView4, ShadowButton shadowButton6, ShadowButton shadowButton7, ShadowButton shadowButton8, ImageView imageView5, ConstraintLayout constraintLayout3, ShadowButton shadowButton9, TextView textView4, ShadowButton shadowButton10, Guideline guideline, Guideline guideline2, ImageView imageView6, ImageView imageView7, ImageView imageView8, ImageView imageView9, Button button10, Guideline guideline3, TextView textView5, ImageView imageView10, ImageView imageView11, ImageView imageView12, ConstraintLayout constraintLayout4, LinearLayout linearLayout13, LinearLayout linearLayout14, LinearLayout linearLayout15, LinearLayout linearLayout16, ConstraintLayout constraintLayout5, LinearLayout linearLayout17, ImageView imageView13, FrameLayout frameLayout2, CircleTooView circleTooView, LineHorizontalView lineHorizontalView, ImageButton imageButton2, Button button11, LinearLayout linearLayout18, LinearLayout linearLayout19, LinearLayout linearLayout20, LinearLayout linearLayout21, LinearLayout linearLayout22, LinearLayout linearLayout23, LinearLayout linearLayout24, LinearLayout linearLayout25, LinearLayout linearLayout26, LinearLayout linearLayout27, LinearLayout linearLayout28, LinearLayout linearLayout29, LinearLayout linearLayout30, LinearLayout linearLayout31, LinearLayout linearLayout32, LinearLayout linearLayout33, Button button12, TextView textView6, TextView textView7, MyGlTextureView myGlTextureView, TextView textView8, ConstraintLayout constraintLayout6, TextView textView9, TextView textView10, TextView textView11, RelativeLayout relativeLayout, FrameLayout frameLayout3, ImageButton imageButton3, ImageView imageView14, RelativeLayout relativeLayout2, ImageView imageView15, LinearLayout linearLayout34, LinearLayout linearLayout35, RelativeLayout relativeLayout3, RecyclerView recyclerView, LinearLayout linearLayout36, Button button13, TextView textView12, ImageButton imageButton4, TabLayout tabLayout, TabItem tabItem, TextView textView13, TextView textView14, TextView textView15, TextView textView16, TextView textView17, TextView textView18, TextView textView19, TextView textView20, TextView textView21, TextView textView22, TextView textView23, TextView textView24, TextView textView25, TextView textView26, TextView textView27, TextView textView28, TextView textView29, TextView textView30, TextView textView31, TextView textView32, TextView textView33, TextView textView34, TextView textView35, TextView textView36, TextView textView37, IPCTitleView iPCTitleView, TextView textView38, TextView textView39, Button button14, ProgressBar progressBar, ImageButton imageButton5, TextView textView40, Button button15, Button button16) {
        super(dataBindingComponent, view2, i);
        this.SensorView = sensorView;
        this.ZOOMView = linearLayout;
        this.addZoom = imageView;
        this.autorView = linearLayout2;
        this.back = textView;
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
        this.button1 = shadowButton;
        this.button2 = shadowButton2;
        this.button3 = shadowButton3;
        this.button4 = shadowButton4;
        this.button5 = linearLayout8;
        this.button6 = linearLayout9;
        this.captureBtn = imageButton;
        this.changeZoom = linearLayout10;
        this.clarity = constraintLayout;
        this.cloudText = textView2;
        this.controllerBtn = button6;
        this.controllerEdit = imageView2;
        this.controllerPanel = constraintLayout2;
        this.deleteLl = linearLayout11;
        this.doorText = textView3;
        this.doorbell = shadowButton5;
        this.drawLineView = drawLineView;
        this.etEnter = editText;
        this.etPreset = editText2;
        this.exoZoomTbtn = toggleButton;
        this.flipBtn = button7;
        this.focusAddBtn = button8;
        this.focusReduceBtn = button9;
        this.fourOnePic = imageView3;
        this.fourPic = fourPicturesView;
        this.fourPicBtn = linearLayout12;
        this.f6229fragment = frameLayout;
        this.fullAddZoom = imageView4;
        this.fullCamera = shadowButton6;
        this.fullIntercom = shadowButton7;
        this.fullNightVision = shadowButton8;
        this.fullReduceZoom = imageView5;
        this.fullScreen = constraintLayout3;
        this.fullSound = shadowButton9;
        this.fullText = textView4;
        this.fullVideo = shadowButton10;
        this.guideline4 = guideline;
        this.guideline5 = guideline2;
        this.image1 = imageView6;
        this.image2 = imageView7;
        this.image3 = imageView8;
        this.image4 = imageView9;
        this.immediateRenewal = button10;
        this.ipcLine1 = guideline3;
        this.ipcOfflineText = textView5;
        this.ivCharge4gFlow = imageView10;
        this.ivLightWhile = imageView11;
        this.ivSnap = imageView12;
        this.landscapePlayer = constraintLayout4;
        this.layoutCloudPlayback = linearLayout13;
        this.layoutFaceModel = linearLayout14;
        this.layoutFaceQuery = linearLayout15;
        this.layoutFaceRecognition = linearLayout16;
        this.layoutMain = constraintLayout5;
        this.layoutOsd = linearLayout17;
        this.leftDevice = imageView13;
        this.lightDlg = frameLayout2;
        this.lineViewItem = circleTooView;
        this.lineview = lineHorizontalView;
        this.listenerBtn = imageButton2;
        this.liveBtn = button11;
        this.liveLl = linearLayout18;
        this.ll1 = linearLayout19;
        this.ll2 = linearLayout20;
        this.ll3 = linearLayout21;
        this.ll4 = linearLayout22;
        this.llBottom = linearLayout23;
        this.llCapture = linearLayout24;
        this.llController = linearLayout25;
        this.llFlip = linearLayout26;
        this.llListener = linearLayout27;
        this.llMore = linearLayout28;
        this.llQuality = linearLayout29;
        this.llRecord = linearLayout30;
        this.llShare = linearLayout31;
        this.llVideoQt = linearLayout32;
        this.llZoom = linearLayout33;
        this.moreBtn = button12;
        this.moreText = textView6;
        this.outlineTime = textView7;
        this.play = myGlTextureView;
        this.playerInfoTv2 = textView8;
        this.portraitPlayer = constraintLayout6;
        this.qualityBtn1 = textView9;
        this.qualityBtn2 = textView10;
        this.qualityBtn3 = textView11;
        this.qualityControl = relativeLayout;
        this.qualityDlg = frameLayout3;
        this.recordBtn = imageButton3;
        this.reduceZoom = imageView14;
        this.relativeLayout = relativeLayout2;
        this.rightDevice = imageView15;
        this.rlTouchView = linearLayout34;
        this.rlTouchViewPosition = linearLayout35;
        this.rlcenter = relativeLayout3;
        this.rvFace = recyclerView;
        this.scroll = linearLayout36;
        this.shareBtn = button13;
        this.shareText = textView12;
        this.speakerBtn = imageButton4;
        this.tabLayout = tabLayout;
        this.tabPtz = tabItem;
        this.text1 = textView13;
        this.text2 = textView14;
        this.text3 = textView15;
        this.text4 = textView16;
        this.timer = textView17;
        this.tips = textView18;
        this.traffic4gExpired = textView19;
        this.tvCapture = textView20;
        this.tvDelete = textView21;
        this.tvDeleteAll = textView22;
        this.tvEnter = textView23;
        this.tvEnter1 = textView24;
        this.tvEnterModel = textView25;
        this.tvFaceData = textView26;
        this.tvHQuality = textView27;
        this.tvLQuality = textView28;
        this.tvLight1 = textView29;
        this.tvLight2 = textView30;
        this.tvLight3 = textView31;
        this.tvMQuality = textView32;
        this.tvOsd = textView33;
        this.tvPreset = textView34;
        this.tvQuery = textView35;
        this.tvRecord = textView36;
        this.tvStopEnter = textView37;
        this.tvTitle = iPCTitleView;
        this.tvVoice = textView38;
        this.videoBackText = textView39;
        this.videoBtn = button14;
        this.videoBufferingBar = progressBar;
        this.videoPlayIbtn = imageButton5;
        this.wakeupText = textView40;
        this.zoomAddBtn = button15;
        this.zoomReduceBtn = button16;
    }

    @NonNull
    public static ActivityIpcameraLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityIpcameraLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityIpcameraLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ipcamera_layout, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityIpcameraLayoutBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityIpcameraLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityIpcameraLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ipcamera_layout, null, false, dataBindingComponent);
    }

    public static ActivityIpcameraLayoutBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityIpcameraLayoutBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityIpcameraLayoutBinding) bind(dataBindingComponent, view2, R.layout.activity_ipcamera_layout);
    }
}
