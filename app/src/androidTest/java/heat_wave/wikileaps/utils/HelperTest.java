package heat_wave.wikileaps.utils;

import android.support.test.InstrumentationRegistry;

import junit.framework.TestCase;

/**
 * Created by heat_wave on 16.12.16.
 */

public class HelperTest extends TestCase {
    public void testGetRunsCount() throws Exception {
        assertFalse(Helper.getRunsCount().isEmpty());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Helper.initSharedPrefs(InstrumentationRegistry.getContext());
    }

    public void testInitSharedPrefs() throws Exception {
        assertNotNull(Helper.getPreferences());
    }



    public void testParseUnicodeString() throws Exception {
        assertEquals("https://en.wikipedia.org/wiki/Ü", Helper.parseUnicodeString("https://en.wikipedia.org/wiki/%C3%9C"));
        assertNotSame("https://en.wikipedia.org/wiki/%C3%9C", Helper.parseUnicodeString("https://en.wikipedia.org/wiki/%C3%9C"));
    }

}