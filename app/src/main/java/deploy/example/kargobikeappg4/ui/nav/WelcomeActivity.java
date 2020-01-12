package deploy.example.kargobikeappg4.ui.nav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.ui.transport.TransportDetailActivity;
import deploy.example.kargobikeappg4.ui.user.UserListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.db.entities.WorkDetails;
import deploy.example.kargobikeappg4.ui.product.ProductListActivity;
import deploy.example.kargobikeappg4.ui.transport.TransportListActivity;
import deploy.example.kargobikeappg4.ui.user.ProfileActivity;
import deploy.example.kargobikeappg4.ui.zone.ZoneListActivity;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.user.UserViewModel;
import deploy.example.kargobikeappg4.viewmodel.workDetails.WorkDetailsViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {

    //Attributes
    private static final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
    private static final Date startSession = Calendar.getInstance().getTime();

    private WorkDetailsViewModel viewModel;
    private String workDetailsId;
    private Button logOut;
    private FirebaseAuth fAuth;
    private String userIdFunction;

    private Button productList;
    private Button userList;
    private Button zoneList;
    private User user;
    private UserViewModel userViewmodel;


    //On create method, initialize all stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        logOut = findViewById(R.id.btn_logOut);

        userList = findViewById(R.id.button_UserList);
        productList = findViewById(R.id.button_ProductList);
        zoneList = findViewById(R.id.button_zoneList);


        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();


        //Create viewmodel
        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(), fbUser.getUid());
        userViewmodel = ViewModelProviders.of(this, factory)
                .get(UserViewModel.class);
        userViewmodel.getUser().observe(this, userEntitie -> {
            if (userEntitie != null) {
                user = userEntitie;
                userIdFunction = user.getIdFunction();

                if (userIdFunction.equals("Rider")) {
                    productList.setVisibility(View.INVISIBLE);
                    userList.setVisibility(View.INVISIBLE);
                    zoneList.setVisibility(View.INVISIBLE);
                }

                if (userIdFunction.equals("Dispatcher")) {
                    userList.setVisibility(View.INVISIBLE);
                }
            }
        });


        //Stuff for the logout wit Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        super.onStart();

        //logout with google
        logOut.setOnClickListener(v -> mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            //On Succesfull signout we navigate the user back to LoginActivity
            Toast.makeText(getApplicationContext(),
                    "Logout successful", Toast.LENGTH_LONG).show();
            GetHours();
            TransportDetailActivity.nbDelivery = 0;
        }));
    }


    /**
     * All Button on the mainpage
     */
    public void Welcome_button_zoneList(View view) {
        Intent intent = new Intent(this, ZoneListActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_transportList(View view) {
        Intent intent = new Intent(this, TransportListActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_productList(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_profile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_userList(View view) {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    //Wil calculate how long the user was loged ing
    public void GetHours() {

        String workingTime = null;

        Date endTime = Calendar.getInstance().getTime();

        long different = endTime.getTime() - startSession.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;

        String dtStart = elapsedHours + ":" + elapsedMinutes;

        try {
            SimpleDateFormat convertToDate = new SimpleDateFormat("HH:mm");
            Date date = convertToDate.parse(dtStart);
            SimpleDateFormat keepOnlyTime = new SimpleDateFormat("HH:mm");
            workingTime = keepOnlyTime.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Save the hours
        InsertWorkingHours(workingTime);
    }


    //Save the workinghours in the DB
    public void InsertWorkingHours(String workingTime) {

        String userId = fAuth.getInstance().getCurrentUser().getUid();


        //Create viewmodel
        WorkDetailsViewModel.Factory factory = new WorkDetailsViewModel.Factory(
                getApplication(), userId, workDetailsId);
        viewModel = ViewModelProviders.of(this, factory)
                .get(WorkDetailsViewModel.class);
        WorkDetails wd = new WorkDetails();

        wd.setDate(date);
        wd.setHours(workingTime);
        wd.setDeliveries(TransportDetailActivity.nbDelivery);

        viewModel.createWorkDetails(wd, userId, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "Sorry, something unexpected happend", Toast.LENGTH_LONG).show();
            }
        });

    }

}
