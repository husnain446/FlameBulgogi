package com.flame.bulgogi.api;

public interface RxJava2ApiCallback<T> {
    void onSuccess(T t);

    void onFailed(Throwable throwable);
}