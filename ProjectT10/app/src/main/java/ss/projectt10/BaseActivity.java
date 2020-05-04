package ss.projectt10;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Hashtable;

public class BaseActivity extends AppCompatActivity  {
    public static final String DBPROJECTNAME = "T10DATABASE";
    public static final String DBUSER = "users";
    public static final String DBUSER_CARD = "user-cards";


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

//    public final FirebaseStorage mStorage = FirebaseStorage.getInstance();
//    final StorageReference mStorageRef = mStorage.getReferenceFromUrl("gs://projectt10-cc6d8.appspot.com/PROJECTT10");
    private ProgressBar mProgressBar;

    public void setProgressBar(int resId) {
        mProgressBar = findViewById(resId);
    }

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


//    public String SaveImageAndGetLink (ImageView mImageView, String mImageName) {
//        Bitmap capture = Bitmap.createBitmap(
//                mImageView.getMaxWidth(),
//                mImageView.getHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas captureCanvas = new Canvas(capture);
//        mImageView.draw(captureCanvas);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        capture.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//        byte[] data = outputStream.toByteArray();
//
//        Calendar calendar = Calendar.getInstance();
//        final StorageReference imgRef = mStorageRef.child(getUid()).child(mImageName + calendar.getTimeInMillis() + ".png");
//
//        StorageMetadata metadata = new StorageMetadata.Builder()
//                .setCustomMetadata("caption", mImageName).build();
//        UploadTask uploadTask = imgRef.putBytes(data, metadata);
//        uploadTask.addOnCompleteListener(BaseActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                Log.i("MA", " up load anh thanh cong");
//            }
//        });
//
//        Task<Uri> getDownloadUriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()){
//                    throw task.getException();
//                }
//                return imgRef.getDownloadUrl();
//            }
//        });
//
//        final String[] uri = new String[1];
//        getDownloadUriTask.addOnCompleteListener(BaseActivity.this, new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()){
//                    Uri downloadUri = task.getResult();
//
//                    uri[0] = downloadUri.toString();
//                }
//            }
//        });
//        return uri[0];
//    }
//    public String SaveImageAndGetLink1(ImageView imageView, String imageName) {
//        Calendar calendar = Calendar.getInstance();
//        StorageReference imageRef = mStorageRef.child(getUid()).child(imageName + calendar.getTimeInMillis() + ".png");
//
//        // Get the data from an ImageView as bytes
//        imageView.setDrawingCacheEnabled(true);
//        imageView.buildDrawingCache();
//        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = imageRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//                Toast.makeText(BaseActivity.this, "Lỗi lưu ảnh", Toast.LENGTH_LONG).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//            }
//        });
//
//        return "akdjg";
//    }

    public void loadImageBarcode(String codeType, TextView mBarcodeTextView, ImageView mBarcodeImage) {
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer coWriter;
            coWriter = new MultiFormatWriter();
            BarcodeFormat format = BarcodeFormat.valueOf(codeType);
            BitMatrix byteMatrix = coWriter.encode(mBarcodeTextView.getText().toString(), format, 400, 200, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            mBarcodeImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
    }

}