package bean;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class EventCallbackbean {
    private String batchId;
    private long gmtCreate;
    private String groupId;
    private List<String> groupIdList;
    private String iotId;
    private ItemsBean items;
    private String productKey;
    private String tenantId;
    private String thingType;

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String str) {
        this.groupId = str;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String str) {
        this.tenantId = str;
    }

    public String getThingType() {
        return this.thingType;
    }

    public void setThingType(String str) {
        this.thingType = str;
    }

    public String getBatchId() {
        return this.batchId;
    }

    public void setBatchId(String str) {
        this.batchId = str;
    }

    public long getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(long j) {
        this.gmtCreate = j;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public ItemsBean getItems() {
        return this.items;
    }

    public void setItems(ItemsBean itemsBean) {
        this.items = itemsBean;
    }

    public List<String> getGroupIdList() {
        return this.groupIdList;
    }

    public void setGroupIdList(List<String> list) {
        this.groupIdList = list;
    }

    public static class ItemsBean {
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
                private int Saturation = 999;
                private int Value = 999;
                private int Hue = 999;

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
