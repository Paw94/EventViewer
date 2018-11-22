package com.example.paw.myapplication.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paw.myapplication.model.User;
import com.example.paw.myapplication.MyApplication;
import com.example.paw.myapplication.R;
import com.example.paw.myapplication.room.config.AppDatabase;

import org.apache.commons.validator.routines.EmailValidator;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private UserLoginTask mAuthTask = null;
    //
    // UI references.
    //private AutoCompleteTextView mEmailView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences sp1;
    private boolean isValid;
    private Logger logger = Logger.getLogger(LoginActivity.class.toString());
    //
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        boolean showToast = getIntent().getBooleanExtra("isRegistered", false);
        if (showToast) {
            Toast toast = Toast.makeText(getApplicationContext(), "Account was added to database. You can now log in", Toast.LENGTH_LONG);
            toast.show();
        }


        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        logger.log(Level.INFO, "Created LoginActivity");
//        SharedPreferences sp = getSharedPreferences("Login", 0);
//        SharedPreferences.Editor Ed = sp.edit();
//        Ed.putString("Unm", null);
//        Ed.putString("Psw", null);
//        Ed.commit();

        if (checkCredentials()) {

            sp1 = getSharedPreferences("Login", 0);
            String email = sp1.getString("Unm", null);
            String password = sp1.getString("Psw", null);


            setContentView(R.layout.activity_login);
            mEmailView = findViewById(R.id.email);
            mPasswordView = findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();

                        return true;
                    }
                    return false;
                }
            });

            Button signInButton = findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            Button signUpButton = findViewById(R.id.register_button);
            signUpButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                    startActivity(intent);
                    //finish();
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress_bar);


            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            showProgress(false);

        } else {
            setContentView(R.layout.activity_login);
            mEmailView = findViewById(R.id.email);
            mPasswordView = findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();

                        return true;
                    }
                    return false;
                }
            });

            Button signInButton = findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            Button signUpButton = findViewById(R.id.register_button);
            signUpButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress_bar);
        }
    }


//
//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }
//
//
//    /**
//     * Attempts to sign in or register the account specified by the login form.
//     * If there are form errors (invalid email, missing fields, etc.), the
//     * errors are presented and no actual login attempt is made.
//     */

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);


        }
    }


    private boolean isEmailValid(String email) {
        boolean allowLocal = true;
        return EmailValidator.getInstance(allowLocal).isValid(email);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 1;
    }

    //    /**
//     * Shows the progress UI and hides the login form.
//     */


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }
//    /**
//     * Represents an asynchronous login/registration task used to authenticate
//     * the user.
//     */

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                hideKeyboard(LoginActivity.this);

                User user = AppDatabase.getInstance(getBaseContext()).userDao().getUserByMail(mEmail);
                if(user!=null)
                    if(user.getPassword().equals(mPassword)) isValid=true;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (isValid) {
                MyApplication.getInstance().setUser(AppDatabase.getInstance(getBaseContext()).userDao().getUserByMail(mEmail));
                saveCredentials(mEmail, mPassword);
                return true;
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Account doesn't exist", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean success) {
            mAuthTask = null;
            showProgress(false);


            if (success == null){
                Snackbar.make(findViewById(android.R.id.content), "Something happened", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else if (success) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

    public void saveCredentials(String email, String password) {
        // saving given credentials to shared preferences //
        SharedPreferences sp = getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("Unm", email);
        Ed.putString("Psw", password);
        Ed.commit();
    }

    public boolean checkCredentials() {
        // Checking if credentials are not NULL //
        // TODO: if not NULL validate with DB //
        sp1 = getSharedPreferences("Login", 0);
        String username = sp1.getString("Unm", null);
        String password = sp1.getString("Psw", null);

        User user = AppDatabase.getInstance(getApplicationContext()).userDao().getUserByMail(username);
        boolean correctPref = false;
        if(user!=null)
            if(user.getPassword().equals(password)) correctPref=true;
        if (correctPref) MyApplication.getInstance().setUser(AppDatabase.getInstance(getApplicationContext()).userDao().getUserByMail(username));

        return correctPref;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

