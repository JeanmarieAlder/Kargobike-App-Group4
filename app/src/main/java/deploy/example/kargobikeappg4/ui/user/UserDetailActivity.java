package deploy.example.kargobikeappg4.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.adapter.ListAdapter;
import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.ui.nav.LoginActivity;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.type.TypeListViewModel;
import deploy.example.kargobikeappg4.viewmodel.user.UserViewModel;

public class UserDetailActivity extends AppCompatActivity {

    private static final String TAG = "UserDetailActivity";

    //Variable instanciations
    private String userId;
    private boolean editMode;
    private User user;
    private UserViewModel viewModel;

    private Button btnSave;
    private Button btnDelete;

    //private EditText eProduct;
    private EditText eName;
    private EditText eLanguage;
    private EditText ePassword;
    private EditText eWorkingSince;
    private EditText eEmail;
    private EditText ePhone;
    private EditText eAddress;

    //Spinner
    private Spinner spinnerTypes;
    private ListAdapter adapterTypesList;
    private TypeListViewModel typesViewModel;

    //Firebase Authentification
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser fUser;

    //DB
    private DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        //Initializes buttons, views, current ID and edit mode
        initialize();

        //Create viewmodel
        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(), userId);
        viewModel = ViewModelProviders.of(this, factory)
                .get(UserViewModel.class);

        if (editMode) {
            viewModel.getUser().observe(this, userEntity -> {
                if (userEntity != null) {
                    user = userEntity;
                    updateContent();
                }
            });
        }
    }


    /**
     * Initializes views, buttons, id and editmode
     */
    private void initialize() {
        eName = findViewById(R.id.td_input_name);
        ePassword = findViewById(R.id.td_input_pasword);
        eLanguage = findViewById(R.id.td_input_language);
        eWorkingSince = findViewById(R.id.td_input_workingsince);
        eEmail = findViewById(R.id.td_input_email);
        ePhone = findViewById(R.id.td_input_phoneNumber);
        eAddress = findViewById(R.id.td_input_address);
        reff = FirebaseDatabase.getInstance().getReference().child("Order");
        btnSave = findViewById(R.id.button_save);
        btnDelete = findViewById(R.id.td_button_cancel);
        btnSave.setOnClickListener(new View.OnClickListener(){

               @Override
               public void onClick(View v) {
                   saveChanges();
               }
           }
        );

        //get order ID from intent and set edit mode to false if new order
        editMode = getIntent().getBooleanExtra("isEdit", false);

        if(editMode){
            userId = getIntent().getExtras().get("userId").toString();
        }
    }

    private void updateContent() {
        if (user != null) {
            eName.setText(user.getName());
            //ePassword.setText("Secret, to do");
            eLanguage.setText(user.getLanguage());
            eWorkingSince.setText(user.getWorkingsince());
            eEmail.setText(user.getEmail());
            ePhone.setText(user.getPhoneNumber());
            eAddress.setText(user.getIdAddress());
        }
    }

    public void User_button_delete(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.delete));
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Delete a user");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete), (dialog, which) -> {
            viewModel.deleteUser(user, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Delete user: success");
                    goToTripsActivity();
                }

                private void goToTripsActivity() {
                    Intent intent = new Intent(UserDetailActivity.this, UserListActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Exception e) {}
            });
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    private void saveChanges() {

        if(editMode){
            updateUserDB(false);
        }else{
            User user = new User();

            user.setName(eName.getText().toString());
            user.setLanguage(eLanguage.getText().toString());
            user.setWorkingsince(eWorkingSince.getText().toString());
            user.setEmail(eEmail.getText().toString());
            user.setPhoneNumber(ePhone.getText().toString());
            user.setIdAddress(eAddress.getText().toString());

            String uEmail = eEmail.getText().toString().trim();
            String uPwd = ePassword.getText().toString().trim();

            mFirebaseAuth.createUserWithEmailAndPassword(uEmail, uPwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                FirebaseUser userF = mFirebaseAuth.getCurrentUser();
                                String idUser = userF.getUid();
                                createUser(user, idUser);
                                //progressBar.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(UserDetailActivity.this, LoginActivity.class));

                            } else {

                                // If sign in fails, display a message to the user.
                                //progressBar.setVisibility(View.INVISIBLE);

                                Toast.makeText(UserDetailActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        }
    }

    public void createUser(User user, String id){
        viewModel.createUser(user, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
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
                }
                ,id
        );
    }

    /**
     * Updates an existing order in the DB. Different behaviour if
     * the user only changes the status.
     * @param isChangingStatus true if only status is changing
     */
    private void updateUserDB(boolean isChangingStatus){
        user.setName(eName.getText().toString());
        user.setLanguage(eLanguage.getText().toString());
        user.setWorkingsince(eWorkingSince.getText().toString());
        //user.setEmail(eEmail.getText().toString());
        user.setPhoneNumber(ePhone.getText().toString());
        user.setIdAddress(eAddress.getText().toString());

        viewModel.updateUser(user, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),
                        "Update succesful", Toast.LENGTH_LONG).show();
                if(!isChangingStatus){
                    onBackPressed(); //finally, go back to previous screen
                }else{
                    updateContent(); //If only status has changed, stay on page and update content
                }
            }

            @Override
            public void onFailure(Exception e) {

                Toast.makeText(getApplicationContext(),
                        "Update failed", Toast.LENGTH_LONG).show();
            }
        });
    }

}


