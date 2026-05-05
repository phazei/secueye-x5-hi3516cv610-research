package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityNanoSimTips3Binding;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class NanoSimTips3Activity extends CommonActivity {
    private ActivityNanoSimTips3Binding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_nano_sim_tips3;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityNanoSimTips3Binding) DataBindingUtil.setContentView(this, R.layout.activity_nano_sim_tips3);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.NanoSimTips3Activity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                NanoSimTips3Activity.this.finish();
            }
        });
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.gif_open)).into(this.binding.ivGif);
        this.binding.btFeedback.setOnClickListener(new OnMultiClickListener() { // from class: activity.NanoSimTips3Activity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                NanoSimTips3Activity nanoSimTips3Activity = NanoSimTips3Activity.this;
                nanoSimTips3Activity.startActivity(new Intent(nanoSimTips3Activity.getActivity(), (Class<?>) FeedbackRecordActivity.class));
            }
        });
        this.binding.btPower.setOnClickListener(new OnMultiClickListener() { // from class: activity.NanoSimTips3Activity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                NanoSimTips3Activity nanoSimTips3Activity = NanoSimTips3Activity.this;
                nanoSimTips3Activity.startActivity(new Intent(nanoSimTips3Activity.getActivity(), (Class<?>) ChargeCameraActivity.class));
            }
        });
        this.binding.btNano.setOnClickListener(new OnMultiClickListener() { // from class: activity.NanoSimTips3Activity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                NanoSimTips3Activity nanoSimTips3Activity = NanoSimTips3Activity.this;
                nanoSimTips3Activity.startActivity(new Intent(nanoSimTips3Activity.getActivity(), (Class<?>) NanoSimTips4Activity.class));
                NanoSimTips3Activity.this.finish();
            }
        });
    }
}
