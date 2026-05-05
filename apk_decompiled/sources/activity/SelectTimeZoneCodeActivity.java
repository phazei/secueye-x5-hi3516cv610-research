package activity;

import adapter.SelectTimeZoneCodeAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bean.TimeZoneCodeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import java.util.List;
import tools.Utils;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class SelectTimeZoneCodeActivity extends CommonActivity {
    private List<TimeZoneCodeModel> datalist;
    private String iotId;
    private LinearLayoutManager layoutManager;
    RelativeLayout layout_main;
    private SelectTimeZoneCodeAdapter mAdapter;
    private String timeZone;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_select_time_zone_code;
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.iotId = intent.getStringExtra("iotId");
        this.timeZone = intent.getStringExtra("TimeZone");
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        ((TitleView) findViewById(R.id.fl_titlebar)).setOnViewClick(new TitleView.OnViewClick() { // from class: activity.SelectTimeZoneCodeActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                SelectTimeZoneCodeActivity.this.finish();
            }
        });
        this.datalist = Utils.readTimeZoneList(this);
        for (TimeZoneCodeModel timeZoneCodeModel : this.datalist) {
            if (timeZoneCodeModel.getCode().equals(this.timeZone)) {
                timeZoneCodeModel.setCheck(true);
            }
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(this.layoutManager);
        this.mAdapter = new SelectTimeZoneCodeAdapter(R.layout.item_time_zone_code, this.datalist);
        this.mAdapter.bindToRecyclerView(recyclerView);
        this.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: activity.SelectTimeZoneCodeActivity.2
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i) {
                Intent intent = new Intent();
                TimeZoneCodeModel timeZoneCodeModel2 = new TimeZoneCodeModel();
                timeZoneCodeModel2.setCode(SelectTimeZoneCodeActivity.this.mAdapter.getData().get(i).getCode());
                intent.putExtra("deviceTimeZone", timeZoneCodeModel2);
                SelectTimeZoneCodeActivity.this.setResult(-1, intent);
                SelectTimeZoneCodeActivity.this.finish();
            }
        });
    }
}
