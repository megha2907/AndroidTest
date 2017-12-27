package in.sportscafe.nostragamus.module.splash;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.sportscafe.nostragamus.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class JoinContestTwo {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void joinContestTwo() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.login_btn_fb),
                        withParent(allOf(withId(R.id.content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        relativeLayout.perform(click());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.challenge_recycler), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction customButton = onView(
                allOf(withId(R.id.new_challenge_matches_join_button), withText("Join Another Contest"), isDisplayed()));
        customButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatTextView")), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction customViewPager = onView(
                allOf(withId(R.id.contest_viewPager),
                        withParent(withId(R.id.contestsContentLayout)),
                        isDisplayed()));
        customViewPager.perform(swipeLeft());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatTextView")), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction customViewPager2 = onView(
                allOf(withId(R.id.contest_viewPager),
                        withParent(withId(R.id.contestsContentLayout)),
                        isDisplayed()));
        customViewPager2.perform(swipeRight());

        ViewInteraction customButton2 = onView(
                allOf(withId(R.id.pool_row_btn_join), withText("Join"),
                        withParent(allOf(withId(R.id.pool_row_ll_join_layout),
                                withParent(withId(R.id.pool_row_rl_detail_layout)))),
                        isDisplayed()));
        customButton2.perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction customButton3 = onView(
                allOf(withId(R.id.complete_payment_btn), withText("Join"), isDisplayed()));
        customButton3.perform(click());

    }

}
