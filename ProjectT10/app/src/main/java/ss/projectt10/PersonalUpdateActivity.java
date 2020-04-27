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

public class PersonalUpdateActivity extends AppCompatActivity {
    private TextView accountId, fullName, dateOfBirth, address, email, phoneNumber, company;
    private Button btnUpdate;

    private Account myAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_update);

        accountId = findViewById(R.id.ac_personal_update_accountId);
        fullName = findViewById(R.id.ac_personal_update_fullName);
        dateOfBirth = findViewById(R.id.ac_personal_update_dateOfBirth);
        address = findViewById(R.id.ac_personal_update_address);
        email = findViewById(R.id.ac_personal_update_Email);
        phoneNumber = findViewById(R.id.ac_personal_update_phoneNumber);
        company = findViewById(R.id.ac_personal_update_company);

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
        myAcc = (Account) intent.getSerializableExtra("data");
        if (myAcc != null) {
            accountId.setText(myAcc.getAccountId());
            fullName.setText(myAcc.getFullName());
            dateOfBirth.setText(myAcc.getDateOfBirth());
            address.setText(myAcc.getAddress());
            email.setText(myAcc.getEmail());
            phoneNumber.setText(myAcc.getPhoneNumber());
            company.setText(myAcc.getCompany());
        } else {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDataAccount() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("account");
        String name = fullName.getText().toString();
        String dateBirth = dateOfBirth.getText().toString();
        String add = address.getText().toString();
        String mail = email.getText().toString();
        String phone = phoneNumber.getText().toString();
        String com = company.getText().toString();

        myAcc.setFullName(name);
        myAcc.setDateOfBirth(dateBirth);
        myAcc.setAddress(add);
        myAcc.setEmail(mail);
        myAcc.setPhoneNumber(phone);
        myAcc.setCompany(com);

        database.child("MASON").setValue(myAcc);
    }
}
