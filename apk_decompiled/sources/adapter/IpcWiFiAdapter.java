package adapter;

import android.widget.TextView;
import androidx.annotation.Nullable;
import bean.WifiBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class IpcWiFiAdapter extends BaseQuickAdapter<WifiBean, com.chad.library.adapter.base.BaseViewHolder> {
    public IpcWiFiAdapter(int i) {
        super(i);
    }

    public IpcWiFiAdapter(int i, @Nullable List<WifiBean> list) {
        super(i, list);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, WifiBean wifiBean) {
        baseViewHolder.setText(R.id.tv_ssid, wifiBean.getSsid()).setVisible(R.id.tv_connect, wifiBean.isCurrentWifi());
        if (wifiBean.isCurrentWifi()) {
            ((TextView) baseViewHolder.getView(R.id.tv_ssid)).setTextColor(this.mContext.getResources().getColor(R.color.colorAccent));
        } else {
            ((TextView) baseViewHolder.getView(R.id.tv_ssid)).setTextColor(this.mContext.getResources().getColor(R.color.color_text));
        }
    }
}
