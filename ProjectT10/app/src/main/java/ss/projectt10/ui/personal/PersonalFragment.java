package ss.projectt10.ui.personal;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ss.projectt10.PersonalUpdateActivity;
import ss.projectt10.R;
import ss.projectt10.model.Account;

public class PersonalFragment extends Fragment {

    private PersonalViewModel mViewModel;
    private View personalView;

    private ImageView avatar;
    private TextView fullName, dateOfBirth, accountId, password, address, email, phoneNumber, company;
    private TextView numberOfCard;
    private Button btnUpdate, btnChangeImage;

    private DatabaseReference database;
    private StorageReference storage;

    private Uri uri;

    private Account myAcc;

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        personalView = inflater.inflate(R.layout.fragment_personal, container, false);
        return personalView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PersonalViewModel.class);
        // TODO: Use the ViewModel
        avatar = personalView.findViewById(R.id.fr_personal_avatar);
        fullName = personalView.findViewById(R.id.fr_personal_fullName);
        dateOfBirth = personalView.findViewById(R.id.fr_personal_dateOfBirth);
        accountId = personalView.findViewById(R.id.fr_personal_accountId);
        password = personalView.findViewById(R.id.fr_personal_password);
        address = personalView.findViewById(R.id.fr_personal_address);
        email = personalView.findViewById(R.id.fr_personal_email);
        phoneNumber = personalView.findViewById(R.id.fr_personal_phoneNumber);
        company = personalView.findViewById(R.id.fr_personal_company);

        numberOfCard = personalView.findViewById(R.id.fr_personal_numberOfCard);

        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference();

        btnUpdate = personalView.findViewById(R.id.fr_personal_btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePersonalData();
            }
        });
        btnChangeImage = personalView.findViewById(R.id.fr_personal_btnChangeImage);
        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        loadDataPersonal();

    }

    private void loadDataPersonal() {
        database.child("account").child("MASON").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myAcc = dataSnapshot.getValue(Account.class);
                fullName.setText(myAcc.getFullName());
                dateOfBirth.setText(myAcc.getDateOfBirth());
                accountId.setText(myAcc.getAccountId());
                password.setText(myAcc.getPassword());
                address.setText(myAcc.getAddress());
                email.setText(myAcc.getEmail());
                phoneNumber.setText(myAcc.getPhoneNumber());
                company.setText(myAcc.getCompany());

                String url = myAcc.getAvatar();
                Glide.with(getActivity()).load(url).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "LoadData is cancelled!", Toast.LENGTH_SHORT).show();
            }
        });

        database.child("card").child("MASON").child("cards").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numberOfCard.setText("" +dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //click choose image and Update
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == -1 && data != null && data.getData() != null) {
            uri = data.getData();
            Glide.with(this).load(uri).into(avatar);
            avatar.setImageURI(uri);

            upLoadImage();
        }
    }

    private void upLoadImage() {
        final StorageReference imageReference = storage.child(accountId.getText().toString() + ".jpg");
        UploadTask uploadTask = imageReference.putFile(uri);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return imageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    database.child("account").child("MASON").child("avatar").setValue(downloadUri.toString());
                } else {
                    Toast.makeText(getActivity(), "Có lỗi!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Cập nhật ảnh thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updatePersonalData() {
        Intent intent = new Intent(getContext(), PersonalUpdateActivity.class);
        intent.putExtra("data", myAcc);
        startActivity(intent);
    }
}
