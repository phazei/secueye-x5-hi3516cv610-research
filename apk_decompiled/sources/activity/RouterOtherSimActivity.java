package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityRouterOtherSimBinding;
import tools.OnMultiClickListener;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class RouterOtherSimActivity extends CommonActivity {
    private ActivityRouterOtherSimBinding binding;
    private DeviceInfoBean device;
    private String iccid;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_router_other_sim;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.iccid = getIntent().getStringExtra("iccid");
        }
        this.binding = (ActivityRouterOtherSimBinding) DataBindingUtil.setContentView(this, R.layout.activity_router_other_sim);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.RouterOtherSimActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                RouterOtherSimActivity.this.finish();
            }
        });
        this.binding.btFeedback.setOnClickListener(new OnMultiClickListener() { // from class: activity.RouterOtherSimActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                RouterOtherSimActivity routerOtherSimActivity = RouterOtherSimActivity.this;
                routerOtherSimActivity.startActivity(new Intent(routerOtherSimActivity.getActivity(), (Class<?>) FeedbackRecordActivity.class));
            }
        });
        if (!this.iccid.isEmpty()) {
            this.binding.tvIccid.setText(this.iccid);
        }
        this.binding.tvProvider.setText(SharePreferenceManager.getInstance().getCarrier1(this.device.getIotId()));
    }
}
