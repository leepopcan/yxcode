package lm.com.gencode;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import lm.com.gencode.account.AccountManager;
import lm.com.gencode.network.IService;
import lm.com.gencode.network.model.ApplyModel;
import lm.com.gencode.network.model.LoginModel;
import lm.com.gencode.utils.SharePreKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lihaifeng on 16/12/18.
 * Desc :
 */

public class LoginActivity extends BaseActivity {

    EditText account;
    EditText password;
    Button btnLogin;

    public static void launch(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.length() == 0 ){
                    Toast.makeText(getApplicationContext(),"请输入账号",Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.length() == 0 ){
                    Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_LONG).show();
                    return;
                }

                String accountStr =  account.getText().toString().trim();
                String passwordStr =  password.getText().toString().trim();

                Login(accountStr,passwordStr);
            }
        });

    }

    private void Login(String account,String password) {

        IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);

        RetrofitClient.execute(
                iService.login(account, password),
                new BaseSubscriber<LoginModel>(getApplicationContext()) {
                    @Override
                    public void onNext(LoginModel loginResponseModel) {
                        super.onNext(loginResponseModel);
                        if (loginResponseModel.isSuccess()) {
                            AccountManager.saveLoginModel(getApplicationContext(), SharePreKey.LOGINMODEL, loginResponseModel);
                            finish();
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
