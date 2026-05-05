package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.cardview.widget.CardView;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.seculink.app.R;
import dialog.BaseDialog;
import sdk.DemoApplication;
import tools.PreferenceManager;
import tools.SharePreferenceManager;

/* JADX INFO: loaded from: classes.dex */
public class SelectAreaActivity extends CommonActivity implements View.OnClickListener {
    CardView cv_china;
    CardView cv_oversea;
    RelativeLayout layout_main;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_select_area;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.cv_china = (CardView) findViewById(R.id.cv_china);
        this.cv_oversea = (CardView) findViewById(R.id.cv_oversea);
        this.cv_china.setOnClickListener(this);
        this.cv_oversea.setOnClickListener(this);
        findViewById(R.id.fl_titlebar).setOnClickListener(new View.OnClickListener() { // from class: activity.SelectAreaActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SelectAreaActivity.this.finish();
            }
        });
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) SelectAreaActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchToArea() {
        showProgressDialog();
        ((DemoApplication) DemoApplication.getInstance()).stopPushAndDisconnect();
        LoginBusiness.logout(new ILogoutCallback() { // from class: activity.SelectAreaActivity.2
            @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
            public void onLogoutSuccess() {
                SelectAreaActivity.this.dismissProgressDialog();
                SharePreferenceManager.getInstance().clear();
                Intent intent = new Intent(SelectAreaActivity.this.getActivity(), (Class<?>) StartActivity.class);
                intent.addFlags(268468224);
                SelectAreaActivity.this.startActivity(intent);
                System.gc();
                Process.killProcess(Process.myPid());
            }

            @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
            public void onLogoutFailed(int i, String str) {
                SelectAreaActivity.this.dismissProgressDialog();
                SharePreferenceManager.getInstance().clear();
                Intent intent = new Intent(SelectAreaActivity.this.getActivity(), (Class<?>) StartActivity.class);
                intent.addFlags(268468224);
                SelectAreaActivity.this.startActivity(intent);
                System.gc();
                Process.killProcess(Process.myPid());
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(final View view2) {
        new BaseDialog.Builder().view(R.layout.dialog_common).content(getString(R.string.switch_area_need_restart)).leftBtnText(getString(R.string.cancel)).rightBtnText(getString(R.string.confirm)).clickRight(new View.OnClickListener() { // from class: activity.SelectAreaActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                int id = view2.getId();
                if (id == R.id.cv_china) {
                    PreferenceManager.getInstance(SelectAreaActivity.this.getActivity()).attach("RegionFile").put("region", 1);
                    Log.d("switchRegion", "switch to china");
                } else if (id == R.id.cv_oversea) {
                    PreferenceManager.getInstance(SelectAreaActivity.this.getActivity()).attach("RegionFile").put("region", 2);
                    Log.d("switchRegion", "switch to oversea");
                }
                SelectAreaActivity.this.switchToArea();
            }
        }).canCancel(false).create().show(getSupportFragmentManager(), "");
    }
}
