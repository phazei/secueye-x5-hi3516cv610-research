package kt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: CloudButton.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u0014B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u000e\u001a\u00020\nH\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\nH\u0002J\u000e\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\nJ\u000e\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0007\u001a\u00020\bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkt/CloudButton;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attributeSet", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "changeIndex", "Lkt/CloudButton$ChangeIndex;", "currentIndex", "", AlinkConstants.KEY_LIST, "", "Landroid/widget/Button;", "getCurrentIndex", "select", "", FirebaseAnalytics.Param.INDEX, "setCurrentIndex", "setListener", "ChangeIndex", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class CloudButton extends LinearLayout {
    private HashMap _$_findViewCache;
    private ChangeIndex changeIndex;
    private int currentIndex;
    private final List<Button> list;

    /* JADX INFO: compiled from: CloudButton.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lkt/CloudButton$ChangeIndex;", "", "change", "", FirebaseAnalytics.Param.INDEX, "", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
    public interface ChangeIndex {
        void change(int index);
    }

    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view2 = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view2 != null) {
            return view2;
        }
        View viewFindViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), viewFindViewById);
        return viewFindViewById;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CloudButton(@NotNull Context context, @NotNull AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(attributeSet, "attributeSet");
        this.list = new ArrayList();
        LinearLayout.inflate(context, R.layout.cloud_button_layout, this);
        LinearLayout cloud_button_layout_view = (LinearLayout) _$_findCachedViewById(R.id.cloud_button_layout_view);
        Intrinsics.checkExpressionValueIsNotNull(cloud_button_layout_view, "cloud_button_layout_view");
        int childCount = cloud_button_layout_view.getChildCount();
        for (final int i = 0; i < childCount; i++) {
            View childAt = ((LinearLayout) _$_findCachedViewById(R.id.cloud_button_layout_view)).getChildAt(i);
            if (childAt == null) {
                throw new TypeCastException("null cannot be cast to non-null type android.widget.Button");
            }
            Button button = (Button) childAt;
            button.setOnClickListener(new View.OnClickListener() { // from class: kt.CloudButton.1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    CloudButton.this.select(i);
                }
            });
            this.list.add(button);
        }
        this.list.get(0).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_button_blue));
        Button button2 = this.list.get(0);
        Context context2 = getContext();
        Intrinsics.checkExpressionValueIsNotNull(context2, "getContext()");
        button2.setTextColor(context2.getResources().getColor(R.color.color_white));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void select(int index) {
        int size = this.list.size();
        for (int i = 0; i < size; i++) {
            this.list.get(i).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_button_white));
            Button button = this.list.get(i);
            Context context = getContext();
            Intrinsics.checkExpressionValueIsNotNull(context, "context");
            button.setTextColor(context.getResources().getColor(R.color.color_black));
        }
        this.list.get(index).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_button_blue));
        Button button2 = this.list.get(index);
        Context context2 = getContext();
        Intrinsics.checkExpressionValueIsNotNull(context2, "context");
        button2.setTextColor(context2.getResources().getColor(R.color.color_white));
        this.currentIndex = index;
        ChangeIndex changeIndex = this.changeIndex;
        if (changeIndex == null) {
            Intrinsics.throwUninitializedPropertyAccessException("changeIndex");
        }
        changeIndex.change(getCurrentIndex());
    }

    public final void setListener(@NotNull ChangeIndex changeIndex) {
        Intrinsics.checkParameterIsNotNull(changeIndex, "changeIndex");
        this.changeIndex = changeIndex;
    }

    private final int getCurrentIndex() {
        switch (this.currentIndex) {
        }
        return 0;
    }

    public final void setCurrentIndex(int index) {
        select(index);
    }
}
