package adapter;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import bean.FileBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.seculink.app.R;
import com.xiaomi.mipush.sdk.Constants;
import java.util.List;
import tools.DateUtil;

/* JADX INFO: loaded from: classes.dex */
public class PictureAdapter extends BaseMultiItemQuickAdapter<FileBean, com.chad.library.adapter.base.BaseViewHolder> {
    public PictureAdapter() {
        super(null);
        addItemType(0, R.layout.item_pic_text);
        addItemType(1, R.layout.item_picture);
    }

    public PictureAdapter(@Nullable List<FileBean> list) {
        super(list);
        addItemType(0, R.layout.item_pic_text);
        addItemType(1, R.layout.item_picture);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, FileBean fileBean) {
        switch (baseViewHolder.getItemViewType()) {
            case 0:
                ((TextView) baseViewHolder.getView(R.id.tv_text)).setText(DateUtil.getTimeDes(fileBean.getCreateTime() / 1000, Constants.ACCEPT_TIME_SEPARATOR_SERVER));
                TextView textView = (TextView) baseViewHolder.getView(R.id.tv_tip);
                int size = fileBean.photos.size();
                int size2 = fileBean.videos.size();
                if (size == 0) {
                    textView.setText("(" + size2 + this.mContext.getResources().getString(R.string.video) + ")");
                } else if (size2 == 0) {
                    textView.setText("(" + size + this.mContext.getResources().getString(R.string.photo) + ")");
                } else {
                    textView.setText("(" + size + this.mContext.getResources().getString(R.string.photo) + "," + size2 + this.mContext.getResources().getString(R.string.video) + ")");
                }
                break;
            case 1:
                ImageView imageView = (ImageView) baseViewHolder.getView(R.id.iv_pic);
                Button button = (Button) baseViewHolder.getView(R.id.bt_select);
                Glide.with(this.mContext).load(fileBean.getUrl()).into(imageView);
                button.setVisibility(fileBean.isSelected() ? 0 : 8);
                baseViewHolder.addOnClickListener(R.id.bt_select);
                baseViewHolder.setGone(R.id.iv_record, fileBean.getMediaType() == 2);
                break;
        }
    }
}
