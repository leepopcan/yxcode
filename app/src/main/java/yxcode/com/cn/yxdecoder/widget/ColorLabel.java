package yxcode.com.cn.yxdecoder.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import yxcode.com.cn.yxdecoder.R;
import yxcode.com.cn.yxdecoder.utils.DisplayUtil;


/**
 * Created by lihaifeng on 16/11/19.
 * Desc :
 */
public class ColorLabel extends LinearLayout {

    TextView textView;
    CharSequence label;
    View colorView;
    int color;

    public ColorLabel(Context context) {
        this(context,null);
    }

    public ColorLabel(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ColorLabel, defStyleAttr, 0);
        label = a.getText(R.styleable.ColorLabel_label);
        color = a.getColor(R.styleable.ColorLabel_markColor,getResources().getColor(R.color.colorAccent));
        a.recycle();
    }

    private void init(Context context){
        setGravity(Gravity.TOP);
        textView = new TextView(context);
        textView.setTextSize(20);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        colorView = new View(context);
        colorView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        addView(colorView,new LayoutParams(DisplayUtil.px2dip(200f,context), DisplayUtil.px2dip(40f,context)));
        addView(textView);
    }

    public TextView getTextView() {
        return textView;
    }
}
