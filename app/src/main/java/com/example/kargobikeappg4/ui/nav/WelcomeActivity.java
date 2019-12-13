package com.example.kargobikeappg4.ui.nav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.ui.product.ProductListActivity;
import com.example.kargobikeappg4.ui.transport.TransportListActivity;
import com.example.kargobikeappg4.ui.user.ProfileActivity;
import com.example.kargobikeappg4.ui.user.UserListActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {
    Button logOut;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        logOut = findViewById(R.id.btn_logOut);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        super.onStart();


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //On Succesfull signout we navigate the user back to LoginActivity

                        Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);

                        //updateUI(null);
                    }
                });
            }
        });
    }

    public void Welcome_button_transportList(View view)
    {
        Intent intent = new Intent(this, TransportListActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_productList(View view)
    {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_profile(View view)
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_userList(View view)
    {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }
}
