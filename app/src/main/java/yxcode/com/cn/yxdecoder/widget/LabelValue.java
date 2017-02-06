package yxcode.com.cn.yxdecoder.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lihaifeng on 16/12/28.
 * Desc :
 */

public class LabelValue extends LinearLayout {

    TextView titleView;
    TextView valueView;

    public LabelValue(Context context) {
        this(context,null);
    }

    public LabelValue(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LabelValue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setOrientation(VERTICAL);

        titleView = new TextView(context);
        titleView.setSingleLine();
        titleView.setTextSize(18);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        titleView.setTextColor(Color.BLACK);

        valueView = new TextView(context);
        valueView.setTextSize(16);
        valueView.setSingleLine(false);
        valueView.setTextColor(Color.DKGRAY);

        addView(titleView);
        addView(valueView);

        LayoutParams lp = (LayoutParams) titleView.getLayoutParams();
        lp.topMargin = 20;

        lp = (LayoutParams) valueView.getLayoutParams();
        lp.leftMargin = 20;
        lp.topMargin = 20;
        lp.rightMargin = 20;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getValueView() {
        return valueView;
    }
}
