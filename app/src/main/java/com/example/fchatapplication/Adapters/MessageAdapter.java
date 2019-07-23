package com.example.fchatapplication.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.fchatapplication.Entidades.Chat;
import com.example.fchatapplication.R;
import com.example.fchatapplication.Utilidades.TraerFecha;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static com.example.fchatapplication.Utilidades.Contantes.UBIC;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public final static int LEFT = 1;
    public final static int RIGHT = 0;

    Context context;
    List<Chat> chats;

    FirebaseUser user;
    private FirebaseStorage storage;

    public MessageAdapter(Context context, List<Chat> Chat){
        this.context = context;
        this.chats = Chat;
        storage = FirebaseStorage.getInstance();
    }

    public void actualizarmensaje(int position, Chat chat){
        chats.set(position, chat);
        notifyItemChanged(position);

    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == RIGHT){
            view = LayoutInflater.from(context).inflate(R.layout.item_emisor, parent, false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.item_receptor, parent, false);
        }
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, final int position) {

        final Chat chat = chats.get(position);

        TraerFecha traerFecha = new TraerFecha(chat);

        holder.message.setText(chat.getMessage());
        holder.tiempo.setText(traerFecha.getFecha());

        if(position == chats.size()-1){
            if(chat.isIsseen()){
                holder.visto.setText("Visto");
            }else{
                holder.visto.setText("Enviado");
            }

        }else{
            holder.visto.setVisibility(View.GONE);
        }

        if(chat.isImage()){
            holder.img_mensaje.setVisibility(View.VISIBLE);
            final String fichero = Environment.getExternalStorageDirectory().getAbsolutePath()+UBIC;
            Glide.with(context).asBitmap().load(fichero + chat.getNameFoto()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) { holder.img_mensaje.setImageBitmap(resource); }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    Glide.with(context).asBitmap().load(chat.getUrlFoto()).error(R.drawable.ic_launcher_background).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            saveImage(resource, chat.getNameFoto(), fichero);
                            storage.getReference().child("imagenes_chat").child(chat.getNameFoto()).delete();
                            actualizarmensaje(position, chat);
                            notifyItemInserted(position);
                        }
                    });
                }
            });

            if(holder.message.getText().toString().trim().isEmpty()){
                holder.message.setVisibility(View.GONE);
            }
        }else{
            holder.img_mensaje.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        Chat chat = chats.get(position);
        if(chat.getSender().equals(user.getUid())){
            return RIGHT;
        }else{
            return LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void saveImage(Bitmap bitmap, String fileName, String fichero){

        File file = new File(fichero, fileName);
        if(!file.exists()){
            Log.d("pathImage", file.toString());
            FileOutputStream fos = null;
            try{
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                Log.d("pathImage", file.toString());
            }catch (java.io.IOException e){
                e.printStackTrace();
            }
        }
        //Toast.makeText(c, file.toString(), Toast.LENGTH_SHORT).show();
    }

    //Holder de Usuarios

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView message, tiempo, visto;
        ImageView img_mensaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.mensajeMensaje);
            tiempo = itemView.findViewById(R.id.horaMensaje);
            visto = itemView.findViewById(R.id.txt_visto);
            img_mensaje = itemView.findViewById(R.id.img_mensaje);
        }
    }
}
