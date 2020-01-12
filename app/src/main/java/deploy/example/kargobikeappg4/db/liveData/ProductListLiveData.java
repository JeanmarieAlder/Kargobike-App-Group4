package deploy.example.kargobikeappg4.db.liveData;

import deploy.example.kargobikeappg4.db.entities.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ProductListLiveData extends LiveData<List<Product>> {

    private final DatabaseReference reference;
    private final ProductListLiveData.MyValueEventListener listener = new ProductListLiveData.MyValueEventListener();

    //constructor
    public ProductListLiveData(DatabaseReference reference) {
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
            setValue(toProducts(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    }

    private List<Product> toProducts(DataSnapshot snapshot) {
        //get values for list
        List<Product> products = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Product entity = childSnapshot.getValue(Product.class);
            entity.setIdProduct(childSnapshot.getKey());
            products.add(entity);
        }
        return products;
    }
}

