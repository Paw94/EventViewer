package com.example.paw.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paw.myapplication.model.User;
import com.example.paw.myapplication.R;
import com.example.paw.myapplication.room.config.AppDatabase;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.logging.Level;
import java.util.logging.Logger;



public class RegisterActivity extends AppCompatActivity {

    private EditText rEmail;
    private EditText rName;
    private EditText rPassword;
    private Logger logger = Logger.getLogger(RegisterActivity.class.toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        logger.log(Level.INFO, "Created RegisterActivity");

        setContentView(R.layout.activity_register);

        rEmail = (EditText) findViewById(R.id.emailR);
        rName = (EditText) findViewById(R.id.nameR);
        rPassword = (EditText) findViewById(R.id.passwordR);

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = rEmail.getText().toString();
                String name = rName.getText().toString();
                String password = rPassword.getText().toString();

                boolean validateEmail = EmailValidator.getInstance(true).isValid(email);
                boolean validateName = false;
                boolean validatePassword = false;

                if (!validateEmail){
                    rEmail.setError("Email is invalid");
                }

                if (name.length()<2){
                    validateName = false;
                    rName.setError("Name is too short");
                }
                else validateName = true;

                if (password.length()<2){
                    validatePassword=false;
                    rPassword.setError("Password is too short");
                }
                else validatePassword = true;

                if (validateEmail && validatePassword && validateName) {
                    new requestTask().execute(email, name, password);
                }

                //attemptSignUp();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void attemptSignUp() {
        hideKeyboard(RegisterActivity.this);
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

    class requestTask extends AsyncTask<String, Void, String > {

        protected String doInBackground(String... urls) {
            try {
                String userMail = urls[0];
                String userName = urls[1];
                String userPassword = urls[2];

                User newUser = new User();
                newUser.setEmail(userMail);
                newUser.setName(userName);
                newUser.setPassword(userPassword);

                if (AppDatabase.getInstance(getBaseContext()).userDao().getUserByMail(newUser.getEmail())==null){
                    AppDatabase.getInstance(getBaseContext()).userDao().insertUser(newUser);
                }
                else
                    return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return "elo";
        }

        protected void onPostExecute(String result) {
            if (result == null){
                Toast toast = Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_LONG);
                toast.show();
                rEmail.setError("Email already exists");
            }
            else {
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                attemptSignUp();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("isRegistered", true);
                startActivity(intent);
                finish();
            }
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }


}