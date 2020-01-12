package deploy.example.kargobikeappg4.ui.zone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.adapter.RecyclerAdapter;
import deploy.example.kargobikeappg4.db.entities.Zone;
import deploy.example.kargobikeappg4.viewmodel.zone.ZoneListViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class ZoneListActivity extends AppCompatActivity {

    //Attributes
    private RecyclerAdapter<Zone> adapter;
    private List<Zone> zones;
    private ZoneListViewModel listViewModel;

    private RecyclerView rView;
    private ImageButton btnAdd;

    //On create method, initialize all stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_list);

        initialize();
        initializeAdapter();
        initializeViewModel();
    }


    //Initialize the elements oof th UI
    private void initialize(){
        rView = findViewById(R.id.zl_recycler_view);
        btnAdd = findViewById(R.id.zl_buton_add);

        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes

        zones = new ArrayList<>();
    }

    //Initialize the elements for the list (display the whole list)
    private void initializeAdapter(){
        //Add click listener, opens details of the selected order
        adapter = new RecyclerAdapter<>((v, position) -> {
            Intent intent = new Intent(this,
                    ZoneDetailActivity.class);
            intent.putExtra("zoneId", zones.get(position).getIdZone());
            intent.putExtra("isEdit", true);
            startActivity(intent);
        });
    }

    //Initialize ViewModel and set the informations to the page
    private void initializeViewModel(){
        ZoneListViewModel.Factory factory = new ZoneListViewModel.Factory(
                getApplication()
        );

        listViewModel = ViewModelProviders.of(this, factory)
                .get(ZoneListViewModel.class);
        listViewModel.getAllZones().observe(this, zoneEntity -> {
            if (zoneEntity != null) {
                zones = zoneEntity;
                adapter.setData(zones);
            }
        });
        rView.setAdapter(adapter);
    }

    //Button add, is forwarded to a new page
    public void buttonAdd(View view){
        Intent intent = new Intent(this, ZoneDetailActivity.class);
        intent.putExtra("isEdit", false);
        startActivity(intent);
    }
}
