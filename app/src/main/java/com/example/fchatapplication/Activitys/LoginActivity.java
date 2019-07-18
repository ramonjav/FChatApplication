package com.example.fchatapplication.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fchatapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText password, email;
    Button aceptar, registrar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password = findViewById(R.id.PasswordLogin);
        email = findViewById(R.id.EmailLogin);
        aceptar = findViewById(R.id.aceptarLogin);
        registrar = findViewById(R.id.registerLogin);

        mAuth = FirebaseAuth.getInstance();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String correo = email.getText().toString();
                if(isValidEmail(correo) && validarcontraseña()){
                    String contrasena = password.getText().toString();

                    mAuth.signInWithEmailAndPassword(correo, contrasena)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(LoginActivity.this,"Bienvenido su majestad", Toast.LENGTH_LONG).show();
                                        next();

                                    } else {

                                        Toast.makeText(LoginActivity.this,"Error, credenciales incorrectas", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });


    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public  boolean validarcontraseña(){
        String contrasena = password.getText().toString();
        if(contrasena.length() >= 6 && contrasena.length()<= 16){
            return true;
        }else return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            next();
        }
    }

    public void next(){

        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
        finish();
    }
}
