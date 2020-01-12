package deploy.example.kargobikeappg4.ui.nav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.ui.login.Register;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private EditText name;
    private EditText pwd;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton btnGoogleSignIn;
    private Button btnUserSignIn;

    private EditText eLogin;
    private EditText ePassword;

    //On create method, initialize all stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize Buttons, FirebaseAuth, Text
        initialize();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //if there are users already loged in
        mGoogleSignInClient.signOut();

        //Listeners
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnUserSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if Email or Password is missing
                if(name.getText().toString().equals("")&&pwd.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Email and Password is missing.", Toast.LENGTH_SHORT).show();
                }else if (name.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Email is missing.", Toast.LENGTH_SHORT).show();
                }else if (pwd.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Password is missing.", Toast.LENGTH_SHORT).show();
                }else {
                    userSignIn();
                }
            }
        });

    }

    //Sign in with google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        progressBar.setVisibility(View.VISIBLE);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    //Sign in with email user
    private void userSignIn(){
        String email = name.getText().toString().trim();
        String password = pwd.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            startActivity(new Intent(LoginActivity.this, BikeConfirmationActivity.class));
                            progressBar.setVisibility(View.INVISIBLE);

                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }

    //Look, if login was successful or not
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
                Log.d(TAG,"Google sign in successfuly!");
                firebaseAuthWithGoogle(account);
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(new Intent(LoginActivity.this, BikeConfirmationActivity.class));


            }catch(ApiException e){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),
                        "Please contact the manager for a valid login", Toast.LENGTH_LONG).show();
                Log.d(TAG,"Google sign in failed!", e);
            }
        }
    }

    //Login with google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Sign In with Google failed.", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    //Initialize all elements
    protected void initialize(){

        btnGoogleSignIn = (SignInButton)findViewById(R.id.btn_googleSignIn);
        btnUserSignIn = findViewById(R.id.btn_signIn);

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.edit_loginUserName);
        pwd = findViewById(R.id.edit_loginPassword);
        progressBar = findViewById(R.id.progressBar);

        eLogin = findViewById(R.id.edit_loginUserName);
        ePassword = findViewById(R.id.edit_loginPassword);

        //TODO: Remove for production (static login)
        eLogin.setText("mr.demo@hesso.ch");
        ePassword.setText("hessovs");
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
