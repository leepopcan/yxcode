package lm.com.gencode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xytxw.yangxin_core.dto.ImageDto;
import com.xytxw.yangxin_core.dto.InsideCodeDto;
import com.xytxw.yangxin_core.util.RSCoder;
import com.xytxw.yangxin_core.util.exception.DecodeFailedException;
import com.xytxw.yangxin_core.util.exception.ErrorException;
import com.xytxw.yangxin_core.yxCode.YXCode;

import java.io.UnsupportedEncodingException;

/**
 * Created by lihaifeng on 16/11/6.
 * Desc :
 */
public class EditTwoActivity extends BaseActivity {



    public static void launch(Context context){
        Intent intent = new Intent(context,EditTwoActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_two);

        final EditText outerText = (EditText) findViewById(R.id.outer_editView);
        final EditText innerText = (EditText) findViewById(R.id.inner_editView);

        findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String outerStr = outerText.getText().toString();
                String innerStr = innerText.getText().toString();
                if(TextUtils.isEmpty(innerStr)){
                    Toast.makeText(EditTwoActivity.this, innerText.getHint(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(outerStr)){
                    Toast.makeText(EditTwoActivity.this, outerText.getHint(), Toast.LENGTH_SHORT).show();
                    return;
                }
                genCode(outerStr,innerStr);
            }
        });

    }


    private void genCode(String outerStr,String innerStr){
        try {
            YXCode yxCode = YXCode.newInstanceWithInsideCode(outerStr, RSCoder.Level.per30, InsideCodeDto.newInstance(innerStr, ErrorCorrectionLevel.H, BarcodeFormat.QR_CODE));
            JumperUtils.launchResult(this,yxCode);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ErrorException e) {
            e.printStackTrace();
        } catch (DecodeFailedException e) {
            e.printStackTrace();
        }
    }
}
