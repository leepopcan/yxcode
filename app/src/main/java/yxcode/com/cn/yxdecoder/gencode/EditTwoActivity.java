package yxcode.com.cn.yxdecoder.gencode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.yxcode.util.G;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import yxcode.com.cn.yxdecoder.R;
import yxcode.com.cn.yxdecoder.base.BaseActivity;

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
                JumperUtils.launchResult(EditTwoActivity.this,testGenLevelYXCode(outerStr,innerStr));
            }
        });

    }


    public BitMatrix testGenLevelYXCode(String outerStr,String innerStr) {
            int width = 300;
            int height = 300;
            String yxcodeFormat = "png";
            HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            hints.put(EncodeHintType.YX_INSIDE_CODE_TYPE, G.InsideCodeType.QR);
            // hints.put(EncodeHintType.YX_INSIDE_CODE_TYPE,
            // InsideCodeType.Atezc);
            // hints.put(EncodeHintType.YX_INSIDE_CODE_TYPE,
            // InsideCodeType.DataMatrix);

            hints.put(EncodeHintType.YX_CODE_TYPE, G.YXCodeType.levelInfo);
            hints.put(EncodeHintType.YX_INSIDE_CODE_CONTENT, innerStr);
        try {
            return new MultiFormatWriter().encode(outerStr, BarcodeFormat.YX_CODE, width, height,
                    hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
//			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//			File codeFile = new File(path);
//			if (codeFile.exists()) {
//				codeFile.delete();
//			}
//			ImageIO.write(image, yxcodeFormat, codeFile);
//			MatrixToImageWriter.writeToPath(bitMatrix, yxcodeFormat, codeFile.toPath());
//			Desktop desktop = Desktop.getDesktop();
//			desktop.open(codeFile);
	}
}
