package org.odk.collect.android.support.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.odk.collect.android.R;

public class FormMetadataPage extends PreferencePage<FormMetadataPage> {

    @Override
    public FormMetadataPage assertOnPage() {
        assertText(R.string.form_metadata_title);
        return this;
    }

    public FormMetadataPage clickEmail() {
        onView(withText(getTranslatedString(R.string.email))).perform(click());
        return this;
    }

    public FormMetadataPage clickUsername() {
        onView(withText(getTranslatedString(R.string.username))).perform(click());
        return this;
    }

    public FormMetadataPage clickPhoneNumber() {
        onView(withText(getTranslatedString(R.string.phone_number))).perform(click());
        return this;
    }
}
