package com.example.adrax.dely.core;

public interface InternetCallback<TData> {
    void call(Result<TData> result);
}
