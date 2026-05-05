package activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityPushMiBinding;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class WXEntryActivity extends CommonActivity {
    private ActivityPushMiBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_push_mi;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityPushMiBinding) DataBindingUtil.setContentView(this, R.layout.activity_push_mi);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.WXEntryActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                WXEntryActivity.this.finish();
            }
        });
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) "1.长按小眯眼APP桌面图标，点击“应用信息”");
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 18, 22, 33);
        this.binding.tvText1.setText(spannableStringBuilder);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
        spannableStringBuilder2.append((CharSequence) "6.返回应用信息页面，点击“省电策略”");
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 14, 18, 33);
        this.binding.tvText6.setText(spannableStringBuilder2);
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
