package lm.com.gencode.account;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;

import lm.com.gencode.PreferencesUtils;
import lm.com.gencode.network.model.LoginModel;
import lm.com.gencode.utils.SharePreKey;


/**
 * Created by lihaifeng on 16/12/18.
 * Desc :
 */

public class AccountManager {


    public static void saveLoginModel(Context context,String key,LoginModel loginModel){
        try {
            PreferencesUtils.saveSerialize(context,key,loginModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getToken(Context context){
        if(PreferencesUtils.containKey(context,SharePreKey.LOGINMODEL)){
            try {
                LoginModel loginModel = (LoginModel) PreferencesUtils.getSerializable(context,SharePreKey.LOGINMODEL);
                if(null != loginModel){
                    LoginModel.Data data = loginModel.getData();
                    if(null != data){
                        LoginModel.TokenVo tokenVo = data.getTokenVo();
                        if(null != tokenVo){
                            return tokenVo.getToken();
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isLogin(Context context){
        String token = getToken(context);
        return !TextUtils.isEmpty(token);
    }
}
