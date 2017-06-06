package in.sportscafe.nostragamus.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class FragmentHelper {
	private static final String TAG = FragmentHelper.class.getSimpleName();

	public static void replaceFragmentImmediate(@NonNull AppCompatActivity activity,
												final int containerId, Fragment fragment) {
		FragmentManager manager = activity.getSupportFragmentManager();
		manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		manager.beginTransaction().replace(containerId, fragment, fragment.getClass().getSimpleName()).commit();
		manager.executePendingTransactions();
	}

	/** this method will add fragment to backstack having older fragment available in stack */
	public static void replaceAndAddContentFragment(@NonNull final AppCompatActivity activity, final int containerId,
													final Fragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(containerId, fragment, fragment.getClass().getSimpleName()).
				addToBackStack(null).commit();
		fragmentManager.executePendingTransactions();
	}

	/** this method will add fragment to backstack - older fragment will be in stack */
	public static void addContentFragment(@NonNull final AppCompatActivity activity, final int containerId,
										  final Fragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.beginTransaction().add(containerId, fragment,fragment.getClass().getSimpleName()).addToBackStack(null).commit();
		fragmentManager.executePendingTransactions();
	}

	/** this method won't add fragment to backstack but replaces it */
	public static void replaceFragment(@NonNull final AppCompatActivity activity,
									   final int containerId, final Fragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(containerId, fragment,fragment.getClass().getSimpleName()).commit();
		fragmentManager.executePendingTransactions();
	}

	public static void popBackStackInclusive(FragmentManager fragMgr) {
		if (fragMgr != null) {
			try {
				fragMgr.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			} catch (IllegalStateException e) {
				// ignore
				e.printStackTrace();
			}
		}
	}

	/** this method will add fragment to backstack */
	public static void addContentFragmentWithAnimation(@NonNull final AppCompatActivity activity, final int containerId,
													   final Fragment fragment, int enterAnimId, int exitAnimId,
													   int enterPopAnimId, int exitPopAnimId) throws Exception{
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(enterAnimId, exitAnimId, enterPopAnimId, exitPopAnimId);
		ft.add(containerId, fragment,fragment.getClass().getSimpleName()).addToBackStack(null)
				.commit();
		manager.executePendingTransactions();
	}

	/*public static void replaceAndAddContentFragmentWithAnimation(@NonNull final AppCompatActivity activity, final int containerId,
																 final Fragment fragment) {
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.slide_in_up, android.R.anim.slide_out_right, R.anim.slide_out_up, R.anim.slide_out_up);
		ft.replace(containerId, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
		manager.executePendingTransactions();
	}*/

	/*public static synchronized void addContentFragmentWithAnimation(@NonNull final AppCompatActivity activity, final int containerId,
													   final Fragment fragment) throws Exception {
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.slide_in_up, android.R.anim.slide_out_right, R.anim.slide_out_up, R.anim.slide_out_up);
		ft.add(containerId, fragment).addToBackStack(null)
				.commit();
		manager.executePendingTransactions();
	}

	public static synchronized void removeContentFragmentWithAnimation(@NonNull final AppCompatActivity activity, Fragment fragment) throws Exception{
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.slide_out_up, R.anim.slide_out_up);
		ft.remove(fragment);
		ft.commit();
		manager.popBackStackImmediate();
	}*/

}
