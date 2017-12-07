package com.example.kongwenyao.guessinggameapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    MainActivity mActivity = new MainActivity();

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        mActivity.setSecretValue(10);
        assertTrue(mActivity.inputVerification(10).equals("You guess it! Hooray!!"));

        mActivity.setMinimum(10);
        assertEquals(10, 10);


    }

}