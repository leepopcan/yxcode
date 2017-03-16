package yxcode.com.cn.yxdecoder.gencode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;

import com.google.zxing.common.BitMatrix;


/**
 * Created by lihaifeng on 16/11/6.
 * Desc :
 */
public class JumperUtils {




    public static void launchResult(final Context context, final BitMatrix bitMatrix){
        if(null == bitMatrix){
            Toast.makeText(context, "生成失败,请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(){
            @Override
            public void run() {
                final Bitmap bitmap = Bitmap.createBitmap(bitMatrix.getWidth(),bitMatrix.getHeight(), Bitmap.Config.ARGB_8888);
                for(int j = 0;j<bitMatrix.getHeight();j++){
                    for(int i = 0;i<bitMatrix.getWidth();i++){
                        if(bitMatrix.get(i,j)){
                            bitmap.setPixel(i,j, Color.BLACK);
                        }
                    }
                }
                GenResultActivity.launch(context,bitmap);
            }
        }.start();
    }

    public static void launchResult(final Context context, final BitMatrix bitMatrix, final String content, final String innerCodeType, final String keyInfo){
        if(null == bitMatrix){
            Toast.makeText(context, "生成失败,请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(){
            @Override
            public void run() {

                final Bitmap bitmap = Bitmap.createBitmap(bitMatrix.getWidth(),bitMatrix.getHeight(), Bitmap.Config.ARGB_8888);
                for(int j = 0;j<bitMatrix.getHeight();j++){
                    for(int i = 0;i<bitMatrix.getWidth();i++){
                        if(bitMatrix.get(i,j)){
                            bitmap.setPixel(i,j, Color.BLACK);
                        }
                    }
                }
                GenResultActivity.launch(context,bitmap,content,innerCodeType,keyInfo);

            }
        }.start();
    }
}
