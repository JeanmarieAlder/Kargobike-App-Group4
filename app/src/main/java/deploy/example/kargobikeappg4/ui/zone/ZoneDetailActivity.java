package deploy.example.kargobikeappg4.ui.zone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.adapter.ListAdapter;
import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.db.entities.Zone;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.user.UserListViewModel;
import deploy.example.kargobikeappg4.viewmodel.zone.ZoneViewModel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ZoneDetailActivity extends AppCompatActivity {

    //Attributes
    private String zoneId;
    private boolean isEdit;

    private Zone zone;
    private ZoneViewModel zoneViewModel;

    private UserListViewModel viewModelUsers;
    private List<User> userList;
    private User respUser;
    private ListAdapter userAdapter;

    private EditText eName;
    private EditText eCity;
    private Spinner spinnerRiders;

    //OnCreate to initialize the information
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_detail);

        initialize();
        initializeViewModels();
    }

    //Initialize all UI elements
    private void initialize(){

        eName = findViewById(R.id.zd_input_name);
        eCity = findViewById(R.id.zd_input_city);
        spinnerRiders = findViewById(R.id.zd_spinner_riders);

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if(isEdit){
            zoneId = getIntent().getStringExtra("zoneId");

        }
    }

    //Initialize the viewmodel
    private void initializeViewModels(){
        ZoneViewModel.Factory factory = new ZoneViewModel.Factory(
                getApplication(), zoneId);
        zoneViewModel = ViewModelProviders.of(this, factory)
                .get(ZoneViewModel.class);
        zoneViewModel.getZone().observe(this, zoneEntity -> {
            if (zoneEntity != null) {
                zone = zoneEntity;
                updateContent();
            }
        });

        //Adapter for all users
        userAdapter = new ListAdapter<>(ZoneDetailActivity.this,
                R.layout.rowlist, new ArrayList<>());
        spinnerRiders.setAdapter(userAdapter);
        UserListViewModel.Factory factoryUser = new UserListViewModel.Factory(
                getApplication()
        );

        //return all users
        viewModelUsers = ViewModelProviders.of(this, factoryUser)
                .get(UserListViewModel.class);
        viewModelUsers.getAllUsers().observe(this, users -> {
            if (users != null) {

                userList = users;
                ArrayList<String> userNames = new ArrayList<>();
                for (User u : users) {
                    userNames.add(u.getName());
                }
                updateAdapterRiderList(userNames);
                updateContent();
            }
        });
    }

    //Update the content
    private void updateContent(){
        //Zone exist
        if(zone != null){
            eName.setText(zone.getName());
            eCity.setText(zone.getCity());

            //look at the right user
            if(zone.getIdMainRider() != null && userList != null){
                respUser = new User();
                for(User u : userList){
                    if(u.getIdUser().equals(zone.getIdMainRider())){
                        respUser = u;
                    }
                }
                int spinnerPosition = userAdapter.getPosition(respUser.getName());
                spinnerRiders.setSelection(spinnerPosition);
            }

        }
    }


    //Update the selectet Rider of the DropDwon
    private void updateAdapterRiderList(List<String> riderNames){
        userAdapter.updateData(new ArrayList<>(riderNames));
    }

    //Save the changes
    private void saveChanges() {

        //No zone exist, create one
        if(!isEdit){
            zone = new Zone();
        }
        zone.setName(eName.getText().toString());
        zone.setCity(eCity.getText().toString());

        respUser = new User();
        for(User u : userList){
            if(u.getName().equals(spinnerRiders.getSelectedItem())){
                respUser = u;
                break;
            }
        }
        zone.setIdMainRider(respUser.getIdUser());

        //Zone exist, edit it an save the changes
        if(isEdit){
            //update
            zoneViewModel.updateZone(zone, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),
                            "Update succesful", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFailure(Exception e) {

                    Toast.makeText(getApplicationContext(),
                            "Update failed", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            zoneViewModel.createZone(zone, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Creation succesful", Toast.LENGTH_LONG).show();
                    onBackPressed(); //finally, go back to previous screen
                }
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), "Creation failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    //Save the changes
    public void saveButton(View view){
        saveChanges();
    }
}
