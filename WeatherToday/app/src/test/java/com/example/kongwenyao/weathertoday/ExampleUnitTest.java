package com.example.kongwenyao.weathertoday;

import org.junit.Test;

import java.security.spec.ECField;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private final String conditions = "http://api.wunderground.com/api/e4287e3de768ea5e/conditions/q/autoip.json";
    private final String hourly = "http://api.wunderground.com/api/e4287e3de768ea5e/hourly/q/autoip.json";

    MainActivity mainActivity = new MainActivity();
    MainActivity.RetrieveData retrieveData = mainActivity.new RetrieveData();

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSetTemperature() throws Exception {
       String temp = retrieveData.setTempCelsius("38");
       assertNotNull(temp);
       assertEquals("38 \u2103", temp);
       assertTrue(retrieveData.setTempCelsius("1").equals("1 \u2103"));
    }

    @Test
    public void testSetDateFormat() throws Exception {
        String newDate = retrieveData.setDateFormat("Thu, 07 Dec 2017 8:52:00 +0800");
        assertEquals("Thursday, Dec 7", newDate);
        assertTrue(retrieveData.setDateFormat("Mon, 07 Nov 2017").equals("Monday, Nov 7"));
        assertFalse(retrieveData.setDateFormat("Mon, 27 Jan 2017").equals("Mon, Jan 27"));
    }

    @Test
    public void testDoInBackgroud() throws Exception {
        Map<String, String> jsontxt = retrieveData.doInBackground(conditions, hourly);
        assertNotNull(jsontxt);
    }
}