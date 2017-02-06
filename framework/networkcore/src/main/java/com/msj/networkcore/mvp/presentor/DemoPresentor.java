package com.msj.networkcore.mvp.presentor;


import com.msj.networkcore.mvp.biz.IBaseBiz;
import com.msj.networkcore.mvp.view.IBaseView;

import retrofit2.Retrofit;

/**
 * @author mengxiangcheng
 * @date 2016/10/13 下午1:35
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc
 */
public class DemoPresentor {

    private IBaseBiz demoBiz;

    private IBaseView baseView;

    public DemoPresentor(IBaseView baseView, Retrofit retrofit){
        this.baseView = baseView;
        demoBiz = retrofit.create(IBaseBiz.class);
    }

    public void get(){
        baseView.showLoadingDialog();

        //"http://ip.taobao.com/service/getIpInfo.php?ip=21.22.11.33";
//        RetrofitClient.getInstance(baseView.getContext(),"").createBaseApi().postForm("service/getIpInfo.php"
//                , "", new BaseSubscriber<ApiResponse<Object>>(baseView.getContext()) {
//
//                    @Override
//                    public void onNext(ApiResponse<Object> baseEntityApiResponse) {
//
//                    }
//
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        Log.e("Lyk", e.getMessage());
//                        Toast.makeText(baseView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//
//                    }
//
//                });
//        ApiRequest request = new ApiRequest();
//        Call<BaseEntity> call = demoBiz.demo("gsonData");
//        call.enqueue(new Callback<BaseEntity>() {
//            @Override
//            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
//                baseView.cancelLoadingDialog();
//            }
//
//            @Override
//            public void onFailure(Call<BaseEntity> call, Throwable t) {
//                baseView.cancelLoadingDialog();
//            }
//        });
    }

}
