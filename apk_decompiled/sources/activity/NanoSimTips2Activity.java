package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityNanoSimTips2Binding;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class NanoSimTips2Activity extends CommonActivity {
    private ActivityNanoSimTips2Binding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_nano_sim_tips2;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityNanoSimTips2Binding) DataBindingUtil.setContentView(this, R.layout.activity_nano_sim_tips2);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.NanoSimTips2Activity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                NanoSimTips2Activity.this.finish();
            }
        });
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.icon_install_card)).into(this.binding.ivGif);
        this.binding.btFeedback.setOnClickListener(new OnMultiClickListener() { // from class: activity.NanoSimTips2Activity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                NanoSimTips2Activity nanoSimTips2Activity = NanoSimTips2Activity.this;
                nanoSimTips2Activity.startActivity(new Intent(nanoSimTips2Activity.getActivity(), (Class<?>) FeedbackRecordActivity.class));
            }
        });
        this.binding.btNano.setOnClickListener(new OnMultiClickListener() { // from class: activity.NanoSimTips2Activity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                NanoSimTips2Activity nanoSimTips2Activity = NanoSimTips2Activity.this;
                nanoSimTips2Activity.startActivity(new Intent(nanoSimTips2Activity.getActivity(), (Class<?>) NanoSimTips3Activity.class));
                NanoSimTips2Activity.this.finish();
            }
        });
    }
}
