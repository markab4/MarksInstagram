package mark.marksinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";


    // UI references.
    private AutoCompleteTextView etUsername;
    private EditText etPassword;
    private AutoCompleteTextView etNewUsername;
    private EditText etNewPassword;
    private EditText etNewEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        etNewEmail = findViewById(R.id.etNewEmail);


        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                attemptLogin(username, password);
            }
        });

        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etNewUsername.getText().toString(),
                        password = etNewPassword.getText().toString(),
                        email = etNewEmail.getText().toString();
                if(isEmailValid(email) && isPasswordValid(password) && username.length() > 4)
                    attemptSignUp(username, password, email);
                else{
                    Toast.makeText(LoginActivity.this, "Make sure you enter a valid " + (isEmailValid(email) ? "password!" : "email!"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void attemptSignUp(final String username, final String password, final String email) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    attemptLogin(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "There was an error signing up", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with sign up.\tusername: " + username + "\tpassword: " + password + "\temail: " + email);
                    e.printStackTrace();
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }

    private void attemptLogin(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Toast.makeText(LoginActivity.this, "There was an error logging in", Toast.LENGTH_SHORT).show(); //TODO: better error handling
                    Log.e(TAG, "Issue with login");
                    e.printStackTrace();
                    return;
                }
                goMainActivity();

            }
        });
//        if (mAuthTask != null) {
//            return;
//        }
//
//        // Reset errors.
//        etUsername.setError(null);
//        etPassword.setError(null);
//
//        // Store values at the time of the login attempt.
//        String email = etUsername.getText().toString();
//        String password = etPassword.getText().toString();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            etPassword.setError(getString(R.string.error_invalid_password));
//            focusView = etPassword;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            etUsername.setError(getString(R.string.error_field_required));
//            focusView = etUsername;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            etUsername.setError(getString(R.string.error_invalid_email));
//            focusView = etUsername;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
//        }
    }

    private void goMainActivity() {
        Log.d(TAG, "Navigating to Main Activity");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

}

