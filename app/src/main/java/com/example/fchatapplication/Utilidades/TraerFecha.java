package com.example.fchatapplication.Utilidades;

import com.example.fchatapplication.Entidades.Chat;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

public class TraerFecha {

    Chat chat;

    public TraerFecha(Chat chat) {
        this.chat = chat;
    }

    public String getFecha(){
        long time = (long)chat.getCreateTimeStamp();
        Date d = new Date(time);
        PrettyTime pretty = new PrettyTime(new Date(), Locale.getDefault());
        return pretty.format(d);
    }
}
