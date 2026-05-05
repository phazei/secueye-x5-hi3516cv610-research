package tools;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import bean.PalettesDialogBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: loaded from: classes4.dex */
public class ColorTools {
    public static float[] RGBtoHSV(String str) {
        int color = Color.parseColor(MqttTopic.MULTI_LEVEL_WILDCARD + str);
        Log.d("ColorTools", "n:" + color);
        int i = (16711680 & color) >> 16;
        int i2 = (65280 & color) >> 8;
        int i3 = color & 255;
        Log.d("ColorTools", "r:" + i);
        Log.d("ColorTools", "g:" + i2);
        Log.d("ColorTools", "b:" + i3);
        float[] fArr = new float[3];
        Color.RGBToHSV(i, i2, i3, fArr);
        Log.d("ColorTools", "h:" + fArr[0]);
        Log.d("ColorTools", "s:" + fArr[1]);
        Log.d("ColorTools", "v:" + fArr[2]);
        Log.d("ColorTools", fArr.length + "");
        return fArr;
    }

    public static List<PalettesDialogBean> getColorData(Context context) {
        return (List) new Gson().fromJson(JsonTools.getJson("lamps_hsv.json", context), new TypeToken<List<PalettesDialogBean>>() { // from class: tools.ColorTools.1
        }.getType());
    }
}
