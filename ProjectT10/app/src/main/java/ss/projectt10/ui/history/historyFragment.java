package ss.projectt10.ui.history;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;
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
import ss.projectt10.adapter.HistoryAdapter;
import ss.projectt10.helper.RecyclerItemTouchHelper;
import ss.projectt10.model.Card;

import static ss.projectt10.BaseActivity.DBHISTORY;
import static ss.projectt10.BaseActivity.DBPROJECTNAME;
import static ss.projectt10.BaseActivity.DBUSER_CARD;
import static ss.projectt10.BaseActivity.databaseInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link historyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class historyFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener  {
    private View historyView;
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<Card> arrayListCard;

    private CoordinatorLayout historyFragmentLayout;

    private DatabaseReference mDatabaseRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public historyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment history.
     */
    // TODO: Rename and change types and number of parameters
    public static historyFragment newInstance(String param1, String param2) {
        historyFragment fragment = new historyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        historyView = inflater.inflate(R.layout.fragment_history, container, false);
        return historyView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDatabaseRef = databaseInstance.getReference();
        mDatabaseRef.keepSynced(true);
        loadHistory();
        historyFragmentLayout = historyView.findViewById(R.id.frame_layout_history_fragment);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void loadHistory() {
        recyclerView = historyView.findViewById(R.id.rcv_history);
        arrayListCard = new ArrayList<>();
        Query mHistoryQuery = null;
        mHistoryQuery =  mDatabaseRef.child(DBPROJECTNAME).child(DBHISTORY).child(user.getUid())
                .orderByChild("useTime");

        mHistoryQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCard.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Card card = child.getValue(Card.class);
                    arrayListCard.add(card);

                }

                historyAdapter = new HistoryAdapter(getContext(), arrayListCard);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(historyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {


        if (viewHolder instanceof HistoryAdapter.HistoryViewHolder) {

            String name = arrayListCard.get( historyAdapter.getItemCount()-1- viewHolder.getAdapterPosition()).getCardName();

            final Card deleteItem = arrayListCard.get(historyAdapter.getItemCount()-1- viewHolder.getAdapterPosition());
            deleteCardHistory(deleteItem);
            // remove the item from recycler view
            historyAdapter.removeItemHistory(historyAdapter.getItemCount()-1- viewHolder.getAdapterPosition());

            // showing snack bar
            Snackbar snackbar = Snackbar
                    .make(historyFragmentLayout, name + "đã xóa!", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
    public void deleteCardHistory(Card myCard){
        String str = Long.toString(myCard.getUseTime());
        Log.i("DEL_HISTORY", myCard.getUseTimeID());
        mDatabaseRef.child(DBPROJECTNAME).child(DBHISTORY).child(user.getUid()).child(myCard.getUseTimeID()).removeValue();
    }
}
