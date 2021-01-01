package com.armadillogamestudios.engine2d.util;

import java.util.*;

public class Range {

    public static List<Integer> of(int start, int end) {
        List<Integer> list = new ArrayList<>();

        iterator(start, end, end < start ? -1 : 1).forEachRemaining(list::add);

        return list;
    }

    public static List<Integer> of(int end) {
        return of(0, end);
    }

    private static Iterator<Integer> iterator(int start, int end, int dir) {
        final int max = end;

        return new Iterator<Integer>() {

            private int current = start;

            @Override
            public boolean hasNext() {
                if (dir > 0) {
                    return current < max;
                }
                return current > max;
            }

            @Override
            public Integer next() {
                if (hasNext()) {
                    int r = current;
                    current += dir;
                    return r;
                } else {
                    throw new NoSuchElementException("Range reached the end");
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Can't remove values from a Range");
            }
        };
    }
}
