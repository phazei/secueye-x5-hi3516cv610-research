package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class EQSettingItemView extends FrameLayout {
    public EQSettingItemView(@NonNull Context context) {
        super(context);
        initView();
    }

    public EQSettingItemView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public EQSettingItemView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.equipment_setting_item_view, this);
    }
}
