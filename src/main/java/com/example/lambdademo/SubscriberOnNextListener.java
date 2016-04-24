package com.example.lambdademo;


public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
