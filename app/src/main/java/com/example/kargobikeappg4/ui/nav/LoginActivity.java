package com.example.kargobikeappg4.ui.nav;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.ui.login.Register;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;
    Button login;
    FirebaseAuth mAuth;
    GoogleSignInClient google;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        google = GoogleSignIn.getClient(this, googleOptions);

        login.setOnClickListener(v -> signInGoogle());

        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }

    }

    public void signInGoogle(){
        progressBar.setVisibility(View.VISIBLE);

  /*
        Intent intent2 = google.getSignInIntent();
        startActivityForResult(intent2, GOOGLE_SIGN);

      */

        Intent intent = new Intent(this, BikeConfirmationActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN){
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    firebaseAuthWithGoogle(account);
                }
            }catch(ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG", "signin successfully");

                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);

                        riderSelected();
                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG", "signin failure", task.getException());

                        Toast.makeText(this,"SignIn Failed!", Toast.LENGTH_SHORT);
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if(user != null){
            String name = user.getDisplayName();
            String email = user.getEmail();
            Log.d("TAG", "Name: " + name + " " + email);

        }
    }

    public void riderSelected()
    {
        Intent intent = new Intent(this, BikeConfirmationActivity.class);
        startActivity(intent);
    }

    public void registerUser(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}
