package moe.characterrevolution.charkointelligine.Subscribe;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Subscribe 类
 * 用于描述一个订阅对象
 */
public class Subscribe {

    @JSONField(name = "url", ordinal = 1)
    private String url;
    public String getUrl() {return url; }
    public void setUrl(String url) {this.url = url;}

    @JSONField(name = "target", ordinal = 2)
    private long groupId;
    public long getGroupId() {return groupId;}
    public void setGroupId(long groupId) {this.groupId = groupId;}

    @JSONField(name = "period", ordinal = 3)
    private int period;
    public int getPeriod() {return period;}
    public void setPeriod(int period) {this.period = period;}

    public Subscribe() {}

    /**
     * 构造函数
     * @param url 订阅地址
     * @param groupId 订阅目标群
     * @param period 更新频率（分钟）
     */
    public Subscribe(String url, long groupId, int period) {
        this.url = url;
        this.groupId = groupId;
        this.period = period;
    }
}
