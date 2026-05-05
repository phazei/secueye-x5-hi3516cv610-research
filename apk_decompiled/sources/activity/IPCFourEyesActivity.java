package activity;

import adapter.GridSpaceItemDecoration;
import adapter.HomeTopicPagerAdapter;
import adapter.IpcWiFiAdapter;
import adapter.LiveAdapter;
import adapter.LiveFourAdapter;
import adapter.LiveHorizontalAdapter;
import adapter.TopicAdapterDoubleEye;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import anet.channel.strategy.dispatch.DispatchConstants;
import bean.AreaCodeModel;
import bean.CameraRemove;
import bean.CameraSnapUpdate;
import bean.DeviceInfoBean;
import bean.RefreshPicture;
import bean.TopicBean;
import bean.WifiBean;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
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
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityIpcameraFourEyesBinding;
import com.smarx.notchlib.NotchScreenManager;
import config.AppConfig;
import config.Constants;
import dialog.BaseDialog;
import dialog.DialogUtil;
import dialog.InputDialogViewIpc;
import dialog.ShareDialog;
import enums.ActionTypeEnum;
import enums.SpeedEnum;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.DummyPagerTitleView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.android.agoo.message.MessageService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import sdk.IPCManager;
import tools.DensityUtil;
import tools.FileUtil;
import tools.LogEx;
import tools.MapUtils;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.ScreenUtil;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import tools.SpUtil;
import tools.StatusBarUtil;
import tools.SystemUtil;
import tools.TimeUtil;
import tools.Utils;
import view.JoystickTouchViewListener;
import view.MyGlTextureView;
import view.OnViewPagerListener;
import view.PagerLayoutManager;
import view.SelectorDialogFragment;
import view.ShadowButton;
import view.TouchView;
import view.WhiteProgressDialog;
import view.ZoomableTextureView;

/* JADX INFO: loaded from: classes.dex */
public class IPCFourEyesActivity extends CommonActivity {
    private static String[] PERMISSIONS_STORAGE = {Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
    private static final int RECORD_UPDATE_TIME = 102;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private int WifiConfigIsExist;
    private boolean ZoomIsMax;
    private int ZoomMax;
    String address;
    private DeviceInfoBean ballDevice;
    private ActivityIpcameraFourEyesBinding binding;
    private CountDownTimer countDownTimer;

    /* JADX INFO: renamed from: dialog, reason: collision with root package name */
    private AlertDialog f1575dialog;
    private int faceDetectionAbility;
    private DeviceInfoBean gunDevice1;
    private DeviceInfoBean gunDevice2;
    private DeviceInfoBean gunDevice3;
    private int h;
    private Timer inactivityTimer;
    private String[] infrarredMode;
    private InputDialogViewIpc inputDialogView;
    private boolean isHorizontal;
    private boolean isOtherCard;
    private ActionTypeEnum lastActionTypeEnum;
    private long lastCtrlTime;
    String lat;
    private PagerLayoutManager linearLayoutManager;
    LiveAdapter liveAdapter;
    LiveFourAdapter liveFourAdapter;
    LiveHorizontalAdapter liveHorizontalAdapter;
    private LiveIntercomV2 liveIntercom;
    String lon;
    private IpcWiFiAdapter mAdapter;
    private SelectorDialogFragment mapFragment;
    private SelectorDialogFragment nightModeFragment;
    private DeviceInfoBean nvrDevice;
    private Timer onTouchTimer;
    private LivePlayer playBall;
    private LivePlayer playGun1;
    private LivePlayer playGun2;
    private LivePlayer playGun3;
    private Timer ptzTimer;
    private ShareDialog shareDialog2;
    private float startX;
    private float startY;
    private int supportMotionDetect;
    private Timer timer;
    private TouchView touchView;
    private double viewHeight;
    private int w;
    private WhiteProgressDialog whiteProgressDialog;
    private boolean isRatio = false;
    private long lastOnclickTime = 0;
    private int showMode = -1;
    private int rowNum = 2;
    private int columnNum = 3;
    private int lowPowerMode = -1;
    private int is1100ErrorPre = 10;
    private int countWakeUp = 0;
    private List<String> showList = new ArrayList();
    private ArrayList<TopicBean> mTopicData = new ArrayList<>();
    private ArrayList<RecyclerView> mList = new ArrayList<>();
    private ArrayList<MyGlTextureView> liveList = new ArrayList<>();
    MutableLiveData<Float> zoom = new MutableLiveData<>();
    private List<String> definitionList = new ArrayList();
    private List<WifiBean> wifiBeanList = new ArrayList();
    private boolean isRecording = false;
    private boolean needWakeUp = false;
    private boolean needRecharge = false;
    private boolean isDetecting = false;
    private boolean needTFInit = true;
    private List<String> nightModelList = new ArrayList();
    private String selectIotId = "";
    private String selectSsid = "";
    private boolean havePermission = false;
    int moveMode = 0;
    private Handler handler = new Handler(new Handler.Callback() { // from class: activity.IPCFourEyesActivity.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(@NonNull Message message) {
            if (message.what != 102) {
                return false;
            }
            IPCFourEyesActivity.this.binding.playerInfoTv.setText(((IPCFourEyesActivity.this.playBall.getCurrentPlayInfo().bitRate / 1024) / 8) + "KB/S");
            if (IPCFourEyesActivity.this.binding.playerInfoTv.getVisibility() != 0) {
                return false;
            }
            IPCFourEyesActivity.this.updateInfoTv();
            return false;
        }
    });
    private Handler wakeUpHandler = new AnonymousClass78();
    ViewTreeObserver.OnGlobalLayoutListener nGlobalLayoutListener = new AnonymousClass98();
    int i = 0;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ipcamera_four_eyes;
    }

    static /* synthetic */ int access$3408(IPCFourEyesActivity iPCFourEyesActivity) {
        int i = iPCFourEyesActivity.showMode;
        iPCFourEyesActivity.showMode = i + 1;
        return i;
    }

    static /* synthetic */ int access$7108(IPCFourEyesActivity iPCFourEyesActivity) {
        int i = iPCFourEyesActivity.countWakeUp;
        iPCFourEyesActivity.countWakeUp = i + 1;
        return i;
    }

    static /* synthetic */ int access$7410(IPCFourEyesActivity iPCFourEyesActivity) {
        int i = iPCFourEyesActivity.is1100ErrorPre;
        iPCFourEyesActivity.is1100ErrorPre = i - 1;
        return i;
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                startActivity(new Intent("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION"));
                return;
            } else {
                this.havePermission = true;
                Log.i("swyLog", "Android 11以上，当前已有权限");
                return;
            }
        }
        if (Build.VERSION.SDK_INT > 23) {
            if (ActivityCompat.checkSelfPermission(this, Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
                return;
            } else {
                this.havePermission = true;
                Log.i("swyLog", "Android 6.0以上，11以下，当前已有权限");
                return;
            }
        }
        this.havePermission = true;
        Log.i("swyLog", "Android 6.0以下，已获取权限");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cameraRemove(CameraRemove cameraRemove) {
        finish();
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityIpcameraFourEyesBinding) DataBindingUtil.setContentView(this, R.layout.activity_ipcamera_four_eyes);
        setEdgeToEdge(this.binding.maxLayout);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        NotchScreenManager.getInstance().setDisplayInNotch(this);
        StatusBarUtil.setTranslucentStatus(getActivity());
        StatusBarUtil.setLightStatusBar(getActivity(), false);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.ballDevice = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.gunDevice1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.gunDevice2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.gunDevice3 = (DeviceInfoBean) extras.getSerializable("device3");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            SettingsCtrl.getInstance().getProperties(this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCFourEyesActivity.2
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            SettingsCtrl.getInstance().getProperties(this.gunDevice1.getIotId(), new MyCallback() { // from class: activity.IPCFourEyesActivity.3
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            SettingsCtrl.getInstance().getProperties(this.gunDevice2.getIotId(), new MyCallback() { // from class: activity.IPCFourEyesActivity.4
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            SettingsCtrl.getInstance().getProperties(this.gunDevice3.getIotId(), new MyCallback() { // from class: activity.IPCFourEyesActivity.5
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            SettingsCtrl.getInstance().getProperties(this.nvrDevice.getIotId(), new MyCallback() { // from class: activity.IPCFourEyesActivity.6
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
        }
        initView();
        initImg();
        initPlayerBall();
        initPlayerGun1();
        initPlayerGun2();
        initPlayerGun3();
        playLive();
        initLiveIntercom();
        initListener();
        if (getIntent().getIntExtra("strongRemind", 0) == 1) {
            startLiveIntercom();
            this.binding.llListener.setSelected(true);
            this.playBall.setVolume(this.binding.llListener.isSelected() ? 1.0f : 0.0f);
        }
        resetInactivityTimer();
    }

    private void initImg() {
        if (SpUtil.hasPrefix(this, Utils.getDevSnapKey(OpenAccountUIConstants.UNDER_LINE + this.ballDevice.getIotId()) + OpenAccountUIConstants.UNDER_LINE).size() <= 0) {
            Glide.with((FragmentActivity) this).load(new StringBuilder(SpUtil.getString(this, Utils.getDevSnapKey(this.ballDevice.getIotId()), "")).toString()).into(this.binding.iv1);
        }
        if (SpUtil.hasPrefix(this, Utils.getDevSnapKey(OpenAccountUIConstants.UNDER_LINE + this.gunDevice2.getIotId()) + OpenAccountUIConstants.UNDER_LINE).size() <= 0) {
            Glide.with((FragmentActivity) this).load(new StringBuilder(SpUtil.getString(this, Utils.getDevSnapKey(this.gunDevice2.getIotId()), "")).toString()).into(this.binding.iv2);
        }
        if (SpUtil.hasPrefix(this, Utils.getDevSnapKey(OpenAccountUIConstants.UNDER_LINE + this.gunDevice1.getIotId()) + OpenAccountUIConstants.UNDER_LINE).size() <= 0) {
            Glide.with((FragmentActivity) this).load(new StringBuilder(SpUtil.getString(this, Utils.getDevSnapKey(this.gunDevice1.getIotId()), "")).toString()).into(this.binding.iv3);
        }
        if (SpUtil.hasPrefix(this, Utils.getDevSnapKey(OpenAccountUIConstants.UNDER_LINE + this.gunDevice3.getIotId()) + OpenAccountUIConstants.UNDER_LINE).size() <= 0) {
            Glide.with((FragmentActivity) this).load(new StringBuilder(SpUtil.getString(this, Utils.getDevSnapKey(this.gunDevice3.getIotId()), "")).toString()).into(this.binding.iv4);
        }
        this.binding.btTop.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.scrollview.post(new Runnable() { // from class: activity.IPCFourEyesActivity.7.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCFourEyesActivity.this.binding.scrollview.fullScroll(33);
                        if (IPCFourEyesActivity.this.binding.rvLive.getLayoutManager() != null) {
                            IPCFourEyesActivity.this.binding.rvLive.smoothScrollToPosition(0);
                        }
                    }
                });
            }
        });
        this.binding.btBottom.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.scrollview.post(new Runnable() { // from class: activity.IPCFourEyesActivity.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCFourEyesActivity.this.binding.scrollview.fullScroll(130);
                        if (IPCFourEyesActivity.this.binding.rvLive.getLayoutManager() != null) {
                            IPCFourEyesActivity.this.binding.rvLive.smoothScrollToPosition(3);
                        }
                    }
                });
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        TouchView touchView = this.touchView;
        if (touchView != null) {
            touchView.resetView();
        }
        stopInactivityTimer();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        stopScreenLight();
        AutoSnap();
        this.playBall.stop();
        this.playGun1.stop();
        this.playGun2.stop();
        this.playGun3.stop();
        Timer timer = this.ptzTimer;
        if (timer != null) {
            timer.cancel();
            this.ptzTimer = null;
        }
        stopInactivityTimer();
        LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
        if (liveIntercomV2 != null) {
            liveIntercomV2.release();
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.binding.ipcOfflineText.setVisibility(8);
        this.binding.qualityBtn.setText(this.definitionList.get(SharePreferenceManager.getInstance().getStreamVideoQuality(this.ballDevice.getIotId())));
        SettingsCtrl.getInstance().getProperties(this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCFourEyesActivity.9
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                if (SharePreferenceManager.getInstance().getMixZoom(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.9.1
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCFourEyesActivity.this.binding.tvZoom.setVisibility(8);
                        }
                    });
                }
                if (SharePreferenceManager.getInstance().getSupportZoom(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.9.2
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCFourEyesActivity.this.binding.tvZoom.setVisibility(8);
                        }
                    });
                } else {
                    IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.9.3
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCFourEyesActivity.this.binding.layoutAf.setVisibility(8);
                        }
                    });
                }
                if (SharePreferenceManager.getInstance().getSensorViewDisplay(IPCFourEyesActivity.this.ballDevice.getIotId()) == 0) {
                    IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.9.4
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCFourEyesActivity.this.binding.SensorView.setVisibility(8);
                        }
                    });
                } else {
                    IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.9.5
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCFourEyesActivity.this.binding.SensorView.setVisibility(0);
                        }
                    });
                }
                if (!AppConfig.isChina) {
                    IPCFourEyesActivity.this.binding.ivCharge4gFlow.setImageResource(R.drawable.selector_server_en);
                }
                IPCFourEyesActivity.this.binding.ivCharge4gFlow.setSelected(SharePreferenceManager.getInstance().getSupport4G(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1);
                Log.e("云存=", "" + SharePreferenceManager.getInstance().getEventRecord(IPCFourEyesActivity.this.ballDevice.getIotId()) + "  4G=" + SharePreferenceManager.getInstance().getSupport4G(IPCFourEyesActivity.this.ballDevice.getIotId()));
                if (SharePreferenceManager.getInstance().getEventRecord(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1 || SharePreferenceManager.getInstance().getSupport4G(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCFourEyesActivity.this.binding.ivCharge4gFlow.setVisibility(0);
                } else {
                    IPCFourEyesActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                }
                if (!IPCFourEyesActivity.this.binding.ivCharge4gFlow.isSelected()) {
                    IPCFourEyesActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                }
                IPCFourEyesActivity.this.faceDetectionAbility = SharePreferenceManager.getInstance().getHumanoidTracking(IPCFourEyesActivity.this.ballDevice.getIotId()).intValue();
                IPCFourEyesActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(IPCFourEyesActivity.this.ballDevice.getIotId());
                if (IPCFourEyesActivity.this.faceDetectionAbility == 1) {
                    IPCFourEyesActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(IPCFourEyesActivity.this.ballDevice.getIotId()).intValue() == 1;
                } else {
                    IPCFourEyesActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1;
                }
                IPCFourEyesActivity.this.showMore();
                IPCFourEyesActivity.this.initMore();
            }
        });
        if (SharePreferenceManager.getInstance().getLowPower(this.ballDevice.getIotId()) == 1) {
            this.wakeUpHandler.removeCallbacksAndMessages(null);
            wakeUpDevice();
            wakeUpDeviceHandel();
            playLive();
        } else {
            playLive();
        }
        getThingsStatus(this.ballDevice.getIotId());
        DeviceInfoBean deviceInfoBean = this.ballDevice;
        if (deviceInfoBean != null && deviceInfoBean.getOwned() == 1 && SharePreferenceManager.getInstance().getDoubleNetWork(this.ballDevice.getIotId()) == 1) {
            this.WifiConfigIsExist = SharePreferenceManager.getInstance().getWifiConfigIsExist(this.ballDevice.getIotId());
            if (SharePreferenceManager.getInstance().getFirstNet(this.ballDevice.getIotId())) {
                SharePreferenceManager.getInstance().setFirstNet(this.ballDevice.getIotId(), true);
                if (SharePreferenceManager.getInstance().getWifiConfigIsExist(this.ballDevice.getIotId()) != 1) {
                    getWiFiList();
                }
            }
        }
        if (SharePreferenceManager.getInstance().getFirstFormatInIpc(this.ballDevice.getIotId()) && SharePreferenceManager.getInstance().getStorageStatus(this.ballDevice.getIotId()) == 2 && this.needTFInit) {
            showFormatDialog(SharePreferenceManager.getInstance().getStorageTotalCapacity(this.ballDevice.getIotId()) / 1024.0f, SharePreferenceManager.getInstance().getStorageRemainingCapacity(this.ballDevice.getIotId()) / 1024.0f, SharePreferenceManager.getInstance().getStorageStatus(this.ballDevice.getIotId()));
        }
        resetInactivityTimer();
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 1 || SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == 0) {
            this.binding.ivNightBottom.setVisibility(8);
            this.binding.fullNightVision.setVisibility(8);
        }
    }

    @Override // android.app.Activity
    public void onUserInteraction() {
        resetInactivityTimer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetInactivityTimer() {
        if (SharePreferenceManager.getInstance().getNetState(this.ballDevice.getIotId()) == 0) {
            if (SharePreferenceManager.getInstance().getSupport4G(this.ballDevice.getIotId()) == 1) {
                stopInactivityTimer();
                startInactivityTimer();
                return;
            }
            return;
        }
        if (SharePreferenceManager.getInstance().getNetState(this.ballDevice.getIotId()) == 3) {
            stopInactivityTimer();
            startInactivityTimer();
        }
    }

    private void startInactivityTimer() {
        if (this.inactivityTimer == null) {
            this.inactivityTimer = new Timer();
            this.inactivityTimer.schedule(new InactivityTimerTask(), AppConfig.INACTIVITY_DELAY);
            Log.e("防沉迷", "开始");
        }
    }

    private void stopInactivityTimer() {
        Timer timer = this.inactivityTimer;
        if (timer != null) {
            timer.cancel();
            this.inactivityTimer.purge();
            this.inactivityTimer = null;
            Log.e("防沉迷", "停止");
        }
    }

    private class InactivityTimerTask extends TimerTask {
        private InactivityTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.InactivityTimerTask.1
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCFourEyesActivity.this.playBall.getPlayState() == 3 || IPCFourEyesActivity.this.playGun1.getPlayState() == 3 || IPCFourEyesActivity.this.playGun2.getPlayState() == 3) {
                        if (IPCFourEyesActivity.this.playBall != null) {
                            IPCFourEyesActivity.this.playBall.stop();
                        }
                        if (IPCFourEyesActivity.this.playGun1 != null) {
                            IPCFourEyesActivity.this.playGun1.stop();
                        }
                        if (IPCFourEyesActivity.this.playGun2 != null) {
                            IPCFourEyesActivity.this.playGun2.stop();
                        }
                        Log.e("防沉迷", "在播放");
                        IPCFourEyesActivity.this.showPlayButton();
                        DialogUtil.showTipsConfirmDiaLog(IPCFourEyesActivity.this.getActivity(), IPCFourEyesActivity.this.getString(R.string.warm_tips), IPCFourEyesActivity.this.getString(R.string.warm_tips_1), IPCFourEyesActivity.this.getString(R.string.i_know), new DialogUtil.OnConfirmClickListener() { // from class: activity.IPCFourEyesActivity.InactivityTimerTask.1.1
                            @Override // dialog.DialogUtil.OnConfirmClickListener
                            public void ConfirmListener() {
                                IPCFourEyesActivity.this.resetInactivityTimer();
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initMore() {
        int i = this.rowNum * this.columnNum;
        final int size = this.mTopicData.size() / i;
        if (this.mTopicData.size() % i > 0) {
            size++;
        }
        this.mList = new ArrayList<>();
        int i2 = 0;
        while (i2 < size) {
            RecyclerView recyclerView = new RecyclerView(this);
            recyclerView.setLayoutManager(new GridLayoutManager(this, this.columnNum));
            recyclerView.addItemDecoration(new GridSpaceItemDecoration(this.columnNum, 1, 1));
            int i3 = i2 * i;
            i2++;
            int size2 = i2 * i;
            if (size2 > this.mTopicData.size()) {
                size2 = this.mTopicData.size();
            }
            TopicAdapterDoubleEye topicAdapterDoubleEye = new TopicAdapterDoubleEye(this, new ArrayList(this.mTopicData.subList(i3, size2)));
            topicAdapterDoubleEye.setOnItemClickListener(new AnonymousClass10());
            recyclerView.setAdapter(topicAdapterDoubleEye);
            this.mList.add(recyclerView);
        }
        this.binding.topicViewPager.setAdapter(new HomeTopicPagerAdapter(this.mList));
        this.binding.topicViewPager.setOffscreenPageLimit(size - 1);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() { // from class: activity.IPCFourEyesActivity.11
            @Override // net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
            public int getCount() {
                return size;
            }

            @Override // net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
            public IPagerTitleView getTitleView(Context context, int i4) {
                return new DummyPagerTitleView(context);
            }

            @Override // net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(2);
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 3.0d));
                linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 66 / size));
                linePagerIndicator.setRoundRadius(UIUtil.dip2px(context, 3.0d));
                linePagerIndicator.setStartInterpolator(new AccelerateInterpolator());
                linePagerIndicator.setEndInterpolator(new DecelerateInterpolator(3.0f));
                linePagerIndicator.setColors(Integer.valueOf(ContextCompat.getColor(context, R.color.colorAccent)));
                return linePagerIndicator;
            }
        });
        this.binding.topicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(this.binding.topicIndicator, this.binding.topicViewPager);
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$10, reason: invalid class name */
    class AnonymousClass10 implements TopicAdapterDoubleEye.OnItemClickListener {
        AnonymousClass10() {
        }

        @Override // adapter.TopicAdapterDoubleEye.OnItemClickListener
        public void onTopicItemClick(TopicBean topicBean, int i) {
            if (topicBean.getIcon() == R.drawable.icon_card_back_false || topicBean.getIcon() == R.drawable.video_back) {
                Intent intent = new Intent(IPCFourEyesActivity.this, (Class<?>) RecordVideoActivity.class);
                intent.putExtra("title", IPCFourEyesActivity.this.ballDevice.getName());
                intent.putExtra("iotId", IPCFourEyesActivity.this.ballDevice.getIotId());
                intent.putExtra("iotId2", IPCFourEyesActivity.this.gunDevice1.getIotId());
                intent.putExtra("iotId3", IPCFourEyesActivity.this.gunDevice2.getIotId());
                intent.putExtra("iotId4", IPCFourEyesActivity.this.gunDevice3.getIotId());
                IPCFourEyesActivity.this.startActivity(intent);
                return;
            }
            if (topicBean.getIcon() == R.drawable.icon_cloud_back_false) {
                Intent intent2 = new Intent(IPCFourEyesActivity.this.getActivity(), (Class<?>) CloudStorageActivity.class);
                intent2.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCFourEyesActivity.this.ballDevice);
                intent2.putExtra("device1", IPCFourEyesActivity.this.gunDevice1);
                intent2.putExtra("device2", IPCFourEyesActivity.this.gunDevice2);
                intent2.putExtra("device3", IPCFourEyesActivity.this.gunDevice3);
                intent2.putExtra("nvrDevice", IPCFourEyesActivity.this.nvrDevice);
                IPCFourEyesActivity.this.startActivity(intent2);
                return;
            }
            if (topicBean.getIcon() == R.drawable.share_ipc) {
                IPCFourEyesActivity.this.shareDialog2 = ((ShareDialog.Builder) new ShareDialog.Builder().view(R.layout.dialog_input2).leftBtnText(IPCFourEyesActivity.this.getString(R.string.cancel)).rightBtnText(IPCFourEyesActivity.this.getString(R.string.share_immediately)).clickRight(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.10.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        if (IPCFourEyesActivity.this.shareDialog2.getContent() != null) {
                            if (IPCFourEyesActivity.this.shareDialog2.getMode() != 0 || SystemUtil.isPhone(IPCFourEyesActivity.this.shareDialog2.getContent())) {
                                if (IPCFourEyesActivity.this.shareDialog2.getMode() == 1 && !SystemUtil.isEmail(IPCFourEyesActivity.this.shareDialog2.getContent())) {
                                    ToastUtils.toast(IPCFourEyesActivity.this.getActivity(), IPCFourEyesActivity.this.getString(R.string.email_invalid));
                                    return;
                                }
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(IPCFourEyesActivity.this.nvrDevice.getIotId());
                                arrayList.add(IPCFourEyesActivity.this.ballDevice.getIotId());
                                arrayList.add(IPCFourEyesActivity.this.gunDevice1.getIotId());
                                arrayList.add(IPCFourEyesActivity.this.gunDevice2.getIotId());
                                arrayList.add(IPCFourEyesActivity.this.gunDevice3.getIotId());
                                IPCFourEyesActivity.this.shareDevice(IPCFourEyesActivity.this.shareDialog2.getContent(), arrayList, IPCFourEyesActivity.this.shareDialog2.getMode() == 0 ? IPCFourEyesActivity.this.shareDialog2.getDistinct() : null);
                                return;
                            }
                            ToastUtils.toast(IPCFourEyesActivity.this.getActivity(), IPCFourEyesActivity.this.getString(R.string.phone_invalid));
                            return;
                        }
                        Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.share_user_null_error, 0).show();
                    }
                })).create();
                IPCFourEyesActivity.this.shareDialog2.setOnShareClick(new ShareDialog.OnShareClickListener() { // from class: activity.IPCFourEyesActivity.10.2
                    @Override // dialog.ShareDialog.OnShareClickListener
                    public void onDistinctChange() {
                    }

                    @Override // dialog.ShareDialog.OnShareClickListener
                    public void onDistinctSelect(AreaCodeModel areaCodeModel) {
                    }

                    @Override // dialog.ShareDialog.OnShareClickListener
                    public void onShareSwitchChange() {
                    }
                });
                IPCFourEyesActivity.this.shareDialog2.setExtra(IPCFourEyesActivity.this.ballDevice);
                IPCFourEyesActivity.this.shareDialog2.show(IPCFourEyesActivity.this.getSupportFragmentManager(), "");
                return;
            }
            if (topicBean.getTitle().equals(IPCFourEyesActivity.this.getResources().getString(R.string.zoom))) {
                IPCFourEyesActivity.this.binding.rlTouchView.setVisibility(8);
                IPCFourEyesActivity.this.binding.layoutMore.setVisibility(8);
                IPCFourEyesActivity.this.binding.layoutZoom.setVisibility(0);
                return;
            }
            if (topicBean.getTitle().equals(IPCFourEyesActivity.this.getResources().getString(R.string.track)) || topicBean.getTitle().equals(IPCFourEyesActivity.this.getResources().getString(R.string.mobile_tracking))) {
                if (SharePreferenceManager.getInstance().getFaceDetectMode(IPCFourEyesActivity.this.ballDevice.getIotId()) == 0) {
                    HashMap map = new HashMap();
                    map.put(Constants.FACE_DETECT_SENSITIVITY, 2);
                    IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.10.3
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, Object obj) {
                            if (!z || obj == null || "".equals(String.valueOf(obj))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.3.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                        }
                                    });
                                } else {
                                    SharePreferenceManager.getInstance().setFaceDetectMode(IPCFourEyesActivity.this.ballDevice.getIotId(), 2);
                                }
                            }
                        }
                    });
                }
                if (IPCFourEyesActivity.this.faceDetectionAbility == 1) {
                    IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.4
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCFourEyesActivity.this.showProgressDialog();
                        }
                    });
                    HashMap map2 = new HashMap();
                    if (SharePreferenceManager.getInstance().getTlrClRgn(IPCFourEyesActivity.this.ballDevice.getIotId()).intValue() == 1) {
                        if (!IPCFourEyesActivity.this.isDetecting) {
                            i = SharePreferenceManager.getInstance().getAreaDetectEnable(IPCFourEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? SharePreferenceManager.getInstance().getCrossLineEnable(IPCFourEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? 7 : 5 : SharePreferenceManager.getInstance().getCrossLineEnable(IPCFourEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? 3 : 1;
                        } else if (SharePreferenceManager.getInstance().getAreaDetectEnable(IPCFourEyesActivity.this.ballDevice.getIotId()).intValue() != 0) {
                            i = SharePreferenceManager.getInstance().getCrossLineEnable(IPCFourEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? 6 : 4;
                        } else if (SharePreferenceManager.getInstance().getCrossLineEnable(IPCFourEyesActivity.this.ballDevice.getIotId()).intValue() != 0) {
                            i = 2;
                        }
                    } else if (IPCFourEyesActivity.this.isDetecting) {
                        i = 1;
                    }
                    map2.put(Constants.IvpExSwitch, Integer.valueOf(i));
                    IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).setProperties(map2, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.10.5
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            IPCFourEyesActivity.this.dismissProgressDialog();
                            if (!z) {
                                IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.5.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                    }
                                });
                                return;
                            }
                            if (obj == null || "".equals(String.valueOf(obj))) {
                                return;
                            }
                            try {
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.containsKey("code")) {
                                    if (object.getInteger("code").intValue() != 200) {
                                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.5.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                            }
                                        });
                                        return;
                                    }
                                    IPCFourEyesActivity.this.isDetecting = !IPCFourEyesActivity.this.isDetecting;
                                    if (IPCFourEyesActivity.this.faceDetectionAbility == 1) {
                                        if (IPCFourEyesActivity.this.isDetecting) {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCFourEyesActivity.this.ballDevice.getIotId(), 1);
                                        } else {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCFourEyesActivity.this.ballDevice.getIotId(), 0);
                                        }
                                    } else if (IPCFourEyesActivity.this.isDetecting) {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCFourEyesActivity.this.ballDevice.getIotId(), 1);
                                    } else {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCFourEyesActivity.this.ballDevice.getIotId(), 0);
                                    }
                                    SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCFourEyesActivity.this.selectIotId, i & 1);
                                    SharePreferenceManager.getInstance().setAreaDetectEnable(IPCFourEyesActivity.this.selectIotId, (i & 4) >> 2);
                                    SharePreferenceManager.getInstance().setCrossLineEnable(IPCFourEyesActivity.this.selectIotId, (i & 2) >> 1);
                                    IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.5.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            IPCFourEyesActivity.this.showMore();
                                            IPCFourEyesActivity.this.initMore();
                                            Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.6
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCFourEyesActivity.this.showProgressDialog();
                        }
                    });
                    HashMap map3 = new HashMap();
                    map3.put(Constants.INTELLIGENT_TRACKING, Integer.valueOf(1 ^ (IPCFourEyesActivity.this.isDetecting ? 1 : 0)));
                    IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).setProperties(map3, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.10.7
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            IPCFourEyesActivity.this.dismissProgressDialog();
                            if (!z) {
                                IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.7.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                    }
                                });
                                return;
                            }
                            if (obj == null || "".equals(String.valueOf(obj))) {
                                return;
                            }
                            try {
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.containsKey("code")) {
                                    if (object.getInteger("code").intValue() != 200) {
                                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.7.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                            }
                                        });
                                        return;
                                    }
                                    IPCFourEyesActivity.this.isDetecting = !IPCFourEyesActivity.this.isDetecting;
                                    if (IPCFourEyesActivity.this.faceDetectionAbility == 1) {
                                        if (IPCFourEyesActivity.this.isDetecting) {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCFourEyesActivity.this.ballDevice.getIotId(), 1);
                                        } else {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCFourEyesActivity.this.ballDevice.getIotId(), 0);
                                        }
                                    } else if (IPCFourEyesActivity.this.isDetecting) {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCFourEyesActivity.this.ballDevice.getIotId(), 1);
                                    } else {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCFourEyesActivity.this.ballDevice.getIotId(), 0);
                                    }
                                    IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.7.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            IPCFourEyesActivity.this.showMore();
                                            IPCFourEyesActivity.this.initMore();
                                            Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                if (topicBean.isSelect()) {
                    IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).changePresetLocation(103, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.10.8
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                                Log.e("预置位", "103");
                            }
                        }
                    });
                    return;
                } else {
                    IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).addPresetLocation(99, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.10.9
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                                Log.e("预置位", "99");
                            }
                        }
                    });
                    IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).changePresetLocation(100, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.10.10
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                                Log.e("预置位", MessageService.MSG_DB_COMPLETE);
                            }
                        }
                    });
                    return;
                }
            }
            if (topicBean.getTitle().equals(IPCFourEyesActivity.this.getResources().getString(R.string.floodlight))) {
                HashMap map4 = new HashMap();
                map4.put(Constants.FloodlightSwitch, Integer.valueOf(SharePreferenceManager.getInstance().getFloodlightSwitch(IPCFourEyesActivity.this.ballDevice.getIotId()) != 1 ? 1 : 0));
                IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).setProperties(map4, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.10.11
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.11.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCFourEyesActivity.this.showMore();
                                    IPCFourEyesActivity.this.initMore();
                                    if (SharePreferenceManager.getInstance().getFloodlightSwitch(IPCFourEyesActivity.this.ballDevice.getIotId()) == 0) {
                                        SharePreferenceManager.getInstance().setFloodlightSwitch(IPCFourEyesActivity.this.ballDevice.getIotId(), 1);
                                    } else {
                                        SharePreferenceManager.getInstance().setFloodlightSwitch(IPCFourEyesActivity.this.ballDevice.getIotId(), 0);
                                    }
                                    Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                });
            } else if (topicBean.getTitle().equals(IPCFourEyesActivity.this.getResources().getString(R.string.locate))) {
                IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).getLocationBasedService(new AnonymousClass12());
            } else if (topicBean.getTitle().equals(IPCFourEyesActivity.this.getResources().getString(R.string.map_gps))) {
                IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).getGPSPositioningService(new IPanelCallback() { // from class: activity.IPCFourEyesActivity.10.13
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable final Object obj) {
                        if (z) {
                            if (obj != null && !"".equals(String.valueOf(obj))) {
                                IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.13.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Log.e("基站定位信息", String.valueOf(obj));
                                        JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                        if (object.getInteger("code").intValue() != 200) {
                                            IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
                                            return;
                                        }
                                        if (!String.valueOf(obj).contains("Latitude")) {
                                            IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
                                            return;
                                        }
                                        JSONObject jSONObject = object.getJSONObject("data");
                                        String string = jSONObject.getString("Latitude");
                                        String string2 = jSONObject.getString("Longitude");
                                        MapUtils.dddmmToDecimal(Double.parseDouble(string));
                                        IPCFourEyesActivity.this.lat = MapUtils.dddmmToDecimal(Double.parseDouble(string)) + "";
                                        IPCFourEyesActivity.this.lon = MapUtils.dddmmToDecimal(Double.parseDouble(string2)) + "";
                                        IPCFourEyesActivity.this.mapFragment.showAllowingStateLoss(IPCFourEyesActivity.this.getSupportFragmentManager(), "");
                                    }
                                });
                                return;
                            } else {
                                IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
                                return;
                            }
                        }
                        IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
                    }
                });
            }
        }

        /* JADX INFO: renamed from: activity.IPCFourEyesActivity$10$12, reason: invalid class name */
        class AnonymousClass12 implements IPanelCallback {
            AnonymousClass12() {
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable final Object obj) {
                if (z) {
                    if (obj != null && !"".equals(String.valueOf(obj))) {
                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.10.12.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Log.e("基站定位信息", String.valueOf(obj));
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.getInteger("code").intValue() != 200) {
                                    IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
                                    return;
                                }
                                if (!String.valueOf(obj).contains("CellIdentity")) {
                                    IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
                                    return;
                                }
                                JSONObject jSONObject = object.getJSONObject("data");
                                String string = jSONObject.getString("CellIdentity");
                                jSONObject.getInteger("MobileNetworkCode").intValue();
                                String string2 = jSONObject.getString("TrackingAreaCode");
                                int i = Integer.parseInt(string, 16);
                                int i2 = Integer.parseInt(string2, 16);
                                new OkHttpClient().newCall(new Request.Builder().url("http://api.cellocation.com:84/cell/?mcc=460&mnc=1&lac=" + i2 + "&ci=" + i + "&output=json").get().build()).enqueue(new Callback() { // from class: activity.IPCFourEyesActivity.10.12.1.1
                                    static final /* synthetic */ boolean $assertionsDisabled = false;

                                    @Override // okhttp3.Callback
                                    public void onFailure(Call call, IOException iOException) {
                                        IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
                                    }

                                    @Override // okhttp3.Callback
                                    public void onResponse(Call call, Response response) throws IOException {
                                        try {
                                            JSONObject object2 = JSONObject.parseObject(response.body().string());
                                            if (object2.getInteger("errcode").intValue() != 0) {
                                                IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
                                                return;
                                            }
                                            IPCFourEyesActivity.this.lat = object2.getString(DispatchConstants.LATITUDE);
                                            IPCFourEyesActivity.this.lon = object2.getString("lon");
                                            object2.getString("radius");
                                            IPCFourEyesActivity.this.address = object2.getString("address");
                                            IPCFourEyesActivity.this.mapFragment.showAllowingStateLoss(IPCFourEyesActivity.this.getSupportFragmentManager(), "");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                        return;
                    } else {
                        IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
                        return;
                    }
                }
                IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.play_failed_retry));
            }
        }
    }

    private void initView() {
        this.whiteProgressDialog = new WhiteProgressDialog(this);
        this.binding.tvTitle.setText(this.ballDevice.getName());
        this.binding.maxLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: activity.IPCFourEyesActivity.12
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                IPCFourEyesActivity.this.binding.maxLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                iPCFourEyesActivity.w = iPCFourEyesActivity.binding.maxLayout.getWidth();
                IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                iPCFourEyesActivity2.h = iPCFourEyesActivity2.binding.maxLayout.getHeight();
                double dDoubleValue = new BigDecimal(IPCFourEyesActivity.this.w / IPCFourEyesActivity.this.h).setScale(2, 4).doubleValue();
                Log.e("屏幕", "宽高比例" + dDoubleValue);
                if (dDoubleValue >= 0.49d) {
                    IPCFourEyesActivity.this.isRatio = true;
                    IPCFourEyesActivity.this.viewHeight = r0.w / 2;
                } else {
                    IPCFourEyesActivity.this.isRatio = false;
                    IPCFourEyesActivity.this.viewHeight = (r0.w / 16) * 9;
                    CommonActivity.setViewLayoutParams(IPCFourEyesActivity.this.binding.layoutGunOrientation, IPCFourEyesActivity.this.w, ((IPCFourEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCFourEyesActivity.this.binding.playerGun1, IPCFourEyesActivity.this.w / 2, ((IPCFourEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCFourEyesActivity.this.binding.playerGun3, IPCFourEyesActivity.this.w / 2, ((IPCFourEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCFourEyesActivity.this.binding.layoutPlay, IPCFourEyesActivity.this.w, (int) (((double) (((IPCFourEyesActivity.this.w / 2) / 16) * 9)) + (IPCFourEyesActivity.this.viewHeight * 2.0d)));
                }
                IPCFourEyesActivity.this.addControlTouchView();
                return true;
            }
        });
        this.infrarredMode = getResources().getStringArray(R.array.InfrarredMode);
        this.nightModelList.clear();
        int i = 0;
        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == -1) {
            while (true) {
                String[] strArr = this.infrarredMode;
                if (i >= strArr.length) {
                    break;
                }
                this.nightModelList.add(strArr[i]);
                i++;
            }
        } else {
            StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()))).reverse();
            for (int i2 = 0; i2 < sbReverse.length(); i2++) {
                if (sbReverse.charAt(i2) - '0' == 1) {
                    if (i2 == 0) {
                        this.nightModelList.add(this.infrarredMode[2]);
                    }
                    if (i2 == 1) {
                        this.nightModelList.add(this.infrarredMode[0]);
                    }
                    if (i2 == 2) {
                        this.nightModelList.add(this.infrarredMode[1]);
                    }
                }
            }
        }
        this.nightModeFragment = new SelectorDialogFragment(getString(R.string.night_mode), this.nightModelList);
        this.nightModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCFourEyesActivity.13
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v10, types: [int] */
            /* JADX WARN: Type inference failed for: r0v12 */
            /* JADX WARN: Type inference failed for: r0v13 */
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i3) {
                ((String) IPCFourEyesActivity.this.nightModelList.get(i3)).equals(IPCFourEyesActivity.this.infrarredMode[0]);
                ?? Equals = ((String) IPCFourEyesActivity.this.nightModelList.get(i3)).equals(IPCFourEyesActivity.this.infrarredMode[1]);
                if (((String) IPCFourEyesActivity.this.nightModelList.get(i3)).equals(IPCFourEyesActivity.this.infrarredMode[2])) {
                    Equals = 2;
                }
                IPCFourEyesActivity.this.updateNightMode(Integer.valueOf((int) Equals), IPCFourEyesActivity.this.selectIotId);
            }
        });
        this.inputDialogView = new InputDialogViewIpc.Builder().build();
        this.inputDialogView.addOnClickListener(new AnonymousClass14());
        this.showList.add(getResources().getString(R.string.show_mode4));
        this.showList.add(getResources().getString(R.string.show_mode2));
        this.showList.add(getResources().getString(R.string.show_mode2));
        this.showList.add(getResources().getString(R.string.show_mode3));
        this.definitionList.add(getString(R.string.quality_l));
        this.definitionList.add(getString(R.string.quality_m));
        this.definitionList.add(getString(R.string.quality_h));
        this.mapFragment = new SelectorDialogFragment("请选择地图，请确保安装了以下地图APP", "高德地图", "百度地图");
        this.mapFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCFourEyesActivity.15
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i3) {
                switch (i3) {
                    case 0:
                        if (MapUtils.isAvilible(IPCFourEyesActivity.this, "com.autonavi.minimap")) {
                            try {
                                StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=");
                                stringBuffer.append("yitu8_driver");
                                stringBuffer.append("&lat=");
                                stringBuffer.append(IPCFourEyesActivity.this.lat);
                                stringBuffer.append("&lon=");
                                stringBuffer.append(IPCFourEyesActivity.this.lon);
                                stringBuffer.append("&dev=");
                                stringBuffer.append(1);
                                stringBuffer.append("&style=");
                                stringBuffer.append(0);
                                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setPackage("com.autonavi.minimap");
                                IPCFourEyesActivity.this.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        } else {
                            Toast.makeText(IPCFourEyesActivity.this, "您尚未安装高德地图", 1).show();
                        }
                        break;
                    case 1:
                        if (MapUtils.isAvilible(IPCFourEyesActivity.this, "com.baidu.BaiduMap")) {
                            try {
                                StringBuffer stringBuffer2 = new StringBuffer("baidumap://map/navi?location=");
                                stringBuffer2.append(IPCFourEyesActivity.this.lat);
                                stringBuffer2.append(",");
                                stringBuffer2.append(IPCFourEyesActivity.this.lon);
                                stringBuffer2.append("&type=TIME");
                                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer2.toString()));
                                intent2.setPackage("com.baidu.BaiduMap");
                                IPCFourEyesActivity.this.startActivity(intent2);
                            } catch (Exception e2) {
                                Log.e("intent", e2.getMessage());
                                return;
                            }
                        } else {
                            Toast.makeText(IPCFourEyesActivity.this, "您尚未安装百度地图", 1).show();
                        }
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$14, reason: invalid class name */
    class AnonymousClass14 implements InputDialogViewIpc.OnClickListener {
        AnonymousClass14() {
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onPositiveClick(String str, Object obj) {
            IPCFourEyesActivity.this.showProgressDialog();
            IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).setAPList(IPCFourEyesActivity.this.selectSsid, str, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.14.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (z) {
                        try {
                            if (obj2 == null) {
                                IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.14.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCFourEyesActivity.this.getActivity(), IPCFourEyesActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else if (((IoTResponse) JSON.parseObject(obj2.toString()).toJavaObject(IoTResponse.class)).getCode() != 200) {
                                IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.14.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCFourEyesActivity.this.getActivity(), IPCFourEyesActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else {
                                IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.14.1.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        IPCFourEyesActivity.this.connect();
                                    }
                                });
                            }
                        } finally {
                            IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.14.1.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCFourEyesActivity.this.dismissProgressDialog();
                                }
                            });
                        }
                    }
                }
            });
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onNegativeClick() {
            IPCFourEyesActivity.this.inputDialogView.dismiss();
            IPCFourEyesActivity.this.f1575dialog.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMore() {
        this.mTopicData.clear();
        if (SharePreferenceManager.getInstance().getEventRecord(this.ballDevice.getIotId()) == 1) {
            this.mTopicData.add(new TopicBean(R.drawable.icon_cloud_back_false, getResources().getString(R.string.cloud_playback)));
            this.mTopicData.add(new TopicBean(R.drawable.icon_card_back_false, getResources().getString(R.string.sd_playback)));
        } else {
            this.mTopicData.add(new TopicBean(R.drawable.video_back, getString(R.string.video_back), false));
        }
        if (this.ballDevice.getOwned() == 1) {
            this.mTopicData.add(new TopicBean(R.drawable.share_ipc, getResources().getString(R.string.share)));
        }
        if (this.isDetecting) {
            if (SharePreferenceManager.getInstance().getSupportMotionDetect(this.ballDevice.getIotId()) == 0) {
                this.mTopicData.add(new TopicBean(R.drawable.humanoid_hracking_ipc_light, getResources().getString(R.string.track), true));
            } else {
                this.mTopicData.add(new TopicBean(R.drawable.humanoid_hracking_ipc_light, getResources().getString(R.string.track), true));
            }
        } else if (SharePreferenceManager.getInstance().getSupportMotionDetect(this.ballDevice.getIotId()) == 0) {
            this.mTopicData.add(new TopicBean(R.drawable.humanoid_hracking_ipc, getResources().getString(R.string.track), false));
        } else {
            this.mTopicData.add(new TopicBean(R.drawable.humanoid_hracking_ipc, getResources().getString(R.string.track), false));
        }
        this.mTopicData.add(new TopicBean(R.drawable.zoom_out, getResources().getString(R.string.zoom)));
        if (SharePreferenceManager.getInstance().getFloodlightSwitchShow(this.ballDevice.getIotId()) == 1) {
            if (SharePreferenceManager.getInstance().getFloodlightSwitch(this.ballDevice.getIotId()) == 1) {
                this.mTopicData.add(new TopicBean(R.drawable.icon_on_full, getResources().getString(R.string.floodlight), true));
            } else {
                this.mTopicData.add(new TopicBean(R.drawable.icon_off_full, getResources().getString(R.string.floodlight), false));
            }
        }
        if (SharePreferenceManager.getInstance().getMapShow(this.ballDevice.getIotId()) == 1) {
            this.mTopicData.add(new TopicBean(R.drawable.icon_more_loaction, getResources().getString(R.string.locate)));
        }
        if (SharePreferenceManager.getInstance().getLocationAbility(this.ballDevice.getIotId()) != -1) {
            StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getLocationAbility(this.ballDevice.getIotId()))).reverse();
            for (int i = 0; i < sbReverse.length(); i++) {
                if (sbReverse.charAt(i) - '0' == 1 && i == 1) {
                    this.mTopicData.add(new TopicBean(R.drawable.icon_more_gps, getResources().getString(R.string.map_gps)));
                }
            }
        }
    }

    private void initListener() {
        this.binding.ivBack.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.16
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.this.onBackPressed();
            }
        });
        this.binding.ivSetting.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.17
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(IPCFourEyesActivity.this, (Class<?>) SettingsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCFourEyesActivity.this.ballDevice);
                bundle.putSerializable("device1", IPCFourEyesActivity.this.gunDevice1);
                bundle.putSerializable("device2", IPCFourEyesActivity.this.gunDevice2);
                bundle.putSerializable("device3", IPCFourEyesActivity.this.gunDevice3);
                bundle.putSerializable("nvrDevice", IPCFourEyesActivity.this.nvrDevice);
                intent.putExtras(bundle);
                IPCFourEyesActivity.this.startActivity(intent);
            }
        });
        this.binding.llMoreDoubleEye.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.18
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.this.binding.llMoreDoubleEye.setSelected(!IPCFourEyesActivity.this.binding.llMoreDoubleEye.isSelected());
                if (IPCFourEyesActivity.this.binding.llMoreDoubleEye.isSelected()) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) IPCFourEyesActivity.this.binding.layoutControl.getLayoutParams();
                    layoutParams.removeRule(3);
                    layoutParams.addRule(12, -1);
                    IPCFourEyesActivity.this.binding.layoutControl.setLayoutParams(layoutParams);
                    CommonActivity.setViewLayoutParams(IPCFourEyesActivity.this.binding.layoutControl, -1, ScreenUtil.dp2Px(IPCFourEyesActivity.this, 300.0f));
                    IPCFourEyesActivity.this.binding.layoutCenter.setVisibility(0);
                    IPCFourEyesActivity.this.binding.layoutMore.setVisibility(0);
                    IPCFourEyesActivity.this.showMore();
                    IPCFourEyesActivity.this.initMore();
                    return;
                }
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) IPCFourEyesActivity.this.binding.layoutControl.getLayoutParams();
                layoutParams2.addRule(3, R.id.layout_play);
                layoutParams2.addRule(12, -1);
                IPCFourEyesActivity.this.binding.layoutControl.setLayoutParams(layoutParams2);
                CommonActivity.setViewLayoutParams(IPCFourEyesActivity.this.binding.layoutControl, -1, -1);
                IPCFourEyesActivity.this.binding.layoutCenter.setVisibility(8);
                IPCFourEyesActivity.this.binding.layoutMore.setVisibility(8);
                IPCFourEyesActivity.this.addControlTouchView();
            }
        });
        this.binding.llFull.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.19
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.access$3408(IPCFourEyesActivity.this);
                if (IPCFourEyesActivity.this.showMode > 3) {
                    IPCFourEyesActivity.this.showMode = -1;
                }
                if (IPCFourEyesActivity.this.showMode == 0) {
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                    iPCFourEyesActivity.liveAdapter = new LiveAdapter(iPCFourEyesActivity, iPCFourEyesActivity.liveList, true);
                    IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity2.linearLayoutManager = new PagerLayoutManager(iPCFourEyesActivity2.getActivity(), 1) { // from class: activity.IPCFourEyesActivity.19.1
                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollHorizontally() {
                            return false;
                        }

                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollVertically() {
                            return true;
                        }
                    };
                    IPCFourEyesActivity.this.linearLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.19.2
                        @Override // view.OnViewPagerListener
                        public void onInitComplete(View view3) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageDragging() {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageRelease(boolean z, int i, View view3) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageSelected(int i, boolean z, View view3) {
                            IPCFourEyesActivity.this.touchView.setVisibility(i == 2 ? 0 : 8);
                            IPCFourEyesActivity.this.showMode = i;
                        }
                    });
                    IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(IPCFourEyesActivity.this.linearLayoutManager);
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveAdapter);
                    return;
                }
                if (IPCFourEyesActivity.this.showMode == -1) {
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity3.liveFourAdapter = new LiveFourAdapter(iPCFourEyesActivity3, iPCFourEyesActivity3.liveList, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h, false);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new LinearLayoutManager(IPCFourEyesActivity.this));
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(IPCFourEyesActivity.this, 2);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: activity.IPCFourEyesActivity.19.3
                        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                        public int getSpanSize(int i) {
                            return IPCFourEyesActivity.this.liveFourAdapter.getItemViewType(i) == 0 ? 1 : 2;
                        }
                    });
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(gridLayoutManager);
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveFourAdapter);
                    return;
                }
                IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
            }
        });
        this.binding.tvZoomBack.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.20
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.layoutZoom.setVisibility(8);
                IPCFourEyesActivity.this.binding.layoutMore.setVisibility(0);
            }
        });
        this.binding.playerBall.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCFourEyesActivity.21
            /* JADX WARN: Removed duplicated region for block: B:54:0x00fc  */
            @Override // android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public boolean onTouch(android.view.View r7, android.view.MotionEvent r8) {
                /*
                    Method dump skipped, instruction units count: 362
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: activity.IPCFourEyesActivity.AnonymousClass21.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        });
        this.binding.zoomReduceBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCFourEyesActivity.22
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCFourEyesActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_press_ipc);
                    if (IPCFourEyesActivity.this.onTouchTimer == null) {
                        IPCFourEyesActivity.this.onTouchTimer = new Timer();
                        IPCFourEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCFourEyesActivity.22.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCFourEyesActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCFourEyesActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_ipc);
                    if (IPCFourEyesActivity.this.onTouchTimer != null) {
                        IPCFourEyesActivity.this.onTouchTimer.cancel();
                        IPCFourEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.btZoomReduceBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCFourEyesActivity.23
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCFourEyesActivity.this.binding.btZoomReduceBtn.setBackgroundResource(R.drawable.reduce_press_ipc);
                    if (IPCFourEyesActivity.this.onTouchTimer == null) {
                        IPCFourEyesActivity.this.onTouchTimer = new Timer();
                        IPCFourEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCFourEyesActivity.23.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCFourEyesActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCFourEyesActivity.this.binding.btZoomReduceBtn.setBackgroundResource(R.drawable.reduce_ipc);
                    if (IPCFourEyesActivity.this.onTouchTimer != null) {
                        IPCFourEyesActivity.this.onTouchTimer.cancel();
                        IPCFourEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.zoomAddBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCFourEyesActivity.24
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCFourEyesActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_press_ipc);
                    if (IPCFourEyesActivity.this.onTouchTimer == null) {
                        IPCFourEyesActivity.this.onTouchTimer = new Timer();
                        IPCFourEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCFourEyesActivity.24.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCFourEyesActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCFourEyesActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_ipc);
                    if (IPCFourEyesActivity.this.onTouchTimer != null) {
                        IPCFourEyesActivity.this.onTouchTimer.cancel();
                        IPCFourEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.btZoomAddBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCFourEyesActivity.25
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCFourEyesActivity.this.binding.btZoomAddBtn.setBackgroundResource(R.drawable.add_press_ipc);
                    if (IPCFourEyesActivity.this.onTouchTimer == null) {
                        IPCFourEyesActivity.this.onTouchTimer = new Timer();
                        IPCFourEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCFourEyesActivity.25.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCFourEyesActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCFourEyesActivity.this.binding.btZoomAddBtn.setBackgroundResource(R.drawable.add_ipc);
                    if (IPCFourEyesActivity.this.onTouchTimer != null) {
                        IPCFourEyesActivity.this.onTouchTimer.cancel();
                        IPCFourEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.fullAddZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCFourEyesActivity.26
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCFourEyesActivity.this.onTouchTimer == null) {
                        IPCFourEyesActivity.this.onTouchTimer = new Timer();
                        IPCFourEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCFourEyesActivity.26.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCFourEyesActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCFourEyesActivity.this.onTouchTimer != null) {
                    IPCFourEyesActivity.this.onTouchTimer.cancel();
                    IPCFourEyesActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.fullReduceZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCFourEyesActivity.27
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCFourEyesActivity.this.onTouchTimer == null) {
                        IPCFourEyesActivity.this.onTouchTimer = new Timer();
                        IPCFourEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCFourEyesActivity.27.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCFourEyesActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCFourEyesActivity.this.onTouchTimer != null) {
                    IPCFourEyesActivity.this.onTouchTimer.cancel();
                    IPCFourEyesActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.focusReduceBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.28
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.changeFocus(0);
            }
        });
        this.binding.focusAddBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.29
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.changeFocus(1);
            }
        });
        this.zoom.observe(this, new Observer<Float>() { // from class: activity.IPCFourEyesActivity.30
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Float f) {
                Log.d(IPCFourEyesActivity.this.TAG, "changeOpticalZoom:- " + f);
                if (f != null) {
                    if (f.floatValue() > 1.0f) {
                        IPCFourEyesActivity.this.binding.tvZoom.setVisibility(0);
                    } else {
                        IPCFourEyesActivity.this.binding.tvZoom.setVisibility(8);
                    }
                }
            }
        });
        this.binding.qualityBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.31
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.qualityDlg.setVisibility(0);
                IPCFourEyesActivity.this.binding.qualityDlg.bringToFront();
                IPCFourEyesActivity.this.changeQualityDlgView(SharePreferenceManager.getInstance().getStreamVideoQuality(IPCFourEyesActivity.this.ballDevice.getIotId()));
            }
        });
        this.binding.tvHQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.32
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.qualityDlg.setVisibility(8);
                IPCFourEyesActivity.this.changeDefinition(2);
            }
        });
        this.binding.tvMQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.33
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.qualityDlg.setVisibility(8);
                IPCFourEyesActivity.this.changeDefinition(1);
            }
        });
        this.binding.tvLQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.34
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.qualityDlg.setVisibility(8);
                IPCFourEyesActivity.this.changeDefinition(0);
            }
        });
        this.binding.tvLight1.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.35
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.lightDlg.setVisibility(8);
                IPCFourEyesActivity.this.updateNightMode(0, IPCFourEyesActivity.this.selectIotId);
            }
        });
        this.binding.tvLight2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.36
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.lightDlg.setVisibility(8);
                IPCFourEyesActivity.this.updateNightMode(1, IPCFourEyesActivity.this.selectIotId);
            }
        });
        this.binding.tvLight3.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.37
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.lightDlg.setVisibility(8);
                IPCFourEyesActivity.this.updateNightMode(2, IPCFourEyesActivity.this.selectIotId);
            }
        });
        this.binding.videoPlayIbtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.38
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.dismissPlayButton();
                if (SharePreferenceManager.getInstance().getLowPower(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCFourEyesActivity.this.wakeUpDevice();
                    IPCFourEyesActivity.this.wakeUpDeviceHandel();
                } else {
                    IPCFourEyesActivity.this.playLive();
                }
            }
        });
        this.binding.btPresetInvoke.setOnClickListener(new AnonymousClass39());
        this.binding.btPresetAdd.setOnClickListener(new AnonymousClass40());
        this.binding.fullSwitchWindow.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.41
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.access$3408(IPCFourEyesActivity.this);
                if (IPCFourEyesActivity.this.showMode > 3) {
                    IPCFourEyesActivity.this.showMode = -1;
                }
                if (IPCFourEyesActivity.this.showMode == 0) {
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                    iPCFourEyesActivity.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity, iPCFourEyesActivity.liveList, true, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                    PagerLayoutManager pagerLayoutManager = new PagerLayoutManager(IPCFourEyesActivity.this.getActivity(), 0) { // from class: activity.IPCFourEyesActivity.41.1
                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollHorizontally() {
                            return true;
                        }

                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    pagerLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.41.2
                        @Override // view.OnViewPagerListener
                        public void onInitComplete(View view3) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageRelease(boolean z, int i, View view3) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageSelected(int i, boolean z, View view3) {
                            Log.e("setOnViewPagerListener", "用户滑动完毕" + i);
                            IPCFourEyesActivity.this.showMode = i;
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageDragging() {
                            Log.e("setOnViewPagerListener", "用户滑动中");
                        }
                    });
                    IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(pagerLayoutManager);
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                    return;
                }
                if (IPCFourEyesActivity.this.showMode == -1) {
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity2.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity2, iPCFourEyesActivity2.liveList, false, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new GridLayoutManager(IPCFourEyesActivity.this, 2));
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                    return;
                }
                IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
            }
        });
        this.binding.layoutPlay.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.42
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.setFloatBarState();
            }
        });
        this.binding.ivNightBottom.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.43
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                iPCFourEyesActivity.selectIotId = iPCFourEyesActivity.ballDevice.getIotId();
                int i = 0;
                for (int i2 = 0; i2 < IPCFourEyesActivity.this.nightModelList.size(); i2++) {
                    if (((String) IPCFourEyesActivity.this.nightModelList.get(i2)).equals(IPCFourEyesActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(IPCFourEyesActivity.this.selectIotId)])) {
                        i = i2;
                    }
                }
                IPCFourEyesActivity.this.nightModeFragment.showAllowingStateLoss(IPCFourEyesActivity.this.getSupportFragmentManager(), "", i);
            }
        });
        this.binding.ivFull.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.44
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCFourEyesActivity.this.getRequestedOrientation() == 1) {
                    IPCFourEyesActivity.this.setRequestedOrientation(0);
                } else {
                    IPCFourEyesActivity.this.setRequestedOrientation(8);
                }
            }
        });
        this.binding.llCapture.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.45
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.this.snapshot();
            }
        });
        this.binding.fullCamera.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.46
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.this.snapshot();
            }
        });
        this.binding.fullVideo.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.47
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.llRecord.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.48
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.tvPtz.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.49
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.touchView.setVisibility(IPCFourEyesActivity.this.touchView.getVisibility() == 0 ? 8 : 0);
            }
        });
        this.binding.fullNightVision.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.50
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCFourEyesActivity.this.binding.lightDlg.getVisibility() == 8) {
                    IPCFourEyesActivity.this.changeLightDlgView(SharePreferenceManager.getInstance().getDayNightMode(IPCFourEyesActivity.this.selectIotId));
                    IPCFourEyesActivity.this.binding.lightDlg.setVisibility(0);
                    if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCFourEyesActivity.this.ballDevice.getIotId()) != -1) {
                        IPCFourEyesActivity.this.binding.tvLight1.setVisibility(8);
                        IPCFourEyesActivity.this.binding.tvLight2.setVisibility(8);
                        IPCFourEyesActivity.this.binding.tvLight3.setVisibility(8);
                        StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCFourEyesActivity.this.ballDevice.getIotId()))).reverse();
                        for (int i = 0; i < sbReverse.length(); i++) {
                            if (sbReverse.charAt(i) - '0' == 1) {
                                if (i == 0) {
                                    IPCFourEyesActivity.this.binding.tvLight3.setVisibility(0);
                                }
                                if (i == 1) {
                                    IPCFourEyesActivity.this.binding.tvLight1.setVisibility(0);
                                }
                                if (i == 2) {
                                    IPCFourEyesActivity.this.binding.tvLight2.setVisibility(0);
                                }
                            }
                        }
                        return;
                    }
                    return;
                }
                IPCFourEyesActivity.this.binding.lightDlg.setVisibility(8);
            }
        });
        this.binding.lightDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.51
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.lightDlg.setVisibility(8);
            }
        });
        this.binding.qualityDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.52
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.qualityDlg.setVisibility(8);
            }
        });
        this.binding.speakerBtn.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.53
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.this.startOrStopLiveIntercom();
            }
        });
        this.binding.fullIntercom.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.54
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.this.startOrStopLiveIntercom();
            }
        });
        this.binding.llListener.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.55
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.binding.llListener.setSelected(!IPCFourEyesActivity.this.binding.llListener.isSelected());
                IPCFourEyesActivity.this.binding.fullSound.setSelected(IPCFourEyesActivity.this.binding.llListener.isSelected());
                IPCFourEyesActivity.this.playBall.setVolume(IPCFourEyesActivity.this.binding.llListener.isSelected() ? 1.0f : 0.0f);
            }
        });
        this.binding.fullSound.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCFourEyesActivity.56
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCFourEyesActivity.this.binding.llListener.setSelected(!IPCFourEyesActivity.this.binding.llListener.isSelected());
                IPCFourEyesActivity.this.binding.fullSound.setSelected(IPCFourEyesActivity.this.binding.llListener.isSelected());
                IPCFourEyesActivity.this.playBall.setVolume(IPCFourEyesActivity.this.binding.llListener.isSelected() ? 1.0f : 0.0f);
            }
        });
        this.binding.ivCharge4gFlow.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.57
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCFourEyesActivity.this.binding.ivCharge4gFlow.isSelected()) {
                    IPCFourEyesActivity.this.isNet4GSwitch();
                    return;
                }
                Intent intent = new Intent(IPCFourEyesActivity.this.getActivity(), (Class<?>) YunThreeSelectActivity.class);
                intent.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCFourEyesActivity.this.ballDevice);
                intent.putExtra("device1", IPCFourEyesActivity.this.gunDevice1);
                intent.putExtra("device2", IPCFourEyesActivity.this.gunDevice2);
                intent.putExtra("nvrDevice", IPCFourEyesActivity.this.nvrDevice);
                IPCFourEyesActivity.this.startActivity(intent);
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$39, reason: invalid class name */
    class AnonymousClass39 implements View.OnClickListener {
        AnonymousClass39() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            if (IPCFourEyesActivity.this.getActivity() == null) {
                return;
            }
            ((InputMethodManager) IPCFourEyesActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(IPCFourEyesActivity.this.getWindow().getDecorView().getWindowToken(), 0);
            if ("".equals(((Editable) Objects.requireNonNull(IPCFourEyesActivity.this.binding.etPreset.getText())).toString())) {
                return;
            }
            IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).changePresetLocation(Integer.parseInt(IPCFourEyesActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCFourEyesActivity.39.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.39.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.set_success, 0).show();
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$40, reason: invalid class name */
    class AnonymousClass40 implements View.OnClickListener {
        AnonymousClass40() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            if (IPCFourEyesActivity.this.getActivity() == null) {
                return;
            }
            ((InputMethodManager) IPCFourEyesActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(IPCFourEyesActivity.this.getWindow().getDecorView().getWindowToken(), 0);
            if ("".equals(((Editable) Objects.requireNonNull(IPCFourEyesActivity.this.binding.etPreset.getText())).toString())) {
                return;
            }
            IPCManager.getInstance().getDevice(IPCFourEyesActivity.this.ballDevice.getIotId()).addPresetLocation(Integer.parseInt(IPCFourEyesActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCFourEyesActivity.40.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.40.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.set_success, 0).show();
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlayButton() {
        this.binding.videoBufferingBar.setVisibility(8);
        this.binding.wakeupText.setVisibility(8);
        this.binding.videoPlayIbtn.bringToFront();
        this.binding.videoPlayIbtn.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPlayButton() {
        this.binding.videoPlayIbtn.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBuffering() {
        this.binding.videoBufferingBar.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissSnapPicture() {
        this.binding.ivSnap.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissBuffering() {
        this.binding.videoBufferingBar.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFloatBarState() {
        if (this.isHorizontal) {
            this.binding.layoutPlay.setSelected(!this.binding.layoutPlay.isSelected());
            this.binding.fullScreen.setVisibility(this.binding.layoutPlay.isSelected() ? 0 : 8);
            this.binding.layoutTop.setVisibility(this.binding.layoutPlay.isSelected() ? 0 : 8);
            this.binding.layoutTop.bringToFront();
            return;
        }
        this.binding.playerInfoTv.setVisibility(this.binding.playerInfoTv.getVisibility() != 8 ? 8 : 0);
        updateInfoTv();
    }

    private void AutoSnap() {
        Bitmap bitmapSnapShot;
        Bitmap bitmapSnapShot2;
        Bitmap bitmapSnapShot3;
        Bitmap bitmapSnapShot4;
        if (this.playBall.getPlayState() == 3 && (bitmapSnapShot4 = this.playBall.snapShot()) != null) {
            saveBitmap(bitmapSnapShot4, this.ballDevice.getIotId());
        }
        if (this.playGun3.getPlayState() == 3 && (bitmapSnapShot3 = this.playGun3.snapShot()) != null) {
            saveBitmap(bitmapSnapShot3, this.gunDevice3.getIotId());
        }
        if (this.playGun1.getPlayState() == 3 && (bitmapSnapShot2 = this.playGun1.snapShot()) != null) {
            saveBitmap(bitmapSnapShot2, this.gunDevice1.getIotId());
        }
        if (this.playGun2.getPlayState() != 3 || (bitmapSnapShot = this.playGun2.snapShot()) == null) {
            return;
        }
        saveBitmap(bitmapSnapShot, this.gunDevice2.getIotId());
    }

    private void stopScreenLight() {
        getWindow().clearFlags(128);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInfoTv() {
        if (this.binding.playerInfoTv.getVisibility() == 0) {
            Message messageObtain = Message.obtain();
            messageObtain.what = 102;
            this.handler.sendMessageDelayed(messageObtain, 1000L);
        }
    }

    public void saveBitmap(final Bitmap bitmap, final String str) {
        Log.d(this.TAG, "saveBitmap: -------------------------------");
        final Application application = getApplication();
        new Thread(new Runnable() { // from class: activity.IPCFourEyesActivity.58
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                FileOutputStream fileOutputStream;
                File file;
                String string = SpUtil.getString(IPCFourEyesActivity.this.getActivity(), Utils.getDevSnapKey(str), "");
                long jCurrentTimeMillis = System.currentTimeMillis();
                LogEx.d(true, IPCFourEyesActivity.this.TAG, "保存图片");
                StringBuilder sb = new StringBuilder();
                IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                sb.append(iPCFourEyesActivity.getFilesPath(iPCFourEyesActivity.getApplication()));
                sb.append("/snap/");
                sb.append(str);
                sb.append("/");
                String string2 = sb.toString();
                Log.d(IPCFourEyesActivity.this.TAG, "run: puppet:dirPath====" + string2);
                FileOutputStream fileOutputStream2 = null;
                try {
                    try {
                        try {
                            File file2 = new File(string2);
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }
                            file = new File(string2, jCurrentTimeMillis + ".png");
                            fileOutputStream = new FileOutputStream(file);
                        } catch (Exception e) {
                            e = e;
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                    }
                    try {
                        boolean zCompress = bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                        Log.d(IPCFourEyesActivity.this.TAG, "run: puppet:b=====" + zCompress);
                        fileOutputStream.flush();
                        LogEx.e(true, IPCFourEyesActivity.this.TAG, "图片保存地址: " + file.getAbsolutePath());
                        Log.d(IPCFourEyesActivity.this.TAG, "puppet:图片保存地址: file.getAbsolutePath()======" + file.getAbsolutePath());
                        Log.d(IPCFourEyesActivity.this.TAG, "puppet:图片保存地址: Utils.getDevSnapKey(tempIotId)======" + Utils.getDevSnapKey(str));
                        SpUtil.putValue(application, Utils.getDevSnapKey(str), file.getAbsolutePath());
                        EventBus.getDefault().post(new CameraSnapUpdate(str, jCurrentTimeMillis));
                        FileUtil.deleteFile(string);
                        fileOutputStream.close();
                    } catch (Exception e2) {
                        e = e2;
                        fileOutputStream2 = fileOutputStream;
                        e.printStackTrace();
                        FileUtil.delete(string2);
                        if (fileOutputStream2 == null) {
                        } else {
                            fileOutputStream2.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }).start();
    }

    private void startLiveIntercom() {
        if (this.binding.speakerBtn.isSelected()) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Permission.RECORD_AUDIO) != 0) {
            ActivityCompat.requestPermissions(this, new String[]{Permission.RECORD_AUDIO}, 4372);
            return;
        }
        LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
        if (liveIntercomV2 != null) {
            liveIntercomV2.start();
            Log.e("speaker----", "play");
            this.whiteProgressDialog.setText(getResources().getString(R.string.open_speech));
            this.whiteProgressDialog.show();
            this.binding.speakerBtn.setSelected(true);
            this.binding.fullIntercom.setSelected(this.binding.speakerBtn.isSelected());
        }
    }

    private void getThingsStatus(String str) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/status/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass59());
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$59, reason: invalid class name */
    class AnonymousClass59 implements IoTCallback {
        AnonymousClass59() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            exc.printStackTrace();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
            Log.d(IPCFourEyesActivity.this.TAG, "run: ---------------" + Thread.currentThread().getName());
            try {
                if (((org.json.JSONObject) ioTResponse.getData()).get("status").toString().equals("3")) {
                    if (SharePreferenceManager.getInstance().getIccId(IPCFourEyesActivity.this.ballDevice.getIotId()) == null || "".equals(SharePreferenceManager.getInstance().getIccId(IPCFourEyesActivity.this.ballDevice.getIotId()))) {
                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.59.2
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCFourEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                IPCFourEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                            }
                        });
                    } else {
                        new OkHttpClient().newCall(new Request.Builder().url("http://www.secueye.cn:8000/api/smsApi?iccid=" + SharePreferenceManager.getInstance().getIccId(IPCFourEyesActivity.this.ballDevice.getIotId()) + "&method=smsStatusSecueye").get().build()).enqueue(new Callback() { // from class: activity.IPCFourEyesActivity.59.1
                            static final /* synthetic */ boolean $assertionsDisabled = false;

                            @Override // okhttp3.Callback
                            public void onFailure(Call call, IOException iOException) {
                            }

                            @Override // okhttp3.Callback
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject object = JSONObject.parseObject(response.body().string());
                                    if (object.containsKey("code")) {
                                        int iIntValue = object.getInteger("code").intValue();
                                        if (iIntValue == 400) {
                                            IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.59.1.1
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCFourEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCFourEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                    IPCFourEyesActivity.this.isOtherCard = true;
                                                }
                                            });
                                            return;
                                        } else if (iIntValue != 200) {
                                            IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.exception_4g_data));
                                            return;
                                        }
                                    }
                                    if (!object.containsKey("values") || IPCFourEyesActivity.this.isOtherCard) {
                                        return;
                                    }
                                    JSONObject jSONObject = object.getJSONObject("values");
                                    if (jSONObject.containsKey("status")) {
                                        if (!jSONObject.getString("status").equals("停机")) {
                                            IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.59.1.4
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCFourEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCFourEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                }
                                            });
                                        } else if (AppConfig.isChina) {
                                            IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.59.1.2
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    if (!IPCFourEyesActivity.this.isHorizontal) {
                                                        IPCFourEyesActivity.this.binding.traffic4gExpired.bringToFront();
                                                        IPCFourEyesActivity.this.binding.immediateRenewal.bringToFront();
                                                        IPCFourEyesActivity.this.binding.outlineTime.bringToFront();
                                                        IPCFourEyesActivity.this.binding.videoPlayIbtn.setVisibility(8);
                                                        IPCFourEyesActivity.this.binding.ipcOfflineText.setVisibility(8);
                                                        IPCFourEyesActivity.this.binding.traffic4gExpired.setVisibility(0);
                                                        IPCFourEyesActivity.this.binding.immediateRenewal.setVisibility(0);
                                                        IPCFourEyesActivity.this.binding.outlineTime.setVisibility(0);
                                                    }
                                                    try {
                                                        IPCFourEyesActivity.this.binding.outlineTime.setText(((Object) IPCFourEyesActivity.this.getResources().getText(R.string.time_of_off_line)) + "：" + TimeUtil.TimeStamp2Date(((org.json.JSONObject) ioTResponse.getData()).get("time").toString()));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    IPCFourEyesActivity.this.needRecharge = true;
                                                }
                                            });
                                        } else {
                                            IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.59.1.3
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCFourEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCFourEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                }
                                            });
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getWiFiList() {
        SharePreferenceManager.getInstance().setFirstNet(this.ballDevice.getIotId(), false);
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).queryAPList(new IPanelCallback() { // from class: activity.IPCFourEyesActivity.60
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null) {
                    return;
                }
                try {
                    IoTResponse ioTResponse = (IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class);
                    if (ioTResponse.getCode() != 200) {
                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.60.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCFourEyesActivity.this, IPCFourEyesActivity.this.getString(R.string.get_wifi_failed), 0).show();
                            }
                        });
                    } else {
                        Object data = ioTResponse.getData();
                        if (data != null) {
                            try {
                                JSONArray jSONArray = ((JSONObject) data).getJSONArray("APList");
                                IPCFourEyesActivity.this.wifiBeanList = JSON.parseArray(jSONArray.toString(), WifiBean.class);
                                IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.60.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCFourEyesActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCFourEyesActivity.this.FourGChangeDialog();
                                        IPCFourEyesActivity.this.getWiFiListSucceed(IPCFourEyesActivity.this.wifiBeanList);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.60.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCFourEyesActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getString(R.string.query_wifi_list_fail));
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void FourGChangeDialog() {
        View viewInflate = View.inflate(this, R.layout.wifi_list_ipc, null);
        this.f1575dialog = new AlertDialog.Builder(this).setView(viewInflate).create();
        this.f1575dialog.setCanceledOnTouchOutside(true);
        this.f1575dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.f1575dialog.show();
        int i = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams attributes = this.f1575dialog.getWindow().getAttributes();
        attributes.width = (int) (((double) i) * 0.95d);
        this.f1575dialog.getWindow().setAttributes(attributes);
        this.f1575dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCFourEyesActivity.61
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCFourEyesActivity.this.cancelCount();
            }
        });
        ((Button) viewInflate.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.62
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity.this.f1575dialog.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new IpcWiFiAdapter(R.layout.item_wifi_ipc);
        this.mAdapter.bindToRecyclerView(recyclerView);
        this.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: activity.IPCFourEyesActivity.63
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i2) {
                WifiBean wifiBean = IPCFourEyesActivity.this.mAdapter.getData().get(i2);
                if (wifiBean.isCurrentWifi()) {
                    return;
                }
                IPCFourEyesActivity.this.f1575dialog.dismiss();
                IPCFourEyesActivity.this.selectSsid = wifiBean.getSsid();
                IPCFourEyesActivity.this.inputDialogView.setTitle(IPCFourEyesActivity.this.selectSsid);
                IPCFourEyesActivity.this.inputDialogView.show(IPCFourEyesActivity.this.getSupportFragmentManager(), IPCFourEyesActivity.this.TAG);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void FourGChangeDialog(int i, AlertDialog alertDialog) {
        View viewInflate = View.inflate(this, R.layout.switch_layout, null);
        ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.load_progressbar);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.close_btn);
        Drawable drawable = getResources().getDrawable(R.drawable.et_cancel);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        imageButton.setBackground(drawable);
        Button button = (Button) viewInflate.findViewById(R.id.cancel);
        TextView textView = (TextView) viewInflate.findViewById(R.id.load_progressbar_text);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.image_result_text);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(true);
        alertDialogCreate.show();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCFourEyesActivity.64
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCFourEyesActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.65
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.66
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass67(60000L, 4000L, i, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$67, reason: invalid class name */
    class AnonymousClass67 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ int val$position;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass67(long j, long j2, int i, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$position = i;
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(IPCFourEyesActivity.this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCFourEyesActivity.67.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCFourEyesActivity.this.ballDevice.getIotId()) == AnonymousClass67.this.val$position) {
                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.67.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCFourEyesActivity.this.playLive();
                                AnonymousClass67.this.val$progressBarText.setVisibility(8);
                                AnonymousClass67.this.val$progressBar.setVisibility(8);
                                AnonymousClass67.this.val$imageViewText.setVisibility(0);
                                AnonymousClass67.this.val$imageViewText.setText(IPCFourEyesActivity.this.getString(R.string.switched_success));
                                AnonymousClass67.this.val$imageView.setVisibility(0);
                                AnonymousClass67.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass67.this.val$imageButton.setVisibility(0);
                                Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                        AnonymousClass67.this.cancel();
                        IPCFourEyesActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.67.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass67.this.val$progressBarText.setVisibility(8);
                    AnonymousClass67.this.val$progressBar.setVisibility(8);
                    AnonymousClass67.this.val$imageViewText.setVisibility(0);
                    AnonymousClass67.this.val$imageViewText.setText(IPCFourEyesActivity.this.getString(R.string.switched_fail));
                    AnonymousClass67.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass67.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCFourEyesActivity.this.countDownTimer = null;
        }
    }

    public void connect() {
        View viewInflate = View.inflate(this, R.layout.switch_layout, null);
        ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.load_progressbar);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.close_btn);
        Drawable drawable = getResources().getDrawable(R.drawable.et_cancel);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        imageButton.setBackground(drawable);
        Button button = (Button) viewInflate.findViewById(R.id.cancel);
        TextView textView = (TextView) viewInflate.findViewById(R.id.load_progressbar_text);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.image_result_text);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(true);
        alertDialogCreate.show();
        this.inputDialogView.dismiss();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCFourEyesActivity.68
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCFourEyesActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.69
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.70
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass71(60000L, 4000L, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$71, reason: invalid class name */
    class AnonymousClass71 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass71(long j, long j2, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(IPCFourEyesActivity.this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCFourEyesActivity.71.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getWifiConfigIsExist(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.71.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass71.this.val$progressBarText.setVisibility(8);
                                AnonymousClass71.this.val$progressBar.setVisibility(8);
                                AnonymousClass71.this.val$imageViewText.setVisibility(0);
                                AnonymousClass71.this.val$imageViewText.setText(IPCFourEyesActivity.this.getString(R.string.switched_success));
                                AnonymousClass71.this.val$imageView.setVisibility(0);
                                AnonymousClass71.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass71.this.val$imageButton.setVisibility(0);
                            }
                        });
                        AnonymousClass71.this.cancel();
                        IPCFourEyesActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.71.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass71.this.val$progressBarText.setVisibility(8);
                    AnonymousClass71.this.val$progressBar.setVisibility(8);
                    AnonymousClass71.this.val$imageViewText.setVisibility(0);
                    AnonymousClass71.this.val$imageViewText.setText(IPCFourEyesActivity.this.getString(R.string.switched_fail));
                    AnonymousClass71.this.val$imageView.setVisibility(0);
                    AnonymousClass71.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass71.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCFourEyesActivity.this.countDownTimer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getWiFiListSucceed(List<WifiBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.mAdapter.replaceData(list);
    }

    public void showBadNetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflate = View.inflate(this, R.layout.switch_network_layout, null);
        builder.setView(viewInflate);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ((Button) viewInflate.findViewById(R.id.btn_left)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.72
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        ((Button) viewInflate.findViewById(R.id.btn_right)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.73
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                iPCFourEyesActivity.switch4gMode(iPCFourEyesActivity.getString(R.string.Net4GEnableSwitch), 2);
                alertDialogCreate.dismiss();
                IPCFourEyesActivity.this.FourGChangeDialog(2, alertDialogCreate);
            }
        });
    }

    public void cancelCount() {
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.countDownTimer = null;
        }
    }

    private void showFormatDialog(final float f, final float f2, final int i) {
        this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.74
            @Override // java.lang.Runnable
            public void run() {
                new BaseDialog.Builder().view(R.layout.dialog_common).content(IPCFourEyesActivity.this.getString(R.string.sd_card_not_initialized)).leftBtnText(IPCFourEyesActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.74.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        SharePreferenceManager.getInstance().setFirstFormatInIpc(IPCFourEyesActivity.this.ballDevice.getIotId(), false);
                    }
                }).rightBtnText(IPCFourEyesActivity.this.getString(R.string.format)).clickRight(new View.OnClickListener() { // from class: activity.IPCFourEyesActivity.74.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Intent intent = new Intent(IPCFourEyesActivity.this, (Class<?>) StorageStatusActivity.class);
                        intent.putExtra("totalStorage", f);
                        intent.putExtra("remainStorage", f2);
                        intent.putExtra("storageStatusValues", i);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCFourEyesActivity.this.ballDevice);
                        if (IPCFourEyesActivity.this.gunDevice1 != null) {
                            bundle.putSerializable("device1", IPCFourEyesActivity.this.gunDevice1);
                        }
                        if (IPCFourEyesActivity.this.gunDevice2 != null) {
                            bundle.putSerializable("device2", IPCFourEyesActivity.this.gunDevice2);
                        }
                        if (IPCFourEyesActivity.this.nvrDevice != null) {
                            bundle.putSerializable("nvrDevice", IPCFourEyesActivity.this.nvrDevice);
                        }
                        intent.putExtras(bundle);
                        IPCFourEyesActivity.this.startActivity(intent);
                        IPCFourEyesActivity.this.needTFInit = false;
                    }
                }).canCancel(false).create().show(IPCFourEyesActivity.this.getSupportFragmentManager(), "");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpDevice() {
        if (this.playBall.getPlayState() == 3 || this.playGun1.getPlayState() == 3 || this.playGun2.getPlayState() == 3 || this.playGun3.getPlayState() == 3 || !isActivityForeground()) {
            return;
        }
        this.countWakeUp = 0;
        HashMap map = new HashMap();
        map.put(Constants.LowPowerAppStatus, 1);
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.75
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                IPCFourEyesActivity.this.needWakeUp = true;
                if (IPCFourEyesActivity.this.playBall != null && IPCFourEyesActivity.this.playBall.getPlayState() != 3) {
                    SettingsCtrl.getInstance().getProperties(IPCFourEyesActivity.this.ballDevice.getIotId(), new AnonymousClass1());
                }
                IPCFourEyesActivity.access$7108(IPCFourEyesActivity.this);
                IPCFourEyesActivity.this.wakeUpHandler.sendEmptyMessageDelayed(1, 8000L);
            }

            /* JADX INFO: renamed from: activity.IPCFourEyesActivity$75$1, reason: invalid class name */
            class AnonymousClass1 implements MyCallback {
                AnonymousClass1() {
                }

                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (SharePreferenceManager.getInstance().getLowPowerStatus(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                        Handler handler = IPCFourEyesActivity.this.wakeUpHandler;
                        final IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                        handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCFourEyesActivity$75$1$QPbL1P8zFsbDkbaaUzJzJTiWu0k
                            @Override // java.lang.Runnable
                            public final void run() {
                                iPCFourEyesActivity.playLive();
                            }
                        });
                    }
                }
            }
        });
        LivePlayer livePlayer = this.playBall;
        if (livePlayer != null && livePlayer.getPlayState() != 3) {
            SettingsCtrl.getInstance().getProperties(this.ballDevice.getIotId(), new AnonymousClass76());
        }
        HashMap map2 = new HashMap();
        map2.put(Constants.LowPowerWakeUp, 1);
        IPCManager.getInstance().getDevice(this.nvrDevice.getIotId()).setProperties(map2, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.77
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$76, reason: invalid class name */
    class AnonymousClass76 implements MyCallback {
        AnonymousClass76() {
        }

        @Override // tools.MyCallback
        public void onComplete(boolean z) {
            if (SharePreferenceManager.getInstance().getLowPowerStatus(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                Handler handler = IPCFourEyesActivity.this.wakeUpHandler;
                final IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCFourEyesActivity$76$kPSFtuy7BkC5FBFtzNIFVbbxqZg
                    @Override // java.lang.Runnable
                    public final void run() {
                        iPCFourEyesActivity.playLive();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpDeviceHandel() {
        Message messageObtain = Message.obtain();
        messageObtain.what = 2;
        this.wakeUpHandler.sendMessageDelayed(messageObtain, AppConfig.LOW_POWER);
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$78, reason: invalid class name */
    class AnonymousClass78 extends Handler {
        AnonymousClass78() {
        }

        @Override // android.os.Handler
        public void handleMessage(@NonNull Message message) {
            if (message.what == 1) {
                if (IPCFourEyesActivity.this.playBall.getPlayState() != 3) {
                    IPCFourEyesActivity.access$7108(IPCFourEyesActivity.this);
                    if (SharePreferenceManager.getInstance().getNetState(IPCFourEyesActivity.this.ballDevice.getIotId()) != 3) {
                        Handler handler = IPCFourEyesActivity.this.handler;
                        final IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                        handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCFourEyesActivity$78$xUmunCJbprPHZsfsxi1kVKczvAk
                            @Override // java.lang.Runnable
                            public final void run() {
                                iPCFourEyesActivity.playLive();
                            }
                        });
                    }
                    if (IPCFourEyesActivity.this.countWakeUp < 5) {
                        IPCFourEyesActivity.this.wakeUpHandler.sendEmptyMessageDelayed(1, 8000L);
                        return;
                    }
                    return;
                }
                return;
            }
            if (message.what == 2) {
                IPCFourEyesActivity.this.wakeUpDevice();
                IPCFourEyesActivity.this.wakeUpDeviceHandel();
            }
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        if (configuration.orientation == 2) {
            this.isHorizontal = true;
            setFullScreen();
        } else {
            this.isHorizontal = false;
            backFullScreen();
        }
        super.onConfigurationChanged(configuration);
        setSwipeBackEnable(!this.isLand);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void changeLightDlgView(int i) {
        switch (i) {
            case 0:
                this.binding.tvLight1.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.binding.tvLight1.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvLight2.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight2.setBackground(null);
                this.binding.tvLight3.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight3.setBackground(null);
                break;
            case 1:
                this.binding.tvLight1.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight1.setBackground(null);
                this.binding.tvLight2.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvLight2.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.binding.tvLight3.setBackground(null);
                this.binding.tvLight3.setTextColor(getResources().getColor(R.color.color_white));
                break;
            case 2:
                this.binding.tvLight1.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight1.setBackground(null);
                this.binding.tvLight2.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight2.setBackground(null);
                this.binding.tvLight3.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvLight3.setTextColor(getResources().getColor(R.color.color_2999ff));
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeQualityDlgView(int i) {
        switch (i) {
            case 0:
                this.binding.tvHQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvHQuality.setBackground(null);
                this.binding.tvMQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvMQuality.setBackground(null);
                this.binding.tvLQuality.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvLQuality.setTextColor(getResources().getColor(R.color.color_2999ff));
                break;
            case 1:
                this.binding.tvHQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvHQuality.setBackground(null);
                this.binding.tvMQuality.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvMQuality.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.binding.tvLQuality.setBackground(null);
                this.binding.tvLQuality.setTextColor(getResources().getColor(R.color.color_white));
                break;
            case 2:
                this.binding.tvHQuality.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.binding.tvHQuality.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvMQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvMQuality.setBackground(null);
                this.binding.tvLQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLQuality.setBackground(null);
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
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.79
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() == 200) {
                            SharePreferenceManager.getInstance().setStreamVideoQuality(IPCFourEyesActivity.this.ballDevice.getIotId(), i);
                            IPCFourEyesActivity.this.binding.qualityBtn.setText((CharSequence) IPCFourEyesActivity.this.definitionList.get(SharePreferenceManager.getInstance().getStreamVideoQuality(IPCFourEyesActivity.this.ballDevice.getIotId())));
                        } else {
                            IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.79.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setFullScreen() {
        this.binding.layoutTop.setVisibility(8);
        this.binding.layoutControl.setVisibility(8);
        this.binding.ivSetting.setVisibility(8);
        this.binding.ivNightBottom.setVisibility(8);
        this.binding.ivFull.setVisibility(8);
        this.binding.llFull.setVisibility(8);
        this.binding.immediateRenewal.setVisibility(8);
        this.binding.traffic4gExpired.setVisibility(8);
        this.binding.outlineTime.setVisibility(8);
        this.binding.layoutUpDown.setVisibility(8);
        if (this.binding.layoutQuality.getParent() != null) {
            ((ViewGroup) this.binding.layoutQuality.getParent()).removeView(this.binding.layoutQuality);
        }
        this.binding.layoutTop.addView(this.binding.layoutQuality);
        this.binding.layoutQuality.requestLayout();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(15, -1);
        layoutParams.addRule(11, -1);
        this.binding.layoutQuality.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams2.setMargins(0, 0, 0, 0);
        this.binding.layoutPlay.setLayoutParams(layoutParams2);
        this.binding.layoutTop.setLayoutParams(new RelativeLayout.LayoutParams(-1, ScreenUtil.dp2Px(this, 44.0f)));
        this.binding.layoutTop.setBackground(getResources().getDrawable(R.drawable.bg_gray_transparent));
        addControlTouchView();
        if (this.isRatio) {
            this.viewHeight = this.w / 2;
            Log.e("屏幕0", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
            MyGlTextureView myGlTextureView = this.binding.playerGun1;
            int i = this.w;
            setViewLayoutParams(myGlTextureView, i / 2, ((i / 2) / 16) * 9);
            MyGlTextureView myGlTextureView2 = this.binding.playerGun2;
            int i2 = this.w;
            setViewLayoutParams(myGlTextureView2, i2 / 2, ((i2 / 2) / 16) * 9);
            setViewLayoutParams(this.binding.layoutGun, -1, ((this.w / 2) / 16) * 9);
            setViewLayoutParams(this.binding.playerBall, -1, (this.w / 16) * 9);
            setViewLayoutParams(this.binding.playerGun3, -1, (this.w / 16) * 9);
            RelativeLayout relativeLayout = this.binding.layoutPlay;
            int i3 = this.w;
            setViewLayoutParams(relativeLayout, -1, (((i3 / 2) / 16) * 9) + ((i3 / 16) * 9));
        } else {
            this.showMode = -1;
            this.viewHeight = (this.w / 16) * 9;
            Log.e("屏幕1", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
            setViewLayoutParams(this.binding.layoutPlay, -1, -1);
            setViewLayoutParams(this.binding.playerBall, this.h / 2, this.w / 2);
            setViewLayoutParams(this.binding.playerGun1, this.h / 2, this.w / 2);
            setViewLayoutParams(this.binding.playerGun2, this.h / 2, this.w / 2);
            setViewLayoutParams(this.binding.playerGun3, this.h / 2, this.w / 2);
            if (this.binding.playerBall.getParent() != null) {
                ((ViewGroup) this.binding.playerBall.getParent()).removeView(this.binding.playerBall);
            }
            if (this.binding.playerGun1.getParent() != null) {
                ((ViewGroup) this.binding.playerGun1.getParent()).removeView(this.binding.playerGun1);
            }
            if (this.binding.playerGun2.getParent() != null) {
                ((ViewGroup) this.binding.playerGun2.getParent()).removeView(this.binding.playerGun2);
            }
            if (this.binding.playerGun3.getParent() != null) {
                ((ViewGroup) this.binding.playerGun3.getParent()).removeView(this.binding.playerGun3);
            }
            this.liveList.clear();
            this.liveList.add(this.binding.playerGun1);
            this.liveList.add(this.binding.playerGun3);
            this.liveList.add(this.binding.playerBall);
            this.liveList.add(this.binding.playerGun2);
            this.liveHorizontalAdapter = new LiveHorizontalAdapter(this, this.liveList, false, this.w, this.h);
            this.binding.rvLive.setLayoutManager(new GridLayoutManager(this, 2));
            this.binding.rvLive.setAdapter(this.liveHorizontalAdapter);
        }
        getWindow().setFlags(1024, 1024);
    }

    public void backFullScreen() {
        this.showMode = -1;
        if (this.binding.playerBall.getParent() != null) {
            ((ViewGroup) this.binding.playerBall.getParent()).removeView(this.binding.playerBall);
        }
        if (this.binding.playerGun1.getParent() != null) {
            ((ViewGroup) this.binding.playerGun1.getParent()).removeView(this.binding.playerGun1);
        }
        if (this.binding.playerGun2.getParent() != null) {
            ((ViewGroup) this.binding.playerGun2.getParent()).removeView(this.binding.playerGun2);
        }
        if (this.binding.playerGun3.getParent() != null) {
            ((ViewGroup) this.binding.playerGun3.getParent()).removeView(this.binding.playerGun3);
        }
        this.liveList.clear();
        this.liveList.add(this.binding.playerGun1);
        this.liveList.add(this.binding.playerGun3);
        this.liveList.add(this.binding.playerBall);
        this.liveList.add(this.binding.playerGun2);
        this.liveFourAdapter = new LiveFourAdapter(this, this.liveList, this.w, this.h, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: activity.IPCFourEyesActivity.80
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                return IPCFourEyesActivity.this.liveFourAdapter.getItemViewType(i) == 0 ? 1 : 2;
            }
        });
        this.binding.rvLive.setLayoutManager(gridLayoutManager);
        this.binding.rvLive.setAdapter(this.liveFourAdapter);
        this.binding.ivSetting.setVisibility(0);
        this.binding.layoutControl.setVisibility(0);
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
            this.binding.ivNightBottom.setVisibility(0);
        }
        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == 0) {
            this.binding.ivNightBottom.setVisibility(8);
        }
        this.binding.ivFull.setVisibility(0);
        this.binding.llFull.setVisibility(0);
        this.binding.fullScreen.setVisibility(8);
        this.binding.lightDlg.setVisibility(8);
        if (this.needRecharge) {
            dismissPlayButton();
            this.binding.immediateRenewal.setVisibility(0);
            this.binding.traffic4gExpired.setVisibility(0);
            this.binding.outlineTime.setVisibility(0);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, ScreenUtil.dp2Px(this, 40.0f));
        layoutParams.setMargins(0, ScreenUtil.dp2Px(this, 27.0f), 0, 0);
        this.binding.layoutTop.setLayoutParams(layoutParams);
        this.binding.layoutTop.setBackgroundColor(getResources().getColor(R.color.color_black));
        this.binding.layoutTop.setVisibility(0);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams2.setMargins(0, ScreenUtil.dp2Px(this, 70.0f), 0, 0);
        this.binding.layoutPlay.setLayoutParams(layoutParams2);
        if (this.binding.layoutQuality.getParent() != null) {
            ((ViewGroup) this.binding.layoutQuality.getParent()).removeView(this.binding.layoutQuality);
        }
        this.binding.layoutPlay.addView(this.binding.layoutQuality);
        this.binding.layoutQuality.requestLayout();
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams3.setMargins(ScreenUtil.dp2Px(this, 5.0f), ScreenUtil.dp2Px(this, 5.0f), ScreenUtil.dp2Px(this, 5.0f), ScreenUtil.dp2Px(this, 15.0f));
        layoutParams3.addRule(12, -1);
        this.binding.layoutQuality.setLayoutParams(layoutParams3);
        setViewLayoutParams(this.binding.layoutPlay, -1, ScreenUtil.dp2Px(this, 510.0f));
        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) this.binding.layoutControl.getLayoutParams();
        layoutParams4.addRule(3, R.id.layout_play);
        layoutParams4.addRule(12, -1);
        this.binding.layoutControl.setLayoutParams(layoutParams4);
        this.touchView.setVisibility(0);
        addControlTouchView();
        getWindow().clearFlags(1024);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void isNet4GSwitch() {
        if (((SharePreferenceManager.getInstance().getPageControlEx(this.ballDevice.getIotId()) & 524288) >> 19) == 1) {
            if (SharePreferenceManager.getInstance().getIccId1(this.ballDevice.getIotId()).equals("") && SharePreferenceManager.getInstance().getIccId2(this.ballDevice.getIotId()).equals("")) {
                Intent intent = new Intent(getActivity(), (Class<?>) Traffic4GActivity.class);
                intent.putExtra("iotId", this.ballDevice.getIotId());
                intent.putExtra("iccid", SharePreferenceManager.getInstance().getIccId(this.ballDevice.getIotId()));
                intent.putExtra(AlinkConstants.KEY_DN, this.nvrDevice.getDeviceName());
                intent.putExtra(AlinkConstants.KEY_PK, this.nvrDevice.getProductKey());
                startActivity(intent);
                return;
            }
            Intent intent2 = new Intent(this, (Class<?>) Net4GSwitchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.ballDevice);
            bundle.putSerializable("device1", this.gunDevice1);
            bundle.putSerializable("nvrDevice", this.nvrDevice);
            intent2.putExtras(bundle);
            startActivity(intent2);
            return;
        }
        Intent intent3 = new Intent(getActivity(), (Class<?>) Traffic4GActivity.class);
        intent3.putExtra("iotId", this.ballDevice.getIotId());
        intent3.putExtra("iccid", SharePreferenceManager.getInstance().getIccId(this.ballDevice.getIotId()));
        intent3.putExtra(AlinkConstants.KEY_DN, this.nvrDevice.getDeviceName());
        intent3.putExtra(AlinkConstants.KEY_PK, this.nvrDevice.getProductKey());
        startActivity(intent3);
    }

    private void initPlayerBall() {
        this.playBall = new LivePlayer(getApplicationContext());
        this.playBall.setTextureView(this.binding.playerBall);
        this.binding.playerBall.setClickable(true);
        this.playBall.setVolume(this.binding.llListener.isSelected() ? 1.0f : 0.0f);
        this.playBall.setVideoScalingMode(1);
        this.binding.playerBall.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCFourEyesActivity.81
            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            @SuppressLint({"SetTextI18n"})
            public void onScaleChanged(ZoomableTextureView zoomableTextureView, float f) {
                String str;
                double dDoubleValue = new BigDecimal(f).setScale(2, 4).doubleValue();
                DecimalFormat decimalFormat = new DecimalFormat("##0");
                Log.d(IPCFourEyesActivity.this.TAG, "onScaleChanged: " + dDoubleValue);
                if (SharePreferenceManager.getInstance().getMixZoom(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                    str = decimalFormat.format((((dDoubleValue - 1.0d) * 2.25d) + 1.0d) * ((double) IPCFourEyesActivity.this.ZoomMax));
                } else {
                    str = decimalFormat.format(((dDoubleValue - 1.0d) * 2.25d) + 1.0d);
                }
                IPCFourEyesActivity.this.zoom.postValue(Float.valueOf(f));
                IPCFourEyesActivity.this.binding.tvZoom.setText(str + "X");
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                int i = 0;
                int i2 = 1;
                if (IPCFourEyesActivity.this.isHorizontal) {
                    if (IPCFourEyesActivity.this.showMode == -1) {
                        IPCFourEyesActivity.this.showMode = 2;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                        iPCFourEyesActivity.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity, iPCFourEyesActivity.liveList, true, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                        PagerLayoutManager pagerLayoutManager = new PagerLayoutManager(IPCFourEyesActivity.this.getActivity(), i) { // from class: activity.IPCFourEyesActivity.81.1
                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollHorizontally() {
                                return true;
                            }

                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollVertically() {
                                return false;
                            }
                        };
                        pagerLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.81.2
                            @Override // view.OnViewPagerListener
                            public void onInitComplete(View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageRelease(boolean z, int i3, View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageSelected(int i3, boolean z, View view2) {
                                Log.e("setOnViewPagerListener", "用户滑动完毕" + i3);
                                IPCFourEyesActivity.this.touchView.setVisibility(i3 == 2 ? 0 : 8);
                                IPCFourEyesActivity.this.showMode = i3;
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageDragging() {
                                Log.e("setOnViewPagerListener", "用户滑动中");
                            }
                        });
                        IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(pagerLayoutManager);
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                        IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
                    } else {
                        IPCFourEyesActivity.this.showMode = -1;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                        iPCFourEyesActivity2.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity2, iPCFourEyesActivity2.liveList, false, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new GridLayoutManager(IPCFourEyesActivity.this, 2));
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                    }
                } else {
                    if (IPCFourEyesActivity.this.showMode == -1) {
                        IPCFourEyesActivity.this.showMode = 2;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                        iPCFourEyesActivity3.liveAdapter = new LiveAdapter(iPCFourEyesActivity3, iPCFourEyesActivity3.liveList, true);
                        IPCFourEyesActivity iPCFourEyesActivity4 = IPCFourEyesActivity.this;
                        iPCFourEyesActivity4.linearLayoutManager = new PagerLayoutManager(iPCFourEyesActivity4.getActivity(), i2) { // from class: activity.IPCFourEyesActivity.81.3
                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollHorizontally() {
                                return false;
                            }

                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollVertically() {
                                return true;
                            }
                        };
                        IPCFourEyesActivity.this.linearLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.81.4
                            @Override // view.OnViewPagerListener
                            public void onInitComplete(View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageDragging() {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageRelease(boolean z, int i3, View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageSelected(int i3, boolean z, View view2) {
                                IPCFourEyesActivity.this.touchView.setVisibility(i3 == 2 ? 0 : 8);
                                IPCFourEyesActivity.this.showMode = i3;
                            }
                        });
                        IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(IPCFourEyesActivity.this.linearLayoutManager);
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveAdapter);
                        IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
                    } else {
                        IPCFourEyesActivity.this.showMode = -1;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity5 = IPCFourEyesActivity.this;
                        iPCFourEyesActivity5.liveFourAdapter = new LiveFourAdapter(iPCFourEyesActivity5, iPCFourEyesActivity5.liveList, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h, false);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new LinearLayoutManager(IPCFourEyesActivity.this));
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(IPCFourEyesActivity.this, 2);
                        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: activity.IPCFourEyesActivity.81.5
                            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                            public int getSpanSize(int i3) {
                                return IPCFourEyesActivity.this.liveFourAdapter.getItemViewType(i3) == 0 ? 1 : 2;
                            }
                        });
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(gridLayoutManager);
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveFourAdapter);
                    }
                    IPCFourEyesActivity.this.touchView.setVisibility(0);
                }
                Log.e("setOnViewPagerListener", "binding.playerBall双击");
                return true;
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCFourEyesActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playBall.setOnErrorListener(new AnonymousClass82());
        this.playBall.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCFourEyesActivity.83
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                Log.e(IPCFourEyesActivity.this.TAG, "playerState= " + i);
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCFourEyesActivity.this.dismissPlayButton();
                        IPCFourEyesActivity.this.showBuffering();
                        if (IPCFourEyesActivity.this.needWakeUp) {
                            IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        IPCFourEyesActivity.this.lowPowerMode = 1;
                        IPCFourEyesActivity.this.needWakeUp = false;
                        IPCFourEyesActivity.this.is1100ErrorPre = 10;
                        IPCFourEyesActivity.this.dismissSnapPicture();
                        IPCFourEyesActivity.this.dismissBuffering();
                        IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_READY");
                        IPCFourEyesActivity.this.updateInfoTv();
                        break;
                    case 4:
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_ENDED");
                        IPCFourEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                        IPCFourEyesActivity.this.playBall.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$82, reason: invalid class name */
    class AnonymousClass82 implements OnErrorListener {
        AnonymousClass82() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            Log.e(IPCFourEyesActivity.this.TAG, "exception= " + playerException.getLocalizedMessage());
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCFourEyesActivity.this.needWakeUp || IPCFourEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                        case 7:
                            IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                            iPCFourEyesActivity.showToast(iPCFourEyesActivity.getString(R.string.connect_failed));
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCFourEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCFourEyesActivity.this.showBadNetDialog();
                                    }
                                    IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                                    iPCFourEyesActivity2.showToast(iPCFourEyesActivity2.getResources().getString(R.string.play_failed_retry));
                                } else {
                                    IPCFourEyesActivity.access$7410(IPCFourEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCFourEyesActivity.this.ballDevice.getIotId());
                                    IPCFourEyesActivity.this.playBall.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCFourEyesActivity.this.ballDevice.getIotId()) != 3) {
                                        Handler handler = IPCFourEyesActivity.this.handler;
                                        final IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCFourEyesActivity$82$-yBFQEQHHdMS99YmhSNeL3KKA8s
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCFourEyesActivity3.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCFourEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCFourEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCFourEyesActivity iPCFourEyesActivity4 = IPCFourEyesActivity.this;
            iPCFourEyesActivity4.showToast(iPCFourEyesActivity4.getString(R.string.account_squeezed));
        }
    }

    private void initPlayerGun1() {
        this.playGun1 = new LivePlayer(getApplicationContext());
        this.playGun1.setTextureView(this.binding.playerGun1);
        this.binding.playerGun1.setClickable(true);
        this.playGun1.setVolume(this.binding.llListener.isSelected() ? 1.0f : 0.0f);
        this.playGun1.setVideoScalingMode(1);
        this.binding.playerGun1.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCFourEyesActivity.84
            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            @SuppressLint({"SetTextI18n"})
            public void onScaleChanged(ZoomableTextureView zoomableTextureView, float f) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                int i = 0;
                int i2 = 1;
                if (IPCFourEyesActivity.this.isHorizontal) {
                    if (IPCFourEyesActivity.this.showMode == -1) {
                        IPCFourEyesActivity.this.showMode = 0;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                        iPCFourEyesActivity.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity, iPCFourEyesActivity.liveList, true, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                        PagerLayoutManager pagerLayoutManager = new PagerLayoutManager(IPCFourEyesActivity.this.getActivity(), i) { // from class: activity.IPCFourEyesActivity.84.1
                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollHorizontally() {
                                return true;
                            }

                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollVertically() {
                                return false;
                            }
                        };
                        pagerLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.84.2
                            @Override // view.OnViewPagerListener
                            public void onInitComplete(View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageRelease(boolean z, int i3, View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageSelected(int i3, boolean z, View view2) {
                                Log.e("setOnViewPagerListener", "用户滑动完毕" + i3);
                                IPCFourEyesActivity.this.touchView.setVisibility(i3 == 2 ? 0 : 8);
                                IPCFourEyesActivity.this.showMode = i3;
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageDragging() {
                                Log.e("setOnViewPagerListener", "用户滑动中");
                            }
                        });
                        IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(pagerLayoutManager);
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                        IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
                    } else {
                        IPCFourEyesActivity.this.showMode = -1;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                        iPCFourEyesActivity2.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity2, iPCFourEyesActivity2.liveList, false, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new GridLayoutManager(IPCFourEyesActivity.this, 2));
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                    }
                } else if (IPCFourEyesActivity.this.showMode == -1) {
                    IPCFourEyesActivity.this.showMode = 0;
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity3.liveAdapter = new LiveAdapter(iPCFourEyesActivity3, iPCFourEyesActivity3.liveList, true);
                    IPCFourEyesActivity iPCFourEyesActivity4 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity4.linearLayoutManager = new PagerLayoutManager(iPCFourEyesActivity4.getActivity(), i2) { // from class: activity.IPCFourEyesActivity.84.3
                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollHorizontally() {
                            return false;
                        }

                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollVertically() {
                            return true;
                        }
                    };
                    IPCFourEyesActivity.this.linearLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.84.4
                        @Override // view.OnViewPagerListener
                        public void onInitComplete(View view2) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageDragging() {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageRelease(boolean z, int i3, View view2) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageSelected(int i3, boolean z, View view2) {
                            IPCFourEyesActivity.this.touchView.setVisibility(i3 == 2 ? 0 : 8);
                            IPCFourEyesActivity.this.showMode = i3;
                        }
                    });
                    IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(IPCFourEyesActivity.this.linearLayoutManager);
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveAdapter);
                    IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
                    IPCFourEyesActivity.this.touchView.setVisibility(8);
                } else {
                    IPCFourEyesActivity.this.showMode = -1;
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity5 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity5.liveFourAdapter = new LiveFourAdapter(iPCFourEyesActivity5, iPCFourEyesActivity5.liveList, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h, false);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new LinearLayoutManager(IPCFourEyesActivity.this));
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(IPCFourEyesActivity.this, 2);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: activity.IPCFourEyesActivity.84.5
                        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                        public int getSpanSize(int i3) {
                            return IPCFourEyesActivity.this.liveFourAdapter.getItemViewType(i3) == 0 ? 1 : 2;
                        }
                    });
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(gridLayoutManager);
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveFourAdapter);
                }
                return true;
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCFourEyesActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playGun1.setOnErrorListener(new AnonymousClass85());
        this.playGun1.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCFourEyesActivity.86
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                Log.e(IPCFourEyesActivity.this.TAG, "playerState= " + i);
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCFourEyesActivity.this.dismissPlayButton();
                        IPCFourEyesActivity.this.showBuffering();
                        if (IPCFourEyesActivity.this.needWakeUp) {
                            IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        IPCFourEyesActivity.this.lowPowerMode = 1;
                        IPCFourEyesActivity.this.needWakeUp = false;
                        IPCFourEyesActivity.this.is1100ErrorPre = 10;
                        IPCFourEyesActivity.this.dismissSnapPicture();
                        IPCFourEyesActivity.this.dismissBuffering();
                        IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_READY");
                        IPCFourEyesActivity.this.updateInfoTv();
                        break;
                    case 4:
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_ENDED");
                        IPCFourEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                        IPCFourEyesActivity.this.playGun1.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$85, reason: invalid class name */
    class AnonymousClass85 implements OnErrorListener {
        AnonymousClass85() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            Log.e(IPCFourEyesActivity.this.TAG, "exception= " + playerException.getLocalizedMessage());
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCFourEyesActivity.this.needWakeUp || IPCFourEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                        case 7:
                            IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                            iPCFourEyesActivity.showToast(iPCFourEyesActivity.getString(R.string.play_failed_retry));
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCFourEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCFourEyesActivity.this.showBadNetDialog();
                                    }
                                    IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                                    iPCFourEyesActivity2.showToast(iPCFourEyesActivity2.getString(R.string.play_failed_retry));
                                } else {
                                    IPCFourEyesActivity.access$7410(IPCFourEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCFourEyesActivity.this.gunDevice1.getIotId());
                                    IPCFourEyesActivity.this.playGun1.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCFourEyesActivity.this.ballDevice.getIotId()) != 3) {
                                        Handler handler = IPCFourEyesActivity.this.handler;
                                        final IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCFourEyesActivity$85$hln50bNUIgvosJ9f7iRwW2b6poI
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCFourEyesActivity3.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCFourEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCFourEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCFourEyesActivity iPCFourEyesActivity4 = IPCFourEyesActivity.this;
            iPCFourEyesActivity4.showToast(iPCFourEyesActivity4.getString(R.string.account_squeezed));
        }
    }

    private void initPlayerGun2() {
        this.playGun2 = new LivePlayer(getApplicationContext());
        this.playGun2.setTextureView(this.binding.playerGun2);
        this.binding.playerGun2.setClickable(true);
        this.playGun2.setVolume(this.binding.llListener.isSelected() ? 1.0f : 0.0f);
        this.playGun2.setVideoScalingMode(1);
        this.binding.playerGun2.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCFourEyesActivity.87
            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            @SuppressLint({"SetTextI18n"})
            public void onScaleChanged(ZoomableTextureView zoomableTextureView, float f) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                int i = 1;
                if (IPCFourEyesActivity.this.isHorizontal) {
                    if (IPCFourEyesActivity.this.showMode == -1) {
                        IPCFourEyesActivity.this.showMode = 3;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                        iPCFourEyesActivity.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity, iPCFourEyesActivity.liveList, true, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                        PagerLayoutManager pagerLayoutManager = new PagerLayoutManager(IPCFourEyesActivity.this.getActivity(), 0) { // from class: activity.IPCFourEyesActivity.87.1
                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollHorizontally() {
                                return true;
                            }

                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollVertically() {
                                return false;
                            }
                        };
                        pagerLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.87.2
                            @Override // view.OnViewPagerListener
                            public void onInitComplete(View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageRelease(boolean z, int i2, View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageSelected(int i2, boolean z, View view2) {
                                Log.e("setOnViewPagerListener", "用户滑动完毕" + i2);
                                IPCFourEyesActivity.this.touchView.setVisibility(i2 == 2 ? 0 : 8);
                                IPCFourEyesActivity.this.showMode = i2;
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageDragging() {
                                Log.e("setOnViewPagerListener", "用户滑动中");
                            }
                        });
                        IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(pagerLayoutManager);
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                        IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
                    } else {
                        IPCFourEyesActivity.this.showMode = -1;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                        iPCFourEyesActivity2.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity2, iPCFourEyesActivity2.liveList, false, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new GridLayoutManager(IPCFourEyesActivity.this, 2));
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                    }
                } else if (IPCFourEyesActivity.this.showMode == -1) {
                    IPCFourEyesActivity.this.showMode = 3;
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity3.liveAdapter = new LiveAdapter(iPCFourEyesActivity3, iPCFourEyesActivity3.liveList, true);
                    IPCFourEyesActivity iPCFourEyesActivity4 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity4.linearLayoutManager = new PagerLayoutManager(iPCFourEyesActivity4.getActivity(), i) { // from class: activity.IPCFourEyesActivity.87.3
                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollHorizontally() {
                            return false;
                        }

                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollVertically() {
                            return true;
                        }
                    };
                    IPCFourEyesActivity.this.linearLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.87.4
                        @Override // view.OnViewPagerListener
                        public void onInitComplete(View view2) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageDragging() {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageRelease(boolean z, int i2, View view2) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageSelected(int i2, boolean z, View view2) {
                            IPCFourEyesActivity.this.touchView.setVisibility(i2 == 2 ? 0 : 8);
                            IPCFourEyesActivity.this.showMode = i2;
                        }
                    });
                    IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(IPCFourEyesActivity.this.linearLayoutManager);
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveAdapter);
                    IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
                    IPCFourEyesActivity.this.touchView.setVisibility(8);
                } else {
                    IPCFourEyesActivity.this.showMode = -1;
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity5 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity5.liveFourAdapter = new LiveFourAdapter(iPCFourEyesActivity5, iPCFourEyesActivity5.liveList, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h, false);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new LinearLayoutManager(IPCFourEyesActivity.this));
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(IPCFourEyesActivity.this, 2);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: activity.IPCFourEyesActivity.87.5
                        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                        public int getSpanSize(int i2) {
                            return IPCFourEyesActivity.this.liveFourAdapter.getItemViewType(i2) == 0 ? 1 : 2;
                        }
                    });
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(gridLayoutManager);
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveFourAdapter);
                }
                return true;
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCFourEyesActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playGun2.setOnErrorListener(new AnonymousClass88());
        this.playGun2.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCFourEyesActivity.89
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                Log.e(IPCFourEyesActivity.this.TAG, "playerState= " + i);
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCFourEyesActivity.this.dismissPlayButton();
                        IPCFourEyesActivity.this.showBuffering();
                        if (IPCFourEyesActivity.this.needWakeUp) {
                            IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        IPCFourEyesActivity.this.lowPowerMode = 1;
                        IPCFourEyesActivity.this.needWakeUp = false;
                        IPCFourEyesActivity.this.is1100ErrorPre = 10;
                        IPCFourEyesActivity.this.dismissSnapPicture();
                        IPCFourEyesActivity.this.dismissBuffering();
                        IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_READY");
                        IPCFourEyesActivity.this.updateInfoTv();
                        break;
                    case 4:
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_ENDED");
                        IPCFourEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                        IPCFourEyesActivity.this.playGun1.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$88, reason: invalid class name */
    class AnonymousClass88 implements OnErrorListener {
        AnonymousClass88() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            Log.e(IPCFourEyesActivity.this.TAG, "exception= " + playerException.getLocalizedMessage());
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCFourEyesActivity.this.needWakeUp || IPCFourEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                        case 7:
                            IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                            iPCFourEyesActivity.showToast(iPCFourEyesActivity.getString(R.string.play_failed_retry));
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCFourEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCFourEyesActivity.this.showBadNetDialog();
                                    }
                                    IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                                    iPCFourEyesActivity2.showToast(iPCFourEyesActivity2.getString(R.string.play_failed_retry));
                                } else {
                                    IPCFourEyesActivity.access$7410(IPCFourEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCFourEyesActivity.this.gunDevice2.getIotId());
                                    IPCFourEyesActivity.this.playGun2.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCFourEyesActivity.this.ballDevice.getIotId()) != 3) {
                                        Handler handler = IPCFourEyesActivity.this.handler;
                                        final IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCFourEyesActivity$88$K9gxOIlWeuzNy5c-41vvuWhCGLM
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCFourEyesActivity3.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCFourEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCFourEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCFourEyesActivity iPCFourEyesActivity4 = IPCFourEyesActivity.this;
            iPCFourEyesActivity4.showToast(iPCFourEyesActivity4.getString(R.string.account_squeezed));
        }
    }

    private void initPlayerGun3() {
        this.playGun3 = new LivePlayer(getApplicationContext());
        this.playGun3.setTextureView(this.binding.playerGun3);
        this.binding.playerGun2.setClickable(true);
        this.playGun3.setVolume(this.binding.llListener.isSelected() ? 1.0f : 0.0f);
        this.playGun3.setVideoScalingMode(1);
        this.binding.playerGun3.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCFourEyesActivity.90
            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            @SuppressLint({"SetTextI18n"})
            public void onScaleChanged(ZoomableTextureView zoomableTextureView, float f) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                int i = 1;
                if (IPCFourEyesActivity.this.isHorizontal) {
                    if (IPCFourEyesActivity.this.showMode == -1) {
                        IPCFourEyesActivity.this.showMode = 1;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                        iPCFourEyesActivity.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity, iPCFourEyesActivity.liveList, true, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                        PagerLayoutManager pagerLayoutManager = new PagerLayoutManager(IPCFourEyesActivity.this.getActivity(), 0) { // from class: activity.IPCFourEyesActivity.90.1
                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollHorizontally() {
                                return true;
                            }

                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public boolean canScrollVertically() {
                                return false;
                            }
                        };
                        pagerLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.90.2
                            @Override // view.OnViewPagerListener
                            public void onInitComplete(View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageRelease(boolean z, int i2, View view2) {
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageSelected(int i2, boolean z, View view2) {
                                Log.e("setOnViewPagerListener", "用户滑动完毕" + i2);
                                IPCFourEyesActivity.this.touchView.setVisibility(i2 == 2 ? 0 : 8);
                                IPCFourEyesActivity.this.showMode = i2;
                            }

                            @Override // view.OnViewPagerListener
                            public void onPageDragging() {
                                Log.e("setOnViewPagerListener", "用户滑动中");
                            }
                        });
                        IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                        IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(pagerLayoutManager);
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                        IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
                    } else {
                        IPCFourEyesActivity.this.showMode = -1;
                        if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                        }
                        if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                            ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                        }
                        IPCFourEyesActivity.this.liveList.clear();
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                        IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                        IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                        iPCFourEyesActivity2.liveHorizontalAdapter = new LiveHorizontalAdapter(iPCFourEyesActivity2, iPCFourEyesActivity2.liveList, false, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h);
                        IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new GridLayoutManager(IPCFourEyesActivity.this, 2));
                        IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveHorizontalAdapter);
                    }
                } else if (IPCFourEyesActivity.this.showMode == -1) {
                    IPCFourEyesActivity.this.showMode = 1;
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity3.liveAdapter = new LiveAdapter(iPCFourEyesActivity3, iPCFourEyesActivity3.liveList, true);
                    IPCFourEyesActivity iPCFourEyesActivity4 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity4.linearLayoutManager = new PagerLayoutManager(iPCFourEyesActivity4.getActivity(), i) { // from class: activity.IPCFourEyesActivity.90.3
                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollHorizontally() {
                            return false;
                        }

                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean canScrollVertically() {
                            return true;
                        }
                    };
                    IPCFourEyesActivity.this.linearLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: activity.IPCFourEyesActivity.90.4
                        @Override // view.OnViewPagerListener
                        public void onInitComplete(View view2) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageDragging() {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageRelease(boolean z, int i2, View view2) {
                        }

                        @Override // view.OnViewPagerListener
                        public void onPageSelected(int i2, boolean z, View view2) {
                            IPCFourEyesActivity.this.touchView.setVisibility(i2 == 2 ? 0 : 8);
                            IPCFourEyesActivity.this.showMode = i2;
                        }
                    });
                    IPCFourEyesActivity.this.binding.rvLive.setItemViewCacheSize(20);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheEnabled(true);
                    IPCFourEyesActivity.this.binding.rvLive.setDrawingCacheQuality(1048576);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(IPCFourEyesActivity.this.linearLayoutManager);
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveAdapter);
                    IPCFourEyesActivity.this.binding.rvLive.getLayoutManager().scrollToPosition(IPCFourEyesActivity.this.showMode);
                    IPCFourEyesActivity.this.binding.layoutUpDown.setVisibility(8);
                } else {
                    IPCFourEyesActivity.this.showMode = -1;
                    if (IPCFourEyesActivity.this.binding.playerBall.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerBall.getParent()).removeView(IPCFourEyesActivity.this.binding.playerBall);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun1.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun1.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun1);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun2.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun2.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun2);
                    }
                    if (IPCFourEyesActivity.this.binding.playerGun3.getParent() != null) {
                        ((ViewGroup) IPCFourEyesActivity.this.binding.playerGun3.getParent()).removeView(IPCFourEyesActivity.this.binding.playerGun3);
                    }
                    IPCFourEyesActivity.this.liveList.clear();
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun1);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun3);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerBall);
                    IPCFourEyesActivity.this.liveList.add(IPCFourEyesActivity.this.binding.playerGun2);
                    IPCFourEyesActivity iPCFourEyesActivity5 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity5.liveFourAdapter = new LiveFourAdapter(iPCFourEyesActivity5, iPCFourEyesActivity5.liveList, IPCFourEyesActivity.this.w, IPCFourEyesActivity.this.h, false);
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(new LinearLayoutManager(IPCFourEyesActivity.this));
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(IPCFourEyesActivity.this, 2);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: activity.IPCFourEyesActivity.90.5
                        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                        public int getSpanSize(int i2) {
                            return IPCFourEyesActivity.this.liveFourAdapter.getItemViewType(i2) == 0 ? 1 : 2;
                        }
                    });
                    IPCFourEyesActivity.this.binding.rvLive.setLayoutManager(gridLayoutManager);
                    IPCFourEyesActivity.this.binding.rvLive.setAdapter(IPCFourEyesActivity.this.liveFourAdapter);
                }
                return true;
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCFourEyesActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playGun3.setOnErrorListener(new AnonymousClass91());
        this.playGun3.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCFourEyesActivity.92
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                Log.e(IPCFourEyesActivity.this.TAG, "playerState= " + i);
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCFourEyesActivity.this.dismissPlayButton();
                        IPCFourEyesActivity.this.showBuffering();
                        if (IPCFourEyesActivity.this.needWakeUp) {
                            IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        IPCFourEyesActivity.this.lowPowerMode = 1;
                        IPCFourEyesActivity.this.needWakeUp = false;
                        IPCFourEyesActivity.this.is1100ErrorPre = 10;
                        IPCFourEyesActivity.this.dismissSnapPicture();
                        IPCFourEyesActivity.this.dismissBuffering();
                        IPCFourEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_READY");
                        IPCFourEyesActivity.this.updateInfoTv();
                        break;
                    case 4:
                        LogEx.i(true, IPCFourEyesActivity.this.TAG, "STATE_ENDED");
                        IPCFourEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                        IPCFourEyesActivity.this.playGun1.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$91, reason: invalid class name */
    class AnonymousClass91 implements OnErrorListener {
        AnonymousClass91() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            Log.e(IPCFourEyesActivity.this.TAG, "exception= " + playerException.getLocalizedMessage());
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCFourEyesActivity.this.needWakeUp || IPCFourEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                        case 7:
                            IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                            iPCFourEyesActivity.showToast(iPCFourEyesActivity.getString(R.string.play_failed_retry));
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCFourEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCFourEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCFourEyesActivity.this.showBadNetDialog();
                                    }
                                    IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                                    iPCFourEyesActivity2.showToast(iPCFourEyesActivity2.getString(R.string.play_failed_retry));
                                } else {
                                    IPCFourEyesActivity.access$7410(IPCFourEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCFourEyesActivity.this.gunDevice2.getIotId());
                                    IPCFourEyesActivity.this.playGun3.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCFourEyesActivity.this.ballDevice.getIotId()) != 3) {
                                        Handler handler = IPCFourEyesActivity.this.handler;
                                        final IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCFourEyesActivity$91$cTnKWmquNP-sg0QJSESIPpTrbEs
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCFourEyesActivity3.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCFourEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCFourEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCFourEyesActivity iPCFourEyesActivity4 = IPCFourEyesActivity.this;
            iPCFourEyesActivity4.showToast(iPCFourEyesActivity4.getString(R.string.account_squeezed));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playLive() {
        if (isFinishing()) {
            return;
        }
        if (this.playBall.getPlayState() != 3) {
            this.binding.playerBall.reset();
            showSnapPicture();
        }
        if (this.playGun1.getPlayState() != 3) {
            this.binding.playerGun1.reset();
            showSnapPicture();
        }
        if (this.playGun2.getPlayState() != 3) {
            this.binding.playerGun2.reset();
            showSnapPicture();
        }
        if (this.playGun3.getPlayState() != 3) {
            this.binding.playerGun3.reset();
            showSnapPicture();
        }
        LogEx.i(true, this.TAG, "playLive");
        this.playBall.stop();
        this.playBall.setIPCLiveDataSource(this.ballDevice.getIotId(), 0, false, 0, true, 0);
        this.playBall.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCFourEyesActivity.93
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCFourEyesActivity.this.playBall.start();
            }
        });
        this.playBall.prepare();
        this.playGun1.stop();
        Log.d(this.TAG, "playLive: puppet:iotId1====" + this.gunDevice1.getIotId());
        this.playGun1.setIPCLiveDataSource(this.gunDevice1.getIotId(), 0, false, 0, true, 0);
        this.playGun1.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCFourEyesActivity.94
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCFourEyesActivity.this.playGun1.start();
            }
        });
        this.playGun1.prepare();
        this.playGun2.stop();
        Log.d(this.TAG, "playLive: puppet:iotId1====" + this.gunDevice2.getIotId());
        this.playGun2.setIPCLiveDataSource(this.gunDevice2.getIotId(), 0, false, 0, true, 0);
        this.playGun2.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCFourEyesActivity.95
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCFourEyesActivity.this.playGun2.start();
            }
        });
        this.playGun2.prepare();
        this.playGun3.stop();
        Log.d(this.TAG, "playLive: puppet:iotId1====" + this.gunDevice2.getIotId());
        this.playGun3.setIPCLiveDataSource(this.gunDevice3.getIotId(), 0, false, 0, true, 0);
        this.playGun3.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCFourEyesActivity.96
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCFourEyesActivity.this.playGun3.start();
            }
        });
        this.playGun3.prepare();
    }

    private void initLiveIntercom() {
        this.liveIntercom = new LiveIntercomV2(this, this.ballDevice.getIotId(), LiveIntercomV2.LiveIntercomMode.SingleTalk, AudioParams.AUDIOPARAM_MONO_8K_G711A);
        this.liveIntercom.setGainLevel(-1);
        this.liveIntercom.setLiveIntercomV2Listener(new LiveIntercomV2Listener() { // from class: activity.IPCFourEyesActivity.97
            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordBufferReceived(byte[] bArr, int i, int i2) {
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onTalkReady() {
                IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.97.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCFourEyesActivity.this.showToast(IPCFourEyesActivity.this.getResources().getString(R.string.can_begin_talk));
                        if (IPCFourEyesActivity.this.isFinishing()) {
                            return;
                        }
                        IPCFourEyesActivity.this.whiteProgressDialog.dismiss();
                        IPCFourEyesActivity.this.binding.speakerBtn.setSelected(true);
                        IPCFourEyesActivity.this.binding.fullIntercom.setSelected(IPCFourEyesActivity.this.binding.speakerBtn.isSelected());
                        IPCFourEyesActivity.this.binding.llListener.setSelected(true);
                        IPCFourEyesActivity.this.playBall.setVolume(IPCFourEyesActivity.this.binding.llListener.isSelected() ? 1.0f : 0.0f);
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onError(LiveIntercomException liveIntercomException) {
                int code = liveIntercomException.getCode();
                if (code != 16) {
                    switch (code) {
                        case 1:
                            IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                            iPCFourEyesActivity.showToast(iPCFourEyesActivity.getString(R.string.record_error1));
                            IPCFourEyesActivity.this.handleLiveIntercomError();
                            break;
                        case 2:
                            IPCFourEyesActivity iPCFourEyesActivity2 = IPCFourEyesActivity.this;
                            iPCFourEyesActivity2.showToast(iPCFourEyesActivity2.getString(R.string.record_error2));
                            IPCFourEyesActivity.this.handleLiveIntercomError();
                            break;
                        case 3:
                            IPCFourEyesActivity iPCFourEyesActivity3 = IPCFourEyesActivity.this;
                            iPCFourEyesActivity3.showToast(iPCFourEyesActivity3.getString(R.string.record_error3));
                            IPCFourEyesActivity.this.handleLiveIntercomError();
                            break;
                        default:
                            switch (code) {
                                case 5:
                                    IPCFourEyesActivity iPCFourEyesActivity4 = IPCFourEyesActivity.this;
                                    iPCFourEyesActivity4.showToast(iPCFourEyesActivity4.getString(R.string.record_error4));
                                    IPCFourEyesActivity.this.handleLiveIntercomError();
                                    break;
                                case 6:
                                    IPCFourEyesActivity iPCFourEyesActivity5 = IPCFourEyesActivity.this;
                                    iPCFourEyesActivity5.showToast(iPCFourEyesActivity5.getString(R.string.record_error5));
                                    IPCFourEyesActivity.this.handleLiveIntercomError();
                                    break;
                                case 7:
                                    IPCFourEyesActivity iPCFourEyesActivity6 = IPCFourEyesActivity.this;
                                    iPCFourEyesActivity6.showToast(iPCFourEyesActivity6.getString(R.string.record_error6));
                                    IPCFourEyesActivity.this.onRecordError();
                                    break;
                                case 8:
                                    IPCFourEyesActivity iPCFourEyesActivity7 = IPCFourEyesActivity.this;
                                    iPCFourEyesActivity7.showToast(iPCFourEyesActivity7.getString(R.string.record_error7));
                                    IPCFourEyesActivity.this.onRecordError();
                                    break;
                                case 9:
                                    IPCFourEyesActivity iPCFourEyesActivity8 = IPCFourEyesActivity.this;
                                    iPCFourEyesActivity8.showToast(iPCFourEyesActivity8.getString(R.string.record_error8));
                                    IPCFourEyesActivity.this.onRecordError();
                                    break;
                            }
                            break;
                    }
                } else {
                    IPCFourEyesActivity iPCFourEyesActivity9 = IPCFourEyesActivity.this;
                    iPCFourEyesActivity9.showToast(iPCFourEyesActivity9.getString(R.string.record_error9));
                    IPCFourEyesActivity.this.onRecordError();
                }
                liveIntercomException.printStackTrace();
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordStart() {
                LogEx.d(true, IPCFourEyesActivity.this.TAG, "onRecordStart");
                IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.97.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCFourEyesActivity.this.binding.speakerBtn.setSelected(true);
                        IPCFourEyesActivity.this.binding.fullIntercom.setSelected(IPCFourEyesActivity.this.binding.speakerBtn.isSelected());
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordEnd() {
                LogEx.d(true, IPCFourEyesActivity.this.TAG, "onRecordEnd");
                IPCFourEyesActivity.this.liveIntercom.stop();
                IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.97.3
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCFourEyesActivity.this.binding.speakerBtn.setSelected(false);
                        IPCFourEyesActivity.this.binding.fullIntercom.setSelected(IPCFourEyesActivity.this.binding.speakerBtn.isSelected());
                        if (IPCFourEyesActivity.this.isFinishing()) {
                            return;
                        }
                        IPCFourEyesActivity.this.whiteProgressDialog.dismiss();
                    }
                });
            }
        });
    }

    private void showSnapPicture() {
        SpUtil.getString(getActivity(), Utils.getDevSnapKey(this.ballDevice.getIotId()), "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addControlTouchView() {
        if (this.touchView == null) {
            this.touchView = new TouchView(getActivity());
        }
        this.binding.rlTouchView.getViewTreeObserver().addOnGlobalLayoutListener(this.nGlobalLayoutListener);
    }

    /* JADX INFO: renamed from: activity.IPCFourEyesActivity$98, reason: invalid class name */
    class AnonymousClass98 implements ViewTreeObserver.OnGlobalLayoutListener {
        AnonymousClass98() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (IPCFourEyesActivity.this.touchView.getParent() != null) {
                ((ViewGroup) IPCFourEyesActivity.this.touchView.getParent()).removeView(IPCFourEyesActivity.this.touchView);
            }
            if (!IPCFourEyesActivity.this.isLand) {
                if (IPCFourEyesActivity.this.binding.rlTouchView.getHeight() == 0) {
                    return;
                }
                IPCFourEyesActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad3);
                int dimensionPixelSize = IPCFourEyesActivity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                Log.e("屏幕", "" + IPCFourEyesActivity.this.isRatio);
                if (IPCFourEyesActivity.this.isRatio) {
                    IPCFourEyesActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCFourEyesActivity.this.getActivity(), 115.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                } else {
                    IPCFourEyesActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCFourEyesActivity.this.getActivity(), 115.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                }
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(13, -1);
                IPCFourEyesActivity.this.touchView.setLayoutParams(layoutParams);
                IPCFourEyesActivity.this.binding.rlTouchView.addView(IPCFourEyesActivity.this.touchView);
            } else {
                IPCFourEyesActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad3);
                int dimensionPixelSize2 = IPCFourEyesActivity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                IPCFourEyesActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCFourEyesActivity.this.getActivity(), 115.0f) + (dimensionPixelSize2 * 2), dimensionPixelSize2);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams2.addRule(12, -1);
                IPCFourEyesActivity.this.touchView.setLayoutParams(layoutParams2);
                IPCFourEyesActivity.this.binding.fullScreen.addView(IPCFourEyesActivity.this.touchView);
            }
            IPCFourEyesActivity.this.touchView.setListener(new JoystickTouchViewListener() { // from class: activity.IPCFourEyesActivity.98.1
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
                        Method dump skipped, instruction units count: 261
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: activity.IPCFourEyesActivity.AnonymousClass98.AnonymousClass1.onTouch(float, float):void");
                }

                @Override // view.JoystickTouchViewListener
                public void onReset() {
                    if (IPCFourEyesActivity.this.ptzTimer != null) {
                        IPCFourEyesActivity.this.ptzTimer.cancel();
                        IPCFourEyesActivity.this.ptzTimer = null;
                    }
                    IPCFourEyesActivity.this.lastActionTypeEnum = null;
                }

                @Override // view.JoystickTouchViewListener
                public void onActionUp() {
                    Log.e("云台", "抬起");
                    IPCFourEyesActivity.this.touchView.resetView();
                    if (IPCFourEyesActivity.this.ptzTimer != null) {
                        IPCFourEyesActivity.this.ptzTimer.cancel();
                        IPCFourEyesActivity.this.ptzTimer = null;
                    }
                    IPCFourEyesActivity.this.lastActionTypeEnum = null;
                }
            });
            IPCFourEyesActivity.this.binding.rlTouchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    public void startPTZEx(ActionTypeEnum actionTypeEnum, SpeedEnum speedEnum) {
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).startPTZEx(actionTypeEnum.getCode(), speedEnum.getCode(), new IPanelCallback() { // from class: activity.IPCFourEyesActivity.99
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                String str = IPCFourEyesActivity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startPTZExControl:");
                sb.append(z);
                sb.append("       o:");
                sb.append(obj != null ? String.valueOf(obj) : TmpConstant.GROUP_ROLE_UNKNOWN);
                LogEx.e(true, str, sb.toString());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switch4gMode(String str, int i) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.Net4GEnableSwitch))) {
            map.put(Constants.Net4GEnableSwitch, Integer.valueOf(i));
        }
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.100
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        object.getInteger("code").intValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
    public void snapshot() {
        Bitmap bitmapSnapShot;
        Bitmap bitmapSnapShot2;
        Bitmap bitmapSnapShot3;
        verifyStoragePermissions(this);
        if (this.playBall.getPlayState() != 3) {
            Toast.makeText(getActivity(), R.string.only_play_snap, 0).show();
            return;
        }
        LivePlayer livePlayer = this.playGun1;
        if (livePlayer != null && (bitmapSnapShot3 = livePlayer.snapShot()) != null) {
            Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapSnapShot3, 2560, 1440, true);
            if (bitmapCreateScaledBitmap == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            }
            if (bitmapCreateScaledBitmap != null) {
                scanFile(bitmapCreateScaledBitmap);
                if (Build.VERSION.SDK_INT >= 29) {
                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapCreateScaledBitmap, "IMG" + Calendar.getInstance().getTime(), (String) null);
                } else {
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmapCreateScaledBitmap, "", "");
                }
            }
        }
        LivePlayer livePlayer2 = this.playGun2;
        if (livePlayer2 != null && (bitmapSnapShot2 = livePlayer2.snapShot()) != null) {
            Bitmap bitmapCreateScaledBitmap2 = Bitmap.createScaledBitmap(bitmapSnapShot2, 2560, 1440, true);
            if (bitmapCreateScaledBitmap2 == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            }
            scanFile(bitmapCreateScaledBitmap2);
            if (Build.VERSION.SDK_INT >= 29) {
                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapCreateScaledBitmap2, "IMG" + Calendar.getInstance().getTime(), (String) null);
            } else {
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmapCreateScaledBitmap2, "", "");
            }
        }
        LivePlayer livePlayer3 = this.playGun3;
        if (livePlayer3 != null && (bitmapSnapShot = livePlayer3.snapShot()) != null) {
            Bitmap bitmapCreateScaledBitmap3 = Bitmap.createScaledBitmap(bitmapSnapShot, 2560, 1440, true);
            if (bitmapCreateScaledBitmap3 == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            }
            scanFile(bitmapCreateScaledBitmap3);
            if (Build.VERSION.SDK_INT >= 29) {
                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapCreateScaledBitmap3, "IMG" + Calendar.getInstance().getTime(), (String) null);
            } else {
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmapCreateScaledBitmap3, "", "");
            }
        }
        LivePlayer livePlayer4 = this.playBall;
        if (livePlayer4 != null) {
            Bitmap bitmapCreateScaledBitmap4 = Bitmap.createScaledBitmap(livePlayer4.snapShot(), 2560, 1440, true);
            if (bitmapCreateScaledBitmap4 == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            } else if (bitmapCreateScaledBitmap4 != null) {
                scanFile(bitmapCreateScaledBitmap4);
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmapCreateScaledBitmap4, "", "");
            }
        }
        showToast(getResources().getString(R.string.camera_check));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOrStopRecordingMp4() {
        Resources resources;
        int i;
        Resources resources2;
        int i2;
        Resources resources3;
        int i3;
        if (!this.isRecording) {
            this.isRecording = true;
            File file = new File(getFilesPath(this) + "/video/");
            if (!file.exists() && !file.mkdirs()) {
                return;
            }
            try {
                this.playGun1.startRecordingContent(new File(file, (System.currentTimeMillis() + 200) + ".mp4"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.playGun2.startRecordingContent(new File(file, (System.currentTimeMillis() + 100) + ".mp4"));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                this.playGun3.startRecordingContent(new File(file, (System.currentTimeMillis() + 100) + ".mp4"));
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            try {
                this.playBall.startRecordingContent(new File(file, System.currentTimeMillis() + ".mp4"));
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            showTimer();
        } else {
            this.isRecording = false;
            this.playBall.stopRecordingContent();
            this.playGun1.stopRecordingContent();
            this.playGun2.stopRecordingContent();
            this.playGun3.stopRecordingContent();
            hideTimer();
        }
        ShadowButton shadowButton = this.binding.fullVideo;
        if (this.isRecording) {
            resources = getResources();
            i = R.drawable.full_video_;
        } else {
            resources = getResources();
            i = R.drawable.full_video;
        }
        shadowButton.setBackground(resources.getDrawable(i));
        ImageButton imageButton = this.binding.recordBtn;
        if (this.isRecording) {
            resources2 = getResources();
            i2 = R.drawable.video_video_light;
        } else {
            resources2 = getResources();
            i2 = R.drawable.video_video;
        }
        imageButton.setImageDrawable(resources2.getDrawable(i2));
        TextView textView = this.binding.tvRecord;
        if (this.isRecording) {
            resources3 = getResources();
            i3 = R.color.colorAccent;
        } else {
            resources3 = getResources();
            i3 = R.color.colors_ipc_image_text;
        }
        textView.setTextColor(resources3.getColor(i3));
    }

    private void showTimer() {
        if (this.timer == null) {
            this.timer = new Timer();
        }
        this.binding.timer.setVisibility(0);
        this.binding.timer.bringToFront();
        this.timer.schedule(new TimerTask() { // from class: activity.IPCFourEyesActivity.101
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.101.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCFourEyesActivity.this.binding.timer.setText(IPCFourEyesActivity.this.transformTime(IPCFourEyesActivity.this.i));
                    }
                });
                IPCFourEyesActivity.this.i++;
            }
        }, 0L, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeZoom(int i) {
        if (SharePreferenceManager.getInstance().getNewSupportEZOOM(this.ballDevice.getIotId()) == 1) {
            changechangeEZoom(i);
            return;
        }
        if (SharePreferenceManager.getInstance().getMixZoom(this.ballDevice.getIotId()) == 1) {
            if (i == 1 && this.ZoomIsMax && this.binding.playerBall.getScale() < this.binding.playerBall.getMaxScale()) {
                this.binding.playerBall.addZoom();
                return;
            } else if (i == 0 && this.binding.playerBall.getScale() > 1.0f) {
                this.binding.playerBall.reduceZoom();
                return;
            } else {
                changeOpticalZoom(i);
                return;
            }
        }
        if (SharePreferenceManager.getInstance().getSupportZoom(this.ballDevice.getIotId()) != 1) {
            if (i == 1) {
                this.binding.playerBall.addZoom();
                return;
            } else {
                this.binding.playerBall.reduceZoom();
                return;
            }
        }
        if (i == 1) {
            changeOpticalZoom(1);
        } else if (i == 0) {
            changeOpticalZoom(0);
        }
    }

    private void changechangeEZoom(int i) {
        long jUptimeMillis = SystemClock.uptimeMillis();
        if (jUptimeMillis - this.lastOnclickTime >= 200) {
            this.lastOnclickTime = jUptimeMillis;
            IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).changeEZoom(i, 0, SharePreferenceManager.getInstance().getStreamVideoQuality(this.ballDevice.getIotId()) == 2 ? 0 : 1, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.102
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        final JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCFourEyesActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCFourEyesActivity iPCFourEyesActivity = IPCFourEyesActivity.this;
                        boolean z2 = true;
                        if (object.getInteger("ZoomIsMax").intValue() != 1) {
                            z2 = false;
                        }
                        iPCFourEyesActivity.ZoomIsMax = z2;
                        IPCFourEyesActivity.this.zoom.postValue(Float.valueOf(object.getInteger("Lens").intValue()));
                        IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.102.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCFourEyesActivity.this.binding.tvZoom.setText(object.getInteger("Lens") + "X");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void changeOpticalZoom(int i) {
        long jUptimeMillis = SystemClock.uptimeMillis();
        if (jUptimeMillis - this.lastOnclickTime >= 200) {
            this.lastOnclickTime = jUptimeMillis;
            IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).changeZoom(i, this.binding.playerBall.getTimes(), new IPanelCallback() { // from class: activity.IPCFourEyesActivity.103
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCFourEyesActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCFourEyesActivity.this.ZoomIsMax = object.getBoolean("ZoomIsMax").booleanValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeFocus(int i) {
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).changeFocus(i, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.104
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCFourEyesActivity.this.TAG, "invoke focus");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOrStopLiveIntercom() {
        if (!this.binding.speakerBtn.isSelected()) {
            if (ActivityCompat.checkSelfPermission(this, Permission.RECORD_AUDIO) != 0) {
                ActivityCompat.requestPermissions(this, new String[]{Permission.RECORD_AUDIO}, 4372);
                return;
            }
            LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
            if (liveIntercomV2 != null) {
                liveIntercomV2.start();
            }
            Log.e("speaker----", "play");
            this.whiteProgressDialog.setText(getResources().getString(R.string.open_speech));
            this.whiteProgressDialog.show();
            return;
        }
        this.whiteProgressDialog.setText(getResources().getString(R.string.close_speech));
        this.whiteProgressDialog.show();
        runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.105
            @Override // java.lang.Runnable
            public void run() {
                IPCFourEyesActivity.this.binding.speakerBtn.clearAnimation();
            }
        });
        LiveIntercomV2 liveIntercomV22 = this.liveIntercom;
        if (liveIntercomV22 != null) {
            liveIntercomV22.stop();
        }
        Log.e("speaker----", "stop");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLiveIntercomError() {
        runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.106
            @Override // java.lang.Runnable
            public void run() {
                if (IPCFourEyesActivity.this.isFinishing()) {
                    return;
                }
                IPCFourEyesActivity.this.whiteProgressDialog.dismiss();
            }
        });
        this.liveIntercom.stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecordError() {
        handleLiveIntercomError();
        runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.107
            @Override // java.lang.Runnable
            public void run() {
                IPCFourEyesActivity.this.binding.speakerBtn.setSelected(false);
                IPCFourEyesActivity.this.binding.fullIntercom.setSelected(IPCFourEyesActivity.this.binding.speakerBtn.isSelected());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String transformTime(int i) {
        String str;
        String str2;
        if (i <= 60) {
            if (i > 9) {
                return "00:" + i;
            }
            return "00:0" + i;
        }
        int i2 = i / 60;
        int i3 = i % 60;
        if (i2 > 9) {
            str = i2 + "";
        } else {
            str = "0" + i2;
        }
        if (i3 > 9) {
            str2 = i3 + "";
        } else {
            str2 = "0" + i3;
        }
        return str + ":" + str2;
    }

    private void hideTimer() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
            this.i = 0;
            this.binding.timer.setVisibility(8);
        }
    }

    public void scanFile(Bitmap bitmap) {
        File file = new File(getFilesPath(getApplication()) + "/photo/");
        if (file.exists() || file.mkdirs()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(file, System.currentTimeMillis() + ".jpg"));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                EventBus.getDefault().post(new RefreshPicture());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilesPath(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = ((File) Objects.requireNonNull(context.getExternalFilesDir(""))).getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "//" + Utils.getUserPhone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareDevice(final String str, List<String> list, String str2) {
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_ACCOUNT_ATTR, str);
        if (TextUtils.isEmpty(str2)) {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL);
        } else {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_MOBILE);
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, str2);
        }
        map.put("iotIdList", list);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE).setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.IPCFourEyesActivity.108
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, IPCFourEyesActivity.this.TAG, "onFailure");
                Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.share_failed, 0).show();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                final int code = ioTResponse.getCode();
                Log.e(IPCFourEyesActivity.this.TAG, "shareDevice onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.108.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCFourEyesActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            if (code == 2077) {
                                DialogUtil.showTipsConfirmDiaLog(IPCFourEyesActivity.this.getActivity(), IPCFourEyesActivity.this.getString(R.string.sharing_failed), IPCFourEyesActivity.this.getString(R.string.sharing_tips_1) + SdkConstant.CLOUDAPI_LF + IPCFourEyesActivity.this.getString(R.string.sharing_tips_2) + SdkConstant.CLOUDAPI_LF + IPCFourEyesActivity.this.getString(R.string.sharing_tips_3) + SdkConstant.CLOUDAPI_LF + IPCFourEyesActivity.this.getString(R.string.sharing_tips_4), IPCFourEyesActivity.this.getString(R.string.i_know));
                                return;
                            }
                            Toast.makeText(IPCFourEyesActivity.this.getActivity(), localizedMsg, 0).show();
                        }
                    });
                } else {
                    IPCFourEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCFourEyesActivity.108.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCFourEyesActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            Toast.makeText(IPCFourEyesActivity.this.getActivity(), IPCFourEyesActivity.this.getString(R.string.share_succeed, new Object[]{((DeviceInfoBean) IPCFourEyesActivity.this.shareDialog2.getExtra()).getName(), str}), 0).show();
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNightMode(final Object obj, final String str) {
        HashMap map = new HashMap();
        map.put(Constants.DAY_NIGHT_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: activity.IPCFourEyesActivity.109
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.109.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setDayNightMode(str, Integer.parseInt(obj.toString()));
                            IPCFourEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCFourEyesActivity.109.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCFourEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
