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
public class AlarmPlanActivity extends CommonActivity {

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    RecyclerAdapter f1564adapter;
    private ImageView add_plan;
    ConstraintLayout layout_main;
    private RecyclerView recycler;
    private TitleView titleView;
    private String iotId = "";

    /* JADX INFO: renamed from: bean, reason: collision with root package name */
    AlarmPlanBean f1565bean = new AlarmPlanBean();
    Boolean isEdit = false;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_alarm_plan_layout;
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
        this.titleView.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.AlarmPlanActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                AlarmPlanActivity.this.finish();
            }

            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
                AlarmPlanActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmPlanActivity.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!AlarmPlanActivity.this.isEdit.booleanValue()) {
                            AlarmPlanActivity.this.isEdit = Boolean.valueOf(!AlarmPlanActivity.this.isEdit.booleanValue());
                            AlarmPlanActivity.this.titleView.setRightIconText(AlarmPlanActivity.this.getString(R.string.cancel));
                            AlarmPlanActivity.this.f1564adapter.notifyDataSetChanged();
                            AlarmPlanActivity.this.f1565bean.setEdit(AlarmPlanActivity.this.isEdit.booleanValue());
                            return;
                        }
                        AlarmPlanActivity.this.isEdit = Boolean.valueOf(!AlarmPlanActivity.this.isEdit.booleanValue());
                        AlarmPlanActivity.this.titleView.setRightIconText(AlarmPlanActivity.this.getString(R.string.edit));
                        AlarmPlanActivity.this.f1564adapter.notifyDataSetChanged();
                        AlarmPlanActivity.this.f1565bean.setEdit(AlarmPlanActivity.this.isEdit.booleanValue());
                    }
                });
            }
        });
        try {
            this.f1565bean = (AlarmPlanBean) new Gson().fromJson(SharePreferenceManager.getInstance().getAlarmPlanJson(this.iotId), AlarmPlanBean.class);
        } catch (Exception unused) {
            this.f1565bean = new AlarmPlanBean();
        }
        AlarmPlanBean alarmPlanBean = this.f1565bean;
        if (alarmPlanBean != null) {
            alarmPlanBean.setIotId(this.iotId);
            this.f1564adapter = new RecyclerAdapter(this.f1565bean, this);
            this.recycler.setLayoutManager(new LinearLayoutManager(this));
            this.recycler.setAdapter(this.f1564adapter);
        }
        this.add_plan.setOnClickListener(new View.OnClickListener() { // from class: activity.AlarmPlanActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (AlarmPlanActivity.this.f1565bean != null && AlarmPlanActivity.this.f1565bean.getList().size() >= 3) {
                    AlarmPlanActivity alarmPlanActivity = AlarmPlanActivity.this;
                    alarmPlanActivity.showToast(alarmPlanActivity.getResources().getString(R.string.max_support));
                } else {
                    Intent intent = new Intent(AlarmPlanActivity.this.getActivity(), (Class<?>) SetAlarmPlanActivity.class);
                    intent.putExtra("bean", new Gson().toJson(AlarmPlanActivity.this.f1565bean, AlarmPlanBean.class));
                    intent.putExtra("isEdit", RequestConstant.FALSE);
                    AlarmPlanActivity.this.startActivity(intent);
                }
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        AlarmPlanBean alarmPlanBean = (AlarmPlanBean) new Gson().fromJson(SharePreferenceManager.getInstance().getAlarmPlanJson(this.iotId), AlarmPlanBean.class);
        AlarmPlanBean alarmPlanBean2 = this.f1565bean;
        if (alarmPlanBean2 != null) {
            alarmPlanBean2.setEdit(false);
            this.f1565bean.setList(alarmPlanBean.getList());
        }
        this.titleView.setRightIconText(getString(R.string.edit));
        RecyclerAdapter recyclerAdapter = this.f1564adapter;
        if (recyclerAdapter != null) {
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    private void setAlarmPlan(String str) {
        HashMap map = new HashMap();
        map.put(Constants.AlarmSchedule, str);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.AlarmPlanActivity.3
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }
}
