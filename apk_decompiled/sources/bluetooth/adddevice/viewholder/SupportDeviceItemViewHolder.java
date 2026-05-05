package bluetooth.adddevice.viewholder;

import adapter.BaseViewHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import bean.FoundDevice;
import com.aliyun.iot.aep.component.router.Router;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class SupportDeviceItemViewHolder extends BaseViewHolder<FoundDevice> {
    private static String CODE = "link://router/connectConfig";
    private Button btn_device_connect;
    private ImageView iv_device_icon;
    private TextView tv_device_name;

    public SupportDeviceItemViewHolder(View view2) {
        super(view2);
        this.iv_device_icon = (ImageView) view2.findViewById(R.id.list_item_device_icon);
        this.tv_device_name = (TextView) view2.findViewById(R.id.list_item_device_name);
        this.btn_device_connect = (Button) view2.findViewById(R.id.list_item_device_action);
    }

    @Override // adapter.BaseViewHolder
    public void onBind(final FoundDevice foundDevice, int i) {
        super.onBind(foundDevice, i);
        this.tv_device_name.setText(foundDevice.deviceName);
        this.btn_device_connect.setOnClickListener(new View.OnClickListener() { // from class: bluetooth.adddevice.viewholder.SupportDeviceItemViewHolder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String str = SupportDeviceItemViewHolder.CODE;
                Bundle bundle = new Bundle();
                bundle.putString("productKey", foundDevice.productKey);
                Router.getInstance().toUrlForResult((Activity) view2.getContext(), str, 1, bundle);
            }
        });
    }
}
