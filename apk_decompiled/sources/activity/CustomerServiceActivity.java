package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityCustomerServiceBinding;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class CustomerServiceActivity extends CommonActivity {
    private ActivityCustomerServiceBinding binding;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean nvrDevice;
    private Handler uiHandler = new Handler(Looper.myLooper());

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_customer_service;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityCustomerServiceBinding) DataBindingUtil.setContentView(this, R.layout.activity_customer_service);
        setEdgeToEdge(this.binding.layoutMain);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
        }
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.CustomerServiceActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                CustomerServiceActivity.this.finish();
            }
        });
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
    }
}
