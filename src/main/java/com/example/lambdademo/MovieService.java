package com.example.lambdademo;

import com.example.lambdademo.model.HttpResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by wuyin on 2016/4/23.
 */
public interface MovieService {
    //GET请求，传入请求参数
    @GET("top250")
    Call<HttpResult> getTopMovie(@Query("start") int start, @Query("count") int count);
}
