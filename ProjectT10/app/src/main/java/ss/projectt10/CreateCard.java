//package ss.projectt10;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//
//import java.util.ArrayList;
//
//import ss.projectt10.model.Card;
//import ss.projectt10.model.Category;
//import ss.projectt10.ui.category.CategorySpinnerAdapter;
//
//public class CreateCard extends AppCompatActivity {
//    private Spinner cardCategory;
//
//    private DatabaseReference database;
//    private Button createCard_Scan, createCard_Accept, createCard_Deny;
//    private TextView create_codeId, create_nameCard, create_cardType;
//    private String codeType, imageCard, nameCard;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_card);
//
//        addItemOnSpinner();
//
//        create_codeId = findViewById(R.id.ac_create_card_codeId);
//        create_nameCard = findViewById(R.id.ac_create_card_nameCard);
//        create_cardType = findViewById(R.id.ac_create_card_cardType);
//        createCard_Scan = findViewById(R.id.ac_create_card_btnScanCode);
//        createCard_Accept = findViewById(R.id.ac_create_card_btnAccept);
//        createCard_Deny = findViewById(R.id.ac_create_card_btnDeny);
//
//        createCard_Scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scanImageCode();
//            }
//        });
//
//        createCard_Accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submitCard();
//            }
//        });
//
//        createCard_Deny.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//
//    private void addItemOnSpinner() {
//        cardCategory = findViewById(R.id.ac_create_card_category);
//        database = FirebaseDatabase.getInstance().getReference();
//
//        final ArrayList<Category> categoryList = new ArrayList<>();
//        final CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList);
//        database.child("caterory").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    Category category = child.getValue(Category.class);
//                    categoryList.add(category);
//                }
//                cardCategory.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        cardCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                loadDataCategory(categoryList.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    private void loadDataCategory(Category defaultCategory) {
//        create_nameCard.setText(defaultCategory.getNameCard());
//        create_cardType.setText(defaultCategory.getCardType());
//        imageCard = defaultCategory.getImageIcon();
//    }
//
//    private void scanImageCode() {
//        IntentIntegrator intent = new IntentIntegrator(this);
//        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        intent.setCameraId(0);
//        intent.initiateScan();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (intentResult != null) {
//            create_codeId.setText(intentResult.getContents());
//            codeType = intentResult.getFormatName();
//            Toast.makeText(this, codeType, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void submitCard() {
//        if (codeType == null) {
//            Toast.makeText(this, "Không có mã thẻ", Toast.LENGTH_SHORT).show();
//        } else {
//            String codeId = create_codeId.getText().toString();
//            nameCard = create_nameCard.getText().toString();
//            String category = create_cardType.getText().toString();
//            Card card = new Card(codeId, nameCard, category, codeType, imageCard);
//
//            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//            database.child("card").child("MASON").child("cards").child(nameCard).setValue(card);
//            finish();
//        }
//    }
//}
