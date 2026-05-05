package dialog;

import adapter.PalettesDialogListviewAdapter;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import bean.PalettesDialogBean;
import com.seculink.app.R;
import dialog.ASlideDialog;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class HSVDialog {
    private static HSVDialog hsvDialog;

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    private PalettesDialogListviewAdapter f7869adapter;
    private ImageView cancleIv;
    private ListView listView;
    private ASlideDialog menuDialog;

    public interface OnHSVItemClickListener {
        void onItemClick(List<PalettesDialogBean> list, int i);
    }

    private HSVDialog() {
    }

    public static HSVDialog getInstance() {
        if (hsvDialog == null) {
            hsvDialog = new HSVDialog();
        }
        return hsvDialog;
    }

    public void showMenuDialog(Context context, String str, final OnHSVItemClickListener onHSVItemClickListener) {
        this.menuDialog = ASlideDialog.newInstance(context, ASlideDialog.Gravity.Bottom, R.layout.hsv_palettes_dialog);
        this.menuDialog.setCanceledOnTouchOutside(true);
        this.listView = (ListView) this.menuDialog.findViewById(R.id.hsv_palettes_lv);
        this.cancleIv = (ImageView) this.menuDialog.findViewById(R.id.hsv_palettes_cancle_iv);
        this.f7869adapter = new PalettesDialogListviewAdapter(context);
        this.listView.setAdapter((ListAdapter) this.f7869adapter);
        this.f7869adapter.setSelectPos(str);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: dialog.-$$Lambda$HSVDialog$uXZ__Ux8xHy13ft1uohvizw7BGA
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view2, int i, long j) {
                HSVDialog.lambda$showMenuDialog$0(this.f$0, onHSVItemClickListener, adapterView, view2, i, j);
            }
        });
        this.cancleIv.setOnClickListener(new View.OnClickListener() { // from class: dialog.-$$Lambda$HSVDialog$CmJVjXTV2nANs_b0Wt3RPWMWTck
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                this.f$0.hideMenuDialog();
            }
        });
        this.menuDialog.show();
    }

    public static /* synthetic */ void lambda$showMenuDialog$0(HSVDialog hSVDialog, OnHSVItemClickListener onHSVItemClickListener, AdapterView adapterView, View view2, int i, long j) {
        PalettesDialogListviewAdapter palettesDialogListviewAdapter = hSVDialog.f7869adapter;
        palettesDialogListviewAdapter.setSelectPos(palettesDialogListviewAdapter.getArr().get(i).getColor());
        if (onHSVItemClickListener != null) {
            onHSVItemClickListener.onItemClick(hSVDialog.f7869adapter.getArr(), i);
        }
        hSVDialog.hideMenuDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideMenuDialog() {
        ASlideDialog aSlideDialog = this.menuDialog;
        if (aSlideDialog != null) {
            aSlideDialog.hide();
        }
    }
}
