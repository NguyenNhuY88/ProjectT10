package ss.projectt10.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ss.projectt10.R;
import ss.projectt10.adapter.CardSuggestRecyclerAdapter;
import ss.projectt10.model.Category;

import static ss.projectt10.helper.Util.CATEGORY;

public class CardSuggestActivity extends AppCompatActivity {
    private final String TAT_CA = "Tất cả";
    private final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
   private SearchView searchCardSuggest;
    private RecyclerView recyclerViewCard;
    private CardSuggestRecyclerAdapter recyclerAdapterCardSuggest;
    private ArrayList<Category> cardsListSuggest;
    private Button mAddOtherCardButton;
    private Spinner mLocationSpiner;
    private DatabaseReference mDatabase;
    private String locationChoosen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_suggest);
        init();
        initSpiner();

        searchCardSuggest.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapterCardSuggest.getFilter().filter(newText);
                return true;
            }
        });

        mAddOtherCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CardSuggestActivity.this, CreateOtherCardActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
    private void initSpiner() {
        mLocationSpiner = (Spinner) findViewById(R.id.spnCategory);
        TextView tv = (TextView) findViewById(R.id.heading_label);
        List<String> list = new ArrayList<>();
        list.add(TAT_CA);
        list.add("Hà Nội");
        list.add("TP HCM");
        list.add("Đà Nẵng");


        ArrayAdapter<String> adapter = new ArrayAdapter(CardSuggestActivity.this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mLocationSpiner.setAdapter(adapter);
        mLocationSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationChoosen = mLocationSpiner.getSelectedItem().toString();
                QueryCardsSuggest(locationChoosen);
                // QueryCardsSuggest(locationChoosen);
                Toast.makeText(CardSuggestActivity.this, locationChoosen, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void QueryCardsSuggest(String location) {
        Query myCategoryQuery = null;
        if(location.equals(TAT_CA)) {
            myCategoryQuery = mDatabase.child(CATEGORY);
        } else {
            myCategoryQuery = mDatabase.child(CATEGORY).orderByChild("location/" + location)
                    .equalTo(true);
        }
        myCategoryQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardsListSuggest.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Category category = child.getValue(Category.class);
                    cardsListSuggest.add(category);
                }
                recyclerAdapterCardSuggest = new CardSuggestRecyclerAdapter(CardSuggestActivity.this, cardsListSuggest);
                recyclerViewCard.setLayoutManager(new LinearLayoutManager(CardSuggestActivity.this));
                recyclerViewCard.setAdapter(recyclerAdapterCardSuggest);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        searchCardSuggest = (SearchView) findViewById(R.id.search_field);
        mAddOtherCardButton = (Button) findViewById(R.id.btn_add_other_card);
        recyclerViewCard = (RecyclerView) findViewById(R.id.result_list);
        cardsListSuggest = new ArrayList<>();

    }
}
