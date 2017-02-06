package lm.com.gencode.network;

import lm.com.gencode.network.model.ApplyModel;
import lm.com.gencode.network.model.FetchCodeInfo;
import lm.com.gencode.network.model.LoginModel;
import lm.com.gencode.network.model.ResponseModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by lihaifeng on 16/12/18.
 * Desc :
 */

public interface IService {

    @GET("login.do")
    Observable<LoginModel> login(
            @Query("name") String name,
            @Query("password") String password);

    @GET("fetchCodeInfo.do")
    Observable<FetchCodeInfo> fetchCodeInfo(
            @Query("keyInfo") String keyInfo);
    @GET("apply.do")
    Observable<ApplyModel> apply(
            @Query("token") String token,
            @Query("code") String code,
            @Query("type") String type);

    @GET("reportCode.do")
    Observable<ResponseModel> reportCode(
            @Query("token") String token,
            @Query("content") String content,
            @Query("innerCodeType") String innerCodeType,
            @Query("keyInfo") String keyInfo);


}
