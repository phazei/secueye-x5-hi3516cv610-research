package adapter;

import activity.LocalPhotoDetailsActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bean.MessageBean;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import config.AppConfig;
import java.util.ArrayList;
import java.util.List;
import tools.DateUtil;

/* JADX INFO: loaded from: classes.dex */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapterViewHolder> {
    Context context;
    List<MessageBean.EventListBean> listBeans;
    MessageBean messageBean;

    public MessageAdapter(Context context, List<MessageBean.EventListBean> list) {
        this.listBeans = list;
        this.context = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MessageAdapterViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_message2, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder messageAdapterViewHolder, int i) {
        if (i < this.listBeans.size()) {
            final MessageBean.EventListBean eventListBean = this.listBeans.get(i);
            messageAdapterViewHolder.iv_video.setVisibility(0);
            messageAdapterViewHolder.tv_name.setVisibility(0);
            Glide.with(this.context).load(eventListBean.getEventPicUrl()).into(messageAdapterViewHolder.iv_video);
            messageAdapterViewHolder.tv_name.setText(eventListBean.getEventNickName());
            messageAdapterViewHolder.iv_video.setOnClickListener(new View.OnClickListener() { // from class: adapter.MessageAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(eventListBean.getEventPicUrl());
                    Intent intent = new Intent(MessageAdapter.this.context, (Class<?>) LocalPhotoDetailsActivity.class);
                    intent.putExtra("photos", arrayList);
                    intent.putExtra("pos", 0);
                    intent.putExtra("isHttpUrl", true);
                    MessageAdapter.this.context.startActivity(intent);
                }
            });
            if (!AppConfig.isChina) {
                messageAdapterViewHolder.tv_time.setText(DateUtil.utc2Local(eventListBean.getEventTimeUTC()));
            } else {
                messageAdapterViewHolder.tv_time.setText(eventListBean.getEventTime());
            }
            if (eventListBean.getEventType() == 10006) {
                messageAdapterViewHolder.tv_desc.setText(this.context.getString(R.string.awaken));
            } else {
                messageAdapterViewHolder.tv_desc.setText(eventListBean.getEventDesc());
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.listBeans.size();
    }

    static class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_video;
        TextView tv_desc;
        TextView tv_name;
        TextView tv_time;

        public MessageAdapterViewHolder(@NonNull View view2) {
            super(view2);
            this.iv_video = (ImageView) view2.findViewById(R.id.iv_video);
            this.tv_name = (TextView) view2.findViewById(R.id.tv_place);
            this.tv_time = (TextView) view2.findViewById(R.id.tv_name);
            this.tv_desc = (TextView) view2.findViewById(R.id.tv_desc);
        }
    }
}
