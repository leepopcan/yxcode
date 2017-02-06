package yxcode.com.cn.yxdecoder.network;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import yxcode.com.cn.yxdecoder.network.model.ApplyModel;
import yxcode.com.cn.yxdecoder.network.model.ExtraDetailDto;
import yxcode.com.cn.yxdecoder.network.model.LoginModel;
import yxcode.com.cn.yxdecoder.network.model.ResponseModel;

/**
 * Created by lihaifeng on 16/12/18.
 * Desc :
 */

public interface IService {

    @GET("login.do")
    Observable<LoginModel> login(
            @Query("name") String name,
            @Query("password") String password);

    @GET("fetchCodeInfo.do")//显示详细信息
    Observable<ExtraDetailDto> fetchCodeInfo(
            @Query("keyInfo") String keyInfo,
            @Query("content") String content);


    @GET("apply.do")//申请权限
    Observable<ApplyModel> apply(
            @Query("token") String token,
            @Query("code") String code,
            @Query("type") String type);


    public static final String CONTENT = "content";
    public static final String INNERCODETYPE = "innerCodeType";
    public static final String KEYINFO = "keyInfo";

    @GET("reportCode.do")//上报
    Observable<ResponseModel> reportCode(
            @Query("token") String token,
            @Query(CONTENT) String content,
            @Query(INNERCODETYPE) String innerCodeType,
            @Query(KEYINFO) String keyInfo);

    @GET("checkDeviceRight.do")//检查权限
    Observable<ApplyModel> checkDeviceRight(
            @Query("token") String token,
            @Query("deviceCode") String deviceCode,
            @Query("deviceType") String deviceType);

}
