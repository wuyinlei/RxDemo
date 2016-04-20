package com.example.lambdademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView,btu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //所有不符合lambda语法的都是灰色
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", "hello，我是简单的匿名线程");
            }
        }).start();

        new Thread(()->{
            Log.d("MainActivity", "hello，我是lambda表达式");
        }).start();

        Button button = new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "我是普通的button点击事件");
            }
        });

        button.setOnClickListener(v -> {
            Log.d("MainActivity", "我是lambda实现的点击事件");
            v.postDelayed(()->{
                Log.d("MainActivity", "我是对v这个参数进行了一个修改");
            },200);
        });


        mTextView = (TextView) findViewById(R.id.helloRx);

        //常规的观察者和被观察者
       /* //创建一个被观察者   事件的生产者
        Observable observable = rx.Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //添加队列
                subscriber.onNext("hello rxJava");
                //完成  还有一个onError()和下面的这个只能在一次的任务中执行一个
                subscriber.onCompleted();
            }
        });

        //创建一个观察者   事件的最终消费者
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                //完成的时候响应
                mTextView.setText(s);
            }
        };

        //
        observable.subscribe(subscriber);
*/

        //上面的代码的简化
        Observable.just("hello rxJava")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mTextView.setText(s);
                    }
                });

        //利用RxJava和Lambda进一步简化代码
        Observable.just("hello rxjava and Lambda表达式").subscribe(s->{
            mTextView.setText(s);
        });

    }


}
