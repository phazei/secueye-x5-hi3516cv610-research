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
import android.text.format.DateFormat;
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
import com.seculink.app.databinding.ActivityIpcameraThreeFalseEyesBinding;
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
public class IPCThreeFalseEyesActivity extends CommonActivity {
    private static String[] PERMISSIONS_STORAGE = {Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
    private static final int RECORD_UPDATE_TIME = 102;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private int WifiConfigIsExist;
    private boolean ZoomIsMax;
    private int ZoomMax;
    private DeviceInfoBean ballDevice;
    private ActivityIpcameraThreeFalseEyesBinding binding;
    private CountDownTimer countDownTimer;

    /* JADX INFO: renamed from: dialog, reason: collision with root package name */
    private AlertDialog f1577dialog;
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
    private LiveIntercomV2 liveIntercom;
    private IpcWiFiAdapter mAdapter;
    private SelectorDialogFragment nightModeFragment;
    private DeviceInfoBean nvrDevice;
    private Timer onTouchTimer;
    private LivePlayer playBall;
    private LivePlayer playGun1;
    private LivePlayer playGun2;
    private Timer ptzTimer;
    private ShareDialog shareDialog2;
    private int supportMotionDetect;
    private Timer timer;
    private TouchView touchView;
    private double viewHeight;
    private double viewHeight2;
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
    private boolean isZoomRight = false;
    private boolean isZoomLeft = false;
    private List<String> nightModelList = new ArrayList();
    private String selectIotId = "";
    private String selectSsid = "";
    private boolean havePermission = false;
    private Handler handler = new Handler(new Handler.Callback() { // from class: activity.IPCThreeFalseEyesActivity.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(@NonNull Message message) {
            int i = message.what;
            if (i == 2) {
                CharSequence charSequence = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
                IPCThreeFalseEyesActivity.this.binding.tvTime2.setText(charSequence);
                IPCThreeFalseEyesActivity.this.binding.tvTime1.setText(charSequence);
                return false;
            }
            if (i != 102) {
                return false;
            }
            IPCThreeFalseEyesActivity.this.binding.playerInfoTv.setText(((IPCThreeFalseEyesActivity.this.playBall.getCurrentPlayInfo().bitRate / 1024) / 8) + "KB/S");
            if (IPCThreeFalseEyesActivity.this.binding.playerInfoTv.getVisibility() != 0) {
                return false;
            }
            IPCThreeFalseEyesActivity.this.updateInfoTv();
            return false;
        }
    });
    private Handler wakeUpHandler = new AnonymousClass80();
    ViewTreeObserver.OnGlobalLayoutListener nGlobalLayoutListener = new AnonymousClass93();
    int i = 0;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ipcamera_three_false_eyes;
    }

    static /* synthetic */ int access$3208(IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity) {
        int i = iPCThreeFalseEyesActivity.showMode;
        iPCThreeFalseEyesActivity.showMode = i + 1;
        return i;
    }

    static /* synthetic */ int access$6308(IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity) {
        int i = iPCThreeFalseEyesActivity.countWakeUp;
        iPCThreeFalseEyesActivity.countWakeUp = i + 1;
        return i;
    }

    static /* synthetic */ int access$6710(IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity) {
        int i = iPCThreeFalseEyesActivity.is1100ErrorPre;
        iPCThreeFalseEyesActivity.is1100ErrorPre = i - 1;
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
        this.binding = (ActivityIpcameraThreeFalseEyesBinding) DataBindingUtil.setContentView(this, R.layout.activity_ipcamera_three_false_eyes);
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
            SettingsCtrl.getInstance().getProperties(this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeFalseEyesActivity.2
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            SettingsCtrl.getInstance().getProperties(this.gunDevice1.getIotId(), new MyCallback() { // from class: activity.IPCThreeFalseEyesActivity.3
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            SettingsCtrl.getInstance().getProperties(this.nvrDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeFalseEyesActivity.4
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
        Timer timer = this.ptzTimer;
        if (timer != null) {
            timer.cancel();
            this.ptzTimer = null;
        }
        stopInactivityTimer();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.playBall.stop();
        this.playGun1.stop();
        this.playGun2.stop();
        Timer timer = this.ptzTimer;
        if (timer != null) {
            timer.cancel();
            this.ptzTimer = null;
        }
        LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
        if (liveIntercomV2 != null) {
            liveIntercomV2.release();
        }
        EventBus.getDefault().unregister(this);
        stopInactivityTimer();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.binding.ipcOfflineText.setVisibility(8);
        this.binding.qualityBtn.setText(this.definitionList.get(SharePreferenceManager.getInstance().getStreamVideoQuality(this.ballDevice.getIotId())));
        SettingsCtrl.getInstance().getProperties(this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeFalseEyesActivity.5
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                if (SharePreferenceManager.getInstance().getMixZoom(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeFalseEyesActivity.this.binding.tvZoom.setVisibility(8);
                        }
                    });
                }
                if (SharePreferenceManager.getInstance().getSupportZoom(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.5.2
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeFalseEyesActivity.this.binding.tvZoom.setVisibility(8);
                        }
                    });
                } else {
                    IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.5.3
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeFalseEyesActivity.this.binding.layoutAf.setVisibility(8);
                        }
                    });
                }
                if (SharePreferenceManager.getInstance().getSensorViewDisplay(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 0) {
                    IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.5.4
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeFalseEyesActivity.this.binding.SensorView.setVisibility(8);
                        }
                    });
                } else {
                    IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.5.5
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeFalseEyesActivity.this.binding.SensorView.setVisibility(0);
                        }
                    });
                }
                if (!AppConfig.isChina) {
                    IPCThreeFalseEyesActivity.this.binding.ivCharge4gFlow.setImageResource(R.drawable.selector_server_en);
                }
                IPCThreeFalseEyesActivity.this.binding.ivCharge4gFlow.setSelected(SharePreferenceManager.getInstance().getSupport4G(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1);
                Log.e("云存=", "" + SharePreferenceManager.getInstance().getEventRecord(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) + "  4G=" + SharePreferenceManager.getInstance().getSupport4G(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()));
                if (SharePreferenceManager.getInstance().getEventRecord(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1 || SharePreferenceManager.getInstance().getSupport4G(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1) {
                    IPCThreeFalseEyesActivity.this.binding.ivCharge4gFlow.setVisibility(0);
                } else {
                    IPCThreeFalseEyesActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                }
                IPCThreeFalseEyesActivity.this.faceDetectionAbility = SharePreferenceManager.getInstance().getHumanoidTracking(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).intValue();
                IPCThreeFalseEyesActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(IPCThreeFalseEyesActivity.this.ballDevice.getIotId());
                if (IPCThreeFalseEyesActivity.this.faceDetectionAbility == 1) {
                    IPCThreeFalseEyesActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).intValue() == 1;
                } else {
                    IPCThreeFalseEyesActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1;
                }
                IPCThreeFalseEyesActivity.this.showMore();
                IPCThreeFalseEyesActivity.this.initMore();
            }
        });
        if (SharePreferenceManager.getInstance().getLowPowerSwitch(this.ballDevice.getIotId()) == 1) {
            this.lowPowerMode = SharePreferenceManager.getInstance().getLowPowerStatus(this.ballDevice.getIotId());
            if (this.lowPowerMode == 0) {
                wakeUpDevice();
            }
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
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 1) {
            this.binding.ivNightBottom.setVisibility(8);
            this.binding.ivNightTop.setVisibility(8);
            this.binding.fullNightVision.setVisibility(8);
        }
        resetInactivityTimer();
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
            IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.InactivityTimerTask.1
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCThreeFalseEyesActivity.this.playBall.getPlayState() == 3 || IPCThreeFalseEyesActivity.this.playGun1.getPlayState() == 3 || IPCThreeFalseEyesActivity.this.playGun2.getPlayState() == 3) {
                        if (IPCThreeFalseEyesActivity.this.playBall != null) {
                            IPCThreeFalseEyesActivity.this.playBall.stop();
                        }
                        if (IPCThreeFalseEyesActivity.this.playGun1 != null) {
                            IPCThreeFalseEyesActivity.this.playGun1.stop();
                        }
                        if (IPCThreeFalseEyesActivity.this.playGun2 != null) {
                            IPCThreeFalseEyesActivity.this.playGun2.stop();
                        }
                        Log.e("防沉迷", "在播放");
                        IPCThreeFalseEyesActivity.this.showPlayButton();
                        DialogUtil.showTipsConfirmDiaLog(IPCThreeFalseEyesActivity.this.getActivity(), IPCThreeFalseEyesActivity.this.getString(R.string.warm_tips), IPCThreeFalseEyesActivity.this.getString(R.string.warm_tips_1), IPCThreeFalseEyesActivity.this.getString(R.string.i_know), new DialogUtil.OnConfirmClickListener() { // from class: activity.IPCThreeFalseEyesActivity.InactivityTimerTask.1.1
                            @Override // dialog.DialogUtil.OnConfirmClickListener
                            public void ConfirmListener() {
                                IPCThreeFalseEyesActivity.this.resetInactivityTimer();
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
            topicAdapterDoubleEye.setOnItemClickListener(new AnonymousClass6());
            recyclerView.setAdapter(topicAdapterDoubleEye);
            this.mList.add(recyclerView);
        }
        this.binding.topicViewPager.setAdapter(new HomeTopicPagerAdapter(this.mList));
        this.binding.topicViewPager.setOffscreenPageLimit(size - 1);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() { // from class: activity.IPCThreeFalseEyesActivity.7
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

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$6, reason: invalid class name */
    class AnonymousClass6 implements TopicAdapterDoubleEye.OnItemClickListener {
        AnonymousClass6() {
        }

        @Override // adapter.TopicAdapterDoubleEye.OnItemClickListener
        public void onTopicItemClick(TopicBean topicBean, int i) {
            if (topicBean.getIcon() == R.drawable.icon_card_back_false || topicBean.getIcon() == R.drawable.video_back) {
                Intent intent = new Intent(IPCThreeFalseEyesActivity.this, (Class<?>) RecordVideoActivity.class);
                intent.putExtra("title", IPCThreeFalseEyesActivity.this.ballDevice.getName());
                intent.putExtra("iotId", IPCThreeFalseEyesActivity.this.ballDevice.getIotId());
                intent.putExtra("iotId2", IPCThreeFalseEyesActivity.this.gunDevice1.getIotId());
                IPCThreeFalseEyesActivity.this.startActivity(intent);
                return;
            }
            if (topicBean.getIcon() == R.drawable.icon_cloud_back_false) {
                Intent intent2 = new Intent(IPCThreeFalseEyesActivity.this.getActivity(), (Class<?>) CloudStorageActivity.class);
                intent2.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCThreeFalseEyesActivity.this.ballDevice);
                intent2.putExtra("device1", IPCThreeFalseEyesActivity.this.gunDevice1);
                intent2.putExtra("nvrDevice", IPCThreeFalseEyesActivity.this.nvrDevice);
                IPCThreeFalseEyesActivity.this.startActivity(intent2);
                return;
            }
            if (topicBean.getIcon() == R.drawable.share_ipc) {
                IPCThreeFalseEyesActivity.this.shareDialog2 = ((ShareDialog.Builder) new ShareDialog.Builder().view(R.layout.dialog_input2).leftBtnText(IPCThreeFalseEyesActivity.this.getString(R.string.cancel)).rightBtnText(IPCThreeFalseEyesActivity.this.getString(R.string.share_immediately)).clickRight(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.6.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        if (IPCThreeFalseEyesActivity.this.shareDialog2.getContent() != null) {
                            if (IPCThreeFalseEyesActivity.this.shareDialog2.getMode() != 0 || SystemUtil.isPhone(IPCThreeFalseEyesActivity.this.shareDialog2.getContent())) {
                                if (IPCThreeFalseEyesActivity.this.shareDialog2.getMode() == 1 && !SystemUtil.isEmail(IPCThreeFalseEyesActivity.this.shareDialog2.getContent())) {
                                    ToastUtils.toast(IPCThreeFalseEyesActivity.this.getActivity(), IPCThreeFalseEyesActivity.this.getString(R.string.email_invalid));
                                    return;
                                }
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(IPCThreeFalseEyesActivity.this.nvrDevice.getIotId());
                                arrayList.add(IPCThreeFalseEyesActivity.this.ballDevice.getIotId());
                                arrayList.add(IPCThreeFalseEyesActivity.this.gunDevice1.getIotId());
                                IPCThreeFalseEyesActivity.this.shareDevice(IPCThreeFalseEyesActivity.this.shareDialog2.getContent(), arrayList, IPCThreeFalseEyesActivity.this.shareDialog2.getMode() == 0 ? IPCThreeFalseEyesActivity.this.shareDialog2.getDistinct() : null);
                                return;
                            }
                            ToastUtils.toast(IPCThreeFalseEyesActivity.this.getActivity(), IPCThreeFalseEyesActivity.this.getString(R.string.phone_invalid));
                            return;
                        }
                        Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.share_user_null_error, 0).show();
                    }
                })).create();
                IPCThreeFalseEyesActivity.this.shareDialog2.setOnShareClick(new ShareDialog.OnShareClickListener() { // from class: activity.IPCThreeFalseEyesActivity.6.2
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
                IPCThreeFalseEyesActivity.this.shareDialog2.setExtra(IPCThreeFalseEyesActivity.this.ballDevice);
                IPCThreeFalseEyesActivity.this.shareDialog2.show(IPCThreeFalseEyesActivity.this.getSupportFragmentManager(), "");
                return;
            }
            if (topicBean.getTitle().equals(IPCThreeFalseEyesActivity.this.getResources().getString(R.string.zoom))) {
                IPCThreeFalseEyesActivity.this.binding.rlTouchView.setVisibility(8);
                IPCThreeFalseEyesActivity.this.binding.layoutMore.setVisibility(8);
                IPCThreeFalseEyesActivity.this.binding.layoutZoom.setVisibility(0);
                return;
            }
            if (topicBean.getTitle().equals(IPCThreeFalseEyesActivity.this.getResources().getString(R.string.track)) || topicBean.getTitle().equals(IPCThreeFalseEyesActivity.this.getResources().getString(R.string.mobile_tracking))) {
                if (SharePreferenceManager.getInstance().getFaceDetectMode(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 0) {
                    HashMap map = new HashMap();
                    map.put(Constants.FACE_DETECT_SENSITIVITY, 2);
                    IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.6.3
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, Object obj) {
                            if (!z || obj == null || "".equals(String.valueOf(obj))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.3.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                        }
                                    });
                                } else {
                                    SharePreferenceManager.getInstance().setFaceDetectMode(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 2);
                                }
                            }
                        }
                    });
                }
                if (IPCThreeFalseEyesActivity.this.faceDetectionAbility == 1) {
                    IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.4
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeFalseEyesActivity.this.showProgressDialog();
                        }
                    });
                    HashMap map2 = new HashMap();
                    if (SharePreferenceManager.getInstance().getTlrClRgn(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).intValue() == 1) {
                        if (!IPCThreeFalseEyesActivity.this.isDetecting) {
                            i = SharePreferenceManager.getInstance().getAreaDetectEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? SharePreferenceManager.getInstance().getCrossLineEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? 7 : 5 : SharePreferenceManager.getInstance().getCrossLineEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? 3 : 1;
                        } else if (SharePreferenceManager.getInstance().getAreaDetectEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).intValue() != 0) {
                            i = SharePreferenceManager.getInstance().getCrossLineEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).intValue() != 0 ? 6 : 4;
                        } else if (SharePreferenceManager.getInstance().getCrossLineEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).intValue() != 0) {
                            i = 2;
                        }
                    } else if (IPCThreeFalseEyesActivity.this.isDetecting) {
                        i = 1;
                    }
                    map2.put(Constants.IvpExSwitch, Integer.valueOf(i));
                    IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).setProperties(map2, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.6.5
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            IPCThreeFalseEyesActivity.this.dismissProgressDialog();
                            if (!z) {
                                IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.5.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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
                                        IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.5.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                            }
                                        });
                                        return;
                                    }
                                    IPCThreeFalseEyesActivity.this.isDetecting = !IPCThreeFalseEyesActivity.this.isDetecting;
                                    if (IPCThreeFalseEyesActivity.this.faceDetectionAbility == 1) {
                                        if (IPCThreeFalseEyesActivity.this.isDetecting) {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 1);
                                        } else {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 0);
                                        }
                                    } else if (IPCThreeFalseEyesActivity.this.isDetecting) {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 1);
                                    } else {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 0);
                                    }
                                    SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeFalseEyesActivity.this.selectIotId, i & 1);
                                    SharePreferenceManager.getInstance().setAreaDetectEnable(IPCThreeFalseEyesActivity.this.selectIotId, (i & 4) >> 2);
                                    SharePreferenceManager.getInstance().setCrossLineEnable(IPCThreeFalseEyesActivity.this.selectIotId, (i & 2) >> 1);
                                    IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.5.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            IPCThreeFalseEyesActivity.this.showMore();
                                            IPCThreeFalseEyesActivity.this.initMore();
                                            Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.6
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCThreeFalseEyesActivity.this.showProgressDialog();
                        }
                    });
                    HashMap map3 = new HashMap();
                    map3.put(Constants.INTELLIGENT_TRACKING, Integer.valueOf(1 ^ (IPCThreeFalseEyesActivity.this.isDetecting ? 1 : 0)));
                    IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).setProperties(map3, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.6.7
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            IPCThreeFalseEyesActivity.this.dismissProgressDialog();
                            if (!z) {
                                IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.7.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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
                                        IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.7.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                            }
                                        });
                                        return;
                                    }
                                    IPCThreeFalseEyesActivity.this.isDetecting = !IPCThreeFalseEyesActivity.this.isDetecting;
                                    if (IPCThreeFalseEyesActivity.this.faceDetectionAbility == 1) {
                                        if (IPCThreeFalseEyesActivity.this.isDetecting) {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 1);
                                        } else {
                                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 0);
                                        }
                                    } else if (IPCThreeFalseEyesActivity.this.isDetecting) {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 1);
                                    } else {
                                        SharePreferenceManager.getInstance().setIntelligentMode(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 0);
                                    }
                                    IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.7.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            IPCThreeFalseEyesActivity.this.showMore();
                                            IPCThreeFalseEyesActivity.this.initMore();
                                            Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
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
                    IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).changePresetLocation(103, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.6.8
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                                Log.e("预置位", "103");
                            }
                        }
                    });
                    return;
                } else {
                    IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).addPresetLocation(99, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.6.9
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                                Log.e("预置位", "99");
                            }
                        }
                    });
                    IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).changePresetLocation(100, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.6.10
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
            if (topicBean.getTitle().equals(IPCThreeFalseEyesActivity.this.getResources().getString(R.string.floodlight))) {
                HashMap map4 = new HashMap();
                map4.put(Constants.FloodlightSwitch, Integer.valueOf(SharePreferenceManager.getInstance().getFloodlightSwitch(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) != 1 ? 1 : 0));
                IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).setProperties(map4, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.6.11
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            new Handler().post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.6.11.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (SharePreferenceManager.getInstance().getFloodlightSwitch(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 0) {
                                        SharePreferenceManager.getInstance().setFloodlightSwitch(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 1);
                                    } else {
                                        SharePreferenceManager.getInstance().setFloodlightSwitch(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), 0);
                                    }
                                    IPCThreeFalseEyesActivity.this.showMore();
                                    IPCThreeFalseEyesActivity.this.initMore();
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void initView() {
        this.whiteProgressDialog = new WhiteProgressDialog(this);
        this.binding.tvTitle.setText(this.ballDevice.getName());
        this.binding.maxLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: activity.IPCThreeFalseEyesActivity.8
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                IPCThreeFalseEyesActivity.this.binding.maxLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                iPCThreeFalseEyesActivity.w = iPCThreeFalseEyesActivity.binding.maxLayout.getWidth();
                IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity2 = IPCThreeFalseEyesActivity.this;
                iPCThreeFalseEyesActivity2.h = iPCThreeFalseEyesActivity2.binding.maxLayout.getHeight();
                double dDoubleValue = new BigDecimal(IPCThreeFalseEyesActivity.this.w / IPCThreeFalseEyesActivity.this.h).setScale(2, 4).doubleValue();
                Log.e("屏幕", "宽高比例" + dDoubleValue);
                if (dDoubleValue >= 0.49d) {
                    IPCThreeFalseEyesActivity.this.isRatio = true;
                    IPCThreeFalseEyesActivity.this.viewHeight = r0.w / 2;
                    Log.e("屏幕", "宽=" + IPCThreeFalseEyesActivity.this.w + " 高=" + IPCThreeFalseEyesActivity.this.h + " viewHeight=" + IPCThreeFalseEyesActivity.this.viewHeight);
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun1, IPCThreeFalseEyesActivity.this.w / 2, ((IPCThreeFalseEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun2, IPCThreeFalseEyesActivity.this.w / 2, ((IPCThreeFalseEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutGun, -1, ((IPCThreeFalseEyesActivity.this.w / 2) / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.playerBall, -1, (IPCThreeFalseEyesActivity.this.w / 16) * 9);
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlay, -1, (((IPCThreeFalseEyesActivity.this.w / 2) / 16) * 9) + ((IPCThreeFalseEyesActivity.this.w / 16) * 9));
                } else {
                    IPCThreeFalseEyesActivity.this.isRatio = false;
                    IPCThreeFalseEyesActivity.this.viewHeight = (r0.w / 16) * 9;
                    IPCThreeFalseEyesActivity.this.viewHeight2 = (r0.w / 19) * 9;
                    Log.e("屏幕", "宽=" + IPCThreeFalseEyesActivity.this.w + " 高=" + IPCThreeFalseEyesActivity.this.h + " viewHeight=" + IPCThreeFalseEyesActivity.this.viewHeight);
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlay, -1, (int) (IPCThreeFalseEyesActivity.this.viewHeight + IPCThreeFalseEyesActivity.this.viewHeight2));
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutGun, -1, (int) IPCThreeFalseEyesActivity.this.viewHeight2);
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.playerBall, -1, (int) IPCThreeFalseEyesActivity.this.viewHeight);
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun1, IPCThreeFalseEyesActivity.this.w / 2, (int) IPCThreeFalseEyesActivity.this.viewHeight2);
                    CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun2, IPCThreeFalseEyesActivity.this.w / 2, (int) IPCThreeFalseEyesActivity.this.viewHeight2);
                }
                IPCThreeFalseEyesActivity.this.addControlTouchView();
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
        this.nightModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCThreeFalseEyesActivity.9
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v10, types: [int] */
            /* JADX WARN: Type inference failed for: r0v12 */
            /* JADX WARN: Type inference failed for: r0v13 */
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i3) {
                ((String) IPCThreeFalseEyesActivity.this.nightModelList.get(i3)).equals(IPCThreeFalseEyesActivity.this.infrarredMode[0]);
                ?? Equals = ((String) IPCThreeFalseEyesActivity.this.nightModelList.get(i3)).equals(IPCThreeFalseEyesActivity.this.infrarredMode[1]);
                if (((String) IPCThreeFalseEyesActivity.this.nightModelList.get(i3)).equals(IPCThreeFalseEyesActivity.this.infrarredMode[2])) {
                    Equals = 2;
                }
                IPCThreeFalseEyesActivity.this.updateNightMode(Integer.valueOf((int) Equals), IPCThreeFalseEyesActivity.this.selectIotId);
            }
        });
        this.inputDialogView = new InputDialogViewIpc.Builder().build();
        this.inputDialogView.addOnClickListener(new AnonymousClass10());
        this.showList.add(getResources().getString(R.string.show_mode4));
        this.showList.add(getResources().getString(R.string.show_mode2));
        this.showList.add(getResources().getString(R.string.show_mode2));
        this.showList.add(getResources().getString(R.string.show_mode3));
        this.definitionList.add(getString(R.string.quality_l));
        this.definitionList.add(getString(R.string.quality_m));
        this.definitionList.add(getString(R.string.quality_h));
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$10, reason: invalid class name */
    class AnonymousClass10 implements InputDialogViewIpc.OnClickListener {
        AnonymousClass10() {
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onPositiveClick(String str, Object obj) {
            IPCThreeFalseEyesActivity.this.showProgressDialog();
            IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).setAPList(IPCThreeFalseEyesActivity.this.selectSsid, str, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.10.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (z) {
                        try {
                            if (obj2 == null) {
                                IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.10.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCThreeFalseEyesActivity.this.getActivity(), IPCThreeFalseEyesActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else if (((IoTResponse) JSON.parseObject(obj2.toString()).toJavaObject(IoTResponse.class)).getCode() != 200) {
                                IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.10.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCThreeFalseEyesActivity.this.getActivity(), IPCThreeFalseEyesActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else {
                                IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.10.1.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        IPCThreeFalseEyesActivity.this.connect();
                                    }
                                });
                            }
                        } finally {
                            IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.10.1.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCThreeFalseEyesActivity.this.dismissProgressDialog();
                                }
                            });
                        }
                    }
                }
            });
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onNegativeClick() {
            IPCThreeFalseEyesActivity.this.inputDialogView.dismiss();
            IPCThreeFalseEyesActivity.this.f1577dialog.show();
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
    }

    private void initListener() {
        this.binding.ivBack.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.11
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeFalseEyesActivity.this.onBackPressed();
            }
        });
        this.binding.ivSetting.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.12
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(IPCThreeFalseEyesActivity.this, (Class<?>) SettingsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCThreeFalseEyesActivity.this.ballDevice);
                bundle.putSerializable("device1", IPCThreeFalseEyesActivity.this.gunDevice1);
                bundle.putSerializable("nvrDevice", IPCThreeFalseEyesActivity.this.nvrDevice);
                intent.putExtras(bundle);
                IPCThreeFalseEyesActivity.this.startActivity(intent);
            }
        });
        this.binding.llMoreDoubleEye.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.13
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCThreeFalseEyesActivity.this.showMode != 1) {
                    IPCThreeFalseEyesActivity.this.binding.llMoreDoubleEye.setSelected(true ^ IPCThreeFalseEyesActivity.this.binding.llMoreDoubleEye.isSelected());
                    if (IPCThreeFalseEyesActivity.this.binding.llMoreDoubleEye.isSelected()) {
                        IPCThreeFalseEyesActivity.this.showMore();
                        IPCThreeFalseEyesActivity.this.binding.rlTouchView.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.layoutZoom.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.layoutMore.setVisibility(0);
                        return;
                    }
                    IPCThreeFalseEyesActivity.this.binding.layoutMore.setVisibility(8);
                    IPCThreeFalseEyesActivity.this.binding.layoutZoom.setVisibility(8);
                    IPCThreeFalseEyesActivity.this.binding.rlTouchView.setVisibility(0);
                    return;
                }
                IPCThreeFalseEyesActivity.this.showToast("请先退出大屏模式");
            }
        });
        this.binding.btWh.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String string = IPCThreeFalseEyesActivity.this.binding.editWidth.getText().toString();
                String string2 = IPCThreeFalseEyesActivity.this.binding.editHeight.getText().toString();
                IPCThreeFalseEyesActivity.this.binding.playerGun2.Right(IPCThreeFalseEyesActivity.this, Double.parseDouble(string), Double.parseDouble(string2));
                IPCThreeFalseEyesActivity.this.binding.playerGun1.Left(IPCThreeFalseEyesActivity.this, Double.parseDouble(string), Double.parseDouble(string2));
            }
        });
        this.binding.btZoom.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.15
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String string = IPCThreeFalseEyesActivity.this.binding.editZoom.getText().toString();
                IPCThreeFalseEyesActivity.this.binding.playerGun2.Zomm(Double.parseDouble(string));
                IPCThreeFalseEyesActivity.this.binding.playerGun1.Zomm(Double.parseDouble(string));
            }
        });
        this.binding.btReset.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.16
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.playerGun2.reset();
                IPCThreeFalseEyesActivity.this.binding.playerGun1.reset();
            }
        });
        this.binding.btLeftMove.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.17
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.playerGun2.LeftMove(IPCThreeFalseEyesActivity.this, Double.parseDouble(IPCThreeFalseEyesActivity.this.binding.editX.getText().toString()), Double.parseDouble(IPCThreeFalseEyesActivity.this.binding.editY.getText().toString()));
            }
        });
        this.binding.btRightMove.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.18
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.playerGun2.RightMove(IPCThreeFalseEyesActivity.this, Double.parseDouble(IPCThreeFalseEyesActivity.this.binding.editX.getText().toString()), Double.parseDouble(IPCThreeFalseEyesActivity.this.binding.editY.getText().toString()));
            }
        });
        this.binding.llFull.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.19
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeFalseEyesActivity.access$3208(IPCThreeFalseEyesActivity.this);
                if (IPCThreeFalseEyesActivity.this.showMode > 4) {
                    IPCThreeFalseEyesActivity.this.showMode = 0;
                }
                IPCThreeFalseEyesActivity.this.setSWitch();
            }
        });
        this.binding.tvZoomBack.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.20
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.llMoreDoubleEye.setSelected(false);
                IPCThreeFalseEyesActivity.this.binding.layoutMore.setVisibility(8);
                IPCThreeFalseEyesActivity.this.binding.layoutZoom.setVisibility(8);
                IPCThreeFalseEyesActivity.this.binding.rlTouchView.setVisibility(0);
            }
        });
        this.binding.zoomReduceBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeFalseEyesActivity.21
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCThreeFalseEyesActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_press_ipc);
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer == null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeFalseEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeFalseEyesActivity.21.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeFalseEyesActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCThreeFalseEyesActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_ipc);
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer != null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer.cancel();
                        IPCThreeFalseEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.btZoomReduceBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeFalseEyesActivity.22
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCThreeFalseEyesActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_press_ipc);
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer == null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeFalseEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeFalseEyesActivity.22.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeFalseEyesActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCThreeFalseEyesActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_ipc);
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer != null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer.cancel();
                        IPCThreeFalseEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.zoomAddBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeFalseEyesActivity.23
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCThreeFalseEyesActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_press_ipc);
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer == null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeFalseEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeFalseEyesActivity.23.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeFalseEyesActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCThreeFalseEyesActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_ipc);
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer != null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer.cancel();
                        IPCThreeFalseEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.btZoomAddBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeFalseEyesActivity.24
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCThreeFalseEyesActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_press_ipc);
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer == null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeFalseEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeFalseEyesActivity.24.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeFalseEyesActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCThreeFalseEyesActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_ipc);
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer != null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer.cancel();
                        IPCThreeFalseEyesActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.fullAddZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeFalseEyesActivity.25
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer == null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeFalseEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeFalseEyesActivity.25.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeFalseEyesActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCThreeFalseEyesActivity.this.onTouchTimer != null) {
                    IPCThreeFalseEyesActivity.this.onTouchTimer.cancel();
                    IPCThreeFalseEyesActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.fullReduceZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCThreeFalseEyesActivity.26
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCThreeFalseEyesActivity.this.onTouchTimer == null) {
                        IPCThreeFalseEyesActivity.this.onTouchTimer = new Timer();
                        IPCThreeFalseEyesActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCThreeFalseEyesActivity.26.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCThreeFalseEyesActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCThreeFalseEyesActivity.this.onTouchTimer != null) {
                    IPCThreeFalseEyesActivity.this.onTouchTimer.cancel();
                    IPCThreeFalseEyesActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.focusReduceBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.27
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.changeFocus(0);
            }
        });
        this.binding.focusAddBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.28
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.changeFocus(1);
            }
        });
        this.zoom.observe(this, new Observer<Float>() { // from class: activity.IPCThreeFalseEyesActivity.29
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Float f) {
                Log.d(IPCThreeFalseEyesActivity.this.TAG, "changeOpticalZoom:- " + f);
                if (f != null) {
                    if (f.floatValue() > 1.0f) {
                        IPCThreeFalseEyesActivity.this.binding.tvZoom.setVisibility(0);
                    } else {
                        IPCThreeFalseEyesActivity.this.binding.tvZoom.setVisibility(8);
                    }
                }
            }
        });
        this.binding.qualityBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.30
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.qualityDlg.setVisibility(0);
                IPCThreeFalseEyesActivity.this.binding.qualityDlg.bringToFront();
                IPCThreeFalseEyesActivity.this.changeQualityDlgView(SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()));
            }
        });
        this.binding.tvHQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.31
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.qualityDlg.setVisibility(8);
                IPCThreeFalseEyesActivity.this.changeDefinition(2);
            }
        });
        this.binding.tvMQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.32
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.qualityDlg.setVisibility(8);
                IPCThreeFalseEyesActivity.this.changeDefinition(1);
            }
        });
        this.binding.tvLQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.33
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.qualityDlg.setVisibility(8);
                IPCThreeFalseEyesActivity.this.changeDefinition(0);
            }
        });
        this.binding.tvLight1.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.34
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.lightDlg.setVisibility(8);
                IPCThreeFalseEyesActivity.this.updateNightMode(0, IPCThreeFalseEyesActivity.this.selectIotId);
            }
        });
        this.binding.tvLight2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.35
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.lightDlg.setVisibility(8);
                IPCThreeFalseEyesActivity.this.updateNightMode(1, IPCThreeFalseEyesActivity.this.selectIotId);
            }
        });
        this.binding.tvLight3.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.36
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.lightDlg.setVisibility(8);
                IPCThreeFalseEyesActivity.this.updateNightMode(2, IPCThreeFalseEyesActivity.this.selectIotId);
            }
        });
        this.binding.videoPlayIbtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.37
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.dismissPlayButton();
                if (IPCThreeFalseEyesActivity.this.lowPowerMode == 0) {
                    IPCThreeFalseEyesActivity.this.wakeUpDevice();
                } else {
                    IPCThreeFalseEyesActivity.this.playLive();
                }
            }
        });
        this.binding.btPresetInvoke.setOnClickListener(new AnonymousClass38());
        this.binding.btPresetAdd.setOnClickListener(new AnonymousClass39());
        this.binding.fullSwitchWindow.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.40
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.access$3208(IPCThreeFalseEyesActivity.this);
                if (IPCThreeFalseEyesActivity.this.showMode > 3) {
                    IPCThreeFalseEyesActivity.this.showMode = 0;
                }
                IPCThreeFalseEyesActivity.this.binding.lightDlg.setVisibility(8);
                switch (IPCThreeFalseEyesActivity.this.showMode) {
                    case 0:
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                        layoutParams.removeRule(13);
                        IPCThreeFalseEyesActivity.this.binding.layoutGun.setLayoutParams(layoutParams);
                        IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun1.setVisibility(0);
                        IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun2.setVisibility(0);
                        IPCThreeFalseEyesActivity.this.binding.layoutGun.setVisibility(0);
                        IPCThreeFalseEyesActivity.this.binding.playerBall.setVisibility(0);
                        IPCThreeFalseEyesActivity.this.binding.fullNightVision.setVisibility(8);
                        CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutGun, -1, IPCThreeFalseEyesActivity.this.w / 2);
                        CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun1, IPCThreeFalseEyesActivity.this.h / 2, IPCThreeFalseEyesActivity.this.w / 2);
                        CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun2, IPCThreeFalseEyesActivity.this.h / 2, IPCThreeFalseEyesActivity.this.w / 2);
                        CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.playerBall, IPCThreeFalseEyesActivity.this.h / 2, IPCThreeFalseEyesActivity.this.w / 2);
                        break;
                    case 1:
                        IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                        iPCThreeFalseEyesActivity.selectIotId = iPCThreeFalseEyesActivity.gunDevice1.getIotId();
                        IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun2.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.playerBall.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun1.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeFalseEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutGun, -1, -1);
                        CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun1, -1, -1);
                        break;
                    case 2:
                        IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity2 = IPCThreeFalseEyesActivity.this;
                        iPCThreeFalseEyesActivity2.selectIotId = iPCThreeFalseEyesActivity2.gunDevice2.getIotId();
                        IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun1.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.playerBall.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun2.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeFalseEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutGun, -1, -1);
                        CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun2, -1, -1);
                        break;
                    case 3:
                        IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity3 = IPCThreeFalseEyesActivity.this;
                        iPCThreeFalseEyesActivity3.selectIotId = iPCThreeFalseEyesActivity3.ballDevice.getIotId();
                        IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun1.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.layoutPlayerGun2.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.layoutGun.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.playerBall.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeFalseEyesActivity.this.binding.fullNightVision.setVisibility(0);
                        }
                        CommonActivity.setViewLayoutParams(IPCThreeFalseEyesActivity.this.binding.playerBall, -1, -1);
                        break;
                }
            }
        });
        this.binding.layoutPlay.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.41
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.setFloatBarState();
            }
        });
        this.binding.playerGun1.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.42
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.setFloatBarState();
            }
        });
        this.binding.playerGun2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.43
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.setFloatBarState();
            }
        });
        this.binding.ivNightTop.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.44
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCThreeFalseEyesActivity.this.showMode == 0 || IPCThreeFalseEyesActivity.this.showMode == 1) {
                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                    iPCThreeFalseEyesActivity.selectIotId = iPCThreeFalseEyesActivity.gunDevice1.getIotId();
                }
                int i = 0;
                for (int i2 = 0; i2 < IPCThreeFalseEyesActivity.this.nightModelList.size(); i2++) {
                    if (((String) IPCThreeFalseEyesActivity.this.nightModelList.get(i2)).equals(IPCThreeFalseEyesActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(IPCThreeFalseEyesActivity.this.selectIotId)])) {
                        i = i2;
                    }
                }
                IPCThreeFalseEyesActivity.this.nightModeFragment.showAllowingStateLoss(IPCThreeFalseEyesActivity.this.getSupportFragmentManager(), "", i);
            }
        });
        this.binding.ivNightBottom.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.45
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCThreeFalseEyesActivity.this.showMode == 0) {
                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                    iPCThreeFalseEyesActivity.selectIotId = iPCThreeFalseEyesActivity.ballDevice.getIotId();
                }
                if (IPCThreeFalseEyesActivity.this.showMode == 1) {
                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity2 = IPCThreeFalseEyesActivity.this;
                    iPCThreeFalseEyesActivity2.selectIotId = iPCThreeFalseEyesActivity2.gunDevice1.getIotId();
                }
                if (IPCThreeFalseEyesActivity.this.showMode == 2) {
                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity3 = IPCThreeFalseEyesActivity.this;
                    iPCThreeFalseEyesActivity3.selectIotId = iPCThreeFalseEyesActivity3.gunDevice2.getIotId();
                }
                if (IPCThreeFalseEyesActivity.this.showMode == 3) {
                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity4 = IPCThreeFalseEyesActivity.this;
                    iPCThreeFalseEyesActivity4.selectIotId = iPCThreeFalseEyesActivity4.ballDevice.getIotId();
                }
                int i = 0;
                for (int i2 = 0; i2 < IPCThreeFalseEyesActivity.this.nightModelList.size(); i2++) {
                    if (((String) IPCThreeFalseEyesActivity.this.nightModelList.get(i2)).equals(IPCThreeFalseEyesActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(IPCThreeFalseEyesActivity.this.selectIotId)])) {
                        i = i2;
                    }
                }
                IPCThreeFalseEyesActivity.this.nightModeFragment.showAllowingStateLoss(IPCThreeFalseEyesActivity.this.getSupportFragmentManager(), "", i);
            }
        });
        this.binding.ivFull.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.46
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCThreeFalseEyesActivity.this.getRequestedOrientation() == 1) {
                    IPCThreeFalseEyesActivity.this.setRequestedOrientation(0);
                } else {
                    IPCThreeFalseEyesActivity.this.setRequestedOrientation(8);
                }
            }
        });
        this.binding.llCapture.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.47
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeFalseEyesActivity.this.snapshot();
            }
        });
        this.binding.fullCamera.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.48
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeFalseEyesActivity.this.snapshot();
            }
        });
        this.binding.fullVideo.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.49
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeFalseEyesActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.llRecord.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.50
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeFalseEyesActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.fullNightVision.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.51
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCThreeFalseEyesActivity.this.binding.lightDlg.getVisibility() == 8) {
                    IPCThreeFalseEyesActivity.this.changeLightDlgView(SharePreferenceManager.getInstance().getDayNightMode(IPCThreeFalseEyesActivity.this.selectIotId));
                    IPCThreeFalseEyesActivity.this.binding.lightDlg.setVisibility(0);
                    if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) != -1) {
                        IPCThreeFalseEyesActivity.this.binding.tvLight1.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.tvLight2.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.tvLight3.setVisibility(8);
                        StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()))).reverse();
                        for (int i = 0; i < sbReverse.length(); i++) {
                            if (sbReverse.charAt(i) - '0' == 1) {
                                if (i == 0) {
                                    IPCThreeFalseEyesActivity.this.binding.tvLight3.setVisibility(0);
                                }
                                if (i == 1) {
                                    IPCThreeFalseEyesActivity.this.binding.tvLight1.setVisibility(0);
                                }
                                if (i == 2) {
                                    IPCThreeFalseEyesActivity.this.binding.tvLight2.setVisibility(0);
                                }
                            }
                        }
                        return;
                    }
                    return;
                }
                IPCThreeFalseEyesActivity.this.binding.lightDlg.setVisibility(8);
            }
        });
        this.binding.lightDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.52
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.lightDlg.setVisibility(8);
            }
        });
        this.binding.qualityDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.53
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.qualityDlg.setVisibility(8);
            }
        });
        this.binding.speakerBtn.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.54
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeFalseEyesActivity.this.startOrStopLiveIntercom();
            }
        });
        this.binding.fullIntercom.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.55
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeFalseEyesActivity.this.startOrStopLiveIntercom();
            }
        });
        this.binding.llListener.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.56
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.llListener.setSelected(!IPCThreeFalseEyesActivity.this.binding.llListener.isSelected());
                IPCThreeFalseEyesActivity.this.binding.fullSound.setSelected(IPCThreeFalseEyesActivity.this.binding.llListener.isSelected());
                IPCThreeFalseEyesActivity.this.playBall.setVolume(IPCThreeFalseEyesActivity.this.binding.llListener.isSelected() ? 1.0f : 0.0f);
            }
        });
        this.binding.fullSound.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCThreeFalseEyesActivity.57
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCThreeFalseEyesActivity.this.binding.llListener.setSelected(!IPCThreeFalseEyesActivity.this.binding.llListener.isSelected());
                IPCThreeFalseEyesActivity.this.binding.fullSound.setSelected(IPCThreeFalseEyesActivity.this.binding.llListener.isSelected());
                IPCThreeFalseEyesActivity.this.playBall.setVolume(IPCThreeFalseEyesActivity.this.binding.llListener.isSelected() ? 1.0f : 0.0f);
            }
        });
        this.binding.ivCharge4gFlow.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.58
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCThreeFalseEyesActivity.this.binding.ivCharge4gFlow.isSelected()) {
                    IPCThreeFalseEyesActivity.this.isNet4GSwitch();
                    return;
                }
                Intent intent = new Intent(IPCThreeFalseEyesActivity.this.getActivity(), (Class<?>) YunDoubleSelectActivity.class);
                intent.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCThreeFalseEyesActivity.this.ballDevice);
                intent.putExtra("device1", IPCThreeFalseEyesActivity.this.gunDevice1);
                intent.putExtra("nvrDevice", IPCThreeFalseEyesActivity.this.nvrDevice);
                IPCThreeFalseEyesActivity.this.startActivity(intent);
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$38, reason: invalid class name */
    class AnonymousClass38 implements View.OnClickListener {
        AnonymousClass38() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            if (IPCThreeFalseEyesActivity.this.getActivity() == null) {
                return;
            }
            ((InputMethodManager) IPCThreeFalseEyesActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(IPCThreeFalseEyesActivity.this.getWindow().getDecorView().getWindowToken(), 0);
            if ("".equals(((Editable) Objects.requireNonNull(IPCThreeFalseEyesActivity.this.binding.etPreset.getText())).toString())) {
                return;
            }
            IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).changePresetLocation(Integer.parseInt(IPCThreeFalseEyesActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.38.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.38.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.set_success, 0).show();
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$39, reason: invalid class name */
    class AnonymousClass39 implements View.OnClickListener {
        AnonymousClass39() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            if (IPCThreeFalseEyesActivity.this.getActivity() == null) {
                return;
            }
            ((InputMethodManager) IPCThreeFalseEyesActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(IPCThreeFalseEyesActivity.this.getWindow().getDecorView().getWindowToken(), 0);
            if ("".equals(((Editable) Objects.requireNonNull(IPCThreeFalseEyesActivity.this.binding.etPreset.getText())).toString())) {
                return;
            }
            IPCManager.getInstance().getDevice(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()).addPresetLocation(Integer.parseInt(IPCThreeFalseEyesActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.39.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.39.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.set_success, 0).show();
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSWitch() {
        switch (this.showMode) {
            case 0:
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.removeRule(13);
                this.binding.layoutGun.setLayoutParams(layoutParams);
                if (this.isRatio) {
                    this.isRatio = true;
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
                    setViewLayoutParams(this.binding.layoutPlay, -1, (int) (this.viewHeight + this.viewHeight2));
                    setViewLayoutParams(this.binding.layoutGun, -1, (int) this.viewHeight2);
                    setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutPlayerGun1, this.w / 2, (int) this.viewHeight2);
                    setViewLayoutParams(this.binding.layoutPlayerGun2, this.w / 2, (int) this.viewHeight2);
                }
                this.binding.layoutPlayerGun2.setVisibility(0);
                this.binding.layoutPlayerGun1.setVisibility(0);
                this.binding.viewGun.setVisibility(0);
                this.binding.layoutGun.setVisibility(0);
                this.binding.playerBall.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.ivNightTop.setVisibility(0);
                }
                this.binding.fullNightVision.setVisibility(8);
                this.binding.tvTime1.setVisibility(0);
                this.binding.tvTime2.setVisibility(0);
                VerticalPlay();
                break;
            case 1:
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.ivNightTop.setVisibility(0);
                }
                this.binding.layoutCenter.setVisibility(8);
                this.binding.viewGun.setVisibility(8);
                this.binding.layoutGunOrientation.setOrientation(1);
                if (this.isRatio) {
                    this.viewHeight = this.w / 2;
                    Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
                    setViewLayoutParams(this.binding.playerGun1, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutPlayerGun2, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutGun, -1, ((int) this.viewHeight) * 2);
                    setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutPlay, -1, ((int) this.viewHeight) * 3);
                } else {
                    this.viewHeight = (this.w / 16) * 9;
                    Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
                    setViewLayoutParams(this.binding.layoutPlayerGun1, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutPlayerGun2, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutGun, -1, ((int) this.viewHeight) * 2);
                    setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.layoutPlay, -1, ((int) this.viewHeight) * 3);
                }
                this.binding.llFull.setVisibility(8);
                this.binding.viewBack.setVisibility(0);
                this.binding.tvTime2.setVisibility(0);
                this.binding.viewBack.bringToFront();
                this.binding.playerGun1.reset2();
                this.binding.playerGun2.reset2();
                new Handler().postDelayed(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.59
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeFalseEyesActivity.this.binding.playerGun1.Left(IPCThreeFalseEyesActivity.this, 1.6d, 1.0d);
                        IPCThreeFalseEyesActivity.this.binding.playerGun2.Right(IPCThreeFalseEyesActivity.this, 1.6d, 1.0d);
                    }
                }, 600L);
                new Handler().postDelayed(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.60
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeFalseEyesActivity.this.binding.playerGun1.Zomm(1.7d);
                        IPCThreeFalseEyesActivity.this.binding.playerGun2.Zomm(1.7d);
                    }
                }, 650L);
                new Handler().postDelayed(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.61
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeFalseEyesActivity.this.binding.playerGun1.LeftMove(IPCThreeFalseEyesActivity.this, 250.0d, 0.0d);
                        IPCThreeFalseEyesActivity.this.binding.playerGun2.RightMove(IPCThreeFalseEyesActivity.this, 250.0d, 0.0d);
                        IPCThreeFalseEyesActivity.this.binding.viewBack.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.llFull.setVisibility(0);
                    }
                }, 700L);
                break;
            case 2:
                this.binding.layoutCenter.setVisibility(0);
                this.binding.layoutGunOrientation.setOrientation(0);
                if (this.isRatio) {
                    this.viewHeight = this.w / 2;
                    Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
                    MyGlTextureView myGlTextureView3 = this.binding.playerGun1;
                    int i4 = this.w;
                    setViewLayoutParams(myGlTextureView3, i4 / 2, ((i4 / 2) / 16) * 9);
                    RelativeLayout relativeLayout2 = this.binding.layoutPlayerGun2;
                    int i5 = this.w;
                    setViewLayoutParams(relativeLayout2, i5 / 2, ((i5 / 2) / 16) * 9);
                    setViewLayoutParams(this.binding.layoutGun, -1, ((this.w / 2) / 16) * 9);
                    setViewLayoutParams(this.binding.playerBall, -1, (this.w / 16) * 9);
                    RelativeLayout relativeLayout3 = this.binding.layoutPlay;
                    int i6 = this.w;
                    setViewLayoutParams(relativeLayout3, -1, (((i6 / 2) / 16) * 9) + ((i6 / 16) * 9));
                } else {
                    this.viewHeight = (this.w / 16) * 9;
                    Log.e("屏幕", "宽=" + this.w + " 高=" + this.h + " viewHeight=" + this.viewHeight);
                    setViewLayoutParams(this.binding.layoutPlay, -1, ((int) this.viewHeight) * 2);
                    setViewLayoutParams(this.binding.layoutGun, -1, (int) this.viewHeight);
                    setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
                    RelativeLayout relativeLayout4 = this.binding.layoutPlayerGun1;
                    int i7 = this.w;
                    setViewLayoutParams(relativeLayout4, i7 / 2, ((i7 / 2) / 16) * 9);
                    RelativeLayout relativeLayout5 = this.binding.layoutPlayerGun2;
                    int i8 = this.w;
                    setViewLayoutParams(relativeLayout5, i8 / 2, ((i8 / 2) / 16) * 9);
                }
                this.binding.playerBall.setVisibility(8);
                this.binding.layoutPlayerGun2.setVisibility(8);
                this.binding.layoutPlayerGun1.setVisibility(0);
                this.binding.tvTime1.setVisibility(0);
                this.binding.tvTime2.setVisibility(8);
                this.binding.ivNightTop.setVisibility(8);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(0);
                }
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams2.addRule(13, -1);
                this.binding.layoutGun.setLayoutParams(layoutParams2);
                setViewLayoutParams(this.binding.layoutPlayerGun1, -1, (this.w / 16) * 9);
                break;
            case 3:
                this.binding.playerBall.setVisibility(8);
                this.binding.layoutPlayerGun1.setVisibility(8);
                this.binding.tvTime1.setVisibility(8);
                this.binding.tvTime2.setVisibility(0);
                this.binding.ivNightTop.setVisibility(8);
                this.binding.layoutPlayerGun2.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(0);
                }
                setViewLayoutParams(this.binding.layoutPlayerGun2, -1, (this.w / 16) * 9);
                break;
            case 4:
                this.binding.layoutPlayerGun1.setVisibility(8);
                this.binding.layoutPlayerGun2.setVisibility(8);
                this.binding.tvTime1.setVisibility(8);
                this.binding.tvTime2.setVisibility(8);
                this.binding.ivNightTop.setVisibility(8);
                this.binding.layoutGun.setVisibility(8);
                this.binding.playerBall.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(0);
                }
                setViewLayoutParams(this.binding.playerBall, -1, (this.w / 16) * 9);
                break;
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
        new Thread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.62
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                FileOutputStream fileOutputStream;
                File file;
                String string = SpUtil.getString(IPCThreeFalseEyesActivity.this.getActivity(), Utils.getDevSnapKey(str), "");
                long jCurrentTimeMillis = System.currentTimeMillis();
                LogEx.d(true, IPCThreeFalseEyesActivity.this.TAG, "保存图片");
                StringBuilder sb = new StringBuilder();
                IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                sb.append(iPCThreeFalseEyesActivity.getFilesPath(iPCThreeFalseEyesActivity.getApplication()));
                sb.append("/snap/");
                sb.append(str);
                sb.append("/");
                String string2 = sb.toString();
                Log.d(IPCThreeFalseEyesActivity.this.TAG, "run: puppet:dirPath====" + string2);
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
                        Log.d(IPCThreeFalseEyesActivity.this.TAG, "run: puppet:b=====" + zCompress);
                        fileOutputStream.flush();
                        LogEx.e(true, IPCThreeFalseEyesActivity.this.TAG, "图片保存地址: " + file.getAbsolutePath());
                        Log.d(IPCThreeFalseEyesActivity.this.TAG, "puppet:图片保存地址: file.getAbsolutePath()======" + file.getAbsolutePath());
                        Log.d(IPCThreeFalseEyesActivity.this.TAG, "puppet:图片保存地址: Utils.getDevSnapKey(tempIotId)======" + Utils.getDevSnapKey(str));
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
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/status/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass63());
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$63, reason: invalid class name */
    class AnonymousClass63 implements IoTCallback {
        AnonymousClass63() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            exc.printStackTrace();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
            Log.d(IPCThreeFalseEyesActivity.this.TAG, "run: ---------------" + Thread.currentThread().getName());
            try {
                if (((org.json.JSONObject) ioTResponse.getData()).get("status").toString().equals("3")) {
                    if (SharePreferenceManager.getInstance().getIccId(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == null || "".equals(SharePreferenceManager.getInstance().getIccId(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()))) {
                        IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.63.2
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCThreeFalseEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                IPCThreeFalseEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                            }
                        });
                    } else {
                        new OkHttpClient().newCall(new Request.Builder().url("http://www.secueye.cn:8000/api/smsApi?iccid=" + SharePreferenceManager.getInstance().getIccId(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) + "&method=smsStatusSecueye").get().build()).enqueue(new Callback() { // from class: activity.IPCThreeFalseEyesActivity.63.1
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
                                            IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.63.1.1
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCThreeFalseEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCThreeFalseEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                    IPCThreeFalseEyesActivity.this.isOtherCard = true;
                                                }
                                            });
                                            return;
                                        } else if (iIntValue != 200) {
                                            IPCThreeFalseEyesActivity.this.showToast(IPCThreeFalseEyesActivity.this.getString(R.string.exception_4g_data));
                                            return;
                                        }
                                    }
                                    if (!object.containsKey("values") || IPCThreeFalseEyesActivity.this.isOtherCard) {
                                        return;
                                    }
                                    JSONObject jSONObject = object.getJSONObject("values");
                                    if (jSONObject.containsKey("status")) {
                                        if (!jSONObject.getString("status").equals("停机")) {
                                            IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.63.1.4
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCThreeFalseEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCThreeFalseEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                }
                                            });
                                        } else if (AppConfig.isChina) {
                                            IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.63.1.2
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    if (!IPCThreeFalseEyesActivity.this.isHorizontal) {
                                                        IPCThreeFalseEyesActivity.this.binding.traffic4gExpired.bringToFront();
                                                        IPCThreeFalseEyesActivity.this.binding.immediateRenewal.bringToFront();
                                                        IPCThreeFalseEyesActivity.this.binding.outlineTime.bringToFront();
                                                        IPCThreeFalseEyesActivity.this.binding.videoPlayIbtn.setVisibility(8);
                                                        IPCThreeFalseEyesActivity.this.binding.ipcOfflineText.setVisibility(8);
                                                        IPCThreeFalseEyesActivity.this.binding.traffic4gExpired.setVisibility(0);
                                                        IPCThreeFalseEyesActivity.this.binding.immediateRenewal.setVisibility(0);
                                                        IPCThreeFalseEyesActivity.this.binding.outlineTime.setVisibility(0);
                                                    }
                                                    try {
                                                        IPCThreeFalseEyesActivity.this.binding.outlineTime.setText(((Object) IPCThreeFalseEyesActivity.this.getResources().getText(R.string.time_of_off_line)) + "：" + TimeUtil.TimeStamp2Date(((org.json.JSONObject) ioTResponse.getData()).get("time").toString()));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    IPCThreeFalseEyesActivity.this.needRecharge = true;
                                                }
                                            });
                                        } else {
                                            IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.63.1.3
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCThreeFalseEyesActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCThreeFalseEyesActivity.this.binding.ipcOfflineText.setVisibility(0);
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
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).queryAPList(new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.64
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null) {
                    return;
                }
                try {
                    IoTResponse ioTResponse = (IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class);
                    if (ioTResponse.getCode() != 200) {
                        IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.64.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(IPCThreeFalseEyesActivity.this, IPCThreeFalseEyesActivity.this.getString(R.string.get_wifi_failed), 0).show();
                            }
                        });
                    } else {
                        Object data = ioTResponse.getData();
                        if (data != null) {
                            try {
                                JSONArray jSONArray = ((JSONObject) data).getJSONArray("APList");
                                IPCThreeFalseEyesActivity.this.wifiBeanList = JSON.parseArray(jSONArray.toString(), WifiBean.class);
                                IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.64.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCThreeFalseEyesActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCThreeFalseEyesActivity.this.FourGChangeDialog();
                                        IPCThreeFalseEyesActivity.this.getWiFiListSucceed(IPCThreeFalseEyesActivity.this.wifiBeanList);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.64.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCThreeFalseEyesActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCThreeFalseEyesActivity.this.showToast(IPCThreeFalseEyesActivity.this.getString(R.string.query_wifi_list_fail));
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
        this.f1577dialog = new AlertDialog.Builder(this).setView(viewInflate).create();
        this.f1577dialog.setCanceledOnTouchOutside(true);
        this.f1577dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.f1577dialog.show();
        int i = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams attributes = this.f1577dialog.getWindow().getAttributes();
        attributes.width = (int) (((double) i) * 0.95d);
        this.f1577dialog.getWindow().setAttributes(attributes);
        this.f1577dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCThreeFalseEyesActivity.65
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCThreeFalseEyesActivity.this.cancelCount();
            }
        });
        ((Button) viewInflate.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.66
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity.this.f1577dialog.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new IpcWiFiAdapter(R.layout.item_wifi_ipc);
        this.mAdapter.bindToRecyclerView(recyclerView);
        this.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: activity.IPCThreeFalseEyesActivity.67
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i2) {
                WifiBean wifiBean = IPCThreeFalseEyesActivity.this.mAdapter.getData().get(i2);
                if (wifiBean.isCurrentWifi()) {
                    return;
                }
                IPCThreeFalseEyesActivity.this.f1577dialog.dismiss();
                IPCThreeFalseEyesActivity.this.selectSsid = wifiBean.getSsid();
                IPCThreeFalseEyesActivity.this.inputDialogView.setTitle(IPCThreeFalseEyesActivity.this.selectSsid);
                IPCThreeFalseEyesActivity.this.inputDialogView.show(IPCThreeFalseEyesActivity.this.getSupportFragmentManager(), IPCThreeFalseEyesActivity.this.TAG);
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
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCThreeFalseEyesActivity.68
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCThreeFalseEyesActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.69
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.70
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass71(60000L, 4000L, i, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$71, reason: invalid class name */
    class AnonymousClass71 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ int val$position;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass71(long j, long j2, int i, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
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
            SettingsCtrl.getInstance().getProperties(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeFalseEyesActivity.71.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == AnonymousClass71.this.val$position) {
                        IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.71.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCThreeFalseEyesActivity.this.playLive();
                                AnonymousClass71.this.val$progressBarText.setVisibility(8);
                                AnonymousClass71.this.val$progressBar.setVisibility(8);
                                AnonymousClass71.this.val$imageViewText.setVisibility(0);
                                AnonymousClass71.this.val$imageViewText.setText(IPCThreeFalseEyesActivity.this.getString(R.string.switched_success));
                                AnonymousClass71.this.val$imageView.setVisibility(0);
                                AnonymousClass71.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass71.this.val$imageButton.setVisibility(0);
                                Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                        AnonymousClass71.this.cancel();
                        IPCThreeFalseEyesActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.71.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass71.this.val$progressBarText.setVisibility(8);
                    AnonymousClass71.this.val$progressBar.setVisibility(8);
                    AnonymousClass71.this.val$imageViewText.setVisibility(0);
                    AnonymousClass71.this.val$imageViewText.setText(IPCThreeFalseEyesActivity.this.getString(R.string.switched_fail));
                    AnonymousClass71.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass71.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCThreeFalseEyesActivity.this.countDownTimer = null;
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
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCThreeFalseEyesActivity.72
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCThreeFalseEyesActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.73
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.74
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass75(60000L, 4000L, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$75, reason: invalid class name */
    class AnonymousClass75 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass75(long j, long j2, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), new MyCallback() { // from class: activity.IPCThreeFalseEyesActivity.75.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getWifiConfigIsExist(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1) {
                        IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.75.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass75.this.val$progressBarText.setVisibility(8);
                                AnonymousClass75.this.val$progressBar.setVisibility(8);
                                AnonymousClass75.this.val$imageViewText.setVisibility(0);
                                AnonymousClass75.this.val$imageViewText.setText(IPCThreeFalseEyesActivity.this.getString(R.string.switched_success));
                                AnonymousClass75.this.val$imageView.setVisibility(0);
                                AnonymousClass75.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass75.this.val$imageButton.setVisibility(0);
                            }
                        });
                        AnonymousClass75.this.cancel();
                        IPCThreeFalseEyesActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.75.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass75.this.val$progressBarText.setVisibility(8);
                    AnonymousClass75.this.val$progressBar.setVisibility(8);
                    AnonymousClass75.this.val$imageViewText.setVisibility(0);
                    AnonymousClass75.this.val$imageViewText.setText(IPCThreeFalseEyesActivity.this.getString(R.string.switched_fail));
                    AnonymousClass75.this.val$imageView.setVisibility(0);
                    AnonymousClass75.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass75.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCThreeFalseEyesActivity.this.countDownTimer = null;
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
        ((Button) viewInflate.findViewById(R.id.btn_left)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.76
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        ((Button) viewInflate.findViewById(R.id.btn_right)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.77
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                iPCThreeFalseEyesActivity.switch4gMode(iPCThreeFalseEyesActivity.getString(R.string.Net4GEnableSwitch), 2);
                alertDialogCreate.dismiss();
                IPCThreeFalseEyesActivity.this.FourGChangeDialog(2, alertDialogCreate);
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
        this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.78
            @Override // java.lang.Runnable
            public void run() {
                new BaseDialog.Builder().view(R.layout.dialog_common).content(IPCThreeFalseEyesActivity.this.getString(R.string.sd_card_not_initialized)).leftBtnText(IPCThreeFalseEyesActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.78.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        SharePreferenceManager.getInstance().setFirstFormatInIpc(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), false);
                    }
                }).rightBtnText(IPCThreeFalseEyesActivity.this.getString(R.string.format)).clickRight(new View.OnClickListener() { // from class: activity.IPCThreeFalseEyesActivity.78.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Intent intent = new Intent(IPCThreeFalseEyesActivity.this, (Class<?>) StorageStatusActivity.class);
                        intent.putExtra("totalStorage", f);
                        intent.putExtra("remainStorage", f2);
                        intent.putExtra("storageStatusValues", i);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCThreeFalseEyesActivity.this.ballDevice);
                        if (IPCThreeFalseEyesActivity.this.gunDevice1 != null) {
                            bundle.putSerializable("device1", IPCThreeFalseEyesActivity.this.gunDevice1);
                        }
                        if (IPCThreeFalseEyesActivity.this.nvrDevice != null) {
                            bundle.putSerializable("nvrDevice", IPCThreeFalseEyesActivity.this.nvrDevice);
                        }
                        intent.putExtras(bundle);
                        IPCThreeFalseEyesActivity.this.startActivity(intent);
                        IPCThreeFalseEyesActivity.this.needTFInit = false;
                    }
                }).canCancel(false).create().show(IPCThreeFalseEyesActivity.this.getSupportFragmentManager(), "");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpDevice() {
        this.countWakeUp = 0;
        HashMap map = new HashMap();
        map.put("iotId", this.ballDevice.getIotId());
        map.put("productKey", this.ballDevice.getProductKey());
        map.put("deviceName", this.ballDevice.getDeviceName());
        map.put("packet", this.ballDevice.getWakeUpData());
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/living/device/lowpower/wakeup").setScheme(Scheme.HTTPS).setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass79());
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$79, reason: invalid class name */
    class AnonymousClass79 implements IoTCallback {
        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
        }

        AnonymousClass79() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            IPCThreeFalseEyesActivity.this.needWakeUp = true;
            Handler handler = IPCThreeFalseEyesActivity.this.handler;
            final IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
            handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCThreeFalseEyesActivity$79$6PdTzItKEq2Ob3PxW2KvfpDVs1E
                @Override // java.lang.Runnable
                public final void run() {
                    iPCThreeFalseEyesActivity.playLive();
                }
            });
            IPCThreeFalseEyesActivity.access$6308(IPCThreeFalseEyesActivity.this);
            IPCThreeFalseEyesActivity.this.wakeUpHandler.sendEmptyMessageDelayed(1, 8000L);
        }
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$80, reason: invalid class name */
    class AnonymousClass80 extends Handler {
        AnonymousClass80() {
        }

        @Override // android.os.Handler
        public void handleMessage(@NonNull Message message) {
            if (message.what != 1 || IPCThreeFalseEyesActivity.this.playBall.getPlayState() == 3) {
                return;
            }
            IPCThreeFalseEyesActivity.access$6308(IPCThreeFalseEyesActivity.this);
            Handler handler = IPCThreeFalseEyesActivity.this.handler;
            final IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
            handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCThreeFalseEyesActivity$80$iR_QOQmj4FJwIrV4GZbM2gctd7U
                @Override // java.lang.Runnable
                public final void run() {
                    iPCThreeFalseEyesActivity.playLive();
                }
            });
            if (IPCThreeFalseEyesActivity.this.countWakeUp < 5) {
                IPCThreeFalseEyesActivity.this.wakeUpHandler.sendEmptyMessageDelayed(1, 8000L);
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
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.81
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() == 200) {
                            SharePreferenceManager.getInstance().setStreamVideoQuality(IPCThreeFalseEyesActivity.this.ballDevice.getIotId(), i);
                            IPCThreeFalseEyesActivity.this.binding.qualityBtn.setText((CharSequence) IPCThreeFalseEyesActivity.this.definitionList.get(SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeFalseEyesActivity.this.ballDevice.getIotId())));
                            int i2 = i;
                        } else {
                            IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.81.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.removeRule(13);
        this.binding.layoutGun.setLayoutParams(layoutParams);
        if (this.isRatio) {
            this.isRatio = true;
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
            setViewLayoutParams(this.binding.layoutPlay, -1, (int) (this.viewHeight + this.viewHeight2));
            setViewLayoutParams(this.binding.layoutGun, -1, (int) this.viewHeight2);
            setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
            setViewLayoutParams(this.binding.layoutPlayerGun1, this.w / 2, (int) this.viewHeight2);
            setViewLayoutParams(this.binding.layoutPlayerGun2, this.w / 2, (int) this.viewHeight2);
        }
        this.binding.layoutPlayerGun2.setVisibility(0);
        this.binding.layoutPlayerGun1.setVisibility(0);
        this.binding.layoutGun.setVisibility(0);
        this.binding.playerBall.setVisibility(0);
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
            this.binding.ivNightTop.setVisibility(0);
        }
        this.binding.fullNightVision.setVisibility(8);
        this.showMode = 0;
        this.binding.tvTime2.setTextSize(12.0f);
        this.binding.tvTime1.setTextSize(12.0f);
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
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.addRule(15, -1);
        layoutParams2.addRule(11, -1);
        this.binding.layoutQuality.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams3.setMargins(0, 0, 0, 0);
        this.binding.layoutPlay.setLayoutParams(layoutParams3);
        if (this.isZoomLeft) {
            this.binding.playerGun2.LeftMove(this, ((double) this.w) * 0.381d, 0.0d);
            this.isZoomLeft = false;
        }
        this.binding.layoutTop.setLayoutParams(new RelativeLayout.LayoutParams(-1, ScreenUtil.dp2Px(this, 44.0f)));
        this.binding.layoutTop.setBackground(getResources().getDrawable(R.drawable.bg_gray_transparent));
        addControlTouchView();
        if (this.showMode == 0) {
            this.binding.playerGun1.setVisibility(0);
            this.binding.playerGun2.setVisibility(0);
            this.binding.layoutGun.setVisibility(0);
            this.binding.playerBall.setVisibility(0);
            this.binding.fullNightVision.setVisibility(8);
            setViewLayoutParams(this.binding.layoutGun, -1, this.w / 2);
            setViewLayoutParams(this.binding.layoutPlayerGun1, this.h / 2, this.w / 2);
            setViewLayoutParams(this.binding.layoutPlayerGun2, this.h / 2, this.w / 2);
            setViewLayoutParams(this.binding.playerBall, this.h / 2, this.w / 2);
            HorizontalPlay();
        }
        getWindow().setFlags(1024, 1024);
    }

    public void backFullScreen() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.removeRule(13);
        this.binding.layoutGun.setLayoutParams(layoutParams);
        this.binding.layoutPlayerGun1.setVisibility(0);
        this.binding.layoutPlayerGun2.setVisibility(0);
        this.binding.layoutGun.setVisibility(0);
        this.binding.playerBall.setVisibility(0);
        this.binding.fullNightVision.setVisibility(8);
        setViewLayoutParams(this.binding.layoutGun, -1, (int) this.viewHeight2);
        setViewLayoutParams(this.binding.layoutPlayerGun1, this.h / 2, (int) this.viewHeight2);
        setViewLayoutParams(this.binding.layoutPlayerGun2, this.h / 2, (int) this.viewHeight2);
        setViewLayoutParams(this.binding.playerBall, -1, (int) this.viewHeight);
        this.showMode = 0;
        this.binding.tvTime2.setTextSize(9.0f);
        this.binding.tvTime1.setTextSize(9.0f);
        this.binding.layoutTop.setVisibility(0);
        this.binding.ivSetting.setVisibility(0);
        this.binding.layoutControl.setVisibility(0);
        this.binding.layoutCenter.setVisibility(0);
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.ballDevice.getIotId()) == 0) {
            this.binding.ivNightBottom.setVisibility(0);
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
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.setMargins(ScreenUtil.dp2Px(this, 5.0f), ScreenUtil.dp2Px(this, 5.0f), ScreenUtil.dp2Px(this, 5.0f), ScreenUtil.dp2Px(this, 5.0f));
        layoutParams2.addRule(12, -1);
        this.binding.layoutQuality.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams3.setMargins(0, ScreenUtil.dp2Px(this, 74.0f), 0, 0);
        this.binding.layoutPlay.setLayoutParams(layoutParams3);
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(-1, ScreenUtil.dp2Px(this, 44.0f));
        layoutParams4.setMargins(0, ScreenUtil.dp2Px(this, 30.0f), 0, 0);
        this.binding.layoutTop.setLayoutParams(layoutParams4);
        this.binding.layoutTop.setBackgroundColor(getResources().getColor(R.color.color_black));
        setSWitch();
        addControlTouchView();
        getWindow().clearFlags(1024);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void isNet4GSwitch() {
        if (((SharePreferenceManager.getInstance().getPageControlEx(this.ballDevice.getIotId()) & 524288) >> 19) == 1) {
            if (SharePreferenceManager.getInstance().getIccId1(this.ballDevice.getIotId()).equals("") && SharePreferenceManager.getInstance().getIccId2(this.ballDevice.getIotId()).equals("")) {
                Intent intent = new Intent(getActivity(), (Class<?>) Traffic4GActivity.class);
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
        this.binding.playerBall.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCThreeFalseEyesActivity.82
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
                Log.d(IPCThreeFalseEyesActivity.this.TAG, "onScaleChanged: " + dDoubleValue);
                if (SharePreferenceManager.getInstance().getMixZoom(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1) {
                    str = decimalFormat.format((((dDoubleValue - 1.0d) * 2.25d) + 1.0d) * ((double) IPCThreeFalseEyesActivity.this.ZoomMax));
                } else {
                    str = decimalFormat.format(((dDoubleValue - 1.0d) * 2.25d) + 1.0d);
                }
                IPCThreeFalseEyesActivity.this.zoom.postValue(Float.valueOf(f));
                IPCThreeFalseEyesActivity.this.binding.tvZoom.setText(str + "X");
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCThreeFalseEyesActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playBall.setOnErrorListener(new AnonymousClass83());
        this.playBall.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCThreeFalseEyesActivity.84
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCThreeFalseEyesActivity.this.dismissPlayButton();
                        IPCThreeFalseEyesActivity.this.showBuffering();
                        if (IPCThreeFalseEyesActivity.this.needWakeUp) {
                            IPCThreeFalseEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCThreeFalseEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        IPCThreeFalseEyesActivity.this.lowPowerMode = 1;
                        IPCThreeFalseEyesActivity.this.needWakeUp = false;
                        IPCThreeFalseEyesActivity.this.is1100ErrorPre = 10;
                        IPCThreeFalseEyesActivity.this.dismissSnapPicture();
                        IPCThreeFalseEyesActivity.this.dismissBuffering();
                        IPCThreeFalseEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_READY");
                        IPCThreeFalseEyesActivity.this.updateInfoTv();
                        break;
                    case 4:
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_ENDED");
                        IPCThreeFalseEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.playBall.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$83, reason: invalid class name */
    class AnonymousClass83 implements OnErrorListener {
        AnonymousClass83() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCThreeFalseEyesActivity.this.needWakeUp || IPCThreeFalseEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                        case 7:
                            IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                            iPCThreeFalseEyesActivity.showToast(iPCThreeFalseEyesActivity.getString(R.string.connect_failed));
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCThreeFalseEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCThreeFalseEyesActivity.this.showBadNetDialog();
                                    }
                                } else {
                                    IPCThreeFalseEyesActivity.access$6710(IPCThreeFalseEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeFalseEyesActivity.this.ballDevice.getIotId());
                                    IPCThreeFalseEyesActivity.this.playBall.stop();
                                    Handler handler = IPCThreeFalseEyesActivity.this.handler;
                                    final IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity2 = IPCThreeFalseEyesActivity.this;
                                    handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCThreeFalseEyesActivity$83$hMEK20YQuijy7l8KXzYn55LMj5M
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            iPCThreeFalseEyesActivity2.playLive();
                                        }
                                    }, 500L);
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCThreeFalseEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCThreeFalseEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity3 = IPCThreeFalseEyesActivity.this;
            iPCThreeFalseEyesActivity3.showToast(iPCThreeFalseEyesActivity3.getString(R.string.account_squeezed));
        }
    }

    private void initPlayerGun1() {
        this.playGun1 = new LivePlayer(getApplicationContext());
        this.playGun1.setTextureView(this.binding.playerGun1);
        this.binding.playerGun1.setClickable(true);
        this.playGun1.setVolume(this.binding.llListener.isSelected() ? 1.0f : 0.0f);
        this.playGun1.setVideoScalingMode(1);
        this.playGun1.setOnErrorListener(new AnonymousClass85());
        this.playGun1.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCThreeFalseEyesActivity.86
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_IDLE");
                        return;
                    case 2:
                        IPCThreeFalseEyesActivity.this.dismissPlayButton();
                        IPCThreeFalseEyesActivity.this.showBuffering();
                        if (IPCThreeFalseEyesActivity.this.needWakeUp) {
                            IPCThreeFalseEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCThreeFalseEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_BUFFERING");
                        return;
                    case 3:
                        if (IPCThreeFalseEyesActivity.this.showMode == 0 && SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeFalseEyesActivity.this.binding.ivNightTop.setVisibility(0);
                        }
                        IPCThreeFalseEyesActivity.this.lowPowerMode = 1;
                        IPCThreeFalseEyesActivity.this.needWakeUp = false;
                        IPCThreeFalseEyesActivity.this.is1100ErrorPre = 10;
                        IPCThreeFalseEyesActivity.this.dismissSnapPicture();
                        IPCThreeFalseEyesActivity.this.dismissBuffering();
                        IPCThreeFalseEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_READY");
                        IPCThreeFalseEyesActivity.this.updateInfoTv();
                        if (!IPCThreeFalseEyesActivity.this.isHorizontal && !IPCThreeFalseEyesActivity.this.isZoomRight) {
                            IPCThreeFalseEyesActivity.this.isZoomRight = true;
                            IPCThreeFalseEyesActivity.this.VerticalPlay();
                        } else {
                            IPCThreeFalseEyesActivity.this.HorizontalPlay();
                        }
                        break;
                    case 4:
                        break;
                    default:
                        return;
                }
                LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_ENDED");
                IPCThreeFalseEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                IPCThreeFalseEyesActivity.this.binding.tvTime1.setVisibility(8);
                IPCThreeFalseEyesActivity.this.binding.tvTime2.setVisibility(8);
                IPCThreeFalseEyesActivity.this.binding.viewBack.setVisibility(0);
                IPCThreeFalseEyesActivity.this.binding.viewBack.bringToFront();
                IPCThreeFalseEyesActivity.this.playGun1.stopRecordingContent();
                IPCThreeFalseEyesActivity.this.binding.playerGun1.reset();
                IPCThreeFalseEyesActivity.this.binding.playerGun2.reset();
                IPCThreeFalseEyesActivity.this.showMode = 0;
                IPCThreeFalseEyesActivity.this.isZoomRight = false;
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$85, reason: invalid class name */
    class AnonymousClass85 implements OnErrorListener {
        AnonymousClass85() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCThreeFalseEyesActivity.this.needWakeUp || IPCThreeFalseEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCThreeFalseEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCThreeFalseEyesActivity.this.showBadNetDialog();
                                    }
                                } else {
                                    IPCThreeFalseEyesActivity.access$6710(IPCThreeFalseEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeFalseEyesActivity.this.gunDevice1.getIotId());
                                    IPCThreeFalseEyesActivity.this.playGun1.stop();
                                    Handler handler = IPCThreeFalseEyesActivity.this.handler;
                                    final IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                                    handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCThreeFalseEyesActivity$85$uvk9_Ym5GuckgOZcsSys3OlAhic
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            iPCThreeFalseEyesActivity.playLive();
                                        }
                                    }, 500L);
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCThreeFalseEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCThreeFalseEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity2 = IPCThreeFalseEyesActivity.this;
            iPCThreeFalseEyesActivity2.showToast(iPCThreeFalseEyesActivity2.getString(R.string.account_squeezed));
        }
    }

    private void initPlayerGun2() {
        this.playGun2 = new LivePlayer(getApplicationContext());
        this.playGun2.setTextureView(this.binding.playerGun2);
        this.binding.playerGun2.setClickable(true);
        this.playGun2.setVolume(this.binding.llListener.isSelected() ? 1.0f : 0.0f);
        this.playGun2.setVideoScalingMode(1);
        this.playGun2.setOnErrorListener(new AnonymousClass87());
        this.playGun2.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCThreeFalseEyesActivity.88
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCThreeFalseEyesActivity.this.dismissPlayButton();
                        IPCThreeFalseEyesActivity.this.showBuffering();
                        if (IPCThreeFalseEyesActivity.this.needWakeUp) {
                            IPCThreeFalseEyesActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCThreeFalseEyesActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        if (IPCThreeFalseEyesActivity.this.showMode == 0 && SharePreferenceManager.getInstance().getNightVisionHide(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 0) {
                            IPCThreeFalseEyesActivity.this.binding.ivNightTop.setVisibility(0);
                        }
                        IPCThreeFalseEyesActivity.this.lowPowerMode = 1;
                        IPCThreeFalseEyesActivity.this.needWakeUp = false;
                        IPCThreeFalseEyesActivity.this.is1100ErrorPre = 10;
                        IPCThreeFalseEyesActivity.this.dismissSnapPicture();
                        IPCThreeFalseEyesActivity.this.dismissBuffering();
                        IPCThreeFalseEyesActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_READY");
                        IPCThreeFalseEyesActivity.this.updateInfoTv();
                        Message message = new Message();
                        message.what = 2;
                        IPCThreeFalseEyesActivity.this.handler.sendMessage(message);
                        IPCThreeFalseEyesActivity.this.new TimeThread().start();
                        break;
                    case 4:
                        LogEx.i(true, IPCThreeFalseEyesActivity.this.TAG, "STATE_ENDED");
                        IPCThreeFalseEyesActivity.this.binding.playerInfoTv.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.tvTime1.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.binding.tvTime2.setVisibility(8);
                        IPCThreeFalseEyesActivity.this.playGun2.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$87, reason: invalid class name */
    class AnonymousClass87 implements OnErrorListener {
        AnonymousClass87() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCThreeFalseEyesActivity.this.needWakeUp || IPCThreeFalseEyesActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCThreeFalseEyesActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCThreeFalseEyesActivity.this.ballDevice.getIotId()) == 1) {
                                        IPCThreeFalseEyesActivity.this.showBadNetDialog();
                                    }
                                } else {
                                    IPCThreeFalseEyesActivity.access$6710(IPCThreeFalseEyesActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCThreeFalseEyesActivity.this.gunDevice2.getIotId());
                                    IPCThreeFalseEyesActivity.this.playGun2.stop();
                                    Handler handler = IPCThreeFalseEyesActivity.this.handler;
                                    final IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                                    handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCThreeFalseEyesActivity$87$hNDKtEYEi2_QwKRtdndOo31QQOo
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            iPCThreeFalseEyesActivity.playLive();
                                        }
                                    }, 500L);
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCThreeFalseEyesActivity.this.needRecharge) {
                        return;
                    }
                    IPCThreeFalseEyesActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity2 = IPCThreeFalseEyesActivity.this;
            iPCThreeFalseEyesActivity2.showToast(iPCThreeFalseEyesActivity2.getString(R.string.account_squeezed));
        }
    }

    class TimeThread extends Thread {
        TimeThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000L);
                    Message message = new Message();
                    message.what = 2;
                    IPCThreeFalseEyesActivity.this.handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
        this.playBall.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCThreeFalseEyesActivity.89
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCThreeFalseEyesActivity.this.playBall.start();
            }
        });
        this.playBall.prepare();
        this.playGun1.stop();
        Log.d(this.TAG, "playLive: puppet:iotId1====" + this.gunDevice1.getIotId());
        this.playGun1.setIPCLiveDataSource(this.gunDevice1.getIotId(), 0, false, 0, true, 0);
        this.playGun1.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCThreeFalseEyesActivity.90
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCThreeFalseEyesActivity.this.playGun1.start();
            }
        });
        this.playGun1.prepare();
        this.playGun2.stop();
        Log.d(this.TAG, "playLive: puppet:iotId1====" + this.gunDevice2.getIotId());
        this.playGun2.setIPCLiveDataSource(this.gunDevice2.getIotId(), 0, false, 0, true, 0);
        this.playGun2.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCThreeFalseEyesActivity.91
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCThreeFalseEyesActivity.this.playGun2.start();
            }
        });
        this.playGun2.prepare();
    }

    private void initLiveIntercom() {
        this.liveIntercom = new LiveIntercomV2(this, this.ballDevice.getIotId(), LiveIntercomV2.LiveIntercomMode.SingleTalk, AudioParams.AUDIOPARAM_MONO_8K_G711A);
        this.liveIntercom.setGainLevel(-1);
        this.liveIntercom.setLiveIntercomV2Listener(new LiveIntercomV2Listener() { // from class: activity.IPCThreeFalseEyesActivity.92
            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordBufferReceived(byte[] bArr, int i, int i2) {
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onTalkReady() {
                IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.92.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeFalseEyesActivity.this.showToast(IPCThreeFalseEyesActivity.this.getResources().getString(R.string.can_begin_talk));
                        if (IPCThreeFalseEyesActivity.this.isFinishing()) {
                            return;
                        }
                        IPCThreeFalseEyesActivity.this.whiteProgressDialog.dismiss();
                        IPCThreeFalseEyesActivity.this.binding.speakerBtn.setSelected(true);
                        IPCThreeFalseEyesActivity.this.binding.fullIntercom.setSelected(IPCThreeFalseEyesActivity.this.binding.speakerBtn.isSelected());
                        IPCThreeFalseEyesActivity.this.binding.llListener.setSelected(true);
                        IPCThreeFalseEyesActivity.this.playBall.setVolume(IPCThreeFalseEyesActivity.this.binding.llListener.isSelected() ? 1.0f : 0.0f);
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onError(LiveIntercomException liveIntercomException) {
                int code = liveIntercomException.getCode();
                if (code != 16) {
                    switch (code) {
                        case 1:
                            IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                            iPCThreeFalseEyesActivity.showToast(iPCThreeFalseEyesActivity.getString(R.string.record_error1));
                            IPCThreeFalseEyesActivity.this.handleLiveIntercomError();
                            break;
                        case 2:
                            IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity2 = IPCThreeFalseEyesActivity.this;
                            iPCThreeFalseEyesActivity2.showToast(iPCThreeFalseEyesActivity2.getString(R.string.record_error2));
                            IPCThreeFalseEyesActivity.this.handleLiveIntercomError();
                            break;
                        case 3:
                            IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity3 = IPCThreeFalseEyesActivity.this;
                            iPCThreeFalseEyesActivity3.showToast(iPCThreeFalseEyesActivity3.getString(R.string.record_error3));
                            IPCThreeFalseEyesActivity.this.handleLiveIntercomError();
                            break;
                        default:
                            switch (code) {
                                case 5:
                                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity4 = IPCThreeFalseEyesActivity.this;
                                    iPCThreeFalseEyesActivity4.showToast(iPCThreeFalseEyesActivity4.getString(R.string.record_error4));
                                    IPCThreeFalseEyesActivity.this.handleLiveIntercomError();
                                    break;
                                case 6:
                                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity5 = IPCThreeFalseEyesActivity.this;
                                    iPCThreeFalseEyesActivity5.showToast(iPCThreeFalseEyesActivity5.getString(R.string.record_error5));
                                    IPCThreeFalseEyesActivity.this.handleLiveIntercomError();
                                    break;
                                case 7:
                                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity6 = IPCThreeFalseEyesActivity.this;
                                    iPCThreeFalseEyesActivity6.showToast(iPCThreeFalseEyesActivity6.getString(R.string.record_error6));
                                    IPCThreeFalseEyesActivity.this.onRecordError();
                                    break;
                                case 8:
                                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity7 = IPCThreeFalseEyesActivity.this;
                                    iPCThreeFalseEyesActivity7.showToast(iPCThreeFalseEyesActivity7.getString(R.string.record_error7));
                                    IPCThreeFalseEyesActivity.this.onRecordError();
                                    break;
                                case 9:
                                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity8 = IPCThreeFalseEyesActivity.this;
                                    iPCThreeFalseEyesActivity8.showToast(iPCThreeFalseEyesActivity8.getString(R.string.record_error8));
                                    IPCThreeFalseEyesActivity.this.onRecordError();
                                    break;
                            }
                            break;
                    }
                } else {
                    IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity9 = IPCThreeFalseEyesActivity.this;
                    iPCThreeFalseEyesActivity9.showToast(iPCThreeFalseEyesActivity9.getString(R.string.record_error9));
                    IPCThreeFalseEyesActivity.this.onRecordError();
                }
                liveIntercomException.printStackTrace();
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordStart() {
                LogEx.d(true, IPCThreeFalseEyesActivity.this.TAG, "onRecordStart");
                IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.92.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeFalseEyesActivity.this.binding.speakerBtn.setSelected(true);
                        IPCThreeFalseEyesActivity.this.binding.fullIntercom.setSelected(IPCThreeFalseEyesActivity.this.binding.speakerBtn.isSelected());
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordEnd() {
                LogEx.d(true, IPCThreeFalseEyesActivity.this.TAG, "onRecordEnd");
                IPCThreeFalseEyesActivity.this.liveIntercom.stop();
                IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.92.3
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeFalseEyesActivity.this.binding.speakerBtn.setSelected(false);
                        IPCThreeFalseEyesActivity.this.binding.fullIntercom.setSelected(IPCThreeFalseEyesActivity.this.binding.speakerBtn.isSelected());
                        if (IPCThreeFalseEyesActivity.this.isFinishing()) {
                            return;
                        }
                        IPCThreeFalseEyesActivity.this.whiteProgressDialog.dismiss();
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

    /* JADX INFO: renamed from: activity.IPCThreeFalseEyesActivity$93, reason: invalid class name */
    class AnonymousClass93 implements ViewTreeObserver.OnGlobalLayoutListener {
        AnonymousClass93() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (IPCThreeFalseEyesActivity.this.touchView.getParent() != null) {
                ((ViewGroup) IPCThreeFalseEyesActivity.this.touchView.getParent()).removeView(IPCThreeFalseEyesActivity.this.touchView);
            }
            if (!IPCThreeFalseEyesActivity.this.isLand) {
                if (IPCThreeFalseEyesActivity.this.binding.rlTouchView.getHeight() == 0) {
                    return;
                }
                IPCThreeFalseEyesActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad2);
                int dimensionPixelSize = IPCThreeFalseEyesActivity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                Log.e("屏幕", "" + IPCThreeFalseEyesActivity.this.isRatio);
                if (IPCThreeFalseEyesActivity.this.isRatio) {
                    IPCThreeFalseEyesActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCThreeFalseEyesActivity.this.getActivity(), 120.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                } else {
                    IPCThreeFalseEyesActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCThreeFalseEyesActivity.this.getActivity(), 150.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                }
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(13, -1);
                IPCThreeFalseEyesActivity.this.touchView.setLayoutParams(layoutParams);
                IPCThreeFalseEyesActivity.this.binding.rlTouchView.addView(IPCThreeFalseEyesActivity.this.touchView);
            } else {
                IPCThreeFalseEyesActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad3);
                int dimensionPixelSize2 = IPCThreeFalseEyesActivity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                IPCThreeFalseEyesActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCThreeFalseEyesActivity.this.getActivity(), 120.0f) + (dimensionPixelSize2 * 2), dimensionPixelSize2);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams2.addRule(12, -1);
                IPCThreeFalseEyesActivity.this.touchView.setLayoutParams(layoutParams2);
                IPCThreeFalseEyesActivity.this.binding.fullScreen.addView(IPCThreeFalseEyesActivity.this.touchView);
            }
            IPCThreeFalseEyesActivity.this.touchView.setListener(new JoystickTouchViewListener() { // from class: activity.IPCThreeFalseEyesActivity.93.1
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
                    throw new UnsupportedOperationException("Method not decompiled: activity.IPCThreeFalseEyesActivity.AnonymousClass93.AnonymousClass1.onTouch(float, float):void");
                }

                @Override // view.JoystickTouchViewListener
                public void onReset() {
                    if (IPCThreeFalseEyesActivity.this.ptzTimer != null) {
                        IPCThreeFalseEyesActivity.this.ptzTimer.cancel();
                        IPCThreeFalseEyesActivity.this.ptzTimer = null;
                    }
                    IPCThreeFalseEyesActivity.this.lastActionTypeEnum = null;
                }

                @Override // view.JoystickTouchViewListener
                public void onActionUp() {
                    Log.e("云台", "抬起");
                    IPCThreeFalseEyesActivity.this.touchView.resetView();
                    if (IPCThreeFalseEyesActivity.this.ptzTimer != null) {
                        IPCThreeFalseEyesActivity.this.ptzTimer.cancel();
                        IPCThreeFalseEyesActivity.this.ptzTimer = null;
                    }
                    IPCThreeFalseEyesActivity.this.lastActionTypeEnum = null;
                }
            });
            IPCThreeFalseEyesActivity.this.binding.rlTouchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    public void startPTZEx(ActionTypeEnum actionTypeEnum, SpeedEnum speedEnum) {
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).startPTZEx(actionTypeEnum.getCode(), speedEnum.getCode(), new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.94
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                String str = IPCThreeFalseEyesActivity.this.TAG;
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
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.95
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
        LivePlayer livePlayer2;
        LivePlayer livePlayer3;
        verifyStoragePermissions(this);
        if (this.playBall.getPlayState() != 3) {
            Toast.makeText(getActivity(), R.string.only_play_snap, 0).show();
            return;
        }
        int i = this.showMode;
        if ((i == 0 || i == 1 || i == 2) && (livePlayer = this.playGun1) != null) {
            Bitmap bitmapSnapShot = livePlayer.snapShot();
            if (bitmapSnapShot == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            }
            if (bitmapSnapShot != null) {
                scanFile(bitmapSnapShot);
                if (Build.VERSION.SDK_INT >= 29) {
                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapSnapShot, "IMG" + Calendar.getInstance().getTime(), (String) null);
                } else {
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmapSnapShot, "", "");
                }
            }
        }
        int i2 = this.showMode;
        if ((i2 == 0 || i2 == 1 || i2 == 3) && (livePlayer2 = this.playGun2) != null) {
            Bitmap bitmapSnapShot2 = livePlayer2.snapShot();
            if (bitmapSnapShot2 == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            }
            if (bitmapSnapShot2 != null) {
                scanFile(bitmapSnapShot2);
                if (Build.VERSION.SDK_INT >= 29) {
                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapSnapShot2, "IMG" + Calendar.getInstance().getTime(), (String) null);
                } else {
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmapSnapShot2, "", "");
                }
            }
        }
        int i3 = this.showMode;
        if ((i3 == 0 || i3 == 1 || i3 == 4) && (livePlayer3 = this.playBall) != null) {
            Bitmap bitmapSnapShot3 = livePlayer3.snapShot();
            if (bitmapSnapShot3 == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            } else if (bitmapSnapShot3 != null) {
                scanFile(bitmapSnapShot3);
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmapSnapShot3, "", "");
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
        this.timer.schedule(new TimerTask() { // from class: activity.IPCThreeFalseEyesActivity.96
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.96.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCThreeFalseEyesActivity.this.binding.timer.setText(IPCThreeFalseEyesActivity.this.transformTime(IPCThreeFalseEyesActivity.this.i));
                    }
                });
                IPCThreeFalseEyesActivity.this.i++;
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
            IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).changeEZoom(i, 0, SharePreferenceManager.getInstance().getStreamVideoQuality(this.ballDevice.getIotId()) == 2 ? 0 : 1, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.97
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        final JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCThreeFalseEyesActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                        boolean z2 = true;
                        if (object.getInteger("ZoomIsMax").intValue() != 1) {
                            z2 = false;
                        }
                        iPCThreeFalseEyesActivity.ZoomIsMax = z2;
                        IPCThreeFalseEyesActivity.this.zoom.postValue(Float.valueOf(object.getInteger("Lens").intValue()));
                        IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.97.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCThreeFalseEyesActivity.this.binding.tvZoom.setText(object.getInteger("Lens") + "X");
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
            IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).changeZoom(i, this.binding.playerBall.getTimes(), new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.98
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCThreeFalseEyesActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCThreeFalseEyesActivity.this.ZoomIsMax = object.getBoolean("ZoomIsMax").booleanValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeFocus(int i) {
        IPCManager.getInstance().getDevice(this.ballDevice.getIotId()).changeFocus(i, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.99
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCThreeFalseEyesActivity.this.TAG, "invoke focus");
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
            this.liveIntercom.start();
            Log.e("speaker----", "play");
            this.whiteProgressDialog.setText(getResources().getString(R.string.open_speech));
            this.whiteProgressDialog.show();
            return;
        }
        this.whiteProgressDialog.setText(getResources().getString(R.string.close_speech));
        this.whiteProgressDialog.show();
        runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.100
            @Override // java.lang.Runnable
            public void run() {
                IPCThreeFalseEyesActivity.this.binding.speakerBtn.clearAnimation();
            }
        });
        this.liveIntercom.stop();
        Log.e("speaker----", "stop");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLiveIntercomError() {
        runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.101
            @Override // java.lang.Runnable
            public void run() {
                if (IPCThreeFalseEyesActivity.this.isFinishing()) {
                    return;
                }
                IPCThreeFalseEyesActivity.this.whiteProgressDialog.dismiss();
            }
        });
        this.liveIntercom.stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecordError() {
        handleLiveIntercomError();
        runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.102
            @Override // java.lang.Runnable
            public void run() {
                IPCThreeFalseEyesActivity.this.binding.speakerBtn.setSelected(false);
                IPCThreeFalseEyesActivity.this.binding.fullIntercom.setSelected(IPCThreeFalseEyesActivity.this.binding.speakerBtn.isSelected());
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
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE).setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.IPCThreeFalseEyesActivity.103
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, IPCThreeFalseEyesActivity.this.TAG, "onFailure");
                Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.share_failed, 0).show();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                final int code = ioTResponse.getCode();
                Log.e(IPCThreeFalseEyesActivity.this.TAG, "shareDevice onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.103.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCThreeFalseEyesActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            if (code == 2077) {
                                DialogUtil.showTipsConfirmDiaLog(IPCThreeFalseEyesActivity.this.getActivity(), IPCThreeFalseEyesActivity.this.getString(R.string.sharing_failed), IPCThreeFalseEyesActivity.this.getString(R.string.sharing_tips_1) + SdkConstant.CLOUDAPI_LF + IPCThreeFalseEyesActivity.this.getString(R.string.sharing_tips_2) + SdkConstant.CLOUDAPI_LF + IPCThreeFalseEyesActivity.this.getString(R.string.sharing_tips_3) + SdkConstant.CLOUDAPI_LF + IPCThreeFalseEyesActivity.this.getString(R.string.sharing_tips_4), IPCThreeFalseEyesActivity.this.getString(R.string.i_know));
                                return;
                            }
                            Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), localizedMsg, 0).show();
                        }
                    });
                } else {
                    IPCThreeFalseEyesActivity.this.handler.post(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.103.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCThreeFalseEyesActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), IPCThreeFalseEyesActivity.this.getString(R.string.share_succeed, new Object[]{((DeviceInfoBean) IPCThreeFalseEyesActivity.this.shareDialog2.getExtra()).getName(), str}), 0).show();
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
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: activity.IPCThreeFalseEyesActivity.104
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.104.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setDayNightMode(str, Integer.parseInt(obj.toString()));
                            IPCThreeFalseEyesActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.104.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCThreeFalseEyesActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
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

    public void VerticalPlay() {
        this.binding.llFull.setVisibility(8);
        this.binding.viewBack.setVisibility(0);
        this.binding.viewBack.bringToFront();
        this.binding.playerGun1.reset2();
        this.binding.playerGun2.reset2();
        new Handler().postDelayed(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.105
            @Override // java.lang.Runnable
            public void run() {
                IPCThreeFalseEyesActivity.this.binding.playerGun1.Left(IPCThreeFalseEyesActivity.this, 1.0d, 1.6d);
                IPCThreeFalseEyesActivity.this.binding.playerGun2.Right(IPCThreeFalseEyesActivity.this, 1.0d, 1.6d);
            }
        }, 600L);
        new Handler().postDelayed(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.106
            @Override // java.lang.Runnable
            public void run() {
                IPCThreeFalseEyesActivity.this.binding.playerGun1.Zomm(1.9d);
                IPCThreeFalseEyesActivity.this.binding.playerGun2.Zomm(1.9d);
            }
        }, 650L);
        new Handler().postDelayed(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.107
            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            @Override // java.lang.Runnable
            public void run() {
                MyGlTextureView myGlTextureView = IPCThreeFalseEyesActivity.this.binding.playerGun1;
                IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity = IPCThreeFalseEyesActivity.this;
                myGlTextureView.LeftMove(iPCThreeFalseEyesActivity, ((double) iPCThreeFalseEyesActivity.w) / 4.5d, IPCThreeFalseEyesActivity.this.h / 26);
                MyGlTextureView myGlTextureView2 = IPCThreeFalseEyesActivity.this.binding.playerGun2;
                IPCThreeFalseEyesActivity iPCThreeFalseEyesActivity2 = IPCThreeFalseEyesActivity.this;
                myGlTextureView2.RightMove(iPCThreeFalseEyesActivity2, ((double) iPCThreeFalseEyesActivity2.w) / 4.5d, IPCThreeFalseEyesActivity.this.h / 26);
                IPCThreeFalseEyesActivity.this.binding.viewBack.setVisibility(8);
                IPCThreeFalseEyesActivity.this.binding.llFull.setVisibility(0);
                IPCThreeFalseEyesActivity.this.binding.tvTime1.setVisibility(0);
                IPCThreeFalseEyesActivity.this.binding.tvTime1.bringToFront();
                IPCThreeFalseEyesActivity.this.binding.tvTime2.setVisibility(0);
                IPCThreeFalseEyesActivity.this.binding.tvTime2.bringToFront();
            }
        }, 700L);
    }

    public void HorizontalPlay() {
        this.binding.viewBack.setVisibility(0);
        this.binding.viewBack.bringToFront();
        this.binding.playerGun1.reset();
        this.binding.playerGun2.reset();
        new Handler().postDelayed(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.108
            @Override // java.lang.Runnable
            public void run() {
            }
        }, 700L);
        new Handler().postDelayed(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.109
            @Override // java.lang.Runnable
            public void run() {
                IPCThreeFalseEyesActivity.this.binding.playerGun1.Zomm(1.6d);
                IPCThreeFalseEyesActivity.this.binding.playerGun2.Zomm(1.6d);
            }
        }, 750L);
        new Handler().postDelayed(new Runnable() { // from class: activity.IPCThreeFalseEyesActivity.110
            @Override // java.lang.Runnable
            public void run() {
                int i = (int) (((double) IPCThreeFalseEyesActivity.this.w) / 3.2d);
                Log.e("宽度", i + "");
                double d2 = (double) i;
                IPCThreeFalseEyesActivity.this.binding.playerGun1.LeftMove(IPCThreeFalseEyesActivity.this, d2, 0.0d);
                IPCThreeFalseEyesActivity.this.binding.playerGun2.RightMove(IPCThreeFalseEyesActivity.this, d2, 0.0d);
                IPCThreeFalseEyesActivity.this.binding.viewBack.setVisibility(8);
            }
        }, 850L);
    }
}
