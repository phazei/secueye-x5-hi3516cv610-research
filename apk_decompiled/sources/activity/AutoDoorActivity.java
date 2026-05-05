package activity;

import adapter.AutoDoorRecyclerViewAdapter;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.seculink.app.R;
import com.seculink.app.databinding.AutoDoorLayoutBinding;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class AutoDoorActivity extends CommonActivity {
    AutoDoorLayoutBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.auto_door_layout;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.binding = AutoDoorLayoutBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.titleViewDoor.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.AutoDoorActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                AutoDoorActivity.this.finish();
            }
        });
        AutoDoorRecyclerViewAdapter autoDoorRecyclerViewAdapter = new AutoDoorRecyclerViewAdapter(getIntent().getStringExtra("iotId"), this, 5);
        this.binding.recyclerViewDoor.setLayoutManager(new LinearLayoutManager(this));
        this.binding.recyclerViewDoor.setAdapter(autoDoorRecyclerViewAdapter);
    }
}
