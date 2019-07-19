package com.example.fchatapplication.Activitys;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fchatapplication.Entidades.Chat;
import com.example.fchatapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.fchatapplication.Utilidades.Contantes.KEY;
import static com.example.fchatapplication.Utilidades.Contantes.UBIC;
import static com.example.fchatapplication.Utilidades.Contantes.URI;


public class FotoActivity extends AppCompatActivity {

    ImageView imageView;
    EditText text;
    Button btnEnviar;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    Uri uri;
    int isvalided = 0;
    String KEY_RECEPTOR;

    private static String fichero = Environment.getExternalStorageDirectory().getAbsolutePath()+UBIC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        imageView = findViewById(R.id.imageView2);
        btnEnviar = findViewById(R.id.btnEnviar);
        text = findViewById(R.id.txtMensaje);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            uri = Uri.parse(bundle.getString(URI));
            imageView.setImageURI(uri);
            KEY_RECEPTOR = bundle.getString(KEY);
        }else{finish();}

        storage = FirebaseStorage.getInstance();

        final File file = new File(fichero);

        if(!file.exists()){
            file.mkdirs();
        }

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isvalided++;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = "Image_" + timeStamp + ".jpg";
                subirfoto(isvalided, fileName);
                finish();
               saveImage(((BitmapDrawable)imageView.getDrawable()).getBitmap(), fileName);
               // storage.getReference().child("imagenes_chat").child("40").delete();
            }
        });
    }

    public void subirfoto(int c, final String name){
        if(c==1){
            storageReference = storage.getReference("imagenes_chat");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(name);
            fotoReferencia.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    String url = task.getResult().toString();
                    EnviarMensaje(url, name);
                    finish();
                }
            });
        }
    }

    public void EnviarMensaje(String url, String name){
        if(!url.isEmpty()){
            Chat chat = new Chat();

            text.setText("");
        }
    }

    public void saveImage(Bitmap bitmap, String fileName){

        File file = new File(fichero, fileName);
        if(!file.exists()){
            Log.d("pathImage", file.toString());
            FileOutputStream fos = null;
            try{
               //file.mkdirs();
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                Log.d("pathImage", file.toString());
            }catch (java.io.IOException e){
                e.printStackTrace();
            }
        }
        //Toast.makeText(FotoActivity.this, file.toString(), Toast.LENGTH_SHORT).show();
    }

}
