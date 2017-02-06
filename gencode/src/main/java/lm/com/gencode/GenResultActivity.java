package lm.com.gencode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xytxw.yangxin_core.util.MD5Util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import lm.com.gencode.account.AccountManager;
import lm.com.gencode.network.IService;
import lm.com.gencode.network.model.ApplyModel;
import lm.com.gencode.network.model.FetchCodeInfo;
import lm.com.gencode.network.model.LoginModel;
import lm.com.gencode.utils.SharePreKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by lihaifeng on 16/11/6.
 * Desc :
 */
public class GenResultActivity extends BaseActivity{

    public static final String BITMAP = "BITMAP";


    public static void launch(Context context,Bitmap bitmap){
        Intent intent = new Intent(context,GenResultActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(BITMAP,bitmap);
        context.startActivity(intent);
    }

    Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        Bitmap bitmap = getIntent().getParcelableExtra(BITMAP);
        if(null != bitmap){
            ResultImage imageView = (ResultImage) findViewById(R.id.result_img);
            imageView.drawBitmap(bitmap);
        }

        apply();
    }

    private void apply(){
        String token = AccountManager.getToken(getApplicationContext());
        if(TextUtils.isEmpty(token)){
            return;
        }

        IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);
        RetrofitClient.execute(
                iService.apply(token, DeviceUtils.getDeviceId(getApplicationContext()),"android"),
                new BaseSubscriber<ApplyModel>(getApplicationContext()) {
                    @Override
                    public void onNext(ApplyModel loginResponseModel) {
                        super.onNext(loginResponseModel);
                        if (loginResponseModel.isSuccess()) {
                            if(loginResponseModel.isData()){

                            } else {
                                applyButton.setVisibility(View.VISIBLE);
                                applyButton.setText("申请权限");
                                applyButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        applyButton.setText("请等到后台审核...");
                                        applyButton.setOnClickListener(null);
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), loginResponseModel.getErrorMsg(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
