package ss.projectt10.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.Calendar;
import java.util.Map;

import ss.projectt10.BaseActivity;
import ss.projectt10.R;
import ss.projectt10.model.Card;

import static ss.projectt10.helper.Util.DATA_CARD_UPDATE;

public class EditCardActivity extends BaseActivity implements IPickResult {
    private ImageView mImageCardImage;
    private EditText mCardNameField,  mNoteField;
    private CheckBox mIsFavoriteCheckBox;
    private Boolean mIsFavoriteCard;
    private Button mSaveButton, mCancelButton, mChangeImageCardButton;
    String linkCardAvatar;

    private Card card;
    Uri uriCardImage;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        storage = FirebaseStorage.getInstance().getReference();
        initView();
        mIsFavoriteCheckBox.setChecked(true);
        getIntenData();
        mChangeImageCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickSetup setup = new PickSetup().setSystemDialog(true).setTitle("Chọn ảnh");

                PickImageDialog.build(setup).show(EditCardActivity.this);
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCard();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initView() {
        mImageCardImage = (ImageView) findViewById(R.id.iv_card_image_edit);
        mCardNameField = (EditText) findViewById(R.id.edt_add_card_name_edit);
        mNoteField = (EditText) findViewById(R.id.edt_add_card_note_edit);
        mIsFavoriteCheckBox = (CheckBox) findViewById(R.id.cb_favorite_other_card_edit);
        mSaveButton = (Button) findViewById(R.id.btn_save_other_card_edit);
        mCancelButton = (Button) findViewById(R.id.btn_cancel_other_card_edit);
        mChangeImageCardButton = (Button) findViewById(R.id.btn_add_front_side_card_edit);
    }

    private void getIntenData() {
        Intent intent = getIntent();
        card = (Card) intent.getSerializableExtra(DATA_CARD_UPDATE);
        Glide.with(EditCardActivity.this).load(card.getCardAvatar()).into(mImageCardImage);
        mCardNameField.setText(card.getCardName());
        mNoteField.setText(card.getNote());
        mIsFavoriteCheckBox.setChecked(card.getIsFavorite());
        linkCardAvatar = card.getCardAvatar();


    }
    private void submitCard() {
        if (mIsFavoriteCheckBox.isChecked()) {
            mIsFavoriteCard = true;
        } else {
            mIsFavoriteCard = false;
        }
        if(mCardNameField.getText().toString() == null) {
            Toast.makeText(this, "Không có tên thẻ", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String uId = getUid();
        card.setCardName(mCardNameField.getText().toString());
        card.setNote(mNoteField.getText().toString());
        card.setIsFavorite(mIsFavoriteCard.booleanValue());
        card.setCardAvatar(linkCardAvatar);
        Map<String, Object> cardValues = card.toMap();


        database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).child(card.getID()).setValue(cardValues);
        showProgressBar();
        finish();


    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            mImageCardImage.setImageURI(r.getUri());
            uriCardImage = r.getUri();
            upLoadImage();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void upLoadImage() {
        StorageReference storageRef = storage.child("PROJECTT10").child(getUid());
        Calendar calendar = Calendar.getInstance();
        final StorageReference cardImageRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");
        UploadTask uploadTask = cardImageRef.putFile(uriCardImage);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return cardImageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    linkCardAvatar = downloadUri.toString();
                } else {
                    Toast.makeText(EditCardActivity.this, "Có lỗi!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditCardActivity.this, "Cập nhật ảnh thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
