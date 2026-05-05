package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import bean.MessageBean;
import com.bumptech.glide.Glide;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class MessageInfoAdapter extends PagedListAdapter<MessageBean.EventListBean, MessageInfoAdapterViewHolder> {
    private static DiffUtil.ItemCallback<MessageBean.EventListBean> DIFF_CALLBACK = new DiffUtil.ItemCallback<MessageBean.EventListBean>() { // from class: adapter.MessageInfoAdapter.1
        @Override // androidx.recyclerview.widget.DiffUtil.ItemCallback
        public boolean areItemsTheSame(MessageBean.EventListBean eventListBean, MessageBean.EventListBean eventListBean2) {
            return eventListBean.getEventId().equals(eventListBean2.getEventId());
        }

        @Override // androidx.recyclerview.widget.DiffUtil.ItemCallback
        @SuppressLint({"DiffUtilEquals"})
        public boolean areContentsTheSame(MessageBean.EventListBean eventListBean, @NonNull MessageBean.EventListBean eventListBean2) {
            return eventListBean.equals(eventListBean2);
        }
    };
    Context context;

    public MessageInfoAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public MessageInfoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MessageInfoAdapterViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_message2, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull MessageInfoAdapterViewHolder messageInfoAdapterViewHolder, int i) {
        MessageBean.EventListBean item = getItem(i);
        if (item != null) {
            Glide.with(this.context).load(item.getEventPicUrl()).into(messageInfoAdapterViewHolder.iv_video);
            messageInfoAdapterViewHolder.tv_name.setText(item.getEventNickName());
            messageInfoAdapterViewHolder.tv_time.setText(item.getEventTime());
            messageInfoAdapterViewHolder.tv_desc.setText(item.getEventDesc());
        }
    }

    static class MessageInfoAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_video;
        TextView tv_desc;
        TextView tv_name;
        TextView tv_time;

        public MessageInfoAdapterViewHolder(@NonNull View view2) {
            super(view2);
            this.iv_video = (ImageView) view2.findViewById(R.id.iv_video);
            this.tv_name = (TextView) view2.findViewById(R.id.tv_place);
            this.tv_time = (TextView) view2.findViewById(R.id.tv_name);
            this.tv_desc = (TextView) view2.findViewById(R.id.tv_desc);
        }
    }
}
