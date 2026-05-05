package activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityPushVivoBinding;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class PushVivoActivity extends CommonActivity {
    private ActivityPushVivoBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_push_vivo;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityPushVivoBinding) DataBindingUtil.setContentView(this, R.layout.activity_push_vivo);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.PushVivoActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                PushVivoActivity.this.finish();
            }
        });
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) "1.长按小眯眼APP桌面图标，点击“应用信息”");
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 18, 22, 33);
        this.binding.tvText1.setText(spannableStringBuilder);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
        spannableStringBuilder2.append((CharSequence) "3.请按下图设置推送权限，然后点击“iot-push”");
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 18, 26, 33);
        this.binding.tvText3.setText(spannableStringBuilder2);
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
