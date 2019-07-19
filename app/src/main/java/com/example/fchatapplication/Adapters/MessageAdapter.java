package com.example.fchatapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fchatapplication.Entidades.Chat;
import com.example.fchatapplication.R;
import com.example.fchatapplication.Utilidades.TraerFecha;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public final static int LEFT = 1;
    public final static int RIGHT = 0;

    Context context;
    List<Chat> chats;

    FirebaseUser user;

    public MessageAdapter(Context context, List<Chat> Chat){
        this.context = context;
        this.chats = Chat;
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
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, int position) {

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

    //Holder de Usuarios

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView message, tiempo, visto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.mensajeMensaje);
            tiempo = itemView.findViewById(R.id.horaMensaje);
            visto = itemView.findViewById(R.id.txt_visto);
        }
    }
}
