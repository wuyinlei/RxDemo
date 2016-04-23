package com.example.lambdademo.http;

import com.example.lambdademo.MovieService;
import com.example.lambdademo.model.HttpResult;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuyin on 2016/4/23.
 */
public class HttpMethods {

    public static final String BASE_URL ="https://api.douban.com/v2/movie/";

    private static final int DEFAUT_TIMEOUT = 5;

    private Retrofit mRetrofit;

    private MovieService mMovieService;

    //构造方法私有化
    public HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAUT_TIMEOUT, TimeUnit.MINUTES);

        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mMovieService = mRetrofit.create(MovieService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingleMethod{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //对外提供获取单例的方法
    public static HttpMethods getInstance(){
        return SingleMethod.INSTANCE;
    }

    public void getTopMovie(Subscriber<HttpResult> subscriber,int start,int count){
        mMovieService.getTopRxMovie(start,count)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
