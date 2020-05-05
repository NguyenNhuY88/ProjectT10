package ss.projectt10.ui.search_location;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ss.projectt10.R;
import ss.projectt10.adapter.CardRecyclerAdapter;
import ss.projectt10.adapter.CardSuggestRecyclerAdapter;
import ss.projectt10.model.Card;
import ss.projectt10.model.Category;

public class SearchFragment extends Fragment {
    private final String TAT_CA = "Tất cả";
    private final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private SearchView searchCardSuggest;
    private RecyclerView recyclerViewCard;
    private CardSuggestRecyclerAdapter recyclerAdapterCardSuggest;
    private ArrayList<Category> cardsListSuggest;
    private Button mAddOtherCardButton;
    private FrameLayout frameLayout;
    private View cardsSuggestView;
    private Spinner mLocationSpiner;
    private DatabaseReference mDatabase;
    private String locationChoosen;
    private SearchView getSearchCardSuggest;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        cardsSuggestView = inflater.inflate(R.layout.fragment_search, container, false);
        return cardsSuggestView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initSpiner();

        getSearchCardSuggest.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    }

    private void initSpiner() {
        mLocationSpiner = (Spinner) cardsSuggestView.findViewById(R.id.spnCategory);
        TextView tv = (TextView) cardsSuggestView.findViewById(R.id.heading_label);
        List<String> list = new ArrayList<>();
        list.add(TAT_CA);
        list.add("Hà Nội");
        list.add("TP HCM");
        list.add("Đà Nẵng");


        ArrayAdapter<String> adapter = new ArrayAdapter(cardsSuggestView.getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mLocationSpiner.setAdapter(adapter);
        mLocationSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationChoosen = mLocationSpiner.getSelectedItem().toString();
                QueryCardsSuggest(locationChoosen);
               // QueryCardsSuggest(locationChoosen);
                Toast.makeText(cardsSuggestView.getContext(), locationChoosen, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void QueryCardsSuggest(String location) {
        Query myCategoryQuery = null;
        if(location.equals(TAT_CA)) {
            myCategoryQuery = mDatabase.child("caterory");
        } else {
            myCategoryQuery = mDatabase.child("caterory").orderByChild("location/" + location)
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
                recyclerAdapterCardSuggest = new CardSuggestRecyclerAdapter(getContext(), cardsListSuggest);
                recyclerViewCard.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewCard.setAdapter(recyclerAdapterCardSuggest);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        searchCardSuggest = cardsSuggestView.findViewById(R.id.search_field);
        mAddOtherCardButton = (Button) cardsSuggestView.findViewById(R.id.btn_add_other_card);

        recyclerViewCard = cardsSuggestView.findViewById(R.id.result_list);
        getSearchCardSuggest = cardsSuggestView.findViewById(R.id.search_field);
        cardsListSuggest = new ArrayList<>();

    }

}
