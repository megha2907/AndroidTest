package in.sportscafe.nostragamus.module.splash;


import android.app.Instrumentation;
import android.content.Intent;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.FailureHandler;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.base.DefaultFailureHandler;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListItem;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.splash.Utils.EspressoFailureHandler;
import in.sportscafe.nostragamus.module.splash.Utils.ThreadHelper;
import in.sportscafe.nostragamus.module.splash.assertion.RecyclerViewItemCountAssertion;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SimpleJoinFlow  {

    private final String TAG = SimpleJoinFlow.class.getSimpleName();

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    @Rule
    public ActivityTestRule<NostraHomeActivity> mActivityTestRule = new ActivityTestRule<>(NostraHomeActivity.class);

    @Before
    public void wakupDevice() throws Throwable {
        uiThreadTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NostraHomeActivity splashActivity = mActivityTestRule.getActivity();
                splashActivity.getWindow()
                        .addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        });

        // Custom Failure handler
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Espresso.setFailureHandler(new EspressoFailureHandler(instrumentation));
    }

    @Test
    public void simpleJoinFlow() {

        /* Wait for some time so that initial loading gets over
         */
        ThreadHelper.pauseMainThread(5000);
        /*
        If user is not Logged in, make him to do so
         */
        if (!NostragamusDataHandler.getInstance().isLoggedInUser()) {
            // Login through FB
            ViewInteraction relativeLayout = onView(
                    allOf(withId(R.id.login_btn_fb),
                            withParent(allOf(withId(R.id.content),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            relativeLayout.perform(click());

            ThreadHelper.pauseMainThread(5000);
        }

        /*
        Home screen with New challenges
         */
        onView(allOf(withId(R.id.challenge_recycler), isDisplayed()));



        ThreadHelper.pauseMainThread(2000);
        ViewInteraction recyclerView = onView(allOf(withId(R.id.challenge_recycler), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));



        ThreadHelper.pauseMainThread(2000);
        onView(allOf(withId(R.id.match_timeline_rv), isDisplayed()));
        ViewInteraction matchesInteraction = onView(withId(R.id.match_timeline_rv)).check(new RecyclerViewItemCountAssertion(greaterThan(0)));
        matchesInteraction.check(matches(hasDescendant(withText("Play"))));



        ThreadHelper.pauseMainThread(2000);
        ViewInteraction customButton = onView(allOf(withId(R.id.new_challenge_matches_join_button), isDisplayed()));
        customButton.perform(click());



        ThreadHelper.pauseMainThread(2000);
        ViewInteraction contest_recycler = onView(allOf(withId(R.id.contest_recycler), isDisplayed()));
        contest_recycler.perform(actionOnItemAtPosition(0, click()));
//        onView(withId(R.id.contest_recycler)).check(new RecyclerViewItemCountAssertion(greaterThan(0)));



        ThreadHelper.pauseMainThread(2000);
        ViewInteraction joinButton = onView(allOf(withId(R.id.join_contest_btn), isDisplayed()));
        joinButton.perform(click());



        ThreadHelper.pauseMainThread(2000);
        ViewInteraction customButton3 = onView(
                allOf(withId(R.id.complete_payment_btn), isDisplayed()));
        customButton3.perform(click());



        ThreadHelper.pauseMainThread(2000);
        ViewInteraction inplayRecyclerInteration =  onView(allOf(withId(R.id.inplay_contest_recycler), isDisplayed()));
        inplayRecyclerInteration.withFailureHandler(new FailureHandler() {
            @Override
            public void handle(Throwable error, Matcher<View> viewMatcher) {
                Log.e(TAG, "Failure handler");
            }
        });

    }

}
