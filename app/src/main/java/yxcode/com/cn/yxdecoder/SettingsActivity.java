package yxcode.com.cn.yxdecoder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import yxcode.com.cn.yxdecoder.base.APP;
import yxcode.com.cn.yxdecoder.base.BaseActivity;

/**
 * Created by lihaifeng on 16/8/13.
 * Desc :
 */
public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox debug = (CheckBox) findViewById(R.id.debugBox);
        debug.setChecked(APP.debug);
        debug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                APP.debug = b;
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.modeSettings);

        if(APP.decodeMode == APP.DECODE_MODE.YXCODE){
            radioGroup.check(radioGroup.getChildAt(0).getId());
        } else if(APP.decodeMode == APP.DECODE_MODE.QRCODE){
            radioGroup.check(radioGroup.getChildAt(1).getId());
        } else if(APP.decodeMode == APP.DECODE_MODE.FIRST){
            radioGroup.check(radioGroup.getChildAt(2).getId());
        } else if(APP.decodeMode == APP.DECODE_MODE.BOTH){
            radioGroup.check(radioGroup.getChildAt(3).getId());
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == radioGroup.getChildAt(0).getId()) {
                    APP.decodeMode = APP.DECODE_MODE.YXCODE;
                } else if (i == radioGroup.getChildAt(1).getId()) {
                    APP.decodeMode = APP.DECODE_MODE.QRCODE;
                } else if (i == radioGroup.getChildAt(2).getId()) {
                    APP.decodeMode = APP.DECODE_MODE.FIRST;
                } else if (i == radioGroup.getChildAt(3).getId()) {
                    APP.decodeMode = APP.DECODE_MODE.BOTH;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_login){
            startActivity(new Intent(this,ReDecodeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.redecode, menu);
        return true;
    }
}
