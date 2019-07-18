package com.example.fchatapplication.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fchatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.example.fchatapplication.Utilidades.Contantes.KEY;
import static com.example.fchatapplication.Utilidades.Contantes.NODO_MENSAJES;

public class MessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button send;
    EditText editText;

    String id;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recyclerView = findViewById(R.id.rvMenssage);
        send = findViewById(R.id.btnEnviar);
        editText = findViewById(R.id.txtMensaje);

        Bundle bundle = getIntent().getExtras();

        id = bundle.getString(KEY);

        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

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

    }

    private void send(String sender, String reciver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> mensaje = new HashMap<>();
        mensaje.put("sender", sender);
        mensaje.put("reciver", reciver);
        mensaje.put("message", message);

        reference.child(NODO_MENSAJES).push().setValue(mensaje);

    }
}
