package view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class DeleteDialog extends Dialog {
    int i;
    private ImageView imageView;
    private ImageView imageView_;

    public DeleteDialog(@NonNull Context context, int i) {
        super(context, R.style.Translucent_NoTitle);
        this.i = i;
    }

    @Override // android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.delete_dialog_layout);
        this.imageView_ = (ImageView) findViewById(R.id.image);
        this.imageView_.setImageResource(this.i);
        this.imageView = (ImageView) findViewById(R.id.image_delete);
        this.imageView.setOnClickListener(new View.OnClickListener() { // from class: view.DeleteDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                DeleteDialog.this.dismiss();
            }
        });
        setCanceledOnTouchOutside(true);
    }

    public ImageView getImageView() {
        return this.imageView_;
    }
}
