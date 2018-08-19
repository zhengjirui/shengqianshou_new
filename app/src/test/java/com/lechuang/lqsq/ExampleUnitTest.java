package com.lechuang.lqsq;

import com.lechuang.lqsq.utils.StringUtils;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(Pattern.matches(".*[?|&]id=.*","http://&id=1111"));
    }
}