package anet.channel.request;

import android.os.Parcelable;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface BodyEntry extends Parcelable {
    String getContentType();

    int writeTo(OutputStream outputStream) throws IOException;
}
