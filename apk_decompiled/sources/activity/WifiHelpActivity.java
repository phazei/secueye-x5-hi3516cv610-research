package activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.seculink.app.R;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class WifiHelpActivity extends CommonActivity {
    private ImageView indoor_image;
    LinearLayout layout_main;
    private ImageView outdoor_image;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_wifi_helper;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.indoor_image = (ImageView) findViewById(R.id.indoor_image);
        this.outdoor_image = (ImageView) findViewById(R.id.outdoor_image);
        if (Locale.getDefault().getLanguage().contains("zh")) {
            this.indoor_image.setBackgroundResource(R.drawable.indoor_image_zh);
            this.outdoor_image.setBackgroundResource(R.drawable.outdoor_image_zh);
        } else {
            this.indoor_image.setBackgroundResource(R.drawable.indoor_image);
            this.outdoor_image.setBackgroundResource(R.drawable.outdoor_image);
        }
    }
}
