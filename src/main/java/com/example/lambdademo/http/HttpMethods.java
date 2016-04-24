package com.example.lambdademo.http;

import com.example.lambdademo.listener.MovieService;
import com.example.lambdademo.model.HttpResult;
import com.example.lambdademo.model.Subjects;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wuyin on 2016/4/23.
 */
public class HttpMethods {

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

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
    private static class SingleMethod {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //对外提供获取单例的方法
    public static HttpMethods getInstance() {
        return SingleMethod.INSTANCE;
    }

    public void getTopMovie(Subscriber<HttpResult> subscriber, int start, int count) {
       /* mMovieService.getTopRxMovie(start, count)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);*/

        Observable observable = mMovieService.getTopDialogMovie(start, count)
                .map(new HttpResultFunc<List<Subjects>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * @param o   观察者
     * @param s   被观察者
     * @param <T>   泛型
     */
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    /**
     *  RxJava+Retrofit，在联网返回后如何先进行统一的判断？
     *  https://www.zhihu.com/question/39182019
     * {
     *      "resultCode": 0,
     *      "resultMessage": "成功",
     *      "data": {}
     *  }
     */

    /**
     * 我们在这里可以模拟一个当我们获得到的resultCode的值，如果获得的不是0，代表获取失败
     * 抛出我们自定义的异常，如果获得的是resultCode=0，代表我们获得的有数据，那么我们就返回
     * 我们得到的数
     *
     * @param <T>
     */
    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T>{

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCount() == 0) {
                throw new ApiException(100);
            }
            return httpResult.getSubjects();
        }
    }

    /**
     * @param subscriber   由调用者传过来的观察者对象
     * @param start        起始位置
     * @param count        显示的数据条数
     */
    public void getTopRxListMovie(Subscriber<List<Subjects>> subscriber, int start, int count) {
        mMovieService.getTopRxListMovie(start, count)
                .map(new HttpResultFunc<List<Subjects>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }







}
