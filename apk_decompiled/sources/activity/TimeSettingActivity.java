package activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import bean.DeviceTimeModel;
import bean.TimeSectionForPlan;
import bean.TimeZoneCodeModel;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.seculink.app.R;
import config.AppConfig;
import config.Constants;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import sdk.ChannelManager;
import sdk.IPCManager;
import tools.DateUtil;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import tools.TimeUtil;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class TimeSettingActivity extends CommonActivity {
    private boolean DSTSwitch;
    private SimpleDateFormat dateFormat;
    private int deviceTime;
    private String iotId;
    private ItemView itemDeviceTime;
    private ItemView itemDeviceTimeZone;
    private ItemView itemSyncPhone;
    ConstraintLayout layout_main;
    private Button saveButton;
    private ItemView summer_time;
    private SwitchCompat sw_summer_time;
    private String timeZone;
    private Handler uiHandler = new Handler();
    private List<TimeSectionForPlan> timeLst = new LinkedList();
    private SharePreferenceManager.OnCallSetListener mSPModifyListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.TimeSettingActivity.8
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            TimeSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.TimeSettingActivity.8.1
                @Override // java.lang.Runnable
                public void run() {
                    String str3 = str2;
                    if (str3 == null || str3.trim().equals("")) {
                        return;
                    }
                    if (str2.equals(TimeSettingActivity.this.getString(R.string.device_time_key))) {
                        TimeSettingActivity.this.deviceTime = SharePreferenceManager.getInstance().getDeviceTime(str);
                        TimeSettingActivity.this.timeZone = SharePreferenceManager.getInstance().getDeviceTZ(str);
                        Log.e(TimeSettingActivity.this.TAG, "getDeviceTime(): " + TimeSettingActivity.this.deviceTime);
                        Date date = new Date(((long) TimeSettingActivity.this.deviceTime) * 1000);
                        TimeSettingActivity.this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        try {
                            TimeSettingActivity.this.dateFormat.setTimeZone(TimeZone.getTimeZone(TimeSettingActivity.this.timeZone));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        TimeSettingActivity.this.itemDeviceTime.setRightText(TimeSettingActivity.this.dateFormat.format(date));
                        TimeSettingActivity.this.itemDeviceTimeZone.setRightText(TimeSettingActivity.this.timeZone);
                        return;
                    }
                    if (str2.equals(TimeSettingActivity.this.getString(R.string.sync_phone_time_key))) {
                        TimeSettingActivity.this.deviceTime = SharePreferenceManager.getInstance().getDeviceTime(str);
                        TimeSettingActivity.this.timeZone = SharePreferenceManager.getInstance().getDeviceTZ(str);
                        Log.e(TimeSettingActivity.this.TAG, "getDeviceTime(): " + TimeSettingActivity.this.deviceTime);
                        Date date2 = new Date(((long) TimeSettingActivity.this.deviceTime) * 1000);
                        TimeSettingActivity.this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        try {
                            TimeSettingActivity.this.dateFormat.setTimeZone(TimeZone.getTimeZone(TimeSettingActivity.this.timeZone));
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        TimeSettingActivity.this.itemDeviceTime.setRightText(TimeSettingActivity.this.dateFormat.format(date2));
                        TimeSettingActivity.this.itemDeviceTimeZone.setRightText(TimeSettingActivity.this.timeZone);
                    }
                }
            });
        }
    };
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: activity.TimeSettingActivity.9
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(TimeSettingActivity.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            if (str.equals("/thing/properties")) {
                SettingsCtrl.getInstance().getProperties(TimeSettingActivity.this.iotId, new MyCallback() { // from class: activity.TimeSettingActivity.9.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_time_setting;
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.iotId = intent.getStringExtra("iotId");
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        ((TitleView) findViewById(R.id.fl_titlebar)).setOnViewClick(new TitleView.OnViewClick() { // from class: activity.TimeSettingActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                TimeSettingActivity.this.setResult(-1);
                TimeSettingActivity.this.finish();
            }
        });
        this.itemDeviceTime = (ItemView) findViewById(R.id.item_device_time);
        this.itemDeviceTimeZone = (ItemView) findViewById(R.id.item_device_timezone);
        this.itemSyncPhone = (ItemView) findViewById(R.id.item_sync_phone);
        this.summer_time = (ItemView) findViewById(R.id.summer_time);
        this.summer_time.addRightView(new SwitchCompat(this));
        this.sw_summer_time = (SwitchCompat) this.summer_time.getRightView();
        this.sw_summer_time.setTextOff("");
        this.sw_summer_time.setTextOn("");
        this.sw_summer_time.setText("");
        this.sw_summer_time.setThumbDrawable(null);
        this.sw_summer_time.setBackgroundResource(R.drawable.sel_switch);
        this.itemSyncPhone.setOnClickListener(new View.OnClickListener() { // from class: activity.TimeSettingActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Date date = new Date(System.currentTimeMillis());
                TimeSettingActivity.this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                TimeSettingActivity.this.itemDeviceTime.setRightText(TimeSettingActivity.this.dateFormat.format(date));
                String strSubstring = TimeSettingActivity.this.getCurrentTimeZone().substring(0, 9);
                if (strSubstring.contains("GMT")) {
                    TimeSettingActivity.this.itemDeviceTimeZone.setRightText(strSubstring);
                }
            }
        });
        this.itemDeviceTimeZone.setOnClickListener(new View.OnClickListener() { // from class: activity.TimeSettingActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(TimeSettingActivity.this.getActivity(), (Class<?>) SelectTimeZoneCodeActivity.class);
                intent.putExtra("iotId", TimeSettingActivity.this.iotId);
                intent.putExtra("TimeZone", TimeSettingActivity.this.itemDeviceTimeZone.getRightText());
                TimeSettingActivity.this.startActivityForResult(intent, 1);
            }
        });
        this.itemDeviceTime.setOnClickListener(new View.OnClickListener() { // from class: activity.TimeSettingActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TimeSettingActivity.this.showTimePicker();
            }
        });
        this.saveButton = (Button) findViewById(R.id.bt_save_settings);
        this.saveButton.setOnClickListener(new View.OnClickListener() { // from class: activity.TimeSettingActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TimeSettingActivity timeSettingActivity = TimeSettingActivity.this;
                timeSettingActivity.updateTimeParam(timeSettingActivity.getString(R.string.device_time_key), TimeSettingActivity.this.itemDeviceTime.getRightText(), TimeSettingActivity.this.itemDeviceTimeZone.getRightText());
            }
        });
        this.sw_summer_time.setOnClickListener(new View.OnClickListener() { // from class: activity.TimeSettingActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TimeSettingActivity.this.DSTSwitch = !r3.DSTSwitch;
                TimeSettingActivity.this.sw_summer_time.setChecked(TimeSettingActivity.this.DSTSwitch);
                TimeSettingActivity timeSettingActivity = TimeSettingActivity.this;
                timeSettingActivity.updateDevParam(timeSettingActivity.getString(R.string.DSTSwitch), Integer.valueOf(TimeSettingActivity.this.DSTSwitch ? 1 : 0));
            }
        });
    }

    public static boolean isInDaylightSavingTime() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.inDaylightTime(Calendar.getInstance(timeZone).getTime());
    }

    private boolean isInDaylightSavingTime2() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.inDaylightTime(Calendar.getInstance(timeZone).getTime());
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.mSPModifyListener);
        this.deviceTime = SharePreferenceManager.getInstance().getDeviceTime(this.iotId);
        this.timeZone = SharePreferenceManager.getInstance().getDeviceTZ(this.iotId);
        Log.e(this.TAG, "getDeviceTime(): " + this.deviceTime);
        Date date = new Date(((long) this.deviceTime) * 1000);
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            this.dateFormat.setTimeZone(TimeZone.getTimeZone(this.timeZone));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.itemDeviceTime.setRightText(this.dateFormat.format(date));
        this.itemDeviceTimeZone.setRightText(this.timeZone);
        if (!AppConfig.isChina) {
            this.DSTSwitch = SharePreferenceManager.getInstance().getDSTSwitch(this.iotId) != 0;
            this.sw_summer_time.setChecked(this.DSTSwitch);
            this.summer_time.setVisibility(0);
        }
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.TimeSettingActivity.7
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
    }

    public String getCurrentTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        String str = timeZone.getDisplayName(false, 0) + " " + timeZone.getDisplayName(false, 1);
        return str.contains("EST") ? "GMT-05:00" : str.contains("EDT") ? "GMT-04:00" : str.contains("CET") ? "GMT+01:00" : str.contains("JST") ? "GMT+09:00" : str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevParam(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.DSTSwitch))) {
            map.put(Constants.DSTSwitch, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.TimeSettingActivity.10
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z) {
                        TimeSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.TimeSettingActivity.10.3
                            @Override // java.lang.Runnable
                            public void run() {
                                TimeSettingActivity.this.DSTSwitch = SharePreferenceManager.getInstance().getDSTSwitch(TimeSettingActivity.this.iotId) != 0;
                                TimeSettingActivity.this.sw_summer_time.setChecked(TimeSettingActivity.this.DSTSwitch);
                                Toast.makeText(TimeSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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
                            TimeSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.TimeSettingActivity.10.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(TimeSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            TimeSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.TimeSettingActivity.10.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    SharePreferenceManager.getInstance().setDSTSwitch(TimeSettingActivity.this.iotId, Integer.parseInt(obj.toString()));
                                    TimeSettingActivity.this.DSTSwitch = SharePreferenceManager.getInstance().getDSTSwitch(TimeSettingActivity.this.iotId) != 0;
                                    TimeSettingActivity.this.sw_summer_time.setChecked(TimeSettingActivity.this.DSTSwitch);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTimeParam(String str, String str2, final String str3) {
        HashMap map = new HashMap();
        try {
            this.dateFormat.setTimeZone(TimeZone.getTimeZone(str3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final long dateTime = DateUtil.getDateTime(str2, this.dateFormat);
        DeviceTimeModel deviceTimeModel = new DeviceTimeModel();
        deviceTimeModel.setTime((int) (dateTime / 1000));
        deviceTimeModel.setTZ(str3);
        Log.e(this.TAG, "updateTimeParam: " + deviceTimeModel.toString());
        if (str.equals(getString(R.string.device_time_key))) {
            map.put(Constants.DEVICE_TIME, deviceTimeModel);
        }
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.TimeSettingActivity.11
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        TimeSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.TimeSettingActivity.11.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(TimeSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                        return;
                    }
                    SharePreferenceManager.getInstance().setDeviceTime(TimeSettingActivity.this.iotId, (int) (dateTime / 1000));
                    SharePreferenceManager.getInstance().setDeviceTZ(TimeSettingActivity.this.iotId, str3);
                    TimeSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.TimeSettingActivity.11.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(TimeSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 1, 0, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2025, 10, 11, 23, 59);
        TimePickerView timePickerViewBuild = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() { // from class: activity.TimeSettingActivity.13
            @Override // com.bigkoo.pickerview.listener.OnTimeSelectListener
            public void onTimeSelect(Date date, View view2) {
                TimeSettingActivity.this.itemDeviceTime.setRightText(DateUtil.getDateFormat(date, TimeSettingActivity.this.dateFormat));
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() { // from class: activity.TimeSettingActivity.12
            @Override // com.bigkoo.pickerview.listener.OnTimeSelectChangeListener
            public void onTimeSelectChanged(Date date) {
            }
        }).setRangDate(calendar, calendar2).setType(new boolean[]{true, true, true, true, true, true}).setSubmitText(getResources().getString(R.string.confirm)).setCancelText(getResources().getString(R.string.cancel)).setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day), getString(R.string.hour), getString(R.string.minute), getString(R.string.second)).setSubmitColor(getResources().getColor(R.color.colorAccent)).setCancelColor(getResources().getColor(R.color.colorAccent)).build();
        Dialog dialog2 = timePickerViewBuild.getDialog();
        if (dialog2 != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2, 80);
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            timePickerViewBuild.getDialogContainerLayout().setLayoutParams(layoutParams);
            Window window = dialog2.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.picker_view_slide_anim);
                window.setGravity(80);
            }
        }
        timePickerViewBuild.setDate(DateUtil.getCalendar(this.itemDeviceTime.getRightText(), this.dateFormat));
        timePickerViewBuild.show();
    }

    private String changeTime(int i) {
        return (i < 0 || i > 86399) ? "时间非法" : formatTime(i);
    }

    public String formatTime(int i) {
        int i2;
        StringBuilder sb = new StringBuilder();
        int i3 = 0;
        if (i >= 60) {
            i2 = i / 60;
            int i4 = i % 60;
        } else {
            i2 = 0;
        }
        if (i2 >= 60) {
            i3 = i2 / 60;
            i2 %= 60;
        }
        if (i3 < 10) {
            sb.append("0");
        }
        sb.append(i3);
        sb.append(":");
        if (i2 < 10) {
            sb.append("0");
        }
        sb.append(i2);
        sb.append(":00");
        return sb.toString();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1) {
            TimeZoneCodeModel timeZoneCodeModel = (TimeZoneCodeModel) intent.getSerializableExtra("deviceTimeZone");
            Date dateTransformTime = TimeUtil.transformTime(new Date(((long) this.deviceTime) * 1000), TimeZone.getTimeZone(this.timeZone), TimeZone.getTimeZone(timeZoneCodeModel.getCode()));
            this.itemDeviceTimeZone.setRightText(timeZoneCodeModel.getCode());
            this.itemDeviceTime.setRightText(DateUtil.getDateFormat(dateTransformTime, this.dateFormat));
        }
    }
}
