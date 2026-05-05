package dialog;

import adapter.DiaLogListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import java.util.List;
import tools.DensityUtil;
import tools.OnMultiClickListener;
import tools.ScreenUtil;

/* JADX INFO: loaded from: classes3.dex */
public class DialogUtil {

    public interface OnCancelConfirmClickListener {
        void CancelListener();

        void ConfirmListener();
    }

    public interface OnConfirmClickListener {
        void ConfirmListener();
    }

    public interface OnEditClickListener {
        void onEditClick(String str);
    }

    public interface OnItemClickListener {
        void ItemListener(int i);
    }

    public static void showListDiaLog(Context context, List<String> list, int i, final OnItemClickListener onItemClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_gun_ball, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_cancel);
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DiaLogListAdapter diaLogListAdapter = new DiaLogListAdapter(context, list, i);
        recyclerView.setAdapter(diaLogListAdapter);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(80);
        alertDialogCreate.getWindow().setLayout(-2, -2);
        textView.setOnClickListener(new View.OnClickListener() { // from class: dialog.DialogUtil.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        diaLogListAdapter.setOnItemClickListener(new DiaLogListAdapter.OnItemClickListener() { // from class: dialog.DialogUtil.2
            @Override // adapter.DiaLogListAdapter.OnItemClickListener
            public void onItemClick(int i2) {
                alertDialogCreate.dismiss();
                OnItemClickListener onItemClickListener2 = onItemClickListener;
                if (onItemClickListener2 != null) {
                    onItemClickListener2.ItemListener(i2);
                }
            }
        });
    }

    public static void showListDiaLog(Context context, List<String> list, String str, int i, final OnItemClickListener onItemClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_gun_ball, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_cancel);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_show_mode);
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DiaLogListAdapter diaLogListAdapter = new DiaLogListAdapter(context, list, i);
        recyclerView.setAdapter(diaLogListAdapter);
        textView2.setText(str);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(80);
        alertDialogCreate.getWindow().setLayout(-2, -2);
        textView.setOnClickListener(new View.OnClickListener() { // from class: dialog.DialogUtil.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        diaLogListAdapter.setOnItemClickListener(new DiaLogListAdapter.OnItemClickListener() { // from class: dialog.DialogUtil.4
            @Override // adapter.DiaLogListAdapter.OnItemClickListener
            public void onItemClick(int i2) {
                alertDialogCreate.dismiss();
                OnItemClickListener onItemClickListener2 = onItemClickListener;
                if (onItemClickListener2 != null) {
                    onItemClickListener2.ItemListener(i2);
                }
            }
        });
    }

    public static void showCancelConfirmDiaLog(Context context, String str, String str2, String str3, String str4, final OnCancelConfirmClickListener onCancelConfirmClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_cancel_confirm, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_title);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_content);
        TextView textView3 = (TextView) viewInflate.findViewById(R.id.tv_cancel);
        TextView textView4 = (TextView) viewInflate.findViewById(R.id.tv_confirm);
        textView.setText(str);
        textView2.setText(str2);
        textView3.setText(str3);
        textView4.setText(str4);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(17);
        alertDialogCreate.getWindow().setLayout(-2, -2);
        textView3.setOnClickListener(new OnMultiClickListener() { // from class: dialog.DialogUtil.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                alertDialogCreate.dismiss();
                OnCancelConfirmClickListener onCancelConfirmClickListener2 = onCancelConfirmClickListener;
                if (onCancelConfirmClickListener2 != null) {
                    onCancelConfirmClickListener2.CancelListener();
                }
            }
        });
        textView4.setOnClickListener(new OnMultiClickListener() { // from class: dialog.DialogUtil.6
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                alertDialogCreate.dismiss();
                OnCancelConfirmClickListener onCancelConfirmClickListener2 = onCancelConfirmClickListener;
                if (onCancelConfirmClickListener2 != null) {
                    onCancelConfirmClickListener2.ConfirmListener();
                }
            }
        });
    }

    public static void showConfirmDiaLog(Context context, String str, String str2, final OnConfirmClickListener onConfirmClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_tips);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_confirm);
        textView.setText(str);
        textView2.setText(str2);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(17);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(context, 330.0f), -2);
        textView2.setOnClickListener(new OnMultiClickListener() { // from class: dialog.DialogUtil.7
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                alertDialogCreate.dismiss();
                OnConfirmClickListener onConfirmClickListener2 = onConfirmClickListener;
                if (onConfirmClickListener2 != null) {
                    onConfirmClickListener2.ConfirmListener();
                }
            }
        });
    }

    public static void showConfirmDiaLog(Context context, String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_tips);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_confirm);
        textView.setText(str);
        textView2.setText(str2);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(17);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(context, 330.0f), -2);
        textView2.setOnClickListener(new View.OnClickListener() { // from class: dialog.DialogUtil.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
    }

    public static void showSuccessDiaLog(final Activity activity2, String str, int i, int i2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity2, R.style.ShareDialogTheme);
        View viewInflate = LayoutInflater.from(activity2).inflate(R.layout.dialog_success, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.tv_text)).setText(str);
        final AlertDialog alertDialogCreate = builder.create();
        Window window = alertDialogCreate.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.y = DensityUtil.dip2px(activity2, 100.0f);
        window.setAttributes(attributes);
        window.setBackgroundDrawable(new ColorDrawable(0));
        if (!activity2.isFinishing() && !alertDialogCreate.isShowing()) {
            alertDialogCreate.show();
        }
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(80);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(activity2, i), -2);
        new Handler().postDelayed(new Runnable() { // from class: dialog.DialogUtil.9
            @Override // java.lang.Runnable
            public void run() {
                if (activity2.isFinishing() || !alertDialogCreate.isShowing()) {
                    return;
                }
                alertDialogCreate.dismiss();
            }
        }, i2);
    }

    public static void showTipsConfirmDiaLog(Context context, String str, String str2, String str3, final OnConfirmClickListener onConfirmClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_title_confirm, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_title);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_tips);
        TextView textView3 = (TextView) viewInflate.findViewById(R.id.tv_confirm);
        textView.setText(str);
        textView2.setText(str2);
        textView3.setText(str3);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(17);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(context, 330.0f), -2);
        textView3.setOnClickListener(new View.OnClickListener() { // from class: dialog.DialogUtil.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
                OnConfirmClickListener onConfirmClickListener2 = onConfirmClickListener;
                if (onConfirmClickListener2 != null) {
                    onConfirmClickListener2.ConfirmListener();
                }
            }
        });
    }

    public static void showTipsConfirmDiaLog(Activity activity2, String str, String str2, String str3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity2);
        View viewInflate = LayoutInflater.from(activity2).inflate(R.layout.dialog_title_confirm, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_title);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_tips);
        TextView textView3 = (TextView) viewInflate.findViewById(R.id.tv_confirm);
        textView.setText(str);
        textView2.setText(str2);
        textView3.setText(str3);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        if (!activity2.isFinishing() && !alertDialogCreate.isShowing()) {
            alertDialogCreate.show();
        }
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(17);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(activity2, 330.0f), -2);
        textView3.setOnClickListener(new View.OnClickListener() { // from class: dialog.DialogUtil.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
    }

    public static void showWeChatDiaLog(Context context, final OnConfirmClickListener onConfirmClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_wechat, (ViewGroup) null);
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.iv_close);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_save);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        if (!alertDialogCreate.isShowing()) {
            alertDialogCreate.show();
        }
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(17);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(context, 250.0f), -2);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: dialog.DialogUtil.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() { // from class: dialog.DialogUtil.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
                OnConfirmClickListener onConfirmClickListener2 = onConfirmClickListener;
                if (onConfirmClickListener2 != null) {
                    onConfirmClickListener2.ConfirmListener();
                }
            }
        });
    }

    public static void showLoadingDiaLog(Context context, String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_loading, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.tv_text)).setText(str);
        AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(17);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(context, 330.0f), -2);
    }

    public static void showScoreDiaLog(Context context, final OnConfirmClickListener onConfirmClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_score, (ViewGroup) null);
        LinearLayout linearLayout = (LinearLayout) viewInflate.findViewById(R.id.layout_score);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_cancel);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(17);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(context, 260.0f), -2);
        textView.setOnClickListener(new OnMultiClickListener() { // from class: dialog.DialogUtil.14
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        linearLayout.setOnClickListener(new OnMultiClickListener() { // from class: dialog.DialogUtil.15
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                alertDialogCreate.dismiss();
                OnConfirmClickListener onConfirmClickListener2 = onConfirmClickListener;
                if (onConfirmClickListener2 != null) {
                    onConfirmClickListener2.ConfirmListener();
                }
            }
        });
    }

    public static void showEditDiaLog(Context context, String str, String str2, final OnEditClickListener onEditClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_edit, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_title);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.ed_content);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_confirm);
        TextView textView3 = (TextView) viewInflate.findViewById(R.id.tv_cancel);
        textView.setText(str);
        editText.setHint(str2);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new BitmapDrawable());
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setContentView(viewInflate);
        alertDialogCreate.getWindow().setGravity(17);
        alertDialogCreate.getWindow().clearFlags(131080);
        alertDialogCreate.getWindow().setSoftInputMode(4);
        alertDialogCreate.getWindow().setLayout(ScreenUtil.dp2Px(context, 300.0f), -2);
        textView2.setOnClickListener(new OnMultiClickListener() { // from class: dialog.DialogUtil.16
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                alertDialogCreate.dismiss();
                OnEditClickListener onEditClickListener2 = onEditClickListener;
                if (onEditClickListener2 != null) {
                    onEditClickListener2.onEditClick(editText.getText().toString());
                }
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() { // from class: dialog.DialogUtil.17
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
    }
}
