package redisTest.fakeWebRetailer;

/**
 * Created by 18435 on 2018/6/28.
 * 商品
 */
public class Good {
    private String goodId;
    private int total;

    public Good(String goodId, int total) {
        this.goodId = goodId;
        this.total = total;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
