package bean;

import com.alibaba.fastjson.annotation.JSONField;
import config.Constants;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class CrossLineDetectBean implements Serializable {

    @JSONField(name = "Direction")
    private int Direction;

    @JSONField(name = Constants.Enable)
    private int Enable;

    @JSONField(name = "EndX")
    private int EndX;

    @JSONField(name = "EndY")
    private int EndY;

    @JSONField(name = "StartX")
    private int StartX;

    @JSONField(name = "StartY")
    private int StartY;

    public int getEnable() {
        return this.Enable;
    }

    public void setEnable(int i) {
        this.Enable = i;
    }

    public int getStartX() {
        return this.StartX;
    }

    public void setStartX(int i) {
        this.StartX = i;
    }

    public int getStartY() {
        return this.StartY;
    }

    public void setStartY(int i) {
        this.StartY = i;
    }

    public int getEndX() {
        return this.EndX;
    }

    public void setEndX(int i) {
        this.EndX = i;
    }

    public int getEndY() {
        return this.EndY;
    }

    public void setEndY(int i) {
        this.EndY = i;
    }

    public int getDirection() {
        return this.Direction;
    }

    public void setDirection(int i) {
        this.Direction = i;
    }
}
