package com.tweb.lovrenco.pizzaclick;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;

import java.util.List;

import model.*;
import utilities.GsonRequest;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsername;
    EditText txtPassword;
    Button btnAccedi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        txtUsername=(EditText)findViewById(R.id.txtUsername);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        btnAccedi =(Button)findViewById(R.id.btnAccedi);


        btnAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Gson gson = new Gson();
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                final Utente utente = new Utente();
                utente.setUsername(username);
                utente.setPassword(password);


                Response.Listener<List> listener= new Response.Listener<List>() {
                    @Override
                    public void onResponse(List response) {

                        Utente utente=gson.fromJson((String)response.get(0), Utente.class);
                        ArrayList<Pizza> menu=gson.fromJson((String)response.get(1), ArrayList.class);

                        if(utente.getRuolo().equals("")){
                            Toast.makeText(getApplicationContext(), "Login fallito, riprovare", Toast.LENGTH_LONG).show();
                            txtPassword.setText("");
                            txtUsername.setText("");
                        }else {
                            Toast.makeText(getApplicationContext(), "Benvenuto " + utente.getUsername(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("menu", menu);
                            bundle.putParcelable("utenteLoggato", utente);
                            i.putExtras(bundle);
                            startActivity(i);
                        }
                    }
                };

                Response.ErrorListener errorListener =new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "<errorListener>: "+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String json = gson.toJson(utente);
                String localhost = getString(R.string.localhost);
                //String url ="http://"+localhost+":8084/PIZZACLICK/MobileServlet?cmdMobile=login&utente="+json;
                String url ="http://"+localhost+":8084/PIZZACLICK/Dispatcher?srcMobile=mobile&cmdMobile=login&utente="+json;

                //String url ="http://192.168.1.30:8084/PIZZACLICK/MobileServlet?cmdMobile=login&utente="+json;
                //String url ="http://"+localhost+":8084/PIZZACLICK/MobileServlet?cmdMobile=prova";

                GsonRequest<List> gsonRequest = new GsonRequest<>(
                        url,List.class, null, listener, errorListener
                );
                queue.add(gsonRequest);



            }

        });
    }
}


