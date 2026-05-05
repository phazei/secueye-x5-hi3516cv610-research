package adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bean.CloudRecordBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import view.GlideRoundTransform;

/* JADX INFO: loaded from: classes.dex */
public class CloudPictureAdapter extends BaseQuickAdapter<CloudRecordBean, com.chad.library.adapter.base.BaseViewHolder> {
    private int current;
    private SimpleDateFormat simpleDateFormat;

    public CloudPictureAdapter(int i) {
        super(i);
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.current = 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, CloudRecordBean cloudRecordBean) {
        String str;
        final ImageView imageView = (ImageView) baseViewHolder.getView(R.id.iv_video);
        LinearLayout linearLayout = (LinearLayout) baseViewHolder.getView(R.id.ll_item_view);
        linearLayout.setVisibility(cloudRecordBean.isShow ? 0 : 8);
        int i = this.current;
        if (i == 0 || i == 3 || i == 11500 || i == 11501 || i == 1) {
            linearLayout.setVisibility(0);
        }
        Glide.with(this.mContext).load(cloudRecordBean.thumbUrl).apply(new RequestOptions().transform(new GlideRoundTransform(this.mContext, 4)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.mipmap.default_snap)).into(new SimpleTarget<Drawable>() { // from class: adapter.CloudPictureAdapter.1
            @Override // com.bumptech.glide.request.target.Target
            public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                onResourceReady((Drawable) obj, (Transition<? super Drawable>) transition);
            }

            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                if (drawable != null) {
                    imageView.setImageDrawable(drawable);
                }
            }
        });
        TextView textView = (TextView) baseViewHolder.getView(R.id.tv_name);
        TextView textView2 = (TextView) baseViewHolder.getView(R.id.tv_desc);
        if (TextUtils.isEmpty(cloudRecordBean.picCreateTime) && TextUtils.isEmpty(cloudRecordBean.place)) {
            textView.setVisibility(8);
        } else {
            textView.setVisibility(0);
            String str2 = this.simpleDateFormat.format(new Date(Long.parseLong(cloudRecordBean.picCreateTime)));
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(" ");
            if (cloudRecordBean.place == null) {
                str = "";
            } else {
                str = " " + cloudRecordBean.place;
            }
            sb.append(str);
            textView.setText(sb.toString());
        }
        if (TextUtils.isEmpty(cloudRecordBean.desc)) {
            textView2.setVisibility(8);
        } else {
            textView2.setVisibility(0);
            textView2.setText(cloudRecordBean.desc);
        }
    }

    public void setCurrent(int i) {
        this.current = i;
        notifyDataSetChanged();
    }
}
