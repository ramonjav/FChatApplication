package com.example.fchatapplication.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fchatapplication.Adapters.UserAdapter;
import com.example.fchatapplication.Entidades.User;
import com.example.fchatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.fchatapplication.Utilidades.Contantes.NODO_USUARIOS;

public class UsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private UserAdapter adapter;

    List<User> users;

    DatabaseReference reference;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.recycleUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        users = new ArrayList<>();

        showUsers();

    }

   /* private void showUsers() {

       final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);
                   if(!user.getId().equals(firebaseUser.getUid())){
                        users.add(user);
                    }
                }
                adapter = new UserAdapter(UsersActivity.this, users, false);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void showUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance()
                .getReference(NODO_USUARIOS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                            User user = childSnapshot.getValue(User.class);
                            if(!user.getId().equals(firebaseUser.getUid())){
                                users.add(user);
                            }
                        }
                        adapter = new UserAdapter(UsersActivity.this, users, false);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void Status(boolean status){

        reference = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS).child(user.getUid());

        HashMap<String, Object> Set = new HashMap<>();
        Set.put("status", status);

        reference.updateChildren(Set);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Status(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Status(false);
    }
}

