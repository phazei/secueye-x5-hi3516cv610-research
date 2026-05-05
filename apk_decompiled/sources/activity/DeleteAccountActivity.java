package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.seculink.app.R;
import java.util.HashMap;
import sdk.DemoApplication;
import tools.ActivityManager;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class DeleteAccountActivity extends CommonActivity {
    private Button cancelBtn;
    ConstraintLayout layout_main;
    private Button nextBtn;
    private TitleView titleView;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_account_delete_layout;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.titleView = (TitleView) findViewById(R.id.titleView);
        this.nextBtn = (Button) findViewById(R.id.next);
        this.cancelBtn = (Button) findViewById(R.id.cancel);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        this.titleView.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.DeleteAccountActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                DeleteAccountActivity.this.finish();
            }
        });
        this.cancelBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.DeleteAccountActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                DeleteAccountActivity.this.finish();
            }
        });
        this.nextBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.DeleteAccountActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                DeleteAccountActivity.this.showDialog();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteAccount() {
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("account/unregister").setScheme(Scheme.HTTPS).setApiVersion("1.0.6").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(new HashMap()).build(), new IoTCallback() { // from class: activity.DeleteAccountActivity.4
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                DeleteAccountActivity.this.runOnUiThread(new Runnable() { // from class: activity.DeleteAccountActivity.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(DeleteAccountActivity.this, "注销失败！", 0).show();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    DeleteAccountActivity.this.loginOut();
                    DeleteAccountActivity.this.runOnUiThread(new Runnable() { // from class: activity.DeleteAccountActivity.4.3
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(DeleteAccountActivity.this, "注销成功!", 0).show();
                        }
                    });
                } else {
                    DeleteAccountActivity.this.runOnUiThread(new Runnable() { // from class: activity.DeleteAccountActivity.4.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(DeleteAccountActivity.this, "注销失败！", 0).show();
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginOut() {
        ((DemoApplication) DemoApplication.getInstance()).stopPushAndDisconnect();
        SharePreferenceManager.getInstance().clear();
        ActivityManager.getInstance().clear();
        ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
        Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
        intent.setFlags(268468224);
        DemoApplication.getInstance().startActivity(intent);
    }

    public void showDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.unregister_account).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: activity.DeleteAccountActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteAccountActivity.this.deleteAccount();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: activity.DeleteAccountActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }
}
