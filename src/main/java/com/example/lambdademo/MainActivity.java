package com.example.lambdademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView, btu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //所有不符合lambda语法的都是灰色
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", "hello，我是简单的匿名线程");
            }
        }).start();

        new Thread(() -> {
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
            v.postDelayed(() -> {
                Log.d("MainActivity", "我是对v这个参数进行了一个修改");
            }, 200);
        });*/


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

      /*  //上面的代码的简化
        Observable.just("hello rxJava")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mTextView.setText(s);
                    }
                });
*/

     /*   //利用RxJava和Lambda进一步简化代码
        Observable.just("hello rxjava and Lambda表达式").subscribe(s -> {
            mTextView.setText(s);
        });
*/

        //注册观察活动
        Observable<String> observable = Observable.create(mOnSubscribe);

        //分发订阅消息
        //表示工作在UI线程上
        observable.observeOn(AndroidSchedulers.mainThread());


        observable.subscribe(mSubscriber);
        observable.subscribe(mSubscriber1);
    }

    //被观察者，发送消息时间
    Observable.OnSubscribe mOnSubscribe = new
            Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                   //发送事件
                    subscriber.onNext("hello RxAndroid");
                   //发送完成的事件
                    subscriber.onCompleted();
                }
            };


    //观察者一  接受消息时间，修改TextView控件输出
    Subscriber<String> mSubscriber = new Subscriber<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            //接受到onNext事件后，就把textview的显示显示为
            //接受到被观察者发送的消息，设置显示内容
            mTextView.setText(s);
        }
    };

    //观察者二   接受被观察者的消息，然后toast提示消息
    Subscriber<String> mSubscriber1 = new Subscriber<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };


}
