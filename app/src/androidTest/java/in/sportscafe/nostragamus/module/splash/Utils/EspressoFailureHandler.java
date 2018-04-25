package in.sportscafe.nostragamus.module.splash.Utils;

import android.app.Instrumentation;
import android.support.test.espresso.FailureHandler;
import android.support.test.espresso.base.DefaultFailureHandler;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;

/**
 * Created by sc on 24/4/18.
 */

public class EspressoFailureHandler implements FailureHandler {

    private final String TAG = EspressoFailureHandler.class.getSimpleName();
    private final FailureHandler failureHandler;

    public EspressoFailureHandler(Instrumentation instrumentation) {
        failureHandler = new DefaultFailureHandler(instrumentation.getTargetContext());
    }

    @Override
    public void handle(Throwable error, Matcher<View> viewMatcher) {
        Log.e(TAG, "Esporesso error : ");
        error.printStackTrace();
        failureHandler.handle(error, viewMatcher);
    }
}
