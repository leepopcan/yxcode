package com.msj.networkcore.mvp.biz;


import com.msj.networkcore.mvp.bean.ApiResponse;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author mengxiangcheng
 * @date 2016/10/13 下午1:35
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc
 */
public interface IBaseBiz {

    @GET("{url}")
    Observable<ApiResponse<Object>> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> maps
    );


    @POST("{url}")
    Observable<ApiResponse<Object>> executePost(
            @Path("url") String url,
            //  @Header("") String authorization,
            @QueryMap Map<String, String> maps);

    @Multipart
    @POST("{url}")
    Observable<ApiResponse<Object>> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Path("headers") Map<String, String> headers,
            @Part("filename") String description,
            @PartMap() Map<String, RequestBody> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);


    @FormUrlEncoded
    @POST("{url}")
    Observable<ApiResponse<Object>> executePostForm(@Path("url") String url, @Field("data") String json);
}
