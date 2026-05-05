package adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.SwitchCompat;
import bean.SharePermissionBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import view.ItemView;

/* JADX INFO: loaded from: classes.dex */
public class SharePermissionAdapter extends BaseQuickAdapter<String, com.chad.library.adapter.base.BaseViewHolder> {
    private Context context;
    private boolean flag;
    private boolean isCodeName;
    private SharePermissionBean sharePermissionBean;

    public SharePermissionAdapter(int i, Context context, SharePermissionBean sharePermissionBean) {
        super(i);
        this.context = context;
        if (context != null) {
            Log.d(TAG, "SharePermissionAdapter: " + context);
        }
        this.sharePermissionBean = sharePermissionBean;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, final String str) {
        ItemView itemView = (ItemView) baseViewHolder.getView(R.id.share_permission_check);
        itemView.setTitleText(str);
        itemView.addRightView(new SwitchCompat(this.context));
        final SwitchCompat switchCompat = (SwitchCompat) itemView.getRightView();
        switchCompat.setTextOff("");
        switchCompat.setTextOn("");
        switchCompat.setText("");
        switchCompat.setThumbDrawable(null);
        switchCompat.setBackgroundResource(R.drawable.sel_switch);
        this.isCodeName = selectName(str, this.sharePermissionBean);
        switchCompat.setChecked(this.isCodeName);
        switchCompat.setOnClickListener(new View.OnClickListener() { // from class: adapter.SharePermissionAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SharePermissionAdapter sharePermissionAdapter = SharePermissionAdapter.this;
                sharePermissionAdapter.flag = sharePermissionAdapter.changeState(str, sharePermissionAdapter.sharePermissionBean);
                switchCompat.setChecked(SharePermissionAdapter.this.flag);
            }
        });
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean changeState(java.lang.String r4, bean.SharePermissionBean r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case -2027471802: goto L32;
                case -1741759775: goto L28;
                case -1468140546: goto L1e;
                case 3494755: goto L14;
                case 985233329: goto La;
                default: goto L9;
            }
        L9:
            goto L3c
        La:
            java.lang.String r0 = "系统权限"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L3c
            r4 = 4
            goto L3d
        L14:
            java.lang.String r0 = "基础设置权限"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L3c
            r4 = r1
            goto L3d
        L1e:
            java.lang.String r0 = "设置报警权限"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L3c
            r4 = r2
            goto L3d
        L28:
            java.lang.String r0 = "设备网络权限"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L3c
            r4 = 3
            goto L3d
        L32:
            java.lang.String r0 = "设备存储权限"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L3c
            r4 = 2
            goto L3d
        L3c:
            r4 = -1
        L3d:
            switch(r4) {
                case 0: goto L65;
                case 1: goto L5c;
                case 2: goto L53;
                case 3: goto L4a;
                case 4: goto L41;
                default: goto L40;
            }
        L40:
            return r1
        L41:
            boolean r4 = r5.isOpenSetPermission
            r4 = r4 ^ r2
            r5.setOpenSetPermission(r4)
            boolean r4 = r5.isOpenSetPermission
            return r4
        L4a:
            boolean r4 = r5.isOpenDeviceNetworkPermission
            r4 = r4 ^ r2
            r5.setOpenDeviceNetworkPermission(r4)
            boolean r4 = r5.isOpenDeviceNetworkPermission
            return r4
        L53:
            boolean r4 = r5.isOpenDeviceStoragePermission
            r4 = r4 ^ r2
            r5.setOpenDeviceStoragePermission(r4)
            boolean r4 = r5.isOpenDeviceStoragePermission
            return r4
        L5c:
            boolean r4 = r5.isOpenDeviceWarningPermission
            r4 = r4 ^ r2
            r5.setOpenDeviceWarningPermission(r4)
            boolean r4 = r5.isOpenDeviceWarningPermission
            return r4
        L65:
            boolean r4 = r5.isOpenBaseSettingPermission
            r4 = r4 ^ r2
            r5.setOpenBaseSettingPermission(r4)
            boolean r4 = r5.isOpenBaseSettingPermission
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: adapter.SharePermissionAdapter.changeState(java.lang.String, bean.SharePermissionBean):boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean selectName(java.lang.String r3, bean.SharePermissionBean r4) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = 0
            switch(r0) {
                case -2027471802: goto L31;
                case -1741759775: goto L27;
                case -1468140546: goto L1d;
                case 3494755: goto L13;
                case 985233329: goto L9;
                default: goto L8;
            }
        L8:
            goto L3b
        L9:
            java.lang.String r0 = "系统权限"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L3b
            r3 = 4
            goto L3c
        L13:
            java.lang.String r0 = "基础设置权限"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L3b
            r3 = r1
            goto L3c
        L1d:
            java.lang.String r0 = "设置报警权限"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L3b
            r3 = 1
            goto L3c
        L27:
            java.lang.String r0 = "设备网络权限"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L3b
            r3 = 3
            goto L3c
        L31:
            java.lang.String r0 = "设备存储权限"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L3b
            r3 = 2
            goto L3c
        L3b:
            r3 = -1
        L3c:
            switch(r3) {
                case 0: goto L4c;
                case 1: goto L49;
                case 2: goto L46;
                case 3: goto L43;
                case 4: goto L40;
                default: goto L3f;
            }
        L3f:
            return r1
        L40:
            boolean r3 = r4.isOpenSetPermission
            return r3
        L43:
            boolean r3 = r4.isOpenDeviceNetworkPermission
            return r3
        L46:
            boolean r3 = r4.isOpenDeviceStoragePermission
            return r3
        L49:
            boolean r3 = r4.isOpenDeviceWarningPermission
            return r3
        L4c:
            boolean r3 = r4.isOpenBaseSettingPermission
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: adapter.SharePermissionAdapter.selectName(java.lang.String, bean.SharePermissionBean):boolean");
    }
}
