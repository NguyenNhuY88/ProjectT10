package ss.projectt10.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Map;

import ss.projectt10.BaseActivity;
import ss.projectt10.R;
import ss.projectt10.model.Category;
import ss.projectt10.model.Card;
import ss.projectt10.ui.category.CategorySpinnerAdapter;

public class CreateCardActivity extends BaseActivity {
    private Spinner mSuggestCardSpiner;

    private DatabaseReference mDatabase;
    private Button mScanButton, mAcceptButton, mCancelButton;
    private TextView mCardCodeTextView, mCardNameTextView, mCardTypeTexView;
    private CheckBox mIsFavoriteCheckBox;
    private ImageView mBarcodeImage;
    private String mCodeType, mCardAvatar, mCardName, mCardCode, mCategory;
    private Boolean mIsFavoriteCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        addItemOnSpinner();
        initView();


        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanImageCode();
            }
        });

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
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

//        mIsFavoriteCheckBox.setChecked(false);
//        mIsFavoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                mIsFavoriteCard = isChecked;
//            }
//        });

    }
    private void initView(){
        mCardCodeTextView = findViewById(R.id.tv_new_card_code);
        mCardNameTextView = findViewById(R.id.tv_new_card_name);
        mCardTypeTexView = findViewById(R.id.tv_new_card_category);
        mScanButton = findViewById(R.id.btn_scan_barcode);
        mAcceptButton = findViewById(R.id.btn_accept);
        mCancelButton = findViewById(R.id.btn_cancel);
        mIsFavoriteCheckBox = findViewById(R.id.cb_favorite);
        mBarcodeImage = findViewById(R.id.iv_new_barcode);
        setProgressBar(R.id.pb_add_card);
    }
    private void addItemOnSpinner() {
        mSuggestCardSpiner = findViewById(R.id.spinner_suggest_card);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final ArrayList<Category> categoryList = new ArrayList<>();
        final CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList);
        mDatabase.child("caterory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Category category = child.getValue(Category.class);

                    categoryList.add(category);
                }
                mSuggestCardSpiner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mSuggestCardSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadDataCategory(categoryList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadDataCategory(Category defaultCategory) {
        mCardNameTextView.setText(defaultCategory.getNameCard());
        mCardTypeTexView.setText(defaultCategory.getCardType());
        mCardAvatar = defaultCategory.getImageIcon();
    }

    private void scanImageCode() {
        IntentIntegrator intent = new IntentIntegrator(this);
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

    private void submitCard() {
        if (mIsFavoriteCheckBox.isChecked()) {
            mIsFavoriteCard = true;
        } else {
            mIsFavoriteCard = false;
        }
        mCardCode = mCardCodeTextView.getText().toString();
        mCardName = mCardNameTextView.getText().toString();
        mCategory = mCardTypeTexView.getText().toString();
        if (mCodeType == null) {
            Toast.makeText(this, "Không có mã thẻ", Toast.LENGTH_SHORT).show();
        } else if (mCardCode != null ) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            String uId = getUid();
            String id = database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).push().getKey();

            Card myCard = new Card(id, mCardCode, mCardName, "", "", mCodeType, mCategory, mCardAvatar, "", mIsFavoriteCard);

            Map<String, Object> cardValues = myCard.toMap();


            database.child(DBPROJECTNAME).child(DBUSER_CARD).child(uId).child(id).setValue(cardValues);
            showProgressBar();
            finish();
        } else {
            Toast.makeText(this, "Lỗi không đủ dữ liệu", Toast.LENGTH_SHORT).show();
           hideProgressBar();
        }
    }
}
