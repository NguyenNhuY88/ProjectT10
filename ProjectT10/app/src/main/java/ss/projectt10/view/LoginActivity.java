package ss.projectt10.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ss.projectt10.BaseActivity;
import ss.projectt10.HomeActivity;
import ss.projectt10.R;


public class LoginActivity extends BaseActivity {
    private EditText mEmailField, mPasswordField;
    private FirebaseAuth mAuth;

    private Button mSignUpButton, mLogInButton, mResetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        setProgressBar(R.id.progressBar);
        mSignUpButton = (Button) findViewById(R.id.btn_signup);
        mLogInButton = (Button) findViewById(R.id.btn_login);
        mResetButton = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgressBar();


                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                hideProgressBar();
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                                } else {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
