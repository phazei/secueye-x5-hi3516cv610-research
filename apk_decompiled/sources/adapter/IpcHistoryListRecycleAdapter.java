package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import bean.VideoInfo;
import com.seculink.app.R;
import java.util.List;
import tools.TimeUtil;

/* JADX INFO: loaded from: classes.dex */
public class IpcHistoryListRecycleAdapter extends RecyclerView.Adapter<ViewHolder> {
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private TextView footerTextView;
    private View footerView;
    private OnHistoryRecordItemClickedListener onHistoryRecordItemClickedListener;
    private List<VideoInfo> videoList;

    public interface OnHistoryRecordItemClickedListener {
        void onItemClick(String str);

        void onItemLongClick(String str);

        void scrollBottom();
    }

    public IpcHistoryListRecycleAdapter(List<VideoInfo> list) {
        this.videoList = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view2 = this.footerView;
        if (view2 != null && i == 1) {
            return new ViewHolder(view2, 1);
        }
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.ipc_history_video_item, viewGroup, false), 2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (isFooterView(i)) {
            return;
        }
        VideoInfo videoInfo = this.videoList.get(i);
        viewHolder.iotId = videoInfo.iotId;
        viewHolder.fileName = videoInfo.fileName;
        viewHolder.startTime.setText(TimeUtil.TimeStamp2Date(videoInfo.beginTime, "yyyy-MM-dd HH:mm:ss"));
        viewHolder.endTime.setText(TimeUtil.TimeStamp2Date(videoInfo.endTime, "yyyy-MM-dd HH:mm:ss"));
        viewHolder.endTime.setVisibility(0);
        viewHolder.desc.setVisibility(8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return (this.footerView != null && i == getItemCount() - 1) ? 1 : 2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<VideoInfo> list = this.videoList;
        int size = list == null ? 0 : list.size();
        return this.footerView != null ? size + 1 : size;
    }

    private boolean isFooterView(int i) {
        return this.footerView != null && i == getItemCount() - 1;
    }

    public void revertFooterText() {
        TextView textView = this.footerTextView;
        if (textView != null) {
            textView.setText(R.string.btn_footer_more);
        }
    }

    public void setVideoList(List<VideoInfo> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        for (VideoInfo videoInfo : list) {
            if (!list.contains(videoInfo)) {
                this.videoList.add(videoInfo);
            }
        }
    }

    public void setVideoChangeListener(OnHistoryRecordItemClickedListener onHistoryRecordItemClickedListener) {
        this.onHistoryRecordItemClickedListener = onHistoryRecordItemClickedListener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getFooterView() {
        return this.footerView;
    }

    public void setFooterView(View view2) {
        this.footerView = view2;
        notifyItemInserted(getItemCount() - 1);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        TextView endTime;
        String fileName;
        TextView footerText;
        String iotId;
        TextView startTime;
        int type;

        ViewHolder(View view2, int i) {
            super(view2);
            if (2 != i) {
                IpcHistoryListRecycleAdapter.this.footerTextView = (TextView) view2.findViewById(R.id.list_item_video_footer);
                view2.setOnClickListener(new View.OnClickListener() { // from class: adapter.IpcHistoryListRecycleAdapter.ViewHolder.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        IpcHistoryListRecycleAdapter.this.footerTextView.setText(R.string.btn_footer_loading);
                        if (IpcHistoryListRecycleAdapter.this.onHistoryRecordItemClickedListener != null) {
                            IpcHistoryListRecycleAdapter.this.onHistoryRecordItemClickedListener.scrollBottom();
                        }
                    }
                });
                return;
            }
            this.startTime = (TextView) view2.findViewById(R.id.list_item_video_start_tv);
            this.endTime = (TextView) view2.findViewById(R.id.list_item_video_end_tv);
            this.desc = (TextView) view2.findViewById(R.id.list_item_event_desc_tv);
            view2.setOnClickListener(new View.OnClickListener() { // from class: adapter.IpcHistoryListRecycleAdapter.ViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    if (IpcHistoryListRecycleAdapter.this.onHistoryRecordItemClickedListener != null) {
                        IpcHistoryListRecycleAdapter.this.onHistoryRecordItemClickedListener.onItemClick(ViewHolder.this.fileName);
                    }
                }
            });
            view2.setOnLongClickListener(new View.OnLongClickListener() { // from class: adapter.IpcHistoryListRecycleAdapter.ViewHolder.2
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view3) {
                    if (IpcHistoryListRecycleAdapter.this.onHistoryRecordItemClickedListener == null) {
                        return true;
                    }
                    IpcHistoryListRecycleAdapter.this.onHistoryRecordItemClickedListener.onItemLongClick(ViewHolder.this.fileName);
                    return true;
                }
            });
        }
    }
}
