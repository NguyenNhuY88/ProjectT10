package ss.projectt10.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

import ss.projectt10.HomeActivity;
import ss.projectt10.R;
import ss.projectt10.model.User;

import static ss.projectt10.BaseActivity.DBPROJECTNAME;
import static ss.projectt10.BaseActivity.DBUSER;
import static ss.projectt10.BaseActivity.databaseInstance;
import static ss.projectt10.helper.Util.AVATAR_DEFAULT;

public class LoginWithPhoneNumber extends AppCompatActivity {

    EditText editTextPhone, editTextCode;
    Button getOPTButton, loginButonWithphone;

    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    String codeSent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = databaseInstance.getReference();
        mDatabase.keepSynced(true);

        setContentView(R.layout.activity_login_with_phone_number);

        mAuth = FirebaseAuth.getInstance();

        editTextCode = findViewById(R.id.editTextCode);
        editTextPhone = findViewById(R.id.editTextPhone);

        findViewById(R.id.buttonGetVerificationCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
                editTextCode.setFocusable(true);
            }
        });


        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        });
    }

    private void verifySignInCode() {
        String code = editTextCode.getText().toString();
        if (code != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(LoginWithPhoneNumber.this,"Hãy nhập mã xách minh", Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity


                            FirebaseUser user = task.getResult().getUser();

                            onAuthSucess(user);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Mã xác minh không đúng ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode() {

        String phone = editTextPhone.getText().toString();

        Toast.makeText(LoginWithPhoneNumber.this, phone, Toast.LENGTH_LONG).show();

        if (phone.isEmpty()) {
            editTextPhone.setError("Hãy nhập số điện thoại");
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() < 10) {
            editTextPhone.setError("Hãy nhập mã xác minh");
            editTextPhone.requestFocus();
            return;
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" +phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.i("SMSL", "onVerificationCompleted");
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                Toast.makeText(LoginWithPhoneNumber.this, code, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(LoginWithPhoneNumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };



    private void onAuthSucess ( FirebaseUser user) {
        writeNewUser(user.getUid(), user.getPhoneNumber());

        // go to HomeAcitivity
        startActivity(new Intent(LoginWithPhoneNumber.this, HomeActivity.class));
        finish();
    }
    private void writeNewUser(String userId, String phone) {
        final User user = new User("", "", AVATAR_DEFAULT, "", "", phone );

        mDatabase.child(DBPROJECTNAME).child(DBUSER).child(userId).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(getApplicationContext(),
                        "Đăng nhập thành công", Toast.LENGTH_LONG).show();
            }

        });
    }

}
