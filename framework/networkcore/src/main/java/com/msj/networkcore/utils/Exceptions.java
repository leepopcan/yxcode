package com.msj.networkcore.utils;

/**
 * @author mengxiangcheng
 * @date 2016/10/14 上午10:20
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc 非网络层异常处理
 */
public class Exceptions
{
    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }


}
