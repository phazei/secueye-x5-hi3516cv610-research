package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import anetwork.channel.util.RequestConstant;
import bean.AlarmPlanBean;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.google.gson.Gson;
import com.seculink.app.R;
import config.Constants;
import java.util.HashMap;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class FloodlightTimeActivity extends CommonActivity {

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    FloodlightAdapter f1571adapter;
    private ImageView add_plan;
    ConstraintLayout layout_main;
    private RecyclerView recycler;
    private TitleView titleView;
    private String iotId = "";

    /* JADX INFO: renamed from: bean, reason: collision with root package name */
    AlarmPlanBean f1572bean = new AlarmPlanBean();
    Boolean isEdit = false;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_floodlight_time_layout;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.titleView = (TitleView) findViewById(R.id.titleView_alarm_plan);
        this.recycler = (RecyclerView) findViewById(R.id.recycler);
        this.add_plan = (ImageView) findViewById(R.id.add_plan);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.iotId = getIntent().getStringExtra("iotId");
        this.titleView.setLineViewId(R.color.color_gray);
        this.titleView.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.FloodlightTimeActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                FloodlightTimeActivity.this.finish();
            }

            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
                FloodlightTimeActivity.this.runOnUiThread(new Runnable() { // from class: activity.FloodlightTimeActivity.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!FloodlightTimeActivity.this.isEdit.booleanValue()) {
                            FloodlightTimeActivity.this.isEdit = Boolean.valueOf(!FloodlightTimeActivity.this.isEdit.booleanValue());
                            FloodlightTimeActivity.this.titleView.setRightIconText(FloodlightTimeActivity.this.getString(R.string.cancel));
                            FloodlightTimeActivity.this.f1571adapter.notifyDataSetChanged();
                            FloodlightTimeActivity.this.f1572bean.setEdit(FloodlightTimeActivity.this.isEdit.booleanValue());
                            return;
                        }
                        FloodlightTimeActivity.this.isEdit = Boolean.valueOf(!FloodlightTimeActivity.this.isEdit.booleanValue());
                        FloodlightTimeActivity.this.titleView.setRightIconText(FloodlightTimeActivity.this.getString(R.string.edit));
                        FloodlightTimeActivity.this.f1571adapter.notifyDataSetChanged();
                        FloodlightTimeActivity.this.f1572bean.setEdit(FloodlightTimeActivity.this.isEdit.booleanValue());
                    }
                });
            }
        });
        try {
            this.f1572bean = (AlarmPlanBean) new Gson().fromJson(SharePreferenceManager.getInstance().getFloodlightScheduleJson(this.iotId), AlarmPlanBean.class);
        } catch (Exception unused) {
            this.f1572bean = new AlarmPlanBean();
        }
        AlarmPlanBean alarmPlanBean = this.f1572bean;
        if (alarmPlanBean != null) {
            alarmPlanBean.setIotId(this.iotId);
            this.f1571adapter = new FloodlightAdapter(this.f1572bean, this);
            this.recycler.setLayoutManager(new LinearLayoutManager(this));
            this.recycler.setAdapter(this.f1571adapter);
        }
        this.add_plan.setOnClickListener(new View.OnClickListener() { // from class: activity.FloodlightTimeActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (FloodlightTimeActivity.this.f1572bean != null && FloodlightTimeActivity.this.f1572bean.getList().size() >= 3) {
                    FloodlightTimeActivity floodlightTimeActivity = FloodlightTimeActivity.this;
                    floodlightTimeActivity.showToast(floodlightTimeActivity.getResources().getString(R.string.max_support));
                } else {
                    Intent intent = new Intent(FloodlightTimeActivity.this.getActivity(), (Class<?>) FloodlightTimeSetActivity.class);
                    intent.putExtra("bean", new Gson().toJson(FloodlightTimeActivity.this.f1572bean, AlarmPlanBean.class));
                    intent.putExtra("isEdit", RequestConstant.FALSE);
                    FloodlightTimeActivity.this.startActivity(intent);
                }
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        AlarmPlanBean alarmPlanBean = (AlarmPlanBean) new Gson().fromJson(SharePreferenceManager.getInstance().getFloodlightScheduleJson(this.iotId), AlarmPlanBean.class);
        AlarmPlanBean alarmPlanBean2 = this.f1572bean;
        if (alarmPlanBean2 != null) {
            alarmPlanBean2.setEdit(false);
            this.f1572bean.setList(alarmPlanBean.getList());
        }
        this.titleView.setRightIconText(getString(R.string.edit));
        FloodlightAdapter floodlightAdapter = this.f1571adapter;
        if (floodlightAdapter != null) {
            floodlightAdapter.notifyDataSetChanged();
        }
    }

    private void setAlarmPlan(String str) {
        HashMap map = new HashMap();
        map.put(Constants.AlarmSchedule, str);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.FloodlightTimeActivity.3
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }
}
