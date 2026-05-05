package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivitySelectSimBinding;
import config.AppConfig;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class InsertSelectSimActivity extends CommonActivity {
    private ActivitySelectSimBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_select_sim;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivitySelectSimBinding) DataBindingUtil.setContentView(this, R.layout.activity_select_sim);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.InsertSelectSimActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                InsertSelectSimActivity.this.finish();
            }
        });
        this.binding.btFeedback.setOnClickListener(new OnMultiClickListener() { // from class: activity.InsertSelectSimActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                InsertSelectSimActivity insertSelectSimActivity = InsertSelectSimActivity.this;
                insertSelectSimActivity.startActivity(new Intent(insertSelectSimActivity.getActivity(), (Class<?>) FeedbackRecordActivity.class));
            }
        });
        this.binding.btBuilt.setOnClickListener(new OnMultiClickListener() { // from class: activity.InsertSelectSimActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AppConfig.isInternal = true;
                InsertSelectSimActivity insertSelectSimActivity = InsertSelectSimActivity.this;
                insertSelectSimActivity.startActivity(new Intent(insertSelectSimActivity.getActivity(), (Class<?>) NanoSimTips3Activity.class));
                InsertSelectSimActivity.this.finish();
            }
        });
        this.binding.btNano.setOnClickListener(new OnMultiClickListener() { // from class: activity.InsertSelectSimActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AppConfig.isInternal = false;
                InsertSelectSimActivity insertSelectSimActivity = InsertSelectSimActivity.this;
                insertSelectSimActivity.startActivity(new Intent(insertSelectSimActivity.getActivity(), (Class<?>) NanoSimTips2Activity.class));
                InsertSelectSimActivity.this.finish();
            }
        });
    }
}
