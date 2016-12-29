package in.sportscafe.nostragamus.module.crash;

/**
 * Created by Jeeva on 8/8/16.
 */
public class NostragamusUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(null != ex) {
            ex.printStackTrace();
        }

        /*Context context = ScGame.getInstance().getBaseContext();
        Intent intent = new Intent(context, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.BundleKeys.SHOW_CRASH_REPORT, true);
        context.startActivity(intent);*/

//        AppSnippet.closeApp();
    }
}