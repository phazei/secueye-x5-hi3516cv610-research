package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityBleLinkFailedTips4Binding;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class BleLinkFailedTips4Activity extends CommonActivity {
    private ActivityBleLinkFailedTips4Binding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ble_link_failed_tips4;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityBleLinkFailedTips4Binding) DataBindingUtil.setContentView(this, R.layout.activity_ble_link_failed_tips4);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.BleLinkFailedTips4Activity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                BleLinkFailedTips4Activity.this.finish();
            }
        });
        this.binding.btFeedback.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleLinkFailedTips4Activity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                BleLinkFailedTips4Activity bleLinkFailedTips4Activity = BleLinkFailedTips4Activity.this;
                bleLinkFailedTips4Activity.startActivity(new Intent(bleLinkFailedTips4Activity.getActivity(), (Class<?>) FeedbackRecordActivity.class));
            }
        });
        this.binding.btNano.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleLinkFailedTips4Activity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                BleLinkFailedTips4Activity.this.finish();
            }
        });
    }
}
