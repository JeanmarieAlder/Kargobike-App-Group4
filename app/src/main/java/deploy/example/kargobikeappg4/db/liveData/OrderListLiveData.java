package deploy.example.kargobikeappg4.db.liveData;

import deploy.example.kargobikeappg4.db.entities.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class OrderListLiveData extends LiveData<List<Order>> {

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    //constructor
    public OrderListLiveData(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() { }


    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toOrders(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    }

    private List<Order> toOrders(DataSnapshot snapshot){
        //get values for list
        List<Order> orders = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Order entity = childSnapshot.getValue(Order.class);
            entity.setIdOrder(childSnapshot.getKey());
            orders.add(entity);
        }

        //Sort orders by delivery date
        orders.sort(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getDateDelivery().compareTo(o2.getDateDelivery());
            }
        });
        return orders;
    }


}
