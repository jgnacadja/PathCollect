package org.odk.collect.android.support.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;

import org.odk.collect.android.R;

public class BlankFormSearchPage extends Page<BlankFormSearchPage> {

    @Override
    public BlankFormSearchPage assertOnPage() {
        onView(withHint(getTranslatedString(R.string.search))).check(matches(isDisplayed()));
        return this;
    }
}
