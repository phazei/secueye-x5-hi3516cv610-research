package adapter;

import android.widget.TextView;
import androidx.annotation.Nullable;
import bean.TimeZoneCodeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SelectTimeZoneCodeAdapter extends BaseQuickAdapter<TimeZoneCodeModel, com.chad.library.adapter.base.BaseViewHolder> {
    public SelectTimeZoneCodeAdapter(int i) {
        super(i);
    }

    public SelectTimeZoneCodeAdapter(int i, @Nullable List<TimeZoneCodeModel> list) {
        super(i, list);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, TimeZoneCodeModel timeZoneCodeModel) {
        ((TextView) baseViewHolder.getView(R.id.tvArea)).setText(timeZoneCodeModel.getName());
        ((TextView) baseViewHolder.getView(R.id.tvCode)).setText(timeZoneCodeModel.getCode());
        baseViewHolder.setGone(R.id.iv_check, timeZoneCodeModel.check);
    }
}
