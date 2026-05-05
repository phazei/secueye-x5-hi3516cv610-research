package activity;

import adapter.YunPayAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import bean.CloudPayModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityNvrYunHistoryBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/* JADX INFO: loaded from: classes.dex */
public class NvrYunPayHistoryActivity extends CommonActivity {
    private ActivityNvrYunHistoryBinding binding;
    CloudPayModel cloudPayModel;
    String iotId = "";
    String dn = "";
    String pk = "";
    List<CloudPayModel> cloudPayModels = new ArrayList();
    private YunPayAdapter.OnItemClickListener adapterListener = new YunPayAdapter.OnItemClickListener() { // from class: activity.NvrYunPayHistoryActivity.3
        @Override // adapter.YunPayAdapter.OnItemClickListener
        public void onItemClick(int i) {
            NvrYunPayHistoryActivity nvrYunPayHistoryActivity = NvrYunPayHistoryActivity.this;
            nvrYunPayHistoryActivity.cloudPayModel = nvrYunPayHistoryActivity.cloudPayModels.get(i);
            Intent intent = new Intent(NvrYunPayHistoryActivity.this, (Class<?>) YunPayHistoryDetailsActivity.class);
            intent.putExtra("cloudPayModel", NvrYunPayHistoryActivity.this.cloudPayModel);
            NvrYunPayHistoryActivity.this.startActivity(intent);
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_nvr_yun_history;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityNvrYunHistoryBinding) DataBindingUtil.setContentView(this, R.layout.activity_nvr_yun_history);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.NvrYunPayHistoryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                NvrYunPayHistoryActivity.this.finish();
            }
        });
        this.iotId = getIntent().getStringExtra("iotId");
        this.dn = getIntent().getStringExtra(AlinkConstants.KEY_DN);
        this.pk = getIntent().getStringExtra(AlinkConstants.KEY_PK);
        new OkHttpClient().newCall(new Request.Builder().url("http://www.secueye.cn/monitorCombo/monitor/findComboRechare?iotId=" + this.iotId).get().build()).enqueue(new Callback() { // from class: activity.NvrYunPayHistoryActivity.2
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject object = JSONObject.parseObject(response.body().string());
                if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                    String string = object.getString("data");
                    NvrYunPayHistoryActivity.this.cloudPayModels = JSON.parseArray(string, CloudPayModel.class);
                    if (NvrYunPayHistoryActivity.this.cloudPayModels.size() != 0) {
                        NvrYunPayHistoryActivity.this.runOnUiThread(new Runnable() { // from class: activity.NvrYunPayHistoryActivity.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                NvrYunPayHistoryActivity.this.binding.layoutNoData.setVisibility(8);
                                NvrYunPayHistoryActivity.this.binding.rvList.setVisibility(0);
                                NvrYunPayHistoryActivity.this.binding.rvList.setLayoutManager(new LinearLayoutManager(NvrYunPayHistoryActivity.this));
                                YunPayAdapter yunPayAdapter = new YunPayAdapter(NvrYunPayHistoryActivity.this, NvrYunPayHistoryActivity.this.cloudPayModels);
                                NvrYunPayHistoryActivity.this.binding.rvList.setAdapter(yunPayAdapter);
                                yunPayAdapter.setOnItemClickListener(NvrYunPayHistoryActivity.this.adapterListener);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        if (getIntent().getExtras() != null) {
            this.iotId = getIntent().getStringExtra("iotId");
            this.dn = getIntent().getStringExtra(AlinkConstants.KEY_DN);
            this.pk = getIntent().getStringExtra(AlinkConstants.KEY_PK);
        }
    }
}
