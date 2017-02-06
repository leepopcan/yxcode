package com.msj.networkcore.mvp.bean;


import protoarchi.mvc.IModel;

/**
 * @author mengxiangcheng
 * @date 2016/10/12 下午2:53
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc
 */
public class ResponseHeader implements IModel {

    private String repcd;

    private String repmsg;

    private String MOBILETOKEN;

    public String getRepcd() {
        return repcd;
    }

    public void setRepcd(String repcd) {
        this.repcd = repcd;
    }

    public String getRepmsg() {
        return repmsg;
    }

    public void setRepmsg(String repmsg) {
        this.repmsg = repmsg;
    }

    public String getMOBILETOKEN() {
        return MOBILETOKEN;
    }

    public void setMOBILETOKEN(String MOBILETOKEN) {
        this.MOBILETOKEN = MOBILETOKEN;
    }
}
