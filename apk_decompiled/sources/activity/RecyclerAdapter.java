package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import bean.AlarmPlanBean;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.google.gson.Gson;
import com.seculink.app.R;
import com.seculink.app.databinding.RecyclerItemActivityPlanBinding;
import com.xiaomi.mipush.sdk.Constants;
import java.text.DecimalFormat;
import java.util.HashMap;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import tools.TimeUtil;

/* JADX INFO: compiled from: AlarmPlanActivity.java */
/* JADX INFO: loaded from: classes.dex */
class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    /* JADX INFO: renamed from: activity, reason: collision with root package name */
    Activity f1580activity;

    /* JADX INFO: renamed from: bean, reason: collision with root package name */
    private final AlarmPlanBean f1581bean;
    String[] data;

    /* JADX INFO: compiled from: AlarmPlanActivity.java */
    interface setPlan {
        void finish(boolean z);
    }

    public RecyclerAdapter(AlarmPlanBean alarmPlanBean, Activity activity2) {
        this.f1581bean = alarmPlanBean;
        this.f1580activity = activity2;
        this.data = activity2.getResources().getStringArray(R.array.day_name);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(RecyclerItemActivityPlanBinding.inflate(LayoutInflater.from(viewGroup.getContext()), null, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @SuppressLint({"SetTextI18n"})
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.binding.alarmSwitch.setTextOff("");
        viewHolder.binding.alarmSwitch.setTextOn("");
        viewHolder.binding.alarmSwitch.setText("");
        viewHolder.binding.alarmSwitch.setThumbDrawable(null);
        viewHolder.binding.alarmSwitch.setBackgroundResource(R.drawable.sel_switch);
        viewHolder.binding.alarmSwitch.setChecked(this.f1581bean.getList().get(i).isEnable() != 0);
        viewHolder.binding.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.RecyclerAdapter.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                RecyclerAdapter.this.f1581bean.getList().get(viewHolder.getAdapterPosition()).setEnable(z ? 1 : 0);
                RecyclerAdapter recyclerAdapter = RecyclerAdapter.this;
                recyclerAdapter.setAlarmPlan(recyclerAdapter.f1581bean, new setPlan() { // from class: activity.RecyclerAdapter.1.1
                    @Override // activity.RecyclerAdapter.setPlan
                    public void finish(boolean z2) {
                        SharePreferenceManager.getInstance().setAlarmPlanJson(RecyclerAdapter.this.f1581bean.getIotId(), new Gson().toJson(RecyclerAdapter.this.f1581bean));
                    }
                });
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String str = decimalFormat.format(TimeUtil.getHours(this.f1581bean.getList().get(i).getBeginTime())) + ":" + decimalFormat.format(TimeUtil.getMins(this.f1581bean.getList().get(i).getBeginTime()));
        String str2 = decimalFormat.format(TimeUtil.getHours(this.f1581bean.getList().get(i).getEndTime())) + ":" + decimalFormat.format(TimeUtil.getMins(this.f1581bean.getList().get(i).getEndTime()));
        if (this.f1581bean.getList().get(i).isAcrossDay() == 0) {
            viewHolder.binding.time.setText(this.f1580activity.getString(R.string.today) + " " + str + Constants.ACCEPT_TIME_SEPARATOR_SERVER + this.f1580activity.getString(R.string.today) + " " + str2);
        } else {
            viewHolder.binding.time.setText(this.f1580activity.getString(R.string.today) + " " + str + Constants.ACCEPT_TIME_SEPARATOR_SERVER + this.f1580activity.getString(R.string.tomorrow) + " " + str2);
        }
        int weekMask = this.f1581bean.getList().get(i).getWeekMask();
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < 7; i2++) {
            if ((((1 << i2) & weekMask) >> i2) == 1) {
                sb.append(this.data[i2]);
                sb.append("、");
            }
        }
        try {
            if (weekMask == 127) {
                viewHolder.binding.day.setText(this.f1580activity.getString(R.string.everyday));
            } else {
                viewHolder.binding.day.setText(sb.substring(0, sb.length() - 1));
            }
        } catch (Exception unused) {
        }
        viewHolder.binding.recyclerItemDelete.setOnClickListener(new AnonymousClass2(viewHolder));
        viewHolder.binding.recyclerItemEdit.setOnClickListener(new View.OnClickListener() { // from class: activity.RecyclerAdapter.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(RecyclerAdapter.this.f1580activity, (Class<?>) SetAlarmPlanActivity.class);
                intent.putExtra("bean", new Gson().toJson(RecyclerAdapter.this.f1581bean, AlarmPlanBean.class));
                intent.putExtra("isEdit", viewHolder.getAdapterPosition() + "");
                RecyclerAdapter.this.f1580activity.startActivity(intent);
            }
        });
        if (this.f1581bean.isEdit()) {
            viewHolder.binding.recyclerItemDelete.setVisibility(0);
            viewHolder.binding.recyclerItemEdit.setVisibility(0);
        } else {
            viewHolder.binding.recyclerItemDelete.setVisibility(8);
            viewHolder.binding.recyclerItemEdit.setVisibility(8);
        }
    }

    /* JADX INFO: renamed from: activity.RecyclerAdapter$2, reason: invalid class name */
    /* JADX INFO: compiled from: AlarmPlanActivity.java */
    class AnonymousClass2 implements View.OnClickListener {
        final /* synthetic */ ViewHolder val$viewHolder;

        AnonymousClass2(ViewHolder viewHolder) {
            this.val$viewHolder = viewHolder;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            RecyclerAdapter.this.f1581bean.getList().remove(this.val$viewHolder.getAdapterPosition());
            RecyclerAdapter recyclerAdapter = RecyclerAdapter.this;
            recyclerAdapter.setAlarmPlan(recyclerAdapter.f1581bean, new setPlan() { // from class: activity.RecyclerAdapter.2.1
                @Override // activity.RecyclerAdapter.setPlan
                public void finish(boolean z) {
                    RecyclerAdapter.this.f1580activity.runOnUiThread(new Runnable() { // from class: activity.RecyclerAdapter.2.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            RecyclerAdapter.this.notifyItemRemoved(AnonymousClass2.this.val$viewHolder.getAdapterPosition());
                            SharePreferenceManager.getInstance().setAlarmPlanJson(RecyclerAdapter.this.f1581bean.getIotId(), new Gson().toJson(RecyclerAdapter.this.f1581bean));
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAlarmPlan(AlarmPlanBean alarmPlanBean, final setPlan setplan) {
        HashMap map = new HashMap();
        map.put(config.Constants.AlarmSchedule, alarmPlanBean.getList());
        IPCManager.getInstance().getDevice(this.f1581bean.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.RecyclerAdapter.4
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                setplan.finish(z);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.f1581bean.getList().size();
    }

    /* JADX INFO: compiled from: AlarmPlanActivity.java */
    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerItemActivityPlanBinding binding;

        public ViewHolder(@NonNull RecyclerItemActivityPlanBinding recyclerItemActivityPlanBinding) {
            super(recyclerItemActivityPlanBinding.getRoot());
            this.binding = recyclerItemActivityPlanBinding;
        }
    }
}
