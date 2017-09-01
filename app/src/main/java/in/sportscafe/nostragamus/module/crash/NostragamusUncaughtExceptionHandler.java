package in.sportscafe.nostragamus.module.crash;

import android.content.Context;
import android.content.Intent;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.newChallenges.ui.NewChallengesActivity;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;

/**
 * Created by Jeeva on 8/8/16.
 */
public class NostragamusUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (null != ex) {
            ex.printStackTrace();
        }

        if (NostragamusDataHandler.getInstance().isLoggedInUser()) {
            startNewIntent(NewChallengesActivity.class);
        } else {
            startNewIntent(LogInActivity.class);
        }

        if (!BuildConfig.DEBUG) {
            AppSnippet.closeApp();
        }
    }

    private void startNewIntent(Class<?> cls) {
        Context context = Nostragamus.getInstance().getBaseContext();
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}