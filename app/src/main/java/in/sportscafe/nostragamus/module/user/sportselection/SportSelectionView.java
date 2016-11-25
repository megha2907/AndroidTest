package in.sportscafe.nostragamus.module.user.sportselection;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.View;

/**
 * Created by Jeeva on 27/5/16.
 */
public interface SportSelectionView extends View {

    void setAdapter(RecyclerView.Adapter adapter);

    void navigateToLogin();

    void navigateToHome();

    void navigateToEditProfile();

    void goBackWithSuccessResult();

    void showToast();

    void changeViewforProfile();

    void changeViewforLogin();
}