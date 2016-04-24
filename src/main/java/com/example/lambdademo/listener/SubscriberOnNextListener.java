package com.example.lambdademo.listener;


public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
