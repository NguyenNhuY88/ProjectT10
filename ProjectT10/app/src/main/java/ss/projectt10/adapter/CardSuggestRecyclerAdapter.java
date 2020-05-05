

package ss.projectt10.adapter;


        import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;

        import java.util.ArrayList;
        import java.util.Collection;
        import java.util.List;

        import ss.projectt10.DetailCardActivity;
        import ss.projectt10.R;
        import ss.projectt10.model.Card;
        import ss.projectt10.model.Category;
        import ss.projectt10.view.CreateCardActivity;

        import static ss.projectt10.helper.Util.DATA_CATEGORY;

public class CardSuggestRecyclerAdapter  extends RecyclerView.Adapter<CardSuggestRecyclerAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<Category> cardsList;

    private ArrayList<Category> cardsListAll;

    public CardSuggestRecyclerAdapter(Context context, ArrayList<Category> cardsList) {
        this.mContext = context;
        this.cardsList = cardsList;
        cardsListAll = new ArrayList<>();
        cardsListAll.addAll(cardsList);
    }



    @NonNull
    @Override
    public CardSuggestRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_suggest_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardSuggestRecyclerAdapter.MyViewHolder holder, final int position) {
      Glide.with(mContext).load(cardsList.get(position).getImageIcon()).into(holder.cardImage);
        holder.cardName.setText(cardsList.get(position).getNameCard());
        holder.category.setText(cardsList.get(position).getCardType());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(mContext,"Implement this method", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, CreateCardActivity.class);
                Category category = cardsList.get(position);
                intent.putExtra(DATA_CATEGORY, category);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardName, category;
        ViewGroup parentLayout;
        //   public View viewBackground, viewForeground;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.tv_card_name_suggest);
            cardImage = itemView.findViewById(R.id.iv_card_suggest);
            parentLayout = itemView.findViewById(R.id.parent_layout_suggest_card);
            category = itemView.findViewById(R.id.tv_category_suggest);

//            viewBackground = itemView.findViewById(R.id.view_background);
//            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Category> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(cardsListAll);
                } else {
                    for (Category card : cardsListAll) {
                        if (card.getNameCard().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredList.add(card);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cardsList.clear();
                cardsList.addAll((Collection<? extends Category>) results.values);
                notifyDataSetChanged();
            }
        };

    }

}
