package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bean.TopicBean;
import com.seculink.app.R;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TopicAdapterDoubleEye extends RecyclerView.Adapter<TopicViewHolder> {
    private int columnCount = 5;
    private Context mContext;
    private List<TopicBean> mData;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onTopicItemClick(TopicBean topicBean, int i);
    }

    public TopicAdapterDoubleEye(Context context, List<TopicBean> list) {
        this.mData = list;
        this.mContext = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public TopicViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TopicViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_topic_double_eye_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @SuppressLint({"ResourceAsColor"})
    public void onBindViewHolder(@NonNull final TopicViewHolder topicViewHolder, int i) {
        final TopicBean topicBean = this.mData.get(i);
        if (topicBean == null) {
            return;
        }
        topicViewHolder.title.setText(topicBean.getTitle());
        topicViewHolder.imageView.setImageResource(topicBean.getIcon() == 0 ? R.drawable.light_ipc : topicBean.getIcon());
        if (topicBean.isSelect()) {
            topicViewHolder.title.setTextColor(Color.parseColor("#1AA0FF"));
        } else {
            topicViewHolder.title.setTextColor(Color.parseColor("#30374e"));
        }
        topicViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: adapter.TopicAdapterDoubleEye.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (TopicAdapterDoubleEye.this.onItemClickListener != null) {
                    TopicAdapterDoubleEye.this.onItemClickListener.onTopicItemClick(topicBean, topicViewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mData.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title;

        public TopicViewHolder(View view2) {
            super(view2);
            this.title = (TextView) view2.findViewById(R.id.title);
            this.imageView = (ImageView) view2.findViewById(R.id.imageView);
        }
    }
}
