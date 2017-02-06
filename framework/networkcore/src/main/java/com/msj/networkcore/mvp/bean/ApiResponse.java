package com.msj.networkcore.mvp.bean;

import protoarchi.mvc.IModel;

/**
 * @author mengxiangcheng
 * @date 2016/10/12 下午3:14
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc
 */
public class ApiResponse<T> implements IModel {

    private ResponseHeader header;

    private T data;

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return "".equals(header.getRepcd());
    }
}
