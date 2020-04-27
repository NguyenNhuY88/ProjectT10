package ss.projectt10.ui.cards;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ss.projectt10.MainActivity;
import ss.projectt10.R;
import ss.projectt10.adapter.CardRecyclerAdapter;
import ss.projectt10.model.Card;
import ss.projectt10.model.MyCard;
import ss.projectt10.view.NewCardActivity;

import static ss.projectt10.BaseActivity.DBPROJECTNAME;
import static ss.projectt10.BaseActivity.DBUSER_CARD;

public class CardsFragment extends Fragment {
    static final String TAG = MainActivity.class.getSimpleName();
    private View cardsView;

    private DatabaseReference database;
    private Button btnAddNewCard;
    private SearchView searchCard;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private RecyclerView recyclerViewCard;
    private CardRecyclerAdapter recyclerAdapterCard;
    private ArrayList<MyCard> cardsList2;
    public static CardsFragment newInstance() {
        return new CardsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        cardsView = inflater.inflate(R.layout.fragment_cards, container, false);
        return cardsView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        database = FirebaseDatabase.getInstance().getReference();


        loadListCards();

        btnAddNewCard = cardsView.findViewById(R.id.fr_cards_addNewCard);
        btnAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     Intent intent = new Intent(getContext(), CreateCard.class);
                Intent intent = new Intent(getContext(), NewCardActivity.class);
                startActivity(intent);
            }
        });

        searchCard = cardsView.findViewById(R.id.fr_cards_search);
        searchCard.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    private void loadListCards() {
        recyclerViewCard = cardsView.findViewById(R.id.rcv_card);
        cardsList2 = new ArrayList<>();

        database.child(DBPROJECTNAME).child(DBUSER_CARD).child(user.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardsList2.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    MyCard card = child.getValue(MyCard.class);
                    cardsList2.add(card);
                    Log.i("CARD", card.getCardName());
                }

                recyclerAdapterCard = new CardRecyclerAdapter(getContext(), cardsList2);
                recyclerViewCard.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewCard.setAdapter(recyclerAdapterCard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
