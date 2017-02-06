package yxcode.com.cn.yxdecoder.gencode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import yxcode.com.cn.yxdecoder.R;
import yxcode.com.cn.yxdecoder.account.AccountManager;
import yxcode.com.cn.yxdecoder.base.BaseActivity;
import yxcode.com.cn.yxdecoder.base.LoginActivity;
import yxcode.com.cn.yxdecoder.network.IService;
import yxcode.com.cn.yxdecoder.network.model.ApplyModel;
import yxcode.com.cn.yxdecoder.network.model.ResponseModel;
import yxcode.com.cn.yxdecoder.utils.BaseSubscriber;
import yxcode.com.cn.yxdecoder.utils.DeviceUtils;
import yxcode.com.cn.yxdecoder.utils.RetrofitClient;
import yxcode.com.cn.yxdecoder.utils.ToastUtils;


/**
 * Created by lihaifeng on 16/11/6.
 * Desc :
 */
public class GenResultActivity extends BaseActivity {

    public static final String BITMAP = "BITMAP";

    public static void launch(Context context,Bitmap bitmap){
        Intent intent = new Intent(context,GenResultActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(BITMAP,bitmap);
        context.startActivity(intent);
    }


    public static void launch(Context context,Bitmap bitmap,String content,String innerCodeType,String keyInfo){
        Intent intent = new Intent(context,GenResultActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(BITMAP,bitmap);
        intent.putExtra(IService.CONTENT,content);
        intent.putExtra(IService.INNERCODETYPE,innerCodeType);
        intent.putExtra(IService.KEYINFO,keyInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        checkDeviceRight();

        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        if(null == loginReceiver){
            loginReceiver = new LoginReceiver();
        }
        localBroadcastManager.registerReceiver(loginReceiver,new IntentFilter(AccountManager.LOGIN_ACTION));

        Bitmap bitmap = getIntent().getParcelableExtra(BITMAP);
        if(null != bitmap){
            ResultImage imageView = (ResultImage) findViewById(R.id.result_img);
            imageView.drawBitmap(bitmap);
        }
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
                    public void onNext(ApplyModel applyModel) {
                        super.onNext(applyModel);
                        if (applyModel.isSuccess()) {
                            hasPermission = applyModel.isData();
                            invalidateOptionsMenu();
                        } else {
                            Toast.makeText(getApplicationContext(), applyModel.getErrorMsg(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    boolean hasPermission = false;

    private void checkDeviceRight(){
        String token = AccountManager.getToken(getApplicationContext());
        if(TextUtils.isEmpty(token)){
            return;
        }

        IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);
        RetrofitClient.execute(
                iService.checkDeviceRight(token, DeviceUtils.getDeviceId(getApplicationContext()),"android"),
                new BaseSubscriber<ApplyModel>(getApplicationContext()) {
                    @Override
                    public void onNext(ApplyModel loginResponseModel) {
                        super.onNext(loginResponseModel);
                        if (loginResponseModel.isSuccess()) {
                            hasPermission = loginResponseModel.isData();
                            invalidateOptionsMenu();
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

    private void reportCode(){

        if(!getIntent().hasExtra(IService.CONTENT) ||
                !getIntent().hasExtra(IService.INNERCODETYPE) ||
                !getIntent().hasExtra(IService.KEYINFO)){
            return;
        }

        Context context = getApplicationContext();
        String content = getIntent().getStringExtra(IService.CONTENT);
        String innerCodeType = getIntent().getStringExtra(IService.INNERCODETYPE);
        String keyInfo = getIntent().getStringExtra(IService.KEYINFO);
        String token = AccountManager.getToken(context);

        IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);
        RetrofitClient.execute(iService.reportCode(token, content, innerCodeType, keyInfo), new BaseSubscriber<ResponseModel>(getApplicationContext()) {
            @Override
            public void onNext(ResponseModel responseModel) {
                super.onNext(responseModel);
                ToastUtils.mkShortTimeToast(getApplicationContext(),responseModel.getErrorMsg());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtils.mkShortTimeToast(getApplicationContext(),e.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_login){
            LoginActivity.launch(this);
        } else if(item.getItemId() == R.id.action_report){
            reportCode();
        } else if(item.getItemId() == R.id.action_permission){
            apply();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!getIntent().hasExtra(IService.CONTENT) ||
                !getIntent().hasExtra(IService.INNERCODETYPE) ||
                !getIntent().hasExtra(IService.KEYINFO)){
            return super.onCreateOptionsMenu(menu);
        }

        getMenuInflater().inflate(R.menu.result_menu, menu);


        if(AccountManager.isLogin(getBaseContext())){
            if(hasPermission){
                menu.findItem(R.id.action_login).setVisible(false);
                menu.findItem(R.id.action_permission).setVisible(false);
            } else {
                menu.findItem(R.id.action_login).setVisible(false);
                menu.findItem(R.id.action_report).setVisible(false);
            }
        } else {
            menu.findItem(R.id.action_report).setVisible(false);
            menu.findItem(R.id.action_permission).setVisible(false);
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
