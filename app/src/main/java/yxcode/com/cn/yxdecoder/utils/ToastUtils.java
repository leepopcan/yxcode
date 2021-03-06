package yxcode.com.cn.yxdecoder.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class ToastUtils {
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void mkLongTimeToast(Context context, String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mkShortTimeToast(Context context, String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param text     内容string
     * @param duration 时长
     * @return void 返回类型
     * @Title mkToast
     * @Description 自定义toast内容和时长
     */
    public static void mkToast(Context mContext, String text, int duration) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mHandler.postDelayed(r, duration);

        mToast.show();
    }

    /**
     * @param resId    内容string id
     * @param duration 时长
     * @return void 返回类型
     * @Title mkToast
     * @Description 自定义toast内容和时长
     */
    public static void mkToast(Context mContext, int resId, int duration) {
        String text = mContext.getResources().getString(resId);
        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mHandler.postDelayed(r, duration);

        mToast.show();
    }
}
