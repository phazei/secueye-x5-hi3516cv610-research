package activity;

import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityAccessibilityBinding;
import config.AppConfig;
import receiver.BaseService;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityActivity extends CommonActivity {
    private ActivityAccessibilityBinding binding;
    BaseService instance = BaseService.getInstance();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_accessibility;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityAccessibilityBinding) DataBindingUtil.setContentView(this, R.layout.activity_accessibility);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.AccessibilityActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                AccessibilityActivity.this.finish();
            }
        });
        this.instance.init(this);
        this.binding.tvAccessibility.setOnClickListener(new OnMultiClickListener() { // from class: activity.AccessibilityActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AccessibilityActivity.this.instance.goAccess();
            }
        });
        this.binding.tvBase.setOnClickListener(new OnMultiClickListener() { // from class: activity.AccessibilityActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AppConfig.type = 1;
                BaseService baseService = AccessibilityActivity.this.instance;
                BaseService.toSelfSetting(AccessibilityActivity.this);
            }
        });
        this.binding.tvSuspension.setOnClickListener(new OnMultiClickListener() { // from class: activity.AccessibilityActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AppConfig.type = 2;
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.binding.tvAccessibility.setText("无障碍权限:" + this.instance.checkAccessibilityEnabled(this, "AccessService"));
    }
}
