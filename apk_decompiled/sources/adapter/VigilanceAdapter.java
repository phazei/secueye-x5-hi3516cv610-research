package adapter;

import activity.VigilanceActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import bean.AlarmAudioRecordBean;
import bean.VigilanceBean;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.VigilanceRecyclerviewItemBinding;
import config.Constants;
import java.util.HashMap;
import java.util.List;
import sdk.IPCManager;

/* JADX INFO: loaded from: classes.dex */
public class VigilanceAdapter extends RecyclerView.Adapter<ViewHolder> {
    VigilanceActivity context;
    String iotId;
    List<VigilanceBean> list;
    private int mSelectedPos = 1;

    interface callback {
        void onComplete(boolean z);
    }

    public VigilanceAdapter(VigilanceActivity vigilanceActivity, List<VigilanceBean> list, String str) {
        this.context = vigilanceActivity;
        this.list = list;
        this.iotId = str;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(VigilanceRecyclerviewItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), null, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        final String string = this.context.getString(R.string.add_alert);
        if (i >= this.list.size()) {
            return;
        }
        if (this.list.get(i).getType().equals("title")) {
            viewHolder.binding.VigilanceTitle.setVisibility(0);
            viewHolder.binding.vigilanceNameText.setText(R.string.sys_vigilance);
            viewHolder.binding.edit.setVisibility(8);
            viewHolder.binding.line.setVisibility(8);
        } else if (this.list.get(i).getType().equals("title_1")) {
            viewHolder.binding.VigilanceTitle.setVisibility(0);
            viewHolder.binding.vigilanceNameText.setText(R.string.custom_alert_sound);
            viewHolder.binding.edit.setVisibility(0);
            viewHolder.binding.line.setVisibility(8);
        } else {
            viewHolder.binding.VigilanceTitle.setVisibility(8);
            viewHolder.binding.line.setVisibility(0);
        }
        if (this.list.get(i).getContent().equals(string)) {
            viewHolder.binding.textView.setTextColor(viewHolder.binding.getRoot().getResources().getColor(R.color.colorAccent));
        } else {
            viewHolder.binding.textView.setTextColor(viewHolder.binding.getRoot().getResources().getColor(R.color.color_black));
        }
        viewHolder.binding.textView.setText(this.list.get(i).getContent());
        if (this.list.get(i).getCheck().booleanValue()) {
            viewHolder.binding.image.setVisibility(0);
        } else {
            viewHolder.binding.image.setVisibility(8);
        }
        if (this.list.get(i).getEdit().booleanValue()) {
            viewHolder.binding.delete.setVisibility(0);
        } else {
            viewHolder.binding.delete.setVisibility(8);
        }
        viewHolder.binding.edit.setOnClickListener(new View.OnClickListener() { // from class: adapter.VigilanceAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                int i2 = 4;
                if (VigilanceAdapter.this.list.get(4).getEdit().booleanValue()) {
                    while (i2 < VigilanceAdapter.this.getItemCount() - 1) {
                        VigilanceAdapter.this.list.get(i2).setEdit(false);
                        viewHolder.itemView.setClickable(true);
                        i2++;
                    }
                } else {
                    while (i2 < VigilanceAdapter.this.getItemCount()) {
                        if (!VigilanceAdapter.this.list.get(i2).getContent().equals(string)) {
                            VigilanceAdapter.this.list.get(i2).setEdit(true);
                        }
                        viewHolder.itemView.setClickable(false);
                        i2++;
                    }
                }
                VigilanceAdapter.this.notifyDataSetChanged();
            }
        });
        viewHolder.binding.delete.setOnClickListener(new View.OnClickListener() { // from class: adapter.VigilanceAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                VigilanceAdapter.this.showTips(i);
            }
        });
        viewHolder.itemView.setOnClickListener(new AnonymousClass3(i, string));
    }

    /* JADX INFO: renamed from: adapter.VigilanceAdapter$3, reason: invalid class name */
    class AnonymousClass3 implements View.OnClickListener {
        final /* synthetic */ int val$position;
        final /* synthetic */ String val$str;

        AnonymousClass3(int i, String str) {
            this.val$position = i;
            this.val$str = str;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            String content;
            if (VigilanceAdapter.this.getItemCount() - 1 == this.val$position && VigilanceAdapter.this.list.get(this.val$position).getContent().equals(this.val$str)) {
                VigilanceAdapter.this.context.addT();
                return;
            }
            if (this.val$position < 4) {
                content = VigilanceAdapter.this.context.getResources().getStringArray(R.array.VigilanceValue)[this.val$position];
            } else {
                content = VigilanceAdapter.this.list.get(this.val$position).getContent();
            }
            VigilanceAdapter vigilanceAdapter = VigilanceAdapter.this;
            vigilanceAdapter.update(vigilanceAdapter.iotId, content, new callback() { // from class: adapter.VigilanceAdapter.3.1
                @Override // adapter.VigilanceAdapter.callback
                public void onComplete(boolean z) {
                    VigilanceAdapter.this.context.runOnUiThread(new Runnable() { // from class: adapter.VigilanceAdapter.3.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (VigilanceAdapter.this.mSelectedPos != AnonymousClass3.this.val$position) {
                                VigilanceAdapter.this.list.get(VigilanceAdapter.this.mSelectedPos).setCheck(false);
                                VigilanceAdapter.this.notifyItemChanged(VigilanceAdapter.this.mSelectedPos);
                                VigilanceAdapter.this.mSelectedPos = AnonymousClass3.this.val$position;
                                VigilanceAdapter.this.list.get(VigilanceAdapter.this.mSelectedPos).setCheck(true);
                                VigilanceAdapter.this.notifyItemChanged(VigilanceAdapter.this.mSelectedPos);
                            }
                        }
                    });
                }
            });
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return Math.min(this.list.size(), 9);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        VigilanceRecyclerviewItemBinding binding;

        public ViewHolder(@NonNull VigilanceRecyclerviewItemBinding vigilanceRecyclerviewItemBinding) {
            super(vigilanceRecyclerviewItemBinding.getRoot());
            this.binding = vigilanceRecyclerviewItemBinding;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"SetTextI18n"})
    public void showTips(final int i) {
        TextView textView = new TextView(this.context);
        textView.setText(R.string.tip);
        textView.setTextSize(20.0f);
        textView.setGravity(17);
        textView.setPadding(0, 50, 0, 0);
        textView.setTextColor(this.context.getResources().getColor(R.color.color_black));
        View viewInflate = View.inflate(this.context, R.layout.dialog_message, null);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.textView);
        textView2.setText(this.context.getString(R.string.delete_or_not) + "\"" + this.list.get(i).getName() + "\"");
        textView2.setGravity(17);
        textView2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this.context).setCustomTitle(textView).setView(viewInflate).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: adapter.VigilanceAdapter.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                VigilanceAdapter vigilanceAdapter = VigilanceAdapter.this;
                vigilanceAdapter.deleteVoiceFile(vigilanceAdapter.list.get(i).getName(), 1, i);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: adapter.VigilanceAdapter.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                dialogInterface.dismiss();
            }
        }).create();
        alertDialogCreate.show();
        alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
        alertDialogCreate.getButton(-2).setTextColor(SupportMenu.CATEGORY_MASK);
        Button button = alertDialogCreate.getButton(-1);
        Button button2 = alertDialogCreate.getButton(-2);
        button2.setTextSize(18.0f);
        button.setTextSize(18.0f);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 10.0f;
        button.setLayoutParams(layoutParams);
        button2.setLayoutParams(layoutParams);
    }

    void update(String str, String str2, final callback callbackVar) {
        HashMap map = new HashMap();
        AlarmAudioRecordBean alarmAudioRecordBean = new AlarmAudioRecordBean(1, str2);
        Log.e("提交的参数", JSON.toJSONString(alarmAudioRecordBean));
        map.put(Constants.Voice_Prompt_Type, alarmAudioRecordBean);
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: adapter.VigilanceAdapter.6
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    callbackVar.onComplete(z);
                }
            }
        });
    }

    public void mSelectedPosInit() {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getCheck().booleanValue()) {
                this.mSelectedPos = i;
            }
        }
    }

    public void deleteVoiceFile(String str, int i, final int i2) {
        IPCManager.getInstance().getDevice(this.iotId).getVoiceList(str, i, new IPanelCallback() { // from class: adapter.VigilanceAdapter.7
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                Log.d("TAG", "onComplete: ");
                if (z) {
                    VigilanceAdapter.this.list.remove(i2);
                    VigilanceAdapter.this.context.get();
                }
            }
        });
    }
}
