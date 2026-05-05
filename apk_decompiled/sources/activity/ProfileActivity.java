package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.seculink.app.R;
import fragment.MyAccountTabFragment;
import view.ItemView;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class ProfileActivity extends CommonActivity {
    private ItemView accountItem;
    private LongItemView areaLocationItem;
    private LongItemView connectionLocationItem;
    private ItemView deleteAccountItem;
    ConstraintLayout layout_main;
    private TitleView titleView;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_profile_setting_layout;
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) ProfileActivity.class));
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.titleView = (TitleView) findViewById(R.id.titleView);
        this.accountItem = (ItemView) findViewById(R.id.account);
        this.areaLocationItem = (LongItemView) findViewById(R.id.area_location);
        this.connectionLocationItem = (LongItemView) findViewById(R.id.connection_location);
        this.deleteAccountItem = (ItemView) findViewById(R.id.delete_account);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        this.titleView.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ProfileActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                ProfileActivity.this.finish();
            }
        });
        this.accountItem.setRightText(MyAccountTabFragment.getUserNick());
        this.areaLocationItem.setRightText(GlobalConfig.getInstance().getCountry().areaName);
        this.connectionLocationItem.setRightText(GlobalConfig.getInstance().getCountry().areaName);
        this.deleteAccountItem.setOnClickListener(new View.OnClickListener() { // from class: activity.ProfileActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ProfileActivity.this.startActivity(new Intent(ProfileActivity.this, (Class<?>) DeleteAccountActivity.class));
            }
        });
    }
}
