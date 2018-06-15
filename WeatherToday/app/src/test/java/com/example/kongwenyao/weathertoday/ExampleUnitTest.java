package com.example.kongwenyao.weathertoday;

import com.example.kongwenyao.weathertoday.main_activity.MainActivity;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host). This unit tests on
 * on methods within MainActivity.class
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private MainActivity mainActivity = new MainActivity();
    private MainActivity.RetrieveData retrieveData = mainActivity.new RetrieveData();

    /**
     * Test method for setTempCelsius(String temperature) method within MainActivity.new RetrieveData
     */
    @Test
    public void testSetTemperatureC() throws Exception {
       String temp = retrieveData.setTempCelsius("38");

       assertNotNull(temp);
       assertEquals("38 \u2103", temp);
       assertTrue(retrieveData.setTempCelsius("1").equals("1 \u2103"));
    }

    /**
     * Test method for setTempFahrenheit(String temperature) method within MainActivity.new RetrieveData
     */
    @Test
    public void testSetTemperatureF() throws Exception {
        String temp = retrieveData.setTempFahrenheit("38");

        assertNotNull(temp);
        assertNotEquals("38 \u2103", temp);
        assertEquals("38 \u2109", temp);
        assertTrue(retrieveData.setTempFahrenheit("1").equals("1 \u2109"));
    }

    /**
     * Test method for setDatFormat(String date) method within MainActivity.new RetrieveData
     */
    @Test
    public void testSetDateFormat() throws Exception {
        String newDate = retrieveData.setDateFormat("Thu, 07 Dec 2017 8:52:00 +0800");

        assertEquals("Thursday, Dec 7", newDate);
        assertTrue(retrieveData.setDateFormat("Mon, 07 Nov 2017").equals("Monday, Nov 7"));
        assertFalse(retrieveData.setDateFormat("Mon, 27 Jan 2017").equals("Mon, Jan 27"));
    }

    /**
     * Test method for doInBackground(String... strings) method within MainActivity.new RetrieveData
     */
    @Test
    public void testDoInBackground() throws Exception {
        String conditions = "http://api.wunderground.com/api/e4287e3de768ea5e/conditions/q/autoip.json";
        String hourly = "http://api.wunderground.com/api/e4287e3de768ea5e/hourly/q/autoip.json";
        Map<String, String> jsonTexts = retrieveData.doInBackground(conditions, hourly);
        assertNotNull(jsonTexts);
    }

    /**
     * Test method for processTimeFormat(String time) method within MainActivity.new RetrieveData
     */
    @Test
    public void testProcessTimeFormat() throws Exception {
        String time = retrieveData.processTimeFormat("5:00 PM");

        assertNotNull(time);
        assertEquals("5 pm", time);
        assertTrue(retrieveData.processTimeFormat("10:00 AM").equals("10 am"));
    }
}