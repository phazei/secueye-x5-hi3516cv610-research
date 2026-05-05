package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityOtherSimBinding;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class OtherSIMCardsActivity extends CommonActivity {
    private ActivityOtherSimBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_other_sim;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityOtherSimBinding) DataBindingUtil.setContentView(this, R.layout.activity_other_sim);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.OtherSIMCardsActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                OtherSIMCardsActivity.this.finish();
            }
        });
        this.binding.btFeedback.setOnClickListener(new OnMultiClickListener() { // from class: activity.OtherSIMCardsActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                OtherSIMCardsActivity otherSIMCardsActivity = OtherSIMCardsActivity.this;
                otherSIMCardsActivity.startActivity(new Intent(otherSIMCardsActivity.getActivity(), (Class<?>) FeedbackRecordActivity.class));
            }
        });
    }
}
