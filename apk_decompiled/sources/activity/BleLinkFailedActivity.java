package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityBleLinkFailedBinding;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class BleLinkFailedActivity extends CommonActivity {
    private ActivityBleLinkFailedBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ble_link_failed;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityBleLinkFailedBinding) DataBindingUtil.setContentView(this, R.layout.activity_ble_link_failed);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.BleLinkFailedActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                BleLinkFailedActivity.this.finish();
            }
        });
        this.binding.btNano.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleLinkFailedActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                BleLinkFailedActivity.this.startActivity(new Intent(BleLinkFailedActivity.this.getActivity(), (Class<?>) BleLinkFailedTips4Activity.class));
                BleLinkFailedActivity.this.finish();
            }
        });
    }
}
