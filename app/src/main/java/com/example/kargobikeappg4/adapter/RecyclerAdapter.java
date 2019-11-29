package com.example.kargobikeappg4.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.db.entities.Rider;
import com.example.kargobikeappg4.util.RecyclerViewItemClickListener;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter class for recyclerviews. Uses act_item.xml to arrange items
 * in viewholders.
 * @param <T>
 */
public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<T> mData;
    private RecyclerViewItemClickListener mListener;
    private static int pos; //get the position of the click (used for long clics)
    private Context ctx;

    /**
     * Get the position of click
     * @return click position
     */
    public static int getPos() {
        return pos;
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        //listens for changes
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        //set the simple click listener
        v.setOnClickListener(view -> mListener.onItemClick(view, viewHolder.getAdapterPosition()));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = mData.get(position);
        //binds text from db to recycler holder according to class
        if (item.getClass().equals(Order.class)) {
            //View for order
            holder.tvFirstHeader.setText(R.string.s_product_points);
            holder.tvFirstRow.setText(((Order) item).getIdProduct());
            holder.tvSecondHeader.setText(R.string.s_date_points);
            holder.tvSecondRow.setText(((Order) item).getDateDelivery());
            holder.tvThirdHeader.setText(R.string.s_time_points);
            holder.tvThirdRow.setText(((Order) item).getTimeDelivery());
            if(((Order) item).getStatus().equals("1")){
                holder.tvRightSide.setText(R.string.s_loaded);
            }else{
                holder.tvRightSide.setText(R.string.s_pending);
            }

        }
        if(item.getClass().equals((Rider.class))){

        }

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<T> data) {
        if (mData == null) {
            mData = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            //Check if there are differences with database.
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mData.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof Order) {
                        return ((Order) mData.get(oldItemPosition)).getIdOrder().equals(((Order) data.get(newItemPosition)).getIdOrder());
                    }

                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    //checks if Act entity is the same
                    if (mData instanceof Order) {
                        Order newOrder = (Order) data.get(newItemPosition);
                        Order oldOrder = (Order) mData.get(newItemPosition);
                        return newOrder.getIdOrder().equals(oldOrder.getIdOrder())
                                && Objects.equals(newOrder.getQuantity(), oldOrder.getQuantity())
                                && Objects.equals(newOrder.getDateOrder(), oldOrder.getDateOrder())
                                && Objects.equals(newOrder.getDateDelivery(), oldOrder.getDateDelivery())
                                && Objects.equals(newOrder.getTimeDelivery(), oldOrder.getTimeDelivery())
                                && Objects.equals(newOrder.getSignature(), oldOrder.getSignature())
                                && Objects.equals(newOrder.getDeliveryPicture(), oldOrder.getDeliveryPicture())
                                && Objects.equals(newOrder.getIdCustomer(), oldOrder.getIdCustomer())
                                && Objects.equals(newOrder.getIdProduct(), oldOrder.getIdProduct())
                                && Objects.equals(newOrder.getIdPickupCheckpoint(), oldOrder.getIdPickupCheckpoint())
                                && Objects.equals(newOrder.getIdDeliveryCheckpoint(), oldOrder.getIdDeliveryCheckpoint())
                                ;
                    }
                    if(mData instanceof Rider){

                    }

                    return false;
                }
            });
            mData = data;
            result.dispatchUpdatesTo(this);
        }
    }

    /**
     * View Holder class that places elements in card view.
     * Also sets the view ready fo context menu
     */
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView tvFirstHeader;
        private TextView tvSecondHeader;
        private TextView tvThirdHeader;
        private TextView tvFirstRow;
        private TextView tvSecondRow;
        private TextView tvThirdRow;
        private TextView tvRightSide;
        private CardView cardView;
        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFirstHeader = itemView.findViewById(R.id.ri_tv_firstrow_header);
            tvSecondHeader = itemView.findViewById(R.id.ri_tv_secondrow_header);
            tvThirdHeader = itemView.findViewById(R.id.ri_tv_thirdrow_header);
            tvFirstRow = itemView.findViewById(R.id.ri_tv_firstrow);
            tvSecondRow = itemView.findViewById(R.id.ri_tv_secondrow);
            tvThirdRow = itemView.findViewById(R.id.ri_tv_thirdrow);
            tvRightSide = itemView.findViewById(R.id.ri_tv_right);
            cardView = itemView.findViewById(R.id.oi_cardView);
            cardView.setOnCreateContextMenuListener(this);


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //User can edit or delete an entity
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2,2, "Delete");
            pos = getAdapterPosition();
        }

    }

}
