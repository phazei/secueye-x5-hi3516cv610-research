package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityPushHelpBinding;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class PushHelpActivity extends CommonActivity {
    private ActivityPushHelpBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_push_help;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityPushHelpBinding) DataBindingUtil.setContentView(this, R.layout.activity_push_help);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.PushHelpActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                PushHelpActivity.this.finish();
            }
        });
        this.binding.layoutHuawei.setOnClickListener(new OnMultiClickListener() { // from class: activity.PushHelpActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                PushHelpActivity pushHelpActivity = PushHelpActivity.this;
                pushHelpActivity.startActivity(new Intent(pushHelpActivity, (Class<?>) PushHuaweiActivity.class));
            }
        });
        this.binding.layoutMi.setOnClickListener(new OnMultiClickListener() { // from class: activity.PushHelpActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                PushHelpActivity pushHelpActivity = PushHelpActivity.this;
                pushHelpActivity.startActivity(new Intent(pushHelpActivity, (Class<?>) PushMiActivity.class));
            }
        });
        this.binding.layoutOppo.setOnClickListener(new OnMultiClickListener() { // from class: activity.PushHelpActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                PushHelpActivity pushHelpActivity = PushHelpActivity.this;
                pushHelpActivity.startActivity(new Intent(pushHelpActivity, (Class<?>) PushOppoActivity.class));
            }
        });
        this.binding.layoutVivo.setOnClickListener(new OnMultiClickListener() { // from class: activity.PushHelpActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                PushHelpActivity pushHelpActivity = PushHelpActivity.this;
                pushHelpActivity.startActivity(new Intent(pushHelpActivity, (Class<?>) PushVivoActivity.class));
            }
        });
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
    }
}
