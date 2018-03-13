package com.github.musichin.reactivelivedata;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class ReactiveLiveDataJavaTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Test
    public void testCast() {
        TestObserver<String> observer = new TestObserver<>();

        Object value = "data";
        ReactiveLiveData.of(ReactiveLiveData.just(value))
                .cast(String.class)
                .observeForever(observer);

        observer.assertEquals(value);
    }

    @Test
    public void testMap() {
        TestObserver<Integer> observer = new TestObserver<>();

        LiveData<String> source = ReactiveLiveData.just("data");
        LiveData<Integer> length = ReactiveLiveData.map(source, String::length);
        length.observeForever(observer);

        observer.assertEquals(4);
    }
}
