package lm.com.gencode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xytxw.yangxin_core.dto.InsideCodeDto;
import com.xytxw.yangxin_core.util.RSCoder;
import com.xytxw.yangxin_core.util.exception.DecodeFailedException;
import com.xytxw.yangxin_core.util.exception.ErrorException;
import com.xytxw.yangxin_core.yxCode.YXCode;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

import lm.com.gencode.account.AccountManager;
import lm.com.gencode.network.IService;
import lm.com.gencode.network.model.ApplyModel;
import lm.com.gencode.network.model.ResponseModel;

/**
 * Created by lihaifeng on 16/11/6.
 * Desc :
 */
public class EditOneActivity extends BaseActivity {

    public static final String TITLE = "TITLE";
    public static final String TYPE = "TYPE";
    public static final int TYPE_COMMON = 0;
    public static final int TYPE_INNER = 1;
    private int type =0;

    LinearLayout moreInfo;
    Spinner errorLevel;
    RadioGroup radioGroup;

    public static void launch(Context context, int type,String title){
        Intent intent = new Intent(context,EditOneActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(TYPE,type);
        intent.putExtra(TITLE,title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_one);
        type = getIntent().getIntExtra(TYPE,TYPE_COMMON);
        moreInfo = (LinearLayout) findViewById(R.id.moreInfo);

        if(AccountManager.isLogin(getApplicationContext())){
            moreInfo.setVisibility(View.VISIBLE);
            radioGroup = (RadioGroup) findViewById(R.id.moreInfo);
            errorLevel = (Spinner) findViewById(R.id.errorLevel);

            //适配器
            ArrayAdapter<CharSequence> arr_adapter= new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, ErrorCorrectionLevel.values());
            //设置样式
            arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //加载适配器
            errorLevel.setAdapter(arr_adapter);
        }

        setTitle(getIntent().getStringExtra(TITLE));
        final EditText et = (EditText) findViewById(R.id.editView);

        findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = et.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(EditOneActivity.this, et.getHint(), Toast.LENGTH_SHORT).show();
                    return;
                }

                genCode(content);
            }
        });
    }

    private void genCode(String content){

        if(type == TYPE_COMMON){
            try {
                YXCode yxCode = YXCode.newInstance(content, RSCoder.Level.per30, 0);
                JumperUtils.launchResult(this,yxCode);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ErrorException e) {
                e.printStackTrace();
            }
        } else if(type == TYPE_INNER){
            try {
                ErrorCorrectionLevel errorCorrectionLevel = (ErrorCorrectionLevel) errorLevel.getSelectedItem();


                InsideCodeDto insideCode = null;

                int checkId = radioGroup.getCheckedRadioButtonId();
                if(checkId == radioGroup.getChildAt(0).getId()){
                    insideCode = InsideCodeDto.newAztecInstance(content, errorCorrectionLevel);
                } else if(checkId == radioGroup.getChildAt(1).getId()){
                    insideCode = InsideCodeDto.newDataMatrixInstance(content, errorCorrectionLevel);
                } else if(checkId == radioGroup.getChildAt(2).getId()){
                    insideCode = InsideCodeDto.newDataMatrixInstance(content, errorCorrectionLevel);
                }

                String md5 = new String(Hex.encodeHex(DigestUtils.getMd5Digest().digest(StringUtils.getBytesUtf8(content))));
//                log.info("outSide：" + md5 + " | inside:" + content);
                YXCode yxCode = YXCode.newInstanceWithInsideCode(md5, RSCoder.Level.per20, insideCode);


                apply(getApplicationContext(),md5,insideCode.getContent());
//                YXCode yxCode = YXCode.newDefaultInstanceWithQRInside(content, ErrorCorrectionLevel.L);
                JumperUtils.launchResult(this,yxCode);

            } catch (DecodeFailedException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ErrorException e) {
                e.printStackTrace();
            }
        }
    }

    private void apply(Context context,final String md5,final String insideCodeContent){
        String token = AccountManager.getToken(getApplicationContext());
        if(TextUtils.isEmpty(token)){
            return;
        }


        IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);

        RetrofitClient.execute(iService.apply(token, DeviceUtils.getDeviceId(getApplicationContext()),"android"), new BaseSubscriber<ApplyModel>(getApplicationContext()) {
            @Override
            public void onNext(ApplyModel responseModel) {
                super.onNext(responseModel);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });

    }

    private void reportCode(Context context,String content,String keyInfo){
        String token = AccountManager.getToken(context);
        if(TextUtils.isEmpty(token)){
            return;
        }


        IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);

        RetrofitClient.execute(iService.reportCode(token, content, "QR", keyInfo), new BaseSubscriber<ResponseModel>(getApplicationContext()) {
            @Override
            public void onNext(ResponseModel responseModel) {
                super.onNext(responseModel);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });
    }

}
