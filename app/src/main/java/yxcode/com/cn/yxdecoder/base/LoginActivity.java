package yxcode.com.cn.yxdecoder.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import yxcode.com.cn.yxdecoder.R;
import yxcode.com.cn.yxdecoder.account.AccountManager;
import yxcode.com.cn.yxdecoder.network.IService;
import yxcode.com.cn.yxdecoder.network.model.LoginModel;
import yxcode.com.cn.yxdecoder.utils.BaseSubscriber;
import yxcode.com.cn.yxdecoder.utils.RetrofitClient;

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

                try {
                    Login(accountStr,passwordStr);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }
    private void Login(final String account, final String password) {

        IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);

        RetrofitClient.execute(iService.login(account, password),
                new BaseSubscriber<LoginModel>(getApplicationContext()) {
                    @Override
                    public void onNext(LoginModel loginResponseModel) {
                        super.onNext(loginResponseModel);
                        Toast.makeText(getApplicationContext(), loginResponseModel.getErrorMsg(), Toast.LENGTH_LONG).show();
                        if (loginResponseModel.isSuccess()) {
                            AccountManager.saveLoginModel(getApplicationContext(),loginResponseModel);
                            AccountManager.saveAccountPassword(getApplicationContext(),account,password);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(AccountManager.LOGIN_ACTION));
                            finish();
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
