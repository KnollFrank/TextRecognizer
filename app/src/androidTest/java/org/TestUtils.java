package org;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

public class TestUtils {

    public static Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }
}
