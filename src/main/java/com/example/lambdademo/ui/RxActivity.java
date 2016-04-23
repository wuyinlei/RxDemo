package com.example.lambdademo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lambdademo.http.HttpMethods;
import com.example.lambdademo.model.HttpResult;
import com.example.lambdademo.model.MovieEntity;
import com.example.lambdademo.MovieService;
import com.example.lambdademo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxActivity extends AppCompatActivity {

    @Bind(R.id.click_me_BN)
    Button clickMeBN;

    @Bind(R.id.result_TV)
    TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);

        //绑定布局
        ButterKnife.bind(this);
    }

    @OnClick(R.id.click_me_BN)
    public void onClick() {
        //getMovie();
        getRxMovie();
    }

    //进行网络的请求  不要忘了添加访问网络的权限
    private void getMovie() {

        //https://api.douban.com/v2/movie/top250?start=0&count=1
        String baseUrl = "https://api.douban.com/v2/movie/";

        //创建Retrofit实例   以上为没有经过封装的、原生态的Retrofit写网络请求的代码
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)  //传入url
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

        Call<HttpResult> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<HttpResult>() {
            @Override
            public void onResponse(Call<HttpResult> call, Response<HttpResult> response) {
                resultTV.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<HttpResult> call, Throwable t) {
                resultTV.setText(t.getMessage());
            }
        });
    }

    /**
     * 加入RxJava的Retrofit的请求
     */
    private void getRxMovie() {

        //https://api.douban.com/v2/movie/top250?start=0&count=1
        String baseUrl = "https://api.douban.com/v2/movie/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

        movieService.getTopRxMovie(0, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        resultTV.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        resultTV.setText(httpResult.toString());
                    }
                });
    }

    /**
     * 这里简单的封装了一下RxJava和使用OkHttpClient
     */
    private void getInstanceMovie() {
        Subscriber<HttpResult> subscriber = new Subscriber<HttpResult>() {
            @Override
            public void onCompleted() {
                Toast.makeText(RxActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                resultTV.setText(e.getMessage());
            }

            @Override
            public void onNext(HttpResult httpResult) {
                resultTV.setText(httpResult.toString());
            }
        };

        HttpMethods.getInstance().getTopMovie(subscriber, 0, 10);
    }
}
