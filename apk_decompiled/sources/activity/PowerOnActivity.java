package activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityPowerOnBinding;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class PowerOnActivity extends CommonActivity {
    private ActivityPowerOnBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_power_on;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityPowerOnBinding) DataBindingUtil.setContentView(this, R.layout.activity_power_on);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.PowerOnActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PowerOnActivity.this.finish();
            }
        });
        this.binding.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.PowerOnActivity.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                PowerOnActivity.this.binding.btNext.setSelected(z);
            }
        });
        Log.e("wifi配网", "type=" + getIntent().getStringExtra("type"));
        Log.e("wifi配网", "subType=" + getIntent().getStringExtra("subType"));
        this.binding.btNext.setOnClickListener(new OnMultiClickListener() { // from class: activity.PowerOnActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(PowerOnActivity.this.getActivity(), (Class<?>) ResetActivity.class);
                intent.putExtra("type", PowerOnActivity.this.getIntent().getStringExtra("type"));
                intent.putExtra("subType", PowerOnActivity.this.getIntent().getStringExtra("subType"));
                PowerOnActivity.this.startActivity(intent);
                PowerOnActivity.this.finish();
            }
        });
        this.binding.ivGif.setImageResource(R.drawable.icon_power_on);
    }
}
