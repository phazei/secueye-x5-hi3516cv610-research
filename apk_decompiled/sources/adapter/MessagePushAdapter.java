package adapter;

import android.widget.TextView;
import bean.MessageInfoBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class MessagePushAdapter extends BaseQuickAdapter<MessageInfoBean, com.chad.library.adapter.base.BaseViewHolder> {
    public MessagePushAdapter(int i) {
        super(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, MessageInfoBean messageInfoBean) {
        TextView textView = (TextView) baseViewHolder.getView(R.id.tv_title);
        TextView textView2 = (TextView) baseViewHolder.getView(R.id.tv_body);
        TextView textView3 = (TextView) baseViewHolder.getView(R.id.date);
        TextView textView4 = (TextView) baseViewHolder.getView(R.id.time);
        textView.setText(messageInfoBean.title);
        textView2.setText(messageInfoBean.body);
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(messageInfoBean.gmtModified));
        textView3.setText(str.substring(0, 10));
        textView4.setText(str.substring(11));
    }
}
