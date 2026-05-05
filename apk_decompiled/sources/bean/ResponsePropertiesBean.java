package bean;

/* JADX INFO: loaded from: classes.dex */
public class ResponsePropertiesBean {
    private int code;
    private DataBean data;
    private String id;

    public ResponsePropertiesBean(int i, DataBean dataBean, String str) {
        this.code = i;
        this.data = dataBean;
        this.id = str;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean dataBean) {
        this.data = dataBean;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public static class DataBean {
        private HSVColorBean HSVColor;
        private LightSwitchBean LightSwitch;
        private NightLightSwitchBean NightLightSwitch;

        public LightSwitchBean getLightSwitch() {
            return this.LightSwitch;
        }

        public void setLightSwitch(LightSwitchBean lightSwitchBean) {
            this.LightSwitch = lightSwitchBean;
        }

        public NightLightSwitchBean getNightLightSwitch() {
            return this.NightLightSwitch;
        }

        public void setNightLightSwitch(NightLightSwitchBean nightLightSwitchBean) {
            this.NightLightSwitch = nightLightSwitchBean;
        }

        public HSVColorBean getHSVColor() {
            return this.HSVColor;
        }

        public void setHSVColor(HSVColorBean hSVColorBean) {
            this.HSVColor = hSVColorBean;
        }

        public static class LightSwitchBean {
            private long time;
            private int value;

            public LightSwitchBean(int i) {
                this.value = i;
            }

            public long getTime() {
                return this.time;
            }

            public void setTime(long j) {
                this.time = j;
            }

            public int getValue() {
                return this.value;
            }

            public void setValue(int i) {
                this.value = i;
            }
        }

        public static class NightLightSwitchBean {
            private long time;
            private int value;

            public long getTime() {
                return this.time;
            }

            public void setTime(long j) {
                this.time = j;
            }

            public int getValue() {
                return this.value;
            }

            public void setValue(int i) {
                this.value = i;
            }
        }

        public static class HSVColorBean {
            private long time;
            private ValueBean value;

            public long getTime() {
                return this.time;
            }

            public void setTime(long j) {
                this.time = j;
            }

            public ValueBean getValue() {
                return this.value;
            }

            public void setValue(ValueBean valueBean) {
                this.value = valueBean;
            }

            public static class ValueBean {
                private int Hue;
                private int Saturation;
                private int Value;

                public int getSaturation() {
                    return this.Saturation;
                }

                public void setSaturation(int i) {
                    this.Saturation = i;
                }

                public int getValue() {
                    return this.Value;
                }

                public void setValue(int i) {
                    this.Value = i;
                }

                public int getHue() {
                    return this.Hue;
                }

                public void setHue(int i) {
                    this.Hue = i;
                }
            }
        }
    }
}
