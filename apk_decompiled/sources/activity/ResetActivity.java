package activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityResetBinding;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class ResetActivity extends CommonActivity {
    private ActivityResetBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_reset;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityResetBinding) DataBindingUtil.setContentView(this, R.layout.activity_reset);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.ResetActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ResetActivity.this.finish();
            }
        });
        Log.e("wifi配网", "type=" + getIntent().getStringExtra("type"));
        Log.e("wifi配网", "subType=" + getIntent().getStringExtra("subType"));
        this.binding.btQrCode.setOnClickListener(new OnMultiClickListener() { // from class: activity.ResetActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if ("ble".equals(ResetActivity.this.getIntent().getStringExtra("subType"))) {
                    ResetActivity.this.startActivity(new Intent(ResetActivity.this.getActivity(), (Class<?>) BleScantActivity.class));
                    ResetActivity.this.finish();
                } else {
                    Intent intent = new Intent(ResetActivity.this.getActivity(), (Class<?>) ScanActivity.class);
                    intent.putExtra("type", ResetActivity.this.getIntent().getStringExtra("type"));
                    intent.putExtra("subType", ResetActivity.this.getIntent().getStringExtra("subType"));
                    ResetActivity.this.startActivity(intent);
                    ResetActivity.this.finish();
                }
            }
        });
        this.binding.btSmart.setOnClickListener(new OnMultiClickListener() { // from class: activity.ResetActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(ResetActivity.this.getActivity(), (Class<?>) ScanActivity.class);
                intent.putExtra("type", "scan");
                intent.putExtra("subType", "wifi");
                ResetActivity.this.startActivity(intent);
                ResetActivity.this.finish();
            }
        });
    }
}
