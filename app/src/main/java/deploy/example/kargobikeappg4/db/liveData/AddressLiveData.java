package deploy.example.kargobikeappg4.db.liveData;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import deploy.example.kargobikeappg4.db.entities.Address;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AddressLiveData extends LiveData<Address>{
    private final DatabaseReference reference;
    private final AddressLiveData.MyValueEventListener listener = new AddressLiveData.MyValueEventListener();

    public AddressLiveData(DatabaseReference ref) {
        reference = ref;
    }

    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Address entity = dataSnapshot.getValue(Address.class);
            if(entity == null){
                return;
            }else{
                entity.setIdAddress(dataSnapshot.getKey());
                setValue(entity);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
