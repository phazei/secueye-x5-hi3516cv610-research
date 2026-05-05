package bluetooth.adddevice.viewholder;

import adapter.BaseViewHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import bean.FoundDevice;
import bean.FoundDeviceListItem;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.component.router.Router;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class LocalDeviceFoundViewHolder extends BaseViewHolder<FoundDevice> {
    private static String CODE = "link://router/connectConfig";
    private Button btn_device_connect;
    private ImageView iv_device_icon;
    private TextView tv_device_name;

    public LocalDeviceFoundViewHolder(View view2) {
        super(view2);
        this.iv_device_icon = (ImageView) view2.findViewById(R.id.list_item_device_icon);
        this.tv_device_name = (TextView) view2.findViewById(R.id.list_item_device_name);
        this.btn_device_connect = (Button) view2.findViewById(R.id.list_item_device_action);
    }

    @Override // adapter.BaseViewHolder
    public void onBind(FoundDevice foundDevice, int i) {
        super.onBind(foundDevice, i);
        final FoundDeviceListItem foundDeviceListItem = (FoundDeviceListItem) foundDevice;
        this.tv_device_name.setText(foundDeviceListItem.deviceName);
        if (foundDeviceListItem.discoveryType == DiscoveryType.CLOUD_ENROLLEE_DEVICE) {
            this.btn_device_connect.setText("连接");
        } else {
            this.btn_device_connect.setText("绑定");
        }
        final DeviceInfo deviceInfo = foundDeviceListItem.deviceInfo;
        this.btn_device_connect.setOnClickListener(new View.OnClickListener() { // from class: bluetooth.adddevice.viewholder.LocalDeviceFoundViewHolder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (foundDeviceListItem.discoveryType == DiscoveryType.LOCAL_ONLINE_DEVICE) {
                    String str = LocalDeviceFoundViewHolder.CODE;
                    Bundle bundle = new Bundle();
                    bundle.putString("awssVer", deviceInfo.awssVer.toString());
                    bundle.putString("productKey", deviceInfo.productKey);
                    bundle.putString("deviceName", deviceInfo.deviceName);
                    bundle.putString("regProductKey", deviceInfo.regProductKey);
                    bundle.putString("regDeviceName", deviceInfo.regDeviceName);
                    bundle.putString("token", deviceInfo.token);
                    bundle.putString(AlinkConstants.KEY_DEV_TYPE, deviceInfo.devType);
                    bundle.putString("addDeviceFrom", deviceInfo.addDeviceFrom);
                    bundle.putString(AlinkConstants.KEY_LINKTYPE, deviceInfo.linkType);
                    Router.getInstance().toUrlForResult((Activity) view2.getContext(), str, 1, bundle);
                    return;
                }
                String str2 = LocalDeviceFoundViewHolder.CODE;
                Bundle bundle2 = new Bundle();
                if (deviceInfo.awssVer != null) {
                    bundle2.putString("awssVer", deviceInfo.awssVer.toString());
                }
                bundle2.putString("productKey", deviceInfo.productKey);
                bundle2.putString("deviceName", deviceInfo.deviceName);
                bundle2.putString("regProductKey", deviceInfo.regProductKey);
                bundle2.putString("regDeviceName", deviceInfo.regDeviceName);
                bundle2.putString("token", deviceInfo.token);
                bundle2.putString(AlinkConstants.KEY_DEV_TYPE, deviceInfo.devType);
                bundle2.putString("addDeviceFrom", deviceInfo.addDeviceFrom);
                bundle2.putString(AlinkConstants.KEY_LINKTYPE, deviceInfo.linkType);
                Router.getInstance().toUrlForResult((Activity) view2.getContext(), str2, 1, bundle2);
            }
        });
    }
}
