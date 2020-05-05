package ss.projectt10.view;

import androidx.appcompat.app.AppCompatActivity;

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
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.sql.BatchUpdateException;
import java.util.Map;

import ss.projectt10.BaseActivity;
import ss.projectt10.R;
import ss.projectt10.model.Card;

import static ss.projectt10.helper.Util.URL_CARD_IMAGE_DEFAULT;

public class CreateOtherCardActivity extends BaseActivity {
    private ImageView mImageCardDefaultImage;
    private EditText mCardNameField, mCardNumberField, mNoteField;
    private CheckBox mIsFavoriteCheckBox;
    private Boolean mIsFavoriteCard;
    private Button mSaveButton, mCancelButton, mChangeImageCardButton;
    private String mCardAvatar;
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
    }

    private void initView() {
        mImageCardDefaultImage = (ImageView) findViewById(R.id.iv_default_card_image);
        mCardNameField = (EditText) findViewById(R.id.edt_add_card_name);
        mCardNumberField = (EditText) findViewById(R.id.edt_add_card_number);
        mNoteField = (EditText) findViewById(R.id.edt_add_card_note);
        mIsFavoriteCheckBox = (CheckBox) findViewById(R.id.cb_favorite_other_card);
        mSaveButton = (Button) findViewById(R.id.btn_save_other_card);
        mCancelButton = (Button) findViewById(R.id.btn_cancel_other_card);
        mChangeImageCardButton = (Button) findViewById(R.id.btn_add_front_side_card);

        mCardAvatar = URL_CARD_IMAGE_DEFAULT;
    }

    private void submitCard() {
        if (mIsFavoriteCheckBox.isChecked()) {
            mIsFavoriteCard = true;
        } else {
            mIsFavoriteCard = false;
        }
        if (mCardNumberField.getText().toString() == null) {
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

            Card myCard = new Card(id, mCardNumberField.getText().toString(), mCardNameField.getText().toString(), "", "", "EAN_13", "default", mCardAvatar, "", mIsFavoriteCard);

            Map<String, Object> cardValues = myCard.toMap();


            database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).child(id).setValue(cardValues);
            showProgressBar();
            finish();


    }

}
