package ss.projectt10.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.sql.BatchUpdateException;
import java.util.Map;

import ss.projectt10.BaseActivity;
import ss.projectt10.R;
import ss.projectt10.model.Card;

import static ss.projectt10.helper.Util.URL_CARD_IMAGE_DEFAULT;

public class CreateOtherCardActivity extends BaseActivity {
    private ImageView mImageCardDefaultImage, mBarcodeImage;
    private EditText mCardNameField,  mNoteField;
    private TextView mCardCodeTextView;
    private CheckBox mIsFavoriteCheckBox;
    private Boolean mIsFavoriteCard;
    private Button mSaveButton, mCancelButton, mChangeImageCardButton, mScanButton;
    private String mCardAvatar, mCodeType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_other_card);
        initView();
        mChangeImageCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            mCardCodeTextView.setText(intentResult.getContents());
            mCodeType = intentResult.getFormatName();
            Toast.makeText(this, mCodeType, Toast.LENGTH_SHORT).show();
            loadImageBarcode(mCodeType, mCardCodeTextView, mBarcodeImage);
        }
    }
    private void initView() {
        mImageCardDefaultImage = (ImageView) findViewById(R.id.iv_default_card_image);
        mCardNameField = (EditText) findViewById(R.id.edt_add_card_name);
        mCardCodeTextView = (TextView) findViewById(R.id.tv_new_card_code_other);
        mNoteField = (EditText) findViewById(R.id.edt_add_card_note);
        mIsFavoriteCheckBox = (CheckBox) findViewById(R.id.cb_favorite_other_card);
        mSaveButton = (Button) findViewById(R.id.btn_save_other_card);
        mCancelButton = (Button) findViewById(R.id.btn_cancel_other_card);
        mChangeImageCardButton = (Button) findViewById(R.id.btn_add_front_side_card);
        mBarcodeImage = (ImageView) findViewById(R.id.iv_new_barcode_other);
        mCardAvatar = URL_CARD_IMAGE_DEFAULT;
        mScanButton = (Button) findViewById(R.id.btn_scan_barcode_other);
    }

    private void submitCard() {
        if (mIsFavoriteCheckBox.isChecked()) {
            mIsFavoriteCard = true;
        } else {
            mIsFavoriteCard = false;
        }
        if (mCardCodeTextView.getText().toString() == null) {
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

        Card myCard = new Card(id, mCardCodeTextView.getText().toString(), mCardNameField.getText().toString(), "", "", mCodeType, "default", mCardAvatar, mNoteField.getText().toString(), mIsFavoriteCard);

        Map<String, Object> cardValues = myCard.toMap();


        database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).child(id).setValue(cardValues);
        showProgressBar();
        finish();


    }

}
