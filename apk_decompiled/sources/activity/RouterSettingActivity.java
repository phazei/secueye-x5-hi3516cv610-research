package activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class RouterSettingActivity extends CommonActivity {
    LinearLayout layout_main;
    private int position;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        int i = this.position;
        if (i == 0) {
            return R.layout.router_one_layout;
        }
        if (i == 1) {
            return R.layout.router_two_layout;
        }
        if (i == 2) {
            return R.layout.router_three_layout;
        }
        if (i == 3) {
            return R.layout.router_four_layout;
        }
        return 0;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.position = intent.getIntExtra(RequestParameters.POSITION, 0);
        return super.initArgs(intent);
    }
}
