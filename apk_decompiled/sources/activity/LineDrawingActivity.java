package activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.seculink.app.R;
import tools.Utils;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class LineDrawingActivity extends CommonActivity {
    ItemView edit;
    TitleView fl_titlebar;
    ItemView flowStatistics;
    String iotId;
    ConstraintLayout layout_main;
    ImageView lineView;
    SwitchCompat switch2;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_line_drawing;
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    @SuppressLint({"CheckResult"})
    protected void onResume() {
        super.onResume();
        this.iotId = getIntent().getStringExtra("iotId");
        String str = getFilesPath(this) + "/line/" + this.iotId + ".jpg";
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
        Glide.with((FragmentActivity) this).asBitmap().load(str).apply(requestOptions).into(this.lineView);
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    @SuppressLint({"CheckResult"})
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public String getFilesPath(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = context.getExternalFilesDir(null).getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "//" + Utils.getUserPhone();
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.fl_titlebar = (TitleView) findViewById(R.id.titleView);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.LineDrawingActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                LineDrawingActivity.this.finish();
            }
        });
        this.lineView = (ImageView) findViewById(R.id.line_view_item);
        this.flowStatistics = (ItemView) findViewById(R.id.flow_statistics);
        this.flowStatistics.addRightView(new SwitchCompat(this));
        this.switch2 = (SwitchCompat) this.flowStatistics.getRightView();
        this.switch2.setTextOff("");
        this.switch2.setTextOn("");
        this.switch2.setText("");
        this.switch2.setThumbDrawable(null);
        this.switch2.setBackgroundResource(R.drawable.sel_switch);
        this.edit = (ItemView) findViewById(R.id.edit);
        this.edit.setOnClickListener(new View.OnClickListener() { // from class: activity.LineDrawingActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(LineDrawingActivity.this.getApplicationContext(), (Class<?>) SetTheBoundaryActivity.class);
                intent.putExtra("iotId", LineDrawingActivity.this.iotId);
                LineDrawingActivity.this.startActivity(intent);
            }
        });
    }
}
