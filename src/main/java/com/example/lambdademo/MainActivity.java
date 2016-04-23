package com.example.lambdademo;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private int drawables = R.mipmap.ic_launcher;
    private ImageView mImageView;

    private TextView mTextView, btu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image);

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
          //just(T...): 将传入的参数依次发送出来
        Observable.just("hello rxjava and Lambda表达式").subscribe(s -> {
            mTextView.setText(s);
        });
*/

        //注册观察活动
        Observable<String> observable = Observable.create(mOnSubscribe);

        //分发订阅消息
        //表示工作在UI线程上
        observable.observeOn(AndroidSchedulers.mainThread());


        //创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来，整条链子就可以工作了
        observable.subscribe(mSubscriber);
        observable.subscribe(mSubscriber1);


        //
        String[] names = {"hello", "I", "am", "a", "student"};
        //from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        Observable.from(names).subscribe(A -> {
            Log.d("wuyinlei", A);
        });

        //这里来加载本地的一张图片
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getTheme().getDrawable(drawables);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
            /*那么，加载图片将会发生在 IO 线程，而设置图片则被设定在了主线程。这就意味着，
            即使加载图片耗费了几十甚至几百毫秒的时间，也不会造成丝毫界面的卡顿。*/
        }).subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        mImageView.setImageDrawable(drawable);
                    }
                });

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

    //在一个正确运行的事件序列中, onCompleted() 和 onError() 有且只有一个，
    // 并且是事件序列中的最后一个。需要注意的是，onCompleted() 和 onError()
    // 二者也是互斥的，即在队列中调用了其中一个，就不应该再调用另一个。
    Subscriber<String> mSubscriber = new Subscriber<String>() {

        //事件队列完结。RxJava 不仅把每个事件单独处理，还会把它们看做一个队列。RxJava 规定，
        // 当不会再有新的onNext() 发出时，需要触发 onCompleted() 方法作为标志。
        @Override
        public void onCompleted() {

        }

        //onError(): 事件队列异常。在事件处理过程中出异常时，onError() 会被触发，
        // 同时队列自动终止，不允许再有事件发出。
        @Override
        public void onError(Throwable e) {

        }

        // RxJava 的事件回调方法除了普通事件 onNext() （相当于 onClick() / onEvent()）
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

    /**
     * 在 RxJava 的默认规则中，事件的发出和消费都是在同一个线程的。也就是说，如果只用上
     * 面的方法，实现出来的只是一个同步的观察者模式。观察者模式本身的目的就是『后台处理，
     * 前台回调』的异步机制，因此异步对于 RxJava 是至关重要的。而要实现异步，则需要用到
     * RxJava 的另一个概念： Scheduler 。
     */

    /**
     * <p>在RxJava 中，Scheduler ——调度器，相当于线程控制器，RxJava 通过它来指定每一段</p>
     * <p>代码应该运行在什么样的线程。RxJava 已经内置了几个 Scheduler ，它们已经适合大多</p>
     * <p>数的使用场景：</p>
     * <p>Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。</p>
     * <p>Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。</p>
     * <p>Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。</p>
     * <p>行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空
     * <p>闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。</p>
     * <p>Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会</p>
     * <p>被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 </p>
     * <p>computation() 中，否则 I/O 操作的等待时间会浪费 CPU。</p>
     * <p>另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。</p>
     * <p>有了这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。 </p>
     * <p> subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程</p>
     * <p> observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。</p>
     */

    //线程测试
    public void ThreadText() {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        Log.d("wuyinlei", "number:" + number);
                    }
                });
    }


    /**
     * 这些变换虽然功能各有不同，但实质上都是针对事件序列的处理和再发送。而在 RxJava
     * 的内部，它们是基于同一个基础的变换方法： lift(Operator)。首先看一下 lift()
     * 的内部实现（仅核心代码）：
     * <p>
     * // 注意：这不是 lift() 的源码，而是将源码中与性能、兼容性、扩展性有关的代码剔除后的核心代码。
     * // 如果需要看源码，可以去 RxJava 的 GitHub 仓库下载。
     * public <R> Observable<R> lift(Operator<? extends R, ? super T> operator) {
     * return Observable.create(new OnSubscribe<R>() {
     *
     * @Override public void call(Subscriber subscriber) {
     * Subscriber newSubscriber = operator.call(subscriber);
     * newSubscriber.onStart();
     * onSubscribe.call(newSubscriber);
     * }
     * });
     * }
     * <p>
     * 这段代码很有意思：它生成了一个新的 Observable 并返回，而且创建新 Observable 所用的参数
     * OnSubscribe 的回调方法 call() 中的实现竟然看起来和前面讲过的 Observable.subscribe() 一样！
     * 然而它们并不一样哟~不一样的地方关键就在于第二行 onSubscribe.call(subscriber) 中的 onSubscribe
     * 所指代的对象不同（高能预警：接下来的几句话可能会导致身体的严重不适）——
     * <p>
     * subscribe() 中这句话的 onSubscribe 指的是 Observable 中的 onSubscribe 对象，这个没有问题，
     * 但是 lift() 之后的情况就复杂了点。
     * 当含有 lift() 时：
     * 1.lift() 创建了一个 Observable 后，加上之前的原始 Observable，已经有两个 Observable 了；
     * 2.而同样地，新 Observable 里的新 OnSubscribe 加上之前的原始 Observable 中的原始 OnSubscribe，
     * 也就有了两个 OnSubscribe；
     * 3.当用户调用经过 lift() 后的 Observable 的 subscribe() 的时候，使用的是 lift() 所返回的新的 Observable
     * ，于是它所触发的 onSubscribe.call(subscriber)，也是用的新 Observable 中的新 OnSubscribe，即在 lift()
     * 中生成的那个 OnSubscribe；
     * 4.而这个新 OnSubscribe 的 call() 方法中的 onSubscribe ，就是指的原始 Observable 中的原始 OnSubscribe ，
     * 在这个 call() 方法里，新 OnSubscribe 利用 operator.call(subscriber) 生成了一个新的 Subscriber
     * （Operator 就是在这里，通过自己的 call() 方法将新 Subscriber 和原始 Subscriber 进行关联，并插入自己的
     * 『变换』代码以实现变换），然后利用这个新 Subscriber 向原始 Observable 进行订阅。
     * 这样就实现了 lift() 过程，有点像一种代理机制，通过事件拦截和处理实现事件序列的变换。
     * 精简掉细节的话，也可以这么说：在 Observable 执行了 lift(Operator) 方法之后，会返回一个新的 Observable，
     * 这个新的 Observable 会像一个代理一样，负责接收原始的 Observable 发出的事件，并在处理后发送给 Subscriber。
     */

    /*将事件中的 Integer 对象转换成 String 的例子*/
    public void LiftTest() {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        });

        observable.lift(new Observable.Operator<String, Integer>() {

            @Override
            public Subscriber<? super Integer> call(Subscriber<? super String> subscriber) {

                return new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        subscriber.onNext("" + integer);
                    }
                };
            }
        });

        /**
         * 讲述 lift() 的原理只是为了让你更好地了解 RxJava ，从而可以更好地使用它。
         * 然而不管你是否理解了 lift() 的原理，RxJava 都不建议开发者自定义 Operator
         * 来直接使用 lift()，而是建议尽量使用已有的 lift() 包装方法（如 map() flatMap()
         * 等）进行组合来实现需求，因为直接使用 lift() 非常容易发生一些难以发现的错误。
         */

    }

    /**
     *  线程控制：Scheduler (二)
     *  1) Scheduler 的 API (二)
     *   前面讲到了，可以利用 subscribeOn() 结合 observeOn() 来实现线程控制，让事件的产生和消
     *   费发生在不同的线程。可是在了解了 map() flatMap() 等变换方法后，有些好事的（其实就是当
     *   初刚接触 RxJava 时的我）就问了：能不能多切换几次线程？

     *   答案是：能。因为 observeOn() 指定的是 Subscriber 的线程，而这个 Subscriber 并不是
     *  （严格说应该为『不一定是』，但这里不妨理解为『不是』）subscribe() 参数中的 Subscriber
     *   ，而是 observeOn() 执行时的当前 Observable 所对应的 Subscriber ，即它的直接下级
     *   Subscriber 。换句话说，observeOn() 指定的是它之后的操作所在的线程。因此如果有多次切换
     *   线程的需求，只要在每个想要切换线程的位置调用一次 observeOn() 即可。上代码：
     *
     *   Observable.just(1, 2, 3, 4) // IO 线程，由 subscribeOn() 指定
     *           .subscribeOn(Schedulers.io())
     *           .observeOn(Schedulers.newThread())
     *           .map(mapOperator) // 新线程，由 observeOn() 指定
     *           .observeOn(Schedulers.io())
     *           .map(mapOperator2) // IO 线程，由 observeOn() 指定
     *           .observeOn(AndroidSchedulers.mainThread)
     *           .subscribe(subscriber);  // Android 主线程，由 observeOn() 指定

     * 如上，通过 observeOn() 的多次调用，程序实现了线程的多次切换。

     * 不过，不同于 observeOn() ， subscribeOn() 的位置放在哪里都可以，但它是只能调用一次的。

     *又有好事的（其实还是当初的我）问了：如果我非要调用多次 subscribeOn() 呢？会有什么效果？
     */


    /**
     * 3) 延伸：doOnSubscribe()

     * 然而，虽然超过一个的 subscribeOn() 对事件处理的流程没有影响，但在流程之前却是可以利用的。

     * 在前面讲 Subscriber 的时候，提到过 Subscriber 的 onStart() 可以用作流程开始前的初始化。
     * 然而 onStart() 由于在 subscribe() 发生时就被调用了，因此不能指定线程，而是只能执行在 subscribe()
     * 被调用时的线程。这就导致如果 onStart() 中含有对线程有要求的代码（例如在界面上显示一个 ProgressBar，
     * 这必须在主线程执行），将会有线程非法的风险，因为有时你无法预测 subscribe() 将会在什么线程执行。

     * 而与 Subscriber.onStart() 相对应的，有一个方法 Observable.doOnSubscribe() 。它和
     * Subscriber.onStart() 同样是在 subscribe() 调用后而且在事件发送前执行，但区别在于它可以指定线程。
     * 默认情况下， doOnSubscribe() 执行在 subscribe() 发生的线程；而如果在 doOnSubscribe() 之后有 subscribeOn()
     * 的话，它将执行在离它最近的 subscribeOn() 所指定的线程。
     */

    /*Observable.create(onSubscribe)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe(new Action0() {
        @Override
        public void call() {
            progressBar.setVisibility(View.VISIBLE); // 需要在主线程执行
        }
    })
            .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);
*/
}
