package yxcode.com.cn.yxdecoder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xytxw.yangxin_android.ImgForYUVByteArr;
import com.xytxw.yangxin_core.dto.DecodeDto;
import com.xytxw.yangxin_core.util.Decoder;
import com.xytxw.yangxin_core.util.exception.DecodeFailedException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import dtr.zxing.camera.CameraManager;
import dtr.zxing.utils.BeepManager;
import yxcode.com.cn.yxdecoder.base.BaseActivity;
import yxcode.com.cn.yxdecoder.utils.DateFormator;
import yxcode.com.cn.yxdecoder.utils.FileUtil;
import yxcode.com.cn.yxdecoder.utils.PreferencesUtils;

/**
 * Created by lihaifeng on 16/9/14.
 * Desc :
 */
public class ReDecodeActivity extends BaseActivity {

    private static final int REQ_CODE = 1234;

    private int MODE_CRASH = 0;
    private int MODE_FAILED = 1;
    int mode = 0;

    private int width,height;

    private TextView result,crashName,failedName;
    private BeepManager beepManager;
    private Bitmap bitmap;
    private ImageView crashImage,failedImage;


    ProgressDialog progressDialog;
    String message = "";

    private void showDialog(){
        hideDialog();
        progressDialog = ProgressDialog.show(this,"稍等","正在解码...");
    }

    private void hideDialog(){
        if(null != progressDialog && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(PreferencesUtils.getInt(getApplicationContext(), CameraManager.preWidth) == PreferencesUtils.DEFAULT_INT ||
                PreferencesUtils.getInt(getApplicationContext(), CameraManager.preHeight) == PreferencesUtils.DEFAULT_INT	){
            Toast.makeText(getApplicationContext(),"请先正常扫描,采集摄像头参数",Toast.LENGTH_LONG).show();
            finish();
        }

        beepManager = new BeepManager(this);
        width = PreferencesUtils.getInt(getApplicationContext(),CameraManager.preWidth);
        height = PreferencesUtils.getInt(getApplicationContext(),CameraManager.preHeight);

        setContentView(R.layout.activity_redecode);

        setTitle("crash重新扫描");


        result = (TextView) findViewById(R.id.result);
        crashName = (TextView) findViewById(R.id.crashName);
        failedName = (TextView) findViewById(R.id.failedName);
        crashImage = (ImageView) findViewById(R.id.crash_image);
        failedImage = (ImageView) findViewById(R.id.failed_image);
        crashImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = MODE_CRASH;
                startActivityForResult(new Intent(ReDecodeActivity.this,CrashImageSelectActivity.class),REQ_CODE);
            }
        });
        failedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = MODE_FAILED;
                startActivityForResult(new Intent(ReDecodeActivity.this,FailedImageSelectActivity.class),REQ_CODE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialog();
        recycleBitmap();
    }

    private void recycleBitmap(){
        if(null != bitmap && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
    }


    private String fileName;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            if(mode == MODE_CRASH){
                fileName = data.getStringExtra("SELECT_KEY");

                if(TextUtils.isEmpty(fileName)){
                    return;
                }
                File f = new File(FileUtil.CRASH_PATH + fileName+".dat");
                if(!f.exists()){
                    Toast.makeText(getApplicationContext(),"file "+f.getName()+" not exist!",Toast.LENGTH_LONG).show();;
                    return;
                }

                recycleBitmap();
                bitmap = BitmapFactory.decodeFile(FileUtil.CRASH_PATH+fileName+".jpg");
                crashImage.setImageBitmap(bitmap);
                crashName.setText(fileName+".jpg");
                showDialog();
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            reDecode(FileUtil.readFileByBytes(FileUtil.CRASH_PATH+fileName+".dat"));
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();
            } else if(mode == MODE_FAILED){

                fileName = data.getStringExtra("SELECT_KEY");

                if(TextUtils.isEmpty(fileName)){
                    return;
                }
                File f = new File(FileUtil.FAILED_PATH + fileName+".dat");
                if(!f.exists()){
                    Toast.makeText(getApplicationContext(),"file "+f.getName()+" not exist!",Toast.LENGTH_LONG).show();;
                    return;
                }

                recycleBitmap();
                bitmap = BitmapFactory.decodeFile(FileUtil.FAILED_PATH+fileName+".jpg");

                failedImage.setImageBitmap(bitmap);
                failedName.setText(fileName+".jpg");


                showDialog();
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            reDecode(FileUtil.readFileByBytes(FileUtil.FAILED_PATH+fileName+".dat"));
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();
            }


        }
    }


    private void reDecode(byte[] data){
        DecodeDto decodeDto = null;
        ImgForYUVByteArr source = buildLuminanceSourceforYxCode(data, height, width);
        if (source != null) {
            try {
                long time = System.currentTimeMillis();
                if(mode == MODE_CRASH){
                    FileUtil.writeStrToFile(FileUtil.CRASH_PATH, DateFormator.formTime(time) + "----> width:" +source.getWidth()+"  height "+source.getHeight()+" \n "+ Arrays.toString(source.getBinaryArr()));
                } else if(mode == MODE_FAILED){
                    FileUtil.writeStrToFile(FileUtil.FAILED_PATH, DateFormator.formTime(time) + "----> width:" +source.getWidth()+"  height "+source.getHeight()+" \n "+ Arrays.toString(source.getBinaryArr()));
                }

                decodeDto = Decoder.newInstance().decode(source);

                if(mode == MODE_CRASH){
                    FileUtil.deleteLog(FileUtil.CRASH_PATH);
                } else if(mode == MODE_FAILED){
                    FileUtil.deleteLog(FileUtil.FAILED_PATH);
                }

            } catch (final DecodeFailedException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(e.getMessage());
                        hideDialog();
                    }
                });

                return;
            }
        }

        if(null == decodeDto){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.setText("null decodeDto");
                    hideDialog();
                }
            });
            return;
        }

        beepManager.playBeepSoundAndVibrate();
        if (!decodeDto.isError()) {
            message = decodeDto.getRes();
        } else {
            message = decodeDto.getErrorMsg();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.setText(message);
                hideDialog();
            }
        });
    }

    public ImgForYUVByteArr buildLuminanceSourceforYxCode(byte[] data, int width, int height) {
        int x = PreferencesUtils.getInt(getApplicationContext(),"rect_x");
        int y = PreferencesUtils.getInt(getApplicationContext(),"rect_y");
        int _width = PreferencesUtils.getInt(getApplicationContext(),"rect_w");
        int _height = PreferencesUtils.getInt(getApplicationContext(),"rect_h");
        return new ImgForYUVByteArr(data, width, height, x, y, _width, _height, false);
    }

}
