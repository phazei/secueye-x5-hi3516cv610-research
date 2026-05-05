package adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import bean.MessageInfoBean;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
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
public class MessageAdapter2 extends BaseQuickAdapter<MessageInfoBean, com.chad.library.adapter.base.BaseViewHolder> {
    public MessageAdapter2(int i) {
        super(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, MessageInfoBean messageInfoBean) {
        final ImageView imageView = (ImageView) baseViewHolder.getView(R.id.iv_video);
        RequestOptions requestOptionsError = new RequestOptions().transform(new GlideRoundTransform(this.mContext, 4)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.mipmap.default_snap);
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(messageInfoBean.gmtCreated));
        Glide.with(this.mContext).load(messageInfoBean.picUrl).apply(requestOptionsError).into(new SimpleTarget<Drawable>() { // from class: adapter.MessageAdapter2.1
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
        if (UTConstants.E_SDK_CONNECT_DEVICE_ACTION.equals(messageInfoBean.messageType)) {
            imageView.setVisibility(0);
        } else {
            imageView.setVisibility(8);
        }
        TextView textView = (TextView) baseViewHolder.getView(R.id.tv_name);
        TextView textView2 = (TextView) baseViewHolder.getView(R.id.tv_place);
        TextView textView3 = (TextView) baseViewHolder.getView(R.id.tv_desc);
        if (!TextUtils.isEmpty(str)) {
            textView.setVisibility(0);
            textView.setText(str);
        } else {
            textView.setVisibility(8);
        }
        String str2 = !TextUtils.isEmpty(messageInfoBean.nickName) ? messageInfoBean.nickName : messageInfoBean.productName;
        if (!TextUtils.isEmpty(str2)) {
            textView2.setVisibility(0);
            textView2.setText(str2);
        } else {
            textView2.setVisibility(8);
        }
        if (!TextUtils.isEmpty(messageInfoBean.body)) {
            textView3.setVisibility(0);
            textView3.setText(getRecyclerView().getResources().getString(R.string.mobile_detection));
        } else {
            textView3.setVisibility(8);
        }
    }
}
