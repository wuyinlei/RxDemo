package com.example.lambdademo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
import retrofit2.converter.gson.GsonConverterFactory;

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
    public void onClick(){
        getMovie();
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

        Call<HttpResult> call = movieService.getTopMovie(0,10);
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
}
