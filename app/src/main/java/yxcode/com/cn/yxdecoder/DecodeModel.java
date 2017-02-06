package yxcode.com.cn.yxdecoder;

import yxcode.com.cn.yxdecoder.base.APP;

/**
 * Created by lihaifeng on 16/12/27.
 * Desc :
 */

public class DecodeModel {

    long cost;
    APP.DECODE_MODE type;
    String content;
    String keyInfo;

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public APP.DECODE_MODE getType() {
        return type;
    }

    public void setType(APP.DECODE_MODE type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(String keyInfo) {
        this.keyInfo = keyInfo;
    }
}
