package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityThreeYunSelectBinding;

/* JADX INFO: loaded from: classes.dex */
public class YunThreeSelectActivity extends CommonActivity {
    private ActivityThreeYunSelectBinding binding;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private DeviceInfoBean nvrDevice;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_three_yun_select;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
        }
        this.binding = (ActivityThreeYunSelectBinding) DataBindingUtil.setContentView(this, R.layout.activity_three_yun_select);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.YunThreeSelectActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                YunThreeSelectActivity.this.finish();
            }
        });
        this.binding.gun1.setOnClickListener(new View.OnClickListener() { // from class: activity.YunThreeSelectActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(YunThreeSelectActivity.this.getActivity(), (Class<?>) NvrYunServiceActivity.class);
                intent.putExtra("iotId", YunThreeSelectActivity.this.device1.getIotId());
                intent.putExtra(AlinkConstants.KEY_DN, YunThreeSelectActivity.this.device1.getDeviceName());
                intent.putExtra(AlinkConstants.KEY_PK, YunThreeSelectActivity.this.device1.getProductKey());
                YunThreeSelectActivity.this.startActivity(intent);
            }
        });
        this.binding.gun2.setOnClickListener(new View.OnClickListener() { // from class: activity.YunThreeSelectActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(YunThreeSelectActivity.this.getActivity(), (Class<?>) NvrYunServiceActivity.class);
                intent.putExtra("iotId", YunThreeSelectActivity.this.device2.getIotId());
                intent.putExtra(AlinkConstants.KEY_DN, YunThreeSelectActivity.this.device2.getDeviceName());
                intent.putExtra(AlinkConstants.KEY_PK, YunThreeSelectActivity.this.device2.getProductKey());
                YunThreeSelectActivity.this.startActivity(intent);
            }
        });
        this.binding.ptz.setOnClickListener(new View.OnClickListener() { // from class: activity.YunThreeSelectActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(YunThreeSelectActivity.this.getActivity(), (Class<?>) NvrYunServiceActivity.class);
                intent.putExtra("iotId", YunThreeSelectActivity.this.device.getIotId());
                intent.putExtra(AlinkConstants.KEY_DN, YunThreeSelectActivity.this.device.getDeviceName());
                intent.putExtra(AlinkConstants.KEY_PK, YunThreeSelectActivity.this.device.getProductKey());
                YunThreeSelectActivity.this.startActivity(intent);
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
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
        }
    }
}
