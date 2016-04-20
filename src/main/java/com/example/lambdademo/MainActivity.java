package com.example.lambdademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


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




    }


}
