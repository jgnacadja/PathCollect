package org.odk.collect.android.utilities;

import static android.net.Uri.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

@RunWith(AndroidJUnit4.class)
public class ExternalWebPageHelperTest {

    @Test
    public void uriShouldBeNormalized() {
        Activity activity = Robolectric.buildActivity(Activity.class).get();

        ExternalWebPageHelper externalWebPageHelper = new ExternalWebPageHelper();
        externalWebPageHelper.openWebPageInCustomTab(activity, parse("HTTP://example.com"));

        Uri uri = shadowOf(activity).getNextStartedActivity().getData();
        assertThat(uri, equalTo(Uri.parse("http://example.com")));
    }
}
