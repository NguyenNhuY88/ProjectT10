package ss.projectt10.helper;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import ss.projectt10.adapter.CardRecyclerAdapter;
import ss.projectt10.adapter.HistoryAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foregroundView = null;
            if ( viewHolder instanceof CardRecyclerAdapter.MyViewHolder) {
                foregroundView = ((CardRecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
            } else if ( viewHolder instanceof HistoryAdapter.HistoryViewHolder) {
                foregroundView = ((HistoryAdapter.HistoryViewHolder) viewHolder).viewForeground;
            }

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        View foregroundView = null;
        if ( viewHolder instanceof CardRecyclerAdapter.MyViewHolder) {
            foregroundView = ((CardRecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
        } else if ( viewHolder instanceof HistoryAdapter.HistoryViewHolder) {
            foregroundView = ((HistoryAdapter.HistoryViewHolder) viewHolder).viewForeground;
        }

        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      //  final View foregroundView = ((CardRecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
        View foregroundView = null;
        if ( viewHolder instanceof CardRecyclerAdapter.MyViewHolder) {
            foregroundView = ((CardRecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
        } else if ( viewHolder instanceof HistoryAdapter.HistoryViewHolder) {
            foregroundView = ((HistoryAdapter.HistoryViewHolder) viewHolder).viewForeground;
        }

        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        //final View foregroundView = ((CardRecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
        View foregroundView = null;
        if ( viewHolder instanceof CardRecyclerAdapter.MyViewHolder) {
            foregroundView = ((CardRecyclerAdapter.MyViewHolder) viewHolder).viewForeground;
        } else if ( viewHolder instanceof HistoryAdapter.HistoryViewHolder) {
            foregroundView = ((HistoryAdapter.HistoryViewHolder) viewHolder).viewForeground;
        }
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}