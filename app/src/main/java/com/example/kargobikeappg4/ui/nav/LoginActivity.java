package com.example.kargobikeappg4.ui.nav;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.ui.login.Register;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;
    Button loginGoogle;
    FirebaseAuth mAuth;
    GoogleSignInClient google;
    ProgressBar progressBar;

    EditText email;

    Button loginAccount;
    EditText emailId, password;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginGoogle = findViewById(R.id.btn_login);
        loginAccount = findViewById(R.id.btn_signIn);
        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.edit_Naccount);
        password = findViewById(R.id.edit_Npassword);
        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions googleOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        google = GoogleSignIn.getClient(this, googleOptions);

        loginGoogle.setOnClickListener(v -> signInGoogle());

        loginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intToHome = new Intent(LoginActivity.this,BikeConfirmationActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }

            }
        });


        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }


/*
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, BikeConfirmationActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };

*/

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

                        Intent intent = new Intent(this, BikeConfirmationActivity.class);
                        startActivity(intent);
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

    public void riderSelected(View view)
    {
        Intent intent = new Intent(this, BikeConfirmationActivity.class);
        startActivity(intent);
    }

    public void registerUser(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
    /*
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
*/
}
