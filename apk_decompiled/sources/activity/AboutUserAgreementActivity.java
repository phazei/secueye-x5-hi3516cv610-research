package activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class AboutUserAgreementActivity extends CommonActivity {
    WebView about_webView;
    TitleView flTitlebar;
    LinearLayout layout_main;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.about_user_agreement;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.flTitlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.AboutUserAgreementActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                AboutUserAgreementActivity.this.finish();
            }
        });
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.about_webView = (WebView) findViewById(R.id.about_webView);
        this.about_webView.loadUrl("http://paction.secueye.cn/document.html");
    }
}
