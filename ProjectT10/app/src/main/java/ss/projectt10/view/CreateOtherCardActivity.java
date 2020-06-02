package ss.projectt10.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.sql.BatchUpdateException;
import java.util.Calendar;
import java.util.Map;

import ss.projectt10.BaseActivity;
import ss.projectt10.R;
import ss.projectt10.model.Card;

import static ss.projectt10.helper.Util.URL_CARD_IMAGE_DEFAULT;

public class CreateOtherCardActivity extends BaseActivity implements IPickResult {
    private ImageView mImageCardDefaultImage, mBarcodeImage;
    private EditText mCardNameField,  mNoteField, mAddCardNumberField;
    private TextView mCardCodeTextView;
    private CheckBox mIsFavoriteCheckBox;
    private Boolean mIsFavoriteCard;
    private Button mSaveButton, mCancelButton, mChangeCardImageButton, mScanButton;
    private String mCardAvatar, mCodeType;
    Uri uriCardImage;

    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_other_card);
        storage = FirebaseStorage.getInstance().getReference();
        initView();
        mChangeCardImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                //Toast.makeText(getApplicationContext(),"ẢNH", Toast.LENGTH_LONG).show();
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
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanImageCode();
            }
        });
    }
    private void scanImageCode() {
        IntentIntegrator intent = new IntentIntegrator(CreateOtherCardActivity.this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setCameraId(0);
        intent.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            mCardCodeTextView.setText(intentResult.getContents());
            mCodeType = intentResult.getFormatName();
            mAddCardNumberField.setText(intentResult.getContents());
            mAddCardNumberField.setEnabled(false);

            Toast.makeText(this, mCodeType, Toast.LENGTH_SHORT).show();
            loadImageBarcode(mCodeType, mCardCodeTextView, mBarcodeImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void initView() {
        mImageCardDefaultImage = (ImageView) findViewById(R.id.iv_default_card_image);
        mCardNameField = (EditText) findViewById(R.id.edt_add_card_name);
        mCardCodeTextView = (TextView) findViewById(R.id.tv_new_card_code_other);
        mNoteField = (EditText) findViewById(R.id.edt_add_card_note);
        mIsFavoriteCheckBox = (CheckBox) findViewById(R.id.cb_favorite_other_card);
        mSaveButton = (Button) findViewById(R.id.btn_save_other_card);
        mCancelButton = (Button) findViewById(R.id.btn_cancel_other_card);
        mChangeCardImageButton = (Button) findViewById(R.id.btn_change_card_image);
        mBarcodeImage = (ImageView) findViewById(R.id.iv_new_barcode_other);
        mCardAvatar = URL_CARD_IMAGE_DEFAULT;
        mScanButton = (Button) findViewById(R.id.btn_scan_barcode_other);
        mAddCardNumberField = (EditText) findViewById(R.id.edt_add_card_number);
    }

    private void submitCard() {
        if (mIsFavoriteCheckBox.isChecked()) {
            mIsFavoriteCard = true;
        } else {
            mIsFavoriteCard = false;
        }
        if (mCardCodeTextView.getText().toString() == null  && mAddCardNumberField.getText() == null) {
            Toast.makeText(this, "Không có mã thẻ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mCardNameField.getText().toString() == null) {
            Toast.makeText(this, "Không có tên thẻ", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String uId = getUid();
        String id = database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).push().getKey();

        Card myCard = new Card(id, mAddCardNumberField.getText().toString(), mCardNameField.getText().toString(), "", "", mCodeType, "default", mCardAvatar, mNoteField.getText().toString(), mIsFavoriteCard);

        Map<String, Object> cardValues = myCard.toMap();


        database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).child(id).setValue(cardValues);
        showProgressBar();
        finish();


    }


    //click choose image and Update
    private void chooseImage() {
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //startActivityForResult(intent, REQUES_CODE_CHANGE_IMAGE);
        PickSetup setup = new PickSetup().setSystemDialog(true).setTitle("Chọn ảnh");

        PickImageDialog.build(setup).show(CreateOtherCardActivity.this);
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

           // Setting the real returned image.
            mImageCardDefaultImage.setImageURI(r.getUri());

            //If you want the Bitmap.
           // getImageView().setImageBitmap(r.getBitmap());


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
                    mCardAvatar = downloadUri.toString();
                } else {
                    Toast.makeText(CreateOtherCardActivity.this, "Có lỗi!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateOtherCardActivity.this, "Cập nhật ảnh thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
