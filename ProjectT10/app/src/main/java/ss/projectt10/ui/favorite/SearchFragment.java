package ss.projectt10.ui.favorite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ss.projectt10.R;
import ss.projectt10.adapter.CardRecyclerAdapter;
import ss.projectt10.model.Card;

import static ss.projectt10.BaseActivity.DBPROJECTNAME;
import static ss.projectt10.BaseActivity.DBUSER_CARD;
import static ss.projectt10.BaseActivity.databaseInstance;

public class SearchFragment extends Fragment {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final String TAT_CA = "Tất cả";
    private final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private SearchView searchCardSuggest;
    private RecyclerView recyclerViewCard;
    private CardRecyclerAdapter recyclerAdapterCard;
    private ArrayList<Card> cardsList;

    private View cardsSuggestView;
    private DatabaseReference mDatabase;
    private SearchView getSearchCardSuggest;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        cardsSuggestView = inflater.inflate(R.layout.fragment_favorite, container, false);
        return cardsSuggestView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        QueryCardsSuggest();

        getSearchCardSuggest.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapterCard.getFilter().filter(newText);
                return true;
            }
        });

    }

    private void QueryCardsSuggest() {
        Query myCategoryQuery = null;
        myCategoryQuery =  mDatabase.child(DBPROJECTNAME).child(DBUSER_CARD).child(user.getUid())
                .orderByChild("isFavorite").equalTo(true);

        myCategoryQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardsList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Card card = child.getValue(Card.class);
                    cardsList.add(card);
                }
                recyclerAdapterCard = new CardRecyclerAdapter(getContext(), cardsList);
                recyclerViewCard.setLayoutManager(new GridLayoutManager(getContext(),2));
                recyclerViewCard.setAdapter(recyclerAdapterCard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void init() {


        mDatabase = databaseInstance.getReference();
        mDatabase.keepSynced(true);
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.keepSynced(true);
        searchCardSuggest = cardsSuggestView.findViewById(R.id.search_field);

        recyclerViewCard = cardsSuggestView.findViewById(R.id.rc_favorite_list);
        getSearchCardSuggest = cardsSuggestView.findViewById(R.id.sv_favorite_search_field);
        cardsList = new ArrayList<>();

    }

}
