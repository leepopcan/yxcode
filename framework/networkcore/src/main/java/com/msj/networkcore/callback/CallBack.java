package com.msj.networkcore.callback;

/**
 * @author mengxiangcheng
 * @date 2016/10/14 上午8:53
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc
 */
public abstract class CallBack {

    public void onStart(){}

    public void onCompleted(){}

    abstract public void onError(Throwable e);

    public void onProgress(long fileSizeDownloaded){}

    abstract public void onSucess(String path, String name, long fileSize);
}
