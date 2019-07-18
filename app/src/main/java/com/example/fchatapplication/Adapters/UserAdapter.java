package com.example.fchatapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fchatapplication.Activitys.MessageActivity;
import com.example.fchatapplication.Entidades.User;
import com.example.fchatapplication.R;

import java.util.List;

import static com.example.fchatapplication.Utilidades.Contantes.KEY;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    List<User> Users;

    public UserAdapter(Context context, List<User> users){
        this.context = context;
        this.Users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final User user = Users.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra(KEY, user.getId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Users.size();
    }

    //Holder de Usuarios

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name);
            email = itemView.findViewById(R.id.txt_correo);


        }
    }
}
