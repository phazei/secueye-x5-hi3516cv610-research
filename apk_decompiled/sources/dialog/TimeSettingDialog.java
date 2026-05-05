package dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import bean.TimeSectionForPlan;
import com.seculink.app.R;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class TimeSettingDialog {

    public interface DataCallBack {
        void onDataChanged();
    }

    private TimeSettingDialog() {
    }

    private static class TimeSettingDialogHolder {

        /* JADX INFO: renamed from: dialog, reason: collision with root package name */
        private static TimeSettingDialog f7876dialog = new TimeSettingDialog();

        private TimeSettingDialogHolder() {
        }
    }

    public static TimeSettingDialog getInstance() {
        return TimeSettingDialogHolder.f7876dialog;
    }

    @SuppressLint({"NewApi"})
    public void openDialog(final Context context, final List<TimeSectionForPlan> list, final int i, final DataCallBack dataCallBack) {
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.time_picker_layout, (ViewGroup) null);
        final TimePicker timePicker = (TimePicker) viewInflate.findViewById(R.id.start_picker);
        final TimePicker timePicker2 = (TimePicker) viewInflate.findViewById(R.id.end_picker);
        timePicker.setIs24HourView(true);
        timePicker2.setIs24HourView(true);
        int begin = list.get(i).getBegin() / 3600;
        int begin2 = (list.get(i).getBegin() / 60) % 60;
        int end = list.get(i).getEnd() / 3600;
        int end2 = (list.get(i).getEnd() / 60) % 60;
        timePicker.setHour(begin);
        timePicker2.setHour(end);
        timePicker.setMinute(begin2);
        timePicker2.setMinute(end2);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, 3);
        builder.setTitle(context.getResources().getString(R.string.choose_time));
        builder.setView(viewInflate);
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), (DialogInterface.OnClickListener) null);
        builder.setPositiveButton(context.getResources().getString(R.string.confirm), (DialogInterface.OnClickListener) null);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.show();
        alertDialogCreate.getButton(-1).setOnClickListener(new View.OnClickListener() { // from class: dialog.TimeSettingDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                int hour;
                int hour2;
                timePicker.clearFocus();
                timePicker2.clearFocus();
                try {
                    if (Build.VERSION.SDK_INT < 23) {
                        hour = (timePicker.getCurrentHour().intValue() * 3600) + (timePicker.getCurrentMinute().intValue() * 60);
                        hour2 = (timePicker2.getCurrentHour().intValue() * 3600) + (timePicker2.getCurrentMinute().intValue() * 60);
                    } else {
                        hour = (timePicker.getHour() * 3600) + (timePicker.getMinute() * 60);
                        hour2 = (timePicker2.getHour() * 3600) + (timePicker2.getMinute() * 60);
                    }
                    if (TimeSettingDialog.this.checkTimeValid(context, hour, hour2)) {
                        ((TimeSectionForPlan) list.get(i)).setBegin(hour);
                        ((TimeSectionForPlan) list.get(i)).setEnd(hour2);
                        dataCallBack.onDataChanged();
                        alertDialogCreate.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean checkTimeValid(Context context, int i, int i2) {
        if (i < 0 || i > 86399 || i2 < 0 || i2 > 86399) {
            Toast.makeText(context, R.string.time_no_valid, 1).show();
            return false;
        }
        if (i <= i2) {
            return true;
        }
        Toast.makeText(context, R.string.time_no_valid, 1).show();
        return false;
    }
}
