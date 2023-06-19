package org.odk.collect.android.support.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import org.odk.collect.android.R;

public class ResetApplicationDialog extends Page<ResetApplicationDialog> {

    @Override
    public ResetApplicationDialog assertOnPage() {
        onView(withText(R.string.reset_settings_dialog_title)).inRoot(isDialog()).check(matches(isDisplayed()));
        return this;
    }
}



