package ss.projectt10.ui.cards;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import ss.projectt10.MainActivity;
import ss.projectt10.R;
import ss.projectt10.adapter.CardRecyclerAdapter;
import ss.projectt10.helper.RecyclerItemTouchHelper;
import ss.projectt10.model.Card;
import ss.projectt10.view.CardSuggestActivity;


import static ss.projectt10.BaseActivity.DBPROJECTNAME;
import static ss.projectt10.BaseActivity.DBUSER_CARD;

public class CardsFragment extends Fragment  implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    static final String TAG = MainActivity.class.getSimpleName();
    private View cardsView;

    private DatabaseReference database;
    private Button btnAddNewCard;
    private SearchView searchCard;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private RecyclerView recyclerViewCard;
    private CardRecyclerAdapter recyclerAdapterCard;
    private ArrayList<Card> cardsList;
    private FrameLayout frameLayout;

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

        btnAddNewCard = cardsView.findViewById(R.id.btn_add_card);
        btnAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getContext(), CardSuggestActivity.class);
             //   Intent intent = new Intent(getContext(), CreateCardActivity.class);
                startActivity(intent);
            }
        });

        searchCard = cardsView.findViewById(R.id.sv_seach_card);
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

        frameLayout = cardsView.findViewById(R.id.frame_layout);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewCard);
    }
    private void loadListCards() {
        recyclerViewCard = cardsView.findViewById(R.id.rcv_card);
        cardsList = new ArrayList<>();

        database.child(DBPROJECTNAME).child(DBUSER_CARD).child(user.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardsList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Card card = child.getValue(Card.class);
                    cardsList.add(card);
                    if (card.getIsFavorite()==true) {
                        Log.i("CBOX", "true");
                    } else  {
                        Log.i("CBOX", "false");
                    }

                }

                recyclerAdapterCard = new CardRecyclerAdapter(getContext(), cardsList);
                recyclerViewCard.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewCard.setAdapter(recyclerAdapterCard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CardRecyclerAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = cardsList.get(viewHolder.getAdapterPosition()).getCardName();

            // backup of removed item for undo purpose
            final Card deletedItem = cardsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            deleteCard(deletedItem);
            // remove the item from recycler view
           recyclerAdapterCard.removeCardItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(frameLayout, name + " removed from  list card!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddCardAgain(deletedItem);
                    // undo is selected, restore the deleted item
                   recyclerAdapterCard.restoreCardItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
    public void AddCardAgain(Card myCard){

        Map<String, Object> cardValues = myCard.toMap();
        database.child(DBPROJECTNAME).child(DBUSER_CARD).child(user.getUid()).child(myCard.getID()).setValue(cardValues);
    }
    public void deleteCard(Card myCard){
        database.child(DBPROJECTNAME).child(DBUSER_CARD).child(user.getUid()).child(myCard.getID()).removeValue();


    }
}

