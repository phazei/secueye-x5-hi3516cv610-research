package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class ModuleView extends LinearLayout {
    private ImageView img;
    public int imgId;
    public int textId;
    private TextView tv_name;

    public ModuleView(Context context) {
        this(context, null);
    }

    public ModuleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ModuleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ModuleView);
        this.textId = typedArrayObtainStyledAttributes.getResourceId(1, 0);
        this.imgId = typedArrayObtainStyledAttributes.getResourceId(0, 0);
        typedArrayObtainStyledAttributes.recycle();
        LayoutInflater.from(context).inflate(R.layout.item_module, (ViewGroup) this, true);
        this.img = (ImageView) findViewById(R.id.icon);
        setImg(this.imgId);
        this.tv_name = (TextView) findViewById(R.id.name);
        setText(this.textId);
    }

    public void setText(int i) {
        this.textId = i;
        this.tv_name.setText(i);
    }

    public void setImg(int i) {
        this.imgId = i;
        this.img.setImageResource(i);
    }
}
