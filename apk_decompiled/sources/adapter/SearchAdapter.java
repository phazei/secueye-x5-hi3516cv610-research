package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bean.SearchModel;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemSearchBinding;
import java.text.SimpleDateFormat;
import java.util.List;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class SearchAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<SearchModel> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final SimpleDateFormat timeLineFormatter = new SimpleDateFormat("mm:ss");

    public interface OnItemClickListener {
        void onItemClick(int i, SearchModel searchModel);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public SearchAdapter(Context context, List<SearchModel> list) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemSearchBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_search, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<SearchModel> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.tvTime.setText(this.itemList.get(i).Message + "");
        viewHolder.binding.layoutItem.setOnClickListener(new OnMultiClickListener() { // from class: adapter.SearchAdapter.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (SearchAdapter.this.onItemClickListener != null) {
                    SearchAdapter.this.onItemClickListener.onItemClick(i, (SearchModel) SearchAdapter.this.itemList.get(i));
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemSearchBinding binding;

        public ViewHolder(ItemSearchBinding itemSearchBinding) {
            super(itemSearchBinding.getRoot());
            this.binding = itemSearchBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
