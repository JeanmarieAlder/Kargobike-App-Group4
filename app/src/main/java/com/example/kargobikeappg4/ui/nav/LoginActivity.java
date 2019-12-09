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
import android.widget.TextView;
import android.widget.Toast;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.ui.login.Register;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton btnGoogleSignIn;
    private Button btnSignOut;
    private Button btnUserSignIn;
    private TextView currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        currentUser = findViewById(R.id.txt_currentUser);
        btnGoogleSignIn = (SignInButton)findViewById(R.id.btn_googleSignIn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Check if there are Users already loged in

        /*
        currentUser = mAuth.getCurrentUser();
        FirebaseAuth.getInstance().signOut();

        updateUI(currentUser);
        */

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnSignOut = findViewById(R.id.btn_signOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                 /*
          Sign-out is initiated by simply calling the googleSignInClient.signOut API. We add a
          listener which will be invoked once the sign out is the successful
           */


            }
        });

    }

    //Update the content of the text label
    private void updateUI(GoogleSignInAccount account) {
        if(account != null){
            currentUser.setText(account.getEmail());

        }else{
            currentUser.setText("No User!");
        }
    }

    //Sign in and Sign out
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                GoogleSignInAccount account2 = GoogleSignIn.getLastSignedInAccount(this);
                Log.d(TAG,"Google sign in successfuly 1!");
                updateUI(account2);
                firebaseAuthWithGoogle(account);
                Intent intent = new Intent(LoginActivity.this, BikeConfirmationActivity.class);
                startActivity(intent);

               // handleSignInResult(task);

            }catch(ApiException e){
                Log.w(TAG,"Google sign in failed!", e);
                Log.d(TAG,"Google sign in failed!", e);
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // Snackbar.make(f, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    //some tests for clients

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onpause", "------------------------ON PAUSE");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onstop", "------------------------ON STOP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ondestroy", "--------------------------ON DESTROY");
    }
}
