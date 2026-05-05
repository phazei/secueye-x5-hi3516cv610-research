package activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityPushOppoBinding;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class PushOppoActivity extends CommonActivity {
    private ActivityPushOppoBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_push_oppo;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityPushOppoBinding) DataBindingUtil.setContentView(this, R.layout.activity_push_oppo);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.PushOppoActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                PushOppoActivity.this.finish();
            }
        });
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) "1.长按小眯眼APP桌面图标，点击“应用信息”");
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 18, 22, 33);
        this.binding.tvText1.setText(spannableStringBuilder);
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
