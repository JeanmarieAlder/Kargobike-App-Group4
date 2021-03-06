package deploy.example.kargobikeappg4.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.db.entities.Checkpoint;
import deploy.example.kargobikeappg4.db.entities.Customer;
import deploy.example.kargobikeappg4.db.entities.Order;
import deploy.example.kargobikeappg4.db.entities.Product;
import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.db.entities.Zone;
import deploy.example.kargobikeappg4.db.repository.ZoneRepository;
import deploy.example.kargobikeappg4.util.RecyclerViewItemClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter class for recyclerviews. Uses act_item.xml to arrange items
 * in viewholders.
 *
 * @param <T>
 */
public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //Attributes
    private List<T> mData;
    private RecyclerViewItemClickListener mListener;
    private static int pos; //get the position of the click (used for long clics)
    private ZoneRepository repository;
    private Context ctx;
    private CardView v;


    /**
     * Get the position of click
     *
     * @return click position
     */
    public static int getPos() {
        return pos;
    }

    //Constructor
    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        //listens for changes
        mListener = listener;
    }

    //Initialize the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        v = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        //set the simple click listener
        v.setOnClickListener(view -> mListener.onItemClick(view, viewHolder.getAdapterPosition()));
        return viewHolder;
    }

    //Display the correct values in the list depending on the class
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
            holder.tvRightSide.setText(((Order) item).getStatus());

            //Add red borders if it is currently on a train
            if (((Order) item).getCheckpoints() != null) {
                HashMap<String, Checkpoint> checkpoints = ((Order) item).getCheckpoints();

                for (Map.Entry<String, Checkpoint> cp : checkpoints.entrySet()) {
                    if (cp.getValue().getType().equals("Train Station") && cp.getValue().getDepartureTimestamp() == null) {
                        v.setCardBackgroundColor(0x4AFF0000);
                        holder.tvThirdHeader.setText(R.string.s_train_arrival);
                        holder.tvThirdRow.setText(cp.getValue().getArrivalTime());
                        holder.tvThirdHeader.setTypeface(holder.tvThirdHeader.getTypeface(), Typeface.BOLD);
                        holder.tvThirdRow.setTypeface(holder.tvThirdRow.getTypeface(), Typeface.BOLD);
                        break;
                    }
                }
            }

        }

        //View for User
        if (item.getClass().equals((User.class))) {

            holder.tvFirstHeader.setText(R.string.s_user_name);
            holder.tvFirstRow.setText(((User) item).getName());
            holder.tvSecondHeader.setText(R.string.s_user_email);
            holder.tvSecondRow.setText(((User) item).getEmail());
            holder.tvThirdHeader.setText(R.string.s_user_phone);
            holder.tvThirdRow.setText(((User) item).getPhoneNumber());
            holder.tvRightSide.setText("");
        }

        //View for products
        if (item.getClass().equals((Product.class))) {

            holder.tvSecondHeader.setText(R.string.s_name_points);
            holder.tvSecondRow.setText(((Product) item).getName());
            holder.tvThirdHeader.setText(R.string.s_description_points);
            holder.tvThirdRow.setText(((Product) item).getDescription());
            holder.tvRightSide.setText("");
        }

        //View for checkpoints
        if (item.getClass().equals((Checkpoint.class))) {

            holder.tvFirstHeader.setText(R.string.s_type_points);
            holder.tvFirstRow.setText(((Checkpoint) item).getType());
            holder.tvSecondHeader.setText(((Checkpoint) item).getArrivalTimestamp());
            holder.tvSecondSeparator.setText(R.string.s_separator_dash);
            holder.tvSecondRow.setText(((Checkpoint) item).getDepartureTimestamp());
            holder.tvThirdHeader.setText(R.string.s_reamrk_points);
            holder.tvThirdRow.setText(((Checkpoint) item).getRemark());
            holder.tvRightSide.setText("");
        }

        //View for Customer
        if (item.getClass().equals((Customer.class))) {

            holder.tvFirstHeader.setText("");
            holder.tvFirstRow.setText(((Customer) item).getBillingName());
            holder.tvSecondHeader.setText("");
            holder.tvSecondRow.setText(((Customer) item).getIdAddress());
            holder.tvThirdHeader.setVisibility(View.GONE);
            holder.tvThirdRow.setVisibility(View.GONE);
            holder.tvRightSide.setVisibility(View.GONE);
        }

        //View for Zones
        if (item.getClass().equals((Zone.class))) {
            holder.tvFirstHeader.setText("");
            holder.tvFirstRow.setText(((Zone) item).getName());
            holder.tvSecondHeader.setText("");
            holder.tvSecondRow.setText(((Zone) item).getCity());
            holder.tvThirdHeader.setVisibility(View.GONE);
            holder.tvThirdRow.setVisibility(View.GONE);
            holder.tvRightSide.setVisibility(View.GONE);
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
                    if (mData instanceof Customer) {
                        Customer newCustomer = (Customer) data.get(newItemPosition);
                        Customer oldCustomer = (Customer) mData.get(newItemPosition);
                        return newCustomer.getIdCustomer().equals(oldCustomer.getIdCustomer())
                                && Objects.equals(newCustomer.getBillingName(), oldCustomer.getBillingName())
                                && Objects.equals(newCustomer.getIdAddress(), oldCustomer.getIdAddress())
                                && Objects.equals(newCustomer.getTitre(), oldCustomer.getTitre())
                                ;
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
        private TextView tvSecondSeparator;
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
            tvSecondSeparator = itemView.findViewById(R.id.ri_tv_secondrow_separator);
            cardView = itemView.findViewById(R.id.oi_cardView);
            cardView.setOnCreateContextMenuListener(this);


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //User can edit or delete an entity
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            pos = getAdapterPosition();
        }

    }


}
