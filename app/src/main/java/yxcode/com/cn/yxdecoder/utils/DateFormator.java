package yxcode.com.cn.yxdecoder.utils;

import java.text.SimpleDateFormat;

/**
 * Created by lihaifeng on 16/8/18.
 * Desc :
 */
public class DateFormator {
    private static SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd.HH-mm-ss");

    public static String getFileName(){
        return format.format(System.currentTimeMillis());
    }



    public static String formTime(long time){
        return format.format(time);
    }
}
