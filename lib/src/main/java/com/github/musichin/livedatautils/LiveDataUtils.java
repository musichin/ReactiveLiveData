package com.github.musichin.livedatautils;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LiveDataUtils {
    private static Object NOT_SET = new Object();

    private LiveDataUtils() {
    }

    @MainThread
    public static <T> LiveData<T> empty() {
        return new LiveData<T>() {
        };
    }

    @MainThread
    public static <T> LiveData<T> just(@Nullable final T value) {
        return new LiveData<T>() {
            {
                setValue(value);
            }
        };
    }

    @MainThread
    public static <T, R> LiveData<R> map(@NonNull LiveData<T> source, @NonNull Function<T, R> func) {
        return Transformations.map(source, func);
    }

    @MainThread
    public static <T> LiveData<T> filter(@NonNull LiveData<T> source, @NonNull final Function<T, Boolean> func) {
        final MediatorLiveData<T> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (func.apply(t)) result.setValue(t);
            }
        });
        return result;
    }

    @MainThread
    public static <T> LiveData<T> filterNotNull(@NonNull LiveData<T> source) {
        Function<T, Boolean> func = new Function<T, Boolean>() {
            @Override
            public Boolean apply(T input) {
                return input != null;
            }
        };
        return filter(source, func);
    }

    @MainThread
    public static <T> LiveData<T> distinctUntilChanged(@NonNull LiveData<T> source) {
        Function<T, Boolean> func = new Function<T, Boolean>() {
            Object prev = NOT_SET;

            @Override
            public Boolean apply(Object input) {
                if (input != prev) {
                    prev = input;
                    return true;
                }
                return false;
            }
        };

        return filter(source, func);
    }
}
