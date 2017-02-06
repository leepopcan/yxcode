package com.msj.networkcore.constant;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @author mengxiangcheng
 * @date 2016/10/12 下午4:48
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc
 */
public class CacheConstant {

    public static final int MAX_AGE = 3;

    public static final int MAX_STALE = 60 * 60 * 24 * 3;

    public static final int CACHE_SIZE = 10 * 1024 * 1024;//10MB

    public static final String CACHE_FOLDER = "request";//缓存文件夹名

    /**
     * 获取缓存路径地址
     * @param context
     * @return
     */
    public static String getDiskCachePath(Context context){
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
            // || !Environment.isExternalStorageRemovable()
                ) {
            try {
                ///sdcard/Android/data/<application package>/cache
                //getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据  files对应设置中的清楚数据
                //通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据  cache对应设置中的清楚缓存
                cachePath = context.getExternalCacheDir().getPath();
            } catch (Exception e) {
                //getCacheDir()方法用于获取/data/data/<application package>/cache目录
                //getFilesDir()方法用于获取/data/data/<application package>/files目录
                cachePath = context.getCacheDir().getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 创建和获取缓存路径 
     *
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = getDiskCachePath(context);
        File file = new File(cachePath + File.separator + uniqueName);
        file.mkdirs();
        return file;
    }

}
