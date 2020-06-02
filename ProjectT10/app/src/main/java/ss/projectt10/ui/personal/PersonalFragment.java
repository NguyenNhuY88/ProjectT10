package ss.projectt10.ui.personal;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import ss.projectt10.PersonalUpdateActivity;
import ss.projectt10.R;
import ss.projectt10.model.Account;
import ss.projectt10.model.User;

import static android.app.Activity.RESULT_OK;
import static ss.projectt10.BaseActivity.DBPROJECTNAME;
import static ss.projectt10.BaseActivity.DBUSER;
import static ss.projectt10.BaseActivity.DBUSER_CARD;

public class PersonalFragment extends Fragment {
    private final int REQUES_CHANGE_AVATAR = 13579;
    private final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private PersonalViewModel mViewModel;
    private View personalView;

    private ImageView avatar;
    private TextView fullName, dateOfBirth, accountId, password, address, email, phoneNumber, company;
    private TextView numberOfCard;
    private Button btnUpdate, btnChangeImage;

    private DatabaseReference database;
    private StorageReference storage;

    private Uri uri;

    private User myAcc;

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

        address = personalView.findViewById(R.id.fr_personal_address);
        email = personalView.findViewById(R.id.fr_personal_email);
        phoneNumber = personalView.findViewById(R.id.fr_personal_phoneNumber);


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
//        btnChangeImage = personalView.findViewById(R.id.fr_personal_btnChangeImage);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        loadDataPersonal();

    }

    private void loadDataPersonal() {
        database.child(DBPROJECTNAME).child(DBUSER).child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myAcc = dataSnapshot.getValue(User.class);

                    fullName.setText(myAcc.getUsername());
                    dateOfBirth.setText(myAcc.getDateOfBirth());
                    address.setText(myAcc.getAddress());
                    email.setText(myAcc.getEmail());
                    phoneNumber.setText(myAcc.getPhoneNumber());

                String url = myAcc.getAvatar();
                if (getActivity() == null) {
                    return;
                }
                Glide.with(getActivity()).load(url).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "LoadData is cancelled!", Toast.LENGTH_SHORT).show();
            }
        });

        database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).addValueEventListener(new ValueEventListener() {
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
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, REQUES_CHANGE_AVATAR);

        PickSetup setup = new PickSetup().setSystemDialog(true).setTitle("Chọn ảnh");

        PickImageDialog.build(setup)
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        uri =  r.getUri();
                        avatar.setImageBitmap(r.getBitmap());
                        upLoadImage();
                    }
                })
                .setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {
                        return;
                    }
                }).show(getActivity());
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUES_CHANGE_AVATAR && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            uri = data.getData();
//            Glide.with(this).load(uri).into(avatar);
//            avatar.setImageURI(uri);
//
//            upLoadImage();
//        }
//    }

    private void upLoadImage() {
        final StorageReference imageReference = storage.child("PROJECTT10").child(uId).child("avatar.jpg");
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
                    Log.i("LINKANH", downloadUri.toString());
                    database.child(DBPROJECTNAME).child(DBUSER).child(uId).child("avatar").setValue(downloadUri.toString());
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
