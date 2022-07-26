package top.strelitzia.model;

/**
 * @author strelitzia
 * @Date 2022/06/13 16:08
 * 用户塔罗牌当日抽取数量
 **/

public class TarotInfo {
    private long qq;
    private Integer tarotCount;
    private String tarotCard1;
    private String tarotCard2;
    private String tarotCard3;
    private Integer tarotCard1Position;
    private Integer tarotCard2Position;
    private Integer tarotCard3Position;


    public long getQq() {
        return qq;
    }

    public void setQq(long qq) {
        this.qq = qq;
    }

    public Integer getTarotCount() {
        return tarotCount;
    }

    public void setTarotCount(Integer tarotCount) {
        this.tarotCount = tarotCount;
    }

    public String getTarotCard1() {
        return tarotCard1;
    }

    public void setTarotCard1(String tarotCard1) {
        this.tarotCard1 = tarotCard1;
    }

    public String getTarotCard2() {
        return tarotCard2;
    }

    public void setTarotCard2(String tarotCard2) {
        this.tarotCard2 = tarotCard2;
    }

    public String getTarotCard3() {
        return tarotCard3;
    }

    public void setTarotCard3(String tarotCard3) {
        this.tarotCard3 = tarotCard3;
    }

    public Integer getTarotCard1Position() { return tarotCard1Position; }

    public void setTarotCard1Position(Integer tarotCard1Position) { this.tarotCard1Position = tarotCard1Position; }

    public Integer getTarotCard2Position() { return tarotCard2Position; }

    public void setTarotCard2Position(Integer tarotCard2Position) { this.tarotCard2Position = tarotCard2Position; }

    public Integer getTarotCard3Position() { return tarotCard3Position; }

    public void setTarotCard3Position(Integer tarotCard3Position) { this.tarotCard3Position = tarotCard3Position; }
}
