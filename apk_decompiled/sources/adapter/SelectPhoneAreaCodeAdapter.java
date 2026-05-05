package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bean.AreaCodeModel;
import com.seculink.app.R;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import java.util.ArrayList;
import java.util.List;
import tools.Utils;

/* JADX INFO: loaded from: classes.dex */
public class SelectPhoneAreaCodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter {
    private List<AreaCodeModel> dataList = new ArrayList();
    private OnItemClickListener onItemClickListener;
    private int stickHeaderColor;

    public interface OnItemClickListener {
        void onItemClick(AreaCodeModel areaCodeModel);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setStickHeaderColor(int i) {
        this.stickHeaderColor = i;
    }

    public void setDataList(List<AreaCodeModel> list) {
        if (list == null) {
            return;
        }
        this.dataList = list;
        notifyDataSetChanged();
    }

    @Override // com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
    public long getHeaderId(int i) {
        if (this.dataList.size() == 0) {
            return 0L;
        }
        return Utils.getFirstPinYin(this.dataList.get(i).getName()).hashCode();
    }

    @Override // com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return new HeaderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_header, viewGroup, false));
    }

    @Override // com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((HeaderHolder) viewHolder).bindData(Utils.getFirstPinYin(this.dataList.get(i).getName()));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_area_code, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((Holder) viewHolder).bindData(this.dataList.get(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.dataList.size();
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;

        public HeaderHolder(@NonNull View view2) {
            super(view2);
            this.tvHeader = (TextView) view2.findViewById(R.id.tvHeader);
            view2.findViewById(R.id.clParent).setBackgroundColor(view2.getContext().getResources().getColor(SelectPhoneAreaCodeAdapter.this.stickHeaderColor));
        }

        public void bindData(String str) {
            this.tvHeader.setText(str);
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tvArea;
        TextView tvCode;

        public Holder(@NonNull View view2) {
            super(view2);
            this.tvArea = (TextView) view2.findViewById(R.id.tvArea);
            this.tvCode = (TextView) view2.findViewById(R.id.tvCode);
        }

        public void bindData(final AreaCodeModel areaCodeModel) {
            this.tvArea.setText(areaCodeModel.getName());
            this.tvCode.setText("(+" + areaCodeModel.getTel() + ")");
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: adapter.SelectPhoneAreaCodeAdapter.Holder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (SelectPhoneAreaCodeAdapter.this.onItemClickListener == null) {
                        return;
                    }
                    SelectPhoneAreaCodeAdapter.this.onItemClickListener.onItemClick(areaCodeModel);
                }
            });
        }
    }
}
