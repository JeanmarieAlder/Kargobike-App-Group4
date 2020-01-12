package deploy.example.kargobikeappg4.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.adapter.RecyclerAdapter;
import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.viewmodel.user.UserListViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    //Attributes
    private RecyclerAdapter<User> adapter;
    private List<User> users;
    private UserListViewModel listViewModel;
    private RecyclerView rView;

    //On create method, initialize all stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //initializes recyclerview
        rView = findViewById(R.id.recycler_view_userlist);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes

        users = new ArrayList<>();

        //Add click listener, opens details of the selected user
        adapter = new RecyclerAdapter<>((v, position) -> {
            Intent intent = new Intent(UserListActivity.this,
                    UserDetailActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            intent.putExtra("userId", users.get(position).getIdUser());
            intent.putExtra("isEdit", true);
            startActivity(intent);
        });

        UserListViewModel.Factory factory = new UserListViewModel.Factory(
                getApplication()
        );

        //getting all users
        listViewModel = ViewModelProviders.of(this, factory)
                .get(UserListViewModel.class);
        listViewModel.getAllUsers().observe(this, userEntities -> {
            if (userEntities != null) {
                users = userEntities;
                adapter.setData(users);
            }
        });
        rView.setAdapter(adapter);
    }

    //Button for adding a new user
    public void Product_button_registerUser(View view) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("isEdit", false);

        startActivity(intent);
    }

}
