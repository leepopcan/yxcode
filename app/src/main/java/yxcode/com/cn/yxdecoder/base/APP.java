package yxcode.com.cn.yxdecoder.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Rect;

/**
 * Created by yupu on 2016/5/24-10:33.
 * Email:459112332@qq.com
 */
public class APP extends Application {
    public static Context app;
    public static boolean debug = false;
    public static boolean firstDone = false;
    public static String qrResult = null,yxResult = null;

    public static final String COST = "COST";
    public static final String CONTENT = "CONTENT";
    public static final String KEYINFO = "KEYINFO";
    public static final String TYPE = "TYPE";

    public static DECODE_MODE decodeMode = DECODE_MODE.YXCODE;

    public static Rect mCropRect;

    public enum DECODE_MODE{
        YXCODE,QRCODE,FIRST,BOTH;
    }

    public static void resetResult(){
        qrResult = null;
        yxResult = null;
        firstDone = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = getApplicationContext();

    }


}
