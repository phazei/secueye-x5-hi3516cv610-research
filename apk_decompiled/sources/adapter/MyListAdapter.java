package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import bean.TimeSectionForPlan;
import com.seculink.app.R;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MyListAdapter extends android.widget.BaseAdapter {
    private Context context;
    private List<TimeSectionForPlan> infoList;
    private boolean isEnable = true;
    private LayoutInflater mInflater;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MyListAdapter(Context context, List<TimeSectionForPlan> list) {
        this.mInflater = LayoutInflater.from(context);
        this.infoList = list;
        this.context = context;
    }

    public void setEnable(boolean z) {
        this.isEnable = z;
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.infoList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.infoList.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view2, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        final TimeSectionForPlan timeSectionForPlan = this.infoList.get(i);
        if (view2 == null) {
            view2 = this.mInflater.inflate(R.layout.time_item_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_day = (TextView) view2.findViewById(R.id.tv_day);
            viewHolder.tv_time = (TextView) view2.findViewById(R.id.tv_time);
            viewHolder.checkBox = (CheckBox) view2.findViewById(R.id.checkbox);
            view2.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view2.getTag();
        }
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: adapter.MyListAdapter.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                timeSectionForPlan.setCheck(z);
            }
        });
        viewHolder.checkBox.setFocusable(false);
        viewHolder.checkBox.setFocusableInTouchMode(false);
        viewHolder.tv_day.setText(changeDay(timeSectionForPlan.getMday().intValue()));
        viewHolder.tv_time.setText(changeTime(timeSectionForPlan.getBegin(), timeSectionForPlan.getEnd()));
        viewHolder.checkBox.setChecked(timeSectionForPlan.isCheck());
        viewHolder.checkBox.setEnabled(this.isEnable);
        return view2;
    }

    private String changeDay(int i) {
        String[] stringArray = this.context.getResources().getStringArray(R.array.day_name);
        switch (i) {
            case 0:
                return stringArray[0];
            case 1:
                return stringArray[1];
            case 2:
                return stringArray[2];
            case 3:
                return stringArray[3];
            case 4:
                return stringArray[4];
            case 5:
                return stringArray[5];
            case 6:
                return stringArray[6];
            default:
                return "";
        }
    }

    private String changeTime(int i, int i2) {
        if (i < 0 || i > 86399 || i2 < 0 || i2 > 86399) {
            return this.context.getResources().getString(R.string.time_no_valid);
        }
        if (i > i2) {
            return this.context.getResources().getString(R.string.time_no_valid);
        }
        return formatTime(i) + " - " + formatTime(i2);
    }

    public String formatTime(int i) {
        int i2;
        StringBuilder sb = new StringBuilder();
        int i3 = 0;
        if (i >= 60) {
            i2 = i / 60;
            int i4 = i % 60;
        } else {
            i2 = 0;
        }
        if (i2 >= 60) {
            i3 = i2 / 60;
            i2 %= 60;
        }
        if (i3 < 10) {
            sb.append("0");
        }
        sb.append(i3);
        sb.append(":");
        if (i2 < 10) {
            sb.append("0");
        }
        sb.append(i2);
        return sb.toString();
    }

    private class ViewHolder {
        CheckBox checkBox;
        TextView tv_day;
        TextView tv_time;

        private ViewHolder() {
        }
    }
}
