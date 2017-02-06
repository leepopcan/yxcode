package lm.com.gencode;

import android.content.Context;
import android.util.Log;

import com.msj.networkcore.utils.ExceptionHandle;
import com.msj.networkcore.utils.NetworkUtils;

import rx.Subscriber;

/**
 * @author mengxiangcheng
 * @date 2016/10/14 上午9:30
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc Rxjava相应错做基础订阅
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onNext(T t) {
        if(t instanceof ResponseModel){
            if(((ResponseModel) t).needLogin()){
                AccountManager.clear();
                LoginActivity.launch(context);
//                ToastUtils.mkShortTimeToast(context,((ResponseModel) t).getErrorMsg());
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e("msj", e.getMessage());
        // todo error somthing

        if(e instanceof ExceptionHandle.ResponeThrowable){
            onError((ExceptionHandle.ResponeThrowable)e);
        } else {
            ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
            onError(responeThrowable);
        }
        //错误也要执行完毕，错误写法，onError和onCompleted()互斥，一个队列只能调取一个
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e("msj","http is start");

        // todo some common as show loadding  and check netWork is NetworkAvailable
        // if  NetworkAvailable no !   must to call onCompleted
        if (!NetworkUtils.isNetworkAvailable(context)) {
//            Toast.makeText(context, "无网络，读取缓存数据", Toast.LENGTH_SHORT).show();
            onCompleted();
        }

    }

    @Override
    public void onCompleted() {

        Log.e("msj","onComleted");
        // todo some common as  dismiss loadding
    }


    public void onError(ExceptionHandle.ResponeThrowable e){
//        Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show();
    };

}
