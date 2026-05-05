package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityNvrYunSelectBinding;

/* JADX INFO: loaded from: classes.dex */
public class YunDoubleSelectActivity extends CommonActivity {
    private ActivityNvrYunSelectBinding binding;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean nvrDevice;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_nvr_yun_select;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
        }
        this.binding = (ActivityNvrYunSelectBinding) DataBindingUtil.setContentView(this, R.layout.activity_nvr_yun_select);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.YunDoubleSelectActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                YunDoubleSelectActivity.this.finish();
            }
        });
        this.binding.gun.setOnClickListener(new View.OnClickListener() { // from class: activity.YunDoubleSelectActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(YunDoubleSelectActivity.this.getActivity(), (Class<?>) NvrYunServiceActivity.class);
                intent.putExtra("iotId", YunDoubleSelectActivity.this.device1.getIotId());
                intent.putExtra(AlinkConstants.KEY_DN, YunDoubleSelectActivity.this.device1.getDeviceName());
                intent.putExtra(AlinkConstants.KEY_PK, YunDoubleSelectActivity.this.device1.getProductKey());
                YunDoubleSelectActivity.this.startActivity(intent);
            }
        });
        this.binding.ptz.setOnClickListener(new View.OnClickListener() { // from class: activity.YunDoubleSelectActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(YunDoubleSelectActivity.this.getActivity(), (Class<?>) NvrYunServiceActivity.class);
                intent.putExtra("iotId", YunDoubleSelectActivity.this.device.getIotId());
                intent.putExtra(AlinkConstants.KEY_DN, YunDoubleSelectActivity.this.device.getDeviceName());
                intent.putExtra(AlinkConstants.KEY_PK, YunDoubleSelectActivity.this.device.getProductKey());
                YunDoubleSelectActivity.this.startActivity(intent);
            }
        });
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
        }
    }
}
