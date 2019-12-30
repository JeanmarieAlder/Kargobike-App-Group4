package deploy.example.kargobikeappg4.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.db.repository.UserRepository;
import deploy.example.kargobikeappg4.viewmodel.address.AddressViewModel;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.db.entities.Address;
import deploy.example.kargobikeappg4.viewmodel.user.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity{

    private View group;
    private View loading;
    private UserViewModel userViewmodel;
    private String userId;
    private User user;
    private TextView name;
    private TextView address;
    private TextView email;
    private TextView phone;
    private Boolean currentUser;
    private Intent currentIntent;
    private UserRepository uRep;
    private Button about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Initialize();

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = currentIntent.getBooleanExtra("user", true);

        if(!currentUser) {
            userId = currentIntent.getStringExtra("userId");
            about.setVisibility(View.INVISIBLE);
        }
        else
            userId = fbUser.getUid();

        //Create viewmodel for user
        UserViewModel.Factory userFactory = new UserViewModel.Factory(
                getApplication(), userId);
        userViewmodel = ViewModelProviders.of(this, userFactory)
                .get(UserViewModel.class);
        userViewmodel.getUser().observe(this, userEntity ->{
            if(userEntity != null)
            {
                user = userEntity;
                UpdateContent();
            }
        });


    }

    private void Initialize(){

        name = findViewById(R.id.profile_tv_name);
        address  = findViewById(R.id.profile_tv_address);
        email = findViewById(R.id.profile_tv_mailAddress);
        phone = findViewById(R.id.profile_tv_phone);

        group = findViewById(R.id.groupProfileInfos);
        group.setVisibility(View.INVISIBLE);
        loading = findViewById(R.id.profile_loading);
        about = findViewById(R.id.profile_btn_about);


        currentIntent = getIntent();
    }

    private void UpdateContent()
    {
        loading.setVisibility(View.INVISIBLE);

        name.setText(user.getName());
        address.setText(user.getIdAddress());
        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());

        group.setVisibility(View.VISIBLE);
    }

    public void user_button_monthlyReport(View view)
    {
        Intent intent = new Intent(this, MonthlyReportActivity.class);
        intent.putExtra("userId", user.getIdUser());

        startActivity(intent);
    }

    public void user_button_about(View view)
    {
        Intent intent = new Intent(this, AboutActivity.class);

        startActivity(intent);
    }
}
