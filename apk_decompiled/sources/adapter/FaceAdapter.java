package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemFaceBinding;
import java.util.List;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class FaceAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<String> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int i, String str);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public FaceAdapter(Context context, List<String> list) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemFaceBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_face, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<String> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.tvName.setText(this.itemList.get(i));
        viewHolder.binding.tvDelete.setOnClickListener(new OnMultiClickListener() { // from class: adapter.FaceAdapter.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (FaceAdapter.this.onItemClickListener != null) {
                    FaceAdapter.this.onItemClickListener.onItemClick(i, (String) FaceAdapter.this.itemList.get(i));
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemFaceBinding binding;

        public ViewHolder(ItemFaceBinding itemFaceBinding) {
            super(itemFaceBinding.getRoot());
            this.binding = itemFaceBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
