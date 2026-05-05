package activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import bean.AlertInfoBean;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityMotionDetectBinding;
import config.AppConfig;
import config.Constants;
import dialog.DialogUtil;
import event.EventType;
import event.MyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kt.AreaDetectActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.android.agoo.message.MessageService;
import org.greenrobot.eventbus.EventBus;
import sdk.IPCManager;
import tools.FileUtil;
import tools.LogEx;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.SelectorDialogFragment;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class MotionDetectActivity extends CommonActivity {
    private ActivityMotionDetectBinding binding;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    SwitchCompat deviceAutoLocateSwitch;
    private SelectorDialogFragment faceDetectBulletDialogFragment;
    private SelectorDialogFragment faceDialogFragment;
    private String[] jingjieModel;
    private SelectorDialogFragment jingjieModelFragment;
    SwitchCompat kuaXianSwitch;
    SwitchCompat lianDongSwitch;
    private DeviceInfoBean nvrDevice;
    private boolean pushSwitch;
    private boolean pushSwitch2;
    SwitchCompat quYuSwitch;
    private String[] renXingRenLianZhenCeArr;
    private SelectorDialogFragment renXingRenLianZhenCeFragment;
    private String[] renXingRenLianZhenCeValue;
    SwitchCompat renXingZhuiZongSwitch;
    private String selectIotId;
    private boolean strongPushSwitch;
    SwitchCompat swMobilePush;
    SwitchCompat swMobilePush2;
    SwitchCompat swMobileStrongPush;
    private String[] yiDongZhenCeLingMingDu;
    private SelectorDialogFragment yiDongZhenCeLingMingDuFragment;
    private String[] yiDongZhenCeLingMingDuValue;
    SwitchCompat yiDongZhuiZongSwitch;
    private Handler uiHandler = new Handler(new AnonymousClass1());
    private List<SwitchCompat> switchCompatList = new ArrayList();
    private boolean isOutCard = false;
    private boolean isExit = false;
    private boolean isFirstQueryOutCard = true;
    private SharePreferenceManager.OnCallSetListener mSPModifyListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.MotionDetectActivity.37
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.37.1
                @Override // java.lang.Runnable
                public void run() {
                    String str3 = str2;
                    if (str3 == null || str3.trim().equals("")) {
                        return;
                    }
                    if (str2.equals(MotionDetectActivity.this.getString(R.string.push_switch_key))) {
                        if (SharePreferenceManager.getInstance().getEventRecord(str) != 1) {
                            MotionDetectActivity.this.pushSwitch2 = SharePreferenceManager.getInstance().getPushSwitch(str);
                            MotionDetectActivity.this.swMobilePush2.setChecked(MotionDetectActivity.this.pushSwitch2);
                            return;
                        }
                        return;
                    }
                    if (!str2.equals(MotionDetectActivity.this.getString(R.string.strong_reminder_switch)) || SharePreferenceManager.getInstance().getEventRecord(str) == 1) {
                        return;
                    }
                    MotionDetectActivity.this.strongPushSwitch = SharePreferenceManager.getInstance().getStrongReminderSwitch(str) == 1;
                    MotionDetectActivity.this.swMobileStrongPush.setChecked(MotionDetectActivity.this.strongPushSwitch);
                }
            });
        }
    };
    private OnMultiClickListener deviceAutoLocateListener = new OnMultiClickListener() { // from class: activity.MotionDetectActivity.56
        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            MotionDetectActivity.this.setIvpZoomEnable(SharePreferenceManager.getInstance().getIvpZoomEnable(MotionDetectActivity.this.selectIotId) == 1 ? 0 : 1);
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_motion_detect;
    }

    /* JADX INFO: renamed from: activity.MotionDetectActivity$1, reason: invalid class name */
    class AnonymousClass1 implements Handler.Callback {
        AnonymousClass1() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 0:
                    if (!MotionDetectActivity.this.isExit) {
                        IPCManager.getInstance().getDevice(MotionDetectActivity.this.selectIotId).getQueryFileList(0, new C01101());
                    }
                    break;
                case 1:
                    if (!MotionDetectActivity.this.isExit) {
                        MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                MotionDetectActivity.this.hideLoadingDialog();
                                DialogUtil.showSuccessDiaLog(MotionDetectActivity.this, MotionDetectActivity.this.getString(R.string.config_overtime), 220, 3000);
                            }
                        });
                    }
                    break;
            }
            return true;
        }

        /* JADX INFO: renamed from: activity.MotionDetectActivity$1$1, reason: invalid class name and collision with other inner class name */
        class C01101 implements IPanelCallback {
            C01101() {
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                    AlertInfoBean alertInfoBean = (AlertInfoBean) JSON.parseObject(obj.toString(), AlertInfoBean.class);
                    boolean z2 = false;
                    for (int i = 0; i < alertInfoBean.getData().getFileList().size(); i++) {
                        if (alertInfoBean.getData().getFileList().get(i).getFileName().equals("ivp")) {
                            MotionDetectActivity.this.isExit = true;
                            z2 = true;
                        }
                    }
                    if (!z2) {
                        MotionDetectActivity.this.HandlerSend();
                    } else {
                        MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCManager.getInstance().getDevice(MotionDetectActivity.this.selectIotId).reboot(new IPanelCallback() { // from class: activity.MotionDetectActivity.1.1.1.1
                                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                                    public void onComplete(boolean z3, Object obj2) {
                                        Log.e(MotionDetectActivity.this.TAG, "reboot onComplete: " + z3);
                                    }
                                });
                                MotionDetectActivity.this.hideLoadingDialog();
                                DialogUtil.showSuccessDiaLog(MotionDetectActivity.this, MotionDetectActivity.this.getString(R.string.config_success), 280, 5000);
                                new Handler().postDelayed(new Runnable() { // from class: activity.MotionDetectActivity.1.1.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        EventBus.getDefault().post(new MyEvent(EventType.FINISH_ACTIVITY, null));
                                        MotionDetectActivity.this.finish();
                                    }
                                }, 5000L);
                            }
                        });
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void HandlerSend() {
        Message messageObtain = Message.obtain();
        messageObtain.what = 0;
        this.uiHandler.sendMessageDelayed(messageObtain, 1000L);
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            this.selectIotId = this.device.getIotId();
        }
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityMotionDetectBinding) DataBindingUtil.setContentView(this, R.layout.activity_motion_detect);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.MotionDetectActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                MotionDetectActivity.this.finish();
            }
        });
        FileUtil.copyFilesFromRaw(this, R.raw.ivp, "ivp.oms", FileUtil.getFilesPath(this));
        this.binding.renXingZhuiZong.addRightView(new SwitchCompat(this));
        this.renXingZhuiZongSwitch = (SwitchCompat) this.binding.renXingZhuiZong.getRightView();
        this.renXingZhuiZongSwitch.setTextOff("");
        this.renXingZhuiZongSwitch.setTextOn("");
        this.renXingZhuiZongSwitch.setText("");
        this.renXingZhuiZongSwitch.setThumbDrawable(null);
        this.renXingZhuiZongSwitch.setBackgroundResource(R.drawable.sel_switch);
        if (SharePreferenceManager.getInstance().getHumanoidTracking(this.device.getIotId()).intValue() == 1) {
            this.renXingZhuiZongSwitch.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (SharePreferenceManager.getInstance().getTlrClRgn(MotionDetectActivity.this.selectIotId).intValue() == 1) {
                        if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                            if (MotionDetectActivity.this.quYuSwitch.isChecked()) {
                                if (MotionDetectActivity.this.kuaXianSwitch.isChecked()) {
                                    MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                                    motionDetectActivity.checkSwitch(7, motionDetectActivity.renXingZhuiZongSwitch);
                                } else {
                                    MotionDetectActivity motionDetectActivity2 = MotionDetectActivity.this;
                                    motionDetectActivity2.checkSwitch(5, motionDetectActivity2.renXingZhuiZongSwitch);
                                }
                            } else if (MotionDetectActivity.this.kuaXianSwitch.isChecked()) {
                                MotionDetectActivity motionDetectActivity3 = MotionDetectActivity.this;
                                motionDetectActivity3.checkSwitch(3, motionDetectActivity3.renXingZhuiZongSwitch);
                            } else {
                                MotionDetectActivity motionDetectActivity4 = MotionDetectActivity.this;
                                motionDetectActivity4.checkSwitch(1, motionDetectActivity4.renXingZhuiZongSwitch);
                            }
                        } else if (MotionDetectActivity.this.quYuSwitch.isChecked()) {
                            if (MotionDetectActivity.this.kuaXianSwitch.isChecked()) {
                                MotionDetectActivity motionDetectActivity5 = MotionDetectActivity.this;
                                motionDetectActivity5.checkSwitch(6, motionDetectActivity5.renXingZhuiZongSwitch);
                            } else {
                                MotionDetectActivity motionDetectActivity6 = MotionDetectActivity.this;
                                motionDetectActivity6.checkSwitch(4, motionDetectActivity6.renXingZhuiZongSwitch);
                            }
                        } else if (MotionDetectActivity.this.kuaXianSwitch.isChecked()) {
                            MotionDetectActivity motionDetectActivity7 = MotionDetectActivity.this;
                            motionDetectActivity7.checkSwitch(2, motionDetectActivity7.renXingZhuiZongSwitch);
                        } else {
                            MotionDetectActivity motionDetectActivity8 = MotionDetectActivity.this;
                            motionDetectActivity8.checkSwitch(0, motionDetectActivity8.renXingZhuiZongSwitch);
                        }
                    } else if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                        MotionDetectActivity motionDetectActivity9 = MotionDetectActivity.this;
                        motionDetectActivity9.checkSwitch(1, motionDetectActivity9.renXingZhuiZongSwitch);
                    } else {
                        MotionDetectActivity motionDetectActivity10 = MotionDetectActivity.this;
                        motionDetectActivity10.checkSwitch(0, motionDetectActivity10.renXingZhuiZongSwitch);
                    }
                    if (!MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked() || SharePreferenceManager.getInstance().getNewSupportEZOOM(MotionDetectActivity.this.selectIotId) != 1) {
                        MotionDetectActivity.this.binding.deviceAutoLocate.setVisibility(8);
                    } else {
                        MotionDetectActivity.this.binding.deviceAutoLocate.setVisibility(0);
                    }
                }
            });
        } else {
            this.renXingZhuiZongSwitch.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    MotionDetectActivity.this.showProgressDialog();
                    MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                    motionDetectActivity.setMobileTracking(motionDetectActivity.renXingZhuiZongSwitch.isChecked() ? 1 : 0);
                    if (!MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked() || SharePreferenceManager.getInstance().getNewSupportEZOOM(MotionDetectActivity.this.selectIotId) != 1) {
                        MotionDetectActivity.this.binding.deviceAutoLocate.setVisibility(8);
                    } else {
                        MotionDetectActivity.this.binding.deviceAutoLocate.setVisibility(0);
                    }
                }
            });
        }
        this.binding.yiDongZhenCeLingMingDu.setOnClickListener(new OnMultiClickListener() { // from class: activity.MotionDetectActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                MotionDetectActivity.this.yiDongZhenCeLingMingDuFragment.showAllowingStateLoss(MotionDetectActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itemAlarmTime.setOnClickListener(new OnMultiClickListener() { // from class: activity.MotionDetectActivity.6
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (SharePreferenceManager.getInstance().getAlarmPlanVersion(MotionDetectActivity.this.device.getIotId()) == 0) {
                    Intent intent = new Intent(MotionDetectActivity.this.getActivity(), (Class<?>) AlarmPlanSettingsActivity.class);
                    intent.putExtra("iotId", MotionDetectActivity.this.device.getIotId());
                    MotionDetectActivity.this.startActivity(intent);
                } else {
                    Intent intent2 = new Intent(MotionDetectActivity.this.getActivity(), (Class<?>) AlarmPlanActivity.class);
                    intent2.putExtra("iotId", MotionDetectActivity.this.device.getIotId());
                    MotionDetectActivity.this.startActivity(intent2);
                }
            }
        });
        this.binding.itemAlarmMode.setOnClickListener(new OnMultiClickListener() { // from class: activity.MotionDetectActivity.7
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                MotionDetectActivity.this.jingjieModelFragment.showAllowingStateLoss(MotionDetectActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.lianDong.addRightView(new SwitchCompat(this));
        this.lianDongSwitch = (SwitchCompat) this.binding.lianDong.getRightView();
        this.lianDongSwitch.setTextOff("");
        this.lianDongSwitch.setTextOn("");
        this.lianDongSwitch.setText("");
        this.lianDongSwitch.setThumbDrawable(null);
        this.lianDongSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.yiDongZhenCeLingMingDu = getResources().getStringArray(R.array.MotionDetectSensitivity);
        this.yiDongZhenCeLingMingDuValue = getResources().getStringArray(R.array.MotionDetectSensitivityValue);
        this.yiDongZhenCeLingMingDuFragment = new SelectorDialogFragment(getString(R.string.motion_detect_sensitivity_title), this.yiDongZhenCeLingMingDu);
        this.yiDongZhenCeLingMingDuFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.MotionDetectActivity.8
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.updateDevParam(motionDetectActivity.getString(R.string.motion_detect_sensitivity_key), MotionDetectActivity.this.yiDongZhenCeLingMingDuValue[i]);
            }
        });
        this.jingjieModel = new String[]{getResources().getString(R.string.smart_alert), getResources().getString(R.string.all_day_alert), getResources().getString(R.string.timing_alert)};
        this.jingjieModelFragment = new SelectorDialogFragment(getString(R.string.alert_mode), this.jingjieModel);
        this.jingjieModelFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.MotionDetectActivity.9
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.updateDevParam(motionDetectActivity.getString(R.string.alarm_mode_key), Integer.valueOf(i));
            }
        });
        this.binding.kuaXianSet.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(MotionDetectActivity.this.getActivity(), (Class<?>) SetTheBoundaryActivity.class);
                intent.putExtra("iotId", MotionDetectActivity.this.selectIotId);
                MotionDetectActivity.this.startActivity(intent);
            }
        });
        this.binding.quYuSet.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(MotionDetectActivity.this.getActivity(), (Class<?>) AreaDetectActivity.class);
                intent.putExtra("iotId", MotionDetectActivity.this.selectIotId);
                MotionDetectActivity.this.startActivity(intent);
            }
        });
        this.binding.deviceAutoLocate.addRightView(new SwitchCompat(this));
        this.deviceAutoLocateSwitch = (SwitchCompat) this.binding.deviceAutoLocate.getRightView();
        this.deviceAutoLocateSwitch.setTextOff("");
        this.deviceAutoLocateSwitch.setTextOn("");
        this.deviceAutoLocateSwitch.setText("");
        this.deviceAutoLocateSwitch.setThumbDrawable(null);
        this.deviceAutoLocateSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.deviceAutoLocateSwitch.setOnClickListener(this.deviceAutoLocateListener);
        this.binding.quYu.addRightView(new SwitchCompat(this));
        this.quYuSwitch = (SwitchCompat) this.binding.quYu.getRightView();
        this.quYuSwitch.setTextOff("");
        this.quYuSwitch.setTextOn("");
        this.quYuSwitch.setText("");
        this.quYuSwitch.setThumbDrawable(null);
        this.quYuSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.quYuSwitch.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (SharePreferenceManager.getInstance().getTlrClRgn(MotionDetectActivity.this.selectIotId).intValue() == 1) {
                    if (MotionDetectActivity.this.quYuSwitch.isChecked()) {
                        if (MotionDetectActivity.this.kuaXianSwitch.isChecked()) {
                            if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                                motionDetectActivity.checkSwitch(7, motionDetectActivity.quYuSwitch);
                                return;
                            } else {
                                MotionDetectActivity motionDetectActivity2 = MotionDetectActivity.this;
                                motionDetectActivity2.checkSwitch(6, motionDetectActivity2.quYuSwitch);
                                return;
                            }
                        }
                        if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                            MotionDetectActivity motionDetectActivity3 = MotionDetectActivity.this;
                            motionDetectActivity3.checkSwitch(5, motionDetectActivity3.quYuSwitch);
                            return;
                        } else {
                            MotionDetectActivity motionDetectActivity4 = MotionDetectActivity.this;
                            motionDetectActivity4.checkSwitch(4, motionDetectActivity4.quYuSwitch);
                            return;
                        }
                    }
                    if (MotionDetectActivity.this.kuaXianSwitch.isChecked()) {
                        if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                            MotionDetectActivity motionDetectActivity5 = MotionDetectActivity.this;
                            motionDetectActivity5.checkSwitch(3, motionDetectActivity5.quYuSwitch);
                            return;
                        } else {
                            MotionDetectActivity motionDetectActivity6 = MotionDetectActivity.this;
                            motionDetectActivity6.checkSwitch(2, motionDetectActivity6.quYuSwitch);
                            return;
                        }
                    }
                    if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                        MotionDetectActivity motionDetectActivity7 = MotionDetectActivity.this;
                        motionDetectActivity7.checkSwitch(1, motionDetectActivity7.quYuSwitch);
                        return;
                    } else {
                        MotionDetectActivity motionDetectActivity8 = MotionDetectActivity.this;
                        motionDetectActivity8.checkSwitch(0, motionDetectActivity8.quYuSwitch);
                        return;
                    }
                }
                if (MotionDetectActivity.this.quYuSwitch.isChecked()) {
                    MotionDetectActivity motionDetectActivity9 = MotionDetectActivity.this;
                    motionDetectActivity9.checkSwitch(4, motionDetectActivity9.quYuSwitch);
                } else {
                    MotionDetectActivity motionDetectActivity10 = MotionDetectActivity.this;
                    motionDetectActivity10.checkSwitch(0, motionDetectActivity10.quYuSwitch);
                }
            }
        });
        this.binding.kuaXian.addRightView(new SwitchCompat(this));
        this.kuaXianSwitch = (SwitchCompat) this.binding.kuaXian.getRightView();
        this.kuaXianSwitch.setTextOff("");
        this.kuaXianSwitch.setTextOn("");
        this.kuaXianSwitch.setText("");
        this.kuaXianSwitch.setThumbDrawable(null);
        this.kuaXianSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.kuaXianSwitch.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (SharePreferenceManager.getInstance().getTlrClRgn(MotionDetectActivity.this.selectIotId).intValue() == 1) {
                    if (MotionDetectActivity.this.kuaXianSwitch.isChecked()) {
                        if (MotionDetectActivity.this.quYuSwitch.isChecked()) {
                            if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                                motionDetectActivity.checkSwitch(7, motionDetectActivity.kuaXianSwitch);
                                return;
                            } else {
                                MotionDetectActivity motionDetectActivity2 = MotionDetectActivity.this;
                                motionDetectActivity2.checkSwitch(6, motionDetectActivity2.kuaXianSwitch);
                                return;
                            }
                        }
                        if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                            MotionDetectActivity motionDetectActivity3 = MotionDetectActivity.this;
                            motionDetectActivity3.checkSwitch(3, motionDetectActivity3.kuaXianSwitch);
                            return;
                        } else {
                            MotionDetectActivity motionDetectActivity4 = MotionDetectActivity.this;
                            motionDetectActivity4.checkSwitch(2, motionDetectActivity4.kuaXianSwitch);
                            return;
                        }
                    }
                    if (MotionDetectActivity.this.quYuSwitch.isChecked()) {
                        if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                            MotionDetectActivity motionDetectActivity5 = MotionDetectActivity.this;
                            motionDetectActivity5.checkSwitch(5, motionDetectActivity5.kuaXianSwitch);
                            return;
                        } else {
                            MotionDetectActivity motionDetectActivity6 = MotionDetectActivity.this;
                            motionDetectActivity6.checkSwitch(4, motionDetectActivity6.kuaXianSwitch);
                            return;
                        }
                    }
                    if (MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked()) {
                        MotionDetectActivity motionDetectActivity7 = MotionDetectActivity.this;
                        motionDetectActivity7.checkSwitch(1, motionDetectActivity7.kuaXianSwitch);
                        return;
                    } else {
                        MotionDetectActivity motionDetectActivity8 = MotionDetectActivity.this;
                        motionDetectActivity8.checkSwitch(0, motionDetectActivity8.kuaXianSwitch);
                        return;
                    }
                }
                if (MotionDetectActivity.this.kuaXianSwitch.isChecked()) {
                    MotionDetectActivity motionDetectActivity9 = MotionDetectActivity.this;
                    motionDetectActivity9.checkSwitch(2, motionDetectActivity9.kuaXianSwitch);
                } else {
                    MotionDetectActivity motionDetectActivity10 = MotionDetectActivity.this;
                    motionDetectActivity10.checkSwitch(0, motionDetectActivity10.kuaXianSwitch);
                }
            }
        });
        this.renXingRenLianZhenCeArr = getResources().getStringArray(R.array.FaceDetectSensitivity);
        this.renXingRenLianZhenCeValue = getResources().getStringArray(R.array.FaceDetectSensitivityValue);
        this.renXingRenLianZhenCeFragment = new SelectorDialogFragment(getString(R.string.humanoid_detection), this.renXingRenLianZhenCeArr);
        this.renXingRenLianZhenCeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.MotionDetectActivity.14
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.updateDevParam(motionDetectActivity.getResources().getString(R.string.face_detect_key), MotionDetectActivity.this.renXingRenLianZhenCeValue[i]);
            }
        });
        this.binding.renXingZhenCe.setOnClickListener(new OnMultiClickListener() { // from class: activity.MotionDetectActivity.15
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (SharePreferenceManager.getInstance().getTFStorageIVP(MotionDetectActivity.this.selectIotId).intValue() == 1) {
                    if (SharePreferenceManager.getInstance().getStorageStatus(MotionDetectActivity.this.selectIotId) != 0) {
                        if (SharePreferenceManager.getInstance().getStorageStatus(MotionDetectActivity.this.selectIotId) != 2) {
                            MotionDetectActivity.this.getFileList();
                            return;
                        } else {
                            MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                            DialogUtil.showTipsConfirmDiaLog(motionDetectActivity, motionDetectActivity.getString(R.string.config_fail), MotionDetectActivity.this.getString(R.string.check_tf_state), MotionDetectActivity.this.getString(R.string.i_know));
                            return;
                        }
                    }
                    MotionDetectActivity motionDetectActivity2 = MotionDetectActivity.this;
                    DialogUtil.showConfirmDiaLog(motionDetectActivity2, motionDetectActivity2.getString(R.string.no_tf), MotionDetectActivity.this.getString(R.string.i_know));
                    return;
                }
                MotionDetectActivity.this.renXingRenLianZhenCeFragment.showAllowingStateLoss(MotionDetectActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.yiDongZhuiZong.addRightView(new SwitchCompat(this));
        this.yiDongZhuiZongSwitch = (SwitchCompat) this.binding.yiDongZhuiZong.getRightView();
        this.yiDongZhuiZongSwitch.setTextOff("");
        this.yiDongZhuiZongSwitch.setTextOn("");
        this.yiDongZhuiZongSwitch.setText("");
        this.yiDongZhuiZongSwitch.setThumbDrawable(null);
        this.yiDongZhuiZongSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.yiDongZhuiZongSwitch.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.16
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MotionDetectActivity.this.showProgressDialog();
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.setMobileTracking(motionDetectActivity.yiDongZhuiZongSwitch.isChecked() ? 1 : 0);
            }
        });
        this.binding.itemMobilePush.addRightView(new SwitchCompat(this));
        this.swMobilePush = (SwitchCompat) this.binding.itemMobilePush.getRightView();
        this.swMobilePush.setTextOff("");
        this.swMobilePush.setTextOn("");
        this.swMobilePush.setText("");
        this.swMobilePush.setThumbDrawable(null);
        this.swMobilePush.setBackgroundResource(R.drawable.sel_switch);
        this.swMobilePush.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.17
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MotionDetectActivity.this.pushSwitch = !r4.pushSwitch;
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.updateDevPush(motionDetectActivity.pushSwitch, MotionDetectActivity.this.selectIotId);
                if (MotionDetectActivity.this.device1 != null && MotionDetectActivity.this.device2 != null && MotionDetectActivity.this.selectIotId.equals(MotionDetectActivity.this.device1.getIotId())) {
                    MotionDetectActivity motionDetectActivity2 = MotionDetectActivity.this;
                    motionDetectActivity2.updateDevPush(motionDetectActivity2.pushSwitch, MotionDetectActivity.this.device2.getIotId());
                }
                new Handler().postDelayed(new Runnable() { // from class: activity.MotionDetectActivity.17.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MotionDetectActivity.this.getYunPush();
                    }
                }, 50L);
            }
        });
        this.binding.itemMobilePush2.addRightView(new SwitchCompat(this));
        this.swMobilePush2 = (SwitchCompat) this.binding.itemMobilePush2.getRightView();
        this.swMobilePush2.setTextOff("");
        this.swMobilePush2.setTextOn("");
        this.swMobilePush2.setText("");
        this.swMobilePush2.setThumbDrawable(null);
        this.swMobilePush2.setBackgroundResource(R.drawable.sel_switch);
        this.swMobilePush2.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.18
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MotionDetectActivity.this.pushSwitch2 = !r4.pushSwitch2;
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.updateDevPush2(motionDetectActivity.pushSwitch2, MotionDetectActivity.this.selectIotId);
                if (MotionDetectActivity.this.device1 != null && MotionDetectActivity.this.device2 != null && MotionDetectActivity.this.selectIotId.equals(MotionDetectActivity.this.device1.getIotId())) {
                    MotionDetectActivity motionDetectActivity2 = MotionDetectActivity.this;
                    motionDetectActivity2.updateDevPush2(motionDetectActivity2.pushSwitch2, MotionDetectActivity.this.device2.getIotId());
                }
                new Handler().postDelayed(new Runnable() { // from class: activity.MotionDetectActivity.18.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MotionDetectActivity.this.getYunPush2();
                    }
                }, 50L);
            }
        });
        this.binding.itemMobileStrongPush.addRightView(new SwitchCompat(this));
        this.swMobileStrongPush = (SwitchCompat) this.binding.itemMobileStrongPush.getRightView();
        this.swMobileStrongPush.setTextOff("");
        this.swMobileStrongPush.setTextOn("");
        this.swMobileStrongPush.setText("");
        this.swMobileStrongPush.setThumbDrawable(null);
        this.swMobileStrongPush.setBackgroundResource(R.drawable.sel_switch);
        this.swMobileStrongPush.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MotionDetectActivity.this.strongPushSwitch = !r4.strongPushSwitch;
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.updateDevCallPush(motionDetectActivity.strongPushSwitch, MotionDetectActivity.this.selectIotId);
                if (MotionDetectActivity.this.device1 != null && MotionDetectActivity.this.device2 != null && MotionDetectActivity.this.selectIotId.equals(MotionDetectActivity.this.device1.getIotId())) {
                    MotionDetectActivity motionDetectActivity2 = MotionDetectActivity.this;
                    motionDetectActivity2.updateDevCallPush(motionDetectActivity2.strongPushSwitch, MotionDetectActivity.this.device2.getIotId());
                }
                new Handler().postDelayed(new Runnable() { // from class: activity.MotionDetectActivity.19.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MotionDetectActivity.this.getYunCallPush();
                    }
                }, 50L);
            }
        });
        this.binding.layoutPtz.setOnClickListener(new OnMultiClickListener() { // from class: activity.MotionDetectActivity.20
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.selectIotId = motionDetectActivity.device.getIotId();
                MotionDetectActivity.this.binding.layoutBullet.setSelected(false);
                MotionDetectActivity.this.binding.layoutPtz.setSelected(true);
                MotionDetectActivity.this.binding.layoutBulletShow.setVisibility(8);
                MotionDetectActivity.this.binding.layoutPtzShow.setVisibility(0);
                MotionDetectActivity.this.binding.itemAlarmMode.setVisibility(0);
                MotionDetectActivity.this.binding.alarmText.setVisibility(0);
                MotionDetectActivity.this.initPush();
                MotionDetectActivity.this.updateAlarmTime();
            }
        });
        this.binding.layoutBullet.setOnClickListener(new OnMultiClickListener() { // from class: activity.MotionDetectActivity.21
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.selectIotId = motionDetectActivity.device1.getIotId();
                MotionDetectActivity.this.binding.layoutPtz.setSelected(false);
                MotionDetectActivity.this.binding.layoutBullet.setSelected(true);
                MotionDetectActivity.this.binding.layoutPtzShow.setVisibility(8);
                MotionDetectActivity.this.binding.layoutBulletShow.setVisibility(0);
                MotionDetectActivity.this.initPush();
                MotionDetectActivity.this.binding.itemAlarmMode.setVisibility(8);
                MotionDetectActivity.this.binding.itemAlarmTime.setVisibility(8);
                MotionDetectActivity.this.binding.alarmText.setVisibility(8);
            }
        });
        this.lianDongSwitch.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.22
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SharePreferenceManager.getInstance().setPTZLinkageTrackSwitch(MotionDetectActivity.this.device1.getIotId(), SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(MotionDetectActivity.this.device1.getIotId()) == 0 ? 1 : 0);
            }
        });
        this.faceDialogFragment = new SelectorDialogFragment(getString(R.string.humanoid_detection), this.yiDongZhenCeLingMingDu);
        this.faceDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.MotionDetectActivity.23
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.updateDevParam(motionDetectActivity.getString(R.string.face_detect_key), MotionDetectActivity.this.yiDongZhenCeLingMingDuValue[i]);
            }
        });
        this.binding.renXingZhenCeBullet.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.24
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MotionDetectActivity.this.faceDialogFragment.showAllowingStateLoss(MotionDetectActivity.this.getSupportFragmentManager(), "", SharePreferenceManager.getInstance().getFaceDetectMode(MotionDetectActivity.this.device1.getIotId()));
            }
        });
        this.faceDetectBulletDialogFragment = new SelectorDialogFragment(getString(R.string.motion_detect_sensitivity_title), this.yiDongZhenCeLingMingDu);
        this.faceDetectBulletDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.MotionDetectActivity.25
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                MotionDetectActivity motionDetectActivity = MotionDetectActivity.this;
                motionDetectActivity.updateDevParam(motionDetectActivity.getString(R.string.motion_detect_sensitivity_key), MotionDetectActivity.this.yiDongZhenCeLingMingDuValue[i]);
            }
        });
        this.binding.yiDongZhenCeLingMingDuBullet.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.26
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MotionDetectActivity.this.faceDetectBulletDialogFragment.showAllowingStateLoss(MotionDetectActivity.this.getSupportFragmentManager(), "", SharePreferenceManager.getInstance().getMotionDetectSensitivity(MotionDetectActivity.this.device1.getIotId()));
            }
        });
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.mSPModifyListener);
        this.switchCompatList.clear();
        this.switchCompatList.add(this.yiDongZhuiZongSwitch);
        this.switchCompatList.add(this.quYuSwitch);
        this.switchCompatList.add(this.kuaXianSwitch);
        SettingsCtrl.getInstance().getProperties(this.device.getIotId(), new MyCallback() { // from class: activity.MotionDetectActivity.27
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        Log.e("排序球机dn", " :" + this.device.getDeviceName());
        if (this.device1 != null) {
            Log.e("排序枪机1dn", " :" + this.device1.getDeviceName());
            SettingsCtrl.getInstance().getProperties(this.device1.getIotId(), new MyCallback() { // from class: activity.MotionDetectActivity.28
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.28.1
                        @Override // java.lang.Runnable
                        public void run() {
                            MotionDetectActivity.this.lianDongSwitch.setChecked(SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(MotionDetectActivity.this.device1.getIotId()) == 1);
                        }
                    });
                }
            });
        }
        if (this.device2 != null) {
            Log.e("排序枪机2dn", " :" + this.device2.getDeviceName());
            SettingsCtrl.getInstance().getProperties(this.device2.getIotId(), new MyCallback() { // from class: activity.MotionDetectActivity.29
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
        }
        getData();
    }

    private void getData() {
        if (this.device1 != null) {
            this.binding.layoutSwitch.setVisibility(0);
            this.binding.layoutPtz.setSelected(true);
            this.lianDongSwitch.setChecked(SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(this.device1.getIotId()) == 1);
            if (SharePreferenceManager.getInstance().getSupportMotionDetect(this.device1.getIotId()) == 1) {
                this.binding.yiDongZhenCeLingMingDuBullet.setRightText(this.yiDongZhenCeLingMingDu[SharePreferenceManager.getInstance().getMotionDetectSensitivity(this.device1.getIotId())]);
                this.binding.yiDongZhenCeLingMingDuBullet.setVisibility(0);
                this.binding.renXingZhenCeBullet.setVisibility(8);
            }
            if (-1 != SharePreferenceManager.getInstance().getFaceDetectMode(this.device1.getIotId())) {
                this.binding.renXingZhenCeBullet.setRightText(this.yiDongZhenCeLingMingDu[SharePreferenceManager.getInstance().getFaceDetectMode(this.device1.getIotId())]);
            }
        }
        if (this.device2 != null) {
            this.binding.lianDong.setVisibility(8);
        }
        if (SharePreferenceManager.getInstance().getSupportMotionDetect(this.selectIotId) == 0 || SharePreferenceManager.getInstance().getMotionDetectSensitivity(this.selectIotId) == 0) {
            this.binding.yiDongZhuiZong.setVisibility(8);
        }
        SharePreferenceManager.getInstance().getEventRecord(this.device.getIotId());
        if (SharePreferenceManager.getInstance().getHumanoidTracking(this.selectIotId).intValue() == 1) {
            this.yiDongZhuiZongSwitch.setChecked(SharePreferenceManager.getInstance().getHumanoidTrackingEnable(this.selectIotId).intValue() == 1);
            this.renXingZhuiZongSwitch.setChecked(SharePreferenceManager.getInstance().getHumanoidTrackingEnable(this.selectIotId).intValue() == 1);
            if (this.renXingZhuiZongSwitch.isChecked() && SharePreferenceManager.getInstance().getNewSupportEZOOM(this.selectIotId) == 1) {
                this.binding.deviceAutoLocate.setVisibility(0);
                this.deviceAutoLocateSwitch.setChecked(SharePreferenceManager.getInstance().getIvpZoomEnable(this.selectIotId) == 1);
            } else {
                this.binding.deviceAutoLocate.setVisibility(8);
            }
        } else {
            this.yiDongZhuiZongSwitch.setChecked(SharePreferenceManager.getInstance().getIntelligentMode(this.selectIotId) == 1);
            this.renXingZhuiZongSwitch.setChecked(SharePreferenceManager.getInstance().getIntelligentMode(this.selectIotId) == 1);
            if (this.renXingZhuiZongSwitch.isChecked() && SharePreferenceManager.getInstance().getNewSupportEZOOM(this.selectIotId) == 1) {
                this.binding.deviceAutoLocate.setVisibility(0);
                this.deviceAutoLocateSwitch.setChecked(SharePreferenceManager.getInstance().getIvpZoomEnable(this.selectIotId) == 1);
            } else {
                this.binding.deviceAutoLocate.setVisibility(8);
            }
        }
        if (SharePreferenceManager.getInstance().getFaceDetectMode(this.selectIotId) != -1 && SharePreferenceManager.getInstance().getSupportMotionDetect(this.selectIotId) == 0) {
            this.binding.yiDongZhenCeLingMingDu.setVisibility(8);
            this.binding.renXingZhenCe.setVisibility(0);
            this.binding.renXingZhenCe.setRightText(this.renXingRenLianZhenCeArr[SharePreferenceManager.getInstance().getFaceDetectMode(this.selectIotId)]);
            if (SharePreferenceManager.getInstance().getFaceDetectMode(this.selectIotId) == 0) {
                this.binding.renXingZhuiZong.setVisibility(8);
                this.binding.renXingZhenCe.setLineShow(false);
            } else {
                this.binding.renXingZhuiZong.setVisibility(0);
            }
        } else {
            this.binding.renXingZhenCe.setVisibility(8);
            this.binding.renXingZhuiZong.setVisibility(8);
            this.binding.yiDongZhenCeLingMingDu.setVisibility(0);
            this.binding.renXingZhenCe.setLineShow(false);
        }
        initPush();
        updateAlarmTime();
        this.binding.layoutKuaXian.setVisibility(SharePreferenceManager.getInstance().getCrossLine(this.selectIotId).intValue() == 1 ? 0 : 8);
        this.binding.layoutQuYu.setVisibility(SharePreferenceManager.getInstance().getAreaDetect(this.selectIotId).intValue() == 1 ? 0 : 8);
        if (SharePreferenceManager.getInstance().getTlrClRgn(this.selectIotId).intValue() == 1) {
            this.binding.layoutKuaXian.setVisibility(0);
            this.binding.layoutQuYu.setVisibility(0);
        }
        this.kuaXianSwitch.setChecked(SharePreferenceManager.getInstance().getCrossLineEnable(this.selectIotId).intValue() != 0);
        this.quYuSwitch.setChecked(SharePreferenceManager.getInstance().getAreaDetectEnable(this.selectIotId).intValue() != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPush() {
        this.binding.itemMobileStrongPush.setVisibility(SharePreferenceManager.getInstance().getStrongReminder(this.device.getIotId()) == 1 ? 0 : 8);
        this.binding.itemMobilePush.setLineShow(SharePreferenceManager.getInstance().getStrongReminder(this.device.getIotId()) == 1);
        getYunPush();
        getYunPush2();
        getYunCallPush();
        if (SharePreferenceManager.getInstance().getEventRecord(this.selectIotId) != 1) {
            this.pushSwitch = SharePreferenceManager.getInstance().getPushSwitch(this.selectIotId);
            this.swMobilePush.setChecked(this.pushSwitch);
        }
        if (SharePreferenceManager.getInstance().getEventRecord(this.selectIotId) == 1) {
            return;
        }
        this.strongPushSwitch = SharePreferenceManager.getInstance().getStrongReminderSwitch(this.selectIotId) == 1;
        this.swMobileStrongPush.setChecked(this.strongPushSwitch);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getYunPush() {
        HashMap map = new HashMap();
        map.put("iotId", this.selectIotId);
        map.put("eventType", 1);
        map.put("alarmType", -1);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/get").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass30());
    }

    /* JADX INFO: renamed from: activity.MotionDetectActivity$30, reason: invalid class name */
    class AnonymousClass30 implements IoTCallback {
        AnonymousClass30() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            if (ioTResponse.getCode() == 200) {
                JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                final int iIntValue = object.getInteger("eventInterval").intValue();
                final boolean zBooleanValue = object.getBoolean("switchOn").booleanValue();
                Log.e("新版推送间隔", "" + iIntValue);
                Log.e("新版推送开关", "" + zBooleanValue);
                MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.30.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MotionDetectActivity.this.pushSwitch = zBooleanValue;
                        MotionDetectActivity.this.swMobilePush.setChecked(MotionDetectActivity.this.pushSwitch);
                        if (iIntValue == AppConfig.PushInterval || !MotionDetectActivity.this.pushSwitch) {
                            return;
                        }
                        Log.e("新版推送间隔不一致", "值为" + iIntValue);
                        HashMap map = new HashMap();
                        map.put("iotId", MotionDetectActivity.this.selectIotId);
                        map.put("eventType", 1);
                        map.put("alarmType", -1);
                        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
                        map.put("switchOn", Boolean.valueOf(MotionDetectActivity.this.pushSwitch));
                        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.MotionDetectActivity.30.1.1
                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                                LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
                            }

                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                                if (ioTResponse2.getCode() == 200) {
                                    Log.e("新版推送间隔不一致", "修改成功");
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getYunPush2() {
        HashMap map = new HashMap();
        map.put("iotId", this.selectIotId);
        map.put("eventType", 1);
        map.put("alarmType", 1);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/get").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass31());
    }

    /* JADX INFO: renamed from: activity.MotionDetectActivity$31, reason: invalid class name */
    class AnonymousClass31 implements IoTCallback {
        AnonymousClass31() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            if (ioTResponse.getCode() == 200) {
                JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                final int iIntValue = object.getInteger("eventInterval").intValue();
                final boolean zBooleanValue = object.getBoolean("switchOn").booleanValue();
                Log.e("新版推送间隔", "" + iIntValue);
                Log.e("新版推送开关", "" + zBooleanValue);
                MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.31.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MotionDetectActivity.this.pushSwitch2 = zBooleanValue;
                        MotionDetectActivity.this.swMobilePush2.setChecked(MotionDetectActivity.this.pushSwitch2);
                        if (iIntValue == AppConfig.PushInterval || !MotionDetectActivity.this.pushSwitch2) {
                            return;
                        }
                        Log.e("新版推送间隔不一致", "值为" + iIntValue);
                        HashMap map = new HashMap();
                        map.put("iotId", MotionDetectActivity.this.selectIotId);
                        map.put("eventType", 1);
                        map.put("alarmType", -1);
                        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
                        map.put("switchOn", Boolean.valueOf(MotionDetectActivity.this.pushSwitch2));
                        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.MotionDetectActivity.31.1.1
                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                                LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
                            }

                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                                if (ioTResponse2.getCode() == 200) {
                                    Log.e("新版推送间隔不一致", "修改成功");
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevPush(boolean z, final String str) {
        Log.e("新版推送开关修改", "" + z);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("eventType", 1);
        map.put("alarmType", 1);
        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
        map.put("switchOn", Boolean.valueOf(z));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.MotionDetectActivity.32
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                if (str.equals(MotionDetectActivity.this.device.getIotId())) {
                    if (code == 200) {
                        MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.32.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    } else {
                        MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.32.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevPush2(boolean z, String str) {
        Log.e("新版推送开关修改", "" + z);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("eventType", 1);
        map.put("alarmType", 1);
        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
        map.put("switchOn", Boolean.valueOf(z));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.MotionDetectActivity.33
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }
        });
        HashMap map2 = new HashMap();
        map2.put("iotId", str);
        map2.put("eventType", 1);
        map2.put("alarmType", 1);
        map2.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
        map2.put("switchOn", Boolean.valueOf(z));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.MotionDetectActivity.34
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getYunCallPush() {
        HashMap map = new HashMap();
        map.put("iotId", this.selectIotId);
        map.put("eventType", 1);
        map.put("alarmType", 2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/get").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass35());
    }

    /* JADX INFO: renamed from: activity.MotionDetectActivity$35, reason: invalid class name */
    class AnonymousClass35 implements IoTCallback {
        AnonymousClass35() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            if (ioTResponse.getCode() == 200) {
                JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                final int iIntValue = object.getInteger("eventInterval").intValue();
                final boolean zBooleanValue = object.getBoolean("switchOn").booleanValue();
                Log.e("强推送间隔", "" + iIntValue);
                Log.e("强推送开关", "" + zBooleanValue);
                MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.35.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MotionDetectActivity.this.strongPushSwitch = zBooleanValue;
                        MotionDetectActivity.this.swMobileStrongPush.setChecked(MotionDetectActivity.this.strongPushSwitch);
                        if (iIntValue == AppConfig.PushInterval || !MotionDetectActivity.this.strongPushSwitch) {
                            return;
                        }
                        Log.e("强推送间隔不一致", "值为" + iIntValue);
                        HashMap map = new HashMap();
                        map.put("iotId", MotionDetectActivity.this.selectIotId);
                        map.put("eventType", 1);
                        map.put("alarmType", 2);
                        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
                        map.put("switchOn", Boolean.valueOf(MotionDetectActivity.this.strongPushSwitch));
                        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.MotionDetectActivity.35.1.1
                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                                LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
                            }

                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                                if (ioTResponse2.getCode() == 200) {
                                    Log.e("强推送间隔不一致", "修改成功");
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevCallPush(boolean z, String str) {
        Log.e("强推送开关修改", "" + z);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("eventType", 1);
        map.put("alarmType", 2);
        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
        map.put("switchOn", Boolean.valueOf(z));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.MotionDetectActivity.36
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, MotionDetectActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.36.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                        }
                    });
                } else {
                    MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.36.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        }
                    });
                }
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        SettingsCtrl.getInstance().getProperties(this.selectIotId, new MyCallback() { // from class: activity.MotionDetectActivity.38
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
    }

    public void updateAlarmTime() {
        if (SharePreferenceManager.getInstance().getSupportMotionDetect(this.selectIotId) == 1 && SharePreferenceManager.getInstance().getMotionDetectSensitivity(this.device.getIotId()) < this.yiDongZhenCeLingMingDu.length) {
            this.binding.yiDongZhenCeLingMingDu.setRightText(this.yiDongZhenCeLingMingDu[SharePreferenceManager.getInstance().getMotionDetectSensitivity(this.device.getIotId())]);
        }
        this.binding.itemAlarmMode.setRightText(this.jingjieModel[SharePreferenceManager.getInstance().getAlarmMode(this.device.getIotId())]);
        if (SharePreferenceManager.getInstance().getAlarmMode(this.device.getIotId()) == 2) {
            this.binding.itemAlarmTime.setVisibility(0);
            this.binding.alarmText.setText(getResources().getString(R.string.timing_alert_tip));
        } else if (SharePreferenceManager.getInstance().getAlarmMode(this.selectIotId) == 0) {
            this.binding.itemAlarmTime.setVisibility(8);
            this.binding.alarmText.setText(getResources().getString(R.string.smart_alert_tip));
        } else {
            this.binding.itemAlarmTime.setVisibility(8);
            this.binding.alarmText.setText(getResources().getString(R.string.all_day_alert_tip));
        }
        if (this.binding.itemAlarmMode.getVisibility() == 8) {
            this.binding.itemAlarmTime.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getFileList() {
        IPCManager.getInstance().getDevice(this.selectIotId).getQueryFileList(0, new IPanelCallback() { // from class: activity.MotionDetectActivity.39
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                    AlertInfoBean alertInfoBean = (AlertInfoBean) JSON.parseObject(obj.toString(), AlertInfoBean.class);
                    boolean z2 = false;
                    for (int i = 0; i < alertInfoBean.getData().getFileList().size(); i++) {
                        if (alertInfoBean.getData().getFileList().get(i).getFileName().equals("ivp")) {
                            z2 = true;
                        }
                    }
                    if (z2) {
                        MotionDetectActivity.this.renXingRenLianZhenCeFragment.showAllowingStateLoss(MotionDetectActivity.this.getSupportFragmentManager(), "");
                        return;
                    }
                    MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.39.1
                        @Override // java.lang.Runnable
                        public void run() {
                            MotionDetectActivity.this.showLoadingDialog(MotionDetectActivity.this.getString(R.string.configing));
                        }
                    });
                    MotionDetectActivity.this.isExit = false;
                    MotionDetectActivity.this.uploadLocalFileCustomer("ivp");
                    Message messageObtain = Message.obtain();
                    messageObtain.what = 1;
                    MotionDetectActivity.this.uiHandler.sendMessageDelayed(messageObtain, 60000L);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadLocalFileCustomer(final String str) {
        HashMap map = new HashMap();
        map.put("iotId", this.selectIotId);
        map.put("fileName", str);
        map.put("fileType", 0);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/app/file/upload").setScheme(Scheme.HTTPS).setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.MotionDetectActivity.40
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.40.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MotionDetectActivity.this.showLoadingLongFailDialog(MotionDetectActivity.this.getString(R.string.config_fail_retry));
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                String string = JSON.parseObject(ioTResponse.getData().toString()).getString("uploadUrl");
                Log.d(MotionDetectActivity.this.TAG, "onResponse: -------------------" + string);
                MotionDetectActivity.this.upLoadFile(string, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void upLoadFile(String str, String str2) {
        File file = new File(FileUtil.getFilesPath(this) + "/ivp.oms");
        if (file.exists() || file.mkdirs()) {
            new OkHttpClient.Builder().connectTimeout(100L, TimeUnit.SECONDS).writeTimeout(150L, TimeUnit.SECONDS).build().newCall(new Request.Builder().url(str).put(RequestBody.create(MediaType.get("application/octet-stream"), file)).build()).enqueue(new Callback() { // from class: activity.MotionDetectActivity.41
                @Override // okhttp3.Callback
                public void onFailure(@NonNull Call call, @NonNull IOException iOException) {
                    MotionDetectActivity.this.runOnUiThread(new Runnable() { // from class: activity.MotionDetectActivity.41.1
                        @Override // java.lang.Runnable
                        public void run() {
                            MotionDetectActivity.this.showLoadingLongFailDialog(MotionDetectActivity.this.getString(R.string.config_fail_retry));
                        }
                    });
                }

                @Override // okhttp3.Callback
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (MotionDetectActivity.this.isExit) {
                        return;
                    }
                    MotionDetectActivity.this.HandlerSend();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevParam(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.motion_detect_sensitivity_key))) {
            map.put(Constants.MOTION_DETECT_SENSITIVITY_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.42
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.42.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                            return;
                        }
                        SharePreferenceManager.getInstance().setMotionDetectSensitivity(MotionDetectActivity.this.selectIotId, Integer.parseInt(obj.toString()));
                        if (MotionDetectActivity.this.selectIotId.equals(MotionDetectActivity.this.device.getIotId())) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.42.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    MotionDetectActivity.this.updateAlarmTime();
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                    if (SharePreferenceManager.getInstance().getMotionDetectSensitivity(MotionDetectActivity.this.selectIotId) == 0) {
                                        MotionDetectActivity.this.binding.yiDongZhuiZong.setVisibility(8);
                                    } else {
                                        MotionDetectActivity.this.binding.yiDongZhuiZong.setVisibility(0);
                                    }
                                }
                            });
                        } else if (MotionDetectActivity.this.selectIotId.equals(MotionDetectActivity.this.device1.getIotId())) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.42.3
                                @Override // java.lang.Runnable
                                public void run() {
                                    MotionDetectActivity.this.binding.yiDongZhenCeLingMingDuBullet.setRightText(MotionDetectActivity.this.yiDongZhenCeLingMingDu[SharePreferenceManager.getInstance().getMotionDetectSensitivity(MotionDetectActivity.this.device1.getIotId())]);
                                }
                            });
                        }
                    }
                }
            });
            return;
        }
        if (str.equals(getString(R.string.alarm_mode_key))) {
            map.put(Constants.ALARM_MODE_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.43
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.43.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setAlarmMode(MotionDetectActivity.this.selectIotId, Integer.parseInt(obj.toString()));
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.43.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    MotionDetectActivity.this.updateAlarmTime();
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
            return;
        }
        if (str.equals(getString(R.string.face_detect_key))) {
            map.put(Constants.FACE_DETECT_SENSITIVITY, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.44
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.44.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setFaceDetectMode(MotionDetectActivity.this.selectIotId, Integer.parseInt(obj.toString()));
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.44.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                    if (MotionDetectActivity.this.device1 != null && MotionDetectActivity.this.selectIotId.equals(MotionDetectActivity.this.device1.getIotId())) {
                                        MotionDetectActivity.this.binding.renXingZhenCeBullet.setRightText(MotionDetectActivity.this.yiDongZhenCeLingMingDu[SharePreferenceManager.getInstance().getFaceDetectMode(MotionDetectActivity.this.device1.getIotId())]);
                                    }
                                    if (MotionDetectActivity.this.selectIotId.equals(MotionDetectActivity.this.device.getIotId())) {
                                        MotionDetectActivity.this.binding.renXingZhenCe.setRightText(MotionDetectActivity.this.yiDongZhenCeLingMingDu[SharePreferenceManager.getInstance().getFaceDetectMode(MotionDetectActivity.this.device.getIotId())]);
                                    }
                                    MotionDetectActivity.this.binding.renXingZhuiZong.setVisibility(Integer.parseInt(obj.toString()) == 0 ? 8 : 0);
                                    MotionDetectActivity.this.binding.renXingZhenCe.setLineShow(MotionDetectActivity.this.binding.renXingZhuiZong.getVisibility() == 0);
                                }
                            });
                        }
                    }
                }
            });
            return;
        }
        if (str.equals(getString(R.string.intellgent_key))) {
            map.put(Constants.INTELLIGENT_TRACKING, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.45
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.45.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    MotionDetectActivity.this.renXingZhuiZongSwitch.setChecked(!MotionDetectActivity.this.renXingZhuiZongSwitch.isChecked());
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setIntelligentMode(MotionDetectActivity.this.selectIotId, Integer.parseInt(obj.toString()));
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.45.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                    if (SharePreferenceManager.getInstance().getFaceDetectMode(MotionDetectActivity.this.selectIotId) == -1 || SharePreferenceManager.getInstance().getSupportMotionDetect(MotionDetectActivity.this.selectIotId) != 0) {
                                        MotionDetectActivity.this.binding.renXingZhenCe.setVisibility(8);
                                        MotionDetectActivity.this.binding.renXingZhuiZong.setVisibility(8);
                                        MotionDetectActivity.this.binding.renXingZhenCe.setLineShow(false);
                                        return;
                                    }
                                    MotionDetectActivity.this.binding.renXingZhenCe.setVisibility(0);
                                    MotionDetectActivity.this.binding.renXingZhenCe.setRightText(MotionDetectActivity.this.renXingRenLianZhenCeArr[SharePreferenceManager.getInstance().getFaceDetectMode(MotionDetectActivity.this.selectIotId)]);
                                    if (SharePreferenceManager.getInstance().getFaceDetectMode(MotionDetectActivity.this.selectIotId) == 0) {
                                        MotionDetectActivity.this.binding.renXingZhuiZong.setVisibility(8);
                                        MotionDetectActivity.this.binding.renXingZhenCe.setLineShow(false);
                                    } else {
                                        MotionDetectActivity.this.binding.renXingZhuiZong.setVisibility(0);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        } else if (str.equals(getString(R.string.push_switch_key))) {
            map.put(Constants.PUSH_SWITCH, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.46
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (z) {
                        if (obj2 == null || "".equals(String.valueOf(obj2))) {
                            return;
                        }
                        JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                        if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                            Log.e("推送老通道", "" + "1".equals(obj.toString()));
                            SharePreferenceManager.getInstance().setPushSwitch(MotionDetectActivity.this.selectIotId, "1".equals(obj.toString()));
                            return;
                        }
                        return;
                    }
                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                }
            });
        } else if (str.equals(getString(R.string.strong_reminder_switch))) {
            map.put(Constants.StrongReminderSwitch, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.47
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z) {
                        MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.47.2
                            @Override // java.lang.Runnable
                            public void run() {
                                MotionDetectActivity.this.swMobileStrongPush.setChecked(!MotionDetectActivity.this.swMobileStrongPush.isChecked());
                                Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                        return;
                    }
                    if (obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.47.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    MotionDetectActivity.this.swMobileStrongPush.setChecked(!MotionDetectActivity.this.swMobileStrongPush.isChecked());
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setStrongReminderSwitch(MotionDetectActivity.this.selectIotId, Integer.parseInt(obj.toString()));
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMobileTracking(int i) {
        HashMap map = new HashMap();
        map.put(Constants.INTELLIGENT_TRACKING, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.48
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                MotionDetectActivity.this.dismissProgressDialog();
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkSwitch(final int i, final SwitchCompat switchCompat) {
        if (SharePreferenceManager.getInstance().getTlrClRgn(this.selectIotId).intValue() == 1) {
            this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.49
                @Override // java.lang.Runnable
                public void run() {
                    MotionDetectActivity.this.showProgressDialog();
                }
            });
            HashMap map = new HashMap();
            map.put(Constants.IvpExSwitch, Integer.valueOf(i));
            IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.50
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.50.1
                        @Override // java.lang.Runnable
                        public void run() {
                            MotionDetectActivity.this.dismissProgressDialog();
                        }
                    });
                    if (!z) {
                        MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.50.3
                            @Override // java.lang.Runnable
                            public void run() {
                                switchCompat.setChecked(!switchCompat.isChecked());
                                Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                        return;
                    }
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.50.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    switchCompat.setChecked(!switchCompat.isChecked());
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                            return;
                        }
                        for (int i2 = 0; i2 < MotionDetectActivity.this.switchCompatList.size(); i2++) {
                            if (i2 == 0) {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(MotionDetectActivity.this.selectIotId, 1 & i);
                            } else if (i2 == 1) {
                                SharePreferenceManager.getInstance().setAreaDetectEnable(MotionDetectActivity.this.selectIotId, (i & 4) >> 2);
                            } else if (i2 == 2) {
                                SharePreferenceManager.getInstance().setCrossLineEnable(MotionDetectActivity.this.selectIotId, (2 & i) >> 1);
                            }
                        }
                    }
                }
            });
        } else if (switchCompat == this.kuaXianSwitch || switchCompat == this.quYuSwitch) {
            queryCard(this.selectIotId, i, switchCompat);
        } else {
            this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.51
                @Override // java.lang.Runnable
                public void run() {
                    MotionDetectActivity.this.showProgressDialog();
                }
            });
            HashMap map2 = new HashMap();
            map2.put(Constants.IvpExSwitch, Integer.valueOf(i));
            IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map2, new IPanelCallback() { // from class: activity.MotionDetectActivity.52
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.52.1
                        @Override // java.lang.Runnable
                        public void run() {
                            MotionDetectActivity.this.dismissProgressDialog();
                        }
                    });
                    if (!z) {
                        MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.52.5
                            @Override // java.lang.Runnable
                            public void run() {
                                switchCompat.setChecked(!switchCompat.isChecked());
                                Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                        return;
                    }
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.52.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    switchCompat.setChecked(!switchCompat.isChecked());
                                    Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                            return;
                        }
                        MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.52.3
                            @Override // java.lang.Runnable
                            public void run() {
                                for (int i2 = 0; i2 < MotionDetectActivity.this.switchCompatList.size(); i2++) {
                                    ((SwitchCompat) MotionDetectActivity.this.switchCompatList.get(i2)).setChecked(false);
                                }
                            }
                        });
                        for (int i2 = 0; i2 < MotionDetectActivity.this.switchCompatList.size(); i2++) {
                            if (i2 == 0) {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(MotionDetectActivity.this.selectIotId, 1 & i);
                            } else if (i2 == 1) {
                                SharePreferenceManager.getInstance().setAreaDetectEnable(MotionDetectActivity.this.selectIotId, (i & 4) >> 2);
                            } else if (i2 == 2) {
                                SharePreferenceManager.getInstance().setCrossLineEnable(MotionDetectActivity.this.selectIotId, (2 & i) >> 1);
                            }
                        }
                        if (i != 0) {
                            MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.52.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    switchCompat.setChecked(true);
                                }
                            });
                        }
                    }
                }
            });
        }
        if (!switchCompat.isChecked()) {
            IPCManager.getInstance().getDevice(this.selectIotId).changePresetLocation(103, new IPanelCallback() { // from class: activity.MotionDetectActivity.53
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        Log.e("预置位", "103");
                    }
                }
            });
        } else {
            IPCManager.getInstance().getDevice(this.selectIotId).addPresetLocation(99, new IPanelCallback() { // from class: activity.MotionDetectActivity.54
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        Log.e("预置位", "99");
                    }
                }
            });
            IPCManager.getInstance().getDevice(this.selectIotId).changePresetLocation(100, new IPanelCallback() { // from class: activity.MotionDetectActivity.55
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        Log.e("预置位", MessageService.MSG_DB_COMPLETE);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIvpZoomEnable(final int i) {
        HashMap map = new HashMap();
        map.put(Constants.IvpZoomEnable, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.selectIotId).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.57
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(final boolean z, final Object obj) {
                MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.57.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Object obj2;
                        if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                            return;
                        }
                        try {
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() == 200) {
                                    SharePreferenceManager.getInstance().setIvpZoomEnable(MotionDetectActivity.this.selectIotId, i);
                                    MotionDetectActivity.this.deviceAutoLocateSwitch.setChecked(SharePreferenceManager.getInstance().getIvpZoomEnable(MotionDetectActivity.this.selectIotId) == 1);
                                } else {
                                    Toast.makeText(MotionDetectActivity.this, R.string.mofify_failed, 0).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void queryCard(final String str, final int i, final SwitchCompat switchCompat) {
        if (this.isFirstQueryOutCard) {
            if (SharePreferenceManager.getInstance().getSupport4G(str) == 1) {
                this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.58
                    @Override // java.lang.Runnable
                    public void run() {
                        MotionDetectActivity.this.showProgressDialog();
                    }
                });
                new OkHttpClient.Builder().connectTimeout(5000L, TimeUnit.SECONDS).readTimeout(5000L, TimeUnit.SECONDS).build().newCall(new Request.Builder().url("http://www.secueye.cn:8000/api/smsApi?iccid=" + SharePreferenceManager.getInstance().getIccId(str) + "&method=smsStatusSecueye").get().build()).enqueue(new Callback() { // from class: activity.MotionDetectActivity.59
                    static final /* synthetic */ boolean $assertionsDisabled = false;

                    @Override // okhttp3.Callback
                    public void onFailure(Call call, IOException iOException) {
                        MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.59.1
                            @Override // java.lang.Runnable
                            public void run() {
                                MotionDetectActivity.this.dismissProgressDialog();
                            }
                        });
                        MotionDetectActivity.this.isFirstQueryOutCard = false;
                        MotionDetectActivity.this.isOutCard = false;
                        MotionDetectActivity.this.update(str, i, switchCompat);
                        iOException.printStackTrace();
                    }

                    @Override // okhttp3.Callback
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.59.2
                            @Override // java.lang.Runnable
                            public void run() {
                                MotionDetectActivity.this.dismissProgressDialog();
                            }
                        });
                        MotionDetectActivity.this.isFirstQueryOutCard = false;
                        try {
                            String strString = response.body().string();
                            if (strString != null && !strString.equals("")) {
                                JSONObject object = JSONObject.parseObject(strString);
                                if (object.containsKey("code")) {
                                    int iIntValue = object.getInteger("code").intValue();
                                    MotionDetectActivity.this.isOutCard = iIntValue == 400;
                                    if (MotionDetectActivity.this.isOutCard) {
                                        MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.59.3
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                switchCompat.setChecked(!switchCompat.isChecked());
                                                View viewInflate = View.inflate(MotionDetectActivity.this.getApplicationContext(), R.layout.out_card_tips, null);
                                                Button button = (Button) viewInflate.findViewById(R.id.cancel);
                                                final AlertDialog alertDialogCreate = new AlertDialog.Builder(MotionDetectActivity.this.getActivity()).setView(viewInflate).create();
                                                alertDialogCreate.setCanceledOnTouchOutside(false);
                                                alertDialogCreate.show();
                                                button.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.59.3.1
                                                    @Override // android.view.View.OnClickListener
                                                    public void onClick(View view2) {
                                                        alertDialogCreate.dismiss();
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        MotionDetectActivity.this.update(str, i, switchCompat);
                                    }
                                } else {
                                    MotionDetectActivity.this.update(str, i, switchCompat);
                                }
                            } else {
                                MotionDetectActivity.this.update(str, i, switchCompat);
                            }
                        } catch (Exception e) {
                            MotionDetectActivity.this.update(str, i, switchCompat);
                            e.printStackTrace();
                        }
                    }
                });
                return;
            }
            update(str, i, switchCompat);
            return;
        }
        if (this.isOutCard) {
            this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.60
                @Override // java.lang.Runnable
                public void run() {
                    switchCompat.setChecked(!r0.isChecked());
                    View viewInflate = View.inflate(MotionDetectActivity.this.getApplicationContext(), R.layout.out_card_tips, null);
                    Button button = (Button) viewInflate.findViewById(R.id.cancel);
                    final AlertDialog alertDialogCreate = new AlertDialog.Builder(MotionDetectActivity.this.getActivity()).setView(viewInflate).create();
                    alertDialogCreate.setCanceledOnTouchOutside(false);
                    alertDialogCreate.show();
                    button.setOnClickListener(new View.OnClickListener() { // from class: activity.MotionDetectActivity.60.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view2) {
                            alertDialogCreate.dismiss();
                        }
                    });
                }
            });
        } else {
            update(str, i, switchCompat);
        }
    }

    public void update(final String str, final int i, final SwitchCompat switchCompat) {
        this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.61
            @Override // java.lang.Runnable
            public void run() {
                MotionDetectActivity.this.showProgressDialog();
            }
        });
        HashMap map = new HashMap();
        map.put(Constants.IvpExSwitch, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: activity.MotionDetectActivity.62
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.62.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MotionDetectActivity.this.dismissProgressDialog();
                    }
                });
                if (!z) {
                    MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.62.5
                        @Override // java.lang.Runnable
                        public void run() {
                            switchCompat.setChecked(!switchCompat.isChecked());
                            Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        }
                    });
                    return;
                }
                if (obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.62.2
                            @Override // java.lang.Runnable
                            public void run() {
                                switchCompat.setChecked(!switchCompat.isChecked());
                                Toast.makeText(MotionDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                        return;
                    }
                    MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.62.3
                        @Override // java.lang.Runnable
                        public void run() {
                            ((SwitchCompat) MotionDetectActivity.this.switchCompatList.get(0)).setChecked(false);
                            ((SwitchCompat) MotionDetectActivity.this.switchCompatList.get(1)).setChecked(false);
                            ((SwitchCompat) MotionDetectActivity.this.switchCompatList.get(2)).setChecked(false);
                        }
                    });
                    for (int i2 = 0; i2 < MotionDetectActivity.this.switchCompatList.size(); i2++) {
                        if (i2 == 0) {
                            SharePreferenceManager.getInstance().setHumanoidTrackingEnable(str, 1 & i);
                        } else if (i2 == 1) {
                            SharePreferenceManager.getInstance().setAreaDetectEnable(str, (i & 4) >> 2);
                        } else if (i2 == 2) {
                            SharePreferenceManager.getInstance().setCrossLineEnable(str, (2 & i) >> 1);
                        }
                    }
                    if (i != 0) {
                        MotionDetectActivity.this.uiHandler.post(new Runnable() { // from class: activity.MotionDetectActivity.62.4
                            @Override // java.lang.Runnable
                            public void run() {
                                switchCompat.setChecked(true);
                            }
                        });
                    }
                }
            }
        });
    }
}
