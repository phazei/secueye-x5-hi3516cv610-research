package bean;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class yunCloudListBean {
    private List<OrderListBean> orderList;
    private int pageCount;
    private int pageNo;
    private int pageSize;
    private int total;

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int i) {
        this.pageCount = i;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int i) {
        this.total = i;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int i) {
        this.pageNo = i;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int i) {
        this.pageSize = i;
    }

    public List<OrderListBean> getOrderList() {
        return this.orderList;
    }

    public void setOrderList(List<OrderListBean> list) {
        this.orderList = list;
    }

    public static class OrderListBean {
        private String commodityCode;
        private int commodityType;
        private int copies;
        private String endTime;
        private String endTimeUTC;
        private int expired;
        private String orderId;
        private String outOrderNo;
        private int paymentStatus;
        private String price;
        private String specification;
        private String startTime;
        private String startTimeUTC;
        private String userName;

        public String getEndTimeUTC() {
            return this.endTimeUTC;
        }

        public void setEndTimeUTC(String str) {
            this.endTimeUTC = str;
        }

        public String getOrderId() {
            return this.orderId;
        }

        public void setOrderId(String str) {
            this.orderId = str;
        }

        public String getSpecification() {
            return this.specification;
        }

        public void setSpecification(String str) {
            this.specification = str;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String str) {
            this.userName = str;
        }

        public int getCommodityType() {
            return this.commodityType;
        }

        public void setCommodityType(int i) {
            this.commodityType = i;
        }

        public String getOutOrderNo() {
            return this.outOrderNo;
        }

        public void setOutOrderNo(String str) {
            this.outOrderNo = str;
        }

        public int getCopies() {
            return this.copies;
        }

        public void setCopies(int i) {
            this.copies = i;
        }

        public int getExpired() {
            return this.expired;
        }

        public void setExpired(int i) {
            this.expired = i;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String str) {
            this.price = str;
        }

        public String getCommodityCode() {
            return this.commodityCode;
        }

        public void setCommodityCode(String str) {
            this.commodityCode = str;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public void setStartTime(String str) {
            this.startTime = str;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setEndTime(String str) {
            this.endTime = str;
        }

        public int getPaymentStatus() {
            return this.paymentStatus;
        }

        public void setPaymentStatus(int i) {
            this.paymentStatus = i;
        }

        public String getStartTimeUTC() {
            return this.startTimeUTC;
        }

        public void setStartTimeUTC(String str) {
            this.startTimeUTC = str;
        }
    }
}
