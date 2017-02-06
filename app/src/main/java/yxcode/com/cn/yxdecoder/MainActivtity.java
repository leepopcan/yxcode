package yxcode.com.cn.yxdecoder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import dtr.zxing.activity.CaptureActivity;
import yxcode.com.cn.yxdecoder.account.AccountManager;
import yxcode.com.cn.yxdecoder.base.BaseActivity;
import yxcode.com.cn.yxdecoder.base.LoginActivity;
import yxcode.com.cn.yxdecoder.network.IService;
import yxcode.com.cn.yxdecoder.network.model.LoginModel;
import yxcode.com.cn.yxdecoder.utils.BaseSubscriber;
import yxcode.com.cn.yxdecoder.utils.FileUtil;
import yxcode.com.cn.yxdecoder.utils.PreferencesUtils;
import yxcode.com.cn.yxdecoder.utils.RetrofitClient;
import yxcode.com.cn.yxdecoder.utils.SharePreKey;


/**
 * Created by yupu on 2016/6/13-13:27.
 * Email:459112332@qq.com
 */
public class MainActivtity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        if(null == loginReceiver){
            loginReceiver = new LoginReceiver();
        }
        localBroadcastManager.registerReceiver(loginReceiver,new IntentFilter(AccountManager.LOGIN_ACTION));

        try {
            FileUtil.initSdCard();
        } catch (IOException e) {
            e.printStackTrace();
        }


        findViewById(R.id.btn_san).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivtity.this, CaptureActivity.class));

            }
        });

        showBackArray(false);
        login();
    }

    private void login(){
        if(PreferencesUtils.containKey(getApplicationContext(), SharePreKey.ACCOUNT) &&
                PreferencesUtils.containKey(getApplicationContext(), SharePreKey.PASSWORD)){

            String account = PreferencesUtils.getString(getApplicationContext(),SharePreKey.ACCOUNT);
            String password = PreferencesUtils.getString(getApplicationContext(),SharePreKey.PASSWORD);

            IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);

            RetrofitClient.execute(iService.login(account, password),
                    new BaseSubscriber<LoginModel>(getApplicationContext()) {
                        @Override
                        public void onNext(LoginModel loginResponseModel) {
                            super.onNext(loginResponseModel);
                            Toast.makeText(getApplicationContext(), loginResponseModel.getErrorMsg(), Toast.LENGTH_LONG).show();
                            if (loginResponseModel.isSuccess()) {
                                AccountManager.saveLoginModel(getApplicationContext(),loginResponseModel);
                                invalidateOptionsMenu();
                            } else {
                                AccountManager.clear(getApplicationContext());
                                invalidateOptionsMenu();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade, R.anim.hold);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_login){
            if(!AccountManager.isLogin(getBaseContext())){
                LoginActivity.launch(this);
                return true;
            } else {
                AccountManager.clear(getApplicationContext());
                invalidateOptionsMenu();
                return true;
            }
        } else if(item.getItemId() == R.id.action_setting){
            if(PreferencesUtils.getInt(getApplicationContext(),"rect_x") == PreferencesUtils.DEFAULT_INT){
                Toast.makeText(getApplicationContext(),"请至少打开一次扫描二维码界面,采集RECT参数",Toast.LENGTH_LONG).show();
                return true;
            }
            startActivity(new Intent(this,SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);

//        menu.findItem(R.id.action_setting).setVisible(false);

        if(AccountManager.isLogin(getBaseContext())){
            menu.findItem(R.id.action_login).setTitle("退出");
        } else {
            menu.findItem(R.id.action_login).setTitle("登录");
        }

        return true;
    }

    LocalBroadcastManager localBroadcastManager;
    LoginReceiver loginReceiver;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != loginReceiver){
            localBroadcastManager.unregisterReceiver(loginReceiver);
            loginReceiver = null;
        }
    }


    private class LoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            invalidateOptionsMenu();
        }
    }
}
