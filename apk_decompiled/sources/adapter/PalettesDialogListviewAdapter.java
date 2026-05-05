package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import bean.PalettesDialogBean;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.List;
import tools.ColorTools;
import view.CircleView;

/* JADX INFO: loaded from: classes.dex */
public class PalettesDialogListviewAdapter extends android.widget.BaseAdapter {
    private List<PalettesDialogBean> arr;
    private String color = "";
    private Context context;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public PalettesDialogListviewAdapter(Context context) {
        this.arr = new ArrayList();
        this.context = context;
        this.arr = ColorTools.getColorData(context);
    }

    public List<PalettesDialogBean> getArr() {
        return this.arr;
    }

    public void setSelectPos(String str) {
        this.color = str;
        Log.d("PalettesDialogListviewA", str);
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.arr.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.arr.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view2, ViewGroup viewGroup) {
        View viewInflate = LayoutInflater.from(this.context).inflate(R.layout.item_palettes_dialog, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.item_palettes_title);
        CircleView circleView = (CircleView) viewInflate.findViewById(R.id.item_palettes_circleview);
        LinearLayout linearLayout = (LinearLayout) viewInflate.findViewById(R.id.item_palettes_ll);
        textView.setText(this.arr.get(i).getTitle());
        circleView.setColor(this.arr.get(i).getColor());
        if (this.arr.get(i).getColor().equals(this.color)) {
            linearLayout.setBackgroundResource(R.drawable.shape_lamps_hsvdialog_select_bg);
        } else {
            linearLayout.setBackgroundColor(-1);
        }
        return viewInflate;
    }
}
