package com.example.kargobikeappg4.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.ui.nav.BikeConfirmationActivity;
import com.example.kargobikeappg4.ui.nav.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.edit_RegName);
        password = findViewById(R.id.edit_RegPwd);
        btnSignUp = findViewById(R.id.btn_RegNew);

        btnSignUp.setOnClickListener(v -> {
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
                Toast.makeText(Register.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
            }
            else  if(!(email.isEmpty() && pwd.isEmpty())){
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(Register.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(Register.this, BikeConfirmationActivity.class));
                    }
                });
            }
            else{
                Toast.makeText(Register.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

            }
        });
        /*
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,LoginActivity.class);
                startActivity(i);
            }
        });

         */
    }
}
