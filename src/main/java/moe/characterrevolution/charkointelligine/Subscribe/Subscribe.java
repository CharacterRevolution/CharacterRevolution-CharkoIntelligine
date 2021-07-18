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

    @JSONField(name = "overwrite", ordinal = 4)
    private int overwrite;
    public int getOverwrite() {return overwrite;}
    public void setOverwrite(int overwrite) {this.overwrite = overwrite;}

    public Subscribe() {}

    /**
     * 构造函数
     * @param url 订阅地址
     * @param groupId 订阅目标群
     * @param period 更新频率（分钟）
     * @param overwrite 是否复写相同词条（0表示不覆写，1表示合并，2表示覆写）
     */
    public Subscribe(String url, long groupId, int period, int overwrite) {
        this.url = url;
        this.groupId = groupId;
        this.period = period;
        this.overwrite = overwrite;
    }
}
