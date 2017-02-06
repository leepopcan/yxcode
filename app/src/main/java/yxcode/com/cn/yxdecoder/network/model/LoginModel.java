package yxcode.com.cn.yxdecoder.network.model;

import java.io.Serializable;

/**
 * Created by lihaifeng on 16/12/18.
 * Desc :
 */

public class LoginModel extends ResponseModel {


    Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        TokenVo tokenVo;
        UserVo userVo;

        public TokenVo getTokenVo() {
            return tokenVo;
        }

        public UserVo getUserVo() {
            return userVo;
        }
    }

    public static class TokenVo implements Serializable{
        long ctime;
        int id;
        String token;
        int userId;
        long utime;

        public long getCtime() {
            return ctime;
        }

        public int getId() {
            return id;
        }

        public String getToken() {
            return token;
        }

        public int getUserId() {
            return userId;
        }

        public long getUtime() {
            return utime;
        }
    }

    public static class UserVo implements Serializable{
        long ctime;
        int id;
        String name;
        String pass;
        long utime;

        public long getCtime() {
            return ctime;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPass() {
            return pass;
        }

        public long getUtime() {
            return utime;
        }
    }


}
