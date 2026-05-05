package activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import bean.Device;
import bluetooth.bind.DeviceBindBusiness;
import bluetooth.bind.OnBindDeviceCompletedListener;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.framework.AActivity;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class BindAndUseActivity extends AActivity {
    private Button bindAndUseBtn;
    private DeviceBindBusiness deviceBindBusiness;
    private Button mBackBtn;
    private String TAG = BindAndUseActivity.class.getSimpleName();
    private Handler mHandler = new Handler();

    @Override // com.aliyun.iot.aep.sdk.framework.AActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.bind_and_use_activity);
        this.deviceBindBusiness = new DeviceBindBusiness();
        this.mBackBtn = (Button) findViewById(R.id.ilop_bind_back_btn);
        this.mBackBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.BindAndUseActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                BindAndUseActivity.this.finish();
            }
        });
        String string = "";
        String string2 = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            string = extras.getString("productKey");
            string2 = extras.getString("deviceName");
        }
        final Device device = new Device();
        device.pk = string;
        device.dn = string2;
        Log.e(this.TAG, "onCreate: " + string + "   " + string2);
        this.deviceBindBusiness.queryProductInfo(device);
        this.bindAndUseBtn = (Button) findViewById(R.id.bind_and_use_btn);
        this.bindAndUseBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.BindAndUseActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                BindAndUseActivity.this.deviceBindBusiness.bindDevice(device, new OnBindDeviceCompletedListener() { // from class: activity.BindAndUseActivity.2.1
                    @Override // bluetooth.bind.OnBindDeviceCompletedListener
                    public void onSuccess(String str) {
                        Router.getInstance().toUrl(BindAndUseActivity.this, "page/ilopmain");
                        BindAndUseActivity.this.finish();
                    }

                    @Override // bluetooth.bind.OnBindDeviceCompletedListener
                    public void onFailed(Exception exc) {
                        ALog.e("TAG", "bindDevice onFail s = " + exc);
                        Toast.makeText(BindAndUseActivity.this.getApplicationContext(), "bindDeviceFailed", 0).show();
                    }

                    @Override // bluetooth.bind.OnBindDeviceCompletedListener
                    public void onFailed(int i, String str, String str2) {
                        ALog.d("TAG", "onFailure");
                        Toast.makeText(BindAndUseActivity.this.getApplicationContext(), "code = " + i + " msg =" + str, 0).show();
                    }
                });
            }
        });
    }
}
