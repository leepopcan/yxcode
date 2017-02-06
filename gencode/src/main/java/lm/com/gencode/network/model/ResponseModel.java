package lm.com.gencode.network.model;

import java.io.Serializable;

/**
 * Created by lihaifeng on 16/12/18.
 * Desc :
 */

public class ResponseModel implements Serializable{
    public int code;
    public String msg;

    public boolean isSuccess(){
        return code == 1;
    }

    public boolean needLogin(){return code == -2;}

    public String getErrorMsg(){
        return msg;
    }
}
