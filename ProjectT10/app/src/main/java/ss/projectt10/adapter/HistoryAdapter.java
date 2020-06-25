package ss.projectt10.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ss.projectt10.DetailCardActivity;
import ss.projectt10.R;
import ss.projectt10.helper.RecyclerItemTouchHelper;
import ss.projectt10.model.Card;

import static ss.projectt10.helper.Util.DATA_CARD;

public class HistoryAdapter extends RecyclerView.Adapter <HistoryAdapter.HistoryViewHolder> {
    private Context mContext;
    private ArrayList<Card> cardsList;

    public HistoryAdapter(Context mContext, ArrayList<Card> cardsList) {
        this.mContext = mContext;
        this.cardsList = cardsList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_history, parent, false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);
        return historyViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, final int position) {

        final int positionBySortedSescending = getItemCount()-1- position;

        Glide.with(mContext).load(cardsList.get(positionBySortedSescending).getCardAvatar()).into(holder.mCardImage);
        holder.mCardName.setText(cardsList.get(positionBySortedSescending).getCardName());
        String time = getTime(cardsList.get(positionBySortedSescending).getUseTime());
        holder.mTimeUse.setText(time);

        holder.mLayoutItemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailCardActivity.class);
                Card detailCard = cardsList.get(positionBySortedSescending);
                intent.putExtra(DATA_CARD, detailCard);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }


    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView mCardImage;
        TextView mCardName, mTimeUse;
        ViewGroup mLayoutItemHistory;
        public View viewBackground, viewForeground;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardImage = itemView.findViewById(R.id.iv_card_image_history);
            mCardName = itemView.findViewById(R.id.tv_card_name_history);
            mTimeUse = itemView.findViewById(R.id.tv_time_history);
            mLayoutItemHistory = itemView.findViewById(R.id.layout_item_history_card);

            viewBackground = itemView.findViewById(R.id.view_background_history);
            viewForeground = itemView.findViewById(R.id.view_foreground_history);
        }
    }


    String getTime (long myDateObj) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(myDateObj);
        return formattedDate;
    }

    public void removeItemHistory(int position) {
        cardsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
}
