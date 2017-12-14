package com.github.musichin.reactlivedata;


public interface Function3<I1, T2, T3, R> {
    R apply(I1 input1, T2 input2, T3 input3);
}
