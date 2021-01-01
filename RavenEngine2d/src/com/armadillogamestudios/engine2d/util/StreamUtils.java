package com.armadillogamestudios.engine2d.util;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class StreamUtils {

    public static <A, B, C> Stream<C> pair(Stream<A> s1, Stream<B> s2, BiFunction<A, B, C> function) {

        Iterator<A> i1 = s1.iterator();
        Iterator<B> i2 = s2.iterator();

        Stream.Builder<C> b = Stream.builder();

        while (i1.hasNext() && i2.hasNext()) {
            b.add(function.apply(i1.next(), i2.next()));
        }

        return b.build();
    }


}

