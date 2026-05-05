package activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class CommonRouterSettingActivity extends CommonActivity implements View.OnClickListener {
    TextView asus_router;
    TextView d_link_router;
    LinearLayout layout_main;
    TextView netgear_text;
    TextView routerTips;
    TextView tp_link_router;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.router_setting_methods_layout;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.routerTips = (TextView) findViewById(R.id.router_setting_tips);
        this.netgear_text = (TextView) findViewById(R.id.netgear_text);
        this.asus_router = (TextView) findViewById(R.id.asus_router);
        this.d_link_router = (TextView) findViewById(R.id.d_link_router);
        this.tp_link_router = (TextView) findViewById(R.id.tp_link_router);
        this.netgear_text.setOnClickListener(this);
        this.asus_router.setOnClickListener(this);
        this.d_link_router.setOnClickListener(this);
        this.tp_link_router.setOnClickListener(this);
        this.routerTips.setText(Html.fromHtml(getResources().getString(R.string.router_setting_tips)));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        int id = view2.getId();
        if (id == R.id.asus_router) {
            Intent intent = new Intent(this, (Class<?>) RouterSettingActivity.class);
            intent.putExtra(RequestParameters.POSITION, 1);
            startActivity(intent);
            return;
        }
        if (id == R.id.d_link_router) {
            Intent intent2 = new Intent(this, (Class<?>) RouterSettingActivity.class);
            intent2.putExtra(RequestParameters.POSITION, 2);
            startActivity(intent2);
        } else if (id == R.id.netgear_text) {
            Intent intent3 = new Intent(this, (Class<?>) RouterSettingActivity.class);
            intent3.putExtra(RequestParameters.POSITION, 0);
            startActivity(intent3);
        } else {
            if (id != R.id.tp_link_router) {
                return;
            }
            Intent intent4 = new Intent(this, (Class<?>) RouterSettingActivity.class);
            intent4.putExtra(RequestParameters.POSITION, 3);
            startActivity(intent4);
        }
    }
}
