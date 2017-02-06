package lm.com.gencode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Toast;

import com.xytxw.yangxin_core.dto.ImageDto;
import com.xytxw.yangxin_core.yxCode.YXCode;

import lm.com.gencode.account.AccountManager;
import lm.com.gencode.network.IService;
import lm.com.gencode.network.model.ResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lihaifeng on 16/11/6.
 * Desc :
 */
public class JumperUtils {

    public static void launchResult(final Context context, YXCode yxCode){
        if(null == yxCode){
            Toast.makeText(context, "生成失败,请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        final ImageDto imageDto = yxCode.paint();
        new Thread(){
            @Override
            public void run() {


                final Bitmap bitmap = Bitmap.createBitmap(imageDto.getWidth(),imageDto.getHeight(), Bitmap.Config.ARGB_8888);
                for(int j = 0;j<imageDto.getHeight();j++){
                    for(int i = 0;i<imageDto.getWidth();i++){
                        if(imageDto.getValue(i,j)){
                            bitmap.setPixel(i,j, Color.BLACK);
                        }
                    }
                }

                GenResultActivity.launch(context,bitmap);

            }
        }.start();
    }



}
