package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.seculink.app.BuildConfig;
import com.seculink.app.R;
import com.xiaomi.mipush.sdk.Constants;
import config.AppConfig;
import tools.Utils;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class AboutAppActivity extends CommonActivity {
    ConstraintLayout about;
    TitleView flTitlebar;
    TextView item_ad;
    LinearLayout llAgreement;
    TextView privacyAgreement;
    TextView tvVersion;
    TextView userAgreement;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_about_app;
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) AboutAppActivity.class));
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.about = (ConstraintLayout) findViewById(R.id.about);
        setEdgeToEdge(this.about);
        this.flTitlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.flTitlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.AboutAppActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                AboutAppActivity.this.finish();
            }
        });
        this.llAgreement = (LinearLayout) findViewById(R.id.ll_agreement);
        this.tvVersion = (TextView) findViewById(R.id.tv_version);
        this.userAgreement = (TextView) findViewById(R.id.user_agreement);
        this.userAgreement.setOnClickListener(new View.OnClickListener() { // from class: activity.AboutAppActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AboutAppActivity aboutAppActivity = AboutAppActivity.this;
                aboutAppActivity.startActivity(new Intent(aboutAppActivity.getActivity(), (Class<?>) AboutUserAgreementActivity.class));
            }
        });
        this.privacyAgreement = (TextView) findViewById(R.id.privacy_agreement);
        this.privacyAgreement.setOnClickListener(new View.OnClickListener() { // from class: activity.AboutAppActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AboutAppActivity aboutAppActivity = AboutAppActivity.this;
                aboutAppActivity.startActivity(new Intent(aboutAppActivity.getActivity(), (Class<?>) PrivacyAgreementActivity.class));
            }
        });
        this.item_ad = (TextView) findViewById(R.id.item_ad);
        this.item_ad.setOnClickListener(new View.OnClickListener() { // from class: activity.AboutAppActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AboutAppActivity aboutAppActivity = AboutAppActivity.this;
                aboutAppActivity.startActivity(new Intent(aboutAppActivity.getActivity(), (Class<?>) ADActivity.class));
            }
        });
        if (AppConfig.isChina) {
            this.llAgreement.setVisibility(0);
        } else {
            this.item_ad.setVisibility(8);
            this.llAgreement.setVisibility(8);
        }
    }

    @Override // activity.CommonActivity
    protected void initData() {
        String str;
        super.initData();
        String versionName = Utils.getVersionName(getActivity());
        if ((getApplicationInfo().flags & 2) != 0) {
            str = versionName + Constants.ACCEPT_TIME_SEPARATOR_SERVER + BuildConfig.debugVersion + "-DEBUG";
        } else {
            str = versionName + Constants.ACCEPT_TIME_SEPARATOR_SERVER + BuildConfig.debugVersion;
        }
        this.tvVersion.setText(getString(R.string.current_version, new Object[]{str}));
    }
}
