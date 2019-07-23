package com.example.fchatapplication.Activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.fchatapplication.Adapters.UserAdapter;
import com.example.fchatapplication.Entidades.Chat;
import com.example.fchatapplication.Entidades.User;
import com.example.fchatapplication.R;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Environment;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.fchatapplication.Utilidades.Contantes.NODO_MENSAJES;
import static com.example.fchatapplication.Utilidades.Contantes.NODO_USUARIOS;
import static com.example.fchatapplication.Utilidades.Contantes.UBIC;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    UserAdapter adapter;
    List<User> users;

    FirebaseUser Fuser;
    DatabaseReference reference;
    DatabaseReference reference2;

    private List<String> usersList;
    NavigationView navigationView;

    private static String fichero = Environment.getExternalStorageDirectory().getAbsolutePath()+UBIC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.rvUsers);
        LinearLayoutManager l = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(l);
        Fuser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();

        reference2 = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS+"/"+Fuser.getUid());

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null){
                    View hView = navigationView.getHeaderView(0);
                    TextView correo = hView.findViewById(R.id.CorreoMenu);
                    TextView name = hView.findViewById(R.id.NameMenu);
                    correo.setText(user.getEmail());
                    name.setText(user.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //checkPermission();
        permisos();

        final File file = new File(fichero);

        if(!file.exists()){
            file.mkdirs();
        }


        reference = FirebaseDatabase.getInstance().getReference(NODO_MENSAJES);
        usersList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(Fuser.getUid())){
                        usersList.add(chat.getReciver());
                    }
                    if(chat.getReciver().equals(Fuser.getUid())) {
                        usersList.add(chat.getSender());
                    }
                }
               read();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void Status(boolean status){

        reference = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS).child(Fuser.getUid());

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Status(false);
    }

   public void read(){

        users = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference(NODO_USUARIOS);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for(String id : usersList){
                        if(user.getId().equals(id)){
                            if(users.size() > 0){
                                for(User user1 : users){
                                    if(!user.getId().equals(user1.getId())){
                                        users.add(user);
                                    }
                                }
                            }else users.add(user);
                        }
                    }
                }

                adapter = new UserAdapter(MenuActivity.this, users, true);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Cerrando sesion", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MenuActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(MenuActivity.this, UsersActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //pedir permisos para escribir y grabar audio
    public void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
    }
}
