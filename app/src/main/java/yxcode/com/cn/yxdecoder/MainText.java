package yxcode.com.cn.yxdecoder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *  通用渐变字体
 * @author yupu
 * @date 2015年10月21日
 */
public class MainText extends TextView {
	private Shader shader;

	public MainText(Context context) {
		super(context, null);
	}
	
	public MainText(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public MainText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		shader = new LinearGradient(0, 0, 0,
				getMeasuredHeight(), Color.parseColor("#FFEC8B"),
				Color.parseColor("#DAA520"), TileMode.CLAMP);
		getPaint().setShader(shader);
		super.onDraw(canvas);
	}

}
