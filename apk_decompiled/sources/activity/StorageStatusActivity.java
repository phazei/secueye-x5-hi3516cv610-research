package activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import anet.channel.util.StringUtils;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.seculink.app.R;
import config.Constants;
import java.text.DecimalFormat;
import java.util.HashMap;
import sdk.ChannelManager;
import sdk.IPCManager;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.ItemView;
import view.RingView;
import view.SelectorDialogFragment;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class StorageStatusActivity extends CommonActivity implements View.OnClickListener {
    private CountDownTimer countDownTimer;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;

    /* JADX INFO: renamed from: dialog, reason: collision with root package name */
    AlertDialog f1586dialog;
    private TitleView fl_titlebar;
    private Button formatButton;
    private ImageView imageView;
    private String iotId;
    private ItemView itemRecordMode;
    private ItemView itemRecordQuality;
    private ItemView itemSDCardStatus;
    private ItemView item_record_time;
    private ItemView item_record_time_switch;
    private LinearLayout layout_bullet;
    LinearLayout layout_main;
    private LinearLayout layout_ptz;
    private LinearLayout layout_switch;
    private DeviceInfoBean nvrDevice;
    private int percent;
    private TextView progressTextView;
    private int recordMode;
    private String[] recordModeArr;
    private String[] recordModeArrDesc;
    private String[] recordModeArrValue;
    private SelectorDialogFragment recordModeDialogFragment;
    private int recordQuality;
    private String[] recordQualityArr;
    private String[] recordQualityArrDesc;
    private String[] recordQualityArrValue;
    private SelectorDialogFragment recordQualityFragment;
    private TextView recordTextView;
    private float remainStorage;
    private RingView ringView;
    private SelectorDialogFragment selectorDialogFragment;
    private int storageStatusValues;
    private SwitchCompat swRecordTime;
    private float totalStorage;
    private TextView tvPercent;
    private TextView tvPercentIcon;
    private TextView tvStorageRecord;
    private TextView tvStorageRemain;
    private TextView tvStorageTotal;
    private TextView tvStorageUse;
    private float usedStorage;
    private View view3;
    private boolean isCardFormat = false;
    private boolean isFloodlightTime = false;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: activity.StorageStatusActivity.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(@NonNull Message message) {
            if (message.what != 0) {
                return true;
            }
            IPCManager.getInstance().getDevice(StorageStatusActivity.this.iotId).queryTFCard(new IPanelCallback() { // from class: activity.StorageStatusActivity.1.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    if (!z) {
                        StorageStatusActivity.this.isCardFormat = true;
                        StorageStatusActivity.this.isFailFormat = 0;
                        return;
                    }
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        StorageStatusActivity.this.isCardFormat = true;
                        StorageStatusActivity.this.isFailFormat = 0;
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code") && object.containsKey("data")) {
                        try {
                            JSONObject jSONObject = object.getJSONObject("data");
                            if (jSONObject.containsKey("TFCardFormatStatus")) {
                                int iIntValue = jSONObject.getInteger("TFCardFormatStatus").intValue();
                                if (iIntValue == 2) {
                                    StorageStatusActivity.this.isCardFormat = true;
                                    StorageStatusActivity.this.isFailFormat = 2;
                                } else if (iIntValue == 0) {
                                    StorageStatusActivity.this.isCardFormat = true;
                                    StorageStatusActivity.this.isFailFormat = 0;
                                } else if (iIntValue == 1) {
                                    StorageStatusActivity.this.isCardFormat = false;
                                    StorageStatusActivity.this.isFailFormat = 1;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            if (!StorageStatusActivity.this.isCardFormat) {
                StorageStatusActivity.this.HandlerSend();
                return true;
            }
            StorageStatusActivity.this.f1586dialog.dismiss();
            StorageStatusActivity.this.dismissProgressDialog();
            if (StorageStatusActivity.this.isFailFormat == 2) {
                Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.format_succeed, 0).show();
                StorageStatusActivity storageStatusActivity = StorageStatusActivity.this;
                storageStatusActivity.remainStorage = storageStatusActivity.totalStorage;
            }
            StorageStatusActivity.this.initData();
            return true;
        }
    });
    private boolean isNewDevice = false;
    private int isFailFormat = 1;
    private int secondProgress = 75;
    private int fastProgress = 0;
    private long time = 0;
    private int count = 1;
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: activity.StorageStatusActivity.10
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(StorageStatusActivity.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            if (str.equals("/thing/properties")) {
                SettingsCtrl.getInstance().getProperties(StorageStatusActivity.this.iotId, new MyCallback() { // from class: activity.StorageStatusActivity.10.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        }
    };
    private SharePreferenceManager.OnCallSetListener mSPModifyListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.StorageStatusActivity.11
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.11.1
                @Override // java.lang.Runnable
                public void run() {
                    String str3 = str2;
                    if (str3 == null || str3.trim().equals("")) {
                        return;
                    }
                    if (str2.equals(StorageStatusActivity.this.getString(R.string.storage_status_key))) {
                        StorageStatusActivity.this.storageStatusValues = SharePreferenceManager.getInstance().getStorageStatus(str);
                        StorageStatusActivity.this.totalStorage = SharePreferenceManager.getInstance().getStorageTotalCapacity(str) / 1024.0f;
                        StorageStatusActivity.this.remainStorage = SharePreferenceManager.getInstance().getStorageRemainingCapacity(str) / 1024.0f;
                        StorageStatusActivity.this.usedStorage = StorageStatusActivity.this.totalStorage - StorageStatusActivity.this.remainStorage;
                        StorageStatusActivity.this.ringView.setValue(StorageStatusActivity.this.totalStorage, StorageStatusActivity.this.usedStorage);
                        StorageStatusActivity.this.percent = (int) Math.ceil((StorageStatusActivity.this.usedStorage * 100.0f) / StorageStatusActivity.this.totalStorage);
                        StorageStatusActivity.this.tvStorageTotal.setText(StorageStatusActivity.this.getString(R.string.storage_total, new Object[]{new DecimalFormat("0.00").format(StorageStatusActivity.this.totalStorage)}));
                        if (StorageStatusActivity.this.storageStatusValues == 0) {
                            StorageStatusActivity.this.formatButton.setTextColor(StorageStatusActivity.this.getResources().getColor(R.color.color_gray));
                            StorageStatusActivity.this.formatButton.setBackgroundResource(R.drawable.bg_button_format_gray);
                            StorageStatusActivity.this.formatButton.setEnabled(false);
                            StorageStatusActivity.this.tvPercent.setText("--");
                            StorageStatusActivity.this.tvPercentIcon.setVisibility(8);
                            StorageStatusActivity.this.itemSDCardStatus.setRightText(StorageStatusActivity.this.getString(R.string.no_sd_card));
                            StorageStatusActivity.this.itemSDCardStatus.setRightTextMargin();
                            StorageStatusActivity.this.tvStorageUse.setVisibility(8);
                            StorageStatusActivity.this.imageView.setVisibility(8);
                            StorageStatusActivity.this.tvStorageTotal.setVisibility(8);
                            StorageStatusActivity.this.view3.setVisibility(8);
                            StorageStatusActivity.this.tvStorageRemain.setVisibility(8);
                            StorageStatusActivity.this.tvStorageRecord.setText(StorageStatusActivity.this.getString(R.string.storage_record_normal));
                            StorageStatusActivity.this.progressTextView.setText("--");
                            return;
                        }
                        if (StorageStatusActivity.this.storageStatusValues == 2) {
                            StorageStatusActivity.this.tvPercent.setText("--");
                            StorageStatusActivity.this.tvPercentIcon.setVisibility(8);
                            StorageStatusActivity.this.tvStorageUse.setVisibility(8);
                            StorageStatusActivity.this.imageView.setVisibility(8);
                            StorageStatusActivity.this.tvStorageTotal.setVisibility(8);
                            StorageStatusActivity.this.view3.setVisibility(8);
                            StorageStatusActivity.this.tvStorageRemain.setVisibility(8);
                            StorageStatusActivity.this.tvStorageRecord.setText(StorageStatusActivity.this.getString(R.string.storage_record_normal));
                            StorageStatusActivity.this.progressTextView.setText("--");
                            if (StorageStatusActivity.this.isNewDevice) {
                                StorageStatusActivity.this.itemSDCardStatus.setRightText(StorageStatusActivity.this.getString(R.string.uninitialized));
                                StorageStatusActivity.this.itemSDCardStatus.setRightTextColor(R.color.color_ff0000);
                            } else {
                                StorageStatusActivity.this.itemSDCardStatus.setRightText(StorageStatusActivity.this.getString(R.string.card_inserted));
                                StorageStatusActivity.this.itemSDCardStatus.setRightTextColor(R.color.color_tf_green);
                            }
                            StorageStatusActivity.this.itemSDCardStatus.setRightTextMargin();
                            return;
                        }
                        StorageStatusActivity.this.tvPercent.setText(StorageStatusActivity.this.percent + "");
                        StorageStatusActivity.this.tvPercentIcon.setVisibility(0);
                        if (StorageStatusActivity.this.isNewDevice) {
                            StorageStatusActivity.this.itemSDCardStatus.setRightText(StorageStatusActivity.this.getString(R.string.normal));
                            StorageStatusActivity.this.itemSDCardStatus.setRightTextColor(R.color.color_tf_green);
                        } else {
                            StorageStatusActivity.this.itemSDCardStatus.setRightText(StorageStatusActivity.this.getString(R.string.card_inserted));
                            StorageStatusActivity.this.itemSDCardStatus.setRightTextColor(R.color.color_tf_green);
                        }
                        StorageStatusActivity.this.itemSDCardStatus.setRightTextMargin();
                        StorageStatusActivity.this.tvStorageRecord.setText(StorageStatusActivity.this.getString(R.string.storage_record_normal));
                        StorageStatusActivity.this.progressTextView.setText(StorageStatusActivity.this.percent + "%");
                        return;
                    }
                    if (str2.equals(StorageStatusActivity.this.getString(R.string.record_quality))) {
                        StorageStatusActivity.this.recordQuality = SharePreferenceManager.getInstance().getRecordQuality(str);
                    }
                }
            });
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_storage_status;
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.totalStorage = intent.getFloatExtra("totalStorage", 0.0f);
        this.remainStorage = intent.getFloatExtra("remainStorage", 0.0f);
        this.storageStatusValues = intent.getIntExtra("storageStatusValues", 1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            this.iotId = this.device.getIotId();
            Log.d("设置", "device: " + this.device.getIotId());
            Log.d("设置dn", "device: " + this.device.getDeviceName());
            Log.d("设置pk", "device: " + this.device.getProductKey());
            if (this.device1 != null) {
                Log.d("设置", "device1: " + this.device1.getIotId());
                Log.d("设置dn", "device1: " + this.device1.getDeviceName());
                Log.d("设置pk", "device1: " + this.device1.getProductKey());
            }
            if (this.device2 != null) {
                Log.d("设置", "device2: " + this.device2.getIotId());
                Log.d("设置dn", "device2: " + this.device2.getDeviceName());
                Log.d("设置pk", "device2: " + this.device2.getProductKey());
            }
            if (this.nvrDevice != null) {
                Log.d("设置", "nvrDevice: " + this.nvrDevice.getIotId());
                Log.d("设置dn", "nvrDevice: " + this.nvrDevice.getDeviceName());
                Log.d("设置pk", "nvrDevice: " + this.nvrDevice.getProductKey());
            }
        }
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.StorageStatusActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                StorageStatusActivity.this.finish();
            }
        });
        this.formatButton = (Button) findViewById(R.id.bt_format);
        this.formatButton.setOnClickListener(this);
        this.ringView = (RingView) findViewById(R.id.ringView);
        this.ringView.setOnClickListener(this);
        this.tvPercent = (TextView) findViewById(R.id.tv_percent);
        this.tvStorageUse = (TextView) findViewById(R.id.tv_storage_use);
        this.tvStorageTotal = (TextView) findViewById(R.id.tv_storage_total);
        this.tvStorageRecord = (TextView) findViewById(R.id.tv_storage_record);
        this.progressTextView = (TextView) findViewById(R.id.progress_text);
        this.tvStorageRemain = (TextView) findViewById(R.id.tv_storage_remain);
        this.tvPercentIcon = (TextView) findViewById(R.id.tv_percent_icon);
        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.view3 = findViewById(R.id.view3);
        this.layout_bullet = (LinearLayout) findViewById(R.id.layout_bullet);
        this.layout_ptz = (LinearLayout) findViewById(R.id.layout_ptz);
        this.layout_switch = (LinearLayout) findViewById(R.id.layout_switch);
        this.itemRecordMode = (ItemView) findViewById(R.id.item_record_modes);
        this.itemRecordMode.setOnClickListener(this);
        this.item_record_time = (ItemView) findViewById(R.id.item_record_time);
        this.item_record_time.setOnClickListener(this);
        this.itemRecordQuality = (ItemView) findViewById(R.id.item_record_quality);
        this.itemRecordQuality.setOnClickListener(this);
        this.recordTextView = (TextView) findViewById(R.id.record_text);
        this.itemSDCardStatus = (ItemView) findViewById(R.id.sd_card_status);
        this.itemSDCardStatus.setOnClickListener(this);
        this.item_record_time_switch = (ItemView) findViewById(R.id.item_record_time_switch);
        this.item_record_time_switch.addRightView(new SwitchCompat(getActivity()));
        this.swRecordTime = (SwitchCompat) this.item_record_time_switch.getRightView();
        this.swRecordTime.setTextOff("");
        this.swRecordTime.setTextOn("");
        this.swRecordTime.setText("");
        this.swRecordTime.setThumbDrawable(null);
        this.swRecordTime.setBackgroundResource(R.drawable.sel_switch);
        this.swRecordTime.setOnClickListener(new AnonymousClass3());
        this.recordModeArr = new String[]{getResources().getString(R.string.alarm_record), getResources().getString(R.string.day_record)};
        this.recordModeArrValue = new String[]{"1", "2"};
        this.recordModeArrDesc = new String[]{getResources().getString(R.string.record_desc1), getResources().getString(R.string.record_desc)};
        this.recordModeDialogFragment = new SelectorDialogFragment(getString(R.string.record_mode), this.recordModeArr);
        this.recordModeDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.StorageStatusActivity.4
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                StorageStatusActivity storageStatusActivity = StorageStatusActivity.this;
                storageStatusActivity.updateDevParam(storageStatusActivity.getString(R.string.storage_record_mode_key), StorageStatusActivity.this.recordModeArrValue[i]);
            }
        });
        this.recordQualityArr = new String[]{getResources().getString(R.string.quality_m), getResources().getString(R.string.quality_h)};
        this.recordQualityArrValue = new String[]{"0", "1"};
        this.recordQualityFragment = new SelectorDialogFragment(getString(R.string.record_video_quality), this.recordQualityArr);
        this.recordQualityFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.StorageStatusActivity.5
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                StorageStatusActivity storageStatusActivity = StorageStatusActivity.this;
                storageStatusActivity.updateDevParam(storageStatusActivity.getString(R.string.record_quality), StorageStatusActivity.this.recordQualityArrValue[i]);
            }
        });
        this.selectorDialogFragment = new SelectorDialogFragment(getString(R.string.clean_micro_sd_card), getString(R.string.format));
        this.selectorDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.StorageStatusActivity.6
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(final int i) {
                if (StorageStatusActivity.this.isNewDevice) {
                    StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            StorageStatusActivity.this.FourGChangeDialog(i);
                        }
                    });
                } else {
                    StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.6.2
                        @Override // java.lang.Runnable
                        public void run() {
                            StorageStatusActivity.this.showTFDialog();
                        }
                    });
                }
            }
        });
        if (this.storageStatusValues == 4) {
            this.tvStorageUse.setVisibility(8);
            this.imageView.setVisibility(8);
            this.tvStorageTotal.setVisibility(8);
            this.view3.setVisibility(8);
            this.tvStorageRemain.setVisibility(8);
        }
        this.ringView.setBgColor(R.color.color_gray_shallow);
        this.ringView.setFillColor(R.color.colorAccent);
        if (this.device1 != null) {
            this.layout_switch.setVisibility(0);
            this.layout_ptz.setSelected(true);
        }
        this.layout_ptz.setOnClickListener(new OnMultiClickListener() { // from class: activity.StorageStatusActivity.7
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                StorageStatusActivity storageStatusActivity = StorageStatusActivity.this;
                storageStatusActivity.iotId = storageStatusActivity.device.getIotId();
                StorageStatusActivity.this.layout_bullet.setSelected(false);
                StorageStatusActivity.this.layout_ptz.setSelected(true);
                StorageStatusActivity.this.getRecdData();
            }
        });
        this.layout_bullet.setOnClickListener(new OnMultiClickListener() { // from class: activity.StorageStatusActivity.8
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                StorageStatusActivity storageStatusActivity = StorageStatusActivity.this;
                storageStatusActivity.iotId = storageStatusActivity.device1.getIotId();
                StorageStatusActivity.this.layout_ptz.setSelected(false);
                StorageStatusActivity.this.layout_bullet.setSelected(true);
                StorageStatusActivity.this.getRecdData();
            }
        });
    }

    /* JADX INFO: renamed from: activity.StorageStatusActivity$3, reason: invalid class name */
    class AnonymousClass3 extends OnMultiClickListener {
        AnonymousClass3() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            HashMap map = new HashMap();
            map.put(Constants.TimeRecordEnable, Integer.valueOf(!StorageStatusActivity.this.isFloodlightTime ? 1 : 0));
            IPCManager.getInstance().getDevice(StorageStatusActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.StorageStatusActivity.3.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, @Nullable final Object obj) {
                    new Handler().post(new Runnable() { // from class: activity.StorageStatusActivity.3.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                                StorageStatusActivity.this.isFloodlightTime = !StorageStatusActivity.this.isFloodlightTime;
                                StorageStatusActivity.this.swRecordTime.setChecked(StorageStatusActivity.this.isFloodlightTime);
                                SharePreferenceManager.getInstance().setTimeRecordEnable(StorageStatusActivity.this.device.getIotId(), StorageStatusActivity.this.isFloodlightTime ? 1 : 0);
                            }
                        }
                    });
                }
            });
        }
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.mSPModifyListener);
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.StorageStatusActivity.9
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
        this.isNewDevice = SharePreferenceManager.getInstance().getTFCardInfo(this.iotId) != 0;
        float f = this.totalStorage;
        this.usedStorage = f - this.remainStorage;
        this.ringView.setValue(f, this.usedStorage);
        this.percent = (int) Math.ceil((this.usedStorage * 100.0f) / this.totalStorage);
        if (this.percent < 0) {
            this.percent = 0;
        }
        int i = this.storageStatusValues;
        if (i == 2 || i == 0) {
            this.tvPercent.setText("--");
            this.tvPercentIcon.setVisibility(8);
        } else {
            this.tvPercent.setText(this.percent + "");
            this.tvPercentIcon.setVisibility(0);
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Object obj = decimalFormat.format(this.totalStorage);
        String str = decimalFormat.format(this.remainStorage);
        String str2 = decimalFormat.format(this.totalStorage - this.remainStorage);
        Log.e(this.TAG, "totalStorage: " + this.totalStorage + "  remainStorage: " + str + "---" + this.remainStorage);
        TextView textView = this.tvStorageUse;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(str2);
        textView.setText(getString(R.string.storage_use, new Object[]{sb.toString(), obj}));
        this.tvStorageTotal.setText(getString(R.string.storage_total, new Object[]{obj}));
        int i2 = this.storageStatusValues;
        if (i2 == 2 || i2 == 0) {
            this.tvStorageUse.setVisibility(8);
            this.imageView.setVisibility(8);
            this.tvStorageTotal.setVisibility(8);
            this.view3.setVisibility(8);
            this.tvStorageRemain.setVisibility(8);
            this.tvStorageRecord.setText(getString(R.string.storage_record_normal));
            this.progressTextView.setText("--");
        } else {
            this.tvStorageRecord.setText(getString(R.string.storage_record_normal));
            this.progressTextView.setText(this.percent + "%");
        }
        this.tvStorageRemain.setText(getString(R.string.storage_remain, new Object[]{str}));
        getRecdData();
        if (this.isNewDevice) {
            int i3 = this.storageStatusValues;
            if (i3 == 2) {
                this.itemSDCardStatus.setRightText(getString(R.string.uninitialized));
                this.itemSDCardStatus.setRightTextColor(R.color.color_ff0000);
                this.itemSDCardStatus.setRightTextMargin();
                return;
            } else {
                if (i3 == 0) {
                    this.formatButton.setTextColor(getResources().getColor(R.color.color_gray));
                    this.formatButton.setBackgroundResource(R.drawable.bg_button_format_gray);
                    this.formatButton.setEnabled(false);
                    this.itemSDCardStatus.setRightText(getString(R.string.no_sd_card));
                    this.itemSDCardStatus.setRightTextMargin();
                    return;
                }
                this.itemSDCardStatus.setRightText(getString(R.string.normal));
                this.itemSDCardStatus.setRightTextColor(R.color.color_tf_green);
                this.itemSDCardStatus.setRightTextMargin();
                return;
            }
        }
        this.itemSDCardStatus.setRightText(getString(R.string.card_inserted));
        this.itemSDCardStatus.setRightTextColor(R.color.color_tf_green);
        this.itemSDCardStatus.setRightTextMargin();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getRecdData() {
        this.recordMode = SharePreferenceManager.getInstance().getStorageRecordMode(this.iotId);
        this.recordQuality = SharePreferenceManager.getInstance().getRecordQuality(this.iotId);
        if (this.recordMode == 0) {
            this.recordMode = 1;
        }
        this.itemRecordMode.setRightText(this.recordModeArr[this.recordMode - 1]);
        this.recordTextView.setText(this.recordModeArrDesc[this.recordMode - 1]);
        if (this.device1 != null) {
            this.itemRecordQuality.setVisibility(0);
        } else {
            this.itemRecordQuality.setVisibility(SharePreferenceManager.getInstance().getRecordQualityAbility(this.iotId) == 1 ? 0 : 8);
        }
        this.itemRecordQuality.setRightText(this.recordQualityArr[this.recordQuality]);
        this.isFloodlightTime = SharePreferenceManager.getInstance().getTimeRecordEnable(this.iotId) == 1;
        this.swRecordTime.setChecked(this.isFloodlightTime);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.bt_format) {
            this.selectorDialogFragment.showAllowingStateLoss(getSupportFragmentManager(), "", 0);
            return;
        }
        if (view2.getId() == R.id.item_record_modes) {
            this.recordModeDialogFragment.showAllowingStateLoss(getSupportFragmentManager(), "");
            return;
        }
        if (view2.getId() == R.id.item_record_time) {
            Intent intent = new Intent(getActivity(), (Class<?>) RecordTimeSettingsActivity.class);
            intent.putExtra("iotId", this.device.getIotId());
            startActivity(intent);
            return;
        }
        if (view2.getId() == R.id.item_record_quality) {
            this.recordQualityFragment.showAllowingStateLoss(getSupportFragmentManager(), "");
            return;
        }
        if (view2.getId() == R.id.ringView) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (jCurrentTimeMillis - this.time < AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS) {
                this.count++;
            } else {
                this.count = 1;
            }
            this.time = jCurrentTimeMillis;
            if (this.count >= 3) {
                if (this.tvStorageUse.getVisibility() == 8) {
                    this.tvStorageUse.setVisibility(0);
                }
                if (this.imageView.getVisibility() == 8) {
                    this.imageView.setVisibility(0);
                }
                if (this.tvStorageTotal.getVisibility() == 8) {
                    this.tvStorageTotal.setVisibility(0);
                }
                if (this.view3.getVisibility() == 8) {
                    this.view3.setVisibility(0);
                }
                if (this.tvStorageRemain.getVisibility() == 8) {
                    this.tvStorageRemain.setVisibility(0);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void HandlerSend() {
        Message messageObtain = Message.obtain();
        messageObtain.what = 0;
        this.mHandler.sendMessageDelayed(messageObtain, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void FourGChangeDialog(int i) {
        ProgressBar progressBar;
        View viewInflate = View.inflate(this, R.layout.switch_tf_layout, null);
        final ProgressBar progressBar2 = (ProgressBar) viewInflate.findViewById(R.id.load_progressbar);
        final TextView textView = (TextView) viewInflate.findViewById(R.id.progress_finish_text);
        final TextView textView2 = (TextView) viewInflate.findViewById(R.id.load_progressbar_text);
        final TextView textView3 = (TextView) viewInflate.findViewById(R.id.image_result_text);
        final TextView textView4 = (TextView) viewInflate.findViewById(R.id.image_result_text1);
        final View viewFindViewById = viewInflate.findViewById(R.id.view1);
        this.f1586dialog = new AlertDialog.Builder(this).setView(viewInflate).create();
        this.f1586dialog.setCanceledOnTouchOutside(false);
        this.f1586dialog.show();
        this.f1586dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        int i2 = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams attributes = this.f1586dialog.getWindow().getAttributes();
        attributes.width = (int) (((double) i2) * 0.85d);
        this.f1586dialog.getWindow().setAttributes(attributes);
        this.f1586dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.StorageStatusActivity.12
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                StorageStatusActivity.this.cancelCount();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() { // from class: activity.StorageStatusActivity.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                StorageStatusActivity.this.f1586dialog.dismiss();
            }
        });
        textView.setEnabled(false);
        this.secondProgress = 75;
        this.fastProgress = 0;
        if (this.countDownTimer == null) {
            progressBar = progressBar2;
            this.countDownTimer = new CountDownTimer(120000L, 1000L) { // from class: activity.StorageStatusActivity.14
                @Override // android.os.CountDownTimer
                public void onTick(final long j) {
                    int i3 = (int) ((120000 - j) / 1000);
                    if (StorageStatusActivity.this.fastProgress > 0) {
                        StorageStatusActivity.this.fastProgress += 2;
                        if (StorageStatusActivity.this.fastProgress >= 100) {
                            StorageStatusActivity.this.fastProgress = 100;
                        }
                        StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.1
                            @Override // java.lang.Runnable
                            @SuppressLint({"SetTextI18n"})
                            public void run() {
                                if (StorageStatusActivity.this.fastProgress <= 100) {
                                    progressBar2.setProgress(StorageStatusActivity.this.fastProgress);
                                    textView.setText(StorageStatusActivity.this.fastProgress + "%");
                                }
                            }
                        });
                    }
                    if (StorageStatusActivity.this.fastProgress <= 0 || StorageStatusActivity.this.fastProgress >= 100) {
                        if (StorageStatusActivity.this.fastProgress >= 100) {
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    StorageStatusActivity.this.initData();
                                    StorageStatusActivity.this.f1586dialog.dismiss();
                                    Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.format_succeed, 0).show();
                                }
                            });
                            cancel();
                            StorageStatusActivity.this.countDownTimer = null;
                            StorageStatusActivity storageStatusActivity = StorageStatusActivity.this;
                            storageStatusActivity.remainStorage = storageStatusActivity.totalStorage;
                            StorageStatusActivity.this.initData();
                        }
                        if (StorageStatusActivity.this.secondProgress > 75) {
                            StorageStatusActivity.this.secondProgress++;
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.3
                                @Override // java.lang.Runnable
                                @SuppressLint({"SetTextI18n"})
                                public void run() {
                                    if (StorageStatusActivity.this.secondProgress <= 100) {
                                        progressBar2.setProgress(StorageStatusActivity.this.secondProgress);
                                        textView.setText(StorageStatusActivity.this.secondProgress + "%");
                                    }
                                }
                            });
                        }
                        if (StorageStatusActivity.this.secondProgress >= 100) {
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    StorageStatusActivity.this.initData();
                                    StorageStatusActivity.this.f1586dialog.dismiss();
                                    Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.format_succeed, 0).show();
                                }
                            });
                            cancel();
                            StorageStatusActivity.this.countDownTimer = null;
                            StorageStatusActivity storageStatusActivity2 = StorageStatusActivity.this;
                            storageStatusActivity2.remainStorage = storageStatusActivity2.totalStorage;
                            StorageStatusActivity.this.initData();
                        }
                        if (i3 < 75 || i3 >= 105) {
                            if (i3 >= 105) {
                                StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.8
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        textView.setEnabled(true);
                                        textView2.setVisibility(8);
                                        progressBar2.setVisibility(8);
                                        textView3.setVisibility(0);
                                        textView3.setText(R.string.format_failed);
                                        textView4.setVisibility(0);
                                        textView4.setText(R.string.check_tf_status);
                                        viewFindViewById.setVisibility(0);
                                        textView.setText(R.string.i_know);
                                        textView.setEnabled(true);
                                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
                                        layoutParams.topToBottom = textView4.getId();
                                        textView.setLayoutParams(layoutParams);
                                    }
                                });
                                cancel();
                                StorageStatusActivity.this.countDownTimer = null;
                            } else {
                                if (StorageStatusActivity.this.isFailFormat == 2) {
                                    StorageStatusActivity.this.fastProgress = i3;
                                    StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.9
                                        @Override // java.lang.Runnable
                                        @SuppressLint({"SetTextI18n"})
                                        public void run() {
                                            if (StorageStatusActivity.this.fastProgress <= 100) {
                                                progressBar2.setProgress(StorageStatusActivity.this.fastProgress);
                                                textView.setText(StorageStatusActivity.this.fastProgress + "%");
                                            }
                                        }
                                    });
                                    return;
                                }
                                StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.10
                                    @Override // java.lang.Runnable
                                    @SuppressLint({"SetTextI18n"})
                                    public void run() {
                                        int i4 = (int) ((120000 - j) / 1000);
                                        if (i4 <= 100) {
                                            progressBar2.setProgress(i4);
                                            textView.setText(i4 + "%");
                                        }
                                    }
                                });
                            }
                        } else if (StorageStatusActivity.this.isFailFormat == 0) {
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    textView.setEnabled(true);
                                    textView2.setVisibility(8);
                                    progressBar2.setVisibility(8);
                                    textView3.setVisibility(0);
                                    textView3.setText(R.string.format_failed);
                                    textView4.setVisibility(0);
                                    textView4.setText(R.string.check_tf_status);
                                    viewFindViewById.setVisibility(0);
                                    textView.setText(R.string.i_know);
                                    textView.setEnabled(true);
                                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
                                    layoutParams.topToBottom = textView4.getId();
                                    textView.setLayoutParams(layoutParams);
                                }
                            });
                            cancel();
                            StorageStatusActivity.this.countDownTimer = null;
                        } else if (StorageStatusActivity.this.isFailFormat != 2) {
                            if (StorageStatusActivity.this.isFailFormat == 1) {
                                StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.7
                                    @Override // java.lang.Runnable
                                    @SuppressLint({"SetTextI18n"})
                                    public void run() {
                                        progressBar2.setProgress(75);
                                        textView.setText("75%");
                                    }
                                });
                            }
                        } else {
                            StorageStatusActivity.this.secondProgress++;
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.6
                                @Override // java.lang.Runnable
                                @SuppressLint({"SetTextI18n"})
                                public void run() {
                                    if (StorageStatusActivity.this.secondProgress <= 100) {
                                        progressBar2.setProgress(StorageStatusActivity.this.secondProgress);
                                        textView.setText(StorageStatusActivity.this.secondProgress + "%");
                                    }
                                }
                            });
                            return;
                        }
                        IPCManager.getInstance().getDevice(StorageStatusActivity.this.iotId).queryTFCard(new IPanelCallback() { // from class: activity.StorageStatusActivity.14.11
                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                            public void onComplete(boolean z, Object obj) {
                                if (!z) {
                                    StorageStatusActivity.this.isFailFormat = 0;
                                    return;
                                }
                                if (obj == null || "".equals(String.valueOf(obj))) {
                                    StorageStatusActivity.this.isFailFormat = 0;
                                    return;
                                }
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.containsKey("code") && object.containsKey("data")) {
                                    try {
                                        JSONObject jSONObject = object.getJSONObject("data");
                                        if (jSONObject.containsKey("TFCardFormatStatus")) {
                                            int iIntValue = jSONObject.getInteger("TFCardFormatStatus").intValue();
                                            if (iIntValue == 2) {
                                                StorageStatusActivity.this.isFailFormat = 2;
                                            } else if (iIntValue == 0) {
                                                StorageStatusActivity.this.isFailFormat = 0;
                                            } else if (iIntValue == 1) {
                                                StorageStatusActivity.this.isFailFormat = 1;
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }

                @Override // android.os.CountDownTimer
                public void onFinish() {
                    StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.14.12
                        @Override // java.lang.Runnable
                        public void run() {
                            StorageStatusActivity.this.initData();
                            StorageStatusActivity.this.f1586dialog.dismiss();
                            Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.format_succeed, 0).show();
                        }
                    });
                    cancel();
                    StorageStatusActivity.this.countDownTimer = null;
                    StorageStatusActivity storageStatusActivity = StorageStatusActivity.this;
                    storageStatusActivity.remainStorage = storageStatusActivity.totalStorage;
                    StorageStatusActivity.this.initData();
                }
            };
            this.countDownTimer.start();
            this.isCardFormat = false;
            HandlerSend();
        } else {
            progressBar = progressBar2;
        }
        final ProgressBar progressBar3 = progressBar;
        IPCManager.getInstance().getDevice(this.iotId).formatStorageMedium(new IPanelCallback() { // from class: activity.StorageStatusActivity.15
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (!object.containsKey("code") || object.getInteger("code").intValue() == 200) {
                        return;
                    }
                    StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.15.1
                        @Override // java.lang.Runnable
                        public void run() {
                            textView.setEnabled(true);
                            textView2.setVisibility(8);
                            progressBar3.setVisibility(8);
                            textView3.setVisibility(0);
                            textView3.setText(R.string.format_failed);
                            textView4.setVisibility(0);
                            viewFindViewById.setVisibility(0);
                            textView4.setText(R.string.check_tf_status);
                            textView.setText(R.string.i_know);
                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
                            layoutParams.topToBottom = textView4.getId();
                            textView.setLayoutParams(layoutParams);
                        }
                    });
                    StorageStatusActivity.this.countDownTimer.cancel();
                    StorageStatusActivity.this.countDownTimer = null;
                    return;
                }
                if (obj == null || String.valueOf(obj).equals("")) {
                    StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.15.4
                        @Override // java.lang.Runnable
                        public void run() {
                            textView.setEnabled(true);
                            textView2.setVisibility(8);
                            progressBar3.setVisibility(8);
                            textView3.setVisibility(0);
                            textView3.setText(R.string.format_failed);
                            textView4.setVisibility(0);
                            viewFindViewById.setVisibility(0);
                            textView4.setText(R.string.check_tf_status);
                            textView.setText(R.string.i_know);
                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
                            layoutParams.topToBottom = textView4.getId();
                            textView.setLayoutParams(layoutParams);
                        }
                    });
                    StorageStatusActivity.this.countDownTimer.cancel();
                    StorageStatusActivity.this.countDownTimer = null;
                } else if (StorageStatusActivity.getJSONType(String.valueOf(obj))) {
                    JSONObject object2 = JSONObject.parseObject(String.valueOf(obj));
                    if (!object2.containsKey(AlinkConstants.KEY_LOCALIZED_MSG)) {
                        StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.15.3
                            @Override // java.lang.Runnable
                            public void run() {
                                textView.setEnabled(true);
                                textView2.setVisibility(8);
                                progressBar3.setVisibility(8);
                                textView3.setVisibility(0);
                                textView3.setText(R.string.format_failed);
                                textView4.setVisibility(0);
                                viewFindViewById.setVisibility(0);
                                textView4.setText(R.string.check_tf_status);
                                textView.setText(R.string.i_know);
                                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
                                layoutParams.topToBottom = textView4.getId();
                                textView.setLayoutParams(layoutParams);
                            }
                        });
                        StorageStatusActivity.this.countDownTimer.cancel();
                        StorageStatusActivity.this.countDownTimer = null;
                    } else if (object2.getString(AlinkConstants.KEY_LOCALIZED_MSG).equals("设备不在线")) {
                        StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.15.2
                            @Override // java.lang.Runnable
                            public void run() {
                                StorageStatusActivity.this.f1586dialog.dismiss();
                                Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.device_offline_tf, 0).show();
                            }
                        });
                    }
                }
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

    public void showTFDialog() {
        showProgressDialog();
        IPCManager.getInstance().getDevice(this.iotId).formatStorageMedium(new IPanelCallback() { // from class: activity.StorageStatusActivity.16
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.16.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    StorageStatusActivity.this.dismissProgressDialog();
                                    Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.format_failed, 0).show();
                                }
                            });
                            return;
                        } else {
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.16.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    StorageStatusActivity.this.dismissProgressDialog();
                                    Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.format_succeed, 0).show();
                                    StorageStatusActivity.this.remainStorage = StorageStatusActivity.this.totalStorage;
                                    StorageStatusActivity.this.initData();
                                }
                            });
                            return;
                        }
                    }
                    return;
                }
                if (obj == null || String.valueOf(obj).equals("")) {
                    StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.16.5
                        @Override // java.lang.Runnable
                        public void run() {
                            StorageStatusActivity.this.dismissProgressDialog();
                            Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.format_failed, 0).show();
                        }
                    });
                    return;
                }
                if (StorageStatusActivity.getJSONType(String.valueOf(obj))) {
                    JSONObject object2 = JSONObject.parseObject(String.valueOf(obj));
                    if (!object2.containsKey(AlinkConstants.KEY_LOCALIZED_MSG)) {
                        StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.16.4
                            @Override // java.lang.Runnable
                            public void run() {
                                StorageStatusActivity.this.dismissProgressDialog();
                                Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.format_failed, 0).show();
                            }
                        });
                    } else if (object2.getString(AlinkConstants.KEY_LOCALIZED_MSG).equals("设备不在线")) {
                        StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.16.3
                            @Override // java.lang.Runnable
                            public void run() {
                                StorageStatusActivity.this.dismissProgressDialog();
                                Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.device_offline_tf, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevParam(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.storage_record_mode_key))) {
            map.put(Constants.STORAGE_RECORD_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.StorageStatusActivity.17
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.17.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setStorageRecordMode(StorageStatusActivity.this.iotId, Integer.parseInt(obj.toString()));
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.17.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    StorageStatusActivity.this.recordMode = SharePreferenceManager.getInstance().getStorageRecordMode(StorageStatusActivity.this.iotId);
                                    StorageStatusActivity.this.itemRecordMode.setRightText(StorageStatusActivity.this.recordModeArr[StorageStatusActivity.this.recordMode - 1]);
                                    StorageStatusActivity.this.recordTextView.setText(StorageStatusActivity.this.recordModeArrDesc[Integer.parseInt(obj.toString()) - 1]);
                                    Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        } else if (str.equals(getString(R.string.record_quality))) {
            map.put(Constants.StorageRecordQuality, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.StorageStatusActivity.18
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.18.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setRecordQuality(StorageStatusActivity.this.iotId, Integer.parseInt(obj.toString()));
                            StorageStatusActivity.this.mHandler.post(new Runnable() { // from class: activity.StorageStatusActivity.18.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    StorageStatusActivity.this.itemRecordQuality.setRightText(StorageStatusActivity.this.recordQualityArr[StorageStatusActivity.this.recordQuality]);
                                    Toast.makeText(StorageStatusActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public static boolean getJSONType(String str) {
        if (StringUtils.isNotBlank(str)) {
            String strTrim = str.trim();
            if (strTrim.startsWith("{") && strTrim.endsWith("}")) {
                return true;
            }
            if (strTrim.startsWith("[") && strTrim.endsWith("]")) {
                return true;
            }
        }
        return false;
    }
}
