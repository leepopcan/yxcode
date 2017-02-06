package lm.com.gencode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lihaifeng on 16/11/6.
 * Desc :
 */
public class ResultImage extends View {

    Bitmap bitmap;

    public ResultImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);

    }

    Paint blackPaint;
    Paint whitePaint;
    int desteny = 30;

    public void drawBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        desteny = getContext().getResources().getDisplayMetrics().widthPixels / bitmap.getWidth();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null == bitmap){
            return;
        }

        for(int i = 0;i<bitmap.getWidth();i++){
            for(int j = 0;j<bitmap.getHeight();j++){
                int value = bitmap.getPixel(i,j);
                if(value == Color.BLACK){
                    canvas.drawRect(i * desteny,j * desteny,(i + 1) * desteny,(j + 1) * desteny,blackPaint);
                } else {
                    canvas.drawRect(i * desteny,j * desteny,(i + 1) * desteny,(j + 1) * desteny,whitePaint);
                }
            }
        }
    }
}
