package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityNanoSimTips4Binding;
import config.AppConfig;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class NanoSimTips4Activity extends CommonActivity {
    private ActivityNanoSimTips4Binding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_nano_sim_tips4;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityNanoSimTips4Binding) DataBindingUtil.setContentView(this, R.layout.activity_nano_sim_tips4);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.NanoSimTips4Activity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                NanoSimTips4Activity.this.finish();
            }
        });
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.gif_light)).into(this.binding.ivGif);
        this.binding.btFeedback.setOnClickListener(new OnMultiClickListener() { // from class: activity.NanoSimTips4Activity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                NanoSimTips4Activity nanoSimTips4Activity = NanoSimTips4Activity.this;
                nanoSimTips4Activity.startActivity(new Intent(nanoSimTips4Activity.getActivity(), (Class<?>) FeedbackRecordActivity.class));
            }
        });
        this.binding.btOtherSim.setOnClickListener(new OnMultiClickListener() { // from class: activity.NanoSimTips4Activity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (AppConfig.isInternal) {
                    NanoSimTips4Activity nanoSimTips4Activity = NanoSimTips4Activity.this;
                    nanoSimTips4Activity.startActivity(new Intent(nanoSimTips4Activity.getActivity(), (Class<?>) ConnectionFailedActivity.class));
                } else {
                    NanoSimTips4Activity nanoSimTips4Activity2 = NanoSimTips4Activity.this;
                    nanoSimTips4Activity2.startActivity(new Intent(nanoSimTips4Activity2.getActivity(), (Class<?>) OtherSIMCardsActivity.class));
                }
            }
        });
        this.binding.btNano.setOnClickListener(new OnMultiClickListener() { // from class: activity.NanoSimTips4Activity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(NanoSimTips4Activity.this.getActivity(), (Class<?>) ScanActivity.class);
                intent.putExtra("subType", "4g");
                NanoSimTips4Activity.this.startActivity(intent);
                NanoSimTips4Activity.this.finish();
            }
        });
    }
}
