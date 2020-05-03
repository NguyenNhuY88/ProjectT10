package ss.projectt10.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ss.projectt10.DetailCardActivity;
import ss.projectt10.R;
import ss.projectt10.model.Card;

public class CardRecyclerAdapter  extends RecyclerView.Adapter<CardRecyclerAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<Card> cardsList;

    private ArrayList<Card> cardsListAll;

    public CardRecyclerAdapter(Context context, ArrayList<Card> cardsList) {
        this.mContext = context;
        this.cardsList = cardsList;
        cardsListAll = new ArrayList<>();
        cardsListAll.addAll(cardsList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Card> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(cardsListAll);
                } else {
                    for (Card card : cardsListAll) {
                        if (card.getCardName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
                cardsList.addAll((Collection<? extends Card>) results.values);
                notifyDataSetChanged();
            }
        };

    }

    @NonNull
    @Override
    public CardRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardRecyclerAdapter.MyViewHolder holder, final int position) {
        Glide.with(mContext).load(cardsList.get(position).getcardAvatar()).into(holder.cardImage);
        holder.cardName.setText(cardsList.get(position).getCardName());
        holder.category.setText(cardsList.get(position).getCategory());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailCardActivity.class);
                Card detailCard = cardsList.get(position);
                intent.putExtra("data", detailCard);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardName, category;
        ViewGroup parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.tv_card_name);
            cardImage = itemView.findViewById(R.id.iv_card);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            category = itemView.findViewById(R.id.tv_category);
        }

    }
}
