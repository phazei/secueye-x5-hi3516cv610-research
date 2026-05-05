package adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import bean.APNBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class APNDeviceInfoAdapter extends BaseQuickAdapter<APNBean.bean, com.chad.library.adapter.base.BaseViewHolder> {
    private OnChildClickListener listener;

    public interface OnChildClickListener {
        void click(int i);
    }

    public APNDeviceInfoAdapter(int i) {
        super(i);
    }

    public void setListener(OnChildClickListener onChildClickListener) {
        this.listener = onChildClickListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(final com.chad.library.adapter.base.BaseViewHolder baseViewHolder, APNBean.bean beanVar) {
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.apn_check);
        if (beanVar.getEnable() == 1) {
            imageView.setImageResource(R.drawable.blue_oval);
        } else {
            imageView.setImageResource(R.drawable.gray_oval);
        }
        ((FrameLayout) baseViewHolder.getView(R.id.fl_content_right_apn)).setOnClickListener(new View.OnClickListener() { // from class: adapter.APNDeviceInfoAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                APNDeviceInfoAdapter.this.listener.click(baseViewHolder.getAdapterPosition());
            }
        });
        ((TextView) baseViewHolder.getView(R.id.tv_text_title_apn)).setText(beanVar.getCarrier());
    }
}
