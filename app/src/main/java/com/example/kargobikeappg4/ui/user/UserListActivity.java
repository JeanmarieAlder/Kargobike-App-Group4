package com.example.kargobikeappg4.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.RecyclerAdapter;
import com.example.kargobikeappg4.db.entities.User;
import com.example.kargobikeappg4.viewmodel.user.UserListViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerAdapter<User> adapter;
    private List<User> users;
    private UserListViewModel listViewModel;
    private RecyclerView rView;

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
                    ProfileActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            intent.putExtra("userId", users.get(position).getIdUser());
            intent.putExtra("user", false);
            Log.d("UID", "User ID of selected user: " + users.get(position).getIdUser());
            startActivity(intent);
        });

        UserListViewModel.Factory factory = new UserListViewModel.Factory(
                getApplication()
        );

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
}
