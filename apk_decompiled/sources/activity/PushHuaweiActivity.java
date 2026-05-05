package activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityPushHuaweiBinding;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class PushHuaweiActivity extends CommonActivity {
    private ActivityPushHuaweiBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_push_huawei;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityPushHuaweiBinding) DataBindingUtil.setContentView(this, R.layout.activity_push_huawei);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.PushHuaweiActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                PushHuaweiActivity.this.finish();
            }
        });
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) "1.进入华为手机“设置”页面，点击“应用和服务”");
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 9, 11, 33);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 18, 23, 33);
        this.binding.tvText1.setText(spannableStringBuilder);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
        spannableStringBuilder2.append((CharSequence) "7.返回“应用和服务”页面，点击“应用启动管理”");
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 5, 10, 33);
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 17, 23, 33);
        this.binding.tvText7.setText(spannableStringBuilder2);
        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder();
        spannableStringBuilder3.append((CharSequence) "8.关闭“小眯眼”App自动管理，并按下图方法设置");
        spannableStringBuilder3.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 5, 8, 33);
        this.binding.tvText8.setText(spannableStringBuilder3);
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
