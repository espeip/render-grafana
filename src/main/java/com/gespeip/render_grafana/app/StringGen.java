package com.gespeip.render_grafana.app;

import java.util.Random;

public class StringGen {
    public static String genString(int left, int right, int length) {
        return new Random().ints(left, right + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
