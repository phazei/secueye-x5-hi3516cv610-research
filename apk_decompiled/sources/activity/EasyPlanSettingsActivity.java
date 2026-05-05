package activity;

import adapter.MyListAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import bean.RecordPlanResponse;
import bean.TimeSection;
import bean.TimeSectionForPlan;
import com.alibaba.fastjson.JSON;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.seculink.app.R;
import dialog.TimeSettingDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import sdk.IPCManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class EasyPlanSettingsActivity extends Activity {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreData() {
        this.isAllDay = false;
        this.hasPlan = false;
        this.templateId = "";
        this.templateName = "";
        this.planId = "";
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.uiHandler = new Handler(getMainLooper());
        setContentView(R.layout.activity_easy_plan_settings);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.iotId = getIntent().getStringExtra("iotId");
        initView();
        refreshUI();
    }

    public void setEdgeToEdge(View view2) {
        ViewCompat.setOnApplyWindowInsetsListener(view2, new OnApplyWindowInsetsListener() { // from class: activity.-$$Lambda$EasyPlanSettingsActivity$SOIeqDGMtc2waMzEwXPJtJCxj4g
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view3, WindowInsetsCompat windowInsetsCompat) {
                return EasyPlanSettingsActivity.lambda$setEdgeToEdge$0(view3, windowInsetsCompat);
            }
        });
    }

    static /* synthetic */ WindowInsetsCompat lambda$setEdgeToEdge$0(View view2, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
        view2.setPadding(insets.left, view2.getPaddingTop(), insets.right, insets.bottom);
        return WindowInsetsCompat.CONSUMED;
    }

    private void initView() {
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.myListAdapter = new MyListAdapter(this, this.timeLst);
        this.listView = (ListView) findViewById(R.id.time_lst);
        this.listView.setChoiceMode(1);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: activity.EasyPlanSettingsActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                EasyPlanSettingsActivity.this.showSetTimeDialog(i);
            }
        });
        this.listView.setAdapter((ListAdapter) this.myListAdapter);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.EasyPlanSettingsActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                EasyPlanSettingsActivity.this.finish();
            }
        });
        this.unbindBtn = (Button) findViewById(R.id.plan_unbind_btn);
        this.bindBtn = (Button) findViewById(R.id.plan_bind_btn);
        this.unbindBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.EasyPlanSettingsActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                EasyPlanSettingsActivity.this.onPassive();
            }
        });
        this.bindBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.EasyPlanSettingsActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                EasyPlanSettingsActivity.this.onPositive();
            }
        });
        this.isAllDayCb = (CheckBox) findViewById(R.id.is_all_day_cb);
        this.isAllDayCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.EasyPlanSettingsActivity.5
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    EasyPlanSettingsActivity easyPlanSettingsActivity = EasyPlanSettingsActivity.this;
                    easyPlanSettingsActivity.isAllDay = true;
                    easyPlanSettingsActivity.listView.setEnabled(false);
                } else {
                    EasyPlanSettingsActivity easyPlanSettingsActivity2 = EasyPlanSettingsActivity.this;
                    easyPlanSettingsActivity2.isAllDay = false;
                    easyPlanSettingsActivity2.listView.setEnabled(true);
                }
            }
        });
        initTimeMap();
        initData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showToast(final String str) {
        this.uiHandler.post(new Runnable() { // from class: activity.EasyPlanSettingsActivity.6
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(EasyPlanSettingsActivity.this.getApplicationContext(), str, 0).show();
            }
        });
    }

    private void initTimeMap() {
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
        this.uiHandler.post(new Runnable() { // from class: activity.EasyPlanSettingsActivity.7
            @Override // java.lang.Runnable
            public void run() {
                EasyPlanSettingsActivity.this.myListAdapter.notifyDataSetChanged();
            }
        });
    }

    private boolean isIotIdValid() {
        String str = this.iotId;
        return (str == null || "".equals(str)) ? false : true;
    }

    public void initData() {
        if (isIotIdValid()) {
            getEventPlan();
        }
    }

    private void getEventPlan() {
        IPCManager.getInstance().getDevice(this.iotId).getEventRecordPlan2Dev(new IoTCallback() { // from class: activity.EasyPlanSettingsActivity.8
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.e(EasyPlanSettingsActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
                EasyPlanSettingsActivity.this.showToast("" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Log.d(EasyPlanSettingsActivity.this.TAG, "getRecordPlan2Dev     code:" + ioTResponse.getCode() + "      data:[" + ioTResponse.getData() + "]      id:" + ioTResponse.getId());
                if (ioTResponse.getCode() == 200) {
                    if (ioTResponse.getData() == null || "".equals(ioTResponse.getData().toString().trim())) {
                        EasyPlanSettingsActivity.this.hasPlan = false;
                        EasyPlanSettingsActivity.this.showToast("没有对应的事件录像计划   code:" + ioTResponse.getCode());
                        return;
                    }
                    RecordPlanResponse.Data data = (RecordPlanResponse.Data) JSON.parseObject(ioTResponse.getData().toString(), RecordPlanResponse.Data.class);
                    EasyPlanSettingsActivity.this.hasPlan = true;
                    if (!EasyPlanSettingsActivity.this.isNullOrEmpty(data.getTemplateId())) {
                        EasyPlanSettingsActivity.this.templateId = data.getTemplateId();
                        Log.d(EasyPlanSettingsActivity.this.TAG, "templateId:" + EasyPlanSettingsActivity.this.templateId);
                    }
                    if (1 == data.getTimeTemplateDTO().getIsAllDay()) {
                        EasyPlanSettingsActivity.this.setIsAllDayUI(true);
                    } else {
                        EasyPlanSettingsActivity.this.setIsAllDayUI(false);
                        List<TimeSectionForPlan> timeSectionList = data.getTimeTemplateDTO().getTimeSectionList();
                        if (timeSectionList != null && !timeSectionList.isEmpty()) {
                            Collections.sort(timeSectionList);
                            for (TimeSectionForPlan timeSectionForPlan : timeSectionList) {
                                Log.d(EasyPlanSettingsActivity.this.TAG, "mDay:" + timeSectionForPlan.getMday() + "   begin:" + timeSectionForPlan.getBegin() + "   end:" + timeSectionForPlan.getEnd());
                            }
                            EasyPlanSettingsActivity.this.timeLst.clear();
                            EasyPlanSettingsActivity.this.timeLst.addAll(timeSectionList);
                            EasyPlanSettingsActivity.this.refreshUI();
                        }
                    }
                    EasyPlanSettingsActivity easyPlanSettingsActivity = EasyPlanSettingsActivity.this;
                    easyPlanSettingsActivity.showToast(easyPlanSettingsActivity.getResources().getString(R.string.get_success));
                    return;
                }
                EasyPlanSettingsActivity.this.showToast("获取事件录像计划失败    code:" + ioTResponse.getCode());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"NewApi"})
    public void showSetTimeDialog(int i) {
        TimeSettingDialog.getInstance().openDialog(this, this.timeLst, i, new TimeSettingDialog.DataCallBack() { // from class: activity.EasyPlanSettingsActivity.9
            @Override // dialog.TimeSettingDialog.DataCallBack
            public void onDataChanged() {
                EasyPlanSettingsActivity.this.refreshUI();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPositive() {
        onPositiveEventPlan();
    }

    private void onPositiveEventPlan() {
        ArrayList arrayList = new ArrayList();
        if (!this.isAllDay) {
            for (int i = 0; i < 7; i++) {
                TimeSection timeSection = new TimeSection();
                timeSection.setMday(this.timeLst.get(i).getMday().intValue());
                timeSection.setBegin(this.timeLst.get(i).getBegin());
                timeSection.setEnd(this.timeLst.get(i).getEnd());
                arrayList.add(timeSection);
            }
        }
        if (this.hasPlan) {
            IPCManager.getInstance().updateTimeTemplate(this.templateId, this.templateName, this.isAllDay, arrayList, new IoTCallback() { // from class: activity.EasyPlanSettingsActivity.10
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    Log.e(EasyPlanSettingsActivity.this.TAG, "updateTimeTemplate   onFailure    e:" + exc.toString());
                    EasyPlanSettingsActivity.this.showToast("设置失败，e:" + exc.toString());
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    Log.d(EasyPlanSettingsActivity.this.TAG, "updateTimeTemplate   code:" + ioTResponse.getCode() + "      data:" + ioTResponse.getData().toString() + "      id:" + ioTResponse.getId());
                    if (ioTResponse.getCode() == 200) {
                        if (ioTResponse.getData() == null) {
                            EasyPlanSettingsActivity.this.showToast("设置失败,数据为空");
                            return;
                        } else if (((Boolean) ioTResponse.getData()).booleanValue()) {
                            EasyPlanSettingsActivity.this.showToast("设置成功");
                            return;
                        } else {
                            EasyPlanSettingsActivity.this.showToast("设置失败");
                            return;
                        }
                    }
                    EasyPlanSettingsActivity.this.showToast("设置失败,code:" + ioTResponse.getCode());
                }
            });
            return;
        }
        IPCManager.getInstance().createTimeTemplate("T" + System.currentTimeMillis(), this.isAllDay, arrayList, new AnonymousClass11());
    }

    /* JADX INFO: renamed from: activity.EasyPlanSettingsActivity$11, reason: invalid class name */
    class AnonymousClass11 implements IoTCallback {
        AnonymousClass11() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            Log.e(EasyPlanSettingsActivity.this.TAG, "createTimeTemplate   onFailure    e:" + exc.toString());
            EasyPlanSettingsActivity.this.showToast("设置失败   e:" + exc.toString());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            Log.d(EasyPlanSettingsActivity.this.TAG, "createTimeTemplate    code:" + ioTResponse.getCode() + "      data:" + ioTResponse.getData() + "      id:" + ioTResponse.getId());
            if (ioTResponse.getCode() == 200) {
                if (ioTResponse.getData() == null) {
                    EasyPlanSettingsActivity.this.showToast("设置失败,  数据为空");
                    return;
                }
                if ("".equals(ioTResponse.getData().toString().trim())) {
                    return;
                }
                EasyPlanSettingsActivity.this.templateId = ioTResponse.getData().toString().trim();
                IPCManager.getInstance().setEventRecordPlan("T" + System.currentTimeMillis(), "1", 5, 30, EasyPlanSettingsActivity.this.templateId, new IoTCallback() { // from class: activity.EasyPlanSettingsActivity.11.1
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                        Log.e(EasyPlanSettingsActivity.this.TAG, "setRecordPlan   onFailure    e:" + exc.toString());
                        EasyPlanSettingsActivity.this.showToast("设置失败  e:" + exc.toString());
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                        Log.d(EasyPlanSettingsActivity.this.TAG, "setRecordPlan     code:" + ioTResponse2.getCode() + "      data:" + ioTResponse2.getData() + "      id:" + ioTResponse2.getId());
                        if (ioTResponse2.getCode() == 200) {
                            if (ioTResponse2.getData() == null || "".equals(ioTResponse2.getData().toString().trim())) {
                                return;
                            }
                            EasyPlanSettingsActivity.this.planId = ioTResponse2.getData().toString().trim();
                            IPCManager.getInstance().getDevice(EasyPlanSettingsActivity.this.iotId).addEventRecordPlan2Dev(EasyPlanSettingsActivity.this.planId, 0, new IoTCallback() { // from class: activity.EasyPlanSettingsActivity.11.1.1
                                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                                public void onFailure(IoTRequest ioTRequest3, Exception exc) {
                                    Log.e(EasyPlanSettingsActivity.this.TAG, "addRecordPlan2Dev   onFailure    e:" + exc.toString());
                                    EasyPlanSettingsActivity.this.showToast("设置失败   e:" + exc.toString());
                                }

                                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                                public void onResponse(IoTRequest ioTRequest3, IoTResponse ioTResponse3) {
                                    Log.d(EasyPlanSettingsActivity.this.TAG, "addRecordPlan2Dev     code:" + ioTResponse3.getCode() + "      data:" + ioTResponse3.getData() + "      id:" + ioTResponse3.getId());
                                    if (ioTResponse3.getCode() == 200) {
                                        if (((Boolean) ioTResponse3.getData()).booleanValue()) {
                                            EasyPlanSettingsActivity.this.showToast("设置成功");
                                            return;
                                        } else {
                                            EasyPlanSettingsActivity.this.showToast("设置失败");
                                            return;
                                        }
                                    }
                                    EasyPlanSettingsActivity.this.showToast("设置失败   code:" + ioTResponse3.getCode());
                                }
                            });
                            return;
                        }
                        EasyPlanSettingsActivity.this.showToast("设置失败   code:" + ioTResponse2.getCode());
                    }
                });
                return;
            }
            EasyPlanSettingsActivity.this.showToast("设置失败  code:" + ioTResponse.getCode());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIsAllDayUI(final boolean z) {
        runOnUiThread(new Runnable() { // from class: activity.EasyPlanSettingsActivity.12
            @Override // java.lang.Runnable
            public void run() {
                EasyPlanSettingsActivity.this.isAllDayCb.setChecked(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPassive() {
        onPassiveEventPlan();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean StringIsNullOrEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreView() {
        this.uiHandler.post(new Runnable() { // from class: activity.EasyPlanSettingsActivity.13
            @Override // java.lang.Runnable
            public void run() {
                EasyPlanSettingsActivity.this.isAllDayCb.setChecked(false);
            }
        });
        initTimeMap();
        refreshUI();
    }

    private void onPassiveEventPlan() {
        IPCManager.getInstance().getDevice(this.iotId).deleteEventRecordPlan2Dev(0, new IoTCallback() { // from class: activity.EasyPlanSettingsActivity.14
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.e(EasyPlanSettingsActivity.this.TAG, "deleteRecordPlan2Dev :" + exc.toString());
                EasyPlanSettingsActivity.this.showToast("删除事件录像计划与设备关系失败，e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Log.d(EasyPlanSettingsActivity.this.TAG, "deleteRecordPlan2Dev  code:" + ioTResponse.getCode() + "      data:" + ioTResponse.getData() + "      id:" + ioTResponse.getId());
                if (ioTResponse.getCode() == 200 && ioTResponse.getData() != null && !EasyPlanSettingsActivity.this.StringIsNullOrEmpty(ioTResponse.getData().toString())) {
                    EasyPlanSettingsActivity.this.showToast("删除事件录像计划与设备关系成功，data:" + ioTResponse.getData());
                    if (((Boolean) ioTResponse.getData()).booleanValue()) {
                        EasyPlanSettingsActivity.this.restoreView();
                        EasyPlanSettingsActivity.this.restoreData();
                        EasyPlanSettingsActivity.this.showToast("删除事件录像计划与设备关系成功");
                        return;
                    }
                    EasyPlanSettingsActivity.this.showToast("删除事件录像计划与设备关系失败");
                    return;
                }
                EasyPlanSettingsActivity.this.showToast("删除事件录像计划与设备关系失败，  code:" + ioTResponse.getCode() + "      data:" + ioTResponse.getData());
            }
        });
    }
}
