package yxcode.com.cn.yxdecoder.account;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;

import yxcode.com.cn.yxdecoder.network.model.LoginModel;
import yxcode.com.cn.yxdecoder.utils.PreferencesUtils;
import yxcode.com.cn.yxdecoder.utils.SharePreKey;

/**
 * Created by lihaifeng on 16/12/18.
 * Desc :
 */

public class AccountManager {

    public static final String LOGIN_ACTION = "LOGIN_ACTION";

    public static void saveLoginModel(Context context,LoginModel loginModel){
        try {
            PreferencesUtils.saveSerialize(context,SharePreKey.LOGINMODEL,loginModel);
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

    public static void clear(Context context){
        PreferencesUtils.remove(context,SharePreKey.LOGINMODEL);
    }

    public static void saveAccountPassword(Context context,String account,String password){
        PreferencesUtils.saveString(context,SharePreKey.ACCOUNT,account);
        PreferencesUtils.saveString(context,SharePreKey.PASSWORD,password);
    }

}
