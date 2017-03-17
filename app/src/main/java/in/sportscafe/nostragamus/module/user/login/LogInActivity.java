package in.sportscafe.nostragamus.module.user.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.jeeva.android.Log;

import java.io.IOException;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.TermsConditions;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.onboard.OnBoardingFragment;

public class LogInActivity extends NostragamusActivity implements LogInView, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LogInActivity";

    private static final int RC_SIGN_IN = 9001;

    private static final String SCOPES = "oauth2:" + "https://www.googleapis.com/auth/plus.login"
            + " " + "https://www.googleapis.com/auth/userinfo.profile"
            /*+ " " + CLIENT_ID_SCOPE*/;

    private LogInPresenter mLogInPresenter;

    private GoogleApiClient mGoogleApiClient;

    private ProgressDialog mProgressDialog;

    private String personName;
    private String personEmail;
    private String personId;
    private String persongender;
    private String profileUrl;
    private String personPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Activity fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        TextView termstv = (TextView) findViewById(R.id.login_tv_terms);
        termstv.setText(Html.fromHtml(getString(R.string.termspolicy)));

        initGoogle();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fl_onboard_holder, OnBoardingFragment.newInstance()).commit();

        mLogInPresenter = LogInPresenterImpl.newInstance(LogInActivity.this);
        mLogInPresenter.onCreateLogIn(getIntent().getExtras());

    }

    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/plus.login"))
                .requestIdToken(getString(R.string.oauth_server_id))
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        signOut();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

    }

    @Override
    public void initViews() {
        //Setting Button click listeners
        findViewById(R.id.login_btn_fb).setOnClickListener(this);

        findViewById(R.id.login_btn_google).setOnClickListener(this);

        findViewById(R.id.login_tv_terms).setOnClickListener(this);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        onBackPressed();
    }

    @Override
    public void navigateToEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("screen", Constants.BundleKeys.LOGIN_SCREEN);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            if (null != result.getSignInAccount()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                personName = acct.getDisplayName();
                personEmail = acct.getEmail();
                personId = acct.getId();
                persongender = "";
                profileUrl = "";
                personPhoto = String.valueOf(acct.getPhotoUrl());

            }

            int statusCode = result.getStatus().getStatusCode();
            Log.i("status", String.valueOf(statusCode));
        } else {
            mLogInPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_fb:
                mLogInPresenter.onClickFacebook();
                break;
            case R.id.login_btn_google:
                mLogInPresenter.onClickGoogle();
                break;
            case R.id.login_tv_terms:
                gotoTermsConditions();
                break;
        }
    }

    public void gotoTermsConditions() {
        startActivity(new Intent(this, TermsConditions.class));
    }

    private void getGoogleAccessToken(final String email) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    token = GoogleAuthUtil.getToken(LogInActivity.this, email, SCOPES);
                } catch (IOException transientEx) {
                    // Network or server error, try later
                    Log.e(TAG, transientEx.toString());
                } catch (UserRecoverableAuthException e) {
                    // Recover (with e.getIntent())
                    Log.e(TAG, e.toString());
                    signIn(e.getIntent());
                } catch (GoogleAuthException authEx) {
                    // The call is not ever expected to succeed
                    // assuming you have already verified that
                    // Google Play services is installed.
                    Log.e(TAG, authEx.toString());
                }

                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                if (null != token) {
                    Log.d("GPlus Token", token);
                    mLogInPresenter.onSuccessGoogleToken(token,
                            personId, personName,
                            persongender, profileUrl,
                            personEmail, personPhoto);
                }
            }

        };
        task.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        // signOut();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (null != result) {
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                getGoogleAccessToken(result.getSignInAccount().getEmail());

            }
        }
    }

    @Override
    public void signIn(Intent intent) {
        if (null == intent) {
            intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        }
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOut();
    }

    @Override
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    public void showProgressDialog() {
        showProgressbar();
        /*if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();*/
    }

    @Override
    public void hideProgressDialog() {
        dismissProgressbar();
        /*if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }*/
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.LOGIN;
    }
}