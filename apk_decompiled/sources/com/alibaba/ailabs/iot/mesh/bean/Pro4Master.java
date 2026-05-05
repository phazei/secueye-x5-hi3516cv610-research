package com.alibaba.ailabs.iot.mesh.bean;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Pro4Master {
    public String api;
    public DataBean data;
    public List<String> ret;
    public String v;

    public static class DataBean {
        public ModelBean model;
        public String msgCode;
        public String msgInfo;
        public String statusCode;
        public String success;

        public static class ModelBean {
            public Object activePayload;
            public List<AddModelClientBean> addModelClient;
            public int nodeMaxSize;
            public int provisionerAddr;
            public List<ShareIotDevListBean> shareIotDevList;
            public List<SigmeshIotDevListBean> sigmeshIotDevList;
            public List<SigmeshKeysBeanXX> sigmeshKeys;
            public List<SubscribeGroupAddrBean> subscribeGroupAddr;

            public static class AddModelClientBean {
                public int modelId;
                public List<Integer> statusOpcodes;

                public int getModelId() {
                    return this.modelId;
                }

                public List<Integer> getStatusOpcodes() {
                    return this.statusOpcodes;
                }

                public void setModelId(int i) {
                    this.modelId = i;
                }

                public void setStatusOpcodes(List<Integer> list) {
                    this.statusOpcodes = list;
                }
            }

            public static class ShareIotDevListBean {
                public Object appKey;
                public int appKeyIndex;
                public String devId;
                public Object deviceKey;
                public Object deviceVersion;
                public Object mac;
                public Object netKey;
                public Object netKeyIndex;
                public String productKey;
                public Object secret;
                public List<SigmeshKeysBean> sigmeshKeys;
                public int unicastaddress;

                public static class SigmeshKeysBean {
                    public List<ProvisionAppKeysBean> provisionAppKeys;
                    public ProvisionNetKeyBean provisionNetKey;

                    public static class ProvisionAppKeysBean {
                        public String appKey;
                        public int appKeyIndex;

                        public String getAppKey() {
                            return this.appKey;
                        }

                        public int getAppKeyIndex() {
                            return this.appKeyIndex;
                        }

                        public void setAppKey(String str) {
                            this.appKey = str;
                        }

                        public void setAppKeyIndex(int i) {
                            this.appKeyIndex = i;
                        }
                    }

                    public static class ProvisionNetKeyBean {
                        public String netKey;
                        public int netKeyIndex;

                        public String getNetKey() {
                            return this.netKey;
                        }

                        public int getNetKeyIndex() {
                            return this.netKeyIndex;
                        }

                        public void setNetKey(String str) {
                            this.netKey = str;
                        }

                        public void setNetKeyIndex(int i) {
                            this.netKeyIndex = i;
                        }
                    }

                    public List<ProvisionAppKeysBean> getProvisionAppKeys() {
                        return this.provisionAppKeys;
                    }

                    public ProvisionNetKeyBean getProvisionNetKey() {
                        return this.provisionNetKey;
                    }

                    public void setProvisionAppKeys(List<ProvisionAppKeysBean> list) {
                        this.provisionAppKeys = list;
                    }

                    public void setProvisionNetKey(ProvisionNetKeyBean provisionNetKeyBean) {
                        this.provisionNetKey = provisionNetKeyBean;
                    }
                }

                public Object getAppKey() {
                    return this.appKey;
                }

                public int getAppKeyIndex() {
                    return this.appKeyIndex;
                }

                public String getDevId() {
                    return this.devId;
                }

                public Object getDeviceKey() {
                    return this.deviceKey;
                }

                public Object getDeviceVersion() {
                    return this.deviceVersion;
                }

                public Object getMac() {
                    return this.mac;
                }

                public Object getNetKey() {
                    return this.netKey;
                }

                public Object getNetKeyIndex() {
                    return this.netKeyIndex;
                }

                public String getProductKey() {
                    return this.productKey;
                }

                public Object getSecret() {
                    return this.secret;
                }

                public List<SigmeshKeysBean> getSigmeshKeys() {
                    return this.sigmeshKeys;
                }

                public int getUnicastaddress() {
                    return this.unicastaddress;
                }

                public void setAppKey(Object obj) {
                    this.appKey = obj;
                }

                public void setAppKeyIndex(int i) {
                    this.appKeyIndex = i;
                }

                public void setDevId(String str) {
                    this.devId = str;
                }

                public void setDeviceKey(Object obj) {
                    this.deviceKey = obj;
                }

                public void setDeviceVersion(Object obj) {
                    this.deviceVersion = obj;
                }

                public void setMac(Object obj) {
                    this.mac = obj;
                }

                public void setNetKey(Object obj) {
                    this.netKey = obj;
                }

                public void setNetKeyIndex(Object obj) {
                    this.netKeyIndex = obj;
                }

                public void setProductKey(String str) {
                    this.productKey = str;
                }

                public void setSecret(Object obj) {
                    this.secret = obj;
                }

                public void setSigmeshKeys(List<SigmeshKeysBean> list) {
                    this.sigmeshKeys = list;
                }

                public void setUnicastaddress(int i) {
                    this.unicastaddress = i;
                }
            }

            public static class SigmeshIotDevListBean {
                public Object appKey;
                public int appKeyIndex;
                public String devId;
                public Object deviceKey;
                public Object deviceVersion;
                public Object mac;
                public Object netKey;
                public Object netKeyIndex;
                public String productKey;
                public Object secret;
                public List<SigmeshKeysBeanX> sigmeshKeys;
                public int unicastaddress;

                public static class SigmeshKeysBeanX {
                    public List<ProvisionAppKeysBeanX> provisionAppKeys;
                    public ProvisionNetKeyBeanX provisionNetKey;

                    public static class ProvisionAppKeysBeanX {
                        public String appKey;
                        public int appKeyIndex;

                        public String getAppKey() {
                            return this.appKey;
                        }

                        public int getAppKeyIndex() {
                            return this.appKeyIndex;
                        }

                        public void setAppKey(String str) {
                            this.appKey = str;
                        }

                        public void setAppKeyIndex(int i) {
                            this.appKeyIndex = i;
                        }
                    }

                    public static class ProvisionNetKeyBeanX {
                        public String netKey;
                        public int netKeyIndex;

                        public String getNetKey() {
                            return this.netKey;
                        }

                        public int getNetKeyIndex() {
                            return this.netKeyIndex;
                        }

                        public void setNetKey(String str) {
                            this.netKey = str;
                        }

                        public void setNetKeyIndex(int i) {
                            this.netKeyIndex = i;
                        }
                    }

                    public List<ProvisionAppKeysBeanX> getProvisionAppKeys() {
                        return this.provisionAppKeys;
                    }

                    public ProvisionNetKeyBeanX getProvisionNetKey() {
                        return this.provisionNetKey;
                    }

                    public void setProvisionAppKeys(List<ProvisionAppKeysBeanX> list) {
                        this.provisionAppKeys = list;
                    }

                    public void setProvisionNetKey(ProvisionNetKeyBeanX provisionNetKeyBeanX) {
                        this.provisionNetKey = provisionNetKeyBeanX;
                    }
                }

                public Object getAppKey() {
                    return this.appKey;
                }

                public int getAppKeyIndex() {
                    return this.appKeyIndex;
                }

                public String getDevId() {
                    return this.devId;
                }

                public Object getDeviceKey() {
                    return this.deviceKey;
                }

                public Object getDeviceVersion() {
                    return this.deviceVersion;
                }

                public Object getMac() {
                    return this.mac;
                }

                public Object getNetKey() {
                    return this.netKey;
                }

                public Object getNetKeyIndex() {
                    return this.netKeyIndex;
                }

                public String getProductKey() {
                    return this.productKey;
                }

                public Object getSecret() {
                    return this.secret;
                }

                public List<SigmeshKeysBeanX> getSigmeshKeys() {
                    return this.sigmeshKeys;
                }

                public int getUnicastaddress() {
                    return this.unicastaddress;
                }

                public void setAppKey(Object obj) {
                    this.appKey = obj;
                }

                public void setAppKeyIndex(int i) {
                    this.appKeyIndex = i;
                }

                public void setDevId(String str) {
                    this.devId = str;
                }

                public void setDeviceKey(Object obj) {
                    this.deviceKey = obj;
                }

                public void setDeviceVersion(Object obj) {
                    this.deviceVersion = obj;
                }

                public void setMac(Object obj) {
                    this.mac = obj;
                }

                public void setNetKey(Object obj) {
                    this.netKey = obj;
                }

                public void setNetKeyIndex(Object obj) {
                    this.netKeyIndex = obj;
                }

                public void setProductKey(String str) {
                    this.productKey = str;
                }

                public void setSecret(Object obj) {
                    this.secret = obj;
                }

                public void setSigmeshKeys(List<SigmeshKeysBeanX> list) {
                    this.sigmeshKeys = list;
                }

                public void setUnicastaddress(int i) {
                    this.unicastaddress = i;
                }
            }

            public static class SigmeshKeysBeanXX {
                public List<ProvisionAppKeysBeanXX> provisionAppKeys;
                public ProvisionNetKeyBeanXX provisionNetKey;

                public static class ProvisionAppKeysBeanXX {
                    public String appKey;
                    public int appKeyIndex;

                    public String getAppKey() {
                        return this.appKey;
                    }

                    public int getAppKeyIndex() {
                        return this.appKeyIndex;
                    }

                    public void setAppKey(String str) {
                        this.appKey = str;
                    }

                    public void setAppKeyIndex(int i) {
                        this.appKeyIndex = i;
                    }
                }

                public static class ProvisionNetKeyBeanXX {
                    public String netKey;
                    public int netKeyIndex;

                    public String getNetKey() {
                        return this.netKey;
                    }

                    public int getNetKeyIndex() {
                        return this.netKeyIndex;
                    }

                    public void setNetKey(String str) {
                        this.netKey = str;
                    }

                    public void setNetKeyIndex(int i) {
                        this.netKeyIndex = i;
                    }
                }

                public List<ProvisionAppKeysBeanXX> getProvisionAppKeys() {
                    return this.provisionAppKeys;
                }

                public ProvisionNetKeyBeanXX getProvisionNetKey() {
                    return this.provisionNetKey;
                }

                public void setProvisionAppKeys(List<ProvisionAppKeysBeanXX> list) {
                    this.provisionAppKeys = list;
                }

                public void setProvisionNetKey(ProvisionNetKeyBeanXX provisionNetKeyBeanXX) {
                    this.provisionNetKey = provisionNetKeyBeanXX;
                }
            }

            public static class SubscribeGroupAddrBean {
                public int appKeyIndex;
                public int groupAddr;
                public int modelElementAddr;
                public int modelId;
                public int netKeyIndex;
                public Object opcode;
                public Object statusOpcode;

                public int getAppKeyIndex() {
                    return this.appKeyIndex;
                }

                public int getGroupAddr() {
                    return this.groupAddr;
                }

                public int getModelElementAddr() {
                    return this.modelElementAddr;
                }

                public int getModelId() {
                    return this.modelId;
                }

                public int getNetKeyIndex() {
                    return this.netKeyIndex;
                }

                public Object getOpcode() {
                    return this.opcode;
                }

                public Object getStatusOpcode() {
                    return this.statusOpcode;
                }

                public void setAppKeyIndex(int i) {
                    this.appKeyIndex = i;
                }

                public void setGroupAddr(int i) {
                    this.groupAddr = i;
                }

                public void setModelElementAddr(int i) {
                    this.modelElementAddr = i;
                }

                public void setModelId(int i) {
                    this.modelId = i;
                }

                public void setNetKeyIndex(int i) {
                    this.netKeyIndex = i;
                }

                public void setOpcode(Object obj) {
                    this.opcode = obj;
                }

                public void setStatusOpcode(Object obj) {
                    this.statusOpcode = obj;
                }
            }

            public Object getActivePayload() {
                return this.activePayload;
            }

            public List<AddModelClientBean> getAddModelClient() {
                return this.addModelClient;
            }

            public int getNodeMaxSize() {
                return this.nodeMaxSize;
            }

            public int getProvisionerAddr() {
                return this.provisionerAddr;
            }

            public List<ShareIotDevListBean> getShareIotDevList() {
                return this.shareIotDevList;
            }

            public List<SigmeshIotDevListBean> getSigmeshIotDevList() {
                return this.sigmeshIotDevList;
            }

            public List<SigmeshKeysBeanXX> getSigmeshKeys() {
                return this.sigmeshKeys;
            }

            public List<SubscribeGroupAddrBean> getSubscribeGroupAddr() {
                return this.subscribeGroupAddr;
            }

            public void setActivePayload(Object obj) {
                this.activePayload = obj;
            }

            public void setAddModelClient(List<AddModelClientBean> list) {
                this.addModelClient = list;
            }

            public void setNodeMaxSize(int i) {
                this.nodeMaxSize = i;
            }

            public void setProvisionerAddr(int i) {
                this.provisionerAddr = i;
            }

            public void setShareIotDevList(List<ShareIotDevListBean> list) {
                this.shareIotDevList = list;
            }

            public void setSigmeshIotDevList(List<SigmeshIotDevListBean> list) {
                this.sigmeshIotDevList = list;
            }

            public void setSigmeshKeys(List<SigmeshKeysBeanXX> list) {
                this.sigmeshKeys = list;
            }

            public void setSubscribeGroupAddr(List<SubscribeGroupAddrBean> list) {
                this.subscribeGroupAddr = list;
            }
        }

        public ModelBean getModel() {
            return this.model;
        }

        public String getMsgCode() {
            return this.msgCode;
        }

        public String getMsgInfo() {
            return this.msgInfo;
        }

        public String getStatusCode() {
            return this.statusCode;
        }

        public String getSuccess() {
            return this.success;
        }

        public void setModel(ModelBean modelBean) {
            this.model = modelBean;
        }

        public void setMsgCode(String str) {
            this.msgCode = str;
        }

        public void setMsgInfo(String str) {
            this.msgInfo = str;
        }

        public void setStatusCode(String str) {
            this.statusCode = str;
        }

        public void setSuccess(String str) {
            this.success = str;
        }
    }

    public String getApi() {
        return this.api;
    }

    public DataBean getData() {
        return this.data;
    }

    public List<String> getRet() {
        return this.ret;
    }

    public String getV() {
        return this.v;
    }

    public void setApi(String str) {
        this.api = str;
    }

    public void setData(DataBean dataBean) {
        this.data = dataBean;
    }

    public void setRet(List<String> list) {
        this.ret = list;
    }

    public void setV(String str) {
        this.v = str;
    }
}
