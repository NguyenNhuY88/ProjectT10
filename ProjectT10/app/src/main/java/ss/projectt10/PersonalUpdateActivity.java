package ss.projectt10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ss.projectt10.model.Account;
import ss.projectt10.model.User;

import static ss.projectt10.BaseActivity.DBPROJECTNAME;
import static ss.projectt10.BaseActivity.DBUSER;

public class PersonalUpdateActivity extends BaseActivity {

    private TextView accountEmail, fullName, dateOfBirth, address,  phoneNumber ;
    private Button btnUpdate;

    private User myAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_update);

        accountEmail = findViewById(R.id.ac_personal_update_accountId);
        fullName = findViewById(R.id.ac_personal_update_fullName);
        dateOfBirth = findViewById(R.id.ac_personal_update_dateOfBirth);
        address = findViewById(R.id.ac_personal_update_address);
        phoneNumber = findViewById(R.id.ac_personal_update_phoneNumber);


        btnUpdate = findViewById(R.id.ac_personal_update_btnAccept);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataAccount();
                finish();
            }
        });

        loadDataAccount();
    }

    private void loadDataAccount() {
        Intent intent = getIntent();
        myAcc = (User) intent.getSerializableExtra("data");
        if (myAcc != null) {
            accountEmail.setText(myAcc.getEmail());
            fullName.setText(myAcc.getUsername());
            dateOfBirth.setText(myAcc.getDateOfBirth());
            address.setText(myAcc.getAddress());
            phoneNumber.setText(myAcc.getPhoneNumber());
        } else {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDataAccount() {
        DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference();
//        = ;
       // DatabaseReference database = FirebaseDatabase.getInstance().getReference("account");
        String name = fullName.getText().toString();
        String dateBirth = dateOfBirth.getText().toString();
        String add = address.getText().toString();
        String phone = phoneNumber.getText().toString();


        myAcc.setUsername(name);
        myAcc.setDateOfBirth(dateBirth);
        myAcc.setAddress(add);
        myAcc.setPhoneNumber(phone);

        mDatabase.child(DBPROJECTNAME).child(DBUSER).child(getUid()).setValue(myAcc);
    }
}
