package deploy.example.kargobikeappg4.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.ui.nav.LoginActivity;

import deploy.example.kargobikeappg4.R;

import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.user.UserViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Register extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText name;
    private Button btnSignUp;
    private EditText phone;
    private EditText adress;

    private FirebaseAuth mFirebaseAuth;
    private UserViewModel viewModel;
    private FirebaseUser fUser;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize Buttons, FirebaseAuth, Text
        initialize();

        //initialize viewModel
        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(), fUser.getUid());
        viewModel = ViewModelProviders.of(this, factory)
                .get(UserViewModel.class);

        //Listener
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if email, name or password is missing
                if (email.getText().toString().equals("") || password.getText().toString().equals("") || name.getText().toString().equals("")) {
                    Toast.makeText(Register.this, "Not completed", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    register();
                }


            }
        });

    }

    //register the user in the Firebase authentification
    public void register() {
        String uEmail = email.getText().toString().trim();
        String uPwd = password.getText().toString().trim();
        String uNameUser = name.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();
        String idAddress = adress.getText().toString().trim();

        String language = "EN";
        String workingsince = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String idFunction = "Rider";

        //Create user object
        User user = new User();
        user.setName(uNameUser);
        user.setEmail(uEmail);
        user.setLanguage(language);
        user.setWorkingsince(workingsince);
        user.setPhoneNumber(phoneNumber);
        user.setIdFunction(idFunction);
        user.setIdAddress(idAddress);

        mFirebaseAuth.createUserWithEmailAndPassword(uEmail, uPwd)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser userF = mFirebaseAuth.getCurrentUser();
                        String idUser = userF.getUid();
                        createUser(user, idUser);
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(Register.this, LoginActivity.class));

                    } else {

                        // If sign in fails, display a message to the user.
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(Register.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }

    //save the user in the firebase user database
    public void createUser(User user, String id) {
        viewModel.createUser(user, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Creation succesful", Toast.LENGTH_LONG).show();
                        onBackPressed(); //finally, go back to previous screen
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (e.getMessage().contains("FOREIGN KEY")) {
                            Toast.makeText(getApplicationContext(), "Creation error: stage name doesn't exist", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Creation failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                , id
        );
    }

    protected void initialize() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edit_registerMail);
        password = findViewById(R.id.edit_registerPassword);
        name = findViewById(R.id.edit_registerName);
        phone = findViewById(R.id.edit_registerPhone);
        adress = findViewById(R.id.edit_registerAdress);
        btnSignUp = findViewById(R.id.btn_newUser);
        progressBar = findViewById(R.id.progressBar);
        fUser = mFirebaseAuth.getCurrentUser();

    }
}
