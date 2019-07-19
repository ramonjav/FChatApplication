package com.example.fchatapplication.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fchatapplication.Adapters.MessageAdapter;
import com.example.fchatapplication.Entidades.Chat;
import com.example.fchatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.fchatapplication.Utilidades.Contantes.KEY;
import static com.example.fchatapplication.Utilidades.Contantes.NODO_MENSAJES;
import static com.example.fchatapplication.Utilidades.Contantes.NODO_USUARIOS;

public class MessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button send;
    EditText editText;

    String id;

    FirebaseUser user;
    DatabaseReference reference;

    MessageAdapter adapter;
    List<Chat> chats;

    ValueEventListener seeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recyclerView = findViewById(R.id.rvMenssage);
        send = findViewById(R.id.btnEnviar);
        editText = findViewById(R.id.txtMensaje);

        Bundle bundle = getIntent().getExtras();

        id = bundle.getString(KEY);

        LinearLayoutManager l = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(l);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!editText.getText().toString().trim().isEmpty()){
                    send.setVisibility(View.VISIBLE);
                }else{
                    send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(user.getUid(), id, editText.getText().toString());
                editText.setText("");
            }
        });

        read(user.getUid(), id);
        seeMensaje(id);
    }

    private void seeMensaje(final String userId){
        reference = FirebaseDatabase.getInstance().getReference(NODO_MENSAJES);
        seeListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReciver().equals(user.getUid()) && chat.getSender().equals(userId)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void send(String sender, String reciver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Object createTimeStamp = ServerValue.TIMESTAMP;

        String mGroupId = reference.push().getKey();

        Chat chat = new Chat();
        chat.setId(mGroupId);
        chat.setReciver(reciver);
        chat.setSender(sender);
        chat.setMessage(message);
        chat.setCreateTimeStamp(createTimeStamp);
        chat.setIsseen(false);

        reference.child(NODO_MENSAJES).child(mGroupId).setValue(chat);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chatList")
                .child(user.getUid())
                .child(id);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRef.child("id").setValue(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

   public void read(final String myid, final String userid){
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference(NODO_MENSAJES);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(myid) && chat.getReciver().equals(userid) ||
                    chat.getSender().equals(userid) && chat.getReciver().equals(myid)){
                        chats.add(chat);
                    }
                }

                adapter = new MessageAdapter(MessageActivity.this, chats);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        reference.removeEventListener(seeListener);
        Status(false);
    }
}
