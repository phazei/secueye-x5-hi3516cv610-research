package bean;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class RequestPropertiesBean {
    private String iotId;
    private Items items;

    public RequestPropertiesBean() {
    }

    public RequestPropertiesBean(String str, Items items) {
        this.iotId = str;
        this.items = items;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public String getIotId() {
        return this.iotId;
    }

    public Items getItems() {
        return this.items;
    }

    public static class Items {
        private HSVColor HSVColor;
        private int LightSwitch;
        private int NightLightSwitch;

        public Items(HSVColor hSVColor) {
            this.HSVColor = hSVColor;
        }

        public Items(int i, int i2) {
            this.LightSwitch = i;
            this.NightLightSwitch = i2;
        }

        public void setLightSwitch(int i) {
            this.LightSwitch = i;
        }

        public int getLightSwitch() {
            return this.LightSwitch;
        }

        public int getNightLightSwitch() {
            return this.NightLightSwitch;
        }

        public HSVColor getHSVColor() {
            return this.HSVColor;
        }

        public void setNightLightSwitch(int i) {
            this.NightLightSwitch = i;
        }

        public void setHSVColor(HSVColor hSVColor) {
            this.HSVColor = hSVColor;
        }

        public Items(int i, int i2, HSVColor hSVColor) {
            this.LightSwitch = i;
            this.NightLightSwitch = i2;
            this.HSVColor = hSVColor;
        }

        public static class HSVColor {
            private int Hue;
            private int Saturation;
            private int Value;

            public HSVColor(double d2, double d3, double d4) {
                this.Hue = (int) (d2 * 100.0d);
                this.Saturation = (int) (d3 * 100.0d);
                this.Value = (int) (d4 * 100.0d);
            }

            public HSVColor(float[] fArr) {
                this.Hue = (int) (fArr[0] * 100.0f);
                this.Saturation = (int) (fArr[1] * 100.0f);
                this.Value = (int) (fArr[2] * 100.0f);
                Log.d("HSVColor", "Hue:" + this.Hue);
                Log.d("HSVColor", "Saturation:" + this.Saturation);
                Log.d("HSVColor", "Value:" + this.Value);
            }
        }
    }
}
