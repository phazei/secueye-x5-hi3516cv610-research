package bluetooth.adddevice;

import bean.FoundDeviceListItem;
import bean.SupportDeviceListItem;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface OnDeviceAddListener {
    void onFilterComplete(List<FoundDeviceListItem> list);

    void onSupportDeviceSuccess(List<SupportDeviceListItem> list);

    void showToast(String str);
}
