package adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class CameraDeviceInfoAdapter extends BaseQuickAdapter<String, com.chad.library.adapter.base.BaseViewHolder> {
    private OnCheckListener listener;
    private int pos;

    public interface OnCheckListener {
        void onCheck(int i, String str, boolean z);
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int i) {
        this.pos = i;
        notifyDataSetChanged();
    }

    public CameraDeviceInfoAdapter(int i) {
        super(i);
        this.pos = -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(final com.chad.library.adapter.base.BaseViewHolder baseViewHolder, final String str) {
        TextView textView = (TextView) baseViewHolder.getView(R.id.tv_share_number);
        final CheckBox checkBox = (CheckBox) baseViewHolder.getView(R.id.bt_select);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: adapter.CameraDeviceInfoAdapter.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (CameraDeviceInfoAdapter.this.listener != null) {
                    CameraDeviceInfoAdapter.this.listener.onCheck(baseViewHolder.getAdapterPosition(), str, z);
                }
                checkBox.setBackgroundResource(z ? R.drawable.ic_check : R.drawable.ic_sel_all);
            }
        });
        textView.setText(str + "");
        if (baseViewHolder.getLayoutPosition() == this.pos) {
            checkBox.setVisibility(0);
            checkBox.setChecked(true);
        } else {
            checkBox.setVisibility(4);
        }
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.listener = onCheckListener;
    }
}
