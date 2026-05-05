package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import bean.CameraNameModify;
import bean.CameraRemove;
import bean.CameraSnapUpdate;
import bean.DeviceInfoBean;
import bean.NetWorkEvent;
import bean.RefreshPicture;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.api.share.DeviceShareManager;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.aliyun.iotx.linkvisual.media.audio.LiveIntercomException;
import com.aliyun.iotx.linkvisual.media.audio.LiveIntercomV2;
import com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener;
import com.aliyun.iotx.linkvisual.media.video.PlayerException;
import com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener;
import com.aliyun.iotx.linkvisual.media.video.player.LivePlayer;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import config.Constants;
import dialog.DialogUtil;
import dialog.InputDialogView;
import dialog.LenCamCtrlDialog;
import dialog.SnapshotPreviewDialog;
import enums.ActionTypeEnum;
import enums.SpeedEnum;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import sdk.IPCManager;
import tools.DateUtil;
import tools.FileUtil;
import tools.LogEx;
import tools.MediaStoreUtil;
import tools.MyCallback;
import tools.ScreenUtil;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import tools.SpUtil;
import tools.Utils;
import view.ComponentGLLayout;
import view.JoystickTouchViewListener;
import view.ModuleView;
import view.TitleView;
import view.TouchView;
import view.WhiteProgressDialog;

/* JADX INFO: loaded from: classes.dex */
public class IPCamera4Activity extends CommonActivity implements View.OnClickListener {
    private static final int SENSOR_LANDSCAPE = 1;
    private static final int SENSOR_PORTRAIT = 0;
    private static final int SENSOR_REVERSE_LANDSCAPE = 3;
    private static final int SENSOR_REVERSE_PORTRAIT = 2;
    private String DeviceName;
    private String IccId;
    private String ProductKey;
    private Animation alphaAnimation;
    private LinearLayout bottomView;
    private Button bt_control;
    private ImageButton captureBtn;
    private Button cardVideoBtn;
    private RelativeLayout containerRl;
    private long countTime;
    private int currentInfrarred;
    private View decorView;
    private DeviceInfoBean device;
    private Button downBtn;
    private Button downLeftBtn;
    private Button downRightBtn;
    private LinearLayout fl_title;
    private Button flip_btn;
    private int focusSupport;
    private boolean isFirstShowStreamType;
    private boolean isFloat;
    private boolean isOwner;
    private int isSpeakerOpen;
    private int isSupport4G;
    private boolean isTouchStop;
    private ActionTypeEnum lastActionTypeEnum;
    private long lastCtrlTime;
    private int lastOrientation;
    private Button leftBtn;
    private LenCamCtrlDialog lenCamCtrlDialog;
    private ImageButton lightBtn;
    private TextView lightTv1;
    private TextView lightTv2;
    private TextView lightTv3;
    private FrameLayout light_dlg;
    private ImageButton listenerBtn;
    private LiveIntercomV2[] liveIntercoms;
    private LinearLayout ll_capture;
    private LinearLayout ll_flip;
    private LinearLayout ll_flips;
    private LinearLayout ll_listener;
    private LinearLayout ll_quality;
    private LinearLayout ll_record;
    private LinearLayout ll_root;
    private LinearLayout ll_service_4g;
    private LinearLayout ll_title;
    private LinearLayout ll_video_qt;
    private LinearLayout ll_zoom;
    private Activity mContext;
    private GestureDetector mGestureDector;
    private OrientationEventListener mOrientationEventListener;
    private ModuleView mvFocusAdd;
    private ModuleView mvFocusMinus;
    private ModuleView mvZoomAdd;
    private ModuleView mvZoomMinus;
    private MyGLSurfaceViewGestureListener myGLSurfaceViewGestureListener;
    private Button pictureBtn;
    private TextView playInfoTv;
    private ConstraintLayout playerContainer;
    private int playerContainerHeight;
    private int playerContainerWidth;
    private int presetSupport;
    private Timer ptzTimer;
    private TextView qualityBtn1;
    private TextView qualityBtn2;
    private TextView qualityBtn3;
    private FrameLayout quality_dlg;
    private ImageButton recordBtn;
    private Button rightBtn;
    private LinearLayout rightView;
    private LinearLayout rl_touch_view;
    private Button service4gBtn;
    private Button settingsBtn;
    private InputDialogView shareDialog;
    private ImageButton speakBtn;
    private float startX;
    private float startY;
    private Button stopBtn;
    private Timer timer;
    private TouchView touchView;
    private TextView tvRecordTime;
    private TextView tv_capture;
    private TextView tv_h_quality;
    private TextView tv_l_quality;
    private TextView tv_line;
    private TextView tv_m_quality;
    private TextView tv_record;
    private TextView tv_times;
    private TitleView tv_title;
    private TextView tv_voice;
    private Handler uiHandler;
    private int uiVisibility;
    private Button upBtn;
    private Button upLeftBtn;
    private Button upRightBtn;
    ScheduledFuture<?> updatePlayInfoHandle;
    private Button videoBtn;
    private RelativeLayout video_panel_rl;
    private View view_line;
    private WhiteProgressDialog whiteProgressDialog;
    private ToggleButton zoomBtn;
    private Button zoomInBtn;
    private Button zoomOutBtn;
    private int zoomSupport;
    private final String TAG = getClass().getSimpleName();
    private String iotId = "";
    private String appKey = "";
    private String title = "";
    private LivePlayer[] players = new LivePlayer[4];
    private ComponentGLLayout[] zoomableTextureViews = new ComponentGLLayout[4];
    private Handler msgHandler = new Handler();
    private boolean isLiveIntercoming = false;
    private Handler mHandler = new Handler();
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private int defaultDefinition = 1;
    private boolean[] speakerSwitch = new boolean[4];
    private int rotationOrientate = 0;
    private int ptzControlTime = 100;
    private boolean isRecordingMp4 = false;
    private File file = null;
    private SharePreferenceManager.OnCallSetListener definitionChangeListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.IPCamera4Activity.3
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            IPCamera4Activity.this.uiHandler.post(new Runnable() { // from class: activity.IPCamera4Activity.3.1
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCamera4Activity.this.iotId.equals(str)) {
                        if (str2.equals(IPCamera4Activity.this.getString(R.string.stream_video_quality_key))) {
                            IPCamera4Activity.this.setVideoStreamQuality();
                            LivePlayer livePlayerFindPlayerByIotId = IPCamera4Activity.this.findPlayerByIotId(str);
                            if (livePlayerFindPlayerByIotId == null || IPCamera4Activity.this.getResources().getConfiguration().orientation == 2) {
                                return;
                            }
                            if (IPCamera4Activity.this.defaultDefinition != 2) {
                                livePlayerFindPlayerByIotId.setVideoScalingMode(1);
                                return;
                            } else {
                                livePlayerFindPlayerByIotId.setVideoScalingMode(2);
                                return;
                            }
                        }
                        if (str2.equals(IPCamera4Activity.this.getString(R.string.image_flip_status_key))) {
                            IPCamera4Activity.this.setImageFlip();
                            return;
                        }
                        if (str2.equals(IPCamera4Activity.this.getString(R.string.day_night_mode_key))) {
                            IPCamera4Activity.this.setDayLightMode();
                            return;
                        }
                        if (str2.equals(IPCamera4Activity.this.getString(R.string.support_zoom_key)) || str2.equals(IPCamera4Activity.this.getString(R.string.support_focus_key))) {
                            IPCamera4Activity.this.setCamSupportShow();
                        } else if (str2.equals(IPCamera4Activity.this.getString(R.string.support_4g_key)) || str2.equals(IPCamera4Activity.this.getString(R.string.iccid_key))) {
                            IPCamera4Activity.this.set4GService();
                        }
                    }
                }
            });
        }
    };
    private int curPageNum = 4;
    private int currentFocusPlayerIndex = 0;
    final Runnable updatePlayInfoTimerTask = new Runnable() { // from class: activity.IPCamera4Activity.57
        @Override // java.lang.Runnable
        public void run() {
            IPCamera4Activity.this.runOnUiThread(new Runnable() { // from class: activity.IPCamera4Activity.57.1
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCamera4Activity.this.isFinishing()) {
                        return;
                    }
                    IPCamera4Activity.this.updatePlayInfo();
                }
            });
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ipcamera4;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cameraNameModify(CameraNameModify cameraNameModify) {
        this.device.setNickName(cameraNameModify.getName());
        this.title = cameraNameModify.getName();
        this.tv_title.setTitleText(this.title);
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.decorView = getWindow().getDecorView();
        this.uiVisibility = this.decorView.getSystemUiVisibility();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.mContext = this;
        this.whiteProgressDialog = new WhiteProgressDialog(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= 24) {
            builder.detectFileUriExposure();
        }
        this.uiHandler = new Handler(getMainLooper());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            DeviceInfoBean deviceInfoBean = this.device;
            if (deviceInfoBean != null) {
                this.iotId = deviceInfoBean.getIotId();
                this.title = this.device.getName();
                this.isOwner = this.device.getOwned() == 1;
                this.DeviceName = this.device.getDeviceName();
                this.ProductKey = this.device.getProductKey();
            }
        }
        this.appKey = getIntent().getStringExtra("appKey");
        this.players = new LivePlayer[this.device.getChildDevices().size()];
        initView();
        initPlayer();
        initLiveIntercom();
        this.flip_btn.setBackgroundResource(R.drawable.cloudicon);
        initTextureViews();
        setFourTextureViews();
    }

    public boolean isNotExistIndex(int i) {
        return i >= this.players.length;
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.definitionChangeListener);
        setVideoStreamQuality();
        setImageFlip();
        getProperties();
        playLive();
        registerSensorEvent();
        this.IccId = SharePreferenceManager.getInstance().getIccId(this.iotId);
    }

    public void setVideoStreamQuality() {
        this.defaultDefinition = SharePreferenceManager.getInstance().getStreamVideoQuality(this.iotId);
        changeDefinitionView(this.defaultDefinition);
    }

    public void setImageFlip() {
        this.rotationOrientate = SharePreferenceManager.getInstance().getImageFlip(this.iotId);
        changeImageFlip(this.rotationOrientate);
    }

    private void changeImageFlip(int i) {
        this.flip_btn.setBackgroundResource(i == 0 ? R.drawable.ic_flip : R.drawable.ic_flip_rotate);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netEventChange(NetWorkEvent netWorkEvent) {
        final int i = 0;
        while (true) {
            LivePlayer[] livePlayerArr = this.players;
            if (i >= livePlayerArr.length) {
                return;
            }
            if (livePlayerArr[i].getPlayState() == 3 || this.players[i].getPlayState() == 4) {
                this.players[i].stop();
                this.uiHandler.postDelayed(new Runnable() { // from class: activity.IPCamera4Activity.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCamera4Activity.this.playLive(i);
                    }
                }, 500L);
            }
            i++;
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mOrientationEventListener.disable();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        stopLiveIntercom();
        stopRecording();
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.tvRecordTime.setText("");
        this.tvRecordTime.setVisibility(8);
        this.speakBtn.clearAnimation();
        dismissPlayInfo();
        SharePreferenceManager.getInstance().unRegisterOnCallSetListener(this.definitionChangeListener);
        try {
            this.liveIntercoms[this.currentFocusPlayerIndex].stop();
        } catch (Exception e) {
            LogEx.e(true, this.TAG, "liveIntercom.stop() error", e);
        }
        AutoSnap();
        stopLive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRecording() {
        if (this.isRecordingMp4) {
            LivePlayer[] livePlayerArr = this.players;
            int i = this.currentFocusPlayerIndex;
            if (livePlayerArr[i] != null && livePlayerArr[i].stopRecordingContent()) {
                if (this.countTime <= 3) {
                    File file = this.file;
                    if (file != null && file.exists()) {
                        this.file.delete();
                    }
                } else {
                    EventBus.getDefault().post(new RefreshPicture());
                    long j = 0;
                    try {
                        j = Long.parseLong(this.file.getName().substring(0, this.file.getName().indexOf(".")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        MediaStoreUtil.addVideoFileToMediaStore(this, this.file, j);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            this.isRecordingMp4 = false;
            setRecordState();
            Timer timer = this.timer;
            if (timer != null) {
                timer.cancel();
                this.timer = null;
            }
            this.tvRecordTime.setText("");
            this.tvRecordTime.setVisibility(8);
        }
    }

    public void releaseLive() {
        int i = 0;
        while (true) {
            LivePlayer[] livePlayerArr = this.players;
            if (i >= livePlayerArr.length) {
                return;
            }
            livePlayerArr[i].release();
            i++;
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        releaseLive();
        this.uiHandler.removeCallbacksAndMessages(null);
        this.mHandler.removeCallbacksAndMessages(null);
        this.msgHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        Timer timer = this.ptzTimer;
        if (timer != null) {
            timer.cancel();
            this.ptzTimer = null;
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        if (configuration.orientation == 2) {
            int i = 0;
            while (true) {
                LivePlayer[] livePlayerArr = this.players;
                if (i >= livePlayerArr.length) {
                    break;
                }
                if (livePlayerArr[i] != null) {
                    livePlayerArr[i].setVideoScalingMode(1);
                }
                this.zoomableTextureViews[i].getGlTextureView().reset();
                i++;
            }
            hideOtherView();
        } else {
            int i2 = 0;
            while (true) {
                LivePlayer[] livePlayerArr2 = this.players;
                if (i2 >= livePlayerArr2.length) {
                    break;
                }
                if (livePlayerArr2[i2] != null) {
                    if (this.defaultDefinition != 2) {
                        livePlayerArr2[i2].setVideoScalingMode(1);
                    } else {
                        livePlayerArr2[i2].setVideoScalingMode(2);
                    }
                }
                this.zoomableTextureViews[i2].getGlTextureView().reset();
                i2++;
            }
            showOtherView();
        }
        if (this.curPageNum == 1) {
            setOneTextureView(this.currentFocusPlayerIndex);
        } else {
            setFourTextureViews();
        }
        super.onConfigurationChanged(configuration);
        if (this.isLand) {
            setSwipeBackEnable(false);
        } else {
            setSwipeBackEnable(true);
        }
    }

    private void getProperties() {
        for (int i = 0; i < this.device.childDevices.size(); i++) {
            SettingsCtrl.getInstance().getProperties(this.device.childDevices.get(i).getIotId(), new MyCallback() { // from class: activity.IPCamera4Activity.2
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
        }
    }

    public LivePlayer findPlayerByIotId(String str) {
        for (int i = 0; i < this.device.childDevices.size(); i++) {
            if (str.equals(this.device.childDevices.get(i).getIotId())) {
                return this.players[i];
            }
        }
        return null;
    }

    public void setDayLightMode() {
        this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(this.iotId);
        changeLightDlgView(this.currentInfrarred);
        DeviceInfoBean deviceInfoBean = this.device;
        if (deviceInfoBean != null && deviceInfoBean.getOwned() == 1) {
            this.lightBtn.setVisibility(0);
        } else {
            this.lightBtn.setVisibility(8);
        }
    }

    public void set4GService() {
        DeviceInfoBean deviceInfoBean = this.device;
        if (deviceInfoBean != null && deviceInfoBean.getOwned() == 1) {
            this.isSupport4G = SharePreferenceManager.getInstance().getSupport4G(this.iotId);
            if (this.isSupport4G == 1) {
                this.ll_service_4g.setVisibility(0);
            } else {
                this.ll_service_4g.setVisibility(8);
            }
        } else {
            this.ll_service_4g.setVisibility(8);
        }
        this.IccId = SharePreferenceManager.getInstance().getIccId(this.iotId);
    }

    public void registerSensorEvent() {
        this.mOrientationEventListener = new OrientationEventListener(this, 3) { // from class: activity.IPCamera4Activity.4
            @Override // android.view.OrientationEventListener
            public void onOrientationChanged(int i) {
                if (IPCamera4Activity.this.isSensorOpen()) {
                    if (i >= 245 && i < 295) {
                        if (IPCamera4Activity.this.lastOrientation != 1) {
                            IPCamera4Activity.this.setRequestedOrientation(0);
                            IPCamera4Activity.this.lastOrientation = 1;
                            return;
                        }
                        return;
                    }
                    if (i >= 65 && i < 115) {
                        if (IPCamera4Activity.this.lastOrientation != 3) {
                            IPCamera4Activity.this.setRequestedOrientation(8);
                            IPCamera4Activity.this.lastOrientation = 3;
                            return;
                        }
                        return;
                    }
                    if (i < 155 || i >= 205) {
                        if ((i >= 335 || i < 25) && IPCamera4Activity.this.lastOrientation != 0) {
                            IPCamera4Activity.this.setRequestedOrientation(1);
                            IPCamera4Activity.this.lastOrientation = 0;
                            return;
                        }
                        return;
                    }
                    if (IPCamera4Activity.this.lastOrientation != 2) {
                        IPCamera4Activity.this.setRequestedOrientation(9);
                        IPCamera4Activity.this.lastOrientation = 2;
                    }
                }
            }
        };
        this.mOrientationEventListener.enable();
    }

    private void initView() {
        this.ll_service_4g = (LinearLayout) findViewById(R.id.ll_service_4g);
        this.ll_service_4g.setOnClickListener(this);
        this.service4gBtn = (Button) findViewById(R.id.service_4g_btn);
        this.service4gBtn.setOnClickListener(this);
        this.playerContainer = (ConstraintLayout) findViewById(R.id.player_container);
        this.playInfoTv = (TextView) findViewById(R.id.player_info_tv2);
        this.tvRecordTime = (TextView) findViewById(R.id.tv_record_time);
        this.ll_title = (LinearLayout) findViewById(R.id.ll_title);
        this.fl_title = (LinearLayout) findViewById(R.id.fl_title);
        this.tv_times = (TextView) findViewById(R.id.tv_times);
        this.tv_line = (TextView) findViewById(R.id.tv_line);
        this.ll_root = (LinearLayout) findViewById(R.id.ll_root);
        setEdgeToEdge(this.ll_root);
        this.tv_capture = (TextView) findViewById(R.id.tv_capture);
        this.tv_record = (TextView) findViewById(R.id.tv_record);
        this.ll_flips = (LinearLayout) findViewById(R.id.ll_flips);
        this.tv_voice = (TextView) findViewById(R.id.tv_voice);
        this.video_panel_rl = (RelativeLayout) findViewById(R.id.video_panel_rl);
        this.ll_listener = (LinearLayout) findViewById(R.id.ll_listener);
        this.ll_capture = (LinearLayout) findViewById(R.id.ll_capture);
        this.ll_record = (LinearLayout) findViewById(R.id.ll_record);
        this.rl_touch_view = (LinearLayout) findViewById(R.id.rl_touch_view);
        this.tv_title = (TitleView) findViewById(R.id.tv_title);
        this.ll_quality = (LinearLayout) findViewById(R.id.ll_quality);
        this.ll_video_qt = (LinearLayout) findViewById(R.id.ll_video_qt);
        this.quality_dlg = (FrameLayout) findViewById(R.id.quality_dlg);
        this.light_dlg = (FrameLayout) findViewById(R.id.light_dlg);
        this.tv_h_quality = (TextView) findViewById(R.id.tv_h_quality);
        this.tv_m_quality = (TextView) findViewById(R.id.tv_m_quality);
        this.tv_l_quality = (TextView) findViewById(R.id.tv_l_quality);
        this.lightBtn = (ImageButton) findViewById(R.id.light_btn);
        this.tv_title.setLineViewId(0);
        this.tv_title.setTitleText(this.title);
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.IPCamera4Activity.5
            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                IPCamera4Activity.this.onBackPressed();
            }

            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
                if (view2.getId() == R.id.right_ll) {
                    IPCamera4Activity.this.device.getChildDevices().get(IPCamera4Activity.this.currentFocusPlayerIndex).setOwned(IPCamera4Activity.this.device.getOwned());
                    Intent intent = new Intent(IPCamera4Activity.this.mContext, (Class<?>) SettingsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCamera4Activity.this.device);
                    intent.putExtras(bundle);
                    IPCamera4Activity.this.startActivity(intent);
                }
            }
        });
        this.zoomBtn = (ToggleButton) findViewById(R.id.exo_zoom_tbtn);
        this.ll_zoom = (LinearLayout) findViewById(R.id.ll_zoom);
        this.ll_zoom.setOnClickListener(this);
        this.view_line = findViewById(R.id.view_line);
        this.captureBtn = (ImageButton) findViewById(R.id.capture_btn);
        this.recordBtn = (ImageButton) findViewById(R.id.record_btn);
        this.speakBtn = (ImageButton) findViewById(R.id.speaker_btn);
        this.videoBtn = (Button) findViewById(R.id.video_btn);
        this.qualityBtn1 = (TextView) findViewById(R.id.quality_btn1);
        this.qualityBtn2 = (TextView) findViewById(R.id.quality_btn2);
        this.qualityBtn3 = (TextView) findViewById(R.id.quality_btn3);
        this.lightTv1 = (TextView) findViewById(R.id.tv_light1);
        this.lightTv2 = (TextView) findViewById(R.id.tv_light2);
        this.lightTv3 = (TextView) findViewById(R.id.tv_light3);
        this.listenerBtn = (ImageButton) findViewById(R.id.listener_btn);
        this.flip_btn = (Button) findViewById(R.id.flip_btn);
        this.rightView = (LinearLayout) findViewById(R.id.rightView);
        this.bottomView = (LinearLayout) findViewById(R.id.bottomView);
        this.ll_flip = (LinearLayout) findViewById(R.id.ll_flip);
        this.ll_capture.setOnClickListener(this);
        this.ll_record.setOnClickListener(this);
        this.speakBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCamera4Activity.6
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCamera4Activity.this.startOrStopLiveIntercom();
                    return false;
                }
                if (motionEvent.getAction() == 1) {
                    return false;
                }
                motionEvent.getAction();
                return false;
            }
        });
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
            }
        });
        this.ll_flip.setOnClickListener(this);
        this.containerRl = (RelativeLayout) findViewById(R.id.container);
        addPtzBtn();
        this.tv_h_quality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.quality_dlg.setVisibility(8);
                IPCamera4Activity.this.changeDefinition(2);
            }
        });
        this.tv_m_quality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.quality_dlg.setVisibility(8);
                IPCamera4Activity.this.changeDefinition(1);
            }
        });
        this.tv_l_quality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.quality_dlg.setVisibility(8);
                IPCamera4Activity.this.changeDefinition(0);
            }
        });
        this.quality_dlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.quality_dlg.setVisibility(8);
            }
        });
        this.qualityBtn1.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.quality_dlg.setVisibility(0);
                IPCamera4Activity.this.changeQualityDlgView(2);
            }
        });
        this.qualityBtn2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.quality_dlg.setVisibility(0);
                IPCamera4Activity.this.changeQualityDlgView(1);
            }
        });
        this.qualityBtn3.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.quality_dlg.setVisibility(0);
                IPCamera4Activity.this.changeQualityDlgView(0);
            }
        });
        this.light_dlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.15
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.light_dlg.setVisibility(8);
            }
        });
        this.lightTv1.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.16
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.light_dlg.setVisibility(8);
                IPCamera4Activity.this.updateNightMode(0);
            }
        });
        this.lightTv2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.17
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.light_dlg.setVisibility(8);
                IPCamera4Activity.this.updateNightMode(1);
            }
        });
        this.lightTv3.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.18
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.light_dlg.setVisibility(8);
                IPCamera4Activity.this.updateNightMode(2);
            }
        });
        this.lightBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.light_dlg.setVisibility(0);
                IPCamera4Activity iPCamera4Activity = IPCamera4Activity.this;
                iPCamera4Activity.changeLightDlgView(iPCamera4Activity.currentInfrarred);
            }
        });
        this.alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_loop);
        this.ll_listener.setOnClickListener(this);
        this.ll_flips.setOnClickListener(this);
        this.zoomBtn.setOnClickListener(this);
        this.captureBtn.setOnClickListener(this);
        this.recordBtn.setOnClickListener(this);
        this.listenerBtn.setOnClickListener(this);
        this.flip_btn.setOnClickListener(this);
        this.videoBtn.setOnClickListener(this);
        this.mvZoomAdd = (ModuleView) findViewById(R.id.mv_zoom_add);
        this.mvZoomMinus = (ModuleView) findViewById(R.id.mv_zoom_minus);
        this.mvFocusAdd = (ModuleView) findViewById(R.id.mv_focus_add);
        this.mvFocusMinus = (ModuleView) findViewById(R.id.mv_focus_minus);
        this.mvZoomAdd.setOnClickListener(this);
        this.mvZoomMinus.setOnClickListener(this);
        this.mvFocusAdd.setOnClickListener(this);
        this.mvFocusMinus.setOnClickListener(this);
        addControlTouchView(false);
        setFloatBarState();
        float f = (ScreenUtil.getDisplayMetrics(getActivity())[1] * 1.0f) / ScreenUtil.getDisplayMetrics(getActivity())[0];
        float f2 = (((double) f) < 1.85d || f >= 2.0f) ? f >= 2.0f ? 0.38f : 0.28f : 0.33f;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.video_panel_rl.getLayoutParams();
        layoutParams.width = ScreenUtil.getDisplayMetrics(getActivity())[0];
        layoutParams.height = (int) Math.max((ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f, f2 * ScreenUtil.getDisplayMetrics(getActivity())[1]);
        this.video_panel_rl.setLayoutParams(layoutParams);
        if (this.isOwner) {
            return;
        }
        this.ll_flips.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCamSupportShow() {
        this.zoomSupport = SharePreferenceManager.getInstance().getSupportZoom(this.iotId);
        this.focusSupport = SharePreferenceManager.getInstance().getSupportFocus(this.iotId);
        this.presetSupport = SharePreferenceManager.getInstance().getSupportPreset(this.iotId);
        if (this.zoomSupport == 0 && this.focusSupport == 0 && this.presetSupport == 0) {
            this.bt_control.setVisibility(8);
        } else {
            this.bt_control.setVisibility(0);
        }
    }

    public boolean isSensorOpen() {
        try {
            return Settings.System.getInt(getContentResolver(), "accelerometer_rotation") == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addControlTouchView(boolean z) {
        if (this.touchView == null) {
            this.touchView = new TouchView(getActivity());
        }
        this.rl_touch_view.getViewTreeObserver().addOnGlobalLayoutListener(new AnonymousClass20(z));
    }

    /* JADX INFO: renamed from: activity.IPCamera4Activity$20, reason: invalid class name */
    class AnonymousClass20 implements ViewTreeObserver.OnGlobalLayoutListener {
        final /* synthetic */ boolean val$isLand;

        AnonymousClass20(boolean z) {
            this.val$isLand = z;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (IPCamera4Activity.this.touchView.getParent() != null) {
                ((ViewGroup) IPCamera4Activity.this.touchView.getParent()).removeView(IPCamera4Activity.this.touchView);
            }
            if (!this.val$isLand) {
                if (IPCamera4Activity.this.rl_touch_view.getHeight() == 0) {
                    return;
                }
                IPCamera4Activity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad2);
                IPCamera4Activity.this.touchView.setDefaultSize((IPCamera4Activity.this.rl_touch_view.getHeight() * 170) / 192, IPCamera4Activity.this.getResources().getDimensionPixelSize(R.dimen.ui_joystick_circle_bg_padding));
                IPCamera4Activity.this.touchView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                IPCamera4Activity.this.rl_touch_view.addView(IPCamera4Activity.this.touchView);
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                IPCamera4Activity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad3);
                int dimensionPixelSize = IPCamera4Activity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                IPCamera4Activity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCamera4Activity.this.getActivity(), 120.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                layoutParams.addRule(12);
                IPCamera4Activity.this.video_panel_rl.addView(IPCamera4Activity.this.touchView, layoutParams);
            }
            IPCamera4Activity.this.touchView.setListener(new JoystickTouchViewListener() { // from class: activity.IPCamera4Activity.20.1
                @Override // view.JoystickTouchViewListener
                public void onActionDown() {
                }

                /* JADX WARN: Removed duplicated region for block: B:38:0x0087  */
                @Override // view.JoystickTouchViewListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public void onTouch(float r8, float r9) {
                    /*
                        Method dump skipped, instruction units count: 268
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: activity.IPCamera4Activity.AnonymousClass20.AnonymousClass1.onTouch(float, float):void");
                }

                @Override // view.JoystickTouchViewListener
                public void onReset() {
                    if (IPCamera4Activity.this.ptzTimer != null) {
                        IPCamera4Activity.this.ptzTimer.cancel();
                        IPCamera4Activity.this.ptzTimer = null;
                    }
                    IPCamera4Activity.this.lastActionTypeEnum = null;
                }

                @Override // view.JoystickTouchViewListener
                public void onActionUp() {
                    if (IPCamera4Activity.this.ptzTimer != null) {
                        IPCamera4Activity.this.ptzTimer.cancel();
                        IPCamera4Activity.this.ptzTimer = null;
                    }
                    IPCamera4Activity.this.lastActionTypeEnum = null;
                }
            });
            IPCamera4Activity.this.rl_touch_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    private void setListen(boolean z) {
        if (getResources().getConfiguration().orientation == 2) {
            this.listenerBtn.setImageResource(z ? R.drawable.vocie_on2 : R.drawable.voice_off2);
        } else {
            this.listenerBtn.setImageResource(z ? R.drawable.voice_on : R.drawable.voice_off);
        }
    }

    public String getStatus(int i) {
        switch (i) {
            case 0:
                return getString(R.string.quality_l);
            case 1:
                return getString(R.string.quality_m);
            default:
                return getString(R.string.quality_h);
        }
    }

    private void addPtzBtn() {
        this.zoomInBtn = (Button) findViewById(R.id.zoom_in_btn);
        this.zoomOutBtn = (Button) findViewById(R.id.zoom_out_btn);
        this.upRightBtn = (Button) findViewById(R.id.up_right_btn);
        this.upBtn = (Button) findViewById(R.id.up_btn);
        this.upLeftBtn = (Button) findViewById(R.id.up_left_btn);
        this.rightBtn = (Button) findViewById(R.id.right_btn);
        this.stopBtn = (Button) findViewById(R.id.stop_btn);
        this.leftBtn = (Button) findViewById(R.id.left_btn);
        this.downRightBtn = (Button) findViewById(R.id.down_right_btn);
        this.downBtn = (Button) findViewById(R.id.down_btn);
        this.downLeftBtn = (Button) findViewById(R.id.down_left_btn);
        this.zoomInBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.21
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.ZOOM_IN, SpeedEnum.FAST);
            }
        });
        this.zoomOutBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.22
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.ZOOM_OUT, SpeedEnum.FAST);
            }
        });
        this.upRightBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.23
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.UP_RIGHT, SpeedEnum.FAST);
            }
        });
        this.upBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.24
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.UP, SpeedEnum.FAST);
            }
        });
        this.upLeftBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.25
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.UP_LEFT, SpeedEnum.FAST);
            }
        });
        this.rightBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.26
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.RIGHT, SpeedEnum.FAST);
            }
        });
        this.stopBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.27
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.stopPTZ();
            }
        });
        this.leftBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.28
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.LEFT, SpeedEnum.FAST);
            }
        });
        this.downRightBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.29
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.DOWN_RIGHT, SpeedEnum.FAST);
            }
        });
        this.downBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.30
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.DOWN, SpeedEnum.FAST);
            }
        });
        this.downLeftBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCamera4Activity.31
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCamera4Activity.this.startPTZ(ActionTypeEnum.DOWN_LEFT, SpeedEnum.FAST);
            }
        });
    }

    @Override // activity.CommonActivity, androidx.activity.ComponentActivity, android.app.Activity
    @SuppressLint({"SourceLockedOrientationActivity"})
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == 2) {
            if (getRequestedOrientation() == 0) {
                setRequestedOrientation(1);
                return;
            } else {
                setRequestedOrientation(9);
                return;
            }
        }
        super.onBackPressed();
    }

    private boolean isDefinitionChooseMode() {
        return this.qualityBtn1.getVisibility() == 0 && this.qualityBtn2.getVisibility() == 0 && this.qualityBtn3.getVisibility() == 0;
    }

    private void showDefinitionView() {
        this.qualityBtn1.setVisibility(0);
        this.qualityBtn2.setVisibility(0);
        this.qualityBtn3.setVisibility(0);
    }

    private void changeDefinitionView(int i) {
        switch (i) {
            case 0:
                this.qualityBtn1.setVisibility(8);
                this.qualityBtn2.setVisibility(8);
                this.qualityBtn3.setVisibility(0);
                break;
            case 1:
                this.qualityBtn1.setVisibility(8);
                this.qualityBtn2.setVisibility(0);
                this.qualityBtn3.setVisibility(8);
                break;
            case 2:
                this.qualityBtn1.setVisibility(0);
                this.qualityBtn2.setVisibility(8);
                this.qualityBtn3.setVisibility(8);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeQualityDlgView(int i) {
        switch (i) {
            case 0:
                this.tv_h_quality.setTextColor(getResources().getColor(R.color.color_white));
                this.tv_h_quality.setBackgroundDrawable(null);
                this.tv_m_quality.setTextColor(getResources().getColor(R.color.color_white));
                this.tv_m_quality.setBackgroundDrawable(null);
                this.tv_l_quality.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.tv_l_quality.setTextColor(getResources().getColor(R.color.color_2999ff));
                break;
            case 1:
                this.tv_h_quality.setTextColor(getResources().getColor(R.color.color_white));
                this.tv_h_quality.setBackgroundDrawable(null);
                this.tv_m_quality.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.tv_m_quality.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.tv_l_quality.setBackgroundDrawable(null);
                this.tv_l_quality.setTextColor(getResources().getColor(R.color.color_white));
                break;
            case 2:
                this.tv_h_quality.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.tv_h_quality.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.tv_m_quality.setTextColor(getResources().getColor(R.color.color_white));
                this.tv_m_quality.setBackgroundDrawable(null);
                this.tv_l_quality.setTextColor(getResources().getColor(R.color.color_white));
                this.tv_l_quality.setBackgroundDrawable(null);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeLightDlgView(int i) {
        switch (i) {
            case 0:
                this.lightTv1.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.lightTv1.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.lightTv2.setTextColor(getResources().getColor(R.color.color_white));
                this.lightTv2.setBackgroundDrawable(null);
                this.lightTv3.setTextColor(getResources().getColor(R.color.color_white));
                this.lightTv3.setBackgroundDrawable(null);
                break;
            case 1:
                this.lightTv1.setTextColor(getResources().getColor(R.color.color_white));
                this.lightTv1.setBackgroundDrawable(null);
                this.lightTv2.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.lightTv2.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.lightTv3.setBackgroundDrawable(null);
                this.lightTv3.setTextColor(getResources().getColor(R.color.color_white));
                break;
            case 2:
                this.lightTv1.setTextColor(getResources().getColor(R.color.color_white));
                this.lightTv1.setBackgroundDrawable(null);
                this.lightTv2.setTextColor(getResources().getColor(R.color.color_white));
                this.lightTv2.setBackgroundDrawable(null);
                this.lightTv3.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.lightTv3.setTextColor(getResources().getColor(R.color.color_2999ff));
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeDefinition(final int i) {
        if (i < 0 || i > 3) {
            return;
        }
        HashMap map = new HashMap();
        map.put(Constants.STREAM_VIDEO_QUALITY_MODEL_NAME, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCamera4Activity.32
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        IPCamera4Activity.this.uiHandler.post(new Runnable() { // from class: activity.IPCamera4Activity.32.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCamera4Activity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        SharePreferenceManager.getInstance().setStreamVideoQuality(IPCamera4Activity.this.iotId, i);
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNightMode(final Object obj) {
        HashMap map = new HashMap();
        map.put(Constants.DAY_NIGHT_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCamera4Activity.33
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        IPCamera4Activity.this.uiHandler.post(new Runnable() { // from class: activity.IPCamera4Activity.33.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCamera4Activity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        SharePreferenceManager.getInstance().setDayNightMode(IPCamera4Activity.this.iotId, Integer.parseInt(obj.toString()));
                        IPCamera4Activity.this.uiHandler.post(new Runnable() { // from class: activity.IPCamera4Activity.33.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCamera4Activity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    public void startPTZEx(ActionTypeEnum actionTypeEnum, SpeedEnum speedEnum) {
        IPCManager.getInstance().getDevice(this.iotId).startPTZEx(actionTypeEnum.getCode(), speedEnum.getCode(), new IPanelCallback() { // from class: activity.IPCamera4Activity.34
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                String str = IPCamera4Activity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startPTZExControl:");
                sb.append(z);
                sb.append("       o:");
                sb.append(obj != null ? String.valueOf(obj) : TmpConstant.GROUP_ROLE_UNKNOWN);
                LogEx.e(true, str, sb.toString());
                if (!z || obj == null) {
                    return;
                }
                JSON.parseObject(obj.toString()).getInteger("code").intValue();
            }
        });
    }

    public void startPTZ(ActionTypeEnum actionTypeEnum, SpeedEnum speedEnum) {
        IPCManager.getInstance().getDevice(this.iotId).startPTZ(actionTypeEnum.getCode(), speedEnum.getCode(), new IPanelCallback() { // from class: activity.IPCamera4Activity.35
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                String str = IPCamera4Activity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startPTZControl:");
                sb.append(z);
                sb.append("       o:");
                sb.append(obj != null ? String.valueOf(obj) : TmpConstant.GROUP_ROLE_UNKNOWN);
                LogEx.e(true, str, sb.toString());
                if (!z || obj == null) {
                    return;
                }
                JSON.parseObject(obj.toString()).getInteger("code").intValue();
            }
        });
    }

    public void stopPTZ() {
        IPCManager.getInstance().getDevice(this.iotId).stopPTZ(new IPanelCallback() { // from class: activity.IPCamera4Activity.36
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                String str = IPCamera4Activity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("stopPTZControl:");
                sb.append(z);
                sb.append("       o:");
                sb.append(obj != null ? String.valueOf(obj) : TmpConstant.GROUP_ROLE_UNKNOWN);
                LogEx.e(true, str, sb.toString());
                if (!z || obj == null) {
                    return;
                }
                JSON.parseObject(obj.toString()).getInteger("code").intValue();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFloatBarState() {
        this.playInfoTv.setVisibility((this.isFloat && this.players[this.currentFocusPlayerIndex].getPlayState() == 3) ? 0 : 8);
        if (getResources().getConfiguration().orientation == 2) {
            this.rightView.setVisibility(this.isFloat ? 0 : 8);
            this.touchView.setVisibility(this.isFloat ? 0 : 8);
            this.tv_title.setVisibility(this.isFloat ? 0 : 4);
        } else {
            this.rightView.setVisibility(8);
            this.touchView.setVisibility(0);
            this.tv_title.setVisibility(0);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        final String iotId = this.device.childDevices.get(this.currentFocusPlayerIndex).getIotId();
        if (view2.getId() == R.id.capture_btn || view2.getId() == R.id.ll_capture) {
            snapshot(this.currentFocusPlayerIndex);
            return;
        }
        if (view2.getId() == R.id.record_btn || view2.getId() == R.id.ll_record) {
            startOrStopRecordingMp4();
            return;
        }
        if (view2.getId() == R.id.ll_listener || view2.getId() == R.id.listener_btn) {
            boolean[] zArr = this.speakerSwitch;
            int i = this.currentFocusPlayerIndex;
            zArr[i] = !zArr[i];
            this.players[i].setVolume(zArr[i] ? 1.0f : 0.0f);
            setListen(this.speakerSwitch[this.currentFocusPlayerIndex]);
            return;
        }
        if (view2.getId() == R.id.ll_zoom || view2.getId() == R.id.exo_zoom_tbtn) {
            if (getRequestedOrientation() == 1) {
                setRequestedOrientation(0);
                return;
            } else {
                setRequestedOrientation(8);
                return;
            }
        }
        if (view2.getId() == R.id.ll_flips || view2.getId() == R.id.flip_btn) {
            Intent intent = new Intent(getActivity(), (Class<?>) PayYunServiceActivity2.class);
            intent.putExtra("iotId", iotId);
            intent.putExtra(AlinkConstants.KEY_DN, this.DeviceName);
            intent.putExtra(AlinkConstants.KEY_PK, this.ProductKey);
            startActivity(intent);
            return;
        }
        if (view2.getId() == R.id.ll_flip || view2.getId() == R.id.video_btn) {
            Intent intent2 = new Intent(this, (Class<?>) RecordVideoActivity.class);
            intent2.putExtra("title", this.tv_title.getTitleText());
            intent2.putExtra("iotId", iotId);
            intent2.putExtra("appKey", this.appKey);
            startActivity(intent2);
            return;
        }
        if (view2.getId() == R.id.mv_focus_add) {
            changeFocus(iotId, 1);
            return;
        }
        if (view2.getId() == R.id.mv_focus_minus) {
            changeFocus(iotId, 0);
        } else {
            if (view2.getId() == R.id.mv_zoom_add || view2.getId() == R.id.mv_zoom_minus || view2.getId() != R.id.bt_control) {
                return;
            }
            this.lenCamCtrlDialog = new LenCamCtrlDialog(this, new LenCamCtrlDialog.OnFuncListener() { // from class: activity.IPCamera4Activity.37
                @Override // dialog.LenCamCtrlDialog.OnFuncListener
                public void OnZoomChange(boolean z) {
                    IPCamera4Activity.this.changeZoom(iotId, z ? 1 : 0);
                }

                @Override // dialog.LenCamCtrlDialog.OnFuncListener
                public void OnFocusChange(boolean z) {
                    IPCamera4Activity.this.changeFocus(iotId, z ? 1 : 0);
                }

                @Override // dialog.LenCamCtrlDialog.OnFuncListener
                public void OnPresetInvoke(String str) {
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    IPCamera4Activity.this.changePresetLocation(iotId, Integer.parseInt(str));
                }

                @Override // dialog.LenCamCtrlDialog.OnFuncListener
                public void OnPresetSet(String str) {
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    IPCamera4Activity.this.addPresetLocation(iotId, Integer.parseInt(str));
                }

                @Override // dialog.LenCamCtrlDialog.OnFuncListener
                public void OnPresetDelete(String str) {
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    IPCamera4Activity.this.deletePresetLocation(iotId, Integer.parseInt(str));
                }

                @Override // dialog.LenCamCtrlDialog.OnFuncListener
                public void setWatchPos() {
                    IPCamera4Activity.this.WatchPos();
                }
            }).setNearViewId(R.id.ll_bottom).showZoomCtrl(this.zoomSupport == 1).showFocusCtrl(this.focusSupport == 1).showPresetCtrl(this.presetSupport == 1).showDialog();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void WatchPos() {
        IPCManager.getInstance().getDevice(this.iotId).setWatchPos(new IPanelCallback() { // from class: activity.IPCamera4Activity.38
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    Log.d(IPCamera4Activity.this.TAG, "onComplete: setWatchPos Finish");
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeZoom(String str, int i) {
        IPCManager.getInstance().getDevice(str).changeZoom(i, 0.0f, new IPanelCallback() { // from class: activity.IPCamera4Activity.39
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCamera4Activity.this.TAG, "invoke zoom");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeFocus(String str, int i) {
        IPCManager.getInstance().getDevice(str).changeFocus(i, new IPanelCallback() { // from class: activity.IPCamera4Activity.40
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCamera4Activity.this.TAG, "invoke focus");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changePresetLocation(String str, int i) {
        IPCManager.getInstance().getDevice(str).changePresetLocation(i, new IPanelCallback() { // from class: activity.IPCamera4Activity.41
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCamera4Activity.this.TAG, "change preset");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addPresetLocation(String str, int i) {
        IPCManager.getInstance().getDevice(str).addPresetLocation(i, new IPanelCallback() { // from class: activity.IPCamera4Activity.42
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCamera4Activity.this.TAG, "add preset");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deletePresetLocation(String str, int i) {
        IPCManager.getInstance().getDevice(str).deletePresetLocation(i, new IPanelCallback() { // from class: activity.IPCamera4Activity.43
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCamera4Activity.this.TAG, "delete preset");
            }
        });
    }

    public class MyGLSurfaceViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        public MyGLSurfaceViewGestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            IPCamera4Activity.this.isFloat = !r3.isFloat;
            IPCamera4Activity.this.setFloatBarState();
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            int iIntValue = ((Integer) IPCamera4Activity.this.zoomableTextureViews[IPCamera4Activity.this.currentFocusPlayerIndex].getTag()).intValue();
            if (IPCamera4Activity.this.curPageNum == 1) {
                if (IPCamera4Activity.this.zoomableTextureViews[IPCamera4Activity.this.currentFocusPlayerIndex].getGlTextureView().isScaleMode()) {
                    IPCamera4Activity.this.zoomableTextureViews[IPCamera4Activity.this.currentFocusPlayerIndex].getGlTextureView().reset();
                } else {
                    Log.e("tafsdas", "缩小" + (IPCamera4Activity.this.currentFocusPlayerIndex + 1) + "号窗口，显示4分屏");
                    IPCamera4Activity iPCamera4Activity = IPCamera4Activity.this;
                    iPCamera4Activity.displayFourScreen(iPCamera4Activity.currentFocusPlayerIndex);
                }
            } else {
                Log.e("tafsdas", "放大" + (iIntValue + 1) + "号窗口");
                IPCamera4Activity.this.displayOneScreen(iIntValue);
            }
            return true;
        }
    }

    public void setFocusIndex(int i) {
        int i2 = this.currentFocusPlayerIndex;
        if (i != i2) {
            this.zoomableTextureViews[i2].setBackgroundColor(getResources().getColor(R.color.color_transparent));
            stopRecording();
            stopLiveIntercom();
            this.currentFocusPlayerIndex = i;
            try {
                this.liveIntercoms[i].stop();
            } catch (Exception e) {
                LogEx.e(true, this.TAG, "liveIntercom.stop() error", e);
            }
            this.zoomableTextureViews[i].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            this.iotId = this.device.getChildDevices().get(i).getIotId();
            setListen(this.speakerSwitch[i]);
            setVideoStreamQuality();
            setImageFlip();
            setCamSupportShow();
            set4GService();
            setDayLightMode();
            this.msgHandler.removeCallbacksAndMessages(null);
        }
    }

    private void initTextureViews() {
        for (final int i = 0; i < 4; i++) {
            this.zoomableTextureViews[i] = new ComponentGLLayout(this);
            LivePlayer[] livePlayerArr = this.players;
            if (i < livePlayerArr.length) {
                livePlayerArr[i].setTextureView(this.zoomableTextureViews[i].getGlTextureView());
                this.players[i].setVideoScalingMode(1);
            }
            this.zoomableTextureViews[i].setTag(Integer.valueOf(i));
            this.zoomableTextureViews[i].setId(View.generateViewId());
            this.zoomableTextureViews[i].setClickable(true);
            this.zoomableTextureViews[i].setOnGLLayoutListener(new ComponentGLLayout.OnGLLayoutListener() { // from class: activity.IPCamera4Activity.44
                @Override // view.ComponentGLLayout.OnGLLayoutListener
                public void onVideoPlayBtnChange() {
                    IPCamera4Activity.this.playLive(i);
                }

                @Override // view.ComponentGLLayout.OnGLLayoutListener
                public void onVideoStopBtnChange() {
                    IPCamera4Activity.this.stopLive(i);
                }
            });
            this.zoomableTextureViews[i].getGlTextureView().setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCamera4Activity.45
                /* JADX WARN: Removed duplicated region for block: B:59:0x0109  */
                @Override // android.view.View.OnTouchListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public boolean onTouch(android.view.View r7, android.view.MotionEvent r8) {
                    /*
                        Method dump skipped, instruction units count: 406
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: activity.IPCamera4Activity.AnonymousClass45.onTouch(android.view.View, android.view.MotionEvent):boolean");
                }
            });
            this.playerContainer.addView(this.zoomableTextureViews[i]);
        }
        this.zoomableTextureViews[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayFourScreen(int i) {
        this.curPageNum = 4;
        setFourTextureViews();
        playOthers(i);
        stopRecording();
    }

    private void playOthers(int i) {
        for (int i2 = 0; i2 < this.players.length; i2++) {
            if (i != i2) {
                playLive(i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayOneScreen(int i) {
        this.curPageNum = 1;
        setOneTextureView(i);
        stopOthers(i);
    }

    private void setOneTextureView(int i) {
        this.playerContainerWidth = this.video_panel_rl.getLayoutParams().width;
        this.playerContainerHeight = this.video_panel_rl.getLayoutParams().height;
        for (int i2 = 0; i2 < this.players.length; i2++) {
            if (i2 != i) {
                this.zoomableTextureViews[i2].setVisibility(8);
            }
        }
        Log.e("display change one", "width: " + this.playerContainerWidth + " height: " + this.playerContainerHeight);
        this.zoomableTextureViews[i].setLayoutParams(new ConstraintLayout.LayoutParams(this.playerContainerWidth, this.playerContainerHeight));
    }

    private void stopOthers(int i) {
        int i2 = 0;
        while (true) {
            LivePlayer[] livePlayerArr = this.players;
            if (i2 >= livePlayerArr.length) {
                return;
            }
            if (i2 != i) {
                livePlayerArr[i2].stop();
            }
            i2++;
        }
    }

    private void setFourTextureViews() {
        this.playerContainerWidth = this.video_panel_rl.getLayoutParams().width;
        this.playerContainerHeight = this.video_panel_rl.getLayoutParams().height;
        for (int i = 0; i < this.curPageNum; i++) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(this.playerContainer);
            switch (i) {
                case 0:
                    constraintSet.connect(this.zoomableTextureViews[i].getId(), 1, this.playerContainer.getId(), 1, 0);
                    constraintSet.connect(this.zoomableTextureViews[i].getId(), 3, this.playerContainer.getId(), 3, 0);
                    break;
                case 1:
                    constraintSet.connect(this.zoomableTextureViews[i].getId(), 2, this.playerContainer.getId(), 2, 0);
                    constraintSet.connect(this.zoomableTextureViews[i].getId(), 3, this.playerContainer.getId(), 3, 0);
                    break;
                case 2:
                    constraintSet.connect(this.zoomableTextureViews[i].getId(), 1, this.playerContainer.getId(), 1, 0);
                    constraintSet.connect(this.zoomableTextureViews[i].getId(), 4, this.playerContainer.getId(), 4, 0);
                    break;
                case 3:
                    constraintSet.connect(this.zoomableTextureViews[i].getId(), 2, this.playerContainer.getId(), 2, 0);
                    constraintSet.connect(this.zoomableTextureViews[i].getId(), 4, this.playerContainer.getId(), 4, 0);
                    break;
            }
            Log.e("display change four", "width: " + (this.playerContainerWidth / 2) + " height: " + (this.playerContainerHeight / 2));
            constraintSet.constrainWidth(this.zoomableTextureViews[i].getId(), this.playerContainerWidth / 2);
            constraintSet.constrainHeight(this.zoomableTextureViews[i].getId(), this.playerContainerHeight / 2);
            constraintSet.applyTo(this.playerContainer);
            this.zoomableTextureViews[i].setPadding(2, 2, 2, 2);
            this.zoomableTextureViews[i].getGlTextureView().reset();
            this.zoomableTextureViews[i].setVisibility(0);
        }
    }

    private void initPlayer() {
        this.myGLSurfaceViewGestureListener = new MyGLSurfaceViewGestureListener();
        this.mGestureDector = new GestureDetector(this, this.myGLSurfaceViewGestureListener);
        final int i = 0;
        while (true) {
            LivePlayer[] livePlayerArr = this.players;
            if (i >= livePlayerArr.length) {
                return;
            }
            livePlayerArr[i] = new LivePlayer();
            LivePlayer livePlayer = this.players[i];
            if (getResources().getConfiguration().orientation == 2 || this.defaultDefinition != 2) {
                livePlayer.setVideoScalingMode(1);
            } else {
                livePlayer.setVideoScalingMode(2);
            }
            livePlayer.setVolume(this.speakerSwitch[i] ? 1.0f : 0.0f);
            setListen(this.speakerSwitch[i]);
            livePlayer.setOnErrorListener(new OnErrorListener() { // from class: activity.IPCamera4Activity.46
                @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
                @SuppressLint({"StringFormatInvalid"})
                public void onError(PlayerException playerException) {
                    if (playerException.getSubCode() == 1009 && playerException.getCode() == 6 && playerException.getLocalizedMessage().equals("请求认证错误")) {
                        IPCamera4Activity iPCamera4Activity = IPCamera4Activity.this;
                        iPCamera4Activity.showToast(iPCamera4Activity.getString(R.string.account_squeezed));
                        return;
                    }
                    switch (playerException.getCode()) {
                        case 6:
                            switch (playerException.getSubCode()) {
                                case 1005:
                                    IPCamera4Activity iPCamera4Activity2 = IPCamera4Activity.this;
                                    iPCamera4Activity2.showToast(iPCamera4Activity2.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1006:
                                    IPCamera4Activity iPCamera4Activity3 = IPCamera4Activity.this;
                                    iPCamera4Activity3.showToast(iPCamera4Activity3.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1007:
                                    IPCamera4Activity iPCamera4Activity4 = IPCamera4Activity.this;
                                    iPCamera4Activity4.showToast(iPCamera4Activity4.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1008:
                                    IPCamera4Activity iPCamera4Activity5 = IPCamera4Activity.this;
                                    iPCamera4Activity5.showToast(iPCamera4Activity5.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1009:
                                    IPCamera4Activity iPCamera4Activity6 = IPCamera4Activity.this;
                                    iPCamera4Activity6.showToast(iPCamera4Activity6.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                            }
                            break;
                        case 7:
                            if (playerException.getSubCode() == 1000) {
                                IPCamera4Activity iPCamera4Activity7 = IPCamera4Activity.this;
                                iPCamera4Activity7.showToast(iPCamera4Activity7.getString(R.string.play_failed_retry, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                            }
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCamera4Activity.this.zoomableTextureViews[i].is1100ErrorPre > 0) {
                                    IPCamera4Activity.this.zoomableTextureViews[i].is1100ErrorPre--;
                                    IPCamera4Activity.this.defaultDefinition = SharePreferenceManager.getInstance().getStreamVideoQuality(IPCamera4Activity.this.iotId);
                                    int unused = IPCamera4Activity.this.defaultDefinition;
                                    IPCamera4Activity.this.players[i].stop();
                                    IPCamera4Activity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.IPCamera4Activity.46.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            IPCamera4Activity.this.playLive(i);
                                        }
                                    }, 500L);
                                    return;
                                }
                                IPCamera4Activity iPCamera4Activity8 = IPCamera4Activity.this;
                                iPCamera4Activity8.showToast(iPCamera4Activity8.getString(R.string.play_failed_retry, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                            }
                            break;
                    }
                    IPCamera4Activity.this.zoomableTextureViews[i].showPlayButton();
                }
            });
            livePlayer.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCamera4Activity.47
                @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
                public void onPlayerStateChange(int i2) {
                    switch (i2) {
                        case 1:
                            LogEx.i(true, IPCamera4Activity.this.TAG, "STATE_IDLE");
                            break;
                        case 2:
                            IPCamera4Activity.this.zoomableTextureViews[i].dismissPlayButton();
                            IPCamera4Activity.this.zoomableTextureViews[i].showBuffering();
                            LogEx.i(true, IPCamera4Activity.this.TAG, "STATE_BUFFERING");
                            break;
                        case 3:
                            IPCamera4Activity.this.zoomableTextureViews[i].is1100ErrorPre = 10;
                            IPCamera4Activity.this.zoomableTextureViews[i].dismissSnapPicture();
                            IPCamera4Activity.this.zoomableTextureViews[i].dismissBuffering();
                            IPCamera4Activity.this.AutoSnap(i);
                            LogEx.i(true, IPCamera4Activity.this.TAG, "STATE_READY");
                            IPCamera4Activity.this.isFirstShowStreamType = true;
                            IPCamera4Activity.this.showPlayInfo();
                            break;
                        case 4:
                            LogEx.i(true, IPCamera4Activity.this.TAG, "STATE_ENDED");
                            IPCamera4Activity.this.dismissPlayInfo();
                            IPCamera4Activity.this.zoomableTextureViews[i].showPlayButton();
                            IPCamera4Activity.this.setRecordState();
                            if (IPCamera4Activity.this.currentFocusPlayerIndex == i) {
                                IPCamera4Activity.this.stopRecording();
                            }
                            break;
                    }
                }
            });
            i++;
        }
    }

    public void setSpeakerBtn(final int i) {
        runOnUiThread(new Runnable() { // from class: activity.IPCamera4Activity.48
            @Override // java.lang.Runnable
            public void run() {
                if (IPCamera4Activity.this.isFinishing()) {
                    return;
                }
                IPCamera4Activity.this.isSpeakerOpen = i;
                if (IPCamera4Activity.this.getResources().getConfiguration().orientation == 2) {
                    if (IPCamera4Activity.this.isSpeakerOpen == 1) {
                        IPCamera4Activity.this.speakBtn.setImageResource(R.drawable.camera_mic_push2);
                        return;
                    } else if (IPCamera4Activity.this.isSpeakerOpen == 0) {
                        IPCamera4Activity.this.speakBtn.setImageResource(R.drawable.camera_mic_nor2);
                        return;
                    } else {
                        IPCamera4Activity.this.speakBtn.setImageResource(R.drawable.ic_speaking2);
                        return;
                    }
                }
                if (IPCamera4Activity.this.isSpeakerOpen == 1) {
                    IPCamera4Activity.this.speakBtn.setImageResource(R.drawable.camera_mic_push);
                } else if (IPCamera4Activity.this.isSpeakerOpen == 0) {
                    IPCamera4Activity.this.speakBtn.setImageResource(R.drawable.camera_mic_nor);
                } else {
                    IPCamera4Activity.this.speakBtn.setImageResource(R.drawable.ic_speaking2);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecordError() {
        handleLiveIntercomError();
        runOnUiThread(new Runnable() { // from class: activity.IPCamera4Activity.49
            @Override // java.lang.Runnable
            public void run() {
                IPCamera4Activity.this.isLiveIntercoming = false;
                if (IPCamera4Activity.this.isFinishing()) {
                    return;
                }
                IPCamera4Activity.this.setSpeakerBtn(0);
                IPCamera4Activity.this.speakBtn.setEnabled(true);
            }
        });
    }

    private void initLiveIntercom() {
        for (int i = 0; i < this.players.length; i++) {
            final LiveIntercomV2 liveIntercomV2 = new LiveIntercomV2(this, this.iotId, LiveIntercomV2.LiveIntercomMode.SingleTalk, AudioParams.AUDIOPARAM_MONO_8K_G711A);
            liveIntercomV2.setGainLevel(-1);
            liveIntercomV2.setLiveIntercomV2Listener(new LiveIntercomV2Listener() { // from class: activity.IPCamera4Activity.50
                @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
                public void onRecordBufferReceived(byte[] bArr, int i2, int i3) {
                }

                @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
                public void onTalkReady() {
                    LogEx.e(true, "speaker----", "1 " + IPCamera4Activity.this.isLiveIntercoming);
                    IPCamera4Activity.this.runOnUiThread(new Runnable() { // from class: activity.IPCamera4Activity.50.1
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCamera4Activity.this.showToast(IPCamera4Activity.this.getResources().getString(R.string.can_begin_talk));
                            if (IPCamera4Activity.this.isFinishing()) {
                                return;
                            }
                            IPCamera4Activity.this.whiteProgressDialog.dismiss();
                            IPCamera4Activity.this.setSpeakerBtn(2);
                            IPCamera4Activity.this.speakBtn.setEnabled(true);
                        }
                    });
                }

                @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
                public void onError(LiveIntercomException liveIntercomException) {
                    LogEx.e(true, "speaker----", "2 " + IPCamera4Activity.this.isLiveIntercoming);
                    int code = liveIntercomException.getCode();
                    if (code != 16) {
                        switch (code) {
                            case 1:
                                IPCamera4Activity iPCamera4Activity = IPCamera4Activity.this;
                                iPCamera4Activity.showToast(iPCamera4Activity.getString(R.string.record_error1));
                                IPCamera4Activity.this.handleLiveIntercomError();
                                break;
                            case 2:
                                IPCamera4Activity iPCamera4Activity2 = IPCamera4Activity.this;
                                iPCamera4Activity2.showToast(iPCamera4Activity2.getString(R.string.record_error2));
                                IPCamera4Activity.this.handleLiveIntercomError();
                                break;
                            case 3:
                                IPCamera4Activity iPCamera4Activity3 = IPCamera4Activity.this;
                                iPCamera4Activity3.showToast(iPCamera4Activity3.getString(R.string.record_error3));
                                IPCamera4Activity.this.handleLiveIntercomError();
                                break;
                            default:
                                switch (code) {
                                    case 5:
                                        IPCamera4Activity iPCamera4Activity4 = IPCamera4Activity.this;
                                        iPCamera4Activity4.showToast(iPCamera4Activity4.getString(R.string.record_error4));
                                        IPCamera4Activity.this.handleLiveIntercomError();
                                        break;
                                    case 6:
                                        IPCamera4Activity iPCamera4Activity5 = IPCamera4Activity.this;
                                        iPCamera4Activity5.showToast(iPCamera4Activity5.getString(R.string.record_error5));
                                        IPCamera4Activity.this.handleLiveIntercomError();
                                        break;
                                    case 7:
                                        IPCamera4Activity iPCamera4Activity6 = IPCamera4Activity.this;
                                        iPCamera4Activity6.showToast(iPCamera4Activity6.getString(R.string.record_error6));
                                        IPCamera4Activity.this.onRecordError();
                                        break;
                                    case 8:
                                        IPCamera4Activity iPCamera4Activity7 = IPCamera4Activity.this;
                                        iPCamera4Activity7.showToast(iPCamera4Activity7.getString(R.string.record_error7));
                                        IPCamera4Activity.this.onRecordError();
                                        break;
                                    case 9:
                                        IPCamera4Activity iPCamera4Activity8 = IPCamera4Activity.this;
                                        iPCamera4Activity8.showToast(iPCamera4Activity8.getString(R.string.record_error8));
                                        IPCamera4Activity.this.onRecordError();
                                        break;
                                }
                                break;
                        }
                    } else {
                        IPCamera4Activity iPCamera4Activity9 = IPCamera4Activity.this;
                        iPCamera4Activity9.showToast(iPCamera4Activity9.getString(R.string.record_error9));
                        IPCamera4Activity.this.onRecordError();
                    }
                    liveIntercomException.printStackTrace();
                }

                @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
                public void onRecordStart() {
                    LogEx.d(true, IPCamera4Activity.this.TAG, "onRecordStart");
                    IPCamera4Activity.this.runOnUiThread(new Runnable() { // from class: activity.IPCamera4Activity.50.2
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCamera4Activity.this.isLiveIntercoming = true;
                        }
                    });
                }

                @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
                public void onRecordEnd() {
                    LogEx.d(true, IPCamera4Activity.this.TAG, "onRecordEnd");
                    liveIntercomV2.stop();
                    IPCamera4Activity.this.runOnUiThread(new Runnable() { // from class: activity.IPCamera4Activity.50.3
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCamera4Activity.this.isLiveIntercoming = false;
                            if (IPCamera4Activity.this.isFinishing()) {
                                return;
                            }
                            IPCamera4Activity.this.whiteProgressDialog.dismiss();
                            IPCamera4Activity.this.setSpeakerBtn(0);
                            IPCamera4Activity.this.speakBtn.setEnabled(true);
                        }
                    });
                }
            });
        }
        verifyStoragePermissions(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLiveIntercomError() {
        runOnUiThread(new Runnable() { // from class: activity.IPCamera4Activity.51
            @Override // java.lang.Runnable
            public void run() {
                if (IPCamera4Activity.this.isFinishing()) {
                    return;
                }
                IPCamera4Activity.this.whiteProgressDialog.dismiss();
            }
        });
        this.liveIntercoms[this.currentFocusPlayerIndex].stop();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 4372 && iArr.length > 0 && iArr[0] == 0 && !this.isLiveIntercoming) {
            this.speakBtn.setEnabled(false);
            this.whiteProgressDialog.setText(getResources().getString(R.string.open_speech));
            this.whiteProgressDialog.show();
            setSpeakerBtn(1);
            this.liveIntercoms[this.currentFocusPlayerIndex].start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOrStopLiveIntercom() {
        if (!this.isLiveIntercoming) {
            if (ActivityCompat.checkSelfPermission(this, Permission.RECORD_AUDIO) != 0) {
                ActivityCompat.requestPermissions(this, new String[]{Permission.RECORD_AUDIO}, 4372);
                return;
            }
            setSpeakerBtn(1);
            this.liveIntercoms[this.currentFocusPlayerIndex].start();
            Log.e("speaker----", "play");
            this.speakBtn.setEnabled(false);
            this.whiteProgressDialog.setText(getResources().getString(R.string.open_speech));
            this.whiteProgressDialog.show();
            return;
        }
        this.speakBtn.setEnabled(false);
        this.whiteProgressDialog.setText(getResources().getString(R.string.close_speech));
        this.whiteProgressDialog.show();
        runOnUiThread(new Runnable() { // from class: activity.IPCamera4Activity.52
            @Override // java.lang.Runnable
            public void run() {
                IPCamera4Activity.this.speakBtn.clearAnimation();
            }
        });
        this.liveIntercoms[this.currentFocusPlayerIndex].stop();
        Log.e("speaker----", "stop");
    }

    private void stopLiveIntercom() {
        if (this.isLiveIntercoming) {
            this.speakBtn.setEnabled(false);
            this.whiteProgressDialog.setText(getResources().getString(R.string.close_speech));
            this.whiteProgressDialog.show();
            runOnUiThread(new Runnable() { // from class: activity.IPCamera4Activity.53
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCamera4Activity.this.isFinishing()) {
                        return;
                    }
                    IPCamera4Activity.this.speakBtn.clearAnimation();
                }
            });
            this.liveIntercoms[this.currentFocusPlayerIndex].stop();
            Log.e("speaker----", "stop");
            return;
        }
        this.whiteProgressDialog.dismiss();
    }

    private void startOrStopRecordingMp4() {
        verifyStoragePermissions(this);
        long j = 0;
        if (this.isRecordingMp4) {
            if (this.countTime <= 3) {
                showToast(getResources().getString(R.string.record_time_short));
                return;
            }
            if (this.players[this.currentFocusPlayerIndex].stopRecordingContent()) {
                EventBus.getDefault().post(new RefreshPicture());
                showToast(getResources().getString(R.string.record_check));
                try {
                    j = Long.parseLong(this.file.getName().substring(0, this.file.getName().indexOf(".")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    MediaStoreUtil.addVideoFileToMediaStore(this, this.file, j);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } else {
                showToast(getResources().getString(R.string.save_fail));
            }
            this.isRecordingMp4 = false;
            setRecordState();
            Timer timer = this.timer;
            if (timer != null) {
                timer.cancel();
                this.timer = null;
            }
            this.tvRecordTime.setText("");
            this.tvRecordTime.setVisibility(8);
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getActivity().getPackageName() + "/" + Utils.getUserPhone() + "/video/");
        if (file.exists() || file.mkdirs()) {
            this.file = new File(file, System.currentTimeMillis() + ".mp4");
            try {
                if (this.players[this.currentFocusPlayerIndex].startRecordingContent(this.file)) {
                    this.isRecordingMp4 = true;
                    setRecordState();
                    if (this.timer != null) {
                        this.timer.cancel();
                        this.timer = null;
                    }
                    this.countTime = 0L;
                    this.tvRecordTime.setVisibility(0);
                    this.tvRecordTime.setText(getResources().getString(R.string.recording, "00:00:00"));
                    this.timer = new Timer();
                    this.timer.schedule(new TimerTask() { // from class: activity.IPCamera4Activity.54
                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            if (IPCamera4Activity.this.timer != null) {
                                IPCamera4Activity.this.mHandler.post(new Runnable() { // from class: activity.IPCamera4Activity.54.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        String strValueOf;
                                        String strValueOf2;
                                        String strValueOf3;
                                        if (IPCamera4Activity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCamera4Activity.this.countTime++;
                                        int i = (int) (IPCamera4Activity.this.countTime / 3600);
                                        int i2 = (int) ((IPCamera4Activity.this.countTime % 3600) / 60);
                                        int i3 = (int) ((IPCamera4Activity.this.countTime % 3600) % 60);
                                        StringBuilder sb = new StringBuilder();
                                        if (i > 9) {
                                            strValueOf = String.valueOf(i);
                                        } else {
                                            strValueOf = "0" + i;
                                        }
                                        sb.append(strValueOf);
                                        sb.append(":");
                                        if (i2 > 9) {
                                            strValueOf2 = String.valueOf(i2);
                                        } else {
                                            strValueOf2 = "0" + i2;
                                        }
                                        sb.append(strValueOf2);
                                        sb.append(":");
                                        if (i3 > 9) {
                                            strValueOf3 = String.valueOf(i3);
                                        } else {
                                            strValueOf3 = "0" + i3;
                                        }
                                        sb.append(strValueOf3);
                                        IPCamera4Activity.this.tvRecordTime.setText(IPCamera4Activity.this.getResources().getString(R.string.recording, sb.toString()));
                                    }
                                });
                            }
                        }
                    }, 50L, 1000L);
                    return;
                }
                showToast(getResources().getString(R.string.record_fail));
            } catch (Exception e3) {
                e3.printStackTrace();
                showToast(this.file.getAbsolutePath() + getResources().getString(R.string.not_write));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRecordState() {
        if (getResources().getConfiguration().orientation == 2) {
            this.recordBtn.setImageResource(this.isRecordingMp4 ? R.drawable.camera_record_selected2 : R.drawable.camera_record2);
        } else {
            this.recordBtn.setImageResource(this.isRecordingMp4 ? R.drawable.camera_record_selected : R.drawable.camera_record);
        }
    }

    public boolean isFullScreen() {
        return getResources().getConfiguration().orientation == 2;
    }

    public static void verifyStoragePermissions(Activity activity2) {
        try {
            if (ActivityCompat.checkSelfPermission(activity2, Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                ActivityCompat.requestPermissions(activity2, new String[]{Permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlayInfo() {
        if (this.isFloat) {
            this.playInfoTv.setVisibility(0);
        }
        updatePlayInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPlayInfo() {
        this.playInfoTv.setVisibility(8);
        ScheduledFuture<?> scheduledFuture = this.updatePlayInfoHandle;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.updatePlayInfoHandle = null;
        }
    }

    public void updatePlayInfo() {
        if (this.updatePlayInfoHandle == null) {
            this.updatePlayInfoHandle = this.scheduledExecutorService.scheduleAtFixedRate(this.updatePlayInfoTimerTask, 1L, 1L, TimeUnit.SECONDS);
        }
        final String str = ((this.players[this.currentFocusPlayerIndex].getCurrentPlayInfo().bitRate / 1024) / 8) + "KB/S";
        this.tv_times.setText(DateUtil.getCompleteTime(getActivity()));
        if (this.isFirstShowStreamType) {
            this.playInfoTv.setText(str);
            this.msgHandler.postDelayed(new Runnable() { // from class: activity.IPCamera4Activity.55
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCamera4Activity.this.isFinishing()) {
                        return;
                    }
                    IPCamera4Activity.this.playInfoTv.setText(str + "[" + IPCamera4Activity.this.players[IPCamera4Activity.this.currentFocusPlayerIndex].getStreamConnectType().getValue() + "]");
                    IPCamera4Activity.this.isFirstShowStreamType = false;
                }
            }, 5000L);
        } else {
            this.msgHandler.post(new Runnable() { // from class: activity.IPCamera4Activity.56
                @Override // java.lang.Runnable
                public void run() {
                    IPCamera4Activity.this.playInfoTv.setText(str + "[" + IPCamera4Activity.this.players[IPCamera4Activity.this.currentFocusPlayerIndex].getStreamConnectType().getValue() + "]");
                }
            });
        }
    }

    private void playLive() {
        for (int i = 0; i < this.players.length; i++) {
            playLive(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playLive(final int i) {
        if (isFinishing() || isNotExistIndex(i)) {
            return;
        }
        String iotId = this.device.childDevices.get(i).getIotId();
        if (this.players[i].getPlayState() != 3) {
            this.zoomableTextureViews[i].getGlTextureView().reset();
            this.zoomableTextureViews[i].showSnapPicture(this.device.getIotId(), iotId, i);
        }
        LogEx.i(true, this.TAG, "playLive");
        this.players[i].stop();
        this.players[i].setIPCLiveDataSource(iotId, 0, false, 0, true);
        keepScreenLight();
        this.players[i].setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCamera4Activity.58
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCamera4Activity.this.players[i].start();
            }
        });
        this.players[i].prepare();
    }

    private void AutoSnap() {
        for (int i = 0; i < this.players.length; i++) {
            AutoSnap(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void AutoSnap(int i) {
        Bitmap bitmapSnapShot;
        if (ActivityCompat.checkSelfPermission(this.mContext, Permission.WRITE_EXTERNAL_STORAGE) == 0 && this.players[i].getPlayState() == 3 && (bitmapSnapShot = this.players[i].snapShot()) != null && bitmapSnapShot != null) {
            saveBitmap(i, bitmapSnapShot);
        }
    }

    private void stopLive() {
        for (int i = 0; i < this.players.length; i++) {
            stopLive(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLive(int i) {
        if (isNotExistIndex(i)) {
            return;
        }
        this.players[i].stop();
        stopScreenLight();
    }

    private void snapshot(int i) {
        if (this.players[i].getPlayState() != 3) {
            Toast.makeText(this.mContext, R.string.only_play_snap, 0).show();
            return;
        }
        Bitmap bitmapSnapShot = this.players[i].snapShot();
        if (bitmapSnapShot == null) {
            showToast(getResources().getString(R.string.no_snap));
        } else if (bitmapSnapShot != null) {
            SnapshotPreviewDialog.saveImageToGallery(this, bitmapSnapShot);
            showToast(getResources().getString(R.string.camera_check));
        }
    }

    public void saveBitmap(final int i, final Bitmap bitmap) {
        final String iotId = this.device.getChildDevices().get(i).getIotId();
        final String iotId2 = this.device.getIotId();
        final Application application = getApplication();
        new Thread(new Runnable() { // from class: activity.IPCamera4Activity.59
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                FileOutputStream fileOutputStream;
                File file;
                String string = SpUtil.getString(IPCamera4Activity.this.mContext, Utils.getDevSnapKey(OpenAccountUIConstants.UNDER_LINE + iotId2 + OpenAccountUIConstants.UNDER_LINE + iotId), "");
                long jCurrentTimeMillis = System.currentTimeMillis();
                LogEx.d(true, IPCamera4Activity.this.TAG, "保存图片");
                String devSnapDir = Utils.getDevSnapDir(application, iotId);
                FileOutputStream fileOutputStream2 = null;
                try {
                    try {
                        try {
                            File file2 = new File(devSnapDir);
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }
                            file = new File(devSnapDir, jCurrentTimeMillis + com.xiaomi.mipush.sdk.Constants.ACCEPT_TIME_SEPARATOR_SERVER + i + ".png");
                            fileOutputStream = new FileOutputStream(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    } catch (Exception e2) {
                        e = e2;
                    }
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                }
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                    fileOutputStream.flush();
                    LogEx.e(true, IPCamera4Activity.this.TAG, "图片保存地址: " + file.getAbsolutePath());
                    SpUtil.putValue(application, Utils.getDevSnapKey(OpenAccountUIConstants.UNDER_LINE + iotId2 + OpenAccountUIConstants.UNDER_LINE + iotId), file.getAbsolutePath());
                    EventBus.getDefault().post(new CameraSnapUpdate(iotId2, jCurrentTimeMillis));
                    FileUtil.deleteFile(string);
                    fileOutputStream.close();
                } catch (Exception e3) {
                    e = e3;
                    fileOutputStream2 = fileOutputStream;
                    e.printStackTrace();
                    FileUtil.delete(devSnapDir);
                    if (fileOutputStream2 == null) {
                    } else {
                        fileOutputStream2.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        }).start();
    }

    private void hideOtherView() {
        this.rightView.setVisibility(0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.video_panel_rl.getLayoutParams();
        layoutParams.width = ScreenUtil.getDisplayMetrics(getActivity())[0];
        layoutParams.height = ScreenUtil.getDisplayMetrics(getActivity())[1];
        this.video_panel_rl.setLayoutParams(layoutParams);
        addControlTouchView(true);
        this.isFloat = false;
        setFloatBarState();
        this.fl_title.setVisibility(8);
        this.view_line.setVisibility(8);
        this.containerRl.setVisibility(8);
        this.tv_capture.setVisibility(8);
        this.tv_record.setVisibility(8);
        this.tv_voice.setVisibility(8);
        this.tv_capture.setTextColor(getResources().getColor(R.color.colorAccent));
        this.tv_record.setTextColor(getResources().getColor(R.color.colorAccent));
        this.tv_voice.setTextColor(getResources().getColor(R.color.colorAccent));
        if (this.ll_title.getParent() != null) {
            ((ViewGroup) this.ll_title.getParent()).removeView(this.ll_title);
        }
        this.video_panel_rl.addView(this.ll_title, 2);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.tvRecordTime.getLayoutParams();
        layoutParams2.removeRule(12);
        layoutParams2.addRule(3, R.id.ll_title);
        this.tvRecordTime.setLayoutParams(layoutParams2);
        this.tv_title.setRightLlGone(true);
        this.tv_title.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        this.tv_title.setTitleTextColor(R.color.color_white);
        this.tv_title.setTitleBackTextColor(R.color.color_white);
        this.tv_times.setTextColor(getResources().getColor(R.color.color_white));
        this.tv_line.setTextColor(getResources().getColor(R.color.color_white));
        this.tv_title.setLeftImgId(R.drawable.ic_back2);
        if (Build.VERSION.SDK_INT >= 19) {
            LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.tv_title.getLayoutParams();
            layoutParams3.height = ScreenUtil.dp2Px(this, 39.0f);
            this.tv_title.setTitleRlPadding(0);
            this.tv_title.setLayoutParams(layoutParams3);
        }
        this.tv_title.setTBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_transparent));
        LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) this.listenerBtn.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams) this.ll_video_qt.getLayoutParams();
        LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) this.speakBtn.getLayoutParams();
        LinearLayout.LayoutParams layoutParams7 = (LinearLayout.LayoutParams) this.recordBtn.getLayoutParams();
        LinearLayout.LayoutParams layoutParams8 = (LinearLayout.LayoutParams) this.captureBtn.getLayoutParams();
        if (this.ll_listener.getParent() != null) {
            ((ViewGroup) this.ll_listener.getParent()).removeView(this.ll_listener);
        }
        layoutParams4.width = ScreenUtil.dp2Px(getActivity(), 42.0f);
        layoutParams4.height = ScreenUtil.dp2Px(getActivity(), 42.0f);
        this.listenerBtn.setLayoutParams(layoutParams4);
        this.rightView.addView(this.ll_listener, 1);
        setListen(this.speakerSwitch[this.currentFocusPlayerIndex]);
        this.listenerBtn.setBackgroundResource(R.drawable.bg_gray_oval);
        this.listenerBtn.setPadding(ScreenUtil.dp2Px(this, 10.0f), ScreenUtil.dp2Px(this, 10.0f), ScreenUtil.dp2Px(this, 10.0f), ScreenUtil.dp2Px(this, 10.0f));
        layoutParams5.removeRule(12);
        layoutParams5.leftMargin = 0;
        layoutParams5.bottomMargin = 0;
        layoutParams5.addRule(10);
        layoutParams5.addRule(11);
        layoutParams5.topMargin = ScreenUtil.dp2Px(getActivity(), 10.0f);
        layoutParams5.rightMargin = ScreenUtil.dp2Px(getActivity(), 20.0f);
        this.ll_video_qt.setLayoutParams(layoutParams5);
        this.bottomView.setVisibility(8);
        if (this.speakBtn.getParent() != null) {
            ((ViewGroup) this.speakBtn.getParent()).removeView(this.speakBtn);
        }
        layoutParams6.width = ScreenUtil.dp2Px(getActivity(), 42.0f);
        layoutParams6.height = ScreenUtil.dp2Px(getActivity(), 42.0f);
        setSpeakerBtn(this.isSpeakerOpen);
        this.rightView.addView(this.speakBtn, 2, layoutParams6);
        if (this.ll_record.getParent() != null) {
            ((ViewGroup) this.ll_record.getParent()).removeView(this.ll_record);
        }
        layoutParams7.width = ScreenUtil.dp2Px(getActivity(), 42.0f);
        layoutParams7.height = ScreenUtil.dp2Px(getActivity(), 42.0f);
        this.recordBtn.setLayoutParams(layoutParams7);
        setRecordState();
        this.recordBtn.setBackgroundResource(R.drawable.bg_gray_oval);
        this.recordBtn.setPadding(ScreenUtil.dp2Px(this, 10.0f), ScreenUtil.dp2Px(this, 10.0f), ScreenUtil.dp2Px(this, 10.0f), ScreenUtil.dp2Px(this, 10.0f));
        this.rightView.addView(this.ll_record, 3);
        if (this.ll_capture.getParent() != null) {
            ((ViewGroup) this.ll_capture.getParent()).removeView(this.ll_capture);
        }
        layoutParams8.width = ScreenUtil.dp2Px(getActivity(), 42.0f);
        layoutParams8.height = ScreenUtil.dp2Px(getActivity(), 42.0f);
        this.captureBtn.setLayoutParams(layoutParams8);
        this.captureBtn.setImageResource(R.drawable.camera_capture2);
        this.captureBtn.setBackgroundResource(R.drawable.bg_gray_oval);
        this.captureBtn.setPadding(ScreenUtil.dp2Px(this, 10.0f), ScreenUtil.dp2Px(this, 10.0f), ScreenUtil.dp2Px(this, 10.0f), ScreenUtil.dp2Px(this, 10.0f));
        this.rightView.addView(this.ll_capture, 4);
        for (int i = 0; i < this.rightView.getChildCount(); i++) {
            View childAt = this.rightView.getChildAt(i);
            LinearLayout.LayoutParams layoutParams9 = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            layoutParams9.rightMargin = ScreenUtil.dp2Px(getActivity(), 20.0f);
            childAt.setLayoutParams(layoutParams9);
        }
        if (this.quality_dlg.getParent() != null) {
            ((ViewGroup) this.quality_dlg.getParent()).removeView(this.quality_dlg);
        }
        ((RelativeLayout) this.rightView.getParent()).addView(this.quality_dlg);
        if (this.light_dlg.getParent() != null) {
            ((ViewGroup) this.light_dlg.getParent()).removeView(this.light_dlg);
        }
        ((RelativeLayout) this.rightView.getParent()).addView(this.light_dlg);
        hideSystemUI();
    }

    private void showOtherView() {
        this.rightView.setVisibility(8);
        for (int i = 0; i < this.rightView.getChildCount(); i++) {
            View childAt = this.rightView.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            layoutParams.rightMargin = 0;
            childAt.setLayoutParams(layoutParams);
        }
        float f = (ScreenUtil.getDisplayMetrics(getActivity())[1] * 1.0f) / ScreenUtil.getDisplayMetrics(getActivity())[0];
        float f2 = (((double) f) < 1.85d || f >= 2.0f) ? f >= 2.0f ? 0.38f : 0.28f : 0.33f;
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.video_panel_rl.getLayoutParams();
        layoutParams2.width = ScreenUtil.getDisplayMetrics(getActivity())[0];
        layoutParams2.height = (int) Math.max((ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f, f2 * ScreenUtil.getDisplayMetrics(getActivity())[1]);
        this.video_panel_rl.setLayoutParams(layoutParams2);
        addControlTouchView(false);
        this.isFloat = false;
        setFloatBarState();
        this.fl_title.setVisibility(0);
        this.view_line.setVisibility(0);
        this.containerRl.setVisibility(0);
        this.tv_capture.setVisibility(0);
        this.tv_record.setVisibility(0);
        this.tv_voice.setVisibility(0);
        this.tv_capture.setTextColor(getResources().getColor(R.color.colors_aeb3c0));
        this.tv_record.setTextColor(getResources().getColor(R.color.colors_aeb3c0));
        this.tv_voice.setTextColor(getResources().getColor(R.color.colors_aeb3c0));
        if (this.ll_title.getParent() != null) {
            ((ViewGroup) this.ll_title.getParent()).removeView(this.ll_title);
        }
        this.ll_root.addView(this.ll_title, 0);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.tvRecordTime.getLayoutParams();
        layoutParams3.removeRule(3);
        layoutParams3.addRule(12);
        this.tvRecordTime.setLayoutParams(layoutParams3);
        this.tv_title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        this.tv_title.setTitleTextColor(R.color.color_black);
        this.tv_title.setTitleBackTextColor(R.color.colorAccent);
        this.tv_times.setTextColor(getResources().getColor(R.color.colors_aeb3c0));
        this.tv_line.setTextColor(getResources().getColor(R.color.colors_aeb3c0));
        this.tv_title.setLeftImgId(R.drawable.ic_back);
        this.tv_title.setRightLlGone(false);
        if (Build.VERSION.SDK_INT >= 19) {
            LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) this.tv_title.getLayoutParams();
            layoutParams4.height = ScreenUtil.dp2Px(this, 64.0f);
            this.tv_title.setTitleRlPadding(getResources().getDimensionPixelSize(R.dimen.dp_30));
            this.tv_title.setLayoutParams(layoutParams4);
        }
        this.tv_title.setBackground(R.color.color_transparent);
        RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams) this.ll_video_qt.getLayoutParams();
        LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) this.listenerBtn.getLayoutParams();
        LinearLayout.LayoutParams layoutParams7 = (LinearLayout.LayoutParams) this.speakBtn.getLayoutParams();
        LinearLayout.LayoutParams layoutParams8 = (LinearLayout.LayoutParams) this.recordBtn.getLayoutParams();
        LinearLayout.LayoutParams layoutParams9 = (LinearLayout.LayoutParams) this.captureBtn.getLayoutParams();
        layoutParams5.addRule(12);
        layoutParams5.leftMargin = ScreenUtil.dp2Px(getActivity(), 30.0f);
        layoutParams5.bottomMargin = ScreenUtil.dp2Px(getActivity(), 9.0f);
        layoutParams5.removeRule(10);
        layoutParams5.removeRule(11);
        layoutParams5.topMargin = 0;
        layoutParams5.rightMargin = 0;
        this.ll_video_qt.setLayoutParams(layoutParams5);
        if (this.ll_capture.getParent() != null) {
            ((ViewGroup) this.ll_capture.getParent()).removeView(this.ll_capture);
        }
        layoutParams9.width = ScreenUtil.dp2Px(getActivity(), 25.0f);
        layoutParams9.height = ScreenUtil.dp2Px(getActivity(), 25.0f);
        this.captureBtn.setLayoutParams(layoutParams9);
        this.captureBtn.setImageResource(R.drawable.camera_capture);
        this.captureBtn.setBackgroundDrawable(null);
        this.captureBtn.setPadding(0, 0, 0, 0);
        this.bottomView.addView(this.ll_capture, 0);
        if (this.ll_record.getParent() != null) {
            ((ViewGroup) this.ll_record.getParent()).removeView(this.ll_record);
        }
        layoutParams8.width = ScreenUtil.dp2Px(getActivity(), 25.0f);
        layoutParams8.height = ScreenUtil.dp2Px(getActivity(), 25.0f);
        this.recordBtn.setLayoutParams(layoutParams8);
        setRecordState();
        this.recordBtn.setBackgroundDrawable(null);
        this.recordBtn.setPadding(0, 0, 0, 0);
        this.bottomView.addView(this.ll_record, 1);
        if (this.speakBtn.getParent() != null) {
            ((ViewGroup) this.speakBtn.getParent()).removeView(this.speakBtn);
        }
        layoutParams7.width = ScreenUtil.dp2Px(getActivity(), 60.0f);
        layoutParams7.height = ScreenUtil.dp2Px(getActivity(), 60.0f);
        setSpeakerBtn(this.isSpeakerOpen);
        this.bottomView.addView(this.speakBtn, 2, layoutParams7);
        if (this.ll_listener.getParent() != null) {
            ((ViewGroup) this.ll_listener.getParent()).removeView(this.ll_listener);
        }
        layoutParams6.width = ScreenUtil.dp2Px(getActivity(), 25.0f);
        layoutParams6.height = ScreenUtil.dp2Px(getActivity(), 25.0f);
        this.listenerBtn.setLayoutParams(layoutParams6);
        setListen(this.speakerSwitch[this.currentFocusPlayerIndex]);
        this.listenerBtn.setBackgroundDrawable(null);
        this.listenerBtn.setPadding(0, 0, 0, 0);
        this.bottomView.addView(this.ll_listener, 3);
        this.bottomView.setVisibility(0);
        if (this.quality_dlg.getParent() != null) {
            ((ViewGroup) this.quality_dlg.getParent()).removeView(this.quality_dlg);
        }
        this.video_panel_rl.addView(this.quality_dlg);
        if (this.light_dlg.getParent() != null) {
            ((ViewGroup) this.light_dlg.getParent()).removeView(this.light_dlg);
        }
        this.video_panel_rl.addView(this.light_dlg);
        showSystemUI();
    }

    private void hideSystemUI() {
        this.decorView.setSystemUiVisibility(3846);
        getWindow().setFlags(1024, 1024);
    }

    private void showSystemUI() {
        this.decorView.setSystemUiVisibility(this.uiVisibility);
        getWindow().clearFlags(1024);
    }

    private void keepScreenLight() {
        getWindow().addFlags(128);
    }

    private void stopScreenLight() {
        getWindow().clearFlags(128);
    }

    private void shareDevice(String str, String str2, String str3) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str2);
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_ACCOUNT_ATTR, str);
        map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_MOBILE);
        map.put("iotIdList", arrayList);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE).setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass60(str3, str));
    }

    /* JADX INFO: renamed from: activity.IPCamera4Activity$60, reason: invalid class name */
    class AnonymousClass60 implements IoTCallback {
        final /* synthetic */ String val$accountAttr;
        final /* synthetic */ String val$devName;

        AnonymousClass60(String str, String str2) {
            this.val$devName = str;
            this.val$accountAttr = str2;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, IPCamera4Activity.this.TAG, "onFailure");
            Toast.makeText(IPCamera4Activity.this.mContext, R.string.share_failed, 0).show();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            final int code = ioTResponse.getCode();
            LogEx.d(true, IPCamera4Activity.this.TAG, "shareDevice onResponse: code: " + code);
            final String localizedMsg = ioTResponse.getLocalizedMsg();
            if (code != 200) {
                IPCamera4Activity.this.mHandler.post(new Runnable() { // from class: activity.IPCamera4Activity.60.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (IPCamera4Activity.this.isFinishing()) {
                            return;
                        }
                        if (code == 2077) {
                            IPCamera4Activity.this.mHandler.post(new Runnable() { // from class: activity.IPCamera4Activity.60.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Activity activity2 = IPCamera4Activity.this.getActivity();
                                    if (activity2 == null || activity2.isFinishing()) {
                                        return;
                                    }
                                    DialogUtil.showTipsConfirmDiaLog(IPCamera4Activity.this.getActivity(), IPCamera4Activity.this.getString(R.string.sharing_failed), IPCamera4Activity.this.getString(R.string.sharing_tips_1) + SdkConstant.CLOUDAPI_LF + IPCamera4Activity.this.getString(R.string.sharing_tips_2) + SdkConstant.CLOUDAPI_LF + IPCamera4Activity.this.getString(R.string.sharing_tips_3) + SdkConstant.CLOUDAPI_LF + IPCamera4Activity.this.getString(R.string.sharing_tips_4), IPCamera4Activity.this.getString(R.string.i_know));
                                }
                            });
                        } else {
                            Toast.makeText(IPCamera4Activity.this.getActivity(), localizedMsg, 0).show();
                        }
                    }
                });
            } else {
                IPCamera4Activity.this.mHandler.post(new Runnable() { // from class: activity.IPCamera4Activity.60.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (IPCamera4Activity.this.isFinishing()) {
                            return;
                        }
                        Toast.makeText(IPCamera4Activity.this.mContext, IPCamera4Activity.this.getString(R.string.share_succeed, new Object[]{AnonymousClass60.this.val$devName, AnonymousClass60.this.val$accountAttr}), 0).show();
                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cameraRemove(CameraRemove cameraRemove) {
        finish();
    }
}
