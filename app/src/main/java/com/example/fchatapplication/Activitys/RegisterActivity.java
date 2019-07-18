package com.example.fchatapplication.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fchatapplication.Entidades.User;
import com.example.fchatapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText password, email, name;
    Button registrar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
   //TODO: private String token = FirebaseInstanceId.getInstance().getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        password = findViewById(R.id.PasswordRegister);
        email = findViewById(R.id.EmailRegister);
        name = findViewById(R.id.NameRegister);
        registrar = findViewById(R.id.registrar);

        mAuth = FirebaseAuth.getInstance();

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String correo = email.getText().toString();
                final String nombre = name.getText().toString();

                if(isValidEmail(correo) && validarcontraseña() && validarnombre(nombre)){
                    String contrasena = password.getText().toString();

                    mAuth.createUserWithEmailAndPassword(correo, contrasena)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(RegisterActivity.this, "Se registto correctamente", Toast.LENGTH_LONG).show();
                                        FirebaseUser currentUser = mAuth.getCurrentUser();

                                        User user = new User();
                                        user.setId(currentUser.getUid());
                                        user.setEmail(correo);
                                        user.setName(nombre);
                                        //TODO: user.setToken(token);

                                        DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
                                        reference.setValue(user);

                                        regresar();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });


    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public  boolean validarcontraseña(){
        String contra = password.getText().toString();
            if(contra.length() >= 6 && contra.length()<= 16){
                return true;
            }else return false;
    }

    public boolean validarnombre(String name){
        return !name.trim().isEmpty();
    }

    public void regresar(){
        startActivity(new Intent(this, LoginActivity.class));
    }

}
