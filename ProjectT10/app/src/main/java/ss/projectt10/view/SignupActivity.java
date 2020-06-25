package ss.projectt10.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ss.projectt10.BaseActivity;
import ss.projectt10.HomeActivity;
import ss.projectt10.R;
import ss.projectt10.model.User;

import static ss.projectt10.helper.Util.AVATAR_DEFAULT;


public class SignupActivity extends BaseActivity {
    private EditText mEmailField, mPasswordField, mUserNameField;
    private Button mSignUpButton;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        mSignUpButton = (Button) findViewById(R.id.btn_sign_up);
        mEmailField = (EditText) findViewById(R.id.edt_email);
        mPasswordField = (EditText) findViewById(R.id.edt_pasword);
        mUserNameField = (EditText) findViewById(R.id.edt_username);
        setProgressBar(R.id.progressBar);
        mDatabase = databaseInstance.getReference();
        mDatabase.keepSynced(true);
        // khi nhan dang ky
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();
                final String username = mUserNameField.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Hãy nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_minimum_password), Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressBar();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignupActivity.this, "Tạo tài khoản thành công:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Tạo tài khoản thất bại" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            onAuthSucess(username, task.getResult().getUser());
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        hideProgressBar();
    }

    private void onAuthSucess (String username, FirebaseUser user) {
        writeNewUser(user.getUid(), username, user.getEmail());

        // go to HomeAcitivity
        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
        finish();
    }
    private void writeNewUser(String userId, String username, String email) {
        final User user = new User(username, email, AVATAR_DEFAULT, "", "", "");

        mDatabase.child(DBPROJECTNAME).child(DBUSER).child(userId).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(SignupActivity.this, "tạo tài khoản thành công", Toast.LENGTH_LONG);
                Log.i("TAOUSER", user.getUsername());
            }

        });
    }
    // [END basic_write]
}
