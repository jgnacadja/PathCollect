package org.odk.collect.android.support.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.odk.collect.android.support.matchers.CustomMatchers.withIndex;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;

import org.hamcrest.Matchers;
import org.odk.collect.android.R;
import org.odk.collect.android.support.ActivityHelpers;
import org.odk.collect.android.support.WaitFor;
import org.odk.collect.android.utilities.FlingRegister;

import java.util.concurrent.Callable;

public class FormEntryPage extends Page<FormEntryPage> {

    private final String formName;

    public FormEntryPage(String formName) {
        this.formName = formName;
    }

    @Override
    public FormEntryPage assertOnPage() {
        // Make sure we wait for loading to finish
        WaitFor.waitFor((Callable<Void>) () -> {
            assertTextDoesNotExist(R.string.loading_form);
            return null;
        });

        assertToolbarTitle(formName);
        return this;
    }

    public FormEntryPage fillOut(QuestionAndAnswer... questionsAndAnswers) {
        FormEntryPage page = this;

        for (int i = 0; i < questionsAndAnswers.length; i++) {
            QuestionAndAnswer current = questionsAndAnswers[i];
            page = page.answerQuestion(current.question, current.isRequired, current.answer);

            if (i < questionsAndAnswers.length - 1) {
                QuestionAndAnswer next = questionsAndAnswers[i + 1];
                page = page.swipeToNextQuestion(next.question, current.isRequired);
            }
        }

        return page;
    }

    public MainMenuPage fillOutAndSave(QuestionAndAnswer... questionsAndAnswers) {
        return fillOut(questionsAndAnswers)
                .swipeToEndScreen()
                .clickSaveAndExit();
    }

    public FormEntryPage swipeToNextQuestion(String questionText) {
        return swipeToNextQuestion(questionText, false);
    }

    public FormEntryPage swipeToNextQuestion(String questionText, boolean isRequired) {
        flingLeft();

        // Make sure any saving finishes (can happen with saveIncomplete forms)
        WaitFor.waitFor((Callable<Void>) () -> {
            assertTextDoesNotExist(R.string.saving_form);
            return null;
        });

        if (isRequired) {
            assertQuestionText("* " + questionText);
        } else {
            assertQuestionText(questionText);
        }

        return this;
    }

    public FormEntryPage swipeToPreviousQuestion(String questionText) {
        return swipeToPreviousQuestion(questionText, false);
    }

    public FormEntryPage swipeToPreviousQuestion(String questionText, boolean isRequired) {
        onView(withId(R.id.questionholder)).perform(swipeRight());

        if (isRequired) {
            assertQuestionText("* " + questionText);
        } else {
            assertQuestionText(questionText);
        }

        return this;
    }

    public FormEntryPage swipeToNextRepeat(String repeatLabel, int repeatNumber) {
        waitForText(repeatLabel + " > " + (repeatNumber - 1));
        flingLeft();
        waitForText(repeatLabel + " > " + repeatNumber);
        return this;
    }

    public FormEndPage swipeToEndScreen() {
        flingLeft();
        return WaitFor.waitFor(() -> new FormEndPage(formName).assertOnPage());
    }

    public ErrorDialog swipeToNextQuestionWithError() {
        flingLeft();
        return new ErrorDialog().assertOnPage();
    }

    public FormEntryPage swipeToNextQuestionWithConstraintViolation(String constraintText) {
        flingLeft();
        assertConstraintDisplayed(constraintText);

        return this;
    }

    private void assertQuestionText(String text) {
        onView(withIndex(withId(R.id.text_label), 0)).check(matches(withText(containsString(text))));
    }

    public FormEntryPage clickOptionsIcon() {
        tryAgainOnFail(() -> {
            Espresso.openActionBarOverflowOrOptionsMenu(ActivityHelpers.getActivity());
            assertText(R.string.project_settings);
        });

        return this;
    }

    public ProjectSettingsPage clickGeneralSettings() {
        onView(withText(getTranslatedString(R.string.project_settings))).perform(click());
        return new ProjectSettingsPage().assertOnPage();
    }

    public FormEntryPage assertNavigationButtonsAreDisplayed() {
        onView(withId(R.id.form_forward_button)).check(matches(isDisplayed()));
        onView(withId(R.id.form_back_button)).check(matches(isDisplayed()));
        return this;
    }

    public FormEntryPage assertNavigationButtonsAreHidden() {
        onView(withId(R.id.form_forward_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.form_back_button)).check(matches(not(isDisplayed())));
        return this;
    }

    public FormHierarchyPage clickGoToArrow() {
        onView(withId(R.id.menu_goto)).perform(click());
        return new FormHierarchyPage(formName).assertOnPage();
    }

    public FormEntryPage clickWidgetButton() {
        onView(withId(R.id.simple_button)).perform(click());
        return this;
    }

    public FormEntryPage clickRankingButton() {
        onView(withId(R.id.simple_button)).perform(click());
        return this;
    }

    public FormEntryPage deleteGroup(String questionText) {
        onView(withText(questionText)).perform(longClick());
        onView(withText(R.string.delete_repeat)).perform(click());
        clickOnButtonInDialog(R.string.discard_group, this);
        return this;
    }

    public FormEntryPage clickForwardButton() {
        onView(withText(getTranslatedString(R.string.form_forward))).perform(click());
        return this;
    }

    public FormEndPage clickForwardButtonToEndScreen() {
        onView(withText(getTranslatedString(R.string.form_forward))).perform(click());
        return new FormEndPage(formName).assertOnPage();
    }

    public FormEntryPage clickBackwardButton() {
        onView(withText(getTranslatedString(R.string.form_backward))).perform(click());
        return this;
    }

    public FormEntryPage clickSave() {
        onView(withId(R.id.menu_save)).perform(click());
        return this;
    }

    public ChangesReasonPromptPage clickSaveWithChangesReasonPrompt() {
        onView(withId(R.id.menu_save)).perform(click());
        return new ChangesReasonPromptPage(formName).assertOnPage();
    }

    public AddNewRepeatDialog clickPlus(String repeatName) {
        onView(withId(R.id.menu_add_repeat)).perform(click());
        return new AddNewRepeatDialog(repeatName).assertOnPage();
    }

    public FormEntryPage longPressOnQuestion(int id, int index) {
        onView(withIndex(withId(id), index)).perform(longClick());
        return this;
    }

    public FormEntryPage longPressOnQuestion(String question) {
        longPressOnQuestion(question, false);
        return this;
    }

    public FormEntryPage longPressOnQuestion(String question, boolean isRequired) {
        if (isRequired) {
            onView(withText("* " + question)).perform(longClick());
        } else {
            onView(withText(question)).perform(longClick());
        }

        return this;
    }

    public FormEntryPage removeResponse() {
        onView(withText(R.string.clear_answer)).perform(click());
        return clickOnButtonInDialog(R.string.discard_answer, this);
    }

    public AddNewRepeatDialog swipeToNextQuestionWithRepeatGroup(String repeatName) {
        flingLeft();
        return WaitFor.waitFor(() -> new AddNewRepeatDialog(repeatName).assertOnPage());
    }

    public AddNewRepeatDialog swipeToPreviousQuestionWithRepeatGroup(String repeatName) {
        flingRight();
        return WaitFor.waitFor(() -> new AddNewRepeatDialog(repeatName).assertOnPage());
    }

    public FormEntryPage answerQuestion(String question, String answer) {
        answerQuestion(question, false, answer);
        return this;
    }

    public FormEntryPage answerQuestion(String question, boolean isRequired, String answer) {
        if (isRequired) {
            assertQuestionText("* " + question);
        } else {
            assertQuestionText(question);
        }

        inputText(answer);
        closeSoftKeyboard();
        return this;
    }

    public FormEntryPage answerQuestion(int index, String answer) {
        onView(withIndex(withClassName(endsWith("Text")), index)).perform(replaceText(answer));
        return this;
    }

    public FormEntryPage assertQuestion(String text) {
        return assertQuestion(text, false);
    }

    public FormEntryPage assertQuestion(String text, boolean isRequired) {
        if (isRequired) {
            waitForText("* " + text);
        } else {
            waitForText(text);
        }

        return this;
    }

    private void flingLeft() {
        tryAgainOnFail(() -> {
            FlingRegister.attemptingFling();
            onView(withId(R.id.questionholder)).perform(swipeLeft());

            WaitFor.waitFor(() -> {
                if (FlingRegister.isFlingDetected()) {
                    return true;
                } else {
                    throw new RuntimeException("Fling never detected!");
                }
            });
        }, 5);
    }

    private void flingRight() {
        tryAgainOnFail(() -> {
            FlingRegister.attemptingFling();
            onView(withId(R.id.questionholder)).perform(swipeRight());

            WaitFor.waitFor(() -> {
                if (FlingRegister.isFlingDetected()) {
                    return true;
                } else {
                    throw new RuntimeException("Fling never detected!");
                }
            });
        }, 5);
    }

    public FormEntryPage openSelectMinimalDialog() {
        openSelectMinimalDialog(0);
        return this;
    }

    public FormEntryPage openSelectMinimalDialog(int index) {
        onView(withIndex(withClassName(Matchers.endsWith("TextInputEditText")), index)).perform(click());
        return this;
    }

    public FormEntryPage assertSelectMinimalDialogAnswer(String answer) {
        onView(withId(R.id.answer)).check(matches(withText(answer)));
        return this;
    }

    public OkDialog swipeToEndScreenWhileRecording() {
        flingLeft();
        OkDialog okDialog = new OkDialog().assertOnPage();
        assertText(R.string.recording_warning);
        return okDialog;
    }

    public CancelRecordingDialog clickRecordAudio() {
        clickOnString(R.string.record_audio);
        return new CancelRecordingDialog(formName);
    }

    public FormEntryPage assertConstraintDisplayed(String constraintText) {
        // Constraints warnings show as dialogs in Android 11+
        if (Build.VERSION.SDK_INT < 30) {
            checkIsToastWithMessageDisplayed(constraintText);
        } else {
            new OkDialog().assertOnPage()
                    .assertText(constraintText)
                    .clickOK(this);
        }

        return this;
    }

    public MainMenuPage pressBackAndIgnoreChanges() {
        return closeSoftKeyboard()
                .pressBack(new SaveOrIgnoreDialog<>(formName, new MainMenuPage()))
                .clickIgnoreChanges();
    }

    public FormEntryPage assertBackgroundLocationSnackbarShown() {
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(String.format(ApplicationProvider.getApplicationContext().getString(R.string.background_location_enabled), "⋮"))));
        return this;
    }

    public static class QuestionAndAnswer {

        private final String question;
        private final String answer;
        private final boolean isRequired;

        public QuestionAndAnswer(String question, String answer) {
            this(question, answer, false);
        }

        public QuestionAndAnswer(String question, String answer, boolean isRequired) {
            this.question = question;
            this.answer = answer;
            this.isRequired = isRequired;
        }
    }
}
