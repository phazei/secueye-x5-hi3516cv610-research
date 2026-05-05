package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityChargeCameraBinding;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class ChargeCameraActivity extends CommonActivity {
    private ActivityChargeCameraBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_charge_camera;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityChargeCameraBinding) DataBindingUtil.setContentView(this, R.layout.activity_charge_camera);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ChargeCameraActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                ChargeCameraActivity.this.finish();
            }
        });
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.gif_power)).into(this.binding.ivGif);
        this.binding.btFeedback.setOnClickListener(new OnMultiClickListener() { // from class: activity.ChargeCameraActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                ChargeCameraActivity chargeCameraActivity = ChargeCameraActivity.this;
                chargeCameraActivity.startActivity(new Intent(chargeCameraActivity.getActivity(), (Class<?>) FeedbackRecordActivity.class));
            }
        });
        this.binding.btNano.setOnClickListener(new OnMultiClickListener() { // from class: activity.ChargeCameraActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                ChargeCameraActivity.this.finish();
            }
        });
    }
}
