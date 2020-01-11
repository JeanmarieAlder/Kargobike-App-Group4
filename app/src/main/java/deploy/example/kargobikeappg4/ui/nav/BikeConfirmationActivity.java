package deploy.example.kargobikeappg4.ui.nav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.viewmodel.user.UserViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class BikeConfirmationActivity extends AppCompatActivity {

    private UserViewModel userViewmodel;
    private User user;
    private TextView name;
    private Button btnReady;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_confirmation);

        Initialize();

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();


        //Create viewmodel
        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(), fbUser.getUid());
        userViewmodel = ViewModelProviders.of(this, factory)
                .get(UserViewModel.class);
        userViewmodel.getUser().observe(this, userEntitie ->{
            if(userEntitie != null)
            {
                user = userEntitie;
                UpdateContent();
            }
        });
    }

    private void Initialize(){

        name = findViewById(R.id.bikeca_tv_welcome);
        btnReady = findViewById(R.id.bikeca_button_confirmation);
        btnReady.setEnabled(false);

    }

    private void UpdateContent()
    {
        name.setText("Welcome, "+ user.getName());
        btnReady.setEnabled(true);
    }

    public void bikeConfirmation(View view)
    {
        String userNotif = user.getName();

        for(int i=0; i<userNotif.length();i++)
        {
            if(userNotif.charAt(i)==' ') {
                String debut = userNotif.substring(0,i);
                String fin = userNotif.substring(i+1,userNotif.length());
                userNotif = debut+fin;
            }
        }
        Intent intent = new Intent(this, WelcomeActivity.class);


        FirebaseMessaging.getInstance().subscribeToTopic(userNotif);

        Log.d("------ IDRESPONSIBLERIDER-------", userNotif);

        startActivity(intent);
    }
}
