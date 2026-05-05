package activity;

import adapter.GridSpaceItemDecoration;
import adapter.HomeTopicPagerAdapter;
import adapter.IpcWiFiAdapter;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityIpcameraThreeEyesBinding;
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
import view.SelectorDialogFragment;
import view.ShadowButton;
import view.TouchView;
import view.WhiteProgressDialog;
import view.ZoomableTextureView;

/* JADX INFO: loaded from: classes.dex */
public class IPCThreeEyesActivity extends CommonActivity {
    private static String[] PERMISSIONS_STORAGE = {Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
    private static final int RECORD_UPDATE_TIME = 102;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private int WifiConfigIsExist;
    private boolean ZoomIsMax;
    private int ZoomMax;
    String address;
    private DeviceInfoBean ballDevice;
    private ActivityIpcameraThreeEyesBinding binding;
    private CountDownTimer countDownTimer;

    /* JADX INFO: renamed from: dialog, reason: collision with root package name */
    private AlertDialog f1576dialog;
    private int faceDetectionAbility;
    private DeviceInfoBean gunDevice1;
    private DeviceInfoBean gunDevice2;
    private int h;
    private Timer inactivityTimer;
    private String[] infrarredMode;
    private InputDialogViewIpc inputDialogView;
    private boolean isHorizontal;
    private boolean isOtherCard;
    private ActionTypeEnum lastActionTypeEnum;
    private long lastCtrlTime;
    String lat;
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
    private int showMode = 0;
    private int rowNum = 2;
    private int columnNum = 3;
    private int lowPowerMode = -1;
    private int is1100ErrorPre = 10;
    private int countWakeUp = 0;
    private List<String> showList = new ArrayList();
    private ArrayList<TopicBean> mTopicData = new ArrayList<>();
    private ArrayList<RecyclerView> mList = new ArrayList<>();
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
    private Handler handler = new Handler(new Handler.Callback() { // from class: activity.IPCThreeEyesActivity.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(@NonNull Message message) {
            if (message.what != 102) {
                return false;
            }
            IPCThreeEyesActivity.this.binding.playerInfoTv.setText(((IPCThreeEyesActivity.this.playBall.getCurrentPlayInfo().bitRate / 1024) / 8) + "KB/S");
            if (IPCThreeEyesActivity.this.binding.playerInfoTv.getVisibility() != 0) {
                return false;
            }
            IPCThreeEyesActivity.this.updateInfoTv();
            return false;
        }
    });
    private Handler wakeUpHandler = new AnonymousClass75();
    ViewTreeObserver.OnGlobalLayoutListener nGlobalLayoutListener = new AnonymousClass90();
    int i = 0;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ipcamera_three_eyes;
    }

    static /* synthetic */ int access$3308(IPCThreeEyesActivity iPCThreeEyesActivity) {
        int i = iPCThreeEyesActivity.showMode;
        iPCThreeEyesActivity.showMode = i + 1;
        return i;
    }

    static /* synthetic */ int access$6808(IPCThreeEyesActivity iPCThreeEyesActivity) {
        int i = iPCThreeEyesActivity.countWakeUp;
        iPCThreeEyesActivity.countWakeUp = i + 1;
        return i;
    }

    static /* synthetic */ int access$7110(IPCThreeEyesActivity iPCThreeEyesActivity) {
        int i = iPCThreeEyesActivity.is1100ErrorPre;
        iPCThreeEyesActivity.is1100ErrorPre = i - 1;
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
        this.binding = (ActivityIpcameraThreeEyesBinding) DataBindingUtil.setContentView(this, R.layout.activity_ipcamera_three_eyes);
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
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            SettingsCtrl.getInstance().getProperties(this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeEyesActivity.2
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            SettingsCtrl.getInstance().getProperties(this.gunDevice1.getIotId(), new MyCallback() { // from class: activity.IPCThreeEyesActivity.3
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            SettingsCtrl.getInstance().getProperties(this.gunDevice2.getIotId(), new MyCallback() { // from class: activity.IPCThreeEyesActivity.4
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            SettingsCtrl.getInstance().getProperties(this.nvrDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeEyesActivity.5
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
        }
        initView();
        initPlayerBall();
        initPlayerGun1();
        initPlayerGun2();
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
        LivePlayer livePlayer = this.playBall;
        if (livePlayer != null) {
            livePlayer.stop();
        }
        LivePlayer livePlayer2 = this.playGun1;
        if (livePlayer2 != null) {
            livePlayer2.stop();
        }
        LivePlayer livePlayer3 = this.playGun2;
        if (livePlayer3 != null) {
            livePlayer3.stop();
        }
        Timer timer = this.ptzTimer;
        if (timer != null) {
            timer.cancel();
            this.ptzTimer = null;
        }
        EventBus.getDefault().unregister(this);
        Handler handler = this.wakeUpHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.wakeUpHandler = null;
        }
        LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
        if (liveIntercomV2 != null) {
            liveIntercomV2.release();
        }
        stopInactivityTimer();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.binding.ipcOfflineText.setVisibility(8);
        this.binding.qualityBtn.setText(this.definitionList.get(SharePreferenceManager.getInstance().getStreamVideoQuality(this.ballDevice.getIotId())));
        SettingsCtrl.getInstance().getProperties(this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeEyesActivity.6
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                if (SharePreferenceManager.getInstance().getMixZoom(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeEyesActivity.this.binding.tvZoom.setVisibility(8);
                        }
                    });
                }
                if (SharePreferenceManager.getInstance().getSupportZoom(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.6.2
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeEyesActivity.this.binding.tvZoom.setVisibility(8);
                        }
                    });
                } else {
                    IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.6.3
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeEyesActivity.this.binding.layoutAf.setVisibility(8);
                        }
                    });
                }
                if (SharePreferenceManager.getInstance().getSensorViewDisplay(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                    IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.6.4
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeEyesActivity.this.binding.SensorView.setVisibility(8);
                        }
                    });
                } else {
                    IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.6.5
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeEyesActivity.this.binding.SensorView.setVisibility(0);
                        }
                    });
                }
                if (!AppConfig.isChina) {
                    IPCThreeEyesActivity.this.binding.ivCharge4gFlow.setImageResource(R.drawable.selector_server_en);
                }
                IPCThreeEyesActivity.this.binding.ivCharge4gFlow.setSelected(SharePreferenceManager.getInstance().getSupport4G(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1);
                Log.e("云存=", "" + SharePreferenceManager.getInstance().getEventRecord(IPCThreeEyesActivity.this.ballDevice.getIotId()) + "  4G=" + SharePreferenceManager.getInstance().getSupport4G(IPCThreeEyesActivity.this.ballDevice.getIotId()));
                if (SharePreferenceManager.getInstance().getEventRecord(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1 || SharePreferenceManager.getInstance().getSupport4G(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCThreeEyesActivity.this.binding.ivCharge4gFlow.setVisibility(0);
                } else {
                    IPCThreeEyesActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                }
                IPCThreeEyesActivity.this.faceDetectionAbility = SharePreferenceManager.getInstance().getHumanoidTracking(IPCThreeEyesActivity.this.ballDevice.getIotId()).intValue();
                IPCThreeEyesActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(IPCThreeEyesActivity.this.ballDevice.getIotId());
                if (IPCThreeEyesActivity.this.faceDetectionAbility == 1) {
                    IPCThreeEyesActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(IPCThreeEyesActivity.this.ballDevice.getIotId()).intValue() == 1;
                } else {
                    IPCThreeEyesActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1;
                }
                IPCThreeEyesActivity.this.showMore();
                IPCThreeEyesActivity.this.initMore();
            }
        });
        if (SharePreferenceManager.getInstance().getLowPowerSwitch(this.ballDevice.getIotId()) == 1) {
            this.wakeUpHandler.removeCallbacksAndMessages(null);
            wakeUpDevice();
            wakeUpDeviceHandel();
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
            this.binding.ivNightTop.setVisibility(8);
            this.binding.fullNightVision.setVisibility(8);
        }
        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.ballDevice.getIotId()) == 1) {
            this.binding.ivNightTop.setVisibility(8);
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
                Log.e("防沉迷", "没有上报网络状态属性");
                stopInactivityTimer();
                startInactivityTimer();
                return;
            }
            return;
        }
        if (SharePreferenceManager.getInstance().getNetState(this.ballDevice.getIotId()) == 3) {
            Log.e("防沉迷", "4G模式");
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
            IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.InactivityTimerTask.1
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCThreeEyesActivity.this.playBall.getPlayState() == 3 || IPCThreeEyesActivity.this.playGun1.getPlayState() == 3 || IPCThreeEyesActivity.this.playGun2.getPlayState() == 3) {
                        if (IPCThreeEyesActivity.this.playBall != null) {
                            IPCThreeEyesActivity.this.playBall.stop();
                        }
                        if (IPCThreeEyesActivity.this.playGun1 != null) {
                            IPCThreeEyesActivity.this.playGun1.stop();
                        }
                        if (IPCThreeEyesActivity.this.playGun2 != null) {
                            IPCThreeEyesActivity.this.playGun2.stop();
                        }
                        Log.e("防沉迷", "在播放");
                        IPCThreeEyesActivity.this.showPlayButton();
                        DialogUtil.showTipsConfirmDiaLog(IPCThreeEyesActivity.this.getActivity(), IPCThreeEyesActivity.this.getString(R.string.warm_tips), IPCThreeEyesActivity.this.getString(R.string.warm_tips_1), IPCThreeEyesActivity.this.getString(R.string.i_know), new DialogUtil.OnConfirmClickListener() { // from class: activity.IPCThreeEyesActivity.InactivityTimerTask.1.1
                            @Override // dialog.DialogUtil.OnConfirmClickListener
                            public void ConfirmListener() {
                                IPCThreeEyesActivity.this.resetInactivityTimer();
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
            topicAdapterDoubleEye.setOnItemClickListener(new AnonymousClass7());
            recyclerView.setAdapter(topicAdapterDoubleEye);
            this.mList.add(recyclerView);
        }
        this.binding.topicViewPager.setAdapter(new HomeTopicPagerAdapter(this.mList));
        this.binding.topicViewPager.setOffscreenPageLimit(size - 1);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() { // from class: activity.IPCThreeEyesActivity.8
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

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$7, reason: invalid class name */
    class AnonymousClass7 implements TopicAdapterDoubleEye.OnItemClickListener {
        AnonymousClass7() {
        }

        @Override // adapter.TopicAdapterDoubleEye.OnItemClickListener
        public void onTopicItemClick(TopicBean topicBean, int i) {
            if (topicBean.getIcon() == R.drawable.icon_card_back_false || topicBean.getIcon() == R.drawable.video_back) {
                Intent intent = new Intent(IPCThreeEyesActivity.this, (Class<?>) RecordVideoActivity.class);
                intent.putExtra("title", IPCThreeEyesActivity.this.ballDevice.getName());
                intent.putExtra("iotId", IPCThreeEyesActivity.this.ballDevice.getIotId());
                intent.putExtra("iotId2", IPCThreeEyesActivity.this.gunDevice1.getIotId());
                intent.putExtra("iotId3", IPCThreeEyesActivity.this.gunDevice2.getIotId());
                IPCThreeEyesActivity.this.startActivity(intent);
                return;
            }
            if (topicBean.getIcon() == R.drawable.icon_cloud_back_false) {
                Intent intent2 = new Intent(IPCThreeEyesActivity.this.getActivity(), (Class<?>) CloudStorageActivity.class);
                intent2.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCThreeEyesActivity.this.ballDevice);
                intent2.putExtra("device1", IPCThreeEyesActivity.this.gunDevice1);
                intent2.putExtra("device2", IPCThreeEyesActivity.this.gunDevice2);
                intent2.putExtra("nvrDevice", IPCThreeEyesActivity.this.nvrDevice);
                IPCThreeEyesActivity.this.startActivity(intent2);
                return;
            }
            if (topicBean.getIcon() == R.drawable.share_ipc) {
                IPCThreeEyesActivity.this.shareDialog2 = ((ShareDialog.Builder) new ShareDialog.Builder().view(R.layout.dialog_input2).leftBtnText(IPCThreeEyesActivity.this.getString(R.string.cancel)).rightBtnText(IPCThreeEyesActivity.this.getString(R.string.share_immediately)).clickRight(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.7.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        if (IPCThreeEyesActivity.this.shareDialog2.getContent() != null) {
                            if (IPCThreeEyesActivity.this.shareDialog2.getMode() != 0 || SystemUtil.isPhone(IPCThreeEyesActivity.this.shareDialog2.getContent())) {
                                if (IPCThreeEyesActivity.this.shareDialog2.getMode() == 1 && !SystemUtil.isEmail(IPCThreeEyesActivity.this.shareDialog2.getContent())) {
                                    ToastUtils.toast(IPCThreeEyesActivity.this.getActivity(), IPCThreeEyesActivity.this.getString(R.string.email_invalid));
                                    return;
                                }
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(IPCThreeEyesActivity.this.nvrDevice.getIotId());
                                arrayList.add(IPCThreeEyesActivity.this.ballDevice.getIotId());
                                arrayList.add(IPCThreeEyesActivity.this.gunDevice1.getIotId());
                                arrayList.add(IPCThreeEyesActivity.this.gunDevice2.getIotId());
                                IPCThreeEyesActivity.this.shareDevice(IPCThreeEyesActivity.this.shareDialog2.getContent(), arrayList, IPCThreeEyesActivity.this.shareDialog2.getMode() == 0 ? IPCThreeEyesActivity.this.shareDialog2.getDistinct() : null);
                                return;
                            }
                            ToastUtils.toast(IPCThreeEyesActivity.this.getActivity(), IPCThreeEyesActivity.this.getString(R.string.phone_invalid));
                            return;
                        }
                        Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.share_user_null_error, 0).show();
                    }
                })).create();
                IPCThreeEyesActivity.this.shareDialog2.setOnShareClick(new ShareDialog.OnShareClickListener() { // from class: activity.IPCThreeEyesActivity.7.2
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
                IPCThreeEyesActivity.this.shareDialog2.setExtra(IPCThreeEyesActivity.this.ballDevice);
                IPCThreeEyesActivity.this.shareDialog2.show(IPCThreeEyesActivity.this.getSupportFragmentManager(), "");
                return;
            }
            if (topicBean.getTitle().equals(IPCThreeEyesActivity.this.getResources().getString(R.string.zoom))) {
                IPCThreeEyesActivity.this.binding.rlTouchView.setVisibility(8);
                IPCThreeEyesActivity.this.binding.layoutMore.setVisibility(8);
                IPCThreeEyesActivity.this.binding.layoutZoom.setVisibility(0);
                return;
            }
            if (topicBean.getTitle().equals(IPCThreeEyesActivity.this.getResources().getString(R.string.track)) || topicBean.getTitle().equals(IPCThreeEyesActivity.this.getResources().getString(R.string.mobile_tracking))) {
                if (SharePreferenceManager.getInstance().getFaceDetectMode(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                    HashMap map = new HashMap();
                    map.put(Constants.FACE_DETECT_SENSITIVITY, 2);
                    IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.7.3
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, Object obj) {
                            if (!z || obj == null || "".equals(String.valueOf(obj))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.3.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                        }
                                    });
                                } else {
                                    SharePreferenceManager.getInstance().setFaceDetectMode(IPCThreeEyesActivity.this.ballDevice.getIotId(), 2);
                                }
                            }
                        }
                    });
                }
                if (IPCThreeEyesActivity.this.faceDetectionAbility == 1) {
                    IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.4
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeEyesActivity.this.showProgressDialog();
                        }
                    });
                    HashMap map2 = new HashMap();
                    if (SharePreferenceManager.getInstance().getTlrClRgn(IPCThreeEyesActivity.this.ballDevice.getIotId()).intValue() == 1) {
                        if (!IPCThreeEyesActivity.this.isDetecting) {
                            i = SharePreferenceManager.getInstance().getAreaDetectEnable(IPCThreeEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? SharePreferenceManager.getInstance().getCrossLineEnable(IPCThreeEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? 7 : 5 : SharePreferenceManager.getInstance().getCrossLineEnable(IPCThreeEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? 3 : 1;
                        } else if (SharePreferenceManager.getInstance().getAreaDetectEnable(IPCThreeEyesActivity.this.ballDevice.getIotId()).intValue() != 0) {
                            i = SharePreferenceManager.getInstance().getCrossLineEnable(IPCThreeEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? 6 : 4;
                        } else if (SharePreferenceManager.getInstance().getCrossLineEnable(IPCThreeEyesActivity.this.ballDevice.getIotId()).intValue() != 0) {
                            i = 2;
                        }
                    } else if (IPCThreeEyesActivity.this.isDetecting) {
                        i = 1;
                    }
                    map2.put(Constants.IvpExSwitch, Integer.valueOf(i));
                    IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).setProperties(map2, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.7.5
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            IPCThreeEyesActivity.this.dismissProgressDialog();
                            if (!z) {
                                IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.5.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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
                                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.5.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                            }
                                        });
                                        return;
                                    }
                                    IPCThreeEyesActivity.this.isDetecting = !IPCThreeEyesActivity.this.isDetecting;
                                    if (IPCThreeEyesActivity.this.faceDetectionAbility == 1) {
                                        if (IPCThreeEyesActivity.this.isDetecting) {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeEyesActivity.this.ballDevice.getIotId(), 1);
                                        } else {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeEyesActivity.this.ballDevice.getIotId(), 0);
                                        }
                                    } else if (IPCThreeEyesActivity.this.isDetecting) {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCThreeEyesActivity.this.ballDevice.getIotId(), 1);
                                    } else {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCThreeEyesActivity.this.ballDevice.getIotId(), 0);
                                    }
                                    SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeEyesActivity.this.selectIotId, i & 1);
                                    SharePreferenceManager.getInstance().setAreaDetectEnable(IPCThreeEyesActivity.this.selectIotId, (i & 4) >> 2);
                                    SharePreferenceManager.getInstance().setCrossLineEnable(IPCThreeEyesActivity.this.selectIotId, (i & 2) >> 1);
                                    IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.5.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            IPCThreeEyesActivity.this.showMore();
                                            IPCThreeEyesActivity.this.initMore();
                                            Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.6
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeEyesActivity.this.showProgressDialog();
                        }
                    });
                    HashMap map3 = new HashMap();
                    map3.put(Constants.INTELLIGENT_TRACKING, Integer.valueOf(1 ^ (IPCThreeEyesActivity.this.isDetecting ? 1 : 0)));
                    IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).setProperties(map3, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.7.7
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            IPCThreeEyesActivity.this.dismissProgressDialog();
                            if (!z) {
                                IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.7.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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
                                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.7.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                            }
                                        });
                                        return;
                                    }
                                    IPCThreeEyesActivity.this.isDetecting = !IPCThreeEyesActivity.this.isDetecting;
                                    if (IPCThreeEyesActivity.this.faceDetectionAbility == 1) {
                                        if (IPCThreeEyesActivity.this.isDetecting) {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeEyesActivity.this.ballDevice.getIotId(), 1);
                                        } else {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeEyesActivity.this.ballDevice.getIotId(), 0);
                                        }
                                    } else if (IPCThreeEyesActivity.this.isDetecting) {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCThreeEyesActivity.this.ballDevice.getIotId(), 1);
                                    } else {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCThreeEyesActivity.this.ballDevice.getIotId(), 0);
                                    }
                                    IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.7.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            IPCThreeEyesActivity.this.showMore();
                                            IPCThreeEyesActivity.this.initMore();
                                            Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
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
                    IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).changePresetLocation(103, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.7.8
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                                Log.e("预置位", "103");
                            }
                        }
                    });
                    return;
                } else {
                    IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).addPresetLocation(99, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.7.9
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                                Log.e("预置位", "99");
                            }
                        }
                    });
                    IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).changePresetLocation(100, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.7.10
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
            if (topicBean.getTitle().equals(IPCThreeEyesActivity.this.getResources().getString(R.string.floodlight))) {
                HashMap map4 = new HashMap();
                map4.put(Constants.FloodlightSwitch, Integer.valueOf(SharePreferenceManager.getInstance().getFloodlightSwitch(IPCThreeEyesActivity.this.ballDevice.getIotId()) != 1 ? 1 : 0));
                IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).setProperties(map4, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.7.11
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.11.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCThreeEyesActivity.this.showMore();
                                    IPCThreeEyesActivity.this.initMore();
                                    if (SharePreferenceManager.getInstance().getFloodlightSwitch(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                                        SharePreferenceManager.getInstance().setFloodlightSwitch(IPCThreeEyesActivity.this.ballDevice.getIotId(), 1);
                                    } else {
                                        SharePreferenceManager.getInstance().setFloodlightSwitch(IPCThreeEyesActivity.this.ballDevice.getIotId(), 0);
                                    }
                                    Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                });
            } else if (topicBean.getTitle().equals(IPCThreeEyesActivity.this.getResources().getString(R.string.locate))) {
                IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).getLocationBasedService(new AnonymousClass12());
            } else if (topicBean.getTitle().equals(IPCThreeEyesActivity.this.getResources().getString(R.string.map_gps))) {
                IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).getGPSPositioningService(new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.7.13
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable final Object obj) {
                        if (z) {
                            if (obj != null && !"".equals(String.valueOf(obj))) {
                                IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.13.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Log.e("基站定位信息", String.valueOf(obj));
                                        JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                        if (object.getInteger("code").intValue() != 200) {
                                            IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
                                            return;
                                        }
                                        if (!String.valueOf(obj).contains("Latitude")) {
                                            IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
                                            return;
                                        }
                                        JSONObject jSONObject = object.getJSONObject("data");
                                        String string = jSONObject.getString("Latitude");
                                        String string2 = jSONObject.getString("Longitude");
                                        MapUtils.dddmmToDecimal(Double.parseDouble(string));
                                        IPCThreeEyesActivity.this.lat = MapUtils.dddmmToDecimal(Double.parseDouble(string)) + "";
                                        IPCThreeEyesActivity.this.lon = MapUtils.dddmmToDecimal(Double.parseDouble(string2)) + "";
                                        IPCThreeEyesActivity.this.mapFragment.showAllowingStateLoss(IPCThreeEyesActivity.this.getSupportFragmentManager(), "");
                                    }
                                });
                                return;
                            } else {
                                IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
                                return;
                            }
                        }
                        IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
                    }
                });
            }
        }

        /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$7$12, reason: invalid class name */
        class AnonymousClass12 implements IPanelCallback {
            AnonymousClass12() {
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable final Object obj) {
                if (z) {
                    if (obj != null && !"".equals(String.valueOf(obj))) {
                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.7.12.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Log.e("基站定位信息", String.valueOf(obj));
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.getInteger("code").intValue() != 200) {
                                    IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
                                    return;
                                }
                                if (!String.valueOf(obj).contains("CellIdentity")) {
                                    IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
                                    return;
                                }
                                JSONObject jSONObject = object.getJSONObject("data");
                                String string = jSONObject.getString("CellIdentity");
                                jSONObject.getInteger("MobileNetworkCode").intValue();
                                String string2 = jSONObject.getString("TrackingAreaCode");
                                int i = Integer.parseInt(string, 16);
                                int i2 = Integer.parseInt(string2, 16);
                                new OkHttpClient().newCall(new Request.Builder().url("http://api.cellocation.com:84/cell/?mcc=460&mnc=1&lac=" + i2 + "&ci=" + i + "&output=json").get().build()).enqueue(new Callback() { // from class: activity.IPCThreeEyesActivity.7.12.1.1
                                    static final /* synthetic */ boolean $assertionsDisabled = false;

                                    @Override // okhttp3.Callback
                                    public void onFailure(Call call, IOException iOException) {
                                        IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
                                    }

                                    @Override // okhttp3.Callback
                                    public void onResponse(Call call, Response response) throws IOException {
                                        try {
                                            JSONObject object2 = JSONObject.parseObject(response.body().string());
                                            if (object2.getInteger("errcode").intValue() != 0) {
                                                IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
                                                return;
                                            }
                                            IPCThreeEyesActivity.this.lat = object2.getString(DispatchConstants.LATITUDE);
                                            IPCThreeEyesActivity.this.lon = object2.getString("lon");
                                            object2.getString("radius");
                                            IPCThreeEyesActivity.this.address = object2.getString("address");
                                            IPCThreeEyesActivity.this.mapFragment.showAllowingStateLoss(IPCThreeEyesActivity.this.getSupportFragmentManager(), "");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                        return;
                    } else {
                        IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
                        return;
                    }
                }
                IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.play_failed_retry));
            }
        }
    }

    private void initView() {
        String[] strArr;
        this.whiteProgressDialog = new WhiteProgressDialog(this);
        this.binding.tvTitle.setText(this.ballDevice.getName());
        this.binding.maxLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: activity.IPCThreeEyesActivity.9
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                IPCThreeEyesActivity.this.binding.maxLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                iPCThreeEyesActivity.w = iPCThreeEyesActivity.binding.maxLayout.getWidth();
                IPCThreeEyesActivity iPCThreeEyesActivity2 = IPCThreeEyesActivity.this;
                iPCThreeEyesActivity2.h = iPCThreeEyesActivity2.binding.maxLayout.getHeight();
                double dDoubleValue = new BigDecimal(IPCThreeEyesActivity.this.w / IPCThreeEyesActivity.this.h).setScale(2, 4).doubleValue();
                Log.e("屏幕", "宽高比例" + dDoubleValue);
                if (dDoubleValue >= 0.49d) {
                    IPCThreeEyesActivity.this.isRatio = true;
                    IPCThreeEyesActivity.this.viewHeight = r0.w / 2;
                    Log.e("屏幕", "宽=" + IPCThreeEyesActivity.this.w + " 高=" + IPCThreeEyesActivity.this.h + " viewHeight=" + IPCThreeEyesActivity.this.viewHeight);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (IPCThreeEyesActivity.this.w / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutPlay, -1, (((IPCThreeEyesActivity.this.w / 2) / 16) * 9) + ((IPCThreeEyesActivity.this.w / 16) * 9));
                } else {
                    IPCThreeEyesActivity.this.isRatio = false;
                    IPCThreeEyesActivity.this.viewHeight = (r0.w / 16) * 9;
                    Log.e("屏幕", "宽=" + IPCThreeEyesActivity.this.w + " 高=" + IPCThreeEyesActivity.this.h + " viewHeight=" + IPCThreeEyesActivity.this.viewHeight);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutPlay, -1, ((int) IPCThreeEyesActivity.this.viewHeight) * 2);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                }
                IPCThreeEyesActivity.this.addControlTouchView();
                return true;
            }
        });
        this.infrarredMode = getResources().getStringArray(R.array.InfrarredMode);
        this.nightModelList.clear();
        int i = 0;
        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == -1) {
            while (true) {
                String[] strArr2 = this.infrarredMode;
                if (i >= strArr2.length) {
                    break;
                }
                this.nightModelList.add(strArr2[i]);
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
        this.nightModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCThreeEyesActivity.10
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v10, types: [int] */
            /* JADX WARN: Type inference failed for: r0v12 */
            /* JADX WARN: Type inference failed for: r0v13 */
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i3) {
                ((String) IPCThreeEyesActivity.this.nightModelList.get(i3)).equals(IPCThreeEyesActivity.this.infrarredMode[0]);
                ?? Equals = ((String) IPCThreeEyesActivity.this.nightModelList.get(i3)).equals(IPCThreeEyesActivity.this.infrarredMode[1]);
                if (((String) IPCThreeEyesActivity.this.nightModelList.get(i3)).equals(IPCThreeEyesActivity.this.infrarredMode[2])) {
                    Equals = 2;
                }
                IPCThreeEyesActivity.this.updateNightMode(Integer.valueOf((int) Equals), IPCThreeEyesActivity.this.selectIotId);
            }
        });
        this.inputDialogView = new InputDialogViewIpc.Builder().build();
        this.inputDialogView.addOnClickListener(new AnonymousClass11());
        this.showList.add(getResources().getString(R.string.show_mode4));
        this.showList.add(getResources().getString(R.string.show_mode2));
        this.showList.add(getResources().getString(R.string.show_mode2));
        this.showList.add(getResources().getString(R.string.show_mode3));
        this.definitionList.add(getString(R.string.quality_l));
        this.definitionList.add(getString(R.string.quality_m));
        this.definitionList.add(getString(R.string.quality_h));
        if (AppConfig.isChina) {
            strArr = new String[]{"高德地图", "百度地图"};
        } else {
            strArr = new String[]{"Google Map"};
        }
        this.mapFragment = new SelectorDialogFragment("" + getResources().getString(R.string.select_map), strArr);
        this.mapFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCThreeEyesActivity.12
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i3) {
                switch (i3) {
                    case 0:
                        if (AppConfig.isChina) {
                            if (MapUtils.isAvilible(IPCThreeEyesActivity.this, "com.autonavi.minimap")) {
                                try {
                                    StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=");
                                    stringBuffer.append("yitu8_driver");
                                    stringBuffer.append("&lat=");
                                    stringBuffer.append(IPCThreeEyesActivity.this.lat);
                                    stringBuffer.append("&lon=");
                                    stringBuffer.append(IPCThreeEyesActivity.this.lon);
                                    stringBuffer.append("&dev=");
                                    stringBuffer.append(1);
                                    stringBuffer.append("&style=");
                                    stringBuffer.append(0);
                                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.setPackage("com.autonavi.minimap");
                                    IPCThreeEyesActivity.this.startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            } else {
                                Toast.makeText(IPCThreeEyesActivity.this, "您尚未安装高德地图", 1).show();
                            }
                        } else if (MapUtils.isAvilible(IPCThreeEyesActivity.this, "com.google.android.apps.maps")) {
                            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse("google.navigation:q=" + IPCThreeEyesActivity.this.lat + "," + IPCThreeEyesActivity.this.lon + ", + Sydney +Australia"));
                            intent2.setPackage("com.google.android.apps.maps");
                            IPCThreeEyesActivity.this.startActivity(intent2);
                        } else {
                            Toast.makeText(IPCThreeEyesActivity.this, IPCThreeEyesActivity.this.getString(R.string.not_installed) + "Google Map", 1).show();
                            IPCThreeEyesActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.android.apps.maps")));
                        }
                        break;
                    case 1:
                        if (MapUtils.isAvilible(IPCThreeEyesActivity.this, "com.baidu.BaiduMap")) {
                            try {
                                StringBuffer stringBuffer2 = new StringBuffer("baidumap://map/navi?location=");
                                stringBuffer2.append(IPCThreeEyesActivity.this.lat);
                                stringBuffer2.append(",");
                                stringBuffer2.append(IPCThreeEyesActivity.this.lon);
                                stringBuffer2.append("&type=TIME");
                                Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer2.toString()));
                                intent3.setPackage("com.baidu.BaiduMap");
                                IPCThreeEyesActivity.this.startActivity(intent3);
                            } catch (Exception e2) {
                                Log.e("intent", e2.getMessage());
                                return;
                            }
                        } else {
                            Toast.makeText(IPCThreeEyesActivity.this, "您尚未安装百度地图", 1).show();
                        }
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$11, reason: invalid class name */
    class AnonymousClass11 implements InputDialogViewIpc.OnClickListener {
        AnonymousClass11() {
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onPositiveClick(String str, Object obj) {
            IPCThreeEyesActivity.this.showProgressDialog();
            IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).setAPList(IPCThreeEyesActivity.this.selectSsid, str, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.11.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (z) {
                        try {
                            if (obj2 == null) {
                                IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.11.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCThreeEyesActivity.this.getActivity(), IPCThreeEyesActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else if (((IoTResponse) JSON.parseObject(obj2.toString()).toJavaObject(IoTResponse.class)).getCode() != 200) {
                                IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.11.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCThreeEyesActivity.this.getActivity(), IPCThreeEyesActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else {
                                IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.11.1.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        IPCThreeEyesActivity.this.connect();
                                    }
                                });
                            }
                        } finally {
                            IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.11.1.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCThreeEyesActivity.this.dismissProgressDialog();
                                }
                            });
                        }
                    }
                }
            });
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onNegativeClick() {
            IPCThreeEyesActivity.this.inputDialogView.dismiss();
            IPCThreeEyesActivity.this.f1576dialog.show();
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
        this.binding.ivBack.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.13
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeEyesActivity.this.onBackPressed();
            }
        });
        this.binding.ivSetting.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.14
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(IPCThreeEyesActivity.this, (Class<?>) SettingsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCThreeEyesActivity.this.ballDevice);
                bundle.putSerializable("device1", IPCThreeEyesActivity.this.gunDevice1);
                bundle.putSerializable("device2", IPCThreeEyesActivity.this.gunDevice2);
                bundle.putSerializable("nvrDevice", IPCThreeEyesActivity.this.nvrDevice);
                intent.putExtras(bundle);
                IPCThreeEyesActivity.this.startActivity(intent);
            }
        });
        this.binding.llMoreDoubleEye.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.15
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCThreeEyesActivity.this.showMode == 1) {
                    IPCThreeEyesActivity.this.showMode = 0;
                    IPCThreeEyesActivity.this.binding.layoutCenter.setVisibility(0);
                    IPCThreeEyesActivity.this.binding.layoutGunOrientation.setOrientation(0);
                    if (IPCThreeEyesActivity.this.isRatio) {
                        IPCThreeEyesActivity.this.viewHeight = r7.w / 2;
                        Log.e("屏幕", "宽=" + IPCThreeEyesActivity.this.w + " 高=" + IPCThreeEyesActivity.this.h + " viewHeight=" + IPCThreeEyesActivity.this.viewHeight);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (IPCThreeEyesActivity.this.w / 16) * 9);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutPlay, -1, (((IPCThreeEyesActivity.this.w / 2) / 16) * 9) + ((IPCThreeEyesActivity.this.w / 16) * 9));
                    } else {
                        IPCThreeEyesActivity.this.viewHeight = (r7.w / 16) * 9;
                        Log.e("屏幕", "宽=" + IPCThreeEyesActivity.this.w + " 高=" + IPCThreeEyesActivity.this.h + " viewHeight=" + IPCThreeEyesActivity.this.viewHeight);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutPlay, -1, ((int) IPCThreeEyesActivity.this.viewHeight) * 2);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                    }
                }
                IPCThreeEyesActivity.this.binding.llMoreDoubleEye.setSelected(true ^ IPCThreeEyesActivity.this.binding.llMoreDoubleEye.isSelected());
                if (IPCThreeEyesActivity.this.binding.llMoreDoubleEye.isSelected()) {
                    IPCThreeEyesActivity.this.showMore();
                    IPCThreeEyesActivity.this.binding.rlTouchView.setVisibility(8);
                    IPCThreeEyesActivity.this.binding.layoutZoom.setVisibility(8);
                    IPCThreeEyesActivity.this.binding.layoutMore.setVisibility(0);
                    return;
                }
                IPCThreeEyesActivity.this.binding.layoutMore.setVisibility(8);
                IPCThreeEyesActivity.this.binding.layoutZoom.setVisibility(8);
                IPCThreeEyesActivity.this.binding.rlTouchView.setVisibility(0);
            }
        });
        this.binding.llFull.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.16
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeEyesActivity.access$3308(IPCThreeEyesActivity.this);
                if (IPCThreeEyesActivity.this.showMode > 4) {
                    IPCThreeEyesActivity.this.showMode = 0;
                }
                switch (IPCThreeEyesActivity.this.showMode) {
                    case 0:
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                        layoutParams.removeRule(13);
                        IPCThreeEyesActivity.this.binding.layoutGun.setLayoutParams(layoutParams);
                        if (IPCThreeEyesActivity.this.isRatio) {
                            IPCThreeEyesActivity.this.isRatio = true;
                            IPCThreeEyesActivity.this.viewHeight = r10.w / 2;
                            Log.e("屏幕", "宽=" + IPCThreeEyesActivity.this.w + " 高=" + IPCThreeEyesActivity.this.h + " viewHeight=" + IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (IPCThreeEyesActivity.this.w / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutPlay, -1, (((IPCThreeEyesActivity.this.w / 2) / 16) * 9) + ((IPCThreeEyesActivity.this.w / 16) * 9));
                        } else {
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, (IPCThreeEyesActivity.this.w / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (IPCThreeEyesActivity.this.w / 16) * 9);
                        }
                        IPCThreeEyesActivity.this.binding.playerGun2.setVisibility(0);
                        IPCThreeEyesActivity.this.binding.playerGun1.setVisibility(0);
                        IPCThreeEyesActivity.this.binding.layoutGun.setVisibility(0);
                        IPCThreeEyesActivity.this.binding.playerBall.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(0);
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        break;
                    case 1:
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(0);
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        IPCThreeEyesActivity.this.binding.layoutCenter.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.layoutGunOrientation.setOrientation(1);
                        if (IPCThreeEyesActivity.this.isRatio) {
                            IPCThreeEyesActivity.this.viewHeight = r10.w / 2;
                            Log.e("屏幕", "宽=" + IPCThreeEyesActivity.this.w + " 高=" + IPCThreeEyesActivity.this.h + " viewHeight=" + IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, ((int) IPCThreeEyesActivity.this.viewHeight) * 2);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutPlay, -1, ((int) IPCThreeEyesActivity.this.viewHeight) * 3);
                        } else {
                            IPCThreeEyesActivity.this.viewHeight = (r10.w / 16) * 9;
                            Log.e("屏幕", "宽=" + IPCThreeEyesActivity.this.w + " 高=" + IPCThreeEyesActivity.this.h + " viewHeight=" + IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, ((int) IPCThreeEyesActivity.this.viewHeight) * 2);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutPlay, -1, ((int) IPCThreeEyesActivity.this.viewHeight) * 3);
                        }
                        break;
                    case 2:
                        IPCThreeEyesActivity.this.binding.layoutCenter.setVisibility(0);
                        IPCThreeEyesActivity.this.binding.layoutGunOrientation.setOrientation(0);
                        if (IPCThreeEyesActivity.this.isRatio) {
                            IPCThreeEyesActivity.this.viewHeight = r10.w / 2;
                            Log.e("屏幕", "宽=" + IPCThreeEyesActivity.this.w + " 高=" + IPCThreeEyesActivity.this.h + " viewHeight=" + IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (IPCThreeEyesActivity.this.w / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutPlay, -1, (((IPCThreeEyesActivity.this.w / 2) / 16) * 9) + ((IPCThreeEyesActivity.this.w / 16) * 9));
                        } else {
                            IPCThreeEyesActivity.this.viewHeight = (r10.w / 16) * 9;
                            Log.e("屏幕", "宽=" + IPCThreeEyesActivity.this.w + " 高=" + IPCThreeEyesActivity.this.h + " viewHeight=" + IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutPlay, -1, ((int) IPCThreeEyesActivity.this.viewHeight) * 2);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (int) IPCThreeEyesActivity.this.viewHeight);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                            CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, IPCThreeEyesActivity.this.w / 2, ((IPCThreeEyesActivity.this.w / 2) / 16) * 9);
                        }
                        IPCThreeEyesActivity.this.binding.playerBall.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerGun2.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerGun1.setVisibility(0);
                        IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                            IPCThreeEyesActivity.this.binding.ivNightBottom.setVisibility(8);
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                        layoutParams2.addRule(13, -1);
                        IPCThreeEyesActivity.this.binding.layoutGun.setLayoutParams(layoutParams2);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, -1, (IPCThreeEyesActivity.this.w / 16) * 9);
                        break;
                    case 3:
                        IPCThreeEyesActivity.this.binding.playerBall.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerGun1.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerGun2.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                            IPCThreeEyesActivity.this.binding.ivNightBottom.setVisibility(8);
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, -1, (IPCThreeEyesActivity.this.w / 16) * 9);
                        break;
                    case 4:
                        IPCThreeEyesActivity.this.binding.playerGun1.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerGun2.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.layoutGun.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerBall.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                            IPCThreeEyesActivity.this.binding.ivNightBottom.setVisibility(0);
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, (IPCThreeEyesActivity.this.w / 16) * 9);
                        break;
                }
            }
        });
        this.binding.tvZoomBack.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.17
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.llMoreDoubleEye.setSelected(false);
                IPCThreeEyesActivity.this.binding.layoutMore.setVisibility(8);
                IPCThreeEyesActivity.this.binding.layoutZoom.setVisibility(8);
                IPCThreeEyesActivity.this.binding.rlTouchView.setVisibility(0);
            }
        });
        this.binding.playerBall.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeEyesActivity.18
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
                throw new UnsupportedOperationException("Method not decompiled: activity.IPCThreeEyesActivity.AnonymousClass18.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        });
        this.binding.zoomReduceBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeEyesActivity.19
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCThreeEyesActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_press_ipc);
                    if (IPCThreeEyesActivity.this.onTouchTimer == null) {
                        IPCThreeEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeEyesActivity.19.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeEyesActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCThreeEyesActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_ipc);
                    if (IPCThreeEyesActivity.this.onTouchTimer != null) {
                        IPCThreeEyesActivity.this.onTouchTimer.cancel();
                        IPCThreeEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.btZoomReduceBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeEyesActivity.20
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCThreeEyesActivity.this.binding.btZoomReduceBtn.setBackgroundResource(R.drawable.reduce_press_ipc);
                    if (IPCThreeEyesActivity.this.onTouchTimer == null) {
                        IPCThreeEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeEyesActivity.20.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeEyesActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCThreeEyesActivity.this.binding.btZoomReduceBtn.setBackgroundResource(R.drawable.reduce_ipc);
                    if (IPCThreeEyesActivity.this.onTouchTimer != null) {
                        IPCThreeEyesActivity.this.onTouchTimer.cancel();
                        IPCThreeEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.zoomAddBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeEyesActivity.21
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCThreeEyesActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_press_ipc);
                    if (IPCThreeEyesActivity.this.onTouchTimer == null) {
                        IPCThreeEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeEyesActivity.21.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeEyesActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCThreeEyesActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_ipc);
                    if (IPCThreeEyesActivity.this.onTouchTimer != null) {
                        IPCThreeEyesActivity.this.onTouchTimer.cancel();
                        IPCThreeEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.btZoomAddBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeEyesActivity.22
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCThreeEyesActivity.this.binding.btZoomAddBtn.setBackgroundResource(R.drawable.add_press_ipc);
                    if (IPCThreeEyesActivity.this.onTouchTimer == null) {
                        IPCThreeEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeEyesActivity.22.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeEyesActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCThreeEyesActivity.this.binding.btZoomAddBtn.setBackgroundResource(R.drawable.add_ipc);
                    if (IPCThreeEyesActivity.this.onTouchTimer != null) {
                        IPCThreeEyesActivity.this.onTouchTimer.cancel();
                        IPCThreeEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.fullAddZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeEyesActivity.23
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCThreeEyesActivity.this.onTouchTimer == null) {
                        IPCThreeEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeEyesActivity.23.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeEyesActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCThreeEyesActivity.this.onTouchTimer != null) {
                    IPCThreeEyesActivity.this.onTouchTimer.cancel();
                    IPCThreeEyesActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.fullReduceZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeEyesActivity.24
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCThreeEyesActivity.this.onTouchTimer == null) {
                        IPCThreeEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeEyesActivity.24.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeEyesActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCThreeEyesActivity.this.onTouchTimer != null) {
                    IPCThreeEyesActivity.this.onTouchTimer.cancel();
                    IPCThreeEyesActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.focusReduceBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.25
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.changeFocus(0);
            }
        });
        this.binding.focusAddBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.26
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.changeFocus(1);
            }
        });
        this.zoom.observe(this, new Observer<Float>() { // from class: activity.IPCThreeEyesActivity.27
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Float f) {
                Log.d(IPCThreeEyesActivity.this.TAG, "changeOpticalZoom:- " + f);
                if (f != null) {
                    if (f.floatValue() > 1.0f) {
                        IPCThreeEyesActivity.this.binding.tvZoom.setVisibility(0);
                    } else {
                        IPCThreeEyesActivity.this.binding.tvZoom.setVisibility(8);
                    }
                }
            }
        });
        this.binding.qualityBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.28
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.qualityDlg.setVisibility(0);
                IPCThreeEyesActivity.this.binding.qualityDlg.bringToFront();
                IPCThreeEyesActivity.this.changeQualityDlgView(SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeEyesActivity.this.ballDevice.getIotId()));
            }
        });
        this.binding.tvHQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.29
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.qualityDlg.setVisibility(8);
                IPCThreeEyesActivity.this.changeDefinition(2);
            }
        });
        this.binding.tvMQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.30
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.qualityDlg.setVisibility(8);
                IPCThreeEyesActivity.this.changeDefinition(1);
            }
        });
        this.binding.tvLQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.31
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.qualityDlg.setVisibility(8);
                IPCThreeEyesActivity.this.changeDefinition(0);
            }
        });
        this.binding.tvLight1.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.32
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.lightDlg.setVisibility(8);
                IPCThreeEyesActivity.this.updateNightMode(0, IPCThreeEyesActivity.this.selectIotId);
            }
        });
        this.binding.tvLight2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.33
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.lightDlg.setVisibility(8);
                IPCThreeEyesActivity.this.updateNightMode(1, IPCThreeEyesActivity.this.selectIotId);
            }
        });
        this.binding.tvLight3.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.34
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.lightDlg.setVisibility(8);
                IPCThreeEyesActivity.this.updateNightMode(2, IPCThreeEyesActivity.this.selectIotId);
            }
        });
        this.binding.videoPlayIbtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.35
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.dismissPlayButton();
                if (IPCThreeEyesActivity.this.lowPowerMode == 0) {
                    IPCThreeEyesActivity.this.wakeUpDevice();
                    IPCThreeEyesActivity.this.wakeUpDeviceHandel();
                } else {
                    IPCThreeEyesActivity.this.playLive();
                }
            }
        });
        this.binding.btPresetInvoke.setOnClickListener(new AnonymousClass36());
        this.binding.btPresetAdd.setOnClickListener(new AnonymousClass37());
        this.binding.fullSwitchWindow.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.38
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.access$3308(IPCThreeEyesActivity.this);
                if (IPCThreeEyesActivity.this.showMode > 4) {
                    IPCThreeEyesActivity.this.showMode = 0;
                }
                if (IPCThreeEyesActivity.this.isHorizontal && IPCThreeEyesActivity.this.showMode == 1) {
                    IPCThreeEyesActivity.this.showMode = 2;
                }
                IPCThreeEyesActivity.this.binding.lightDlg.setVisibility(8);
                switch (IPCThreeEyesActivity.this.showMode) {
                    case 0:
                    case 1:
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                        layoutParams.removeRule(13);
                        IPCThreeEyesActivity.this.binding.layoutGun.setLayoutParams(layoutParams);
                        IPCThreeEyesActivity.this.binding.playerGun1.setVisibility(0);
                        IPCThreeEyesActivity.this.binding.playerGun2.setVisibility(0);
                        IPCThreeEyesActivity.this.binding.layoutGun.setVisibility(0);
                        IPCThreeEyesActivity.this.binding.playerBall.setVisibility(0);
                        IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, IPCThreeEyesActivity.this.w / 2);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, IPCThreeEyesActivity.this.h / 2, IPCThreeEyesActivity.this.w / 2);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, IPCThreeEyesActivity.this.h / 2, IPCThreeEyesActivity.this.w / 2);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, IPCThreeEyesActivity.this.h / 2, IPCThreeEyesActivity.this.w / 2);
                        break;
                    case 2:
                        IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                        iPCThreeEyesActivity.selectIotId = iPCThreeEyesActivity.gunDevice1.getIotId();
                        IPCThreeEyesActivity.this.binding.playerGun2.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerBall.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerGun1.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, -1);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun1, -1, -1);
                        break;
                    case 3:
                        IPCThreeEyesActivity iPCThreeEyesActivity2 = IPCThreeEyesActivity.this;
                        iPCThreeEyesActivity2.selectIotId = iPCThreeEyesActivity2.gunDevice2.getIotId();
                        IPCThreeEyesActivity.this.binding.playerGun1.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerBall.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerGun2.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.layoutGun, -1, -1);
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerGun2, -1, -1);
                        break;
                    case 4:
                        IPCThreeEyesActivity iPCThreeEyesActivity3 = IPCThreeEyesActivity.this;
                        iPCThreeEyesActivity3.selectIotId = iPCThreeEyesActivity3.ballDevice.getIotId();
                        IPCThreeEyesActivity.this.binding.playerGun1.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerGun2.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.layoutGun.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.playerBall.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                            IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(0);
                            IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        CommonActivity.setViewLayoutParams(IPCThreeEyesActivity.this.binding.playerBall, -1, -1);
                        break;
                }
            }
        });
        this.binding.layoutPlay.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.39
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.setFloatBarState();
            }
        });
        this.binding.ivNightTop.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.40
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCThreeEyesActivity.this.showMode == 0 || IPCThreeEyesActivity.this.showMode == 1) {
                    IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                    iPCThreeEyesActivity.selectIotId = iPCThreeEyesActivity.gunDevice1.getIotId();
                }
                int i = 0;
                for (int i2 = 0; i2 < IPCThreeEyesActivity.this.nightModelList.size(); i2++) {
                    if (((String) IPCThreeEyesActivity.this.nightModelList.get(i2)).equals(IPCThreeEyesActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(IPCThreeEyesActivity.this.selectIotId)])) {
                        i = i2;
                    }
                }
                IPCThreeEyesActivity.this.nightModeFragment.showAllowingStateLoss(IPCThreeEyesActivity.this.getSupportFragmentManager(), "", i);
            }
        });
        this.binding.ivNightBottom.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.41
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCThreeEyesActivity.this.showMode == 0 || IPCThreeEyesActivity.this.showMode == 1) {
                    IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                    iPCThreeEyesActivity.selectIotId = iPCThreeEyesActivity.ballDevice.getIotId();
                }
                if (IPCThreeEyesActivity.this.showMode == 2) {
                    IPCThreeEyesActivity iPCThreeEyesActivity2 = IPCThreeEyesActivity.this;
                    iPCThreeEyesActivity2.selectIotId = iPCThreeEyesActivity2.gunDevice1.getIotId();
                }
                if (IPCThreeEyesActivity.this.showMode == 3) {
                    IPCThreeEyesActivity iPCThreeEyesActivity3 = IPCThreeEyesActivity.this;
                    iPCThreeEyesActivity3.selectIotId = iPCThreeEyesActivity3.gunDevice2.getIotId();
                }
                if (IPCThreeEyesActivity.this.showMode == 4) {
                    IPCThreeEyesActivity iPCThreeEyesActivity4 = IPCThreeEyesActivity.this;
                    iPCThreeEyesActivity4.selectIotId = iPCThreeEyesActivity4.ballDevice.getIotId();
                }
                int i = 0;
                for (int i2 = 0; i2 < IPCThreeEyesActivity.this.nightModelList.size(); i2++) {
                    if (((String) IPCThreeEyesActivity.this.nightModelList.get(i2)).equals(IPCThreeEyesActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(IPCThreeEyesActivity.this.selectIotId)])) {
                        i = i2;
                    }
                }
                IPCThreeEyesActivity.this.nightModeFragment.showAllowingStateLoss(IPCThreeEyesActivity.this.getSupportFragmentManager(), "", i);
            }
        });
        this.binding.ivFull.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.42
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCThreeEyesActivity.this.getRequestedOrientation() == 1) {
                    IPCThreeEyesActivity.this.setRequestedOrientation(0);
                } else {
                    IPCThreeEyesActivity.this.setRequestedOrientation(8);
                }
            }
        });
        this.binding.llCapture.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.43
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeEyesActivity.this.snapshot();
            }
        });
        this.binding.fullCamera.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.44
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeEyesActivity.this.snapshot();
            }
        });
        this.binding.fullVideo.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.45
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeEyesActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.llRecord.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.46
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeEyesActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.fullNightVision.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.47
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCThreeEyesActivity.this.binding.lightDlg.getVisibility() == 8) {
                    IPCThreeEyesActivity.this.changeLightDlgView(SharePreferenceManager.getInstance().getDayNightMode(IPCThreeEyesActivity.this.selectIotId));
                    IPCThreeEyesActivity.this.binding.lightDlg.setVisibility(0);
                    if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) != -1) {
                        IPCThreeEyesActivity.this.binding.tvLight1.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.tvLight2.setVisibility(8);
                        IPCThreeEyesActivity.this.binding.tvLight3.setVisibility(8);
                        StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()))).reverse();
                        for (int i = 0; i < sbReverse.length(); i++) {
                            if (sbReverse.charAt(i) - '0' == 1) {
                                if (i == 0) {
                                    IPCThreeEyesActivity.this.binding.tvLight3.setVisibility(0);
                                }
                                if (i == 1) {
                                    IPCThreeEyesActivity.this.binding.tvLight1.setVisibility(0);
                                }
                                if (i == 2) {
                                    IPCThreeEyesActivity.this.binding.tvLight2.setVisibility(0);
                                }
                            }
                        }
                        return;
                    }
                    return;
                }
                IPCThreeEyesActivity.this.binding.lightDlg.setVisibility(8);
            }
        });
        this.binding.lightDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.48
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.lightDlg.setVisibility(8);
            }
        });
        this.binding.qualityDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.49
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.qualityDlg.setVisibility(8);
            }
        });
        this.binding.speakerBtn.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.50
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeEyesActivity.this.startOrStopLiveIntercom();
            }
        });
        this.binding.fullIntercom.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.51
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeEyesActivity.this.startOrStopLiveIntercom();
            }
        });
        this.binding.llListener.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.52
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.binding.llListener.setSelected(!IPCThreeEyesActivity.this.binding.llListener.isSelected());
                IPCThreeEyesActivity.this.binding.fullSound.setSelected(IPCThreeEyesActivity.this.binding.llListener.isSelected());
                IPCThreeEyesActivity.this.playBall.setVolume(IPCThreeEyesActivity.this.binding.llListener.isSelected() ? 1.0f : 0.0f);
            }
        });
        this.binding.fullSound.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeEyesActivity.53
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeEyesActivity.this.binding.llListener.setSelected(!IPCThreeEyesActivity.this.binding.llListener.isSelected());
                IPCThreeEyesActivity.this.binding.fullSound.setSelected(IPCThreeEyesActivity.this.binding.llListener.isSelected());
                IPCThreeEyesActivity.this.playBall.setVolume(IPCThreeEyesActivity.this.binding.llListener.isSelected() ? 1.0f : 0.0f);
            }
        });
        this.binding.ivCharge4gFlow.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.54
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCThreeEyesActivity.this.binding.ivCharge4gFlow.isSelected()) {
                    IPCThreeEyesActivity.this.isNet4GSwitch();
                    return;
                }
                Intent intent = new Intent(IPCThreeEyesActivity.this.getActivity(), (Class<?>) YunThreeSelectActivity.class);
                intent.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCThreeEyesActivity.this.ballDevice);
                intent.putExtra("device1", IPCThreeEyesActivity.this.gunDevice1);
                intent.putExtra("device2", IPCThreeEyesActivity.this.gunDevice2);
                intent.putExtra("nvrDevice", IPCThreeEyesActivity.this.nvrDevice);
                IPCThreeEyesActivity.this.startActivity(intent);
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$36, reason: invalid class name */
    class AnonymousClass36 implements View.OnClickListener {
        AnonymousClass36() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            if (IPCThreeEyesActivity.this.getActivity() == null) {
                return;
            }
            ((InputMethodManager) IPCThreeEyesActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(IPCThreeEyesActivity.this.getWindow().getDecorView().getWindowToken(), 0);
            if ("".equals(((Editable) Objects.requireNonNull(IPCThreeEyesActivity.this.binding.etPreset.getText())).toString())) {
                return;
            }
            IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).changePresetLocation(Integer.parseInt(IPCThreeEyesActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.36.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.36.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.set_success, 0).show();
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$37, reason: invalid class name */
    class AnonymousClass37 implements View.OnClickListener {
        AnonymousClass37() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            if (IPCThreeEyesActivity.this.getActivity() == null) {
                return;
            }
            ((InputMethodManager) IPCThreeEyesActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(IPCThreeEyesActivity.this.getWindow().getDecorView().getWindowToken(), 0);
            if ("".equals(((Editable) Objects.requireNonNull(IPCThreeEyesActivity.this.binding.etPreset.getText())).toString())) {
                return;
            }
            IPCManager.getInstance().getDevice(IPCThreeEyesActivity.this.ballDevice.getIotId()).addPresetLocation(Integer.parseInt(IPCThreeEyesActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.37.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.37.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.set_success, 0).show();
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
        this.binding.videoPlayIbtn.setVisibility(0);
        this.binding.videoPlayIbtn.bringToFront();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPlayButton() {
        this.binding.videoPlayIbtn.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBuffering() {
        this.binding.videoBufferingBar.setVisibility(0);
        this.binding.videoBufferingBar.bringToFront();
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
        if (this.playBall.getPlayState() == 3 && (bitmapSnapShot3 = this.playBall.snapShot()) != null) {
            saveBitmap(bitmapSnapShot3, this.ballDevice.getIotId());
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
        new Thread(new Runnable() { // from class: activity.IPCThreeEyesActivity.55
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                FileOutputStream fileOutputStream;
                File file;
                String string = SpUtil.getString(IPCThreeEyesActivity.this.getActivity(), Utils.getDevSnapKey(str), "");
                long jCurrentTimeMillis = System.currentTimeMillis();
                LogEx.d(true, IPCThreeEyesActivity.this.TAG, "保存图片");
                StringBuilder sb = new StringBuilder();
                IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                sb.append(iPCThreeEyesActivity.getFilesPath(iPCThreeEyesActivity.getApplication()));
                sb.append("/snap/");
                sb.append(str);
                sb.append("/");
                String string2 = sb.toString();
                Log.d(IPCThreeEyesActivity.this.TAG, "run: puppet:dirPath====" + string2);
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
                        Log.d(IPCThreeEyesActivity.this.TAG, "run: puppet:b=====" + zCompress);
                        fileOutputStream.flush();
                        LogEx.e(true, IPCThreeEyesActivity.this.TAG, "图片保存地址: " + file.getAbsolutePath());
                        Log.d(IPCThreeEyesActivity.this.TAG, "puppet:图片保存地址: file.getAbsolutePath()======" + file.getAbsolutePath());
                        Log.d(IPCThreeEyesActivity.this.TAG, "puppet:图片保存地址: Utils.getDevSnapKey(tempIotId)======" + Utils.getDevSnapKey(str));
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
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/status/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass56());
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$56, reason: invalid class name */
    class AnonymousClass56 implements IoTCallback {
        AnonymousClass56() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            exc.printStackTrace();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
            Log.d(IPCThreeEyesActivity.this.TAG, "run: ---------------" + Thread.currentThread().getName());
            try {
                if (((org.json.JSONObject) ioTResponse.getData()).get("status").toString().equals("3")) {
                    if (SharePreferenceManager.getInstance().getIccId(IPCThreeEyesActivity.this.ballDevice.getIotId()) == null || "".equals(SharePreferenceManager.getInstance().getIccId(IPCThreeEyesActivity.this.ballDevice.getIotId()))) {
                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.56.2
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCThreeEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                IPCThreeEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                            }
                        });
                    } else {
                        new OkHttpClient().newCall(new Request.Builder().url("http://www.secueye.cn:8000/api/smsApi?iccid=" + SharePreferenceManager.getInstance().getIccId(IPCThreeEyesActivity.this.ballDevice.getIotId()) + "&method=smsStatusSecueye").get().build()).enqueue(new Callback() { // from class: activity.IPCThreeEyesActivity.56.1
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
                                            IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.56.1.1
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCThreeEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCThreeEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                    IPCThreeEyesActivity.this.isOtherCard = true;
                                                }
                                            });
                                            return;
                                        } else if (iIntValue != 200) {
                                            IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.exception_4g_data));
                                            return;
                                        }
                                    }
                                    if (!object.containsKey("values") || IPCThreeEyesActivity.this.isOtherCard) {
                                        return;
                                    }
                                    JSONObject jSONObject = object.getJSONObject("values");
                                    if (jSONObject.containsKey("status")) {
                                        if (!jSONObject.getString("status").equals("停机")) {
                                            IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.56.1.4
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCThreeEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCThreeEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                }
                                            });
                                        } else if (AppConfig.isChina) {
                                            IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.56.1.2
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    if (!IPCThreeEyesActivity.this.isHorizontal) {
                                                        IPCThreeEyesActivity.this.binding.traffic4gExpired.bringToFront();
                                                        IPCThreeEyesActivity.this.binding.immediateRenewal.bringToFront();
                                                        IPCThreeEyesActivity.this.binding.outlineTime.bringToFront();
                                                        IPCThreeEyesActivity.this.binding.videoPlayIbtn.setVisibility(8);
                                                        IPCThreeEyesActivity.this.binding.ipcOfflineText.setVisibility(8);
                                                        IPCThreeEyesActivity.this.binding.traffic4gExpired.setVisibility(0);
                                                        IPCThreeEyesActivity.this.binding.immediateRenewal.setVisibility(0);
                                                        IPCThreeEyesActivity.this.binding.outlineTime.setVisibility(0);
                                                    }
                                                    try {
                                                        IPCThreeEyesActivity.this.binding.outlineTime.setText(((Object) IPCThreeEyesActivity.this.getResources().getText(R.string.time_of_off_line)) + "：" + TimeUtil.TimeStamp2Date(((org.json.JSONObject) ioTResponse.getData()).get("time").toString()));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    IPCThreeEyesActivity.this.needRecharge = true;
                                                }
                                            });
                                        } else {
                                            IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.56.1.3
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCThreeEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCThreeEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
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
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).queryAPList(new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.57
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null) {
                    return;
                }
                try {
                    IoTResponse ioTResponse = (IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class);
                    if (ioTResponse.getCode() != 200) {
                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.57.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCThreeEyesActivity.this, IPCThreeEyesActivity.this.getString(R.string.get_wifi_failed), 0).show();
                            }
                        });
                    } else {
                        Object data = ioTResponse.getData();
                        if (data != null) {
                            try {
                                JSONArray jSONArray = ((JSONObject) data).getJSONArray("APList");
                                IPCThreeEyesActivity.this.wifiBeanList = JSON.parseArray(jSONArray.toString(), WifiBean.class);
                                IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.57.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCThreeEyesActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCThreeEyesActivity.this.FourGChangeDialog();
                                        IPCThreeEyesActivity.this.getWiFiListSucceed(IPCThreeEyesActivity.this.wifiBeanList);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.57.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCThreeEyesActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getString(R.string.query_wifi_list_fail));
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
        this.f1576dialog = new AlertDialog.Builder(this).setView(viewInflate).create();
        this.f1576dialog.setCanceledOnTouchOutside(true);
        this.f1576dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.f1576dialog.show();
        int i = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams attributes = this.f1576dialog.getWindow().getAttributes();
        attributes.width = (int) (((double) i) * 0.95d);
        this.f1576dialog.getWindow().setAttributes(attributes);
        this.f1576dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCThreeEyesActivity.58
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCThreeEyesActivity.this.cancelCount();
            }
        });
        ((Button) viewInflate.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.59
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity.this.f1576dialog.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new IpcWiFiAdapter(R.layout.item_wifi_ipc);
        this.mAdapter.bindToRecyclerView(recyclerView);
        this.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: activity.IPCThreeEyesActivity.60
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i2) {
                WifiBean wifiBean = IPCThreeEyesActivity.this.mAdapter.getData().get(i2);
                if (wifiBean.isCurrentWifi()) {
                    return;
                }
                IPCThreeEyesActivity.this.f1576dialog.dismiss();
                IPCThreeEyesActivity.this.selectSsid = wifiBean.getSsid();
                IPCThreeEyesActivity.this.inputDialogView.setTitle(IPCThreeEyesActivity.this.selectSsid);
                IPCThreeEyesActivity.this.inputDialogView.show(IPCThreeEyesActivity.this.getSupportFragmentManager(), IPCThreeEyesActivity.this.TAG);
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
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCThreeEyesActivity.61
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCThreeEyesActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.62
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.63
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass64(60000L, 4000L, i, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$64, reason: invalid class name */
    class AnonymousClass64 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ int val$position;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass64(long j, long j2, int i, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
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
            SettingsCtrl.getInstance().getProperties(IPCThreeEyesActivity.this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeEyesActivity.64.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCThreeEyesActivity.this.ballDevice.getIotId()) == AnonymousClass64.this.val$position) {
                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.64.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCThreeEyesActivity.this.playLive();
                                AnonymousClass64.this.val$progressBarText.setVisibility(8);
                                AnonymousClass64.this.val$progressBar.setVisibility(8);
                                AnonymousClass64.this.val$imageViewText.setVisibility(0);
                                AnonymousClass64.this.val$imageViewText.setText(IPCThreeEyesActivity.this.getString(R.string.switched_success));
                                AnonymousClass64.this.val$imageView.setVisibility(0);
                                AnonymousClass64.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass64.this.val$imageButton.setVisibility(0);
                                Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                        AnonymousClass64.this.cancel();
                        IPCThreeEyesActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.64.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass64.this.val$progressBarText.setVisibility(8);
                    AnonymousClass64.this.val$progressBar.setVisibility(8);
                    AnonymousClass64.this.val$imageViewText.setVisibility(0);
                    AnonymousClass64.this.val$imageViewText.setText(IPCThreeEyesActivity.this.getString(R.string.switched_fail));
                    AnonymousClass64.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass64.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCThreeEyesActivity.this.countDownTimer = null;
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
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCThreeEyesActivity.65
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCThreeEyesActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.66
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.67
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass68(60000L, 4000L, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$68, reason: invalid class name */
    class AnonymousClass68 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass68(long j, long j2, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(IPCThreeEyesActivity.this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeEyesActivity.68.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getWifiConfigIsExist(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.68.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass68.this.val$progressBarText.setVisibility(8);
                                AnonymousClass68.this.val$progressBar.setVisibility(8);
                                AnonymousClass68.this.val$imageViewText.setVisibility(0);
                                AnonymousClass68.this.val$imageViewText.setText(IPCThreeEyesActivity.this.getString(R.string.switched_success));
                                AnonymousClass68.this.val$imageView.setVisibility(0);
                                AnonymousClass68.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass68.this.val$imageButton.setVisibility(0);
                            }
                        });
                        AnonymousClass68.this.cancel();
                        IPCThreeEyesActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.68.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass68.this.val$progressBarText.setVisibility(8);
                    AnonymousClass68.this.val$progressBar.setVisibility(8);
                    AnonymousClass68.this.val$imageViewText.setVisibility(0);
                    AnonymousClass68.this.val$imageViewText.setText(IPCThreeEyesActivity.this.getString(R.string.switched_fail));
                    AnonymousClass68.this.val$imageView.setVisibility(0);
                    AnonymousClass68.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass68.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCThreeEyesActivity.this.countDownTimer = null;
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
        ((Button) viewInflate.findViewById(R.id.btn_left)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.69
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        ((Button) viewInflate.findViewById(R.id.btn_right)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.70
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                iPCThreeEyesActivity.switch4gMode(iPCThreeEyesActivity.getString(R.string.Net4GEnableSwitch), 2);
                alertDialogCreate.dismiss();
                IPCThreeEyesActivity.this.FourGChangeDialog(2, alertDialogCreate);
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
        this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.71
            @Override // java.lang.Runnable
            public void run() {
                new BaseDialog.Builder().view(R.layout.dialog_common).content(IPCThreeEyesActivity.this.getString(R.string.sd_card_not_initialized)).leftBtnText(IPCThreeEyesActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.71.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        SharePreferenceManager.getInstance().setFirstFormatInIpc(IPCThreeEyesActivity.this.ballDevice.getIotId(), false);
                    }
                }).rightBtnText(IPCThreeEyesActivity.this.getString(R.string.format)).clickRight(new View.OnClickListener() { // from class: activity.IPCThreeEyesActivity.71.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Intent intent = new Intent(IPCThreeEyesActivity.this, (Class<?>) StorageStatusActivity.class);
                        intent.putExtra("totalStorage", f);
                        intent.putExtra("remainStorage", f2);
                        intent.putExtra("storageStatusValues", i);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCThreeEyesActivity.this.ballDevice);
                        if (IPCThreeEyesActivity.this.gunDevice1 != null) {
                            bundle.putSerializable("device1", IPCThreeEyesActivity.this.gunDevice1);
                        }
                        if (IPCThreeEyesActivity.this.gunDevice2 != null) {
                            bundle.putSerializable("device2", IPCThreeEyesActivity.this.gunDevice2);
                        }
                        if (IPCThreeEyesActivity.this.nvrDevice != null) {
                            bundle.putSerializable("nvrDevice", IPCThreeEyesActivity.this.nvrDevice);
                        }
                        intent.putExtras(bundle);
                        IPCThreeEyesActivity.this.startActivity(intent);
                        IPCThreeEyesActivity.this.needTFInit = false;
                    }
                }).canCancel(false).create().show(IPCThreeEyesActivity.this.getSupportFragmentManager(), "");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpDevice() {
        if (this.playBall.getPlayState() == 3 || this.playGun1.getPlayState() == 3 || this.playGun2.getPlayState() == 3 || !isActivityForeground()) {
            return;
        }
        this.countWakeUp = 0;
        HashMap map = new HashMap();
        map.put(Constants.LowPowerAppStatus, 1);
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.72
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                IPCThreeEyesActivity.this.needWakeUp = true;
                if (IPCThreeEyesActivity.this.playBall != null && IPCThreeEyesActivity.this.playBall.getPlayState() != 3) {
                    SettingsCtrl.getInstance().getProperties(IPCThreeEyesActivity.this.ballDevice.getIotId(), new AnonymousClass1());
                }
                IPCThreeEyesActivity.access$6808(IPCThreeEyesActivity.this);
                IPCThreeEyesActivity.this.wakeUpHandler.sendEmptyMessageDelayed(1, 8000L);
            }

            /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$72$1, reason: invalid class name */
            class AnonymousClass1 implements MyCallback {
                AnonymousClass1() {
                }

                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (SharePreferenceManager.getInstance().getLowPowerStatus(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                        Handler handler = IPCThreeEyesActivity.this.wakeUpHandler;
                        final IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                        handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCThreeEyesActivity$72$1$X_f09q-noXSaSv3_igVtdA8daDw
                            @Override // java.lang.Runnable
                            public final void run() {
                                iPCThreeEyesActivity.playLive();
                            }
                        });
                    }
                }
            }
        });
        LivePlayer livePlayer = this.playBall;
        if (livePlayer != null && livePlayer.getPlayState() != 3) {
            SettingsCtrl.getInstance().getProperties(this.ballDevice.getIotId(), new AnonymousClass73());
        }
        HashMap map2 = new HashMap();
        map2.put(Constants.LowPowerWakeUp, 1);
        IPCManager.getInstance().getDevice(this.nvrDevice.getIotId()).setProperties(map2, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.74
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$73, reason: invalid class name */
    class AnonymousClass73 implements MyCallback {
        AnonymousClass73() {
        }

        @Override // tools.MyCallback
        public void onComplete(boolean z) {
            if (SharePreferenceManager.getInstance().getLowPowerStatus(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                Handler handler = IPCThreeEyesActivity.this.wakeUpHandler;
                final IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCThreeEyesActivity$73$CM_kfFlnEFou4l15X7_rkpbugRw
                    @Override // java.lang.Runnable
                    public final void run() {
                        iPCThreeEyesActivity.playLive();
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

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$75, reason: invalid class name */
    class AnonymousClass75 extends Handler {
        AnonymousClass75() {
        }

        @Override // android.os.Handler
        public void handleMessage(@NonNull Message message) {
            if (message.what == 1) {
                if (IPCThreeEyesActivity.this.playBall.getPlayState() != 3) {
                    IPCThreeEyesActivity.access$6808(IPCThreeEyesActivity.this);
                    if (SharePreferenceManager.getInstance().getNetState(IPCThreeEyesActivity.this.ballDevice.getIotId()) != 3) {
                        Handler handler = IPCThreeEyesActivity.this.handler;
                        final IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                        handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCThreeEyesActivity$75$fGDtVeCD20gQ9rXxKlexnJwIa44
                            @Override // java.lang.Runnable
                            public final void run() {
                                iPCThreeEyesActivity.playLive();
                            }
                        });
                    }
                    if (IPCThreeEyesActivity.this.countWakeUp < 5) {
                        IPCThreeEyesActivity.this.wakeUpHandler.sendEmptyMessageDelayed(1, 8000L);
                        return;
                    }
                    return;
                }
                return;
            }
            if (message.what == 2) {
                IPCThreeEyesActivity.this.wakeUpDevice();
                IPCThreeEyesActivity.this.wakeUpDeviceHandel();
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
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.76
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() == 200) {
                            SharePreferenceManager.getInstance().setStreamVideoQuality(IPCThreeEyesActivity.this.ballDevice.getIotId(), i);
                            IPCThreeEyesActivity.this.binding.qualityBtn.setText((CharSequence) IPCThreeEyesActivity.this.definitionList.get(SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeEyesActivity.this.ballDevice.getIotId())));
                        } else {
                            IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.76.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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
        if (this.showMode == 1) {
            this.binding.layoutCenter.setVisibility(0);
            this.binding.layoutGunOrientation.setOrientation(0);
            if (this.isRatio) {
                this.viewHeight = this.w / 2;
                Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
                MyGlTextureView myGlTextureView = this.binding.playerGun1;
                int i = this.w;
                setViewLayoutParams(myGlTextureView, i / 2, ((i / 2) / 16) * 9);
                MyGlTextureView myGlTextureView2 = this.binding.playerGun2;
                int i2 = this.w;
                setViewLayoutParams(myGlTextureView2, i2 / 2, ((i2 / 2) / 16) * 9);
                setViewLayoutParams(this.binding.layoutGun, -1, ((this.w / 2) / 16) * 9);
                setViewLayoutParams(this.binding.playerBall, -1, (this.w / 16) * 9);
                RelativeLayout relativeLayout = this.binding.layoutPlay;
                int i3 = this.w;
                setViewLayoutParams(relativeLayout, -1, (((i3 / 2) / 16) * 9) + ((i3 / 16) * 9));
            } else {
                this.viewHeight = (this.w / 16) * 9;
                Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
                setViewLayoutParams(this.binding.layoutPlay, -1, ((int) this.viewHeight) * 2);
                setViewLayoutParams(this.binding.layoutGun, -1, (int) this.viewHeight);
                setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
                MyGlTextureView myGlTextureView3 = this.binding.playerGun1;
                int i4 = this.w;
                setViewLayoutParams(myGlTextureView3, i4 / 2, ((i4 / 2) / 16) * 9);
                MyGlTextureView myGlTextureView4 = this.binding.playerGun2;
                int i5 = this.w;
                setViewLayoutParams(myGlTextureView4, i5 / 2, ((i5 / 2) / 16) * 9);
            }
        }
        this.binding.layoutTop.setVisibility(8);
        this.binding.ivSetting.setVisibility(8);
        this.binding.layoutControl.setVisibility(8);
        this.binding.ivNightTop.setVisibility(8);
        this.binding.ivNightBottom.setVisibility(8);
        this.binding.ivFull.setVisibility(8);
        this.binding.llFull.setVisibility(8);
        this.binding.immediateRenewal.setVisibility(8);
        this.binding.traffic4gExpired.setVisibility(8);
        this.binding.outlineTime.setVisibility(8);
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
        switch (this.showMode) {
            case 0:
            case 1:
                this.binding.playerGun1.setVisibility(0);
                this.binding.playerGun2.setVisibility(0);
                this.binding.layoutGun.setVisibility(0);
                this.binding.playerBall.setVisibility(0);
                this.binding.fullNightVision.setVisibility(8);
                setViewLayoutParams(this.binding.layoutGun, -1, this.w / 2);
                setViewLayoutParams(this.binding.playerGun1, this.h / 2, this.w / 2);
                setViewLayoutParams(this.binding.playerGun2, this.h / 2, this.w / 2);
                setViewLayoutParams(this.binding.playerBall, this.h / 2, this.w / 2);
                break;
            case 2:
                this.selectIotId = this.gunDevice1.getIotId();
                this.binding.playerGun2.setVisibility(8);
                this.binding.playerBall.setVisibility(8);
                this.binding.playerGun1.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(8);
                }
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.ballDevice.getIotId()) == 1) {
                    this.binding.ivNightTop.setVisibility(8);
                    this.binding.fullNightVision.setVisibility(8);
                }
                setViewLayoutParams(this.binding.layoutGun, -1, -1);
                setViewLayoutParams(this.binding.playerGun1, -1, -1);
                break;
            case 3:
                this.selectIotId = this.gunDevice2.getIotId();
                this.binding.playerGun1.setVisibility(8);
                this.binding.playerBall.setVisibility(8);
                this.binding.playerGun2.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(8);
                }
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.ballDevice.getIotId()) == 1) {
                    this.binding.ivNightTop.setVisibility(8);
                    this.binding.fullNightVision.setVisibility(8);
                }
                setViewLayoutParams(this.binding.layoutGun, -1, -1);
                setViewLayoutParams(this.binding.playerGun2, -1, -1);
                break;
            case 4:
                this.selectIotId = this.ballDevice.getIotId();
                this.binding.playerGun1.setVisibility(8);
                this.binding.playerGun2.setVisibility(8);
                this.binding.layoutGun.setVisibility(8);
                this.binding.playerBall.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(8);
                }
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.ballDevice.getIotId()) == 1) {
                    this.binding.ivNightTop.setVisibility(8);
                    this.binding.fullNightVision.setVisibility(8);
                }
                setViewLayoutParams(this.binding.playerBall, -1, -1);
                break;
        }
        getWindow().setFlags(1024, 1024);
    }

    public void backFullScreen() {
        this.binding.layoutTop.setVisibility(0);
        this.binding.ivSetting.setVisibility(0);
        this.binding.layoutControl.setVisibility(0);
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
            this.binding.ivNightBottom.setVisibility(0);
        }
        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == 0) {
            this.binding.ivNightBottom.setVisibility(8);
        }
        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.ballDevice.getIotId()) == 1) {
            this.binding.ivNightTop.setVisibility(8);
            this.binding.fullNightVision.setVisibility(8);
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
        if (this.binding.layoutQuality.getParent() != null) {
            ((ViewGroup) this.binding.layoutQuality.getParent()).removeView(this.binding.layoutQuality);
        }
        this.binding.layoutPlay.addView(this.binding.layoutQuality);
        this.binding.layoutQuality.requestLayout();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(ScreenUtil.dp2Px(this, 5.0f), ScreenUtil.dp2Px(this, 5.0f), ScreenUtil.dp2Px(this, 5.0f), ScreenUtil.dp2Px(this, 15.0f));
        layoutParams.addRule(12, -1);
        this.binding.layoutQuality.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams2.setMargins(0, ScreenUtil.dp2Px(this, 74.0f), 0, 0);
        this.binding.layoutPlay.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, ScreenUtil.dp2Px(this, 44.0f));
        layoutParams3.setMargins(0, ScreenUtil.dp2Px(this, 30.0f), 0, 0);
        this.binding.layoutTop.setLayoutParams(layoutParams3);
        this.binding.layoutTop.setBackgroundColor(getResources().getColor(R.color.color_black));
        if (this.isRatio) {
            this.viewHeight = this.w / 2;
            Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
            MyGlTextureView myGlTextureView = this.binding.playerGun1;
            int i = this.w;
            setViewLayoutParams(myGlTextureView, i / 2, ((i / 2) / 16) * 9);
            MyGlTextureView myGlTextureView2 = this.binding.playerGun2;
            int i2 = this.w;
            setViewLayoutParams(myGlTextureView2, i2 / 2, ((i2 / 2) / 16) * 9);
            setViewLayoutParams(this.binding.layoutGun, -1, ((this.w / 2) / 16) * 9);
            setViewLayoutParams(this.binding.playerBall, -1, (this.w / 16) * 9);
            RelativeLayout relativeLayout = this.binding.layoutPlay;
            int i3 = this.w;
            setViewLayoutParams(relativeLayout, -1, (((i3 / 2) / 16) * 9) + ((i3 / 16) * 9));
        } else {
            Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
            setViewLayoutParams(this.binding.layoutPlay, -1, ((int) this.viewHeight) * 2);
            setViewLayoutParams(this.binding.layoutGun, -1, (int) this.viewHeight);
            setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
            MyGlTextureView myGlTextureView3 = this.binding.playerGun1;
            int i4 = this.w;
            setViewLayoutParams(myGlTextureView3, i4 / 2, ((i4 / 2) / 16) * 9);
            MyGlTextureView myGlTextureView4 = this.binding.playerGun2;
            int i5 = this.w;
            setViewLayoutParams(myGlTextureView4, i5 / 2, ((i5 / 2) / 16) * 9);
        }
        addControlTouchView();
        if (this.showMode == 1) {
            this.showMode = 0;
        }
        switch (this.showMode) {
            case 0:
                RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams4.removeRule(13);
                this.binding.layoutGun.setLayoutParams(layoutParams4);
                if (this.isRatio) {
                    this.isRatio = true;
                    this.viewHeight = this.w / 2;
                    Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
                    MyGlTextureView myGlTextureView5 = this.binding.playerGun1;
                    int i6 = this.w;
                    setViewLayoutParams(myGlTextureView5, i6 / 2, ((i6 / 2) / 16) * 9);
                    MyGlTextureView myGlTextureView6 = this.binding.playerGun2;
                    int i7 = this.w;
                    setViewLayoutParams(myGlTextureView6, i7 / 2, ((i7 / 2) / 16) * 9);
                    setViewLayoutParams(this.binding.layoutGun, -1, ((this.w / 2) / 16) * 9);
                    setViewLayoutParams(this.binding.playerBall, -1, (this.w / 16) * 9);
                    RelativeLayout relativeLayout2 = this.binding.layoutPlay;
                    int i8 = this.w;
                    setViewLayoutParams(relativeLayout2, -1, (((i8 / 2) / 16) * 9) + ((i8 / 16) * 9));
                } else {
                    MyGlTextureView myGlTextureView7 = this.binding.playerGun1;
                    int i9 = this.w;
                    setViewLayoutParams(myGlTextureView7, i9 / 2, ((i9 / 2) / 16) * 9);
                    MyGlTextureView myGlTextureView8 = this.binding.playerGun2;
                    int i10 = this.w;
                    setViewLayoutParams(myGlTextureView8, i10 / 2, ((i10 / 2) / 16) * 9);
                    setViewLayoutParams(this.binding.layoutGun, -1, (this.w / 16) * 9);
                    setViewLayoutParams(this.binding.playerBall, -1, (this.w / 16) * 9);
                }
                this.binding.playerGun2.setVisibility(0);
                this.binding.playerGun1.setVisibility(0);
                this.binding.layoutGun.setVisibility(0);
                this.binding.playerBall.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.ivNightTop.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == 0) {
                    this.binding.ivNightTop.setVisibility(8);
                }
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.ballDevice.getIotId()) == 1) {
                    this.binding.ivNightTop.setVisibility(8);
                    this.binding.fullNightVision.setVisibility(8);
                }
                break;
            case 1:
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.ivNightTop.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.ballDevice.getIotId()) == 0) {
                    this.binding.ivNightTop.setVisibility(8);
                }
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.ballDevice.getIotId()) == 1) {
                    this.binding.ivNightTop.setVisibility(8);
                    this.binding.fullNightVision.setVisibility(8);
                }
                this.binding.layoutCenter.setVisibility(8);
                this.binding.layoutGunOrientation.setOrientation(1);
                if (this.isRatio) {
                    this.viewHeight = this.w / 2;
                    Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
                    setViewLayoutParams(this.binding.playerGun1, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.playerGun2, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutGun, -1, ((int) this.viewHeight) * 2);
                    setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutPlay, -1, ((int) this.viewHeight) * 3);
                } else {
                    this.viewHeight = (this.w / 16) * 9;
                    Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
                    setViewLayoutParams(this.binding.playerGun1, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.playerGun2, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutGun, -1, ((int) this.viewHeight) * 2);
                    setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutPlay, -1, ((int) this.viewHeight) * 3);
                }
                break;
            case 2:
                this.binding.playerBall.setVisibility(8);
                this.binding.playerGun2.setVisibility(8);
                this.binding.playerGun1.setVisibility(0);
                RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams5.addRule(13, -1);
                this.binding.layoutGun.setLayoutParams(layoutParams5);
                setViewLayoutParams(this.binding.playerGun1, -1, (this.w / 16) * 9);
                break;
            case 3:
                this.binding.playerBall.setVisibility(8);
                this.binding.playerGun1.setVisibility(8);
                this.binding.playerGun2.setVisibility(0);
                setViewLayoutParams(this.binding.playerGun2, -1, (this.w / 16) * 9);
                break;
            case 4:
                this.binding.playerGun1.setVisibility(8);
                this.binding.playerGun2.setVisibility(8);
                this.binding.layoutGun.setVisibility(8);
                this.binding.playerBall.setVisibility(0);
                setViewLayoutParams(this.binding.playerBall, -1, (this.w / 16) * 9);
                break;
        }
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
        this.binding.playerBall.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCThreeEyesActivity.77
            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                return true;
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            @SuppressLint({"SetTextI18n"})
            public void onScaleChanged(ZoomableTextureView zoomableTextureView, float f) {
                String str;
                double dDoubleValue = new BigDecimal(f).setScale(2, 4).doubleValue();
                DecimalFormat decimalFormat = new DecimalFormat("##0");
                Log.d(IPCThreeEyesActivity.this.TAG, "onScaleChanged: " + dDoubleValue);
                if (SharePreferenceManager.getInstance().getMixZoom(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                    str = decimalFormat.format((((dDoubleValue - 1.0d) * 2.25d) + 1.0d) * ((double) IPCThreeEyesActivity.this.ZoomMax));
                } else {
                    str = decimalFormat.format(((dDoubleValue - 1.0d) * 2.25d) + 1.0d);
                }
                IPCThreeEyesActivity.this.zoom.postValue(Float.valueOf(f));
                IPCThreeEyesActivity.this.binding.tvZoom.setText(str + "X");
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCThreeEyesActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playBall.setOnErrorListener(new AnonymousClass78());
        this.playBall.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCThreeEyesActivity.79
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCThreeEyesActivity.this.dismissPlayButton();
                        IPCThreeEyesActivity.this.showBuffering();
                        if (IPCThreeEyesActivity.this.needWakeUp) {
                            IPCThreeEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCThreeEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        IPCThreeEyesActivity.this.lowPowerMode = 1;
                        IPCThreeEyesActivity.this.needWakeUp = false;
                        IPCThreeEyesActivity.this.is1100ErrorPre = 10;
                        IPCThreeEyesActivity.this.dismissSnapPicture();
                        IPCThreeEyesActivity.this.dismissBuffering();
                        IPCThreeEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_READY");
                        IPCThreeEyesActivity.this.updateInfoTv();
                        break;
                    case 4:
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_ENDED");
                        IPCThreeEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                        IPCThreeEyesActivity.this.playBall.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$78, reason: invalid class name */
    class AnonymousClass78 implements OnErrorListener {
        AnonymousClass78() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCThreeEyesActivity.this.needWakeUp || IPCThreeEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                        case 7:
                            IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                            iPCThreeEyesActivity.showToast(iPCThreeEyesActivity.getString(R.string.connect_failed));
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCThreeEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCThreeEyesActivity.this.showBadNetDialog();
                                    }
                                    IPCThreeEyesActivity iPCThreeEyesActivity2 = IPCThreeEyesActivity.this;
                                    iPCThreeEyesActivity2.showToast(iPCThreeEyesActivity2.getResources().getString(R.string.play_failed_retry));
                                } else {
                                    IPCThreeEyesActivity.access$7110(IPCThreeEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeEyesActivity.this.ballDevice.getIotId());
                                    IPCThreeEyesActivity.this.playBall.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCThreeEyesActivity.this.ballDevice.getIotId()) != 3) {
                                        Handler handler = IPCThreeEyesActivity.this.handler;
                                        final IPCThreeEyesActivity iPCThreeEyesActivity3 = IPCThreeEyesActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCThreeEyesActivity$78$X0rFunHm5yIW6MC1GWVLiKNsYVI
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCThreeEyesActivity3.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCThreeEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCThreeEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCThreeEyesActivity iPCThreeEyesActivity4 = IPCThreeEyesActivity.this;
            iPCThreeEyesActivity4.showToast(iPCThreeEyesActivity4.getString(R.string.account_squeezed));
        }
    }

    private void initPlayerGun1() {
        this.playGun1 = new LivePlayer(getApplicationContext());
        this.playGun1.setTextureView(this.binding.playerGun1);
        this.binding.playerGun1.setClickable(true);
        this.playGun1.setVolume(this.binding.llListener.isSelected() ? 1.0f : 0.0f);
        this.playGun1.setVideoScalingMode(1);
        this.binding.playerGun1.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCThreeEyesActivity.80
            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                return true;
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            @SuppressLint({"SetTextI18n"})
            public void onScaleChanged(ZoomableTextureView zoomableTextureView, float f) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCThreeEyesActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playGun1.setOnErrorListener(new AnonymousClass81());
        this.playGun1.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCThreeEyesActivity.82
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCThreeEyesActivity.this.dismissPlayButton();
                        IPCThreeEyesActivity.this.showBuffering();
                        if (IPCThreeEyesActivity.this.needWakeUp) {
                            IPCThreeEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCThreeEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        if (IPCThreeEyesActivity.this.showMode == 0) {
                            if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                                IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(0);
                            }
                            if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                                IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                            }
                            if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                                IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                                IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                            }
                        }
                        IPCThreeEyesActivity.this.lowPowerMode = 1;
                        IPCThreeEyesActivity.this.needWakeUp = false;
                        IPCThreeEyesActivity.this.is1100ErrorPre = 10;
                        IPCThreeEyesActivity.this.dismissSnapPicture();
                        IPCThreeEyesActivity.this.dismissBuffering();
                        IPCThreeEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_READY");
                        IPCThreeEyesActivity.this.updateInfoTv();
                        break;
                    case 4:
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_ENDED");
                        IPCThreeEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                        IPCThreeEyesActivity.this.playGun1.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$81, reason: invalid class name */
    class AnonymousClass81 implements OnErrorListener {
        AnonymousClass81() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCThreeEyesActivity.this.needWakeUp || IPCThreeEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                        case 7:
                            IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                            iPCThreeEyesActivity.showToast(iPCThreeEyesActivity.getString(R.string.play_failed_retry));
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCThreeEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCThreeEyesActivity.this.showBadNetDialog();
                                    }
                                    IPCThreeEyesActivity iPCThreeEyesActivity2 = IPCThreeEyesActivity.this;
                                    iPCThreeEyesActivity2.showToast(iPCThreeEyesActivity2.getString(R.string.play_failed_retry));
                                } else {
                                    IPCThreeEyesActivity.access$7110(IPCThreeEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeEyesActivity.this.gunDevice1.getIotId());
                                    IPCThreeEyesActivity.this.playGun1.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCThreeEyesActivity.this.ballDevice.getIotId()) != 3) {
                                        Handler handler = IPCThreeEyesActivity.this.handler;
                                        final IPCThreeEyesActivity iPCThreeEyesActivity3 = IPCThreeEyesActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCThreeEyesActivity$81$157-8c1K8qp1UneqXUWhaUl6D3U
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCThreeEyesActivity3.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCThreeEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCThreeEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCThreeEyesActivity iPCThreeEyesActivity4 = IPCThreeEyesActivity.this;
            iPCThreeEyesActivity4.showToast(iPCThreeEyesActivity4.getString(R.string.account_squeezed));
        }
    }

    private void initPlayerGun2() {
        this.playGun2 = new LivePlayer(getApplicationContext());
        this.playGun2.setTextureView(this.binding.playerGun2);
        this.binding.playerGun2.setClickable(true);
        this.playGun2.setVolume(this.binding.llListener.isSelected() ? 1.0f : 0.0f);
        this.playGun2.setVideoScalingMode(1);
        this.binding.playerGun2.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCThreeEyesActivity.83
            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                return true;
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            @SuppressLint({"SetTextI18n"})
            public void onScaleChanged(ZoomableTextureView zoomableTextureView, float f) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCThreeEyesActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playGun2.setOnErrorListener(new AnonymousClass84());
        this.playGun2.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCThreeEyesActivity.85
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCThreeEyesActivity.this.dismissPlayButton();
                        IPCThreeEyesActivity.this.showBuffering();
                        if (IPCThreeEyesActivity.this.needWakeUp) {
                            IPCThreeEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCThreeEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        if (IPCThreeEyesActivity.this.showMode == 0) {
                            if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                                IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(0);
                            }
                            if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 0) {
                                IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                            }
                            if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                                IPCThreeEyesActivity.this.binding.ivNightTop.setVisibility(8);
                                IPCThreeEyesActivity.this.binding.fullNightVision.setVisibility(8);
                            }
                        }
                        IPCThreeEyesActivity.this.lowPowerMode = 1;
                        IPCThreeEyesActivity.this.needWakeUp = false;
                        IPCThreeEyesActivity.this.is1100ErrorPre = 10;
                        IPCThreeEyesActivity.this.dismissSnapPicture();
                        IPCThreeEyesActivity.this.dismissBuffering();
                        IPCThreeEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_READY");
                        IPCThreeEyesActivity.this.updateInfoTv();
                        break;
                    case 4:
                        LogEx.i(true, IPCThreeEyesActivity.this.TAG, "STATE_ENDED");
                        IPCThreeEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                        IPCThreeEyesActivity.this.playGun1.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$84, reason: invalid class name */
    class AnonymousClass84 implements OnErrorListener {
        AnonymousClass84() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCThreeEyesActivity.this.needWakeUp || IPCThreeEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                        case 7:
                            IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                            iPCThreeEyesActivity.showToast(iPCThreeEyesActivity.getString(R.string.play_failed_retry));
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCThreeEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCThreeEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCThreeEyesActivity.this.showBadNetDialog();
                                    }
                                    IPCThreeEyesActivity iPCThreeEyesActivity2 = IPCThreeEyesActivity.this;
                                    iPCThreeEyesActivity2.showToast(iPCThreeEyesActivity2.getString(R.string.play_failed_retry));
                                } else {
                                    IPCThreeEyesActivity.access$7110(IPCThreeEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeEyesActivity.this.gunDevice2.getIotId());
                                    IPCThreeEyesActivity.this.playGun2.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCThreeEyesActivity.this.ballDevice.getIotId()) != 3) {
                                        Handler handler = IPCThreeEyesActivity.this.handler;
                                        final IPCThreeEyesActivity iPCThreeEyesActivity3 = IPCThreeEyesActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCThreeEyesActivity$84$2DYs9jLrrsYx4WKs2s3A6vm2NXc
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCThreeEyesActivity3.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCThreeEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCThreeEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCThreeEyesActivity iPCThreeEyesActivity4 = IPCThreeEyesActivity.this;
            iPCThreeEyesActivity4.showToast(iPCThreeEyesActivity4.getString(R.string.account_squeezed));
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
        LogEx.i(true, this.TAG, "playLive");
        this.playBall.stop();
        this.playBall.setIPCLiveDataSource(this.ballDevice.getIotId(), 0, false, 0, true, 0);
        this.playBall.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCThreeEyesActivity.86
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCThreeEyesActivity.this.playBall.start();
            }
        });
        this.playBall.prepare();
        this.playGun1.stop();
        Log.d(this.TAG, "playLive: puppet:iotId1====" + this.gunDevice1.getIotId());
        this.playGun1.setIPCLiveDataSource(this.gunDevice1.getIotId(), 0, false, 0, true, 0);
        this.playGun1.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCThreeEyesActivity.87
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCThreeEyesActivity.this.playGun1.start();
            }
        });
        this.playGun1.prepare();
        this.playGun2.stop();
        Log.d(this.TAG, "playLive: puppet:iotId1====" + this.gunDevice2.getIotId());
        this.playGun2.setIPCLiveDataSource(this.gunDevice2.getIotId(), 0, false, 0, true, 0);
        this.playGun2.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCThreeEyesActivity.88
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCThreeEyesActivity.this.playGun2.start();
            }
        });
        this.playGun2.prepare();
    }

    private void initLiveIntercom() {
        this.liveIntercom = new LiveIntercomV2(this, this.ballDevice.getIotId(), LiveIntercomV2.LiveIntercomMode.SingleTalk, AudioParams.AUDIOPARAM_MONO_8K_G711A);
        this.liveIntercom.setGainLevel(-1);
        this.liveIntercom.setLiveIntercomV2Listener(new LiveIntercomV2Listener() { // from class: activity.IPCThreeEyesActivity.89
            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordBufferReceived(byte[] bArr, int i, int i2) {
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onTalkReady() {
                IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.89.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeEyesActivity.this.showToast(IPCThreeEyesActivity.this.getResources().getString(R.string.can_begin_talk));
                        if (IPCThreeEyesActivity.this.isFinishing()) {
                            return;
                        }
                        IPCThreeEyesActivity.this.whiteProgressDialog.dismiss();
                        IPCThreeEyesActivity.this.binding.speakerBtn.setSelected(true);
                        IPCThreeEyesActivity.this.binding.fullIntercom.setSelected(IPCThreeEyesActivity.this.binding.speakerBtn.isSelected());
                        IPCThreeEyesActivity.this.binding.llListener.setSelected(true);
                        IPCThreeEyesActivity.this.playBall.setVolume(IPCThreeEyesActivity.this.binding.llListener.isSelected() ? 1.0f : 0.0f);
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onError(LiveIntercomException liveIntercomException) {
                int code = liveIntercomException.getCode();
                if (code != 16) {
                    switch (code) {
                        case 1:
                            IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                            iPCThreeEyesActivity.showToast(iPCThreeEyesActivity.getString(R.string.record_error1));
                            IPCThreeEyesActivity.this.handleLiveIntercomError();
                            break;
                        case 2:
                            IPCThreeEyesActivity iPCThreeEyesActivity2 = IPCThreeEyesActivity.this;
                            iPCThreeEyesActivity2.showToast(iPCThreeEyesActivity2.getString(R.string.record_error2));
                            IPCThreeEyesActivity.this.handleLiveIntercomError();
                            break;
                        case 3:
                            IPCThreeEyesActivity iPCThreeEyesActivity3 = IPCThreeEyesActivity.this;
                            iPCThreeEyesActivity3.showToast(iPCThreeEyesActivity3.getString(R.string.record_error3));
                            IPCThreeEyesActivity.this.handleLiveIntercomError();
                            break;
                        default:
                            switch (code) {
                                case 5:
                                    IPCThreeEyesActivity iPCThreeEyesActivity4 = IPCThreeEyesActivity.this;
                                    iPCThreeEyesActivity4.showToast(iPCThreeEyesActivity4.getString(R.string.record_error4));
                                    IPCThreeEyesActivity.this.handleLiveIntercomError();
                                    break;
                                case 6:
                                    IPCThreeEyesActivity iPCThreeEyesActivity5 = IPCThreeEyesActivity.this;
                                    iPCThreeEyesActivity5.showToast(iPCThreeEyesActivity5.getString(R.string.record_error5));
                                    IPCThreeEyesActivity.this.handleLiveIntercomError();
                                    break;
                                case 7:
                                    IPCThreeEyesActivity iPCThreeEyesActivity6 = IPCThreeEyesActivity.this;
                                    iPCThreeEyesActivity6.showToast(iPCThreeEyesActivity6.getString(R.string.record_error6));
                                    IPCThreeEyesActivity.this.onRecordError();
                                    break;
                                case 8:
                                    IPCThreeEyesActivity iPCThreeEyesActivity7 = IPCThreeEyesActivity.this;
                                    iPCThreeEyesActivity7.showToast(iPCThreeEyesActivity7.getString(R.string.record_error7));
                                    IPCThreeEyesActivity.this.onRecordError();
                                    break;
                                case 9:
                                    IPCThreeEyesActivity iPCThreeEyesActivity8 = IPCThreeEyesActivity.this;
                                    iPCThreeEyesActivity8.showToast(iPCThreeEyesActivity8.getString(R.string.record_error8));
                                    IPCThreeEyesActivity.this.onRecordError();
                                    break;
                            }
                            break;
                    }
                } else {
                    IPCThreeEyesActivity iPCThreeEyesActivity9 = IPCThreeEyesActivity.this;
                    iPCThreeEyesActivity9.showToast(iPCThreeEyesActivity9.getString(R.string.record_error9));
                    IPCThreeEyesActivity.this.onRecordError();
                }
                liveIntercomException.printStackTrace();
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordStart() {
                LogEx.d(true, IPCThreeEyesActivity.this.TAG, "onRecordStart");
                IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.89.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeEyesActivity.this.binding.speakerBtn.setSelected(true);
                        IPCThreeEyesActivity.this.binding.fullIntercom.setSelected(IPCThreeEyesActivity.this.binding.speakerBtn.isSelected());
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordEnd() {
                LogEx.d(true, IPCThreeEyesActivity.this.TAG, "onRecordEnd");
                IPCThreeEyesActivity.this.liveIntercom.stop();
                IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.89.3
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeEyesActivity.this.binding.speakerBtn.setSelected(false);
                        IPCThreeEyesActivity.this.binding.fullIntercom.setSelected(IPCThreeEyesActivity.this.binding.speakerBtn.isSelected());
                        if (IPCThreeEyesActivity.this.isFinishing()) {
                            return;
                        }
                        IPCThreeEyesActivity.this.whiteProgressDialog.dismiss();
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

    /* JADX INFO: renamed from: activity.IPCThreeEyesActivity$90, reason: invalid class name */
    class AnonymousClass90 implements ViewTreeObserver.OnGlobalLayoutListener {
        AnonymousClass90() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (IPCThreeEyesActivity.this.touchView.getParent() != null) {
                ((ViewGroup) IPCThreeEyesActivity.this.touchView.getParent()).removeView(IPCThreeEyesActivity.this.touchView);
            }
            if (!IPCThreeEyesActivity.this.isLand) {
                if (IPCThreeEyesActivity.this.binding.rlTouchView.getHeight() == 0) {
                    return;
                }
                IPCThreeEyesActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad2);
                int dimensionPixelSize = IPCThreeEyesActivity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                Log.e("屏幕", "" + IPCThreeEyesActivity.this.isRatio);
                if (IPCThreeEyesActivity.this.isRatio) {
                    IPCThreeEyesActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCThreeEyesActivity.this.getActivity(), 120.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                } else {
                    IPCThreeEyesActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCThreeEyesActivity.this.getActivity(), 150.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                }
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(13, -1);
                IPCThreeEyesActivity.this.touchView.setLayoutParams(layoutParams);
                IPCThreeEyesActivity.this.binding.rlTouchView.addView(IPCThreeEyesActivity.this.touchView);
            } else {
                IPCThreeEyesActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad3);
                int dimensionPixelSize2 = IPCThreeEyesActivity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                IPCThreeEyesActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCThreeEyesActivity.this.getActivity(), 120.0f) + (dimensionPixelSize2 * 2), dimensionPixelSize2);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams2.addRule(12, -1);
                IPCThreeEyesActivity.this.touchView.setLayoutParams(layoutParams2);
                IPCThreeEyesActivity.this.binding.fullScreen.addView(IPCThreeEyesActivity.this.touchView);
            }
            IPCThreeEyesActivity.this.touchView.setListener(new JoystickTouchViewListener() { // from class: activity.IPCThreeEyesActivity.90.1
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
                    throw new UnsupportedOperationException("Method not decompiled: activity.IPCThreeEyesActivity.AnonymousClass90.AnonymousClass1.onTouch(float, float):void");
                }

                @Override // view.JoystickTouchViewListener
                public void onReset() {
                    if (IPCThreeEyesActivity.this.ptzTimer != null) {
                        IPCThreeEyesActivity.this.ptzTimer.cancel();
                        IPCThreeEyesActivity.this.ptzTimer = null;
                    }
                    IPCThreeEyesActivity.this.lastActionTypeEnum = null;
                }

                @Override // view.JoystickTouchViewListener
                public void onActionUp() {
                    Log.e("云台", "抬起");
                    IPCThreeEyesActivity.this.touchView.resetView();
                    if (IPCThreeEyesActivity.this.ptzTimer != null) {
                        IPCThreeEyesActivity.this.ptzTimer.cancel();
                        IPCThreeEyesActivity.this.ptzTimer = null;
                    }
                    IPCThreeEyesActivity.this.lastActionTypeEnum = null;
                }
            });
            IPCThreeEyesActivity.this.binding.rlTouchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    public void startPTZEx(ActionTypeEnum actionTypeEnum, SpeedEnum speedEnum) {
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).startPTZEx(actionTypeEnum.getCode(), speedEnum.getCode(), new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.91
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                String str = IPCThreeEyesActivity.this.TAG;
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
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.92
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
        LivePlayer livePlayer;
        Bitmap bitmapSnapShot;
        LivePlayer livePlayer2;
        Bitmap bitmapSnapShot2;
        LivePlayer livePlayer3;
        verifyStoragePermissions(this);
        if (this.playBall.getPlayState() != 3) {
            Toast.makeText(getActivity(), R.string.only_play_snap, 0).show();
            return;
        }
        int i = this.showMode;
        if ((i == 0 || i == 1 || i == 2) && (livePlayer = this.playGun1) != null && (bitmapSnapShot = livePlayer.snapShot()) != null) {
            Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapSnapShot, 2560, 1440, true);
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
        int i2 = this.showMode;
        if ((i2 == 0 || i2 == 1 || i2 == 3) && (livePlayer2 = this.playGun2) != null && (bitmapSnapShot2 = livePlayer2.snapShot()) != null) {
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
        int i3 = this.showMode;
        if ((i3 == 0 || i3 == 1 || i3 == 4) && (livePlayer3 = this.playBall) != null) {
            Bitmap bitmapCreateScaledBitmap3 = Bitmap.createScaledBitmap(livePlayer3.snapShot(), 2560, 1440, true);
            if (bitmapCreateScaledBitmap3 == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            } else if (bitmapCreateScaledBitmap3 != null) {
                scanFile(bitmapCreateScaledBitmap3);
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmapCreateScaledBitmap3, "", "");
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
            int i4 = this.showMode;
            if (i4 == 0 || i4 == 1 || i4 == 2) {
                try {
                    this.playGun1.startRecordingContent(new File(file, (System.currentTimeMillis() + 200) + ".mp4"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int i5 = this.showMode;
            if (i5 == 0 || i5 == 1 || i5 == 3) {
                try {
                    this.playGun2.startRecordingContent(new File(file, (System.currentTimeMillis() + 100) + ".mp4"));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            int i6 = this.showMode;
            if (i6 == 0 || i6 == 1 || i6 == 4) {
                try {
                    this.playBall.startRecordingContent(new File(file, System.currentTimeMillis() + ".mp4"));
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            showTimer();
        } else {
            this.isRecording = false;
            int i7 = this.showMode;
            if (i7 == 0 || i7 == 1 || i7 == 2) {
                this.playGun1.stopRecordingContent();
            }
            int i8 = this.showMode;
            if (i8 == 0 || i8 == 1 || i8 == 3) {
                this.playGun2.stopRecordingContent();
            }
            int i9 = this.showMode;
            if (i9 == 0 || i9 == 1 || i9 == 4) {
                this.playBall.stopRecordingContent();
            }
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
        this.timer.schedule(new TimerTask() { // from class: activity.IPCThreeEyesActivity.93
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.93.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeEyesActivity.this.binding.timer.setText(IPCThreeEyesActivity.this.transformTime(IPCThreeEyesActivity.this.i));
                    }
                });
                IPCThreeEyesActivity.this.i++;
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
            IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).changeEZoom(i, 0, SharePreferenceManager.getInstance().getStreamVideoQuality(this.ballDevice.getIotId()) == 2 ? 0 : 1, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.94
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        final JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCThreeEyesActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCThreeEyesActivity iPCThreeEyesActivity = IPCThreeEyesActivity.this;
                        boolean z2 = true;
                        if (object.getInteger("ZoomIsMax").intValue() != 1) {
                            z2 = false;
                        }
                        iPCThreeEyesActivity.ZoomIsMax = z2;
                        IPCThreeEyesActivity.this.zoom.postValue(Float.valueOf(object.getInteger("Lens").intValue()));
                        IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.94.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCThreeEyesActivity.this.binding.tvZoom.setText(object.getInteger("Lens") + "X");
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
            IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).changeZoom(i, this.binding.playerBall.getTimes(), new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.95
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCThreeEyesActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCThreeEyesActivity.this.ZoomIsMax = object.getBoolean("ZoomIsMax").booleanValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeFocus(int i) {
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).changeFocus(i, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.96
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCThreeEyesActivity.this.TAG, "invoke focus");
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
        runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.97
            @Override // java.lang.Runnable
            public void run() {
                IPCThreeEyesActivity.this.binding.speakerBtn.clearAnimation();
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
        runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.98
            @Override // java.lang.Runnable
            public void run() {
                if (IPCThreeEyesActivity.this.isFinishing()) {
                    return;
                }
                IPCThreeEyesActivity.this.whiteProgressDialog.dismiss();
            }
        });
        this.liveIntercom.stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecordError() {
        handleLiveIntercomError();
        runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.99
            @Override // java.lang.Runnable
            public void run() {
                IPCThreeEyesActivity.this.binding.speakerBtn.setSelected(false);
                IPCThreeEyesActivity.this.binding.fullIntercom.setSelected(IPCThreeEyesActivity.this.binding.speakerBtn.isSelected());
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
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE).setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.IPCThreeEyesActivity.100
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, IPCThreeEyesActivity.this.TAG, "onFailure");
                Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.share_failed, 0).show();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                final int code = ioTResponse.getCode();
                Log.e(IPCThreeEyesActivity.this.TAG, "shareDevice onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.100.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCThreeEyesActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            if (code == 2077) {
                                DialogUtil.showTipsConfirmDiaLog(IPCThreeEyesActivity.this.getActivity(), IPCThreeEyesActivity.this.getString(R.string.sharing_failed), IPCThreeEyesActivity.this.getString(R.string.sharing_tips_1) + SdkConstant.CLOUDAPI_LF + IPCThreeEyesActivity.this.getString(R.string.sharing_tips_2) + SdkConstant.CLOUDAPI_LF + IPCThreeEyesActivity.this.getString(R.string.sharing_tips_3) + SdkConstant.CLOUDAPI_LF + IPCThreeEyesActivity.this.getString(R.string.sharing_tips_4), IPCThreeEyesActivity.this.getString(R.string.i_know));
                                return;
                            }
                            Toast.makeText(IPCThreeEyesActivity.this.getActivity(), localizedMsg, 0).show();
                        }
                    });
                } else {
                    IPCThreeEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeEyesActivity.100.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCThreeEyesActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            Toast.makeText(IPCThreeEyesActivity.this.getActivity(), IPCThreeEyesActivity.this.getString(R.string.share_succeed, new Object[]{((DeviceInfoBean) IPCThreeEyesActivity.this.shareDialog2.getExtra()).getName(), str}), 0).show();
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
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: activity.IPCThreeEyesActivity.101
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.101.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setDayNightMode(str, Integer.parseInt(obj.toString()));
                            IPCThreeEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeEyesActivity.101.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCThreeEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
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
