package adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseAdapter extends RecyclerView.Adapter<ViewHolder> {
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public abstract int getItemCount();

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public abstract void onBindViewHolder(@NonNull ViewHolder viewHolder, int i);

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public abstract ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i);

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View view2) {
            super(view2);
        }
    }
}
