package activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class WifiScanFailActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout layout_main;
    Button switch_intelligent;
    Button try_button;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_wifi_scan_fail;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.try_button = (Button) findViewById(R.id.try_button);
        this.switch_intelligent = (Button) findViewById(R.id.switch_intelligent);
        this.try_button.setOnClickListener(this);
        this.switch_intelligent.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        int id = view2.getId();
        if (id == R.id.switch_intelligent) {
            setResult(-1);
            finish();
        } else {
            if (id != R.id.try_button) {
                return;
            }
            finish();
        }
    }
}
