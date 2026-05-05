package activity;

import adapter.MyListAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import bean.TimeSectionForPlan;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import config.Constants;
import dialog.TimeSettingDialog;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import sdk.IPCManager;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class AlarmPlanSettingsActivity extends CommonActivity {
    private Button bindBtn;
    private TitleView fl_titlebar;
    private String iotId;
    private CheckBox isAllDayCb;
    LinearLayout layout_main;
    private ListView listView;
    private MyListAdapter myListAdapter;
    Handler uiHandler;
    private Button unbindBtn;
    private String TAG = getClass().getSimpleName();
    private List<TimeSectionForPlan> timeLst = new LinkedList();
    boolean isAllDay = false;
    private boolean hasPlan = false;
    private String templateId = "";
    private String templateName = "";
    private String planId = "";
    private SharePreferenceManager.OnCallSetListener alarmPlanChangeListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.AlarmPlanSettingsActivity.7
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            AlarmPlanSettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmPlanSettingsActivity.7.1
                @Override // java.lang.Runnable
                public void run() {
                    if (str2.equals(AlarmPlanSettingsActivity.this.getString(R.string.alarm_plan))) {
                        String alarmPlan = SharePreferenceManager.getInstance().getAlarmPlan(str);
                        if (!TextUtils.isEmpty(alarmPlan)) {
                            AlarmPlanSettingsActivity.this.initTimeMap();
                            JSONArray array = JSONArray.parseArray(alarmPlan);
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject jSONObject = array.getJSONObject(i);
                                int iIntValue = ((Integer) jSONObject.get("DayOfWeek")).intValue();
                                int iIntValue2 = ((Integer) jSONObject.get("BeginTime")).intValue();
                                int iIntValue3 = ((Integer) jSONObject.get("EndTime")).intValue();
                                TimeSectionForPlan timeSectionForPlan = new TimeSectionForPlan();
                                timeSectionForPlan.setMday(iIntValue);
                                timeSectionForPlan.setBegin(iIntValue2);
                                timeSectionForPlan.setEnd(iIntValue3);
                                timeSectionForPlan.setCheck(true);
                                AlarmPlanSettingsActivity.this.timeLst.set(iIntValue, timeSectionForPlan);
                            }
                        } else {
                            AlarmPlanSettingsActivity.this.initTimeMap();
                        }
                        AlarmPlanSettingsActivity.this.refreshUI();
                    }
                }
            });
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_easy_plan_settings;
    }

    private void restoreData() {
        this.isAllDay = false;
        this.hasPlan = false;
        this.templateId = "";
        this.templateName = "";
        this.planId = "";
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.uiHandler = new Handler(getMainLooper());
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.iotId = getIntent().getStringExtra("iotId");
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.alarmPlanChangeListener);
        initView();
        refreshUI();
    }

    private void initView() {
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.myListAdapter = new MyListAdapter(this, this.timeLst);
        this.listView = (ListView) findViewById(R.id.time_lst);
        this.listView.setChoiceMode(1);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: activity.AlarmPlanSettingsActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                AlarmPlanSettingsActivity.this.showSetTimeDialog(i);
            }
        });
        this.listView.setAdapter((ListAdapter) this.myListAdapter);
        this.unbindBtn = (Button) findViewById(R.id.plan_unbind_btn);
        this.bindBtn = (Button) findViewById(R.id.plan_bind_btn);
        this.unbindBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.AlarmPlanSettingsActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AlarmPlanSettingsActivity.this.onPassive();
            }
        });
        this.bindBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.AlarmPlanSettingsActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AlarmPlanSettingsActivity.this.onPositive();
            }
        });
        this.isAllDayCb = (CheckBox) findViewById(R.id.is_all_day_cb);
        this.isAllDayCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.AlarmPlanSettingsActivity.4
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    AlarmPlanSettingsActivity alarmPlanSettingsActivity = AlarmPlanSettingsActivity.this;
                    alarmPlanSettingsActivity.isAllDay = true;
                    alarmPlanSettingsActivity.listView.setEnabled(false);
                    AlarmPlanSettingsActivity.this.myListAdapter.setEnable(false);
                    return;
                }
                AlarmPlanSettingsActivity alarmPlanSettingsActivity2 = AlarmPlanSettingsActivity.this;
                alarmPlanSettingsActivity2.isAllDay = false;
                alarmPlanSettingsActivity2.listView.setEnabled(true);
                AlarmPlanSettingsActivity.this.myListAdapter.setEnable(true);
            }
        });
        initTimeMap();
        initData();
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.AlarmPlanSettingsActivity.5
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                AlarmPlanSettingsActivity.this.finish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initTimeMap() {
        List<TimeSectionForPlan> list = this.timeLst;
        if (list != null && list.size() != 0) {
            this.timeLst.clear();
        }
        for (int i = 0; i < 7; i++) {
            TimeSectionForPlan timeSectionForPlan = new TimeSectionForPlan();
            timeSectionForPlan.setMday(i);
            timeSectionForPlan.setBegin(0);
            timeSectionForPlan.setEnd(86399);
            this.timeLst.add(timeSectionForPlan);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshUI() {
        this.uiHandler.post(new Runnable() { // from class: activity.AlarmPlanSettingsActivity.6
            @Override // java.lang.Runnable
            public void run() {
                AlarmPlanSettingsActivity.this.myListAdapter.notifyDataSetChanged();
            }
        });
    }

    private boolean isIotIdValid() {
        String str = this.iotId;
        return (str == null || "".equals(str)) ? false : true;
    }

    @Override // activity.CommonActivity
    public void initData() {
        if (isIotIdValid()) {
            getAlarmPlan();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        SharePreferenceManager.getInstance().unRegisterOnCallSetListener(this.alarmPlanChangeListener);
    }

    private void getAlarmPlan() {
        getProperties();
    }

    private void getProperties() {
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.AlarmPlanSettingsActivity.8
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
    }

    private void setAlarmPlan() {
        final JSONArray jSONArray = new JSONArray();
        if (this.isAllDay) {
            for (int i = 0; i < 7; i++) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("DayOfWeek", (Object) Integer.valueOf(i));
                jSONObject.put("BeginTime", (Object) 0);
                jSONObject.put("EndTime", (Object) 86399);
                jSONArray.add(jSONObject);
            }
        } else {
            for (int i2 = 0; i2 < this.timeLst.size(); i2++) {
                TimeSectionForPlan timeSectionForPlan = this.timeLst.get(i2);
                if (timeSectionForPlan.isCheck()) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("DayOfWeek", (Object) timeSectionForPlan.getMday());
                    jSONObject2.put("BeginTime", (Object) Integer.valueOf(timeSectionForPlan.getBegin()));
                    jSONObject2.put("EndTime", (Object) Integer.valueOf(timeSectionForPlan.getEnd()));
                    jSONArray.add(jSONObject2);
                }
            }
        }
        HashMap map = new HashMap();
        map.put(Constants.ALARM_NOTIFY_PLAN_MODEL_NAME, jSONArray);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.AlarmPlanSettingsActivity.9
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() == 200) {
                        SharePreferenceManager.getInstance().setAlarmPlan(AlarmPlanSettingsActivity.this.iotId, jSONArray.toString());
                        AlarmPlanSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmPlanSettingsActivity.9.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(AlarmPlanSettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    } else {
                        AlarmPlanSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmPlanSettingsActivity.9.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(AlarmPlanSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"NewApi"})
    public void showSetTimeDialog(int i) {
        TimeSettingDialog.getInstance().openDialog(this, this.timeLst, i, new TimeSettingDialog.DataCallBack() { // from class: activity.AlarmPlanSettingsActivity.10
            @Override // dialog.TimeSettingDialog.DataCallBack
            public void onDataChanged() {
                AlarmPlanSettingsActivity.this.refreshUI();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPositive() {
        setAlarmPlan();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPassive() {
        onPassiveEventPlan();
    }

    private boolean StringIsNullOrEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    private void restoreView() {
        this.uiHandler.post(new Runnable() { // from class: activity.AlarmPlanSettingsActivity.11
            @Override // java.lang.Runnable
            public void run() {
                AlarmPlanSettingsActivity.this.isAllDayCb.setChecked(false);
            }
        });
        initTimeMap();
        refreshUI();
    }

    private void onPassiveEventPlan() {
        final JSONArray jSONArray = new JSONArray();
        HashMap map = new HashMap();
        map.put(Constants.ALARM_NOTIFY_PLAN_MODEL_NAME, jSONArray);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.AlarmPlanSettingsActivity.12
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() == 200) {
                        SharePreferenceManager.getInstance().setAlarmPlan(AlarmPlanSettingsActivity.this.iotId, jSONArray.toString());
                        AlarmPlanSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmPlanSettingsActivity.12.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(AlarmPlanSettingsActivity.this.getActivity(), R.string.remove_succeed, 0).show();
                            }
                        });
                    } else {
                        AlarmPlanSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmPlanSettingsActivity.12.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(AlarmPlanSettingsActivity.this.getActivity(), R.string.remove_failed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
