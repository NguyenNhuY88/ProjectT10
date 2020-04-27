package ss.projectt10.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.sql.BatchUpdateException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import ss.projectt10.BaseActivity;
import ss.projectt10.R;
import ss.projectt10.model.Card;
import ss.projectt10.model.MyCard;

public class NewCardActivity extends BaseActivity {
    final int REQUEST_CODE_FRONT_IMAGE = 1;
    final int REQUEST_CODE_BACK_IMAGE = 2;
    ImageView mBarcodeImage, mCardFrontImage, mCardBackImage;
    TextView mBarcodeTextView;
    EditText mCardNameTextField;
    Button mScanButton, mAddFrontImageButton, mAddBackImageButton, mAcceptButton, mCancelButton;
    String codeType, cardCode, cardName, imageUri;


    private  FirebaseStorage mStorage;
    private  StorageReference mStorageRef;
 //   private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
     //   mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReferenceFromUrl("gs://projectt10-cc6d8.appspot.com/PROJECTT10");
        initView();
        
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();
            }
        });
        mAddFrontImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_FRONT_IMAGE);
            }
        });
        mAddBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_BACK_IMAGE);
            }
        });
        
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                submitCard();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setProgressBar(R.id.pb_add_card);
    }

    private void submitCard() {
        String cardCode = mBarcodeTextView.getText().toString();
        String cardName = mCardNameTextField.getText().toString();
        if (cardName == null)
        {
            mCardNameTextField.setError("Hãy nhập tên thẻ");
        } else if (codeType != null &&  cardCode != null ) {
            String cardFrontImage = "";
            String cardBackImage = "";
            SaveImageAndGetLink (mCardFrontImage, "front_image");
            cardFrontImage = imageUri;
            SaveImageAndGetLink (mCardBackImage, "front_image");
            cardBackImage = imageUri;
        //    cardBackImage = SaveImageAndGetLink (mCardBackImage, "back_image");
            Log.i("IMAGE", cardBackImage + " \n " + cardFrontImage);
            Toast.makeText(getApplicationContext(),cardFrontImage, Toast.LENGTH_LONG);
            Toast.makeText(getApplicationContext(),cardBackImage, Toast.LENGTH_LONG);
            MyCard card = new MyCard(cardCode, cardName, cardFrontImage, cardBackImage, codeType, false);
            Map<String, Object> cardValues = card.toMap();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            String uId = getUid();
            database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).push().setValue(cardValues);
         //   hideProgressBar();
            finish();
        } else {
            Toast.makeText(this, "Lỗi không đủ dữ liệu", Toast.LENGTH_SHORT).show();
            hideProgressBar();

        }
    }

    private void scanBarcode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
    }

    private void initView() {
        mBarcodeImage = (ImageView) findViewById(R.id.iv_barcode);
        mCardFrontImage = (ImageView) findViewById(R.id.iv_card_front_image);
        mCardBackImage = (ImageView) findViewById(R.id.iv_card_back_image);
        mBarcodeTextView = (TextView) findViewById(R.id.tv_barcode);
        mScanButton = (Button) findViewById(R.id.btn_scan_barcode);
        mAddBackImageButton = (Button) findViewById(R.id.btn_add_back_image);
        mAddFrontImageButton = (Button) findViewById(R.id.btn_add_front_image);
        mAcceptButton = (Button) findViewById(R.id.btn_accept);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);
        mCardNameTextField = (EditText) findViewById(R.id.edt_card_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if (resultCode == RESULT_OK && data != null) {
           if (requestCode == REQUEST_CODE_FRONT_IMAGE ){
               Bitmap bitmap = (Bitmap) data.getExtras().get("data");
               mCardFrontImage.setImageBitmap(bitmap);
           } else if (requestCode == REQUEST_CODE_BACK_IMAGE ){
               Bitmap bitmap = (Bitmap) data.getExtras().get("data");
               mCardBackImage.setImageBitmap(bitmap);
           }

       }
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            mBarcodeTextView.setText(intentResult.getContents());
            codeType = intentResult.getFormatName();
            Toast.makeText(this, codeType, Toast.LENGTH_SHORT).show();
            loadImageBarcode(codeType, mBarcodeTextView, mBarcodeImage);
        }
    }

//    private void upLoadImage() {
//             Calendar calendar = Calendar.getInstance();
//       final StorageReference imgRef = mStorageRef.child(getUid()).child(mImageName + calendar.getTimeInMillis() + ".png");
//        final StorageReference imageReference = storage.child(accountId.getText().toString() + ".jpg");
//        UploadTask uploadTask = imageReference.putFile(uri);
//        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                return imageReference.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    database.child("account").child("MASON").child("avatar").setValue(downloadUri.toString());
//                } else {
//                    Toast.makeText(getActivity(), "Có lỗi!!!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(), "Cập nhật ảnh thất bại!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void SaveImageAndGetLink (ImageView mImageView, String mImageName) {
        Calendar calendar = Calendar.getInstance();
        final StorageReference imgRef = mStorageRef.child(getUid()).child(mImageName + calendar.getTimeInMillis() + ".png");
        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//                Log.i("IMAGE", " up load anh thanh cong");
//
//            }
//        });
//            Log.i("IMAGE", "Link?= "+  imgRef.getDownloadUrl().toString());
        Task<Uri> getDownloadUriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return imgRef.getDownloadUrl();
            }
        }).addOnCompleteListener(NewCardActivity.this, new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    Log.i("IMAGE","lkdgjal" + downloadUri.toString());
                    imageUri  = downloadUri.toString();
                }
                else {
                    Log.i("IMAGE","false uri" );
                }
            }
        });
    }




}
