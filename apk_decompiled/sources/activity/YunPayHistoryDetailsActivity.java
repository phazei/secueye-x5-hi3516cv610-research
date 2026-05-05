package activity;

import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import bean.CloudPayModel;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityYunHistoryDetailsBinding;

/* JADX INFO: loaded from: classes.dex */
public class YunPayHistoryDetailsActivity extends CommonActivity {
    CloudPayModel CloudPayModel;
    private ActivityYunHistoryDetailsBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_yun_history_details;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityYunHistoryDetailsBinding) DataBindingUtil.setContentView(this, R.layout.activity_yun_history_details);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.YunPayHistoryDetailsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                YunPayHistoryDetailsActivity.this.finish();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.CloudPayModel = (CloudPayModel) extras.getSerializable("cloudPayModel");
            this.binding.setModel(this.CloudPayModel);
        }
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        getIntent().getExtras();
    }
}
