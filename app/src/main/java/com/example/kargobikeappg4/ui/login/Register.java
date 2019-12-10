package com.example.kargobikeappg4.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.RecyclerAdapter;
import com.example.kargobikeappg4.db.entities.User;
import com.example.kargobikeappg4.ui.nav.BikeConfirmationActivity;
import com.example.kargobikeappg4.ui.nav.LoginActivity;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.product.ProductListViewModel;
import com.example.kargobikeappg4.viewmodel.product.ProductViewModel;
import com.example.kargobikeappg4.viewmodel.user.UserListViewModel;
import com.example.kargobikeappg4.viewmodel.user.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Register extends AppCompatActivity {
    EditText emailId, password, name;
    private String userId;
    Button btnSignUp;
    FirebaseAuth mFirebaseAuth;
    private UserViewModel viewModel;

    private RecyclerAdapter<User> adapter;
    private List<User> users;
    private UserListViewModel listViewModel;
    private RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.edit_registerMail);
        password = findViewById(R.id.edit_registerPassword);
        name = findViewById(R.id.edit_registerName);
        btnSignUp = findViewById(R.id.btn_newUser);

        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(), userId);
        viewModel = ViewModelProviders.of(this, factory)
                .get(UserViewModel.class);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });

    }

    public void register(){
        String email = emailId.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String nameUser = name.getText().toString().trim();

        String language = "EN";
        String workingsince = "01/02/2019";
        String phoneNumber = "0791110000";
        String idFunction = "1";
        String idAddress = "1";

        User user = new User();
        user.setName(nameUser);
        user.setEmail(email);
        user.setLanguage(language);
        user.setWorkingsince(workingsince);
        user.setPhoneNumber(phoneNumber);
        user.setIdFunction(idFunction);
        user.setIdAddress(idAddress);


        Log.d("Register", "Here A");
        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Register", "Here B");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser userF = mFirebaseAuth.getCurrentUser();
                            String idUser = userF.getUid();

                            createUser(user, idUser);
                            startActivity(new Intent(Register.this, LoginActivity.class));

                        } else {

                            // If sign in fails, display a message to the user.
                            Log.d("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });



    }

    public void createUser(User user, String id){
        viewModel.createUser(user, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d("Register", "Here C");
                Toast.makeText(getApplicationContext(), "Creation succesful", Toast.LENGTH_LONG).show();
                onBackPressed(); //finally, go back to previous screen
            }

            @Override
            public void onFailure(Exception e) {
                if(e.getMessage().contains("FOREIGN KEY")){
                    Toast.makeText(getApplicationContext(), "Creation error: stage name doesn't exist", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Creation failed", Toast.LENGTH_LONG).show();
                }
            }
        },id );
    }
}
