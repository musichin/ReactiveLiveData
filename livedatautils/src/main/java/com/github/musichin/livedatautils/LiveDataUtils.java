package com.github.musichin.livedatautils;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class LiveDataUtils {
    private static Object NOT_SET = new Object();

    LiveDataUtils() {
    }

    @MainThread
    public static <T> LiveData<T> never() {
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
    public static <T, R> LiveData<R> switchMap(@NonNull LiveData<T> source, @NonNull Function<T, LiveData<R>> func) {
        return Transformations.switchMap(source, func);
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
    public static <T> LiveData<T> distinctUntilChanged(@NonNull LiveData<T> source,
                                                       @NonNull final Function<T, Object> func) {
        Function<T, Boolean> filter = new Function<T, Boolean>() {
            Object prev = NOT_SET;

            @Override
            public Boolean apply(T input) {
                Object key = func.apply(input);
                if (key != prev) {
                    prev = key;
                    return true;
                }
                return false;
            }
        };

        return filter(source, filter);
    }

    @MainThread
    public static <T> LiveData<T> distinctUntilChanged(@NonNull LiveData<T> source) {
        Function<T, Object> func = new Function<T, Object>() {
            @Override
            public Object apply(T input) {
                return input;
            }
        };

        return distinctUntilChanged(source, func);
    }

    @SafeVarargs
    @MainThread
    public static <T> LiveData<T> merge(@NonNull LiveData<T>... sources) {
        if (sources.length <= 0) {
            return LiveDataUtils.never();
        }

        final MediatorLiveData<T> result = new MediatorLiveData<>();

        final Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                result.setValue(t);
            }
        };

        for (LiveData<T> source : sources) {
            result.addSource(source, observer);
        }
        return result;
    }

    @MainThread
    @NonNull
    @SuppressWarnings("unchecked")
    public static <T, R> LiveData<R> combineLatest(@NonNull LiveData<? extends T>[] sources,
                                                   @NonNull final Function<T[], R> combiner) {
        if (sources.length <= 0) {
            return LiveDataUtils.never();
        }

        final int size = sources.length;
        final MediatorLiveData result = new MediatorLiveData<>();

        final Object[] values = new Object[sources.length];
        for (int index = 0; index < size; index++) values[index] = NOT_SET;

        final Set<Integer> emmits = new HashSet<>();
        for (int index = 0; index < size; index++) {
            final int observerIndex = index;
            Observer<Object> observer = new Observer<Object>() {
                @Override
                public void onChanged(@Nullable Object t) {
                    values[observerIndex] = t;
                    boolean combine = emmits.size() == size;
                    if (!combine) {
                        emmits.add(observerIndex);
                        combine = emmits.size() == size;
                    }

                    if (combine) {
                        result.setValue(combiner.apply((T[]) values));
                    }
                }
            };
            result.addSource(sources[index], observer);
        }
        return result;
    }

    @SafeVarargs
    @NonNull
    public static <T, R> LiveData<R> combineLatest(@NonNull Function<T[], R> combiner,
                                                   @NonNull LiveData<? extends T>... sources) {
        return combineLatest(sources, combiner);
    }

    @NonNull
    public static <T1, T2, R> LiveData<R> combineLatest(@NonNull LiveData<T1> source1,
                                                        @NonNull LiveData<T2> source2,
                                                        @NonNull final Function2<T1, T2, R> combiner) {
        @SuppressWarnings("unchecked")
        Function<Object[], R> func = new Function<Object[], R>() {
            @Override
            @SuppressWarnings("unchecked")
            public R apply(Object[] input) {
                return combiner.apply((T1) input[0], (T2) input[1]);
            }
        };

        return combineLatest(func, source1, source2);
    }

    @NonNull
    public static <T1, T2, T3, R> LiveData<R> combineLatest(@NonNull LiveData<T1> source1,
                                                            @NonNull LiveData<T2> source2,
                                                            @NonNull LiveData<T2> source3,
                                                            @NonNull final Function3<T1, T2, T3, R> combiner) {
        @SuppressWarnings("unchecked")
        Function<Object[], R> func = new Function<Object[], R>() {
            @Override
            @SuppressWarnings("unchecked")
            public R apply(Object[] input) {
                return combiner.apply((T1) input[0], (T2) input[1], (T3) input[2]);
            }
        };

        return combineLatest(func, source1, source2, source3);
    }
}
