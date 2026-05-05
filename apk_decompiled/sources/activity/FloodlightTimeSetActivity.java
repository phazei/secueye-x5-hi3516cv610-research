package activity;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import anetwork.channel.util.RequestConstant;
import bean.AlarmPlanBean;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.seculink.app.R;
import config.Constants;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import tools.TimeUtil;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class FloodlightTimeSetActivity extends CommonActivity {
    private ItemView beginTime;
    private Button checkBtn;
    String[] days;

    /* JADX INFO: renamed from: dialog, reason: collision with root package name */
    private Dialog f1573dialog;
    private ItemView endTime;
    ConstraintLayout layout_main;
    NumberPicker numberPicker;
    TimePicker picker;
    private ItemView setDay;
    private BottomSheetDialog sheetDialog;
    String[] strings;
    TitleView titleView;
    private boolean isBeginTime = true;
    List<CheckBox> checkBoxList = new ArrayList();
    AlarmPlanBean alarmPlanBean = new AlarmPlanBean();
    AlarmPlanBean.bean AlarmPlanBeanbean = new AlarmPlanBean.bean();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_floodlight_time_set_layout;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.beginTime = (ItemView) findViewById(R.id.beginTime);
        this.endTime = (ItemView) findViewById(R.id.endTime);
        this.checkBtn = (Button) findViewById(R.id.checkBtn);
        this.setDay = (ItemView) findViewById(R.id.setDay);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.alarmPlanBean = (AlarmPlanBean) new Gson().fromJson(getIntent().getStringExtra("bean"), AlarmPlanBean.class);
        this.days = getResources().getStringArray(R.array.day_name);
        this.strings = new String[]{getString(R.string.today), getString(R.string.tomorrow)};
        this.titleView = (TitleView) findViewById(R.id.titleView);
        this.titleView.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.FloodlightTimeSetActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                FloodlightTimeSetActivity.this.finish();
            }
        });
        this.f1573dialog = new Dialog(this, R.style.alarm_dialog);
        this.f1573dialog.setContentView(R.layout.alarm_plan_dialog);
        ((Window) Objects.requireNonNull(this.f1573dialog.getWindow())).setGravity(80);
        getWindowManager().getDefaultDisplay().getSize(new Point());
        this.f1573dialog.getWindow().getAttributes().width = r9.x - 50;
        if (!getIntent().getStringExtra("isEdit").equals(RequestConstant.FALSE)) {
            this.AlarmPlanBeanbean = this.alarmPlanBean.getList().get(Integer.parseInt(getIntent().getStringExtra("isEdit")));
            Log.d(this.TAG, "onCreate: -----------");
            DecimalFormat decimalFormat = new DecimalFormat("00");
            String str = decimalFormat.format(TimeUtil.getHours(this.AlarmPlanBeanbean.getBeginTime())) + ":" + decimalFormat.format(TimeUtil.getMins(this.AlarmPlanBeanbean.getBeginTime()));
            String str2 = decimalFormat.format(TimeUtil.getHours(this.AlarmPlanBeanbean.getEndTime())) + ":" + decimalFormat.format(TimeUtil.getMins(this.AlarmPlanBeanbean.getEndTime()));
            if (this.AlarmPlanBeanbean.isAcrossDay() == 0) {
                this.beginTime.setRightText(getString(R.string.today) + " " + str);
                this.endTime.setRightText(getString(R.string.today) + " " + str2);
            } else {
                this.beginTime.setRightText(getString(R.string.today) + " " + str);
                this.endTime.setRightText(getString(R.string.tomorrow) + " " + str2);
            }
            int weekMask = this.AlarmPlanBeanbean.getWeekMask();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                if ((((1 << i) & weekMask) >> i) == 1) {
                    sb.append(this.days[i]);
                    sb.append("、");
                }
            }
            if (weekMask == 127) {
                this.setDay.setRightText(getString(R.string.everyday));
            } else {
                try {
                    this.setDay.setRightText(sb.substring(0, sb.length() - 1));
                } catch (Exception unused) {
                }
            }
        }
        this.picker = (TimePicker) this.f1573dialog.findViewById(R.id.timepicker);
        this.numberPicker = (NumberPicker) this.f1573dialog.findViewById(R.id.edit);
        this.picker.setIs24HourView(true);
        this.numberPicker.setDisplayedValues(this.strings);
        this.numberPicker.setMinValue(0);
        this.f1573dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeSetActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FloodlightTimeSetActivity.this.f1573dialog.dismiss();
            }
        });
        this.f1573dialog.findViewById(R.id.begin).setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeSetActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                DecimalFormat decimalFormat2 = new DecimalFormat("00");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (FloodlightTimeSetActivity.this.isBeginTime) {
                        FloodlightTimeSetActivity.this.beginTime.setRightText(FloodlightTimeSetActivity.this.strings[FloodlightTimeSetActivity.this.numberPicker.getValue()] + "  " + decimalFormat2.format(FloodlightTimeSetActivity.this.picker.getHour()) + ":" + decimalFormat2.format(FloodlightTimeSetActivity.this.picker.getMinute()));
                        FloodlightTimeSetActivity.this.AlarmPlanBeanbean.setBeginTime((FloodlightTimeSetActivity.this.picker.getHour() * 60 * 60) + (FloodlightTimeSetActivity.this.picker.getMinute() * 60));
                    } else {
                        FloodlightTimeSetActivity.this.endTime.setRightText(FloodlightTimeSetActivity.this.strings[FloodlightTimeSetActivity.this.numberPicker.getValue()] + "  " + decimalFormat2.format(FloodlightTimeSetActivity.this.picker.getHour()) + ":" + decimalFormat2.format(FloodlightTimeSetActivity.this.picker.getMinute()));
                        FloodlightTimeSetActivity.this.AlarmPlanBeanbean.setEndTime((FloodlightTimeSetActivity.this.picker.getHour() * 60 * 60) + (FloodlightTimeSetActivity.this.picker.getMinute() * 60));
                        FloodlightTimeSetActivity.this.AlarmPlanBeanbean.setAcrossDay(FloodlightTimeSetActivity.this.numberPicker.getValue());
                    }
                } else if (FloodlightTimeSetActivity.this.isBeginTime) {
                    FloodlightTimeSetActivity.this.beginTime.setRightText(FloodlightTimeSetActivity.this.strings[FloodlightTimeSetActivity.this.numberPicker.getValue()] + "  " + decimalFormat2.format(FloodlightTimeSetActivity.this.picker.getCurrentHour()) + ":" + decimalFormat2.format(FloodlightTimeSetActivity.this.picker.getCurrentMinute()));
                    FloodlightTimeSetActivity.this.AlarmPlanBeanbean.setBeginTime((FloodlightTimeSetActivity.this.picker.getCurrentHour().intValue() * 60 * 60) + (FloodlightTimeSetActivity.this.picker.getCurrentMinute().intValue() * 60));
                } else {
                    FloodlightTimeSetActivity.this.endTime.setRightText(FloodlightTimeSetActivity.this.strings[FloodlightTimeSetActivity.this.numberPicker.getValue()] + "  " + decimalFormat2.format(FloodlightTimeSetActivity.this.picker.getCurrentHour()) + ":" + decimalFormat2.format(FloodlightTimeSetActivity.this.picker.getCurrentMinute()));
                    FloodlightTimeSetActivity.this.AlarmPlanBeanbean.setEndTime((FloodlightTimeSetActivity.this.picker.getCurrentHour().intValue() * 60 * 60) + (FloodlightTimeSetActivity.this.picker.getCurrentMinute().intValue() * 60));
                    FloodlightTimeSetActivity.this.AlarmPlanBeanbean.setAcrossDay(FloodlightTimeSetActivity.this.numberPicker.getValue());
                }
                FloodlightTimeSetActivity.this.f1573dialog.dismiss();
            }
        });
        this.beginTime.setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeSetActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String[] strArr = {FloodlightTimeSetActivity.this.getString(R.string.today)};
                FloodlightTimeSetActivity.this.numberPicker.setMaxValue(0);
                FloodlightTimeSetActivity.this.numberPicker.setDisplayedValues(strArr);
                FloodlightTimeSetActivity.this.isBeginTime = true;
                FloodlightTimeSetActivity.this.f1573dialog.show();
            }
        });
        this.endTime.setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeSetActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String[] strArr = {FloodlightTimeSetActivity.this.getString(R.string.today), FloodlightTimeSetActivity.this.getString(R.string.tomorrow)};
                FloodlightTimeSetActivity.this.isBeginTime = false;
                FloodlightTimeSetActivity.this.numberPicker.setDisplayedValues(strArr);
                FloodlightTimeSetActivity.this.numberPicker.setMaxValue(strArr.length - 1);
                FloodlightTimeSetActivity.this.f1573dialog.show();
            }
        });
        this.sheetDialog = new BottomSheetDialog(this);
        this.sheetDialog.setContentView(R.layout.alarm_plan_setday_dialog);
        this.checkBoxList.add((CheckBox) this.sheetDialog.findViewById(R.id.checkBox6));
        this.checkBoxList.add((CheckBox) this.sheetDialog.findViewById(R.id.checkBox));
        this.checkBoxList.add((CheckBox) this.sheetDialog.findViewById(R.id.checkBox1));
        this.checkBoxList.add((CheckBox) this.sheetDialog.findViewById(R.id.checkBox2));
        this.checkBoxList.add((CheckBox) this.sheetDialog.findViewById(R.id.checkBox3));
        this.checkBoxList.add((CheckBox) this.sheetDialog.findViewById(R.id.checkBox4));
        this.checkBoxList.add((CheckBox) this.sheetDialog.findViewById(R.id.checkBox5));
        this.sheetDialog.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeSetActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FloodlightTimeSetActivity.this.sheetDialog.dismiss();
            }
        });
        this.sheetDialog.findViewById(R.id.checkBtn).setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeSetActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FloodlightTimeSetActivity.this.setItemDay();
                FloodlightTimeSetActivity.this.sheetDialog.dismiss();
            }
        });
        this.setDay.setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeSetActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FloodlightTimeSetActivity.this.sheetDialog.show();
            }
        });
        this.checkBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeSetActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (FloodlightTimeSetActivity.this.checkBean()) {
                    if (FloodlightTimeSetActivity.this.alarmPlanBean == null) {
                        FloodlightTimeSetActivity.this.alarmPlanBean = new AlarmPlanBean();
                    }
                    if (!FloodlightTimeSetActivity.this.getIntent().getStringExtra("isEdit").equals(RequestConstant.FALSE)) {
                        if (FloodlightTimeSetActivity.this.AlarmPlanBeanbean.getWeekMask() == 0) {
                            FloodlightTimeSetActivity.this.AlarmPlanBeanbean.setWeekMask(127);
                        }
                        FloodlightTimeSetActivity.this.alarmPlanBean.getList().set(Integer.parseInt(FloodlightTimeSetActivity.this.getIntent().getStringExtra("isEdit")), FloodlightTimeSetActivity.this.AlarmPlanBeanbean);
                    } else {
                        if (FloodlightTimeSetActivity.this.AlarmPlanBeanbean.getWeekMask() == 0) {
                            FloodlightTimeSetActivity.this.AlarmPlanBeanbean.setWeekMask(127);
                        }
                        FloodlightTimeSetActivity.this.alarmPlanBean.getList().add(FloodlightTimeSetActivity.this.AlarmPlanBeanbean);
                    }
                    FloodlightTimeSetActivity floodlightTimeSetActivity = FloodlightTimeSetActivity.this;
                    floodlightTimeSetActivity.setAlarmPlan(floodlightTimeSetActivity.alarmPlanBean.getIotId());
                    return;
                }
                final Dialog dialog2 = new Dialog(FloodlightTimeSetActivity.this.getActivity(), R.style.Translucent_Dialog);
                dialog2.setContentView(R.layout.alarm_tips);
                dialog2.findViewById(R.id.alarm_dialog_cancel).setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeSetActivity.9.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        dialog2.dismiss();
                    }
                });
                dialog2.show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkBean() {
        if (this.AlarmPlanBeanbean.isAcrossDay() != 0) {
            return (this.AlarmPlanBeanbean.getBeginTime() == -1 || this.AlarmPlanBeanbean.getEndTime() == -1) ? false : true;
        }
        if (this.AlarmPlanBeanbean.getEndTime() > this.AlarmPlanBeanbean.getBeginTime()) {
            return (this.AlarmPlanBeanbean.getBeginTime() == -1 || this.AlarmPlanBeanbean.getEndTime() == -1) ? false : true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAlarmPlan(String str) {
        HashMap map = new HashMap();
        map.put(Constants.FloodlightSchedule, this.alarmPlanBean.getList());
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: activity.FloodlightTimeSetActivity.10
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    FloodlightTimeSetActivity.this.runOnUiThread(new Runnable() { // from class: activity.FloodlightTimeSetActivity.10.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SharePreferenceManager.getInstance().setFloodlightScheduleJson(FloodlightTimeSetActivity.this.alarmPlanBean.getIotId(), new Gson().toJson(FloodlightTimeSetActivity.this.alarmPlanBean));
                        }
                    });
                    FloodlightTimeSetActivity.this.finish();
                } else {
                    FloodlightTimeSetActivity.this.runOnUiThread(new Runnable() { // from class: activity.FloodlightTimeSetActivity.10.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(FloodlightTimeSetActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setItemDay() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < this.checkBoxList.size(); i++) {
            if (this.checkBoxList.get(i).isChecked()) {
                sb.append(this.days[i]);
                sb.append("、");
                sb2.append("1");
            } else {
                sb2.append("0");
            }
        }
        if (sb.length() > 1) {
            this.setDay.setRightText(sb.substring(0, sb.length() - 1));
        }
        this.AlarmPlanBeanbean.setWeekMask(Integer.parseInt(String.valueOf(sb2.reverse()), 2));
    }
}
