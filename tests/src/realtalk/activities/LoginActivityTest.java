package com.realtalk;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class realtalk.activities.LoginActivityTest \
 * com.realtalk.tests/android.test.InstrumentationTestRunner
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public LoginActivityTest() {
        super("com.realtalk", LoginActivity.class);
    }

}
