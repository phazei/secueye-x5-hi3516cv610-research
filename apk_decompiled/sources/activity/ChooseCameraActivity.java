package activity;

import adapter.CameraDeviceInfoAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import java.util.List;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class ChooseCameraActivity extends CommonActivity {
    private CameraDeviceInfoAdapter cameraDeviceInfoAdapter;
    private TitleView fl_titlebar;
    LinearLayout layout_main;
    private RecyclerView mRecyclerView;
    private List<String> names;
    private int pos = 0;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_choose_camera;
    }

    public void setTvCheckAllDrawable() {
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ChooseCameraActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                ChooseCameraActivity.this.finish();
            }
        });
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.cameraDeviceInfoAdapter = new CameraDeviceInfoAdapter(R.layout.item_choose_camera);
        this.cameraDeviceInfoAdapter.bindToRecyclerView(this.mRecyclerView);
        this.cameraDeviceInfoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() { // from class: activity.ChooseCameraActivity.2
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view2, int i) {
                if (view2.getId() == R.id.bt_select) {
                    ChooseCameraActivity.this.pos = i;
                    ChooseCameraActivity.this.cameraDeviceInfoAdapter.setPos(i);
                    ChooseCameraActivity.this.finish();
                }
            }
        });
        this.cameraDeviceInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: activity.ChooseCameraActivity.3
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i) {
                ChooseCameraActivity.this.pos = i;
                ChooseCameraActivity.this.cameraDeviceInfoAdapter.setPos(i);
                ChooseCameraActivity.this.finish();
            }
        });
        this.names = getIntent().getStringArrayListExtra("names");
        this.pos = getIntent().getIntExtra("pos", 0);
        this.cameraDeviceInfoAdapter.replaceData(this.names);
        this.cameraDeviceInfoAdapter.setPos(this.pos);
    }

    @Override // android.app.Activity
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("pos", this.pos);
        setResult(-1, intent);
        super.finish();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }
}
