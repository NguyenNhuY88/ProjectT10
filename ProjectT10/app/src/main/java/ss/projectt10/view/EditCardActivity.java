package ss.projectt10.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import ss.projectt10.BaseActivity;
import ss.projectt10.R;
import ss.projectt10.model.Card;

import static ss.projectt10.helper.Util.DATA_CARD_UPDATE;

public class EditCardActivity extends BaseActivity {
    private ImageView mImageCardImage;
    private EditText mCardNameField,  mNoteField;
    private CheckBox mIsFavoriteCheckBox;
    private Boolean mIsFavoriteCard;
    private Button mSaveButton, mCancelButton, mChangeImageCardButton;

    private Card card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        initView();
        mIsFavoriteCheckBox.setChecked(true);
        getIntenData();
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

        Map<String, Object> cardValues = card.toMap();


        database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).child(card.getID()).setValue(cardValues);
        showProgressBar();
        finish();


    }
}
