package com.example.fchatapplication.Notification;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fchatapplication.Volleys.Volleys;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.fchatapplication.Utilidades.Contantes.BODY;
import static com.example.fchatapplication.Utilidades.Contantes.DATA;
import static com.example.fchatapplication.Utilidades.Contantes.KEYUSER;
import static com.example.fchatapplication.Utilidades.Contantes.NOTIFICATION;
import static com.example.fchatapplication.Utilidades.Contantes.TITLE;
import static com.example.fchatapplication.Utilidades.Contantes.TOKEN;

public class Notification {

    static public void sendNotification(final Context context, String body, String title, String key, String token){
        RequestQueue colaDePeticiones;
        Volleys volleys;

        volleys = Volleys.getInstance(context);
        colaDePeticiones = volleys.getRequestQueue();

//////////////////////Map de notificacion//////////////////////
        Map<String, String> notification = new HashMap<>();
        notification.put(BODY, body);
        notification.put(TITLE, title);
/////////////////////Map de data//////////////////////////////
        Map<String, String> data = new HashMap<>();
        data.put(KEYUSER, key);
        data.put(TOKEN, token);
//////////////////////Map principal/////////////////////////////
        Map<String, Map> parametros = new HashMap<>();
        parametros.put(NOTIFICATION, notification);
        parametros.put(DATA, data);

        JSONObject jsonObject = new JSONObject(parametros);

        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.POST,
                "https://apcpruebas.es/david/notificaciones/enviarNoti.php",
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // array disponible
                        Log.i("Respuesta", "Respuesta del servidor correcta");
                        try {
                            Toast.makeText(context, "Notificación enviada correctamente", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("datos", error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        colaDePeticiones.add(peticion);
    }
}