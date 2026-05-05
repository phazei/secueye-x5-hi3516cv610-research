package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityConnectionFailedBinding;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionFailedActivity extends CommonActivity {
    private ActivityConnectionFailedBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_connection_failed;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityConnectionFailedBinding) DataBindingUtil.setContentView(this, R.layout.activity_connection_failed);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ConnectionFailedActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                ConnectionFailedActivity.this.finish();
            }
        });
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.scan_image)).into(this.binding.ivGif);
        this.binding.btFeedback.setOnClickListener(new OnMultiClickListener() { // from class: activity.ConnectionFailedActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                ConnectionFailedActivity connectionFailedActivity = ConnectionFailedActivity.this;
                connectionFailedActivity.startActivity(new Intent(connectionFailedActivity.getActivity(), (Class<?>) FeedbackRecordActivity.class));
            }
        });
        this.binding.btOtherSim.setOnClickListener(new OnMultiClickListener() { // from class: activity.ConnectionFailedActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                ConnectionFailedActivity connectionFailedActivity = ConnectionFailedActivity.this;
                connectionFailedActivity.startActivity(new Intent(connectionFailedActivity.getActivity(), (Class<?>) CountrySupportedActivity.class));
            }
        });
        this.binding.btNano.setOnClickListener(new OnMultiClickListener() { // from class: activity.ConnectionFailedActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(ConnectionFailedActivity.this.getActivity(), (Class<?>) ScanActivity.class);
                intent.putExtra("subType", "4g");
                ConnectionFailedActivity.this.startActivity(intent);
            }
        });
    }
}
