package adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bean.EventModel;
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
public class TFPictureAdapter extends BaseQuickAdapter<EventModel, com.chad.library.adapter.base.BaseViewHolder> {
    private int current;
    private SimpleDateFormat simpleDateFormat;
    final SimpleDateFormat timeLineFormatter;

    public TFPictureAdapter(int i) {
        super(i);
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timeLineFormatter = new SimpleDateFormat("mm:ss");
        this.current = 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, EventModel eventModel) {
        final ImageView imageView = (ImageView) baseViewHolder.getView(R.id.iv_video);
        TextView textView = (TextView) baseViewHolder.getView(R.id.tv_file_time);
        LinearLayout linearLayout = (LinearLayout) baseViewHolder.getView(R.id.ll_item_view);
        int i = this.current;
        if (i == 0 || i == 3 || i == 11500 || i == 11501 || i == 1) {
            linearLayout.setVisibility(0);
        }
        Glide.with(this.mContext).load(Integer.valueOf(R.drawable.alarm_msg_bk)).into(new SimpleTarget<Drawable>() { // from class: adapter.TFPictureAdapter.1
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
        if (eventModel != null) {
            String str = "";
            if (!"".equals(eventModel.eventPicThumbUrl)) {
                str = eventModel.eventPicThumbUrl;
            } else if (!"".equals(eventModel.eventPicUrl)) {
                str = eventModel.eventPicUrl;
            } else if (!"".equals(eventModel.thumbUrl)) {
                str = eventModel.thumbUrl;
            } else if (!"".equals(eventModel.picUrl)) {
                str = eventModel.picUrl;
            }
            Glide.with(this.mContext).load(str).apply(new RequestOptions().transform(new GlideRoundTransform(this.mContext, 4)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.mipmap.default_snap)).into(new SimpleTarget<Drawable>() { // from class: adapter.TFPictureAdapter.2
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
        }
        if (eventModel.eventFileTime != 0) {
            textView.setText(this.timeLineFormatter.format(new Date(eventModel.eventFileTime * 1000)) + "");
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
        TextView textView2 = (TextView) baseViewHolder.getView(R.id.tv_name);
        TextView textView3 = (TextView) baseViewHolder.getView(R.id.tv_desc);
        if (TextUtils.isEmpty(eventModel.eventTime) && TextUtils.isEmpty(eventModel.eventTime)) {
            textView2.setVisibility(8);
        } else {
            textView2.setVisibility(0);
            textView2.setText(" " + eventModel.eventTime);
        }
        if (TextUtils.isEmpty(eventModel.eventDesc)) {
            textView3.setVisibility(8);
            return;
        }
        textView3.setVisibility(0);
        int i2 = eventModel.eventType;
        if (i2 == 1) {
            textView3.setText(R.string.motion_detect_alarm);
        }
        switch (i2) {
            case 10005:
                textView3.setText(R.string.car);
                break;
            case 10006:
                textView3.setText(R.string.awaken);
                break;
            default:
                textView3.setText(eventModel.eventDesc);
                break;
        }
    }

    public void setCurrent(int i) {
        this.current = i;
        notifyDataSetChanged();
    }
}
